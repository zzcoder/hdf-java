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

import java.lang.reflect.Array;
import java.util.Vector;

/**
 * The abstract class provides general APIs to read/write information of datasets.
 * Information includes datatype, dimension sizes and data content.
 * <p>
 * One of the important operations on datasets is subsetting. Subsetting is needed
 * for reading different parts of the a dataset or different resolution on large
 * datasets.
 * <p>
 * <b>How to Select a Subset</b>
 * <p>
 * Dataset defines APIs for read/write whole/subset of a dataset. However, no specific 
 * function is defined to select a subset. The selection is done in an implicit way.
 * <p>
 * A selection is specified by three arrays: start, stride and count.
 * <ol>
 *   <li>start:  offset of a selection
 *   <li>stride: determining how many elements to move in each dimension
 *   <li>count:  number of elements to select in each dimension
 * </ol>
 * getStartDims(), getStartDims() and getSelectedDims() returns the start, stride and count
 * arrays respectively. Applications can make a selection by changing the values of the arrays.
 * <p>
 * The following example shows how to make a subset. In the example, the dataset
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
 * @see ncsa.hdf.object.ScalarDS
 * @see ncsa.hdf.object.CompoundDS
 *
 * @version 1.0 12/12/2001
 * @author Peter X. Cao, NCSA
 */
public abstract class Dataset extends HObject
{
    /** H5Z decode filter is enabled */
    public static final String H5Z_FILTER_CONFIG_DECODE_ENABLED = "H5Z_FILTER_CONFIG_DECODE_ENABLED";

    /** H5Z encode filter is enabled */
    public static final String H5Z_FILTER_CONFIG_ENCODE_ENABLED = "H5Z_FILTER_CONFIG_ENCODE_ENABLED";

    /**
     *  The memory buffer that holds the values of this dataset. The type of the data
     * object will be defined by implementing classes.
     */
    protected Object data;

    /**
     * The number of dimensions of this dataset.
     */
    protected int rank;

    /**
     * The current dimension sizes of this dataset
     */
    protected long[] dims;

    /**
     * The number of data points of each dimension of the selected subset.
     * The select size must be less than or equal to the current dimension size.
     * With both the starting position and selected sizes, a subset of a rectangle
     * selection is fully defined.
     * <p>
     * For example, a 4 X 5 data set
     * <pre>
     *     0,  1,  2,  3,  4
     *    10, 11, 12, 13, 14
     *    20, 21, 22, 23, 24
     *    30, 31, 32, 33, 34
     * long[] dims = {4, 5};
     * long[] startDims = {1, 2};
     * long[] selectedDims = {3, 3};
     *
     * then the following subset is selected by the startDims and selectedDims above
     *     12, 13, 14
     *     22, 23, 24
     *     32, 33, 34
     */
    protected long[] selectedDims;

    /**
     * The starting position of each dimension of the selected subset.
     * With both the starting position and selected sizes, the subset of a
     * rectangle selection is fully defined.
     */
    protected long[] startDims;

    /**
     * Indices of 3D selected dimensions [height]width][frame].
     * <B>selectedIndex[] is provied for two purpose:</B>
     * <OL>
     * <LI>
     * selectedIndex[] is used to indicate the order of dimensions for display.
     * selectedIndex[0] = row, selectedIndex[1] = column and selectedIndex[2] = depth.
     * For example, for a four dimesion dataset, if selectedIndex[] = {1, 2, 3},
     * then dim[1] is selected as row index, dim[2] is selected as column index
     * and dim[3] is selected as depth index.
     * <LI>
     * selectedIndex[] is also used to select dimensions for display for
     * datasets with three or more dimensions. We assume that application such
     * as HDFView can only display data up to three dimension (2D spreadsheet/image
     * with a third dimension which the 2D spreadsheet/image is cut from). For
     * dataset with more than three dimensions, we need selectedIndex[] to store
     * which three dimensions are chosen for display.
     * For example, for a four dimesion dataset, if selectedIndex[] = {1, 2, 3},
     * then dim[1] is selected as row index, dim[2] is selected as column index
     * and dim[3] is selected as depth index. dim[0] is not selected. Its location
     * is fixed at 0 by default.
     * </OL>
     */
    protected final int[] selectedIndex;

