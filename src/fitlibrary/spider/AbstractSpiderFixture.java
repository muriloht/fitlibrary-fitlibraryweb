/*
 * Copyright (c) 2009 Rick Mugridge, www.RimuResearch.com
 * Kindly donated by Air New Zealand in October 2009.
 * Released under the terms of the GNU General Public License version 2 or later.
*/
package fitlibrary.spider;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.RenderedWebElement;
import org.openqa.selenium.Speed;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.thoughtworks.selenium.Selenium;

import fit.Parse;
import fitlibrary.exception.FitLibraryException;
import fitlibrary.runResults.TestResults;
import fitlibrary.spider.component.Frame;
import fitlibrary.spider.component.Page;
import fitlibrary.spider.component.SpiderWindow;
import fitlibrary.spider.component.TextInPage;
import fitlibrary.spider.driver.DriverVariation;
import fitlibrary.spider.element.ElementWithAttributes;
import fitlibrary.spider.element.LinkAndFormElement;
import fitlibrary.spider.element.SelectElement;
import fitlibrary.spider.element.TextElement;
import fitlibrary.spider.polling.Poll;
import fitlibrary.spider.polling.PollForMatches;
import fitlibrary.spider.polling.PollForNoException;
import fitlibrary.spider.polling.PollForWithError;
import fitlibrary.spider.selenium.SeleniumWebElement;
import fitlibrary.spider.utility.HtmlTextUtility;
import fitlibrary.spider.utility.WebElementSelector;
import fitlibrary.table.Row;
import fitlibrary.traverse.workflow.DoTraverse;

public abstract class AbstractSpiderFixture extends DoTraverse {
	private static final String CHECKING_TIMEOUT = "checking";
	protected Finder finder;
	private SpiderFixture spiderFixture;
	private boolean checking = true;
	protected DriverVariation driverVariation = new DriverVariation(this);
	private TextElement textElement = new TextElement(this);
	private ElementWithAttributes elementWithAttributes = new ElementWithAttributes(this);
	private SelectElement selectElement = new SelectElement(this);
	private LinkAndFormElement linkAndFormElement = new LinkAndFormElement(this);
	
	public SpiderFixture spiderFixture() {
		return spiderFixture;
	}
	public void pollUrlTimeout(int timeout) {
		putTimeout(Page.POLL_URL_TIMEOUT, timeout);
	}
	public Finder getFinder() {
		return finder;
	}
	public void setElementFinder(Finder finder) {
		this.finder = finder;
	}
	public void setSpiderFixture(SpiderFixture spiderFixture) {
		this.spiderFixture = spiderFixture;
	}
	public WebDriver webDriver() {
		return spiderFixture.webDriver();
	}
	public void setChecking(boolean checking) {
		this.checking = checking;
	}
	public void checkingTimeout(int timeout) {
		putTimeout(CHECKING_TIMEOUT, timeout);
	}
	
	// --------- SHUT DOWN: ---------
	public void shutDown() {
		spiderFixture.tearDownDriver();
	}
	public void shutDownWithScreenDumpOnFailure() {
		if (getRuntimeContext().getTestResults().problems())
			screenDump();
		spiderFixture.tearDownDriver();
	}
	
	// --------- JAVASCRIPT execution: ---------
	public Object executeJavaScript(String s) {
		JavascriptExecutor js = (JavascriptExecutor) webDriver();
		return js.executeScript(s);
	}
	public boolean executeJavaScriptExpecting(String s, boolean expected) {
		try {
			JavascriptExecutor js = (JavascriptExecutor) webDriver();
			js.executeScript(s);
			return expected == true;
		} catch (Exception e) {
			return expected == false;
		}
	}
	// The following can pass out an element, such as:
	// executeJavaScriptWith("document.drawSquare(argument[0])",element);
	// Also see TextElement.innherHtmlOf()
	public Object executeJavaScriptWith(String script, Object param) {
		JavascriptExecutor js = (JavascriptExecutor) webDriver();
		return js.executeScript(script, param);
	}
	// --------- PAGE MANAGEMENT: ---------
	public boolean getUrl(final String url) throws Exception {
		return page().getUrl(url);
	}
	public boolean back() {
		return page().back();
	}
	public boolean forward() {
		return page().forward();
	}
	public String getTitle() {
		return page().getTitle();
	}
	public String getUrl() {
		return page().getUrl();
	}
	public void pollUrl(final Row row, final TestResults testResults) throws Exception {
		page().pollUrl(row, testResults);
	}
	public void refreshPage() {
		page().refresh();
	}
	// --------- DIAGNOSTICS: ---------
	public void showErrorDiagnosticsAtWhenPageContains(String xpath, String pattern) {
		page().showErrorDiagnosticsAtWhenPageContains(xpath, pattern);
	}
	public void checkForError() {
		page().checkForError();
	}
	private Page page() {
		return spiderFixture.getPage();
	}
	
