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
import java.io.File;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;

/**
 * This FileFormat defines general I/O accessing interfaces to file resources,
 * such as open/close file, and retrieve file structure.
 * <p>
 * FileFormat is a plugable component. An implementing class of FileFormat can be
 * added to the supported file list. The current implementing classes include
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
 * @version 1.0 12/12/2001
 * @author Peter X. Cao, NCSA
 */
public abstract class FileFormat extends File
{
    /** File access flag for for read only. */
    public final static int READ = 0;

    /** File access flag for read and write. */
    public final static int WRITE = 1;

    /** Flag for creating a new file. */
    public final static int CREATE = 2;

    /** Flag for deleting an existing file when creating a new file */
    public final static int FILE_CREATE_DELETE = 10;

    /** Flag for open an existing file when creating a new file (do not delete)*/
    public final static int FILE_CREATE_OPEN = 11;

    /** JPEG image file type.*/
    public static final String FILE_TYPE_JPEG = "JPEG";

    /** TIFF image file type. */
    public static final String FILE_TYPE_TIFF = "TIFF";

    /** PNG image file type. */
    public static final String FILE_TYPE_PNG = "PNG";

    /** HDF4 file type. */
    public static final String FILE_TYPE_HDF4 = "HDF";

    /** HDF5 file type. */
    public static final String FILE_TYPE_HDF5 = "HDF5";

    /**
     *  FileList keeps a list current supported file formats.
     *  The FileList contains <key, fileFormat> pairs, such as
     *  <"HDF5", ncsa.hdf.object.h5.H5File>
     */
    private static final Map FileList = new Hashtable(10);

    /**
     * Current Java application such as HDFView cannot handle files with large
     * number of objects such 1,000,000 objects due to JVM memory  limitation.
     * The max_members is defined so that applications such as HDFView will load
     * up to <i>max_members</i> number of objects starting the <i>start_members</i>
     * -th object.
     */
    private int max_members = 10000; // 10,000 by default

    /**
     * Current Java application such as HDFView cannot handle files with large
     * number of objects such 1,000,000 objects due to JVM memory  limitation.
     * The max_members is defined so that applications such as HDFView will load
     * up to <i>max_members</i> number of objects starting the <i>start_members</i>
     * -th object.
     */
    private int start_members = 0; // 0 by default
    
    /* Total number of objects in memory */
    private int n_members = 0;

    /** a list of file extensions for the supported file types */
    private static String extensions = "hdf, h4, hdf5, h5";

    /**
     * file identifier for the open file.
     */
    protected int fid = -1;

    /**
     * By default, we add HDF4 and HDF5 file types to the supported file list
     */
    static {
        // add default HDF4 modules
        try {
            Class fileclass = Class.forName("ncsa.hdf.object.h4.H4File");
            FileFormat fileformat = (FileFormat)fileclass.newInstance();
            if (fileformat != null)
                FileFormat.addFileFormat("HDF", fileformat);
        } catch (Throwable err ) {;}

        // add default HDF5 modules
        try {
            Class fileclass = Class.forName("ncsa.hdf.object.h5.H5File");
            FileFormat fileformat = (FileFormat)fileclass.newInstance();
            if (fileformat != null)
                FileFormat.addFileFormat("HDF5", fileformat);
        } catch (Throwable err ) {;}
    }

    /** Constructs a FileFormat with a given file name.
     * @param filename The full name of the file.
     */
    public FileFormat(String filename) {
        super(filename);
    }

    /**
     * Opens access to the file resource and returns the file identifier.
     * If the file is already open, it returns the file access identifier.
     *
     * @return The file access identifier if successful; otherwise returns a negative value.
     */
    public abstract int open() throws Exception;

    /**
     * Opens a file and returns an instance of implementing class of the FileFormat.
     *
     * @param pathname The full path name of the file.
     * @param access The file access flag, it takes one of two values below:
     * @return The new file if successful; otherwise returns null
     */
    public abstract FileFormat open(String pathname, int access) throws Exception;

    /**
     * Create a new instance of this file. If file exists, delete it, then
     * create a new file and open the new file for read/write.
     * <p>
     * A subclass must implementing this method to create a file of its type.
     * For example,
     * <pre>
     * FileFormat file = H5File.create("test.h5");
     * </pre>
     * creates an HDF5 file "test.h5".
     *
     * @param fileName The file to open.
     * @return A new instance of the FileFormat with the given file name
     * @return The new file if successful; otherwise returns null
     */
    public abstract FileFormat create(String fileName) throws Exception;

