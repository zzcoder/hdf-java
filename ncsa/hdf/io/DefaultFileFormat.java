/****************************************************************************
 * NCSA HDF                                                                 *
 * National Comptational Science Alliance                                   *
 * University of Illinois at Urbana-Champaign                               *
 * 605 E. Springfield, Champaign IL 61820                                   *
 *                                                                          *
 * For conditions of distribution and use, see the accompanying             *
 * java-hdf  COPYING file.                                                  *
 *                                                                          *
 ****************************************************************************/

package ncsa.hdf.io;

import java.util.List;
import java.io.File;
import javax.swing.tree.MutableTreeNode;
import ncsa.hdf.object.*;

/**
 * This class defines access to file, implementing FileFormat
 * <p>
 * @version 1.0 12/12/2001
 * @author Peter X. Cao, NCSA
 */
public abstract class DefaultFileFormat
extends File
implements FileFormat
{
    public DefaultFileFormat(String pathname)
    {
        super(pathname);
    }

    /**
     * Opens access to the file resource.
     * <p>
     * @return true if opening the file is succsessful; otherwise returns false.
     */
    public abstract boolean open() throws Exception;

    /**
     * Closes access to the file resource.
     * <p>
     * @return true if closing the file is succsessful; otherwise returns false.
     */
    public abstract boolean close();

    /**
     * Returns the root node of the file
     */
    public abstract MutableTreeNode getRootNode();

    /**
     * Checks if the given file is this file type.
     * Subclasses must replace this interface to identify its file type.
     * <p>
     * @param filename the file to be checked.
     * @return true if the given file is this type; otherwise returns false.
     */
    public static boolean isThisType(String filename)
    {
        return false;
    }
}