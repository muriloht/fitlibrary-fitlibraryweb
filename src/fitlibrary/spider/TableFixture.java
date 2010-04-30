/*
 * Copyright (c) 2009 Rick Mugridge, www.RimuResearch.com
 * Kindly donated by Air New Zealand in October 2009.
 * Released under the terms of the GNU General Public License version 2 or later.
*/
package fitlibrary.spider;

import java.util.List;

import fitlibrary.runResults.TestResults;
import fitlibrary.table.Cell;
import fitlibrary.table.Row;
import fitlibrary.table.Table;
import fitlibrary.table.TableFactory;
import fitlibrary.traverse.Traverse;

public class TableFixture extends Traverse {
	List<List<String>> array;
	
	public TableFixture(List<List<String>> array) {
		this.array = array;
	}
	@Override
	public Object interpretAfterFirstRow(Table table, TestResults testResults) {
		try {
			for (int rowNo = 1; rowNo < table.size() && rowNo < array.size()+1; rowNo++)
				matchRow(table.at(rowNo), array.get(rowNo-1), testResults);
			missingActualRows(table, testResults);
			missingExpectedRows(table, testResults);
		} catch (Exception e) {
			table.error(testResults, e);
		}
		return null;
	}
	private void missingActualRows(Table table, TestResults testResults) {
		for (int rowNo = array.size() + 1; rowNo < table.size(); rowNo++) {
			Row expectedRow = table.at(rowNo);
			for (int colNo = 0; colNo < expectedRow.size(); colNo++)
				expectedRow.at(colNo).actualElementMissing(testResults);
		}
	}
	private void missingExpectedRows(Table table, TestResults testResults) {
		for (int rowNo = table.size(); rowNo < array.size()+1; rowNo++) {
			List<String> actualRow = array.get(rowNo-1);
			Row newRow = table.newRow();
			for (int colNo = 0; colNo < actualRow.size(); colNo++) {
				Cell cell = TableFactory.cell(actualRow.get(colNo).toString());
				newRow.add(cell);
				cell.actualElementMissing(testResults);
			}
		}
	}
//	private void missingExpectedRows(Table table, TestResults testResults) {
//		for (int rowNo = table.size(); rowNo < array.size()+1; rowNo++) {
//			List<String> actualRow = array.get(rowNo-1);
//			String[] cells = new String[actualRow.size()];
//			for (int colNo = 0; colNo < actualRow.size(); colNo++)
//				cells[colNo] = actualRow.get(colNo).toString();
//			ParseUtility.addRowToTable(table.parse, cells);
//			Row lastRow = table.lastRow();
//			for (int i = 0; i < lastRow.size(); i++) {
//				Cell cell = lastRow.at(i);
//				cell.fail(testResults);
//				cell.parse.body += "<i> extra</i>";
//			}
//		}
//	}
	private void matchRow(Row expectedRow, List<String> actualRow, TestResults testResults) {
		for (int colNo = 0; colNo < expectedRow.size() && colNo < actualRow.size(); colNo++) {
			Cell cell = expectedRow.at(colNo);
			String actualText = actualRow.get(colNo);
			String expectedText = resolve(cell.text());
			if (expectedText.equals(actualText))
				cell.pass(testResults);
			else
				cell.fail(testResults,actualText,this);
		}
		missingActualCellValues(expectedRow, actualRow, testResults);
		missingExpectedCellValues(expectedRow, actualRow, testResults);
	}
//	private void matchRow(Row expectedRow, List<String> actualRow, TestResults testResults) {
//		for (int colNo = 0; colNo < expectedRow.size() && colNo < actualRow.size(); colNo++) {
//			Cell cell = expectedRow.at(colNo);
//			String actualText = actualRow.get(colNo);
//			String expectedText = runtime().dynamicVariables().resolve(cell.text());
//			if (expectedText.equals(actualText))
//				cell.pass(testResults);
//			else {
//				if (expectedText.equals(cell.text())) // No @{property} involved:
//					cell.fail(testResults,actualText.toString(),this);
//				else {
//					cell.fail(testResults);
//					cell.parse.body = cell.parse.body+"<i>: " + expectedText + "<hr/>But was</i>"+": "+actualText;
//				}
//			}
//		}
//		missingActualCellValues(expectedRow, actualRow, testResults);
//		missingExpectedCellValues(expectedRow, actualRow, testResults);
//	}
	private void missingExpectedCellValues(Row expectedRow, List<String> actualRow, TestResults testResults) {
		for (int colNo = expectedRow.size(); colNo < actualRow.size(); colNo++) {
			Cell cell = TableFactory.cell(actualRow.get(colNo));
			expectedRow.add(cell);
			cell.actualElementMissing(testResults);
		}
	}
//	private void missingExpectedCellValues(Row expectedRow, List<String> actualRow, TestResults testResults) {
//		for (int colNo = expectedRow.size(); colNo < actualRow.size(); colNo++) {
//			Parse extraCell = new Parse("td",actualRow.get(colNo)+"<i> extra</i>",null,null);
//			new Cell(extraCell).fail(testResults);
//			expectedRow.parse.parts.last().more = extraCell;
//		}
//	}
	private void missingActualCellValues(Row expectedRow, List<String> actualRow, TestResults testResults) {
		for (int colNo = actualRow.size(); colNo < expectedRow.size(); colNo++)
			expectedRow.at(colNo).actualElementMissing(testResults);
	}
}
