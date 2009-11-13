/*
 * Copyright (c) 2009 Rick Mugridge, www.RimuResearch.com
 * Kindly donated by Air New Zealand in October 2009.
 * Released under the terms of the GNU General Public License version 2 or later.
*/
package fitlibrary.spider.driver;

import com.gargoylesoftware.htmlunit.TopLevelWindow;
import com.gargoylesoftware.htmlunit.WebClient;

import fitlibrary.spider.SpiderFixture;

public class HtmlUnitVariation extends DriverVariation {
	private WebClient webClient;

	public HtmlUnitVariation(SpiderFixture spiderFixture, WebClient webClient) {
		super(spiderFixture);
		this.webClient = webClient;
	}
	@Override
	public boolean close() {
		((TopLevelWindow)webClient.getCurrentWindow()).close();
		return true;
	}
}
