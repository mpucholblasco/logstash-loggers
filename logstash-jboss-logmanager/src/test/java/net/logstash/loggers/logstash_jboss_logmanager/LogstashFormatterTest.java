package net.logstash.loggers.logstash_jboss_logmanager;

import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.logging.Level;

import net.logstash.loggers.LogstashFactory;
import net.logstash.loggers.field.LogstashField;
import net.logstash.loggers.field.LogstashFieldNotCustomException;
import net.logstash.loggers.util.HostInfo;

import org.jboss.logmanager.ExtLogRecord;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;

public class LogstashFormatterTest {
	private final static HostInfo HOST_INFO = new HostInfo();

	@Test
	public void testExtLogWithBasicElements() throws Throwable {
		LogstashFactory factory = new LogstashFactory();
		LogstashFormatter formatter = new LogstashFormatter(factory);
		ExtLogRecord logRecord = new ExtLogRecord(Level.INFO, "My message",
				"net.logstash.loggers.logstash_jboss_logmanager.test");
		logRecord.setMillis(1005270400123L);
		String currentResult = formatter.format(logRecord);
		String expectedResult = "{\"host\":\""
				+ HOST_INFO.getHostName()
				+ "\",\"level\": \"INFO\",\"@timestamp\":\"2001-11-09T01:46:40.123Z\",\"message\":\"My message\",\"thread\": \"main\",\"classname\": \"net.logstash.loggers.logstash_jboss_logmanager.test\"}";
		JSONAssert.assertEquals(expectedResult, currentResult, true);
	}

	@Test
	public void testHostField() throws Throwable {
		LogstashFactory factory = new LogstashFactory();
		factory.getCustomFields().setCustomField(
				LogstashField.HOST.getDefaultName(), "myHost");
		LogstashFormatter formatter = new LogstashFormatter(factory);
		ExtLogRecord logRecord = new ExtLogRecord(Level.INFO, "My message",
				"net.logstash.loggers.logstash_jboss_logmanager.test");
		logRecord.setMillis(1005270400123L);
		String currentResult = formatter.format(logRecord);
		String expectedResult = "{\"host\":\"myHost\",\"level\": \"INFO\",\"@timestamp\":\"2001-11-09T01:46:40.123Z\",\"message\":\"My message\",\"thread\": \"main\",\"classname\": \"net.logstash.loggers.logstash_jboss_logmanager.test\"}";
		JSONAssert.assertEquals(expectedResult, currentResult, true);
	}

	@Test
	public void testMessageStrangeCharacters() throws Throwable {
		ExtLogRecord logRecord = this.createBasicLogRecord();
		logRecord.setMessage("My message: áéíóúÀÈÌÒÙ€");
		LogstashFormatter formatter = new LogstashFormatter(
				createBasicLogstashFactory());
		String currentResult = formatter.format(logRecord);
		String expectedResult = "{\"host\":\"myHost\",\"level\": \"INFO\",\"@timestamp\":\"2001-11-09T01:46:40.123Z\",\"message\":\"My message: áéíóúÀÈÌÒÙ€\",\"thread\": \"main\",\"classname\": \"net.logstash.loggers.logstash_jboss_logmanager.test\"}";
		JSONAssert.assertEquals(expectedResult, currentResult, true);
	}

	@Test
	public void testMessageWithQuotes() throws Throwable {
		ExtLogRecord logRecord = this.createBasicLogRecord();
		logRecord
				.setMessage("My message with quotes: \"double quotes\", 'single quotes'");
		LogstashFormatter formatter = new LogstashFormatter(
				createBasicLogstashFactory());
		String currentResult = formatter.format(logRecord);
		String expectedResult = "{\"host\":\"myHost\",\"level\": \"INFO\",\"@timestamp\":\"2001-11-09T01:46:40.123Z\",\"message\":\"My message with quotes: \\\"double quotes\\\", 'single quotes'\",\"thread\": \"main\",\"classname\": \"net.logstash.loggers.logstash_jboss_logmanager.test\"}";
		JSONAssert.assertEquals(expectedResult, currentResult, true);
	}

