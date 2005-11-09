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
import java.net.*;
import javax.swing.tree.TreeNode;

/**
 * This FileFormat defines general I/O accessing interface to file resources,
 * such as open/close file, and retrieve file structure.
 * <p>
 * FileFormat is a plugable component. A implementing class of FileFormat can be
 * added to the supported file list. The current implementing classes include
 * H5File and H4File. By default, H5File and H4File are added to the list of
 * supported file formats.
 * <p>
 * @version 1.0 12/12/2001
 * @author Peter X. Cao, NCSA
 */
public abstract class FileFormat extends File
{
    /** Flag for opening a file for read only. */
    public final static int READ = 0;

    /** Flag for opening a file for read/write. */
    public final static int WRITE = 1;

    /** Flag for creating a new file. */
    public final static int CREATE = 2;

    /** tag for JPEG image.*/
    public static final String FILE_TYPE_JPEG = "JPEG";

    /** tag for TIFF image. */
    public static final String FILE_TYPE_TIFF = "TIFF";

    /** tag for PNG image. */
    public static final String FILE_TYPE_PNG = "PNG";

    /** tag for HDF4 file. */
    public static final String FILE_TYPE_HDF4 = "HDF";

    /** tag for HDF5 file. */
    public static final String FILE_TYPE_HDF5 = "HDF5";

    /** keep a list current supported file formats.
     * FileList keeps <key, fileFormat> pairs, such as
     * <"H5", ncsa.hdf.object.h5.H5File>
     */
    private static final Map FileList = new Hashtable(10);

    /**
     * Current Java application such as HDFView cannot handle files
     * with large number of objects such 1,000,000 objects.
     * max_members defines the maximum number of objects will be loaded
     * into memory.
     */
    private int max_members = 10000; // 10,000 by default

    /**
     * Current Java application such as HDFView cannot handle files
     * with large number of objects such 1,000,000 objects.
     * start_members defines the starting index of objects will be loaded
     * into memory.
     */
    private int start_members = 0; // 0 by default

    /** a list of file extensions for supported file types */
    private static String extensions = "hdf, h4, hdf5, h5";

    /**
     * file identifier for the open file.
     */
    protected int fid = -1;

    /** Constructs a FileFormat with a given file name.
     * @param filename the full name of the file.
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

    /** Construct a FileFormat with given file name */
    public FileFormat(String filename) {
        super(filename);
    }

    /**
     * Opens access to the file resource and returns the file identifier.
     * If the file is already open, it returns the file access identifier.
     * <p>
     * @return the file access identifier if opening the file is succsessful;
     * otherwise returns a negative value.
     */
    public abstract int open() throws Exception;

    /**
     * Open a file and returns an instance of implementing class of the FileFormat.
     * <p>
     * @param pathname the full path name of the file.
     * @param flag the file access flag, it takes one of two values below:
     */
    public abstract FileFormat open(String pathname, int access) throws Exception;

    /**
     * Closes access to the file resource.
     */
    public abstract void close() throws Exception;

    /**
     * Returns the root node of the file.
     * The root node contains the hierarchy of the file.
     * For file with hierarchical structure such HDF, the structure is stored
     * as a tree. The root of the tree is the root the the file.
     */
    public abstract TreeNode getRootNode();

    /**
     * Returns the full path of the file: file path + file name.
     */
    public abstract String getFilePath();

    /**
     * Returns the true if the file is read-only, otherwise returns false.
     */
    public abstract boolean isReadOnly();

    /**
     * Create a new instance of this file.
     * A subclass must implementing this method to create a file of its type.
     * For example,
     * <pre>
     * FileFormat file = H5File.create("test.h5");
     * </pre>
     * creates an HDF5 file "test.h5".
     */
    public abstract FileFormat create(String fileName) throws Exception;

    /**
     * Create a new group with given group name and a parent in this file.
     * @param name the name fo the new group.
     * @param pgroup the parent group.
     */
    public abstract Group createGroup(String name, Group pgroup) throws Exception;

    /**
     * Create a new dataset in this file.
     * For example, to create a 2D integer dataset of size 100X50 at the root
     * in an HDF5 file.
     * <pre>
     * String name = "2D integer";
     * Group pgroup = (Group)((DefaultMutableTreeNode)getRootNode).getUserObject();
     * Datatype dtype = new H5Datatype(Datatype.CLASS_INTEGER, Datatype.NATIVE,
     *     Datatype.NATIVE, Datatype.NATIVE);
     * long[] dims = {100, 50};
     * long[] maxdims = dims;
     * long[] chunks = null; // no chunking
     * int gzip = 0; // no compression
     * Object data = null; // no initial data values
     *
     * Dataset d = (H5File)file.createScalarDS(name, pgroup, dtype, dims, maxdims, chunks, gzip, data);
     * </pre>
     *
     * @param name the name of the new dataset
     * @param pgroup the parent group where the new dataset is created.
     * @param type the datatype of the new dataset.
     * @param dims dimension sizes of the new dataset.
     * @param maxdims the maximum dimension sizes of the new dataset.
     * @param chunks the chunk sizes of the new dataset.
     * @param gzip the compression level.
     * @param data the data of the new dataset.
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
     * For example, to create a 2D compound dataset with size of 100X50 and
     * member x and y at the root in an HDF5 file.
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
     * @param name the name of the new dataset
     * @param pgroup the parent group where the new dataset is created.
     * @param dims dimension sizes of the new dataset.
     * @param memberNames the names of the members.
     * @param memberDatatypes the datatypes of the members.
     * @param memberSizes the array size of the member.
     * @param data the data of the new dataset.
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
        // subclass to implement it
        throw new UnsupportedOperationException("Dataset FileFormat.createCompoundDS(...) is not implemented.");
    }

    /**
     * Create a new image at given parent group in this file.
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
     * @param name the name of the new image
     * @param pgroup the parent group where the new image is created.
     * @param type the datatype of the new image.
     * @param dims dimension sizes of the new image.
     * @param maxdims the maximum dimension sizes of the new image.
     * @param chunks the chunk sizes of the new image.
     * @param gzip the compression level.
     * @param ncomp the number of components of the new image
     * @param interlace the interlace of this image.
     * @param data the data of the new image.
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
        int intelace,
        Object data) throws Exception;

    /**
     * Create a new datatype based on this FileFormat.
     * <p>
     * For example, the following code creates an instance of H5Datatype.
     * <pre>
     * FileFormat file = FileFormat.getFileFormat(FileFormat.FILE_TYPE_HDF5);
     * H5Datatype dtype = file.createDatatype(Datatype.CLASS_INTEGER,
     *     Datatype.NATIVE, Datatype.NATIVE, Datatype.NATIVE);
     * </pre>
     *
     * @param tclass the class of datatype, such as Integer, Float
     * @param tsize the size of the datatype in bytes
     * @param torder, the order of the byte endianing
     * @param tsign, signed or unsinged the interger
     */
    public abstract Datatype createDatatype(
        int tclass,
        int tsize,
        int torder,
        int tsign) throws Exception;

