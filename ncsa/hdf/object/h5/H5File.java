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
 * The HDF5 file structure is stored in a tree that is organlized by DefaultMutableTreeNode.
 * Each tree node represents an HDF5 object such as Group, Dataset or Named datatype.
 * Starting from the root, <i>rootNode</i>, one can trace the tree to find a
 * specific object.
 *
 * <p>
 * The following example show how to fing an object by a given path
 * <pre>
    HObject findObject(FileFormat file, String path)
    {
        if (file == null || path == null)
            return null;

        if (!path.endsWith("/"))
            path = path+"/";

        DefaultMutableTreeNode theRoot = (DefaultMutableTreeNode)file.getRootNode();

        if (theRoot == null)
            return null;
        else if (path.equals("/"))
            return (HObject)theRoot.getUserObject();

        Enumeration local_enum = ((DefaultMutableTreeNode)theRoot).breadthFirstEnumeration();
        DefaultMutableTreeNode theNode = null;
        HObject theObj = null;
        while(local_enum.hasMoreElements())
        {
            theNode = (DefaultMutableTreeNode)local_enum.nextElement();
            theObj = (HObject)theNode.getUserObject();
            String fullPath = theObj.getFullName()+"/";
            if (path.equals(fullPath))
                break;
        }

        return theObj;
    }
 * </pre>
 *
 * @version 1.0 12/12/2001
 * @author Peter X. Cao, NCSA
 */
public class H5File extends FileFormat
{
    /**
     * the file access flag. Valid values are HDF5Constants.H5F_ACC_RDONLY,
     *  HDF5Constants.H5F_ACC_RDWR and HDF5Constants.H5F_ACC_CREAT.
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
     * Constructs a empty H5File with read access.
     */
    public H5File()
    {
        this("", READ);
    }

    /**
     * Constructs an H5File object of given file name with read/write access.
     */
    public H5File(String pathname)
    {
        this(pathname, WRITE);
    }

    /**
     * Constructs an H5File object with given file name and access flag.
     * <p>
     * @param pathname the full path name of the file.
     * @param access the file access flag, it takes one of two values below:
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
        rootNode = null;

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
     * @param access the file access flag, it takes one of two values below:
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

    /**
     * Opens access to this file
     * @return the file identifier if successful; otherwise returns negative value.
     */
    public int open() throws Exception
    {
        return open(true);
    }

    /**
     * Opens access to this file.
     * @param loadFullHierarchy if true, load the full hierarchy into memory;
     *        otherwise just opens the file idenfitier.
     * @return the file identifier if successful; otherwise returns negative value.
     */
    private int open(boolean loadFullHierarchy) throws Exception
    {
        if ( fid >0 )
            return fid; // file is openned already

        // check for valid file access permission
        if ( flag < 0)
            throw new HDF5Exception("Invalid access identifer -- "+flag);
         else if (HDF5Constants.H5F_ACC_CREAT == flag)
        {
           // create a new file
            fid = H5.H5Fcreate(fullFileName,  HDF5Constants.H5F_ACC_TRUNC,
                HDF5Constants.H5P_DEFAULT, HDF5Constants.H5P_DEFAULT);
            H5.H5Fflush(fid, HDF5Constants.H5F_SCOPE_LOCAL);
            H5.H5Fclose(fid);
            flag = HDF5Constants.H5F_ACC_RDWR;
        }
        else if (!exists())
            throw new HDF5Exception("File does not exist -- "+fullFileName);
        else if ((flag == HDF5Constants.H5F_ACC_RDWR ||
            flag == HDF5Constants.H5F_ACC_CREAT) && !canWrite())
            throw new HDF5Exception("Cannot write file, try open as read-only -- "+fullFileName);
        else if ((flag == HDF5Constants.H5F_ACC_RDONLY) && !canRead())
            throw new HDF5Exception("Cannot read file -- "+fullFileName);

/*
        // BUG: HDF5Constants.H5F_CLOSE_STRONG does not flush cache
        try {
            //All open objects ramaining in the file are closed then file is closed
            plist = H5.H5Pcreate (HDF5Constants.H5P_FILE_ACCESS);
            H5.H5Pset_fclose_degree ( plist, HDF5Constants.H5F_CLOSE_STRONG);
        } catch (Exception ex) {;}
*/

        try {
            fid = H5.H5Fopen( fullFileName, flag, HDF5Constants.H5P_DEFAULT);
        } catch ( Exception ex) {
            isReadOnly = true;
            fid = H5.H5Fopen( fullFileName, HDF5Constants.H5F_ACC_RDONLY, HDF5Constants.H5P_DEFAULT);
        }

        if (fid>=0 && loadFullHierarchy)
        {
            // load the hierearchy of the file
            rootNode = loadTree();
        }

        return fid;
    }

