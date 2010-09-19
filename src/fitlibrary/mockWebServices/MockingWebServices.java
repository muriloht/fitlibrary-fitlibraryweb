/*
 * Copyright (c) 2009 Rick Mugridge, www.RimuResearch.com
 * Kindly donated by Air New Zealand in October 2009.
 * Released under the terms of the GNU General Public License version 2 or later.
*/
package fitlibrary.mockWebServices;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import fitlibrary.log.FitLibraryLogger;
import fitlibrary.mockWebServices.logger.ClockedLogger;
import fitlibrary.mockWebServices.logger.MockLogger;
import fitlibrary.mockWebServices.term.Term;
import fitlibrary.ws.clock.Clock;
import fitlibrary.ws.clock.RealClock;

public class MockingWebServices {
	static Logger logger = FitLibraryLogger.getLogger(MockingWebServices.class);
	protected final MockLogger logging;
	private Map<Integer,MockingServer> servers = new HashMap<Integer,MockingServer>();

	public MockingWebServices() {
		this(new RealClock());
	}
	public MockingWebServices(Clock clock) {
		logging = new ClockedLogger(clock);
		Logger.getRootLogger().setLevel(Level.ALL);
		logger.debug("Started MockingWebServices");
	}
	public MockLogger close(int timeout) throws IOException {
		sleep(timeout);
		closePorts();
		sleep(timeout);
		return logging;
	}
	private void closePorts() throws IOException {
		for (Integer port : servers.keySet())
			servers.get(port).stop();
		servers.clear();
	}
	public void or(int portNo, Term term, boolean insertAtEnd) throws IOException {
		logger.debug("or "+term);
		MockingServer server = servers.get(new Integer(portNo));
		if (server == null) {
			server = new MockingServer(portNo,logging);
			servers.put(new Integer(portNo),server);
		}
		server.or(term,insertAtEnd);
		logger.debug(portNo+" with "+server.getTerm());
	}
	private void sleep(int timeout) {
		try {
			Thread.sleep(timeout);
		} catch (InterruptedException e) {
			// do nothing
		}
	}
}
