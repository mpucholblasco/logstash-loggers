package net.logstash.loggers.field;

/**
 * Exception generated when a field that can not be found on a search.
 * 
 * @author mpucholblasco
 * 
 */
public class LogstashFieldNotFoundException extends LogstashFieldException {

	private static final long serialVersionUID = 3950847344931001235L;
	private final String field;

	public LogstashFieldNotFoundException(final String field) {
		this.field = field;
	}

	public String getField() {
		return field;
	}

	@Override
	public String toString() {
		return "Could not found field <" + field + ">.";
	}
}