    /**
     * The number of elements to move from the start location in each dimension.
     *  For example, if selectedStride[0] = 2, every other data point is selected
     *  along dim[0].
     */
    protected long[] selectedStride;

    /**
     * The chunk size of each dimension
     */
    protected long[] chunkSize;

    /**
     * Compression level.
     */
    protected String compression;

    /** the datatype of this dataset. */
    protected Datatype datatype;

    /** names of dimensions */
    protected String[] dimNames;

    /** Flag to indicate if the byte[] array is converted to strings */
    protected boolean convertByteToString = true;

    /** Flag to indicate if data values are loaded into memory */
    protected boolean isDataLoaded = false;

    /** The number of selected data points in memory */
    protected long nPoints = 1;

    /** The original data buffer. It is useful when unsigned C data is converted */
    protected Object originalBuf = null;

    /** The converted data buffer. It is useful when unsigned C data is converted */
    protected Object convertedBuf = null;


    /**
     * Constructs a Dataset object with a given file, name and path.
     * <p>
     * @param fileFormat the file that contains the dataset.
     * @param name the name of the Dataset, e.g. "dset1".
     * @param path the full group path of this Dataset, e.g. "/arrays/".
     */
    public Dataset(FileFormat fileFormat, String name, String path)
    {
        this(fileFormat, name, path, null);
    }

    /**
     * Constructs a Dataset object with a given file, name and path.
     * <p>
     * @param fileFormat the file that contains the dataset.
     * @param name the name of the Dataset, e.g. "dset1".
     * @param path the full group path of this Dataset, e.g. "/arrays/".
     * @param oid the unique identifier of this dataset, null if it is unknown.
     */
    public Dataset(
        FileFormat fileFormat,
        String name,
        String path,
        long[] oid)
    {
        super (fileFormat, name, path, oid);

        rank = 0;
        data = null;
        dims = null;
        selectedDims = null;
        startDims = null;
        selectedStride = null;
        chunkSize = null;
        compression = "NONE";
        dimNames = null;

        selectedIndex = new int[3];
        selectedIndex[0] = 0;
        selectedIndex[1] = 1;
        selectedIndex[2] = 2;
    }

    /**
     * Clears memory held by the dataset, such as data buffer.
     */
    public void clear() {
     	if (data != null) {
     		if (data instanceof Vector)
     			((Vector) data).setSize(0);
     		data = null;
     	}
     	isDataLoaded = false;
    }
    
    /**
     * Retrieves information of this dataset from file.
     * Information includes datatype, dimension sizes, compound members for 
     * compound dataset, and etc.
     * <p>
     * The init() is designed to support lazy operation in dataset object. 
     * When a data object is retrieved from file, it does not load the datatype 
     * and dataspace information, or data value into memory. When it is asked 
     * to load the data content, init() is first called to get the datatype 
     * and dataspace information, then load the data content.
     * <strong>Applications do not need to call this function. It is called in 
     * other functions such as Dataset.read() when needed.</strong>
     */
    public abstract void init();

    /**
     * Returns the rank (number of dimensions) of this dataset.
     * 
     * @return the number of dimensions of the dataset.
     */
    public final int getRank()
    {
        return rank;
    }

    /**
     * Returns an array of long which are the dimension sizes of the dataset.
     * 
     * @return the dimension sizes of the dataset.
     */
    public final long[] getDims()
    {
        return dims;
    }

