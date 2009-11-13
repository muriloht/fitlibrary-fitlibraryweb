/*
 * Copyright (c) 2009 Rick Mugridge, www.RimuResearch.com
 * Kindly donated by Air New Zealand in October 2009.
 * Released under the terms of the GNU General Public License version 2 or later.
*/
package fitlibrary.database;

import java.sql.SQLException;

import dbfit.OracleTest;
import fitlibrary.DoFixture;

public class Oracle extends DoFixture {
	private OracleTest oracle = new OracleTest();
	
	public Oracle() {
		setSystemUnderTest(oracle);
	}
	public void connectWithUserAndPasswordTo(String host, String username, String password, String database) throws SQLException {
		oracle.connect(host, username, password, database);
	}
	@Override
	public void tearDown() throws Exception {
		try {
			super.tearDown();
			oracle.close();
		} catch (Exception e) {
			//
		}
	}
}
