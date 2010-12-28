/*
 * Copyright (c) 2009 Rick Mugridge, www.RimuResearch.com
 * Kindly donated by Air New Zealand in October 2009.
 * Released under the terms of the GNU General Public License version 2 or later.
*/
/*
 * NOTE: This is incomplete. 
 * There is a WebDriver implementation of this in progress in October 2009, which will likely replace this code here.
 */
package fitlibrary.spider.selenium;

import java.net.URL;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.Speed;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.internal.FindsById;
import org.openqa.selenium.internal.FindsByLinkText;
import org.openqa.selenium.internal.FindsByName;
import org.openqa.selenium.internal.FindsByXPath;

import com.thoughtworks.selenium.DefaultSelenium;
import com.thoughtworks.selenium.Selenium;

import fitlibrary.exception.FitLibraryException;

public class SeleniumDriver implements WebDriver, FindsById, FindsByLinkText, 
								FindsByXPath, FindsByName {
	protected Selenium selenium;
	protected Stack<String> forwards = new Stack<String>();
	
	public SeleniumDriver(String serverHost, int serverPort, String browserStartCommand, String browserURL) {
		selenium = new DefaultSelenium(serverHost,serverPort,browserStartCommand,browserURL);
		selenium.start();
	}
	public Selenium getSelenium() {
		return selenium;
	}
	@Override
	public void get(String url) {
		selenium.open(url);
		clearForwards();
	}
	public void clearForwards() {
		forwards.clear();
	}
	@Override
	public String getCurrentUrl() {
		return selenium.getLocation();
	}
	@Override
	public String getPageSource() {
		return selenium.getHtmlSource();
	}
	@Override
	public String getTitle() {
		return selenium.getTitle();
	}
	public boolean getVisible() {
		notYetImplemented();
		return false;
	}
	@SuppressWarnings("unused")
	public void setVisible(boolean visible) {
		notYetImplemented();
	}
	@Override
	public void close() {
		selenium.close();
	}
	@Override
	public Options manage() {
		return new Options() {
			@Override
			public void addCookie(Cookie cookie) {
				String options = "path="+cookie.getPath();
				if (cookie.getDomain() != null)
					options += ", domain="+cookie.getDomain();
				selenium.createCookie(cookie.getName()+"="+cookie.getValue(),options);
			}
			@Override
			public void deleteAllCookies() {
				notYetImplemented();
			}
			@Override
			public void deleteCookie(Cookie cookie) {
				selenium.deleteCookie(cookie.getName(), cookie.getPath());
			}
			@Override
			public void deleteCookieNamed(String name) {
				notYetImplemented();
				// The following doesn't work:
				selenium.deleteCookie(name,"path=/");
			}
			@Override
			public Set<Cookie> getCookies() {
				String cookies = selenium.getCookie();
				Set<Cookie> set = new HashSet<Cookie>();
				for (String s: cookies.split(";")) {
					String[] keyValue = s.split("=");
					if (keyValue.length == 2)
						set.add(new Cookie(keyValue[0].trim(),keyValue[1].trim(),"",null));
				}
				return set;
			}
			@Override
			public Speed getSpeed() {
				return Speed.SLOW; // Could decode the timeout speed in SLOW, etc
			}
			@Override
			public void setSpeed(Speed speed) {
				selenium.setSpeed(""+speed.getTimeOut());
			}
			@Override
			public Cookie getCookieNamed(String name) {
				notYetImplemented();
				return null;
			}
			@Override
			public Timeouts timeouts() {
				notYetImplemented();
				return null;
			}
		};
	}
	@Override
	public Navigation navigate() {
		return new Navigation() {
			@Override
			public void back() {
				forwards.push(getCurrentUrl());
				selenium.goBack();
			}
			@Override
			public void forward() {
				if (forwards.isEmpty())
					throw new FitLibraryException("Can't go forward as haven't gone back");
				selenium.open(forwards.pop());
			}
			@Override
			public void to(String url) {
				selenium.open(url);
			}
			@Override
			public void to(URL url) {
				to(url.toString()); // Assume that will do the trick - 685 introduces this
			}
			@Override
			public void refresh() {
				selenium.refresh();
			}
		};
	}
	@Override
	public void quit() {
		selenium.stop();
	}
	@Override
	public TargetLocator switchTo() {
		return new TargetLocator() {
			@Override
			public WebElement activeElement() {
				notYetImplemented();
				return null;
			}
			@Override
			public WebDriver defaultContent() {
				selenium.selectWindow(null);
				return SeleniumDriver.this;
			}
			@Override
			public WebDriver frame(int frameNumber) {
				selenium.selectFrame("index="+frameNumber);
				return SeleniumDriver.this;
			}
			@Override
			public WebDriver frame(String frameName) {
				try {
					selenium.selectFrame("//frame[@name=\""+frameName+"\"]");
					return SeleniumDriver.this;
				} catch (Exception e) {
					throw new FitLibraryException(""+e);
				}
			}
			@Override
			public WebDriver window(String windowName) {
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					//
				}
				selenium.openWindow("",windowName);
				System.out.println("Windows: "+Arrays.asList(selenium.getAllWindowNames()));
				selenium.waitForPopUp(windowName,"100");
				selenium.selectWindow(windowName);
				return SeleniumDriver.this;
			}
			@Override
			public WebDriver frame(WebElement frameElement) {
				notYetImplemented();
				return null;
			}
			@Override
			public Alert alert() {
				notYetImplemented();
				return null;
			}
		};
	}
	@Override
	public WebElement findElement(By by) {
		return by.findElement(this);
	}
	@Override
	public List<WebElement> findElements(By by) {
		return by.findElements(this);
	}
	@Override
	public WebElement findElementById(String id) {
		return new SeleniumWebElement(this,id);
	}
	@Override
	public List<WebElement> findElementsById(String arg0) {
		notYetImplemented();
		return null;
	}
	@Override
	public WebElement findElementByLinkText(String linkText) {
		return new SeleniumWebElement(this,"link="+linkText);
	}
	@Override
	public List<WebElement> findElementsByLinkText(String linkText) {
		notYetImplemented();
		return null;
	}
	@Override
	public WebElement findElementByXPath(String xPath) {
		return new SeleniumWebElement(this,"xpath="+xPath);
	}
	@Override
	public List<WebElement> findElementsByXPath(String xPath) {
		notYetImplemented();
		return null;
	}
	@Override
	public WebElement findElementByName(String name) {
		return new SeleniumWebElement(this,"name="+name);
	}
	@Override
	public List<WebElement> findElementsByName(String arg0) {
		notYetImplemented();
		return null;
	}
	protected void notYetImplemented() {
		throw new FitLibraryException("Not yet implemented in Selenium Driver");
	}
	public Selenium selenium() {
		return selenium;
	}
	@Override
	public String getWindowHandle() {
		notYetImplemented();
		return null;
	}
	@Override
	public Set<String> getWindowHandles() {
		notYetImplemented();
		return null;
	}
	@Override
	public WebElement findElementByPartialLinkText(String arg0) {
		notYetImplemented();
		return null;
	}
	@Override
	public List<WebElement> findElementsByPartialLinkText(String arg0) {
		notYetImplemented();
		return null;
	}
}
