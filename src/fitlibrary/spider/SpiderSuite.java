/*
 * Copyright (c) 2009 Rick Mugridge, www.RimuResearch.com
 * Kindly donated by Air New Zealand in October 2009.
 * Released under the terms of the GNU General Public License version 2 or later.
*/
package fitlibrary.spider;

import java.io.IOException;

import fitlibrary.DefineAction;
import fitlibrary.suite.SuiteFixture;

public class SpiderSuite extends SuiteFixture {
	SpiderFixture spider;
	
	public boolean startSpiderWith(String driver) {
		spider = new SpiderFixture();
		setDynamicVariable("webDriver.driver",driver);
		return true;
	}
	public boolean startSpider() {
		spider = new SpiderFixture();
		return true;
	}
	public SpiderFixture spider() throws Exception {
		spider.restart();
		return spider;
	}
	public boolean shutDownSpider() throws IOException {
		spider.shutDown();
		return true;
	}
	public DefineAction fitlibraryDotDefineAction() { 
		return new DefineAction();
	}
}
