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

import javax.swing.tree.MutableTreeNode;

/**
 * This FileFormat defines general I/O accessing interface to file resources,
 * such as open/close file, and retrieve the file structure.
 * <p>
 * @version 1.0 12/12/2001
 * @author Peter X. Cao, NCSA
 */
public interface FileFormat
{
    /** Flag for opening a file for read only. */
    public final static int READ = 0;

    /** Flag for opening a file for read/write. */
    public final static int WRITE = 1;

    /** Flag for creating a new file. */
    public final static int CREATE = 2;


    /**
     * Opens access to the file resource and returns the file identifier.
     * If the file is already open, it returns the file access identifier.
     * <p>
     * @return the file access identifier if opening the file is succsessful;
     * otherwise returns a negative value.
     */
    public abstract int open() throws Exception;

    /**
     * Closes access to the file resource.
     */
    public abstract void close() throws Exception;

    /**
     * Returns the root node of the file.
     * The root node contains the hierarchy of the file.
     */
    public abstract MutableTreeNode getRootNode();

    /**
     * Returns the full path of the file: file path + file name.
     */
    public abstract String getFilePath();
}
