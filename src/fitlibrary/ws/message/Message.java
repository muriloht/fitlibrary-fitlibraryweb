/*
 * Copyright (c) 2009 Rick Mugridge, www.RimuResearch.com
 * Kindly donated by Air New Zealand in October 2009.
 * Released under the terms of the GNU General Public License version 2 or later.
*/
package fitlibrary.ws.message;

import java.util.Map;

public abstract class Message implements HttpMessage {
	private Map<String, String> headerMap;
	private String contents;
	private int resultCode;

	public Message(Map<String, String> headerMap, String contents) {
		this(200,headerMap,contents);
	}
	public Message(int resultCode, Map<String, String> headerMap, String contents) {
		this.resultCode = resultCode;
		this.headerMap = headerMap;
		this.contents = contents;
	}
	public Map<String, String> getHeaderMap() {
		return headerMap;
	}
	public String getUri() {
		return "";
	}
	public String getContents() {
		return contents;
	}
	public String getHeader() {
		StringBuilder s = new StringBuilder();
		for (String key : headerMap.keySet())
			s.append(key+":"+headerMap.get(key)+"\n");
		s.append("\n");
		return s.toString();
	}
	public boolean isOK() {
		return true;
	}
	public int getResultCode() {
		return resultCode;
	}
}
