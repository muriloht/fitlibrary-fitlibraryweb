/*
 * Copyright (c) 2010 Rick Mugridge, www.RimuResearch.com
 * Released under the terms of the GNU General Public License version 2 or later.
*/

package fitlibrary.server;

import java.io.IOException;

import fitlibrary.exception.FitLibraryException;

public class ProxyServerFixture {
	private ProxyServer server;
	
	public boolean startProxyServerOnPort(int portNumber) throws IOException {
		this.server = new ProxyServer(portNumber);
		return true;
	}
	public boolean stopProxyServerAfterSeconds(long seconds) throws IOException, InterruptedException {
		Thread.sleep(seconds*1000);
		return stopProxyServer();
	}
	public boolean stopProxyServer() throws IOException {
		if (server == null)
			throw new FitLibraryException("Proxy is not started");
		server.stop();
		server = null;
		return true;
	}
}
