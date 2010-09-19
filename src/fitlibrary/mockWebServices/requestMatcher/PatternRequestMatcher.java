/*
 * Copyright (c) 2009 Rick Mugridge, www.RimuResearch.com
 * Kindly donated by Air New Zealand in October 2009.
 * Released under the terms of the GNU General Public License version 2 or later.
*/
package fitlibrary.mockWebServices.requestMatcher;

import java.util.regex.Pattern;

import fitlibrary.ws.message.HttpMessage;

public class PatternRequestMatcher extends FussyRequestMatcher {
	private String pattern;

	public PatternRequestMatcher(String pattern) {
		this.pattern = pattern;
	}
	@Override
	public String getExpected() {
		return "match '"+pattern+"'";
	}
	@Override
	public boolean match(HttpMessage request) {
		return Pattern.compile(pattern,Pattern.DOTALL).matcher(request.getContents()).matches();
	}
}
