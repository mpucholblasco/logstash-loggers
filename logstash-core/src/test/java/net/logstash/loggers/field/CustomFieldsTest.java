package net.logstash.loggers.field;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.text.ParseException;
import java.util.Collections;
import java.util.Set;
import java.util.TreeSet;

import net.logstash.loggers.util.HostInfo;

import org.junit.Test;

public class CustomFieldsTest {
	// Custom names
	@Test(expected = ParseException.class)
	public void testFieldNameParserError()
			throws LogstashFieldNotCustomException, ParseException {
		CustomFields customFields = new CustomFields();
		customFields.setFieldNames("###=myValue");
	}

	@Test
	public void testFieldNameNotFound() throws LogstashFieldNotCustomException,
			ParseException {
		CustomFields customFields = new CustomFields();

		try {
			customFields.setFieldNames("nonExistentFieldName=myValue");
			fail("Exception LogstashFieldNotFoundException must be raised.");
		} catch (LogstashFieldNotFoundException ex) {
			assertEquals("nonExistentFieldName", ex.getField());
		}
	}

	@Test
	public void testFieldNameWithSameName()
			throws LogstashFieldNotCustomException, ParseException {
		CustomFields customFields = new CustomFields();
		assertEquals(LogstashField.HOST.getDefaultName(),
				customFields.getFieldName(LogstashField.HOST));
		customFields.setFieldNames(LogstashField.HOST.getDefaultName() + "="
				+ LogstashField.HOST.getDefaultName());
		assertEquals(LogstashField.HOST.getDefaultName(),
				customFields.getFieldName(LogstashField.HOST));
	}

	@Test
	public void testFieldNameFound() throws LogstashFieldNotCustomException,
			ParseException {
		CustomFields customFields = new CustomFields();
		assertEquals(LogstashField.TIMESTAMP.getDefaultName(),
				customFields.getFieldName(LogstashField.TIMESTAMP));
		customFields.setFieldNames(LogstashField.TIMESTAMP.getDefaultName()
				+ "=myTimestamp");
		assertEquals("myTimestamp",
				customFields.getFieldName(LogstashField.TIMESTAMP));
	}

	// Custom fields
	@Test(expected = LogstashFieldNotCustomException.class)
	public void testCustomFieldSetFieldNonWritable()
			throws LogstashFieldNotCustomException {
		CustomFields customFields = new CustomFields();
		customFields.setCustomField(LogstashField.TIMESTAMP.getDefaultName(),
				"myFieldValue");
	}

	@Test
	public void testCustomFieldSetFieldWritable()
			throws LogstashFieldNotCustomException {
		String expectedValue = "myFieldValue";
		CustomFields customFields = new CustomFields();
		customFields.setCustomField(LogstashField.HOST.getDefaultName(),
				expectedValue);
		String currentValue = customFields.getFieldValue(LogstashField.HOST);
		assertEquals(expectedValue, currentValue);
	}

	@Test
	public void testCustomFieldDefaultHostInfo() throws Throwable {
		HostInfo expectedHostName = new HostInfo();
		CustomFields customFields = new CustomFields();
		customFields.setCustomField(LogstashField.HOST.getDefaultName(),
				expectedHostName.getHostName());
		String currentHostName = customFields.getFieldValue(LogstashField.HOST);
		assertEquals(expectedHostName.getHostName(), currentHostName);
	}

	@Test
	public void testExtraFieldsEmpty() {
		CustomFields customFields = new CustomFields();
		Set<String> currentExtraFieldNames = customFields.getExtraFieldsName();
		assertTrue(currentExtraFieldNames.isEmpty());
	}

	@Test
	public void testExtraFieldsFormatEmpty() throws Throwable {
		CustomFields customFields = new CustomFields();
		customFields.setCustomFields("");
		Set<String> extraFieldsName = customFields.getExtraFieldsName();
		assertTrue(extraFieldsName.isEmpty());
	}

	@Test
	public void testExtraFieldsFormatSpaces() throws Throwable {
		CustomFields customFields = new CustomFields();
		customFields.setCustomFields("                ");
		Set<String> extraFieldsName = customFields.getExtraFieldsName();
		assertTrue(extraFieldsName.isEmpty());
	}

