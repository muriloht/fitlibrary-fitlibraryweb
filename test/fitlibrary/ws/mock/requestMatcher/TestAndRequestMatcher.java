/*
 * Copyright (c) 2009 Rick Mugridge, www.RimuResearch.com
 * Kindly donated by Air New Zealand in October 2009.
 * Released under the terms of the GNU General Public License version 2 or later.
*/
package fitlibrary.ws.mock.requestMatcher;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;


import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Test;
import org.junit.runner.RunWith;

import fitlibrary.mockWebServices.requestMatcher.AndRequestMatcher;
import fitlibrary.mockWebServices.requestMatcher.RequestMatcher;
import fitlibrary.ws.message.Message;
import fitlibrary.ws.message.ReplyMessage;

@RunWith(JMock.class)
public class TestAndRequestMatcher {
	Mockery context = new JUnit4Mockery();
	final RequestMatcher first = context.mock(RequestMatcher.class,"first");
	final RequestMatcher second = context.mock(RequestMatcher.class,"second");
	private RequestMatcher matcher = new AndRequestMatcher(first,second);
	protected final Message request = msg("any string");

	@Test public void matchTrueTrue() throws Exception {
		context.checking(new Expectations() {{
			one(first).match(request); will(returnValue(true));
	        one(second).match(request); will(returnValue(true));
	    }});

		assertThat(matcher.match(msg("any string")), equalTo(true));
	}
	@Test public void matchFalseTrue() throws Exception {
		context.checking(new Expectations() {{
	        one(first).match(request); will(returnValue(false));
	    }});

		assertThat(matcher.match(msg("any string")), equalTo(false));
	}
	@Test public void matchTrueFalse() throws Exception {
		context.checking(new Expectations() {{
	        one(first).match(request); will(returnValue(true));
	        one(second).match(request); will(returnValue(false));
	    }});

		assertThat(matcher.match(request), equalTo(false));
	}
	@Test public void matchFalseFalse() throws Exception {
		context.checking(new Expectations() {{
	        one(first).match(request); will(returnValue(false));
	    }});

		assertThat(matcher.match(request), equalTo(false));
	}
	@Test public void and() throws Exception {
		final RequestMatcher third = context.mock(RequestMatcher.class,"third");
		context.checking(new Expectations() {{
	        one(first).match(request); will(returnValue(true));
	        one(second).match(request); will(returnValue(true));
	        one(third).match(request); will(returnValue(true));
	    }});

		assertThat(matcher.and(third).match(request), equalTo(true));
	}
	@Test public void or() throws Exception {
		final RequestMatcher third = context.mock(RequestMatcher.class,"third");
		context.checking(new Expectations() {{
	        one(first).match(request); will(returnValue(true));
	        one(second).match(request); will(returnValue(false));
	        one(third).match(request); will(returnValue(true));
	    }});

		assertThat(matcher.or(third).match(request), equalTo(true));
	}
	@Test public void notFalse() throws Exception {
		context.checking(new Expectations() {{
	        one(first).match(request); will(returnValue(true));
	        one(second).match(request); will(returnValue(false));
	    }});

		assertThat(matcher.not().match(request), equalTo(true));
	}
	@Test public void notTrue() throws Exception {
		context.checking(new Expectations() {{
	        one(first).match(request); will(returnValue(true));
	        one(second).match(request); will(returnValue(true));
	    }});

		assertThat(matcher.not().match(request), equalTo(false));
	}
	private static ReplyMessage msg(String s) {
		return new ReplyMessage(s);
	}
}
