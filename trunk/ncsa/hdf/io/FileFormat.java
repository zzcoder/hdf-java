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

import javax.swing.tree.MutableTreeNode;

/**
 * This interface defines access to file
 * <p>
 * The interface is implemented by ncsa.hdf.io.DefaultFileFormat.
 * <p>
 * @version 1.0 12/12/2001
 * @author Peter X. Cao, NCSA
 */
public interface FileFormat
{
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
}