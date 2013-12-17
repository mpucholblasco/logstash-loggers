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

public class MdcSelectedFieldsTest {
	private final static JsonFactory JSON_FACTORY = new JsonFactory();

	@Test
	public void testNullOnMdc() throws IOException {
		String[] selectedFields = { "mdcFieldName1", "mdcFieldName2" };
		assertMdcFields(selectedFields, "{}", new MdcAssertorHelper() {

			@Override
			public void test(JsonGenerator jsonGenerator, MdcFields mdcFields)
					throws JsonGenerationException, IOException {
				mdcFields.addToLogstash("mdc", jsonGenerator, null);
			}
		});
	}

	@Test
	public void testEmptyOnMdc() throws IOException {
		String[] selectedFields = { "mdcFieldName1", "mdcFieldName2" };
		assertMdcFields(selectedFields, "{}", new MdcAssertorHelper() {

			@Override
			public void test(JsonGenerator jsonGenerator, MdcFields mdcFields)
					throws JsonGenerationException, IOException {
				Map<String, String> emptyMdc = new HashMap<String, String>();
				mdcFields.addToLogstash("mdc", jsonGenerator, emptyMdc);
			}
		});
	}

	@Test
	public void testSeveralFieldsOnMdcAndSelectedEmpty() throws IOException {
		String[] selectedFields = {};
		assertMdcFields(selectedFields, "{}", new MdcAssertorHelper() {

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

	@Test
	public void testSeveralFieldsOnMdcAndSelectedOneExistent()
			throws IOException {
		String[] selectedFields = { "mdcFieldName1" };
		assertMdcFields(selectedFields,
				"{\"mdc\":{\"mdcFieldName1\":\"mdcFieldValue1\"}}",
				new MdcAssertorHelper() {

					@Override
					public void test(JsonGenerator jsonGenerator,
							MdcFields mdcFields)
							throws JsonGenerationException, IOException {
						Map<String, String> mdc = new LinkedHashMap<String, String>();
						mdc.put("mdcFieldName1", "mdcFieldValue1");
						mdc.put("mdcFieldName2", "mdcFieldValue2");
						mdc.put("mdcFieldName3", "mdcFieldValue3");
						mdcFields.addToLogstash("mdc", jsonGenerator, mdc);
					}
				});
	}

	@Test
	public void testSeveralFieldsOnMdcAndSelectedOneNonExistent()
			throws IOException {
		String[] selectedFields = { "NonExistentMdcFieldName" };
		assertMdcFields(selectedFields, "{}", new MdcAssertorHelper() {

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

	@Test
	public void testSeveralFieldsOnMdcAndSelectedSeveralButNotAll()
			throws IOException {
		String[] selectedFields = { "mdcFieldName1", "mdcFieldName2" };
		assertMdcFields(
				selectedFields,
				"{\"mdc\":{\"mdcFieldName1\":\"mdcFieldValue1\",\"mdcFieldName2\":\"mdcFieldValue2\"}}",
				new MdcAssertorHelper() {

					@Override
					public void test(JsonGenerator jsonGenerator,
							MdcFields mdcFields)
							throws JsonGenerationException, IOException {
						Map<String, String> mdc = new LinkedHashMap<String, String>();
						mdc.put("mdcFieldName1", "mdcFieldValue1");
						mdc.put("mdcFieldName2", "mdcFieldValue2");
						mdc.put("mdcFieldName3", "mdcFieldValue3");
						mdcFields.addToLogstash("mdc", jsonGenerator, mdc);
					}
				});
	}

	@Test
	public void testSeveralFieldsOnMdcAndSelectedAll() throws IOException {
		String[] selectedFields = { "mdcFieldName1", "mdcFieldName2",
				"mdcFieldName3" };
		assertMdcFields(
				selectedFields,
				"{\"mdc\":{\"mdcFieldName1\":\"mdcFieldValue1\",\"mdcFieldName2\":\"mdcFieldValue2\",\"mdcFieldName3\":\"mdcFieldValue3\"}}",
				new MdcAssertorHelper() {

					@Override
					public void test(JsonGenerator jsonGenerator,
							MdcFields mdcFields)
							throws JsonGenerationException, IOException {
						Map<String, String> mdc = new LinkedHashMap<String, String>();
						mdc.put("mdcFieldName1", "mdcFieldValue1");
						mdc.put("mdcFieldName2", "mdcFieldValue2");
						mdc.put("mdcFieldName3", "mdcFieldValue3");
						mdcFields.addToLogstash("mdc", jsonGenerator, mdc);
					}
				});
	}

	@Test
	public void testSeveralFieldsOnMdcAndSelectedSeveralButNotEquals()
			throws IOException {
		String[] selectedFields = { "mdcFieldName1", "mdcFieldName2",
				"mdcNonExistentField" };
		assertMdcFields(
				selectedFields,
				"{\"mdc\":{\"mdcFieldName1\":\"mdcFieldValue1\",\"mdcFieldName2\":\"mdcFieldValue2\"}}",
				new MdcAssertorHelper() {

					@Override
					public void test(JsonGenerator jsonGenerator,
							MdcFields mdcFields)
							throws JsonGenerationException, IOException {
						Map<String, String> mdc = new LinkedHashMap<String, String>();
						mdc.put("mdcFieldName1", "mdcFieldValue1");
						mdc.put("mdcFieldName2", "mdcFieldValue2");
						mdc.put("mdcFieldName3", "mdcFieldValue3");
						mdcFields.addToLogstash("mdc", jsonGenerator, mdc);
					}
				});
	}

	private final void assertMdcFields(final String[] selectedFields,
			final String expectedJson, MdcAssertorHelper assertor)
			throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();

		JsonGenerator jsonGenerator = JSON_FACTORY.createJsonGenerator(out,
				JsonEncoding.UTF8);
		jsonGenerator.writeStartObject();
		MdcFields mdcFields = new MdcSelectedFields(selectedFields);
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
