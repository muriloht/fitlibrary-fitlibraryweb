/*
 * Copyright (c) 2009 Rick Mugridge, www.RimuResearch.com
 * Kindly donated by Air New Zealand in October 2009.
 * Released under the terms of the GNU General Public License version 2 or later.
*/
package fitlibrary.spider;

import java.io.File;
import java.util.List;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.firefox.internal.ProfilesIni;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

import com.gargoylesoftware.htmlunit.WebClient;

import fitlibrary.annotation.ShowSelectedActions;
import fitlibrary.annotation.SimpleAction;
import fitlibrary.exception.FitLibraryException;
import fitlibrary.flex.FlexSpiderFixture;
import fitlibrary.spider.component.Alert;
import fitlibrary.spider.component.Frame;
import fitlibrary.spider.component.Page;
import fitlibrary.spider.component.SpiderWindow;
import fitlibrary.spider.component.TextInPage;
import fitlibrary.spider.driver.DriverVariation;
import fitlibrary.spider.driver.FirefoxVariation;
import fitlibrary.spider.driver.HtmlUnitVariation;

@ShowSelectedActions
public class SpiderFixture extends AbstractSpiderFixture {
	public static final String WEB_DRIVER_VARIABLE_NAME = "webDriver.driver";
	protected WebDriver webDriver = null;
	private ChromeOptions chromeOptions;
	DesiredCapabilities capabilities = new DesiredCapabilities();
	private SpiderWindow spiderWindow = new SpiderWindow(this);
	protected FirefoxProfile firefoxProfile = new FirefoxProfile();
	protected Proxy proxy;
	private boolean shutDownAutomatically = true;
	private Page page = new Page(this);
	private Frame frame = new Frame(this);
	private Alert alert = new Alert(this);
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
	@SimpleAction(wiki="|''<i>start spider with</i>''|browser name|",
			tooltip="Select the browser as one of htmlunit, firefox, or ie.")
	public boolean startSpiderWith(String theDriver) {
		setDynamicVariable("webDriver.driver", theDriver);
		return true;
	}
	@SimpleAction(wiki="|''<i>use firefox profile</i>''|profile name|",
			tooltip="When running with firefox, select the profile to use.")
	public boolean useFirefoxProfile(String profileName) {
		firefoxProfile = new ProfilesIni().getProfile(profileName);
		if (firefoxProfile == null) {
			firefoxProfile = new FirefoxProfile();
			return false;
		}
		return true;
	}
	
	@SimpleAction(wiki="|''<i>use chrome options</i>''|extensionsPath|",
			tooltip="When running with chrome, configure using extensions crx file at the given path")
	public void useChromeOptions(String extensionsPath)
	{
		chromeOptions = new ChromeOptions();
		chromeOptions.addExtensions(new File(extensionsPath));
	}
	
	@SimpleAction(wiki="|''<i>use internet explorer legacy internal server for ie</i>''|boolean value|",
			tooltip="set to true to switch off warnings if you don't have the IEDriverSet on your path or at webdriver.ie.driver system property location.")
	public void useInternetExplorerLegacyInternalServer(boolean useLegacyServer) {
        capabilities.setCapability("useLegacyInternalServer", useLegacyServer);
    }
	@SimpleAction(wiki="|''<i>use native events</i>''|boolean value|",
			tooltip="Switch native events mode on / off - currently only available in firefox.")
	public void useNativeEventsInFirefox(boolean enableNativeEvents) {
       // firefoxProfile.setEnableNativeEvents(enableNativeEvents);
    }
	@SimpleAction(wiki="|''<i>firefox profile</i>''|key|''<i>as string</i>''|string value|",
			tooltip="Set the value of a string-based firefox profile key.")
	public void firefoxProfileAsString(String key, String value) {
		firefoxProfile.setPreference(key, value);
	}
	@SimpleAction(wiki="|''<i>firefox profile</i>''|key|''<i>as integer</i>''|integer value|",
			tooltip="Set the value of a integer-based firefox profile key.")
	public void firefoxProfileAsInteger(String key, int value) {
		firefoxProfile.setPreference(key, value);
	}
	@SimpleAction(wiki="|''<i>firefox profile</i>''|key|''<i>as boolean</i>''|boolean value|",
			tooltip="Set the value of a boolean-based firefox profile key.")
	public void firefoxProfileAsBoolean(String key, boolean value) {
		firefoxProfile.setPreference(key, value);
	}
	// PROXY
	@SimpleAction(wiki="|''<i>proxy</i>''|host|''<i>with port</i>''|port number|",
			tooltip="Set the host and port of the proxy server to use.")
	public void proxyWithPort(String host, int port) {
    	String proxystr = host+":"+port;
    	this.proxy = new Proxy();
    	proxy.setHttpProxy(proxystr).setFtpProxy(proxystr).setSslProxy(proxystr);
	}
	
	@SimpleAction(wiki="|''<i>autoconfigration url</i>''|url|",
			tooltip="Configure proxy via autoconfig url i.e. pac file")
	public void proxyWithAutoconfigUrl(String url) {
    	this.proxy = new Proxy();
    	proxy.setProxyAutoconfigUrl(url);
	}
	
