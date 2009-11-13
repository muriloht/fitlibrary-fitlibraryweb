/*
 * Copyright (c) 2009 Rick Mugridge, www.RimuResearch.com
 * Kindly donated by Air New Zealand in October 2009.
 * Released under the terms of the GNU General Public License version 2 or later.
*/
package fitlibrary.spider;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class SpiderElementFixture extends AbstractSpiderFixture {
	protected WebElement webElement;
	protected SpiderFixture spiderFixture;

	public SpiderElementFixture(WebElement webElement, SpiderFixture spiderFixture) {
		this.webElement = webElement;
		this.spiderFixture = spiderFixture;
		setElementFinder(new ElementFinder());
		setSpiderFixture(spiderFixture);
	}
	class ElementFinder implements Finder {
		public WebElement findElement(String locator) {
			return findElement(by(locator));
		}
		public WebElement findElement(By by) {
			return webElement.findElement(by);
		}
		public List<WebElement> findElements(String locator) {
			return webElement.findElements(by(locator));
		}
		public WebElement findOption(String locator, String option,
				AbstractSpiderFixture abstractSpiderFixture)
		{
			return abstractSpiderFixture.findElement(locator
					+ "/option[@value ='" + option.toUpperCase() + "']");
		}
		private By by(String givenLocator) {
			String locator = givenLocator;
			if (locator.startsWith("//"))
				locator = "."+locator;
			return By.xpath(locator);
		}
	}
}
