/*
 * Copyright (c) 2009 Rick Mugridge, www.RimuResearch.com
 * Kindly donated by Air New Zealand in October 2009.
 * Released under the terms of the GNU General Public License version 2 or later.
*/
package fitlibrary.mockWebServices.responder;

import fitlibrary.ws.message.ContentType;

public abstract class SimpleResponder implements Responder {
	private ContentType contentType;
	private int resultCode = 200;
	
	public SimpleResponder(int resultCode, ContentType contentType) {
		this.resultCode = resultCode;
		this.contentType = contentType;
	}
	public SimpleResponder(ContentType contentType) {
		this(200,contentType);
	}
	public SimpleResponder() {
		this(ContentType.INVALID);
	}
	@Override
	public String getUri() {
		return "";
	}
	@Override
	public String contentsOf(String line) {
		return line;
	}
	@Override
	public String getContentType() {
		return contentType.getWholeType();
	}
	@Override
	public int getResultCode() {
		return resultCode ;
	}
}
