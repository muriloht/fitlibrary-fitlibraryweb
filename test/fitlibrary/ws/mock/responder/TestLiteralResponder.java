/*
 * Copyright (c) 2009 Rick Mugridge, www.RimuResearch.com
 * Kindly donated by Air New Zealand in October 2009.
 * Released under the terms of the GNU General Public License version 2 or later.
*/
package fitlibrary.ws.mock.responder;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import fitlibrary.mockWebServices.responder.LiteralResponder;

public class TestLiteralResponder {
	final LiteralResponder literalResponder = new LiteralResponder("aa");
	
	@Test public void responseReturnsSameString() {
		assertThat(literalResponder.getContents(), equalTo("aa"));
	}
	@Test public void okIsTrue() {
		assertThat(literalResponder.isOK(), equalTo(true));
	}
	@Test public void contentsOfReturnsSame() {
		assertThat(literalResponder.contentsOf("aa"), equalTo("aa"));
	}
}
