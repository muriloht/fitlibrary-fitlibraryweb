/*
 * Copyright (c) 2009 Rick Mugridge, www.RimuResearch.com
 * Kindly donated by Air New Zealand in October 2009.
 * Released under the terms of the GNU General Public License version 2 or later.
*/
package fitlibrary.service;

import fit.Fixture;
import fitlibrary.exception.FitLibraryException;
import fitlibrary.table.Row;
import fitlibrary.traverse.workflow.DoTraverse;
import fitlibrary.utility.TestResults;

public abstract class ServicingFixture extends DoTraverse {
	protected long defaultTimeout = 2500;
	
	@Override
	public Object getSystemUnderTest() {
		Object service = getService();
		return service;
	}
	@Override
	public Object interpretRow(Row row, TestResults testResults, Fixture fixtureByName) {
		try {
			return super.interpretRow(row, testResults, fixtureByName);
		} catch (Exception e) {
			throw new FitLibraryException(""+e);
		}
	}
	public void timeoutOf(long timeoutValue) {
		this.defaultTimeout = timeoutValue;
	}
	protected abstract Object getService();
}
