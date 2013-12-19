package net.logstash.loggers.logstash_jboss_logmanager;

import java.io.IOException;

import net.logstash.loggers.LogstashFactory;
import net.logstash.loggers.LogstashGenerator;

import org.jboss.logmanager.ExtFormatter;
import org.jboss.logmanager.ExtLogRecord;

/**
 * Formatter to format {@link ExtLogRecord} into a Logstash string.
 * 
 * @author mpucholblasco
 * 
 */
public class LogstashFormatter extends ExtFormatter {

	private final LogstashFactory factory;

	public LogstashFormatter(LogstashFactory factory) {
		this.factory = factory;
	}

	@Override
	public String format(ExtLogRecord extLogRecord) {
		LogstashGenerator logstashGenerator = factory.createGenerator();
		logstashGenerator.setLevel(extLogRecord.getLevel().toString());
		logstashGenerator.setMessage(extLogRecord.getFormattedMessage());
		logstashGenerator.setThrowable(extLogRecord.getThrown());
		logstashGenerator.setTimestamp(extLogRecord.getMillis());
		logstashGenerator.setClassName(extLogRecord.getLoggerClassName());
		logstashGenerator.setThread(extLogRecord.getThreadName());
		logstashGenerator.setNdc(extLogRecord.getNdc());
		logstashGenerator.setMdc(extLogRecord.getMdcCopy());
		try {
			return logstashGenerator.toLogstashJSonString();
		} catch (IOException e) {
			return LogstashGenerator.getErrorLogstashFromThrowable(e);
		}
	}
}
