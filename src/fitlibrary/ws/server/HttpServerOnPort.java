/*
 * Copyright (c) 2009 Rick Mugridge, www.RimuResearch.com
 * Kindly donated by Air New Zealand in October 2009.
 * Released under the terms of the GNU General Public License version 2 or later.
*/
package fitlibrary.ws.server;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import fitlibrary.ws.logger.IdentifyingLogger;
import fitlibrary.ws.logger.Logger;
import fitlibrary.ws.message.PostMessage;
import fitlibrary.ws.message.ReplyMessage;

public class HttpServerOnPort extends AbstractHttpServer {
	protected final WebService webService;
	protected final int portNo;

	public HttpServerOnPort(int portNo, WebService webService, Logger logger) throws IOException {
		super(new IdentifyingLogger(logger,"server@"+portNo));
		this.portNo = portNo;
		this.webService = webService;
		HttpServer server = HttpServer.create(new InetSocketAddress(portNo),0);
		server.createContext("/", new MyHandler());
		server.setExecutor(null); // use default executor
		server.start();
	}
	public class MyHandler extends AbstractHttpHandler {
		@Override
		public void handle(HttpExchange exchange) throws IOException {
			PostMessage request = handleRequest(exchange);
//			ReplyMessage response = webService.post(request);
//			logger.responded("", request, response,portNo);
//			handleResponse(exchange, response);
		}
		private void handleResponse(HttpExchange exchange, ReplyMessage response) throws IOException {
			Headers responseHeaders = exchange.getResponseHeaders();
//			Map<String, String> headerMap = response.getHeaderMap();
//			for (String key : headerMap.keySet()) {
//				if (!ignoreHeader(key)) {
//					String value = headerMap.get(key);
//					responseHeaders.set(key,value);
//					logger.log("Added header into reply: "+key+" : "+value);
//				}
//			}
			exchange.sendResponseHeaders(response.getResultCode(), response.getContents().length());
			OutputStream os = exchange.getResponseBody();
			os.write(response.getContents().getBytes());
			os.close();
		}
		private boolean ignoreHeader(String key) {
			return key.equals("Transfer-Encoding");
		}
	}
}
