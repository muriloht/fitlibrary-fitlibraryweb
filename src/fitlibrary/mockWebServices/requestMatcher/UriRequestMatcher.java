/*
 * Copyright (c) 2009 Rick Mugridge, www.RimuResearch.com
 * Kindly donated by Air New Zealand in October 2009.
 * Released under the terms of the GNU General Public License version 2 or later.
*/
package fitlibrary.mockWebServices.requestMatcher;

import java.io.IOException;

import fitlibrary.ws.message.HttpMessage;

public class UriRequestMatcher extends FussyRequestMatcher {
	private final String uri;
	
	public UriRequestMatcher(String uri) {
		this.uri = uri;
	}
	public String getExpected() {
		return "URL["+uri+"]";
	}
	public boolean match(HttpMessage request) throws IOException {
		return request.getUri().equals(uri);
	}
}
