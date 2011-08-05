package fitlibrary.spider.element;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import fitlibrary.log.FixturingLogger;
import fitlibrary.spider.AbstractSpiderFixture;
import fitlibrary.spider.polling.PollForMatches;
import fitlibrary.spider.polling.PollForWithError;

public class SelectElement extends SpiderElement {
	static Logger logger = FixturingLogger.getLogger(SelectElement.class);
	
	public SelectElement(AbstractSpiderFixture spiderFixture) {
		super(spiderFixture);
	}
	private Select webDriverSelect(String locator) {
		return new Select(findElement(locator));
	}
	protected List<WebElement> childrenOf(String locator) {
		return webDriverSelect(locator).getOptions();
	}
	public String optionOf(String locator) {
		List<WebElement> childrenOf = childrenOf(locator);
		for (WebElement option : childrenOf) {
			if (option.isSelected()) {
				String value = option.getAttribute("value");
				
				return (value == null || value.length()==0) ? option.getText() : value;
			}
		}
		return "";
	}
	public List<String> optionListOf(String locator) {
		List<String> result = new ArrayList<String>();
		for (WebElement option : childrenOf(locator)) {
			if (option.isSelected()) {
				result.add(option.getAttribute("value"));
			}
		}
		return result;
	}
	public boolean optionallyWithSelectOption(String locator, String option) {
		try {
			if ("".equals(option)) {
				return true;
			}
			withSelectOption(locator, option);
		} catch (Exception e) {
			// Ignore any problems
		}
		return true;
	}
	public boolean withSelectOption(String locator, String option) {
		return selectOption(locator, option, true);
	}
	public boolean withSelectText(String locator, String text) {
		return selectTextThroughChildren(locator, text);
	}
	public boolean withSelectOptionAt(String locator, int index) {
		try {
			final WebElement webElement = childrenOf(locator).get(index);
			setSelected(webElement);
			ensureBecomes(new PollForWithError() {
				public boolean matches() {
					return webElement.isSelected();
				}
				public String error() {
					return "Not selected correctly";
				}
			});
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	public boolean withAddSelection(String locator, String option) {
		return selectOption(locator, option, true);
	}
	public boolean withRemoveSelection(String locator, String option) {
		return selectOption(locator, option, false);
	}
	private boolean selectOption(final String locator, final String option,
			final boolean select) {
		return selectOptionThroughChildren(locator, option, select);
	}
	private boolean selectOptionThroughChildren(final String locator,
			final String option, final boolean select) {
		return ensureMatches(new PollForMatches() {
			@Override
			public boolean matches() {
				List<WebElement> childrenOf = childrenOf(locator);
				for (WebElement optionElement : childrenOf) {
					String value = optionElement.getAttribute("value");
					if ((value == null || value.length()==0)&& option.length()>0) 
                        value = optionElement.getText();
					if (value != null && value.equalsIgnoreCase(option)) {
						if (select != optionElement.isSelected()) {
							if (select) {
								setSelected(optionElement);
							} else {
								toggle(optionElement);
							}
						}
						return true;
					}
				}
				return false;
			}
		});
	}
	private void toggle(WebElement e) {
		e.click();
	}
	private void setSelected(WebElement e) {
		if (e.isSelected())
			return;
		toggle(e);
	}
	private boolean selectTextThroughChildren(final String locator, final String textRequired) {
		return ensureMatches(new PollForMatches() {
			@Override
			public boolean matches() {
				for (WebElement optionElement : childrenOf(locator)) {
					String text = optionElement.getText();
					if (text != null && text.equalsIgnoreCase(textRequired)) {
						setSelected(optionElement);
						return true;
					}
				}
				return false;
			}
		});
	}
	public List<String> optionValues(String locator) {
		List<String> nameList = new ArrayList<String>();
		List<SpiderWebElement> options = options(locator);
		for (SpiderWebElement option : options) {
			nameList.add(option.getValue());
		}
		return nameList;
	}
	public List<SpiderWebElement> options(String locator) {
		String rawHtmlOfParent = spiderFixture().innerHtmlOf(locator); 
		List<SpiderWebElement> elements = new ArrayList<SpiderWebElement>();
		for (WebElement webDriversElement: childrenOf(locator)) {
			elements.add(new SpiderWebElement(webDriversElement, rawHtmlOfParent));
		}
		return elements;
	}
}
