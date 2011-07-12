/*
 * Copyright (c) 2009 Rick Mugridge, www.RimuResearch.com
 * Kindly donated by Air New Zealand in October 2009.
 * Released under the terms of the GNU General Public License version 2 or later.
*/
package fitlibrary.spider;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import fit.Parse;
import fitlibrary.annotation.CompoundAction;
import fitlibrary.annotation.NullaryAction;
import fitlibrary.annotation.ShowSelectedActions;
import fitlibrary.annotation.SimpleAction;
import fitlibrary.exception.FitLibraryException;
import fitlibrary.log.FixturingLogger;
import fitlibrary.runResults.TestResults;
import fitlibrary.spider.component.Alert;
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
import fitlibrary.spider.utility.HtmlTextUtility;
import fitlibrary.spider.utility.WebElementSelector;
import fitlibrary.table.Row;
import fitlibrary.traverse.workflow.DoTraverse;

@ShowSelectedActions
public abstract class AbstractSpiderFixture extends DoTraverse {
	@SuppressWarnings("unused")
	private static Logger logger = FixturingLogger.getLogger(SpiderFixture.class);
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
	@SimpleAction(wiki="|''<i>poll url timeout</i>''|timeout in milliseconds|",
			tooltip="Specify how long to wait for a page to load from a URL.")
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
	@SimpleAction(wiki="|''<i>set checking</i>''|true or false|",
			tooltip="If true, changes to web elements are checked after they have been made.")
	public void setChecking(boolean checking) {
		this.checking = checking;
	}
	@SimpleAction(wiki="|''<i>checking timeout</i>''|timeout in milliseconds|",
			tooltip="If checking that a change has been made to a web element, how long to wait for a change to be visible.")
	public void checkingTimeout(int timeout) {
		putTimeout(CHECKING_TIMEOUT, timeout);
	}
	
	// --------- SHUT DOWN: ---------
	public void shutDown() throws IOException {
		spiderFixture.tearDownDriver();
	}
	@NullaryAction(tooltip="If a failure has occurred, create a screen dump (if supported) and stop.")
	public void shutDownWithScreenDumpOnFailure() {
		if (getRuntimeContext().getTestResults().problems())
			screenDump();
		spiderFixture.tearDownDriver();
	}
	
	// --------- JAVASCRIPT execution: ---------
	@SimpleAction(wiki="|''<i>execute JavaScript</i>''|JavaScript code|",
			tooltip="Execute the JavaScript code in the browser and return the result.")
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
	@SimpleAction(wiki="|''<i>execute JavaScript</i>''|JavaScript code|''<i>with</>''|web element|",
			tooltip="Execute the JavaScript code in the browser with the argument and return the result.")
	public Object executeJavaScriptWith(String script, Object param) {
		JavascriptExecutor js = (JavascriptExecutor) webDriver();
		return js.executeScript(script, param);
	}
	@SimpleAction(wiki="|''<i>execute JavaScript</i>''|JavaScript code|''<i>with element</>''|xpath, id or other locator|",
			tooltip="Find a element on the page using the locator specified and then execute the JavaScript code in the browser using the found element as an the argument and return the result.")
	public Object executeJavaScriptWithElement(String script, String locator) {
		WebElement element = findElement(locator);
		return executeJavaScriptWith(script, element);
	}
	// --------- PAGE MANAGEMENT: ---------
	@SimpleAction(wiki="|''<i>get url</i>''|url|",
			tooltip="Retrieve the page at the url.")
	public boolean getUrl(final String url) throws Exception {
		return page().getUrl(url);
	}
	@NullaryAction(tooltip="Go back to the previous page in the browser, if any.")
	public boolean back() {
		return page().back();
	}
	@NullaryAction(tooltip="Go forward to the next page in the browser, if any.")
	public boolean forward() {
		return page().forward();
	}
	@NullaryAction(tooltip="Returns the title of the current page.")
	public String title() {
		return page().getTitle();
	}
	public String getTitle() {
		return page().getTitle();
	}
	@NullaryAction(tooltip="Returns the url of the current page.")
	public String url() {
		return page().getUrl();
	}
	public String getUrl() {
		return page().getUrl();
	}
	public void pollUrl(final Row row, final TestResults testResults) throws Exception {
		page().pollUrl(row, testResults);
	}
	@NullaryAction(tooltip="Have the browser refresh the current page.")
	public void refreshPage() {
		page().refresh();
	}
	// --------- DIAGNOSTICS: ---------
	@SimpleAction(wiki="|''<i>show error diagnostics at</i>''|xpath|''<i>when page contains</i>''|pattern|",
			tooltip="When a website responds with an error message at xpath that's not expected, it's handy if diagnostic information is automatically provided about this in the storytest report.")
	public void showErrorDiagnosticsAtWhenPageContains(String xpath, String pattern) {
		page().showErrorDiagnosticsAtWhenPageContains(xpath, pattern);
	}
	@NullaryAction(tooltip="Check if any errors have occurred .")
	public void checkForError() {
		page().checkForError();
	}
	private Page page() {
		return spiderFixture.getPage();
	}
	
