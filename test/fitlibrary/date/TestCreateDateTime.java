/*
 * Copyright (c) 2009 Rick Mugridge, www.RimuResearch.com
 * Kindly donated by Air New Zealand in October 2009.
 * Released under the terms of the GNU General Public License version 2 or later.
*/
package fitlibrary.date;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;

public class TestCreateDateTime {
	CreateDate createDate = new CreateDate(1243987143111L); // Fix the date/time for theses tests
	
	@Before
	public void setTimeZone() {
		createDate.timeZone("NZ");
	}
	@Test public void today() {
		assertThat(createDate.result(),equalTo("3 Jun 2009"));
	}
	@Test public void todayInUpperCase() {
		createDate.toUpper();
		assertThat(createDate.result(),equalTo("3 JUN 2009"));
	}
	@Test public void now() {
		createDate.format("EEE dd MMMMMMMMM yyyy H:mma");
		assertThat(createDate.result(),equalTo("Wed 03 June 2009 11:59AM"));
	}
	@Test public void nowWithSeconds() {
		createDate.format("dd MMMMMMMMM yyyy H:mm:ss");
		assertThat(createDate.result(),equalTo("03 June 2009 11:59:03"));
	}
	@Test public void in10days() {
		createDate.plusDays(10);
		assertThat(createDate.result(),equalTo("13 Jun 2009"));
	}
	@Test public void back10days() {
		createDate.minusDays(10);
		assertThat(createDate.result(),equalTo("24 May 2009"));
	}
	@Test public void in2months() {
		createDate.plusMonths(2);
		assertThat(createDate.result(),equalTo("3 Aug 2009"));
	}
	@Test public void back2months() {
		createDate.plusMonths(-2);
		assertThat(createDate.result(),equalTo("3 Apr 2009"));
	}
	@Test public void in2yearsAnd1monthAnd2weeksWithFormat() {
		createDate.plusYears(2);
		createDate.plusMonths(1);
		createDate.plusWeeks(2);
		createDate.format("EEE d MMM yyyy");
		assertThat(createDate.result(),equalTo("Sun 17 Jul 2011"));
	}
	@Test public void onSpecificDayInFuture() {
		createDate.format("EEE d MMM yyyy");
		createDate.plusDays(3);
		assertThat(createDate.result(),equalTo("Sat 6 Jun 2009"));
		createDate.onMonday();
		assertThat(createDate.result(),equalTo("Mon 8 Jun 2009"));
		createDate.onTuesday();
		assertThat(createDate.result(),equalTo("Tue 9 Jun 2009"));
		createDate.onWednesday();
		assertThat(createDate.result(),equalTo("Wed 10 Jun 2009"));
		createDate.onThursday();
		assertThat(createDate.result(),equalTo("Thu 11 Jun 2009"));
		createDate.onFriday();
		assertThat(createDate.result(),equalTo("Fri 12 Jun 2009"));
		createDate.onSaturday();
		assertThat(createDate.result(),equalTo("Sat 13 Jun 2009"));
		createDate.onSunday();
		assertThat(createDate.result(),equalTo("Sun 14 Jun 2009"));
	}
	@Test public void onSpecificDayInPast() {
		createDate.format("EEE d MMM yyyy");
		assertThat(createDate.result(),equalTo("Wed 3 Jun 2009"));
		createDate.minusDays(1);
		assertThat(createDate.result(),equalTo("Tue 2 Jun 2009"));
		createDate.onMonday();
		assertThat(createDate.result(),equalTo("Mon 8 Jun 2009"));
		createDate.onTuesday();
		assertThat(createDate.result(),equalTo("Tue 9 Jun 2009"));
		createDate.onWednesday();
		assertThat(createDate.result(),equalTo("Wed 10 Jun 2009"));
	}
	@Test public void differentTimeZone() {
		createDate.timeZone("America/Los_Angeles");
		createDate.format("d MMMMMMMMM yyyy H:mm");
		assertThat(createDate.result(),equalTo("2 June 2009 16:59"));
	}
	@Test public void lastDayOfMonth() {
		createDate.format("d MMMMMMMMM yyyy");
		createDate.lastDayOfMonth();
		assertThat(createDate.result(),equalTo("30 June 2009"));
	}
	@Test public void laterLastDayOfMonthOverridesParticularDay() {
		createDate.format("d MMMMMMMMM yyyy");
		createDate.onTuesday();
		createDate.lastDayOfMonth();
		assertThat(createDate.result(),equalTo("30 June 2009"));
	}
	@Test public void laterParticularDayOverridesLastDayOfMonth() {
		createDate.format("d MMMMMMMMM yyyy");
		createDate.lastDayOfMonth();
		createDate.onWednesday();
		assertThat(createDate.result(),equalTo("1 July 2009"));
	}
}
