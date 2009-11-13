/*
 * Copyright (c) 2009 Rick Mugridge, www.RimuResearch.com
 * Kindly donated by Air New Zealand in October 2009.
 * Released under the terms of the GNU General Public License version 2 or later.
*/
package fitlibrary.spider.specify;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import fitlibrary.spider.SpiderFixture;
import fitlibrary.table.Row;
import fitlibrary.utility.StringUtility;
import fitlibrary.utility.TestResults;

public class SpecifySpiderFixture extends SpiderFixture {
	private String testFileName;
	private int portNo = 80;
	
	public SpecifySpiderFixture() {
		//
	}
	public SpecifySpiderFixture(int portNo) {
		this.portNo = portNo;
	}
	public boolean saveHtmlInForPort(String testingFileName, int port) {
		this.testFileName = testingFileName;
		this.portNo = port;
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
		writeFile(new File("FitNesseRoot/files/"+testFileName), s);
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
		writeFile(new File("FitNesseRoot/files/"+fileName2), html);
		return true;
	}
}
