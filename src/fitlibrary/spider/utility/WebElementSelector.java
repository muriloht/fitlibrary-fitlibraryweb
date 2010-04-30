/*
 * Copyright (c) 2009 Rick Mugridge, www.RimuResearch.com
 * Kindly donated by Air New Zealand in October 2009.
 * Released under the terms of the GNU General Public License version 2 or later.
*/
package fitlibrary.spider.utility;

import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

import org.openqa.selenium.WebElement;

import fit.Fixture;
import fitlibrary.exception.FitLibraryException;
import fitlibrary.spider.SpiderElementFixture;
import fitlibrary.spider.SpiderFixture;
import fitlibrary.traverse.workflow.DoTraverse;

public class WebElementSelector extends DoTraverse {
	private final SpiderFixture spiderFixture;
	private final String tag;
	private final List<WebElement> elements;

	public WebElementSelector(String tag, List<WebElement> elements, SpiderFixture spiderFixture) {
		this.spiderFixture = spiderFixture;
		this.tag = tag;
		this.elements = elements;
	}
	public SpiderElementFixture select() {
		if (elements.isEmpty())
			throw new FitLibraryException("There are none left selected");
		if (elements.size() != 1)
			throw new FitLibraryException("The following elements are still selected: "+descriptions());
		return new SpiderElementFixture(elements.get(0),spiderFixture);
	}
	private String descriptions() {
		StringBuilder stringBuilder = new StringBuilder();
		for (WebElement element : elements) {
			stringBuilder.append(",");
			stringBuilder.append(new WebElementIdentifier(element,tag).identify());
		}
		if (stringBuilder.length() > 1)
			stringBuilder.deleteCharAt(0);
		String descriptions = Fixture.escape(stringBuilder.toString());
		return descriptions;
	}
	public void textIs(final String s) {
		filter(new Filter() {
			public boolean select(WebElement element) {
				return element.getText().equals(s);
			}});
	}
	public void textContains(final String s) {
		filter(new Filter() {
			public boolean select(WebElement element) {
				return element.getText().contains(s);
			}});
	}
	public void textMatches(final String pattern) {
		final Pattern compiled = Pattern.compile(".*"+pattern+".*",Pattern.DOTALL);
		filter(new Filter() {
			public boolean select(WebElement element) {
				return compiled.matcher(element.getText()).matches();
			}});
	}
	public void attributeIs(final String attribute, final String s) {
		filter(new Filter() {
			public boolean select(WebElement element) {
				return s.equals(element.getAttribute(attribute));
			}});
	}
	public void attributeContains(final String attribute, final String s) {
		filter(new Filter() {
			public boolean select(WebElement element) {
				String value = element.getAttribute(attribute);
				return value != null && value.contains(s);
			}});
	}
	public void attributeMatches(final String attribute, String pattern) {
		final Pattern compiled = Pattern.compile(".*"+pattern+".*",Pattern.DOTALL);
		filter(new Filter() {
			public boolean select(WebElement element) {
				String value = element.getAttribute(attribute);
				return value != null && compiled.matcher(value).matches();
			}});
	}
	public void show() {
		getRuntimeContext().currentRow().addShow(descriptions());
	}
	private void filter(Filter filter) {
		Iterator<WebElement> iterator = elements.iterator();
		while (iterator.hasNext()) {
			if (!filter.select(iterator.next()))
				iterator.remove();
		}
	}
	interface Filter {
		boolean select(WebElement element);
	}
}
