/*
 * Copyright (c) 2009 Rick Mugridge, www.RimuResearch.com
 * Kindly donated by Air New Zealand in October 2009.
 * Released under the terms of the GNU General Public License version 2 or later.
*/
package fitlibrary.spider;

import java.util.List;

import fitlibrary.table.Cell;
import fitlibrary.table.Row;
import fitlibrary.table.Table;
import fitlibrary.table.Tables;
import fitlibrary.traverse.workflow.DoTraverse;
import fitlibrary.utility.TestResults;

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
		Row callRow = table.row(1);
		boolean problems = false;
		for (int i = 0; i < list.size(); i++) {
			TestResults subTestResults = new TestResults();
			Row copyOfRow = copyRow(callRow);
			setDynamicVariable("fromZero",""+i);
			setDynamicVariable("fromOne",""+(i+1));
			setDynamicVariable(iteratorName,list.get(i));
			interpretRow(copyOfRow,subTestResults,null);
			if (subTestResults.problems())
				problems = true;
			table.lastRow().addCommentRow(new Cell(new Tables(new Table(copyOfRow))));
		}
		if (saveFromZero != null)
			setDynamicVariable("fromZero",saveFromZero == null ? null : saveFromZero.toString());
		if (saveFromOne != null)
			setDynamicVariable("fromOne",saveFromOne == null ? null : saveFromOne.toString());
		table.row(1).passOrFail(testResults, !problems);
		return null;
	}
	private Row copyRow(Row callRow) {
		Row row = new Row();
		for (int i = 0; i < callRow.size(); i++)
			row.addCell(callRow.cell(i).text());
		return row;
	}
	@Override
	public String get(String s) {
		return s;
	}
}
