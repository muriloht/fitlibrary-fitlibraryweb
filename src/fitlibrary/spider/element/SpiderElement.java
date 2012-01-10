package fitlibrary.spider.element;

import org.openqa.selenium.WebElement;

import fitlibrary.exception.FitLibraryException;
import fitlibrary.spider.AbstractSpiderFixture;
import fitlibrary.spider.Finder;
import fitlibrary.spider.SpiderFixture;
import fitlibrary.spider.component.SpiderWindow;
import fitlibrary.spider.polling.PollForMatches;
import fitlibrary.spider.polling.PollForNoException;
import fitlibrary.spider.polling.PollForWithError;
import fitlibrary.spider.polling.PollWithElement;

public abstract class SpiderElement {
	private AbstractSpiderFixture spiderFixture;

	public SpiderElement(AbstractSpiderFixture spiderFixture) {
		this.spiderFixture = spiderFixture;
	}
	protected WebElement findElement(final String locator) {
		return spiderFixture.findElement(locator);
	}
	public Finder finder() {
		return spiderFixture.getFinder();
	}
	protected <T> T findAndAction(final String locator, final PollWithElement<T> action) {
	  return spiderFixture.findAndAction(locator, action);
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
