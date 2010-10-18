package fitlibrary.spider.element;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openqa.selenium.WebElement;

import com.sun.corba.se.pept.transport.ContactInfo;

import fit.Parse;
import fitlibrary.spider.AbstractSpiderFixture;
import fitlibrary.spider.MultiLineMatchFixture;
import fitlibrary.spider.polling.PollForWithError;
import fitlibrary.spider.utility.StringUtility;

public class TextElement extends SpiderElement {

	public TextElement(AbstractSpiderFixture spiderFixture) {
		super(spiderFixture);
	}
	public String textOf(String locator) {
		return collectText(findElement(locator));
	}
	public String plainTextOf(String locator) {
		return tagless(collectText(findElement(locator), true));
	}
	public String innerHtmlOf(String locator) {
		WebElement element = findElement(locator);
		Object escaped = spiderFixture().executeJavaScriptWith("return arguments[0].innerHTML;", element);
		return lowerCaseTags(Parse.unescape(escaped.toString()));
	}
	public MultiLineMatchFixture textOfMatchesLines(String locator) {
		// Doesn't wait for Javascript to alter existing values
		String text = collectText(findElement(locator), false);
		return new MultiLineMatchFixture(collectTaglessLines(text));
	}
	private Object[] collectTaglessLines(String lines) {
		if ("".equals(lines)) {
			return new Object[]{};
		}
		return lines.split("\n");
	}
	public boolean optionallyWithSetText(String locator, String s) {
		if (s.equals("") || s.contains("@{")) {
			return true;
		}
		try {
			withSetText(locator, s);
		} catch (Exception e) {
			// Ignore any problems
		}
		return true;
	}
	public boolean withSetText(final String locator, final String s) {
		final WebElement element = findElement(locator);
		if (!element.isEnabled()) // Due to bugs in HtmlUnit (clears a disabled text field) and in Selenium
			return true;
		element.clear();
		element.sendKeys(whiteSpace(s));
		ensureBecomes(new PollForWithError() {
			@Override
			public boolean matches() {
				return textOf(locator).equals(NLandTABasSpace(s));
			}
			@Override
			public String error() {
				return "Text wasn't changed correctly, it's "+(element.getText().length() >0 ? "'"+element.getText()+"'" : "empty");
			}
		});
		return true;
	}
	public boolean withAddText(final String locator, final String s) {
		final WebElement element = findElement(locator);
		if (!element.isEnabled()) {
			return true;
		}
		element.sendKeys(whiteSpace(s));
		ensureBecomes(new PollForWithError() {
			@Override
			public boolean matches() {
				return textOf(locator).endsWith(whiteSpace(s));
			}
			@Override
			public String error() {
				return "Text wasn't changed correctly, it's '"
						+ element.getText() + "'";
			}
		});
		return true;
	}
	public String collectText(WebElement element) {
		return collectText(element, true);
	}
	private String collectText(WebElement element, boolean trim) {
		String value = element.getText();
		if (value == null || "".equals(value.trim())) {
			try {
				value = element.getValue();
			} catch(UnsupportedOperationException uso) {
				// re throw unless exception is about missing value attribute then we just handle gracefully..
				if (!uso.getMessage().contains("Element does not have a value attribute")) {
					throw uso; 
				}
			} 
		}
		if (value == null) {
			value = "";
		}
		if (trim) {
			value = crLfRemoved(
					spacesToSingleSpace(nonBreakingSpaceToSpace(tabToSpace(brToSpace(value)))))
					.trim();
		}
		return Parse.unescape(value);
	}
	public static String brToSpace(String s) {
		return s.replaceAll("<br\\/?>", " ");
	}
	protected static String whiteSpace(String s) {
		return StringUtility.replaceAll(s, "\\n", "\n").replaceAll("\\t", "\t");
	}
	protected static String NLandTABasSpace(String s) {
		return StringUtility.replaceAll(s, "\\n", " ").replaceAll("\\t", " ");
	}
	private String tagless(String text) {
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
	private String lowerCaseTags(String html) {
		Pattern patt = Pattern.compile("(</?[A-Z]+/?>)", Pattern.DOTALL);
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
	public static String crLfRemoved(String s) {
		return s.replaceAll("\\r?\\n", "");
	}
	public static String spacesToSingleSpace(String s) {
		return s.replaceAll("\\s{2,}", " ");
	}
	public static String nonBreakingSpaceToSpace(String s) {
		return s.replaceAll("\\&nbsp\\;", " ");
	}
	public static String tabToSpace(String s) {
		return s.replaceAll("\\t", " ");
	}
}
