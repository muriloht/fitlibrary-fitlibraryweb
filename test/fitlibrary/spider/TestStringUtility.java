/*
 * Copyright (c) 2009 Rick Mugridge, www.RimuResearch.com
 * Kindly donated by Air New Zealand in October 2009.
 * Released under the terms of the GNU General Public License version 2 or later.
*/
package fitlibrary.spider;

import org.junit.Assert;
import org.junit.Test;

import fitlibrary.spider.utility.StringUtility;


public class TestStringUtility {
	@Test
	public void empty() {
		Assert.assertEquals("",StringUtility.replaceAll("", "from", "to"));
	}
	@Test
	public void singleMatch() {
		Assert.assertEquals("to",StringUtility.replaceAll("from", "from", "to"));
	}
	@Test
	public void singleMismatch() {
		Assert.assertEquals("from",StringUtility.replaceAll("from", "froM", "to"));
	}
	@Test
	public void doubleMatch() {
		Assert.assertEquals("to-to",StringUtility.replaceAll("from-from", "from", "to"));
	}
	@Test
	public void rematch() {
		Assert.assertEquals("from",StringUtility.replaceAll("ffrom", "from", "rom"));
	}
	@Test
	public void notForever() {
		Assert.assertEquals("from",StringUtility.replaceAll("from", "from", "from"));
	}
	@Test
	public void nl() {
		Assert.assertEquals("from\nfrom",StringUtility.replaceAll("from\\nfrom", "\\n", "\n"));
	}
	@Test
	public void notMatchAgain() {
		Assert.assertEquals("from",StringUtility.replaceAll("fromrom", "from", "f"));
	}
}
