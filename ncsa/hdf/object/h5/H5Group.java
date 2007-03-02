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
 * An H5Group object represents an existing HDF5 group in file.
 * <p>
 * In HDF5, every object has at least one name. An HDF5 group is used to store
 * a set of the names together in one place, i.e. a group. The general
 * structure of a group is similar to that of the UNIX file system in
 * that the group may contain references to other groups or data objects
 * just as the UNIX directory may contain subdirectories or files.
 * <p>
 * For more information on HDF5 Groups, 
 * @see <a href="http://hdfgroup.com/HDF5/doc/UG/">HDF5 User's Guide</a>
 * <p>
 * @version 1.0 12/12/2001
 * @author Peter X. Cao, NCSA
 */
public class H5Group extends Group
{
	public static final long serialVersionUID = HObject.serialVersionUID;

    /**
     * The list of attributes of this data object. Members of the list are
     * instance of Attribute.
     */
    protected List attributeList;

    /** The default object ID for HDF5 objects */
    public final static long[] DEFAULT_OID = {0};

    
    /**
     * Constructs an HDF5 group with specific name, path, and parent.
     * <p>
     * @param fileFormat the file which containing the group.
     * @param name the name of this group.
     * @param path the full path of this group.
     * @param parent the parent of this group.
     */
    public H5Group(FileFormat fileFormat, String name, String path, Group parent)
    {
        this(fileFormat, name, path, parent, null);
    }

    /**
     * Constructs an HDF5 group with specific name, path, and parent.
     * <p>
     * @param fileFormat the file which containing the group.
     * @param name the name of this group.
     * @param path the full path of this group.
     * @param parent the parent of this group.
     * @param oid the unique identifier of this data object.
     */
    public H5Group(
        FileFormat fileFormat,
        String name,
        String path,
        Group parent,
        long[] oid)
    {
        super (fileFormat, name, path, parent, ((oid == null) ? DEFAULT_OID : oid));

        if (oid == null && fileFormat != null) {
            // retrieve the object ID
            try {
                byte[] ref_buf = H5.H5Rcreate(fileFormat.getFID(), this.getFullName(), HDF5Constants.H5R_OBJECT, -1);
                this.oid = new long[1];
                this.oid[0] = HDFNativeData.byteToLong(ref_buf, 0);
             } catch (Exception ex) {}
        }

        int gid = open();
        try { 
            hasAttribute = (H5.H5Aget_num_attrs(gid)>0);
            long[] nmembers = {0};
            H5.H5Gget_num_objs(gid, nmembers);
            nMembersInFile = (int)nmembers[0];
        }
        catch (Exception ex ) {}
        close(gid);
    }

    /*
     * (non-Javadoc)
     * @see ncsa.hdf.object.Group#clear()
     */
    public void clear() {
    	super.clear(); 
    		
    	if (attributeList != null)
    		((Vector)attributeList).setSize(0);
    }

    /*
     * (non-Javadoc)
     * @see ncsa.hdf.object.DataFormat#getMetadata()
     */
    public List getMetadata() throws HDF5Exception
    {
        if (attributeList == null)
        {
            int gid = open();
            try { attributeList = H5File.getAttribute(gid); }
            finally { close(gid); }
        }

        return attributeList;
    }

    /*
     * (non-Javadoc)
     * @see ncsa.hdf.object.DataFormat#writeMetadata(java.lang.Object)
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
            attributeList = new Vector(10);
        else
            attrExisted = attributeList.contains(attr);

        getFileFormat().writeAttribute(this, attr, attrExisted);
        // add the new attribute into attribute list
        if (!attrExisted) attributeList.add(attr);
    }

    /*
     * (non-Javadoc)
     * @see ncsa.hdf.object.DataFormat#removeMetadata(java.lang.Object)
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

    /*
     * (non-Javadoc)
     * @see ncsa.hdf.object.HObject#open()
     */
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

    /*
     * (non-Javadoc)
     * @see ncsa.hdf.object.HObject#close(int)
     */
    public void close(int gid)
    {
        try { H5.H5Gclose(gid); }
        catch (HDF5Exception ex) {;}
    }

    /**
     * Creates a new group with a given name and parent group
     * .
     * @param name the name of the group to create.
     * @param pgroup the parent group where the new group is created
     * @return the new group if successful. Otherwise returns null.
     */
    public static H5Group create(String name, Group pgroup)
        throws Exception
    {
        H5Group group = null;
        String fullPath = null;

        if (name == null)
            return null;

        H5File file = (H5File)pgroup.getFileFormat();

        if (file == null)
            return null;

        // By default, add the new group to the root
        if (pgroup == null)
            pgroup = (Group)file.get("/");

        String path = HObject.separator;
        if (!pgroup.isRoot()) {
            path = pgroup.getPath()+pgroup.getName()+HObject.separator;
            if (name.endsWith("/"))
                name = name.substring(0, name.length()-1);
                int idx = name.lastIndexOf("/");
                if (idx >=0)
                    name = name.substring(idx+1);
        }

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

        if (group != null)
            pgroup.addToMemberList(group);

        return group;
    }

    /*
     * (non-Javadoc)
     * @see ncsa.hdf.object.HObject#setName(java.lang.String)
     */
    public void setName (String newName) throws Exception
    {
        int linkType = HDF5Constants.H5G_LINK_HARD;

        String currentFullPath = getPath()+getName();
        String newFullPath = getPath()+newName;

        H5.H5Glink(getFID(), linkType, currentFullPath, newFullPath);
        try { H5.H5Gunlink(getFID(), currentFullPath); } catch (Exception ex) {}

        super.setName(newName);

        List members = this.getMemberList();
        if (members == null) return;

        int n = members.size();
        HObject obj = null;
        for (int i=0; i<n; i++)
        {
            obj = (HObject)members.get(i);
            obj.setPath(getPath()+newName+HObject.separator);
        }
    }

    /*
     * (non-Javadoc)
     * @see ncsa.hdf.object.HObject#setPath(java.lang.String)
     */
    public void setPath (String newPath) throws Exception
    {
        super.setPath(newPath);

        List members = this.getMemberList();
        if (members == null) return;

        int n = members.size();
        HObject obj = null;
        for (int i=0; i<n; i++)
        {
            obj = (HObject)members.get(i);
            obj.setPath(getPath()+getName()+HObject.separator);
        }
    }
}
