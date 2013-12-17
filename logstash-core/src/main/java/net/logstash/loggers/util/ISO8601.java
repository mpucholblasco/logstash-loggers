package net.logstash.loggers.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Contains useful methods to convert from several entities to a valid string
 * ISO8601 date format.
 * 
 * @author mpucholblasco
 * 
 */
public final class ISO8601 {
	private final static TimeZone DEFAULT_TIMEZONE = TimeZone
			.getTimeZone("UTC");

	private DateFormat dateFormat = new SimpleDateFormat(
			"yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

	public ISO8601(final TimeZone timeZone) {
		setTimeZone(timeZone);
	}

	/**
	 * Creates a new instance of ISO8601Timestamp using default timezone: UTC.
	 */
	public ISO8601() {
		this(DEFAULT_TIMEZONE);
	}

	public ISO8601 setTimeZone(final TimeZone timeZone) {
		dateFormat.setTimeZone(timeZone);
		return this;
	}

	public ISO8601 setTimeZone(final String timeZone) {
		setTimeZone(TimeZone.getTimeZone(timeZone));
		return this;
	}

	/**
	 * Gets ISO8601 format giving a string in ISO8601 format. Date will be
	 * parsed to know if it is in a correct format.
	 * 
	 * @param date
	 *            date in ISO8601 format.
	 * @throws ParseException
	 *             if date is not in ISO8601 format.
	 */
	public String getStringDate(final String date) throws ParseException {
		return dateFormat.format(dateFormat.parse(date));
	}

	/**
	 * Gets ISO8601 format giving a date.
	 * 
	 * @param date
	 *            date.
	 */
	public String getStringDate(final Date date) {
		return dateFormat.format(date);
	}

	/**
	 * Gets ISO8601 format from current date.
	 */
	public String getStringDate() {
		return dateFormat.format(new Date());
	}

	/**
	 * Gets ISO8601 format from milliseconds since 1970.
	 * 
	 * @param millis
	 *            time in milliseconds since 1970.
	 */
	public String getStringDate(long millis) {
		return dateFormat.format(new Date(millis));
	}
}
