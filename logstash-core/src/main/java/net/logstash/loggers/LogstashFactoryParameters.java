package net.logstash.loggers;

import java.text.ParseException;

import net.logstash.loggers.field.CustomFields;
import net.logstash.loggers.field.LogstashField;
import net.logstash.loggers.field.LogstashFieldNotCustomException;
import net.logstash.loggers.field.mdc.MdcFields;

/**
 * Parameters via set/get methods.
 * 
 * @author mpucholblasco
 * 
 */
public interface LogstashFactoryParameters {

	/**
	 * Sets the time zone used to convert current date to ISO8601 (that
	 * compatible with Logstash). If the time zone is not recognized, GMT will
	 * be used.
	 * 
	 * @param timeZone
	 *            Time Zone.
	 */
	void setTimeZone(final String timeZone);

	/**
	 * Obtains custom-fields.
	 * 
	 * @return Custom fields.
	 */
	CustomFields getCustomFields();

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
	 * @throws ParseException
	 *             if the string format is incorrect.
	 * @throws LogstashFieldNotCustomException
	 *             if the field name belongs to a non-custom field present on
	 *             {@link LogstashField}.
	 */
	void setCustomFields(String extra) throws ParseException,
			LogstashFieldNotCustomException;

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
	void setFieldNames(String fieldNames) throws ParseException;

	/**
	 * Obtains the MDC field names to be included on each log entry.
	 * 
	 * @return MDC field names to be included on each log entry.
	 *         <p>
	 *         The following options are possible:
	 *         <ul>
	 *         <li><tt>null</tt>: if there are no MDC fields to add.
	 *         <li><tt>empty List</tt>: if all MDC fields present on a log entry
	 *         should be shown.
	 *         <li><tt>other case</tt>: only those MDC fields returned should be
	 *         shown.
	 *         </ul>
	 */
	MdcFields getMdcFields();

	/**
	 * Sets the MDC fields processed on each log entry. Accepted format is
	 * (field names separated by a semicolon):
	 * <p>
	 * <tt>fieldName1 ; fieldName2; fieldName3</tt>
	 * <p>
	 * or
	 * <p>
	 * <tt>ALL_FIELDS</tt> (to specify all MDC available fields)
	 * <p>
	 * <p>
	 * NOTE: spaces are ignored.
	 * 
	 * @param mdcFields
	 *            MDC fields added to all Logstash entries.
	 * @throws ParseException
	 *             if the string format is incorrect or a field name has not a
	 *             valid format.
	 * @throws LogstashFieldNotCustomException
	 *             if the field name belongs to a non-writable field present on
	 *             {@link LogstashField}.
	 */
	void setMdcFields(String mdcFields) throws ParseException,
			LogstashFieldNotCustomException;

}
