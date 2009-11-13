/*
 * Copyright (c) 2009 Rick Mugridge, www.RimuResearch.com
 * Kindly donated by Air New Zealand in October 2009.
 * Released under the terms of the GNU General Public License version 2 or later.
*/
/*
 * WARNING: this depends on other's code that is an early stage of development.
 */
package fitlibrary.flex;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;

import fitlibrary.DoFixture;

public class FlexSpiderFixture extends DoFixture {
	private String flexId = "!id not set!";
	private WebDriver driver;
	protected FirefoxProfile firefoxProfile = new FirefoxProfile();

	public FlexSpiderFixture() {
		//
	}
	public FlexSpiderFixture(WebDriver driver) {
		this.driver = driver;
	}

	public void quit() {
		driver.quit();
	}
	public boolean sleepFor(int milliseconds) {
		try {
			Thread.sleep(milliseconds);
		} catch (InterruptedException e) {
			// Nothing to do
		}
		return true;
	}
	public void firefoxProfileAsString(String key, String value) {
		firefoxProfile.setPreference(key, value);
	}
	public void firefoxProfileAsInteger(String key, int value) {
		firefoxProfile.setPreference(key, value);
	}
	public void firefoxProfileAsBoolean(String key, boolean value) {
		firefoxProfile.setPreference(key, value);
	}
	public void flexAt(String flexIdentifier) {
		this.flexId = flexIdentifier;
	}
	public boolean getUrl(String url) {
		driver().get(url);
		return true;
	}
	public String textOf(String locator) {
		return callStringFlex("getFlexText", locator, "");
	}
	public boolean withSetText(String name, String text) {
		return callBoolFlex("doFlexType", name, text);
	}
	public boolean withAddText(String name, String text) {
		return callBoolFlex("doFlexTypeAppend", name, text);
	}
	public boolean click(String locator) {
		return callBoolFlex("doFlexClick", locator, "");
	}
	public boolean elementIsVisible(String locator) {
		return callBoolFlex("getFlexVisible", locator, "");
	}
	public boolean elementIsEnabled(String locator) {
		return callBoolFlex("getFlexEnabled", locator, "");
	}
	public boolean elementExists(String locator) {
		return callBoolFlex("getFlexExists", locator, "");
	}
	public String propertyOf(String property, String locator) {
		return callStringFlex("getFlexProperty", locator, property);
	}
	public boolean selectedItemAt(String target, int index) {
		return callBoolFlex("getFlexSelectedItemAtIndex", target, "" + index);
	}
	public boolean textIsPresent(String target) {
		return callBoolFlex("getFlexTextPresent", target, "");
	}

