package name.subroutine.jardb.db;

import javax.persistence.*;
import java.io.Serializable;

@Entity( name = "entry" )
@NamedQueries( {
@NamedQuery( name = "entry.findByName",
             query = "select e from entry e where e.name = :name" )
} )
public class Entry implements Serializable
{
    @TableGenerator (
        name = "entry",      // content in the pkColumnName
        table = "key_master",        // name of the table to store max id
        pkColumnName = "table_name", // column to store table name
        valueColumnName = "max_oid", // column to store max id
        allocationSize = 100,        // number of ids to increment
        initialValue = 100 )

    @Id
    @Column( name = "oid", nullable = false )
    @GeneratedValue(
        strategy = GenerationType.TABLE,
        generator = "entry" )
    private int oid;

    @Column( name = "package", length = 128 )
    private String pkg;

    @Column( name = "name", length = 128 )
    private String name;

    @Column( name = "size" )
    private long size;

    // @Column( name = "archive", nullable = false )
    // private int archive;
    @ManyToOne
    Archive archive;

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
     * Get pkg.
     *
     * @return pkg as String.
     */
    public String getPackage()
    {
        return pkg;
    }

    /**
     * Set pkg.
     *
     * @param pkg the value to set.
     */
    public void setPackage(String pkg)
    {
        this.pkg = pkg;
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
     * Get archive.
     *
     * @return archive as int.
     */
    public Archive getArchive()
    {
        return archive;
    }

    /**
     * Set archive.
     *
     * @param archive the value to set.
     */
    public void setArchive( Archive archive )
    {
        this.archive = archive;
    }
}


