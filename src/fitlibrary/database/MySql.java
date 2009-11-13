/*
 * Copyright (c) 2009 Rick Mugridge, www.RimuResearch.com
 * Kindly donated by Air New Zealand in October 2009.
 * Released under the terms of the GNU General Public License version 2 or later.
*/
package fitlibrary.database;

import java.sql.SQLException;

import dbfit.MySqlTest;
import fitlibrary.DoFixture;

public class MySql extends DoFixture {
	private SubMySqlTest mySql = new SubMySqlTest();
	
	public MySql() {
		setSystemUnderTest(mySql);
	}
	public void connectWithUserAndPasswordTo(String host, String username, String password, String database) throws SQLException {
		mySql.connect(host, username, password, database);
	}
	@Override
	public void tearDown() throws Exception {
		try {
			super.tearDown();
			mySql.rollingback();
		} catch (NullPointerException e) {
			//
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static class SubMySqlTest extends MySqlTest {
		public void rollingback() throws SQLException {
			if (this.environment != null)
				close();
		}
	}
}
