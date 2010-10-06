/*
 * Copyright (c) 2009 Rick Mugridge, www.RimuResearch.com
 * Kindly donated by Air New Zealand in October 2009.
 * Released under the terms of the GNU General Public License version 2 or later.
*/
package fitlibrary.spider.specify;

import fitlibrary.spider.SpiderFixture;
import fitlibrary.suite.SuiteFixture;

public class SpecifySpiderSuite extends SuiteFixture {
	private int portNo;
	
	public boolean startSpiderOnPortWith(int portNumber, String driver) {
		this.portNo = portNumber;
		setDynamicVariable(SpiderFixture.WEB_DRIVER_VARIABLE_NAME,driver);
		return true;
	}
	public SpecifySpiderFixture spider() throws Exception {
		return new SpecifySpiderFixture(portNo);
	}
}
