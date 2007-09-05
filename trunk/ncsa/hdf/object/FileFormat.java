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

package ncsa.hdf.object;

import java.util.*;
import java.io.File;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;

/**
 * FileFormat class defines general interfaces for retriving file structure 
 * from file,  creating/removing objects in/from file, and etc.
 * <p>
 * FileFormat is a pluggable component. An implementing class of FileFormat 
 * can be added to the list of supported file formats. Current implementing 
 * classes include
 * H5File and H4File. By default, H5File and H4File are added to the list of 
 * supported file formats.
 *
 *  <pre>
 *                                    FileFormat
 *                       _________________|_________________
 *                       |                |                |
 *                     H5File          H4File           Other...
 * </pre>
 *
 * <p>
 * @version 2.4 9/4/2007
 * @author Peter X. Cao
 */
public abstract class FileFormat extends File
{
    /***************************************************************************
     * These file access flags are used in calls to newInstance( String, flag );
     **************************************************************************/

    /** 
     * File access flag for read-only permission. 
     * With this access flag, modifications to the file will not be allowed.
     */
    public final static int READ = 0;

    /** 
     * File access flag for read/write permission.
     * With this access flag, modifications to the file will be allowed.
     */
    public final static int WRITE = 1;

    /** 
     * File access flag for creating/truncating with read-write permission.
     * If the file already exists, it will be truncated when opened.
     * With this access flag, modifications to the file will be allowed.
     */
    public final static int CREATE = 2;

    /** 
     * File access flag for creating/opening with read-write permission.
     * If the file already exists, it will not be truncated when opened.
     * With this access flag, modifications to the file will be allowed.
     */
    public final static int CREATE_OPEN = 3;

    /** Key for JPEG image file type.*/
    public static final String FILE_TYPE_JPEG = "JPEG";

    /** Key for TIFF image file type. */
    public static final String FILE_TYPE_TIFF = "TIFF";

    /** Key for PNG image file type. */
    public static final String FILE_TYPE_PNG = "PNG";

    /***************************************************************************
     * These keys are used in the list of supported file formats.  (FileList)
     **************************************************************************/

    /** Key for HDF4 file format. */
    public static final String FILE_TYPE_HDF4 = "HDF";

    /** Key for HDF5 file format. */
    public static final String FILE_TYPE_HDF5 = "HDF5";

    /**
     *  FileList keeps a list of supported FileFormats.
     *  This list can be updated and queried at runtime.
     *
     *  @see #addFileFormat(String,FileFormat)
     *  @see #getFileFormat(String)
     *  @see #getFileFormatKeys()
     *  @see #getFileFormats()
     *  @see #removeFileFormat(String)
     */
    private static final Map<String,FileFormat> FileList = 
					new Hashtable<String,FileFormat>(10);

    /**
     * Current Java applications, such as HDFView, cannot handle files with 
     * large numbers of objects due to JVM memory limitations.  
     * For example, 1,000,000 objects is too many.
     * max_members is defined so that applications such as HDFView will load
     * up to <i>max_members</i> objects starting with the <i>start_members</i>
     * -th object.
     */
    private int max_members = 10000;		 // 10,000 by default

    /**
     * Current Java applications, such as HDFView, cannot handle files with 
     * large numbers of objects due to JVM memory limitations.
     * For example, 1,000,000 objects is too many.
     * max_members is defined so that applications such as HDFView will load
     * up to <i>max_members</i> objects starting with the <i>start_members</i>
     * -th object.
     */
    private int start_members = 0; 		// 0 by default
    
    /** 
     * Total number of objects in memory 
     */
    private int n_members = 0;

    /** 
     * A list of file extensions for the supported file types. 
     */
    private static String extensions = "hdf, h4, hdf5, h5";

    /**
     * File identifier.
     */
    protected int fid = -1;

    /**
     * The absolute pathname of the file.
     */
    protected String fullFileName = null;

    /** 
     * Flag indicating if the file is open read only. 
     */
    protected boolean isReadOnly = false;

    /***************************************************************************
     * Static initialization methods start here
     **************************************************************************/

    /**
     * By default, we add HDF4 and HDF5 file formats to the supported formats 
     * list.   
     */
    static {
        // add default HDF4 modules
        try {
            Class fileclass = Class.forName("ncsa.hdf.object.h4.H4File");
            FileFormat fileformat = (FileFormat)fileclass.newInstance();
            if (fileformat != null) {
                FileFormat.addFileFormat("HDF", fileformat);
            }
        } catch (Throwable err ) {;}

        // add default HDF5 modules
        try {
            Class fileclass = Class.forName("ncsa.hdf.object.h5.H5File");
            FileFormat fileformat = (FileFormat)fileclass.newInstance();
            if (fileformat != null) {
                FileFormat.addFileFormat("HDF5", fileformat);
            }
        } catch (Throwable err ) {;}
    }

