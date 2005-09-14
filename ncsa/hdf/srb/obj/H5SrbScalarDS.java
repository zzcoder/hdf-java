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
import ncsa.hdf.srb.h5srb.*;

public class H5SrbScalarDS extends ScalarDS
{
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
    private String fullPath; /*path+name*/

    public H5SrbScalarDS(FileFormat fileFormat, String name, String path)
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
    public H5SrbScalarDS(
        FileFormat fileFormat,
        String name,
        String path,
        long[] oid)
    {
        super (fileFormat, name, path, oid);

        opID = -1;
        palette = null;
        unsignedConverted = false;
        if (name == null) fullPath = path;
        else fullPath = path + HObject.separator + name;
    }

    /*abstract methods inherited from ScalarDS */
    public byte[] getPaletteRefs() { return null; }
    public byte[][] getPalette() { return palette; }
    public byte[][] readPalette(int idx) { return null; }

    public void setPalette(byte[] pal)
    {
        if (pal == null || pal.length <768)
            return;

        palette = new byte[3][256];
        for (int i=0; i<256; i++)
        {
            palette[0][i] = pal[i*3];
            palette[1][i] = pal[i*3+1];
            palette[2][i] = pal[i*3+2];
        }
        isImage = true;
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
    public void init() {;}

    /** Loads and returns the data value from file. */
    public Object read() throws Exception, OutOfMemoryError
    {
        String srbInfo[] = ((H5SrbFile)getFileFormat()).getSrbInfo();
        if ( srbInfo == null  || srbInfo.length<5) return null;

        opID = H5DATASET_OP_READ;
        H5SRB.h5ObjRequest (srbInfo, this, H5SRB.H5OBJECT_DATASET);

        return data;
    }

    /** Read data values of this dataset into byte array.
     *  readBytes() loads data as arry of bytes instead of array of its datatype.
     * For example, for an one-dimension 32-bit integer dataset of size 5,
     * the readBytes() returns of a byte array of size 20 instead of a int array
     * of 5.
     * <p>
     * readBytes() is most used for copy data values, at which case, data do not
     * need to be changed or displayed.
     */
    public byte[] readBytes() throws Exception { return null; }

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
        String srbInfo[] = ((H5SrbFile)getFileFormat()).getSrbInfo();
        if ( srbInfo == null  || srbInfo.length<5) return null;

        // load attributes first
        if (attributeList == null)
        {
            attributeList = new Vector();

            opID = H5DATASET_OP_READ_ATTRIBUTE;
            H5SRB.h5ObjRequest (srbInfo, this, H5SRB.H5OBJECT_DATASET);
        } // if (attributeList == null)

        return attributeList;
    }

    void addAttribute(String attrName, Object attrValue, long[] attrDims,
                     int tclass, int tsize, int torder, int tsign)
    {
        if (attributeList == null)
            attributeList = new Vector();

        H5SrbDatatype type = new H5SrbDatatype(tclass, tsize, torder, tsign);
        Attribute attr = new Attribute(attrName, type, attrDims);
        attr.setValue(attrValue);

        attributeList.add(attr);
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

    public void init(int theRank, long theDims[], int tclass, int tsize, int torder, int tsign)
    {
        if (theDims == null)
            return;

        isImage = (palette!=null);
        rank = theRank;
        datatype = new H5SrbDatatype(tclass, tsize, torder, tsign);
        isUnsigned = (tsign==Datatype.SIGN_NONE);

        if (rank == 0) {
            // a scalar data point
            rank = 1;
            dims = new long[1];
            dims[0] = 1;
        }
        else
            dims = new long[rank];

        startDims = new long[rank];
        selectedDims = new long[rank];
        selectedStride = new long[rank];
        for (int i=0; i<rank; i++) {
            dims[i] = theDims[i];
            startDims[i] = 0;
            selectedDims[i] = selectedStride[i] = 1;
        }

        if (rank == 1)
        {
            selectedIndex[0] = 0;
            selectedDims[0] = dims[0];
        }
        else if (rank == 2)
        {
            selectedIndex[0] = 0;
            selectedIndex[1] = 1;
            selectedDims[0] = dims[0];
            selectedDims[1] = dims[1];
        }
        else if (rank > 2)
        {
            //in the case of images with only one component, the dataspace may
            // be either a two dimensional array, or a three dimensional array
            // with the third dimension of size 1.  For example, a 5 by 10 image
            // with 8 bit color indexes would be an HDF5 dataset with type
            // unsigned 8 bit integer.  The dataspace could be either a two
            // dimensional array, with dimensions [10][5], or three dimensions,
            // with dimensions either [10][5][1] or [1][10][5].
            if (dims[0] == 1)
            {
                // case [1][10][5]
                selectedIndex[0] = 1;
                selectedIndex[1] = 2;
                selectedIndex[2] = 0;
                selectedDims[0] = dims[1];
                selectedDims[1] = dims[2];
            }
            else
            {
                // case [10][5][1]
                selectedIndex[0] = 0;
                selectedIndex[1] = 1;
                selectedIndex[2] = 2;
                selectedDims[0] = dims[0];
                selectedDims[1] = dims[1];
            }
        }
    }

}
