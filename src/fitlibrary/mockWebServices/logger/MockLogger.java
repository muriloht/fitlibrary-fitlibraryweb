/*
 * Copyright (c) 2009 Rick Mugridge, www.RimuResearch.com
 * Kindly donated by Air New Zealand in October 2009.
 * Released under the terms of the GNU General Public License version 2 or later.
*/
package fitlibrary.mockWebServices.logger;

import fitlibrary.ws.logger.Logger;

public interface MockLogger extends Logger {
	boolean hasErrors();
	int errorCount();
	String report();
}