    /***************************************************************************
     * Public methods start here
     **************************************************************************/

    /** 
     * Creates a new FileFormat instance by converting the given pathname
     * string into an abstract pathname.
     * <p>
     * As with the java.io.File class, the pathname corresponds to the 
     * file name.
     * 
     * @param pathname A pathname string.
     * @throws NullPointerException If the <code>pathname</code> argument 
     *                              is <code>null</code>.
     * @see java.io.File#File(String)
     */
    public FileFormat(String pathname) {
        super(pathname);
	fullFileName = getAbsolutePath();
    }

    /**
     * Creates a FileFormat instance with specified pathname and access.
     * <p>
     * This method creates an instance of the FileFormat implementing class
     * and sets the pathname and file access parameters.  
     * As with the java.io.File class, the pathname corresponds to the 
     * file name.
     * <p>
     * The file is not opened as part of this call.  
     * The file open is carried out by the open() call.
     * <p>
     * For example (ignoring possible exceptions):
     * <pre>
     *  // Request the implementing class of FileFormat: H5File
     *  FileFormat h5file = FileFormat.getFileFormat(FileFormat.FILE_TYPE_HDF5);
     *     
     *  // Create an instance of H5File object with read/write access
     *  H5File test1 = (H5File)h5file.newInstance("test_hdf5.h5", 
     *                                             FileFormat.WRITE);
     *
     *  // Open the file and load the file structure; file id is returned.
     *  int fid = test1.open;
     *
     *  // Create an instance of H5File object with create/truncate access
     *  H5File test2 = (H5File)h5file.newInstance("new_hdf5.h5", 
     *                                             FileFormat.CREATE);
     *   
     *  // Create the file, or open with truncate if file already exists,  
     *  // and load the file structure of the file; file id is returned.
     *  int fid2 = test2.open();
     * </pre> 
     *
     * @param pathname A pathname string.
     * @param access The file access flag, which determines behavior when file
     *               is opened.  
     * <p>
     * The flag takes one of following values:
     * <DL>
     * <DT> READ <DD> Read-only access; fail if file doesn't exist.</DT>
     * <DT> WRITE <DD> Read/Write access; fail if file doesn't exist.</DT>
     * <DT> CREATE <DD> Read/Write access; create a new file or truncate 
     *                  an existing one. </DT>
     * <DT> CREATE_OPEN <DD> Read/Write access; create a new file or open 
     *                  an existing one. </DT>
     * </DL>
     * @see #FileFormat(String)
     */
    public abstract FileFormat newInstance(String pathname, int access) throws Exception;

    /**
     * Opens file and returns a file identifier.
     * <p>
     * This method uses the <code>pathname</code> and <code>access</code>
     * parameters specified in the {@link #newInstance(String, int)} call
     * to open the file.  It returns the file identifier if successful,
     * or a negative value in case of failure.
     * <p> 
     * The method also loads the file structure and basic information 
     * (name, type) for data objects in the file into the FileFormat instance.
     * It does not load the contents of any data object.
     * <p>
     * The structure of the file is stored in a tree starting from the 
     * root node. The root node is a Java TreeNode object that represents 
     * the root group of a file. 
     * <p>
     * Starting from the root, applications can descend through the hierarchy 
     * and navigate among the file's data objects.
     *  
     * @see #newInstance(String, int)
     * @see #getRootNode()
     *
     * @return File access identifier if successful; otherwise -1.
     */
    public abstract int open() throws Exception;

    /**
     * Returns File identifier of open file associated with this instance.
     * 
     * @return The file identifer or -1 if no file open.
     */
    public int getFID() { return fid; }

    /**
     * Closes access to file associated with this instance.
     * <p>
     * This method closes the file associated with this FileFormat instance,
     * as well as all objects associated with the file.
     * <p>
     * COME BACK AND TALK ABOUT EXCEPTIONS, ETC.  Why do we try to close if
     * fid is -1 when called?   Also, make sure we set isReadOnly to false
     * when file is closed... or maybe not.
     *
     * @see #fid 
     * @see #getInstance(String, int)
     * @see #open()
     */
    public abstract void close() throws Exception;

