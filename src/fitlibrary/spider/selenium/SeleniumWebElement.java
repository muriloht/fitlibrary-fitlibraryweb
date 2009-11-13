/*
 * Copyright (c) 2009 Rick Mugridge, www.RimuResearch.com
 * Kindly donated by Air New Zealand in October 2009.
 * Released under the terms of the GNU General Public License version 2 or later.
*/
package fitlibrary.spider.selenium;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.internal.FindsById;
import org.openqa.selenium.internal.FindsByLinkText;
import org.openqa.selenium.internal.FindsByName;
import org.openqa.selenium.internal.FindsByXPath;

import com.thoughtworks.selenium.Selenium;

import fitlibrary.exception.FitLibraryException;

public class SeleniumWebElement implements WebElement, SearchContext, FindsById, FindsByLinkText, 
						FindsByXPath, FindsByName {
	private final Selenium selenium;
	private final String locator;
	private SeleniumDriver driver;

	public SeleniumWebElement(SeleniumDriver driver, String locator) {
		selenium = driver.selenium();
		this.driver = driver;
		this.locator = locator;
	}
	public String getAttribute(String attributeName) {
		return selenium.getAttribute(locator+"@"+attributeName); // Not correct
	}
	public String getValue() {
		try {
			return selenium.getValue(locator);
		} catch (Exception e) {
			return "";
		}
	}
	public boolean isEnabled() {
		return selenium.isEditable(locator);
	}
	public boolean isSelected() {
		return selenium.isChecked(locator);
	}
	public void setSelected() {
		if (!selenium.isEditable(locator))
			return;
		selenium.check(locator);
	}
	public boolean toggle() {
		if (!selenium.isEditable(locator))
			return selenium.isChecked(locator);
		boolean checked = selenium.isChecked(locator);
		if (checked)
			selenium.uncheck(locator);
		else
			selenium.check(locator);
		return !checked;
	}
	public String getText() {
		try {
			return selenium.getText(locator).replaceAll("\r","");
		} catch (Exception e) {
			System.out.println("Exception: "+e);
			return "";
		}
	}
	public void clear() {
		if (!selenium.isEditable(locator))
			return;
		selenium.type(locator,"");
	}
	public void sendKeys(CharSequence... seq) {
		if (!selenium.isEditable(locator))
			return;
		String s = getText();
		if (s.equals(""))
			s = getValue();
		for (CharSequence c : seq)
			s += c.toString();
		selenium.type(locator,s);
	}
	public void click() {
		try {
			selenium.click(locator);
//			if (false && isLink()) {
//				selenium.waitForPageToLoad("1000");
//				driver.clearForwards();
//			}
		} catch (Exception e) {
			throw new FitLibraryException("Unknown link: "+e);
		}
	}
//	private boolean isLink() {
//		if (locatorByLink())
//			return true;
//		if (locatorByXPath())
//			return locator.contains("/a/") || locator.contains("/a[");
//		if (locatorByName())
//			return selenium.isElementPresent("//a[@name='"+locatorAsName()+"']");
//		if (locatorById())
//			return selenium.isElementPresent("//a[@id='"+locatorAsExplicitId()+"']");
//		return selenium.isElementPresent("//a[@id='"+locator+"']");
//	}
	public void submit() {
		selenium.submit(locator);
		selenium.waitForPageToLoad("1000");
		driver.clearForwards();
	}
	public WebElement findElement(By by) {
		return by.findElement(this);
	}
	public List<WebElement> findElements(By arg0) {
		notYetImplemented();
		return null;
	}
	public List<WebElement> getChildrenOfType(String childType) {
		ArrayList<WebElement> list = new ArrayList<WebElement>();
		int count = selenium.getXpathCount(locator+"//"+childType).intValue();
		for (int i = 0; i < count; i++)
			list.add(new SeleniumWebElement(driver,locator+"//"+childType+"["+(i+1)+"]"));
		return list;
	}
	public WebElement findElementById(String id) {
		return driver.findElementById(id);
	}
	public List<WebElement> findElementsById(String arg0) {
		notYetImplemented();
		return null;
	}
	public WebElement findElementByLinkText(String linkText) {
		return driver.findElementByLinkText(linkText);
	}
	public List<WebElement> findElementsByLinkText(String arg0) {
		notYetImplemented();
		return null;
	}
	public WebElement findElementByXPath(String xPath) {
//		System.out.println("findElementByXPath "+locator+" with "+xPath);
		String xPathWithoutDot = xPath.substring(1);
		String relativeLocator = "";
		if (locatorByXPath())
			relativeLocator = locator+xPathWithoutDot;
		else if (locatorByLink())
			notYetImplemented();
		else if (locatorByName())
			relativeLocator = "xpath=//*[@name='"+locatorAsName()+"']"+xPathWithoutDot;
		else if (locatorById())
			relativeLocator = "xpath=id('"+locatorAsExplicitId()+"')"+xPathWithoutDot;
		else
			relativeLocator = "xpath=id('"+locator+"')"+xPathWithoutDot;
		return new SeleniumWebElement(driver,relativeLocator);
	}
	public List<WebElement> findElementsByXPath(String arg0) {
		notYetImplemented();
		return null;
	}
	public WebElement findElementByName(String name) {
		return driver.findElementByName(name);
	}
	public List<WebElement> findElementsByName(String arg0) {
		notYetImplemented();
		return null;
	}
	protected void notYetImplemented() {
		throw new FitLibraryException("Not yet implemented in Selenium Driver");
	}
	public String getLocator() {
		return locator;
	}
	private String locatorAsExplicitId() {
		return locator.substring(3);
	}
	private String locatorAsName() {
		return locator.substring(5);
	}
	private boolean locatorById() {
		return locator.startsWith("id=");
	}
	private boolean locatorByName() {
		return locator.startsWith("name=");
	}
	private boolean locatorByXPath() {
		return locator.startsWith("//") || locator.startsWith("xpath=");
	}
	private boolean locatorByLink() {
		return locator.startsWith("link=");
	}
	public String getElementName() {
		notYetImplemented();
		return null;
	}
	public WebElement findElementByPartialLinkText(String arg0) {
		notYetImplemented();
		return null;
	}
	public List<WebElement> findElementsByPartialLinkText(String arg0) {
		notYetImplemented();
		return null;
	}
}
