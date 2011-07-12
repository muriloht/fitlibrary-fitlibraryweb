/*
 * Copyright (c) 2009 Rick Mugridge, www.RimuResearch.com
 * Kindly donated by Air New Zealand in October 2009.
 * Released under the terms of the GNU General Public License version 2 or later.
*/
package fitlibrary.spider.component;

import java.util.HashSet;
import java.util.Set;

import fitlibrary.exception.FitLibraryException;
import fitlibrary.spider.SpiderFixture;
import fitlibrary.spider.polling.PollForWithError;

public class SpiderWindow extends SpiderComponent {
	private String initialWindowHandle;

	public SpiderWindow(SpiderFixture spiderFixture) {
		super(spiderFixture);
	}
	public String currentWindow() {
		return webDriver().getWindowHandle();
	}
	public boolean clickAndSelectResultingWindow(String locator) {
		final Set<String> windowHandles = windowHandles();
		try {
			spiderFixture.click(locator);
			spiderFixture.ensureBecomes(new PollForWithError(){
				@Override
				public String error() {
					return "No new window appeared";
				}
				@Override
				public boolean matches() {
					return windowHandles().size() > windowHandles.size();
				}
			});
			final Set<String> newWindowHandles = windowHandles();
			for (String handle: windowHandles)
				newWindowHandles.remove(handle);
			if (newWindowHandles.size() > 1)
				throw new FitLibraryException("More than one new window has appeared: "+newWindowHandles);
			selectWindow(newWindowHandles.iterator().next());
			return true;
		} catch (FitLibraryException e) {
			throw e;
		} catch (RuntimeException e) {
			throw spiderFixture.problem(e.toString(),locator);
		}
	}
	public boolean selectInitialWindow() {
		if (initialWindowHandle != null)
			return selectWindow(initialWindowHandle);
		return false;
	}
	public boolean selectWindow(String name) {
		try {
			webDriver().switchTo().window(name);
			return true;
		} catch (Exception e) {
			Set<String> windowHandles = windowHandles();
			spiderFixture.showAfterTable("Possible window names are: "+windowHandles);
			return false;
		}
	}
	public boolean selectWindowWithAs(final String xpath, final String value) {
		return selectWindowWithMatcher(xpath, new WindowMatches() {
			public boolean matches(String elementText) {
				return elementText.equals(value);
			}
			
		});
	}
	public boolean selectWindowWithContains(final String xpath, final String value) {
		return selectWindowWithMatcher(xpath, new WindowMatches() {
			public boolean matches(String elementText) {
				return elementText.toLowerCase().contains(value.toLowerCase());
			}
			
		});
	}
	private interface WindowMatches {
		boolean matches(String elementText);
	}
	private boolean selectWindowWithMatcher(String xpath, WindowMatches matcher) {
		String currentwindow = currentWindow();
		Set<String> windowHandles = windowHandles();
		for (String name : windowHandles) {
			webDriver().switchTo().window(name);
			try {
				if (matcher.matches(spiderFixture.collectText(spiderFixture.findElement(xpath))))
					return true;
			} catch (Exception e) {
				// may not be an error as we might be doing find element in a
				// window where the element does not exist.
			}
		}
		webDriver().switchTo().window(currentwindow);
		return false;
	}
	public boolean selectOtherWindow() {
		Set<String> windowHandles = windowHandles();
		windowHandles.remove(currentWindow());
		if (windowHandles.size() != 1) {
			spiderFixture.showAfterTable("There are more than two popup windows available: "+windowHandles);
			return false;
		}
		selectWindow(windowHandles.iterator().next());
		return true;
	}
	public String windows() {
		Set<String> windowHandles = new HashSet<String>(windowHandles());
		try {
			if (windowHandles.size() == 1)
				return ""+windowHandles;
			windowHandles.remove(currentWindow());
			return "Current: "+currentWindow()+" + "+windowHandles;
		} catch (Exception e) {
			return "No current in "+windowHandles;
		}
	}
	public boolean close() {
		spiderFixture.getDriverVariation().close();
		try {
			Set<String> windowHandles = windowHandles(); // Check to see that web driver is still working
			if (windowHandles.size() == 1)
				selectWindow(windowHandles.iterator().next());
			return true;
		} catch (Exception e) {
			throw new FitLibraryException("Use |close pop up| for a pop up window: "+e);
		}
	}
	protected Set<String> windowHandles() {
		return webDriver().getWindowHandles();
	}
	public void setInitialWindow() {
		initialWindowHandle = currentWindow();
	}
}