    /** 
     * Returns the total number of hobjects in memory.
     * <p>
     * getNumberOfMembers() returns the total number of objects loaded into memory. The total 
     * number of objects in memory may be different from the total number of objects in a file. 
     * <p>
     * For example, if a file contains 20,000 objects, the application only loads the first 
     * 10,000 objects in memory. In this case, getNumberOfMembers() returns 10,000 instead 
     * of 20,000.
     * 
     * @see #setMaxMembers(int)
     * @see #setStartMembers(int)
     * 
     * @return The total number of objects in memory.
     */
    public final int getNumberOfMembers() {
    	
    	if (n_members > 0) {
            return n_members;
        }
    	
        Enumeration local_enum = ((DefaultMutableTreeNode)getRootNode()).depthFirstEnumeration();

        while(local_enum.hasMoreElements()) {
            local_enum.nextElement();
            n_members++;
        }
        
        return n_members;
    }
    
    /**
     * Returns the root node that contains the file structure of a file.
     * <p>
     * A file structure is stored in a tree constructed by tree nodes 
     * (javax.swing.tree.DefaultMutableTreeNode). In a tree
     * structure, internal nodes represent groups. Leaf nodes represent datasets, named
     * datatypes or empty groups.
     * <p>
     * The root node represents the root group. Using the methods provided by
     * javax.swing.tree.DefaultMutableTreeNode, applocations can easily find objects
     * in the tree.
     * 
     * @return The root node of the file.
     */
    public abstract TreeNode getRootNode();

    /**
     * Returns the absolute path for the file.
     * <p>
     * For example, "/samples/hdf5_test.h5".
     * 
     * @return The full path (file path + file name) of the file.
     */
    public final String getFilePath() {
	return fullFileName;
    } 

    /**
     * Returns true if the file is open read-only.
     *
     * If the file is open read-write, or if the file is not open,
     * false will be returned.  VERIFY
     * 
     * @see #getInstance(String, int)
     * @see #open()
     * 
     * @return true if the file is read-only, otherwise returns false.
     */
    public final boolean isReadOnly() {
    	return isReadOnly;
    }

    /**
     * Creates a new group with a name in a group.
     *
     * @param name   The name of a new group.
     * @param pgroup The parent group object.
     * @return       The new group if successful; otherwise returns null.
     */
    public abstract Group createGroup(String name, Group pgroup) throws Exception;

    /**
     * Creates a new dataset in a file with/without chunking/compression.
     * <p>
     * The following example creates a 2D integer dataset of size 100X50 at the
     * root group in an HDF5 file.
     * <pre>
     * String name = "2D integer";
     * Group pgroup = (Group)((DefaultMutableTreeNode)getRootNode).getUserObject();
     * Datatype dtype = new H5Datatype(Datatype.CLASS_INTEGER, // class
     *                                 4,                      // size in bytes
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
     * @param name    name of the new dataset, e.g. "2D integer"
     * @param pgroup  parent group where the new dataset is created.
     * @param type    datatype of the new dataset.
     * @param dims    dimension sizes of the new dataset, e.g. long[] dims = {100, 50}.
     * @param maxdims maximum dimension sizes of the new dataset, null if maxdims is the same as dims.
     * @param chunks  chunk sizes of the new dataset, null if no chunking.
     * @param gzip    GZIP compression level (1 to 9), 0 or negative values if no compression.
     * @param data    data written to the new dataset, null if no data is written to the new dataset.
     * 
     * @return        The new dataset if successful; otherwise returns null
     */
    public abstract Dataset createScalarDS(
        String name,
        Group pgroup,
        Datatype type,
        long[] dims,
        long[] maxdims,
        long[] chunks,
        int gzip,
        Object data) throws Exception;