    /**
     * Returns the dimension sizes of the selected subset.
     * 
     * The SelectedDims is the number of data points of the selected subset. Applications
     * can use this array to change the size of selected subset.
     *  
     * The select size must be less than or equal to the current dimension size.
     * Combined with the starting position and selected sizes, the subset of a
     * rectangle selection is fully defined.
     * <p>
     * For example, a 4 X 5 dataset
     * <pre>
     *     0,  1,  2,  3,  4
     *    10, 11, 12, 13, 14
     *    20, 21, 22, 23, 24
     *    30, 31, 32, 33, 34
     * long[] dims = {4, 5};
     * long[] startDims = {1, 2};
     * long[] selectedDims = {3, 3};
     *
     * then the following subset is selected by the startDims and selectedDims
     *     12, 13, 14
     *     22, 23, 24
     *     32, 33, 34
     * </pre>
     * 
     * @return the dimension sizes of the selected subset.
     */
    public final long[] getSelectedDims()
    {
        return selectedDims;
    }

    /**
     * Returns the starting position of the selected subset.
     * 
     * Applications can use this array to change the starting coordinates of a selection. 
     * Combined with the selected sizes, the subset of a rectangle selection is defined.
     * <p>
     * For example, a 4 X 5 dataset
     * <pre>
     *     0,  1,  2,  3,  4
     *    10, 11, 12, 13, 14
     *    20, 21, 22, 23, 24
     *    30, 31, 32, 33, 34
     * long[] dims = {4, 5};
     * long[] startDims = {1, 2};
     * long[] selectedDims = {3, 3};
     *
     * then the following subset is selected by the startDims and selectedDims
     *     12, 13, 14
     *     22, 23, 24
     *     32, 33, 34
     * </pre>
     */
    public final long[] getStartDims()
    {
        return startDims;
    }

    /**
     * Returns the selectedStride of the selected dataset.
     * 
     * Applications can use this array to change how many elements to move in each dimension.
     * 
     * Combined with the starting position and selected sizes, the subset of a
     * rectangle selection is defined.
     * <p>
     * For example, a 4 X 5 dataset
     * <pre>
     *     0,  1,  2,  3,  4
     *    10, 11, 12, 13, 14
     *    20, 21, 22, 23, 24
     *    30, 31, 32, 33, 34
     * long[] dims = {4, 5};
     * long[] startDims = {0, 0};
     * long[] selectedDims = {2, 2};
     * long[] selectedStride = {2, 3};
     *
     * then the following subset is selected by the startDims and selectedDims
     *     0,   3
     *     20, 23
     * </pre>
     */
    public final long[] getStride()
    {
        if (rank <=0) return null;

        if (selectedStride == null)
        {
            selectedStride = new long[rank];
            for (int i=0; i<rank; i++)
                selectedStride[i] = 1;
        }

        return selectedStride;
    }

    /**
     * Sets the flag for byte-to-string conversion.
     * <p>
     * In a string dataset, C-type strings (byte char array) are converted
     * to Java String objects by default. For a large dataset (e.g. more
     * than one million strings), there is a problem because an Java object 
     * string requires much more memory than an byte array and the conversion
     * from byte array to strings takes a lot of time.
     * 
     * <p>
     * setConvertByteToString(boolean b) sets the flag so that application
     * can choose to perform the byte-to-string conversion or not. If the
     * flag is set to false, the getData() returns a array of byte instead
     * of array of strings. 
     *  
     * @param b if b is true if byte array is converted to strings; otherwise,
     *          if false, do not convert byte array to strings.
     */
    public final void setConvertByteToString(boolean b)
    {
        convertByteToString = b;
    }

    /**
     * Returns the flag for byte-to-string conversion.
     * 
     * @return true if byte array is converted to string; otherwise, returns false.
     */
    public final boolean getConvertByteToString()
    {
        return convertByteToString;
    }

