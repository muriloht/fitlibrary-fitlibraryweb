/*
 * Copyright (c) 2009 Rick Mugridge, www.RimuResearch.com
 * Kindly donated by Air New Zealand in October 2009.
 * Released under the terms of the GNU General Public License version 2 or later.
*/
package fitlibrary.mockWebServices;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.Executors;

import fitlibrary.mockWebServices.responder.Responder;
import fitlibrary.mockWebServices.term.OrTerm;
import fitlibrary.mockWebServices.term.Term;
import fitlibrary.ws.logger.Logger;
import fitlibrary.ws.message.Message;
import fitlibrary.ws.server.AbstractHttpServer;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

public class MockingServer extends AbstractHttpServer {
	protected final OrTerm term = new OrTerm();
	protected final int portNo;
	protected HttpServer server;

	public MockingServer(int portNo, Logger logger) throws IOException {
		super(logger);
		this.portNo = portNo;
		this.server = HttpServer.create(new InetSocketAddress(portNo),0);
		server.createContext("/", new MyHandler());
		server.setExecutor(Executors.newSingleThreadExecutor());
		server.start();
	}
	public void or(Term term2, boolean insertAtEnd) {
		term.add(term2,insertAtEnd);
	}
	public class MyHandler extends AbstractHttpHandler {
		public void handle(HttpExchange exchange) throws IOException {
			try {
				Message request = handleRequest(exchange);
				Responder responder = term.matchRequest(request);
				logger().responded("Port "+portNo,request,responder,portNo);
				handleResponse(exchange,responder);
			} catch (Exception e) {
				e.printStackTrace();
				throw new IOException(e);
			}
		}
		private void handleResponse(HttpExchange exchange, Responder responder) throws IOException {
			Headers responseHeaders = exchange.getResponseHeaders();
			Map<String,String> header = responder.getHeaders();
			for (String key : header.keySet()) {
				responseHeaders.set(key, header.get(key));
			}
			exchange.sendResponseHeaders(responder.getResultCode(), responder.getContents().length());
			OutputStream os = exchange.getResponseBody();
			os.write(responder.getContents().getBytes());
			os.close();
		}
	}
	public void close() {
		server.stop(0);
		term.logUnused(portNo,logger);
	}
	public Term getTerm() {
		return term;
	}	
}