    /**
     * @deprecated  Not for public use in the future. <br>
     * Using {@link #createCompoundDS(String, Group, long[], long[], long[], int, String[], Datatype[], int[], Object)}
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
        return createCompoundDS(name, pgroup, dims, null, null, -1,
               memberNames, memberDatatypes, memberSizes, data);
    }

    /**
     * Creates a new compound dataset in a file with/without chunking and compression.
     * <p>
     * The following example creates a compressed 2D compound dataset with size of 100X50 in a root group.
     * The compound dataset has two members, x and y. Member x is an interger,
     * member y is an 1-D float array of size 10.
     * <pre>
     * String name = "2D compound";
     * Group pgroup = (Group)((DefaultMutableTreeNode)getRootNode).getUserObject();
     * long[] dims = {100, 50};
     * long[] chunks = {1, 50};
     * int gzip = 9;
     * String[] memberNames = {"x", "y"};
     * 
     * Datatype[] memberDatatypes = {
     *     new H5Datatype(Datatype.CLASS_INTEGER, Datatype.NATIVE, Datatype.NATIVE, Datatype.NATIVE)
     *     new H5Datatype(Datatype.CLASS_FLOAT, Datatype.NATIVE, Datatype.NATIVE, Datatype.NATIVE));
     *     
     * int[] memberSizes = {1, 10};
     * Object data = null; // no initial data values
     *
     * Dataset d = (H5File)file.createCompoundDS(name, pgroup, dims, null, chunks, gzip, memberNames, memberDatatypes, memberSizes, null);
     * </pre>
     *
     * @param name            name of the new dataset
     * @param pgroup          parent group where the new dataset is created.
     * @param dims            dimension sizes of the new dataset.
     * @param maxdims         maximum dimension sizes of the new dataset, null if maxdims is the same as dims.
     * @param chunks          chunk sizes of the new dataset, null if no chunking.
     * @param gzip            GZIP compression level (1 to 9), 0 or negative values if no compression.
     * @param memberNames     names of the members.
     * @param memberDatatypes datatypes of the members.
     * @param memberSizes     array sizes of the members.
     * @param data            data written to the new dataset, null if no data is written to the new dataset.
     * 
     * @return                new dataset object if successful; otherwise returns null
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
        // subclass to implement it
        throw new UnsupportedOperationException("Dataset FileFormat.createCompoundDS(...) is not implemented.");
    }

    /**
     * Creates a new image in a file.
     * <p>
     * The following example creates a 2D image of size 100X50 in a root group.
     * <pre>
     * String name = "2D image";
     * Group pgroup = (Group)((DefaultMutableTreeNode)getRootNode).getUserObject();
     * Datatype dtype = new H5Datatype(Datatype.CLASS_INTEGER, 1, Datatype.NATIVE, Datatype.SIGN_NONE);
     * long[] dims = {100, 50};
     * long[] maxdims = dims;
     * long[] chunks = null; // no chunking
     * int gzip = 0; // no compression
     * int ncomp = 3; // RGB true color image
     * int interlace = ScalarDS.INTERLACE_PIXEL;
     * Object data = null; // no initial data values
     *
     * Dataset d = (H5File)file.createScalarDS(name, pgroup, dtype, dims,
     *     maxdims, chunks, gzip, ncomp, interlace, data);
     * </pre>
     *
     * @param name      name of the new image, "2D image".
     * @param pgroup    parent group where the new image is created.
     * @param type      datatype of the new image.
     * @param dims      dimension sizes of the new dataset, e.g. long[] dims = {100, 50}.
     * @param maxdims   maximum dimension sizes of the new dataset, null if maxdims is the same as dims.
     * @param chunks    chunk sizes of the new dataset, null if no chunking.
     * @param gzip      GZIP compression level (1 to 9), 0 or negative values if no compression.
     * @param ncomp     number of components of the new image, e.g. int ncomp = 3; // RGB true color image.
     * @param interlace interlace mode of the image. Valid values are ScalarDS.INTERLACE_PIXEL, ScalarDS.INTERLACE_PLANEL and ScalarDS.INTERLACE_LINE.
     * @param data      data value of the image, null if no data.
     * 
     * @return          The new image object if successful; otherwise returns null
     */
    public abstract Dataset createImage(
        String name,
        Group pgroup,
        Datatype type,
        long[] dims,
        long[] maxdims,
        long[] chunks,
        int gzip,
        int ncomp,
        int interlace,
        Object data) throws Exception;

    /**
     * Creates a new datatype in memory.
     * <p>
     * The following code creates an instance of H5Datatype in memory.
     * <pre>
     * FileFormat file = FileFormat.getFileFormat(FileFormat.FILE_TYPE_HDF5);
     * H5Datatype dtype = file.createDatatype(Datatype.CLASS_INTEGER,
     *     4, Datatype.NATIVE, Datatype.NATIVE);
     * </pre>
     *
     * @param tclass class of datatype, e.g. Datatype.CLASS_INTEGER
     * @param tsize  size of the datatype in bytes, e.g. 4 for 32-bit integer.
     * @param torder order of the byte endian, e.g. Datatype.ORDER_LE.
     * @param tsign  signed or unsinged of an integer, Datatype.SIGN_NONE.
     * 
     * @return       The new datatype object if successful; otherwise returns null.
     */
    public abstract Datatype createDatatype(
        int tclass,
        int tsize,
        int torder,
        int tsign) throws Exception;

