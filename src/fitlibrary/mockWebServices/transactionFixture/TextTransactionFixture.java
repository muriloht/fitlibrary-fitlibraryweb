/*
 * Copyright (c) 2009 Rick Mugridge, www.RimuResearch.com
 * Kindly donated by Air New Zealand in October 2009.
 * Released under the terms of the GNU General Public License version 2 or later.
*/
package fitlibrary.mockWebServices.transactionFixture;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.apache.commons.io.IOUtils;

import fitlibrary.mockWebServices.MockingWebServices;
import fitlibrary.mockWebServices.requestMatcher.EqualsRequestMatcher;
import fitlibrary.mockWebServices.requestMatcher.NotRequestMatcher;
import fitlibrary.mockWebServices.requestMatcher.PatternRequestMatcher;
import fitlibrary.ws.message.ContentType;

public class TextTransactionFixture extends AbstractTransactionFixture {
	public TextTransactionFixture(int port, MockingWebServices mockingWebServices) {
		super(ContentType.XML,port,mockingWebServices);
	}
	public void matchesRequest(String pattern) {
		requestMatcher = requestMatcher.and(new PatternRequestMatcher(pattern));
	}
	public void equalsRequest(String pattern) {
		requestMatcher = requestMatcher.and(new EqualsRequestMatcher(pattern));
	}
	public void matchesRequestFromFile(String fileName) throws IOException {
		FileReader fileReader = new FileReader(new File(fileName));
		String pattern = IOUtils.toString(fileReader);
		fileReader.close();
		requestMatcher = requestMatcher.and(new PatternRequestMatcher(pattern));
	}
	public void notMatchesRequest(String pattern) {
		requestMatcher = requestMatcher.and(new NotRequestMatcher(new PatternRequestMatcher(pattern)));
	}
}
