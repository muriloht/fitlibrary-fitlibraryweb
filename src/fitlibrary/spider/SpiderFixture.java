/*
 * Copyright (c) 2009 Rick Mugridge, www.RimuResearch.com
 * Kindly donated by Air New Zealand in October 2009.
 * Released under the terms of the GNU General Public License version 2 or later.
*/
package fitlibrary.spider;

import java.util.List;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.firefox.internal.ProfilesIni;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;

import com.gargoylesoftware.htmlunit.WebClient;

import fitlibrary.exception.FitLibraryException;
import fitlibrary.flex.FlexSpiderFixture;
import fitlibrary.spider.component.Frame;
import fitlibrary.spider.component.Page;
import fitlibrary.spider.component.SpiderWindow;
import fitlibrary.spider.component.TextInPage;
import fitlibrary.spider.driver.DriverVariation;
import fitlibrary.spider.driver.FirefoxVariation;
import fitlibrary.spider.driver.HtmlUnitVariation;
import fitlibrary.spider.driver.SeleniumVariation;
import fitlibrary.spider.selenium.SeleniumDriver;

public class SpiderFixture extends AbstractSpiderFixture {
	public static final String WEB_DRIVER_VARIABLE_NAME = "webDriver.driver";
	protected WebDriver webDriver = null;
	private SpiderWindow spiderWindow = new SpiderWindow(this);
	protected FirefoxProfile firefoxProfile = new FirefoxProfile();
	protected String proxyHost;
	protected int proxyPort;
	private boolean shutDownAutomatically = true;
	private Page page = new Page(this);
	private Frame frame = new Frame(this);
	private TextInPage textInPage = new TextInPage(this);