	// --------- WINDOW ACCESS: ---------
	public String currentWindow() {
		return window().currentWindow();
	}
	public String windows() {
		return window().windows();
	}
	public boolean selectInitialWindow() {
		return window().selectInitialWindow();
	}
	public boolean selectWindow(String name) {
		return window().selectWindow(name);
	}
	public boolean selectWindowWithAs(String xpath, String value) {
		return window().selectWindowWithAs(xpath, value);
	}
	public boolean selectOtherWindow() {
		return window().selectOtherWindow();
	}
	public boolean close() {
		return window().close();
	}
	private SpiderWindow window() {
		return spiderFixture.getWindow();
	}
	
	// --------- FRAME ACCESS ---------
	public boolean frame(final int frameNo) {
		return frame().frame(frameNo);
	}
	public boolean frameByName(final String frameName) {
		return frame().frameByName(frameName);
	}
	public boolean defaultFrame() {
		return frame().defaultFrame();
	}
	private Frame frame() {
		return spiderFixture.getFrame();
	}
	
	// --------- TEXT ANYWHERE IN PAGE: ---------
	public boolean pageContainsText(final String text) {
		return textInPage().pageContainsText(text);
	}
	public boolean pageContainsTextIgnoreHtmlBreakingTokens(final String text) {
		return textInPage().pageContainsTextIgnoreHtmlBreakingTokens(text);
	}
	public boolean pageContainsRegularExpression(final String regEx) {
		return textInPage().pageContainsRegularExpression(regEx);
	}
	public String pageSubstringFromTo(String prior, String subsequent) {
		return textInPage().pageSubstringFromTo(prior, subsequent);
	}
	public String pageSource() {
		return textInPage().pageSource();
	}
	public String pageSourceCleaned() {
		return textInPage().pageSourceCleaned();
	}
	private TextInPage textInPage() {
		return spiderFixture.getTextInPage();
	}
	
	// --------- INPUT TEXT & TEXT AREA: ---------
	public String textOf(String locator) {
		return textElement.textOf(locator);
	}
	public String plainTextOf(String locator) {
		return textElement.plainTextOf(locator);
	}
	public MultiLineMatchFixture textOfMatchesLines(String locator) {
		return textElement.textOfMatchesLines(locator);
	}
	public boolean optionallyWithSetText(String locator, String s) {
		return textElement.optionallyWithSetText(locator, s);
	}
	public boolean withSetText(final String locator, final String s) {
		return textElement.withSetText(locator, s);
	}
	public boolean withAddText(final String locator, final String s) {
		return textElement.withAddText(locator, s);
	}
	public String innerHtmlOf(String locator) {
		return textElement.innerHtmlOf(locator);
	}
	// --------- GENERAL ELEMENTS: EXISTENCE, ATTRIBUTES, VALUES, TEXT, COUNT ---------
	public boolean elementExists(String locator) {
		return elementWithAttributes.elementExists(locator);
	}
	public boolean elementDoesNotExist(String locator) {
		return elementWithAttributes.elementDoesNotExist(locator);
	}
	public String elementValue(String locator) {
		return elementWithAttributes.elementValue(locator);
	}
	public String attributeOf(String attributeName, String locator) {
		return elementWithAttributes.attributeOf(attributeName, locator);
	}
	public boolean attributeOfExists(String attributeName, String locator) {
		return elementWithAttributes.attributeOfExists(attributeName, locator);
	}
	public String withCssPropertyOf(String locator, String property) {
		return elementWithAttributes.withCssPropertyOf(locator, property);
	}
	public int countOf(String locator) {
		return elementWithAttributes.countOf(locator);
	}
	public List<String> attributeOfChildrenOfTypeOf(String attribute,
			String childType, String locator) {
		return elementWithAttributes.attributeOfChildrenOfTypeOf(attribute, childType, locator);
	}
	public WebElementSelector findElementFromWithTagWhere(String locator, String tag) {
		return elementWithAttributes.findElementFromWithTagWhere(locator, tag);
	}
	public boolean elementVisible(String locator) {
		return ((RenderedWebElement)findElement(locator)).isDisplayed();
	}
	public boolean elementInvisible(String locator) {
		return !((RenderedWebElement)findElement(locator)).isDisplayed();
	}
	
