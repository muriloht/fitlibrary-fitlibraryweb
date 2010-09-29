/*
 * Copyright (c) 2010 Rick Mugridge, www.RimuResearch.com
 * Released under the terms of the GNU General Public License version 2 or later.
*/

package fitlibrary.ws.recorder;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
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
	private final String soapVersion;

	public FileRecorder(int localPortNo, String resultsFolderName, String soapVersion, RecordingFolderSelector recordingFolderSelector) {
		this.localPortNo = localPortNo;
		this.soapVersion = soapVersion;
		File folder = new File(resultsFolderName);
		resultsFolder = new File(folder,recordingFolderSelector.selectFileName());
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
				"|'''also run'''|''with mock web services''|\n\n"+
				"|''mock full soap as''|"+soapVersion+"|''on port''|"+localPortNo+"|\n");
		else
			s.append("|''then''|\n");
		s.append(
			"|''matches URL''|!-"+uri+"-!|\n"+
			"|''matches request from file''|${diry}/!-"+fileName+"/"+REQUEST_FILENAME+"-!|\n"+
			"|''response from file''|${diry}/!-"+fileName+"/"+RESPONSE_FILENAME+"-!|\n");
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
}
