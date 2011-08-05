package fitlibrary.spider.utility;

import java.io.StringReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.xerces.dom.DocumentImpl;
import org.cyberneko.html.parsers.DOMFragmentParser;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.gargoylesoftware.htmlunit.javascript.host.Node;

import fitlibrary.exception.FitLibraryException;

public class HtmlTextUtility {
	private static final String UNICODE_NON_BREAKING_SPACE = "\u00A0";

	public static String brToSpace(String s) {
		return replaceBr(s, " "); 
	}
	private static String replaceBr(String stringWithBrTag, String replacement) {
		return stringWithBrTag.replaceAll("<(br|BR)\\s?\\/?>", " ");
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
	public static String lowerCaseTags(String html) {
		Pattern patt = Pattern.compile("(</?[A-Z,0-9]+/?>)", Pattern.DOTALL);
		Matcher m = patt.matcher(html);
		StringBuffer sb = new StringBuffer(html.length());
		while (m.find()) {
			String text = m.group(1);
			text = text.toLowerCase();
			m.appendReplacement(sb, Matcher.quoteReplacement(text));
		}
		m.appendTail(sb);
		return sb.toString();
	}
	public static String removeInnerHtml(String stringWithInnerHtml) {
		DOMFragmentParser parser = new DOMFragmentParser();

		try {
			// tell the parser we are working only with a fragment for input
			parser.setFeature("http://cyberneko.org/html/features/document-fragment",true);

			Document document = new DocumentImpl();
			DocumentFragment fragment = document.createDocumentFragment();

			// parse the document into a fragment
			parser.parse(new InputSource(new StringReader(stringWithInnerHtml)), fragment);

			// if the fragment has no child nodes then it has not text as the text(s) of this fragment are also nodes
			if (fragment.hasChildNodes()) {
				StringBuilder content = new StringBuilder();
				NodeList childNodesAndFragmentText = fragment.getChildNodes();
				for (int node = 0; node < childNodesAndFragmentText.getLength(); node++) {
					if (childNodesAndFragmentText.item(node).getNodeType()==Node.TEXT_NODE) {
						content.append(childNodesAndFragmentText.item(node).getTextContent());
					}
				}
				return content.toString();
			}
			return "";
		} catch (Exception e) {
			throw new FitLibraryException(e);
		}
	}
	public static String tagless(String text) {
		String s = text;
		while (true) {
			int pos = s.indexOf("  ");
			if (pos < 0) {
				break;
			}
			s = s.substring(0, pos) + s.substring(pos + 1);
		}
		while (true) {
			int pos = s.indexOf("<");
			if (pos < 0) {
				break;
			}
			int endPos = s.indexOf(">", pos);
			if (endPos < 0) {
				break;
			}
			s = s.substring(0, pos) + s.substring(endPos + 1);
		}
		return s;
	}
}