	public String getFlexNumSelectedItems(String target) {
		return executeCommand("getFlexNumSelectedItems", target, "");
	}
	public String getFlexStepper(String target, String arg2) {
		return executeCommand("doFlexStepper", target, arg2);
	}
	public String getFlexStepper(String target) {
		return executeCommand("getFlexStepper", target, "");
	}
	public String getFlexSelectionIndex(String target, String arg2) {
		return executeCommand("getFlexSelectionIndex", target, arg2);
	}
	public String getFlexSelectionIndex(String target) {
		return executeCommand("getFlexSelectionIndex", target, "");
	}
	public String getFlexSelection(String target, String arg2) {
		return executeCommand("getFlexSelection", target, arg2);
	}
	public String getFlexSelection(String target) {
		return executeCommand("getFlexSelection", target, "");
	}
	public String getFlexRadioButton(String target, String arg2) {
		return executeCommand("getFlexRadioButton", target, arg2);
	}
	public String getFlexRadioButton(String target) {
		return executeCommand("getFlexRadioButton", target, "");
	}
	public String getFlexParseInt(String target, String arg2) {
		return executeCommand("getFlexParseInt", target, arg2);
	}
	public String getFlexParseInt(String target) {
		return executeCommand("getFlexParseInt", target, "");
	}
	public String getFlexNumeric(String target, String arg2) {
		return executeCommand("getFlexNumeric", target, arg2);
	}
	public String getFlexNumeric(String target) {
		return executeCommand("getFlexNumeric", target, "");
	}
	public String getFlexGlobalPosition(String target, String arg2) {
		return executeCommand("getFlexGlobalPosition", target, arg2);
	}
	public String getFlexGlobalPosition(String target) {
		return executeCommand("getFlexGlobalPosition", target, "");
	}
	public String getFlexErrorString(String target, String arg2)
			throws Exception {
		return executeCommand("getFlexErrorString", target, arg2);
	}
	public String getFlexErrorString(String target) {
		return executeCommand("getFlexErrorString", target, "");
	}
	public String getFlexDate(String target, String arg2) {
		return executeCommand("getFlexDate", target, arg2);
	}
	public String getFlexDate(String target) {
		return executeCommand("getFlexDate", target, "");
	}
	public String getFlexDataGridUIComponentLabel(String target, String arg2) {
		return executeCommand("getFlexDataGridUIComponentLabel", target, arg2);
	}
	public String getFlexDataGridUIComponentLabel(String target) {
		return executeCommand("getFlexDataGridUIComponentLabel", target, "");
	}
	public String getFlexDataGridRowIndexForFieldValue(String target,
			String arg2) {
		return executeCommand("getFlexDataGridRowIndexForFieldValue", target,
				arg2);
	}
	public String getFlexDataGridRowIndexForFieldValue(String target) {
		return executeCommand("getFlexDataGridRowIndexForFieldValue", target,
				"");
	}
	public String getFlexDataGridRowCount(String target, String arg2) {
		return executeCommand("getFlexDataGridRowCount", target, arg2);
	}
	public String getFlexDataGridRowCount(String target) {
		return executeCommand("getFlexDataGridRowCount", target, "");
	}
	public String getFlexDataGridFieldValueForGridRow(String target, String arg2) {
		return executeCommand("getFlexDataGridFieldValueForGridRow", target,
				arg2);
	}
	public String getFlexDataGridFieldValueForGridRow(String target) {
		return executeCommand("getFlexDataGridFieldValueForGridRow", target, "");
	}
	public String getFlexDataGridCellText(String target, String arg2) {
		return executeCommand("getFlexDataGridCellText", target, arg2);
	}
	public String getFlexDataGridCellText(String target) {
		return executeCommand("getFlexDataGridCellText", target, "");
	}
	public String getFlexDataGridCell(String target, String arg2) {
		return executeCommand("getFlexDataGridCell", target, arg2);
	}
	public String getFlexDataGridCell(String target) {
		return executeCommand("getFlexDataGridCell", target, "");
	}
	public String getFlexComponentInfo(String target, String arg2) {
		return executeCommand("getFlexComponentInfo", target, arg2);
	}
	public String getFlexComponentInfo(String target) {
		return executeCommand("getFlexComponentInfo", target, "");
	}
	public String getFlexComboContainsLabel(String target, String arg2) {
		return executeCommand("getFlexComboContainsLabel", target, arg2);
	}
	public String getFlexComboContainsLabel(String target) {
		return executeCommand("getFlexComboContainsLabel", target, "");
	}
	public String getFlexCheckBoxChecked(String target, String arg2) {
		return executeCommand("getFlexCheckBoxChecked", target, arg2);
	}
	public String getFlexCheckBoxChecked(String target) {
		return executeCommand("getFlexCheckBoxChecked", target, "");
	}
	public String getFlexAlertTextPresent(String target, String arg2) {
		return executeCommand("getFlexAlertTextPresent", target, arg2);
	}
	public String getFlexAlertTextPresent(String target) {
		return executeCommand("getFlexAlertTextPresent", target, "");
	}
	public String getFlexAlertText(String target, String arg2) {
		return executeCommand("getFlexAlertText", target, arg2);
	}
	public String getFlexAlertText(String target) {
		return executeCommand("getFlexAlertText", target, "");
	}
	public String getFlexAlertPresent(String target, String arg2)
			throws Exception {
		return executeCommand("getFlexAlertPresent", target, arg2);
	}
	public String getFlexAlertPresent(String target) {
		return executeCommand("getFlexAlertPresent", target, "");
	}
	public String getFlexASProperty(String target, String arg2) {
		return executeCommand("getFlexASProperty", target, arg2);
	}
	public String getFlexASProperty(String target) {
		return executeCommand("getFlexASProperty", target, "");
	}
	public String getDataGridUIComponentLabel(String target, String arg2) {
		return executeCommand("getDataGridUIComponentLabel", target, arg2);
	}
	public String getDataGridUIComponentLabel(String target) {
		return executeCommand("getDataGridUIComponentLabel", target, "");
	}
	public String getDataGridCellText(String target, String arg2) {
		return executeCommand("getDataGridCellText", target, arg2);
	}
	public String getDataGridCellText(String target) {
		return executeCommand("getDataGridCellText", target, "");
	}
	public void doRefreshIDToolTips(String target, String arg2) {
		executeCommand("doRefreshIDToolTips", target, arg2);
	}
	public void doRefreshIDToolTips(String target) {
		executeCommand("doRefreshIDToolTips", target, "");
	}
	public void flexWaitForElementVisible(String target, String arg2) {
		executeCommand("flexWaitForElementVisible", target, arg2);
	}
	public void flexWaitForElementVisible(String target) {
		executeCommand("flexWaitForElementVisible", target, "");
	}
	public void flexWaitForElement(String target, String arg2) {
		executeCommand("flexWaitForElement", target, arg2);
	}
	public void flexWaitForElement(String target) {
		executeCommand("flexWaitForElement", target, "");
	}
	public void flexStepper(String target, String arg2) {
		executeCommand("flexStepper", target, arg2);
	}
	public void flexStepper(String target) {
		executeCommand("flexStepper", target, "");
	}
	public void flexSetFocus(String target, String arg2) {
		executeCommand("flexSetFocus", target, arg2);
	}
	public void flexSetFocus(String target) {
		executeCommand("flexSetFocus", target, "");
	}
	public void flexSetDataGridCell(String target, String arg2) {
		executeCommand("flexSetDataGridCell", target, arg2);
	}
	public void flexSetDataGridCell(String target) {
		executeCommand("flexSetDataGridCell", target, "");
	}
	public void flexSelectMatchingOnField(String target, String arg2) {
		executeCommand("flexSelectMatchingOnField", target, arg2);
	}
	public void flexSelectMatchingOnField(String target) {
		executeCommand("flexSelectMatchingOnField", target, "");
	}
	public void flexSelectIndex(String target, int index) {
		executeCommand("getFlexSelectedItemAtIndex", target, "" + index);
	}

