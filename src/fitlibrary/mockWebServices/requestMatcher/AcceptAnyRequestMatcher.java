/*
 * Copyright (c) 2009 Rick Mugridge, www.RimuResearch.com
 * Kindly donated by Air New Zealand in October 2009.
 * Released under the terms of the GNU General Public License version 2 or later.
*/
package fitlibrary.mockWebServices.requestMatcher;

import java.io.IOException;

import fitlibrary.ws.message.HttpMessage;

public class AcceptAnyRequestMatcher implements RequestMatcher {
	public String getExpected() {
		return "Any request";
	}
	public boolean match(HttpMessage request) throws IOException {
		return true;
	}
	public RequestMatcher and(RequestMatcher requestMatcher) {
		return requestMatcher;
	}
	public RequestMatcher or(RequestMatcher requestMatcher) {
		return requestMatcher;
	}
	public RequestMatcher not() {
		return new NotRequestMatcher(this);
	}
}
