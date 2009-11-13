/*
 * Copyright (c) 2009 Rick Mugridge, www.RimuResearch.com
 * Kindly donated by Air New Zealand in October 2009.
 * Released under the terms of the GNU General Public License version 2 or later.
*/
package fitlibrary.ws.message;

public enum ContentType { 
	XML("text/xml","UTF-8"), 
	PLAIN("text/plain","utf-8");
	private final String type;
	private final String charset;

	private ContentType(String type, String charset) {
		this.type = type;
		this.charset  = charset;
	}
	public String getType() {
		return type;
	}
	public String getCharset() {
		return charset;
	}
	public String getWholeType() {
		return type+";charset="+charset;
	}
}
