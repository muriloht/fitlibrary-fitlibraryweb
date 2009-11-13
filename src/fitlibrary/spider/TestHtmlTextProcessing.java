/*
 * Copyright (c) 2009 Rick Mugridge, www.RimuResearch.com
 * Kindly donated by Air New Zealand in October 2009.
 * Released under the terms of the GNU General Public License version 2 or later.
*/
package fitlibrary.spider;

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
		Assert.assertEquals("a b",HtmlTextUtility.brToSpace("a<br>b"));
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
	public void spacesToSingleSpace() {
		Assert.assertEquals("a b c",HtmlTextUtility.spacesToSingleSpace("a    b  c"));
	}
	@Test
	public void tabToSpace() {
		Assert.assertEquals("a b c",HtmlTextUtility.tabToSpace("a\tb\tc"));
	}
}
