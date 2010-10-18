package fitlibrary.spider.utility;

public class HtmlTextUtility {
	private static final String UNICODE_NON_BREAKING_SPACE = "\u00A0";
	public static String brToSpace(String s) {
		return s.replaceAll("<br\\/?>", " ");
	}
	public static String crLfRemoved(String s) {
		return s.replaceAll("\\r?\\n", "");
	}
	public static String nonBreakingSpaceToSpace(String s) {
		return s.replaceAll("\\&nbsp\\;", " ").replace(UNICODE_NON_BREAKING_SPACE," ");
	}
	public static String spacesToSingleSpace(String s) {
		return s.replaceAll("\\s{2,}", " ");
	}
	public static String tabToSpace(String s) {
		return s.replaceAll("\\t", " ");
	}
}
