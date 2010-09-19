/*
 * Copyright (c) 2009 Rick Mugridge, www.RimuResearch.com
 * Kindly donated by Air New Zealand in October 2009.
 * Released under the terms of the GNU General Public License version 2 or later.
*/
package fitlibrary.mockWebServices.requestMatcher;

import java.io.IOException;

import fitlibrary.ws.message.HttpMessage;

public class AcceptAnyRequestMatcher implements RequestMatcher {
	@Override
	public String getExpected() {
		return "Any request";
	}
	@Override
	public boolean match(HttpMessage request) throws IOException {
		return true;
	}
	@Override
	public RequestMatcher and(RequestMatcher requestMatcher) {
		return requestMatcher;
	}
	@Override
	public RequestMatcher or(RequestMatcher requestMatcher) {
		return requestMatcher;
	}
	@Override
	public RequestMatcher not() {
		return new NotRequestMatcher(this);
	}
}
