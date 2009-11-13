/*
 * Copyright (c) 2009 Rick Mugridge, www.RimuResearch.com
 * Kindly donated by Air New Zealand in October 2009.
 * Released under the terms of the GNU General Public License version 2 or later.
*/
package fitlibrary.spider;

import java.util.regex.Pattern;

import fitlibrary.exception.FitLibraryException;
import fitlibrary.table.Row;
import fitlibrary.table.Table;
import fitlibrary.traverse.Traverse;
import fitlibrary.utility.TestResults;

public class LookupFixture extends Traverse {
	private String[] header;
	private boolean[] matchOn;

	@Override
	public Object interpretAfterFirstRow(Table table, TestResults testResults) {
		Row headerRow = table.row(1);
		int headerRowSize = headerRow.size();
		boolean aLookup = analyseHeaders(headerRow, headerRowSize);
		if (!aLookup)
			throw new FitLibraryException("No header with '?'");
		if (!matchInTable(table, headerRowSize))
			table.row(0).cell(0).error(testResults, "No match");
		return null;
	}
	private boolean analyseHeaders(Row headerRow, int headerRowSize) {
		header = new String[headerRowSize];
		matchOn = new boolean[headerRowSize];
		for (int i = 0; i < headerRowSize; i++) {
			matchOn[i] = true;
			header[i] = headerRow.text(i, this);
			if (header[i].endsWith("?")) {
				header[1] = header[1].substring(0,header[1].length()-1);
				matchOn[i] = false;
				return true;
			}
		}
		return false;
	}
	private boolean matchInTable(Table table, int headerRowSize) {
		for (int rowNo = 2; rowNo < table.size(); rowNo++) {
			Row row = table.row(rowNo);
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
			String regEx = row.text(0,this);
			if (matchOn[cell] && !Pattern.compile(".*"+regEx+".*",Pattern.DOTALL).matcher(header[cell]).matches())
					return false;
		}
		return true;
	}
	private void updateVariables(Row row) {
		for (int cell = 0; cell < row.size(); cell++)
			if (!matchOn[cell])
				runtime().dynamicVariables().put(header[cell], row.text(cell,this));
	}
}
