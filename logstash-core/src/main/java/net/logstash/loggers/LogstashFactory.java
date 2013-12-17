package net.logstash.loggers;

import java.text.ParseException;
import java.util.TimeZone;

import net.logstash.loggers.field.CustomFields;
import net.logstash.loggers.field.LogstashFieldNotCustomException;
import net.logstash.loggers.field.mdc.MdcEmptyFields;
import net.logstash.loggers.field.mdc.MdcFields;
import net.logstash.loggers.field.mdc.MdcFieldsFactory;
import net.logstash.loggers.util.ISO8601;

import org.codehaus.jackson.JsonFactory;

/**
 * Logstash factory to create LogstashGenerator objects and keep centralized
 * information on it.
 * 
 * @author mpucholblasco
 * 
 */
public class LogstashFactory implements LogstashFactoryParameters {

	private JsonFactory jsonFactory = new JsonFactory();
	private MdcFields mdcFields = new MdcEmptyFields();
	private CustomFields customFields = new CustomFields();
	private ISO8601 iso8601 = new ISO8601();

	public LogstashFactory setJsonFactory(final JsonFactory jsonFactory) {
		this.jsonFactory = jsonFactory;
		return this;
	}

	public JsonFactory getJsonFactory() {
		return this.jsonFactory;
	}

	public ISO8601 getISO8601() {
		return iso8601;
	}

	public LogstashGenerator createGenerator() {
		return new LogstashGenerator(this);
	}

	// Setters & getters associated implemented for LogstashFactoryParameters
	@Override
	public CustomFields getCustomFields() {
		return customFields;
	}

	@Override
	public void setCustomFields(final String customFields)
			throws LogstashFieldNotCustomException, ParseException {
		this.customFields.setCustomFields(customFields);
	}

	@Override
	public void setFieldNames(String fieldNames) throws ParseException {
		this.customFields.setFieldNames(fieldNames);
	}

	@Override
	public void setMdcFields(String mdcFields)
			throws LogstashFieldNotCustomException, ParseException {
		this.mdcFields = MdcFieldsFactory.createMdcFields(mdcFields);
	}

	@Override
	public MdcFields getMdcFields() {
		return mdcFields;
	}

	@Override
	public void setTimeZone(String timeZone) {
		iso8601.setTimeZone(timeZone);
	}

	public void setTimeZone(TimeZone timeZone) {
		iso8601.setTimeZone(timeZone);
	}
}
