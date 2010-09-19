/*
 * Copyright (c) 2010 Rick Mugridge, www.RimuResearch.com
 * Released under the terms of the GNU General Public License version 2 or later.
*/

package fitlibrary.ws.server;

import org.apache.http.client.methods.HttpPost;

public class WebService extends HttpClientService {
	@Override
	protected void addExtraHeadersToPost(HttpPost post) {
		post.setHeader("SOAPAction","\"http://tempuri.org\"");
	}
}