	/*
	 * |with|..|at|1|select|true|
	 */

	public void withAtSelect(String target, int index, boolean state) {
		if (!getFlexSelection(target, "" + index).equals("" + state))
			flexSelectIndex(target, index);
	}

	public void flexSelectIndex(String target) {
		executeCommand("flexSelectIndex", target, "");
	}
	public void flexSelectComboByLabel(String target, String arg2) {
		executeCommand("flexSelectComboByLabel", target, arg2);
	}
	public void flexSelectComboByLabel(String target) {
		executeCommand("flexSelectComboByLabel", target, "");
	}
	public void flexSelect(String target, String arg2) {
		executeCommand("doFlexSelect", target, arg2);
	}
	public void flexSelect(String target) {
		executeCommand("doFlexSelect", target, "");
	}
	public void flexRefreshIDToolTips(String target, String arg2) {
		executeCommand("flexRefreshIDToolTips", target, arg2);
	}
	public void flexRefreshIDToolTips(String target) {
		executeCommand("flexRefreshIDToolTips", target, "");
	}
	public void flexRadioButton(String target, String arg2) {
		executeCommand("flexRadioButton", target, arg2);
	}
	public void flexRadioButton(String target) {
		executeCommand("flexRadioButton", target, "");
	}
	public void flexMouseUp(String target, String arg2) {
		executeCommand("flexMouseUp", target, arg2);
	}
	public void flexMouseUp(String target) {
		executeCommand("flexMouseUp", target, "");
	}
	public void flexMouseRollOver(String target, String arg2) {
		executeCommand("flexMouseRollOver", target, arg2);
	}
	public void flexMouseRollOver(String target) {
		executeCommand("flexMouseRollOver", target, "");
	}
	public void flexMouseRollOut(String target, String arg2) {
		executeCommand("flexMouseRollOut", target, arg2);
	}
	public void flexMouseRollOut(String target) {
		executeCommand("flexMouseRollOut", target, "");
	}
	public void flexMouseOver(String target, String arg2) {
		executeCommand("flexMouseOver", target, arg2);
	}
	public void flexMouseOver(String target) {
		executeCommand("flexMouseOver", target, "");
	}
	public void flexMouseMove(String target, String arg2) {
		executeCommand("flexMouseMove", target, arg2);
	}
	public void flexMouseMove(String target) {
		executeCommand("flexMouseMove", target, "");
	}
	public void flexMouseDown(String target, String arg2) {
		executeCommand("flexMouseDown", target, arg2);
	}
	public void flexMouseDown(String target) {
		executeCommand("flexMouseDown", target, "");
	}
	public void flexDragTo(String target, String arg2) {
		executeCommand("flexDragTo", target, arg2);
	}
	public void flexDragTo(String target) {
		executeCommand("flexDragTo", target, "");
	}
	public void flexDoubleClick(String target, String arg2) {
		executeCommand("flexDoubleClick", target, arg2);
	}
	public void flexDoubleClick(String target) {
		executeCommand("flexDoubleClick", target, "");
	}
	public void flexDate(String target, String arg2) {
		executeCommand("doFlexDate", target, arg2);
	}
	public void flexDate(String target) {
		executeCommand("getFlexDate", target, "");
	}
	public void flexClickMenuBarUIComponent(String target, String arg2) {
		executeCommand("flexClickMenuBarUIComponent", target, arg2);
	}
	public void flexClickMenuBarUIComponent(String target) {
		executeCommand("flexClickMenuBarUIComponent", target, "");
	}
	public void flexClickDataGridUIComponent(String target, String arg2) {
		executeCommand("flexClickDataGridUIComponent", target, arg2);
	}
	public void flexClickDataGridUIComponent(String target) {
		executeCommand("flexClickDataGridUIComponent", target, "");
	}
	public void flexClickDataGridItem(String target, String arg2) {
		executeCommand("flexClickDataGridItem", target, arg2);
	}
	public void flexClickDataGridItem(String target) {
		executeCommand("flexClickDataGridItem", target, "");
	}
	public void flexClick(String target, String arg2) {
		executeCommand("flexClick", target, arg2);
	}
	public void flexClick(String target) {
		executeCommand("flexClick", target, "");
	}
	public void flexCheckBox(String target, String arg2) {
		executeCommand("flexCheckBox", target, arg2);
	}
	public void flexCheckBox(String target) {
		executeCommand("flexCheckBox", target, "");
	}
	public void flexAlertResponse(String target, String arg2) {
		executeCommand("flexAlertResponse", target, arg2);
	}
	public void flexAlertResponse(String target) {
		executeCommand("flexAlertResponse", target, "");
	}
	public void flexAddSelectMatchingOnField(String target, String arg2) {
		executeCommand("flexAddSelectMatchingOnField", target, arg2);
	}
	public void flexAddSelectMatchingOnField(String target) {
		executeCommand("flexAddSelectMatchingOnField", target, "");
	}
	public void flexAddSelectIndex(String target, String arg2) {
		executeCommand("flexAddSelectIndex", target, arg2);
	}
	public void flexAddSelectIndex(String target) {
		executeCommand("flexAddSelectIndex", target, "");
	}

