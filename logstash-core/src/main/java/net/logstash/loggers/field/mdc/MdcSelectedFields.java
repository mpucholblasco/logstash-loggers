package net.logstash.loggers.field.mdc;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonGenerator;

/**
 * MdcFields class to add only those selected fields by the user present on MDC
 * to Logstash.
 * 
 * @author mpucholblasco
 */
public class MdcSelectedFields implements MdcFields {
	private final String[] mdcFields;

	public MdcSelectedFields(final String[] mdcFields) {
		this.mdcFields = mdcFields;
	}

	public String[] getMdcFields() {
		return mdcFields;
	}

	@Override
	public void addToLogstash(final String mdcFieldName,
			JsonGenerator jsonGenerator, Map<String, String> mdc)
			throws JsonGenerationException, IOException {
		if (mdc != null && !mdc.isEmpty()) {
			List<String> presentFields = getPresentFields(mdc);
			if (!presentFields.isEmpty()) {
				jsonGenerator.writeObjectFieldStart(mdcFieldName);
				for (String fieldName : presentFields) {
					jsonGenerator.writeStringField(fieldName,
							mdc.get(fieldName));
				}
				jsonGenerator.writeEndObject();
			}
		}
	}

	/**
	 * Returns a list with the intersection between mdcFields and MDC keys.
	 * 
	 * @param mdc
	 *            Map representing MDC.
	 * @return a list with the intersection.
	 */
	private final List<String> getPresentFields(Map<String, String> mdc) {
		List<String> result = new LinkedList<String>();

		for (String fieldName : mdcFields) {
			String fieldValue = mdc.get(fieldName);
			if (fieldValue != null && !fieldValue.isEmpty()) {
				result.add(fieldName);
			}
		}

		return result;
	}
}