	// --------- WINDOW ACCESS: ---------
	@NullaryAction(tooltip="The internal name of the current window.")
	public String currentWindow() {
		return window().currentWindow();
	}
	@NullaryAction(tooltip="The internal name of open browser window.")
	public String windows() {
		return window().windows();
	}
	@NullaryAction(tooltip="After selecting another window, select the initial window again.")
	public boolean selectInitialWindow() {
		return window().selectInitialWindow();
	}
	@SimpleAction(wiki="|''<i>select window</i>''|name|",
			tooltip="Select a window by name.")
	public boolean selectWindow(String name) {
		return window().selectWindow(name);
	}
	public boolean selectWindowWithAs(String xpath, String value) {
		return window().selectWindowWithAs(xpath, value);
	}
	public boolean selectWindowWithContains(String xpath, String value) {
		return window().selectWindowWithContains(xpath, value);
	}

	@NullaryAction(tooltip="Selecting the other window, of two.")
	public boolean selectOtherWindow() {
		return window().selectOtherWindow();
	}
	@NullaryAction(tooltip="Close the current window.")
	public boolean close() {
		return window().close();
	}
	private SpiderWindow window() {
		return spiderFixture.getWindow();
	}
	
	// --------- FRAME ACCESS ---------
	@SimpleAction(wiki="|''<i>select frame</i>''|index of frame|",
			tooltip="Select the nth frame.")
	public boolean frame(final int frameNo) {
		return frame().frame(frameNo);
	}
	@SimpleAction(wiki="|''<i>select frame by name</i>''|name of frame|",
			tooltip="Select a frame by name.")
	public boolean frameByName(final String frameName) {
		return frame().frameByName(frameName);
	}
	@NullaryAction(tooltip="Select the default frame.")
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
	@NullaryAction(tooltip="Returns the html of the page.")
	public String pageSource() {
		return textInPage().pageSource();
	}
	@NullaryAction(tooltip="Returns the html of the page after cleaning it up.\nMultiple spaces are turned into a single space.\nBreaks and non-breaking spaces are turned into spaces.")
	public String pageSourceCleaned() {
		return textInPage().pageSourceCleaned();
	}
	private TextInPage textInPage() {
		return spiderFixture.getTextInPage();
	}
	