	public Object callFlexWith(String command, String[] arguments) {
		return callFlex(command, arguments[0], arguments.length == 1
				? ""
				: arguments[1]);
	}

	private String executeCommand(String command, String locator, String arg2) {
		return callStringFlex(command, locator, arg2);
	}
	public String callStringFlex(String command, String locator, String text) {
		return callFlex(command, locator, text).toString();
	}
	public boolean callBoolFlex(String command, String locator, String text) {
		return "true".equals(callFlex(command, locator, text));
	}
	public Object callFlex(String command, String arg1, String arg2) {
		String js = "flexContainer = document.getElementById('" + flexId
				+ "'); " + "return flexContainer['" + command + "']('" + arg1
				+ "', '" + arg2 + "');";
		return js().executeScript(js, "");
	}
	protected WebDriver driver() {
		if (driver == null)
			driver = new FirefoxDriver(firefoxProfile);
		return driver;
	}
	protected JavascriptExecutor js() {
		return (JavascriptExecutor) driver();
	}
	// private void waitForLoadToComplete() throws InterruptedException
	// {
	// Thread.sleep(10 * 1000);
	// // int count = 0;
	// // while (true) {
	// // try
	// // {
	// // String js =
	// // "return document.getElementById('"+flexId+"')['getFlexText']; ";
	// // Object result = js().executeScript(js,"");
	// //
	// System.out.println("result of js call '"+result+"' which is null: "+result==null);
	// // if (result != null) {
	// // System.out.println("Not a null");
	// // break;
	// // }
	// // System.out.println("result of js call '"+result+"'");
	// // count++;
	// // if (count > 50)
	// // break;
	// // Thread.sleep(500);
	// // } catch (WebDriverException e) {
	// // e.printStackTrace();
	// // // if
	// //
	// (e.getMessage().contains("flexContainer.getFlexText is not a function"))
	// // {
	// // // System.out.println("Function not yet available");
	// // // Thread.sleep(500);
	// // // } else break;
	// // }
	// // }
	// }
}