    /** 
     * Reads raw data from a dataset into a buffer. 
     * <p>
     * read() reads data of the dataset from file into memory buffer 
     * if data is not read. If data is already in memory, read() just
     * returns the memory buffer. 
     * <p>
     * read() reads a (partial) dataset from the file into memory. The selection 
     * of the data being read is speficied by start, stride and count. For more
     * details on how to select a subset, read
     * @see #getStartDims()
     * @see #getStride()
     * @see #getSelectedDims()
     * <p>
     * For ScalarDS, the memory data object is an one-dimensional array of byte,
     * short, int, float, double or String type based on the datatype of the 
     * dataset. 
     * <p>
     * For CompoundDS, the meory data object is an java.util.List object. Each
     * element of the list is a data array of a compound field. 
     * <p>
     * For example, if compound dataset "comp" has the following nested structure,
     * and memeber datatypes
     * <pre>
     * comp --> m01 (int)
     * comp --> m02 (float)
     * comp --> nest1 --> m11 (char)
     * comp --> nest1 --> m12 (String)
     * comp --> nest1 --> nest2 --> m21 (long)
     * comp --> nest1 --> nest2 --> m22 (double)
     * </pre>
     * read() returns a list of six arrays: {int[], float[], char[], Stirng[], long[] and double[]}.
     *
     * @return the data object.
     */
    public abstract Object read() throws Exception, OutOfMemoryError;

    /** 
     * Reads data values of this dataset from file to byte array.
     * <p>
     *  readBytes() loads data as arry of bytes instead of array of its datatype.
     * For example, for an one-dimension 32-bit integer dataset of size 5,
     * the readBytes() returns of a byte array of size 20 instead of an int array
     * of 5.
     * <p>
     * readBytes() can be used to effciently copy data values. Since the raw byte data
     * is not converted to its native type, it saves memory space and CPU time.
     * 
     * @return the 1D byte array.
     */
    public abstract byte[] readBytes() throws Exception;

    /**
     * Write data from a memory buffer into a file.
     * 
     * @param buf the data to write
     */
    public abstract void write(Object buf) throws Exception;

    /** 
     * Write memory buffer of this dataset to file. 
     */
    public final void write() throws Exception
    {
        if (data != null)
            write(data);
    }

    /**
     * Copy a subset of this dataset to a new dataset.
     * <p>
     * This function allows applications to create a new dataset from
     * a selection. For example, we can select a specific interesting part 
     * from a large image and create a new image with it.
     * <p>
     * The new dataset retains the same datatype but different dataspace.
     *  
     * @param pgroup the group which the dataset is copied to.
     * @param name the name of the new dataset.
     * @param dims the dimension sizes of the the new dataset.
     * @param data the data values of the subset to be copied.
     * 
     * @return the new dataset.
     */
    public abstract Dataset copy(Group pgroup, String name, long[] dims, Object data) throws Exception;

    /** 
     * Returns the datatype of this dataset. 
     * 
     * @return the datatype of this dataset.
     */
    public abstract Datatype getDatatype();

    /** 
     * Returns the value array of this dataset.
     * 
     * If data is loaded into memory, returns the data value; otherwise, 
     *  calls read() to read data from file into memory.
     *  
     *  @see #read()
     *  
     *  @return the memory buffer of the dataset.
     */
    public final Object getData() throws Exception, OutOfMemoryError
    {
        if (!isDataLoaded) {
            data = read(); // load the data;
            originalBuf = data;
            isDataLoaded = true;
            nPoints = 1;
            for (int j=0; j<selectedDims.length; j++)
                nPoints *= selectedDims[j];
        }

        return data;
    }

    /**
     * Clears the data buffer in memory.
     * 
     * read() reads data of the dataset from file into memory buffer 
     * if data is not read. If data is already in memory, read() just
     * returns the memory buffer. Sometimes we want to force read()
     * to re-read data from file. For example, when the selection is 
     * changed, we need to re-read the data. 
     *  
     * clearData() clears the current memory buffer and force the read()
     * to load the data from file.
     * 
     * @see #getData()
     * @see #read()
     */
    public void clearData()
    {
        isDataLoaded = false;
    }

