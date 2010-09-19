/*
 * Copyright (c) 2009 Rick Mugridge, www.RimuResearch.com
 * Kindly donated by Air New Zealand in October 2009.
 * Released under the terms of the GNU General Public License version 2 or later.
*/
package fitlibrary.mockWebServices.term;

import java.io.IOException;

import fitlibrary.mockWebServices.responder.ErrorResponder;
import fitlibrary.mockWebServices.responder.Responder;
import fitlibrary.ws.message.HttpMessage;

public class SequentialTerm extends CompositeTerm {
	public SequentialTerm(Term... ts) {
		super(ts);
	}
	@Override
	public synchronized Responder matchRequest(HttpMessage request) throws IOException {
		for (Term term : terms)
			if (term.available())
				return term.matchRequest(request);
		return ErrorResponder.create();
	}
}
