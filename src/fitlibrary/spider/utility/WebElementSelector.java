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
import fitlibrary.annotation.NullaryAction;
import fitlibrary.annotation.ShowSelectedActions;
import fitlibrary.annotation.SimpleAction;
import fitlibrary.exception.FitLibraryException;
import fitlibrary.spider.SpiderElementFixture;
import fitlibrary.spider.SpiderFixture;
import fitlibrary.traverse.workflow.DoTraverse;

@ShowSelectedActions
public class WebElementSelector extends DoTraverse {
	private final SpiderFixture spiderFixture;
	private final String tag;
	private final List<WebElement> elements;

	public WebElementSelector(String tag, List<WebElement> elements, SpiderFixture spiderFixture) {
		this.spiderFixture = spiderFixture;
		this.tag = tag;
		this.elements = elements;
	}
	@NullaryAction(tooltip="Select the remaining element, if one is left.\nSubsequent actions in the same table act on that element.")
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
	@SimpleAction(wiki="|''<i>text is</i>''|text|",
			tooltip="Select those elements which have exactly the given text.")
	public void textIs(final String s) {
		filter(new Filter() {
			@Override
			public boolean select(WebElement element) {
				return element.getText().equals(s);
			}});
	}
	@SimpleAction(wiki="|''<i>text contains</i>''|text|",
			tooltip="Select those elements which contain the given text.")
	public void textContains(final String s) {
		filter(new Filter() {
			@Override
			public boolean select(WebElement element) {
				return element.getText().contains(s);
			}});
	}
	@SimpleAction(wiki="|''<i>text matches</i>''|pattern|",
			tooltip="Select those elements which match the given pattern (a regular expression).")
	public void textMatches(final String pattern) {
		final Pattern compiled = Pattern.compile(".*"+pattern+".*",Pattern.DOTALL);
		filter(new Filter() {
			@Override
			public boolean select(WebElement element) {
				return compiled.matcher(element.getText()).matches();
			}});
	}
	@SimpleAction(wiki="|''<i>attribute</i>''|attribute name|''<i>is</i>''|text|",
			tooltip="Select those elements which have an attribute value that is the given text.")
	public void attributeIs(final String attribute, final String s) {
		filter(new Filter() {
			@Override
			public boolean select(WebElement element) {
				return s.equals(element.getAttribute(attribute));
			}});
	}
	@SimpleAction(wiki="|''<i>attribute</i>''|attribute name|''<i>contains</i>''|text|",
			tooltip="Select those elements which have an attribute value that contain the given text.")
	public void attributeContains(final String attribute, final String s) {
		filter(new Filter() {
			@Override
			public boolean select(WebElement element) {
				String value = element.getAttribute(attribute);
				return value != null && value.contains(s);
			}});
	}
	@SimpleAction(wiki="|''<i>attribute</i>''|attribute name|''<i>matches</i>''|pattern|",
			tooltip="Select those elements which have an attribute value that matches the given pattern (a regular expression).")
	public void attributeMatches(final String attribute, String pattern) {
		final Pattern compiled = Pattern.compile(".*"+pattern+".*",Pattern.DOTALL);
		filter(new Filter() {
			@Override
			public boolean select(WebElement element) {
				String value = element.getAttribute(attribute);
				return value != null && compiled.matcher(value).matches();
			}});
	}
	@NullaryAction(tooltip="Show the elements that are left.")
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
