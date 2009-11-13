/*
 * Copyright (c) 2009 Rick Mugridge, www.RimuResearch.com
 * Kindly donated by Air New Zealand in October 2009.
 * Released under the terms of the GNU General Public License version 2 or later.
*/
package fitlibrary.spider.specify;

import fitlibrary.DoFixture;
import fitlibrary.spider.MultiLineMatchFixture;
import fitlibrary.utility.StringUtility;

public class SpecifyMatchingStringFixture extends DoFixture {
	public MultiLineMatchFixture match(String s) {
		if ("".equals(s.trim()))
			return new MultiLineMatchFixture(new Object[]{});
		String s1 = StringUtility.replaceString(s, "\\n", "\n");
		return new MultiLineMatchFixture(s1.split("\n"));
	}
}
