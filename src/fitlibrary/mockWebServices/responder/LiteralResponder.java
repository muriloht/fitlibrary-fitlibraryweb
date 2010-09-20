/*
 * Copyright (c) 2009 Rick Mugridge, www.RimuResearch.com
 * Kindly donated by Air New Zealand in October 2009.
 * Released under the terms of the GNU General Public License version 2 or later.
*/
package fitlibrary.mockWebServices.responder;

import fitlibrary.ws.message.ContentType;

public class LiteralResponder extends SimpleResponder {
	private String response;

	public LiteralResponder(String response, ContentType contentType) {
		super(contentType);
		this.response = response;
	}
	public LiteralResponder(int resultCode, String contents, ContentType contentType) {
		super(resultCode,contentType);
		this.response = contents;
	}
	@Override
	public String getContents() {
		return response;
	}
	@Override
	public boolean isOK() {
		return true;
	}
	@Override
	public String toString() {
		return "LiteralResponder["+getContents()+"]";
	}
}
