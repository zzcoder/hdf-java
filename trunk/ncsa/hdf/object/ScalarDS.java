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


/**
 * A scalar dataset is a multiple dimension array of scalar points. The
 * Datatype of a scalar dataset must be an atomic datatype. Common datatypes
 * of scalar datasets include char, byte, short, int, long, float, double and string.
 * <p>
 * A ScalarDS can be an image or spreadsheet data. ScalarDS defines few methods
 * to deal with both images and spreadsheets.
 * <p>
 * ScalarDS is an abstract class. Current implementing classes are the H4SDS,
 * H5GRImage and H5ScalarDS.
 * <p>
 * <b>How to Select a Subset</b>
 * <p>
 * Dataset defines APIs for reading, writing and subetting a dataset. No function is defined
 * to select a subset of a data array. The selection is done implicitly.
 * Function calls to dimension information such as getSelectedDims() return an array
 * of dimension values, which is a reference to the array in the dataset object.
 * Changes to the array outside the dataset object directly change the values of
 * the array in the dataset object, like pointers in C.
 * <p>
 *
 * The following is an example of how to make a subset. In the example, the dataset
 * is a 4-dimension with size of [200][100][50][10], i.e.
 * dims[0]=200; dims[1]=100; dims[2]=50; dims[3]=10; <br>
 * We want to select every other data points in dims[1] and dims[2]
 * <pre>
     int rank = dataset.getRank();   // number of dimension of the dataset
     long[] dims = dataset.getDims(); // the dimension sizes of the dataset
     long[] selected = dataset.getSelectedDims(); // the selected size of the dataet
     long[] start = dataset.getStartDims(); // the off set of the selection
     long[] stride = dataset.getStride(); // the stride of the dataset
     int[]  selectedIndex = dataset.getSelectedIndex(); // the selected dimensions for display

     // select dim1 and dim2 as 2D data for display,and slice through dim0
     selectedIndex[0] = 1;
     selectedIndex[1] = 2;
     selectedIndex[2] = 0;

     // reset the selection arrays
     for (int i=0; i&lt;rank; i++) {
         start[i] = 0;
         selected[i] = 1;
         stride[i] = 1;
    }

    // set stride to 2 on dim1 and dim2 so that every other data points are selected.
    stride[1] = 2;
    stride[2] = 2;

    // set the selection size of dim1 and dim2
    selected[1] = dims[1]/stride[1];
    selected[2] = dims[1]/stride[2];

    // when dataset.read() is called, the slection above will be used since
    // the dimension arrays is passed by reference. Changes of these arrays
    // outside the dataset object directly change the values of these array
    // in the dataset object.

 * </pre>
 *
 * <p>
 * @version 1.0 12/12/2001
 * @author Peter X. Cao, NCSA
 */
public abstract class ScalarDS extends Dataset
{
    // The following constant strings are copied from
    // http://hdf.ncsa.uiuc.edu/HDF5/doc/ADGuide/ImageSpec.html
    // to make the defination consistent with the image specs.

    /** 
     * Indicates that the pixel RGB values are contiguous. 
     */
    public final static int INTERLACE_PIXEL = 0;

    /** Indicates that each pixel component of RGB is stored as a scan line. */
    public static final int INTERLACE_LINE = 1;

    /** Indicates that each pixel component of RGB is stored as a plane. */
    public final static int INTERLACE_PLANE = 2;

    /**
     * The native datatype identifier of this scalar dataset.
     */
    protected int nativeDatatype;

    /**
     * The interlace mode of the stored raster image data.
     * Valid values are INTERLACE_PIXEL, INTERLACE_LINE and INTERLACE_PLANE.
     */
    protected int interlace;

    /**
     * The min-max range of image data values.
     * For example, [0, 255] indicates the min is 0, and the max is 255.
     */
    protected double[] imageDataRange;

    /**
     * The indexed RGB color model with 256 colors.
     * <p>
     * The palette values are stored in a two-dimensional byte array and arrange
     * by color components of red, green and blue. palette[][] = byte[3][256],
     * where, palette[0][], palette[1][] and palette[2][] are the red, green and
     * blue components respectively.
     */
    protected byte[][] palette;

    /**
     * True if this dataset is an image.
     */
    protected boolean isImage;

    /**
     * True if this dataset is a true color image.
     */
    protected boolean isTrueColor;

    /**
     * True if this dataset is ASCII text.
     */
    protected boolean isText;

