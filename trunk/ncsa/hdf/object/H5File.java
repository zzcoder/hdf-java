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

import java.io.File;
import java.util.*;
import javax.swing.tree.*;
import java.lang.reflect.Array;
import ncsa.hdf.object.*;
import ncsa.hdf.hdf5lib.*;
import ncsa.hdf.hdf5lib.exceptions.*;

/**
 * This class provides file level APIs. File access APIs include retrieving the
 * file hierarchy, opening and closing file, and writing file content to disk.
 * <p>
 * @version 1.0 12/12/2001
 * @author Peter X. Cao, NCSA
 */
public class H5File extends File implements FileFormat
{
    /**
     * file identifier for the open file.
     */
    private int fid;

    /**
     * the file access flag.
     */
    private int flag;

    /**
     * The full name of this file; full path plus name
     */
    private final String fullFileName;

    /**
     * The root node of the file hierearchy.
     */
    private MutableTreeNode rootNode;

    /**
     * Creates an H5File object with read only access.
     */
    public H5File(String pathname)
    {
        this(pathname, HDF5Constants.H5F_ACC_RDONLY);
    }

    /**
     * Creates an H5File object with specific file name and access flag.
     * <p>
     * @param pathname the full path name of the file.
     * @param flag the file access flag, it takes one of two values below:
     * <DL><DL>
     * <DT> H5F_ACC_RDWR <DD> Allow read and write access to file.</DT>
     * <DT> H5F_ACC_RDONLY <DD> Allow read-only access to file.</DT>
     * </DL></DL>
     */
    public H5File(String pathname, int access)
    {
        super(pathname);

        this.fid = -1;
        this.fullFileName = pathname;

        if (access == READ)
            flag = HDF5Constants.H5F_ACC_RDONLY;
        else if (access == WRITE)
            flag = HDF5Constants.H5F_ACC_RDWR;
        else if (access == CREATE)
            flag = HDF5Constants.H5F_ACC_CREAT;
        else
            flag = access;
    }

    /**
     * Checks if a given file is an HDF5 file.
     * <p>
     * @param filename the file to be checked.
     * @return true if the given file is an HDF5 file; otherwise returns false.
     */
    public static boolean isThisType(String filename)
    {
        boolean isH5 = false;

        try {
            isH5 = H5.H5Fis_hdf5(filename);
        } catch (HDF5Exception ex)
        {
            isH5 = false;
        }

        return isH5;
    }

    // Implementing FileFormat
    public int open() throws Exception
    {
        if ( fid >=0 )
            return fid; // file is openned already

        // check for valid file access permission
        if ( flag < 0)
            throw new HDF5Exception("Invalid access identifer -- "+flag);
        else if ((flag == HDF5Constants.H5F_ACC_RDWR ||
            flag == HDF5Constants.H5F_ACC_CREAT) && !canWrite())
            throw new HDF5Exception("Cannot write file -- "+fullFileName);
        else if (!exists())
            throw new HDF5Exception("File does not exist -- "+fullFileName);
        else if ((flag == HDF5Constants.H5F_ACC_RDONLY) && !canRead())
            throw new HDF5Exception("Cannot read file -- "+fullFileName);

        fid = H5.H5Fopen( fullFileName, flag, HDF5Constants.H5P_DEFAULT);

        if (fid>=0)
        {
            // load the hierearchy of the file
            rootNode = loadTree();
        }

        return fid;
    }

    // Implementing FileFormat
    public void close() throws HDF5Exception
    {
        H5.H5Fclose(fid);
        fid = -1;
    }

    // Implementing FileFormat
    public MutableTreeNode getRootNode()
    {
        return rootNode;
    }

    // Implementing FileFormat
    public String getFilePath()
    {
        return fullFileName;
    }

    /**
     * Creates a new HDF5 file.
     * @param fileName the name of the file to create.
     */
    public static void create(String fileName) throws Exception
    {
        H5File newFile = null;

        int fileid = H5.H5Fcreate(fileName,
            HDF5Constants.H5F_ACC_TRUNC,
            HDF5Constants.H5P_DEFAULT,
            HDF5Constants.H5P_DEFAULT);
        try {H5.H5Fclose(fileid);} catch (HDF5Exception ex){}
    }

    /**
     * Delete an object from the file.
     * @param obj the data object to delete.
     */
    public void delete(HObject obj) throws Exception
    {
        if (obj == null || fid < 0)
            return;

        String name = obj.getPath()+obj.getName();
        H5.H5Gunlink(fid, name);
    }

