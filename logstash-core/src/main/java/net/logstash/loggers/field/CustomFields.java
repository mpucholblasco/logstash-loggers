package net.logstash.loggers.field;

import java.io.IOException;
import java.text.ParseException;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.logstash.loggers.util.HostInfo;
import net.logstash.loggers.util.SplittedMatch;
import net.logstash.loggers.util.SplittedMatcher;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonGenerator;

/**
 * Custom logstash fields.
 * <p>
 * This class manages:
 * <ul>
 * <li>Custom field names.
 * <li>Custom/extra fields (can not have the same name than basic ones)
 * <li>Custom values for customizable basic fields.
 * </ul>
 * 
 * @author mpucholblasco
 */
public class CustomFields {
	private final static Pattern PATTERN_FIELD_SEPARATOR = Pattern.compile(";");
	private final static Pattern PATTERN_CUSTOM_FIELDS = Pattern.compile("("
			+ LogstashField.FIELD_NAME_REGEX + ")\\s*=\\s*(.+)");
	private final static Pattern PATTERN_CUSTOM_NAMES = Pattern.compile("("
			+ LogstashField.FIELD_NAME_REGEX + ")\\s*=\\s*("
			+ LogstashField.FIELD_NAME_REGEX + ")");

	private final Map<LogstashField, String> fieldNames = new EnumMap<LogstashField, String>(
			LogstashField.class);
	private final Map<LogstashField, String> fieldValues = new EnumMap<LogstashField, String>(
			LogstashField.class);
	private final Map<String, String> extraFields = new HashMap<String, String>();

	public CustomFields() {
		setDefaultFieldValues();
	}

	/**
	 * Overrides the default field names. The format used must be:
	 * <p>
	 * <tt>originalFieldName1=newFieldName1;originalFieldName2=newFieldName2...</tt>
	 * <p>
	 * The following consideration must be taken into account:
	 * <ul>
	 * <li>Spaces are ignored.
	 * <li>If the user sets the same name than already existent one, the
	 * resulting JSON will contain duplicate entries. Is the user the
	 * responsible to avoid this problem.
	 * <li>New field name must follow the field name syntax and match its
	 * regular expression.
	 * </ul>
	 * 
	 * @param fieldNames
	 *            field names in string format.
	 * @throws ParseException
	 *             if the string format is incorrect or if the field name does
	 *             not follow the field name syntax.
	 */
	public void setFieldNames(final String fieldNames) throws ParseException {
		SplittedMatcher matcher = new SplittedMatcher(PATTERN_FIELD_SEPARATOR,
				PATTERN_CUSTOM_NAMES, true, true);
		matcher.split(fieldNames, new SplittedMatch() {
			@Override
			public void match(Matcher match) {
				setFieldName(match.group(1), match.group(2));
			}
		});
	}

	public String getFieldName(final LogstashField field) {
		String fieldName = fieldNames.get(field);
		if (fieldName != null) {
			return fieldName;
		}
		return field.getDefaultName();
	}

	/**
	 * Sets custom fields as a string with format:
	 * <p>
	 * <tt>fieldName1 = fieldValue1;fieldName2 = fieldValue2</tt>
	 * <p>
	 * <p>
	 * NOTE: spaces are ignored.
	 * 
	 * @param extra
	 *            extra fields added to all Logstash entries.
	 * @throws CustomFieldsParserException
	 *             if the string format is incorrect.
	 * @throws LogstashFieldNotCustomException
	 *             if the field name belongs to a non-custom field present on
	 *             {@link LogstashField}.
	 */
	public void setCustomFields(String customFields) throws ParseException,
			LogstashFieldNotCustomException {
		SplittedMatcher matcher = new SplittedMatcher(PATTERN_FIELD_SEPARATOR,
				PATTERN_CUSTOM_FIELDS, true, true);
		matcher.split(customFields, new SplittedMatch() {
			@Override
			public void match(Matcher match) {
				setCustomField(match.group(1), match.group(2));
			}
		});
	}

	/**
	 * Sets a custom field.
	 * 
	 * @param fieldName
	 *            field name.
	 * @param fieldValue
	 *            field value.
	 * @throws LogstashFieldNotCustomException
	 *             if the field name belongs to a non-custom field present on
	 *             {@link LogstashField}.
	 */
	public void setCustomField(final String fieldName, final String fieldValue)
			throws LogstashFieldNotCustomException {
		LogstashField field = LogstashField.getFieldByDefaultName(fieldName);
		if (field == null) {
			extraFields.put(fieldName, fieldValue);
		} else {
			if (!field.isCustom()) {
				throw new LogstashFieldNotCustomException(field);
			}
			fieldValues.put(field, fieldValue);
		}
	}

	public String getFieldValue(final LogstashField field) {
		return fieldValues.get(field);
	}

	/**
	 * Adds custom extra fields (those fields that do not correspond to basic
	 * fields) and field values to a Logstash Json Generator object.
	 * 
	 * @param jsonGenerator
	 *            JSon Generator object.
	 * @throws JsonGenerationException
	 *             if any JSON Generator method can not be executed.
	 * @throws IOException
	 *             if can not write the generated JSON.
	 */
	public void addToLogstash(JsonGenerator jsonGenerator)
			throws JsonGenerationException, IOException {
		for (Map.Entry<String, String> entry : extraFields.entrySet()) {
			jsonGenerator.writeStringField(entry.getKey(), entry.getValue());
		}

		for (Map.Entry<LogstashField, String> entry : fieldValues.entrySet()) {
			jsonGenerator.writeStringField(getFieldName(entry.getKey()),
					entry.getValue());
		}
	}

	public Set<String> getExtraFieldsName() {
		return extraFields.keySet();
	}

	public String getExtraFieldValue(final String fieldName) {
		return extraFields.get(fieldName);
	}

	protected void setDefaultFieldValues() {
		fieldValues.put(LogstashField.HOST, new HostInfo().getHostName());
	}

	/**
	 * Sets a field name given a valid current field name and a valid new field
	 * name (both names validations performed on other method).
	 * 
	 * @param fieldName
	 *            Field name.
	 * @param newFieldName
	 *            New field name.
	 * @throws LogstashFieldNotFoundException
	 *             if field name is not present on LogstashField list.
	 */
	private final void setFieldName(final String fieldName,
			final String newFieldName) throws LogstashFieldNotFoundException {
		LogstashField field = LogstashField.getFieldByDefaultName(fieldName);
		if (field == null) {
			throw new LogstashFieldNotFoundException(fieldName);
		}
		if (!newFieldName.equals(field.getDefaultName())) {
			fieldNames.put(field, newFieldName);
		}
	}

}
