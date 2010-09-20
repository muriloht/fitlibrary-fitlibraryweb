/*
 * Copyright (c) 2009 Rick Mugridge, www.RimuResearch.com
 * Kindly donated by Air New Zealand in October 2009.
 * Released under the terms of the GNU General Public License version 2 or later.
*/
package fitlibrary.ws.message;

public class PostMessage extends Message {
	private final String uri;
	private final ContentType type;

	public PostMessage(String uri, String contents, ContentType contentType) {
		this(200,uri,contents,contentType);
	}
	public PostMessage(int resultCode, String uri, String contents, ContentType type) {
		super(resultCode,contents);
		this.type = type;
		this.uri = uri;
	}
	@Override
	public String getUri() {
		return uri;
	}
	public ContentType getContentType() {
		return type;
	}
}
