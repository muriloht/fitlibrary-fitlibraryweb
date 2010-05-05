/*
 * Copyright (c) 2009 Rick Mugridge, www.RimuResearch.com
 * Kindly donated by Air New Zealand in October 2009.
 * Released under the terms of the GNU General Public License version 2 or later.
*/
package fitlibrary.spider.specify;

import fitlibrary.spider.SpiderFixture;
import fitlibrary.suite.SuiteFixture;

public class SpecifySpiderSuite extends SuiteFixture {
	SpecifySpiderFixture spider;
	
	public boolean startSpiderOnPortWith(int portNo, String driver) {
		setDynamicVariable(SpiderFixture.WEB_DRIVER_VARIABLE_NAME,driver);
		spider = new SpecifySpiderFixture(portNo);
		return true;
	}
	public boolean startSpider() {
		spider = new SpecifySpiderFixture();
		return true;
	}
	public SpecifySpiderFixture spider() throws Exception {
		spider.restart();
		return new SpecifySpiderFixture();
	}
	public boolean shutDownSpider() {
		spider.shutDown();
		return true;
	}
}
