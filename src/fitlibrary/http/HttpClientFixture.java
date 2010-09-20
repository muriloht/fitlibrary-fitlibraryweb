/*
 * Copyright (c) 2010 Rick Mugridge, www.RimuResearch.com
 * Released under the terms of the GNU General Public License version 2 or later.
*/

package fitlibrary.http;

import fitlibrary.ws.client.HttpClientService;

public class HttpClientFixture extends HttpClientService {
	public void httpPostToWithContentTypeAndContentEncoding(String url, String contents, String contentType, String contentEncoding) {
		httpPost(url,contents,contentType,contentEncoding);
	}
	public void httpPostTextTo(String url, String contents) {
		httpPostToWithContentTypeAndContentEncoding(url,contents,"text/plain","utf-8");
	}
	public void httpPostXmlTo(String url, String contents) {
		httpPostToWithContentTypeAndContentEncoding(url,contents,"text/xml","utf-8");
	}
	public void httpPostHtmlTo(String url, String contents) {
		httpPostToWithContentTypeAndContentEncoding(url,contents,"text/html","utf-8");
	}
}
