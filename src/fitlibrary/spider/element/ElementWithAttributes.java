package fitlibrary.spider.element;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.RenderedWebElement;
import org.openqa.selenium.WebElement;

import fitlibrary.exception.FitLibraryException;
import fitlibrary.spider.AbstractSpiderFixture;
import fitlibrary.spider.polling.PollForMatches;
import fitlibrary.spider.polling.PollForNoException;
import fitlibrary.spider.utility.WebElementSelector;

public class ElementWithAttributes extends SpiderElement {
	public ElementWithAttributes(AbstractSpiderFixture spiderFixture) {
		super(spiderFixture);
	}
	public boolean elementExists(final String locator) {
		if (isSelenium()) {
			return ensureMatches(new PollForMatches() {
				public boolean matches() {
					return selenium().isElementPresent(locator);
				}

			});
		}
		try {
			ensureNoException(new PollForNoException<WebElement>() {
				public WebElement act() {
					return finder().findElement(locator);
				}
			});
			return true;
		} catch (NoSuchElementException ex) {
			return false;
		} catch (Exception ex) {
			throw problem("Unknown xpath", locator, locator);
		}
	}
	public boolean elementDoesNotExist(final String locator) {
		// Poll until we get an exception
		return ensureMatches(new PollForMatches() {
			public boolean matches() {
				try {
					finder().findElement(locator);
					return false;
				} catch (NoSuchElementException e) {
					return true;
				}
			}
		});
	}
	public String elementValue(String locator) {
		return findElement(locator).getValue();
	}
	public String attributeOf(String attributeName, String locator) {
		String attribute = findElement(locator).getAttribute(attributeName);
		if (attribute == null) {
			throw new FitLibraryException("Missing attribute");
		}
		return attribute;
	}
	public boolean attributeOfExists(final String attributeName,
			final String locator) {
		try {
			return ensureMatches(new PollForMatches() {
				public boolean matches() {
					return findElement(locator).getAttribute(attributeName) != null;
				}
			});
		} catch (RuntimeException e) {
			if (isSelenium()) {
				return true;
			}
			throw e;
		}
	}
	public String withCssPropertyOf(String locator, String property) {
		WebElement element = findElement(locator);
		if (!(element instanceof RenderedWebElement)) {
			throw new FitLibraryException("Not a rendered element, but is a "
					+ element.getClass().getName());
		}
		RenderedWebElement rendered = (RenderedWebElement) element;
		return rendered.getValueOfCssProperty(property);
	}
	public int countOf(String locator) {
		if (isSelenium()) {
			return selenium().getXpathCount(locator).intValue();
		}
		return finder().findElements(locator).size();
	}
	public List<String> attributeOfChildrenOfTypeOf(String attribute,
			String childType, String locator) {
		// Doesn't wait for Javascript to alter any values
		ArrayList<String> result = new ArrayList<String>();
		for (WebElement e : childrenOf(locator, childType)) {
			if ("text".equals(attribute)) {
				result.add(e.getText());
			} else {
				result.add(e.getAttribute(attribute));
			}
		}
		return result;
	}
	public WebElementSelector findElementFromWithTagWhere(String locator,
			String tag) {
		// Doesn't wait for Javascript to alter values or descendent elements
		return new WebElementSelector(tag, findElement(locator).findElements(
				By.tagName(tag)), spiderFixture());
	}
}
