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
 * An interface that provides general I/O operations for read/write object data.
 * For example, reading data content or data attribute from file into memory
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
     * <p>
     * The file name is necessary because data objects are uniquely identified
     * by object reference and file name when mutilple files are opened at
     * the same time.
     */
    public abstract String getFile();

    /**
     * Retrieves the metadata such as attributes from file.
     * <p>
     * Metadata such as attributes are stored in a List.
     *
     * @return the list of metadata objects.
     */
    public abstract List getMetadata() throws Exception;

    /**
     * Writes a specific metadata (such as attribute) into file. 
     * 
     * <p>
     * If metadata exists, the method updates its value. If the metadata 
     * does not exists in file, it creates the metadata in file and attaches 
     * it to the object.
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
