/*
 * Copyright (c) 2009 Rick Mugridge, www.RimuResearch.com
 * Kindly donated by Air New Zealand in October 2009.
 * Released under the terms of the GNU General Public License version 2 or later.
*/
package fitlibrary.spider;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public interface Finder {
	WebElement findElement(String locator);
	WebElement findElement(By linkText);
	List<WebElement> findElements(String locator);
	WebElement findOption(String locator, String option, AbstractSpiderFixture abstractSpiderFixture);
}
