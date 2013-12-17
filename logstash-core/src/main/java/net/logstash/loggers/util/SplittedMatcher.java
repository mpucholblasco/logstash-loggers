package net.logstash.loggers.util;

import java.text.ParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utility to split a string and match splitted elements. Those matched elements
 * will execute {@link SplittedMatch#match(Matcher)} method.
 * 
 * @author mpucholblasco
 * 
 */
public class SplittedMatcher {

	private final Pattern splitter;
	private final Pattern patternMatch;
	private boolean ignoreSpaces;
	private boolean ignoreEmpty;

	public SplittedMatcher(final Pattern splitter, final Pattern patternMatch,
			boolean ignoreSpaces, boolean ignoreEmpty) {
		this.splitter = splitter;
		this.patternMatch = patternMatch;
		this.ignoreSpaces = ignoreSpaces;
		this.ignoreEmpty = ignoreEmpty;
	}

	public SplittedMatcher(final Pattern splitter, final Pattern patternMatch) {
		this(splitter, patternMatch, false, false);
	}

	public SplittedMatcher(final Pattern splitter, final Pattern patternMatch,
			boolean ignoreSpaces) {
		this(splitter, patternMatch, ignoreSpaces, false);
	}

	public void split(String input, SplittedMatch splittedMatch)
			throws ParseException {
		if (input != null) {
			if (ignoreSpaces) {
				input = StringUtil.trim(input);
			}
			if (!input.isEmpty()) {
				String[] inputSplittedArray = splitter.split(input);
				for (String inputSplitted : inputSplittedArray) {
					if (ignoreSpaces) {
						inputSplitted = StringUtil.trim(inputSplitted);
					}
					if (!ignoreEmpty || !inputSplitted.isEmpty()) {
						Matcher match = patternMatch.matcher(inputSplitted);
						if (match.matches()) {
							splittedMatch.match(match);
						} else {
							throw new ParseException(inputSplitted, 0);
						}
					}
				}
			}
		}
	}
}