    /**
     * Returns the size of dimension of the vertical axis.
     * 
     * <p> 
     *  This function is used by GUI applications such as HDFView. GUI applications
     *  display dataset a 2D Table or 2D Image. The display order is specified 
     *  by the index array of selectedIndex as follow:
     *  <dl>
     *  <dt> selectedIndex[0] -- height</dt>
     *    <dd> The vertical axis </dd>
     *  <dt> selectedIndex[1] -- width </dt>
     *    <dd> The horizontal axis </dd>
     *  <dt> selectedIndex[2] -- depth </dt>
     *    <dd> The depth axis, which is used for 3 or more dimension datasets. </dd>
     *  </dl>
     *  Applications can use getSelectedIndex() to access and change the display
     *  order. For example, in a 2D dataset of 200x50 (dim0=200, dim1=50), the following
     *  code will set the height=200 and width=100.
     *  <pre>
     *      long[] selectedIndex = dataset.getSelectedIndex();
     *      selectedIndex[0] = 0;
     *      selectedIndex[1] = 1;
     *  </pre>
     * 
     * @see #getSelectedIndex()
     * @see #getWidth()
     * 
     * @return the size of dimension of the vertical axis.
     */
    public final int getHeight()
    {
        if (selectedDims == null ||
            selectedIndex == null )
            return 0;

        return (int)selectedDims[selectedIndex[0]];
    }

    /**
     * Returns the size of dimension of the horizontal axis.
     * 
     * <p> 
     *  This function is used by GUI applications such as HDFView. GUI applications
     *  display dataset a 2D Table or 2D Image. The display order is specified 
     *  by the index array of selectedIndex as follow:
     *  <dl>
     *  <dt> selectedIndex[0] -- height</dt>
     *    <dd> The vertical axis </dd>
     *  <dt> selectedIndex[1] -- width </dt>
     *    <dd> The horizontal axis </dd>
     *  <dt> selectedIndex[2] -- depth </dt>
     *    <dd> The depth axis, which is used for 3 or more dimension datasets. </dd>
     *  </dl>
     *  Applications can use getSelectedIndex() to access and change the display
     *  order. For example, in a 2D dataset of 200x50 (dim0=200, dim1=50), the following
     *  code will set the height=200 and width=100.
     *  <pre>
     *      long[] selectedIndex = dataset.getSelectedIndex();
     *      selectedIndex[0] = 0;
     *      selectedIndex[1] = 1;
     *  </pre>
     * 
     * @see #getSelectedIndex()
     * @see #getHeight()
     * 
     * @return the size of dimension of the horizontal axis.
     */
    public final int getWidth()
    {
        if (selectedDims == null ||
            selectedIndex == null )
            return 0;

        if (selectedDims.length < 2 ||
            selectedIndex.length < 2)
            return 1;

        return (int)selectedDims[selectedIndex[1]];
    }

    /**
     * Returns the indices of display order.
     * <p>
     * 
     * <B>selectedIndex[] is provided for two purpose:</B>
     * <OL>
     * <LI>
     * selectedIndex[] is used to indicate the order of dimensions for display.
     * selectedIndex[0] = row, selectedIndex[1] = column and selectedIndex[2] = depth.
     * <br>
     * For example, for a four dimesion dataset, if selectedIndex[] = {1, 2, 3},
     * then dim[1] is selected as row index, dim[2] is selected as column index
     * and dim[3] is selected as depth index.
     * <LI>
     * selectedIndex[] is also used to select dimensions for display for
     * datasets with three or more dimensions. We assume that applications such
     * as HDFView can only display data values up to three dimension (2D spreadsheet/image
     * with a third dimension which the 2D spreadsheet/image is selected from). For
     * dataset with more than three dimensions, we need selectedIndex[] to tell
     * applications which three dimensions are chosen for display.
     * <br>
     * For example, for a four dimesion dataset, if selectedIndex[] = {1, 2, 3},
     * then dim[1] is selected as row index, dim[2] is selected as column index
     * and dim[3] is selected as depth index. dim[0] is not selected. Its location
     * is fixed at 0 by default.
     * </OL>
     * 
     * @return the indices of display order.
     */
    public final int[] getSelectedIndex()
    {
        return selectedIndex;
    }

    /**
     * Returns the string representation of compression information such as
     * compression type and level.
     * <p>
     * For example, "SZIP: Pixels per block = 8: H5Z_FILTER_CONFIG_DECODE_ENABLED".
     * 
     * @return the string representation of compression information.
     */
    public final String getCompression()
    {
        return compression;
    }