	@Test
	public void testMessageWithCarriageReturn() throws Throwable {
		ExtLogRecord logRecord = this.createBasicLogRecord();
		logRecord.setMessage("My multiline message.\nLine 2\nLine 3");
		LogstashFormatter formatter = new LogstashFormatter(
				createBasicLogstashFactory());
		String currentResult = formatter.format(logRecord);
		String expectedResult = "{\"host\":\"myHost\",\"level\": \"INFO\",\"@timestamp\":\"2001-11-09T01:46:40.123Z\",\"message\":\"My multiline message.\\nLine 2\\nLine 3\",\"thread\": \"main\",\"classname\": \"net.logstash.loggers.logstash_jboss_logmanager.test\"}";
		JSONAssert.assertEquals(expectedResult, currentResult, true);
	}

	@Test
	public void testThread() throws Throwable {
		ExtLogRecord logRecord = this.createBasicLogRecord();
		logRecord.setThreadName("MyThread");
		LogstashFormatter formatter = new LogstashFormatter(
				createBasicLogstashFactory());
		String currentResult = formatter.format(logRecord);
		String expectedResult = "{\"host\":\"myHost\",\"level\": \"INFO\",\"@timestamp\":\"2001-11-09T01:46:40.123Z\",\"message\":\"My message\",\"thread\": \"MyThread\",\"classname\": \"net.logstash.loggers.logstash_jboss_logmanager.test\"}";
		JSONAssert.assertEquals(expectedResult, currentResult, true);
	}

	@Test
	public void testThrowableEmpty() throws Throwable {
		ExtLogRecord logRecord = this.createBasicLogRecord();
		Exception ex = new Exception();
		logRecord.setThrown(ex);
		LogstashFormatter formatter = new LogstashFormatter(
				createBasicLogstashFactory());
		String currentResult = formatter.format(logRecord);
		String expectedResult = "{\"host\":\"myHost\",\"level\": \"INFO\",\"@timestamp\":\"2001-11-09T01:46:40.123Z\",\"message\":\"My message\",\"thread\": \"main\",\"classname\": \"net.logstash.loggers.logstash_jboss_logmanager.test\",\"exception_class\": \"java.lang.Exception\"}";
		JSONAssert.assertEquals(expectedResult, currentResult, false);
	}

	@Test
	public void testThrowableWithMessage() throws Throwable {
		ExtLogRecord logRecord = this.createBasicLogRecord();
		Exception ex = new Exception("Exception message");
		logRecord.setThrown(ex);
		LogstashFormatter formatter = new LogstashFormatter(
				createBasicLogstashFactory());
		String currentResult = formatter.format(logRecord);
		String expectedResult = "{\"host\":\"myHost\",\"level\": \"INFO\",\"@timestamp\":\"2001-11-09T01:46:40.123Z\",\"message\":\"My message\",\"thread\": \"main\",\"classname\": \"net.logstash.loggers.logstash_jboss_logmanager.test\",\"exception_class\": \"java.lang.Exception\",\"exception_message\":\"Exception message\"}";
		JSONAssert.assertEquals(expectedResult, currentResult, false);
	}

	@Test
	public void testThrowableWithInnerThrowable() throws Throwable {
		ExtLogRecord logRecord = this.createBasicLogRecord();
		InnerException innerException = new InnerException();
		Exception ex = new Exception("Exception message", innerException);
		logRecord.setThrown(ex);
		LogstashFormatter formatter = new LogstashFormatter(
				createBasicLogstashFactory());
		String currentResult = formatter.format(logRecord);
		String expectedResult = "{\"host\":\"myHost\",\"level\": \"INFO\",\"@timestamp\":\"2001-11-09T01:46:40.123Z\",\"message\":\"My message\",\"thread\": \"main\",\"classname\": \"net.logstash.loggers.logstash_jboss_logmanager.test\",\"exception_class\": \"java.lang.Exception\",\"exception_message\":\"Exception message\"}";
		JSONAssert.assertEquals(expectedResult, currentResult, false);

		// Ensure inner exception information is present
		assertTrue(currentResult.contains("Caused by: "
				+ InnerException.class.getName()));
	}

