# JarDB
##### Dec 27, 2017

This is an application that indexes the classes in your jar files.  This 
was first written back in 2009 when dependency management was still not 
fully matured.

This is an example of SOAP with JPA, plus JSF.  The build tool is Ant.

This application has been known to work with Java 7 and GlassFish 3.1.1.

### Building

- Install GlassFish or some other JEE container.

- Install Ant.

- Modify build.xml and runjava.sh to point to the javaee.jar in GlassFish.

- Build

```
ant webapp
```

### Deploying

- Start Apache Derby

```
cd c:\glassfish3\javadb
bin\startNetworkServer -p 9999 -noSecurityManager
```

- Edit conf/log4j.properties to set the location of log files.

- Edit web/WEB-INF/classes/META-INF/persistence.xml to set the location of the
database (for JPA style persistence).  This can be any empty directory if 
starting for the first time, but in the connection string (if using Derby),
add ";create=true".

- Edit conf/system.js to set the location of the of the database (for non-JPA
style persistence) and the directories with jars to index.

- Now use add.sh to scan some jars.

- Use search.sh to find some classes.

- set JAVA_HOME.

- Start GlassFish

```
cd c:\glassfish3
bin\asadmin start-domain domain1
bin\asadmin deploy --name jardb --contextroot jardb {path-to-jardb}\web
```

- GlassFish 3.1.1 uses a default port of 8080, so go to
`http://localhost:8080/jardb` to use the application.
