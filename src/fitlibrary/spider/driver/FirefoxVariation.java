/*
 * Copyright (c) 2009 Rick Mugridge, www.RimuResearch.com
 * Kindly donated by Air New Zealand in October 2009.
 * Released under the terms of the GNU General Public License version 2 or later.
*/
package fitlibrary.spider.driver;

import org.openqa.selenium.firefox.FirefoxDriver;

import fitlibrary.differences.LocalFile;
import fitlibrary.spider.AbstractSpiderFixture;
import fitlibrary.spider.polling.PollForNoException;

public class FirefoxVariation extends DriverVariation {
	private final FirefoxDriver firefoxDriver;
	
	public FirefoxVariation(AbstractSpiderFixture spiderFixture, FirefoxDriver firefoxDriver) {
		super(spiderFixture);
		this.firefoxDriver = firefoxDriver;
	}
	@Override
	public void checkTitleOfNewPage(final String url) throws Exception {
		spiderFixture.ensureNoException(new PollForNoException<Boolean>() {
			public Boolean act() {
				if (spiderFixture.getTitle() != null)
					return true;
				throw spiderFixture.problem("Unable to access",url,url);
			}
		});
	}
	@Override
	public boolean close() {
		boolean result = super.close();
		try {
			spiderFixture.selectInitialWindow();
			Thread.sleep(200);
		} catch (Exception e) {
			// ignore error if there is no initial window
		}
		return result;
	}
	@Override
	public void screenDump() {
		LocalFile file = pngFile();
		firefoxDriver.saveScreenshot(file.getFile());
		spiderFixture.showAfterTable(file.htmlImageLink());
	}
	@Override
	public void screenDump(String fileName) {
		LocalFile file = pngFile(fileName);
		firefoxDriver.saveScreenshot(file.getFile());
		spiderFixture.showAfterTable(file.htmlImageLink());
	}
}
