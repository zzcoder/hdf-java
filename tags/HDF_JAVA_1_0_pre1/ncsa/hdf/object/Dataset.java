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

/**
 * Dataset is the superclass for HDF4/5 Dataset, inheriting the HObject.
 * <p>
 * Dataset is an abstract class. Its implementing sub-classes are the HDF4 and
 * HDF5 dataset. This class includes general information of a Dataset object
 * such as datatype and dimensions, and common operation on Datasets of HDF4
 * and HDF5.
 * <p>
 * @version 1.0 12/12/2001
 * @author Peter X. Cao, NCSA
 */
public abstract class Dataset extends HObject
{
    /**
     *  The buff which holds the content of this dataset
     */
    protected Object data;

    /**
     * The rank of this dataset
     */
    protected int rank;

    /**
     * The current dimension sizes of this dataset
     */
    protected long[] dims;

    /**
     * The dimension sizes of the selected subset.
     * The select size must be less than or equal to the current dimension size.
     * With both the starting position and selected sizes, the subset of a
     * rectangle selection is fully defined.
     */
    protected long[] selectedDims;

    /**
     * The starting position of each dimension of the selected subset.
     * With both the starting position and selected sizes, the subset of a
     * rectangle selection is fully defined.
     */
    protected long[] startDims;

    /**
     * Indices of selected dimensions. Dataset of one-dimension will be presented
     * as one column data; selectedIndex[0] = 0, Dataset of two-dimension will
     * be presented as two-dimension table data, selectedIndex[0] = row index;
     * selected[1] = column index. Dataset of three or more dimensions will be
     * presented as two-dimension table cutting along a third dimension,
     * selectedIndex[0] = row index; selectedIndex[1] = column index;
     * selectedIndex[2] = depth index.
     */
    protected int[] selectedIndex;

    /**
     * Creates a Dataset object with specific name and path.
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
    }

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
    public abstract void init();

    /**
     * Returns the rank of this dataset.
     */
    public final int getRank()
    {
        return rank;
    }

    /**
     * Returns the current dimension size of this dataset.
     */
    public final long[] getDims()
    {
        return dims;
    }

    /**
     * Returns the dimension size of the selected subset.
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
     * Removes the data value of this dataset in memory.
     */
    public final void clearData()
    {
        if (data != null)
        {
            data = null;
            System.gc();
        }
    }

    /**
     * Converts an array of bytes into an array of String.
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

        String[] strArray = new String[n];
        String str = null;
        for (int i=0; i<n; i++)
        {
            str = new String(bytes, i*length, length);
            strArray[i] = str.trim();
        }

        return strArray;
    }
}
