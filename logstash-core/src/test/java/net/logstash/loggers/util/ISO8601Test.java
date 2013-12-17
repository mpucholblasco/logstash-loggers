package net.logstash.loggers.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.text.ParseException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import org.junit.Test;

public final class ISO8601Test {

	@Test
	public void testDefaultCurrentDate() throws ParseException {
		ISO8601 iso8601 = new ISO8601();
		assertNotNull(iso8601.getStringDate());
	}

	@Test
	public void testDefaultDate() throws ParseException {
		ISO8601 iso8601 = new ISO8601();
		Calendar calendar = GregorianCalendar.getInstance(TimeZone
				.getTimeZone("UTC"));
		calendar.setTimeInMillis(0);
		calendar.set(2013, 10, 17, 12, 18, 00);
		assertEquals("2013-11-17T12:18:00.000Z",
				iso8601.getStringDate(calendar.getTime()));
	}

	@Test
	public void testDefaultValidString() throws ParseException {
		ISO8601 iso8601 = new ISO8601();
		assertEquals("2013-11-17T12:18:00.000Z",
				iso8601.getStringDate("2013-11-17T12:18:00.000Z"));
	}

	@Test(expected = ParseException.class)
	public void testDefaultInvalidString() throws ParseException {
		ISO8601 iso8601 = new ISO8601();
		assertEquals("2013-11-17T12:18:00.000Z",
				iso8601.getStringDate("notAValidDate"));
	}

	@Test
	public void testDefaultMilliseconds() throws ParseException {
		ISO8601 iso8601 = new ISO8601();
		assertEquals("2001-11-09T01:46:40.123Z",
				iso8601.getStringDate(1005270400123L));
	}

	@Test
	public void testGMT1CurrentDate() throws ParseException {
		ISO8601 iso8601 = getISO8601GMT1();
		assertNotNull(iso8601.getStringDate());
	}

	@Test
	public void testGMT1Date() throws ParseException {
		ISO8601 iso8601 = getISO8601GMT1();
		Calendar calendar = GregorianCalendar.getInstance(TimeZone
				.getTimeZone("UTC"));
		calendar.setTimeInMillis(0);
		calendar.set(2013, 10, 17, 12, 18, 00);
		assertEquals("2013-11-17T13:18:00.000Z",
				iso8601.getStringDate(calendar.getTime()));
	}

	@Test
	public void testGMT1ValidString() throws ParseException {
		ISO8601 iso8601 = getISO8601GMT1();
		assertEquals("2013-11-17T12:18:00.000Z",
				iso8601.getStringDate("2013-11-17T12:18:00.000Z"));
	}

	@Test(expected = ParseException.class)
	public void testGM1InvalidString() throws ParseException {
		ISO8601 iso8601 = getISO8601GMT1();
		assertEquals("2013-11-17T12:18:00.000Z",
				iso8601.getStringDate("notAValidDate"));
	}

	@Test
	public void testGMT1Milliseconds() throws ParseException {
		ISO8601 iso8601 = getISO8601GMT1();
		assertEquals("2001-11-09T02:46:40.123Z",
				iso8601.getStringDate(1005270400123L));
	}

	private final ISO8601 getISO8601GMT1() {
		return new ISO8601().setTimeZone("GMT+1");
	}
}
