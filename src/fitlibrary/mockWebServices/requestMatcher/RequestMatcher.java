/*
 * Copyright (c) 2009 Rick Mugridge, www.RimuResearch.com
 * Kindly donated by Air New Zealand in October 2009.
 * Released under the terms of the GNU General Public License version 2 or later.
*/
package fitlibrary.mockWebServices.requestMatcher;

import java.io.IOException;

import fitlibrary.ws.message.HttpMessage;

public interface RequestMatcher {
	boolean match(HttpMessage request) throws IOException;
	String getExpected();
	RequestMatcher and(RequestMatcher requestMatcher);
	RequestMatcher or(RequestMatcher requestMatcher);
	RequestMatcher not();
}
