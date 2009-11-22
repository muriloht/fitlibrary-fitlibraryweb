/*
 * Copyright (c) 2009 Rick Mugridge, www.RimuResearch.com
 * Kindly donated by Air New Zealand in October 2009.
 * Released under the terms of the GNU General Public License version 2 or later.
*/
package fitlibrary.ws.mock.requestMatcher;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.util.HashMap;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Test;
import org.junit.runner.RunWith;

import fitlibrary.mockWebServices.requestMatcher.FussyRequestMatcher;
import fitlibrary.mockWebServices.requestMatcher.RequestMatcher;
import fitlibrary.ws.message.HttpMessage;
import fitlibrary.ws.message.Message;
import fitlibrary.ws.message.ReplyMessage;

@RunWith(JMock.class)
public class TestFussyRequestMatcher {
	protected final Message request = msg("any string");
	Mockery context = new JUnit4Mockery();
	final RequestMatcher first = context.mock(RequestMatcher.class,"first");
	private RequestMatcher failingMatcher = new FussyRequestMatcher() {
		public String getExpected() {
			return null;
		}
		public boolean match(@SuppressWarnings("hiding") HttpMessage request) throws IOException {
			return false;
		}
	};
	private RequestMatcher passingMatcher = new FussyRequestMatcher() {
		public String getExpected() {
			return null;
		}
		public boolean match(@SuppressWarnings("hiding") HttpMessage request) throws IOException {
			return true;
		}
	};

	@Test public void andFalseTrue() throws Exception {
		assertThat(failingMatcher.and(first).match(request), equalTo(false));
	}
	@Test public void andTrueFalse() throws Exception {
		context.checking(new Expectations() {{
	        one(first).match(request); will(returnValue(false));
	    }});
		assertThat(passingMatcher.and(first).match(request), equalTo(false));
	}
	@Test public void andTrueTrue() throws Exception {
		context.checking(new Expectations() {{
	        one(first).match(request); will(returnValue(true));
	    }});
		assertThat(passingMatcher.and(first).match(request), equalTo(true));
	}
	@Test public void orFalseFalse() throws Exception {
		context.checking(new Expectations() {{
	        one(first).match(request); will(returnValue(false));
	    }});
		assertThat(failingMatcher.or(first).match(request), equalTo(false));
	}
	@Test public void orFalseTrue() throws Exception {
		context.checking(new Expectations() {{
	        one(first).match(request); will(returnValue(true));
	    }});
		assertThat(failingMatcher.or(first).match(request), equalTo(true));
	}
	@Test public void orTrueAny() throws Exception {
		assertThat(passingMatcher.or(first).match(request), equalTo(true));
	}
	@Test public void notFalse() throws Exception {
		assertThat(failingMatcher.not().match(request), equalTo(true));
	}
	@Test public void notTrue() throws Exception {
		assertThat(passingMatcher.not().match(request), equalTo(false));
	}
	private static ReplyMessage msg(String s) {
		return new ReplyMessage(new HashMap<String, String>(),s);
	}
}
