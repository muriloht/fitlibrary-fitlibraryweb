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
		assertThat(createDate.result(),equalTo("3 jun 2009"));
	}
	@Test public void todayInUpperCase() {
		createDate.toUpper();
		assertThat(createDate.result(),equalTo("3 JUN 2009"));
	}
	@Test public void now() {
		createDate.format("EEE dd MMMMMMMMM yyyy H:mma");
		assertThat(createDate.result(),equalTo("qua 03 junho 2009 11:59AM"));
	}
	@Test public void nowWithSeconds() {
		createDate.format("dd MMMMMMMMM yyyy H:mm:ss");
		assertThat(createDate.result(),equalTo("03 junho 2009 11:59:03"));
	}
	@Test public void in10days() {
		createDate.plusDays(10);
		assertThat(createDate.result(),equalTo("13 jun 2009"));
	}
	@Test public void back10days() {
		createDate.minusDays(10);
		assertThat(createDate.result(),equalTo("24 mai 2009"));
	}
	@Test public void in2months() {
		createDate.plusMonths(2);
		assertThat(createDate.result(),equalTo("3 ago 2009"));
	}
	@Test public void back2months() {
		createDate.plusMonths(-2);
		assertThat(createDate.result(),equalTo("3 abr 2009"));
	}
	@Test public void in2yearsAnd1monthAnd2weeksWithFormat() {
		createDate.plusYears(2);
		createDate.plusMonths(1);
		createDate.plusWeeks(2);
		createDate.format("EEE d MMM yyyy");
		assertThat(createDate.result(),equalTo("dom 17 jul 2011"));
	}
	@Test public void onSpecificDayInFuture() {
		createDate.format("EEE d MMM yyyy");
		createDate.plusDays(3);
		assertThat(createDate.result(),equalTo("sáb 6 jun 2009"));
		createDate.onMonday();
		assertThat(createDate.result(),equalTo("seg 8 jun 2009"));
		createDate.onTuesday();
		assertThat(createDate.result(),equalTo("ter 9 jun 2009"));
		createDate.onWednesday();
		assertThat(createDate.result(),equalTo("qua 10 jun 2009"));
		createDate.onThursday();
		assertThat(createDate.result(),equalTo("qui 11 jun 2009"));
		createDate.onFriday();
		assertThat(createDate.result(),equalTo("sex 12 jun 2009"));
		createDate.onSaturday();
		assertThat(createDate.result(),equalTo("sáb 13 jun 2009"));
		createDate.onSunday();
		assertThat(createDate.result(),equalTo("dom 14 jun 2009"));
	}
	@Test public void onSpecificDayInPast() {
		createDate.format("EEE d MMM yyyy");
		assertThat(createDate.result(),equalTo("qua 3 jun 2009"));
		createDate.minusDays(1);
		assertThat(createDate.result(),equalTo("ter 2 jun 2009"));
		createDate.onMonday();
		assertThat(createDate.result(),equalTo("seg 8 jun 2009"));
		createDate.onTuesday();
		assertThat(createDate.result(),equalTo("ter 9 jun 2009"));
		createDate.onWednesday();
		assertThat(createDate.result(),equalTo("qua 10 jun 2009"));
	}
	@Test public void differentTimeZone() {
		createDate.timeZone("America/Los_Angeles");
		createDate.format("d MMMMMMMMM yyyy H:mm");
		assertThat(createDate.result(),equalTo("2 junho 2009 16:59"));
	}
	@Test public void lastDayOfMonth() {
		createDate.format("d MMMMMMMMM yyyy");
		createDate.lastDayOfMonth();
		assertThat(createDate.result(),equalTo("30 junho 2009"));
	}
	@Test public void laterLastDayOfMonthOverridesParticularDay() {
		createDate.format("d MMMMMMMMM yyyy");
		createDate.onTuesday();
		createDate.lastDayOfMonth();
		assertThat(createDate.result(),equalTo("30 junho 2009"));
	}
	@Test public void laterParticularDayOverridesLastDayOfMonth() {
		createDate.format("d MMMMMMMMM yyyy");
		createDate.lastDayOfMonth();
		createDate.onWednesday();
		assertThat(createDate.result(),equalTo("1 julho 2009"));
	}
}
