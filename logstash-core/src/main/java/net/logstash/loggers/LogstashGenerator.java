package net.logstash.loggers;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import net.logstash.loggers.field.LogstashField;
import net.logstash.loggers.util.HostInfo;
import net.logstash.loggers.util.ISO8601;

import org.codehaus.jackson.JsonEncoding;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonGenerator;

/**
 * Generates Logstash log entries.
 * 
 * @author mpucholblasco
 * 
 */
public class LogstashGenerator {
	/**
	 * Generates a valid Logstash string entry showing a throwable message
	 * without using any external library. This method must only be used on
	 * catch regions.
	 * <p>
	 * The log entry will have the form:
	 * <p>
	 * <code>{ "@timestamp": "2013-10-05T10:00:05.753Z", "host": "myhost", "generation_error": "throwable message" }</code>
	 * 
	 * @param throwable
	 *            throwable object to convert to string.
	 * @return a string with valid Logstash entry showing the exception message.
	 */
	public static String getErrorLogstashFromThrowable(Throwable throwable) {
		if (throwable != null) {
			StringBuilder sb = new StringBuilder();
			sb.append("{");
			sb.append("\"").append(LogstashField.TIMESTAMP.getDefaultName())
					.append("\":\"").append(new ISO8601().getStringDate())
					.append("\"");
			sb.append(",\"").append(LogstashField.HOST.getDefaultName())
					.append("\":\"").append(new HostInfo().getHostName())
					.append("\"");
			sb.append(",\"")
					.append(LogstashField.GENERATION_ERROR.getDefaultName())
					.append("\":\"").append(throwable.toString()).append("\"");
			sb.append("}");
			return sb.toString();
		}
		return "";
	}

	private final LogstashFactory factory;
	private String timestamp;
	private String level = null;
	private String message = "-";
	private String thread = null;
	private String classname = null;
	private Throwable throwable = null;
	private String ndc = null;
	private Map<String, String> mdc = null;

	LogstashGenerator(LogstashFactory factory) {
		this.factory = factory;
		timestamp = factory.getISO8601().getStringDate();
	}

	public LogstashGenerator setLevel(final String level) {
		this.level = level;
		return this;
	}

	public LogstashGenerator setMessage(final String message) {
		this.message = message;
		return this;
	}

	public LogstashGenerator setThrowable(final Throwable throwable) {
		this.throwable = throwable;
		return this;
	}

	public LogstashGenerator setThread(final String thread) {
		this.thread = thread;
		return this;
	}

	public LogstashGenerator setClassName(final String classname) {
		this.classname = classname;
		return this;
	}

	/**
	 * Defines the event timestamp in ISO8601 string format.
	 * 
	 * @param timestamp
	 *            time in ISO8601 string format. E.g.: 2013-11-10T13:01:53.024Z
	 */
	public LogstashGenerator setTimestamp(String timestamp) {
		this.timestamp = timestamp;
		return this;
	}

	/**
	 * Defines the event timestamp from now.
	 */
	public LogstashGenerator setTimestamp() {
		this.timestamp = factory.getISO8601().getStringDate();
		return this;
	}

	/**
	 * Defines the event timestamp from a Date object.
	 * 
	 * @param date
	 *            date.
	 */
	public LogstashGenerator setTimestamp(Date date) {
		this.timestamp = factory.getISO8601().getStringDate(date);
		return this;
	}

	/**
	 * Defines the event timestamp in milliseconds format.
	 * 
	 * @param millis
	 *            time in milliseconds since 1970
	 */
	public LogstashGenerator setTimestamp(long millis) {
		this.timestamp = factory.getISO8601().getStringDate(millis);
		return this;
	}

	public LogstashGenerator setNdc(String ndc) {
		this.ndc = ndc;
		return this;
	}

	public LogstashGenerator setMdc(Map<String, String> mdc) {
		if (mdc != null && !mdc.isEmpty()) {
			this.mdc = new HashMap<String, String>(mdc);
		}
		return this;
	}

	public LogstashFactory getFactory() {
		return factory;
	}

	public String getTimestamp() {
		return timestamp;
	}

	/**
	 * Converts current object into a valid Logstash JSon string.
	 * 
	 * @return a valid Logstash JSon string.
	 * @throws IOException
	 *             if the JSon generation process can not generate a valid JSon
	 *             output.
	 */
	public String toLogstashJSonString() throws IOException {

		ByteArrayOutputStream out = new ByteArrayOutputStream();

		JsonGenerator jsonGenerator = factory.getJsonFactory()
				.createJsonGenerator(out, JsonEncoding.UTF8);
		jsonGenerator.writeStartObject();
		jsonWriteStringField(jsonGenerator, LogstashField.TIMESTAMP, timestamp);
		jsonWriteStringField(jsonGenerator, LogstashField.LEVEL, level);
		jsonWriteStringField(jsonGenerator, LogstashField.THREAD, thread);
		jsonWriteStringField(jsonGenerator, LogstashField.CLASSNAME, classname);
		jsonWriteStringField(jsonGenerator, LogstashField.MESSAGE, message);
		jsonWriteStringField(jsonGenerator, LogstashField.NDC, ndc);
		factory.getMdcFields().addToLogstash(
				factory.getCustomFields().getFieldName(LogstashField.MDC),
				jsonGenerator, mdc);

		factory.getCustomFields().addToLogstash(jsonGenerator);

		addThrowableInfo(jsonGenerator);

		jsonGenerator.writeEndObject();
		jsonGenerator.close();
		
		StringBuffer result = new StringBuffer();
		result.append(out.toString("UTF8"));
		result.append('\n');
		return result.toString();
	}

	/**
	 * Adds throwable information to a JSON Generator object.
	 * 
	 * @param jsonGenerator
	 *            JSON Generator object
	 * @param throwable
	 *            Throwable object.
	 * @throws JsonGenerationException
	 *             if any JSON Generator method can not be executed.
	 * @throws IOException
	 *             if can not write the generated JSON.
	 */
	private final void addThrowableInfo(JsonGenerator jsonGenerator)
			throws JsonGenerationException, IOException {
		if (throwable != null) {
			jsonWriteStringField(jsonGenerator, LogstashField.EXCEPTION_CLASS,
					throwable.getClass().getCanonicalName());
			jsonWriteStringField(jsonGenerator,
					LogstashField.EXCEPTION_MESSAGE, throwable.getMessage());
			jsonWriteStringField(jsonGenerator,
					LogstashField.EXCEPTION_STACKTRACE,
					StackTraceInformation.getStackTraceInfo(throwable));
		}
	}

	/**
	 * Writes into a JSonGenerator object only if fieldValue is not null.
	 * 
	 * @param jsonGenerator
	 *            JsonGenerator object to write to.
	 * @param fieldName
	 *            Field name.
	 * @param fieldValue
	 *            Field value.
	 * @throws IOException
	 *             if can not write to the JSON generator object.
	 * @throws JsonGenerationException
	 *             if there is any exception generating JSON.
	 */
	private final void jsonWriteStringField(JsonGenerator jsonGenerator,
			LogstashField field, String fieldValue)
			throws JsonGenerationException, IOException {
		if (fieldValue != null && !fieldValue.isEmpty()) {
			jsonGenerator.writeStringField(factory.getCustomFields()
					.getFieldName(field), fieldValue);
		}
	}
}