    /**
     * Closes the file access and all its open objects
     * @throws HDF5Exception
     */
    public void close() throws HDF5Exception
    {
        // clean unused objects
        if (rootNode != null)
        {
            DefaultMutableTreeNode theNode = null;
            HObject theObj = null;
            Enumeration local_enum = ((DefaultMutableTreeNode)rootNode).breadthFirstEnumeration();
            while(local_enum.hasMoreElements())
            {
                theNode = (DefaultMutableTreeNode)local_enum.nextElement();
                theObj = (HObject)theNode.getUserObject();
                if (theObj instanceof Dataset) ((Dataset)theObj).clearData();
                theObj = null;
                theNode = null;
            }
        }

        // close all open objects associated to this file
        try {
            int n=0, type=-1, oids[];
            n = H5.H5Fget_obj_count(fid, HDF5Constants.H5F_OBJ_ALL);

            if ( n>0)
            {
                oids = new int[n];
                H5.H5Fget_obj_ids(fid, HDF5Constants.H5F_OBJ_ALL, n, oids);

                for (int i=0; i<n; i++)
                {
                    type = H5.H5Iget_type(oids[i]);
                    if (HDF5Constants.H5I_DATASET == type) {
                        try { H5.H5Dclose(oids[i]); } catch (Exception ex2) {}
                    } else if (HDF5Constants.H5I_GROUP == type) {
                        try { H5.H5Gclose(oids[i]); } catch (Exception ex2) {}
                    } else if (HDF5Constants.H5I_DATATYPE == type) {
                        try { H5.H5Tclose(oids[i]); } catch (Exception ex2) {}
                    } else if (HDF5Constants.H5I_ATTR == type) {
                        try { H5.H5Aclose(oids[i]); } catch (Exception ex2) {}
                    }
                } // for (int i=0; i<n; i++)
            } // if ( n>0)
        } catch (Exception ex) {}

        try { H5.H5Fflush(fid, HDF5Constants.H5F_SCOPE_GLOBAL); } catch (Exception ex) {}
        try { H5.H5Fclose(fid); } catch (Exception ex) {}

        fid = -1;
    }

    /**
     *
     * @return the root node of the file
     */
    public TreeNode getRootNode()
    {
        return rootNode;
    }

    /**
     *
     * @return the full path (path+name) of the file
     */
    public String getFilePath()
    {
        return fullFileName;
    }

    /**
     *
     * @return true if the file is read-only; otherwise returns false.
     */
    public boolean isReadOnly()
    {
        return isReadOnly;
    }

    /**
     * Creates a new HDF5 file with given a file name. If the file already exists,
     * erasing all data previously stored in the file.
     * <p>
     * @param fileName the full path name of the file.
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

    /**
     * Create a new group with the given name in a given parent group.
     *
     * @param name   The name fo the new group.
     * @param pgroup The parent group.
     * @return       The new group if successful; otherwise returns null
     */
    public Group createGroup(String name, Group pgroup) throws Exception
    {
        // create new group at the root
        if (pgroup == null)
            pgroup = (Group)this.get("/");

        return H5Group.create(name, pgroup);
    }

    /**
     * Creates a new datatype based on this FileFormat.
     * <p>
     * For example, the following code creates an instance of H5Datatype.
     * <pre>
     * FileFormat file = FileFormat.getFileFormat(FileFormat.FILE_TYPE_HDF5);
     * H5Datatype dtype = file.createDatatype(Datatype.CLASS_INTEGER,
     *     Datatype.NATIVE, Datatype.NATIVE, Datatype.NATIVE);
     * </pre>
     *
     * @param tclass The class of datatype, such as Integer, Float
     * @param tsize The size of the datatype in bytes
     * @param torder The order of the byte endianing
     * @param tsign The signed or unsinged of an integer
     * @return  The new datatype if successful; otherwise returns null
     */
    public Datatype createDatatype(
        int tclass,
        int tsize,
        int torder,
        int tsign) throws Exception
    {
        return new H5Datatype(tclass, tsize, torder, tsign);
    }

    /**
     * Creates a named datatype in file.
     *
     * @param tclass The class of datatype, such as Integer, Float
     * @param tsize The size of the datatype in bytes
     * @param torder The order of the byte endianing
     * @param tsign The signed or unsinged of an integer
     * @param name The full name (path + name) of the datatype to create
     * @return  The new datatype if successful; otherwise returns null
     */
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

