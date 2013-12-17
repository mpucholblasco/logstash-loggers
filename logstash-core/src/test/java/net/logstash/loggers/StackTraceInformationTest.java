package net.logstash.loggers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class StackTraceInformationTest {

	@Test
	public void testGetNullStacktrace() throws Throwable {
		assertEquals("", StackTraceInformation.getStackTraceInfo(null));
	}

	@Test
	public void testBasicException() throws Throwable {
		Exception ex = new Exception();
		assertNotNull(StackTraceInformation.getStackTraceInfo(ex));
	}

	@Test
	public void testBasicInnerException() throws Throwable {
		Exception exInner = new Exception();
		Exception ex = new Exception(exInner);
		assertNotNull(StackTraceInformation.getStackTraceInfo(ex));
	}

	@Test
	public void testMultipleInnerException() throws Throwable {
		Exception exInner = new Exception();
		Exception exInner1 = new Exception(exInner);
		Exception ex = new Exception(exInner1);
		assertNotNull(StackTraceInformation.getStackTraceInfo(ex));
	}

	@Test
	public void testMultipleWithRepeatedInnerException() throws Throwable {
		Exception exInner = new Exception();
		Exception ex = new Exception(exInner);
		Exception ex2 = new Exception(ex);
		assertNotNull(StackTraceInformation.getStackTraceInfo(ex2));
	}
}
