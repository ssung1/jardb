package name.subroutine.jardb.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import java.io.File;
import java.io.IOException;

import java.util.Enumeration;

import java.util.zip.ZipFile;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;

import name.subroutine.jardb.JsConfig;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import org.mozilla.javascript.Scriptable;

/**
 * add archive and entries using old-style SQL statements
 */
public class Add
{
    private JsConfig config;
    private int maxEntryOid;
    private int maxArchiveOid;

    private boolean clean;
    private boolean help;

    private Logger log = Logger.getLogger( Add.class );

    protected Connection getConn()
        throws ClassNotFoundException, SQLException
    {
        JsConfig conf = getConfig();
        String className = conf.get( "db_driver" );
        Class.forName( className );

        String url = conf.get( "db_url" );
        return DriverManager.getConnection( url );
    }

    public void clean( Connection conn )
        throws SQLException
    {
        Statement stmt = conn.createStatement();
        stmt.execute( "delete from entry" );
        stmt.execute( "delete from archive" );
    }

    public int getMaxArchiveOid( Connection conn )
        throws SQLException
    {
        PreparedStatement stmt = conn.prepareStatement(
            "select max(oid) " +
            "from archive"
        );

        ResultSet rs = stmt.executeQuery();
        
        int result = 10;
        if( rs.next() ){
            int max = rs.getInt( 1 );
            if( max > result ){
                result = max;
            }
        }

        rs.close();
        stmt.close();

        return result;
    }

    public int getMaxEntryOid( Connection conn )
        throws SQLException
    {
        PreparedStatement stmt = conn.prepareStatement(
            "select max(oid) " +
            "from entry"
        );

        ResultSet rs = stmt.executeQuery();
        
        int result = 10;
        if( rs.next() ){
            int max = rs.getInt( 1 );
            if( max > result ){
                result = max;
            }
        }

        rs.close();
        stmt.close();

        return result;
    }

    public void addEntry( Connection conn, File archive )
        throws ZipException, IOException, SQLException
    {
        ZipFile zip = new ZipFile( archive );
        Enumeration<? extends ZipEntry> entryLst = zip.entries();

        PreparedStatement stmt = conn.prepareStatement(
            "insert into entry( " +
            "    oid, " +
            "    package, " +
            "    name, " +
            "    size, " +
            "    archive " +
            ") " +
            "values (" +
            "    ?, ?, ?, ?, ? " +
            ") "
        );

        while( entryLst.hasMoreElements() ){
            ZipEntry entry = entryLst.nextElement();
            String name = entry.getName();

            int lastSlash = name.lastIndexOf( '/' );
            if( lastSlash >= 0 ){
                String pkg = name.substring( 0, lastSlash );
                String cls = name.substring( lastSlash + 1 );

                if( cls.endsWith( ".class" ) ){
                    int lastDot = cls.lastIndexOf( '.' );
                    if( lastDot >= 0 ){
                        cls = cls.substring( 0, lastDot );
                    }
                }

                pkg = pkg.replace( '/', '.' );
                pkg = truncate( pkg, 128 );

                stmt.setInt( 1, getMaxEntryOid() );
                stmt.setString( 2, pkg );
                stmt.setString( 3, cls );
                stmt.setLong( 4, entry.getSize() );
                stmt.setInt( 5, getMaxArchiveOid() );

                stmt.execute();

                incMaxEntryOid();
            }
        }

        stmt.close();
    }

    public void add( Connection conn, File directory )
        throws SQLException, ZipException, IOException
    {
        PreparedStatement stmt = conn.prepareStatement(
            "insert into archive( " +
            "    oid, " +
            "    directory, " +
            "    name, " +
            "    size, " +
            "    last_modified " +
            ") " +
            "values (" +
            "    ?, ?, ?, ?, ? " +
            ") "
        );

        File[] fileLst = directory.listFiles();

        for( File f : fileLst ){
            if( f.isDirectory() ){
                add( conn, f );
            }

            String name;
            name = f.getName();
            if( !name.endsWith( ".jar" ) &&
                !name.endsWith( ".zip" ) ) continue;

            getLog().info( "scanning: " + f );

            String path = f.getParent();
            path = truncate( path, 128 );

            stmt.setInt( 1, getMaxArchiveOid() ); 
            stmt.setString( 2, path );
            stmt.setString( 3, name );
            stmt.setLong( 4, f.length() );
            stmt.setTimestamp( 5, new Timestamp( f.lastModified() ) );
            stmt.execute();

            addEntry( conn, f );
            incMaxArchiveOid();
        }

        stmt.close();
    }

    public void add()
        throws ClassNotFoundException, SQLException, ZipException,
               IOException
    {
        Connection conn = getConn();

        if( getClean() ){
            clean( conn );
        }
        int maxArchiveOid = getMaxArchiveOid( conn );
        setMaxEntryOid( maxArchiveOid );
        int maxEntryOid = getMaxEntryOid( conn );
        setMaxEntryOid( maxEntryOid );

        Scriptable library = getConfig().getScriptable( "library" );
        Object[] idLst = library.getIds();
        for( Object id : idLst ){
            String archive = (String)library.get( (String)id, library );
            System.out.println( "archive: " + archive );
            add( conn, new File( archive ) );
        }

        conn.close();
    }

    String truncate( String str, int maxLen )
    {
        int len = str.length();
        if( len > maxLen ){
            str = str.substring( len - maxLen );
        } 
        return str;
    }

    public void parseParam( String[] argv )
    {
        for( String param : argv ){
            if( "-c".equals( param ) ){
                setClean( true );
            }
            else if( "--help".equals( param ) ){
                setHelp( true );
            }
        }
    }

    public static void help()
    {
        System.out.println( "usage:" );
        System.out.println( "add [options]" );
        System.out.println();
        System.out.println( "Options:" );
        System.out.println( "    -c clean database before adding" );
    }

    public void setConfig( JsConfig val )
    {
        config = val;
    }

    public JsConfig getConfig()
    {
        return config;
    }

    public void setMaxEntryOid( int maxEntryOid )
    {
        this.maxEntryOid = maxEntryOid;
    }
    
    public int getMaxEntryOid()
    {
        return maxEntryOid;
    }

    public void incMaxEntryOid()
    {
        ++maxEntryOid;
    }

    public void setMaxArchiveOid( int maxArchiveOid )
    {
        this.maxArchiveOid = maxArchiveOid;
    }
    
    public int getMaxArchiveOid()
    {
        return maxArchiveOid;
    }

    public void incMaxArchiveOid()
    {
        ++maxArchiveOid;
    }

    public Logger getLog()
    {
        return log;
    }

    public void setLog( Logger val )
    {
        log = val;
    }

    public boolean getClean()
    {
        return clean;
    }

    public void setClean( boolean val )
    {
        clean = val;
    }

    public boolean getHelp()
    {
        return help;
    }

    public void setHelp( boolean val )
    {
        help = val;
    }

    public static void main( String[] argv )
        throws Exception
    {
        JsConfig config;
        config = new JsConfig( "system.js" );

        String log4j = config.get( "log4j" );
        PropertyConfigurator.configure( JsConfig.getResource( 
            config.get( "log4j" ) ) );

        Add a = new AddJpa();
        // Add a = new Add();
        a.setConfig( config );
        a.parseParam( argv );

        if( a.getHelp() ){
            help();
            return;
        }

        a.add();
    }
}
