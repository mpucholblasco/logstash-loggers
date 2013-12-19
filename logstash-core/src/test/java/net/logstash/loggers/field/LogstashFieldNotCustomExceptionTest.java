package net.logstash.loggers.field;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class LogstashFieldNotCustomExceptionTest {
	@Test
	public void testGetField() throws Throwable {
		LogstashFieldNotCustomException ex = new LogstashFieldNotCustomException(
				LogstashField.HOST);
		assertEquals(LogstashField.HOST, ex.getField());
	}

	@Test
	public void testToString() throws Throwable {
		LogstashFieldNotCustomException ex = new LogstashFieldNotCustomException(
				LogstashField.HOST);
		assertTrue(ex.toString().contains(LogstashField.HOST.getDefaultName()));
	}
}
