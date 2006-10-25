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

package ncsa.hdf.object.nc2;

import java.util.*;
import ncsa.hdf.object.*;
import ucar.nc2.*;

/**
 * An H5Group represents HDF5 group, inheriting from Group.
 * Every HDF5 object has at least one name. An HDF5 group is used to store
 * a set of the names together in one place, i.e. a group. The general
 * structure of a group is similar to that of the UNIX file system in
 * that the group may contain references to other groups or data objects
 * just as the UNIX directory may contain subdirectories or files.
 * <p>
 * @version 1.0 12/12/2001
 * @author Peter X. Cao, NCSA
 */
public class NC2Group extends Group
{
	public static final long serialVersionUID = HObject.serialVersionUID;

    /**
     * The list of attributes of this data object. Members of the list are
     * instance of Attribute.
     */
    private List attributeList;

    /** The default object ID for HDF5 objects */
    private final static long[] DEFAULT_OID = {0};

    /**
     * Constructs an HDF5 group with specific name, path, and parent.
     * <p>
     * @param fileFormat the file which containing the group.
     * @param name the name of this group.
     * @param path the full path of this group.
     * @param parent the parent of this group.
     * @param oid the unique identifier of this data object.
     */
    public NC2Group(
        FileFormat fileFormat,
        String name,
        String path,
        Group parent,
        long[] theID) {
        super (fileFormat, name, path, parent, ((theID == null) ? DEFAULT_OID : theID));
    }

    // Implementing DataFormat
    public List getMetadata() throws Exception
    {
        if (!isRoot()) return null;

        if (attributeList != null)
            return attributeList;

        NC2File theFile = (NC2File)getFileFormat();
        NetcdfFile ncFile = theFile.getNetcdfFile();

        List netcdfAttributeList = ncFile.getGlobalAttributes();
        if (netcdfAttributeList == null)
            return null;

        int n = netcdfAttributeList.size();
        attributeList = new Vector(n);

        ucar.nc2.Attribute netcdfAttr = null;
        for (int i=0; i<n; i++) {
            netcdfAttr = (ucar.nc2.Attribute)netcdfAttributeList.get(i);
            attributeList.add(NC2File.convertAttribute(netcdfAttr));
        }

        return attributeList;
    }

    /**
     * Creates a new attribute and attached to this dataset if attribute does
     * not exist. Otherwise, just update the value of the attribute.
     *
     * <p>
     * @param info the atribute to attach
     */
    public void writeMetadata(Object info) throws Exception {
        // not supported
        throw new UnsupportedOperationException("Unsupported operation for NetCDF.");
    }

    /**
     * Deletes an attribute from this dataset.
     * <p>
     * @param info the attribute to delete.
     */
    public void removeMetadata(Object info) throws Exception {
        // not supported
        throw new UnsupportedOperationException("Unsupported operation for NetCDF.");
    }

    // Implementing DataFormat
    public int open() {
        // not supported
        throw new UnsupportedOperationException("Unsupported operation for NetCDF.");
    }

    /** close group access */
    public void close(int gid) {
        // not supported
        throw new UnsupportedOperationException("Unsupported operation for NetCDF.");
    }

    /**
     * Creates a new group.
     * @param file the file which the group is added to.
     * @param name the name of the group to create.
     * @param pgroup the parent group of the new group.
     * @return the new group if successful. Otherwise returns null.
     */
    public static NC2Group create(String name, Group pgroup)
        throws Exception {
        // not supported
        throw new UnsupportedOperationException("Unsupported operation for NetCDF.");
    }

}