    /**
     *  Returns the chunk sizes.
     *  
     *  @return the chunk sizes.
     */
    public final long[] getChunkSize()
    {
        return chunkSize;
    }

    /**
     * Converts one-dimension array of unsigned C-type integers to appropriate Java integer.
     * <p>
     * Since Java does not support unsigned integer, values of unsigned C-type integers must
     * be converted into its appropriate Java integer. Otherwise, the data value
     * will not displayed correctly. For example, if an unsigned C byte, x = 200,
     * is stored into an Java byte y, y will be -56 instead of the correct value of 200.
     * <p>
     * Unsigned C integers are upgrade to Java integers according to the following table:
     * <TABLE CELLSPACING=0 BORDER=1 CELLPADDING=5 WIDTH=400>
     *     <caption><b>Mapping Unsigned C Integers to Java Integers</b></caption>
     *     <TR> <TD><B>Unsigned C Integer</B></TD> <TD><B>JAVA Intege</B>r</TD> </TR>
     *     <TR> <TD>unsigned byte</TD> <TD>signed short</TD> </TR>
     *     <TR> <TD>unsigned short</TD> <TD>signed int</TD> </TR>
     *     <TR> <TD>unsigned int</TD> <TD>signed long</TD> </TR>
     *     <TR> <TD>unsigned long</TD> <TD>signed long</TD> </TR>
     * </TABLE>
     * <strong>NOTE: this conversion cannot deal with unsigned 64-bit integers. Therefore,
     *       the values of unsigned 64-bit dataset may be wrong in Java application</strong>.
     * <p>
     * If memory data of unsigned integers is converted by convertFromUnsignedC(),
     * convertToUnsignedC() must be called to convert the data back to unsigned C before
     * data is written into file.
     *  
     * @see #convertToUnsignedC(Object)
     * @see #convertToUnsignedC(Object, Object)
     * 
     * @param data_in the input 1D array of the unsigned C-type integers.
     * 
     * @return the upgraded 1D array of Java integers.
     */
    public static Object convertFromUnsignedC(Object data_in)
    {
        return Dataset.convertFromUnsignedC(data_in, null);
    }

    /**
     * Converts one-dimension array of unsigned C-type integers to appropriate Java integer.
     * <p>
     * Since Java does not support unsigned integer, values of unsigned C-type integers must
     * be converted into its appropriate Java integer. Otherwise, the data value
     * will not displayed correctly. For example, if an unsigned C byte, x = 200,
     * is stored into an Java byte y, y will be -56 instead of the correct value of 200.
     * <p>
     * Unsigned C integers are upgrade to Java integers according to the following table:
     * <TABLE CELLSPACING=0 BORDER=1 CELLPADDING=5 WIDTH=400>
     *     <caption><b>Mapping Unsigned C Integers to Java Integers</b></caption>
     *     <TR> <TD><B>Unsigned C Integer</B></TD> <TD><B>JAVA Intege</B>r</TD> </TR>
     *     <TR> <TD>unsigned byte</TD> <TD>signed short</TD> </TR>
     *     <TR> <TD>unsigned short</TD> <TD>signed int</TD> </TR>
     *     <TR> <TD>unsigned int</TD> <TD>signed long</TD> </TR>
     *     <TR> <TD>unsigned long</TD> <TD>signed long</TD> </TR>
     * </TABLE>
     * <strong>NOTE: this conversion cannot deal with unsigned 64-bit integers. Therefore,
     *       the values of unsigned 64-bit dataset may be wrong in Java application</strong>.
     * <p>
     * If memory data of unsigned integers is converted by convertFromUnsignedC(),
     * convertToUnsignedC() must be called to convert the data back to unsigned C before
     * data is written into file.
     *  
     * @see #convertToUnsignedC(Object)
     * @see #convertToUnsignedC(Object, Object)
     * 
     * @param data_in the input 1D array of the unsigned C-type integers.
     * @param data_out the upgraded 1D array of Java integers
     * 
     * @return the upgraded 1D array of Java integers.
     */
    public static Object convertFromUnsignedC(Object data_in, Object data_out)
    {
        if (data_in == null)
            return null;

        Class data_class = data_in.getClass();
        if (!data_class.isArray())
            return null;

        if (data_out != null) {
            Class data_class_out = data_out.getClass();
            if (!data_class_out.isArray() || (Array.getLength(data_in) != Array.getLength(data_out)))
                data_out = null;
        }

        String cname = data_class.getName();
        char dname = cname.charAt(cname.lastIndexOf("[")+1);
        int size = Array.getLength(data_in);
        
		if (dname == 'B') {
            short[] sdata = null;
            if (data_out == null)
                sdata = new short[size];
            else
                sdata = (short[])data_out;
            byte[] bdata = (byte[])data_in;
            short value = 0;
            for (int i=0; i<size; i++)
            {
                //value = (short)bdata[i];
                //if (value < 0) value += 256;
                //sdata[i] = value;
                if((bdata[i] & 0x80)==0x80) 
                	sdata[i] = (short) (128 + (bdata[i] & 0x7f));
                else 
                	sdata[i] = (short) bdata[i];
            }
            data_out = sdata;
        }
        else if (dname == 'S') {
            int[] idata = null;
            if (data_out == null)
                idata = new int[size];
            else
                idata = (int[]) data_out;
            short[] sdata = (short[])data_in;
            int value = 0;
            for (int i=0; i<size; i++)
            {
                value = (int)sdata[i];
                if (value < 0) value += 65536;
                idata[i] = value;
            }
            data_out = idata;
        }
        else if (dname == 'I') {
            long[] ldata = null;
            if (data_out == null)
                ldata = new long[size];
            else
                ldata = (long[])data_out;
            int[] idata = (int[])data_in;
            long value = 0;
            for (int i=0; i<size; i++)
            {
                value = (long)idata[i];
                if (value < 0) value += 4294967296L;
                ldata[i] = value;
            }
            data_out = ldata;
        }
        else data_out = data_in;
        // Java does not support unsigned long
		
        return data_out;
    }

