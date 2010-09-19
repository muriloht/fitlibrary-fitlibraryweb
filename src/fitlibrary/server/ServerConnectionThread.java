/*
 * Copyright (c) 2010 Rick Mugridge, www.RimuResearch.com
 * Released under the terms of the GNU General Public License version 2 or later.
*/

package fitlibrary.server;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.SocketException;

import org.apache.http.ConnectionClosedException;
import org.apache.http.HttpException;
import org.apache.http.HttpServerConnection;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.HttpService;
import org.apache.log4j.Logger;
import org.apache.log4j.NDC;

import fitlibrary.log.FixturingLogger;

public class ServerConnectionThread extends Thread {
	private static Logger logger2 = FixturingLogger.getLogger(ServerConnectionThread.class);
	private static int WORKERS = 0;
    private final HttpService httpservice;
    private final HttpServerConnection conn;
	private String ipAddress;
    
    public ServerConnectionThread(String id, HttpService httpservice, HttpServerConnection conn, String ipAddress) {
    	super(id+"_"+WORKERS++);
        this.httpservice = httpservice;
        this.conn = conn;
        this.ipAddress = ipAddress;
    }
    @Override
	public void run() {
    	NDC.push("in: "+ipAddress);
    	logger2.debug("Started");
        HttpContext context = new BasicHttpContext(null);
        try {
            while (!Thread.interrupted() && this.conn.isOpen()) {
            	logger2.debug("Handle request");
                this.httpservice.handleRequest(this.conn, context);
            }
        } catch (ConnectionClosedException ex) {
        	logger2.error("Client closed connection");
        } catch (InterruptedIOException ex) {
            //
        } catch (SocketException ex) {
            //
        } catch (IOException ex) {
        	logger2.error("I/O error: " + ex.getMessage()+" "+ex);
        } catch (HttpException ex) {
        	logger2.error("Unrecoverable HTTP protocol violation: " + ex.getMessage());
        } finally {
        	logger2.trace("Finished");
        	NDC.pop();
        	NDC.remove();
            try {
                this.conn.shutdown();
            } catch (IOException ignore) {
            	//
            }
        }
    }
}