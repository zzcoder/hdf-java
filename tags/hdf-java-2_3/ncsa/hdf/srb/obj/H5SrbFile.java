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

package ncsa.hdf.srb.obj;

import java.io.File;
import java.util.*;
import javax.swing.tree.*;
import java.lang.reflect.Array;

import ncsa.hdf.object.*;
import ncsa.hdf.srb.h5srb.*;


public class H5SrbFile extends FileFormat
{
    public static final int H5FILE_OP_ERROR            = -1;
    public static final int H5FILE_OP_OPEN             = 0;
    public static final int H5FILE_OP_CLOSE            = 1;
    public static final int H5FILE_OP_CREATE           = 2;

    private int opID;
    private H5SrbGroup rootGroup;
    private final String fullFileName;
    private boolean isFileOpen = false;
    private String srbInfo[]; /* srbHost, srbPort, srbAuth, userName, domainName*/

    /**
     * The root node of the file hierearchy.
     */
    private DefaultMutableTreeNode rootNode;

    /**
     * Constructs an empty H5File with read-only access.
     */
    public H5SrbFile()
    {
        this("");
    }

    /**
     * Constructs an H5File object of given file name with read-only access.
     */
    public H5SrbFile(String pathname)
    {
        this (null, pathname);
    }

    /**
     * Constructs an H5File object of given file name with read-only access.
     */
    public H5SrbFile(String[] srb, String pathname)
    {
        super(pathname);
        opID = -1;
        rootGroup = null;
        fullFileName = pathname;
        srbInfo = srb;

        long[] oid = {0};
        rootGroup = new H5SrbGroup( this, getName(), null, null, oid);
        rootNode = new DefaultMutableTreeNode(rootGroup)
        {
            public boolean isLeaf() { return false; }
        };
    }

    /**
     * Opens access to the file resource and returns the file identifier.
     * If the file is already open, it returns the file access identifier.
     * <p>
     * @return the file access identifier if opening the file is succsessful;
     * otherwise returns a negative value.
     */
    public int open() throws Exception
    {
        if ( srbInfo == null || srbInfo.length<5) return -1;

        int status = -1;
        opID = H5FILE_OP_OPEN;

       /* there is bug somewhere in the code. This open() is called
          in a infinite loop if thereis more there one groups at the
          root level.
          cannot figure out where is the bug.
          --- Peter Cao
        */

        if (!isFileOpen) /* make sure it is called only once */
        {
            isFileOpen = true;
            status =  H5SRB.h5ObjRequest (srbInfo, this, H5SRB.H5OBJECT_FILE);
            constructTree(rootGroup, rootNode);
        }

        return status;
    }

    /* construct the tree structure from groups */
    private void constructTree(Group pgroup, DefaultMutableTreeNode pnode)
    {
        int n = 0;
        Object obj;
        List list = pgroup.getMemberList();
        DefaultMutableTreeNode node;

        if (list == null || (n=list.size())<=0)
            return;

        for (int i=0; i<n; i++)
        {
            obj = list.get(i);

            if (obj instanceof Group)
            {
                node = new DefaultMutableTreeNode(obj) { public boolean isLeaf() { return false; } };
                pnode.add( node );
                constructTree((Group)obj, node);
            }
            else
            {
                node = new DefaultMutableTreeNode(obj);
                pnode.add( node );
            }
        }
    }

    /**
     * Open a file and returns an instance of implementing class of the FileFormat.
     * <p>
     * @param pathname the full path name of the file.
     * @param flag the file access flag, it takes one of two values below:
     */
    public FileFormat open(String pathname, int access) throws Exception
    {
        return new H5SrbFile(pathname);
    }

    /**
     * Closes access to the file resource.
     */
    public void close() throws Exception
    {
        if ( srbInfo == null || srbInfo.length<5) return;

        opID = H5FILE_OP_CLOSE;
        H5SRB.h5ObjRequest (srbInfo, this, H5SRB.H5OBJECT_FILE);
    }

    // Implementing FileFormat
    public TreeNode getRootNode()
    {
        return rootNode;
    }

    /** Get SRB server information:
     *  srbHost, srbPort, srbAuth, userName, domainName
     */
    public String[] getSrbInfo() { return srbInfo; }

    /**
     * Returns the full path of the file: file path + file name.
     */
    public String getFilePath() { return fullFileName; }

    /**
     * Returns the true if the file is read-only, otherwise returns false.
     */
    public boolean isReadOnly() { return true; }

    // Implementing FileFormat
    public FileFormat create(String fileName) throws Exception
    {
        throw new UnsupportedOperationException("H5SrbFile.create() is not implemented.");
    }

    // Implementing FileFormat
    public Group createGroup(String name, Group pgroup) throws Exception
    {
        throw new UnsupportedOperationException("H5SrbFile.createGroup() is not implemented.");
    }

    // Implementing FileFormat
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
        throw new UnsupportedOperationException("H5SrbFile.createScalarDS() is not implemented.");
    }

    // Implementing FileFormat
    public Dataset createCompoundDS(
        String name,
        Group pgroup,
        long[] dims,
        String[] memberNames,
        Datatype[] memberDatatypes,
        int[] memberSizes,
        Object data) throws Exception
    {
        throw new UnsupportedOperationException("H5SrbFile.createCompoundDS(...) is not implemented.");
    }

    // Implementing FileFormat
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
        throw new UnsupportedOperationException("H5SrbFile.createCompoundDS() is not implemented.");
    }

    // Implementing FileFormat
    public Datatype createDatatype(
        int tclass,
        int tsize,
        int torder,
        int tsign) throws Exception
    {
        throw new UnsupportedOperationException("H5SrbFile.createDatatype() is not implemented.");
    }

    /**
     *  Returns the version of the HDF5 library.
     */
    public String getLibversion() { return "HDF5" ; }

    // Implementing FileFormat
    public  boolean isThisType(String filename) { return true; }

    // Implementing FileFormat
    public boolean isThisType(FileFormat fileformat)
    {
        return (fileformat instanceof H5SrbFile);
    }

    // Implementing FileFormat
    public void writeAttribute(HObject obj, Attribute attr,
        boolean attrExisted) throws Exception
    {
         throw new UnsupportedOperationException("H5SrbFile.writeAttribute() is not implemented.");
    }

    // Implementing FileFormat
    public TreeNode copy(HObject srcObj, Group dstGroup) throws Exception
    {
        throw new UnsupportedOperationException("H5SrbFile.copy() is not implemented.");
    }

    // Implementing FileFormat
    public void delete(HObject obj) throws Exception
    {
        throw new UnsupportedOperationException("H5SrbFile.delete() is not implemented.");
    }

}