	// --------- INPUT TEXT & TEXT AREA: ---------
	@SimpleAction(wiki="|''<i>text of</i>''|xpath, id or other locator|",
			tooltip="Return the text of the given web element.")
	public String textOf(String locator) {
		return textElement.textOf(locator);
	}
	@SimpleAction(wiki="|''<i>plain text of</i>''|xpath, id or other locator|",
			tooltip="Return the plain text of the given web element. Tags are removed.")
	public String plainTextOf(String locator) {
		return textElement.plainTextOf(locator);
	}
	@SimpleAction(wiki="|''<i>text of</i>''|xpath, id or other locator|''<i>matches lines</i>''|",
			tooltip="Checks that the lines in the given text element match each of the following rows in the table.")
	public MultiLineMatchFixture textOfMatchesLines(String locator) {
		return textElement.textOfMatchesLines(locator);
	}
	public boolean optionallyWithSetText(String locator, String s) {
		return textElement.optionallyWithSetText(locator, s);
	}
	@SimpleAction(wiki="|''<i>with</i>''|xpath, id or other locator|''<i>set text</i>''|text|",
			tooltip="Change the text of the given web element.")
	public boolean withSetText(final String locator, final String s) {
		return textElement.withSetText(locator, s);
	}
	@SimpleAction(wiki="|''<i>with</i>''|xpath, id or other locator|''<i>add text</i>''|text|",
			tooltip="Add to the text of the given web element.")
	public boolean withAddText(final String locator, final String s) {
		return textElement.withAddText(locator, s);
	}
	@SimpleAction(wiki="|''<i>inner html of</i>''|xpath, id or other locator|",
			tooltip="Return the html that's enclosed by the given web element.")
	public String innerHtmlOf(String locator) {
		return textElement.innerHtmlOf(locator);
	}
	@SimpleAction(wiki="|''<i>text of element only</i>''|xpath, id or other locator|",
			tooltip="Return only the text that is in the identified element, no text of any inner html will be returned\n."+
			        "This should return the text that an xpath ''//div/text()'' would return as opposed to ''//div/.'' that the browser will normally render (and is returned by '''text of'''.")
	public String textOfElementOnly(String locator) {
		return textElement.textOfElementOnly(locator);
	}
	// --------- GENERAL ELEMENTS: EXISTENCE, ATTRIBUTES, VALUES, TEXT, COUNT ---------
	@SimpleAction(wiki="|''<i>element exists</i>''|xpath, id or other locator|",
			tooltip="Returns true if the given web element exists on the page.")
	public boolean elementExists(String locator) {
		return elementWithAttributes.elementExists(locator);
	}
	@SimpleAction(wiki="|''<i>element does not exist</i>''|xpath, id or other locator|",
			tooltip="Returns true if the given web element does not exist on the page.")
	public boolean elementDoesNotExist(String locator) {
		return elementWithAttributes.elementDoesNotExist(locator);
	}
	@SimpleAction(wiki="|''<i>element value</i>''|xpath, id or other locator|",
			tooltip="Returns the value of the given web element.")
	public String elementValue(String locator) {
		return elementWithAttributes.elementValue(locator);
	}
	@SimpleAction(wiki="|''<i>attribute</i>''|attribute name|''<i>of</i>''|xpath, id or other locator|",
			tooltip="Returns the value of the given attribute in the given web element.")
	public String attributeOf(String attributeName, String locator) {
		return elementWithAttributes.attributeOf(attributeName, locator);
	}
	@SimpleAction(wiki="|''<i>attribute of</i>''|xpath, id or other locator|''<i>exists</i>''|",
			tooltip="Returns true if the given attribute exists in the given web element.")
	public boolean attributeOfExists(String attributeName, String locator) {
		return elementWithAttributes.attributeOfExists(attributeName, locator);
	}
	@SimpleAction(wiki="|''<i>with</i>''|xpath, id or other locator|''<i>css property of</i>''|css property name|",
			tooltip="Returns the value of the given css property of the given web element.")
	public String withCssPropertyOf(String locator, String property) {
		return elementWithAttributes.withCssPropertyOf(locator, property);
	}
	@SimpleAction(wiki="|''<i>count of</i>''|xpath, id or other locator|",
			tooltip="Returns the count of elements that match the given locator.")
	public int countOf(String locator) {
		return elementWithAttributes.countOf(locator);
	}
	@SimpleAction(wiki="|''<i>attribute</i>''|attribute|''<i>of children of type</i>''|child tag|''<i>of</i>''|xpath, id or other locator|",
			tooltip="Returns a list of information about the attributes of each of the children with the given tag contained by the given web element.\n")
	public List<String> attributeOfChildrenOfTypeOf(String attribute,
			String childType, String locator) {
		return elementWithAttributes.attributeOfChildrenOfTypeOf(attribute, childType, locator);
	}
	@CompoundAction(wiki="|''<i>find element from</i>''|xpath, id or other locator|''<i>with tag</i>''|tag|''<i>where</i>''|",
			tooltip="From the given locator, find child elements with the given tag.\nActions in the rest of the table can select further.")
	public WebElementSelector findElementFromWithTagWhere(String locator, String tag) {
		return elementWithAttributes.findElementFromWithTagWhere(locator, tag);
	}
	@SimpleAction(wiki="|''<i>element is visible</i>''|xpath, id or other locator|",
			tooltip="Returns true if the given web element is displayed (visible to the user).")
	public boolean elementVisible(String locator) {
		return findElement(locator).isDisplayed();
	}
	@SimpleAction(wiki="|''<i>element invisible</i>''|xpath, id or other locator|",
			tooltip="Returns true if the given web element is not displayed (invisible to the user).")
	public boolean elementInvisible(String locator) {
		return !elementVisible(locator);
	}
	
