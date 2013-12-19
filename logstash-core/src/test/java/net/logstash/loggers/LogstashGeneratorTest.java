package net.logstash.loggers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import net.logstash.loggers.LogstashGenerator;

import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;

import net.logstash.loggers.field.LogstashField;
import net.logstash.loggers.field.LogstashFieldNotCustomException;
import net.logstash.loggers.util.HostInfo;

public class LogstashGeneratorTest {
	private final static HostInfo HOST_INFO = new HostInfo();
	private final static TimeZone TIMEZONE = TimeZone.getTimeZone("UTC");

	@Test
	public void testEmptyFields() throws Throwable {
		LogstashFactory factory = new LogstashFactory();
		LogstashGenerator logstashGenerator = new LogstashGenerator(factory);
		String currentResult = logstashGenerator.toLogstashJSonString();
		String expectedResult = "{\"host\":\"" + HOST_INFO.getHostName()
				+ "\",\"@timestamp\":\"" + logstashGenerator.getTimestamp()
				+ "\",\"message\":\"-\"}";
		JSONAssert.assertEquals(expectedResult, currentResult, true);
	}

	@Test
	public void testHostField() throws Throwable {
		LogstashGenerator logstashGenerator = createHostInfoWithMyHostName();

		String currentResult = logstashGenerator.toLogstashJSonString();
		String expectedResult = "{\"host\":\"MyHostName\",\"@timestamp\":\""
				+ logstashGenerator.getTimestamp() + "\",\"message\":\"-\"}";
		JSONAssert.assertEquals(expectedResult, currentResult, true);
	}

	@Test
	public void testTimestampFromString() throws Throwable {
		LogstashGenerator logstashGenerator = createHostInfoWithMyHostName();

		logstashGenerator.setTimestamp("2013-11-17T12:18:12.345Z");

		String currentResult = logstashGenerator.toLogstashJSonString();
		String expectedResult = "{\"host\":\"MyHostName\",\"@timestamp\":\"2013-11-17T12:18:12.345Z\",\"message\":\"-\"}";
		JSONAssert.assertEquals(expectedResult, currentResult, true);
	}

	@Test
	public void testTimestampFromDate() throws Throwable {
		LogstashGenerator logstashGenerator = createHostInfoWithMyHostName();

		Calendar calendar = GregorianCalendar.getInstance(TIMEZONE);
		calendar.setTimeInMillis(0);
		calendar.set(2013, 10, 17, 12, 18, 00);

		logstashGenerator.setTimestamp(calendar.getTime());

		String currentResult = logstashGenerator.toLogstashJSonString();
		String expectedResult = "{\"host\":\"MyHostName\",\"@timestamp\":\"2013-11-17T12:18:00.000Z\",\"message\":\"-\"}";
		JSONAssert.assertEquals(expectedResult, currentResult, true);
	}

	@Test
	public void testTimestampFromCurrentDate() throws Throwable {
		LogstashGenerator logstashGenerator = createHostInfoWithMyHostName();
		logstashGenerator.setTimestamp();

		String currentResult = logstashGenerator.toLogstashJSonString();
		String expectedResult = "{\"host\":\"MyHostName\",\"@timestamp\":\""
				+ logstashGenerator.getTimestamp() + "\",\"message\":\"-\"}";
		JSONAssert.assertEquals(expectedResult, currentResult, true);
	}

	@Test
	public void testTimestampFromMillisecondsSince1970() throws Throwable {
		// 1005270400123L = 2001-11-09T01:46:40.123Z UTC
		LogstashGenerator logstashGenerator = createHostInfoWithMyHostName();
		logstashGenerator.setTimestamp(1005270400123L);

		String currentResult = logstashGenerator.toLogstashJSonString();
		String expectedResult = "{\"host\":\"MyHostName\",\"@timestamp\":\"2001-11-09T01:46:40.123Z\",\"message\":\"-\"}";
		JSONAssert.assertEquals(expectedResult, currentResult, true);
	}

	@Test
	public void testMessageEmpty() throws Throwable {
		LogstashGenerator logstashGenerator = createHostInfoWithMyHostNameAndTimestamp();
		logstashGenerator.setMessage("");

		String currentResult = logstashGenerator.toLogstashJSonString();
		String expectedResult = "{\"host\":\"MyHostName\",\"@timestamp\":\"2001-11-09T01:46:40.123Z\"}";
		JSONAssert.assertEquals(expectedResult, currentResult, false);
	}

