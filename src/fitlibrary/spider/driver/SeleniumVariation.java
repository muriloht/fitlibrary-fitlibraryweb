/*
 * Copyright (c) 2009 Rick Mugridge, www.RimuResearch.com
 * Kindly donated by Air New Zealand in October 2009.
 * Released under the terms of the GNU General Public License version 2 or later.
*/
package fitlibrary.spider.driver;

import fitlibrary.differences.LocalFile;
import fitlibrary.spider.SpiderFixture;
import fitlibrary.spider.selenium.SeleniumDriver;

public class SeleniumVariation extends DriverVariation {
	private SeleniumDriver seleniumDriver;
	
	public SeleniumVariation(SpiderFixture spiderFixture, SeleniumDriver seleniumDriver) {
		super(spiderFixture);
		this.seleniumDriver = seleniumDriver;
	}
	@Override
	public void screenDump() {
		LocalFile file = pngFile();
		seleniumDriver.getSelenium().captureScreenshot(file.getFile().getAbsolutePath());
		spiderFixture.showAfterTable(file.htmlImageLink());
	}
}
