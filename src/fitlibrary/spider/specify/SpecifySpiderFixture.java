/*
 * Copyright (c) 2009 Rick Mugridge, www.RimuResearch.com
 * Kindly donated by Air New Zealand in October 2009.
 * Released under the terms of the GNU General Public License version 2 or later.
*/
package fitlibrary.spider.specify;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import fitlibrary.runResults.TestResults;
import fitlibrary.server.WebServerForTesting;
import fitlibrary.spider.SpiderFixture;
import fitlibrary.table.Row;
import fitlibrary.utility.StringUtility;

public class SpecifySpiderFixture extends SpiderFixture {
	private String testFileName;
	private final int portNo;
	private final String driverName;
	private final WebServerForTesting webServer;
	
	public SpecifySpiderFixture() throws IOException {
		this(8096,"htmlUnit");
	}
	public SpecifySpiderFixture(int portNo, String driverName) throws IOException {
		this.portNo = portNo;
		this.driverName = driverName;
		webServer = new WebServerForTesting(portNo,"FitNesseRoot");
	}
	public boolean saveHtmlIn(String testingFileName) {
		this.testFileName = driverName+"/"+testingFileName;
		return true;
	}
	@SuppressWarnings("unused")
	public void zeroReportCounts(final Row row, TestResults testResults) throws Exception {
		testResults.clear();
	}
	public boolean withHtml(String html) throws Exception {
		String s = html.replaceAll("t-table","table");
		s = StringUtility.replaceString(s,"^@(","{");
		s = StringUtility.replaceString(s,"^@)","}");
		s = StringUtility.replaceString(s,"\\n","\n");
		s = StringUtility.replaceString(s,"\\r","\r");
		s = StringUtility.replaceString(s,"\\t","\t");
		writeFile(FITNESSE_DIFFERENCES.getLocalFile(testFileName).getFile(),s);
		selectInitialWindow();
		getUrl("http://localhost:"+portNo+"/files/"+testFileName);
		return true;
	}
	private void writeFile(File file, String html) throws IOException {
		createDirectory(file.getParentFile());
		FileWriter fileWriter = new FileWriter(file);
		fileWriter.write("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\"> \n");
		fileWriter.write("<html>\n");
		fileWriter.write(html);
		fileWriter.write("\n</html>\n");
		fileWriter.close();
	}
	private void createDirectory(File diry) {
		if (!diry.exists())
			diry.mkdirs();
	}
	public boolean makeFileFrom(String fileName, String html) throws IOException {
		String[] split = fileName.split("/");
		if (split.length > 1)
			throw new RuntimeException("fix");
		String fileName2 = split[split.length-1];
		writeFile(FITNESSE_DIFFERENCES.getLocalFile(driverName+"/"+fileName2).getFile(),html);
//		writeFile(new File("FitNesseRoot/files/"+fileName2), html);
		return true;
	}
	@Override
	public void shutDown() throws IOException {
		super.shutDown();
		webServer.stop();
	}
	@Override
	public void tearDown() throws Exception {
		shutDown();
		Thread.sleep(50);
	}
}