    /**
     * Converts Java integer data back to unsigned C-type integer data.
     * <p>
     * If memory data of unsigned integers is converted by convertFromUnsignedC(),
     * convertToUnsignedC() must be called to convert the data back to unsigned C before
     * data is written into file.
     * 
     * @see #convertFromUnsignedC(Object)
     * @see #convertFromUnsignedC(Object, Object)
     * 
     * @param data_in the input Java integer to be converted.
     * @return the converted data of unsigned C-type integer array.
     */
    public static Object convertToUnsignedC(Object data_in)
    {
        return Dataset.convertToUnsignedC(data_in, null);
    }

    /**
     * Converts Java integer data back to unsigned C-type integer data.
     * <p>
     * If memory data of unsigned integers is converted by convertFromUnsignedC(),
     * convertToUnsignedC() must be called to convert the data back to unsigned C before
     * data is written into file.
     * 
     * @see #convertFromUnsignedC(Object)
     * @see #convertFromUnsignedC(Object, Object)
     * 
     * @param data_in the input Java integer to be converted.
     * @param data_out the converted data of unsigned C-type integer.
     * 
     * @return the converted data of unsigned C-type integer array.
     */
    public static Object convertToUnsignedC(Object data_in, Object data_out)
    {
        if (data_in == null)
            return null;

        Class data_class = data_in.getClass();
        if (!data_class.isArray())
            return null;

        if (data_out != null) {
            Class data_class_out = data_out.getClass();
            if (!data_class_out.isArray() || (Array.getLength(data_in) != Array.getLength(data_out)))
                data_out = null;
        }

        String cname = data_class.getName();
        char dname = cname.charAt(cname.lastIndexOf("[")+1);
        int size = Array.getLength(data_in);

        if (dname == 'S') {
            byte[] bdata = null;
            if (data_out == null)
                bdata = new byte[size];
            else
                bdata = (byte[])data_out;
            short[] sdata = (short[])data_in;
            for (int i=0; i<size; i++)
                bdata[i] = (byte)sdata[i];
            data_out = bdata;
        }
        else if (dname == 'I') {
            short[] sdata = null;
            if (data_out == null)
                sdata = new short[size];
            else
                sdata = (short[])data_out;
            int[] idata = (int[])data_in;
            for (int i=0; i<size; i++)
                sdata[i] = (short)idata[i];
            data_out = sdata;
        }
        else if (dname == 'J') {
            int[] idata = null;
            if (data_out == null)
                idata = new int[size];
            else
                idata = (int[])data_out;
            long[] ldata = (long[])data_in;
            for (int i=0; i<size; i++)
                idata[i] = (int)ldata[i];
            data_out = idata;
        }
        else data_out = data_in;
        // Java does not support unsigned long

        return data_out;
    }

