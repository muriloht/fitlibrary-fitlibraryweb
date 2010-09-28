/*
 * Copyright (c) 2009 Rick Mugridge, www.RimuResearch.com
 * Kindly donated by Air New Zealand in October 2009.
 * Released under the terms of the GNU General Public License version 2 or later.
*/
package fitlibrary.mockWebServices.logger;

import fit.Fixture;
import fitlibrary.ws.clock.Clock;
import fitlibrary.ws.clock.RealClock;
import fitlibrary.ws.message.HttpMessage;

public class ClockedLogger implements MockLogger {
	private final Clock clock;
	private final StringBuilder logs = new StringBuilder();
	private int errorCount = 0;
	private int requests = 0;
	private int unusedCount = 0;

	public ClockedLogger() {
		this(new RealClock());
	}
	public ClockedLogger(Clock clock) {
		this.clock = clock;
	}
	@Override
	public void error(String s) {
		logError("Error: "+s);
	}
	@Override
	public void unused(int portNo, String s) {
		logError("Unused on "+portNo+": "+s);
		unusedCount ++;
	}
	@Override
	public void log(String s) {
		logs.append(clock.dateTime()+" "+escape(s)+"\n");
	}
	@Override
	public boolean hasErrors() {
		return errorCount > 0;
	}
	@Override
	public int errorCount() {
		return errorCount;
	}
	@Override
	public String report() {
		return 	"Requests = "+requests+
				"\nErrors = "+(errorCount-unusedCount)+
				"\nUnused = "+unusedCount+
				"\n"+
				logs.toString();
	}
	private void logError(String s) {
		log(s);
		errorCount++;
	}
	@Override
	public void responded(String context, HttpMessage request, HttpMessage response, int portNo) {
		requests ++;
		String contents = response.getContents().trim();
		String firstLine = "\n-----\n"+clock.dateTime()+" Received on "+context+": '"+Fixture.escape(request.getContents())+"'. ";
		String secondLine = "Replied with: "+response.getResultCode()+" '" + escape(contents) + "'\n";
		String lineBreak = "";
		if (firstLine.length() > 20)
			lineBreak = "\n         ";
		logs.append(firstLine + lineBreak  + secondLine);
		if (!response.isOK())
			errorCount++;
	}
	private String escape(String s) {
		return Fixture.escape(s);
	}
}
