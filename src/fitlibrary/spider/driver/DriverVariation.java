/*
 * Copyright (c) 2009 Rick Mugridge, www.RimuResearch.com
 * Kindly donated by Air New Zealand in October 2009.
 * Released under the terms of the GNU General Public License version 2 or later.
*/
package fitlibrary.spider.driver;

import java.io.IOException;
import java.util.Random;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import fitlibrary.differences.LocalFile;
import fitlibrary.exception.FitLibraryException;
import fitlibrary.spider.AbstractSpiderFixture;
import fitlibrary.traverse.Traverse;

public class DriverVariation {
	protected static Random RANDOM = new Random(System.currentTimeMillis());
	protected final AbstractSpiderFixture spiderFixture;
	private String screenDumpDirectoryName = "images";

	public DriverVariation(AbstractSpiderFixture spiderFixture) {
		this.spiderFixture = spiderFixture;
	}
	public boolean close() {
		spiderFixture.webDriver().close();
		return true;
	}
	public void checkTitleOfNewPage(String url) throws Exception {
		if (spiderFixture.getTitle() == null)
			throw spiderFixture.problem("Unable to access",url);
	}
	protected LocalFile pngFile() {
		return pngFile("screenDump"+RANDOM.nextInt());
	}
	protected LocalFile pngFile(String fileName) {
		LocalFile globalFile = Traverse.getGlobalFile(screenDumpDirectoryName+"/"+fileName+".png");
		globalFile.mkdirs();
		return globalFile;
	}
	public void setScreenDumpDirectory(String diryName) {
		this.screenDumpDirectoryName  = diryName;
	}
	public void screenDump() {
		LocalFile file = pngFile();
		saveScreenDumpBytes(file);
		spiderFixture.showAfterTable(file.htmlImageLink());
	}
	public void screenDump(String fileName) {
		LocalFile file = pngFile(fileName);
		saveScreenDumpBytes(file);
		spiderFixture.showAfterTable(file.htmlImageLink());
	}
	private void saveScreenDumpBytes(LocalFile file) {
		byte[] bytes = ((TakesScreenshot) spiderFixture.webDriver())
				.getScreenshotAs(OutputType.BYTES);
		try {
			FileUtils.writeByteArrayToFile(file.getFile(), bytes);
		} catch (IOException e) {
			throw new FitLibraryException(e);
		}
	}
}
