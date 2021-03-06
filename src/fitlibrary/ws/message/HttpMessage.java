/*
 * Copyright (c) 2009 Rick Mugridge, www.RimuResearch.com
 * Kindly donated by Air New Zealand in October 2009.
 * Released under the terms of the GNU General Public License version 2 or later.
*/
package fitlibrary.ws.message;

public interface HttpMessage {
	String getUri();
	String getContents();
	int getResultCode();
	boolean isOK();
}
