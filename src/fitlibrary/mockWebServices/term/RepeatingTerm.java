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

public class RepeatingTerm extends AbstractTerm {
	private final Term term;

	public RepeatingTerm(Term term) {
		this.term = term;
		term.setComposite(this);
	}
	public boolean available() {
		return true;
	}
	public void logUnused(int portNo, Logger logger) {
		// It's never unused
	}
	public Responder matchRequest(HttpMessage request) throws IOException {
		return term.matchRequest(request);
	}
	@Override
	public String toString() {
		return getClass().getName()+"["+term+"]";
	}
}
