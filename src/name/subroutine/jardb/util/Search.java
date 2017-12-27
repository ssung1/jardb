package name.subroutine.jardb.util;

import java.util.List;

import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.Query;

import name.subroutine.jardb.JsConfig;
import name.subroutine.jardb.db.Archive;
import name.subroutine.jardb.db.Entry;
import name.subroutine.util.StringUtil;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

/**
 * add archive and entries using JTA
 */
public class Search
{
    private JsConfig config;
    private String searchTerm;
    private boolean help;

    private Logger log = Logger.getLogger( getClass() );

    public void parseParam( String[] argv )
    {
        for( String param : argv ){
            if( "--help".equals( param ) ){
                setHelp( true );
            }
            else{
                setSearchTerm( param );
            }
        }
    }

    public static void help()
    {
        System.out.println( "usage:" );
        System.out.println( "search [name]" );
    }

    public List<Entry> searchEntry( String entryName )
    {
        EntityManagerFactory emf;
        emf = Persistence.createEntityManagerFactory( "jardb_eclipselink" );

        EntityManager em = emf.createEntityManager();

        Query q;
        q = em.createNamedQuery( "entry.findByName" );
        q.setParameter( "name", entryName );
        List<Entry> rs = q.getResultList();

        em.close();
        emf.close();

        return rs;
    }

    public List<Archive> searchArchive( String archiveName )
    {
        EntityManagerFactory emf;
        emf = Persistence.createEntityManagerFactory( "jardb_eclipselink" );

        EntityManager em = emf.createEntityManager();

        Query q;
        q = em.createNamedQuery( "archive.findByName" );
        q.setParameter( "name", archiveName );
        List<Archive> rs = q.getResultList();

        em.close();
        emf.close();

        return rs;
    }

    public void search( String term )
    {
        List<Entry> entryRs = searchEntry( term );
        for( Entry e : entryRs ){
            String name = e.getPackage() + "." + e.getName();
            System.out.println( StringUtil.alignRight( name, 80 ) );
            Archive a = e.getArchive();
            String arch = a.getDirectory() + "/" + a.getName();
            System.out.println( StringUtil.alignRight( arch, 80 ) );
        }
        if( entryRs.size() > 0 ) return;

        List<Archive> archiveRs = searchArchive( term );
        for( Archive a : archiveRs ){
            String arch = a.getDirectory() + "/" + a.getName();
            System.out.println( StringUtil.alignRight( arch, 80 ) );
        }
    }

    public static void main( String[] argv )
        throws Exception
    {
        JsConfig config;
        config = new JsConfig( "system.js" );

        String log4j = config.get( "log4j" );
        PropertyConfigurator.configure( JsConfig.getResource( 
            config.get( "log4j" ) ) );

        Search s = new Search();
        s.setConfig( config );
        s.parseParam( argv );

        if( s.getHelp() ){
            help();
            return;
        }

        s.search( s.getSearchTerm() );
    }

    public Logger getLog()
    {
        return log;
    }

    public void setLog( Logger val )
    {
        log = val;
    }

    public void setConfig( JsConfig val )
    {
        config = val;
    }

    public JsConfig getConfig()
    {
        return config;
    }

    public boolean getHelp()
    {
        return help;
    }

    public void setHelp( boolean val )
    {
        help = val;
    }

    
    /**
     * Get searchTerm.
     *
     * @return searchTerm as String.
     */
    public String getSearchTerm()
    {
        return searchTerm;
    }
    
    /**
     * Set searchTerm.
     *
     * @param searchTerm the value to set.
     */
    public void setSearchTerm(String searchTerm)
    {
        this.searchTerm = searchTerm;
    }
}
