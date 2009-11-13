/*
 * Copyright (c) 2009 Rick Mugridge, www.RimuResearch.com
 * Kindly donated by Air New Zealand in October 2009.
 * Released under the terms of the GNU General Public License version 2 or later.
*/
package fitlibrary.ws.mock.responder;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import fitlibrary.mockWebServices.responder.ErrorResponder;

public class TestErrorResponder {
	final ErrorResponder errorResponder = ErrorResponder.create();
	
	@Test public void responseReturnsError() {
		assertThat(errorResponder.getContents(), equalTo(ErrorResponder.ERROR_RESPONSE));
	}
	@Test public void okIsFalse() {
		assertThat(errorResponder.isOK(), equalTo(false));
	}
	@Test public void contentsOfReturnsSame() {
		assertThat(errorResponder.contentsOf("aa"), equalTo("aa"));
	}
}
