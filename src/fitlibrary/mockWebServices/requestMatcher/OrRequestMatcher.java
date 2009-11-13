/*
 * Copyright (c) 2009 Rick Mugridge, www.RimuResearch.com
 * Kindly donated by Air New Zealand in October 2009.
 * Released under the terms of the GNU General Public License version 2 or later.
*/
package fitlibrary.mockWebServices.requestMatcher;

import java.io.IOException;

import fitlibrary.ws.message.HttpMessage;

public class OrRequestMatcher extends FussyRequestMatcher {
	private final RequestMatcher first;
	private final RequestMatcher second;

	public OrRequestMatcher(RequestMatcher first, RequestMatcher second) {
		this.first = first;
		this.second = second;
	}
	public String getExpected() {
		return first.getExpected() + " or " + second.getExpected();
	}
	public boolean match(HttpMessage request) throws IOException {
		return first.match(request) || second.match(request);
	}
}
