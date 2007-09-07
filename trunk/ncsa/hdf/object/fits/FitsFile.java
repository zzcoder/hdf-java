/*****************************************************************************
 * Copyright by The HDF Group.                                               *
 * Copyright by the Board of Trustees of the University of Illinois.         *
 * All rights reserved.                                                      *
 *                                                                           *
 * This file is part of the HDF Java Products distribution.                  *
 * The full copyright notice, including terms governing use, modification,   *
 * and redistribution, is contained in the files COPYING and Copyright.html. *
 * COPYING can be found at the root of the source code distribution tree.    *
 * Or, see http://hdfgroup.org/products/hdf-java/doc/Copyright.html.         *
 * If you do not have access to either file, you may request a copy from     *
 * help@hdfgroup.org.                                                        *
 ****************************************************************************/

package ncsa.hdf.object.fits;

import java.io.*;
import javax.swing.tree.*;
import ncsa.hdf.object.*;
import nom.tam.fits.*;

/**
 * This class provides file level APIs. File access APIs include retrieving the
 * file hierarchy, opening and closing file, and writing file content to disk.
 * <p>
 * @version 1.1 9/4/2007
 * @author Peter X. Cao
 */
public class FitsFile extends FileFormat
{
	public static final long serialVersionUID = HObject.serialVersionUID;
	
    /**
     * The root node of the file hierearchy.
     */
    private MutableTreeNode rootNode;

    /** the netcdf file */
    private Fits fitsFile;

    private static boolean isFileOpen;

    /**
     * Constructs an empty FitsFile with read-only access.
     */
    public FitsFile() {
        this("");
    }

    /**
     * Constructs an FitsFile object of given file name with read-only access.
     */
    public FitsFile(String pathname) {
        super(pathname);

        isReadOnly = true;
        isFileOpen = false;
        this.fid = -1;
        this.fullFileName = pathname;
        try { fitsFile = new Fits(fullFileName); }
        catch (Exception ex) {}
    }


    /**
     * Checks if the given file format is an HDF5 file.
     * <p>
     * @param fileformat the fileformat to be checked.
     * @return true if the given file is an HDF5 file; otherwise returns false.
     */
    public boolean isThisType(FileFormat fileformat) {
        return (fileformat instanceof FitsFile);
    }

    /**
     * Checks if a given file is an HDF5 file.
     * <p>
     * @param filename the file to be checked.
     * @return true if the given file is an HDF5 file; otherwise returns false.
     */
    public boolean isThisType(String filename)
    {
        boolean is_fits = false;
        RandomAccessFile raf = null;
        try { raf = new RandomAccessFile(filename, "r"); }
        catch (Exception ex) { raf = null; }

        if (raf == null) {
            try { raf.close();} catch (Exception ex) {}
            return false;
        }

        byte[] header = new byte[80];
        try { raf.read(header); }
        catch (Exception ex) { header = null; }

        if (header != null)
        {
            String front = new String(header, 0, 9);
            if (!front.startsWith("SIMPLE  =")) {
                try { raf.close();} catch (Exception ex) {}
                return false;
            }

            String back = new String(header, 9, 70);
            back = back.trim();
            if ((back.length() < 1) || (back.charAt(0) != 'T')) {
                try { raf.close();} catch (Exception ex) {}
                return false;
            }

            is_fits = true;;
        }

        try { raf.close();} catch (Exception ex) {}

        return is_fits;
    }


    /**
     * Creates an instance of a FitsFile with given file name and access flag.
     * <p>
     * @param pathname the full path name of the file.
     * @param flag the file access flag, must be READ for this file format.
     * <DL><DL>
     * <DT> READ <DD> Allow read-only access to file.</DT>
     * </DL></DL>
     */
    public FileFormat createInstance(String pathname, int access) throws Exception {
        return new FitsFile(pathname);
    }

    /*
     * @deprecated  As of 2.4, replaced by
     *                         {@link #createInstance(String, int)}
     */
    @Deprecated public FileFormat open(String fileName, int access) throws Exception
    {
        return new FitsFile(fileName);
    }

    // Implementing FileFormat
    public int open() throws Exception {
        if (!isFileOpen) {
            isFileOpen = true;
            rootNode = loadTree();
        }

        return 0;
    }

