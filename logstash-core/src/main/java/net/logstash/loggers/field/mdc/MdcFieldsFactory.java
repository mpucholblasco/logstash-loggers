package net.logstash.loggers.field.mdc;

import java.text.ParseException;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.logstash.loggers.field.LogstashField;
import net.logstash.loggers.util.SplittedMatch;
import net.logstash.loggers.util.SplittedMatcher;

/**
 * Factory to generate MDC Fields objects.
 * 
 * @author mpucholblasco
 */
public final class MdcFieldsFactory {
	private final static String MDC_ALL_FIELDS = "ALL_FIELDS";
	private final static Pattern PATTERN_MDC_FIELDS_SEPARATOR = Pattern
			.compile(";");
	private final static Pattern PATTERN_FIELD_NAMES = Pattern
			.compile(LogstashField.FIELD_NAME_REGEX);

	/**
	 * Parses MDC fields and returns a MdcFields object according to:
	 * <ul>
	 * <li><tt>MdcEmptyFields</tt>: if
	 * <li><tt>empty</tt>: if MDC fields is equals to <tt>ALL_FIELDS</tt>, which
	 * implies that all fields should be shown.
	 * <li><tt>other case</tt>: the parsed list of fields.
	 * </ul>
	 * 
	 * @param mdcFields
	 *            MDC fields in an unparsed format.
	 * @return List of MDC fields.
	 * @throws MdcFieldsParserIncorrectNameException
	 *             if a field name is incorrect.
	 */
	public static MdcFields createMdcFields(final String mdcFields)
			throws ParseException {
		SplittedMatcher matcher = new SplittedMatcher(
				PATTERN_MDC_FIELDS_SEPARATOR, PATTERN_FIELD_NAMES, true, true);
		final Set<String> fieldsProcessed = new LinkedHashSet<String>();
		matcher.split(mdcFields, new SplittedMatch() {
			@Override
			public void match(Matcher match) {
				fieldsProcessed.add(match.group());
			}
		});

		if (fieldsProcessed.isEmpty()) {
			return new MdcEmptyFields();
		}

		if (fieldsProcessed.size() == 1
				&& fieldsProcessed.contains(MDC_ALL_FIELDS)) {
			return new MdcAllFields();
		}
		return new MdcSelectedFields(
				fieldsProcessed.toArray(new String[fieldsProcessed.size()]));
	}
}
