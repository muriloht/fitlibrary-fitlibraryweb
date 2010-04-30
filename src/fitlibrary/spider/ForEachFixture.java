/*
 * Copyright (c) 2009 Rick Mugridge, www.RimuResearch.com
 * Kindly donated by Air New Zealand in October 2009.
 * Released under the terms of the GNU General Public License version 2 or later.
*/
package fitlibrary.spider;

import java.util.List;

import fitlibrary.runResults.TestResults;
import fitlibrary.runResults.TestResultsOnCounts;
import fitlibrary.table.Row;
import fitlibrary.table.Table;
import fitlibrary.table.TableFactory;
import fitlibrary.traverse.workflow.DoTraverse;

public class ForEachFixture extends DoTraverse {
	private Object saveFromZero;
	private Object saveFromOne;
	private String iteratorName;
	private List<String> list;

	public ForEachFixture(String iteratorName, List<String> list) {
		this.iteratorName = iteratorName;
		this.list = list;
	}
	@Override
	public Object interpretAfterFirstRow(Table table, TestResults testResults) {
		saveFromZero = getDynamicVariable("fromZero");
		saveFromOne = getDynamicVariable("fromOne");
		Row callRow = table.at(1);
		boolean problems = false;
		for (int i = 0; i < list.size(); i++) {
			TestResults subTestResults = new TestResultsOnCounts();
			Row copyOfRow = copyRow(callRow);
			setDynamicVariable("fromZero",""+i);
			setDynamicVariable("fromOne",""+(i+1));
			setDynamicVariable(iteratorName,list.get(i));
			interpretRow(copyOfRow,subTestResults);
			if (subTestResults.problems())
				problems = true;
			Row newRow = table.newRow();
			newRow.add(TableFactory.cell("note"));
			newRow.add(TableFactory.cell(TableFactory.tables(TableFactory.table(copyOfRow))));
		}
		if (saveFromZero != null)
			setDynamicVariable("fromZero",saveFromZero == null ? null : saveFromZero.toString());
		if (saveFromOne != null)
			setDynamicVariable("fromOne",saveFromOne == null ? null : saveFromOne.toString());
		table.at(1).at(0).passOrFail(testResults, !problems);
		return null;
	}
	private Row copyRow(Row callRow) {
		Row row = TableFactory.row();
		for (int i = 0; i < callRow.size(); i++)
			row.addCell(callRow.at(i).text());
		return row;
	}
}
