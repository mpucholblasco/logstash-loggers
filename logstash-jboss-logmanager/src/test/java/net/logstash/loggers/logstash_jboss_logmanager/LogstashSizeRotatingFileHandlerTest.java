package net.logstash.loggers.logstash_jboss_logmanager;

import org.junit.Test;

public class LogstashSizeRotatingFileHandlerTest {
	@Test
	public void testParametersTimeZone() throws Throwable {
		LogstashSizeRotatingFileHandler fileHandler = new LogstashSizeRotatingFileHandler();
		fileHandler.setTimeZone("");
		fileHandler.close();
	}

	@Test
	public void testParametersCustomFields() throws Throwable {
		LogstashSizeRotatingFileHandler fileHandler = new LogstashSizeRotatingFileHandler();
		fileHandler.setCustomFields("");
		fileHandler.close();
	}

	@Test
	public void testParametersMdcFields() throws Throwable {
		LogstashSizeRotatingFileHandler fileHandler = new LogstashSizeRotatingFileHandler();
		fileHandler.setMdcFields("");
		fileHandler.close();
	}
}
