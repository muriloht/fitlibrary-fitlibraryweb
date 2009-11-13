/*
 * Copyright (c) 2009 Rick Mugridge, www.RimuResearch.com
 * Kindly donated by Air New Zealand in October 2009.
 * Released under the terms of the GNU General Public License version 2 or later.
*/
package fitlibrary.mockWebServices.requestMatcher;

import fitlibrary.ws.message.HttpMessage;

public class EqualsRequestMatcher extends FussyRequestMatcher {
	private String pattern;

	public EqualsRequestMatcher(String pattern) {
		this.pattern = pattern;
	}
	public String getExpected() {
		return "equals '"+pattern+"'";
	}
	public boolean match(HttpMessage request) {
		return pattern.equals(request.getContents());
	}
}
