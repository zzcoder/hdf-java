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

import java.util.List;

/**
 * An interface provides general I/O operations for read/write information about
 * the object, such as reading data content or data attribute from file into memory
 * or writing data content or data attribute from memory into file.
 * <p>
 * @see ncsa.hdf.object.HObject
 *
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
     */
    public abstract String getFile();

    /**
     * Read the metadata such as attributes from file into memory if the metadata
     * is not in memory. If the metadata is in memory, it returns the metadata.
     * The metadata is stored as a collection of metadata in a List.
     *
     * @return the list of metadata objects.
     * @see <a href="http://java.sun.com/j2se/1.5.0/docs/api/java/util/List.html">java.util.List</a>
     */
    public abstract List getMetadata() throws Exception;

    /**
     * Writes a specific metadata into file. If the metadata exists, it
     * updates its value. If the metadata does not exists in file, it creates
     * the metadata and attaches it to the object.
     *
     * @param info the metadata to write.
     */
    public abstract void writeMetadata(Object info) throws Exception;

    /**
     * Deletes an existing metadata from this data object.
     *
     * @param info the metadata to delete.
     */
    public abstract void removeMetadata(Object info) throws Exception;

}
