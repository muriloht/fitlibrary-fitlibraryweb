/*
 * Copyright (c) 2010 Rick Mugridge, www.RimuResearch.com
 * Released under the terms of the GNU General Public License version 2 or later.
*/

package fitlibrary.ws.soap;

import fitlibrary.ws.message.ContentType;

public class Soap {
	public static final String HEADER11 = "<?xml version='1.0' encoding='utf-8'?>\n"+
		"<soap:Envelope xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance'"+
		" xmlns:soap='http://schemas.xmlsoap.org/soap/envelope/'>\n"+
		"<soap:Body>\n";
	public static final String TRAILER11 = "</soap:Body>\n</soap:Envelope>\n";

	public static final String HEADER12 = "<?xml version='1.0' encoding='utf-8'?>\n"+
		"<soap12:Envelope xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance'"+
		" xmlns:xsd='http://www.w3.org/2001/XMLSchema'"+
		" xmlns:soap12='http://www.w3.org/2003/05/soap-envelope'>\n"+
		"<soap12:Body>\n";
	public static final String TRAILER12 = "</soap12:Body>\n</soap12:Envelope>\n";
	
	public static String wrap11(String s) {
		return HEADER11+s+TRAILER11;
	}
	public static String wrap12(String s) {
		return HEADER12+s+TRAILER12;
	}
	
	public static String unwrap11(String response) {
		return unwrap(response, "<soap:Body>", "</soap:Body>");
	}
	public static String unwrap12(String response) {
		return unwrap11(unwrap(response, "<soap12:Body>", "</soap12:Body>"));
	}

	private static String unwrap(String response, String header, String trailer) {
		int headerPos = response.indexOf(header);
		if (headerPos < 0)
			return response;
		int trailerPos = response.indexOf(trailer);
		if (trailerPos < 0)
			return response;
		return response.substring(headerPos+header.length(),trailerPos);
	}
	public static ContentType decodedType(String contentType) {
		if (contentType.equals("application/x-www-form-urlencoded"))
			return ContentType.PLAIN;
		if (contentType.equals("text/xml"))
			return ContentType.SOAP11;
		if (contentType.equals("application/soap+xml"))
			return ContentType.SOAP12;
		return ContentType.INVALID;
	}

}
