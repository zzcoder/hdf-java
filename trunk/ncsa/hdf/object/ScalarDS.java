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
 * A scalar dataset is a multiple dimension array of scalar points.
 * Datatype of a scalar dataset must be a atomic datatype. Most common datatypes
 * of a scalar dataset include char, byte, short, int, long, float, double and string.
 * <p>
 * A ScalarDS can be an image or spreadsheet data. ScalarDS defines few methods
 * to deal with both image and spreadsheet.
 * <p>
 * ScalarDS is an abstract class. Current implementing classes are the H4SDS,
 * H5GRImage and H5ScalarDS.
 * <p>
 * <b>How to Select a Subset</b>
 * <p>
 * Dataset defines APIs for read, write and subet a dataset. No function is defined
 * to select a subset of a data array. The selection is done in an implicit way.
 * Function calls to dimension information such as getSelectedDims() return an array
 * of dimension values, which is a reference to the array in the dataset object.
 * Changes of the array outside the dataset object directly change the values of
 * the array in the dataset object. It is like pointers in C.
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
     selectedIndex[1] = 0;

     // reset the selection arrays
     for (int i=0; i<rank; i++) {
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

    /** The component value for a pixel are contiguous. */
    public final static int INTERLACE_PIXEL = 0;

    /** Each component is stored as a scan line. */
    public static final int INTERLACE_LINE = 1;

    /** Each component is stored as a plane. */
    public final static int INTERLACE_PLANE = 2;

    /**
     * The data type of this scalar dataset
     * such as 32-bit integer, 32-bit float, etc.
     */
    protected int nativeDatatype;

    /**
     * The interlace mode of the stored raster image data
     */
    protected int interlace;

    /**
     * The range of image data values.
     * For example, [0, 255]
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

    /** fill value */
    protected Object fillValue = null;
    
    /** Flag to indicate if the dataset is displayed as an image */
    protected boolean isImageDisplay;
    
    /**
     * Constructs a ScalarDS with given name and path
     *
     * @param fileFormat the HDF file.
     * @param name the name of this ScalarDS.
     * @param path the full path of this ScalarDS.
     */
    public ScalarDS(FileFormat fileFormat, String name, String path)
    {
        this(fileFormat, name, path, null);
    }

    /**
     * Creates a ScalarDS object with specific name, path, and parent.
     * <p>
     * @param fileFormat the HDF file.
     * @param name the name of this ScalarDS.
     * @param path the full path of this ScalarDS.
     * @param oid the unique identifier of this data object.
     */
    public ScalarDS(
        FileFormat fileFormat,
        String name,
        String path,
        long[] oid)
    {
        super (fileFormat, name, path, oid);

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
     * Converts the data values of this dataset to appropriate Java integer if it is unsigned integers.
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
     * if if is unsigned integer. 
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
     * different library to retrieve the associated palette.
     * 
     * @return the 2D byte array of palette.
     */
    public abstract byte[][] getPalette();

    /**
     * Sets the palette for this dataset.
     * 
     * @param pal the 2D byte array of palette.
     */
    public final void setPalette(byte[][] pal)
    {
        palette = pal;
    }

    /** Reads specific image palette from file.
     * <p>
     * A scalar dataset may have multiple palettes attached to it.
     * readPalette(int idx) returns specific palette.
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
     * For example, Dataset "Iceberge" has an attribute of object
     * reference "Palette". The arrtibute "Palette" has value "2538" that is
     * the object reference of the palette data set "Iceberge Palette". 
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
     * Returns true if the original C data is unsigned integer.
     * 
     * @return true if the original C data is unsigned integer.
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