    /**
     * Creates a hard link to an existing object in file.
     *
     * @param parentGroup The parent group for the new link
     * @param name The name of the new link
     * @param currentObj The object pointed by the new link
     * @return The an instance of the object pointed by the link if successful; otherwise returns null
     */
    public HObject createLink(Group parentGroup, String name, HObject currentObj) throws Exception
    {
        HObject obj = null;
        String current_full_name=null, new_full_name=null, parent_path=null;

        if (currentObj == null)
            throw new HDF5Exception("The object pointed by the link cannot be null.");

        if (currentObj instanceof Group && ((Group)currentObj).isRoot())
            throw new HDF5Exception("Cannot make a link to the root group.");

        if (parentGroup == null || parentGroup.isRoot())
            parent_path = HObject.separator;
        else
            parent_path = parentGroup.getPath()+HObject.separator+parentGroup.getName()+HObject.separator;

        new_full_name = parent_path+name;
        current_full_name = currentObj.getPath()+HObject.separator + currentObj.getName();

        H5.H5Glink(fid, HDF5Constants.H5G_LINK_HARD, current_full_name, new_full_name);

        if (currentObj instanceof Group)
            obj = new H5Group(this, name, parent_path, parentGroup, currentObj.getOID());
        else if (currentObj instanceof H5Datatype)
            obj = new H5Datatype(this, name, parent_path, currentObj.getOID());
        else if (currentObj instanceof H5CompoundDS)
            obj = new H5CompoundDS(this, name, parent_path, currentObj.getOID());
        else if (currentObj instanceof H5ScalarDS)
            obj = new H5ScalarDS(this, name, parent_path, currentObj.getOID());

        return obj;
    }

    /**
     * Creates a new dataset in this file.
     * <p>
     * The following example creates a 2D integer dataset of size 100X50 at the
     * root group in an HDF5 file.
     * <pre>
     * String name = "2D integer";
     * Group pgroup = (Group)((DefaultMutableTreeNode)getRootNode).getUserObject();
     * Datatype dtype = new H5Datatype(Datatype.CLASS_INTEGER, // class
     *                                 8,                      // size in bytes
     *                                 Datatype.ORDER_LE,      // byte order
     *                                 Datatype.SIGN_NONE);    // signed or unsigned
     * long[] dims = {100, 50};
     * long[] maxdims = dims;
     * long[] chunks = null; // no chunking
     * int gzip = 0; // no compression
     * Object data = null; // no initial data values
     *
     * Dataset d = (H5File)file.createScalarDS(name, pgroup, dtype, dims, maxdims, chunks, gzip, data);
     * </pre>
     *
     * @param name    The name of the new dataset
     * @param pgroup  The parent group where the new dataset is created.
     * @param type    The datatype of the new dataset.
     * @param dims    The dimension sizes of the new dataset.
     * @param maxdims The maximum dimension sizes of the new dataset.
     * @param chunks  The chunk sizes of the new dataset.
     * @param gzip    The compression level.
     * @param data    The data value of the new dataset.
     * @return        The new dataset if successful; otherwise returns null
     */
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

    /**
     * Create a new compound dataset in this file.
     * <p>
     * The following example creates a 2D compound dataset with size of 100X50 and
     * members x and y at the root group in an HDF5 file. Member x is an interger,
     * member y is an 1-D float array of size 10.
     * <pre>
     * String name = "2D compound";
     * Group pgroup = (Group)((DefaultMutableTreeNode)getRootNode).getUserObject();
     * long[] dims = {100, 50};
     * String[] memberNames = {"x", "y"}
     * Datatype[] memberDatatypes = {
     *     new H5Datatype(Datatype.CLASS_INTEGER, Datatype.NATIVE, Datatype.NATIVE, Datatype.NATIVE)
     *     new H5Datatype(Datatype.CLASS_FLOAT, Datatype.NATIVE, Datatype.NATIVE, Datatype.NATIVE));
     * int[] memberSizes = {1, 10};
     * Object data = null; // no initial data values
     *
     * Dataset d = (H5File)file.createCompoundDS(name, pgroup, dims, memberNames, memberDatatypes, memberSizes, null);
     * </pre>
     *
     * @param name            The name of the new dataset
     * @param pgroup          The parent group where the new dataset is created.
     * @param dims            The dimension sizes of the new dataset.
     * @param memberNames     The names of the members.
     * @param memberDatatypes The datatypes of the members.
     * @param memberSizes     The array sizes of the members.
     * @param data            The data value of the new dataset.
     * @return                The new dataset if successful; otherwise returns null
     */
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

