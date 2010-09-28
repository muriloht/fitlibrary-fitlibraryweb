/*
 * Copyright (c) 2010 Rick Mugridge, www.RimuResearch.com
 * Released under the terms of the GNU General Public License version 2 or later.
*/

package fitlibrary.mockWebServices;

import java.io.IOException;

import org.apache.http.protocol.HttpRequestHandlerRegistry;
import org.apache.log4j.Logger;

import fitlibrary.log.FixturingLogger;
import fitlibrary.mockWebServices.logger.MockLogger;
import fitlibrary.mockWebServices.term.OrTerm;
import fitlibrary.mockWebServices.term.Term;
import fitlibrary.server.HttpServer;

public class MockingServer extends HttpServer {
	static Logger logger = FixturingLogger.getLogger(MockingServer.class);
	protected final OrTerm term = new OrTerm();
	private MockLogger logging;

	public MockingServer(int portNo, MockLogger logging) throws IOException {
		super(portNo,"mock");
		this.logging = logging;
		start();
		logger.trace("Started on "+portNo);
	}
	@Override
	protected void register(HttpRequestHandlerRegistry reqistry) {
		logger.trace("Registered");
		reqistry.register("*", new MockWebServiceHandler(portNo,term,logging));
	}
	public void or(Term term2, boolean insertAtEnd) {
		term.add(term2,insertAtEnd);
	}
	public Term getTerm() {
		return term;
	}
	@Override
	public void stop() throws IOException {
		super.stop();
		term.logUnused(portNo, logging);
	}
}
