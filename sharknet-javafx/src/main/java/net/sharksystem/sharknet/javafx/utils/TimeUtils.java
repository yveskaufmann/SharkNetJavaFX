package net.sharksystem.sharknet.javafx.utils;


import javafx.util.*;
import net.sharksystem.sharknet.javafx.i18n.I18N;

import java.text.SimpleDateFormat;
import java.time.*;
import java.time.Duration;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Date;

/**
 * Time utility functions
 */
public class TimeUtils {

	/******************************************************************************
	 *                                                                             
	 * Constants                                                                   
	 *                                                                              
	 ******************************************************************************/
	
	static final int HOURS_PER_DAY = 24;
	/**
	 * Minutes per hour.
	 */
	static final int MINUTES_PER_HOUR = 60;
	/**
	 * Minutes per day.
	 */
	static final int MINUTES_PER_DAY = MINUTES_PER_HOUR * HOURS_PER_DAY;
	/**
	 * Seconds per minute.
	 */
	static final int SECONDS_PER_MINUTE = 60;
	/**
	 * Seconds per hour.
	 */
	static final int SECONDS_PER_HOUR = SECONDS_PER_MINUTE * MINUTES_PER_HOUR;
	/**
	 * Seconds per day.
	 */
	static final int SECONDS_PER_DAY = SECONDS_PER_HOUR * HOURS_PER_DAY;


	/******************************************************************************
	 *
	 * Formats                                                                   
	 *
	 ******************************************************************************/
	
	/**
	 * Duration format for seconds
	 */
	public static final String UNIT_FORMAT_SECONDS = "timeUnit.format.seconds";

	/**
	 * Duration format for minutes
	 */
	public static final String UNIT_FORMAT_MINUTES = "timeUnit.format.minutes";

	/**
	 * Duration format for hours
	 */
	public static final String UNIT_FORMAT_HOURS = "timeUnit.format.hours";

	/**
	 * Duration format for days
	 */
	public static final String UNIT_FORMAT_DAYS = "timeUnit.format.days";

	/**
	 * Short duration format for seconds
	 */
	public static final String UNIT_FORMAT_SECONDS_ABBREV = "timeUnit.format.seconds.abbrev";

	/**
	 * Short duration format for minutes
	 */
	public static final String UNIT_FORMAT_MINUTES_ABBREV = "timeUnit.format.minutes.abbrev";

	/**
	 * Short duration format for hours
	 */
	public static final String UNIT_FORMAT_HOURS_ABBREV = "timeUnit.format.minutes.abbrev";

	/**
	 * Format which is used for the current day
	 */
	public static final DateTimeFormatter FULL_DATE_FORMAT = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM);

	/**
	 * Format which is used for the current year
	 */
	public static final DateTimeFormatter DAY_MONTH_FORMAT = DateTimeFormatter.ofPattern("d MMM");

	/***
	 * Format which is used for dates which are not in this year
	 */
	public static final DateTimeFormatter TIME_OF_DAY_FORMAT = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT);

	/******************************************************************************
	 *                                                                             
	 * Constructors                                                                   
	 *                                                                              
	 ******************************************************************************/
	
	private TimeUtils () {}

	/******************************************************************************
	 *                                                                             
	 * Methods
	 *                                                                              
	 ******************************************************************************/

	/**
	 * Formats the duration between the timestamp and the current date time.
	 * In a human-friendly format.
	 *
	 * Returns only largest meaningful unit of time, from seconds to days.
	 *
	 * @param timestamp
	 * @return human-friendly format duration format
     */
	public static String formatTimeAgo(Date timestamp) {
		Duration duration = durationSince(timestamp);
		boolean isNegative = duration.isNegative();

		long seconds = duration.abs().getSeconds();
		if (seconds > SECONDS_PER_DAY) {
			return I18N.getString(UNIT_FORMAT_DAYS, seconds / SECONDS_PER_DAY);
		}

		if (seconds > SECONDS_PER_HOUR) {
			return I18N.getString(UNIT_FORMAT_HOURS, seconds / SECONDS_PER_HOUR);
		}

		if (seconds > SECONDS_PER_MINUTE) {
			return I18N.getString(UNIT_FORMAT_MINUTES, seconds / SECONDS_PER_MINUTE);
		}

		return I18N.getString(UNIT_FORMAT_SECONDS, seconds);
	}

	/**
	 * Format a passed date object in human-friendly localized manner.
	 * For example:
	 * <ul>
	 *   <li>"date=5:30 today" will be displayed as "5:30"</li>
	 *   <li>"date=11:30 the day before" will be displayed as "Mar 30"</li>
	 *   <li>"date=11:30 the year before" will be displayed as "2015-30-3"</li>
	 * </ul>
	 *
	 * @param thenTimestamp
	 * @return
     */
	public static String formatDateTime(Date thenTimestamp) {
		LocalDateTime now = LocalDateTime.now();
		LocalDateTime then = LocalDateTime.ofInstant(thenTimestamp.toInstant(), ZoneId.systemDefault());
		Duration duration = Duration.between(now, then).abs();
		long seconds = duration.getSeconds();
		
		if (seconds < SECONDS_PER_DAY && now.getDayOfWeek() == then.getDayOfWeek()) {
			return then.format(TIME_OF_DAY_FORMAT);
		} else if (now.getYear() == then.getYear()) {
			return then.format(DAY_MONTH_FORMAT);
		} else {
			return then.format(FULL_DATE_FORMAT);
		}
	}

	/***
	 * Check if the specified date is today.
	 *
	 * @param date the specified date
     */
	public static boolean isToday(Date date) {
		LocalDate today = LocalDate.now();
		ZonedDateTime localTime = date.toInstant().atZone(ZoneId.systemDefault());
		LocalDate localDate = localTime.toLocalDate();
		return today.isEqual(localDate);
	}

	private static Duration durationSince(Date timestamp) {
		Instant birthInstant = timestamp.toInstant();
		return Duration.between(birthInstant, Instant.now());
	}

	public static void main(String[] args) {
		Date time7ago = java.sql.Timestamp.valueOf("2016-04-06 09:01:10");
		System.out.println(formatDateTime(time7ago));
	}
}
