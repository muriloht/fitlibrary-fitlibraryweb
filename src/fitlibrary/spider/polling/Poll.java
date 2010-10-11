/*
 * Copyright (c) 2009 Rick Mugridge, www.RimuResearch.com
 * Kindly donated by Air New Zealand in October 2009.
 * Released under the terms of the GNU General Public License version 2 or later.
*/
package fitlibrary.spider.polling;

import org.apache.log4j.Logger;

import fitlibrary.exception.FitLibraryException;
import fitlibrary.log.FixturingLogger;

public class Poll {
	static Logger logger = FixturingLogger.getLogger(Poll.class);
	protected static final long MIN_SLEEP = 2;
	protected static final long MAX_SLEEP = 10;
	protected static final long DIV_SLEEP = 50;
	private final long timeout;
	private final long start = now();

	public Poll(long timeout) {
		this.timeout = timeout;
	}
	public void ensureWithError(PollForWithError poll) {
		while (!timedOut()) {
			if (poll.matches()) {
				return;
			}
			sleep(timeout);
		}
		throw new FitLibraryException(poll.error());
	}
	public boolean ensureMatches(PollForMatches poll) {
		while (!timedOut()) {
			if (poll.matches())
				return true;
			sleep(timeout);
		}
		timeOutTrace();
		return false;
	}
	public boolean ensureMatchesNoException(PollForMatches poll) {
		while (!timedOut()) {
			try {
				if (poll.matches())
					return true;
			} catch (Exception e) {
				//
			}
			sleep(timeout);
		}
		timeOutTrace();
		return false;
	}
	public <T> T ensureNoException(PollForNoException<T> poll) throws Exception {
		boolean loggedError = false;
		while (true) {
			try {
				return poll.act();
			} catch (Exception e) {
				if (!loggedError)
					logger.trace("Exception caught: "+e);
				loggedError = true;
				if (timedOut()) {
					throw e;
				}
			}
			sleep(timeout);
		}
	}
	private boolean timedOut() {
		return now() - start > timeout;
	}
	private static void sleep(long timeout) {
		try {
			Thread.sleep(sleepPeriod(timeout));
		} catch (Exception e) {
			//
		}
	}
	public static long sleepPeriod(long timeout) {
		return Math.min(MAX_SLEEP, Math.max(MIN_SLEEP,timeout / DIV_SLEEP));
	}
	private static long now() {
		return System.currentTimeMillis();
	}
	private void timeOutTrace() {
		logger.trace("Timed out after "+(now() - start)+" milliseconds");
	}
}
