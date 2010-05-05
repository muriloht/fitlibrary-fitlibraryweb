/*
 * Copyright (c) 2009 Rick Mugridge, www.RimuResearch.com
 * Kindly donated by Air New Zealand in October 2009.
 * Released under the terms of the GNU General Public License version 2 or later.
*/
package fitlibrary.spider;

import java.util.regex.Pattern;

import fitlibrary.runResults.TestResults;
import fitlibrary.table.Cell;
import fitlibrary.table.Row;
import fitlibrary.table.Table;
import fitlibrary.table.TableFactory;
import fitlibrary.traverse.Traverse;

public class MultiLineMatchFixture extends Traverse {
	private Object[] lines;

	public MultiLineMatchFixture(Object[] lines) {
		this.lines = lines;
	}
	@Override
	public Object interpretAfterFirstRow(Table table, TestResults testResults) {
		if (lines.length == 0 && table.size() == 1)
			table.at(0).at(0).pass(testResults);
		for (int rowNo = 1; rowNo < table.size(); rowNo++) {
			Row row = table.at(rowNo);
			Cell cell = row.at(0);
			if (row.size() > 1) {
				cell.error(testResults, "extra cell(s)");
				return null;
			}
			if (lines.length >= rowNo) {
				String regExp = cell.text(this);
				String actualText = "";
				if (lines[rowNo-1] != null)
					actualText = lines[rowNo-1].toString().trim();
				if (Pattern.compile(regExp,Pattern.DOTALL).matcher(actualText.trim()).matches())
					cell.pass(testResults);
				else
					cell.failWithStringEquals(testResults, actualText, this);
			} else
				cell.actualElementMissing(testResults);
		}
		for (int i = table.size()-1; i < lines.length; i++) {
			Cell cell = TableFactory.cell("");
			cell.unexpected(testResults,"\""+lines[i].toString()+"\"");
			Row row = TableFactory.row();
			row.add(cell);
			table.add(row);
		}
		return null;
	}
}