    /**
     * Flag to indicate if the original C data is unsigned integer.
     */
    protected boolean isUnsigned;

    /**
     * Flag to indicate is the original unsigned C data is converted.
     */
    protected boolean unsignedConverted;

    /** The fill value of the dataset. */
    protected Object fillValue = null;
    
    /** Flag to indicate if the dataset is displayed as an image */
    protected boolean isImageDisplay;
    
    /**
     * Constructs an instance of a ScalarDS with specific name and path.
     * An HDF data object must have a name. The path is the group path starting
     * from the root.
     * <p>
     * For example, in H5ScalarDS(h5file, "dset", "/arrays/"), "dset" is the
     * name of the dataset, "/arrays" is the group path of the dataset.
     *
     * @param theFileFormat the file that contains the data object.
     * @param theName the name of the data object, e.g. "dset".
     * @param thePath the full path of the data object, e.g. "/arrays/".
     */
    public ScalarDS(FileFormat fileFormat, String theName, String thePath)
    {
        this(fileFormat, theName, thePath, null);
    }

    /**
     * @deprecated. Using {@link #ScalarDS(FileFormat, String, String)} 
     * 
     * Constructs an instance of a ScalarDS with specific name, path and OID.
     * An HDF data object must have a name. The path is the group path starting
     * from the root. 
     * <p>
     * For example, in H5ScalarDS(h5file, "dset", "/arrays/"), "dset" is the
     * name of the dataset, "/arrays" is the group path of the dataset.
     *
     * The OID is the object identifier that uniquely identifies the
     * data object in file. In HDF4, the OID is a two-element array of (ref, tag).
     * In HDF5, OID is an one-element array of the object reference.
     * 
     * @param theFileFormat the file that contains the data object.
     * @param theName the name of the data object, e.g. "dset".
     * @param thePath the full path of the data object, e.g. "/arrays/".
     * @param oid the unique identifier of this data object.
     */
    public ScalarDS (
        FileFormat fileFormat,
        String theName,
        String thePath,
        long[] oid)
    {
        super (fileFormat, theName, thePath, oid);

        nativeDatatype = -1;
        palette = null;
        isImage = false;
        isTrueColor = false;
        isText = false;
        isUnsigned = false;
        interlace = -1;
        datatype = null;
        imageDataRange = null;
        isImageDisplay = false;
    }

    /**
     * Returns the native datatype identifier of this scalar dataset.
     * 
     * @return the native datatype identifier of this scalar dataset.
     */
    public final int getNativeDataType()
    {
        return nativeDatatype;
    }

    /*
     * (non-Javadoc)
     * @see ncsa.hdf.object.Dataset#clearData()
     */
    public void clearData()
    {
        super.clearData();
        unsignedConverted = false;
    }

    /**
     * Converts the data values of this dataset to appropriate Java integer if they are unsigned integers.
     * 
     * @see ncsa.hdf.object.Dataset#convertToUnsignedC(Object)
     * @see ncsa.hdf.object.Dataset#convertFromUnsignedC(Object, Object)
      */
    public void convertFromUnsignedC()
    {
        // keep a copy of original buffer and the converted buffer
        // so that they can be reused later to save memory
        if (data != null && isUnsigned && !unsignedConverted)
        {
            originalBuf = data;
            convertedBuf = convertFromUnsignedC(originalBuf, convertedBuf);
            data = convertedBuf;
            unsignedConverted = true;
        }
    }

    /**
     * Converts Java integer data of this dataset back to unsigned C-type integer data
     * if they are unsigned integers. 
     * 
     * @see ncsa.hdf.object.Dataset#convertToUnsignedC(Object)
     * @see ncsa.hdf.object.Dataset#convertToUnsignedC(Object, Object)
     * @see #convertFromUnsignedC(Object data_in)
     */
    public void convertToUnsignedC()
    {
        // keep a copy of original buffer and the converted buffer
        // so that they can be reused later to save memory
        if (data != null && isUnsigned)
        {
            convertedBuf = data;
            originalBuf = convertToUnsignedC(convertedBuf, originalBuf);
            data = originalBuf;
        }
    }

