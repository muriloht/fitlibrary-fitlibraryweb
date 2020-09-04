package fitlibrary.spider.element;

import java.util.List;
import java.util.regex.Pattern;

import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.Rectangle;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;


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

		// WebDriver / Selenium used to often have different behaviour when there was a missing @value attribute. Since 2.21 all implementations now seems standard
		// (to just return the text()) however SpiderFixture used to always return "" so for historical reasons we want to keep the SpiderFixture behavour. 
		// This means running a regex in the DOM of the <options> to make sure the @value IS NOT the text() : 
		if (!value.isBlank() && value.equals(getText())) { 
			if (!Pattern.compile(".*<option.*?value=[',\"]?" + value + "[',\"]?.*?>" + value + "</option>.*", Pattern.DOTALL).matcher(rawHtmlofParent).matches()) {
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

	@Override
	public <X> X getScreenshotAs(OutputType<X> arg0) throws WebDriverException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Rectangle getRect() {
		// TODO Auto-generated method stub
		return null;
	}
}
