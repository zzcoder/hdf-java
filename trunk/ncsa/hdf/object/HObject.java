/****************************************************************************
 * NCSA HDF                                                                 *
 * National Comptational Science Alliance                                   *
 * University of Illinois at Urbana-Champaign                               *
 * 605 E. Springfield, Champaign IL 61820                                   *
 *                                                                          *
 * For conditions of distribution and use, see the accompanying             *
 * hdf-java/COPYING file.                                                   *
 *                                                                          *
 ****************************************************************************/

package ncsa.hdf.object;

import java.io.*;
import java.util.*;

/**
 * HObject is the superclass for HDF data hierarchy, inheriting the DataFormat
 * interface. HObject an abstract class. It includes general and common fields
 * and operation for all HDF4 and HDF5 data objects.
 * <p>
 * All the HDF4 and HDF5 data objects are inherited from HObject. At the top
 * level of hierarchy, both HDF4 and HDF5 have the same super-classes such Group,
 * and Dataset. At bottom level of hierarchy, HDF4 and HDF5 objects have their
 * own implementation such H4Vdata, H4SDS, H4GRImage, H5ScalarDS, H5CompoundDS,
 * and etc.
 * <p>
 * <b>Warning: HDF4 and HDF5 may have multiple links to the same object. Data
 * object in this model does not deal with multiple links. Users may create
 * duplicate copies of the same data object with different path. Applications
 * should check the OID of the data object to avoid duplicate copies of the
 * same object.</b>
 * <p>
 *  HDF4 objects are uniquely identified by the (ref_id, tag_id) pairs.
 *  HDF5 objects uniquely identified by the reference identifier.
 * <p>
 * @version 1.0 12/12/2001
 * @author Peter X. Cao, NCSA
 * @see ncsa.hdf.object.DataFormat
 */
public abstract class HObject
implements Serializable, DataFormat
{
    /**
     * The path separator of HDF4 and HDF5 objects.
     */
    public final static String separator = "/";

    /**
     * The full path of the file that contains this object.
     */
    private final String filename;

    /**
     * The file identifier.
     */
    private final int fid;

    /**
     * The name of the HDF4 or HDF5 data object. The root group has its default
     * name, a slash. The name can be changed except the root group.
     */
    private String name;

    /**
     * The full path of the HDF4 or HDF5 data object. The full path always starts
     * with the root, a slash. The path cannot be changed. Also, a path must
     * ended with a slash.
     */
    private final String path;

    /**
     * array of long integer storing unique identifier for each HDF object.
     * <p>
     * HDF4 objects are uniquely identified by the (tag_id, ref_id) pairs.
     * HDF5 objects uniquely identified by the reference identifier.
     */
    protected final long[] oid;

    /**
     * Constructs an instance of the data object with specific name and path.
     * An HDF data object must have a name. A data object is uniquely identified
     * by its full name (the full path + the name of the object) and the file
     * that contains the data object.
     * <p>
     * @param fileFormat the HDF file.
     * @param name the name of the data object.
     * @param path the full path of the data object.
     * @param oid the unique identifier of this data object.
     */
    public HObject(FileFormat fileFormat, String name, String path, long[] oid)
    {
	int theFID = -1;
        try { theFID = fileFormat.open(); }
        catch (Exception ex) { theFID = -1; }

	this.fid = theFID;

        this.filename = fileFormat.getFilePath();
        this.name = name;

        if (path!=null && !path.endsWith(separator))
            path += separator;

        this.path = path;
        this.oid = oid;
    }

    public final String getFile()
    {
        return filename;
    }

    /**
     * Returns the name of this object.
     */
    public final String getName()
    {
        return name;
    }

    /**
     * Returns the full path of this object.
     */
    public final String getPath()
    {
        return path;
    }

    /**
     * Sets the name of the data object.
     * <p>
     * @param newName the new name of the object.
     */
    public final void rename (String newName)
    {
        name = newName;
    }

    /**
     * Opens access to this object.
     * <p>
     * Sub-classes have to implement this interface so that different data
     * objects have their own ways of how the data resources are opened.
     * <p>
     * @return the interface identifier for access this object.
     */
    public abstract int open();

    /**
     * Returns the file identifier of this data object.
     */
    public final int getFID()
    {
        return fid;
    }

    /**
     * Check if this object has the given object identifier.
     */
    public final boolean equalsOID(long[] theID)
    {
        if (theID == null || oid == null)
            return false;

        int n1 = theID.length;
        int n2 = oid.length;

        if (n1 != n2 )
            return false;

        boolean isMatched = (theID[0]==oid[0]);
        for (int i=1; isMatched && i<n1; i++)
        {
            isMatched = (theID[i]==oid[i]);
        }

        return isMatched;
    }

    /**
     * Returns the string representation of this data object.
     * The String consists of the name and path of the data object.
     */
    public String toString()
    {
        return "[Type: "+this.getClass().getName()+
            "], [Name: "+name+"], [Path: "+path+"]";
    }
}
