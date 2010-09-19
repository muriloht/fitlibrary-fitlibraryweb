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

import fitlibrary.mockWebServices.logger.MockLogger;
import fitlibrary.mockWebServices.responder.LiteralResponder;
import fitlibrary.mockWebServices.responder.Responder;
import fitlibrary.mockWebServices.term.RepeatingTerm;
import fitlibrary.mockWebServices.term.Term;
import fitlibrary.ws.message.Message;
import fitlibrary.ws.message.ReplyMessage;

@RunWith(JMock.class)
public class TestRepeatingTerm {
	final Mockery context = new JUnit4Mockery();
	final static Responder SOME = new LiteralResponder("aa");
	final Term t1 = context.mock(Term.class,"term1");
	final Message request = msg("request");
	final MockLogger logger = context.mock(MockLogger.class);

	@Test public void alwaysAvailableAndNeverUnused() throws Exception {
		context.checking(new Expectations() {{
			one(t1).setComposite(with(any(RepeatingTerm.class)));
	        exactly(2).of(t1).matchRequest(request); will(returnValue(SOME));
	    }});
		final RepeatingTerm repeatingTerm = new RepeatingTerm(t1);
		assertThat(repeatingTerm.available(), equalTo(true));
		repeatingTerm.logUnused(1,logger);
		assertThat(repeatingTerm.matchRequest(request), equalTo(SOME));
		assertThat(repeatingTerm.available(), equalTo(true));
		repeatingTerm.logUnused(1,logger);
		assertThat(repeatingTerm.matchRequest(request), equalTo(SOME));
		assertThat(repeatingTerm.available(), equalTo(true));
		repeatingTerm.logUnused(1,logger);
	}
	private static ReplyMessage msg(String s) {
		return new ReplyMessage(s);
	}
}