    /**
     * Create a new compound dataset in this file.
     * <p>
     * The following example creates a 2D compound dataset with size of 100X50 and
     * members x and y at the root group in an HDF5 file. Member x is an interger,
     * member y is an 1-D float array of size 10.
     * <pre>
     * String name = "2D compound";
     * Group pgroup = (Group)((DefaultMutableTreeNode)getRootNode).getUserObject();
     * long[] dims = {100, 50};
     * String[] memberNames = {"x", "y"}
     * Datatype[] memberDatatypes = {
     *     new H5Datatype(Datatype.CLASS_INTEGER, Datatype.NATIVE, Datatype.NATIVE, Datatype.NATIVE)
     *     new H5Datatype(Datatype.CLASS_FLOAT, Datatype.NATIVE, Datatype.NATIVE, Datatype.NATIVE));
     * int[] memberSizes = {1, 10};
     * Object data = null; // no initial data values
     *
     * Dataset d = (H5File)file.createCompoundDS(name, pgroup, dims, memberNames, memberDatatypes, memberSizes, null);
     * </pre>
     *
     * @param name            The name of the new dataset
     * @param pgroup          The parent group where the new dataset is created.
     * @param dims            The dimension sizes of the new dataset.
     * @param maxdims         The maximum dimension sizes of the new dataset.
     * @param chunks          The chunk sizes of the new dataset.
     * @param gzip            The compression level.
     * @param memberNames     The names of the members.
     * @param memberDatatypes The datatypes of the members.
     * @param memberSizes     The array sizes of the members.
     * @param data            The data value of the new dataset.
     * @return                The new dataset if successful; otherwise returns null
     */
    public Dataset createCompoundDS(
        String name,
        Group pgroup,
        long[] dims,
        long[] maxdims,
        long[] chunks,
        int gzip,
        String[] memberNames,
        Datatype[] memberDatatypes,
        int[] memberSizes,
        Object data) throws Exception
    {
        int nMembers = memberNames.length;
        int memberRanks[] = new int[nMembers];
        int memberDims[][] = new int[nMembers][1];
        for (int i=0; i<nMembers; i++)
        {
            memberRanks[i] = 1;
            if (memberSizes==null)
                memberDims[i][0] = 1;
            else
                memberDims[i][0] = memberSizes[i];
        }

        return H5CompoundDS.create(name, pgroup, dims, maxdims, chunks, gzip,
            memberNames, memberDatatypes, memberRanks, memberDims, data);
    }

    /**
     * Create a new image at given parent group in this file.
     *
     * For example, to create a 2D image of size 100X50 at the root in an HDF5 file.
     * <pre>
     * String name = "2D image";
     * Group pgroup = (Group)((DefaultMutableTreeNode)getRootNode).getUserObject();
     * Datatype dtype = new H5Datatype(Datatype.CLASS_INTEGER, 1, Datatype.NATIVE, Datatype.SIGN_NONE);
     * long[] dims = {100, 50};
     * long[] maxdims = dims;
     * long[] chunks = null; // no chunking
     * int gzip = 0; // no compression
     * int ncomp = 2;
     * int interlace = ScalarDS.INTERLACE_PIXEL;
     * Object data = null; // no initial data values
     *
     * Dataset d = (H5File)file.createScalarDS(name, pgroup, dtype, dims,
     *     maxdims, chunks, gzip, ncomp, interlace, data);
     * </pre>
     *
     * @param name      The name of the new dataset
     * @param pgroup    The parent group where the new dataset is created.
     * @param type      The datatype of the new dataset.
     * @param dims      The dimension sizes of the new dataset.
     * @param maxdims   The maximum dimension sizes of the new dataset.
     * @param chunks    The chunk sizes of the new dataset.
     * @param gzip      The compression level.
     * @param ncomp     The number of components of the new image
     * @param interlace The interlace of this image.
     * @param data      The data value of the new image.
     * @return          The new image if successful; otherwise returns null
     */
    public Dataset createImage(
        String name,
        Group pgroup,
        Datatype type,
        long[] dims,
        long[] maxdims,
        long[] chunks,
        int gzip,
        int ncomp,
        int interlace,
        Object data) throws Exception
    {
        H5ScalarDS dataset = (H5ScalarDS)H5ScalarDS.create(name, pgroup, type, dims, maxdims, chunks, gzip, data);
        try { H5File.createImageAttributes(dataset, interlace); } catch (Exception ex) {}

        return dataset;
    }

    /**
     * Delete an object from the file.
     * @param obj The object to delete.
     */
    public void delete(HObject obj) throws Exception
    {
        if (obj == null || fid < 0)
            return;

        String name = obj.getPath()+obj.getName();
        H5.H5Gunlink(fid, name);
    }

    /**
     * Reads the file structure into memory (tree node)
     * @return the root node of the file structure.
     */
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

