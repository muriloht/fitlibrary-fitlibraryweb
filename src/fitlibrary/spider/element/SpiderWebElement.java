package fitlibrary.spider.element;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebElement;

import util.StringUtil;

/** Wrapper for WebElement to re-introduce the deprecated getValue() method, this is publicly exposed by fixtures and checked in FittNesse tables so needs to be there as a java method. */
public class SpiderWebElement implements WebElement {

	private final WebElement wrappedElement;
	private String rawHtmlofParent;
	
	public SpiderWebElement(WebElement wrappedElement, String rawHtmlOfParent) {
		super();
		this.wrappedElement = wrappedElement;
		this.rawHtmlofParent = rawHtmlOfParent;
	}

	// -- additional methods (no longer provided by WebElement)
	
	public String getValue() {
        String value = getAttribute("value");

        // different behaviour in FireFox than other browsers, FireFox returns the text when value attribute missing other browsers return blank 
        // make FireFox conform to other browsers
        if (!StringUtil.isBlank(value) && value.equals(getText())) {
              if (!rawHtmlofParent.matches("<option.*value=[',\"]"+value+"[',\"].*>"+value+"</option>")) {
                    return "";
              }
        }

        return value;

  }
	
	// --- wrapped implementations of WebElement
	
	@Override
	public void click() {
		wrappedElement.click();
	}

	@Override
	public void submit() {
		wrappedElement.submit();
	}

	public void sendKeys(CharSequence... keysToSend) {
		wrappedElement.sendKeys(keysToSend);
	}

	@Override
	public void clear() {
		wrappedElement.clear();
	}

	@Override
	public String getTagName() {
		return wrappedElement.getTagName();
	}

	@Override
	public String getAttribute(String name) {
		return wrappedElement.getAttribute(name);
	}

	@Override
	public boolean isSelected() {
		// TODO Auto-generated method stub
		return wrappedElement.isSelected();
	}

	@Override
	public boolean isEnabled() {
		return wrappedElement.isEnabled();
	}

	@Override
	public String getText() {
		return wrappedElement.getText();
	}

	@Override
	public List<WebElement> findElements(By by) {
		return wrappedElement.findElements(by);
	}

	@Override
	public WebElement findElement(By by) {
		return wrappedElement.findElement(by);
	}

	@Override
	public boolean isDisplayed() {
		return wrappedElement.isDisplayed();
	}

	@Override
	public Point getLocation() {
		return wrappedElement.getLocation();
	}

	@Override
	public Dimension getSize() {
		return wrappedElement.getSize();
	}

	@Override
	public String getCssValue(String propertyName) {
		return wrappedElement.getCssValue(propertyName);
	}
}