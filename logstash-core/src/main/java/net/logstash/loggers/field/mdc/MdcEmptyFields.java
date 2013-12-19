package net.logstash.loggers.field.mdc;

import java.io.IOException;
import java.util.Map;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonGenerator;

/**
 * MDC class which does not add any MDC information.
 * 
 * @author mpucholblasco
 */
public class MdcEmptyFields implements MdcFields {
	@Override
	public void addToLogstash(final String mdcFieldName,
			JsonGenerator jsonGenerator, final Map<String, String> mdc)
			throws JsonGenerationException, IOException {
	}
}
