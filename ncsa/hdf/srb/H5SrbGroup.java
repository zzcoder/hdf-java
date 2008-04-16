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

package ncsa.hdf.srb;

import java.util.*;

import ncsa.hdf.object.*;

public class H5SrbGroup extends Group
{
	public static final long serialVersionUID = HObject.serialVersionUID;

    private int opID;
    private String fullPath; /*path+name*/

    public static final int H5GROUP_OP_ERROR          = -1;
    public static final int H5GROUP_OP_CREATE         = 0;
    public static final int H5GROUP_OP_DELETE         = 1;
    public static final int H5GROUP_OP_READ_ATTRIBUTE = 2;

    /**
     * The list of attributes of this data object. Members of the list are
     * instance of Attribute.
     */
     private List attributeList;

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
        if (name == null) {
            fullPath = path;
        } else {
            fullPath = path + HObject.separator + name;
        }
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
    // Implementing DataFormat
    public List getMetadata() throws Exception
    {
        String srbInfo[] = ((H5SrbFile)getFileFormat()).getSrbInfo();
        if ( (srbInfo == null) || (srbInfo.length<5)) {
            return null;
        }

        // load attributes first
        if (attributeList == null)
        {
            attributeList = new Vector();
            opID = H5GROUP_OP_READ_ATTRIBUTE;
            try {
                H5SRB.h5ObjRequest (srbInfo, this, H5SRB.H5OBJECT_GROUP);
            } catch (Exception ex) { throw ex; }
        } // if (attributeList == null)

        return attributeList;
    }

    void addAttribute(String attrName, Object attrValue, long[] attrDims,
                     int tclass, int tsize, int torder, int tsign)
    {
        if (attributeList == null) {
            attributeList = new Vector();
        }

        H5SrbDatatype type = new H5SrbDatatype(tclass, tsize, torder, tsign);
        Attribute attr = new Attribute(attrName, type, attrDims);
        attr.setValue(attrValue);

        attributeList.add(attr);
    }
    
    /*
     * (non-Javadoc)
     * @see ncsa.hdf.object.DataFormat#hasAttribute()
     */
    public boolean hasAttribute () 
    { 
        if (attributeList == null) {
            return false;
        }
        
        return (attributeList.size()>0);
    }

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