	@Test(expected = LogstashFieldNotCustomException.class)
	public void testExtraFieldsFormatMultiplePairsWithReservedNames()
			throws Throwable {
		CustomFields customFields = new CustomFields();
		customFields
				.setCustomFields("   myExtraField1  =  myExtraFieldValue1  ;   "
						+ LogstashField.TIMESTAMP.getDefaultName()
						+ "  =   myExtraFieldValue2  ;   myExtraField3  =   myExtraFieldValue3    ");
	}

	@Test
	public void testExtraFieldsWithValues()
			throws LogstashFieldNotCustomException {
		CustomFields customFields = new CustomFields();
		String[] expectedFieldNames = new String[] { "myExtraField1",
				"myExtraField2", "myExtraField3" };
		String[] expectedFieldValues = new String[] { "myExtraFieldValue1",
				"myExtraFieldValue2", "myExtraFieldValue3" };

		for (int i = 0; i < expectedFieldNames.length; ++i) {
			customFields.setCustomField(expectedFieldNames[i],
					expectedFieldValues[i]);
		}
		assertExtraFields(expectedFieldNames, expectedFieldValues, customFields);
	}

	@Test
	public void testExtraFieldsFormatSinglePair() throws Throwable {
		CustomFields customFields = new CustomFields();
		customFields.setCustomFields("myExtraField1=myExtraFieldValue1");

		String[] expectedFieldNames = new String[] { "myExtraField1" };
		String[] expectedFieldValues = new String[] { "myExtraFieldValue1" };
		assertExtraFields(expectedFieldNames, expectedFieldValues, customFields);
	}

	@Test
	public void testExtraFieldsFormatMultiplePairs() throws Throwable {
		CustomFields customFields = new CustomFields();
		customFields
				.setCustomFields("myExtraField1=myExtraFieldValue1;myExtraField2=myExtraFieldValue2;myExtraField3=myExtraFieldValue3");

		String[] expectedFieldNames = new String[] { "myExtraField1",
				"myExtraField2", "myExtraField3" };
		String[] expectedFieldValues = new String[] { "myExtraFieldValue1",
				"myExtraFieldValue2", "myExtraFieldValue3" };

		assertExtraFields(expectedFieldNames, expectedFieldValues, customFields);
	}

	@Test
	public void testExtraFieldsFormatMultiplePairsWithSpaces() throws Throwable {
		CustomFields customFields = new CustomFields();
		customFields
				.setCustomFields("   myExtraField1  =  myExtraFieldValue1  ;   myExtraField2  =   myExtraFieldValue2  ;   myExtraField3  =   myExtraFieldValue3    ");

		String[] expectedFieldNames = new String[] { "myExtraField1",
				"myExtraField2", "myExtraField3" };
		String[] expectedFieldValues = new String[] { "myExtraFieldValue1",
				"myExtraFieldValue2", "myExtraFieldValue3" };

		assertExtraFields(expectedFieldNames, expectedFieldValues, customFields);
	}

	/**
	 * Asserts extra fields given the expected field names and expected field
	 * values (in which element <tt>n</tt> in expectedFieldNames has value
	 * present on position <tt>n</tt> in expectedFieldValues) and the custom
	 * fields object (from which are obtained current values).
	 * 
	 * @param expectedFieldNames
	 *            Expected field names.
	 * @param expectedFieldValues
	 *            Expected field values.
	 * @param customFields
	 *            Custom fields object.
	 */
	private final void assertExtraFields(String[] expectedFieldNames,
			String[] expectedFieldValues, final CustomFields customFields) {
		Set<String> expectedExtraFieldNamesSet = new TreeSet<String>();
		Collections.addAll(expectedExtraFieldNamesSet, expectedFieldNames);
		Set<String> currentExtraFieldNamesSet = customFields
				.getExtraFieldsName();
		assertEquals(expectedExtraFieldNamesSet, currentExtraFieldNamesSet);

		assertEquals(expectedFieldNames.length, expectedFieldValues.length);

		for (int i = expectedFieldValues.length - 1; i >= 0; --i) {
			String expectedFieldValue = expectedFieldValues[i];
			String currentFieldValue = customFields
					.getExtraFieldValue(expectedFieldNames[i]);
			assertEquals(expectedFieldValue, currentFieldValue);
		}
	}
}