    /**
     * Retrieves and returns the tree structure from the file
     */
    private MutableTreeNode loadTree()
    {
        if (fid <0 ) return null;

        long[] oid = {0};
        H5Group rootGroup = new H5Group(
            this,
            getName(), // set the node name to the file name
            null, // root node does not have a parent path
            null, // root node does not have a parent node
            oid);

        MutableTreeNode root = new DefaultMutableTreeNode(rootGroup)
        {
            public boolean isLeaf() { return false; }
        };

        depth_first(root);

        return root;
    }

    /**
     * Copy an object to a group.
     * @param srcObj   the object to copy.
     * @param dstGroup the destination group.
     * @return the new node containing the new object.
     */
    public TreeNode copy(HObject srcObj,
        H5Group dstGroup) throws Exception
    {
        TreeNode newNode = null;

        if (srcObj == null ||
            dstGroup == null)
            return null;

        if (srcObj instanceof Dataset)
        {
           newNode = copy((Dataset)srcObj, dstGroup);
        }
        else if (srcObj instanceof H5Group)
        {
            newNode = copy((H5Group)srcObj, dstGroup);
        }

        return newNode;
    }

    private TreeNode copy(Dataset srcDataset, H5Group pgroup)
         throws Exception
    {
        Dataset dataset = null;
        int srcdid, dstdid, tid, sid, plist;
        String dname=null, path=null;

        if (pgroup.isRoot())
            path = HObject.separator;
        else
            path = pgroup.getPath()+pgroup.getName()+HObject.separator;
        dname = path + srcDataset.getName();

        srcdid = srcDataset.open();
        tid = H5.H5Dget_type(srcdid);
        sid = H5.H5Dget_space(srcdid);
        plist = H5.H5Dget_create_plist(srcdid);
        dstdid = H5.H5Dcreate(fid, dname, tid, sid, plist);

        // copy data values
        H5.H5Dcopy(srcdid, dstdid);

        // copy attributes from one object to the new object
        copyAttributes(srcdid, dstdid);

        byte[] ref_buf = H5.H5Rcreate(
            fid,
            dname,
            HDF5Constants.H5R_OBJECT,
            -1);
        long l = HDFNativeData.byteToLong(ref_buf, 0);
        long[] oid = {l};

        if (srcDataset instanceof H5ScalarDS)
        {
            dataset = new H5ScalarDS(
                this,
                srcDataset.getName(),
                path,
                oid);
        } else
        {
            dataset = new H5CompoundDS(
                this,
                srcDataset.getName(),
                path,
                oid);
        }

        pgroup.addToMemberList(dataset);
        DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(dataset);

        // if the dataset is a image and has palette, copy the pallete
        try {
            int srcaid = H5.H5Aopen_name(srcdid, "PALETTE");
            int dstaid = H5.H5Aopen_name(dstdid, "PALETTE");
            int atid = H5.H5Aget_type(srcaid);
            oid = new long[1];
            H5.H5Aread(srcaid, atid, oid);

            // search and copy palette
            HObject pal = findObject(srcDataset.getFileFormat(), oid);
            if (pal != null && pal instanceof Dataset)
            {
                copy((Dataset)pal, pgroup);
                ref_buf = H5.H5Rcreate(
                    fid,
                    path+pal.getName(),
                    HDF5Constants.H5R_OBJECT,
                    -1);
                H5.H5Awrite(dstaid, atid, ref_buf);
            }

            try { H5.H5Tclose(atid); } catch(Exception ex) {}
            try { H5.H5Aclose(srcaid); } catch(Exception ex) {}
            try { H5.H5Aclose(dstaid); } catch(Exception ex) {}

        } catch (Exception ex) {}

        try { H5.H5Pclose(plist); } catch(Exception ex) {}
        try { H5.H5Sclose(sid); } catch(Exception ex) {}
        try { H5.H5Tclose(tid); } catch(Exception ex) {}
        try { H5.H5Dclose(srcdid); } catch(Exception ex) {}
        try { H5.H5Dclose(dstdid); } catch(Exception ex) {}

        return newNode;
    }

