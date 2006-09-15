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
 * HObject is the root class of all the HDF data objects. Every data class has
 * HObject as a superclass. All objects, Groups and Datasets, implement the
 * methods of this class. The following is the class hierarchy of abstract classes
 * that are inherited from HObject.
 *
 * <pre>
 *                                     HObject
 *                       _________________|_________________
 *                       |                |                |
 *                     Group           Dataset          Datatype
 *                       |         _______|_______         |
 *                       |         |             |         |
 *                       |      ScalarDS     CompoundDS    |
 *                       |         |             |         |
 *      |-------------------------------------------------------------|
 *      |                  Implementing classes such as               |
 *      |              H5Group, H5ScalarDs, H5CompoundDS, H5Datatype  |
 *      |-------------------------------------------------------------|
 * </pre>
 *
 * All HDF4 and HDF5 data objects are inherited from HObject. At the top
 * level of hierarchy, both HDF4 and HDF5 have the same super-classes such as
 * Group and Dataset. At bottom level of hierarchy, HDF4 and HDF5 objects have
 * their own implementation such as H5Group, H5ScalarDs, H5CompoundDS, H5Datatype,
 * and etc.
 * <p>
 * <b>Warning: HDF4 and HDF5 may have multiple links to the same object. Data
 * object in this model does not deal with multiple links. Users may create
 * duplicate copies of the same data object with different pathes. Applications
 * should check the OID of the data object to avoid duplicate copies of the
 * same object.</b>
 * <p>
 *  HDF4 objects are uniquely identified by the OID of the (ref_id, tag_id) pair.
 *  HDF5 objects are uniquely identified by the OID of object reference.
 * <p>
 * @version 1.0 12/12/2001
 * @author Peter X. Cao, NCSA
 * @see <a href="DataFormat.html">ncsa.hdf.object.DataFormat</a>
 */
public abstract class HObject implements Serializable, DataFormat
{
    /**
     * The separator of object path.
     */
    public final static String separator = "/";

    /**
     * The full path of the file that contains this object.
     */
    private final String filename;

    /**
     * The file identifier of this object. The fid is obtained from FileFormat.open().
     */
    private final int fid;

    /**
     * The file which contains this object
     */
    private final FileFormat fileFormat;

    /**
     * The name of the data object. The root group has its default
     * name, a slash. The name can be changed except the root group.
     */
    private String name;

    /**
     * The full path of the data object. The full path always starts with the
     * root, a slash. The path cannot be changed. Also, a path must ended with
     * a slash. For example, /arrays/ints/
     */
    private String path;

    /** The full name of the data object, i.e. "path + name" */
    private String fullName;

    /**
     * Array of long integer storing unique identifier for the object.
     * <p>
     *  HDF4 objects are uniquely identified by the OID of the (ref_id, tag_id) pair.
     *  HDF5 objects are uniquely identified by the OID of object reference.
     */
    protected final long[] oid;

    /* Flag to indicate if the object has any attributes attavhed */
    protected boolean hasAttribute = false;

    /**
     * Constructs an instance of the data object without name and path.
     */
    public HObject()
    {
        this(null, null, null, null);
    }

    /**
     * Constructs an instance of the data object with specific name and path.
     * An HDF data object must have a name.
     *
     * @param fileFormat the file that contains the data object.
     * @param name the name of the data object.
     * @param path the full path of the data object.
     */
    public HObject(FileFormat theFileFormat, String theName, String thePath)
    {
        this(theFileFormat, theName, thePath, null);
    }