	// --------- CHECKBOX ---------
	public boolean checkbox(String locator) {
		return findElement(locator).isSelected();
	}
	public boolean withSelect(String locator, final boolean select) {
		final WebElement element = findElement(locator);
		if (element.isSelected()) {
			if (!select) {
				element.click();
			}
		} else {
			if (select) {
				element.click();
			}
		}
		ensureBecomes(new PollForWithError() {
			public boolean matches() {
				return element.isSelected() == select;
			}
			public String error() {
				return "Not selected correctly";
			}
		});
		return true;
	}
	
	// --------- OPTIONS ---------
	public String optionOf(String locator) {
		return selectElement.optionOf(locator);
	}
	protected List<WebElement> childrenOf(String locator, String tag) {
		return findElement(locator).findElements(By.tagName(tag));
	}
	public List<String> optionListOf(String locator) {
		return selectElement.optionListOf(locator);
	}
	public boolean optionallyWithSelectOption(String locator, String option) {
		return selectElement.optionallyWithSelectOption(locator, option);
	}
	public boolean withSelectOption(String locator, String option) {
		return selectElement.withSelectOption(locator, option);
	}
	public boolean withSelectOptionAt(String locator, int index) {
		return selectElement.withSelectOptionAt(locator, index);
	}
	public boolean withSelectText(String locator, String text) {
		return selectElement.withSelectText(locator, text);
	}
	public boolean withAddSelection(String locator, String option) {
		return selectElement.withAddSelection(locator, option);
	}
	public boolean withRemoveSelection(String locator, String option) {
		return selectElement.withRemoveSelection(locator, option);
	}
	public List<String> optionValues(String locator) {
		return selectElement.optionValues(locator);
	}
	public List<WebElement> options(String locator) {
		return selectElement.options(locator);
	}
	
	// --------- TABLES ---------
	public TableFixture tableValues(String locator) {
		List<List<String>> stringTable = new ArrayList<List<String>>();
		for (WebElement option : childrenOf(locator, "tr")) {
			List<String> stringRow = new ArrayList<String>();
			stringTable.add(stringRow);
			for (WebElement element : option.findElements(By.tagName("td"))) {
				stringRow.add(element.getText());
			}
		}
		return new TableFixture(stringTable);
	}
	
	// --------- LINK TRAVERSAL & FORM SUBMIT ---------
	public boolean click(String locator) {
		return linkAndFormElement.click(locator);
	}
	public boolean clickAndSelectResultingWindow(String locator) {
		return linkAndFormElement.clickAndSelectResultingWindow(locator);
	}
	public boolean clickOnNamedLink(String linkName) {
		return linkAndFormElement.clickOnNamedLink(linkName);
	}
	public boolean clickOnPartiallyNamedLink(String linkName) {
		return linkAndFormElement.clickOnPartiallyNamedLink(linkName);
	}
	public void submit(String locator) {
		linkAndFormElement.submit(locator);
	}
	
	// --------- COOKIES ---------
	public boolean addCookieWithValue(String name, String value) {
		webDriver().manage().addCookie(new Cookie(name, value, ""));
		return true;
	}
	public boolean deleteCookie(String name) {
		webDriver().manage().deleteCookieNamed(name);
		return true;
	}
	public boolean deleteAllCookies() {
		webDriver().manage().deleteAllCookies();
		return true;
	}
	public Set<Cookie> cookies() {
		return webDriver().manage().getCookies();
	}
	
	// --------- MOUSE SPEED ---------
	public Speed mouseSpeed() {
		return webDriver().manage().getSpeed();
	}
	public boolean makeMouseSpeed(Speed speed) {
		webDriver().manage().setSpeed(speed);
		return true;
	}
	
	// --------- SCREEN DUMP ---------
	public void screenDump() {
		driverVariation.screenDump();
	}
	public void screenDump(String fileName) {
		driverVariation.screenDump(fileName);
	}
	public void screenDumpDirectory(String diryName) {
		driverVariation.setScreenDumpDirectory(diryName);
	}
	
