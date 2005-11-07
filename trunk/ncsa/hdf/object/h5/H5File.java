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
    private DefaultMutableTreeNode rootNode;

    /** flag to indicate if the file is readonly. */
    private boolean isReadOnly;

    /**
     * Constructs an empty H5File with read-only access.
     */
    public H5File()
    {
        this("", WRITE);
    }

    /**
     * Constructs an H5File object of given file name with read-only access.
     */
    public H5File(String pathname)
    {
        this(pathname, WRITE);
    }

    /**
     * Constructs an H5File object with given file name and access flag.
     * <p>
     * @param pathname the full path name of the file.
     * @param flag the file access flag, it takes one of two values below:
     * <DL><DL>
     * <DT> READ <DD> Allow read-only access to file.</DT>
     * <DT> WRITE <DD> Allow read and write access to file.</DT>
     * <DT> CREATE <DD> Create a new file.</DT>
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

    /**
     * Creates an instance of an H5File with given file name and access flag.
     * <p>
     * @param pathname the full path name of the file.
     * @param flag the file access flag, it takes one of two values below:
     * <DL><DL>
     * <DT> READ <DD> Allow read-only access to file.</DT>
     * <DT> WRITE <DD> Allow read and write access to file.</DT>
     * <DT> CREATE <DD> Create a new file.</DT>
     * </DL></DL>
     */
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
        else if (!exists())
            throw new HDF5Exception("File does not exist -- "+fullFileName);
        else if ((flag == HDF5Constants.H5F_ACC_RDWR ||
            flag == HDF5Constants.H5F_ACC_CREAT) && !canWrite())
            throw new HDF5Exception("Cannot write file, try open as read-only -- "+fullFileName);
        else if ((flag == HDF5Constants.H5F_ACC_RDONLY) && !canRead())
            throw new HDF5Exception("Cannot read file -- "+fullFileName);

        int plist = HDF5Constants.H5P_DEFAULT;

        /*
        try {
            //All open objects ramaining in the file are closed then file is closed
            plist = H5.H5Pcreate (HDF5Constants.H5P_FILE_ACCESS);
            H5.H5Pset_fclose_degree ( plist, HDF5Constants.H5F_CLOSE_STRONG);
        } catch (Exception ex) {System.out.println(ex);}
        */

        fid = H5.H5Fopen( fullFileName, flag, plist);

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

        // close all open datasets
        try {
            int n = H5.H5Fget_obj_count(fid, HDF5Constants.H5F_OBJ_DATASET);
            int[] ids = new int[n];
            H5.H5Fget_obj_ids(fid, HDF5Constants.H5F_OBJ_DATASET, n, ids);
            for (int i=0; i<n; i++) {
                try { H5.H5Dclose(ids[i]); } catch (Exception ex2) {}
            }
        } catch (Exception ex) {}

        // close all open group
        try {
            int n = H5.H5Fget_obj_count(fid, HDF5Constants.H5F_OBJ_GROUP );
            int[] ids = new int[n];
            H5.H5Fget_obj_ids(fid, HDF5Constants.H5F_OBJ_GROUP , n, ids);
            for (int i=0; i<n; i++) {
                try { H5.H5Gclose(ids[i]); } catch (Exception ex2) {}
            }
        } catch (Exception ex) {}


        try { H5.H5Fflush(fid, HDF5Constants.H5F_SCOPE_GLOBAL); } catch (Exception ex) {}
        try { H5.H5Fclose(fid); } catch (Exception ex) {}

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
     * Creates a new HDF5 file with given file name.
     * <p>
     * @param pathname the full path name of the file.
     * @return an instance of the new H5File.
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

    // implementign FileFormat
    public Group createGroup(String name, Group pgroup) throws Exception
    {
        return H5Group.create(name, pgroup);
    }

    // implementign FileFormat
    public Datatype createDatatype(
        int tclass,
        int tsize,
        int torder,
        int tsign) throws Exception
    {
        return new H5Datatype(tclass, tsize, torder, tsign);
    }

    /* create a name dataype */
    public Datatype createDatatype(
        int tclass,
        int tsize,
        int torder,
        int tsign,
        String name) throws Exception
    {
        int tid=-1;
        H5Datatype dtype = null;

        try {
            H5Datatype t = (H5Datatype) createDatatype(tclass, tsize, torder, tsign);
            tid = t.toNative();
            H5.H5Tcommit(fid, name, tid);

            byte[] ref_buf = H5.H5Rcreate(fid, name, HDF5Constants.H5R_OBJECT, -1);
            long l = HDFNativeData.byteToLong(ref_buf, 0);
            long[] oid = new long[1];
            oid[0] = l; // save the object ID

            dtype = new H5Datatype(this, null, name, oid);

        } finally {
            if (tid>0) H5.H5Tclose(tid);
        }

        return dtype;
    }

    // implementign FileFormat
    public Dataset createScalarDS(
        String name,
        Group pgroup,
        Datatype type,
        long[] dims,
        long[] maxdims,
        long[] chunks,
        int gzip,
        Object data) throws Exception
    {
        return H5ScalarDS.create(name, pgroup, type, dims, maxdims, chunks, gzip, data);
    }

    // implementign FileFormat
    public Dataset createCompoundDS(
        String name,
        Group pgroup,
        long[] dims,
        String[] memberNames,
        Datatype[] memberDatatypes,
        int[] memberSizes,
        Object data) throws Exception
    {
        // not supported
        return H5CompoundDS.create(name, pgroup, dims, memberNames, memberDatatypes, memberSizes, data);
    }

    // implementign FileFormat
    public Dataset createImage(
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
        H5ScalarDS dataset = (H5ScalarDS)H5ScalarDS.create(name, pgroup, type, dims, maxdims, chunks, gzip, data);
        try { H5File.createImageAttributes(dataset, intelace); } catch (Exception ex) {}

        return dataset;
    }

    // implementign FileFormat
    public void delete(HObject obj) throws Exception
    {
        if (obj == null || fid < 0)
            return;

        String name = obj.getPath()+obj.getName();
        H5.H5Gunlink(fid, name);
    }

    // implementign FileFormat
    private DefaultMutableTreeNode loadTree()
    {
        if (fid <0 ) return null;

        long[] oid = {0};
        H5Group rootGroup = new H5Group(
            this,
            getName(), // set the node name to the file name
            null, // root node does not have a parent path
            null, // root node does not have a parent node
            oid);

        DefaultMutableTreeNode root = new DefaultMutableTreeNode(rootGroup)
        {
            public boolean isLeaf() { return false; }
        };

        depth_first(root);

        return root;
    }

    /** reload the sub-tree structure. Sube-calsses need to replace it */
    public void reloadTree(Group g)
    {
        if (fid < 0 || rootNode == null || g==null) return;

        HObject theObj = null;
        DefaultMutableTreeNode theNode = null;

        if (g.equals(rootNode.getUserObject()))
            theNode = rootNode;
        else {
            Enumeration enum = rootNode.breadthFirstEnumeration();
            while(enum.hasMoreElements()) {
                theNode = (DefaultMutableTreeNode)enum.nextElement();
                theObj = (HObject)theNode.getUserObject();
                if (g.equals(theObj)) break;
            }
        }

        theNode.removeAllChildren();

        depth_first(theNode);
    }

    // implementign FileFormat
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
        else if (srcObj instanceof H5Datatype)
        {
            newNode = copyDatatype((H5Datatype)srcObj, (H5Group)dstGroup);
        }

        return newNode;
    }

    /** copy a dataset into another group.
     * @param srcDataset the dataset to be copied.
     * @param pgroup teh group where the dataset is copied to.
     * @return the treeNode containing the new copy of the dataset.
     */

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

        // @@@@@@@@@ HDF5.1.6 bug at H5Dcreate(fid, dname, tid, sid, plist);
        try {
            dstdid = H5.H5Dcreate(fid, dname, tid, sid, plist);
        } catch (Exception ex) { throw new HDF5LibraryException(
            "H5ScalarDS.copyDataset(): HDF5.1.6 failed at H5Dcreate(fid, dname, tid, sid, plist)");}

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

    private TreeNode copyDatatype(Datatype srcType, H5Group pgroup)
         throws Exception
    {
        Datatype datatype = null;
        int tid_src, tid_dst;
        String tname=null, path=null;

        if (pgroup.isRoot())
            path = HObject.separator;
        else
            path = pgroup.getPath()+pgroup.getName()+HObject.separator;

        tname = path + srcType.getName();
        tid_src = srcType.open();
        tid_dst = H5.H5Tcopy(tid_src);

        H5.H5Tcommit(fid, tname, tid_src );

        byte[] ref_buf = H5.H5Rcreate(
            fid,
            tname,
            HDF5Constants.H5R_OBJECT,
            -1);
        long l = HDFNativeData.byteToLong(ref_buf, 0);
        long[] oid = {l};

        datatype = new H5Datatype(this, srcType.getName(), path, oid);

        pgroup.addToMemberList(datatype);
        DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(datatype);

        try { H5.H5Tclose(tid_src); } catch(Exception ex) {}
        try { H5.H5Tclose(tid_dst); } catch(Exception ex) {}

        return newNode;
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
                        if (H5.H5Tequal(tid, HDF5Constants.H5T_STD_REF_OBJ))
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
        if (theRoot == null) return null;

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
        String objName = null;
        DefaultMutableTreeNode pnode = (DefaultMutableTreeNode)parentNode;
        int gid = -1;

        H5Group pgroup = (H5Group)(pnode.getUserObject());
        ppath = pgroup.getPath();

        if (ppath == null)
        {
            fullPath = HObject.separator;
            objName = "/";
        }
        else
        {
            objName = pgroup.getName();
            fullPath = ppath+pgroup.getName()+HObject.separator;
        }

        nelems = 0;
        try {
            gid = pgroup.open();
            long[] nmembers = {0};
            H5.H5Gget_num_objs(gid, nmembers);
            nelems = (int)nmembers[0];
        } catch (HDF5Exception ex) {
            nelems = -1;
            gid = -1;
        }

        if (nelems <= 0 ) {
            return;
        }

        pgroup.setNumberOfMembersInFile(nelems);

        // since each call of H5.H5Gget_objname_by_idx() takes about one second.
        // 1,000,000 calls take 12 days. Instead of calling it in a loop,
        // we use only one call to get all the information, which takes about
        // two seconds
        int[] objTypes = new int[nelems];
        String[] objNames = new String[nelems];
        try { H5.H5Gget_obj_info_all(fid, fullPath, objNames, objTypes ); }
        catch (HDF5Exception ex) {ex.printStackTrace(); }

        int i0 = Math.max(0, getStartMembers());
        int i1 = getMaxMembers();
        if (i1 >= nelems)
        {
            i1 = nelems;
            i0 = 0; // load all members
        }
        i1 += i0;
        i1 = Math.min(i1, nelems);

        long[] oid = null;
        String obj_name;
        int obj_type;
        //Iterate through the file to see members of the group
        for ( int i = i0; i < i1; i++)
        {
            oid = null;
            obj_name = objNames[i];
            obj_type = objTypes[i];

            // Comment it out for bad performance. See the notes above
/*
            try {
                H5.H5Gget_objname_by_idx(gid, i, oName, 80);
                oType[0] = H5.H5Gget_objtype_by_idx(gid, i);
            } catch (HDF5Exception ex) {
                // do not stop if accessing one member fails
                continue;
            }
*/
            try
            {
                byte[] ref_buf = null;
                if (obj_type == HDF5Constants.H5G_LINK)
                {
                    // find the object linked to
                    String[] realName = {""};
                    H5.H5Gget_linkval(fid, fullPath+obj_name, 100, realName);
                    if (realName[0] != null && !realName[0].startsWith(HObject.separator))
                    {
                        realName[0] = fullPath+realName[0];
                    }
                    ref_buf = H5.H5Rcreate(fid, realName[0], HDF5Constants.H5R_OBJECT, -1);
                    if (realName[0] != null && realName[0].length()>0 && ref_buf !=null)
                    {
                        obj_type = H5.H5Rget_obj_type(fid, HDF5Constants.H5R_OBJECT, ref_buf);
                    }
                }
                else
                {
                    // retrieve the object ID.
                    ref_buf = H5.H5Rcreate(fid, fullPath+obj_name, HDF5Constants.H5R_OBJECT, -1);
                }

                long l = HDFNativeData.byteToLong(ref_buf, 0);
                oid = new long[1];
                oid[0] = l; // save the object ID
            } catch (HDF5Exception ex) {System.out.println(ex);}

            if (oid == null)
                continue; // do the next one, if the object is not identified.

            // create a new group
            if (obj_type == HDF5Constants.H5G_GROUP)
            {
                H5Group g = new H5Group(
                    this,
                    obj_name,
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
            } else if (obj_type == HDF5Constants.H5G_DATASET)
            {
                int did=-1, tid=-1, tclass=-1;
                boolean isDefaultImage = false;
                try {
                    did = H5.H5Dopen(fid, fullPath+obj_name);
                    tid = H5.H5Dget_type(did);
                    tclass = H5.H5Tget_class(tid);
                    if (tclass == HDF5Constants.H5T_ARRAY ||
                        tclass == HDF5Constants.H5T_VLEN)
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
                        obj_name,
                        fullPath,
                        oid);
                }
                else
                {
                    // create a new scalar dataset
                    d = new H5ScalarDS(
                        this,
                        obj_name,
                        fullPath,
                        oid);
                }

                node = new DefaultMutableTreeNode(d);
                pnode.add( node );
                pgroup.addToMemberList(d);
            } else if (obj_type == HDF5Constants.H5G_TYPE)
            {
                Datatype t = new H5Datatype( this, obj_name, fullPath, oid);

                node = new DefaultMutableTreeNode(t);
                pnode.add( node );
                pgroup.addToMemberList(t);
            }
        } // for ( i = 0; i < nelems; i++)

        pgroup.close(gid);

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
        if (n <= 0) new Vector(0); // no attribute attached to this object

        attributeList = new Vector(n);
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
                int nativeType = H5Datatype.toNative(tid);

                Attribute attr = new Attribute(nameA[0], new H5Datatype(nativeType), dims);
                attributeList.add(attr);
                boolean is_variable_str = false;
                boolean isVLEN = false;
                boolean isCompound = false;
                try { is_variable_str = H5.H5Tis_variable_str(nativeType); } catch (Exception ex) {}
                try { isVLEN = (H5.H5Tget_class(nativeType)==HDF5Constants.H5T_VLEN); } catch (Exception ex) {}
                try { isCompound = (H5.H5Tget_class(nativeType)==HDF5Constants.H5T_COMPOUND); } catch (Exception ex) {}

                // retrieve the attribute value
                long lsize = 1;
                for (int j=0; j<dims.length; j++) lsize *= dims[j];

                if (lsize <=0 )
                    continue;

                Object value = null;

                if (isVLEN || is_variable_str || isCompound)
                {
                    String[] strs = new String[(int)lsize];
                    for (int j=0; j<lsize; j++) strs[j] = "";
                    H5.H5AreadVL(aid, nativeType, strs);
                    value = strs;
                }
                else
                {
                    value = H5Datatype.allocateArray(nativeType, (int)lsize);
                    if (value == null) continue;

                    if (H5.H5Tget_class(nativeType)==HDF5Constants.H5T_ARRAY)
                        H5.H5Aread(aid, H5Datatype.toNative(H5.H5Tget_super(nativeType)), value);
                    else
                        H5.H5Aread(aid, nativeType, value);
                    int typeClass = H5.H5Tget_class(nativeType);
                    if (typeClass==HDF5Constants.H5T_STRING)
                        value = Dataset.byteToString((byte[])value, H5.H5Tget_size(nativeType));
                    else if (typeClass == HDF5Constants.H5T_REFERENCE)
                        value = HDFNativeData.byteToLong((byte[])value);
                }

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
     *
     * <OL>The basic HDF5 image attributes include:
     * <LI> The image identifier: name="CLASS", value="IMAGE"
     * <LI> The type of the image: name="IMAGE_SUBCLASS", value="IMAGE_TRUECOLOR" or "IMAGE_INDEXED"
     * <LI> The version of image: name="IMAGE_VERSION", value="1.2"
     * <LI> The range of data value: name="IMAGE_MINMAXRANGE", value=[0, 255]
     * <LI> The interlace mode: name="INTERLACE_MODE", value="INTERLACE_PIXEL" or "INTERLACE_PLANE"
     * <LI> The pointer to the palette dataset: name="PALETTE", value=reference id of the palette dataset
     * </OL>
     * For more information about HDF5 image attributes, please read the
     * <a href="http://hdf.ncsa.uiuc.edu/HDF5/doc/ADGuide/ImageSpec.html">
     *    HDF5 Image and Palette Specification</a>
     * <p>
     * @param dataset the image dataset which the attributes are added to.
     * @param interlace interlace the interlace mode of image data.
     * @throws Exception
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
        catch (Throwable ex) {}

        ver += vers[0] + "."+ vers[1] +"."+vers[2];

        return ver;
    }

    /**
     * Get an individual HObject with a given path. It deoes not load the whole
     * file structure.
     */
    public HObject get(String path) throws Exception
    {
        if (path == null || path.length() <= 0)
            return null;

        path = path.replace('\\', '/');
        if (!path.startsWith("/"))
            path = "/"+path;

        String name=null, pPath=null;
        if (path.equals("/"))
        {
            name = "/"; // the root
        } else
        {
            if (path.endsWith("/")) path = path.substring(0, path.length()-2);
            int idx = path.lastIndexOf('/');
            name = path.substring(idx+1);
            if (idx == 0) pPath = "/";
            else pPath = path.substring(0, idx);
            path = path+"/";

        }

        HObject obj = null;
        isReadOnly = false;

        if (fid < 0)
        {
            try {
                fid = H5.H5Fopen( fullFileName, HDF5Constants.H5F_ACC_RDWR, HDF5Constants.H5P_DEFAULT);
            } catch ( Exception ex) {
                isReadOnly = true;
                fid = H5.H5Fopen( fullFileName, HDF5Constants.H5F_ACC_RDONLY, HDF5Constants.H5P_DEFAULT);
            }
        }

        if (fid < 0) return null;

        int did=-1, gid=-1;

        try { did = H5.H5Dopen(fid, path);
        } catch (Exception ex) { did = -1; }
        try { gid = H5.H5Gopen(fid, path);
        } catch (Exception ex) { gid = -1; }

        // requested object is a dataset
        if (did > 0)
        {
            int tid=-1, tclass=-1;
            try {
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

            Dataset dataset = null;
            if (tclass == HDF5Constants.H5T_COMPOUND)
                dataset = new H5CompoundDS(this, name, pPath);
            else
                dataset = new H5ScalarDS( this, name, pPath);

            obj = dataset;
        }
        // requested object is a group
        else if (gid > 0)
        {
            H5Group group = new H5Group(this, name,pPath,null);
            obj = group;

            int nelems = 0;
            try {
                long[] nmembers = {0};
                H5.H5Gget_num_objs(gid, nmembers);
                nelems = (int)nmembers[0];
            } catch (HDF5Exception ex) {
                nelems = -1;
                gid = -1;
            }

            int[] oType = new int[1];
            String [] oName = new String[1];

            //get only one level objects in the group
            for ( int i = 0; i <nelems; i++)
            {
                oName[0] = "";
                oType[0] = -1;
                try {
                    H5.H5Gget_objname_by_idx(gid, i, oName, 80l);
                    oType[0] = H5.H5Gget_objtype_by_idx(gid, i);
                } catch (HDF5Exception ex) {
                    // do not stop if accessing one member fails
                    continue;
                }

                // create a new group
                if (oType[0] == HDF5Constants.H5G_GROUP)
                {
                    H5Group g = new H5Group(this,oName[0],path,group);
                    group.addToMemberList(g);
                } else if (oType[0] == HDF5Constants.H5G_DATASET)
                {
                    int did1=-1, tid1=-1, tclass1=-1;
                    try {
                        did1 = H5.H5Dopen(fid, path+oName[0]);
                        tid1 = H5.H5Dget_type(did);
                        tclass1 = H5.H5Tget_class(tid1);
                        if (tclass1 == HDF5Constants.H5T_ARRAY)
                        {
                            // for ARRAY, the type is determined by the base type
                            int btid = H5.H5Tget_super(tid1);
                            tclass1 = H5.H5Tget_class(btid);
                            try { H5.H5Tclose(btid); } catch (HDF5Exception ex) {}
                        }
                    } catch (HDF5Exception ex) {}
                    finally {
                        try { H5.H5Tclose(tid1); } catch (HDF5Exception ex) {}
                        try { H5.H5Dclose(did1); } catch (HDF5Exception ex) {}
                    }

                    Dataset d = null;

                    if (tclass1 == HDF5Constants.H5T_COMPOUND)
                        d = new H5CompoundDS(this,oName[0],path);
                    else
                        d = new H5ScalarDS(this,oName[0],path);
                    group.addToMemberList(d);
                }
            } // for ( i = 0; i < nelems; i++)
            try {H5.H5Gclose(gid);} catch (Exception ex2) {}
        } // request group

        return obj;
    }

}

