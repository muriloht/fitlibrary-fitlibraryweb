/*
 * Copyright (c) 2009 Rick Mugridge, www.RimuResearch.com
 * Kindly donated by Air New Zealand in October 2009.
 * Released under the terms of the GNU General Public License version 2 or later.
*/
package fitlibrary.spider.utility;

public class StringUtility {
	public static String replaceAll(String s, String from, String to) {
		String result = s;
		int pos = 0;
		while (true) {
			pos = result.indexOf(from, pos);
			if (pos < 0)
				return result ;
			result = result.substring(0,pos) + to + result.substring(pos+from.length());
			pos++;
		}
	}
}