    private TreeNode copy(H5Group srcGroup, H5Group pgroup)
         throws Exception
    {
        H5Group group = null;
        int srcgid, dstgid;
        String gname=null, path=null;

        if (pgroup.isRoot())
            path = HObject.separator;
        else
            path = pgroup.getPath()+pgroup.getName()+HObject.separator;

        gname = path + srcGroup.getName();
        srcgid = srcGroup.open();
        dstgid = H5.H5Gcreate(fid, gname, 0);
        byte[] ref_buf = H5.H5Rcreate(
            fid,
            gname,
            HDF5Constants.H5R_OBJECT,
            -1);
        long l = HDFNativeData.byteToLong(ref_buf, 0);
        long[] oid = {l};
        group = new H5Group(this, srcGroup.getName(), path, pgroup, oid);

        copyAttributes(srcgid, dstgid);

        DefaultMutableTreeNode theNode = new DefaultMutableTreeNode(group)
        {
            public boolean isLeaf() { return false; }
        };
        pgroup.addToMemberList(group);

        // copy members of the source group to the new group
        List members = srcGroup.getMemberList();
        if (members != null && members.size()>0)
        {
            Iterator iterator = members.iterator();
            while (iterator.hasNext())
            {
                HObject mObj = (HObject)iterator.next();

                try {
                    theNode.add((MutableTreeNode)copy(mObj, group));
                } catch (Exception ex) {}
            }
        }

        return theNode;
    }

    private void copyAttributes(int src_id, int dst_id)
    {
        int aid_src=-1, aid_target=-1, atid=-1, asid=-1, num_attr=-1;
        String[] aName = {""};
        int aRank = 0;
        long[] aDims = null;
        Object data_attr = null;
        int[] native_type = {-1};

        try {
            num_attr = H5.H5Aget_num_attrs(src_id);
        } catch (Exception ex) {
            return;
        }

        if (num_attr < 0)
            return;

        for (int i=0; i<num_attr; i++)
        {
            aName[0] = new String("");
            try {
                aid_src = H5.H5Aopen_idx(src_id, i );
                H5.H5Aget_name(aid_src, 80, aName );
                atid = H5.H5Aget_type(aid_src);
                asid = H5.H5Aget_space(aid_src);
                aRank = H5.H5Sget_simple_extent_ndims(asid);
                aDims = new long[aRank];
                H5.H5Sget_simple_extent_dims(asid, aDims, null);
                long size = 1;
                for (int j=0; j<aRank; j++)
                    size *= (int)aDims[j];
                data_attr = H5Datatype.allocateArray(atid, (int)size);
                aid_target = H5.H5Acreate(
                    dst_id,
                    aName[0],
                    atid,
                    asid,
                    HDF5Constants.H5P_DEFAULT );
                H5.H5Aread(aid_src, atid, data_attr);
                H5.H5Awrite(aid_target, atid, data_attr);
            } catch (Exception ex) {}

            try { H5.H5Sclose(asid); } catch(Exception ex) {}
            try { H5.H5Tclose(atid); } catch(Exception ex) {}
            try { H5.H5Aclose(aid_src); } catch(Exception ex) {}
            try { H5.H5Aclose(aid_target); } catch(Exception ex) {}
        } // for (int i=0; i<num_attr; i++)
    }

    /** find object by its OID. */
    private HObject findObject(FileFormat file, long[] oid)
    {
        if (file == null || oid == null)
            return null;

        HObject theObj = null;
        DefaultMutableTreeNode theNode = null;

        MutableTreeNode theRoot = file.getRootNode();
        if (theRoot == null)
            return null;

        Enumeration enum = ((DefaultMutableTreeNode)theRoot).breadthFirstEnumeration();
        while(enum.hasMoreElements())
        {
            theNode = (DefaultMutableTreeNode)enum.nextElement();
            theObj = (HObject)theNode.getUserObject();
            if (theObj.equalsOID(oid))
                break;
        }

        return theObj;
    }

