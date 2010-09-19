/*
 * Copyright (c) 2009 Rick Mugridge, www.RimuResearch.com
 * Kindly donated by Air New Zealand in October 2009.
 * Released under the terms of the GNU General Public License version 2 or later.
*/
package fitlibrary.mockWebServices.requestMatcher;

import fitlibrary.ws.message.HttpMessage;
import fitlibrary.xml.XmlDoFixture;

public class XmlRequestMatcher extends FussyRequestMatcher {
	private String xml;

	public XmlRequestMatcher(String xml) {
		this.xml = xml;
	}
	@Override
	public String getExpected() {
		return "to match XML("+xml+")";
	}
	@Override
	public boolean match(HttpMessage request) {
		XmlDoFixture xmlDoFixture = new XmlDoFixture();
		xmlDoFixture.nameSpace("soap");
		try {
			return xmlDoFixture.xmlSameAs(xml,request.getContents());
		} catch (Exception e) {
			return false;
		}
	}
}