	@Test
	public void testMessageSimple() throws Throwable {
		LogstashGenerator logstashGenerator = createHostInfoWithMyHostNameAndTimestamp();
		logstashGenerator.setMessage("My message1");

		String currentResult = logstashGenerator.toLogstashJSonString();
		String expectedResult = "{\"host\":\"MyHostName\",\"@timestamp\":\"2001-11-09T01:46:40.123Z\",\"message\":\"My message1\"}";
		JSONAssert.assertEquals(expectedResult, currentResult, false);
	}

	@Test
	public void testMessageStrangeCharacters() throws Throwable {
		LogstashGenerator logstashGenerator = createHostInfoWithMyHostNameAndTimestamp();
		logstashGenerator.setMessage("My message: áéíóúÀÈÌÒÙ€");

		String currentResult = logstashGenerator.toLogstashJSonString();
		String expectedResult = "{\"host\":\"MyHostName\",\"@timestamp\":\"2001-11-09T01:46:40.123Z\",\"message\":\"My message: áéíóúÀÈÌÒÙ€\"}";
		JSONAssert.assertEquals(expectedResult, currentResult, true);
	}

	@Test
	public void testMessageWithQuotes() throws Throwable {
		LogstashGenerator logstashGenerator = createHostInfoWithMyHostNameAndTimestamp();
		logstashGenerator
				.setMessage("My message with quotes: \"double quotes\", 'single quotes'");

		String currentResult = logstashGenerator.toLogstashJSonString();
		String expectedResult = "{\"host\":\"MyHostName\",\"@timestamp\":\"2001-11-09T01:46:40.123Z\",\"message\":\"My message with quotes: \\\"double quotes\\\", 'single quotes'\"}";
		JSONAssert.assertEquals(expectedResult, currentResult, false);
	}

	@Test
	public void testMessageWithCarriageReturn() throws Throwable {
		LogstashGenerator logstashGenerator = createHostInfoWithMyHostNameAndTimestamp();
		logstashGenerator.setMessage("My multiline message.\nLine 2\nLine 3");

		String currentResult = logstashGenerator.toLogstashJSonString();
		String expectedResult = "{\"host\":\"MyHostName\",\"@timestamp\":\"2001-11-09T01:46:40.123Z\",\"message\":\"My multiline message.\\nLine 2\\nLine 3\"}";
		JSONAssert.assertEquals(expectedResult, currentResult, true);
	}

	@Test
	public void testThread() throws Throwable {
		LogstashGenerator logstashGenerator = createHostInfoWithMyHostNameAndTimestamp();
		logstashGenerator.setThread("MyThread");

		String currentResult = logstashGenerator.toLogstashJSonString();
		String expectedResult = "{\"host\":\"MyHostName\",\"@timestamp\":\"2001-11-09T01:46:40.123Z\",\"thread\": \"MyThread\",\"message\":\"-\"}";
		JSONAssert.assertEquals(expectedResult, currentResult, true);
	}

	@Test
	public void testLevel() throws Throwable {
		LogstashGenerator logstashGenerator = createHostInfoWithMyHostNameAndTimestamp();
		logstashGenerator.setLevel("ERROR");

		String currentResult = logstashGenerator.toLogstashJSonString();
		String expectedResult = "{\"host\":\"MyHostName\",\"@timestamp\":\"2001-11-09T01:46:40.123Z\",\"level\": \"ERROR\",\"message\":\"-\"}";
		JSONAssert.assertEquals(expectedResult, currentResult, true);
	}

	@Test
	public void testClassname() throws Throwable {
		LogstashGenerator logstashGenerator = createHostInfoWithMyHostNameAndTimestamp();
		logstashGenerator
				.setClassName("net.logstash.loggers.LogstashGeneratorTest");

		String currentResult = logstashGenerator.toLogstashJSonString();
		String expectedResult = "{\"host\":\"MyHostName\",\"@timestamp\":\"2001-11-09T01:46:40.123Z\",\"classname\": \"net.logstash.loggers.LogstashGeneratorTest\",\"message\":\"-\"}";
		JSONAssert.assertEquals(expectedResult, currentResult, true);
	}

