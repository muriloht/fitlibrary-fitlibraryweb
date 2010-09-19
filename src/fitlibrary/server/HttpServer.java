/*
 * $HeadURL: https://svn.apache.org/repos/asf/httpcomponents/httpcore/tags/4.0.1/httpcore/src/examples/org/apache/http/examples/ElementalHttpServer.java $
 * $Revision: 744516 $
 * $Date: 2009-02-14 17:38:14 +0100 (Sat, 14 Feb 2009) $
 *
 * ====================================================================
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 *
 */

package fitlibrary.server;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

import org.apache.http.impl.DefaultConnectionReuseStrategy;
import org.apache.http.impl.DefaultHttpResponseFactory;
import org.apache.http.impl.DefaultHttpServerConnection;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.BasicHttpProcessor;
import org.apache.http.protocol.HttpRequestHandlerRegistry;
import org.apache.http.protocol.HttpService;
import org.apache.http.protocol.ResponseConnControl;
import org.apache.http.protocol.ResponseContent;
import org.apache.http.protocol.ResponseDate;
import org.apache.http.protocol.ResponseServer;
import org.apache.log4j.Logger;

import fitlibrary.exception.FitLibraryException;
import fitlibrary.log.FixturingLogger;

/**
 * Derived from apache.http.ElementalHttpServer @version $Revision: 744516 $
 * "A basic, yet fully functional and spec compliant, HTTP/1.1 file server".
 * 
 * Changes by Rick Mugridge:
 *   Put inner classes into separate files and renamed them (ServerConnectionThread, HttpFileHandler).
 *   Added support for different MIME types, based on the file extension
 *   Created an abstract class for HttpRequestHandlers
 *   Made this class abstract to allow for other HttpRequestHandlers to be registered
 *   Added logging
 */
public abstract class HttpServer {
	static Logger logger = FixturingLogger.getLogger(HttpServer.class);
	protected final String id;
	protected final int portNo;
	private Thread thread;
	protected ServerSocket serversocket;
	protected boolean running = false;
	
	public HttpServer(int portNo, String id) {
		this.portNo = portNo;
		this.id = id+portNo;
	}
	public void start() throws IOException {
		running = true;
		thread = new RequestListenerThread(portNo);
        thread.setDaemon(false);
        thread.start();
		logger.trace("Started on port "+portNo);
	}
	public void stop() throws IOException {
		if (!running)
			throw new FitLibraryException("Not running");
		running = false;
		thread.interrupt();
		serversocket.close();
		logger.trace("Stopped on port "+portNo);
	}
	protected abstract void register(HttpRequestHandlerRegistry reqistry);
	
    class RequestListenerThread extends Thread {
        private final HttpParams params; 
        private final HttpService httpService;
        
        public RequestListenerThread(int port) throws IOException {
        	super(id);
            serversocket = new ServerSocket(port);
            this.params = new BasicHttpParams();
            this.params
                .setIntParameter(CoreConnectionPNames.SO_TIMEOUT, 5000)
                .setIntParameter(CoreConnectionPNames.SOCKET_BUFFER_SIZE, 8 * 1024)
                .setBooleanParameter(CoreConnectionPNames.STALE_CONNECTION_CHECK, false)
                .setBooleanParameter(CoreConnectionPNames.TCP_NODELAY, true)
                .setParameter(CoreProtocolPNames.ORIGIN_SERVER, "HttpComponents/1.1");

            // Set up the HTTP protocol processor
            BasicHttpProcessor httpproc = new BasicHttpProcessor();
            httpproc.addInterceptor(new ResponseDate());
            httpproc.addInterceptor(new ResponseServer());
            httpproc.addInterceptor(new ResponseContent());
            httpproc.addInterceptor(new ResponseConnControl());
            
            // Set up request handlers
            HttpRequestHandlerRegistry reqistry = new HttpRequestHandlerRegistry();
            register(reqistry);
            
            // Set up the HTTP service
            this.httpService = new HttpService(
                    httpproc, 
                    new DefaultConnectionReuseStrategy(), 
                    new DefaultHttpResponseFactory());
            this.httpService.setParams(this.params);
            this.httpService.setHandlerResolver(reqistry);
        }
        @Override
		public void run() {
        	logger.trace("Listening on port " + serversocket.getLocalPort());
            while (!Thread.interrupted()) {
                try {
                    // Set up HTTP connection
                    Socket socket = serversocket.accept();
                    DefaultHttpServerConnection conn = new DefaultHttpServerConnection();
                    logger.trace("Incoming connection from " + socket.getInetAddress());
                    conn.bind(socket, this.params);

                    Thread t = new ServerConnectionThread(id,this.httpService, conn, ""+socket.getInetAddress());
                    t.setDaemon(true);
                    t.start();
                } catch (InterruptedIOException ex) {
                    break;
                } catch (SocketException e) {
                	if (running)
                		logger.error("I/O error initialising connection thread: " + e);
                } catch (IOException e) {
                    logger.error("I/O error initialising connection thread: " + e);
                    break;
                }
            }
        }
    }
}
