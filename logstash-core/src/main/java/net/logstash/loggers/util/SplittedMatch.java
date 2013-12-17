package net.logstash.loggers.util;

import java.util.regex.Matcher;

/**
 * Interface used on SplittedMatcher to execute the method <tt>match</tt> when a
 * match if found.
 * 
 * @author mpucholblasco
 * @see SplittedMatcher#split(String, SplittedMatch)
 */
public interface SplittedMatch {

	void match(Matcher match);

}
