package fitlibrary.spider.component;

import fitlibrary.exception.FitLibraryException;
import fitlibrary.spider.SpiderFixture;
import fitlibrary.spider.polling.PollForMatches;

public class Frame extends SpiderComponent {
	public Frame(SpiderFixture spiderFixture) {
		super(spiderFixture);
	}
	public boolean frame(final int frameNo) {
		boolean matches = ensureMatchesNoException(new PollForMatches() {
			public boolean matches() {
				webDriver().switchTo().frame(frameNo);
				return true;
			}
		});
		if (matches)
			return true;
		throw problem("Unavailable", "" + frameNo, "" + frameNo);
	}
	public boolean frameByName(final String frameName) {
		boolean matches = ensureMatchesNoException(new PollForMatches() {
			public boolean matches() {
				webDriver().switchTo().frame(frameName);
				return true;
			}
		});
		if (matches)
			return true;
		throw problem("Unavailable", frameName, frameName);
	}
	public boolean defaultFrame() {
		boolean matches = ensureMatchesNoException(new PollForMatches() {
			public boolean matches() {
				webDriver().switchTo().defaultContent();
				return true;
			}
		});
		if (matches)
			return true;
		throw new FitLibraryException("Unavailable");
	}

}
