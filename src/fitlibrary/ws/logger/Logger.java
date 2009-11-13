/*
 * Copyright (c) 2009 Rick Mugridge, www.RimuResearch.com
 * Kindly donated by Air New Zealand in October 2009.
 * Released under the terms of the GNU General Public License version 2 or later.
*/
package fitlibrary.ws.logger;

import fitlibrary.ws.message.HttpMessage;

public interface Logger {
	void error(String s);
	void log(String s);
	void responded(String context, HttpMessage request, HttpMessage response, int portNo);
	void unused(int portNo, String expected);
}
