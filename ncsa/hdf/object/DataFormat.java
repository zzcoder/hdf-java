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

package ncsa.hdf.object;

import java.util.List;

/**
 * This interface loads and saves data content from/to file. Implementation of
 * DataFormat defines how data is loaded to memeory and how data is saved to
 * file. The design of this interface is intended to support modular data I/O.
 * <p>
 * The interface is implemented by HObject.
 * <p>
 * @version 1.0 12/12/2001
 * @author Peter X. Cao, NCSA
 */
public interface DataFormat
{
    /**
     * Returns the full path of the file that contains this data object.
     * The file name is necessary because the file of this data object is
     * uniquely identified when mutilple files are opened by an application
     * at the same time.
     * <p>
     * @see ncsa.hdf.view.ViewManager#getCurrentObject()
     */
    public abstract String getFile();

    /**
     * Loads the content of this data object into memory if the data of the
     * object is not loaded. If the content is already loaded, it returns the
     * content. It returns null if the data object has no content or it fails
     * to load the data content.
     * <p>
     * @return the content of the data object
     */
    public abstract Object read() throws Exception;

    /**
     * Saves the content of this data object into file.
     * <p>
     * @return true if writing data into file is succsessful, false otherwise
     */
    public abstract boolean write() throws Exception;

    /**
     * Loads the metadata such as attributes and type of the the data object
     * into memory if the metadata is not loaded. If the metadata is loaded, it
     * returns the metadata. The metadata is stored as a collection of metadata
     * ojbects in a List.
     *<p>
     * @return the list of metadata objects.
     * @see java.util.List
     */
    public abstract List getMetadata() throws Exception;

    /**
     * Saves a specific metadata into file. If the metadata exists, it
     * updates its value. If the metadata does not exists, it creates
     * and attach the new metadata to the object and saves it into file.
     * <p>
     * @param info the specific metadata.
     * @return true if writing metadata into file is succsessful, false otherwise
     */
    public abstract boolean writeMetadata(Object info) throws Exception;

    /**
     * Deletes an existing metadata from this data object.
     * <p>
     * @param info the metadata to delete.
     * @return true if deleting metadata is succsessful, false otherwise.
     */
    public abstract boolean removeMetadata(Object info) throws Exception;

}