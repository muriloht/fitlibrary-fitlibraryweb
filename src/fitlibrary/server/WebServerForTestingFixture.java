/*
 * Copyright (c) 2010 Rick Mugridge, www.RimuResearch.com
 * Released under the terms of the GNU General Public License version 2 or later.
*/

package fitlibrary.server;

import java.io.IOException;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import fitlibrary.exception.FitLibraryException;

public class WebServerForTestingFixture {
	private HttpServer server;
	
	public boolean startFileServerOnPortAtDirectory(int portNo, String diry) throws IOException {
		server = new WebServerForTesting(portNo,diry);
		return true;
	}
	public void startLogging() {
		Logger.getRootLogger().setLevel(Level.ALL);
	}
	public boolean stopTestingServerAfterSeconds(long seconds) throws IOException, InterruptedException {
		Thread.sleep(seconds*1000);
		return stopTestingServer();
	}
	public boolean stopTestingServer() throws IOException {
		if (server == null)
			throw new FitLibraryException("Server is not running");
		server.stop();
		server = null;
		return true;
	}
	public void tearDown() throws IOException {
		if (server != null)
			stopTestingServer();
	}
}
