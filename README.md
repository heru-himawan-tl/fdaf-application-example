# FDAF Application Example

This application example shows you how to build efficiently a sample of Java
EE application based [FDAF framework](https://github.com/heru-himawan-tl/fdaf).
This is a quick overview of the most common FDAF starters, along with examples
on how to use the FDAF framework API's and abstractions.

This FDAF application example has tested under these application servers:

- [WildFly V. 24 Final](https://www.wildfly.org/)   
- [Apache Tomee Plume V. 8.0.6](https://tomee.apache.org/)  

Notice:  
Due [Apache Tomee Plus](https://tomee.apache.org/) applies OpenJPA
and MyFaces, it prevents FDAF application for working properly.

## HOWTO Compile & Run

Since this experiment uses shell script, this guide assumes that you are with
a GNU/Linux-based operating system that already have a MySQL server or a
MariaDB server installed and running on it, an OpenJDK 8 or newer installed on
it, and Apache Maven version 3.6 or later installed on it.

### Compile For & Run Under Apache TomEE Plume

An Apache TomEE Plume version 8.0.6 or higher is required for this
test. Download and extract it inside `./fdaf-application-example/fdaf/`
directory, so that you will, for example, get the TomEE installation
directory (or a `$CATALINA_HOME`) will be alike as:
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
    </tomee>
    
The above data source configuration example requires a MySQL Connector/J JAR
present in `$CATALINA_HOME/lib` directory. Download a MySQL Connector/J from
the [official MySQL development download page](https://dev.mysql.com/downloads/connector/j/),
and put it inside `$CATALINA_HOME/lib` directory.

Notice:  
The data source ID must be identical with that defined in
`./fdaf-application-example/fdaf/development-source/io.sourceforge.fdaf/fdaf/develop.properties`:

        fdaf.dataSourceName=MySqlFDAF
    
#### Specific Required TomEE System Properties

Specific TomEE system properties must be set within the `system.properties`:

    org.apache.el.parser.SKIP_IDENTIFIER_CHECK = true
    tomee.serialization.class.whitelist = *
    
Disable or commented the serialization class blacklist:

    # tomee.serialization.class.blacklist = *

#### Compile & Run

To compile and run, just call the `tomee-run.sh` script from a terminal
console, apply `--compile-test-multi-packs` or `--compile-test-single-tiear`
option if not already compiled:

- Compile as separated EAR and WAR, deploy and run:

        ./tomee-run.sh --compile-test-multi-packs  

- Compile as single WAR archive, deploy and run:

        ./tomee-run.sh --compile-test-single-pack
    
If already compiled, e.g. to test which best TomEE configurations for
running this FDAF application example, you may call `tomee-run.sh`
without an option:

        ./tomee-run.sh
    
or with `--multi-packs` or `--single-pack` option:

- Run with separated EAR and WAR deployment:

        ./tomee-run.sh --multi-packs  

- Run with single WAR deployment:

        ./tomee-run.sh --single-pack

The web application can be accessed from a browser program such as
Firefox with URL: `http://localhost:8080/fdaf-webapp/`

Notice:  
Due the design of Apache TomEE, it is recommended to apply single WAR archive
deployment, whether by apply `--compile-test-single-pack` or `--single-pack`
option.

### Compile To Test Under WildFly Application Server

#### Compiling

To test this FDAF application example under WildFly, the development-source
can be pre-compiled by the call of `compile-test.sh` script with argument
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

Another choice, by the call of `compile-test.sh` script with argument
`with-hibernate-in-single-war`, to yield single WAR application with business
logic package (`fdaf-logic.jar`) packaged in `WEB-INF/lib`:

        ./compile-test.sh with-hibernate-in-single-war

The single WAR compilation will yield the following items:

- `./fdaf-application-example/fdaf/deployment-test/fdaf-with-hibernate-in-single-war/webapp/` -
a directory contains FDAF application example as an exploded single WAR for
unmanaged web-application deployment.
 
- `./fdaf-application-example/fdaf/compilable-source/fdaf-with-hibernate-in-single-war/build/fdaf-webapp.war` -
a WAR archive that contains EJB JAR of `fdaf-logic-jar` of FDAF application
example for a managed web-application deployment.

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
                    <password>xxxxxxxxx</password>
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

To test or deploy this FDAF application example under WildFly, you
must follow the following runtime naming convention, as it is an FDAF
framework standard:

- If deployment applies separation between enterprise application and web
application, in order the web application part to be working properly, the
correct runtime name must be applied to EAR part. This FDAF application
example EAR deployment must have runtime name as `fdaf.ear`.  

- If deployment applies single WAR web application, in order the web
application part to be working properly, the correct runtime name must
be applied to it. The single WAR deployment runtime name of this
FDAF application must be `fdaf-webapp.war`.   


