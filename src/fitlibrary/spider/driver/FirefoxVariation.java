/*
 * Copyright (c) 2009 Rick Mugridge, www.RimuResearch.com
 * Kindly donated by Air New Zealand in October 2009.
 * Released under the terms of the GNU General Public License version 2 or later.
*/
package fitlibrary.spider.driver;

import fitlibrary.spider.AbstractSpiderFixture;
import fitlibrary.spider.polling.PollForNoException;

public class FirefoxVariation extends DriverVariation {
	public FirefoxVariation(AbstractSpiderFixture spiderFixture) {
		super(spiderFixture);
	}
	@Override
	public void checkTitleOfNewPage(final String url) throws Exception {
		spiderFixture.ensureNoException(new PollForNoException<Boolean>() {
			@Override
			public Boolean act() {
				if (spiderFixture.getTitle() != null)
					return true;
				throw spiderFixture.problem("Unable to access",url);
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
}
