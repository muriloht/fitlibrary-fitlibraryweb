/*
 * Copyright (c) 2009 Rick Mugridge, www.RimuResearch.com
 * Kindly donated by Air New Zealand in October 2009.
 * Released under the terms of the GNU General Public License version 2 or later.
*/
package fitlibrary.mockWebServices.term;

import java.io.IOException;

import fitlibrary.mockWebServices.responder.Responder;
import fitlibrary.ws.logger.Logger;
import fitlibrary.ws.message.HttpMessage;

public interface Term {
	// These methods need to be synchronized in implementing classes
	// As terms must form a strict hierarchy to avoid deadlock,
	// we ensure a term is in a single composite container with setComposite()
	Responder matchRequest(HttpMessage request) throws IOException;
	boolean available();
	void logUnused(int portNo, Logger logger);
	void setComposite(Term compositeTerm);
}
