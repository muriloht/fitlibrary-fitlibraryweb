/*
 * Copyright (c) 2009 Rick Mugridge, www.RimuResearch.com
 * Kindly donated by Air New Zealand in October 2009.
 * Released under the terms of the GNU General Public License version 2 or later.
*/
package fitlibrary.mockWebServices;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import fitlibrary.mockWebServices.logger.ClockedLogger;
import fitlibrary.mockWebServices.logger.MockLogger;
import fitlibrary.mockWebServices.term.Term;
import fitlibrary.ws.clock.Clock;
import fitlibrary.ws.clock.RealClock;

public class MockingWebServices {
	private final MockLogger logger;
	private Map<Integer,MockingServer> ports = new HashMap<Integer,MockingServer>();

	public MockingWebServices() {
		this(new RealClock());
	}
	public MockingWebServices(Clock clock) {
		logger = new ClockedLogger(clock);
	}
	public MockLogger close(int timeout) {
		sleep(timeout);
		closePorts();
		sleep(timeout);
		return logger;
	}
	private void closePorts() {
		for (Integer port : ports.keySet())
			ports.get(port).close();
		ports.clear();
	}
	public Term or(int portNo, Term term, boolean insertAtEnd) throws IOException {
		MockingServer port = ports.get(new Integer(portNo));
		if (port == null) {
			port = new MockingServer(portNo,logger);
			ports.put(new Integer(portNo),port);
		}
		port.or(term,insertAtEnd);
		return port.getTerm();
	}
	private void sleep(int timeout) {
		try {
			Thread.sleep(timeout);
		} catch (InterruptedException e) {
			// do nothing
		}
	}
}
