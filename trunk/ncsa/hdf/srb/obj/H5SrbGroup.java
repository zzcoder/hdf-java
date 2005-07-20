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

package ncsa.hdf.srb.obj;

import java.util.*;
import ncsa.hdf.object.*;

public class H5SrbGroup extends Group
{
    private int opID;
    private String fullPath; /*path+name*/

    public H5SrbGroup(FileFormat fileFormat, String name, String path, Group parent)
    {
        this(fileFormat, name, path, parent, null);
    }

    /**
     * Constructs an HDF5 group with specific name, path, and parent.
     * <p>
     * @param fileFormat the file which containing the group.
     * @param name the name of this group.
     * @param path the full path of this group.
     * @param parent the parent of this group.
     * @param oid the unique identifier of this data object.
     */
    public H5SrbGroup(
        FileFormat fileFormat,
        String name,
        String path,
        Group parent,
        long[] theID)
    {
        super (fileFormat, name, path, parent, theID);

        opID = -1;
        if (name == null) fullPath = path;
        else fullPath = path + HObject.separator + name;
    }

    /**
     * Opens access to this object.
     * <p>
     * Sub-classes have to implement this interface so that different data
     * objects have their own ways of how the data resources are opened.
     * <p>
     * @return the interface identifier for access this object.
     */
    public int open() { return -1; }

    /**
     * Closes access to this object.
     * <p>
     * @param id the object identifier.
     * Sub-classes have to implement this interface so that different data
     * objects have their own ways of how the data resources are closed.
     */
    public void close(int id) { ; }

    /**
     * Initializes the dataset such as dimension size of this dataset.
     * Sub-classes have to replace this interface. HDF4 and HDF5 datasets
     * call the different library to have more detailed initialization.
     * <p>
     * The Dataset is designed in a way of "ask and load". When a data object
     * is retrieved from file, it does not load the datatype and dataspce
     * information, and data value into memory. When it is asked to load the
     * data, teh data object first call init() to fill the datatype and
     * dataspace information, then load the data content.
     */
    public void init() {; }

    /**
     * Loads the metadata such as attributes and type of the the data object
     * into memory if the metadata is not loaded. If the metadata is loaded, it
     * returns the metadata. The metadata is stored as a collection of metadata
     * ojbects in a List.
     *<p>
     * @return the list of metadata objects.
     * @see java.util.List
     */
    public List getMetadata() throws Exception { return null; }

    /**
     * Saves a specific metadata into file. If the metadata exists, it
     * updates its value. If the metadata does not exists, it creates
     * and attach the new metadata to the object and saves it into file.
     * <p>
     * @param info the specific metadata.
     */
    public void writeMetadata(Object info) throws Exception {;}

    /**
     * Deletes an existing metadata from this data object.
     * <p>
     * @param info the metadata to delete.
     */
    public void removeMetadata(Object info) throws Exception {;}


}
