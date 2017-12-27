package name.subroutine.jardb.util;

import java.sql.SQLException;

import java.io.IOException;
import java.io.File;

import java.util.Enumeration;
import java.util.List;

import java.util.zip.ZipFile;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;

import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.Query;

import name.subroutine.jardb.db.Archive;
import name.subroutine.jardb.db.Entry;
import org.apache.log4j.Logger;

import org.mozilla.javascript.Scriptable;

/**
 * add archive and entries using JTA
 */
public class AddJpa extends Add
{
    private Logger log = Logger.getLogger( AddJpa.class );

    private EntityManagerFactory emFactory;
    private EntityManager em;

    public void addEntry( EntityManager em, File file, Archive arch )
        throws ZipException, IOException, SQLException
    {
        ZipFile zip = new ZipFile( file );
        Enumeration<? extends ZipEntry> entryLst = zip.entries();

        while( entryLst.hasMoreElements() ){
            ZipEntry entry = entryLst.nextElement();
            String name = entry.getName();

            int lastSlash = name.lastIndexOf( '/' );
            if( lastSlash >= 0 ){
                String pkg = name.substring( 0, lastSlash );
                String cls = name.substring( lastSlash + 1 );

                if( cls.length() <= 0 ) continue;

                if( cls.endsWith( ".class" ) ){
                    int lastDot = cls.lastIndexOf( '.' );
                    if( lastDot >= 0 ){
                        cls = cls.substring( 0, lastDot );
                    }
                }

                pkg = pkg.replace( '/', '.' );
                pkg = truncate( pkg, 128 );

                Entry e = new Entry();
                e.setPackage( pkg );
                e.setName( cls );
                e.setSize( entry.getSize() );
                e.setArchive( arch );

                em.persist( e );
            }
        }
    }

    public void add( File directory )
        throws ClassNotFoundException, SQLException, ZipException,
               IOException
    {
        File[] fileLst = directory.listFiles();

        for( File f : fileLst ){
            if( f.isDirectory() ){
                add( f );
            }

            String name;
            name = f.getName();
            if( !name.endsWith( ".jar" ) &&
                !name.endsWith( ".zip" ) ) continue;

            getLog().info( "scanning: " + f );

            String path = f.getParent();
            path = truncate( path, 128 );

            // keep each EntityManager session short to avoid
            // StackOverflowException
            // EntityManager em = getEmFactory().createEntityManager();
            getEm().getTransaction().begin();
            // em.getTransaction().begin();

            Archive archive = new Archive();
            archive.setDirectory( path );
            archive.setName( name );
            archive.setSize( f.length() );
            archive.setLastModified( new java.util.Date( f.lastModified() ) );
            em.persist( archive );

            addEntry( em, f, archive );
            getEm().getTransaction().commit();
            // looks like .commit does not release resources
            // em.getTransaction().commit();
            // must use .close on the EntityManager
            // em.close();
        }
    }

    /**
     * @deprecated
     * not used because it did not resolve the stack overflow error
     */
    public void add( List<File> archiveLst )
        throws ClassNotFoundException, SQLException, ZipException,
               IOException
    {
        for( File f : archiveLst ){
            String name;
            name = f.getName();
            getEm().getTransaction().begin();

            getLog().info( "scanning: " + f );

            String path = f.getParent();
            path = truncate( path, 128 );

            Archive archive = new Archive();
            archive.setDirectory( path );
            archive.setName( name );
            archive.setSize( f.length() );
            archive.setLastModified( new java.util.Date( f.lastModified() ) );
            em.persist( archive );

            addEntry( em, f, archive );

            getEm().getTransaction().commit();
        }
    }

    /**
     * @deprecated
     * not used because it did not resolve the stack overflow error
     */
    public void addArchive( List<File> dst, File directory )
    {
        File[] fileLst = directory.listFiles();

        for( File f : fileLst ){
            if( f.isDirectory() ){
                addArchive( dst, f );
            }

            String name;
            name = f.getName();
            if( !name.endsWith( ".jar" ) &&
                !name.endsWith( ".zip" ) ) continue;

            dst.add( f );
        }
    }

    public void clean( EntityManager em, String table )
    {
        try{
            em.getTransaction().begin();

            Query q;
            q = em.createQuery( "delete from " + table );
            q.executeUpdate();

            em.getTransaction().commit();
        }
        catch( Exception ex ){
        }
    }

    public void clean()
    {
        EntityManager em = getEm();
        clean( em, "entry" );
        clean( em, "archive" );
    }

    public void add()
        throws ClassNotFoundException, SQLException, ZipException,
               IOException
    {
        initEntityManager();

        try{
            if( getClean() ){
                clean();
            }

            Scriptable libraryLst = getConfig().getScriptable( "library" );
            Object[] idLst = libraryLst.getIds();
            for( Object id : idLst ){
                String library = (String)libraryLst.get(
                    (String)id, libraryLst );
                System.out.println( "library: " + library );

                // List<File> archiveLst = new ArrayList<File>();
                // addArchive( archiveLst, new File( library ) );
                // add( archiveLst );
                add( new File( library ) );
            }
        }
        finally{
            closeEntityManager();
        }
    }

    public void initEntityManager()
    {
        setEmFactory( Persistence.createEntityManagerFactory( "jardb_eclipselink" ) );
        setEm( getEmFactory().createEntityManager() );
    }

    public void closeEntityManager()
    {
        getEm().close();
        getEmFactory().close();
    }

    public Logger getLog()
    {
        return log;
    }

    public void setLog( Logger val )
    {
        log = val;
    }
    
    /**
     * Get emFactory.
     *
     * @return emFactory as EntityManagerFactory.
     */
    public EntityManagerFactory getEmFactory()
    {
        return emFactory;
    }
    
    /**
     * Set emFactory.
     *
     * @param emFactory the value to set.
     */
    public void setEmFactory(EntityManagerFactory emFactory)
    {
        this.emFactory = emFactory;
    }
    
    /**
     * Get em.
     *
     * @return em as EntityManager.
     */
    public EntityManager getEm()
    {
        return em;
    }
    
    /**
     * Set em.
     *
     * @param em the value to set.
     */
    public void setEm(EntityManager em)
    {
        this.em = em;
    }
}
