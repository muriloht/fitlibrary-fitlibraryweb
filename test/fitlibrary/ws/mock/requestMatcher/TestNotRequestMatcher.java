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

import fitlibrary.mockWebServices.requestMatcher.NotRequestMatcher;
import fitlibrary.mockWebServices.requestMatcher.RequestMatcher;
import fitlibrary.ws.message.Message;
import fitlibrary.ws.message.ReplyMessage;

@RunWith(JMock.class)
public class TestNotRequestMatcher {
	Mockery context = new JUnit4Mockery();
	final RequestMatcher first = context.mock(RequestMatcher.class,"first");
	private RequestMatcher matcher = new NotRequestMatcher(first);
	protected final Message request = msg("any string");

	@Test public void matchTrue() throws Exception {
		context.checking(new Expectations() {{
			one(first).match(request); will(returnValue(true));
	    }});

		assertThat(matcher.match(msg("any string")), equalTo(false));
	}
	@Test public void matchFalse() throws Exception {
		context.checking(new Expectations() {{
	        one(first).match(request); will(returnValue(false));
	    }});

		assertThat(matcher.match(msg("any string")), equalTo(true));
	}
	private static ReplyMessage msg(String s) {
		return new ReplyMessage(s);
	}
}