    /**
     * Creates a named datatype in a file.
     *<p>
     * The following code creates a named datatype in a file.
     * <pre>
     * FileFormat file = FileFormat.getFileFormat(FileFormat.FILE_TYPE_HDF5);
     * H5Datatype dtype = file.createDatatype(Datatype.CLASS_INTEGER,
     *     4, Datatype.NATIVE, Datatype.NATIVE, "Native Integer");
     * </pre>
     *
     * @param tclass class of datatype, e.g. Datatype.CLASS_INTEGER
     * @param tsize  size of the datatype in bytes, e.g. 4 for 32-bit integer.
     * @param torder order of the byte endianing, Datatype.ORDER_LE.
     * @param tsign  signed or unsinged of an integer, Datatype.SIGN_NONE.
     * @param name name of the datatype to create, e.g. "Native Integer".
     * 
     * @return  The new datatype if successful; otherwise returns null
     */
    public abstract Datatype createDatatype(
        int tclass,
        int tsize,
        int torder,
        int tsign,
        String name) throws Exception;

    /**
     * Creates a hard link pointing to an existing object in file.
     *<p>
     * @param parentGroup The parent group for the new link.
     * @param name The name of the new link.
     * @param currentObj The object pointed by the new link.
     * 
     * @return The object pointed by the new link if successful; otherwise returns null
     */
    public abstract HObject createLink(Group parentGroup, String name, HObject currentObj) throws Exception;

    /*****************************************************************
     * Methods related to the FileList.
     *****************************************************************/

    /**
     * Adds a FileFormat with specified key to the list of supported formats.
     * <p>
     * This method allows a new FileFormat, tagged with an identifying
     * key, to be added dynamically to the list of supported File Formats.  
     * Using it, applications can add new File Formats at runtime. 
     * <p>
     * For example, to add a new File Format with the key "xyz" that is
     * implemented by the class xyzFile in the package companyC.files,
     * an application would make the following calls:
     * <pre>
     *    Class fileClass = Class.forName( "companyC.files.xyzFile" );
     *    FileFormat ff = (FileFormat) fileClass.newInstance();
     *    if ( ff != null ) {
     *       ff.addFileFormat ("xyz", ff )
     *    }
     * </pre>
     * <p>
     * If either <code>key</code> or <code>fileformat</code> are null, or if 
     * <code>key</code> is already in use, the method returns without updating  
     * the list of supported File Formats.
     *
     * @param 	key        A string that identifies the FileFormat.
     * @param 	fileformat An instance of the FileFormat to be added.
     * 
     */
    public static void addFileFormat(String key, FileFormat fileformat) {
        if ((fileformat == null) || (key == null)) {
            return;
        }

        key = key.trim();
   
        if (!FileList.containsKey(key)) {
            FileList.put(key, fileformat);
        }
    }

    /**
     * Returns the FileFormat with specified key from the list of supported 
     * formats.
     * <p>
     * This method returns a FileFormat instance, as identified by an
     * identifying key, from the list of supported File Formats.
     * <p>
     * If the specified key is in the list of supported formats, 
     * the instance of the associated FileFormat object is returned.
     * If the specified key is not in the list of supported formats,
     * null is returned.
     *
     * @param 	key 	A string that identifies the FileFormat.
     * 
     * @return The FileFormat that matches the given key, or null if the key
     *         is not found in the list of supported File Formats.
     */
    public static FileFormat getFileFormat(String key) {
        return (FileFormat)FileList.get(key);
    }

    /**
     * Returns an Enumeration of keys for all supported formats.
     * <p>
     * This method returns an Enumeration containing the unique keys (Strings)
     * for the all File Formats in the list of supported File Formats.
     *
     * @return An Enumeration of keys that are in the list of supported formats.
     */
    public static final Enumeration getFileFormatKeys() {
        return ((Hashtable)FileList).keys();
    }


    /** Returns an array of all FileFormat instances in the list of supported 
     * formats.
     * <p> 
     * This method returns an array of FileFormat instances that appear in 
     * the list of supported File Formats.
     * <p>
     * If the list of supported formats is empty, null is returned.
     *
     * @return An array of all FileFormat instances in the list of supported
     *         File Formats, or null if the list is empty.
     */
    public static FileFormat[] getFileFormats()
    {
        int n = FileList.size();
        if ( n <=0 ) {
            return null;
        }

        int i = 0;
        FileFormat[] fileformats = new FileFormat[n];
        Enumeration local_enum = ((Hashtable)FileList).elements();
        while (local_enum.hasMoreElements()) {
            fileformats[i++] = (FileFormat)local_enum.nextElement();
        }

        return fileformats;
    }

    /**
     * Removes the FileFormat with specified key from the list of supported 
     * formats.
     * <p>
     * This method removes a FileFormat, as identified by an
     * identifying key, from the list of supported File Formats.
     * <p>
     * If the specified key is in the list of supported formats, 
     * the instance of the FileFormat object that is being removed from 
     * the list is returned.
     * If the key is not in the list of supported formats,
     * null is returned.   
     *
     * @param key 	A string that identifies the FileFormat to 
     *                  be removed.
     *
     * @return The FileFormat that is removed, or null if the key is not
     *			found in the list of supported File Formats.
     */
    public static FileFormat removeFileFormat(String key) {
        return (FileFormat) FileList.remove(key);
    }

