/*
 * Copyright (c) 2009 Rick Mugridge, www.RimuResearch.com
 * Kindly donated by Air New Zealand in October 2009.
 * Released under the terms of the GNU General Public License version 2 or later.
*/
package fitlibrary.ws;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.custommonkey.xmlunit.SimpleNamespaceContext;
import org.custommonkey.xmlunit.XMLUnit;

import fitlibrary.mockWebServices.MockingWebServices;
import fitlibrary.mockWebServices.logger.MockLogger;
import fitlibrary.mockWebServices.transactionFixture.Soap11TransactionFixture;
import fitlibrary.mockWebServices.transactionFixture.Soap12TransactionFixture;
import fitlibrary.mockWebServices.transactionFixture.SoapFullTransactionFixture;
import fitlibrary.mockWebServices.transactionFixture.TextTransactionFixture;
import fitlibrary.traverse.workflow.DoTraverse;
import fitlibrary.ws.message.ContentType;

public class MockWebServicesFixture extends DoTraverse {
	protected MockingWebServices mockingWebServices = new MockingWebServices();
	protected Map<String,String> nameSpaceMap = new HashMap<String,String>();

	public TextTransactionFixture mockPlainTextOnPort(int port) {
		return new TextTransactionFixture(port,mockingWebServices);
	}
	public SoapFullTransactionFixture mockFullSoapAsOnPort(ContentType contentType, int port) {
		return new SoapFullTransactionFixture(contentType,port,mockingWebServices);
	}
	public Soap11TransactionFixture mockSoap11OnPort(int port) {
		return new Soap11TransactionFixture(port,mockingWebServices);
	}
	public Soap12TransactionFixture mockSoap12OnPort(int port) {
		return new Soap12TransactionFixture(port,mockingWebServices);
	}
	public void nameSpace(String prefix) {
	    nameSpaceMap.put(prefix,"urn:"+prefix);
	    XMLUnit.setXpathNamespaceContext(new SimpleNamespaceContext(nameSpaceMap));
	}
	public boolean close() throws IOException {
		MockLogger logger = mockingWebServices.close(100);
		showAfterTable(logger.report());
		return !logger.hasErrors();
	}
	public boolean closeAfter(int milliseconds) throws IOException {
		MockLogger logger = mockingWebServices.close(milliseconds);
		showAfterTable(logger.report());
		return !logger.hasErrors();
	}
	public void sleep(int time) throws InterruptedException {
		Thread.sleep(time);
	}
	public void tearDown() throws Exception {
		mockingWebServices.close(100);
	}
}
