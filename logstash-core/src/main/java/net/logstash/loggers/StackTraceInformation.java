package net.logstash.loggers;

/**
 * Useful methods to extract information from a throwable object to generate
 * stacktrace string information.
 * <p>
 * Code partially obtained from class
 * <tt>org.jboss.logmanager.formatters.Formatters</tt>, version
 * <tt>1.4.0.Final</tt>.
 * 
 * @author mpucholblasco
 * @see org.jboss.logmanager.formatters.Formatters
 */
public class StackTraceInformation {
	/**
	 * Returns the stacktrace information in string format by concatenating all
	 * the stacktrace elements and the causes that generated this
	 * 
	 * @param t
	 *            throwable exception from which to extract the stacktrace.
	 * @return resulting stacktrace information.
	 */
	public static String getStackTraceInfo(final Throwable t) {
		if (t != null) {
			StringBuilder builder = new StringBuilder();
			builder.append(t).append('\n');

			final StackTraceElement[] stackTrace = t.getStackTrace();
			for (StackTraceElement element : stackTrace) {
				renderTrivial(builder, element);
			}
			final Throwable cause = t.getCause();
			if (cause != null) {
				renderCause(builder, t, cause);
			}

			return builder.toString();
		}
		return "";
	}

	/**
	 * Renders a single stack trace element on the string builder passed as
	 * argument.
	 * 
	 * @param builder
	 *            string builder.
	 * @param element
	 *            stack trace element.
	 */
	private static void renderTrivial(final StringBuilder builder,
			final StackTraceElement element) {
		builder.append("\tat ").append(element).append('\n');
	}

	/**
	 * Renders a throwable cause given the father throwable and the throwable
	 * cause on the string builder passed as argument.
	 * <p>
	 * Only those stack trace elements that differs from the father throwable
	 * will be shown.
	 * 
	 * @param builder
	 *            string builder.
	 * @param t
	 *            father throwable.
	 * @param cause
	 *            cause throwable.
	 */
	private static void renderCause(final StringBuilder builder,
			final Throwable t, final Throwable cause) {

		final StackTraceElement[] causeStack = cause.getStackTrace();
		final StackTraceElement[] currentStack = t.getStackTrace();

		int m = causeStack.length - 1;
		int n = currentStack.length - 1;

		// Walk the stacks backwards from the end, until we find an element that
		// is different
		while (m >= 0 && n >= 0 && causeStack[m].equals(currentStack[n])) {
			m--;
			n--;
		}
		int framesInCommon = causeStack.length - 1 - m;

		builder.append("Caused by: ").append(cause).append('\n');

		for (int i = 0; i <= m; i++) {
			renderTrivial(builder, causeStack[i]);
		}
		if (framesInCommon != 0) {
			builder.append("\t... ").append(framesInCommon).append(" more")
					.append('\n');
		}

		// Recurse if we have an inner cause
		final Throwable ourCause = cause.getCause();
		if (ourCause != null) {
			renderCause(builder, cause, ourCause);
		}
	}
}