    /*****************************************************************
     * End of Methods related to the FileList.
     *****************************************************************/

    /**
     *  Returns the version of the library for the file.
     *
     * @return The library version if successful; otherwise returns null
     */
    public abstract String getLibversion();

    /**
     * Checks if a file is an instance of the FileFormat.
     * <p>
     * For example, if "test.h5" is an HDF5 file, H5File.isThisType("test.h5") returns
     * true while H4File.isThisType("test.h5") will return false.
     * <pre>
     * FileFormat file = FileFormat.getFileFormat(FileFormat.FILE_TYPE_HDF5);
     * boolean isH5 = file.isThisType("test.h5");
     * </pre>
     *
     * @param filename The name of the file to be checked.
     * @return true if the file is this type; otherwise returns false.
     */
    public abstract boolean isThisType(String filename);

    /**
     * Checks if a given FileFormat is an instance of this FileFormat.
     * <ul>
     *     This function is needed for tow main reasons:
     *     <li> since hdf-java products were designed in such a way 
     *     that the GUI components only have access to the abstract object 
     *     layer, applications need this function to check if a file instance 
     *     is this file type.
     *     <li> hdf-java applications such as HDFView support plug-in file
     *     formats (FileFormats are loaded at runtime), The Java "instanceof" operation
     *     cannot check if an object is an instance of a FileFormat that is loaded at runtime.
     *     Instead, we use this method to perform the "instanceof" operation.
     * </ul>
     * For example, in HDFView, we use the following code to check if a file is an HDF5 file.
     * <pre>
        FileFormat h5file = FileFormat.getFileFormat(FileFormat.FILE_TYPE_HDF5);
        HObject hObject = viewer.getTreeView().getCurrentObject();
        FileFormat thisFile = hObject.getFileFormat();
        boolean isH5 = h5file.isThisType(thisFile);
     * </pre> 
     * where, isH5 is true if thisFile is an HDF5 file; otherwise, isH5 is false.
     * <p>
     * @param fileformat the Fileformat to be checked.
     * @return true if the given FileFormat is an instance of this FileFormat; otherwise returns false.
     */
    public abstract boolean isThisType(FileFormat fileformat);

    /**
     * Attaches a given attribute to an object.
     * <p>
     * If the attribute does not exists, creates an attibute in file,
     * and attches it the object. If the attribute already exists in the object, 
     * just update the value of the attribute attached to the object.
     *
     * <p>
     * @param obj The object to which the attribute is attached to.
     * @param attr The atribute to attach.
     * @param attrExisted The indicator if the given attribute exists.
     */
    public abstract void writeAttribute(HObject obj, Attribute attr, boolean attrExisted) throws Exception;

    /**
     * @deprecated  Not for public use in the future. <br>
     * Using {@link #copy(HObject, Group, String)}
     */
    public abstract TreeNode copy(HObject srcObj, Group dstGroup) throws Exception;

    /**
     * Copies a given object to a specific group with a new name. 
     * <p>
     * This method copies an object (source) to a specific group (destination) within 
     * a file or cross files. If the destination group is in a different file, the 
     * file which the destination group is located at must be the same file type as 
     * the source file. Copying object cross file format is not supported. For example,
     * an HDF4 dataset cannot be copied to an HDF5 file, and vice versa.
     * <p>
     * The source object can be a group, a dataset or a named datatype. This method 
     * copies the object along with all its attributes and other properties. If the source
     * object is a group, this method also copies all the objects and sub-groups below 
     * the group.
     * <p>
     * The following example shows how to copy an HDF5 object.
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
     * 
     * @param srcObj   The object to copy.
     * @param dstGroup The destination group for the new object.
     * @param dstName  The name of the new object. If dstName is null, the name
     *                 of the new object will be the same as srcObject.
     *                 
     * @return The tree node that contains the new object.
     */
    public abstract TreeNode copy(HObject srcObj, Group dstGroup, String dstName) throws Exception;

    /**
     * Deletes an object from a file.
     * 
     * @param obj The object to delete.
     */
    public abstract void delete(HObject obj) throws Exception;

    /**
     * Sets the maximum number of objects to be loaded into memory.
     * <p>
     * Current Java application such as HDFView cannot handle files with large
     * number of objects such 1,000,000 objects due to JVM memory limitation.
     * A maximum number is used so that applications such as HDFView will load
     * up to the maximum number of objects.
     *
     * @param n The maximum number of objects to be loaded into memory
     */
    public void setMaxMembers(int n) { max_members = n; }

