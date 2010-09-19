/*
 * Copyright (c) 2009 Rick Mugridge, www.RimuResearch.com
 * Kindly donated by Air New Zealand in October 2009.
 * Released under the terms of the GNU General Public License version 2 or later.
*/
package fitlibrary.mockWebServices.transactionFixture;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

import fitlibrary.mockWebServices.MockingWebServices;
import fitlibrary.mockWebServices.requestMatcher.NotRequestMatcher;
import fitlibrary.mockWebServices.requestMatcher.XPathMatcher;
import fitlibrary.mockWebServices.requestMatcher.XmlRequestMatcher;
import fitlibrary.ws.soap.Soap;

public class Soap11TransactionFixture extends AbstractTransactionFixture {
	public Soap11TransactionFixture(int port, MockingWebServices mockingWebServices) {
		super(port,mockingWebServices);
	}
	public void matchesRequest(String xml) {
		requestMatcher = requestMatcher.and(new XmlRequestMatcher(Soap.wrap11(xml)));
	}
	public void matchesRequestFromFile(String fileName) throws IOException {
		matchesRequest(FileUtils.readFileToString(new File(fileName),"utf-8"));
	}
	public void notMatchesRequest(String xml) {
		requestMatcher = requestMatcher.and(new NotRequestMatcher(new XmlRequestMatcher(Soap.wrap11(xml))));
	}
	public void xpathIs(String xpath, String value) {
		requestMatcher = requestMatcher.and(new XPathMatcher(xpath,value));
	}
	@Override
	protected boolean isXml() {
		return true;
	}
}
