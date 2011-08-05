/*
 * Copyright (c) 2009 Rick Mugridge, www.RimuResearch.com
 * Kindly donated by Air New Zealand in October 2009.
 * Released under the terms of the GNU General Public License version 2 or later.
*/
package fitlibrary.spider.utility;

import static org.junit.Assert.*;

import org.junit.Assert;
import org.junit.Test;

import fit.Fixture;
import fitlibrary.spider.utility.HtmlTextUtility;

public class TestHtmlTextUtility
{
	@Test
	public void tagIsEscaped() {
		Assert.assertEquals("&lt;a>",Fixture.escape("<a>"));
	}
	@Test
	public void nonBreakingSpaceIsEscaped() {
		Assert.assertEquals("&amp;nbsp;",Fixture.escape("&nbsp;"));
	}
	@Test
	public void breakIsRemoved() {
		Assert.assertEquals("a b c d",HtmlTextUtility.brToSpace("a<br>b<br/>c<br />d"));
	}
	@Test
	public void breakIsRemovedWhenUpperCase() {
		Assert.assertEquals("a b c d",HtmlTextUtility.brToSpace("a<BR>b<BR/>c<BR />d"));
	}
	@Test
	public void crAndLfRemoved() {
		Assert.assertEquals("abb",HtmlTextUtility.crLfRemoved("a\r\nb\nb"));
	}
	@Test
	public void nonBreakingSpaceToSpace() {
		Assert.assertEquals("a ba b",HtmlTextUtility.nonBreakingSpaceToSpace("a&nbsp;ba&nbsp;b"));
	}
	@Test
    public void unicodeNonBreakingSpaceTospace()  {
        String converted = HtmlTextUtility.nonBreakingSpaceToSpace("hello\u00A0world\u00A0.");
		assertEquals("hello world .",converted);
    }
	@Test
	public void spacesToSingleSpace() {
		assertEquals("a b c",HtmlTextUtility.spacesToSingleSpace("a    b  c"));
	}
	@Test
	public void tabToSpace() {
		assertEquals("a b c",HtmlTextUtility.tabToSpace("a\tb\tc"));
	}
	@Test
	public void lowerCaseTagsWhenNoText() {
		assertEquals("", HtmlTextUtility.lowerCaseTags(""));
	}
	@Test
	public void lowerCaseTagsWhenNoTags() {
		assertEquals("DO NOT TOUCH ME", HtmlTextUtility.lowerCaseTags("DO NOT TOUCH ME"));
	}
	@Test
	public void lowerCaseTagsWhenTagOnly() {
		assertEquals("<table>", HtmlTextUtility.lowerCaseTags("<TABLE>"));
	}
	@Test
	public void lowerCaseTagsWithNumbers() {
		assertEquals("<h1>", HtmlTextUtility.lowerCaseTags("<H1>"));
	}
	@Test
	public void lowerCaseClosingTag() {
		assertEquals("</table>", HtmlTextUtility.lowerCaseTags("</TABLE>"));
	}
	@Test
	public void lowerCaseTagsWhenMultipleTagsAndText() {
		assertEquals("left<div><span>text<div>more</div>some</span></div>right.", HtmlTextUtility.lowerCaseTags("left<DIV><SPAN>text<div>more</div>some</SPAN></DIV>right."));
	}
	@Test
	public void removeInnerHtmlWhenEmptyContent() {
		assertEquals("", HtmlTextUtility.removeInnerHtml(""));
	}
	@Test
	public void removeInnerHtmlWhenNoInnerHtml() {
		assertEquals("foo", HtmlTextUtility.removeInnerHtml("foo"));
	}
	@Test
	public void removeInnerHtmlWhenSimpleInnerHtml() {
		assertEquals("foo", HtmlTextUtility.removeInnerHtml("foo<span>bar</span>"));
	}
	@Test
	public void removeInnerHtmlWhenTagsHaveNumbers() {
		assertEquals("bigtext", HtmlTextUtility.removeInnerHtml("big<h4>small</h4>text"));
	}
	@Test
	public void removeInnerHtmlWhenTextOnBothSidesOfInnerHtml() {
		assertEquals("foobaz", HtmlTextUtility.removeInnerHtml("foo<span>bar</span>baz"));
	}
	@Test
	public void removeInnerHtmlWhenUpperCase() {
		assertEquals("foobaz", HtmlTextUtility.removeInnerHtml("foo<SPAN>bar</SPAN>baz"));
	}
	@Test
	public void removeInnerHtmlWhenOnlyInnerHtml() {
		assertEquals("", HtmlTextUtility.removeInnerHtml("<span>deleteme</span>"));
	}
	@Test
	public void removeInnerHtmlWhenComplexInnerHtml() {
		assertEquals("foobaz",HtmlTextUtility.removeInnerHtml("foo<span>top<div>middle<br><span>bottom</span></div>top2</span>baz"));
	}
	@Test
	public void whenTagsHaveAttributes() {
		assertEquals("Los Angeles",HtmlTextUtility.removeInnerHtml("<div class=\"iopsm\"><span class=\"bolded\">9:15 PM</span> Mon 2nd</div>Los Angeles<br>"));
	}
	@Test
	public void removeInnerHtmlWhenTwoBlocksOfInnerHtml() {
		assertEquals("foobarbaz",HtmlTextUtility.removeInnerHtml("foo<span>delete</span>bar<span>me</span>baz"));
		assertEquals("foobarbaz",HtmlTextUtility.removeInnerHtml("foo<span>delete</span>bar<div>me</div>baz"));
	}
	@Test
	public void removeInnerHtmlWhenCharsThatLookLikeTagsButArent() {
		assertEquals("if cat<hat or hat>cat", HtmlTextUtility.removeInnerHtml("if cat<hat or hat>cat"));
	}
	@Test
	public void removeInnerHtmlWOhenCharsThatLookLikeTagsButArentWithInnerHtml() {
		assertEquals("if cat <hat or hat>cat", HtmlTextUtility.removeInnerHtml("if cat <hat<span>ignore</span> or hat>cat"));
	}
	@Test
	public void taglessWhenEmptyStringReturnsEmptyString() {
		assertEquals("", HtmlTextUtility.tagless(""));
	}
	@Test
	public void taglessWhenNoTagsJustReturnsText() {
		assertEquals("no TEXT here", HtmlTextUtility.tagless("no TEXT here"));
	}
	@Test
	public void taglessWhenOnlyTheTagReturnsEmptyString() {
		assertEquals("", HtmlTextUtility.tagless("<br>"));
	}
	@Test
	public void taglessWhenTagsAndTextREturnsOnlyTheText() {
		assertEquals("big small italic text", HtmlTextUtility.tagless("big <h4>small</h4><i> italic</i> text"));
	}
	@Test
	public void taglessWhenTextLooksLikeTagsButItIsnt() {
		assertEquals("big small italic text", HtmlTextUtility.tagless("big <h4>small</h4><i> italic</i> text"));
	}
}
