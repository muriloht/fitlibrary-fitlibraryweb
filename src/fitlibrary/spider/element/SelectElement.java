package fitlibrary.spider.element;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.openqa.selenium.WebElement;

import fitlibrary.spider.AbstractSpiderFixture;
import fitlibrary.spider.polling.PollForMatches;
import fitlibrary.spider.polling.PollForWithError;

public class SelectElement extends SpiderElement {
	public SelectElement(AbstractSpiderFixture spiderFixture) {
		super(spiderFixture);
	}
	public String optionOf(String locator) {
		if (isSelenium()) {
			String result = selenium().getSelectedValue(locator);
			if (result.equals("")) {
				result = selenium().getSelectedLabel(locator);
			}
			return result;
		}
		List<WebElement> childrenOf = childrenOf(locator, "option");
		for (WebElement option : childrenOf)
			if (option.isSelected()) {
				return option.getValue();
			}
		return "";
	}
	public List<String> optionListOf(String locator) {
		if (isSelenium()) {
			return Arrays.asList(selenium().getSelectedValues(locator));
		}
		List<String> result = new ArrayList<String>();
		for (WebElement option : childrenOf(locator, "option")) {
			if (option.isSelected()) {
				result.add(option.getValue());
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
			final WebElement webElement = childrenOf(locator, "option").get(index);
			webElement.setSelected();
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
		if (isSelenium()) {
			try {
				selenium().addSelection(locator, "value=" + option);
			} catch (Exception e) {
				selenium().addSelection(locator, "label=" + option);
			}
			return true;
		}
		return selectOption(locator, option, true);
	}
	public boolean withRemoveSelection(String locator, String option) {
		if (isSelenium()) {
			try {
				selenium().removeSelection(locator, "value=" + option);
			} catch (Exception e) {
				selenium().removeSelection(locator, "label=" + option);
			}
			return true;
		}
		return selectOption(locator, option, false);
	}
	private boolean selectOption(final String locator, final String option,
			final boolean select) {
		if (isSelenium()) {
			return selectOptionInSelenium(locator, option);
		}
		return selectOptionThroughChildren(locator, option, select);
	}
	private boolean selectOptionThroughChildren(final String locator,
			final String option, final boolean select) {
		return ensureMatches(new PollForMatches() {
			public boolean matches() {
				for (WebElement optionElement : childrenOf(locator, "option")) {
					String value = optionElement.getValue();
					if (value != null && value.equalsIgnoreCase(option)) {
						if (select != optionElement.isSelected()) {
							if (select) {
								optionElement.setSelected();
							} else {
								optionElement.toggle();
							}
						}
						return true;
					}
				}
				return false;
			}
		});
	}
	private boolean selectTextThroughChildren(final String locator, final String textRequired) {
		return ensureMatches(new PollForMatches() {
			public boolean matches() {
				for (WebElement optionElement : childrenOf(locator, "option")) {
					String text = optionElement.getText();
					if (text != null && text.equalsIgnoreCase(textRequired)) {
						optionElement.setSelected();
						return true;
					}
				}
				return false;
			}
		});
	}
	private boolean selectOptionInSelenium(final String locator,
			final String option) {
		try {
			selenium().select(locator, "value=" + option);
		} catch (Exception e) {
			selenium().select(locator, "label=" + option);
		}
		return true;
	}
	public List<String> optionValues(String locator) {
		if (isSelenium()) {
			return Arrays.asList(selenium().getSelectOptions(locator));
		}
		List<String> nameList = new ArrayList<String>();
		List<WebElement> options = options(locator);
		for (WebElement option : options) {
			nameList.add(option.getValue());
		}
		return nameList;
	}
	public List<WebElement> options(String locator) {
		return childrenOf(locator, "option");
	}
}