    /* create a named datatype. subclass should replace the default
       implementation. */
    public Datatype createDatatype(
        int tclass,
        int tsize,
        int torder,
        int tsign,
        String name) throws Exception
    {
        return createDatatype(tclass, tsize, torder, tsign);
    }

    /**
     * Add a new file format.
     * <p>
     * This method is provided for adding new FileFormat dynamically.
     *
     * @param key the unique ID key to identify the file format.
     *   such as "HDF5" or "HDF4"
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
     * Gets a FileFormat from the supported file list.
     * @param key the unique ID key to identify the file format.
     *   such as "HDF5" or "HDF4"
     */
    public static FileFormat getFileFormat(String key) {
        return (FileFormat)FileList.get(key);
    }

    /**
     * Returns a list of keys of the supported FileFormats.
     */
    public static final Enumeration getFileFormatKeys() {
        return ((Hashtable)FileList).keys();
    }

    /**
     *  Returns the version of the HDF5 library.
     */
    public abstract String getLibversion();

    /**
     * Checks if a given file is this type of file.
     * For example, if "test.h5" is an HDF5 file, isThisType("test.h5") returns
     * true for H5File, and false for H4File;
     * <pre>
     * FileFormat file = FileFormat.getFileFormat(FileFormat.FILE_TYPE_HDF5);
     * boolean isH5 = file.isThisType("test.h5");
     * </pre>
     *
     * <p>
     * @param filename the file to be checked.
     */
    public abstract boolean isThisType(String filename);

    /**
     * Checks if a given file format is this type of file.
     * <p>
     * @param fileformat the fileformat to be checked
     */
    public abstract boolean isThisType(FileFormat fileformat);

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
    public abstract void writeAttribute(HObject obj, Attribute attr,
        boolean attrExisted) throws Exception;

    /**
     * Copy a data object to a group.
     * @param srcObj   the object to copy.
     * @param dstGroup the destination group.
     * @return the new node containing the new object.
     */
    public abstract TreeNode copy(HObject srcObj, Group dstGroup) throws Exception;

    /**
     * Delete an object from the file.
     * @param obj the data object to delete.
     */
    public abstract void delete(HObject obj) throws Exception;

    /**
     * Current Java application such as HDFView cannot handle files
     * with large number of objects such 1,000,000 objects.
     * setMaxMembers() sets the maximum number of objects will be loaded
     * into memory.
     *
     * @param n the maximum number of objects to load into memory
     */
    public void setMaxMembers(int n) { max_members = n; }

    /**
     * Current Java application such as HDFView cannot handle files
     * with large number of objects such 1,000,000 objects.
     * setStartMember() sets the starting index of objects will be loaded
     * into memory.
     *
     * @param n the maximum number of objects to load into memory
     */
    public void setStartMembers(int idx) { start_members = idx; }

    protected int getMaxMembers() { return max_members; }

    protected int getStartMembers() { return start_members; }

    public static String getFileExtensions() { return extensions; }

    public int getFID() { return fid; }

    public static void addFileExtension(String extension)
    {
        if (extensions == null || extensions.length() <=0)
        {
            extensions = extension;
        }

        StringTokenizer currentExt = new StringTokenizer(extensions, ",");
        List tokens = new Vector();
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
    }

    /**
     * Constructs a FileFormat corresponding to the data in a file. fileName
     *  may be an absolute or a relative file specification. It checks the registered
     * FileFormats and returns an instance of the matched one, or null if none is matched
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

    /** get HObject with given file name and object path
     * in the format of filename#//path
     *
     * @param fullPath the file name and object path in the
     *  format of filename#//path
     */
    public static HObject getHObject(String fullPath) throws Exception
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

    /** get HObject with given file name and object path
     *
     * @param filename the name of the file to open
     * @param path the path of the data object in the file
     */
    public static HObject getHObject(String filename, String path) throws Exception
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
     * Get an individual HObject with a given path.
     */
    public HObject get(String path) throws Exception
    {
        // subclass to implement it
        throw new UnsupportedOperationException("HObject FileFormat.get(String path) is not implemented.");
    }

    /** returns a list of supported file formats */
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
}
