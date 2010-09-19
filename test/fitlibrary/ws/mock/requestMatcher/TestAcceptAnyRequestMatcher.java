/*
 * Copyright (c) 2009 Rick Mugridge, www.RimuResearch.com
 * Kindly donated by Air New Zealand in October 2009.
 * Released under the terms of the GNU General Public License version 2 or later.
*/
package fitlibrary.ws.mock.requestMatcher;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;


import org.junit.Test;

import fitlibrary.mockWebServices.requestMatcher.AcceptAnyRequestMatcher;
import fitlibrary.mockWebServices.requestMatcher.PatternRequestMatcher;
import fitlibrary.mockWebServices.requestMatcher.RequestMatcher;
import fitlibrary.ws.message.Message;
import fitlibrary.ws.message.ReplyMessage;

public class TestAcceptAnyRequestMatcher {
	protected final Message request = msg("any string");
	private RequestMatcher matcher = new AcceptAnyRequestMatcher();
	RequestMatcher other = new PatternRequestMatcher("(.*)");

	@Test public void match() throws Exception {
		assertThat(matcher.match(request), equalTo(true));
	}
	@Test public void and() throws Exception {
		assertThat(matcher.and(other), equalTo(other));
	}
	@Test public void or() throws Exception {
		assertThat(matcher.or(other), equalTo(other));
	}
	@Test public void notFalse() throws Exception {
		assertThat(matcher.not().match(request), equalTo(false));
	}
	private static ReplyMessage msg(String s) {
		return new ReplyMessage(s);
	}
}
