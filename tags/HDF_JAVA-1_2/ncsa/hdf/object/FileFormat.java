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
import javax.swing.tree.TreeNode;

/**
 * This FileFormat defines general I/O accessing interface to file resources,
 * such as open/close file, and retrieve the file structure.
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
    public static final String FILE_TYPE_HDF5 = "H5";

    /** keep a list current supported file format. */
    private static final Map FileList = new Hashtable(10);

    static {
        Class file_class = null;
        Object hdffile = null;
        ClassLoader cl = ClassLoader.getSystemClassLoader();

        // add H5File into the file list
        try { file_class = cl.loadClass("ncsa.hdf.object.h5.H5File"); }
        catch (ClassNotFoundException ex) {file_class = null;}
        if (file_class != null)
        {
            try { hdffile = file_class.newInstance(); }
            catch (Exception ex) { hdffile = null; }
        }
        if (hdffile != null) FileList.put(FILE_TYPE_HDF5, hdffile);

        // add H4File into the file list
        file_class = null;
        hdffile = null;
        try { file_class = cl.loadClass("ncsa.hdf.object.h4.H4File"); }
        catch (ClassNotFoundException ex) {file_class = null;}
        if (file_class != null)
        {
            try { hdffile = file_class.newInstance(); }
            catch (Exception ex) { hdffile = null; }
        }
        if (hdffile != null) FileList.put(FILE_TYPE_HDF4, hdffile);
    }

    public FileFormat(String filename)
    {
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
     */
    public abstract FileFormat create(String fileName) throws Exception;

    /**
     * Create a new group.
     */
    public abstract Group createGroup(FileFormat file, String name, Group pgroup) throws Exception;

    /**
     * Create a new dataset.
     */
    public abstract Dataset createScalarDS(
        FileFormat file,
        String name,
        Group pgroup,
        Datatype type,
        long[] dims,
        long[] maxdims,
        long[] chunks,
        int gzip,
        Object data) throws Exception;

    /**
     * Create a new dataset.
     */
    public abstract Dataset createImage(
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
        Object data) throws Exception;

    /**
     * Create a new datatype.
     */
    public abstract Datatype createDatatype(
        int tclass,
        int tsize,
        int torder,
        int tsign) throws Exception;

    /**
     * Add a new file format.
     * @param key the unique ID key to identify the file format.
     *   such as "HDF5" or "HDF4"
     * @param fileformat the new file format to be added.
     */
    public static void addFileFormat(String key, FileFormat fileformat)
    {
        if (fileformat != null)
            FileList.put(key, fileformat);
    }

    /**
     * Gets a FileFormat from the supported file list.
     * @param key the unique ID key to identify the file format.
     *   such as "HDF5" or "HDF4"
     */
    public static FileFormat getFileFormat(String key)
    {
        return (FileFormat)FileList.get(key);
    }

    /**
     * Returns an iterator over the supported FileFormats.
     */
    public static Iterator iterator()
    {
        return FileList.values().iterator();
    }

    /**
     *  Returns the version of the HDF5 library.
     */
    public abstract String getLibversion();

    /**
     * Checks if a given file is this type of file.
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

}
