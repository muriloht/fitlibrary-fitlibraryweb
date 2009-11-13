package fitlibrary.spider.utility;

public class HtmlTextUtility {
	public static String brToSpace(String s) {
		return s.replaceAll("<br\\/?>", " ");
	}
	public static String crLfRemoved(String s) {
		return s.replaceAll("\\r?\\n", "");
	}
	public static String nonBreakingSpaceToSpace(String s) {
		return s.replaceAll("\\&nbsp\\;", " ");
	}
	public static String spacesToSingleSpace(String s) {
		return s.replaceAll("\\s{2,}", " ");
	}
	public static String tabToSpace(String s) {
		return s.replaceAll("\\t", " ");
	}
}