    private MutableTreeNode loadTree() {

        long[] oid = {0};
        FitsGroup rootGroup = new FitsGroup(
            this,
            getName(), // set the node name to the file name
            null, // root node does not have a parent path
            null, // root node does not have a parent node
            oid);

        DefaultMutableTreeNode root = new DefaultMutableTreeNode(rootGroup) {
        	public static final long serialVersionUID = HObject.serialVersionUID;

            public boolean isLeaf() { return false; }
        };

        if (fitsFile == null) {
            return root;
        }

        BasicHDU[] hdus = null;

        try { hdus = fitsFile.read(); }
        catch (Exception ex) {}

        if (hdus == null) {
            return root;
        }

        int n = hdus.length;
        int nImageHDU = 0;
        int nTableHDU =  0;
        String hduName = null;
        BasicHDU hdu = null;
        for (int i=0; i<n; i++) {
            hdu = hdus[i];
            hduName = null;
            // only deal with ImageHDU and TableHDU
            if (hdu instanceof ImageHDU) {
                hduName = "ImageHDU #"+nImageHDU++;
            } else if (hdu instanceof RandomGroupsHDU) {
                hduName = "RandomGroupsHDU #"+nImageHDU++;
            } else if (hdu instanceof TableHDU) {
                if (hdu instanceof AsciiTableHDU) {
                    hduName = "AsciiTableHDU #"+nTableHDU++;
                } else if (hdu instanceof BinaryTableHDU) {
                    hduName = "BinaryTableHDU #"+nTableHDU++;
                } else {
                    hduName = "TableHDU #"+nTableHDU++;
                }
            }

            if (hduName != null) {
                oid[0] = hdu.hashCode();
                FitsDataset d =  new FitsDataset(this, hdu, hduName, oid);
                DefaultMutableTreeNode node = new DefaultMutableTreeNode(d);
                root.add( node );
                rootGroup.addToMemberList(d);
            }
        }

        return root;
    }

    // Implementing FileFormat
    public void close() throws IOException {
        if (fitsFile == null) {
            return;
        }

        DataInput di = fitsFile.getStream();
        if (di instanceof InputStream) {
            ((InputStream)di).close();
        }
    }

    // Implementing FileFormat
    public TreeNode getRootNode() {
        return rootNode;
    }

    public Fits getFitsFile() {
        return fitsFile;
    }

    /**
     * Creates a new HDF5 file with given file name.
     * <p>
     * @param pathname the full path name of the file.
     * @return an instance of the new FitsFile.
     */
    public FileFormat create(String fileName) throws Exception {
        // not supported
        return new FitsFile(fileName);
    }

    // implementign FileFormat
    public Group createGroup(String name, Group pgroup) throws Exception {
        // not supported
        throw new UnsupportedOperationException("Unsupported operation for NetCDF.");
    }

    // implementign FileFormat
    public Datatype createDatatype(
        int tclass,
        int tsize,
        int torder,
        int tsign) throws Exception {
        // not supported
        throw new UnsupportedOperationException("Unsupported operation for NetCDF.");
    }

    // implementign FileFormat
    public Datatype createDatatype(
        int tclass,
        int tsize,
        int torder,
        int tsign,
        String name) throws Exception
    {
        throw new UnsupportedOperationException("Unsupported operation for NetCDF.");
    }

    // implementign FileFormat
    public HObject createLink(Group parentGroup, String name, HObject currentObj) throws Exception
    {
        throw new UnsupportedOperationException("createLink() is not supported");
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
        Object data) throws Exception {
        // not supported
        throw new UnsupportedOperationException("Unsupported operation.");
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
        throw new UnsupportedOperationException("Unsupported operation.");
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
        Object data) throws Exception {
        // not supported
        throw new UnsupportedOperationException("Unsupported operation.");
    }

    // implementign FileFormat
    public void delete(HObject obj) throws Exception {
        // not supported
        throw new UnsupportedOperationException("Unsupported operation.");
    }

    // implementign FileFormat
    public TreeNode copy(HObject srcObj, Group dstGroup) throws Exception {
        // not supported
        throw new UnsupportedOperationException("Unsupported operation.");
    }

    // implementign FileFormat
    public TreeNode copy(HObject srcObj, Group dstGroup, String dstName) throws Exception {
        // not supported
        throw new UnsupportedOperationException("Unsupported operation.");
    }

    /** copy a dataset into another group.
     * @param srcDataset the dataset to be copied.
     * @param pgroup teh group where the dataset is copied to.
     * @return the treeNode containing the new copy of the dataset.
     */

    private TreeNode copyDataset(Dataset srcDataset, FitsGroup pgroup)
         throws Exception {
        // not supported
        throw new UnsupportedOperationException("Unsupported operation.");
    }

    private TreeNode copyGroup(FitsGroup srcGroup, FitsGroup pgroup)
         throws Exception {
        // not supported
        throw new UnsupportedOperationException("Unsupported operation.");
    }

    /**
     * Copy attributes of the source object to the destination object.
     */
    public void copyAttributes(HObject src, HObject dst) {
        // not supported
        throw new UnsupportedOperationException("Unsupported operation.");
    }

    /**
     * Copy attributes of the source object to the destination object.
     */
    public void copyAttributes(int src_id, int dst_id) {
        // not supported
        throw new UnsupportedOperationException("Unsupported operation.");
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
    public void writeAttribute(HObject obj, ncsa.hdf.object.Attribute attr,
        boolean attrExisted) throws Exception {
        // not supported
        throw new UnsupportedOperationException("Unsupported operation.");
    }

    /**
     *  Returns the version of the HDF5 library.
     */
    public String getLibversion()
    {
        String ver = "NetCDF Java (version 2.1)";

        return ver;
    }

    // implementign FileFormat
    public HObject get(String path) throws Exception
    {
        throw new UnsupportedOperationException("get() is not supported");
    }
}

