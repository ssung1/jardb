package name.subroutine.jardb;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.io.File;
import java.io.FileReader;
import java.net.URL;

import org.mozilla.javascript.*;

/**
 * @author s96612s
 *
 * a class to retrieve configuration stored as a javascript file
 */
public class JsConfig
{
    private String filename;
    private long scriptTime = -1;
    private Scriptable world;
    private static org.apache.log4j.Logger _log =
        org.apache.log4j.Logger.getLogger( JsConfig.class );
    
    public static String getResource( String name )
    {
        // find directory of the class file
        ClassLoader cl = JsConfig.class.getClassLoader();
        URL url = cl.getResource( name );
        return url.getFile();
    }

    public static String version()
    {
        String last_update;
        try{
            URL url = JsConfig.class.getClassLoader().
                      getResource( "name/subroutine/jardb/" +
                                   "JsConfig.class" );

            File f = new File( url.getFile() );
            DateFormat df = new SimpleDateFormat( "MMMM dd, yyyy  HH:mm:ss" );
            last_update = df.format( new Date( f.lastModified() ) );
        }
        catch( Exception ex ){
            last_update = "";
        }

        return "JarDB: Better Jars for Tomorrow   version 0.0.0" + "   " +
               "Slight Heat,   " +
               last_update;
    }

    public JsConfig( String filename )
    {
        org.apache.log4j.BasicConfigurator.configure();
        setFilename( filename );
    }

    public File getInitScript()
    {
        return new File( getResource( getFilename() ) );
    }
    
    public boolean needsUpdate()
    {
        if( getScriptTime() == -1 ) return true;
        if( getScriptTime() < getInitScript().lastModified() ) return true;
        return false;
    }

    public Scriptable getWorld()
    {
        try{
            if( needsUpdate() ){
                Context cx = Context.enter();
                cx.setOptimizationLevel( -1 );

                world = new ImporterTopLevel( cx );

                FileReader r = new FileReader( getInitScript() );
                cx.evaluateReader( world, r,
                                   getInitScript().getAbsolutePath(),
                                   0, null );
                r.close();
                setScriptTime( getInitScript().lastModified() );

                Context.exit();
            }
        }
        catch( Exception ex ){
            _log.error( ex.getMessage(), ex );
            world = null;
        }

        return world;
    }

    public String get( String key )
    {
        return conf( key );
    }
    
    public Object getObject( String key )
    {
        Scriptable world = getWorld();
        Object val;
        val = world.get( key, world );
        if( val == Scriptable.NOT_FOUND ) return null;

        return val;
    }

    public String conf( String key )
    {
        try{
            return Context.toString( getObject( key ) );
        }
        catch( Exception ex ){
            _log.error( ex.getMessage(), ex );
            return null;
        }
    }

    public Scriptable getScriptable( String key )
    {
        try{
            return (Scriptable)getObject( key );
        }
        catch( Exception ex ){
            _log.error( ex.getMessage(), ex );
            return null;
        }
    }
    
    /**
     * @return Returns the filename.
     */
    public String getFilename()
    {
        return filename;
    }
    /**
     * @param filename The filename to set.
     */
    public void setFilename( String filename )
    {
        this.filename = filename;
    }
    /**
     * @return Returns the scriptTime.
     */
    protected long getScriptTime()
    {
        return scriptTime;
    }
    /**
     * @param scriptTime The scriptTime to set.
     */
    protected void setScriptTime( long scriptTime )
    {
        this.scriptTime = scriptTime;
    }
    /**
     * @param world The world to set.
     */
    protected void setWorld( Scriptable world )
    {
        this.world = world;
    }
}
