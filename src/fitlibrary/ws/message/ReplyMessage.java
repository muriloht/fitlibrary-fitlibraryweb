/*
 * Copyright (c) 2009 Rick Mugridge, www.RimuResearch.com
 * Kindly donated by Air New Zealand in October 2009.
 * Released under the terms of the GNU General Public License version 2 or later.
*/
package fitlibrary.ws.message;


public class ReplyMessage extends Message {
	public ReplyMessage(int responseCode, String contents) {
		super(responseCode,contents);
	}
	public ReplyMessage(String contents) {
		this(200,contents);
	}
	@Override
	public boolean isOK() {
		return !getContents().startsWith("HTTP");
	}
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof ReplyMessage))
			return false;
		return getContents().equals(((ReplyMessage)obj).getContents());
	}
	@Override
	public int hashCode() {
		return getContents().hashCode();
	}
}
