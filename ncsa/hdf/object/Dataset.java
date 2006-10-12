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

import java.util.*;
import java.lang.reflect.Array;

/**
 * The abstract class includes general information of a dataset object
 * such as datatype and dimensions, and common operation on the dataset
 * such as read/write data values.
 * <p>
 * Dataset has two subclasses: ScalarDS and CompoundDS. A ScalarDS is a multiple
 * dimension array of atomic data such as int, float, char, etc.
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
     *  The buff which holds the content of this dataset. The type of the data
     * object will be defined by implementing classes.
     */
    protected Object data;

    /**
     * The rank of this dataset.
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
     * Indices of selected dimensions.
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

    /**
     * Constructs a Dataset object with a given file and dataset name and path.
     * <p>
     * @param fileFormat the HDF file.
     * @param name the name of this Dataset.
     * @param path the full path of this Dataset.
     */
    public Dataset(FileFormat fileFormat, String name, String path)
    {
        this(fileFormat, name, path, null);
    }

    /**
     * Constructs a Dataset object with a given file and dataset name and path.
     * <p>
     * @param fileFormat the HDF file.
     * @param name the name of this Dataset.
     * @param path the full path of this Dataset.
     * @param oid the unique identifier of this dataset.
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
     * Initializes the dataset such as dimension sizes of this dataset.
     * Sub-classes must replace this interface. For each file format, initialization
     * of a dataset is different.
     * <p>
     * The Dataset is designed in a way of "ask and load". When a data object
     * is retrieved from file, it does not load the datatype and dataspce
     * information, or data value into memory. When it is asked to open the
     * data, init() is first called to get the datatype and dataspace information,
     * then load the data content.
     */
    public abstract void init();

    /**
     * Returns the rank of this dataset.
     */
    public final int getRank()
    {
        return rank;
    }

    /**
     * Returns the current dimension sizes of this dataset.
     */
    public final long[] getDims()
    {
        return dims;
    }

    /**
     * Returns the dimension size of the selected subset.
     * The SelectedDims is the number of data points of the selected subset.
     * The select size must be less than or equal to the current dimension size.
     * With both the starting position and selected sizes, the subset of a
     * rectangle selection is fully defined.
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
     * then the following subset is selected by the startDims and selectedDims
     *     12, 13, 14
     *     22, 23, 24
     *     32, 33, 34
     */
    public final long[] getSelectedDims()
    {
        return selectedDims;
    }

    /**
     * Returns the starting position of the selected subset.
     */
    public final long[] getStartDims()
    {
        return startDims;
    }

    /**
     * Returns the selectedStride of the selected dataset.
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
     * Sets the state of byte-to-string conversion
     * @param b true if byte array is converted to string; otherwise false
     */
    public final void setConvertByteToString(boolean b)
    {
        convertByteToString = b;
    }

    /**
     * Gets the state of byte-to-string conversion
     * @return true if byte array is converted to string; otherwise false
     */
    public final boolean getConvertByteToString()
    {
        return convertByteToString;
    }

    /** Loads and returns the data value from file. */
    public abstract Object read() throws Exception, OutOfMemoryError;

    /** Reads data values of this dataset into byte array.
     * <p>
     *  readBytes() loads data as arry of bytes instead of array of its datatype.
     * For example, for an one-dimension 32-bit integer dataset of size 5,
     * the readBytes() returns of a byte array of size 20 instead of an int array
     * of 5.
     * <p>
     * readBytes() is most used for copy data values, at which case, data do not
     * need to be changed or displayed. It is efficient for memory space and CPU time.
     */
    public abstract byte[] readBytes() throws Exception;

    /**
     * Write data values into a file.
     * @param buf the data to write
     * @throws Exception
     */
    public abstract void write(Object buf) throws Exception;

    /** Write the data values of this dataset to file. */
    public final void write() throws Exception
    {
        if (data != null)
            write(data);
    }

    /**
     * Copy a subset of this dataset to a new dataset.
     *
     * @param pgroup the group which the dataset is copied to.
     * @param name the name of the new dataset.
     * @param dims the dimension sizes of the the new dataset.
     * @param data the data values of the subset to be copied.
     * @return the new dataset.
     */
    public abstract Dataset copy(Group pgroup, String name, long[] dims,
        Object data) throws Exception;

    /** Returns the datatype of this dataset. */
    public abstract Datatype getDatatype();

    /** If data is loaded into memory, returns the data value; otherwise
     *  load the data value into memory and returns the data value.
     */
    public final Object getData() throws Exception, OutOfMemoryError
    {
        if (data == null)
            data = read(); // load the data;

        return data;
    }

    /** Sets the data value */
    public final void setData(Object d)  { data =d; }

    /**
     * Removes the data buf of this dataset in memory.
     */
    public void clearData()
    {
        if (data != null)
        {
            data = null;
            Runtime.getRuntime().gc();
        }
    }

    /**
     * Returns the height of the dataset -- selectedDims[selectedIndex[0]]
     */
    public final int getHeight()
    {
        if (selectedDims == null ||
            selectedIndex == null )
            return 0;

        return (int)selectedDims[selectedIndex[0]];
    }

    /**
     * Returns the width of the dataset -- selectedDims[selectedIndex[1]];
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
     * Returns the indices of selected dimensions.
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
    public final int[] getSelectedIndex()
    {
        return selectedIndex;
    }

    /**
     * Return the compression level.
     */
    public final String getCompression()
    {
        return compression;
    }

    /**
     *  Returns the chunk sizes.
     */
    public final long[] getChunkSize()
    {
        return chunkSize;
    }

    /**
     * Converts one-dimension array of unsigned C integers to appropriate Java integer.
     * <p>
     * Since Java does not support unsigned integer, unsigned C integers must
     * be converted into its appropriate Java integer. Otherwise, the data value
     * will not displayed correctly. For example, if an unsigned C byte, x = 200,
     * is stored into an Java byte y, y will be -56 instead of the correct value 200.
     * <p>
     * The following table is used to map the unsigned C integer to Java integer
     * <TABLE TABLE CELLSPACING=0 BORDER=1 CELLPADDING=5 WIDTH=400>
     *     <caption><b>Mapping Unsigned C Integers to Java Integers</b></caption>
     *     <TR> <TD><B>Unsigned C Integer</B></TD> <TD><B>JAVA Intege</B>r</TD> </TR>
     *     <TR> <TD>unsigned byte</TD> <TD>signed short</TD> </TR>
     *     <TR> <TD>unsigned short</TD> <TD>signed int</TD> </TR>
     *     <TR> <TD>unsigned int</TD> <TD>signed long</TD> </TR>
     *     <TR> <TD>unsigned long</TD> <TD>signed long</TD> </TR>
     * </TABLE>
     * <b>NOTE: this conversion cannot deal with unsigned 64-bit integers. For
     *          unsigned 64-bit dataset, the values can be wrong in Java
     *          application</b>.
     * <p>
     * @param data_in the input 1D array of the unsigned C.
     * @return the converted 1D array of Java integer data.
     */
    public static Object convertFromUnsignedC(Object data_in)
    {
        if (data_in == null)
            return null;

        Class data_class = data_in.getClass();
        if (!data_class.isArray())
            return null;

        Object data_out = null;
        String cname = data_class.getName();
        char dname = cname.charAt(cname.lastIndexOf("[")+1);
        int size = Array.getLength(data_in);

        if (dname == 'B') {
            short[] sdata = new short[size];
            byte[] bdata = (byte[])data_in;
            short value = 0;
            for (int i=0; i<size; i++)
            {
                value = (short)bdata[i];
                if (value < 0) value += 256;
                sdata[i] = value;
            }
            data_out = sdata;
        }
        else if (dname == 'S') {
            int[] idata = new int[size];
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
            long[] ldata = new long[size];
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
     * Convert Java integer data back to unsigned C integer data.
     * It is used when Java data converted from unsigned C is writen back to file.
     * <p>
     * @see #convertFromUnsignedC(Object data_in)
     * @param data_in the input Java integer to be convert.
     * @return the converted data of unsigned C integer.
     */
    public static Object convertToUnsignedC(Object data_in)
    {
        Object data_out = null;

        if (data_in == null)
            return null;

        Class data_class = data_in.getClass();
        if (!data_class.isArray())
            return null;

        String cname = data_class.getName();
        char dname = cname.charAt(cname.lastIndexOf("[")+1);
        int size = Array.getLength(data_in);

        if (dname == 'S') {
            byte[] bdata = new byte[size];
            short[] sdata = (short[])data_in;
            for (int i=0; i<size; i++)
                bdata[i] = (byte)sdata[i];
            data_out = bdata;
        }
        else if (dname == 'I') {
            short[] sdata = new short[size];
            int[] idata = (int[])data_in;
            for (int i=0; i<size; i++)
                sdata[i] = (short)idata[i];
            data_out = sdata;
        }
        else if (dname == 'J') {
            int[] idata = new int[size];
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
     * For example,
     * <pre>
      * byte[] bytes = {65, 66, 67, 97, 98, 99};
     *  String[] strs = byteToString(bytes, 3);
     *
     *  The "strs" is an array of string of size two with values
     *  strs[0]="ABC", and strs[1]="abc";
     *
     * </pre>
     *
     * <p>
     * @param bytes the array of bytes
     * @param length the length of string
     * @return the array of string.
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
     * @see #byteToString(byte[] bytes, int length)
     * @param strings the array of string
     * @param length the length of string
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

    /** Returns the names of all dimensions */
    public final String[] getDimNames() { return dimNames; }

    /**
     * Checks if a given datatype is a string. Sub-classes must replace this
     * default implementation.
     *
     * @param dtype The data type to check
     * @return true if the datatype is a string; otherwise returns flase.
     */
    public boolean isString(int dtype) { return false; }

    /**
     * Returns the size of a given datatype. Sub-classes must replace this
     * default implementation.
     *
     * @param dtype The data type
     * @return The size of the datatype
     */
    public int getSize(int dtype) { return -1; }
}
