/*
 * Copyright (c) 2009 Rick Mugridge, www.RimuResearch.com
 * Kindly donated by Air New Zealand in October 2009.
 * Released under the terms of the GNU General Public License version 2 or later.
*/
package fitlibrary.ws.mock.requestMatcher;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import java.util.HashMap;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Test;
import org.junit.runner.RunWith;

import fitlibrary.mockWebServices.requestMatcher.OrRequestMatcher;
import fitlibrary.mockWebServices.requestMatcher.RequestMatcher;
import fitlibrary.ws.message.Message;
import fitlibrary.ws.message.ReplyMessage;

@RunWith(JMock.class)
public class TestOrRequestMatcher {
	Mockery context = new JUnit4Mockery();
	final RequestMatcher first = context.mock(RequestMatcher.class,"first");
	final RequestMatcher second = context.mock(RequestMatcher.class,"second");
	private RequestMatcher matcher = new OrRequestMatcher(first,second);
	protected final Message request = msg("any string");

	@Test public void matchTrueAnything() throws Exception {
		context.checking(new Expectations() {{
			one(first).match(request); will(returnValue(true));
			// won't call the second one at all
	    }});

		assertThat(matcher.match(msg("any string")), equalTo(true));
	}
	@Test public void matchFalseTrue() throws Exception {
		context.checking(new Expectations() {{
	        one(first).match(request); will(returnValue(false));
	        one(second).match(request); will(returnValue(true));
	    }});

		assertThat(matcher.match(msg("any string")), equalTo(true));
	}
	@Test public void matchFalseFalse() throws Exception {
		context.checking(new Expectations() {{
	        one(first).match(request); will(returnValue(false));
	        one(second).match(request); will(returnValue(false));
	    }});

		assertThat(matcher.match(request), equalTo(false));
	}
	private static ReplyMessage msg(String s) {
		return new ReplyMessage(new HashMap<String, String>(),s);
	}
}