    /**
     * Sets the starting index of objects to be loaded into memory.
     * <p>
     * Current Java application such as HDFView cannot handle files with large
     * number of objects such 1,000,000 objects due to JVM memory limitation.
     * A maximum number is used so that applications such as HDFView will load
     * up to the maximum number of objects.
     *
     * @param idx The starting index of the object to be loaded into memory
     */
    public void setStartMembers(int idx) { start_members = idx; }

    /**
     * Returns the maximum number of objects to be loaded into memory.
     * <p>
     * <p>
     * Current Java application such as HDFView cannot handle files with large
     * number of objects such 1,000,000 objects due to JVM memory limitation.
     * A maximum number is used so that applications such as HDFView will load
     * up to the maximum number of objects.
     *
     * @return The maximum number of objects to be loaded into memory
     */
    protected int getMaxMembers() { return max_members; }

    /**
     * Returns an index of the starting object to be loaded into memory.
     * <p>
     * <p>
     * Current Java application such as HDFView cannot handle files with large
     * number of objects such 1,000,000 objects due to JVM memory limitation.
     * A maximum number is used so that applications such as HDFView will load
     * up to the maximum number of objects.
     *
     * @return The index of the starting object to be loaded into memory.
     */
    protected int getStartMembers() { return start_members; }

    /**
     * Returns a list of file extensions for all supported file formats.
     * <p> 
     * The extensions are separates by comma, such as "hdf, h4, hdf5, h5, hdf4, he4, he5"
     *
     * @return A list of file extensions for all supported file formats.
     */
    public static String getFileExtensions() { return extensions; }

    /**
     * Adds a file extension to the file extension list.
     * 
     * @param extension The file extension to add
     */
    public static void addFileExtension(String extension)
    {
        if ((extensions == null) || (extensions.length() <=0))
        {
            extensions = extension;
        }

        StringTokenizer currentExt = new StringTokenizer(extensions, ",");
        Vector tokens = new Vector(currentExt.countTokens()+5);

        while (currentExt.hasMoreTokens())
        {
            tokens.add(currentExt.nextToken().trim().toLowerCase());
        }

        currentExt = new StringTokenizer(extension, ",");
        String ext = null;
        while (currentExt.hasMoreTokens())
        {
            ext = currentExt.nextToken().trim().toLowerCase();
            if (tokens.contains(ext)) {
                continue;
            }

            extensions = extensions + ", "+ext;
        }
        
        tokens.setSize(0); 
    }

    /**
     * Creates an instance of a FileFormat object corresponding to the data in a file.
     * <p>
     * The file name may be an absolute or a relative path. This function checks the list of registered
     * FileFormats and returns an instance of the matched one, or null if none is matched.
     * <p>
     * For example, if "test_hdf5.h5" is an HDF5 file, FileFormat.getInstance("test_hdf5.h5")
     * returns an instance of H5File, i.e. H5File f=(H5File)FileFormat.getInstance("test_hdf5.h5");
     *
     * @param fileName a valid file name.
     * 
     * @return an instance of the matched FileFormat; otherwise returns null
     */
    public static FileFormat getInstance(String fileName) throws Exception
    {
        if ((fileName == null) || (fileName.length()<=0)) {
            throw new IllegalArgumentException("Invalid file name. "+fileName);
        }

        if (!(new File(fileName)).exists()) {
            throw new IllegalArgumentException("File does not exists");
        }

        FileFormat fileformat = null;
        FileFormat theformat = null;
        Enumeration elms = ((Hashtable)FileList).elements();
        while(elms.hasMoreElements())
        {
            theformat = (FileFormat)elms.nextElement();
            if (theformat.isThisType(fileName))
            {
                Class fileclass = theformat.getClass();
                Object[] intiargs = {fileName};
                Class[] paramClass = {fileName.getClass()};
                try {
                    java.lang.reflect.Constructor  constructor = fileclass.getConstructor(paramClass);
                    fileformat = (FileFormat)constructor.newInstance(intiargs);
                } catch (Exception ex) {}
                break;
            }
        }

        return fileformat;
    }

