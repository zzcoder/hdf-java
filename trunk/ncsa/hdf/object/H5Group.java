/****************************************************************************
 * NCSA HDF                                                                 *
 * National Comptational Science Alliance                                   *
 * University of Illinois at Urbana-Champaign                               *
 * 605 E. Springfield, Champaign IL 61820                                   *
 *                                                                          *
 * For conditions of distribution and use, see the accompanying             *
 * java-hdf  COPYING file.                                                  *
 *                                                                          *
 ****************************************************************************/

package ncsa.hdf.object;

import java.util.*;
import ncsa.hdf.hdf5lib.*;
import ncsa.hdf.hdf5lib.exceptions.*;

/**
 * Group is the superclass for HDF4/5 group, inheriting the HObject.
 * <p>
 * Group is an abstract class. Its implementing sub-classes are the H4Group and
 * H5Group. This class includes general information of a group object such as
 * members of a group and common operation on groups for both HDF4 and HDF5.
 * <p>
 * Members of a group may include other groups, datasets or links.
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
        int fid,
        String filename,
        String name,
        String path,
        Group parent,
        long[] oid)
    {
        super (fid, filename, name, path, parent, oid);
    }

    // ***** need to implement from DataFormat *****
    public Object read() throws HDF5Exception
    {
        return null;
    }

    // ***** need to implement from DataFormat *****
    public boolean write()
    {
        return false;
    }

    // ***** implement from DataFormat *****
    public List getMetadata() throws HDF5Exception
    {
        if (attributeList == null)
        {
            int gid = open();
            attributeList = H5Accessory.getAttribute(gid);
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
     * @return true if successful and false otherwise.
     */
    public boolean writeMetadata(Object info) throws HDF5Exception
    {
        Attribute attr = null;

        // only attribute metadata is supported.
        if (info instanceof Attribute)
            attr = (Attribute)info;
        else
            return false;

        boolean b = true;
        String name = attr.getName();
        List attrList = getMetadata();
        boolean attrExisted = attrList.contains(attr);

        int gid = open();
        try {
            b = H5Accessory.writeAttribute(gid, attr, attrExisted);
            // add the new attribute into attribute list
            if (!attrExisted) attrList.add(attr);
        } finally
        {
            close(gid);
        }

        return b;
    }

    /**
     * Deletes an attribute from this dataset.
     * <p>
     * @param info the attribute to delete.
     */
    public boolean removeMetadata(Object info) throws HDF5Exception
    {
        Attribute attr = null;

        // only attribute metadata is supported.
        if (info instanceof Attribute)
            attr = (Attribute)info;
        else
            return false;

        boolean b = true;
        int gid = open();
        try {
            H5.H5Adelete(gid, attr.getName());
        } finally {
            close(gid);
        }

        // delete attribute from the List in memory
        List attrList = getMetadata();
        if (b && attrList != null)
        {
            attrList.remove(attr);
        }

        return b;
    }

    // ***** need to implement from HObejct *****
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

    /**
     * Ends access to a group specified by group_id and releases resources
     * used by it.
     */
    public static boolean close(int gid)
    {
        boolean b = true;

        try
        {
            H5.H5Gclose(gid);
        } catch (HDF5Exception ex)
        {
            b = false;
        }

        return b;
    }
}