	// --------- CHECKBOX ---------
	@SimpleAction(wiki="|''<i>checkbox</i>''|xpath, id or other locator|",
			tooltip="Returns true if the given checkbox is currently selected.")
	public boolean checkbox(String locator) {
		return findElement(locator).isSelected();
	}
	@SimpleAction(wiki="|''<i>with</i>''|xpath, id or other locator|''<i>select</i>''|true or false|",
			tooltip="Either select (true) or unselect (false) the given checkbox.")
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
			@Override
			public boolean matches() {
				return element.isSelected() == select;
			}
			@Override
			public String error() {
				return "Not selected correctly";
			}
		});
		return true;
	}
	
	// --------- OPTIONS ---------
	@SimpleAction(wiki="|''<i>option of</i>''|xpath, id or other locator|",
			tooltip="Returns the name of the currently-selected option in the given select.")
	public String optionOf(String locator) {
		return selectElement.optionOf(locator);
	}
	protected List<WebElement> childrenOf(String locator, String tag) {
		return findElement(locator).findElements(By.tagName(tag));
	}
	@SimpleAction(wiki="|''<i>option list of</i>''|xpath, id or other locator|",
			tooltip="Returns a list of the names of the currently-selected options in the given multi-select.\nThis can be checked in the subsequent rows of the table.")
	public List<String> optionListOf(String locator) {
		return selectElement.optionListOf(locator);
	}
	public boolean optionallyWithSelectOption(String locator, String option) {
		return selectElement.optionallyWithSelectOption(locator, option);
	}
	@SimpleAction(wiki="|''<i>with</i>''|xpath, id or other locator|''<i>select option</i>''|option|",
			tooltip="Changes the option in the given select.")
	public boolean withSelectOption(String locator, String option) {
		return selectElement.withSelectOption(locator, option);
	}
	@SimpleAction(wiki="|''<i>with</i>''|xpath, id or other locator|''<i>select option at</i>''|index|",
			tooltip="Changes the option to the nth one, in the given select.")
	public boolean withSelectOptionAt(String locator, int index) {
		return selectElement.withSelectOptionAt(locator, index);
	}
	@SimpleAction(wiki="|''<i>with</i>''|xpath, id or other locator|''<i>select text</i>''|text of option|",
			tooltip="Changes the option to the one with the given text, in the given select.")
	public boolean withSelectText(String locator, String text) {
		return selectElement.withSelectText(locator, text);
	}
	@SimpleAction(wiki="|''<i>with</i>''|xpath, id or other locator|''<i>add selection</i>''|option|",
			tooltip="Adds the given option to the given multi-select.")
	public boolean withAddSelection(String locator, String option) {
		return selectElement.withAddSelection(locator, option);
	}
	@SimpleAction(wiki="|''<i>with</i>''|xpath, id or other locator|''<i>remove selection</i>''|option|",
			tooltip="Removes the given option from the given multi-select.")
	public boolean withRemoveSelection(String locator, String option) {
		return selectElement.withRemoveSelection(locator, option);
	}
	@SimpleAction(wiki="|''<i>option values</i>''|xpath, id or other locator|",
			tooltip="Returns a list of the values of the currently-selected options in the given multi-select.")
	public List<String> optionValues(String locator) {
		return selectElement.optionValues(locator);
	}
	@SimpleAction(wiki="|''<i>options</i>''|xpath, id or other locator|",
			tooltip="Returns a list of the elements for the options in the select or multi-select.\nThis list can be checked in the subsequent rows of the table.")
	public List<WebElement> options(String locator) {
		return selectElement.options(locator);
	}
	
	// --------- TABLES ---------
	@SimpleAction(wiki="|''<i>table values</i>''|xpath, id or other locator|",
			tooltip="Returns the elements of the given table as a TableFixture, which can be used to check the text of each of those elements.")
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
	@SimpleAction(wiki="|''<i>click</i>''|xpath, id or other locator|",
			tooltip="Click on the given element, such as a link or button.")
	public boolean click(String locator) {
		return linkAndFormElement.click(locator);
	}
	@SimpleAction(wiki="|''<i>click and select resulting window</i>''|xpath, id or other locator|",
			tooltip="Click on the given element and select the popup window that results.")
	public boolean clickAndSelectResultingWindow(String locator) {
		return linkAndFormElement.clickAndSelectResultingWindow(locator);
	}
	@SimpleAction(wiki="|''<i>click on named link</i>''|link name|",
			tooltip="Click on the first link with the given name.")
	public boolean clickOnNamedLink(String linkName) {
		return linkAndFormElement.clickOnNamedLink(linkName);
	}
	@SimpleAction(wiki="|''<i>click on partially named link</i>''|partial link name|",
			tooltip="Click on the first link that contains the given name.")
	public boolean clickOnPartiallyNamedLink(String linkName) {
		return linkAndFormElement.clickOnPartiallyNamedLink(linkName);
	}
	@SimpleAction(wiki="|''<i>submit</i>''|xpath, id or other locator|",
			tooltip="Submit the given form.")
	public void submit(String locator) {
		linkAndFormElement.submit(locator);
	}
	
	// --------- COOKIES ---------
	@SimpleAction(wiki="|''<i>add cookie</i>''|name|''<i>with value</i>''|value|",
			tooltip="Add the cookie.")
	public boolean addCookieWithValue(String name, String value) {
		webDriver().manage().addCookie(new Cookie(name, value, ""));
		return true;
	}
	@SimpleAction(wiki="|''<i>delete cookie</i>''|name|",
			tooltip="Delete the cookie with the given name.")
	public boolean deleteCookie(String name) {
		webDriver().manage().deleteCookieNamed(name);
		return true;
	}
	@NullaryAction(tooltip="Delete all of the cookies.")
	public boolean deleteAllCookies() {
		webDriver().manage().deleteAllCookies();
		return true;
	}
	@NullaryAction(tooltip="Return a set of cookies, which can be checked in the rest of the table.")
	public Set<Cookie> cookies() {
		return webDriver().manage().getCookies();
	}
	
	// --------- ALERT ACCESS ---------
	@NullaryAction(tooltip="Dismiss a pop-up javascript alert, if the alert is a confirm alert will dismiss without accepting.")
	public boolean dismissAlert() {
		return alert().dismissAlert();
	}
	@NullaryAction(tooltip="Dismiss a pop-up javascript alert, if the alert is a confirm alert the alert will be accepted.")
	public boolean acceptAlert() {
		return alert().acceptAlert();
	}
	@NullaryAction(tooltip="Get the text message from within a pop-up javascript alert.")
	public String getAlertMessage() {
		return alert().getAlertMessage();
	}
	@NullaryAction(tooltip="Enter text into a prompt style javascript pop-up alert.")
	public boolean enterAlertText(String text) {
		return alert().enterAlertText(text);
	}
	private Alert alert() {
		return spiderFixture.getAlert();
	}
	// --------- SCREEN DUMP ---------
	@NullaryAction(tooltip="Take a screen dump and insert the result in the report, if the browser supports it.")
	public void screenDump() {
		driverVariation.screenDump();
	}
	@SimpleAction(wiki="|''<i>screen dump</i>''|file name|",
			tooltip="Take a screen dump and write it to the given file.")
	public void screenDump(String fileName) {
		driverVariation.screenDump(fileName);
	}
	@SimpleAction(wiki="|''<i>screen dump directory</i>''|folder name|",
			tooltip="Specify the folder where screen dumps are to be placed.")
	public void screenDumpDirectory(String diryName) {
		driverVariation.setScreenDumpDirectory(diryName);
	}
	
	// --------- WITH CONTEXT ---------
	@SimpleAction(wiki="|''<i>with</i>''|xpath, id or other locator|''<i>do</i>''|",
			tooltip="Select the given web element.\nActions in subsequent rows of the table apply within the context of that element, using any of the Spider actions.")
	public SpiderElementFixture withDo(String locator) {
		return spiderElementFixture(findElement(locator));
	}
	@SimpleAction(wiki="|''<i>with table</i>''|xpath, id or other locator|''<i>select row with</i>''|locator for row within the table|",
			tooltip="Select the given row within the table.\nActions in subsequent rows of the table apply within the context of that element, using any of the Spider actions.")
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
	@NullaryAction(tooltip="Returns a LookupFixture, that can be used to look up values in the following rows of the table.")
	public LookupFixture lookup() {
		return new LookupFixture();
	}
	
	// ------------------- private
	public WebElement findElement(final String locator) {
		try {
			return ensureNoException(new PollForNoException<WebElement>() {
				@Override
				public WebElement act() {
					return finder.findElement(locator);
				}
			});
		} catch (NoSuchElementException ex) {
			throw problem("No such element", locator);
		} catch (Exception ex) {
			throw problem("Unknown xpath (" + ex.getMessage() + ")", locator);
		}
	}
	public FitLibraryException problem(String problemDescription, String details) {
		return new FitLibraryException(problemDescription + ": '" + details + "'"
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
			value = element.getAttribute("value");
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
}
