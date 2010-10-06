/*
 * Copyright (c) 2010 Rick Mugridge, www.RimuResearch.com
 * Released under the terms of the GNU General Public License version 2 or later.
 */

package fitlibrary.server;

import java.io.IOException;

import org.apache.http.protocol.HttpRequestHandlerRegistry;

public class WebServerForTesting extends HttpServer {
	private String docroot;
	
	public WebServerForTesting(int portNo, String docroot) throws IOException {
		super(portNo,"web");
        this.docroot = docroot;
        start();
	}
	@Override
	protected void register(HttpRequestHandlerRegistry reqistry) {
		reqistry.register("/ws*", new WebServiceHandler());
		reqistry.register("/files*", new HttpFileHandler(docroot));
	}
}