package name.subroutine.jardb.jsf;

import java.io.IOException;
import java.util.List;

import name.subroutine.jardb.util.Search;
import name.subroutine.jardb.db.Entry;

public class JsfSearch
{
    private String searchTerm;
    private boolean hasSearchResult;
    private List<Entry> searchResult;

    public List<Entry> getResult()
        throws IOException
    {
        Search s = new Search();
 
        String term = getSearchTerm();

        List<Entry> entryRs = s.searchEntry( term );
        return entryRs;
    }

    public String handleSubmitEnter()
        throws IOException
    {
        setHasSearchResult( true );
        setSearchResult( getResult() );

        return "";
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
    
    public List<Entry> getSearchResult()
    {
        return searchResult;
    }
    
    /**
     * Set searchResult.
     *
     * @param searchResult the value to set.
     */
    public void setSearchResult( List<Entry> val )
    {
        this.searchResult = val;
    }
    
    /**
     * Get hasSearchResult.
     *
     * @return hasSearchResult as boolean.
     */
    public boolean getHasSearchResult()
    {
        return hasSearchResult;
    }
    
    /**
     * Set hasSearchResult.
     *
     * @param hasSearchResult the value to set.
     */
    public void setHasSearchResult(boolean hasSearchResult)
    {
        this.hasSearchResult = hasSearchResult;
    }
}