    /**
     * Retrieves the file structure by depth-first order, recursively.
     * The current implementation retrieves group and dataset only. It does
     * not include named datatype and soft links.
     * <p>
     * It also detects and stops loops. A loop is detected if there exists
     * object with the same object ID by tracing path back up to the root.
     * <p>
     * @param parentNode the parent node.
     */
    private void depth_first(MutableTreeNode parentNode)
    {
        //System.out.println("H5File.depth_first() pnode = "+parentNode);
        int nelems;
        MutableTreeNode node = null;
        String fullPath = null;
        String ppath = null;
        DefaultMutableTreeNode pnode = (DefaultMutableTreeNode)parentNode;

        H5Group pgroup = (H5Group)(pnode.getUserObject());
        ppath = pgroup.getPath();

        if (ppath == null)
        {
            fullPath = HObject.separator;
        }
        else
        {
            fullPath = ppath+pgroup.getName()+HObject.separator;
        }

        nelems = 0;
        try {
            nelems = H5.H5Gn_members(fid, fullPath);
        } catch (HDF5Exception ex) {
            nelems = -1;
        }

        if (nelems < 0 ) {
            return;
        }

        int[] oType = new int[1];
        long[] oid = null;
        String [] oName = new String[1];
        //Iterate through the file to see members of the group
        for ( int i = 0; i < nelems; i++)
        {
            oName[0] = "";
            oType[0] = -1;
            oid = null;
            try {
                H5.H5Gget_obj_info_idx(fid, fullPath, i, oName, oType );
            } catch (HDF5Exception ex) {
                // do not stop if accessing one member fails
                continue;
            }

            try
            {
                // retrieve the object ID.
                byte[] ref_buf = H5.H5Rcreate(
                    fid,
                    fullPath+oName[0],
                    HDF5Constants.H5R_OBJECT,
                    -1);
                long l = HDFNativeData.byteToLong(ref_buf, 0);
                oid = new long[1];
                oid[0] = l; // save the object ID
            } catch (HDF5Exception ex) {}

            if (oid == null)
                continue; // do the next one, if the object is not identified.

            switch (oType[0])
            {
                // create a new group
                case HDF5Constants.H5G_GROUP:
                    H5Group g = new H5Group(
                        this,
                        oName[0],
                        fullPath,
                        pgroup,
                        oid);
                    node = new DefaultMutableTreeNode(g)
                    {
                        public boolean isLeaf() { return false; }
                    };
                    pnode.add( node );
                    pgroup.addToMemberList(g);

                    // detect and stop loops
                    // a loop is detected if there exists object with the same
                    // object ID by tracing path back up to the root.
                    boolean hasLoop = false;
                    HObject tmpObj = null;
                    long[] tmpOID = null;
                    DefaultMutableTreeNode tmpNode = (DefaultMutableTreeNode)pnode;
                    while (tmpNode != null)
                    {
                        tmpObj = (HObject)tmpNode.getUserObject();
                        if (tmpObj.equalsOID(oid))
                        {
                            hasLoop = true;
                            break;
                        }
                        else
                            tmpNode = (DefaultMutableTreeNode)tmpNode.getParent();
                    }

                    // recursively go through the next group
                    // stops if it has loop.
                    if (!hasLoop)
                        depth_first(node);
                    break;
                case HDF5Constants.H5G_DATASET:
                    int did=-1, tid=-1, tclass=-1;
                    boolean isDefaultImage = false;
                    try {
                        did = H5.H5Dopen(fid, fullPath+oName[0]);
                        tid = H5.H5Dget_type(did);
                        tclass = H5.H5Tget_class(tid);
                        if (tclass == HDF5Constants.H5T_ARRAY)
                        {
                            // for ARRAY, the type is determined by the base type
                            int btid = H5.H5Tget_super(tid);
                            tclass = H5.H5Tget_class(btid);
                            try { H5.H5Tclose(btid); } catch (HDF5Exception ex) {}
                        }
                    } catch (HDF5Exception ex) {}
                    finally {
                        try { H5.H5Tclose(tid); } catch (HDF5Exception ex) {}
                        try { H5.H5Dclose(did); } catch (HDF5Exception ex) {}
                    }
                    Dataset d = null;

                    if (tclass == HDF5Constants.H5T_COMPOUND)
                    {
                        // create a new compound dataset
                        d = new H5CompoundDS(
                            this,
                            oName[0],
                            fullPath,
                            oid);
                    }
                    else
                    {
                        // create a new scalar dataset
                        d = new H5ScalarDS(
                            this,
                            oName[0],
                            fullPath,
                            oid);
                    }

                    node = new DefaultMutableTreeNode(d);
                    pnode.add( node );
                    pgroup.addToMemberList(d);
                    break;
                default:
                    break;
            } // switch (oType[0])
        } // for ( i = 0; i < nelems; i++)
    } // private depth_first()

