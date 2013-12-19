What is it?
===========

A library to write Logstash formatted logs.

This library must be used by other libraries/modules to work with specific logging libraries (such as java.util.Logging, log4j, etc).

Logic Flow
----------

 1. Another module creates a new LogstashFactory.
 1. Previously created LogstashFactory admits the parameters shown at Parametrizing section.
 1. Once LogstashFactory is configured according to the user's requirements, for each log entry to shown:
  1. Create a LogstashGenerator object by calling: logstashFactory.createGenerator() method.
  1. Set all fields by using LogstashGenerator setters: setLevel, setMessage, setThrowable, setTimestamp, etc.
  1. Once all fields are set on LogstashGenerator, to obtain the Logstash formatted log, the method logstashGenerator.toLogstashJSonString() must be called.
  1. The external module is the responsible to show previous string into the correct logger.

Parametrizing
-------------

Those parameters accepted by LogstashFactory are:

 1. setTimeZone : Sets the time zone used to convert current date to ISO8601 (that compatible with Logstash). If the time zone is not recognized, GMT wil be used.
 1. setCustomFields : Sets custom fields as a string with format:
`fieldName1 = fieldValue1[;fieldName2 = fieldValue2...]`
  * Spaces are ignored.
 1. setFieldNames : Overrides the default field names. The format used must be:
`originalFieldName1=newFieldName1;[originalFieldName2=newFieldName2...]`
  * Spaces are ignored.
  * If the user sets the same name than already existent one, the resulting JSON will contain duplicate entries. Is the user the responsible to avoid this problem. 
  * New field name must follow the field name syntax and match its regular expression.
 1. setMdcFields : Sets the MDC fields processed on each log entry. Accepted format is (field names separated by a semicolon):
`fieldName1 ; fieldName2; fieldName3`
or
`ALL_FIELDS` (to specify all MDC available fields)
  * Spaces are ignored.

