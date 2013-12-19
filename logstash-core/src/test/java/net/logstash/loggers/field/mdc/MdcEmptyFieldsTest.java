package net.logstash.loggers.field.mdc;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.codehaus.jackson.JsonEncoding;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonGenerator;
import org.junit.Test;

public class MdcEmptyFieldsTest {
	private final static JsonFactory JSON_FACTORY = new JsonFactory();

	@Test
	public void testNull() throws IOException {
		assertMdcFields("{}", new MdcAssertorHelper() {

			@Override
			public void test(JsonGenerator jsonGenerator, MdcFields mdcFields)
					throws JsonGenerationException, IOException {
				mdcFields.addToLogstash("mdc", jsonGenerator, null);
			}
		});
	}

	@Test
	public void testEmpty() throws IOException {
		assertMdcFields("{}", new MdcAssertorHelper() {

			@Override
			public void test(JsonGenerator jsonGenerator, MdcFields mdcFields)
					throws JsonGenerationException, IOException {
				Map<String, String> emptyMdc = new HashMap<String, String>();
				mdcFields.addToLogstash("mdc", jsonGenerator, emptyMdc);
			}
		});
	}

	@Test
	public void testSeveralFields() throws IOException {
		assertMdcFields("{}", new MdcAssertorHelper() {

			@Override
			public void test(JsonGenerator jsonGenerator, MdcFields mdcFields)
					throws JsonGenerationException, IOException {
				Map<String, String> mdc = new LinkedHashMap<String, String>();
				mdc.put("mdcFieldName1", "mdcFieldValue1");
				mdc.put("mdcFieldName2", "mdcFieldValue2");
				mdc.put("mdcFieldName3", "mdcFieldValue3");
				mdcFields.addToLogstash("mdc", jsonGenerator, mdc);
			}
		});
	}

	private final void assertMdcFields(final String expectedJson,
			MdcAssertorHelper assertor) throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();

		JsonGenerator jsonGenerator = JSON_FACTORY.createJsonGenerator(out,
				JsonEncoding.UTF8);
		jsonGenerator.writeStartObject();
		MdcFields mdcFields = new MdcEmptyFields();
		assertor.test(jsonGenerator, mdcFields);
		jsonGenerator.writeEndObject();
		jsonGenerator.close();
		assertEquals(expectedJson, out.toString("UTF8"));
	}

	private interface MdcAssertorHelper {
		void test(JsonGenerator jsonGenerator, MdcFields mdcFields)
				throws JsonGenerationException, IOException;
	}
}