    /**
     * @deprecated  Not for public use in the future.<br>
     * Using {@link #get(String)}
     * 
     * <p>
     * This static method causes two problems: 1) It can be very expensive if 
     * if is called many times or in a loop because each call to the method 
     * creates an instance of a file. 2)Since the method does not return the 
     * instance of the file, the file cannot be closed directly and may be left
     * for open (memory leak). The only way to close the file is through
     * the object returned by this method, such as,
     * <p>
     * <pre>
     * Dataset dset = H5File.getObject("/tmp/hdf5_test.h5#//images/iceberg");
     * ....
     * // close the file through dset
     * dset.getFileFormat().close();
     * </pre> 
     */
    public static final HObject getHObject(String fullPath) throws Exception
    {
        if ((fullPath == null) || (fullPath.length() <=0)) {
            return null;
        }

        String filename=null, path=null;
        int idx = fullPath.indexOf("#//");

        if (idx >0 )
        {
            filename = fullPath.substring(0, idx);
            path = fullPath.substring(idx+3);
            if ((path == null) || (path.length() == 0)) {
                path = "/";
            }
        }
        else
        {
            filename = fullPath;
            path = "/";
        }

        return FileFormat.getHObject(filename, path);
    };

    /**
     * @deprecated  Not for public use in the future.<br>
     * Using {@link #get(String)}
     * 
     * <p>
     * This static method causes two problems: 1) It can be very expensive if 
     * if is called many times or in a loop because each call to the method 
     * creates an instance of a file. 2)Since the method does not return the 
     * if is called many times or in a loop because each call to the method 
     * creates an instance of a file. 2)Since the method does not return the 
     * instance of the file, the file cannot be closed directly and may be left
     * for open (memory leak). The only way to close the file is through
     * the object returned by this method, such as,
     * <p>
     * <pre>
     * Dataset dset = H5File.getObject("hdf5_test.h5", "/images/iceburg");
     * ....
     * // close the file through dset
     * dset.getFileFormat().close();
     * </pre> 
     */
    public static final HObject getHObject(String filename, String path) throws Exception
    {
        if ((filename == null) || (filename.length()<=0)) {
            throw new IllegalArgumentException("Invalid file name. "+filename);
        }

        if (!(new File(filename)).exists()) {
            throw new IllegalArgumentException("File does not exists");
        }

        HObject obj = null;
        FileFormat file = FileFormat.getInstance(filename);

        if (file != null) {
            obj = file.get(path);
            if (obj == null) {
                file.close();
            }
        }
        

        return obj;
    }

    /**
     * Gets an HObject with a given path from a file. 
     * <p>
     * The way of how the get() method retrieves an object depends on 
     * if {@link #open()} method is called.
     * <ul>
     *   <li> If {@link #open()} is called before get() is called, the
     *        full structure of file is loaded into memory. The get() 
     *        finds and returns the object in memory. For a group, the get()
     *        method returns the group and all the members under the group.
     *        In the following example, get("/g0") returns the group "/g0" 
     *        and everything below.
     *        <pre>
              /g0                      Group
              /g0/dataset_comp         Dataset {50, 10}
              /g0/dataset_int          Dataset {50, 10}
              /g0/g00                  Group
              /g0/g00/dataset_float    Dataset {50, 10}
              /g0/g01                  Group
              /g0/g01/dataset_string   Dataset {50, 10}
              </pre>
     *   <li> If {@link #open()} is not called, the get() method retrieves
     *        dataset_comp, dataset_int) and two empty groups (g00, g01).
     *        <pre>
              /g0                      Group
              /g0/dataset_comp         Dataset {50, 10}
              /g0/dataset_int          Dataset {50, 10}
              /g0/g00                  Group
              /g0/g00/dataset_float    Dataset {50, 10}
              /g0/g01                  Group
              /g0/g01/dataset_string   Dataset {50, 10}
              </pre>
     * <p>
     * The following example shows how use the get() to get a group object from file
     * <pre>
         public static void TestHDF5Get (String filename) throws Exception
        {
            H5File file = new H5File(filename, H5File.READ);
            Group group = (Group)file.get("/Group0");
            System.out.println(group);
            file.close();
        }
     * </pre>
     * @param path full path of a data object in a file
     * @return The object if it exists in the file; otherwise returns null
     */
    public abstract HObject get(String path) throws Exception;

    /***************************************************************************
     * Deprecated methods.
     **************************************************************************/

    /**
     * @deprecated  As of 2.4, replaced by 
     *                         {@link #newInstance(String, int)}
     *   The replacement method has an additional parameter
     *   that controls the behavior if the file already exists.
     *   Use FileFormat.CREATE as the second argument in the replacement
     *   method to mimic the behavior originally provided by this method.
     *   The newInstance() method does not attempt to create the actual file
     *   until the open() method is called.
     */
    @Deprecated public abstract FileFormat create(String fileName) throws Exception;

    /**
     * @deprecated  As of 2.4, replaced by 
     *                         {@link #newInstance(String, int)}
     *   
     *   The replacement method has identical functionality and a more
     *   descriptive name.
     */
    @Deprecated public abstract FileFormat open(String pathname, int access) throws Exception;

}
