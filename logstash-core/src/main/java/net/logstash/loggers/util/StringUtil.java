package net.logstash.loggers.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Useful methods for a string.
 * 
 * @author mpucholblasco
 * 
 */
public class StringUtil {
	private final static Pattern START_TRIM_PATTERN = Pattern.compile("^\\s+");
	private final static Pattern END_TRIM_PATTERN = Pattern.compile("\\s+$");

	/**
	 * Trims a string by removing spaces (those detected by pattern \s) from the
	 * start and the end.
	 * 
	 * @param input
	 *            Input to be trimmed.
	 * @return String trimmed.
	 * @throws IllegalArgumentException
	 *             if input is <tt>null</tt>.
	 */
	public static String trim(String input) {
		if (input == null) {
			throw new IllegalArgumentException("<input> can not be null");
		}

		Matcher matcher = START_TRIM_PATTERN.matcher(input);
		if (matcher.find()) {
			input = matcher.replaceFirst("");
		}

		matcher = END_TRIM_PATTERN.matcher(input);
		if (matcher.find()) {
			input = matcher.replaceFirst("");
		}
		return input;
	}
}
