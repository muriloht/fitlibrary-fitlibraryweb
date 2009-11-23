package fitlibrary.spider.component;

import fitlibrary.closure.CalledMethodTarget;
import fitlibrary.exception.FitLibraryException;
import fitlibrary.spider.SpiderFixture;
import fitlibrary.spider.polling.PollForWithError;
import fitlibrary.spider.utility.Diagnostics;
import fitlibrary.spider.utility.HtmlTextUtility;
import fitlibrary.table.Row;
import fitlibrary.utility.TestResults;

public class Page extends SpiderComponent {
	public static final String POLL_URL_TIMEOUT = "pollUrl";
	private final Diagnostics diagnostics;
	
	public Page(SpiderFixture spiderFixture) {
		super(spiderFixture);
		diagnostics = new Diagnostics(spiderFixture);
	}
	public boolean getUrl(final String url) throws Exception {
		checkUnbound(url);
		try {
			webDriver().get(url);
		} catch (Exception e) {
			e.printStackTrace();
			throw problem(e.toString(), url, url);
		}
		driverVariation().checkTitleOfNewPage(url);
		return true;
	}
	private void checkUnbound(String s) {
		int at = s.indexOf("@{");
		if (at < 0) {
			return;
		}
		if (s.indexOf("}", at) > 0) {
			throw new FitLibraryException("Unbound variable");
		}
	}
	public boolean back() {
		webDriver().navigate().back();
		return true;
	}
	public boolean forward() {
		webDriver().navigate().forward();
		return true;
	}
	public boolean refresh() {
		webDriver().navigate().refresh();
		return true;
	}
	public String getTitle() {
		try {
			String title = webDriver().getTitle();
			title = HtmlTextUtility.spacesToSingleSpace(HtmlTextUtility.nonBreakingSpaceToSpace(title));
			diagnostics.checkForShow();
			return title;
		} catch (Exception e) {
			throw new FitLibraryException(e.getMessage());
		}
	}
	public String getUrl() {
		try {
			return fixFileUrlProblem(shortUrl());
		} catch (NullPointerException e) {
			throw new FitLibraryException("Unavailable");
		}
	}
	public void pollUrl(final Row row, final TestResults testResults) throws Exception {
		final String url = row.text(1, spiderFixture);
		final CalledMethodTarget target = spiderFixture.findMethodFromRow(row, 3, 4);

		ensureWithError(new PollForWithError() {
			public String error() {
				return "Condition not satisfied";
			}
			public boolean matches() {
				try {
					webDriver().get(url);
					Object result = target.invokeForSpecial(row.rowFrom(4),
							testResults, true, row.cell(0));
					if (result instanceof Boolean) {
						return ((Boolean) result).booleanValue();
					}
				} catch (Exception e) {
					throw new FitLibraryException(e.getMessage());
				}
				throw new FitLibraryException(
						"Can only use an action that returns a boolean");
			}
		}, POLL_URL_TIMEOUT);
		row.cell(1).pass(testResults);
	}
	// DIAGNOSTICS
	public void showErrorDiagnosticsAtWhenPageContains(String xpath,
			String pattern) {
		diagnostics.setUp(xpath, pattern);
	}
	public void checkForError() {
		diagnostics.checkForShow();
	}
	private String shortUrl() {
		String url = "";
		try {
			url = webDriver().getCurrentUrl();
			int pos = url.indexOf(";");
			if (pos >= 0) {
				return " at " + url.substring(0, pos);
			}
		} catch (Exception e) {
			//
		}
		return url;
	}
	private String fixFileUrlProblem(String currentUrl) {
		if (currentUrl.startsWith("file:/")) {
			return "file:///" + currentUrl.substring(6);
		}
		return currentUrl;
	}
}
