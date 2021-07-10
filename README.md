# FDAF Application Example

This application example shows you how to build efficiently a sample of Java
EE application based [FDAF framework](https://github.com/heru-himawan-tl/fdaf).
This is a quick overview of the most common FDAF starters, along with examples
on how to use the FDAF framework API's and abstractions.

This FDAF application example has tested under these application servers:

- [WildFly V. 24 Final](https://www.wildfly.org/)   
- [Apache Tomee Plume/Plus V. 8.0.6](https://tomee.apache.org/)  

## HOWTO Compile & Run

Since this experiment uses shell script, this guide assumes that you are with
a GNU/Linux-based operating system that already have a MySQL server or a
MariaDB server installed and running on it, an OpenJDK 8 or newer installed on
it, and Apache Maven version 3.6 or later installed on it.

### Compile For & Run Under Apache TomEE Plume/Plus

An Apache TomEE Plume or Plus version 8.0.6 or higher is required for this
test. Download and extract it inside `./fdaf-application-example/fdaf/`
directory, so that you will, for example, get the TomEE installation
directory (or a `$catalina_home`) will be alike as:
`./fdaf-application-example/fdaf/apache-tomee-plume-8.0.6/`.

#### TomEE Data Source & Transaction Manager Configuration

To make this FDAF application example running correctly under Apache TomEE
Plume/Plus 8.0.6 (or higher), you must configure a data source and a
transaction manager for TomEE as shown by the example below:

    <?xml version="1.0" encoding="UTF-8"?>
    <tomee>
        <Resource id="MySqlFDAF" type="DataSource">
            JdbcDriver com.mysql.jdbc.Driver
            JdbcUrl jdbc:mysql://127.0.0.1:3306/fdaf?useUnicode=true&amp;useJDBCCompliantTimezoneShift=true&amp;useLegacyDatetimeCode=false&amp;serverTimezone=UTC
            UserName root
            Password xxxxxxxxx
            JtaManaged true
        </Resource>
        
        <TransactionManager id="myTransactionManager" type="TransactionManager">
            adler32Checksum = true
            bufferSizeKb = 32
            checksumEnabled = true
            defaultTransactionTimeout = 10 minutes
            flushSleepTime = 50 Milliseconds
            logFileDir = txlog
            logFileExt = log
            logFileName = howl
            maxBlocksPerFile = -1
            maxBuffers = 0
            maxLogFiles = 2
            minBuffers = 4
            threadsWaitingForceThreshold = -1
            txRecovery = false
        </TransactionManager>
    </tomee>
    
The above data source configuration example requires a MySQL Connector/J JAR
present in `$catalina_home/lib` directory. Download a MySQL Connector/J from
the [official MySQL development download page](https://dev.mysql.com/downloads/connector/j/),
and put it inside `$catalina_home/lib` directory.

Notice:  
The data source ID must be identical with that defined in
`./fdaf-application-example/fdaf/development-source/io.sourceforge.fdaf/fdaf/develop.properties`:

    fdaf.dataSourceName=MySqlFDAF
    
#### Specific Required TomEE System Properties

Specific TomEE system properties must be set within the `system.properties`:

    org.apache.el.parser.SKIP_IDENTIFIER_CHECK = true
    tomee.serialization.class.whitelist = *

#### Compile & Run

To compile and run, just call the `tomee-run.sh` script from a terminal console.

### Compile To Test Under WildFly Application Server

#### Compiling

To test this FDAF application example under WildFly, the development-source
must be pre-compiled by the call of `compile-test.sh` script with argument
`with-hibernate`:

    ./compile-test.sh with-hibernate
    
The compilation will yield the following items:

- `./fdaf-application-example/fdaf/deployment-test/fdaf-with-hibernate/ear/` -
a directory contains FDAF application example for unmanaged
enterprise-application deployment.

- `./fdaf-application-example/fdaf/deployment-test/fdaf-with-hibernate/webapp/` -
a directory contains FDAF application example for unmanaged
web-application deployment.

- `./fdaf-application-example/fdaf/compilable-source/fdaf-with-hibernate/build/fdaf.ear` -
an EAR archive of FDAF application example for a managed enterprise-application
deployment.
 
- `./fdaf-application-example/fdaf/compilable-source/fdaf-with-hibernate/build/fdaf-webapp.war` -
a WAR archive of FDAF application example for a managed web-application
deployment.

#### Required: Adding MySQL Connector/J Module On WildFly

To run this FDAF application example under WildFly, a WildFly server must
have MySQL Connector/J module resides in
`$WILDFLY_HOME/modules/system/layers/base/com/mysql/main/` with module
configuration e.g:

    <module xmlns="urn:jboss:module:1.5" name="com.mysql">
        <resources>
            <resource-root path="mysql-connector-java-8.0.25.jar" />
        </resources>
        <dependencies>
            <module name="javax.api"/>
            <module name="javax.transaction.api"/>
        </dependencies>
    </module>
    
#### WildFly Data Source & Database Driver Configuration

Current WildFly installation must have MySQL data source and MySQL driver
configuration in `standalone.xml` looks like e.g.:

    <subsystem xmlns="urn:jboss:domain:datasources:6.0">
        <datasources>
            <datasource jndi-name="java:/MySqlFDAF" pool-name="MySqlFDAF" statistics-enabled="true">
                <connection-url>jdbc:mysql://127.0.0.1:3306/fdaf?useUnicode=true&amp;useJDBCCompliantTimezoneShift=true&amp;useLegacyDatetimeCode=false&amp;serverTimezone=UTC</connection-url>
                <driver-class>com.mysql.cj.jdbc.Driver</driver-class>
                <driver>mysql</driver>
                <security>
                    <user-name>root</user-name>
                    <password>debian</password>
                </security>
                <validation>
                    <valid-connection-checker class-name="org.jboss.jca.adapters.jdbc.extensions.mysql.MySQLValidConnectionChecker"/>
                    <background-validation>true</background-validation>
                    <exception-sorter class-name="org.jboss.jca.adapters.jdbc.extensions.mysql.MySQLExceptionSorter"/>
                </validation>
            </datasource>
            ...
            <drivers>
                <driver name="mysql" module="com.mysql">
                    <driver-class>com.mysql.cj.jdbc.Driver</driver-class>
                    <xa-datasource-class>com.mysql.cj.jdbc.MysqlXADataSource</xa-datasource-class>
                </driver>
                ...
            </drivers>
        </datasources>
    </subsystem>
    
Notice:  
The data source JNDI name must be identical with that defined in
`./fdaf-application-example/fdaf/development-source/io.sourceforge.fdaf/fdaf/develop.properties`:

    fdaf.dataSourceName=MySqlFDAF
    
#### Runtime Naming Convention Of Application Deployment Under WildFly

In order the web-application part to be working properly under WildFly, the
correct runtime name must be applied to EAR part of this FDAF application
example deployment, as `fdaf.ear`.
