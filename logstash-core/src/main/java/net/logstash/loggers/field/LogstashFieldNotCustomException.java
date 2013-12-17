package net.logstash.loggers.field;

/**
 * Exception generated when a field that can not be customized is tried to do.
 * 
 * @author mpucholblasco
 * 
 */
public class LogstashFieldNotCustomException extends LogstashFieldException {

	private static final long serialVersionUID = -8071911243738410635L;

	private final LogstashField field;

	public LogstashFieldNotCustomException(final LogstashField field) {
		this.field = field;
	}

	public LogstashField getField() {
		return field;
	}

	@Override
	public String toString() {
		return "Can not set the non-writable field <" + field + ">.";
	}
}