    /**
     * Returns the palette of this scalar dataset or null if palette does not exist.
     * <p>
     * Scalar dataset can be displayed as spreadsheet data or image. When a scalar
     * dataset is chosen to display as an image, the palette or color table may
     * be needed to translate a pixel value to color components (for example,
     * red, green, and blue). Some scalar datasets have no palette and some datasets
     * have one or more than one palettes. If an associated palette exists but not
     * loaded, this interface retrieves the palette from the file and returns
     * the palette. If the palette is loaded, it returnd the palette. It returns
     * null if there is no palette assciated with the dataset.
     * <p>
     * Current implementation only supports palette model of indexed RGB with
     * 256 colors. Other models such as YUV", "CMY", "CMYK", "YCbCr", "HSV will
     * be supported in the future.
     * <p>
     * The palette values are stored in a two-dimensional byte array and arrange
     * by color components of red, green and blue. palette[][] = byte[3][256],
     * where, palette[0][], palette[1][] and palette[2][] are the red, green and
     * blue components respectively.
     * <p>
     * Sub-classes have to implement this interface. HDF4 and HDF5 images use
     * different libraries to retrieve the associated palette.
     * 
     * @return the 2D palette byte array.
     */
    public abstract byte[][] getPalette();

    /**
     * Sets the palette for this dataset.
     * 
     * @param pal the 2D palette byte array.
     */
    public final void setPalette(byte[][] pal)
    {
        palette = pal;
    }

    /** Reads a specific image palette from file.
     * <p>
     * A scalar dataset may have multiple palettes attached to it.
     * readPalette(int idx) returns a specific palette identified by its index.
     * 
     *  @param idx the index of the palette to read.
     */
    public abstract byte[][] readPalette(int idx);

    /** 
     * Returns the byte array of palette refs.
     * <p>
     * A palette reference is an object reference that points to the palette 
     * dataset. 
     * <p>
     * For example, Dataset "Iceberg" has an attribute of object
     * reference "Palette". The arrtibute "Palette" has value "2538" that is
     * the object reference of the palette data set "Iceberg Palette". 
     * 
     * @return null if there is no palette attribute attached to this dataset.
     */
    public abstract byte[] getPaletteRefs();

    /**
     * Returns true if this dataset is an image.
     * <p>
     * For all Images, they must have an attribute called "CLASS".
     * The value of this attribute is "IMAGE". For more details, read   
     * <a href="http://www.hdfgroup.org/HDF5/doc/ADGuide/ImageSpec.html">
     * HDF5 Image and Palette Specification </a>
     * 
     * @return true if the dataset is an image; otherwise, returns false.
     */
    public final boolean isImage()
    {
        return isImage;
    }

    /**
     * Returns true if this dataset is displayed as an image.
     * <p>
     * A ScalarDS can be displayed as an image or table. 
     *  
     * @return true if this dataset is displayed as an image; otherwise, returns false.
     */
    public final boolean isImageDisplay()
    {
        return isImageDisplay;
    }
   
    /**
     * Sets the flag to display the dataset as an image.
     * 
     * @param b if b is true, display the dataset as an image
     */
    public final void setIsImageDisplay(boolean b)
    {
        isImageDisplay = b;
    } 
    
    /**
     * Returns true if this dataset is a true color image.
     */
    
    public final boolean isTrueColor()
    {
        return isTrueColor;
    }

    /**
     * Returns true if this dataset is ASCII text.
     * 
     * @return true if this dataset is ASCII text.
     */
    public final boolean isText()
    {
        return isText;
    }

    /**
     * Returns the interlace mode of a true color image (RGB).
     * 
     * Valid values:
     * <pre>
     *     INTERLACE_PIXEL -- RGB components are contiguous, i.e. rgb, rgb, rgb, ...
     *     INTERLACE_LINE -- each RGB component is stored as a scan line
     *     INTERLACE_PLANE -- each RGB component is stored as a plane
     * </pre>
     * 
     * @return the interlace mode of a true color image (RGB).
     */
    public final int getInterlace()
    {
        return interlace;
    }

    /**
     * Returns true if the original C data are unsigned integers.
     * 
     * @return true if the original C data are unsigned integers.
     */
    public final boolean isUnsigned()
    {
        return isUnsigned;
    }

    /** Returns the (min, max) pair of image data range.
     * 
     * @return the (min, max) pair of image data range.
     */
    public double[] getImageDataRange()
    {
        return imageDataRange;
    }

    /** 
     * Returns the fill values for the dataset.
     * 
     * @return the fill values for the dataset.
     */
    public final Object getFillValue()
    {
        return fillValue;
    }

}
