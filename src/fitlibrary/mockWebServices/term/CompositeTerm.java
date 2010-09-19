/*
 * Copyright (c) 2009 Rick Mugridge, www.RimuResearch.com
 * Kindly donated by Air New Zealand in October 2009.
 * Released under the terms of the GNU General Public License version 2 or later.
*/
package fitlibrary.mockWebServices.term;

import java.util.ArrayList;
import java.util.List;

import fitlibrary.ws.logger.Logger;

public abstract class CompositeTerm extends AbstractTerm {
	protected List<Term> terms = new ArrayList<Term>();
	
	public CompositeTerm(Term... ts) {
		for (Term t : ts)
			add(t);
	}
	public void add(Term term) {
		add(term,true);
	}
	public synchronized void add(Term term, boolean insertAtEnd) {
		if (insertAtEnd)
			terms.add(term);
		else
			terms.add(0, term);
		term.setComposite(this);
	}
	@Override
	public synchronized boolean available() {
		for (Term term : terms)
			if (term.available()) 
				return true;
		return false;
	}
	public synchronized boolean isEmpty() {
		return terms.isEmpty();
	}
	@Override
	public synchronized void logUnused(int portNo, Logger logger) {
		for (Term term: terms)
			term.logUnused(portNo,logger);
	}
	@Override
	public String toString() {
		return getClass().getSimpleName()+terms;
	}
}
