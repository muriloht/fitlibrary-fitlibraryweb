/*
 * Copyright (c) 2009 Rick Mugridge, www.RimuResearch.com
 * Kindly donated by Air New Zealand in October 2009.
 * Released under the terms of the GNU General Public License version 2 or later.
*/
package fitlibrary.mockWebServices.term;

public abstract class AbstractTerm implements Term {
	private Object compositeTerm = null;

	@Override
	public synchronized void setComposite(Term compositeTerm) {
		if (this.compositeTerm  != null)
			throw new RuntimeException("Cannot allow a term to be in more than one composite; otherwise we'll get deadlock.");
		this.compositeTerm = compositeTerm;
	}
}
