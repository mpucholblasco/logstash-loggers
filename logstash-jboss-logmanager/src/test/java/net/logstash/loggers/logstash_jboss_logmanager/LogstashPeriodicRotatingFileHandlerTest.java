package net.logstash.loggers.logstash_jboss_logmanager;

import org.junit.Test;

public class LogstashPeriodicRotatingFileHandlerTest {
	@Test
	public void testParametersTimeZone() throws Throwable {
		LogstashPeriodicRotatingFileHandler fileHandler = new LogstashPeriodicRotatingFileHandler();
		fileHandler.setTimeZone("");
		fileHandler.close();
	}

	@Test
	public void testParametersCustomFields() throws Throwable {
		LogstashPeriodicRotatingFileHandler fileHandler = new LogstashPeriodicRotatingFileHandler();
		fileHandler.setCustomFields("");
		fileHandler.close();
	}

	@Test
	public void testParametersMdcFields() throws Throwable {
		LogstashPeriodicRotatingFileHandler fileHandler = new LogstashPeriodicRotatingFileHandler();
		fileHandler.setMdcFields("");
		fileHandler.close();
	}
}
