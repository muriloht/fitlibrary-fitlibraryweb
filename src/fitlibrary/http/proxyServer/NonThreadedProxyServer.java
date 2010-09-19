package fitlibrary.http.proxyServer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.EnhancedPatternLayout;
import org.apache.log4j.Logger;

import fitlibrary.log.FixturingLogger;

public class NonThreadedProxyServer {
	static Logger logger = FixturingLogger.getLogger(NonThreadedProxyServer.class);
	private int portNumber = 0;
	private ServerSocket serverSocket;
	private int calls = 0;
	
	public NonThreadedProxyServer(int portNumber) {
		this.portNumber = portNumber;
		logger.addAppender(new ConsoleAppender(new EnhancedPatternLayout("%r [%t] %-5p: %m%n")));
	}
	public void start() {
		logger.trace("Starting on port "+portNumber+" ...");
		try {
			serverSocket = new ServerSocket(portNumber, 1);
			byte[] buffer = new byte[10000];
			while (true) {
				Socket clientSocket = serverSocket.accept();
				logger.debug("NonThreadedProxyServer: Got a connection");
				calls  += 1;
				InputStream bis = clientSocket.getInputStream();
				int n = bis.read(buffer);
				logger.debug("Received "+n+" bytes in request");
				String host = getHost(new String(buffer, 0, n));

				Socket hostSocket = forwardToServer(host, buffer, n);

				replyFromServer(hostSocket.getInputStream(), clientSocket.getOutputStream(), buffer);
				hostSocket.close();
				clientSocket.close();
				logger.debug("NonThreadedProxyServer: End of communication");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public int stop() throws IOException {
		serverSocket.close();
		serverSocket = null;
		return calls;
	}
	private String getHost(String request) {
		logger.debug("NonThreadedProxyServer: Request = "+request+"\n==========");
		int start = request.indexOf("Host: ") + 6;
		int end = request.indexOf('\n', start);
		String host = request.substring(start, end - 1);
		logger.debug("NonThreadedProxyServer: Host = " + host);
		return host;
	}
	private Socket forwardToServer(String hostPort, byte[] buffer, int n) throws UnknownHostException, IOException {
		String[] split = hostPort.split(":");
		String host = split[0];
		int port = 80;
		if (split.length == 2)
			port = Integer.parseInt(split[1]);
		logger.debug("NonThreadedProxyServer: Forwarding request to server "+host+" : "+port);
		Socket hostSocket = new Socket(host, port);
		OutputStream sos = hostSocket.getOutputStream();
		logger.debug("NonThreadedProxyServer: Sending "+n+" bytes");
		sos.write(buffer, 0, n);
		sos.flush();
		return hostSocket;
	}
	private void replyFromServer(InputStream fromOtherServer, OutputStream reply, byte[] buffer) throws IOException {
		logger.debug("NonThreadedProxyServer: Replying with response from server");
		while (true) {
			int n = fromOtherServer.read(buffer);
			if (n < 0)
				break;
			logger.debug("NonThreadedProxyServer: Receiving " + n + " bytes: \n"+new String(buffer,0,n)+"\n--------");
			reply.write(buffer, 0, n);
		}
		reply.flush();
	}
	public static void main(String[] args) {
		NonThreadedProxyServer proxyServer = new NonThreadedProxyServer(5555);
		proxyServer.start();
	}
}
