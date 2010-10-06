/*
 * Copyright (c) 2010 Rick Mugridge, www.RimuResearch.com
 * Released under the terms of the GNU General Public License version 2 or later.
*/

package fitlibrary.ws.client;

import org.apache.http.client.methods.HttpPost;

public class WebService extends HttpClientService {
//	private static Logger logger = FixturingLogger.getLogger(WebService.class);

	@Override
	protected void addExtraHeadersToPost(HttpPost post) {
//		String value = "\"http://tempuri.org/CelsiusToFahrenheit\"";
//		post.setHeader("SOAPAction",value);
//		logger.trace("Add header: 'SOAPAction: "+value);
	}
}
