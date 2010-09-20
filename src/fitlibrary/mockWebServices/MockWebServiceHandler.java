/*
 * Copyright (c) 2010 Rick Mugridge, www.RimuResearch.com
 * Released under the terms of the GNU General Public License version 2 or later.
*/

package fitlibrary.mockWebServices;

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
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import fitlibrary.log.FixturingLogger;
import fitlibrary.mockWebServices.logger.MockLogger;
import fitlibrary.mockWebServices.responder.Responder;
import fitlibrary.mockWebServices.term.OrTerm;
import fitlibrary.server.AbstractHttpRequestHandler;
import fitlibrary.ws.message.Message;
import fitlibrary.ws.message.PostMessage;
import fitlibrary.ws.soap.Soap;

public class MockWebServiceHandler extends AbstractHttpRequestHandler {
	private static Logger logger = FixturingLogger.getLogger(MockWebServiceHandler.class);
	private final OrTerm term;
	private MockLogger logging;
	private int portNo;

	public MockWebServiceHandler(int portNo, OrTerm term, MockLogger logging) {
		this.term = term;
		this.logging = logging;
		this.portNo = portNo;
		Logger.getRootLogger().setLevel(Level.ALL);
	}
	@Override
	public void handle(HttpRequest request, HttpResponse response, HttpContext context)
			throws HttpException, IOException {
		logger.debug("handle()");
		String method = request.getRequestLine().getMethod().toUpperCase(Locale.ENGLISH);
		if (!method.equals("POST")) {
			throw new MethodNotSupportedException(method
					+ " method not supported: only POST supported");
		}
		if (!(request instanceof HttpEntityEnclosingRequest)) {
			error(response,HttpStatus.SC_FORBIDDEN, "No text/xml provided");
			return;
		}
		String uri = request.getRequestLine().getUri();
		logger.trace("uri: "+uri);
		HttpEntity entity = ((HttpEntityEnclosingRequest) request).getEntity();
		String contentType = entity.getContentType().getValue();
		logger.trace("Content type: "+contentType);
		call(uri,response, entity);
	}
	private void call(String uri, HttpResponse response, HttpEntity entity) throws IOException {
		String requestContent = EntityUtils.toString(entity);
		logger.trace("Incoming content: " + requestContent);
		Message request = new PostMessage(uri,requestContent,Soap.decodedType(entity.getContentType().getValue()));
		Responder responder = term.matchRequest(request);
		logging.responded("Port "+portNo,request,responder,portNo);
		logger.trace("Responder result code: " + responder.getResultCode());
		logger.trace("Responder Content-Type: " + responder.getContentType());
		logger.trace("Responder: " + responder.getContents());
		EntityTemplate body = makeBody(responder.getContents(),responder.getContentType());
		response.setEntity(body);
		response.setStatusCode(responder.getResultCode());
	}
}
