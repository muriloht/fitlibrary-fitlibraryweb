/*
 * Copyright (c) 2009 Rick Mugridge, www.RimuResearch.com
 * Kindly donated by Air New Zealand in October 2009.
 * Released under the terms of the GNU General Public License version 2 or later.
*/
package fitlibrary.sh;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.SocketException;

import org.apache.commons.net.telnet.TelnetClient;

import fitlibrary.sh.utility.InteractiveFixture;
import fitlibrary.sh.utility.QueueLinesFromStream;

public class TelnetFixture extends InteractiveFixture {
	private TelnetClient telnet = new TelnetClient();
	
	public void connect(InetAddress hostname, int port) throws SocketException, IOException {
		telnet.connect(hostname, port);
		sysIn = new BufferedWriter(new OutputStreamWriter(telnet.getOutputStream()));
		sysOut = new QueueLinesFromStream(telnet.getInputStream());
	}
	public String outputsRemaining() throws InterruptedException {
		sleep(10);
		return "<hr/>"+sysOut.outputRemaining();
	}
	@Override
	protected Object getService() {
		return telnet;
	}
}
