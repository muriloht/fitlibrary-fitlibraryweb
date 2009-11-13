/*
 * Copyright (c) 2009 Rick Mugridge, www.RimuResearch.com
 * Kindly donated by Air New Zealand in October 2009.
 * Released under the terms of the GNU General Public License version 2 or later.
*/
package fitlibrary.mockWebServices.responder;

public class ErrorResponder extends SimpleResponder {
	private static final ErrorResponder ERROR = new ErrorResponder(); // We only need one
	public static String ERROR_RESPONSE = "No matching request.\r\n\r\n";
	
	public static ErrorResponder create() {
		return ERROR;
	}
	private ErrorResponder() {
		//
	}
	public String getContents() {
		return ERROR_RESPONSE;
	}
	public boolean isOK() {
		return false;
	}
	@Override
	public String toString() {
		return "ErrorResponder";
	}
	@Override
	public int getResultCode() {
		return 404;
	}
}
