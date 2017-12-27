package name.subroutine.jardb.ws;

import java.util.List;

import javax.jws.*;

import name.subroutine.jardb.JsConfig;
import name.subroutine.jardb.db.Entry;
import name.subroutine.jardb.util.Search;

@javax.jws.WebService( name = "ws",
    serviceName = "ws",
    targetNamespace = "http://subroutine.name/jardb" )
// @SOAPBinding( style = SOAPBinding.Style.DOCUMENT,
//     use = SOAPBinding.Use.LITERAL,
//     parameterStyle = SOAPBinding.ParameterStyle.WRAPPED )
public class WebService
{
    @WebMethod
    public String getVersion()
    {
        return JsConfig.version();
    }

    @WebMethod
    public List<Entry> search( String term )
    {
        Search s = new Search();
 
        List<Entry> entryRs = s.searchEntry( term );

        return entryRs;
    }
}