	@Test
	public void testThrowableEmpty() throws Throwable {
		LogstashGenerator logstashGenerator = createHostInfoWithMyHostNameAndTimestamp();
		Exception ex = new Exception();
		logstashGenerator.setThrowable(ex);

		String currentResult = logstashGenerator.toLogstashJSonString();
		String expectedResult = "{\"host\":\"MyHostName\",\"@timestamp\":\"2001-11-09T01:46:40.123Z\",\"exception_class\": \"java.lang.Exception\",\"message\":\"-\"}";

		// exception_stacktrace ignored by setting assertion last argument to
		// false
		JSONAssert.assertEquals(expectedResult, currentResult, false);
	}

	@Test
	public void testThrowableWithMessage() throws Throwable {
		LogstashGenerator logstashGenerator = createHostInfoWithMyHostNameAndTimestamp();
		Exception ex = new Exception("Exception message");
		logstashGenerator.setThrowable(ex);

		String currentResult = logstashGenerator.toLogstashJSonString();
		String expectedResult = "{\"host\":\"MyHostName\",\"@timestamp\":\"2001-11-09T01:46:40.123Z\",\"exception_class\": \"java.lang.Exception\",\"exception_message\":\"Exception message\",\"message\":\"-\"}";

		// exception_stacktrace ignored by setting assertion last argument to
		// false
		JSONAssert.assertEquals(expectedResult, currentResult, false);
	}

	@Test
	public void testThrowableWithInnerThrowable() throws Throwable {
		LogstashGenerator logstashGenerator = createHostInfoWithMyHostNameAndTimestamp();
		InnerException innerException = new InnerException();
		Exception ex = new Exception("Exception message", innerException);
		logstashGenerator.setThrowable(ex);

		String currentResult = logstashGenerator.toLogstashJSonString();
		String expectedResult = "{\"host\":\"MyHostName\",\"@timestamp\":\"2001-11-09T01:46:40.123Z\",\"exception_class\": \"java.lang.Exception\",\"exception_message\":\"Exception message\",\"message\":\"-\"}";

		// exception_stacktrace ignored by setting assertion last argument to
		// false
		JSONAssert.assertEquals(expectedResult, currentResult, false);

		// Ensure inner exception information is present
		assertTrue(currentResult.contains("Caused by: "
				+ InnerException.class.getName()));
	}

	@Test
	public void testSeveralLogEntries() throws Throwable {
		LogstashGenerator log1 = createHostInfoWithMyHostNameAndTimestamp();
		log1.setMessage("My message1");

		LogstashGenerator log2 = createHostInfoWithMyHostNameAndTimestamp();
		log2.setMessage("My message2");

		String currentResult1 = log1.toLogstashJSonString();
		String expectedResult1 = "{\"host\":\"MyHostName\",\"@timestamp\":\"2001-11-09T01:46:40.123Z\",\"message\":\"My message1\"}";
		JSONAssert.assertEquals(expectedResult1, currentResult1, true);

		String currentResult2 = log2.toLogstashJSonString();
		String expectedResult2 = "{\"host\":\"MyHostName\",\"@timestamp\":\"2001-11-09T01:46:40.123Z\",\"message\":\"My message2\"}";
		JSONAssert.assertEquals(expectedResult2, currentResult2, true);
	}

	@Test
	public void testGenerationErrorNull() throws Throwable {
		String currentError = LogstashGenerator
				.getErrorLogstashFromThrowable(null);
		String expectedError = "";
		assertEquals(currentError, expectedError);
	}

	@Test
	public void testGenerationErrorWithoutMessage() throws Throwable {
		Exception ex = new Exception();
		String currentError = LogstashGenerator
				.getErrorLogstashFromThrowable(ex);
		String expectedError = "{\"generation_error\":\"java.lang.Exception\"}";
		JSONAssert.assertEquals(expectedError, currentError, false);
	}

	@Test
	public void testGenerationErrorWithMessage() throws Throwable {
		Exception ex = new Exception("My message");
		String currentError = LogstashGenerator
				.getErrorLogstashFromThrowable(ex);
		String expectedError = "{\"generation_error\":\"java.lang.Exception: My message\"}";
		JSONAssert.assertEquals(expectedError, currentError, false);
	}

	@Test
	public void testGenerationErrorWithMessageAndInnerException()
			throws Throwable {
		InnerException innerEx = new InnerException();
		Exception ex = new Exception("My message", innerEx);
		String currentError = LogstashGenerator
				.getErrorLogstashFromThrowable(ex);
		String expectedError = "{\"generation_error\":\"java.lang.Exception: My message\"}";
		JSONAssert.assertEquals(expectedError, currentError, false);
	}