	public SpiderFixture() {
		setElementFinder(new WebDriverFinder());
		setSpiderFixture(this);
	}
	public SpiderWindow getWindow() {
		return spiderWindow;
	}
	public FlexSpiderFixture flex() {
		return new FlexSpiderFixture(webDriver());
	}
	public boolean startSpiderWith(String theDriver) {
		runtime().dynamicVariables().put("webDriver.driver", theDriver);
		return true;
	}
	public boolean useFirefoxProfile(String profileName) {
		firefoxProfile = new ProfilesIni().getProfile(profileName);
		if (firefoxProfile == null) {
			firefoxProfile = new FirefoxProfile();
			return false;
		}
		return true;
	}
	public void firefoxProfileAsString(String key, String value) {
		firefoxProfile.setPreference(key, value);
	}
	public void firefoxProfileAsInteger(String key, int value) {
		firefoxProfile.setPreference(key, value);
	}
	public void firefoxProfileAsBoolean(String key, boolean value) {
		firefoxProfile.setPreference(key, value);
	}
	// PROXY
	public void proxyWithPort(String host, int port) {
		this.proxyHost = host;
		this.proxyPort = port;
		// firefoxProfileAs("", host);
		// firefoxProfileAs("", port+"");
	}
	// RESTART
	public void restart() throws Exception {
		tearDownDriver();
	}
	public void restartWith(String driver) {
		Set<Cookie> cookies = webDriver().manage().getCookies();
		String currentUrl = webDriver().getCurrentUrl();
		webDriver().close();
		webDriver = null;
		startSpiderWith(driver);
		webDriver().get(currentUrl);
		for (Cookie cookie : cookies)
			webDriver().manage().addCookie(
					new Cookie(cookie.getName(), cookie.getValue()));
	}
	// Override to include a default driver or different drivers
	@Override
	public WebDriver webDriver() {
		if (webDriver == null) {
			String driver = getDynamicVariable(WEB_DRIVER_VARIABLE_NAME,
					"htmlunit").toString();
			if ("htmlunit".equals(driver))
				webDriver = htmlUnitDriver();
			else if ("selenium".equals(driver)) {
				SeleniumDriver seleniumDriver = seleniumDriver();
				webDriver = seleniumDriver;
				setSystemUnderTest(seleniumDriver.getSelenium());
			} else if ("firefox".equals(driver))
				webDriver = fireFoxDriver();
			else if ("ie".equals(driver))
				webDriver = ieDriver();
			// else if ("safari".equals(driver))
			// webDriver = safariDriver();
			else
				throw new FitLibraryException(
						"Need to specify property '"
								+ WEB_DRIVER_VARIABLE_NAME
								+ "' as 'htmlunit', 'firefox', 'selenium', 'safari', or 'ie'");
			spiderWindow.setInitialWindow();
		}
		return webDriver;
	}
	protected Object getDynamicVariable(String key, String byDefault) {
		Object dynamicVariable = getDynamicVariable(key);
		if (dynamicVariable != null)
			return dynamicVariable;
		return byDefault;
	}
	private WebDriver ieDriver() {
		return new InternetExplorerDriver();
	}
	// private WebDriver safariDriver() {
	// return new SafariDriver();
	// }
	public void shutdownBrowserAutomatically(
			boolean shutDownBrowserAutomatically) {
		this.shutDownAutomatically = shutDownBrowserAutomatically;
	}
	@Override
	public void tearDown() throws Exception {
		// System.out.println("SpiderFixture.tearDown()");
		super.tearDown();
		tearDownDriver();
	}
	public void tearDownDriver() {
		if (shutDownAutomatically && webDriver != null)
			webDriver.quit();
		webDriver = null;
	}
	// Override to configure differently
	protected SeleniumDriver seleniumDriver() {
		String host = "localhost";
		int portNo = 4444;
		String browserStartCommand = "*iehta";
		String browserURL = "http://localhost";
		SeleniumDriver seleniumDriver = new SeleniumDriver(host, portNo,
				browserStartCommand, browserURL);
		driverVariation = new SeleniumVariation(this, seleniumDriver);
		return seleniumDriver;
	}
	// Override to configure differently, such as with a Firefox profile
	protected FirefoxDriver fireFoxDriver() {
		FirefoxDriver firefoxDriver = new FirefoxDriver(firefoxProfile);
		driverVariation = new FirefoxVariation(this, firefoxDriver);
		return firefoxDriver;
	}
	// Override to configure htmlUnit in a different way
	protected HtmlUnitDriver htmlUnitDriver() {
		class ExtendedHtmlUnitDriver extends HtmlUnitDriver {
			@Override
			public WebClient getWebClient() {
				return super.getWebClient();
			}
		}
		ExtendedHtmlUnitDriver htmlUnitDriver = new ExtendedHtmlUnitDriver();
		if (proxyHost != null)
			htmlUnitDriver.setProxy(proxyHost, proxyPort);
		htmlUnitDriver.setJavascriptEnabled(true);
		driverVariation = new HtmlUnitVariation(this, htmlUnitDriver
				.getWebClient());
		return htmlUnitDriver;
	}
	class WebDriverFinder implements Finder {
		public WebElement findElement(String locator) {
			if (locator.startsWith("id="))
				return findElement(By.id(locator.substring(3)));
			if (locator.startsWith("xpath="))
				return findElement(By.xpath(locator.substring(6)));
			if (locator.startsWith("link="))
				return findElement(By.linkText(locator.substring(5)));
			if (locator.startsWith("name="))
				return findElement(By.name(locator.substring(5)));
			if (locator.startsWith("//"))
				return findElement(By.xpath(locator));
			return findElement(By.id(locator));
		}
		public WebElement findElement(By by) {
			return webDriver().findElement(by);
		}
		public List<WebElement> findElements(String locator) {
			return webDriver().findElements(By.xpath(locator));
		}
		public WebElement findOption(String locator, String option,
				AbstractSpiderFixture abstractSpiderFixture) {
			return abstractSpiderFixture.findElement(locator
					+ "/option[@value ='" + option.toUpperCase() + "']");
		}
	}
	protected boolean isFirefox() {
		return webDriver instanceof FirefoxDriver;
	}
	public ForEachFixture forEachIn(String iteratorName, List<String> list) {
		return new ForEachFixture(iteratorName, list);
	}
	public void clearDriver() {
		webDriver = null;
		showAfterTable("Cleared driver");
	}
	public DriverVariation getDriverVariation() {
		return driverVariation;
	}
	public Page getPage() {
		return page;
	}
	public Frame getFrame() {
		return frame;
	}
	public TextInPage getTextInPage() {
		return textInPage;
	}
}
