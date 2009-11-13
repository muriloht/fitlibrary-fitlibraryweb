/*
 * Copyright (c) 2009 Rick Mugridge, www.RimuResearch.com
 * Kindly donated by Air New Zealand in October 2009.
 * Released under the terms of the GNU General Public License version 2 or later.
*/
package fitlibrary.date;

import java.util.TimeZone;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.DateTime.Property;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import fitlibrary.DoFixture;
import fitlibrary.exception.FitLibraryException;

public class CreateDate extends DoFixture {
	private DateTime dateTime = new DateTime();
	private long millis = -1;

	private boolean toUpper = false;
	private String formatString = "d MMM yyyy";

	public CreateDate() {
		//
	}
	public CreateDate(long millis) { // Used for unit testing
		this.millis = millis;
		dateTime = new DateTime(millis);
	}
	public void dateIsUsing(String date, String parseFormat) {
		dateTime = DateTimeFormat.forPattern(parseFormat).parseDateTime(date);
	}
	public String result() {
		return formatted(dateTime);
	}
	public void plusYears(int offset) {
		dateTime = dateTime.plusYears(offset);
	}
	public void plusMonths(int offset) {
		dateTime = dateTime.plusMonths(offset);
	}
	public void plusWeeks(int offset) {
		dateTime = dateTime.plusWeeks(offset);
	}
	public void plusDays(int offset) {
		dateTime = dateTime.plusDays(offset);
	}
	public void plusHours(int offset) {
		dateTime = dateTime.plusHours(offset);
	}
	public void plusMinutes(int offset) {
		dateTime = dateTime.plusMinutes(offset);
	}
	public void lastDayOfMonth() {
		Property day = dateTime.dayOfMonth();
		dateTime = dateTime.plusDays(day.getMaximumValue() - day.get());
	}
	public void minusYears(int offset) {
		plusYears(-offset);
	}
	public void minusMonths(int offset) {
		plusMonths(-offset);
	}
	public void minusDays(int offset) {
		plusDays(-offset);
	}
	public void minusHours(int offset) {
		plusHours(-offset);
	}
	public void minusMinutes(int offset) {
		plusMinutes(-offset);
	}
	public void toUpper() {
		toUpper = true;
	}
	public void format(String s) {
		formatString = s;
	}
	public void onMonday() {
		requireDay(1);
	}
	public void onTuesday() {
		requireDay(2);
	}
	public void onWednesday() {
		requireDay(3);
	}
	public void onThursday() {
		requireDay(4);
	}
	public void onFriday() {
		requireDay(5);
	}
	public void onSaturday() {
		requireDay(6);
	}
	public void onSunday() {
		requireDay(7);
	}
	public void timeZone(String timeZoneID) {
		if (!aValidTimeZoneID(timeZoneID))
			throw new FitLibraryException(
					"Invalid time zone: use action |possible time zones| to see valid ones");
		if (millis > 0)
			dateTime = new DateTime(millis, DateTimeZone.forID(timeZoneID));
		else
			dateTime = new DateTime(DateTimeZone.forID(timeZoneID));
	}
	public void help() {
		showAfterTable("For date formats available, see <a href='http://joda-time.sourceforge.net/api-release/org/joda/time/format/DateTimeFormat.html'>joda-time</a>");
		showAfterTable("");
		showAfterTable("Possible time zones:");
		for (String id : TimeZone.getAvailableIDs())
			showAfterTable(id);
	}
	public void createVariable(String key) {
		setDynamicVariable(key, result());
	}
	public void createVariableWithFormat(String key, String format) {
		format(format);
		setDynamicVariable(key, result());
	}
	public void pickDateTime(long milliseconds) {
		millis = milliseconds;
		dateTime = new DateTime(milliseconds);
	}
	private int moveForwardsOrBackwards(DateTime dateTimeUpdated) {
		if (dateTimeUpdated.isBefore(dateTime))
			return -1;
		return 1;
	}
	private String formatted(DateTime dateTimeUpdated) {
		DateTimeFormatter format = DateTimeFormat.forPattern(formatString);
		String result = dateTimeUpdated.toString(format);
		if (toUpper)
			return result.toUpperCase();
		return result;
	}
	private boolean aValidTimeZoneID(String timeZoneID) {
		for (String id : TimeZone.getAvailableIDs())
			if (id.equals(timeZoneID))
				return true;
		return false;
	}
	private void requireDay(int day) {
		int deltaDay = moveForwardsOrBackwards(dateTime);
		while (dateTime.dayOfWeek().get() != day) {
			dateTime = dateTime.plusDays(deltaDay);
		}
	}
}
