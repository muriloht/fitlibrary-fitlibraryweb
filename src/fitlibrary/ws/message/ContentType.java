/*
 * Copyright (c) 2009 Rick Mugridge, www.RimuResearch.com
 * Kindly donated by Air New Zealand in October 2009.
 * Released under the terms of the GNU General Public License version 2 or later.
*/
package fitlibrary.ws.message;

public enum ContentType { 
	XML("text/xml","utf-8"), 
	SOAP11("text/xml","utf-8"), 
	SOAP12("application/soap+xml","utf-8"), 
	PLAIN("application/x-www-form-urlencoded","utf-8"),
	INVALID("","");
	private final String contentType;
	private final String charset;

	private ContentType(String type, String charset) {
		this.contentType = type;
		this.charset  = charset;
	}
	public String getContentType() {
		return contentType;
	}
	public String getCharset() {
		return charset;
	}
	public String getWholeType() {
		return contentType+";charset="+charset;
	}
}
