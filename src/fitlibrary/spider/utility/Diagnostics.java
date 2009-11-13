/*
 * Copyright (c) 2009 Rick Mugridge, www.RimuResearch.com
 * Kindly donated by Air New Zealand in October 2009.
 * Released under the terms of the GNU General Public License version 2 or later.
*/
package fitlibrary.spider.utility;

import fit.Fixture;
import fitlibrary.exception.FitLibraryShowException;
import fitlibrary.spider.SpiderFixture;

public class Diagnostics {
	private final SpiderFixture spiderFixture;
	private String xpath;
	private String pattern;

	public Diagnostics(SpiderFixture spiderFixture) {
		this.spiderFixture = spiderFixture;
	}
	public void setUp(String xpath, String pattern) {
		this.xpath = xpath;
		this.pattern = pattern;
	}
	public void checkForShow() {
		if (xpath == null || pattern == null || !spiderFixture.pageContainsRegularExpression(pattern))
			return;
		String show = "";
		try {
			show = spiderFixture.textOf(xpath);
		} catch (Exception e) {
			//
		}
		if (!"".equals(show))
			throw FitLibraryShowException.show(show);
		showAll();
	}
	private void showAll() {
		throw FitLibraryShowException.show(Fixture.escape(spiderFixture.pageSource()));
	}
}
