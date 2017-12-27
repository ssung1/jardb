package name.subroutine.jardb.db;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity( name = "archive" )
@NamedQueries( {
@NamedQuery( name = "archive.findByName",
             query = "select a from archive a where a.name = :name" )
} )
public class Archive implements Serializable
{
    @TableGenerator (
        name = "archive",            // content in the pkColumnName
        table = "key_master",        // name of the table to store max id
        pkColumnName = "table_name", // column to store table name
        valueColumnName = "max_oid", // column to store max id
        allocationSize = 100,        // number of ids to increment
        initialValue = 100 )

    @Id
    @Column( name = "oid", nullable = false )
    @GeneratedValue(
        strategy = GenerationType.TABLE,
        generator = "archive" )
    private int oid;

    @Column( name = "directory", length = 128 )
    private String directory;

    @Column( name = "name", length = 128 )
    private String name;

    @Column( name = "size" )
    private long size;

    @Column( name = "last_modified" )
    @Temporal( TemporalType.TIMESTAMP )
    private java.util.Date lastModified;

    /**
     * Get oid.
     *
     * @return oid as int.
     */
    public int getOid()
    {
        return oid;
    }

    /**
     * Set oid.
     *
     * @param oid the value to set.
     */
    public void setOid(int oid)
    {
        this.oid = oid;
    }

    /**
     * Get directory.
     *
     * @return directory as String.
     */
    public String getDirectory()
    {
        return directory;
    }

    /**
     * Set directory.
     *
     * @param directory the value to set.
     */
    public void setDirectory(String directory)
    {
        this.directory = directory;
    }

    /**
     * Get name.
     *
     * @return name as String.
     */
    public String getName()
    {
        return name;
    }

    /**
     * Set name.
     *
     * @param name the value to set.
     */
    public void setName(String name)
    {
        this.name = name;
    }

    /**
     * Get size.
     *
     * @return size as int.
     */
    public long getSize()
    {
        return size;
    }

    /**
     * Set size.
     *
     * @param size the value to set.
     */
    public void setSize( long size )
    {
        this.size = size;
    }

    /**
     * Get lastModified.
     *
     * @return lastModified as Date.
     */
    public Date getLastModified()
    {
        return lastModified;
    }

    /**
     * Set lastModified.
     *
     * @param lastModified the value to set.
     */
    public void setLastModified(Date lastModified)
    {
        this.lastModified = lastModified;
    }
}

