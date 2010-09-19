/*
 * Copyright (c) 2010 Rick Mugridge, www.RimuResearch.com
 * Released under the terms of the GNU General Public License version 2 or later.
*/

package fitlibrary.ws.recorder;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

import fitlibrary.log.FixturingLogger;
import fitlibrary.server.Recorder;

public class FileRecorder implements Recorder {
	static Logger logger = FixturingLogger.getLogger(FileRecorder.class);
	private static final String REQUEST_FILENAME = "request.xml";
	private static final String RESPONSE_FILENAME = "response.xml";
	private final int localPortNo;
	private final File resultsFolder;
	private final AtomicInteger count = new AtomicInteger(0);

	public FileRecorder(int localPortNo, String resultsFolderName) {
		this.localPortNo = localPortNo;
		File folder = new File(resultsFolderName);
		resultsFolder = new File(folder,selectFileName(formattedDateTime()));
		resultsFolder.mkdirs();
	}
	@Override
	public void record(String uri, String requestContents,String responseContents) {
		String fileName = "Port"+localPortNo+"Response"+count.incrementAndGet();
		File diry = new File(resultsFolder,fileName);
		diry.mkdirs();
		writeFile(diry, REQUEST_FILENAME, requestContents);
		writeFile(diry, RESPONSE_FILENAME, responseContents);

		StringBuilder s = new StringBuilder();
		if (count.get() == 1)
			s.append("!*> diry\n"+
				"!define diry (!-"+resultsFolder.getAbsolutePath()+"-!)\n"+
				"*!\n\n"+
				"|!-fitlibrary.ws.MockWebServicesFixture-!|\n\n"+
				"|mock soap on port|"+localPortNo+"|\n");
		else
			s.append("|then|\n");
		s.append(
			"|matches URL|!-"+uri+"-!|\n"+
			"|matches request from file|${diry}/!-"+fileName+"/"+REQUEST_FILENAME+"-!|\n"+
			"|response from file|${diry}/!-"+fileName+"/"+RESPONSE_FILENAME+"-!|\n");
		writeFile(resultsFolder, "storytest"+localPortNo+".txt", s.toString());
	}
	private void writeFile(File diry, String fileName, String msg) {
		File requestFile = new File(diry,fileName);
		try {
			FileWriter fileWriter = new FileWriter(requestFile);
			IOUtils.write(msg, fileWriter);
			fileWriter.close();
		} catch (IOException e) {
			logger.error("Problem writing file: "+e);
		}
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
