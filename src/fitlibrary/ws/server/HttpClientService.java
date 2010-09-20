/*
 * Copyright (c) 2010 Rick Mugridge, www.RimuResearch.com
 * Released under the terms of the GNU General Public License version 2 or later.
*/

package fitlibrary.ws.server;

import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.AbstractHttpMessage;
import org.apache.log4j.Logger;

import fit.Fixture;
import fitlibrary.SubsetFixture;
import fitlibrary.exception.FitLibraryException;
import fitlibrary.log.FixturingLogger;
import fitlibrary.xml.XmlDoFixture;

public class HttpClientService extends XmlDoFixture {
	static Logger logger = FixturingLogger.getLogger(HttpClientService.class);
	protected HttpClient client = new DefaultHttpClient();
	private Header[] headers = new Header[0];
	protected String reply = "";
	protected String replyContentType = "";

	public boolean proxyUrlWithPort(String proxyHost, int proxyPortNo) {
		HttpHost proxy = new HttpHost(proxyHost, proxyPortNo, "http");
		client.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);
		return true;
	}
	public void timeout(long socketTimeout) {
		client.getParams().setParameter("http.socket.timeout", socketTimeout);
	}
	public void httpHead(String url) {
		logger.trace("Try HEAD "+url);
		HttpHead head = new HttpHead(url);
		setHost(head, url);
		try {
			handleHeaders(client.execute(head));
		} catch (IOException e) {
			head.abort();
			throw new FitLibraryException(e.getMessage());
		} catch (FitLibraryException e) {
			head.abort();
			throw e;
		} finally {
			logger.trace("HEAD finished");
		}
	}
	public void httpGet(String url) {
		logger.trace("Try GET "+url);
		HttpGet get = new HttpGet(url);
		setHost(get, url);
		try {
			handleReply(client.execute(get));
		} catch (IOException e) {
			get.abort();
			throw new FitLibraryException(e.getMessage());
		} catch (FitLibraryException e) {
			get.abort();
			throw e;
		} finally {
			logger.trace("GET finished");
		}
	}
	public void httpPost(String url, String contents, String contentType, String contentEncoding) {
		logger.trace("Start POST "+url+" with content-type "+contentType+" "+contentEncoding);
		logger.trace("POST "+contents);
		HttpPost post = new HttpPost(url);
		setHost(post, url);
		addExtraHeadersToPost(post);
		try {
			StringEntity entity = new StringEntity(contents);
			entity.setContentType(contentType);
			entity.setContentEncoding(contentEncoding);
			post.setEntity(entity);
			handleReply(client.execute(post));
		} catch (IOException e) {
			logger.trace("POST failed");
			post.abort();
			throw new FitLibraryException("Problem: "+e.getMessage());
		} catch (FitLibraryException e) {
			logger.trace("POST failed");
			post.abort();
			throw e;
		} finally {
			logger.trace("POST finished");
		}
	}
	private void setHost(AbstractHttpMessage msg, String url) {
		logger.debug("Host url = '"+url+"'");
		int start = url.indexOf("//")+2;
		int end = url.indexOf("/",start);
		if (end < 0)
			end = url.length();
		msg.setHeader("Host",url.substring(start,end));
	}
	protected void addExtraHeadersToPost(@SuppressWarnings("unused") HttpPost post) {
		// default: do nothing
	}
	public Header[] getHeaders() {
		return headers;
	}
	public SubsetFixture headersInclude() {
		return new SubsetFixture(getHeaders());
	}
	public String getReply() {
		return reply;
	}
	public String getReplyContentType() {
		return replyContentType;
	}
	public String getReplyEscaped() {
		return Fixture.escape(reply).replaceAll("\n", "<br/>");
	}
	private void handleReply(HttpResponse response) throws IOException {
		// Log everything in the reply here...
		handleHeaders(response);
		Header contentTypeHeader = response.getFirstHeader("Content-type");
		HttpEntity entity = response.getEntity();
		if (entity == null || contentTypeHeader == null) {
			logger.error("No data returned in response");
			replyContentType = "failed";
			throw new FitLibraryException("No data returned");
		}
		replyContentType = contentTypeHeader.getValue();
		if (canTreatAsText(replyContentType)) {
			reply = IOUtils.toString(entity.getContent());
			logger.trace("Reply of "+contentTypeHeader.getName()+": "+replyContentType);
			logger.trace("Reply: "+reply);
		} else
			throw new FitLibraryException("The data is not text, it has Content-type: "+replyContentType);
		entity.consumeContent();
	}
	private void handleHeaders(HttpResponse response) {
		headers = response.getAllHeaders();
		StatusLine statusLine = response.getStatusLine();
		if (statusLine.getStatusCode() != 200)
			throw new FitLibraryException("Received Status code = "+statusLine.getStatusCode()+": "+statusLine.getReasonPhrase());
	}
	private boolean canTreatAsText(String value) {
		return value.contains("text/plain") || value.contains("text/html")
			|| value.contains("text/xml") || value.contains("application/soap+xml");
	}
	public String xpathInResponse(String xPathExpression) {
		return xpathIn(xPathExpression, reply);
	}
	public boolean xpathExistsInResponse(String xPathExpression) {
		return xpathExistsIn(xPathExpression,reply);
	}
	public boolean xmlInResponseSameAs(String expectedXml) {
		return xmlSameAs(reply, expectedXml);
	}
}
