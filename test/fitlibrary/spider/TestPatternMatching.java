/*
 * Copyright (c) 2009 Rick Mugridge, www.RimuResearch.com
 * Kindly donated by Air New Zealand in October 2009.
 * Released under the terms of the GNU General Public License version 2 or later.
*/
package fitlibrary.spider;

import java.util.regex.Pattern;

import org.junit.Assert;
import org.junit.Test;

public class TestPatternMatching {
	@Test
	public void regular() {
		Assert.assertTrue(Pattern.matches(".*Descr.ption.*","<tr><td><div class=\"centered\"><a href=\"FitNesse.OneMinuteDescription\">A One-Minute Description</a></div>"));
	}
}
