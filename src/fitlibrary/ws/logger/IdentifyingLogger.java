/*
 * Copyright (c) 2009 Rick Mugridge, www.RimuResearch.com
 * Kindly donated by Air New Zealand in October 2009.
 * Released under the terms of the GNU General Public License version 2 or later.
*/
package fitlibrary.ws.logger;

import fitlibrary.ws.message.HttpMessage;

public class IdentifyingLogger implements Logger {
	private final Logger logger;
	private final String name;

	public IdentifyingLogger(Logger logger, String name) {
		this.logger = logger;
		this.name = name+": ";
	}
	@Override
	public void error(String s) {
		logger.error(name+s);
	}
	@Override
	public void log(String s) {
		logger.log(name+s);
	}
	@Override
	public void responded(String context, HttpMessage request, HttpMessage response, int portNo) {
		logger.responded(context,request,response,portNo);
	}
	@Override
	public void unused(int portNo, String expected) {
		logger.unused(portNo,expected);
	}
}
