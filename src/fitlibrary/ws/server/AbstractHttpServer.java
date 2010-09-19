/*
 * Copyright (c) 2009 Rick Mugridge, www.RimuResearch.com
 * Kindly donated by Air New Zealand in October 2009.
 * Released under the terms of the GNU General Public License version 2 or later.
*/
package fitlibrary.ws.server;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.IOUtils;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import fitlibrary.ws.logger.Logger;
import fitlibrary.ws.message.ContentType;
import fitlibrary.ws.message.PostMessage;

public abstract class AbstractHttpServer {
	protected final int FIRST_PART = 1000;
	protected Logger logger;

	public AbstractHttpServer(Logger logger) {
		this.logger = logger;
	}
	public abstract class AbstractHttpHandler implements HttpHandler {
		protected PostMessage handleRequest(HttpExchange exchange) throws IOException {
			URI requestURI = exchange.getRequestURI();
			logger().log("Received response code = "+exchange.getResponseCode());
			logger().log("Received uri = "+requestURI);
			String methodAndUri = exchange.getRequestMethod()+" "+requestURI;
			logger().log("Received request method: " + methodAndUri);
			InputStream is = exchange.getRequestBody();
			String contents = IOUtils.toString(is,"utf-8");
			is.close();
			logger().log("Received: " + trimmed(contents));
			Map<String, String> headerMap = getHeaderMap(exchange);
			return new PostMessage(exchange.getResponseCode(),requestURI.toString(),contents,ContentType.SOAP12);
		}
		private String trimmed(String contents) {
			if (contents.length() < FIRST_PART+3)
				return contents;
			return contents.substring(0,FIRST_PART)+"...";
		}
		private Map<String, String> getHeaderMap(HttpExchange exchange) {
			Headers requestHeaders = exchange.getRequestHeaders();
			for (String s :requestHeaders.keySet())
				logger().log("Received header: "+s+" : "+requestHeaders.getFirst(s));

			Map<String, String> map = new HashMap<String, String>();
			for (String s :requestHeaders.keySet())
				map.put(s,requestHeaders.getFirst(s));
			return map;
		}
		protected Logger logger() {
			return logger;
		}
	}
}
