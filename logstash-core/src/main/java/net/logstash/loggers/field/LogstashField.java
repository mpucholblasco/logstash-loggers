package net.logstash.loggers.field;

/**
 * Fields present on Logstash.
 * 
 * @author mpucholblasco
 * 
 */
public enum LogstashField {
	HOST("host", true), LEVEL("level", false), MESSAGE("message", false), TIMESTAMP(
			"@timestamp", false), THREAD("thread", false), CLASSNAME(
			"classname", false), GENERATION_ERROR("generation_error", false), EXCEPTION_CLASS(
			"exception_class", false), NDC("ndc", false), MDC("mdc", false), EXCEPTION_MESSAGE(
			"exception_message", false), EXCEPTION_STACKTRACE(
			"exception_stacktrace", false);

	public final static String FIELD_NAME_REGEX = "@?[a-zA-Z_][a-zA-Z0-9_]*";

	/**
	 * Obtains a field by its default name.
	 * 
	 * @param defaultName
	 *            Default name to obtain the field.
	 * @return The field associated with the default name passed as parameter or
	 *         <tt>null</tt> if it is not found or if default name passed as
	 *         parameter is <tt>null</tt>.
	 */
	public static LogstashField getFieldByDefaultName(final String defaultName) {
		if (defaultName == null) {
			return null;
		}

		for (LogstashField field : LogstashField.values()) {
			if (field.defaultName.equals(defaultName)) {
				return field;
			}
		}
		return null;
	}

	private final String defaultName;
	private final boolean isCustom;

	LogstashField(final String defaultName, final boolean isCustom) {
		this.defaultName = defaultName;
		this.isCustom = isCustom;
	}

	public String getDefaultName() {
		return defaultName;
	}

	/**
	 * Whether the field is customizable or not.
	 * 
	 * @return if the field is customizable (<tt>true</tt>) or not (
	 *         <tt>false</tt>).
	 */
	public boolean isCustom() {
		return isCustom;
	}

	@Override
	public String toString() {
		return defaultName;
	}
}
