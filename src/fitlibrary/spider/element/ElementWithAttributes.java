package fitlibrary.spider.element;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
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
		try {
			ensureNoException(new PollForNoException<WebElement>() {
				@Override
				public WebElement act() {
					return finder().findElement(locator);
				}
			});
			return true;
		} catch (NoSuchElementException ex) {
			return false;
		} catch (Exception ex) {
			throw problem("Unknown xpath", locator);
		}
	}
	public boolean elementDoesNotExist(final String locator) {
		// Poll until we get an exception
		return ensureMatches(new PollForMatches() {
			@Override
			public boolean matches() {
				try {
					finder().findElement(locator);
					return false;
				} catch (Exception e) {
					return true;
				} 
			}
		});
	}
	public String elementValue(String locator) {
		return findElement(locator).getAttribute("value");
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
		return ensureMatches(new PollForMatches() {
			@Override
			public boolean matches() {
				String attribute = findElement(locator).getAttribute(attributeName);
				return attribute != null && !attribute.isEmpty();
			}
		});
	}
	public String withCssPropertyOf(String locator, String property) {
		return findElement(locator).getCssValue(property);
	}
	public int countOf(String locator) {
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
	private List<WebElement> childrenOf(String locator, String tag) {
		return findElement(locator).findElements(By.tagName(tag));
	}

	public WebElementSelector findElementFromWithTagWhere(String locator,
			String tag) {
		// Doesn't wait for Javascript to alter values or descendent elements
		return new WebElementSelector(tag, findElement(locator).findElements(
				By.tagName(tag)), spiderFixture());
	}
}