    /**
     * Reloads the sub-tree structure starting a given group
     * @param g the group to reload
     */
    public void reloadTree(Group g)
    {
        if (fid < 0 || rootNode == null || g==null) return;

        HObject theObj = null;
        DefaultMutableTreeNode theNode = null;

        if (g.equals(rootNode.getUserObject()))
            theNode = rootNode;
        else {
            Enumeration local_enum = rootNode.breadthFirstEnumeration();
            while(local_enum.hasMoreElements()) {
                theNode = (DefaultMutableTreeNode)local_enum.nextElement();
                theObj = (HObject)theNode.getUserObject();
                if (g.equals(theObj)) break;
            }
        }

        theNode.removeAllChildren();

        depth_first(theNode);
    }

    /**
     * Copy a data object to a group. The following example shows how to copy
     * an object to a given group.
     * <pre>
     public static void TestHDF5Copy (String filename, String objName) throws Exception
        {
            // Get the source dataset
            H5File file = new H5File(filename, H5File.READ);
            file.open();

            // Create a new file
            H5File newFile = (H5File) file.create(filename+"_new.h5");
            newFile.open();

            // NOTE: have to use the desitionation file to do the copy
            // Copy the dataset to the destination's root group
           Group group = (Group)newFile.get("/");
           file.copy(file.get(objName), group);

           // Make another copy but with different name
            file.copy(file.get(objName), group, "another_copy");

            file.close();
            newFile.close();
        }
     * </pre>
     * @param srcObj   The object to copy.
     * @param dstGroup The destination group for the new object.
     * @return The new node containing the new object.
     */
    public TreeNode copy(HObject srcObj, Group dstGroup) throws Exception
    {
        return this.copy(srcObj, dstGroup, null);
    }

    /**
     * Copy a data object to a group. The following example shows how to copy
     * an object to a given group.
     * <pre>
     public static void TestHDF5Copy (String filename, String objName) throws Exception
        {
            // Get the source dataset
            H5File file = new H5File(filename, H5File.READ);
            file.open();

            // Create a new file
            H5File newFile = (H5File) file.create(filename+"_new.h5");
            newFile.open();

            // NOTE: have to use the desitionation file to do the copy
            // Copy the dataset to the destination's root group
           Group group = (Group)newFile.get("/");
           file.copy(file.get(objName), group);

           // Make another copy but with different name
            file.copy(file.get(objName), group, "another_copy");

            file.close();
            newFile.close();
        }
     * </pre>
     * @param srcObj   The object to copy.
     * @param dstGroup The destination group for the new object.
     * @param dstName  The name of the new object. If dstName is null, the name
     *                 of the new object will be the same as srcObject.
     * @return The new node containing the new object.
     */
    public TreeNode copy(HObject srcObj, Group dstGroup, String dstName) throws Exception
    {
        TreeNode newNode = null;

        if (srcObj == null || dstGroup == null)
            return null;

        if (dstName == null)
            dstName = srcObj.getName();

        if (srcObj instanceof Dataset)
        {
           newNode = copyDataset((Dataset)srcObj, (H5Group)dstGroup, dstName);
        }
        else if (srcObj instanceof H5Group)
        {
            newNode = copyGroup((H5Group)srcObj, (H5Group)dstGroup, dstName);
        }
        else if (srcObj instanceof H5Datatype)
        {
            newNode = copyDatatype((H5Datatype)srcObj, (H5Group)dstGroup, dstName);
        }

        return newNode;
    }


