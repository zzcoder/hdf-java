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

import java.util.*;
import ncsa.hdf.hdflib.*;

/**
 * Group is the superclass for HDF4/5 group, inheriting the HObject.
 * <p>
 * Group is an abstract class. Its implementing sub-classes are the H4Group and
 * H4Group. This class includes general information of a group object such as
 * members of a group and common operation on groups for both HDF4 and HDF5.
 * <p>
 * Members of a group may include other groups, datasets or links.
 * <p>
 * @version 1.0 12/12/2001
 * @author Peter X. Cao, NCSA
 */
public class H4Group extends Group
{
    /**
     * The list of attributes of this data object. Members of the list are
     * instance of Attribute.
     */
    private List attributeList;

     /**
      * The GR interface identifier obtained from GRstart(file_id)
      */
    private int grid;

     /**
      * The SDS interface identifier obtained from SDstart(filename, access)
      */
    private int sdid;

    public H4Group(
        int fid,
        String filename,
        String name,
        String path,
        Group parent,
        long[] oid)
    {
        super (fid, filename, name, path, parent, oid);

        grid = HDFConstants.FAIL;
        sdid = HDFConstants.FAIL;
    }

    // ***** need to implement from DataFormat *****
    public Object read() throws HDFException
    {
        return null;
    }

    // ***** need to implement from DataFormat *****
    public boolean write() throws HDFException
    {
        return false;
    }

    // ***** need to implement from DataFormat *****
    public List getMetadata() throws HDFException
    {
        if (attributeList != null)
        {
            return attributeList;
        }

        if (isRoot())
        {
            // retrieve file annotation, GR and SDS globle attributes
            attributeList = H4Accessory.getFileAnnotation(getFID(), attributeList);
            attributeList = H4Accessory.getGRglobleAttribute(grid, attributeList);
            attributeList = H4Accessory.getSDSglobleAttribute(sdid, attributeList);
        }

        int vgid = open();
        if (vgid <= 0) return attributeList;

        int n = -1;

        try {
            n = HDFLibrary.Vnattrs(vgid);
            if (attributeList == null && n > 0)
                attributeList = new Vector(n, 5);

            boolean b = false;
            String[] attrName = new String[1];
            int[] attrInfo = new int[3];
            for (int i=0; i<n; i++)
            {
                attrName[0] = "";
                try {
                    b = HDFLibrary.Vattrinfo(vgid, i, attrName, attrInfo);
                    // mask off the litend bit
                    attrInfo[0] = attrInfo[0] & (~HDFConstants.DFNT_LITEND);
                } catch (HDFException ex)
                {
                    b = false;
                }

                if (!b) continue;

                long[] attrDims = {attrInfo[1]};
                Attribute attr = new Attribute(attrName[0], attrInfo[0], attrDims);;
                attributeList.add(attr);

                Object buf = H4Accessory.allocateArray(attrInfo[0], attrInfo[1]);
                try {
                    HDFLibrary.Vgetattr(vgid, i, buf);
                } catch (HDFException ex)
                {
                    buf = null;
                }

                if (buf != null)
                {
                    if (attrInfo[0] == HDFConstants.DFNT_CHAR ||
                        attrInfo[0] ==  HDFConstants.DFNT_UCHAR8)
                    {
                        buf = Dataset.byteToString((byte[])buf, attrInfo[1]);
                    }

                    attr.setValue(buf);
                }
            }
        } finally
        {
            close(vgid);
        }

        return attributeList;
    }

    // ***** need to implement from DataFormat *****
    public boolean writeMetadata(Object info) throws HDFException
    {
        return false;
    }

    // ***** need to implement from DataFormat *****
    public boolean removeMetadata(Object info) throws HDFException
    {
        return false;
    }

    // ***** need to implement from HObejct *****
    public int open()
    {
        int vgid = -1;

        try {
            long[] theOID = getOID();
            vgid = HDFLibrary.Vattach(getFID(), (int)theOID[1], "w");
        } catch (HDFException ex)
        {
            vgid = -1;
        }

        return vgid;
    }

    // ***** need to implement from HObejct *****
    public static boolean close(int vgid)
    {
        boolean b = true;

        try {
            HDFLibrary.Vdetach(vgid);
        } catch (Exception ex) {
            b = false;
        }

        return b;
    }

    /**
     * Sets the GR and SD interface identifiers.
     * <p>
     * The GR identifier is returned by GRstart(fid), which initializes the GR
     * interface for the file specified by the parameter. GRstart(fid) is an
     * expensive call. It should be only called once.
     * <p>
     * The SD identifier is returned by SDstart(fname, flag), which initializes the
     * SD interface for the file specified by the parameter. SDstart(fname, flag)
     * is an expensive call. It should be only called once.
     * <p>
     * @param grid the GR interface identifier.
     * @param sdid the SDS interface identifier.
     */
    public void setAccess(int grid, int sdid)
    {
        this.grid = grid;
        this.sdid = sdid;
    }
}