	// --------- WITH CONTEXT ---------
	public SpiderElementFixture withDo(String locator) {
		return spiderElementFixture(findElement(locator));
	}
	public SpiderElementFixture withTableSelectRowWith(String tableLocator, String givenRowLocator) {
		String rowLocator = givenRowLocator;
		if (rowLocator.startsWith("//"))
			rowLocator = "." + rowLocator;
		try {
			WebElement tableWebElement = findElement(tableLocator);
			for (WebElement rowElement : tableWebElement.findElements(By.tagName("tr"))) {
				for (WebElement cellElement : rowElement.findElements(By.tagName("td"))) {
					try {
						WebElement webElement = cellElement.findElement(By.xpath(rowLocator));
						if (webElement != null) {
							if (isSelenium()) {
								if (selenium().isElementPresent(
										((SeleniumWebElement) webElement).getLocator()))
									return spiderElementFixture(rowElement);
							} else
								return spiderElementFixture(rowElement);
						}
					} catch (NoSuchElementException ex) {
						//
					}
				}
			}
		} catch (NoSuchElementException ex) {
			throw new FitLibraryException("Unable to find row with path "
					+ rowLocator);
		}
		throw new FitLibraryException("No matching row");
	}
	// Can be overridden to use a subclass of SpiderElementFixture
	protected SpiderElementFixture spiderElementFixture(WebElement rowElement) {
		return new SpiderElementFixture(rowElement, spiderFixture);
	}
	
	// --------- LOOKUP ---------
	public LookupFixture lookup() {
		return new LookupFixture();
	}
	
	// ------------------- private
	public WebElement findElement(final String locator) {
		try {
			return ensureNoException(new PollForNoException<WebElement>() {
				public WebElement act() {
					return finder.findElement(locator);
				}
			});
		} catch (NoSuchElementException ex) {
			throw problem("Unavailable", locator, locator);
		} catch (Exception ex) {
			throw problem("Unknown xpath (" + ex.getMessage() + ")", locator,
					locator);
		}
	}
	public FitLibraryException problem(String message, String expected,
			String resolvedExpected) {
		if (expected.equals(resolvedExpected)) {
			return new FitLibraryException(message + atShortUrl());
		}
		return new FitLibraryException(message + ": '" + resolvedExpected + "'"
				+ atShortUrl());
	}
	private String atShortUrl() {
		String url = "";
		try {
			url = webDriver().getCurrentUrl();
			int pos = url.indexOf(";");
			if (pos >= 0) {
				return " at " + url.substring(0, pos);
			}
		} catch (Exception e) {
			//
		}
		return " at " + url;
	}
	public String collectText(WebElement element) {
		return collectText(element, true);
	}
	private String collectText(WebElement element, boolean trim) {
		String value = element.getText();
		if (value == null || "".equals(value.trim())) {
			value = element.getValue();
		}
		if (value == null) {
			value = "";
		}
		if (trim) {
			value = HtmlTextUtility.crLfRemoved(HtmlTextUtility.spacesToSingleSpace(HtmlTextUtility.nonBreakingSpaceToSpace(HtmlTextUtility.tabToSpace(HtmlTextUtility.brToSpace(value)))))
					.trim();
		}
		return Parse.unescape(value);
	}
	public void ensureBecomes(PollForWithError poll) {
		ensureWithError(poll, getTimeout("becomes"));
	}
	public void ensureWithError(PollForWithError poll, int timeout) {
		if (checking)
			new Poll(timeout).ensureWithError(poll);
	}
	public boolean ensureMatches(PollForMatches poll) {
		return new Poll(getTimeout(CHECKING_TIMEOUT)).ensureMatches(poll);
	}
	public boolean ensureMatchesNoException(PollForMatches poll) {
		return new Poll(getTimeout(CHECKING_TIMEOUT))
				.ensureMatchesNoException(poll);
	}
	public <T> T ensureNoException(PollForNoException<T> poll) throws Exception {
		return new Poll(getTimeout(CHECKING_TIMEOUT)).ensureNoException(poll);
	}
	public boolean isSelenium() {
		Object systemUnderTest = spiderFixture.getSystemUnderTest();
		return systemUnderTest != null && systemUnderTest instanceof Selenium;
	}
	public Selenium selenium() {
		return (Selenium) spiderFixture.getSystemUnderTest();
	}
}
