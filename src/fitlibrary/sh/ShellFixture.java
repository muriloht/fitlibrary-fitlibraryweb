/*
 * Copyright (c) 2009 Rick Mugridge, www.RimuResearch.com
 * Kindly donated by Air New Zealand in October 2009.
 * Released under the terms of the GNU General Public License version 2 or later.
*/
package fitlibrary.sh;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.concurrent.TimeUnit;

import fitlibrary.exception.FitLibraryException;
import fitlibrary.sh.utility.InteractiveFixture;
import fitlibrary.sh.utility.QueueLinesFromStream;

public class ShellFixture extends InteractiveFixture {
	protected Process process;
	protected QueueLinesFromStream sysErr;
	
	public boolean asynchronously(String commandLine) throws IOException {
		shell(commandLine);
		return true;
	}
	public int synchronously(String commandLine) throws IOException, InterruptedException {
		shell(commandLine);
		return process.waitFor();
	}
	public void shell(String commandLine) throws IOException {
		process = Runtime.getRuntime().exec(commandLine);
		sysOut = new QueueLinesFromStream(process.getInputStream());
		sysErr = new QueueLinesFromStream(process.getErrorStream());
		sysIn = new BufferedWriter(new OutputStreamWriter(process.getOutputStream()));
		new Thread(sysOut).start();
		new Thread(sysErr).start();
	}
	public String sysErrLineWaitingFor(long timeout) throws InterruptedException {
		if (sysErr == null)
			throw new FitLibraryException("Not connected");
		String result = sysErr.take(timeout,TimeUnit.MILLISECONDS);
		if (result == null)
			throw new FitLibraryException("timed out");
		return result;
	}
	public String sysErrLine() throws InterruptedException {
		return sysErrLineWaitingFor(defaultTimeout);
	}
	public String firstMatchingLineWithInSysErr(String pattern) throws InterruptedException {
		return firstMatchingLineWithSysErrWaitingFor(pattern, defaultTimeout);
	}
	public String firstMatchingLineWithSysErrWaitingFor(String pattern, long timeout) throws InterruptedException {
		if (sysOut == null)
			throw new FitLibraryException("Not connected");
		return sysErr.findFirstMatchingLineWaitingFor(".*"+pattern+".*", timeout, TimeUnit.MILLISECONDS);
	}
	public String outputsRemaining() throws InterruptedException {
		sleep(10);
		return "<h3>System Out:</h3><pre>"+sysOut.outputRemaining()+
			"</pre><hr/><h3>System Err:</h3><pre>"+sysErr.outputRemaining()+"</pre>";
	}
	public void waitToFinish() throws InterruptedException {
		waitToFinish(defaultTimeout);
	}
	public void waitToFinish(final long timeout) throws InterruptedException {
		new Thread(new Runnable() {
			public void run() {
				try {
					Thread.sleep(timeout);
				} catch (InterruptedException e) {
					//
				} finally {
					process.destroy();
				}
			}
		}).start();
		process.waitFor();
	}
	@Override
	protected Object getService() {
		return process;
	}
}
