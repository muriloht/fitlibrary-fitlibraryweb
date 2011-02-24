package fitlibrary.spider.element;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import fitlibrary.exception.FitLibraryException;
import fitlibrary.spider.AbstractSpiderFixture;
import fitlibrary.spider.Finder;
import fitlibrary.spider.SpiderFixture;
import fitlibrary.spider.component.SpiderWindow;
import fitlibrary.spider.polling.PollForMatches;
import fitlibrary.spider.polling.PollForNoException;
import fitlibrary.spider.polling.PollForWithError;

public abstract class SpiderElement {
	private AbstractSpiderFixture spiderFixture;

	public SpiderElement(AbstractSpiderFixture spiderFixture) {
		this.spiderFixture = spiderFixture;
	}
	protected WebElement findElement(final String locator) {
		return spiderFixture.findElement(locator);
	}
	protected List<WebElement> childrenOf(String locator, String tag) {
		return findElement(locator).findElements(By.tagName(tag));
	}
	public Finder finder() {
		return spiderFixture.getFinder();
	}
	protected boolean ensureMatches(PollForMatches poll) {
		return spiderFixture.ensureMatches(poll);
	}
	public void ensureBecomes(PollForWithError poll) {
		spiderFixture.ensureBecomes(poll);
	}
	public <T> T ensureNoException(PollForNoException<T> poll) throws Exception {
		return spiderFixture.ensureNoException(poll);
	}
	protected FitLibraryException problem(String message, String details) {
		return spiderFixture.problem(message, details);
	}
	protected SpiderFixture spiderFixture() {
		return spiderFixture.spiderFixture();
	}
	protected SpiderWindow window() {
		return spiderFixture().getWindow();
	}
}
