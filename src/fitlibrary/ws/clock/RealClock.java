/*
 * Copyright (c) 2009 Rick Mugridge, www.RimuResearch.com
 * Kindly donated by Air New Zealand in October 2009.
 * Released under the terms of the GNU General Public License version 2 or later.
*/
package fitlibrary.ws.clock;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;


public class RealClock implements Clock {
	@Override
	public String dateTime() {
		DateTimeFormatter format = DateTimeFormat.forPattern("HH:mm:ss");
		return new DateTime().toString(format);
	}
}
