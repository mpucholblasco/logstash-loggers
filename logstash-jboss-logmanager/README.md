What is it?
===========

A logger handler to write Logstash formatted logs on JBoss Logging Module.

Logic Flow
----------

This module follow the next flow to generate Logstash log:

 1. To initialize Logstash, a LogstashFactory object is created by each file handler.
 1. Then, some Logstash parameters are configured via setters on previous handlers. These configurations are global to all logs and are present on `logstash-core` readme file.
 1. For each log event:
  1. Create a LogstashGenerator object.
  1. Add information to previous object via setters, extracting information from the log event and passing it to the object.
  1. Once all information is present on the object, a Logstash log is obtained from it.

Installing
----------

To install this module, a JBoss Logging module must be created. Follow these steps to create it:

 1. Go to `${jboss_folder}/modules`
 1. Create the path structure `net/logstash/loggers/logstash_jboss_logmanager/main/` inside previous one.
 1. Copy `module.xml` file to folder `${jboss_folder}/modules/net/logstash/loggers/logstash_jboss_logmanager/main/`.
 1. Copy `logstash-core-${version}.jar` and `logstash-jboss-logmanager-${version}.jar` to the same folder.

Configuring
-----------

To configure this module on JBoss, a [custom-handler|https://docs.jboss.org/author/display/AS71/Logging+Configuration#LoggingConfiguration-customhandler] must be used to configure the handler class we want to use and those parameters used by them.

The following handlers are present on this module:

 * `LogstashPeriodicRotatingFileHandler` (class `net.logstash.loggers.logstash_jboss_logmanager.LogstashPeriodicRotatingFileHandler`) : A file handler which rotates the log at a preset time interval and stores information in Logstash format. The interval is determined by the content of the suffix string which is passed in to setSuffix(String).
 * `LogstashSizeRotatingFileHandler` (class `net.logstash.loggers.logstash_jboss_logmanager.LogstashSizeRotatingFileHandler`) : a file handler which rotates the log at certain size and stores information in Logstash format.

There are some common parameters present on all handlers:
 * File handler parameters
  * `file` (string) : the file description consisting of the path and optional relative to path.
  * `append` (boolean) : specifies whether to append to the target file.
  * `autoflush` (boolean): automatically flush after each write.
 * Logstash parameters
  * `timeZone` (string) : sets the time zone used to convert current date to ISO8601 (that compatible with Logstash). If the time zone is not recognized, GMT will be used.
  * `customFields` (string) : sets custom fields present on all logs.
  * `fieldNames` (string) : overrides the default field names present on all logs.
  * `mdcFields` (string) : specify which MDC fields will be present on all logs.

To obtain more information regarding the content and/or the format present on previous parameters, see `logstash-core` readme.

By the other hand, those specific parameters for each handler, are:

 * `LogstashPeriodicRotatingFileHandler`
  * `suffix` (string) : sets the suffix string. The string is in a format which can be understood by java.text.SimpleDateFormat. The period of the rotation is automatically calculated based on the suffix.
 * `LogstashSizeRotatingFileHandler`
  * `rotateSize` (long) : the size at which to rotate the log file in bytes.
  * `maxBackupIndex` (integer) : the maximum number of backups to keep.

For instance, if you would to use a `LogstashSizeRotatingFileHandler`, follow these steps:
 1. Edit your standalone configuration file (`${jboss_folder}/standalone/configuration/standalone.xml` by default) or your domain configuration (according to the one you use).
 1. Look for `<subsystem xmlns="urn:jboss:domain:logging:` tag.
 1. Add a `custom-handler` like this:
```
<custom-handler name="HANDLER_NAME" class="net.logstash.loggers.logstash_jboss_logmanager.LogstashSizeRotatingFileHandler" module="net.logstash.loggers.logstash_jboss_logmanager">
  <properties>
    <property name="fileName" value="${jboss.server.log.dir}/server.log"/>
    <property name="timeZone" value="UTC"/>
    <property name="autoFlush" value="true"/>
    <property name="maxBackupIndex" value="10"/>
    <property name="rotateSize" value="209715200"/>
  </properties>
</custom-handler>
```

On previous example, `server.log` file will show Logstash formatted logs, having UTC time zone, will rotate it by 200MB, and will keep 10 backup files.
