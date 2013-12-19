package net.logstash.loggers.field.mdc;

import java.io.IOException;
import java.util.Map;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonGenerator;

/**
 * MdcFields class to add all fields present on MDC to Logstash.
 * 
 * @author mpucholblasco
 */
public class MdcAllFields implements MdcFields {
	@Override
	public void addToLogstash(final String mdcFieldName,
			JsonGenerator jsonGenerator, final Map<String, String> mdc)
			throws JsonGenerationException, IOException {
		if (mdc != null && !mdc.isEmpty()) {
			jsonGenerator.writeObjectFieldStart(mdcFieldName);
			for (Map.Entry<String, String> entry : mdc.entrySet()) {
				jsonGenerator
						.writeStringField(entry.getKey(), entry.getValue());
			}
			jsonGenerator.writeEndObject();
		}
	}
}
