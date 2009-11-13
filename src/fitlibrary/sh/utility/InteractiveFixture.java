/*
 * Copyright (c) 2009 Rick Mugridge, www.RimuResearch.com
 * Kindly donated by Air New Zealand in October 2009.
 * Released under the terms of the GNU General Public License version 2 or later.
*/
package fitlibrary.sh.utility;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import fitlibrary.exception.FitLibraryException;
import fitlibrary.service.ServicingFixture;

public abstract class InteractiveFixture extends ServicingFixture {
	protected BufferedWriter sysIn;
	protected QueueLinesFromStream sysOut;

	public String sysOutLine() throws InterruptedException {
		return sysOutLine(defaultTimeout);
	}
	public String sysOutLine(long timeout) throws InterruptedException {
		if (sysOut == null)
			throw new FitLibraryException("Not connected");
		return sysOut.take(timeout,TimeUnit.MILLISECONDS).trim();
	}
	public String firstMatchingLineWithInSysOut(String pattern) throws InterruptedException {
		return firstMatchingLineWithSysOutWaitingFor(pattern, defaultTimeout);
	}
	public String firstMatchingLineWithSysOutWaitingFor(String pattern, long timeout) throws InterruptedException {
		if (sysOut == null)
			throw new FitLibraryException("Not connected");
		return sysOut.findFirstMatchingLineWaitingFor(".*"+pattern+".*", timeout, TimeUnit.MILLISECONDS);
	}
	public void sleep(int milliseconds) throws InterruptedException {
		Thread.sleep(milliseconds);
	}
	public void writeInput(String s) throws IOException {
		if (sysIn == null)
			throw new FitLibraryException("Not connected");
		sysIn.write(s+"\n");
	}
}
