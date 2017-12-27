package name.subroutine.jardb.jsf;

import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.io.IOException;

import javax.xml.ws.BindingProvider;

import name.subroutine.jardb.wsclient.Ws;
import name.subroutine.jardb.wsclient.WsService;
import name.subroutine.jardb.db.Archive;
import name.subroutine.jardb.db.Entry;

public class JsfSearchWs extends JsfSearch
{
    String serviceEndpoint;

    public JsfSearchWs()
    {
        super();
        WsService service = new WsService();
        
        Ws proxy = service.getWsPort();

        BindingProvider bp = (BindingProvider)proxy;
        Map<String, Object> context = bp.getRequestContext();
        setServiceEndpoint( 
            (String)context.get( BindingProvider.ENDPOINT_ADDRESS_PROPERTY ) );
    }

    public List<Entry> getResult()
        throws IOException
    {
        WsService service = new WsService();
        
        Ws proxy = service.getWsPort();

        BindingProvider bp = (BindingProvider)proxy;
        Map<String, Object> context = bp.getRequestContext();
        context.put( BindingProvider.ENDPOINT_ADDRESS_PROPERTY,
                     getServiceEndpoint() );

        List<name.subroutine.jardb.wsclient.Entry> retval;
        retval = proxy.search( getSearchTerm() );

        List<Entry> result = new ArrayList<Entry>();
        for( name.subroutine.jardb.wsclient.Entry entry : retval ){
            Entry e = new Entry();
            e.setPackage( entry.getPackage() );
            e.setName( entry.getName() );

            name.subroutine.jardb.wsclient.Archive a;
            a = entry.getArchive();

            Archive arch = new Archive();
            arch.setDirectory( a.getDirectory() );
            arch.setName( a.getName() );

            e.setArchive( arch );
            result.add( e );
        }

        return result;
    }

    /**
     * Get serviceEndpoint.
     *
     * @return serviceEndpoint as String.
     */
    public String getServiceEndpoint()
    {
        return serviceEndpoint;
    }
    
    /**
     * Set serviceEndpoint.
     *
     * @param serviceEndpoint the value to set.
     */
    public void setServiceEndpoint(String serviceEndpoint)
    {
        this.serviceEndpoint = serviceEndpoint;
    }
}

