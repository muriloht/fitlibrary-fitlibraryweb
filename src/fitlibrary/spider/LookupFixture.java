/*
 * Copyright (c) 2009 Rick Mugridge, www.RimuResearch.com
 * Kindly donated by Air New Zealand in October 2009.
 * Released under the terms of the GNU General Public License version 2 or later.
*/
package fitlibrary.spider;

import java.util.regex.Pattern;

import fitlibrary.exception.FitLibraryException;
import fitlibrary.runResults.TestResults;
import fitlibrary.table.Row;
import fitlibrary.table.Table;
import fitlibrary.traverse.Traverse;

public class LookupFixture extends Traverse {
	private String[] header;
	private boolean[] matchOn;

	@Override
	public Object interpretAfterFirstRow(Table table, TestResults testResults) {
		Row headerRow = table.at(1);
		int headerRowSize = headerRow.size();
		
		analyseHeaders(headerRow, headerRowSize);
			
		if (!matchInTable(table, headerRowSize))
			table.at(0).at(0).error(testResults, "No match");
		return null;
	}
	private boolean analyseHeaders(Row headerRow, int headerRowSize) {
		header = new String[headerRowSize];
		matchOn = new boolean[headerRowSize];
		boolean atLeastOneSetVariableHeaderFound = false;
		for (int i = 0; i < headerRowSize; i++) {
			matchOn[i] = true;
			header[i] = headerRow.text(i, this);
			if (header[i].endsWith("?")) {
				header[i] = header[i].substring(0,header[i].length()-1);
				matchOn[i] = false;
				atLeastOneSetVariableHeaderFound = true;
			}
		}
		
		if (atLeastOneSetVariableHeaderFound)
			return true;
		
		throw new FitLibraryException("No header with '?'");
	}
	private boolean matchInTable(Table table, int headerRowSize) {
		for (int rowNo = 2; rowNo < table.size(); rowNo++) {
			Row row = table.at(rowNo);
			if (row.size() != headerRowSize)
				throw new FitLibraryException("Row needs to be "+headerRowSize+" cells wide");
			if (matchRow(row)) {
				updateVariables(row);
				return true;
			}
		}
		return false;
	}
	private boolean matchRow(Row row) {
		for (int cell = 0; cell < row.size(); cell++) {
			String regEx = row.text(cell,this);
			if (matchOn[cell] && !Pattern.compile(".*"+regEx+".*",Pattern.DOTALL).matcher(header[cell]).matches()) {
				return false;
			}
		}
		return true;
	}
	private void updateVariables(Row row) {
		for (int cell = 0; cell < row.size(); cell++)
			if (!matchOn[cell])
				getDynamicVariables().put(header[cell], row.text(cell,this));
	}
}
