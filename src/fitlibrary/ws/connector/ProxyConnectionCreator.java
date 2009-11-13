/*
 * Copyright (c) 2009 Rick Mugridge, www.RimuResearch.com
 * Kindly donated by Air New Zealand in October 2009.
 * Released under the terms of the GNU General Public License version 2 or later.
*/
package fitlibrary.ws.connector;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;

public class ProxyConnectionCreator implements ConnectionCreator {
	private InetSocketAddress address;

	public ProxyConnectionCreator(String proxyUrl, int portNo) throws IOException {
		this.address = new InetSocketAddress(proxyUrl,portNo);
		if (address.isUnresolved())
			throw new IOException("Proxy is unresolved: "+proxyUrl+":"+portNo);
	}
	public URLConnection openConnection(URL url) throws IOException {
		Proxy proxy = new Proxy(Proxy.Type.HTTP,address);
		return url.openConnection(proxy);
	}
	public static ConnectionCreator createConnector(String proxy) throws IOException {
		String[] split = proxy.split(":");
		if (split.length != 2)
			throw new IOException("Invalid proxy: should be of form 'host:port'.");
		try {
			int portNo = Integer.parseInt(split[1]);
			return new ProxyConnectionCreator(split[0],portNo);
		} catch (NumberFormatException e) {
			throw new IOException("Invalid port in proxy: "+split[1]);
		}
	}
}
