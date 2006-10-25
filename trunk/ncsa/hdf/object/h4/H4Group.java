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

package ncsa.hdf.object.h4;

import java.util.*;
import ncsa.hdf.hdflib.*;
import ncsa.hdf.object.*;

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
	public static final long serialVersionUID = HObject.serialVersionUID;

    /**
     * The list of attributes of this data object. Members of the list are
     * instance of Attribute.
     */
    private List attributeList;

    /** The default object ID for HDF4 objects */
    private final static long[] DEFAULT_OID = {0, 0};

    public H4Group(FileFormat fileFormat, String name, String path, Group parent)
    {
        this(fileFormat, name, path, parent, null);
    }

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
        super (fileFormat, name, path, parent, ((oid == null) ? DEFAULT_OID : oid));

        if (fileFormat instanceof H4File)
        {
            int vgid = open();
            try {  hasAttribute =(HDFLibrary.Vnattrs(vgid)>0);
            } catch (Exception ex) {}
            close(vgid);
        }
    }

    // Implementing DataFormat
    public List getMetadata() throws HDFException
    {
        if (attributeList != null)
        {
            return attributeList;
        } else
            attributeList = new Vector();

        int vgid = open();
        if (vgid <= 0) return attributeList;

        int n = -1;

        try {
            n = HDFLibrary.Vnattrs(vgid);
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
                Attribute attr = new Attribute(attrName[0], new H4Datatype(attrInfo[0]), attrDims);;
                attributeList.add(attr);

                Object buf = H4Datatype.allocateArray(attrInfo[0], attrInfo[1]);
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
    public void writeMetadata(Object info) throws Exception
    {
        // only attribute metadata is supported.
        if (!(info instanceof Attribute))
            return;

        getFileFormat().writeAttribute(this, (Attribute)info, true);

        if (attributeList == null)
            attributeList = new Vector();

        attributeList.add(info);
    }


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

    /** close group access. */
    public void close(int vgid)
    {
        try { HDFLibrary.Vdetach(vgid); }
        catch (Exception ex) { ; }
    }

    /**
     * Creates a new group.
     * @param name the name of the group to create.
     * @param pgroup the parent group of the new group.
     * @return the new group if successful. Otherwise returns null.
     */
    public static H4Group create(String name, Group pgroup)
        throws Exception
    {
        H4Group group = null;
        String fullPath = null;

        if (pgroup == null ||
            name == null)
            return null;

        H4File file = (H4File)pgroup.getFileFormat();

        if (file == null)
            return null;

        String path = HObject.separator;
        if (!pgroup.isRoot())
            path = pgroup.getPath()+pgroup.getName()+HObject.separator;
        fullPath = path +  name;

        int fileid = file.open();
        if (fileid < 0)
            return null;

        int gid = HDFLibrary.Vattach(fileid, -1, "w");
        if (gid < 0)
            return null;

        HDFLibrary.Vsetname(gid, name);
        int ref = HDFLibrary.VQueryref(gid);
        int tag = HDFLibrary.VQuerytag(gid);

        if (!pgroup.isRoot())
        {
            // add the dataset to the parent group
            int pid = pgroup.open();
            if (pid < 0)
                throw (new HDFException("Unable to open the parent group."));

            HDFLibrary.Vinsert(pid, gid);

            pgroup.close(pid);
        }

        try { HDFLibrary.Vdetach(gid); }
        catch (Exception ex) { ; }

        long[] oid = {tag, ref};
        group = new H4Group(file, name, path, pgroup, oid);

        if (group != null)
            pgroup.addToMemberList(group);

        return group;
    }

}
