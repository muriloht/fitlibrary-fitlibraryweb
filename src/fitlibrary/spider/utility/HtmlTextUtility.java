package fitlibrary.spider.utility;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import fitlibrary.exception.FitLibraryException;

public class HtmlTextUtility {
	private static final String UNICODE_NON_BREAKING_SPACE = "\u00A0";
	private static final Pattern HTML_TAG_PATTERN = Pattern.compile("<([a-z,A-Z]*)>"); 

	public static String brToSpace(String s) {
		return s.replaceAll("<br\\s?\\/?>", " ");
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
	public static String removeInnerHtml(String stringWithInnerHtml) {
		for (;;) {
			Matcher matcher = HTML_TAG_PATTERN.matcher(stringWithInnerHtml);

			if (!matcher.find()) 
				break;
			
			String matchingOpeningTagName = matcher.group(1);
			stringWithInnerHtml =  recurisiveRemoveTag(matchingOpeningTagName, stringWithInnerHtml);
		}
		return stringWithInnerHtml;
	}
	private static String recurisiveRemoveTag(String tagName, String htmltoRemoveFrom) {
		String openingTag = "<"+tagName+">";
		String closingTag = "</"+tagName+">";
		
		int indexOfOpening = htmltoRemoveFrom.indexOf(openingTag);
		
		if (indexOfOpening == -1)
			throw new FitLibraryException("Expected opening tag: "+openingTag+" in content "+htmltoRemoveFrom);
		
		String leftHandSideOfHtmlToKeep = htmltoRemoveFrom.substring(0, indexOfOpening);
		
		htmltoRemoveFrom = htmltoRemoveFrom.substring(indexOfOpening+openingTag.length());
		
		int indexOfClosing = htmltoRemoveFrom.indexOf(closingTag);
		
		// if there is another identical opening tag before our closing then there is more inner html (and we've matched 
		// the wrong closing tag) so recursively remove it and reset our indexOfClosing
		int innerOpeningTagIndex = htmltoRemoveFrom.indexOf(openingTag);
		if (innerOpeningTagIndex >=0 && innerOpeningTagIndex < indexOfClosing) {
			String rightSideofHtmlToKeep = recurisiveRemoveTag(tagName, htmltoRemoveFrom);
			htmltoRemoveFrom = htmltoRemoveFrom.substring(0, indexOfOpening+openingTag.length())+rightSideofHtmlToKeep;
			indexOfClosing = htmltoRemoveFrom.indexOf(closingTag);
		}
		
		if (indexOfClosing == -1)
			throw new FitLibraryException("Expected closing tag: "+closingTag+" in content "+htmltoRemoveFrom);
			
		return leftHandSideOfHtmlToKeep+htmltoRemoveFrom.substring(indexOfClosing+closingTag.length());
	}
}
