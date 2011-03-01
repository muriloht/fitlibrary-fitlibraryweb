/*
 * Copyright (c) 2009 Rick Mugridge, www.RimuResearch.com
 * Kindly donated by Air New Zealand in October 2009.
 * Released under the terms of the GNU General Public License version 2 or later.
*/
package fitlibrary.date;

import java.util.TimeZone;

import org.joda.time.DateTime;
import org.joda.time.DateTime.Property;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import fitlibrary.DoFixture;
import fitlibrary.annotation.NullaryAction;
import fitlibrary.annotation.SimpleAction;
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
	@SimpleAction(wiki="|''<i>+</i>''|years|''<i>years</i>''|",
			tooltip="Add the given number of years to the current date.")
	public void plusYears(int offset) {
		dateTime = dateTime.plusYears(offset);
	}
	@SimpleAction(wiki="|''<i>+</i>''|months|''<i>months</i>''|",
			tooltip="Add the given number of months to the current date.")
	public void plusMonths(int offset) {
		dateTime = dateTime.plusMonths(offset);
	}
	@SimpleAction(wiki="|''<i>+</i>''|weeks|''<i>weeks</i>''|",
			tooltip="Add the given number of weeks to the current date.")
	public void plusWeeks(int offset) {
		dateTime = dateTime.plusWeeks(offset);
	}
	@SimpleAction(wiki="|''<i>+</i>''|days|''<i>days</i>''|",
			tooltip="Add the given number of days to the current date.")
	public void plusDays(int offset) {
		dateTime = dateTime.plusDays(offset);
	}
	@SimpleAction(wiki="|''<i>+</i>''|hours|''<i>hours</i>''|",
			tooltip="Add the given number of hours to the current date.")
	public void plusHours(int offset) {
		dateTime = dateTime.plusHours(offset);
	}
	@SimpleAction(wiki="|''<i>+</i>''|minutes|''<i>minutes</i>''|",
			tooltip="Add the given number of minutes to the current date.")
	public void plusMinutes(int offset) {
		dateTime = dateTime.plusMinutes(offset);
	}
	@NullaryAction(tooltip="Select the last day of the current month.")
	public void lastDayOfMonth() {
		Property day = dateTime.dayOfMonth();
		dateTime = dateTime.plusDays(day.getMaximumValue() - day.get());
	}
	@SimpleAction(wiki="|''<i>-</i>''|years|''<i>years</i>''|",
			tooltip="Subtract the given number of years to the current date.")
	public void minusYears(int offset) {
		plusYears(-offset);
	}
	@SimpleAction(wiki="|''<i>-</i>''|months|''<i>months</i>''|",
			tooltip="Subtract the given number of months to the current date.")
	public void minusMonths(int offset) {
		plusMonths(-offset);
	}
	@SimpleAction(wiki="|''<i>-</i>''|weeks|''<i>weeks</i>''|",
			tooltip="Subtract the given number of weeks to the current date.")
	public void minusWeeks(int offset) {
		plusWeeks(-offset);
	}
	@SimpleAction(wiki="|''<i>-</i>''|days|''<i>days</i>''|",
			tooltip="Subtract the given number of days to the current date.")
	public void minusDays(int offset) {
		plusDays(-offset);
	}
	@SimpleAction(wiki="|''<i>-</i>''|hours|''<i>hours</i>''|",
			tooltip="Subtract the given number of hours to the current date.")
	public void minusHours(int offset) {
		plusHours(-offset);
	}
	@SimpleAction(wiki="|''<i>-</i>''|minutes|''<i>minutes</i>''|",
			tooltip="Subtract the given number of minutes to the current date.")
	public void minusMinutes(int offset) {
		plusMinutes(-offset);
	}
	@NullaryAction(tooltip="Use upper case.")
	public void toUpper() {
		toUpper = true;
	}
	@SimpleAction(wiki="|''<i>format</i>''|format|",
			tooltip="Specify the format to use.\nFor date formats available, see http://joda-time.sourceforge.net/api-release/org/joda/time/format/DateTimeFormat.html.")
	public void format(String s) {
		formatString = s;
	}
	@NullaryAction(tooltip="Select the next day that's a Monday (or previous, if going back in time).")
	public void onMonday() {
		requireDay(1);
	}
	@NullaryAction(tooltip="Select the next day that's a Tuesday (or previous, if going back in time).")
	public void onTuesday() {
		requireDay(2);
	}
	@NullaryAction(tooltip="Select the next day that's a Wednesday (or previous, if going back in time).")
	public void onWednesday() {
		requireDay(3);
	}
	@NullaryAction(tooltip="Select the next day that's a Thursday (or previous, if going back in time)")
	public void onThursday() {
		requireDay(4);
	}
	@NullaryAction(tooltip="Select the next day that's a Friday (or previous, if going back in time)")
	public void onFriday() {
		requireDay(5);
	}
	@NullaryAction(tooltip="Select the next day that's a Saturday (or previous, if going back in time)")
	public void onSaturday() {
		requireDay(6);
	}
	@NullaryAction(tooltip="Select the next day that's a Sunday (or previous, if going back in time)")
	public void onSunday() {
		requireDay(7);
	}
	@SimpleAction(wiki="|''<i>time zone</i>''|time zone ID|",
			tooltip="Specify the time zone to use.\nUse the help with dates action to find out the available IDs")
	public void timeZone(String timeZoneID) {
		if (!aValidTimeZoneID(timeZoneID))
			throw new FitLibraryException(
					"Invalid time zone: use action |possible time zones| to see valid ones");
		if (millis > 0)
			dateTime = new DateTime(millis, DateTimeZone.forID(timeZoneID));
		else
			dateTime = new DateTime(DateTimeZone.forID(timeZoneID));
	}
	@NullaryAction(tooltip="Provide help with formats and time zones for dates.")
	public void helpWithDates() {
		showAfterTable("For date formats available, see <a href='http://joda-time.sourceforge.net/api-release/org/joda/time/format/DateTimeFormat.html'>joda-time</a>");
		showAfterTable("");
		showAfterTable("Possible time zones:");
		for (String id : TimeZone.getAvailableIDs())
			showAfterTable(id);
	}
	@SimpleAction(wiki="|''<i>create variable</i>''|dynamic variable name|",
			tooltip="Assign the current date to the given dynamic variable")
	public void createVariable(String key) {
		setDynamicVariable(key, result());
	}
	@SimpleAction(wiki="|''<i>create variable</i>''|dynamic variable name|''<i>with format</i>''|format|",
			tooltip="Assign the current date to the given dynamic variable using the given format.\nFor date formats available, see http://joda-time.sourceforge.net/api-release/org/joda/time/format/DateTimeFormat.html.")
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
