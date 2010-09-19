/*
 * Copyright (c) 2009 Rick Mugridge, www.RimuResearch.com
 * Kindly donated by Air New Zealand in October 2009.
 * Released under the terms of the GNU General Public License version 2 or later.
*/
package fitlibrary.mockWebServices.specify;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import fitlibrary.mockWebServices.logger.MockLogger;
import fitlibrary.ws.MockWebServicesFixture;

public class SpecifyWebServiceServers extends MockWebServicesFixture {
	private static final String FIT_NESSE_ROOT_FILES_SOAP = "FitNesseRoot/files/soap";
	
	public SpecifyWebServiceServers() {
		File file = new File(FIT_NESSE_ROOT_FILES_SOAP);
		if (file.exists() && file.isDirectory()) {
			File[] listFiles = file.listFiles();
			if (listFiles != null)
				for (File f : listFiles)
					f.delete();
		}
	}
	public void createSoapFileWith(String fileName, String contents) throws IOException {
		File file = new File(FIT_NESSE_ROOT_FILES_SOAP);
		file.mkdirs();
		FileUtils.writeStringToFile(new File(file,fileName), contents, "utf-8");
	}
	public int closeWithErrors() throws IOException {
		MockLogger logger = mockingWebServices.close(100);
		showAfterTable(logger.report());
		return logger.errorCount();
	}
	public void startLogging() {
		Logger.getRootLogger().setLevel(Level.ALL);
	}
}
