/*
 * Copyright (c) 2009 Rick Mugridge, www.RimuResearch.com
 * Kindly donated by Air New Zealand in October 2009.
 * Released under the terms of the GNU General Public License version 2 or later.
*/
package fitlibrary.mockWebServices.clock;

import fitlibrary.ws.clock.Clock;

public class MockClock implements Clock {
	private long time = 0;

	public void setTime(long time) {
		this.time = time;
	}
	public String dateTime() {
		return time + " ";
	}
}
