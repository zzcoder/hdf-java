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
 * ScalarDS is the superclass for HDF4/5 ScalarDS, inheriting Dataset.
 * <p>
 * ScalarDS is an abstract class. Its implementing sub-classes are the H4SDS,
 * H5GRImage and H5ScalarDS. A scalar dataset is a multiple dimension array of
 * scalar points.
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
    public final static int INTERLACE_PIXEL =
        ncsa.hdf.hdflib.HDFConstants.MFGR_INTERLACE_PIXEL;

    /** Each component is stored as a plane. */
    public final static int INTERLACE_PLANE =
        ncsa.hdf.hdflib.HDFConstants.MFGR_INTERLACE_COMPONENT;

    /**
     * The data type of this scalar dataset
     * such as 32-bit integer, 32-bit float, etc.
     */
    protected int datatype;

    /**
     * The interlace mode of the stored raster image data
     */
    protected int interlace;

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
     * True is this dataset is an image.
     */
    protected boolean isImage;

    /**
     * True is this dataset is ASCII text.
     */
    protected boolean isText;

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

        datatype = -1;
        palette = null;
        isImage = false;
        isText = false;
        interlace = -1;
    }

    /**
     * Returns the type of this scalar dataset.
     */
    public final int getDataType()
    {
        return datatype;
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
     */
    public abstract byte[][] getPalette();

    /**
     * Returns true is this dataset is an image.
     */
    public final boolean isImage()
    {
        return isImage;
    }

    /**
     * Returns true is this dataset is ASCII text.
     */
    public final boolean isText()
    {
        return isText;
    }

    /**
     * Returns the interlace of data points.
     */
    public final int getInterlace()
    {
        return interlace;
    }


}
