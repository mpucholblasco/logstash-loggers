package net.logstash.loggers.logstash_jboss_logmanager;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.ParseException;
import java.util.TimeZone;
import java.util.logging.Formatter;

import net.logstash.loggers.LogstashFactory;
import net.logstash.loggers.LogstashFactoryParameters;
import net.logstash.loggers.field.CustomFields;
import net.logstash.loggers.field.LogstashFieldNotCustomException;
import net.logstash.loggers.field.mdc.MdcFields;

import org.jboss.logmanager.handlers.PeriodicRotatingFileHandler;

/**
 * A file handler which rotates the log at a preset time interval and stores
 * information in Logstash format. The interval is determined by the content of
 * the suffix string which is passed in to setSuffix(String).
 * 
 * @author mpucholblasco
 * 
 */
public class LogstashPeriodicRotatingFileHandler extends
		PeriodicRotatingFileHandler implements LogstashFactoryParameters {
	private final LogstashFactory factory = new LogstashFactory();

	public LogstashPeriodicRotatingFileHandler() {
		super();
		super.setFormatter(new LogstashFormatter(factory));
	}

	public LogstashPeriodicRotatingFileHandler(final File file,
			final String suffix) throws FileNotFoundException {
		super(file, suffix);
		super.setFormatter(new LogstashFormatter(factory));
	}

	public LogstashPeriodicRotatingFileHandler(final File file,
			final String suffix, final boolean append)
			throws FileNotFoundException {
		super(file, suffix, append);
		super.setFormatter(new LogstashFormatter(factory));
	}

	public LogstashPeriodicRotatingFileHandler(final String fileName)
			throws FileNotFoundException {
		super(fileName);
		super.setFormatter(new LogstashFormatter(factory));
	}

	public LogstashPeriodicRotatingFileHandler(final String fileName,
			final boolean append) throws FileNotFoundException {
		super(fileName, append);
		super.setFormatter(new LogstashFormatter(factory));
	}

	@Override
	public void setCustomFields(final String extraFields)
			throws ParseException, LogstashFieldNotCustomException {
		checkAccess(this);
		factory.setCustomFields(extraFields);
	}

	@Override
	public void setFieldNames(String fieldNames) throws ParseException {
		checkAccess(this);
		factory.setFieldNames(fieldNames);
	}

	@Override
	public CustomFields getCustomFields() {
		return factory.getCustomFields();
	}

	@Override
	public MdcFields getMdcFields() {
		return factory.getMdcFields();
	}

	@Override
	public void setMdcFields(String mdcFields) throws ParseException,
			LogstashFieldNotCustomException {
		factory.setMdcFields(mdcFields);
	}

	@Override
	public void setTimeZone(final String timeZone) {
		factory.setTimeZone(timeZone);
	}

	@Override
	public void setTimeZone(final TimeZone timeZone) {
		factory.setTimeZone(timeZone);
	}

	@Override
	public void setFormatter(final Formatter newFormatter)
			throws SecurityException {
		// Do not allow overriding our internal logstash formatter
	}
}
