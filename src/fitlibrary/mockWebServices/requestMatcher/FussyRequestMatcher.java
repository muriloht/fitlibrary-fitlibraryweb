/*
 * Copyright (c) 2009 Rick Mugridge, www.RimuResearch.com
 * Kindly donated by Air New Zealand in October 2009.
 * Released under the terms of the GNU General Public License version 2 or later.
*/
package fitlibrary.mockWebServices.requestMatcher;


public abstract class FussyRequestMatcher implements RequestMatcher {
	public RequestMatcher and(RequestMatcher requestMatcher) {
		return new AndRequestMatcher(this,requestMatcher);
	}
	public RequestMatcher or(RequestMatcher requestMatcher) {
		return new OrRequestMatcher(this,requestMatcher);
	}
	public RequestMatcher not() {
		return new NotRequestMatcher(this);
	}
}
