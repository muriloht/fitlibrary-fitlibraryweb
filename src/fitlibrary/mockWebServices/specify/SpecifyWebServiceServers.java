/*
 * Copyright (c) 2009 Rick Mugridge, www.RimuResearch.com
 * Kindly donated by Air New Zealand in October 2009.
 * Released under the terms of the GNU General Public License version 2 or later.
*/
package fitlibrary.mockWebServices.specify;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import fitlibrary.mockWebServices.logger.MockLogger;
import fitlibrary.ws.MockWebServicesFixture;
import fitlibrary.ws.WebServicesClientFixture;

public class SpecifyWebServiceServers extends MockWebServicesFixture {
	private static final String FIT_NESSE_ROOT_FILES_SOAP = "FitNesseRoot/files/soap";
	protected WebServicesClientFixture client = new WebServicesClientFixture();
	
	public SpecifyWebServiceServers() {
		File file = new File(FIT_NESSE_ROOT_FILES_SOAP);
		if (file.exists() && file.isDirectory()) {
			File[] listFiles = file.listFiles();
			if (listFiles != null)
				for (File f : listFiles)
					f.delete();
		}
	}
	public String toPost(String uri, String request) throws Exception {
		String response = client.postingSoapWith(uri,request);
		return unwrapSoap(response);
	}
	private String unwrapSoap(String response) {
		String HEAD = "<soap:Body>";
		int header = response.indexOf(HEAD);
		if (header < 0)
			return response;
		int trailer = response.indexOf("</soap:Body>");
		if (trailer < 0)
			return response;
		return response.substring(header+HEAD.length(),trailer);
	}
	public void createSoapFileWith(String fileName, String contents) throws IOException {
		File file = new File(FIT_NESSE_ROOT_FILES_SOAP);
		file.mkdirs();
		FileWriter fileWriter = new FileWriter(new File(file,fileName));
		fileWriter.write(contents);
		fileWriter.close();
	}
	public int closeWithErrors() {
		MockLogger logger = mockingWebServices.close(100);
		showAfterTable(logger.report());
		return logger.errorCount();
	}
}
