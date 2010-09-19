/*
 * Copyright (c) 2009 Rick Mugridge, www.RimuResearch.com
 * Kindly donated by Air New Zealand in October 2009.
 * Released under the terms of the GNU General Public License version 2 or later.
*/
package fitlibrary.mockWebServices.term;

import java.io.IOException;

import fitlibrary.mockWebServices.requestMatcher.RequestMatcher;
import fitlibrary.mockWebServices.responder.ErrorResponder;
import fitlibrary.mockWebServices.responder.Responder;
import fitlibrary.ws.logger.Logger;
import fitlibrary.ws.message.HttpMessage;

public class LeafTerm extends AbstractTerm {
	private boolean available = true;
	private final RequestMatcher requestMatcher;
	private final Responder responder;

	public LeafTerm(RequestMatcher requestMatcher, Responder responder) {
		this.requestMatcher = requestMatcher;
		this.responder = responder;
	}
	@Override
	public synchronized boolean available() {
		return available;
	}
	@Override
	public synchronized Responder matchRequest(HttpMessage request) throws IOException {
		if (requestMatcher.match(request)) {
			available = false;
			return responder;
		}
		return ErrorResponder.create();
	}
	@Override
	public synchronized void logUnused(int portNo, Logger logger) {
		if (available)
			logger.unused(portNo,requestMatcher.getExpected());
	}
	@Override
	public String toString() {
		return getClass().getSimpleName()+"["+requestMatcher.getExpected()+"]";
	}
}
