package net.logstash.loggers.logstash_jboss_logmanager;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.ParseException;
import java.util.logging.Formatter;

import net.logstash.loggers.LogstashFactory;
import net.logstash.loggers.LogstashFactoryParameters;
import net.logstash.loggers.field.CustomFields;
import net.logstash.loggers.field.LogstashFieldNotCustomException;
import net.logstash.loggers.field.mdc.MdcFields;

import org.jboss.logmanager.handlers.SizeRotatingFileHandler;

/**
 * A file handler which rotates the log at certain size and stores information
 * in Logstash format.
 * 
 * @author mpucholblasco
 * 
 */
public class LogstashSizeRotatingFileHandler extends SizeRotatingFileHandler
		implements LogstashFactoryParameters {
	private final LogstashFactory factory = new LogstashFactory();

	public LogstashSizeRotatingFileHandler() {
		super();
		super.setFormatter(new LogstashFormatter(factory));
	}

	public LogstashSizeRotatingFileHandler(final File file)
			throws FileNotFoundException {
		super(file);
		super.setFormatter(new LogstashFormatter(factory));
	}

	public LogstashSizeRotatingFileHandler(final File file, final boolean append)
			throws FileNotFoundException {
		super(file, append);
		super.setFormatter(new LogstashFormatter(factory));
	}

	public LogstashSizeRotatingFileHandler(final String fileName)
			throws FileNotFoundException {
		super(fileName);
		super.setFormatter(new LogstashFormatter(factory));
	}

	public LogstashSizeRotatingFileHandler(final String fileName,
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
		checkAccess(this);
		factory.setMdcFields(mdcFields);
	}

	@Override
	public void setTimeZone(String timeZone) {
		checkAccess(this);
		factory.setTimeZone(timeZone);
	}

	@Override
	public void setFormatter(final Formatter newFormatter)
			throws SecurityException {
		// Do not allow overriding out internal logstash formatter
	}

	public void setMaxBackupIndex(final String maxBackupIndex) {
		checkAccess(this);
		super.setMaxBackupIndex(Integer.parseInt(maxBackupIndex));
	}

	public void setRotateSize(final String rotateSize) {
		checkAccess(this);
		super.setRotateSize(Long.parseLong(rotateSize));
	}
}
