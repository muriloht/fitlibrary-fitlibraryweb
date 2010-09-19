/*
 * Copyright (c) 2009 Rick Mugridge, www.RimuResearch.com
 * Kindly donated by Air New Zealand in October 2009.
 * Released under the terms of the GNU General Public License version 2 or later.
*/
package fitlibrary.mockWebServices.requestMatcher;

import java.io.IOException;

import fitlibrary.ws.message.HttpMessage;

public class NotRequestMatcher extends FussyRequestMatcher {
	private RequestMatcher matcher;

	public NotRequestMatcher(RequestMatcher matcher) {
		this.matcher = matcher;
	}
	@Override
	public String getExpected() {
		return "not "+matcher.getExpected();
	}
	@Override
	public boolean match(HttpMessage request) throws IOException {
		return !matcher.match(request);
	}
}
