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
public class H5File extends FileFormat
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

    private boolean isReadOnly;

    /**
     * Creates an H5File with read only access.
     */
    public H5File()
    {
        this("", READ);
    }

    /**
     * Creates an H5File object with read only access.
     */
    public H5File(String pathname)
    {
        this(pathname, READ);
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

        isReadOnly = (access == READ);

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
     * Checks if the given file format is an HDF5 file.
     * <p>
     * @param fileformat the fileformat to be checked.
     * @return true if the given file is an HDF5 file; otherwise returns false.
     */
    public boolean isThisType(FileFormat fileformat)
    {
        return (fileformat instanceof H5File);
    }

    /**
     * Checks if a given file is an HDF5 file.
     * <p>
     * @param filename the file to be checked.
     * @return true if the given file is an HDF5 file; otherwise returns false.
     */
    public boolean isThisType(String filename)
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

    public FileFormat open(String pathname, int access) throws Exception
    {
        return new H5File(pathname, access);
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
            throw new HDF5Exception("Cannot write file, try open as read-only -- "+fullFileName);
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
        // clean unused objects
        if (rootNode != null)
        {
            DefaultMutableTreeNode theNode = null;
            HObject theObj = null;
            Enumeration enum = ((DefaultMutableTreeNode)rootNode).breadthFirstEnumeration();
            while(enum.hasMoreElements())
            {
                theNode = (DefaultMutableTreeNode)enum.nextElement();
                theObj = (HObject)theNode.getUserObject();
                if (theObj instanceof Dataset) ((Dataset)theObj).clearData();
                theObj = null;
                theNode = null;
            }
        }

        H5.H5Fclose(fid);
        fid = -1;
    }

    // Implementing FileFormat
    public TreeNode getRootNode()
    {
        return rootNode;
    }

    // Implementing FileFormat
    public String getFilePath()
    {
        return fullFileName;
    }

    // Implementing FileFormat
    public boolean isReadOnly()
    {
        return isReadOnly;
    }

    /**
     * Creates a new HDF5 file.
     * @param fileName the name of the file to create.
     */
    public FileFormat create(String fileName) throws Exception
    {
        H5File newFile = null;

        int fileid = H5.H5Fcreate(fileName,
            HDF5Constants.H5F_ACC_TRUNC,
            HDF5Constants.H5P_DEFAULT,
            HDF5Constants.H5P_DEFAULT);
        try {H5.H5Fclose(fileid);} catch (HDF5Exception ex){}

        return new H5File(fileName, FileFormat.WRITE);
    }

    public Group createGroup(FileFormat file, String name, Group pgroup) throws Exception
    {
        return H5Group.create(file, name, pgroup);
    }

    public Datatype createDatatype(
        int tclass,
        int tsize,
        int torder,
        int tsign) throws Exception
    {
        return new H5Datatype(tclass, tsize, torder, tsign);
    }

    public Dataset createScalarDS(
        FileFormat file,
        String name,
        Group pgroup,
        Datatype type,
        long[] dims,
        long[] maxdims,
        long[] chunks,
        int gzip,
        Object data) throws Exception
    {
        return H5ScalarDS.create(file, name, pgroup, type, dims, maxdims, chunks, gzip, data);
    }

    public Dataset createImage(
        FileFormat file,
        String name,
        Group pgroup,
        Datatype type,
        long[] dims,
        long[] maxdims,
        long[] chunks,
        int gzip,
        int ncomp,
        int intelace,
        Object data) throws Exception
    {
        H5ScalarDS dataset = (H5ScalarDS)H5ScalarDS.create(file, name, pgroup, type, dims, maxdims, chunks, gzip, data);
        try { H5File.createImageAttributes(dataset, intelace); } catch (Exception ex) {}

        return dataset;
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
    public TreeNode copy(HObject srcObj, Group dstGroup) throws Exception
    {
        TreeNode newNode = null;

        if (srcObj == null || dstGroup == null)
            return null;

        if (srcObj instanceof Dataset)
        {
           newNode = copyDataset((Dataset)srcObj, (H5Group)dstGroup);
        }
        else if (srcObj instanceof H5Group)
        {
            newNode = copyGroup((H5Group)srcObj, (H5Group)dstGroup);
        }

        return newNode;
    }

    private TreeNode copyDataset(Dataset srcDataset, H5Group pgroup)
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
                try { copy((Dataset)pal, pgroup); }
                catch (Exception ex2) {}
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

    private TreeNode copyGroup(H5Group srcGroup, H5Group pgroup)
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

    /**
     * Copy attributes of the source object to the destination object.
     */
    public void copyAttributes(HObject src, HObject dst)
    {
        int srcID = src.open();
        int dstID = dst.open();
        try { copyAttributes(srcID, dstID);
        } catch (Exception ex) {}
        src.close(srcID);
        dst.close(dstID);
    }

    /**
     * Copy attributes of the source object to the destination object.
     */
    public void copyAttributes(int src_id, int dst_id)
    {
        int aid_src=-1, aid_target=-1, atid=-1, asid=-1, num_attr=-1;
        String[] aName = {""};
        int aRank = 0;
        long[] aDims = null;
        Object data_attr = null;
        int[] native_type = {-1};

        try {
            num_attr = H5.H5Aget_num_attrs(src_id);
        } catch (Exception ex) { num_attr = -1; }

        if (num_attr < 0) return;

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

    /**
     * Update values of reference datasets. When a file is saved into a
     * new file, the values of reference dataset will not make sense in
     * the new file and must be updated based on the values of references
     * in the new file.
     */
    public static void updateReferenceDataset(H5File srcFile, H5File newFile)
    throws Exception
    {
        if (srcFile == null || newFile == null)
            return;

        DefaultMutableTreeNode srcRoot = (DefaultMutableTreeNode)srcFile.getRootNode();
        DefaultMutableTreeNode newRoot = (DefaultMutableTreeNode)newFile.getRootNode();

        Enumeration srcEnum = srcRoot.breadthFirstEnumeration();
        Enumeration newEnum = newRoot.breadthFirstEnumeration();

        // build one-to-one table of between objects in
        // the source file and new file
        int did=-1, tid=-1;
        HObject srcObj, newObj;
        Hashtable oidMap = new Hashtable();
        List refDatasets = new Vector();
        while(newEnum.hasMoreElements() && srcEnum.hasMoreElements())
        {
            srcObj = (HObject)((DefaultMutableTreeNode)srcEnum.nextElement()).getUserObject();
            newObj = (HObject)((DefaultMutableTreeNode)newEnum.nextElement()).getUserObject();
            oidMap.put(String.valueOf(((long[])srcObj.getOID())[0]), newObj.getOID());
            did = -1;
            tid = -1;
            if (newObj instanceof ScalarDS)
            {
                ScalarDS sd = (ScalarDS)newObj;
                did = sd.open();
                if (did > 0)
                {
                    try {
                        tid= H5.H5Dget_type(did);
                        if (H5.H5Tequal(tid, H5.J2C(HDF5CDataTypes.JH5T_STD_REF_OBJ)))
                            refDatasets.add(sd);
                    } catch (Exception ex) {}
                    finally
                    { try {H5.H5Tclose(tid);} catch (Exception ex) {}}
                }
                sd.close(did);
            } // if (newObj instanceof ScalarDS)
        }

        H5ScalarDS d = null;
        int sid=-1, size=0, rank=0;
        int n = refDatasets.size();
        for (int i=0; i<n; i++)
        {
            d = (H5ScalarDS)refDatasets.get(i);
            byte[] buf = null;
            long[] refs = null;

            try {
                did = d.open();
                tid = H5.H5Dget_type(did);
                sid = H5.H5Dget_space(did);
                rank = H5.H5Sget_simple_extent_ndims(sid);
                size = 1;
                if (rank > 0)
                {
                    long[] dims = new long[rank];
                    H5.H5Sget_simple_extent_dims(sid, dims, null);
                    for (int j=0; j<rank; j++)
                        size *= (int)dims[j];
                    dims = null;
                }

                buf = new byte[size*8];
                H5.H5Dread(
                    did,
                    tid,
                    HDF5Constants.H5S_ALL,
                    HDF5Constants.H5S_ALL,
                    HDF5Constants.H5P_DEFAULT,
                    buf);

                // update the ref values
                refs = HDFNativeData.byteToLong(buf);
                size = refs.length;
                for (int j=0; j<size; j++)
                {
                    long[] theOID = (long[])oidMap.get(String.valueOf(refs[j]));
                    if (theOID != null)
                    {
                        refs[j] = theOID[0];
                    }
                }

                // write back to file
                H5.H5Dwrite(
                    did,
                    tid,
                    HDF5Constants.H5S_ALL,
                    HDF5Constants.H5S_ALL,
                    HDF5Constants.H5P_DEFAULT,
                    refs);

            } catch (Exception ex)
            {
                continue;
            } finally
            {
                try { H5.H5Tclose(tid); } catch (Exception ex) {}
                try { H5.H5Sclose(sid); } catch (Exception ex) {}
                try { H5.H5Dclose(did); } catch (Exception ex) {}
            }

            refs = null;
            buf = null;
        } // for (int i=0; i<n; i++)

    }

    /** find object by its OID. */
    private HObject findObject(FileFormat file, long[] oid)
    {
        if (file == null || oid == null)
            return null;

        HObject theObj = null;
        DefaultMutableTreeNode theNode = null;

        MutableTreeNode theRoot = (MutableTreeNode)file.getRootNode();
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
     * @return the list of attriubtes of the object.
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

                Attribute attr = new Attribute(nameA[0], new H5Datatype(nativeType), dims);
                attributeList.add(attr);

                // retrieve the attribute value
                long lsize = 1;
                for (int j=0; j<dims.length; j++) lsize *= dims[j];
                Object value = H5Datatype.allocateArray(nativeType, (int)lsize);
                if (value == null)
                    continue;

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
    public void writeAttribute(HObject obj, Attribute attr,
        boolean attrExisted) throws HDF5Exception
    {
        String name = attr.getName();
        int tid=-1, sid=-1, aid=-1;

        int objID = obj.open();
        if (objID < 0) return;

        try
        {
            tid = H5.H5Tcopy(attr.getType().toNative());
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

                if (strValue.length() > size)
                {
                    // truncate the extra characters
                    strValue = strValue.substring(0, size);
                    Array.set(attrValue, 0, strValue);
                }
                else
                {
                    // pad space to the unused space
                    for (int i=strValue.length(); i<size; i++)
                        strValue += " ";
                }

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
     * Creates required atriubtes for HDF5 image.
     * @param dataset the image dataset which the attributes are added to.
     * @param interlace the interlace mode of image data.
     */
    public static void createImageAttributes(Dataset dataset, int interlace) throws Exception
    {
        String subclass = null;
        String interlaceMode = null;

        if (interlace == ScalarDS.INTERLACE_PIXEL)
        {
            subclass = "IMAGE_TRUECOLOR";
            interlaceMode = "INTERLACE_PIXEL";
        } else if (interlace == ScalarDS.INTERLACE_PLANE)
        {
            subclass = "IMAGE_TRUECOLOR";
            interlaceMode = "INTERLACE_PLANE";
        } else
        {
            subclass = "IMAGE_INDEXED";
        }

        long[] attrDims = {1};
        String attrName = "CLASS";
        String[] classValue = {"IMAGE"};
        Datatype attrType = new H5Datatype(Datatype.CLASS_STRING, classValue[0].length()+1, -1, -1);
        Attribute attr = new Attribute(attrName, attrType, attrDims);
        attr.setValue(classValue);
        dataset.writeMetadata(attr);

        attrName = "IMAGE_VERSION";
        String[] versionValue = {"1.2"};
        attrType = new H5Datatype(Datatype.CLASS_STRING, versionValue[0].length()+1, -1, -1);
        attr = new Attribute(attrName, attrType, attrDims);
        attr.setValue(versionValue);
        dataset.writeMetadata(attr);

        attrDims[0] = 2;
        attrName = "IMAGE_MINMAXRANGE";
        byte[] attrValueInt = {0, (byte)255};
        attrType = new H5Datatype(Datatype.CLASS_CHAR, 1, Datatype.NATIVE, Datatype.SIGN_NONE);
        attr = new Attribute(attrName, attrType, attrDims);
        attr.setValue(attrValueInt);
        dataset.writeMetadata(attr);

        attrDims[0] = 1;
        attrName = "IMAGE_SUBCLASS";
        String[] subclassValue = {subclass};
        attrType = new H5Datatype(Datatype.CLASS_STRING, subclassValue[0].length()+1, -1, -1);
        attr = new Attribute(attrName, attrType, attrDims);
        attr.setValue(subclassValue);
        dataset.writeMetadata(attr);

        if (interlace == ScalarDS.INTERLACE_PIXEL || interlace == ScalarDS.INTERLACE_PLANE)
        {
            attrName = "INTERLACE_MODE";
            String[] interlaceValue = {interlaceMode};
            attrType = new H5Datatype(Datatype.CLASS_STRING, interlaceValue[0].length()+1, -1, -1);
            attr = new Attribute(attrName, attrType, attrDims);
            attr.setValue(interlaceValue);
            dataset.writeMetadata(attr);
        }
        else
        {
            attrName = "PALETTE";
            long[] palRef = {-1};
            attrType = new H5Datatype(H5Datatype.CLASS_REFERENCE, 1, Datatype.NATIVE, Datatype.SIGN_NONE);
            attr = new Attribute(attrName, attrType, attrDims);
            attr.setValue(palRef);
            dataset.writeMetadata(attr);
        }
    }

    /**
     *  Returns the version of the HDF5 library.
     */
    public String getLibversion()
    {
        int[] vers = new int[3];
        String ver = "NCSA HDF5 ";

        try { H5.H5get_libversion(vers); }
        catch (HDF5Exception ex) {}

        ver += vers[0] + "."+ vers[1] +"."+vers[2];

        return ver;
    }

}