    /**
     * Constructs an instance of the data object with specific name and path.
     * An HDF data object must have a name. A data object is uniquely identified
     * by its OID.
     *
     * @param fileFormat the file that contains the data object.
     * @param name the name of the data object.
     * @param path the full path of the data object.
     * @param oid the unique identifier of the data object.
     */
    public HObject(FileFormat theFileFormat, String theName, String thePath, long[] oid)
    {
        this.fileFormat = theFileFormat;
        this.oid = oid;

        if (fileFormat != null) {
            this.fid = fileFormat.getFID();
            this.filename = fileFormat.getFilePath();
        } else {
            this.fid = -1;
            this.filename = null;
        }

        // file name is packed in the full path
        if (theName == null && thePath !=null)
        {
            if (thePath.equals(separator)){
                theName = separator;
                thePath = null;
            } else {
                if (thePath.endsWith(HObject.separator))
                    thePath = thePath.substring(0, thePath.length()-1);
                theName = thePath.substring(thePath.lastIndexOf(HObject.separator)+1);
                thePath = thePath.substring(0, thePath.lastIndexOf(HObject.separator));
            }
        }

        // the path must start and end with "/"
        if (thePath!=null)
        {
            thePath = thePath.replaceAll("//", "/");
            if ( !thePath.endsWith(separator))
                thePath += separator;
        }

        this.name = theName;
        this.path = thePath;

        if (thePath != null)
            this.fullName = thePath + theName;
        else
        {
            if (theName == null) // must be the root group
                this.fullName = "/";
            else if (theName.startsWith("/"))
                this.fullName = theName;
            else
                this.fullName = "/"+theName;
        }
    }

    /**
     * Returns the full path of the file that contains this data object.
     * The file name is necessary because the file of this data object is
     * uniquely identified when mutilple files are opened by an application
     * at the same time.
     */
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
     * Returns the full name of this object.
     */
    public final String getFullName()
    {
        return fullName;
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
     *
     * @param newName The new name of the object.
     */
    public void setName (String newName) throws Exception
    {
        name = newName;
    }

    /**
     * Sets the path of the data object.
     *
     * @param newPath The new path of the object.
     */
    public void setPath (String newPath) throws Exception
    {
        path = newPath;
    }

    /**
     * Opens access to this object.
     * <p>
     * Sub-classes must implement this interface so that different data objects
     * have their own ways of how the data resources are opened. For example,
     * H5Group.open() calls the ncsa.hdf.hdf5lib.H5.H5Gopen() native method and
     * returns the group identifier.
     *
     * @return the interface identifier for access this object.
     */
    public abstract int open();

    /**
     * Closes access to this object.
     * <p>
     * Sub-classes must implement this interface so that different data
     * objects have their own ways of how the data resources are closed.
     * For example, H5Group.close() calls the ncsa.hdf.hdf5lib.H5.H5Gclos()
     * native method and closes the group resource specified by the id.
     *
     * @param id The object identifier.
     */
    public abstract void close(int id);

    /**
     * Returns the file identifier of this data object.
     */
    public final int getFID()
    {
        return fid;
    }

    /**
     * Checks if the OID of this object is the same as the given object identifier.
     * <p>
     * HDF4 and HDF5 data objects are identified by their unique OIDs.
     * A data object in file may have multiple logical names (the full names),
     * which are presented in a tree structure as separate objects.
     * <p>
     * The HObject.equalsOID(long[] theID) can be used to check if two data objects
     * with different names are pointed to the same object in file.
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
            isMatched = (theID[i]==oid[i]);

        return isMatched;
    }

    /**
     * Returns the fileformat which contains this object.
     */
    public final FileFormat getFileFormat() { return fileFormat; }

    /**
     * Returns a cloned copy of the object identifier.
     * <p>
     * The object OID cannot be modified once it is created. getIOD() clones
     * the object OID to ensure the object OID cannot be modified outside of
     * this class.
     *
     * @return the cloned copy of the object OID.
     */
    public final long[] getOID()
    {
        if (oid == null)
            return null;

        return (long[]) oid.clone();
    }

    /**
     * Check if this object has any attributes attached.
     *
     * @return true if it has any attribute(s), false otherwise.
     */
    public boolean hasAttribute () { return hasAttribute; }

    /**
     * Returns the string representation of this data object.
     * <p>
     * The String consists of the name of the data object.
     */
    public String toString()
    {
        return name;
    }
}
