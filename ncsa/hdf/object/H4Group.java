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
 * An H4Group is a vgroup in HDF4, inheriting from Group.
 * A vgroup is a structure designed to associate related data objects. The
 * general structure of a vgroup is similar to that of the UNIX file system in
 * that the vgroup may contain references to other vgroups or HDF data objects
 * just as the UNIX directory may contain subdirectories or files.
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

    /**
     * Creates a group object with specific name, path, and parent.
     * <p>
     * @param fileFormat the HDF file.
     * @param name the name of this group.
     * @param path the full path of this group.
     * @param parent the parent of this group.
     * @param oid the unique identifier of this data object.
     */
    public H4Group(
        FileFormat fileFormat,
        String name,
        String path,
        Group parent,
        long[] oid)
    {
        super (fileFormat, name, path, parent, oid);

        if (fileFormat instanceof H4File)
        {
            this.grid = ((H4File)fileFormat).getGRAccessID();
            this.sdid = ((H4File)fileFormat).getSDAccessID();
        }
    }

    // To do: Implementing DataFormat
    public Object read() throws HDFException
    {
        return null;
    }

    // To do: Implementing DataFormat
    public void write() throws HDFException { ; }

    // Implementing DataFormat
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

    // To do: implementing DataFormat
    public void writeMetadata(Object info) throws HDFException {;}

   // To do: implementing DataFormat
    public void removeMetadata(Object info) throws HDFException {;}

   // Implementing HObject
    public int open()
    {
        int vgid = -1;

        // try to open with write permission
        try {
            vgid = HDFLibrary.Vattach(getFID(), (int)oid[1], "w");
        } catch (HDFException ex)
        {
            vgid = -1;
        }

        // try to open with read-only permission
        if (vgid < 0)
        {
            try {
                vgid = HDFLibrary.Vattach(getFID(), (int)oid[1], "r");
            } catch (HDFException ex)
            {
                vgid = -1;
            }
        }

        return vgid;
    }

  // Implementing HObject
    public static void close(int vgid)
    {
        try { HDFLibrary.Vdetach(vgid); }
        catch (Exception ex) { ; }
    }

}
