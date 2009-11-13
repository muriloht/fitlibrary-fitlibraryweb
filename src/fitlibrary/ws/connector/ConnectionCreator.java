/*
 * Copyright (c) 2009 Rick Mugridge, www.RimuResearch.com
 * Kindly donated by Air New Zealand in October 2009.
 * Released under the terms of the GNU General Public License version 2 or later.
*/
package fitlibrary.ws.connector;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

public interface ConnectionCreator {
	URLConnection openConnection(URL url) throws IOException;
}
