/*
 * Copyright (c) 2009 Rick Mugridge, www.RimuResearch.com
 * Kindly donated by Air New Zealand in October 2009.
 * Released under the terms of the GNU General Public License version 2 or later.
*/
package fitlibrary.xml;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

import fitlibrary.exception.FitLibraryException;
import fitlibrary.xml.XmlDoFixture;

public class TestXmlDoFixture extends XmlDoFixture {
	@Test
	public void isSameAs() {
		assertTrue(xmlSameAs(
				"<a>A</a>",
				"<a>A</a>"));
	}
	@Test
	public void textNotSameAs() {
		try {
			xmlSameAs(
					"<a>A</a>",
					"<a>B</a>");
		} catch (FitLibraryException e) {
			check(e, "Expected text value 'B' but was 'A'");
		}
	}
	private void check(FitLibraryException e, String expected) {
		String actualMessage = e.getMessage();
		boolean matches = actualMessage.contains(expected);
		if (!matches)
			fail("Expected '"+expected+"' to be in '"+actualMessage+"'");
	}
	@Test
	public void tagNotSameAs() {
		try {
			xmlSameAs(
					"<a>A</a>",
					"<b>A</b>");
		} catch (FitLibraryException e) {
			check(e,"Expected element tag name 'b' but was 'a'");
		}
	}
	@Test
	public void tagsNotSameAs() {
		try {
			xmlSameAs(
					"<all><a>A</a><z></z></all>",
					"<all><b>A</b><y></y></all>");
		} catch (FitLibraryException e) {
			check(e,"Expected element tag name 'b' but was 'a'");
			check(e,"Expected element tag name 'y' but was 'z'");
		}
	}
	@Test
	public void attributeNotSameAsVerbose() {
		try {
			xmlSameAs(
					"<a id=\"1\">A</a>",
					"<a>A</a>");
		} catch (FitLibraryException e) {
			check(e,"Expected number of element attributes '0' but was '1'");
			check(e,"Expected attribute name 'null' but was 'id'");
		}
	}


	@Test
	public void isSimilarTo() {
		assertTrue(xmlSimilarTo(
				"<a><c>2</c><b>1</b></a>",
				"<a><b>1</b><c>2</c></a>"));
	}
	@Test
	public void textNotSimilarTo() {
		try {
			xmlSimilarTo(
					"<a>A</a>",
					"<a>B</a>");
		} catch (FitLibraryException e) {
			check(e,"Expected text value 'B' but was 'A'");
		}
	}
	@Test
	public void tagNotSimilarTo() {
		try {
			xmlSimilarTo(
					"<a>A</a>",
					"<b>A</b>");
		} catch (FitLibraryException e) {
			check(e,"Expected element tag name 'b' but was 'a'");
		}
	}
}