    /**
     * Returns a list of attriubtes for the given object location.
     * <p>
     * @param objID the object identifier.
     * @retun the list of attriubtes of the object.
     */
    public static List getAttribute(int objID) throws HDF5Exception
    {
        List attributeList = null;

        int aid=-1, sid=-1, tid=-1, n=0;

        n = H5.H5Aget_num_attrs(objID);
        if (n <= 0) return null; // no attribute attached to this object

        attributeList = new Vector(n, 5);
        for (int i=0; i<n; i++)
        {
            try
            {
                aid = H5.H5Aopen_idx(objID, i);
                sid = H5.H5Aget_space(aid);
                long dims[] = null;
                int rank = H5.H5Sget_simple_extent_ndims(sid);
                if (rank == 0)
                {
                    // for scalar data, rank=0
                    rank = 1;
                    dims = new long[1];
                    dims[0] = 1;
                }
                else
                {
                    dims = new long[rank];
                    H5.H5Sget_simple_extent_dims(sid, dims, null);
                }

                String[] nameA = {""};
                H5.H5Aget_name(aid, 80, nameA);

                tid = H5.H5Aget_type(aid);
                int nativeType = H5Datatype.toNativeType(tid);

                Attribute attr = new Attribute(nameA[0], nativeType, dims);

                long lsize = 1;
                for (int j=0; j<dims.length; j++)
                    lsize *= dims[j];

                Object value = H5Datatype.allocateArray(nativeType, (int)lsize);

                if (H5.H5Tget_class(nativeType)==HDF5Constants.H5T_ARRAY)
                    H5.H5Aread(aid, H5Datatype.toNativeType(H5.H5Tget_super(nativeType)), value);
                else
                    H5.H5Aread(aid, nativeType, value);

                int typeClass = H5.H5Tget_class(nativeType);
                if (typeClass==HDF5Constants.H5T_STRING)
                    value = Dataset.byteToString((byte[])value, H5.H5Tget_size(nativeType));
                else if (typeClass == HDF5Constants.H5T_REFERENCE)
                    value = HDFNativeData.byteToLong((byte[])value);

                attr.setValue(value);
                attributeList.add(attr);
            } catch (HDF5Exception ex) {}

            try { H5.H5Tclose(tid); } catch (HDF5Exception ex) {}
            try { H5.H5Sclose(sid); } catch (HDF5Exception ex) {}
            try { H5.H5Aclose(aid); } catch (HDF5Exception ex) {}
        }

        return attributeList;
    }

    /**
     * Creates a new attribute and attached to the object if attribute does
     * not exist. Otherwise, just update the value of the attribute.
     *
     * <p>
     * @param obj the object which the attribute is to be attached to.
     * @param attr the atribute to attach.
     * @param attrExisted The indicator if the given attribute exists.
     * @return true if successful and false otherwise.
     */
    public static void writeAttribute(HObject obj, Attribute attr,
        boolean attrExisted) throws HDF5Exception
    {
        String name = attr.getName();
        int tid=-1, sid=-1, aid=-1;

        int objID = obj.open();
        if (objID < 0) return;

        try
        {
            tid = H5.H5Tcopy(attr.getType());
            sid = H5.H5Screate_simple(attr.getRank(), attr.getDataDims(), null);

            if (attrExisted)
                aid = H5.H5Aopen_name(objID, name);
            else
                aid = H5.H5Acreate(objID, name, tid, sid, HDF5Constants.H5P_DEFAULT);

            // update value of the attribute
            Object attrValue = attr.getValue();
            if (Array.get(attrValue, 0) instanceof String)
            {
                String strValue = (String)Array.get(attrValue, 0);
                int size = H5.H5Tget_size(tid);
                for (int i=strValue.length(); i<size; i++)
                    strValue += " ";
                byte[] bval = strValue.getBytes();
                // add null to the end to get rid of the junks
                bval[(strValue.length() - 1)] = 0;
                attrValue = bval;
            }

            H5.H5Awrite(aid, tid, attrValue);
        } finally
        {
            try { H5.H5Tclose(tid); } catch (HDF5Exception ex) {}
            try { H5.H5Sclose(sid); } catch (HDF5Exception ex) {}
            try { H5.H5Aclose(aid); } catch (HDF5Exception ex) {}
        }

        obj.close(objID);
    }

    /**
     *  Returns the version of the HDF5 library.
     */
    public static final String getLibversion()
    {
        int[] vers = new int[3];
        String ver = "NCSA HDF Version 5.";

        try { H5.H5get_libversion(vers); }
        catch (HDF5Exception ex) {}

        ver += vers[0] + "."+ vers[1] +" Release "+vers[2];

        return ver;
    }

}