	@Test
	public void testExtraFields() throws Throwable {
		LogstashGenerator logstashGenerator = createHostInfoWithMyHostNameAndTimestamp();
		LogstashFactory factory = logstashGenerator.getFactory();
		factory.setCustomFields("extraFieldName1=extraFieldValue1;extraFieldName2=extraFieldValue2");

		String currentResult = logstashGenerator.toLogstashJSonString();
		String expectedResult = "{\"host\":\"MyHostName\",\"@timestamp\":\"2001-11-09T01:46:40.123Z\",\"extraFieldName1\": \"extraFieldValue1\",\"extraFieldName2\":\"extraFieldValue2\",\"message\":\"-\"}";

		JSONAssert.assertEquals(expectedResult, currentResult, true);
	}

	@Test
	public void testNDC() throws Throwable {
		LogstashGenerator logstashGenerator = createHostInfoWithMyHostNameAndTimestamp();
		logstashGenerator.setNdc("myNdcValue");

		String currentResult = logstashGenerator.toLogstashJSonString();
		String expectedResult = "{\"host\":\"MyHostName\",\"@timestamp\":\"2001-11-09T01:46:40.123Z\",\"ndc\": \"myNdcValue\",\"message\":\"-\"}";

		JSONAssert.assertEquals(expectedResult, currentResult, true);
	}

	@Test
	public void testMDC() throws Throwable {
		LogstashGenerator logstashGenerator = createHostInfoWithMyHostNameAndTimestamp();
		logstashGenerator.getFactory().setMdcFields("ALL_FIELDS");

		Map<String, String> mdc = new HashMap<String, String>();
		mdc.put("mdcFieldName1", "mdcFieldValue1");
		mdc.put("mdcFieldName2", "mdcFieldValue2");
		mdc.put("mdcFieldName3", "mdcFieldValue3");
		
		logstashGenerator.setMdc(mdc);

		String currentResult = logstashGenerator.toLogstashJSonString();
		String expectedResult = "{\"host\":\"MyHostName\",\"@timestamp\":\"2001-11-09T01:46:40.123Z\",\"mdc\":{\"mdcFieldName1\":\"mdcFieldValue1\",\"mdcFieldName2\":\"mdcFieldValue2\",\"mdcFieldName3\":\"mdcFieldValue3\"},\"message\":\"-\"}";

		JSONAssert.assertEquals(expectedResult, currentResult, true);
	}

	@Test
	public void testMDCNull() throws Throwable {
		LogstashGenerator logstashGenerator = createHostInfoWithMyHostNameAndTimestamp();
		logstashGenerator.getFactory().setMdcFields("ALL_FIELDS");

		logstashGenerator.setMdc(null);

		String currentResult = logstashGenerator.toLogstashJSonString();
		String expectedResult = "{\"host\":\"MyHostName\",\"@timestamp\":\"2001-11-09T01:46:40.123Z\",\"message\":\"-\"}";

		JSONAssert.assertEquals(expectedResult, currentResult, true);
	}

	@Test
	public void testMDCEmpty() throws Throwable {
		LogstashGenerator logstashGenerator = createHostInfoWithMyHostNameAndTimestamp();
		logstashGenerator.getFactory().setMdcFields("ALL_FIELDS");

		Map<String, String> emptyMdc = new HashMap<String, String>();
		logstashGenerator.setMdc(emptyMdc);

		String currentResult = logstashGenerator.toLogstashJSonString();
		String expectedResult = "{\"host\":\"MyHostName\",\"@timestamp\":\"2001-11-09T01:46:40.123Z\",\"message\":\"-\"}";

		JSONAssert.assertEquals(expectedResult, currentResult, true);
	}

	private final LogstashGenerator createHostInfoWithMyHostName()
			throws LogstashFieldNotCustomException {
		LogstashFactory factory = new LogstashFactory();
		factory.getCustomFields().setCustomField(
				LogstashField.HOST.getDefaultName(), "MyHostName");

		LogstashGenerator logstashGenerator = new LogstashGenerator(factory);
		return logstashGenerator;
	}

	private final LogstashGenerator createHostInfoWithMyHostNameAndTimestamp()
			throws LogstashFieldNotCustomException {
		LogstashGenerator logstashGenerator = createHostInfoWithMyHostName();
		logstashGenerator.setTimestamp(1005270400123L);
		return logstashGenerator;
	}

	private static class InnerException extends Exception {
		private static final long serialVersionUID = 5511858950346193594L;

		public InnerException() {
			super();
		}
	}
}
