/*
 * Copyright (c) 2009 Rick Mugridge, www.RimuResearch.com
 * Kindly donated by Air New Zealand in October 2009.
 * Released under the terms of the GNU General Public License version 2 or later.
*/
package fitlibrary.ws.server;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.URI;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.io.IOUtils;

import fitlibrary.ws.logger.IdentifyingLogger;
import fitlibrary.ws.logger.Logger;
import fitlibrary.ws.message.ContentType;
import fitlibrary.ws.message.PostMessage;
import fitlibrary.ws.message.ReplyMessage;

public class RealWebService {
	HttpClient client = new HttpClient();
	private final String url;
	private final Logger logger;

	public RealWebService(String url, Logger logger) {
		this.url = url;
		this.logger = new IdentifyingLogger(logger,"ws@"+url);
	}
	public void setProxy(String proxyHost, int proxyPortNo) {
		client.getHostConfiguration().setProxy(proxyHost, proxyPortNo);
	}
	public ReplyMessage get(PostMessage message) throws HttpException, IOException {
		GetMethod get = new GetMethod(url+message.getContents()); // May need to escape this
		try {
			getMessage(get);
			return receiveReply(get);
		} finally {
			get.releaseConnection();
			logger.log("Getting finished");
		}
	}
	private void getMessage(GetMethod get) throws HttpException, IOException {
		logger.log("Try getting...");
		get.setRequestHeader("Host","Storytest");
		get.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler(3, false));
		client.executeMethod(get);
	}
	public ReplyMessage post(PostMessage message) throws HttpException, IOException {
		PostMethod post = new PostMethod(url);
		try {
			postMessage(post, message);
			return receiveReply(post);
		} finally {
			post.releaseConnection();
			logger.log("Posting finished");
		}
	}
	public void setSocketTimeout(int timeoutInMilliseconds) {
		client.getParams().setSoTimeout(timeoutInMilliseconds);
	}
	private void postMessage(PostMethod post, PostMessage message) throws HttpException, IOException {
		logger.log("Try posting...");
		setRequestEntity(post, message);
		post.setURI(new URI(message.getUri(),false));
		post.setRequestHeader("Host","Storytest");
		post.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler(3, false));
		client.executeMethod(post);
	}
	private void setRequestEntity(PostMethod post, PostMessage message) throws UnsupportedEncodingException {
		ContentType contentType = message.getContentType();
		RequestEntity requestEntity = new StringRequestEntity(message.getContents(),contentType.getType(),contentType.getCharset());
		post.setRequestEntity(requestEntity);
	}
	private ReplyMessage receiveReply(HttpMethod httpMethod) throws IOException {
		logger.log("Try receiving...");
		String contents = IOUtils.toString(httpMethod.getResponseBodyAsStream());
		String trimmed = trimmed(contents);
		logger.log("Received: " + trimmed);
		logger.log("Status code of the post was: " + httpMethod.getStatusCode());
		return new ReplyMessage(httpMethod.getStatusCode(),getHeaderMap(httpMethod),contents);
	}
	private String trimmed(String contents) {
		if (contents.length() < 23)
			return contents;
		return contents.substring(0,20)+"...";
	}
	private Map<String, String> getHeaderMap(HttpMethod httpMethod) {
		Map<String, String> map = new HashMap<String, String>();
		Header[] responseHeaders = httpMethod.getResponseHeaders();
		for (Header h : responseHeaders) {
			logger.log("Receiver header: "+h.getName()+" : "+h.getValue());
			map.put(h.getName(),h.getValue());
		}
		return map;
	}
}
