/*
 * Copyright (c) 2009 Rick Mugridge, www.RimuResearch.com
 * Kindly donated by Air New Zealand in October 2009.
 * Released under the terms of the GNU General Public License version 2 or later.
*/
package fitlibrary.ws.logger;

import fitlibrary.ws.clock.Clock;
import fitlibrary.ws.clock.RealClock;
import fitlibrary.ws.message.HttpMessage;

public class DisplayingLogger implements Logger {
	private final Clock clock;
	private boolean noisy = true;
	
	public DisplayingLogger(Clock clock) {
		this.clock = clock;
	}
	public DisplayingLogger() {
		this(new RealClock());
	}
	public synchronized void error(String s) {
		System.err.println(clock.dateTime()+" RecordingLogger Error: "+s);
	}
	public synchronized void responded(String context, HttpMessage request, HttpMessage response, int portNo) {
		System.out.println(clock.dateTime()+context+":");
		System.out.println("RX\n"+request+"\nEND-RX");
		System.out.println("TT\n"+response+"\nEND-TX");
	}
	public synchronized void log(String s) {
		if (noisy)
			System.err.println(clock.dateTime()+" "+s);
	}
	public void unused(int portNo, String expected) {
		log("Unused "+portNo+": "+expected);
	}
	public void beQuiet() {
		noisy  = false;
	}
}
