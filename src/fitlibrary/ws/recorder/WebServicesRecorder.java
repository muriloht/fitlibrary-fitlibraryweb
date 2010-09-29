/*
 * Copyright (c) 2009 Rick Mugridge, www.RimuResearch.com
 * Kindly donated by Air New Zealand in October 2009.
 * Released under the terms of the GNU General Public License version 2 or later.
*/
package fitlibrary.ws.recorder;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;

import fitlibrary.log.FixturingLogger;
import fitlibrary.server.ProxyServer;
import fitlibrary.server.Recorder;
import fitlibrary.server.UriMapper;

public class WebServicesRecorder {
	static Logger logger = FixturingLogger.getLogger(WebServicesRecorder.class);
	private List<ProxyServer> servers = new ArrayList<ProxyServer>();

	public WebServicesRecorder(String propertyFileName, String resultsFolderName, String soapVersion, RecordingFolderSelector recordingFolderSelector) throws IOException {
		Properties properties = readProperties(propertyFileName);
		createRecorders(properties,resultsFolderName,soapVersion,recordingFolderSelector);
	}
	private void createRecorders(Properties properties, String resultsFolderName, String soapVersion, RecordingFolderSelector recordingFolderSelector) throws IOException {
		for (int i = 1; i < 10000; i++) {
			Object localPort = properties.get("localPort"+i);
			Object hostUrl = properties.get("ws"+i);
			if (localPort == null || hostUrl == null) {
				if (i == 1) {
					System.err.println("At least one port must be defined in the property file");
					logger.error("At least one port must be defined in the property file");
					return;
				}
				break;
			}
			int localPortNo = Integer.parseInt(localPort.toString());
			createWebServicesRecorder(localPortNo, hostUrl.toString(),resultsFolderName,soapVersion,recordingFolderSelector);
		}
	}
	private void createWebServicesRecorder(int localPortNo, final String wsUrl, String resultsFolderName, String soapVersion, RecordingFolderSelector recordingFolderSelector) throws IOException {
		Recorder fileRecorder = new FileRecorder(localPortNo,resultsFolderName,soapVersion,recordingFolderSelector);
		UriMapper mapper = new UriMapper(){
			@Override
			public String map(String uri, String path) {
				return wsUrl+path;
			}
		};
		logger.trace("Starting (Mapped)ProxyServer for recording on port "+localPortNo+" -> "+wsUrl);
		servers.add(new ProxyServer(localPortNo,mapper,fileRecorder));
	}
	private Properties readProperties(String fileName) throws IOException {
		Properties properties = new Properties();
		FileReader reader = new FileReader(new File(fileName));
		properties.load(reader);
		reader.close();
		return properties;
	}
	public static void main(String[] args) throws IOException {
		if (args.length != 2 && args.length != 3) {
			System.err.println("Usage: fitlibrary.ws.recorder.WebServicesRecorder propertyFileName resultsFolderName [soapVersion]\n"+
					"where the optional soapVersion is SOAP11 or SOAP12 (by default it's SOAP11");
			return;
		}
		String soapVersion = args.length == 3 ? args[2] : "SOAP11";
		@SuppressWarnings("unused")
		WebServicesRecorder webServicesRecorder = new WebServicesRecorder(args[0],args[1],soapVersion,new UniqueRecordingFolderSelector());
	}
	public void stop() throws IOException {
		for (ProxyServer server: servers)
			server.stop();
		servers.clear();
	}
}
