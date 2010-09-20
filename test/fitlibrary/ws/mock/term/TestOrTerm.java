/*
 * Copyright (c) 2009 Rick Mugridge, www.RimuResearch.com
 * Kindly donated by Air New Zealand in October 2009.
 * Released under the terms of the GNU General Public License version 2 or later.
*/
package fitlibrary.ws.mock.term;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;


import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Test;
import org.junit.runner.RunWith;

import fitlibrary.mockWebServices.responder.ErrorResponder;
import fitlibrary.mockWebServices.responder.LiteralResponder;
import fitlibrary.mockWebServices.responder.Responder;
import fitlibrary.mockWebServices.term.CompositeTerm;
import fitlibrary.mockWebServices.term.OrTerm;
import fitlibrary.mockWebServices.term.Term;
import fitlibrary.ws.message.ContentType;
import fitlibrary.ws.message.Message;
import fitlibrary.ws.message.ReplyMessage;

@RunWith(JMock.class)
public class TestOrTerm {
	final Mockery context = new JUnit4Mockery();
	final static Responder ERROR = ErrorResponder.create();
	final static Responder SOME = new LiteralResponder("aa",ContentType.PLAIN);
	final Term t1 = context.mock(Term.class,"term1");
	final Term t2 = context.mock(Term.class,"term2");
	final Message request = msg("request");

	@Test public void emptySequence() throws Exception {
		OrTerm term = new OrTerm();
		assertThat(term.matchRequest(request), equalTo(ERROR));
	}
	@Test public void allUsed() throws Exception {
		context.checking(new Expectations() {{
			one(t1).setComposite(with(any(CompositeTerm.class)));
	        one(t1).available(); will(returnValue(false));
			one(t2).setComposite(with(any(CompositeTerm.class)));
	        one(t2).available(); will(returnValue(false));
	    }});
		assertThat(term(t1,t2).matchRequest(request), equalTo(ERROR));
	}
	@Test public void noMatch() throws Exception {
		context.checking(new Expectations() {{
			one(t1).setComposite(with(any(CompositeTerm.class)));
			one(t2).setComposite(with(any(CompositeTerm.class)));
	        one(t1).available(); will(returnValue(true));
	        one(t1).matchRequest(request); will(returnValue(ERROR));
	        one(t2).available(); will(returnValue(true));
	        one(t2).matchRequest(request); will(returnValue(ERROR));
	    }});
		assertThat(term(t1,t2).matchRequest(request), equalTo(ERROR));
	}
	@Test public void firstMatch() throws Exception {
		context.checking(new Expectations() {{
			one(t1).setComposite(with(any(CompositeTerm.class)));
			one(t2).setComposite(with(any(CompositeTerm.class)));
	        one(t1).available(); will(returnValue(true));
	        one(t1).matchRequest(request); will(returnValue(SOME));
	    }});
		assertThat(term(t1,t2).matchRequest(request), equalTo(SOME));
	}
	@Test public void secondMatch() throws Exception {
		context.checking(new Expectations() {{
			one(t1).setComposite(with(any(CompositeTerm.class)));
			one(t2).setComposite(with(any(CompositeTerm.class)));
	        one(t1).available(); will(returnValue(false));
	        one(t2).available(); will(returnValue(true));
	        one(t2).matchRequest(request); will(returnValue(SOME));
	    }});
		assertThat(term(t1,t2).matchRequest(request), equalTo(SOME));
	}
	@Test public void bothMatch() throws Exception {
		context.checking(new Expectations() {{
			one(t1).setComposite(with(any(CompositeTerm.class)));
			one(t2).setComposite(with(any(CompositeTerm.class)));
	        one(t1).available(); will(returnValue(true));
	        one(t1).matchRequest(request); will(returnValue(SOME));
	    }});
		assertThat(term(t1,t2).matchRequest(request), equalTo(SOME));
	}
	private Term term(Term term1, Term term2) {
		return new OrTerm(term1,term2);
	}
	private static ReplyMessage msg(String s) {
		return new ReplyMessage(s);
	}
}
