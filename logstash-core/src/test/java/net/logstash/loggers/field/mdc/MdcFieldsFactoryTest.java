package net.logstash.loggers.field.mdc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.fail;

import java.text.ParseException;

import net.logstash.loggers.field.mdc.MdcAllFields;
import net.logstash.loggers.field.mdc.MdcEmptyFields;
import net.logstash.loggers.field.mdc.MdcFields;
import net.logstash.loggers.field.mdc.MdcFieldsFactory;
import net.logstash.loggers.field.mdc.MdcSelectedFields;

import org.junit.Test;

public class MdcFieldsFactoryTest {
	@Test
	public void testEmpty() throws ParseException {
		MdcFields mdcFields = MdcFieldsFactory.createMdcFields("");
		assertEquals(mdcFields.getClass(), MdcEmptyFields.class);
	}

	@Test
	public void testEmptyWithSpaces() throws ParseException {
		MdcFields mdcFields = MdcFieldsFactory.createMdcFields("       ");
		assertEquals(mdcFields.getClass(), MdcEmptyFields.class);
	}

	@Test
	public void testAllFields() throws ParseException {
		MdcFields mdcFields = MdcFieldsFactory.createMdcFields("ALL_FIELDS");
		assertEquals(mdcFields.getClass(), MdcAllFields.class);
	}

	@Test
	public void testAllFieldsWithSpaces() throws ParseException {
		MdcFields mdcFields = MdcFieldsFactory
				.createMdcFields("   ALL_FIELDS   ");
		assertEquals(mdcFields.getClass(), MdcAllFields.class);
	}

	@Test
	public void testSelectedFieldsOneField() throws ParseException {
		MdcFields mdcFields = MdcFieldsFactory.createMdcFields("field1");
		assertEquals(mdcFields.getClass(), MdcSelectedFields.class);
		MdcSelectedFields selectedFields = (MdcSelectedFields) mdcFields;
		assertArrayEquals(new String[] { "field1" },
				selectedFields.getMdcFields());
	}

	@Test
	public void testSelectedFieldsSeveralFields() throws ParseException {
		MdcFields mdcFields = MdcFieldsFactory
				.createMdcFields("field1;field2;field3");
		assertEquals(mdcFields.getClass(), MdcSelectedFields.class);
		MdcSelectedFields selectedFields = (MdcSelectedFields) mdcFields;
		assertArrayEquals(new String[] { "field1", "field2", "field3" },
				selectedFields.getMdcFields());
	}

	@Test
	public void testSelectedFieldsSeveralFieldsWithSpaces()
			throws ParseException {
		MdcFields mdcFields = MdcFieldsFactory
				.createMdcFields("  field1 ;   field2 ;   field3   ");
		assertEquals(mdcFields.getClass(), MdcSelectedFields.class);
		MdcSelectedFields selectedFields = (MdcSelectedFields) mdcFields;
		assertArrayEquals(new String[] { "field1", "field2", "field3" },
				selectedFields.getMdcFields());
	}

	@Test
	public void testSingleIncorrectName() throws ParseException {
		try {
			MdcFieldsFactory.createMdcFields("field 1");
			fail("<field 1> must be an incorrect field name.");
		} catch (ParseException ex) {
			assertEquals("field 1", ex.getMessage());
		}
	}

	@Test
	public void testIncorrectNameBetweenCorrectOnes() throws ParseException {
		try {
			MdcFieldsFactory
					.createMdcFields("field1;  #IncorrectField  ; field3");
			fail("<#IncorrectField> must be an incorrect field name.");
		} catch (ParseException ex) {
			assertEquals("#IncorrectField", ex.getMessage());
		}
	}
}
