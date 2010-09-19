/*
 * Copyright (c) 2009 Rick Mugridge, www.RimuResearch.com
 * Kindly donated by Air New Zealand in October 2009.
 * Released under the terms of the GNU General Public License version 2 or later.
*/
package fitlibrary.mockWebServices.responder;

import java.util.Map;

import fitlibrary.ws.message.HttpMessage;

public interface Responder extends HttpMessage {
	String contentsOf(String line);
	Map<String, String> getHeaders();
	@Override
	int getResultCode();
}
