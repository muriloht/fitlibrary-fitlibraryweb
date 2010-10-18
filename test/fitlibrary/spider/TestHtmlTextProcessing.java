/*
 * Copyright (c) 2009 Rick Mugridge, www.RimuResearch.com
 * Kindly donated by Air New Zealand in October 2009.
 * Released under the terms of the GNU General Public License version 2 or later.
*/
package fitlibrary.spider;

import static org.junit.Assert.*;

import org.junit.Assert;
import org.junit.Test;

import fit.Fixture;
import fitlibrary.spider.utility.HtmlTextUtility;

public class TestHtmlTextProcessing
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
		Assert.assertEquals("a b c",HtmlTextUtility.spacesToSingleSpace("a    b  c"));
	}
	@Test
	public void tabToSpace() {
		Assert.assertEquals("a b c",HtmlTextUtility.tabToSpace("a\tb\tc"));
	}
}
