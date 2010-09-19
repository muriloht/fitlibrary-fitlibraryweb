/*
 * Copyright (c) 2009 Rick Mugridge, www.RimuResearch.com
 * Kindly donated by Air New Zealand in October 2009.
 * Released under the terms of the GNU General Public License version 2 or later.
*/
package fitlibrary.mockWebServices.responder;

public class LiteralResponder extends SimpleResponder {
	private String response;

	public LiteralResponder(String response) {
		this.response = response;
	}
	public LiteralResponder(int resultCode, String contents) {
		super(resultCode);
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
