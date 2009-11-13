/*
 * Copyright (c) 2009 Rick Mugridge, www.RimuResearch.com
 * Kindly donated by Air New Zealand in October 2009.
 * Released under the terms of the GNU General Public License version 2 or later.
*/
package fitlibrary.ws.mock.logger;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Test;
import org.junit.runner.RunWith;

import fitlibrary.mockWebServices.logger.ClockedLogger;
import fitlibrary.ws.clock.Clock;

@RunWith(JMock.class)
public class TestClockedLogger {
	Mockery context = new JUnit4Mockery();
	final Clock clock = context.mock(Clock.class);
	final ClockedLogger clockedLogger = new ClockedLogger(clock);

	@Test public void initialState() {
		assertThat(clockedLogger.hasErrors(), equalTo(false));
		assertThat(clockedLogger.report(), equalTo("Requests = 0\nErrors = 0\nUnused = 0\n\n"));
	}
	@Test public void error() {
		context.checking(new Expectations() {{
			one(clock).dateTime(); will(returnValue("12:34"));
	    }});
		clockedLogger.error("ERR");
		assertThat(clockedLogger.hasErrors(), equalTo(true));
		assertThat(clockedLogger.report(), equalTo("Requests = 0\nErrors = 1\nUnused = 0\n\n12:34 Error: ERR\n"));
	}
	@Test public void unused() {
		context.checking(new Expectations() {{
			one(clock).dateTime(); will(returnValue("12:34"));
	    }});
		clockedLogger.unused(1,"ERR");
		assertThat(clockedLogger.hasErrors(), equalTo(true));
		assertThat(clockedLogger.report(), equalTo("Requests = 0\nErrors = 0\nUnused = 1\n\n12:34 Unused on 1: ERR\n"));
	}
	@Test public void log() {
		context.checking(new Expectations() {{
			one(clock).dateTime(); will(returnValue("12:34"));
		}});
		clockedLogger.log("LOG");
		assertThat(clockedLogger.hasErrors(), equalTo(false));
		assertThat(clockedLogger.report(), equalTo("Requests = 0\nErrors = 0\nUnused = 0\n\n12:34 LOG\n"));
	}
	@Test public void errorsAndLogs() {
		context.checking(new Expectations() {{
			one(clock).dateTime(); will(returnValue("12:34"));
			one(clock).dateTime(); will(returnValue("12:36"));
			one(clock).dateTime(); will(returnValue("12:38"));
			one(clock).dateTime(); will(returnValue("12:41"));
	    }});
		clockedLogger.error("ERR");
		clockedLogger.log("LOG");
		clockedLogger.error("WHOOPS");
		clockedLogger.log("FROG");
		assertThat(clockedLogger.hasErrors(), equalTo(true));
		assertThat(clockedLogger.report(), equalTo(
				"Requests = 0\nErrors = 2\nUnused = 0\n\n"+
				"12:34 Error: ERR\n"+
				"12:36 LOG\n"+
				"12:38 Error: WHOOPS\n"+
				"12:41 FROG\n"));
		
	}
}