    /** Copy a dataset into another group.
     * @param srcDataset the dataset to be copied.
     * @param pgroup the group where the dataset is copied to.
     * @param dstname the name of the new dataset
     * @return the treeNode containing the new copy of the dataset.
     */
    private TreeNode copyDataset(Dataset srcDataset, H5Group pgroup, String dstName)
         throws Exception
    {
        Dataset dataset = null;
        int srcdid, dstdid, tid, sid, plist;
        String dname=null, path=null;

        if (pgroup.isRoot())
            path = HObject.separator;
        else
            path = pgroup.getPath()+pgroup.getName()+HObject.separator;

        if (dstName == null || dstName.equals(HObject.separator) || dstName.length()<1)
            dstName = srcDataset.getName();
        dname = path + dstName;

        srcdid = srcDataset.open();
        tid = H5.H5Dget_type(srcdid);
        sid = H5.H5Dget_space(srcdid);
        plist = H5.H5Dget_create_plist(srcdid);

        // @@@@@@@@@ HDF5.1.6 bug at H5Dcreate(fid, dname, tid, sid, plist);
        dstdid = H5.H5Dcreate(pgroup.getFID(), dname, tid, sid, plist);

        // copy data values
        H5.H5Dcopy(srcdid, dstdid);

        // copy attributes from one object to the new object
        copyAttributes(srcdid, dstdid);

        byte[] ref_buf = H5.H5Rcreate(
            pgroup.getFID(),
            dname,
            HDF5Constants.H5R_OBJECT,
            -1);
        long l = HDFNativeData.byteToLong(ref_buf, 0);
        long[] oid = {l};

        if (srcDataset instanceof H5ScalarDS)
        {
            dataset = new H5ScalarDS(
                pgroup.getFileFormat(),
                dstName,
                path,
                oid);
        } else
        {
            dataset = new H5CompoundDS(
                pgroup.getFileFormat(),
                dstName,
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
                    pgroup.getFID(),
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

    /**
     * Copies a group and its members to a new location
     * @param srcGroup the source group
     * @param pgroup the location which the new group is located
     * @param dstName the name of the new group
     * @return the tree node containing the new group;
     */
    private TreeNode copyGroup(H5Group srcGroup, H5Group pgroup, String dstName)
         throws Exception
    {
        H5Group group = null;
        int srcgid, dstgid;
        String gname=null, path=null;

        if (pgroup.isRoot())
            path = HObject.separator;
        else
            path = pgroup.getPath()+pgroup.getName()+HObject.separator;

        if (dstName == null || dstName.equals(HObject.separator) || dstName.length()<1)
            dstName = srcGroup.getName();

        gname = path + dstName;
        srcgid = srcGroup.open();
        dstgid = H5.H5Gcreate(pgroup.getFID(), gname, 0);
        byte[] ref_buf = H5.H5Rcreate(
            pgroup.getFID(),
            gname,
            HDF5Constants.H5R_OBJECT,
            -1);
        long l = HDFNativeData.byteToLong(ref_buf, 0);
        long[] oid = {l};
        group = new H5Group(pgroup.getFileFormat(), dstName, path, pgroup, oid);

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

        try { srcGroup.close(srcgid); } catch (Exception ex) {}
        try { H5.H5Gclose(dstgid); } catch (Exception ex) {}

        return theNode;
    }

    /**
     * Copies a named datatype to another location
     * @param srcType the source datatype
     * @param pgroup the group which the new datatype is copied to
     * @param dstName the name of the new dataype
     * @return the tree node containing the new datatype.
     * @throws Exception
     */
    private TreeNode copyDatatype(Datatype srcType, H5Group pgroup, String dstName)
         throws Exception
    {
        Datatype datatype = null;
        int tid_src, tid_dst;
        String tname=null, path=null;

        if (pgroup.isRoot())
            path = HObject.separator;
        else
            path = pgroup.getPath()+pgroup.getName()+HObject.separator;

        if (dstName == null || dstName.equals(HObject.separator) || dstName.length()<1)
            dstName = srcType.getName();

        tname = path + dstName;
        tid_src = srcType.open();
        tid_dst = H5.H5Tcopy(tid_src);

        H5.H5Tcommit(pgroup.getFID(), tname, tid_dst );

        byte[] ref_buf = H5.H5Rcreate(
            pgroup.getFID(),
            tname,
            HDF5Constants.H5R_OBJECT,
            -1);
        long l = HDFNativeData.byteToLong(ref_buf, 0);
        long[] oid = {l};

        datatype = new H5Datatype(pgroup.getFileFormat(), dstName, path, oid);

        pgroup.addToMemberList(datatype);
        DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(datatype);

        try { H5.H5Tclose(tid_src); } catch(Exception ex) {}
        try { H5.H5Tclose(tid_dst); } catch(Exception ex) {}

        return newNode;
    }

    /**
     * Copies all attributes of the source object to the destionation object.
     * @param src the source object
     * @param dst the destination object
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
     * Copies all attributes of the source object to the destionation object.
     * @param src_id the identifier of the source object
     * @param dst_id the identidier of the destination object
     */
    public void copyAttributes(int src_id, int dst_id)
    {
        int aid_src=-1, aid_dst=-1, atid=-1, asid=-1, num_attr=-1;
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
                aid_dst = H5.H5Acreate(
                    dst_id,
                    aName[0],
                    atid,
                    asid,
                    HDF5Constants.H5P_DEFAULT );

                // use native data copy
                H5.H5Acopy(aid_src, aid_dst);

                /* does not work for variable length datatype
                H5.H5Aread(aid_src, atid, data_attr);
                H5.H5Awrite(aid_dst, atid, data_attr);
                */
            } catch (Exception ex) {}

            try { H5.H5Sclose(asid); } catch(Exception ex) {}
            try { H5.H5Tclose(atid); } catch(Exception ex) {}
            try { H5.H5Aclose(aid_src); } catch(Exception ex) {}
            try { H5.H5Aclose(aid_dst); } catch(Exception ex) {}
        } // for (int i=0; i<num_attr; i++)
    }

    /**
     * Updates the values of all reference datasets. Values of a references dataset
     * are relative file addresses in HDF5. When a file is saved or copied to a
     * new file, the values of reference dataset must be updated because the old
     * address do not make sense in the new file and may cause applications to crash.
     *
     * @param srcFile the source file
     * @param newFile the destinaton file where the references are updated
     * @throws Exception
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

    /**
     * Finds an object by its object ID
     *
     * @param file the file containing the object
     * @param oid the oid to search for
     * @return the object that has the given OID; otherwise returns null
     */
    private HObject findObject(FileFormat file, long[] oid)
    {
        if (file == null || oid == null)
            return null;

        HObject theObj = null;
        DefaultMutableTreeNode theNode = null;

        MutableTreeNode theRoot = (MutableTreeNode)file.getRootNode();
        if (theRoot == null) return null;

        Enumeration local_enum = ((DefaultMutableTreeNode)theRoot).breadthFirstEnumeration();
        while(local_enum.hasMoreElements())
        {
            theNode = (DefaultMutableTreeNode)local_enum.nextElement();
            theObj = (HObject)theNode.getUserObject();
            if (theObj.equalsOID(oid))
                break;
        }

        return theObj;
    }

    /**
     * Finds an object by the full path of the object (path+name)
     *
     * @param file the file containing the object
     * @param oid the path the full path of the object to search for
     * @return the object that has the given path; otherwise returns null
     */
    private HObject findObject(FileFormat file, String path)
    {
        if (file == null || path == null)
            return null;

        if (!path.endsWith("/"))
            path = path+"/";

        DefaultMutableTreeNode theRoot = (DefaultMutableTreeNode)file.getRootNode();

        if (theRoot == null)
            return null;
        else if (path.equals("/"))
            return (HObject)theRoot.getUserObject();

        Enumeration local_enum = ((DefaultMutableTreeNode)theRoot).breadthFirstEnumeration();
        DefaultMutableTreeNode theNode = null;
        HObject theObj = null;
        while(local_enum.hasMoreElements())
        {
            theNode = (DefaultMutableTreeNode)local_enum.nextElement();
            theObj = (HObject)theNode.getUserObject();
            String fullPath = theObj.getFullName()+"/";

            if (path.equals(fullPath))
                break;
            else
                theObj = null;
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
        }

        if (nelems <= 0 ) {
            pgroup.close(gid);
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
        catch (HDF5Exception ex) {; }

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
     * Returns the list of attriubtes for the given object location.
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
     * Creates required attributes for HDF5 image.
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
     * file structure. The following shows an example of how to use get().
     *
     */
    /**
     * Retrieves an individual object for a given path in the file. If the
     * request object is a group, it only retrieves the immediate members
     * of the group. It does not retrieve object in any sub-groups. The following
     * example shows how to use this function.
     * <pre>
         public static void TestHDF5Get (String filename) throws Exception
        {
            H5File file = new H5File(filename, H5File.READ);
            Group group = (Group)file.get("/Group0");
            System.out.println(group);
            file.close();
        }
     * </pre>
     *
     * @param path the full path of the object (path+name)
     * @return the object if successful; otherwise return false.
     * @throws Exception
     */
    public HObject get(String path) throws Exception
    {
        HObject obj = null;

        if (path == null || path.length() <= 0)
            return null;

        // replace the wrong slash and get rid of "//"
        path = path.replace('\\', '/');
        path = "/"+path;
        path = path.replaceAll("//", "/");

        // the whole file tree is loaded. find the object in the tree
        if (rootNode != null)
            obj = findObject(this, path);

        if (obj != null)
            return obj;

        // open only the requested object
        String name=null, pPath=null;
        if (path.equals("/"))
        {
            name = "/"; // the root
        } else
        {
            // separate the parent path and the object name
            if (path.endsWith("/")) path = path.substring(0, path.length()-2);
            int idx = path.lastIndexOf('/');
            name = path.substring(idx+1);
            if (idx == 0) pPath = "/";
            else pPath = path.substring(0, idx);
        }

        // do not open the full tree structure, only the file handler
        fid = open(false);
        if (fid < 0) return null;

        // check if the requested object is a group or dataset
        int did=-1, gid=-1;
        try { did = H5.H5Dopen(fid, path);
        } catch (Exception ex) { did = -1; }
        try { gid = H5.H5Gopen(fid, path);
        } catch (Exception ex) { gid = -1; }

        if (did > 0) {
            // open a dataet
            try { obj = getDataset(did, name, pPath); }
            finally { try { H5.H5Dclose(did); } catch (Exception ex) {} }
        } else if (gid > 0) {
            // open a group
            try {
                H5Group pGroup = null;
                if (pPath != null) {
                    long[] oid = null;
                    try {
                        byte[] ref_buf = H5.H5Rcreate(fid, pPath, HDF5Constants.H5R_OBJECT, -1);
                        long l = HDFNativeData.byteToLong(ref_buf, 0);
                        oid = new long[1];
                        oid[0] = l;
                    } catch (Exception ex) {oid = null;}

                    pGroup = new H5Group(this, null, pPath, null, oid);
                    obj = getGroup(gid, name, pGroup);
                    pGroup.addToMemberList(obj);
                } else
                    obj = getGroup(gid, name, pGroup);
            } finally {
                try { H5.H5Gclose(gid); } catch (Exception ex) {}
            }
        } // else if (gid > 0) {

        return obj;
    }

    /**
     * Constructs a dataset for a given dataset idenfitier.
     * @param did the dataset idenfifier
     * @param name the name of the dataset
     * @param path the path of the dataset
     * @return the dataset if successful; otherwise return null.
     * @throws HDF5Exception
     */
    private Dataset getDataset(int did, String name, String path) throws HDF5Exception
    {
        Dataset dataset = null;
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
        }finally {
            try { H5.H5Tclose(tid); } catch (HDF5Exception ex) {}
        }

        long[] oid = null;
        try {
            byte[] ref_buf = H5.H5Rcreate(fid, path+"/"+name, HDF5Constants.H5R_OBJECT, -1);
            long l = HDFNativeData.byteToLong(ref_buf, 0);
            oid = new long[1];
            oid[0] = l;
        } catch (Exception ex) { oid = null;}

        if (tclass == HDF5Constants.H5T_COMPOUND)
            dataset = new H5CompoundDS(this, name, path, oid);
        else
            dataset = new H5ScalarDS( this, name, path, oid);

        return dataset;
    }

    /**
     * Constructs a group with a given group identifier and retieves the members
     * of the group.
     *
     * @param gid the group identifier
     * @param name the group name
     * @param pGroup the parent group
     * @return the group if successful; otherwise returns false.
     * @throws HDF5Exception
     */
    private H5Group getGroup(int gid, String name, Group pGroup) throws HDF5Exception
    {
        String parentPath = null;
        String thisFullName = null;
        String memberFullName = null;

        if (pGroup == null) { // root
            thisFullName = name = "/";
        }
        else {
            parentPath = pGroup.getFullName();
            if (parentPath == null || parentPath.equals("/"))
                thisFullName = "/" + name;
            else
                thisFullName = parentPath +"/" + name;
        }

        // get rid of any extra "/"
        if (parentPath != null)
            parentPath = parentPath.replaceAll("//", "/");
        if (thisFullName !=null)
            thisFullName = thisFullName.replaceAll("//", "/");

        long[] oid = null;
        try {
            byte[] ref_buf = H5.H5Rcreate(fid, thisFullName, HDF5Constants.H5R_OBJECT, -1);
            long l = HDFNativeData.byteToLong(ref_buf, 0);
            oid = new long[1];
            oid[0] = l;
        } catch (Exception ex) { oid = null;}

        H5Group group = new H5Group(this, name, parentPath, pGroup, oid);

        int nelems = 0;
        try {
            long[] nmembers = {0};
            H5.H5Gget_num_objs(gid, nmembers);
            nelems = (int)nmembers[0];
        } catch (HDF5Exception ex) {
            nelems = -1;
        }

        //get only one level objects in the group
        int[] oType = new int[1];
        String [] oName = new String[1];
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
                try {
                    byte[] ref_buf = H5.H5Rcreate(fid, thisFullName+"/"+oName[0], HDF5Constants.H5R_OBJECT, -1);
                    long l = HDFNativeData.byteToLong(ref_buf, 0);
                    oid = new long[1];
                    oid[0] = l;
                } catch (Exception ex) { oid = null;}

                H5Group g = new H5Group(this, oName[0], thisFullName, group, oid);
                group.addToMemberList(g);
            } else if (oType[0] == HDF5Constants.H5G_DATASET)
            {
                int did=-1;
                Dataset d = null;

                if (thisFullName == null || thisFullName.equals("/"))
                    memberFullName = "/"+oName[0];
                else
                    memberFullName = thisFullName+"/"+oName[0];

                try {
                    did = H5.H5Dopen(fid, memberFullName);
                    d = getDataset(did, oName[0], thisFullName);
                } finally {
                    try { H5.H5Dclose(did); } catch (HDF5Exception ex) {}
                }
                group.addToMemberList(d);
            }
        }  // for ( i = 0; i < nelems; i++)

       return group;
    }

}

