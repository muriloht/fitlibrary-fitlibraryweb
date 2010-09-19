/*
 * Copyright (c) 2010 Rick Mugridge, www.RimuResearch.com
 * Released under the terms of the GNU General Public License version 2 or later.
*/

package fitlibrary.server;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.Locale;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.RequestLine;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentProducer;
import org.apache.http.entity.EntityTemplate;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.HttpRequestHandler;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import fitlibrary.log.FixturingLogger;

public class RecordingProxyHandler implements HttpRequestHandler {
	static Logger logger = FixturingLogger.getLogger(RecordingProxyHandler.class);
	protected HttpClient client = new DefaultHttpClient();
	private UriMapper uriMapper;
	private Recorder recorder;

	public RecordingProxyHandler(UriMapper uriMapper, Recorder recorder) {
		this.uriMapper = uriMapper;
		this.recorder = recorder;
	}
	@Override
	public void handle(HttpRequest request, HttpResponse response, HttpContext context)
			throws IOException {
		try {
			String uri = uriMapper.map(request.getRequestLine().getUri());
			logger.trace("uri: "+uri);
			logger.trace("Request: "+request.getRequestLine());
			String host = request.getFirstHeader("Host").getValue();
			
			switch(decodeMethod(request.getRequestLine())) {
			case GET:
				httpGet(response, uri, host);
				return;
			case HEAD:
				httpHead(response, uri, host);
				return;
			case POST:
				httpPost(request,response, uri, host);
				return;
			case PUT:
			case DELETE:
			case INVALID:
				error(response,"Not yet implemented");
			}
		} catch (IOException e) {
			logger.error(e.getMessage());
			throw e;
		}
	}
	private void httpHead(HttpResponse response, String uri, String host) throws IOException {
		HttpHead head = new HttpHead(uri);
		head.setHeader("Host",host);
		HttpResponse reply = client.execute(head);
		if (!ok(reply.getStatusLine(),response))
			return;
		traceHeaders("Reply", reply.getAllHeaders());
		response.addHeader(reply.getFirstHeader("Content-Type"));
		response.addHeader("Passed-Thru-Proxy","true");
		response.setStatusCode(HttpStatus.SC_OK);
	}
	private void httpGet(HttpResponse response, String uri, String host) throws IOException {
		HttpGet get = new HttpGet(uri);
		get.setHeader("Host",host);
		HttpResponse reply = client.execute(get);
		if (!ok(reply.getStatusLine(),response))
			return;
		traceHeaders("Reply", reply.getAllHeaders());
		HttpEntity entity = reply.getEntity();
		response.setEntity(entity);
		response.addHeader(reply.getFirstHeader("Content-Type"));
		response.addHeader("Passed-Thru-Proxy","true");
		response.setStatusCode(HttpStatus.SC_OK);
	}
	private void httpPost(HttpRequest request, HttpResponse response, String uri, String host)
			throws IOException {
		traceHeaders("Request",request.getAllHeaders());
		HttpPost post = new HttpPost(uri);
		post.setHeader("Host",host);
		Header soapHeader = request.getFirstHeader("SOAPAction");
		if (soapHeader != null)
			post.setHeader("SOAPAction",soapHeader.getValue());
		HttpEntity requestEntity = ((HttpEntityEnclosingRequest) request).getEntity();
		String requestContents = EntityUtils.toString(requestEntity);
		StringEntity outEntity = copyOfEntity("Request", requestEntity,requestContents);
		post.setEntity(outEntity);
		logger.trace("POST uri: "+post.getURI());
		
		HttpResponse reply = client.execute(post);
		if (!ok(reply.getStatusLine(),response))
			return;
		traceHeaders("Reply", reply.getAllHeaders());

		String responseContents = gatherResponseContentsAndPassOn(response,reply);
		recorder.record(uri,requestContents,responseContents);
		response.addHeader("Passed-Thru-Proxy","true");
		response.setStatusCode(HttpStatus.SC_OK);
	}
	private String gatherResponseContentsAndPassOn(HttpResponse response,
			HttpResponse reply) throws IOException,
			UnsupportedEncodingException {
		HttpEntity responseEntity = reply.getEntity();
		if (responseEntity != null) {
			String responseContents = EntityUtils.toString(responseEntity);
			logger.trace("Response contents: "+responseContents);
			StringEntity replyEntity = copyOfEntity("Response", responseEntity,responseContents);
			response.setEntity(replyEntity);
			return responseContents;
		}
		response.setEntity(reply.getEntity());
		return "";
	}
	private StringEntity copyOfEntity(String direction, HttpEntity entity, String contents) throws UnsupportedEncodingException {
		logger.trace(direction+" Contents: "+contents);
		StringEntity replyEntity = new StringEntity(contents);
		replyEntity.setContentType(entity.getContentType());
		replyEntity.setContentEncoding(entity.getContentEncoding());
		return replyEntity;
	}
	private void traceHeaders(String direction, Header[] allHeaders) {
		for (Header header : allHeaders)
			logger.trace(direction+" Header: "+header.getName()+": "+header.getValue());
	}
	private boolean ok(StatusLine statusLine, HttpResponse response) {
		if (statusLine.getStatusCode() == 200)
			return true;
		error(response,"Received Status code = "+statusLine.getStatusCode()+": "+statusLine.getReasonPhrase());
		return false;
	}
	private void error(HttpResponse response, final String error) {
		response.setStatusCode(HttpStatus.SC_FORBIDDEN);
		EntityTemplate body = makeBody(error,"text/html; charset=UTF-8");
		response.setEntity(body);
		logger.error(error);
	}
	private EntityTemplate makeBody(final String contents, String contentType) {
		EntityTemplate body = new EntityTemplate(new ContentProducer() {
			@Override
			public void writeTo(final OutputStream outstream) throws IOException {
				OutputStreamWriter writer = new OutputStreamWriter(outstream, "UTF-8");
                writer.write("<html><body>");
                writer.write("<h1>");
                writer.write(contents);
                writer.write("</h1></body></html>");
                writer.flush();
			}
		});
		body.setContentType(contentType);
		return body;
	}
	private MethodType decodeMethod(RequestLine requestLine) {
		String method = requestLine.getMethod().toUpperCase(Locale.ENGLISH);
		if (method.equals("HEAD"))
			return MethodType.HEAD;
		if (method.equals("GET"))
			return MethodType.GET;
		if (method.equals("POST"))
			return MethodType.POST;
		if (method.equals("PUT"))
			return MethodType.PUT;
		if (method.equals("DELETE"))
			return MethodType.DELETE;
		return MethodType.INVALID;
	}
	enum MethodType {HEAD,GET,POST,PUT,DELETE,INVALID}

}
