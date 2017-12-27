importClass( java.io.File );
importClass( java.lang.System );

// used by non-JPA database access, but to use non-JPA, Add.java
// needs to be modified
//
// db_driver = "org.apache.derby.jdbc.ClientDriver"
// db_url = "jdbc:derby://localhost:9999/c:/demo/jardb/db"
// db_user = "spongebob"
// db_password = "squarepants"

// when adding jars, these are the directories that will get scanned
library = {
   "glassfish" : "c:/glassfish3/glassfish/modules",
}

log4j = "log4j.properties"
