package net.logstash.loggers.field.mdc;

import java.io.IOException;
import java.util.Map;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonGenerator;

/**
 * Interface to add current MDC fields to a Logstash JsonGenerator.
 * 
 * @author mpucholblasco
 */
public interface MdcFields {

	/**
	 * Adds MDC information to a Logstash JSon Generator.
	 * 
	 * @param mdcFieldName
	 *            MDC field name.
	 * @param jsonGenerator
	 *            JSon Generator object.
	 * @param mdc
	 *            MDC information.
	 * @throws JsonGenerationException
	 *             if any JSON Generator method can not be executed.
	 * @throws IOException
	 *             if can not write the generated JSON.
	 */
	void addToLogstash(final String mdcFieldName, JsonGenerator jsonGenerator,
			final Map<String, String> mdc) throws JsonGenerationException,
			IOException;
}
