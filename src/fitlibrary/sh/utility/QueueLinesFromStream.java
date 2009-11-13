/*
 * Copyright (c) 2009 Rick Mugridge, www.RimuResearch.com
 * Kindly donated by Air New Zealand in October 2009.
 * Released under the terms of the GNU General Public License version 2 or later.
*/
package fitlibrary.sh.utility;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import fitlibrary.exception.FitLibraryException;

public class QueueLinesFromStream implements Runnable {
	private BufferedReader output;
	private BlockingQueue<String> lines = new LinkedBlockingQueue<String>();

	public QueueLinesFromStream(InputStream output) {
		this.output = new BufferedReader(new InputStreamReader(output));
	}
	public String take(long timeout, TimeUnit unit) throws InterruptedException {
		String line = lines.poll(timeout, unit);
		if (line == null)
			throw new FitLibraryException("Timed out");
		return line.trim().replaceAll("\\t", "").replaceAll("\\r", "").replaceAll("\\ \\ ", " ");
	}
	public String outputRemaining() throws InterruptedException {
		String result = "";
		while (!lines.isEmpty())
			result += lines.take()+"\n";
		return result;
	}
	public String findFirstMatchingLineWaitingFor(String pattern, long timeout, TimeUnit unit) throws InterruptedException {
		while (true) {
			String line = take(timeout, unit);
			if (Pattern.compile(pattern,Pattern.DOTALL).matcher(line).matches())
				return line;
		}
	}
	public void run() {
		while (true) {
			String line;
			try {
				line = output.readLine();
				if (line == null)
					break;
				lines.add(line);
			} catch (IOException e) {
				lines.add("Problem: "+e);
			}
		}
	}

}