/*
 * Copyright (c) 2010 Rick Mugridge, www.RimuResearch.com
 * Released under the terms of the GNU General Public License version 2 or later.
*/

package fitlibrary.pdf;

import org.junit.Test;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

public class TestStringIterator {
	@Test
	public void anEmptyStringIteratorHasNoNext() {
		assertThat(new StringIterator("").hasNext(),is(false));
	}
	@Test(expected=RuntimeException.class)
	public void anEmptyStringIteratorThrowsExceptionOnNext() {
		new StringIterator("").next();
	}
	@Test
	public void aOneCharStringIteratorHasOneNext() {
		StringIterator it = new StringIterator("A");
		assertThat(it.hasNext(),is(true));
		assertThat(it.next(),is("A"));
		assertThat(it.hasNext(),is(false));
	}
	@Test
	public void a3CharStringIteratorHasOneNext() {
		StringIterator it = new StringIterator("Abc");
		assertThat(it.hasNext(),is(true));
		assertThat(it.next(),is("A"));
		assertThat(it.hasNext(),is(true));
		assertThat(it.next(),is("b"));
		assertThat(it.hasNext(),is(true));
		assertThat(it.next(),is("c"));
		assertThat(it.hasNext(),is(false));
	}
}