	@Test
	public void testSeveralLogEntries() throws Throwable {
		LogstashFormatter formatter = new LogstashFormatter(
				createBasicLogstashFactory());

		ExtLogRecord logRecord1 = this.createBasicLogRecord();
		logRecord1.setMessage("My message1");
		String currentResult1 = formatter.format(logRecord1);
		String expectedResult1 = "{\"host\":\"myHost\",\"level\": \"INFO\",\"@timestamp\":\"2001-11-09T01:46:40.123Z\",\"message\":\"My message1\",\"thread\": \"main\",\"classname\": \"net.logstash.loggers.logstash_jboss_logmanager.test\"}";
		JSONAssert.assertEquals(expectedResult1, currentResult1, true);

		ExtLogRecord logRecord2 = this.createBasicLogRecord();
		logRecord2.setMessage("My message2");
		String currentResult2 = formatter.format(logRecord2);
		String expectedResult2 = "{\"host\":\"myHost\",\"level\": \"INFO\",\"@timestamp\":\"2001-11-09T01:46:40.123Z\",\"message\":\"My message2\",\"thread\": \"main\",\"classname\": \"net.logstash.loggers.logstash_jboss_logmanager.test\"}";
		JSONAssert.assertEquals(expectedResult2, currentResult2, true);
	}

	@Test
	public void testExtraFields() throws Throwable {
		LogstashFactory factory = createBasicLogstashFactory();
		factory.setCustomFields("extraFieldName1=extraFieldValue1;extraFieldName2=extraFieldValue2");

		LogstashFormatter formatter = new LogstashFormatter(factory);
		ExtLogRecord logRecord = this.createBasicLogRecord();
		String currentResult = formatter.format(logRecord);
		String expectedResult = "{\"host\":\"myHost\",\"level\": \"INFO\",\"@timestamp\":\"2001-11-09T01:46:40.123Z\",\"message\":\"My message\",\"thread\": \"main\",\"classname\": \"net.logstash.loggers.logstash_jboss_logmanager.test\",\"extraFieldName1\": \"extraFieldValue1\",\"extraFieldName2\":\"extraFieldValue2\"}";
		JSONAssert.assertEquals(expectedResult, currentResult, true);
	}

	@Test
	public void testCustomNames() throws Throwable {
		LogstashFactory factory = createBasicLogstashFactory();
		factory.setFieldNames(LogstashField.TIMESTAMP.getDefaultName()
				+ "=myTimestamp;" + LogstashField.HOST.getDefaultName()
				+ "=myHost");

		LogstashFormatter formatter = new LogstashFormatter(factory);
		ExtLogRecord logRecord = this.createBasicLogRecord();
		String currentResult = formatter.format(logRecord);
		String expectedResult = "{\"myHost\":\"myHost\",\"level\": \"INFO\",\"myTimestamp\":\"2001-11-09T01:46:40.123Z\",\"message\":\"My message\",\"thread\": \"main\",\"classname\": \"net.logstash.loggers.logstash_jboss_logmanager.test\"}";
		JSONAssert.assertEquals(expectedResult, currentResult, true);
	}

	@Test
	public void testNdc() throws Throwable {
		LogstashFactory factory = createBasicLogstashFactory();
		LogstashFormatter formatter = new LogstashFormatter(factory);
		ExtLogRecord logRecord = this.createBasicLogRecord();
		logRecord.setNdc("ndcValue");

		String currentResult = formatter.format(logRecord);
		String expectedResult = "{\"host\":\"myHost\",\"level\": \"INFO\",\"@timestamp\":\"2001-11-09T01:46:40.123Z\",\"message\":\"My message\",\"thread\": \"main\",\"classname\": \"net.logstash.loggers.logstash_jboss_logmanager.test\",\"ndc\": \"ndcValue\"}";
		JSONAssert.assertEquals(expectedResult, currentResult, true);
	}

	@Test
	public void testMdcWithoutAnySpecifiedField() throws Throwable {
		LogstashFactory factory = createBasicLogstashFactory();
		LogstashFormatter formatter = new LogstashFormatter(factory);
		ExtLogRecord logRecord = createMdcLogRecord();

		String currentResult = formatter.format(logRecord);
		String expectedResult = "{\"host\":\"myHost\",\"level\": \"INFO\",\"@timestamp\":\"2001-11-09T01:46:40.123Z\",\"message\":\"My message\",\"thread\": \"main\",\"classname\": \"net.logstash.loggers.logstash_jboss_logmanager.test\"}";
		JSONAssert.assertEquals(expectedResult, currentResult, true);
	}

