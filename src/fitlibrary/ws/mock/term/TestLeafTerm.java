/*
 * Copyright (c) 2009 Rick Mugridge, www.RimuResearch.com
 * Kindly donated by Air New Zealand in October 2009.
 * Released under the terms of the GNU General Public License version 2 or later.
*/
package fitlibrary.ws.mock.term;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import java.util.HashMap;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Test;
import org.junit.runner.RunWith;

import fitlibrary.mockWebServices.logger.MockLogger;
import fitlibrary.mockWebServices.requestMatcher.RequestMatcher;
import fitlibrary.mockWebServices.responder.LiteralResponder;
import fitlibrary.mockWebServices.responder.Responder;
import fitlibrary.mockWebServices.term.CompositeTerm;
import fitlibrary.mockWebServices.term.LeafTerm;
import fitlibrary.mockWebServices.term.SequentialTerm;
import fitlibrary.mockWebServices.term.Term;
import fitlibrary.ws.message.Message;
import fitlibrary.ws.message.ReplyMessage;

@RunWith(JMock.class)
public class TestLeafTerm {
	final Mockery context = new JUnit4Mockery();
	final RequestMatcher matcher = context.mock(RequestMatcher.class);
	final Responder responder = new LiteralResponder("aa");
	final Message request = msg("request");
	final Term t1 = new LeafTerm(matcher,responder);
	final Term t2 = context.mock(Term.class,"term2");
	final MockLogger logger = context.mock(MockLogger.class);

	@Test public void initialState() throws Exception {
		context.checking(new Expectations() {{
			one(matcher).getExpected(); will(returnValue("UNUSED"));
			one(logger).unused(1,"UNUSED");
		}});
		assertThat(t1.available(), equalTo(true));
		t1.logUnused(1,logger);
	}
	@Test public void used() throws Exception {
		context.checking(new Expectations() {{
			one(matcher).match(request); will(returnValue(true));
	    }});
		assertThat(t1.matchRequest(request), equalTo(responder));
		assertThat(t1.available(), equalTo(false));
		t1.logUnused(1,logger);
	}
	@Test(expected=RuntimeException.class)
	public void termCanOnlyBeInOneCompositeSoAsToAvoidDeadlock() {
		context.checking(new Expectations() {{
			one(t2).setComposite(with(any(CompositeTerm.class)));
	    }});
		term(t1,t2);
		term(t1,t2);
	}
	private Term term(Term term1, Term term2) {
		return new SequentialTerm(term1,term2);
	}
	private static ReplyMessage msg(String s) {
		return new ReplyMessage(new HashMap<String, String>(),s);
	}
}
