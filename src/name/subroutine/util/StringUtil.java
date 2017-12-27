package name.subroutine.util;

public class StringUtil
{
    /**
     * trims the string; if the string is null, the method returns an empty
     * string
     * 
     * @param str
     *            the string to trim
     * @return trimmed string
     */
    public static String trim( String str )
    {
        if( str == null ) return "";
        return str.trim();
    }
    
    /**
     * checks to see if the string contains valuable information
     */
    public static boolean isSignificant( String str )
    {
        if( str == null ) return false;
        if( str.trim().length() <= 0 ) return false;
        return true;
    }

    /**
     * returns the trimmed substring, or null if the original string
     * is null
     */
    public static String substring( String str, int begin, int end )
        throws IndexOutOfBoundsException
    {
        if( str == null ) return null;
        if( str.length() < begin ) return "";

        if( str.length() <= end ){
            return str.substring( begin ).trim();
        }
        else{
            return str.substring( begin, end ).trim();
        }
    }

    /**
     * returns the trimmed substring, or null if the original string
     * is null
     */
    public static String substring( String str, int begin )
        throws IndexOutOfBoundsException
    {
        if( str == null ) return null;
        if( str.length() < begin ) return "";

        return str.substring( begin ).trim();
    }
    
    public static String newString( char c, int len )
    {
        StringBuffer result = new StringBuffer();
        for( int i = 0; i < len; ++i ){
            result.append( c );
        }
        return result.toString();
    }

    public static String newSpace( int len )
    {
        return newString( ' ', len );
    }

    public static String alignLeft( String str, int minLen, int maxLen,
                                    char fill )
    {
        if( str == null ){
            str = "";
        }
        int strLen = str.length();
        if( strLen < minLen ){
            return str + newString( fill, minLen - strLen );
        }
        else if( strLen > maxLen ){
            return str.substring( 0, maxLen );
        }
        else{
            return str;
        }
    }

    public static String alignLeft( String str, int minLen, int maxLen )
    {
        return alignLeft( str, minLen, maxLen, ' ' );
    }

    public static String alignLeft( String str, int len, char fill )
    {
        return alignLeft( str, len, len, fill );
    }

    public static String alignLeft( String str, int len )
    {
        return alignLeft( str, len, len, ' ' );
    }

    public static String alignRight( String str, int minLen, int maxLen,
                                     char fill )
    {
        if( str == null ){
            str = "";
        }
        int strLen = str.length();
        if( strLen < minLen ){
            return newString( fill, minLen - strLen ) + str;
        }
        else if( strLen > maxLen ){
            return str.substring( strLen - maxLen );
        }
        else{
            return str;
        }
    }

    public static String alignRight( String str, int minLen, int maxLen )
    {
        return alignRight( str, minLen, maxLen, ' ' );
    }

    public static String alignRight( String str, int len, char fill )
    {
        return alignRight( str, len, len, fill );
    }

    public static String alignRight( String str, int len )
    {
        return alignRight( str, len, len, ' ' );
    }
    
    /**
     * quotes a string with the given quote
     * 
     * understands common quote pairs
     * 
     * @param str
     * @return
     */
    public static String quote( String str, char quote )
    {
        StringBuffer result = new StringBuffer();
        result.append( quote );
        result.append( str );
        if( quote == '<' ){
            result.append( '>' );
        }
        else if( quote == '(' ){
            result.append( ')' );
        }
        else if( quote == '[' ){
            result.append( ']' );
        }
        else if( quote == '{' ){
            result.append( '}' );
        }
        else{
            result.append( quote );
        }
        return result.toString();
    }
    
    /**
     * joins an array of strings with the given "glue"
     * 
     * @param strLst
     * @param hinge
     * @return
     */
    public static String join( String[] strLst, String glue )
    {
        StringBuffer result = new StringBuffer();
        for( int i = 0; i < strLst.length; ++i ){
            String str = strLst[i];
            if( str == null ){
                str = "";
            }
            if( i > 0 ){
                result.append( glue );
            }
            result.append( str );
        }
        return result.toString();
    }

}
