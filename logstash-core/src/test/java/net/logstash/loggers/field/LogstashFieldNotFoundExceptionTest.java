package net.logstash.loggers.field;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class LogstashFieldNotFoundExceptionTest {
	@Test
	public void testGetField() throws Throwable {
		LogstashFieldNotFoundException ex = new LogstashFieldNotFoundException(
				"FieldNotFound");
		assertEquals("FieldNotFound", ex.getField());
	}

	@Test
	public void testToString() throws Throwable {
		LogstashFieldNotFoundException ex = new LogstashFieldNotFoundException(
				"FieldNotFound");
		assertTrue(ex.toString().contains("FieldNotFound"));
	}
}
