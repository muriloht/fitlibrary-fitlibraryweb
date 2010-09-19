package fitlibrary.spider.component;

import java.util.regex.Pattern;

import fitlibrary.exception.FitLibraryException;
import fitlibrary.spider.SpiderFixture;
import fitlibrary.spider.polling.PollForMatches;
import fitlibrary.spider.utility.HtmlTextUtility;

public class TextInPage extends SpiderComponent {
	public TextInPage(SpiderFixture spiderFixture) {
		super(spiderFixture);
	}
	public boolean pageContainsText(final String text) {
		try {
			boolean match = ensureMatches(new PollForMatches() {
				@Override
				public boolean matches() {
					return passed(HtmlTextUtility.crLfRemoved(pageSource()).contains(text),
							text, text);
				}
			});
			showAfterTable("This action is deprecated. " + "Instead, use: "
					+ deprecate("<i>page source</i>", "<b>contains</b>", text));
			return match;
		} catch (NullPointerException e) {
			throw problem("Unavailable", text, text);
		}
	}
	private String deprecate(String... ss) {
		String result = "<table><tr>";
		for (String s : ss)
			result += "<td>" + s + "</td>";
		return result + "</tr></table>";
	}
	public boolean pageContainsTextIgnoreHtmlBreakingTokens(final String text) {
		try {
			boolean ensureMatches = ensureMatches(new PollForMatches() {
				@Override
				public boolean matches() {
					String src = pageSourceCleaned();

					return passed(src.contains(text), text, text);
				}
			});
			showAfterTable("This action is deprecated. "
					+ "Instead, use: "
					+ deprecate("<i>page source cleaned</i>",
							"<b>eventually matches</b>", text));
			return ensureMatches;
		} catch (NullPointerException e) {
			throw problem("Unavailable", text, text);
		}
	}
	public boolean pageContainsRegularExpression(final String regEx) {
		boolean ensureMatches = ensureMatches(new PollForMatches() {
			@Override
			public boolean matches() {
				return patternMatchesAcrossLines(pageSource(), regEx);
			}
		});
		showAfterTable("This action is deprecated. "
				+ "Instead, use: "
				+ deprecate("<i>page source</i>", "<b>eventually matches</b>",
						regEx));
		return ensureMatches;
	}
	public String pageSubstringFromTo(String prior, String subsequent) {
		String src = pageSource();
		int start = src.indexOf(prior);
		if (start < 0)
			return "";
		start += prior.length();
		int end = src.indexOf(subsequent, start);
		if (end < 0) {
			return "";
		}
		return src.substring(start, end);
	}
	public String pageSource() {
		try {
			return HtmlTextUtility.crLfRemoved(webDriver().getPageSource());
		} catch (Exception e) {
			return "<i>Page Source is unavailable</i>";
		}
	}
	public String pageSourceCleaned() {
		return HtmlTextUtility.spacesToSingleSpace(HtmlTextUtility.brToSpace(HtmlTextUtility.nonBreakingSpaceToSpace(pageSource())));
	}
	protected boolean patternMatchesAcrossLines(String text, String regEx) {
		return Pattern.compile(".*" + regEx + ".*", Pattern.DOTALL).matcher(
				text).matches();
	}
	protected boolean passed(boolean passed, String expected,
			String resolvedTitle) {
		if (passed) {
			return true;
		}
		if (expected.equals(resolvedTitle)) {
			return false;
		}
		throw new FitLibraryException("Failed with: '" + resolvedTitle + "'");
	}
}
