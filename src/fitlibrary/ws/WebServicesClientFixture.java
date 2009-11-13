/*
 * Copyright (c) 2009 Rick Mugridge, www.RimuResearch.com
 * Kindly donated by Air New Zealand in October 2009.
 * Released under the terms of the GNU General Public License version 2 or later.
*/
package fitlibrary.ws;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;

import fitlibrary.exception.FitLibraryException;
import fitlibrary.mockWebServices.logger.ClockedLogger;
import fitlibrary.ws.message.ContentType;
import fitlibrary.ws.message.PostMessage;
import fitlibrary.ws.message.ReplyMessage;
import fitlibrary.ws.server.RealWebService;
import fitlibrary.xml.XmlDoFixture;

public class WebServicesClientFixture extends XmlDoFixture {
	private String lastResponse = "";
	private String proxyHost = "";
	private int proxyPortNo = 0;

	public boolean proxyUrlWithPort(String proxy, int portNo) {
		this.proxyHost = proxy;
		this.proxyPortNo = portNo;
		return true;
	}
	public String getHttp(String url) {
		return getHttp(url, 0);
	}
	public String postTextWith(String url, String s) {
		try {
			return postingTextWith(url, s);
		} catch (Exception e) {
			throw new FitLibraryException(e.getMessage());
		}
	}
	public String postWith(String url, String xmlOut) {
		try {
			return postingSoapWith(url, xmlOut);
		} catch (Exception e) {
			throw new FitLibraryException(e.getMessage());
		}
	}
	public String postingTextWith(String url, String xmlOut) {
		return postHttp(url, xmlOut, ContentType.PLAIN,0);
	}
	public String postingTextWithAndTimeOut(String url, String xmlOut, int timeoutInMilliseconds) {
		return postHttp(url, xmlOut, ContentType.PLAIN,timeoutInMilliseconds);
	}
	public String postingSoapWith(String url, String xmlOut) {
		return postHttp(url, xmlOut, ContentType.XML,0);
	}
	public String postTextFromFile(String url, String fileName) {
		return postFromFile(url,fileName,ContentType.PLAIN);
	}
	public String postSoapFromFile(String url, String fileName) {
		return postFromFile(url,fileName,ContentType.XML);
	}
	public String postFromFile(String url, String fileName, ContentType contentType) {
		try {
			return postHttp(url,readFile(fileName),contentType,0);
		} catch (Exception e) {
			throw new FitLibraryException(e.getMessage());
		}
	}
	private String getHttp(String url, int timeoutInMilliseconds) {
		try {
			RealWebService realWebService = new RealWebService(url,new ClockedLogger());
			if (timeoutInMilliseconds > 0)
				realWebService.setSocketTimeout(timeoutInMilliseconds);
			if (!"".equals(proxyHost))
				realWebService.setProxy(proxyHost, proxyPortNo);
			PostMessage postMessage = new PostMessage(url,"",ContentType.PLAIN);
			ReplyMessage reply = realWebService.get(postMessage);
			if (!reply.isOK())
				throw new FitLibraryException("Error");
			if (reply.getResultCode() != 200)
				throw new FitLibraryException("Error: "+reply.getResultCode()+": "+reply.getContents());
			return reply.getContents();
		} catch (Exception e) {
			throw new FitLibraryException(e.getMessage());
		}
	}
	private String readFile(String fileName) throws IOException {
		InputStream inputStream = new FileInputStream(new File(fileName));
		try {
			return IOUtils.toString(inputStream);
		} finally {
			inputStream.close();
		}
	}
	private String postHttp(String url, String contents, ContentType contentType, int timeoutInMilliseconds) {
		try {
			RealWebService realWebService = new RealWebService(url,new ClockedLogger());
			if (timeoutInMilliseconds > 0)
				realWebService.setSocketTimeout(timeoutInMilliseconds);
			if (!"".equals(proxyHost))
				realWebService.setProxy(proxyHost, proxyPortNo);
			PostMessage postMessage = new PostMessage(url,contents,contentType);
			ReplyMessage reply = realWebService.post(postMessage);
			if (!reply.isOK())
				throw new FitLibraryException("Error");
			if (reply.getResultCode() != 200)
				throw new FitLibraryException("Error: "+reply.getResultCode()+": "+reply.getContents());
			return reply.getContents();
		} catch (Exception e) {
			throw new FitLibraryException(e.getMessage());
		}
	}
	public String xpathInResponse(String xPathExpression) {
		return xpathIn(xPathExpression, lastResponse);
	}
	public boolean xpathExistsInResponse(String xPathExpression) {
		return xpathExistsIn(xPathExpression,lastResponse);
	}
	public boolean xmlInResponseSameAs(String expectedXml) {
		return xmlSameAs(lastResponse, expectedXml);
	}
}
