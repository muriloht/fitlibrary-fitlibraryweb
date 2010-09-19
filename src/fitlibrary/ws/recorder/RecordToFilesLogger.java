/*
 * Copyright (c) 2009 Rick Mugridge, www.RimuResearch.com
 * Kindly donated by Air New Zealand in October 2009.
 * Released under the terms of the GNU General Public License version 2 or later.
*/
package fitlibrary.ws.recorder;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.IOUtils;

import fitlibrary.ws.logger.Logger;
import fitlibrary.ws.message.HttpMessage;

public class RecordToFilesLogger implements Logger {
	private static final String REQUEST_FILENAME = "request.xml";
	private static final String RESPONSE_FILENAME = "response.xml";
	private final Logger displayingLogger;
	private final File resultsFolder;
	private final Map<Integer,Integer> counts = new HashMap<Integer,Integer>();

	public RecordToFilesLogger(String resultsFolderName, Logger displayingLogger) {
		File folder = new File(resultsFolderName);
		this.displayingLogger = displayingLogger;
		resultsFolder = new File(folder,selectFileName(formattedDateTime()));
		resultsFolder.mkdirs();
	}
	@Override
	public void error(String s) {
		displayingLogger.error(s);
	}
	@Override
	public void log(String s) {
		displayingLogger.log(s);
	}
	@Override
	public void responded(String context, HttpMessage request, HttpMessage response, int portNo) {
		boolean first = first(portNo);
		String fileName = "Port"+portNo+"Response"+nextResponseNo(portNo);
		File diry = new File(resultsFolder,fileName);
		diry.mkdirs();
		writeFile(diry, REQUEST_FILENAME, request);
		writeFile(diry, RESPONSE_FILENAME, response);

		String s = "";
		if (first)
			s = "!*> diry\n"+
				"!define diry (!-"+resultsFolder.getAbsolutePath()+"-!)\n"+
				"*!\n\n"+
				"|!-fitlibrary.ws.MockWebServicesFixture-!|\n\n"+
				"|mock soap on port|"+portNo+"|\n";
		else
			s = "|then|\n";
		s +=
			"|matches URL|!-"+request.getUri()+"-!|\n"+
			"|matches request from file|${diry}/!-"+fileName+"/"+REQUEST_FILENAME+"-!|\n"+
			"|response from file|${diry}/!-"+fileName+"/"+RESPONSE_FILENAME+"-!|\n";
		
		File summaryFile = new File(resultsFolder,"storytest"+portNo+".txt");
		try {
			FileWriter fileWriter = new FileWriter(summaryFile,true);
			IOUtils.write(s, fileWriter);
			fileWriter.close();
		} catch (IOException e) {
			error("Problem writing file: "+e);
		}
	}
	private boolean first(int portNo) {
		return currentCount(portNo) == null;
	}
	private int nextResponseNo(int portNo) {
		Integer count = currentCount(portNo);
		if (count == null) {
			counts.put(portNo,1);
			return 1;
		}
		counts.put(portNo, count+1);
		return count+1;
	}
	private Integer currentCount(int portNo) {
		return counts.get(portNo);
	}
	private void writeFile(File diry, String fileName, HttpMessage msg) {
		File requestFile = new File(diry,fileName);
		try {
			FileWriter fileWriter = new FileWriter(requestFile);
			IOUtils.write(msg.getContents(), fileWriter);
			fileWriter.close();
		} catch (IOException e) {
			error("Problem writing file: "+e);
		}
	}
	@Override
	public void unused(int portNo, String expected) {
		displayingLogger.unused(portNo,expected);
	}
	private static String selectFileName(String fileName) {
		String fullFileName = fileName;
		if (new File(fullFileName).exists()) {
			for (int i = 1; i < 10000; i++) {
				String logFileName = fileName+"-"+i;
				if (!new File(logFileName).exists()) {
					return logFileName;
				}
			}
		}
		return fullFileName;
	}
	private static String formattedDateTime() {
		return new SimpleDateFormat("yyyy-MM-dd_HH-mm").format(new Date());
	}
}