    /**
     * Create a new instance of this file. If file exists, delete it, then
     * create a new file and open the new file for read/write.
     * <p>
     * A subclass must implementing this method to create a file of its type.
     * For example,
     * <pre>
     * FileFormat file = H5File.create("test.h5");
     * </pre>
     * creates an HDF5 file "test.h5".
     *
     * @param fileName The file to create/open
     * @param createFlag The file creation option. Allowable values are:
     * <pre>
     *         FILE_CREATE_DELETE -- If file exists, delete it, then create a new file
     *                               and opens the file for read/write (default).
     *                               If file does not exist, create a new file,
     *                               and opens the file for read/write (default).
     *
     *         FILE_CREATE_OPEN   -- If file exists, do not delete it, just open the file
     *                               If file does not exist, create a new file,
     *                               and opens the file for read/write (default).
     * </pre>
     *
     * @return The new file if successful; otherwise returns null
     * @throws Exception
     * @see <a href="#open(java.lang.String, int)">open(java.lang.String, int)</a>
     * @see <a href="#create(java.lang.String)">create(java.lang.String)</a>
     */
    public final FileFormat create(String fileName, int createFlag) throws Exception
    {
        FileFormat theFile = null;

        File f = new File(fileName);
        if (f.exists() && (FILE_CREATE_OPEN == createFlag))
            theFile = open(fileName, WRITE);
        else
            theFile = create(fileName);

        return theFile;
    }

    public final int getNumberOfMembers() {
    	
    	if (n_members > 0) // calculate only once
    		return n_members;
    	
        Enumeration local_enum = ((DefaultMutableTreeNode)getRootNode()).depthFirstEnumeration();

        while(local_enum.hasMoreElements()) {
            local_enum.nextElement();
            n_members++;
        }
        
        return n_members;
    }
    
    /**
     * Closes the access to the file resource.
     */
    public abstract void close() throws Exception;

    /**
     * Returns the root node of the file.
     * <p>
     * The root node contains the hierarchy of the file. For file with hierarchical
     * structure such as HDF, the structure is stored in a tree structure. The root
     * of the tree is the root of the the file.
     */
    public abstract TreeNode getRootNode();

    /**
     * Returns the full path of the file: file path + file name.
     */
    public abstract String getFilePath();

    /**
     * Returns true if the file is read-only, otherwise returns false.
     */
    public abstract boolean isReadOnly();

    /**
     * Create a new group with the given name in a given parent group.
     *
     * @param name   The name fo the new group.
     * @param pgroup The parent group.
     * @return       The new group if successful; otherwise returns null
     */
    public abstract Group createGroup(String name, Group pgroup) throws Exception;

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
        return createCompoundDS(name, pgroup, dims, null, null, -1,
               memberNames, memberDatatypes, memberSizes, data);
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
        // subclass to implement it
        throw new UnsupportedOperationException("Dataset FileFormat.createCompoundDS(...) is not implemented.");
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
    public abstract Datatype createDatatype(
        int tclass,
        int tsize,
        int torder,
        int tsign) throws Exception;

    /**
     * Creates a named datatype in file.
     *
     * @param tclass The class of datatype, such as Integer, Float
     * @param tsize The size of the datatype in bytes
     * @param torder The order of the byte endianing
     * @param tsign The signed or unsinged of an integer
     * @param name The name of the datatype to create
     * @return  The new datatype if successful; otherwise returns null
     */
    public abstract Datatype createDatatype(
        int tclass,
        int tsize,
        int torder,
        int tsign,
        String name) throws Exception;

    /**
     * Creates a hard link to an existing object in file.
     *
     * @param parentGroup The parent group for the new link
     * @param name The name of the new link
     * @param currentObj The object pointed by the new link
     * @return The an instance of the object pointed by the link if successful; otherwise returns null
     */
    public abstract HObject createLink(Group parentGroup, String name, HObject currentObj) throws Exception;

