/*
 * Copyright (c) 2009 Rick Mugridge, www.RimuResearch.com
 * Kindly donated by Air New Zealand in October 2009.
 * Released under the terms of the GNU General Public License version 2 or later.
*/
package fitlibrary.ws.message;

import java.util.HashMap;
import java.util.Map;

public class PostMessage extends Message {
	private final String uri;
	private final ContentType type;

	public PostMessage(String uri, String contents, ContentType contentType) {
		this(uri,new HashMap<String,String>(),contents,contentType);
	}
	public PostMessage(String uri, Map<String, String> headerMap, String contents, ContentType type) {
		this(200,uri,headerMap,contents,type);
	}
	public PostMessage(int resultCode, String uri, Map<String, String> headerMap, String contents, ContentType type) {
		super(resultCode,headerMap,contents);
		this.type = type;
		this.uri = uri;
	}
	@Override
	public String getUri() {
		return uri;
	}
	@Override
	public String getHeader() {
		return "POST "+uri+"\n"+super.getHeader();
	}
	public ContentType getContentType() {
		return type;
	}
}
