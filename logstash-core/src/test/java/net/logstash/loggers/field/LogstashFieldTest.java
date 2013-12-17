package net.logstash.loggers.field;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class LogstashFieldTest {
	@Test
	public void testGetFieldNull() throws Throwable {
		LogstashField expectedField = null;
		LogstashField currentField = LogstashField.getFieldByDefaultName(null);
		assertEquals(expectedField, currentField);
	}

	@Test
	public void testGetFieldCorrect() throws Throwable {
		LogstashField expectedField = LogstashField.HOST;
		LogstashField currentField = LogstashField
				.getFieldByDefaultName(expectedField.getDefaultName());
		assertEquals(expectedField, currentField);
	}

	@Test
	public void testGetFieldIncorrect() throws Throwable {
		LogstashField expectedField = null;
		LogstashField currentField = LogstashField
				.getFieldByDefaultName("###nonExistentField###");
		assertEquals(expectedField, currentField);
	}
}
