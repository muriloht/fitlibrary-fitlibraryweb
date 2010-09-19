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

public class TestPatternRequestMatcher {
	private RequestMatcher literalMatcher = new PatternRequestMatcher("a  X  b");
	private RequestMatcher patternMatcher = new PatternRequestMatcher("a.*b");
	private final Message request = msg("a  X  b");

	@Test public void matchLiteral() throws Exception {
		assertThat(literalMatcher.match(request), equalTo(true));
		assertThat(literalMatcher.match(msg("a  b X")), equalTo(false));
	}
	@Test public void matchPattern() throws Exception {
		assertThat(patternMatcher.match(request), equalTo(true));
		assertThat(patternMatcher.match(msg("abX")), equalTo(false));
	}
	@Test public void and() throws Exception {
		RequestMatcher other = new AcceptAnyRequestMatcher();
		assertThat(patternMatcher.and(other).match(request), equalTo(true));
		assertThat(patternMatcher.and(other).match(msg("b")), equalTo(false));
	}
	private static ReplyMessage msg(String s) {
		return new ReplyMessage(s);
	}
}
