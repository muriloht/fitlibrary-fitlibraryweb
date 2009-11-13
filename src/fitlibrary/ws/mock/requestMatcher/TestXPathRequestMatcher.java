/*
 * Copyright (c) 2009 Rick Mugridge, www.RimuResearch.com
 * Kindly donated by Air New Zealand in October 2009.
 * Released under the terms of the GNU General Public License version 2 or later.
*/
package fitlibrary.ws.mock.requestMatcher;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import java.util.HashMap;

import org.junit.Test;

import fitlibrary.mockWebServices.requestMatcher.AcceptAnyRequestMatcher;
import fitlibrary.mockWebServices.requestMatcher.RequestMatcher;
import fitlibrary.mockWebServices.requestMatcher.XPathMatcher;
import fitlibrary.ws.message.Message;
import fitlibrary.ws.message.ReplyMessage;

public class TestXPathRequestMatcher {
	private static final String XML = "<a id=\"22\">B</a>";
	private static final Message XML_REQUEST = msg(XML);
	private static final String XML_WITH_WHITESPACE = "\n<a id=\"22\">\n B </a>";
	private static final String WRONG_XML = "<a id=\"22\">BC</a>";
	private static final Message WRONG_XML_REQUEST = msg(WRONG_XML);
	private static final String MISSING_ID = "<a id=\"3\">BC</a>";
	private RequestMatcher matcher = new XPathMatcher("//a[@id=\"22\"]","B");

	@Test public void matches() throws Exception {
		assertThat(matcher.match(XML_REQUEST), equalTo(true));
		assertThat(matcher.match(msg(XML_WITH_WHITESPACE)), equalTo(true));
	}
	@Test public void mismatches() throws Exception {
		assertThat(matcher.match(WRONG_XML_REQUEST), equalTo(false));
		assertThat(matcher.match(msg(MISSING_ID)), equalTo(false));
	}
	@Test public void and() throws Exception {
		RequestMatcher other = new AcceptAnyRequestMatcher();
		assertThat(matcher.and(other).match(XML_REQUEST), equalTo(true));
		assertThat(matcher.and(other).match(WRONG_XML_REQUEST), equalTo(false));
	}
	private static ReplyMessage msg(String s) {
		return new ReplyMessage(new HashMap<String, String>(),s);
	}
}
