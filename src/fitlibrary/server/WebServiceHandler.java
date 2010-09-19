/*
 * Copyright (c) 2010 Rick Mugridge, www.RimuResearch.com
 * Released under the terms of the GNU General Public License version 2 or later.
*/

package fitlibrary.server;

import java.io.IOException;
import java.util.Locale;

import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.MethodNotSupportedException;
import org.apache.http.entity.EntityTemplate;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import fitlibrary.log.FixturingLogger;
import fitlibrary.ws.soap.Soap;

public class WebServiceHandler extends AbstractHttpRequestHandler {
	static Logger logger2 = FixturingLogger.getLogger(WebServiceHandler.class);

	@Override
	public void handle(HttpRequest request, HttpResponse response, HttpContext context)
							throws HttpException, IOException {
		String method = request.getRequestLine().getMethod().toUpperCase(Locale.ENGLISH);
		if (!method.equals("POST")) {
			throw new MethodNotSupportedException(method
					+ " method not supported: only POST supported");
		}
		if (!(request instanceof HttpEntityEnclosingRequest)) {
			error(response,HttpStatus.SC_FORBIDDEN, "No text/xml provided");
			return;
		}
		HttpEntity entity = ((HttpEntityEnclosingRequest) request).getEntity();
		String contentType = entity.getContentType().getValue();
		logger2.trace("Content type: "+contentType);
		switch (Soap.decodedType(contentType)) {
		case PLAIN:
			callPlainPost(response, entity);
			break;
		case SOAP11:
			callSoap(response,entity,Soap.HEADER11,Soap.TRAILER11);
			break;
		case SOAP12:
			callSoap(response,entity,Soap.HEADER12,Soap.TRAILER12);
			break;
		case INVALID:
			error(response,HttpStatus.SC_FORBIDDEN, "Request format is invalid: "+contentType);
			break;
		}
	}
	private void callSoap(HttpResponse response, HttpEntity entity, String header, String trailer) throws IOException {
		String entityContent = EntityUtils.toString(entity);
		logger2.trace("Incoming entity content: " + entityContent);
		String countTag = "<Count>";
		int start = entityContent.indexOf(countTag);
		if (start < 0) {
			error(response,HttpStatus.SC_FORBIDDEN, "Missing parameter: Count");
			return;
		}
		int end = entityContent.indexOf("</Count>",start);
		if (end < 0) {
			error(response,HttpStatus.SC_FORBIDDEN, "Missing parameter: Count");
			return;
		}
		String countString = entityContent.substring(start+countTag.length(),end);
		try {
			int count = Integer.parseInt(countString);
			EntityTemplate body = makeBody(
				header + "<CountResult>"+(count+1)+"</CountResult>\n" + trailer,
				"text/xml; charset=UTF-8");
			response.setEntity(body);
			response.setStatusCode(HttpStatus.SC_OK);
		} catch (NumberFormatException e) {
			error(response,HttpStatus.SC_FORBIDDEN, "Bad number format: "+countString);
		}
	}
	private void callPlainPost(HttpResponse response, HttpEntity entity) throws IOException {
		String entityContent = EntityUtils.toString(entity);
		logger2.trace("Incoming entity content: " + entityContent);
		String[] split = entityContent.split("=");
		if (split.length != 2) {
			error(response,HttpStatus.SC_FORBIDDEN, "Missing parameter: Count");
			return;
		}
		try {
			int count = Integer.parseInt(split[1]);
			EntityTemplate body = makeBody("<?xml version='1.0' encoding='utf-8'?>\n"+
					"<string xmlns='http://tempuri.org'>"+(count+1)+"</string>\n",
			"text/xml; charset=UTF-8");
			response.setEntity(body);
			response.setStatusCode(HttpStatus.SC_OK);
		} catch (NumberFormatException e) {
			error(response,HttpStatus.SC_FORBIDDEN, "Bad number format: "+split[1]);
		}
	}
}
