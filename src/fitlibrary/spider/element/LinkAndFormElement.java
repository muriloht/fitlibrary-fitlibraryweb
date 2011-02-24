package fitlibrary.spider.element;

import org.openqa.selenium.By;

import fitlibrary.exception.FitLibraryException;
import fitlibrary.spider.AbstractSpiderFixture;

public class LinkAndFormElement extends SpiderElement {
	public LinkAndFormElement(AbstractSpiderFixture spiderFixture) {
		super(spiderFixture);
	}
	public boolean click(String locator) {
		try {
			findElement(locator).click();
			return true;
		} catch (FitLibraryException e) {
			throw e;
		} catch (RuntimeException e) {
			throw problem(e.toString(), locator);
		}
	}
	public boolean clickAndSelectResultingWindow(String locator) {
		return window().clickAndSelectResultingWindow(locator);
	}
	public boolean clickOnNamedLink(String linkName) {
		try {
			finder().findElement(By.linkText(linkName)).click();
		} catch (Exception e) {
			throw new FitLibraryException("No such link");
		}
		return true;
	}
	public boolean clickOnPartiallyNamedLink(String linkName) {
		try {
			finder().findElement(By.partialLinkText(linkName)).click();
		} catch (Exception e) {
			throw new FitLibraryException("No such partial link");
		}
		return true;
	}
	public void submit(String locator) {
		try {
			findElement(locator).submit();
		} catch (RuntimeException e) {
			throw new FitLibraryException(e.getMessage());
		}
	}
}
