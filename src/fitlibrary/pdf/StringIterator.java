/*
 * Copyright (c) 2010 Rick Mugridge, www.RimuResearch.com
 * Released under the terms of the GNU General Public License version 2 or later.
*/

package fitlibrary.pdf;

import java.util.Iterator;

public class StringIterator implements Iterator<String>{
	private final String s;
	private int pos = 0;

	public StringIterator(String s) {
		this.s = s;
	}
	@Override
	public boolean hasNext() {
		return pos < s.length();
	}
	@Override
	public String next() {
		pos ++;
		return s.substring(pos-1,pos);
	}
	@Override
	public void remove() {
		throw new RuntimeException("Unable to remove from an immutable string");
	}
}