    /**
     * Add a new file format to the supported file list.
     * <p>
     * This method is provided for adding new FileFormat dynamically. Applications
     * can add new file format at runtime. For example, the HDFView allows users
     * to regiter a new file format and saves it in the HDFView properties file.
     * When the HDFView starts, it calls FileFormat.addFileFormat() to add all
     * the file formats listed in the property file.
     *
     * @param key the unique ID key to identify the file format, such as
     *   "HDF5" for ncsa.hdf.object.h5.HDF5 file format and
     *   "Hdf-Eos5" for hdfeos.he5.HE5File file format.
     * @param fileformat the name of the new file format to be added.
     */
    public static void addFileFormat(String key, FileFormat fileformat) {
        if (fileformat == null || key == null)
            return;

        key = key.trim();

        if (!FileList.containsKey(key))
            FileList.put(key, fileformat);
    }

    /**
     * Removes a file format from the supported file list.
     *
     * @param key the key of the file format to remove
     * @return the file format to which the key is matched, or null if no file
     * format has such as a key.
     */
    public static FileFormat removeFileFormat(String key) {
        return (FileFormat) FileList.remove(key);
    }

    /**
     * Returns the FileFormat for a given key from the supported file list.
     *
     * @param key the unique ID key to identify the file format, such as "HDF5" or "Hdf-Eos5"
     * @return The file format if successful; otherwise returns null
     */
    public static FileFormat getFileFormat(String key) {
        return (FileFormat)FileList.get(key);
    }

    /**
     * Returns a list of keys of all supported FileFormats.
     *
     * @return An enumeration of keys if successful; otherwise returns null
     */
    public static final Enumeration getFileFormatKeys() {
        return ((Hashtable)FileList).keys();
    }

    /**
     *  Returns the version of the library for this file format.
     *
     * @return The library version if successful; otherwise returns null
     */
    public abstract String getLibversion();

    /**
     * Checks if a given file is this type of file.
     * <p>
     * For example, if "test.h5" is an HDF5 file, H5File.isThisType("test.h5") returns
     * true while H4File .isThisType("test.h5") will return false.
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
     * Checks if a given file format is this type of file.
     * <p>
     * @param fileformat the Fileformat to be checked
     * @return true if the file format is this type; otherwise returns false.
     */
    public abstract boolean isThisType(FileFormat fileformat);

    /**
     * Creates a new attribute and attached to the object if the attribute does
     * not exist. If the attribute already exists, just update the value.
     *
     * <p>
     * @param obj The object which the attribute is to be attached to.
     * @param attr The atribute to attach.
     * @param attrExisted The indicator if the given attribute exists.
     */
    public abstract void writeAttribute(HObject obj, Attribute attr,
        boolean attrExisted) throws Exception;

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
    public abstract TreeNode copy(HObject srcObj, Group dstGroup) throws Exception;

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
    public abstract TreeNode copy(HObject srcObj, Group dstGroup, String dstName) throws Exception;

    /**
     * Delete an object from the file.
     * @param obj The object to delete.
     */
    public abstract void delete(HObject obj) throws Exception;

    /**
     * Current Java application such as HDFView cannot handle files with large
     * number of objects such 1,000,000 objects due to JVM memory  limitation.
     * The max_members is defined so that applications such as HDFView will load
     * up to <i>max_members</i> number of objects starting the <i>start_members</i>
     * -th object.
     *
     * @param n The maximum number of objects to be loaded into memory
     */
    public void setMaxMembers(int n) { max_members = n; }

    /**
     * Current Java application such as HDFView cannot handle files with large
     * number of objects such 1,000,000 objects due to JVM memory  limitation.
     * The max_members is defined so that applications such as HDFView will load
     * up to <i>max_members</i> number of objects starting the <i>start_members</i>
     * -th object.
     *
     * @param idx The starting object to be loaded into memory
     */
    public void setStartMembers(int idx) { start_members = idx; }

    /**
     * Returns the maximum number of objects to be loaded into memory.
     * <p>
     * Current Java application such as HDFView cannot handle files with large
     * number of objects such 1,000,000 objects due to JVM memory  limitation.
     * The max_members is defined so that applications such as HDFView will load
     * up to <b>max_members</b> number of objects starting the <b>start_members</b>
     * -th object.
     *
     * @return The maximum number of objects to be loaded into memory
     */
    protected int getMaxMembers() { return max_members; }

