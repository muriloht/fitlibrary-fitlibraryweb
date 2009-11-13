/*
 * Copyright (c) 2009 Rick Mugridge, www.RimuResearch.com
 * Kindly donated by Air New Zealand in October 2009.
 * Released under the terms of the GNU General Public License version 2 or later.
*/
package fitlibrary.mockWebServices.requestMatcher;

import java.io.IOException;

import fitlibrary.ws.message.HttpMessage;
import fitlibrary.xml.XmlDoFixture;

public class XPathMatcher extends FussyRequestMatcher {
	private String xpath;
	private String expected;

	public XPathMatcher(String xpath, String expected) {
		this.xpath = xpath;
		this.expected = expected.trim();
	}
	public String getExpected() {
		return xpath+" expected to be '"+expected+"'";
	}
	public boolean match(HttpMessage request) throws IOException {
//		System.out.println("Try XPathMatcher.match() to "+getExpected());
		String contents = fixNameSpace(request.getContents());
		String value = new XmlDoFixture().xpathIn(xpath, contents);
//		System.out.println("XPathMatcher.match('"+value+"' to "+getExpected());
		return value.trim().equals(expected);
	}
	private String fixNameSpace(String contents) {
		// xpath doesn't like a namespace definition without a name
		return contents.replaceAll("xmlns=", "xmlns:f=");
	}
}