    /**
     * Converts an array of bytes into an array of Strings.
     * <p>
     * A C-string is an array of chars while an Java String is an object.
     * When a string dataset is read into Java application, the data is
     * stored in an array of Java bytes. byteToString() is used to convert
     * the array of bytes into array of Java strings so that applications
     * can display and modify the data content.
     * <p>
     * For example, the content of a two element C string dataset is
     * {"ABC", "abc"}. Java applications will read the data into an
     * byte array of {65, 66, 67, 97, 98, 99). byteToString(bytes, 3)
     * returns an array of Java String of strs[0]="ABC", and strs[1]="abc".
     * <p>
     * If memory data of strings is converted to Java Strings, stringToByte()
     * must be called to convert the memory data back to byte array before data
     * is written to file.
     * 
     * @see #stringToByte(String[], int)
     *  
     * @param bytes the array of bytes to convert.
     * @param length the length of string.
     * 
     * @return the array of Java String.
     */
    public static final String[] byteToString(byte[] bytes, int length)
    {
        if (bytes == null)
            return null;

        int n = (int)bytes.length/length;
        String bigstr = new String(bytes);
        String[] strArray = new String[n];
        String str = null;
        int idx = 0, offset=0;
        for (int i=0; i<n; i++) {
            //str = new String(bytes, i*length, length)
            // bigstr.substring uses less memory space
            offset = i*length;
            str = bigstr.substring(offset, offset+length);
            idx = str.indexOf('\0');
            if (idx > 0) str = str.substring(0, idx);
            strArray[i] = str.trim();
        }

        return strArray;
    }

    /**
     * Converts a string array into an array of bytes.
     * <p>
     * If memory data of strings is converted to Java Strings, stringToByte()
     * must be called to convert the memory data back to byte array before data
     * is written to file.
     *
     * @see #byteToString(byte[] bytes, int length)
     * 
     * @param strings the array of string.
     * @param length the length of string.
     * 
     * @return the array of bytes.
     */
    public static final byte[] stringToByte(String[] strings, int length)
    {
        if (strings == null)
            return null;

        int size = strings.length;
        byte[] bytes = new byte[size*length];

        StringBuffer strBuff = new StringBuffer(length);
        for (int i=0; i<size; i++)
        {
            if (strings[i].length() > length)
                strings[i] = strings[i].substring(0, length);

            // padding the string with space
            strBuff.replace(0, length, " ");
            strBuff.replace(0, length, strings[i]);
            strBuff.setLength(length);
            System.arraycopy(strBuff.toString().getBytes(), 0, bytes, length*i, length);
        }

        return bytes;
    }

    /** 
     * Returns the names of all dimensions.
     * <p>
     * Some datasets have pre-defined names for each dimension such as "Latitude"
     * and "Longitude". getDimNames() returns these pre-defined names.
     * 
     * @return the names of dimensions.
     */
    public final String[] getDimNames() { return dimNames; }

    /**
     * Checks if a given datatype is a string. Sub-classes must replace this
     * default implementation.
     *
     * @param tid The data type identifier.
     * 
     * @return true if the datatype is a string; otherwise returns false.
     */
    public boolean isString(int tid) { return false; }

    /**
     * Returns the size of a given datatype. Sub-classes must replace this
     * default implementation.
     *
     * @param tid The data type identifier.
     * 
     * @return The size of the datatype
     */
    public int getSize(int tid) { return -1; }
}
