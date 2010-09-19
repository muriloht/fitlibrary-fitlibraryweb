/*
 * Copyright (c) 2009 Rick Mugridge, www.RimuResearch.com
 * Kindly donated by Air New Zealand in October 2009.
 * Released under the terms of the GNU General Public License version 2 or later.
*/
package fitlibrary.ws.message;


public abstract class Message implements HttpMessage {
	private String contents;
	private int resultCode;

	public Message(int resultCode, String contents) {
		this.resultCode = resultCode;
		this.contents = contents;
	}
	@Override
	public String getUri() {
		return "";
	}
	@Override
	public String getContents() {
		return contents;
	}
	@Override
	public boolean isOK() {
		return true;
	}
	@Override
	public int getResultCode() {
		return resultCode;
	}
}
