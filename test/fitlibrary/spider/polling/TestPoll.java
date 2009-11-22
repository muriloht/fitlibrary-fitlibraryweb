/*
 * Copyright (c) 2009 Rick Mugridge, www.RimuResearch.com
 * Kindly donated by Air New Zealand in October 2009.
 * Released under the terms of the GNU General Public License version 2 or later.
*/
package fitlibrary.spider.polling;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import fitlibrary.exception.FitLibraryException;
import fitlibrary.log.Log;

@RunWith(JMock.class)
public class TestPoll {
	Mockery context = new JUnit4Mockery();

	@Before
	public void before() {
		Log.setDebugging(false);
	}
	@Test
	public void sleepPeriodIsLimitedAtMinimum() {
		assertThat(Poll.sleepPeriod(0),is(Poll.MIN_SLEEP));
		assertThat(Poll.sleepPeriod(1),is(Poll.MIN_SLEEP));
		assertThat(Poll.sleepPeriod(Poll.MIN_SLEEP*Poll.DIV_SLEEP-1),is(Poll.MIN_SLEEP));
		assertThat(Poll.sleepPeriod(Poll.DIV_SLEEP*5) > Poll.MIN_SLEEP,is(true));
	}
	@Test
	public void sleepPeriodIsLimitedAtMaximum() {
		assertThat(Poll.sleepPeriod(100000),is(Poll.MAX_SLEEP));
		assertThat(Poll.sleepPeriod(Poll.DIV_SLEEP*Poll.MAX_SLEEP+1),is(Poll.MAX_SLEEP));
		assertThat(Poll.sleepPeriod(Poll.DIV_SLEEP*Poll.MAX_SLEEP-1) < Poll.MAX_SLEEP,is(true));
	}
	@Test
	public void ensureWithErrorSucceeds() {
		final PollForWithError poll = context.mock(PollForWithError.class);
		context.checking(new Expectations() {{
			one(poll).matches(); will(returnValue(false));
			one(poll).matches(); will(returnValue(false));
			one(poll).matches(); will(returnValue(true));
	    }});

		poll().ensureWithError(poll);
	}
	@Test(expected=FitLibraryException.class)
	public void ensureWithErrorFails() {
		final PollForWithError poll = context.mock(PollForWithError.class);
		context.checking(new Expectations() {{
			atLeast(1).of(poll).matches(); will(returnValue(false));
	        one(poll).error(); will(returnValue("fault"));
	    }});

		poll().ensureWithError(poll);
	}
	@Test
	public void ensureMatchesSucceeds() {
		final PollForMatches poll = context.mock(PollForMatches.class);
		context.checking(new Expectations() {{
			one(poll).matches(); will(returnValue(false));
			one(poll).matches(); will(returnValue(false));
			one(poll).matches(); will(returnValue(true));
	    }});

		assertThat(poll().ensureMatches(poll),is(true));
	}
	@Test
	public void ensureMatchesFails() {
		final PollForMatches poll = context.mock(PollForMatches.class);
		context.checking(new Expectations() {{
			atLeast(1).of(poll).matches(); will(returnValue(false));
	    }});

		assertThat(poll().ensureMatches(poll),is(false));
	}
	@SuppressWarnings("unchecked")
	@Test
	public void ensureNoExceptionSucceeds() throws Exception {
		final PollForNoException<String> poll = context.mock(PollForNoException.class);
		context.checking(new Expectations() {{
			one(poll).act(); will(throwException(new RuntimeException("")));
			one(poll).act(); will(throwException(new RuntimeException("")));
			one(poll).act(); will(throwException(new RuntimeException("")));
			one(poll).act(); will(returnValue("ok"));
	    }});

		
		String result = poll().ensureNoException(poll);
		assertThat(result,is("ok"));
	}
	@SuppressWarnings("unchecked")
	@Test(expected=FitLibraryException.class)
	public void ensureNoExceptionFails() throws Exception {
		final PollForNoException<String> poll = context.mock(PollForNoException.class);
		context.checking(new Expectations() {{
			atLeast(1).of(poll).act(); will(throwException(new FitLibraryException("")));
	    }});
		
		poll().ensureNoException(poll);
	}
	private Poll poll() {
		return new Poll(50);
	}
}
