package net.logstash.loggers.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.text.ParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;

public class SplittedMatcherTest {
	private final static Pattern PATTERN_SEPARATOR = Pattern.compile(";");
	private final static Pattern PATTERN_MATCHER = Pattern
			.compile("([a-z0-9]+)");

	@Test
	public void testNull() throws ParseException {
		SplittedMatcher matcher = new SplittedMatcher(PATTERN_SEPARATOR,
				PATTERN_MATCHER);
		matcher.split(null, new SplittedMatch() {
			@Override
			public void match(Matcher match) {
				throw new RuntimeException("Not matched");
			}
		});
	}

	@Test
	public void testEmpty() throws ParseException {
		SplittedMatcher matcher = new SplittedMatcher(PATTERN_SEPARATOR,
				PATTERN_MATCHER);
		matcher.split("", new SplittedMatch() {
			@Override
			public void match(Matcher match) {
				throw new RuntimeException("Not matched");
			}
		});
	}

	@Test
	public void testIgnoreSpacesEmpty() throws ParseException {
		SplittedMatcher matcher = new SplittedMatcher(PATTERN_SEPARATOR,
				PATTERN_MATCHER, true);
		matcher.split("    \t\t\n\n", new SplittedMatch() {
			@Override
			public void match(Matcher match) {
				throw new RuntimeException("Not matched");
			}
		});
	}

	@Test
	public void testIgnoreEmptyAndIsEmpty() throws ParseException {
		SplittedMatcher matcher = new SplittedMatcher(PATTERN_SEPARATOR,
				PATTERN_MATCHER, false, true);
		matcher.split(";;;", new SplittedMatch() {
			@Override
			public void match(Matcher match) {
				throw new RuntimeException("Not matched");
			}
		});
	}

	@Test
	public void testIgnoreEmptyAndSpaceAndIsEmpty() throws ParseException {
		SplittedMatcher matcher = new SplittedMatcher(PATTERN_SEPARATOR,
				PATTERN_MATCHER, true, true);
		matcher.split("\t;  ;\n\n;\t", new SplittedMatch() {
			@Override
			public void match(Matcher match) {
				throw new RuntimeException("Not matched");
			}
		});
	}

	@Test
	public void testSingleElement() throws ParseException {
		SplittedMatcher matcher = new SplittedMatcher(PATTERN_SEPARATOR,
				PATTERN_MATCHER);
		final Matched matched = new Matched();
		matcher.split("test", new SplittedMatch() {
			@Override
			public void match(Matcher match) {
				matched.matched();
				assertEquals("test", match.group(1));
			}
		});
		assertTrue(matched.isMatched());
	}

	@Test
	public void testSingleElementIgnoreSpaces() throws ParseException {
		SplittedMatcher matcher = new SplittedMatcher(PATTERN_SEPARATOR,
				PATTERN_MATCHER, true);
		final Matched matched = new Matched();
		matcher.split("  \t\t\n\ntest\n\n\t\t  ", new SplittedMatch() {
			@Override
			public void match(Matcher match) {
				matched.matched();
				assertEquals("test", match.group(1));
			}
		});
		assertTrue(matched.isMatched());
	}

	@Test
	public void testMultipleElements() throws ParseException {
		SplittedMatcher matcher = new SplittedMatcher(PATTERN_SEPARATOR,
				PATTERN_MATCHER);
		final Matched matched = new Matched();
		final String[] expectedElements = { "test1", "test2" };
		matcher.split("test1;test2", new SplittedMatch() {
			@Override
			public void match(Matcher match) {
				int matchTime = matched.matched();
				assertMatch(matchTime, expectedElements, match.group(1));
			}
		});
		assertTrue(matched.getMatchedTimes() == 2);
	}

	@Test
	public void testMultipleElementsWithEmpty() throws ParseException {
		SplittedMatcher matcher = new SplittedMatcher(PATTERN_SEPARATOR,
				PATTERN_MATCHER, false, true);
		final Matched matched = new Matched();
		final String[] expectedElements = { "test1", "test2" };
		matcher.split("test1;;test2", new SplittedMatch() {
			@Override
			public void match(Matcher match) {
				int matchTime = matched.matched();
				assertMatch(matchTime, expectedElements, match.group(1));
			}
		});
		assertTrue(matched.getMatchedTimes() == 2);
	}

	@Test
	public void testMultipleElementsWithSpaces() throws ParseException {
		SplittedMatcher matcher = new SplittedMatcher(PATTERN_SEPARATOR,
				PATTERN_MATCHER, true);
		final Matched matched = new Matched();
		final String[] expectedElements = { "test1", "test2" };
		matcher.split("    test1     ;  test2  \t\n", new SplittedMatch() {
			@Override
			public void match(Matcher match) {
				int matchTime = matched.matched();
				assertMatch(matchTime, expectedElements, match.group(1));
			}
		});
		assertTrue(matched.getMatchedTimes() == 2);
	}

	@Test
	public void testMultipleElementsWithSpacesAndEmptyFields()
			throws ParseException {
		SplittedMatcher matcher = new SplittedMatcher(PATTERN_SEPARATOR,
				PATTERN_MATCHER, true, true);
		final Matched matched = new Matched();
		final String[] expectedElements = { "test1", "test2" };
		matcher.split("    test1     ;  ;  test2  \t\n", new SplittedMatch() {
			@Override
			public void match(Matcher match) {
				int matchTime = matched.matched();
				assertMatch(matchTime, expectedElements, match.group(1));
			}
		});
		assertTrue(matched.getMatchedTimes() == 2);
	}

	@Test
	public void testSingleElementNotMatch() {
		SplittedMatcher matcher = new SplittedMatcher(PATTERN_SEPARATOR,
				PATTERN_MATCHER);
		try {
			matcher.split("###", new SplittedMatch() {
				@Override
				public void match(Matcher match) {
					throw new RuntimeException("Not matched");
				}
			});
			fail("Could not split previous string");
		} catch (ParseException e) {
			assertEquals("###", e.getMessage());
		}
	}

	@Test
	public void testMultipleElementsNotMatch() {
		SplittedMatcher matcher = new SplittedMatcher(PATTERN_SEPARATOR,
				PATTERN_MATCHER);
		try {
			matcher.split("test;###;more", new SplittedMatch() {
				@Override
				public void match(Matcher match) {
					assertEquals("test", match.group(1));
				}
			});
			fail("Could not split previous string");
		} catch (ParseException e) {
			assertEquals("###", e.getMessage());
		}
	}

	/**
	 * Asserts a match iteration.
	 * 
	 * @param matchIteration
	 *            Match iteration number.
	 * @param expectedElements
	 *            Array of expected elements (all iteration expected results).
	 * @param currentValue
	 *            Current result to assert with expected one.
	 */
	private final void assertMatch(int matchIteration,
			String[] expectedElements, String currentValue) {
		if (matchIteration > expectedElements.length) {
			fail("Match time <" + matchIteration
					+ "> not present on expected elements (length = <"
					+ expectedElements.length + ">)");
		}
		assertEquals(expectedElements[matchIteration - 1], currentValue);
	}

	/**
	 * Class used to initialize outside a lambda function and set inside it. It
	 * counts the matched iterations.
	 * 
	 * @author mpucholblasco
	 * 
	 */
	private final class Matched {
		private int matchedIterations = 0;

		public int matched() {
			return ++matchedIterations;
		}

		public boolean isMatched() {
			return matchedIterations > 0;
		}

		public int getMatchedTimes() {
			return matchedIterations;
		}
	}
}
