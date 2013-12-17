package net.logstash.loggers.util;

import static org.junit.Assert.assertEquals;

import java.text.ParseException;

import org.junit.Test;

public final class StringUtilTest {

	@Test(expected = IllegalArgumentException.class)
	public void testTrimNull() throws ParseException {
		StringUtil.trim(null);
	}

	@Test
	public void testEmpty() throws ParseException {
		assertEquals("", StringUtil.trim(""));
	}

	@Test
	public void testSpaces() throws ParseException {
		assertEquals("", StringUtil.trim("     "));
	}

	@Test
	public void testTabs() throws ParseException {
		assertEquals("", StringUtil.trim("\t\t\t"));
	}
	
	@Test
	public void testCarriageReturns() throws ParseException {
		assertEquals("", StringUtil.trim("\n\n\n"));
	}

	@Test
	public void testStartSpaces() throws ParseException {
		assertEquals("test", StringUtil.trim("  \t\t\n\ntest"));
	}

	@Test
	public void testEndSpaces() {
		assertEquals("test", StringUtil.trim("test  \t\t\n\n"));
	}

	@Test
	public void testBothSidesSpaces() {
		assertEquals("test", StringUtil.trim("\t\t\n\n  test  \t\t\n\n"));
	}

	@Test
	public void testNoSpaces() {
		assertEquals("test", StringUtil.trim("test"));
	}

	@Test
	public void testInnerSpaces() {
		assertEquals("my single test", StringUtil.trim("my single test"));
	}

	@Test
	public void testInnerSpacesWithStartOnes() {
		assertEquals("my single test", StringUtil.trim("\t\t  my single test"));
	}

	@Test
	public void testInnerSpacesWithEndOnes() {
		assertEquals("my single test",
				StringUtil.trim("my single test  \t\t\n"));
	}

	@Test
	public void testInnerSpacesWithStartAndEndOnes() {
		assertEquals("my single test",
				StringUtil.trim("   \t\tmy single test  \t\t\n"));
	}
}