	@Test
	public void testMdcWithNoExistentField() throws Throwable {
		LogstashFactory factory = createBasicLogstashFactory();
		factory.setMdcFields("NonExistentField");
		LogstashFormatter formatter = new LogstashFormatter(factory);
		ExtLogRecord logRecord = createMdcLogRecord();

		String currentResult = formatter.format(logRecord);
		String expectedResult = "{\"host\":\"myHost\",\"level\": \"INFO\",\"@timestamp\":\"2001-11-09T01:46:40.123Z\",\"message\":\"My message\",\"thread\": \"main\",\"classname\": \"net.logstash.loggers.logstash_jboss_logmanager.test\"}";
		JSONAssert.assertEquals(expectedResult, currentResult, true);
	}

	@Test
	public void testMdcWithSingleSpecifiedField() throws Throwable {
		LogstashFactory factory = createBasicLogstashFactory();
		factory.setMdcFields("mdcKey1");
		LogstashFormatter formatter = new LogstashFormatter(factory);
		ExtLogRecord logRecord = createMdcLogRecord();

		String currentResult = formatter.format(logRecord);
		String expectedResult = "{\"host\":\"myHost\",\"level\": \"INFO\",\"@timestamp\":\"2001-11-09T01:46:40.123Z\",\"message\":\"My message\",\"thread\": \"main\",\"classname\": \"net.logstash.loggers.logstash_jboss_logmanager.test\",\"mdc\":{\"mdcKey1\":\"mdcValue1\"}}";
		JSONAssert.assertEquals(expectedResult, currentResult, true);
	}

	@Test
	public void testMdcWithSeveralSpecifiedField() throws Throwable {
		LogstashFactory factory = createBasicLogstashFactory();
		factory.setMdcFields("mdcKey1;mdcKey3");
		LogstashFormatter formatter = new LogstashFormatter(factory);
		ExtLogRecord logRecord = createMdcLogRecord();

		String currentResult = formatter.format(logRecord);
		String expectedResult = "{\"host\":\"myHost\",\"level\": \"INFO\",\"@timestamp\":\"2001-11-09T01:46:40.123Z\",\"message\":\"My message\",\"thread\": \"main\",\"classname\": \"net.logstash.loggers.logstash_jboss_logmanager.test\",\"mdc\":{\"mdcKey1\":\"mdcValue1\",\"mdcKey3\":\"mdcValue3\"}}";
		JSONAssert.assertEquals(expectedResult, currentResult, true);
	}

	@Test
	public void testMdcWithAllFields() throws Throwable {
		LogstashFactory factory = createBasicLogstashFactory();
		factory.setMdcFields("ALL_FIELDS");
		LogstashFormatter formatter = new LogstashFormatter(factory);
		ExtLogRecord logRecord = createMdcLogRecord();

		String currentResult = formatter.format(logRecord);
		String expectedResult = "{\"host\":\"myHost\",\"level\": \"INFO\",\"@timestamp\":\"2001-11-09T01:46:40.123Z\",\"message\":\"My message\",\"thread\": \"main\",\"classname\": \"net.logstash.loggers.logstash_jboss_logmanager.test\",\"mdc\":{\"mdcKey1\":\"mdcValue1\",\"mdcKey2\":\"mdcValue2\",\"mdcKey3\":\"mdcValue3\"}}";
		JSONAssert.assertEquals(expectedResult, currentResult, true);
	}

	private final ExtLogRecord createBasicLogRecord() {
		ExtLogRecord logRecord = new ExtLogRecord(Level.INFO, "My message",
				"net.logstash.loggers.logstash_jboss_logmanager.test");
		logRecord.setMillis(1005270400123L);
		return logRecord;
	}

	private final ExtLogRecord createMdcLogRecord() {
		ExtLogRecord logRecord = createBasicLogRecord();
		logRecord.setMdc(new HashMap<String, String>() {
			private static final long serialVersionUID = 149131063967919600L;

			{
				put("mdcKey1", "mdcValue1");
				put("mdcKey2", "mdcValue2");
				put("mdcKey3", "mdcValue3");
			}
		});

		return logRecord;
	}

	private final LogstashFactory createBasicLogstashFactory()
			throws LogstashFieldNotCustomException {
		LogstashFactory factory = new LogstashFactory();
		factory.getCustomFields().setCustomField(
				LogstashField.HOST.getDefaultName(), "myHost");
		return factory;
	}

	private static class InnerException extends Exception {
		private static final long serialVersionUID = 5511858950346193594L;

		public InnerException() {
			super();
		}
	}
}
