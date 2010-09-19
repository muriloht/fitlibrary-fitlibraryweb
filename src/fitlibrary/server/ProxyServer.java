/*
 * Copyright (c) 2010 Rick Mugridge, www.RimuResearch.com
 * Released under the terms of the GNU General Public License version 2 or later.
*/

package fitlibrary.server;

import java.io.IOException;

import org.apache.http.protocol.HttpRequestHandlerRegistry;
import org.apache.log4j.Logger;

import fitlibrary.log.FixturingLogger;

public class ProxyServer extends HttpServer {
	static Logger logger2 = FixturingLogger.getLogger(ProxyServer.class);
	private UriMapper uriMapper;
	private Recorder recorder;

	public ProxyServer(int portNumber) throws IOException {
		this(portNumber,new NonMapper(),new NullRecorder());
	}
	public ProxyServer(int portNumber, UriMapper uriMapper, Recorder recorder) throws IOException {
		super(portNumber,"proxy");
		this.uriMapper = uriMapper;
		this.recorder = recorder;
		start();
		logger2.trace("Started on port "+portNumber);
	}
	@Override
	protected void register(HttpRequestHandlerRegistry reqistry) {
		reqistry.register("*", new RecordingProxyHandler(uriMapper,recorder));
	}
	@SuppressWarnings("unused")
	public static void main(String[] args) throws IOException {
		new ProxyServer(Integer.parseInt(args[0]));
	}
	
	static class NonMapper implements UriMapper {
		@Override
		public String map(String uri) {
			return uri;
		}
	}
	static class NullRecorder implements Recorder {
		@Override
		public void record(String uri, String requestContents, String responseContents) {
			//
		}
	}
}
