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

package ncsa.hdf.object.h5;

import java.util.*;
import ncsa.hdf.hdf5lib.*;
import ncsa.hdf.hdf5lib.exceptions.*;
import ncsa.hdf.object.*;

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
public class H5Group extends Group
{
    /**
     * The list of attributes of this data object. Members of the list are
     * instance of Attribute.
     */
    private List attributeList;

    public H5Group(
        FileFormat fileFormat,
        String name,
        String path,
        Group parent,
        long[] oid)
    {
        super (fileFormat, name, path, parent, oid);
    }

    // Implementing DataFormat
    public List getMetadata() throws HDF5Exception
    {
        if (attributeList == null)
        {
            int gid = open();
            attributeList = H5File.getAttribute(gid);
            close(gid);
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
    public void writeMetadata(Object info) throws Exception
    {
        // only attribute metadata is supported.
        if (!(info instanceof Attribute))
            return;

        boolean attrExisted = false;
        Attribute attr = (Attribute)info;
        String name = attr.getName();

        if (attributeList == null)
            attributeList = new Vector();
        else
            attrExisted = attributeList.contains(attr);

        getFileFormat().writeAttribute(this, attr, attrExisted);
        // add the new attribute into attribute list
        if (!attrExisted) attributeList.add(attr);
    }

    /**
     * Deletes an attribute from this dataset.
     * <p>
     * @param info the attribute to delete.
     */
    public void removeMetadata(Object info) throws HDF5Exception
    {
        // only attribute metadata is supported.
        if (!(info instanceof Attribute))
            return;

        Attribute attr = (Attribute)info;
        int gid = open();
        try {
            H5.H5Adelete(gid, attr.getName());
            List attrList = getMetadata();
            attrList.remove(attr);
        } finally {
            close(gid);
        }
    }

    // Implementing DataFormat
    public int open()
    {
        int gid = -1;

        try
        {
            if (isRoot())
                gid = H5.H5Gopen(getFID(), separator);
            else
                gid = H5.H5Gopen(getFID(), getPath()+getName());

        } catch (HDF5Exception ex)
        {
            gid = -1;
        }

        return gid;
    }

    /** close group access */
    public void close(int gid)
    {
        try { H5.H5Gclose(gid); }
        catch (HDF5Exception ex) {;}
    }

    /**
     * Creates a new group.
     * @param file the file which the group is added to.
     * @param name the name of the group to create.
     * @param pgroup the parent group of the new group.
     * @return the new group if successful. Otherwise returns null.
     */
    public static H5Group create(FileFormat file, String name, Group pgroup)
        throws Exception
    {
        H5Group group = null;
        String fullPath = null;

        if (file == null ||
            name == null ||
            pgroup == null)
            return null;

        String path = HObject.separator;
        if (!pgroup.isRoot())
            path = pgroup.getPath()+pgroup.getName()+HObject.separator;
        fullPath = path +  name;

         // create a new group and add ot to the parent node
        int gid = H5.H5Gcreate(file.open(), fullPath, -1);
        byte[] ref_buf = H5.H5Rcreate(
            file.open(),
            fullPath,
            HDF5Constants.H5R_OBJECT,
            -1);
        long l = HDFNativeData.byteToLong(ref_buf, 0);
        long[] oid = {l};

        group = new H5Group(file, name, path, pgroup, oid);

        return group;
    }

}
