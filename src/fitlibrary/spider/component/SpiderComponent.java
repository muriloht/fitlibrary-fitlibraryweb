package fitlibrary.spider.component;

import org.openqa.selenium.WebDriver;

import fitlibrary.exception.FitLibraryException;
import fitlibrary.spider.SpiderFixture;
import fitlibrary.spider.driver.DriverVariation;
import fitlibrary.spider.polling.PollForMatches;
import fitlibrary.spider.polling.PollForWithError;

public abstract class SpiderComponent {
	protected final SpiderFixture spiderFixture;

	public SpiderComponent(SpiderFixture spiderFixture) {
		this.spiderFixture = spiderFixture;
	}
	protected WebDriver webDriver() {
		return spiderFixture.webDriver();
	}
	protected DriverVariation driverVariation() {
		return spiderFixture.getDriverVariation();
	}
	protected boolean ensureMatches(PollForMatches poll) {
		return spiderFixture.ensureMatches(poll);
	}
	protected boolean ensureMatchesNoException(PollForMatches poll) {
		return spiderFixture.ensureMatchesNoException(poll);
	}
	protected FitLibraryException problem(String message, String details) {
		return spiderFixture.problem(message, details);
	}
	protected void showAfterTable(String s) {
		spiderFixture.showAfterTable(s);
	}
	protected void ensureWithError(PollForWithError poll, String timeoutName) {
		spiderFixture.ensureWithError(poll, spiderFixture.getTimeout(timeoutName));
	}
}
