package net.logstash.loggers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.TimeZone;

import net.logstash.loggers.field.LogstashField;
import net.logstash.loggers.field.mdc.MdcEmptyFields;

import org.codehaus.jackson.JsonFactory;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;

public class LogstashFactoryTest {

	@Test
	public void testGenerationErrorWithMessage() throws Throwable {
		Exception ex = new Exception("My message");
		String currentError = LogstashGenerator
				.getErrorLogstashFromThrowable(ex);
		String expectedError = "{\"generation_error\":\"java.lang.Exception: My message\"}";
		JSONAssert.assertEquals(expectedError, currentError, false);
	}

	@Test
	public void testSetJsonFactory() throws Throwable {
		LogstashFactory logstashFactory = new LogstashFactory();
		JsonFactory jsonFactory = new JsonFactory();
		logstashFactory.setJsonFactory(jsonFactory);
		assertEquals(jsonFactory, logstashFactory.getJsonFactory());
	}

	@Test
	public void testCreateGenerator() throws Throwable {
		LogstashFactory logstashFactory = new LogstashFactory();
		LogstashGenerator generator = logstashFactory.createGenerator();
		assertEquals(logstashFactory, generator.getFactory());
	}

	@Test
	public void testCustomFields() throws Throwable {
		LogstashFactory logstashFactory = new LogstashFactory();
		logstashFactory.setCustomFields("");
		assertTrue(logstashFactory.getCustomFields().getExtraFieldsName()
				.isEmpty());
	}

	@Test
	public void testFieldNames() throws Throwable {
		LogstashFactory logstashFactory = new LogstashFactory();
		logstashFactory.setFieldNames(LogstashField.HOST.getDefaultName()
				+ "=myHost");
		assertEquals(
				"myHost",
				logstashFactory.getCustomFields().getFieldName(
						LogstashField.HOST));
	}

	@Test
	public void testMdcFields() throws Throwable {
		LogstashFactory logstashFactory = new LogstashFactory();
		logstashFactory.setMdcFields("");
		assertEquals(MdcEmptyFields.class, logstashFactory.getMdcFields()
				.getClass());
	}

	@Test
	public void testTimezoneString() throws Throwable {
		LogstashFactory logstashFactory = new LogstashFactory();
		logstashFactory.setTimeZone("UTC");
	}

	@Test
	public void testTimezone() throws Throwable {
		LogstashFactory logstashFactory = new LogstashFactory();
		logstashFactory.setTimeZone(TimeZone.getTimeZone("UTC"));
	}
}
