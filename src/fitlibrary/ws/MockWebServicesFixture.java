/*
 * Copyright (c) 2009 Rick Mugridge, www.RimuResearch.com
 * Kindly donated by Air New Zealand in October 2009.
 * Released under the terms of the GNU General Public License version 2 or later.
*/
package fitlibrary.ws;

import java.util.HashMap;
import java.util.Map;

import org.custommonkey.xmlunit.SimpleNamespaceContext;
import org.custommonkey.xmlunit.XMLUnit;

import fitlibrary.mockWebServices.MockingWebServices;
import fitlibrary.mockWebServices.logger.MockLogger;
import fitlibrary.mockWebServices.transactionFixture.SoapTransactionFixture;
import fitlibrary.mockWebServices.transactionFixture.TextTransactionFixture;
import fitlibrary.traverse.workflow.DoTraverse;

public class MockWebServicesFixture extends DoTraverse {
	protected MockingWebServices mockingWebServices = new MockingWebServices();
	protected Map<String,String> nameSpaceMap = new HashMap<String,String>();

	public TextTransactionFixture mockOnPort(int port) {
		return new TextTransactionFixture(port,mockingWebServices);
	}
	public SoapTransactionFixture mockSoapOnPort(int port) {
		return new SoapTransactionFixture(port,mockingWebServices);
	}
	public void nameSpace(String prefix) {
	    nameSpaceMap.put(prefix,"urn:"+prefix);
	    XMLUnit.setXpathNamespaceContext(new SimpleNamespaceContext(nameSpaceMap));
	}
	public boolean close() {
		MockLogger logger = mockingWebServices.close(100);
		showAfterTable(logger.report());
		return !logger.hasErrors();
	}
	public void sleep(int time) throws InterruptedException {
		Thread.sleep(time);
	}
	@Override
	public void tearDown() throws Exception {
		super.tearDown();
		mockingWebServices.close(100);
	}
}
