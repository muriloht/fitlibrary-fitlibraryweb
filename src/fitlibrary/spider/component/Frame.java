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
			@Override
			public boolean matches() {
				webDriver().switchTo().defaultContent().switchTo().frame(frameNo);
				return true;
			}
		});
		if (matches)
			return true;
		throw problem("Unavailable", "" + frameNo);
	}
	public boolean frameByName(final String frameName) {
		boolean matches = ensureMatchesNoException(new PollForMatches() {
			@Override
			public boolean matches() {
				webDriver().switchTo().defaultContent();
				
				// WebDriver has removed the notion of separating nested frame names by . but we still support it
				for (String framesubname: frameName.split("\\.")) {
					webDriver().switchTo().frame(framesubname);
				}
				return true;
			}
		});
		if (matches)
			return true;
		throw problem("Unavailable", frameName);
	}
	
	public boolean defaultFrame() {
		boolean matches = ensureMatchesNoException(new PollForMatches() {
			@Override
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
