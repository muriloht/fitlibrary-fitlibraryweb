package fitlibrary.spider.element;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import fit.Parse;
import fitlibrary.spider.AbstractSpiderFixture;
import fitlibrary.spider.MultiLineMatchFixture;
import fitlibrary.spider.polling.PollForWithError;
import fitlibrary.spider.polling.PollWithElement;
import fitlibrary.spider.utility.StringUtility;
import static fitlibrary.spider.utility.HtmlTextUtility.*;

public class TextElement extends SpiderElement {

	public TextElement(AbstractSpiderFixture spiderFixture) {
		super(spiderFixture);
	}
	public String textOf(String locator) {
		return collectText(locator);
	}
	public String plainTextOf(String locator) {
		return tagless(collectText(locator, true));
	}
	public String innerHtmlOf(String locator) {
		Object escaped = findAndAction(locator, new PollWithElement<Object>() {
			@Override
			public Object act(WebElement element) {
				return spiderFixture().executeJavaScriptWith("return arguments[0].innerHTML;", element);
			}
		});

		return lowerCaseTags(Parse.unescape(escaped.toString()));
	}
	public String textOfElementOnly(String locator) {
		return removeInnerHtml(innerHtmlOf(locator));
	}
	public MultiLineMatchFixture textOfMatchesLines(String locator) {
		// Doesn't wait for Javascript to alter existing values
		String text = collectText(locator, false);
		return new MultiLineMatchFixture(collectTaglessLines(text));
	}
	private Object[] collectTaglessLines(String lines) {
		if ("".equals(lines)) {
			return new Object[]{};
		}
		return lines.split("\n");
	}
	public boolean optionallyWithSetText(String locator, String s) {
		if (s.equals("") || s.contains("@{")) {
			return true;
		}
		try {
			withSetText(locator, s);
		} catch (Exception e) {
			// Ignore any problems
		}
		return true;
	}
	public boolean withSetText(final String locator, final String s) {
		final WebElement element = findElement(locator);
		if (!element.isEnabled()) // Due to bugs in HtmlUnit (clears a disabled text field) and in Selenium
			return true;
		element.clear();
		element.sendKeys(whiteSpace(s));
		ensureBecomes(new PollForWithError() {
			@Override
			public boolean matches() {
				return textOf(locator).equals(NLandTABasSpace(s));
			}
			@Override
			public String error() {
				return "Text wasn't changed correctly, it's "+(element.getText().length() >0 ? "'"+element.getText()+"'" : "empty");
			}
		});
		return true;
	}
	public boolean withAddText(final String locator, final String s) {
		final WebElement element = findElement(locator);
		if (!element.isEnabled()) {
			return true;
		}
		element.sendKeys(whiteSpace(s));
		ensureBecomes(new PollForWithError() {
			@Override
			public boolean matches() {
				return textOf(locator).endsWith(whiteSpace(s));
			}
			@Override
			public String error() {
				return "Text wasn't changed correctly, it's '"
						+ element.getText() + "'";
			}
		});
		return true;
	}
	public String collectText(String locator) {
		return collectText(locator, true);
	}
	private String collectText(String locator, boolean trim) {
		PollWithElement<String> findAndExtractElementText = new PollWithElement<String>() {
			@Override
			public String act(WebElement element) {
				String elementValue = element.getText();

				if (elementValue == null || "".equals(elementValue.trim())) {
					try {
						elementValue = element.getAttribute("value");
					} catch (UnsupportedOperationException uso) {
						// re throw unless exception is about missing value
						// attribute then we just handle drop through
						if (!uso.getMessage().contains(
								"Element does not have a value attribute")) {
							throw uso;
						}
					}
				}

				return elementValue;
			}
		};

		String value = findAndAction(locator, findAndExtractElementText);

		if (value == null) {
			value = "";
		}
		if (trim) {
			value = spacesToSingleSpace(crLfToSpace(nonBreakingSpaceToSpace(tabToSpace(brToSpace(value))))).trim();
		}
		return Parse.unescape(value);
	}
	public boolean withSendKey(String locator, final Keys key)
	{
		return findAndAction(locator, new PollWithElement<Boolean>() {
	        public Boolean act(WebElement located)
	        {
	          new Actions(spiderFixture().webDriver()).sendKeys(located, key).perform();
	          return true;
	        }
		});
	}
	protected static String whiteSpace(String s) {
		return StringUtility.replaceAll(s, "\\n", "\n").replaceAll("\\t", "\t");
	}
	protected static String NLandTABasSpace(String s) {
		return StringUtility.replaceAll(s, "\\n", " ").replaceAll("\\t", " ");
	}
	public static String crLfToSpace(String s) {
		return s.replaceAll("\\r?\\n", " ");
	}
	public static String spacesToSingleSpace(String s) {
		return s.replaceAll("\\s{2,}", " ");
	}
	public static String tabToSpace(String s) {
		return s.replaceAll("\\t", " ");
	}
}