    /**
     * Returns the starting object to be loaded into memory.
     * <p>
     * Current Java application such as HDFView cannot handle files with large
     * number of objects such 1,000,000 objects due to JVM memory  limitation.
     * The max_members is defined so that applications such as HDFView will load
     * up to <i>max_members</i> number of objects starting the <i>start_members</i>
     * -th object.
     *
     * @return The starting object to be loaded into memory
     */
    protected int getStartMembers() { return start_members; }

    /**
     * Return a list of file extensions for the supported file formats. The
     * extensions are separates by comma, such as "hdf, h4, hdf5, h5, hdf4, he4, he5"
     *
     * @return A list of file extensions for the supported file formats
     */
    public static String getFileExtensions() { return extensions; }

    /**
     * Returns the file identifier.
     * @return The file identifer
     */
    public int getFID() { return fid; }

    /**
     * Add file an extension to the file extension list
     * @param extension The file extension to add
     */
    public static void addFileExtension(String extension)
    {
        if (extensions == null || extensions.length() <=0)
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
            if (tokens.contains(ext))
                continue;

            extensions = extensions + ", "+ext;
        }
        
        tokens.setSize(0); 
    }

    /**
     * Constructs a FileFormat corresponding to the data in a file. The fileName
     *  may be an absolute or a relative file specification. It checks the registered
     * FileFormats and returns an instance of the matched one, or null if none is matched.
     * <p>
     * For example, if "test_hdf5.h5" is an HDF5 file, FileFormat.getInstance("test_hdf5.h5")
     * returns an instance of H5File, i.e. H5File f=(H5File)FileFormat.getInstance("test_hdf5.h5");
     *
     * @param fileName The file to open
     * @return an instance of the matched file format; otherwise returns null
     */
    public static FileFormat getInstance(String fileName) throws Exception
    {
        if (fileName == null || fileName.length()<=0)
            throw new IllegalArgumentException("Invalid file name. "+fileName);

        if (!(new File(fileName)).exists())
            throw new IllegalArgumentException("File does not exists");

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
     * Gets the HObject with given file name and object path in the format of filename#//path
     *
     * @param fullPath the file name and object path in the format of filename#//path
     * @return The object if it exists in the file; otherwise returns null
     */
    public static final HObject getHObject(String fullPath) throws Exception
    {
        if (fullPath == null || fullPath.length() <=0)
            return null;

        String filename=null, path=null;
        int idx = fullPath.indexOf("#//");

        if (idx >0 )
        {
            filename = fullPath.substring(0, idx);
            path = fullPath.substring(idx+3);
            if (path == null || path.length() == 0)
                path = "/";
        }
        else
        {
            filename = fullPath;
            path = "/";
        }

        return FileFormat.getHObject(filename, path);
    };

    /**
     * Gets the HObject with given file name and object path
     *
     * @param filename the name of the file to open
     * @param path the path of the data object in the file
     * @return The object if it exists in the file; otherwise returns null
     */
    public static final HObject getHObject(String filename, String path) throws Exception
    {
        if (filename == null || filename.length()<=0)
            throw new IllegalArgumentException("Invalid file name. "+filename);

        if (!(new File(filename)).exists())
            throw new IllegalArgumentException("File does not exists");

        HObject obj = null;
        FileFormat file = FileFormat.getInstance(filename);

        if (file != null)
            obj = file.get(path);

        return obj;
    }

    /**
     * Gets an individual HObject with a given path from this file. The following
     * example shows how use the get() to get an group from a file
     * <pre>
         public static void TestHDF5Get (String filename) throws Exception
        {
            H5File file = new H5File(filename, H5File.READ);
            Group group = (Group)file.get("/Group0");
            System.out.println(group);
            file.close();
        }
     * </pre>
     * @param path the path of the data object in the file
     * @return The object if it exists in the file; otherwise returns null
     */
    public abstract HObject get(String path) throws Exception;

    /** Returns a list of supported file formats */
    public static FileFormat[] getFileFormats()
    {
        int n = FileList.size();
        if ( n <=0 ) return null;

        int i = 0;
        FileFormat[] fileformats = new FileFormat[n];
        Enumeration local_enum = ((Hashtable)FileList).elements();
        while (local_enum.hasMoreElements())
            fileformats[i++] = (FileFormat)local_enum.nextElement();

        return fileformats;
    }

    public final String toString() {
        return this.getClass().getName();
    }

}
