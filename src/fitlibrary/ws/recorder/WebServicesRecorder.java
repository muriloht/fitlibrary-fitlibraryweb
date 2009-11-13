/*
 * Copyright (c) 2009 Rick Mugridge, www.RimuResearch.com
 * Kindly donated by Air New Zealand in October 2009.
 * Released under the terms of the GNU General Public License version 2 or later.
*/
package fitlibrary.ws.recorder;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import fitlibrary.ws.logger.DisplayingLogger;
import fitlibrary.ws.logger.Logger;
import fitlibrary.ws.server.HttpServerOnPort;
import fitlibrary.ws.server.RealWebService;

public class WebServicesRecorder {
	public WebServicesRecorder(String propertyFileName, String resultsFolderName) throws IOException {
		Properties properties = readProperties(propertyFileName);
		Logger logger = pickLogger(resultsFolderName);
		createRecorders(properties,logger);
	}
	private Logger pickLogger(String resultsFolderName) {
		DisplayingLogger displayingLogger = new DisplayingLogger();
//		displayingLogger.beQuiet();
		return new RecordToFilesLogger(resultsFolderName,displayingLogger);
	}
	private void createRecorders(Properties properties,Logger logger) throws IOException {
		System.out.println();
		for (int i = 1; i < 10000; i++) {
			Object localPort = properties.get("localPort"+i);
			Object hostUrl = properties.get("ws"+i);
			if (localPort == null || hostUrl == null) {
				if (i == 1) {
					System.err.println("At least one port must be defined in the property file");
					return;
				}
				break;
			}
			int localPortNo = Integer.parseInt(localPort.toString());
			createWebServicesRecorder(localPortNo, hostUrl.toString(), logger);
		}
	}
	private void createWebServicesRecorder(int localPortNo, String wsUrl, Logger logger) throws IOException {
		RealWebService webServiceQuote = new RealWebService(wsUrl,logger);
		System.out.println("Recording on port "+localPortNo+" through to "+wsUrl);
		new HttpServerOnPort(localPortNo,webServiceQuote,logger);
	}
	private Properties readProperties(String fileName) throws IOException {
		Properties properties = new Properties();
		FileReader reader = new FileReader(new File(fileName));
		properties.load(reader);
		reader.close();
		return properties;
	}

	public static void main(String[] args) throws IOException {
		if (args.length != 2) {
			System.err.println("Usage: fitlibrary.ws.recorder.WebServicesRecorder propertyFileName resultsFolderName");
			return;
		}
		new WebServicesRecorder(args[0],args[1]);
	}
}
