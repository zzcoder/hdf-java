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
import java.lang.reflect.Array;
import ncsa.hdf.object.*;

public class H5SrbCompoundDS extends CompoundDS
{
	public static final long serialVersionUID = HObject.serialVersionUID;

    public static final int H5DATASET_OP_ERROR             = -1;
    public static final int H5DATASET_OP_CREATE            = 0;
    public static final int H5DATASET_OP_DELETE            = 1;
    public static final int H5DATASET_OP_READ              = 2;
    public static final int H5DATASET_OP_WRITE             = 3;
    public static final int H5DATASET_OP_READ_ATTRIBUTE    = 4;

    /**
     * The list of attributes of this data object. Members of the list are
     * instance of Attribute.
     */
     private List attributeList;

    private int opID;

    public H5SrbCompoundDS(FileFormat fileFormat, String name, String path)
    {
        this(fileFormat, name, path, null);
    }

    /**
     * Constructs an H5SrbDataset object with specific name and path.
     * <p>
     * @param fileFormat the HDF file.
     * @param name the name of this H5ScalarDS.
     * @param path the full path of this H5ScalarDS.
     * @param oid the unique identifier of this data object.
     */
    public H5SrbCompoundDS(
        FileFormat fileFormat,
        String name,
        String path,
        long[] oid)
    {
        super (fileFormat, name, path, oid);

        opID = -1;
    }

    public void setMemberCount(int nmembers)
    {
        if (nmembers > 0)
        {
            numberOfMembers = nmembers;
            memberNames = new String[numberOfMembers];
            memberOrders = new int[numberOfMembers];
            isMemberSelected = new boolean[numberOfMembers];
            for (int i=0; i<numberOfMembers; i++)
            {
                memberOrders[i] = 1;
                isMemberSelected[i] = true;
            }
        }
    }

    /*abstract methods inherited from ScalarDS */
    public byte[] getPaletteRefs() { return null; }
    public byte[][] getPalette() { return null; }
    public byte[][] readPalette(int idx) { return null; }

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
    public void init() {;}

    /** Loads and returns the data value from file. */
    public Object read() throws Exception
    {
        opID = H5DATASET_OP_READ;
        
        try {
            H5SRB.h5ObjRequest (this, H5SRB.H5OBJECT_DATASET);
        } catch (Exception ex) { throw new Exception (ex.toString()); }

        if ((data != null) && data.getClass().isArray())
        {
            int numberOfpoints = Array.getLength(data);
            String strs[][] = new String[numberOfMembers][numberOfpoints];

            String str;
            int midx=0, strlen=0;
            for (int i=0; i<numberOfpoints; i++) {
                midx = 0;
                str = (String)Array.get(data, i);
                str = str.trim();
                strlen = str.length();
                if (strlen>1) {
                    str = str.substring(1, strlen-1);
                }

                StringTokenizer st = new StringTokenizer(str, "||");
                while (st.hasMoreTokens() && (midx<numberOfMembers)) {
                    strs[midx++][i] = st.nextToken();
                }
            }

            data = new Vector(numberOfMembers);
            for (int i=0; i<numberOfMembers; i++) {
                ((Vector)data).add(i, strs[i]);
            }
        }

        return data;
    }

    /**
     * Write data values into file.
     * @param buf the data to write
     * @throws Exception
     */
    public void write(Object buf) throws Exception {;}

    /** Copy this dataset to another group.
     * @param pgroup the group which the dataset is copied to.
     * @param name the name of the new dataset.
     * @param dims the dimension sizes of the the new dataset.
     * @param data the data to be copied.
     * @return the new dataset.
     */
    public Dataset copy(Group pgroup, String name, long[] dims,
        Object data) throws Exception { return null; }


    /** returns the datatype of this dataset. */
    public Datatype getDatatype() { return datatype; }

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
        // load attributes first
        if (attributeList == null)
        {
           attributeList = new Vector();

            opID = H5DATASET_OP_READ_ATTRIBUTE;
            try {
                H5SRB.h5ObjRequest (this, H5SRB.H5OBJECT_DATASET);
            } catch (Exception ex) { throw new Exception (ex.toString()); }
        } // if (attributeList == null)

        return attributeList;
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

    /*
     * (non-Javadoc)
     * @see ncsa.hdf.object.Dataset#readBytes()
     */
    public byte[] readBytes() throws Exception { return null; }

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