	// RESTART
	public void restart() throws Exception {
		tearDownDriver();
	}
	@SimpleAction(wiki="|''<i>restart with</i>''|browser|",
			tooltip="Start spider afresh on the current page with the same cookies, using the selected browser.\nThe browser is one of htmlunit, firefox, ie.")
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
	@Override
	public WebDriver webDriver() {
		if (webDriver == null) {
			String driver = getDynamicVariable(WEB_DRIVER_VARIABLE_NAME,"htmlunit").toString();
			
			if ("htmlunit".equals(driver))
				webDriver = htmlUnitDriver();
			else if ("firefox".equals(driver))
				webDriver = fireFoxDriver();
			else if ("ie".equals(driver))
				webDriver = internetExplorerDriver();
			else if ("chrome".equals(driver))
				webDriver = chromeDriver();
			else if ("phantomjs".equals(driver))
				webDriver = phantomJSDriver();
			else // override this in your base class to create your own driver
				webDriver = unknownDriver(driver);
			// else if ("safari".equals(driver))
			// webDriver = safariDriver();
			
			if (webDriver == null) 
				throw new FitLibraryException("Need to specify property '"
								+ WEB_DRIVER_VARIABLE_NAME + "' as 'htmlunit', 'firefox', 'ie' or 'chrome'");  
			
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
	// private WebDriver safariDriver() {
	// return new SafariDriver();
	// }
	@SimpleAction(wiki="|''<i>shut down browser automatically</i>''|true or false|",
			tooltip="" +
					"When true, the browser shuts down automatically at the end of the storytest.\nTrue by default.\nMake this false is you want to view the browser after the storytest finishes.")
	public void shutdownBrowserAutomatically(
			boolean shutDownBrowserAutomatically) {
		this.shutDownAutomatically = shutDownBrowserAutomatically;
	}
	public void tearDown() throws Exception {
		tearDownDriver();
	}
	public void tearDownDriver() {
		if (shutDownAutomatically && webDriver != null)
			webDriver.quit();
		webDriver = null;
	}
	// Override to configure differently, such as with a Firefox profile
	protected FirefoxDriver fireFoxDriver() {
		FirefoxDriver firefoxDriver = new FirefoxDriver();
		driverVariation = new FirefoxVariation(this);
		return firefoxDriver;
	}
	// Override to configure htmlUnit in a different way
	protected HtmlUnitDriver htmlUnitDriver() {
		
		HtmlUnitDriver htmlUnitDriver = new HtmlUnitDriver();
		if (proxy != null)
			htmlUnitDriver.setProxySettings(proxy);
		htmlUnitDriver.setJavascriptEnabled(true);
		driverVariation = new HtmlUnitVariation(this, new WebClient());
		return htmlUnitDriver;
	}
	protected InternetExplorerDriver internetExplorerDriver() {
		return new InternetExplorerDriver(capabilities);
	}
	
	public ChromeDriver chromeDriver() {
		DesiredCapabilities caps = DesiredCapabilities.chrome();
		
		if (proxy != null) {
			caps.setCapability(CapabilityType.PROXY, proxy);
		}
		
		if (chromeOptions != null)
		{
			caps.setCapability(ChromeOptions.CAPABILITY, chromeOptions);
		}
		
		return new ChromeDriver(caps);
	}
	
	protected WebDriver phantomJSDriver() {
	    DesiredCapabilities caps = DesiredCapabilities.phantomjs();
	    caps.setJavascriptEnabled(true);               
	    caps.setCapability("takesScreenshot", true);   
	
	    if (proxy != null) 
	    	caps.setCapability(CapabilityType.PROXY, proxy);
	    
	    // driver.manage().window().setSize(new Dimension(1280, 1024)); 
	    return new PhantomJSDriver(caps);
	}
	
	/** Override this in a base class to create a driver of your own */
	protected WebDriver unknownDriver(String driverName) {
		return null;
	}
	class WebDriverFinder implements Finder {
		@Override
		public WebElement findElement(String locator) {
			if (locator.startsWith("id="))
				return findElement(By.id(locator.substring(3)));
			if (locator.startsWith("xpath="))
				return findElement(By.xpath(locator.substring(6)));
			if (locator.startsWith("link="))
				return findElement(By.linkText(locator.substring(5)));
			if (locator.startsWith("name="))
				return findElement(By.name(locator.substring(5)));
			if (locator.startsWith("css="))
				return findElement(By.cssSelector(locator.substring(4)));
			if (locator.startsWith("class=")) 
				return findElement(By.className(locator.substring(6)));
			if (locator.startsWith("//") || locator.startsWith("(//")) 
				return findElement(By.xpath(locator));
			
			return findElement(By.id(locator));
		}
		@Override
		public WebElement findElement(By by) {
			return webDriver().findElement(by);
		}
		@Override
		public List<WebElement> findElements(String locator) {
			return webDriver().findElements(By.xpath(locator));
		}
		@Override
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
	public ForEachFixture forEachBetweenAnd(String iteratorName, int seed, int limit){
	  return new ForEachFixture(iteratorName, seed, limit);
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
	public Alert getAlert() {
		return alert;
	}
	public TextInPage getTextInPage() {
		return textInPage;
	}
	@Override
	public int getTimeout(String name) {
		return super.getTimeout(name);
	}
}
