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
import java.lang.reflect.*;
import ncsa.hdf.hdflib.*;

/**
 * H4GRImage describes HDF4 general raster(GR) image and operations performed on
 * the GR image. An HDF4 raster image is a two-dimension array of pixel values.
 * <p>
 * Every GR data set must contain the following components: image array, name,
 * pixel type, and dimensions. The name, dimensions, and pixel type must be
 * supplied by the user at the time the GR data set is defined.
 * <p>
 * An image array is a two-dimensional array of pixels. Each element in an image
 * array corresponds to one pixel and each pixel can consist of a number of
 * color component values or pixel components, e.g., Red-Green-Blue or RGB,
 * Cyan-Magenta-Yellow-Black or CMYK, etc. Pixel components can be represented
 * by different methods (8-bit lookup table or 24-bit direct representation) and
 * may have different data types. The data type of pixel components and the number
 * of components in each pixel are collectively known as the pixel type.
 * <p>
 * @version 1.0 12/12/2001
 * @author Peter X. Cao, NCSA
 */
public class H4GRImage extends ScalarDS
{
    /**
     * The list of attributes of this data object. Members of the list are
     * instance of Attribute.
     */
    private List attributeList;

    /**
     * The GR interface identifier obtained from SDstart(filename, access)
     */
    private int grid;

    /**
     * The interlace mode of the stored raster image data
     */
    private int interlace;

    /**
     * The number of components in the raster image
     */
    private int ncomp;

    /**
     * The data type of this data set.
     */
    private int datatype;

    /**
     * The indexed RGB color model with 256 colors.
     * <p>
     * The palette values are stored in a two-dimensional byte array and arrange
     * by color components of red, green and blue. palette[][] = byte[3][256],
     * where, palette[0][], palette[1][] and palette[2][] are the red, green and
     * blue components respectively.
     */
    private byte[][] palette;

    /**
     * Creates a H4GRImage object with specific name, path, and object ID.
     * <p>
     * @param fileFormat the HDF file.
     * @param name the name of this H4GRImage.
     * @param path the full path of this H4GRImage.
     * @param oid the unique identifier of this data object.
     */
    public H4GRImage(
        FileFormat fileFormat,
        String name,
        String path,
        long[] oid)
    {
        super (fileFormat, name, path, oid);
        palette = null;
        isImage = true;

        if (fileFormat instanceof H4File)
        {
            this.grid = ((H4File)fileFormat).getGRAccessID();
        }
    }

    // ***** need to implement from DataFormat *****
    public Object read() throws HDFException
    {
        if (data != null)
            return data; // data is loaded

        if (rank <=0 ) init();

        int id = open();
        if (id < 0)
            return null;

        try {
            // set the interlacing scheme for reading image data
            HDFLibrary.GRreqimageil(id, interlace);
            int datasize = getWidth()*getHeight()*ncomp;
            data = H4Accessory.allocateArray(datatype, datasize);

            if (data != null)
            {
                int[] start = {(int)startDims[0], (int)startDims[1]};
                int[] select = {(int)selectedDims[0], (int)selectedDims[1]};
                HDFLibrary.GRreadimage(id, start, null, select, data);
            }
        } finally
        {
            close(id);
        }

        return data;
    }

    // ***** need to implement from DataFormat *****
    public void write() throws HDFException {;}

    // ***** need to implement from DataFormat *****
    public List getMetadata() throws HDFException
    {
        if (attributeList != null)
            return attributeList;

        int id = open();
        String[] objName = {""};
        int[] grInfo = new int[4]; //ncomp, data_type, interlace, and num_attrs
        int[] idims = new int[2];
        try {
            HDFLibrary.GRgetiminfo(id, objName, grInfo, idims);
            // mask off the litend bit
            grInfo[1] = grInfo[1] & (~HDFConstants.DFNT_LITEND);
            int n = grInfo[3];

            if (attributeList == null && n>0)
                attributeList = new Vector(n, 5);

            boolean b = false;
            String[] attrName = new String[1];
            int[] attrInfo = {0, 0}; // data_type, length
            for (int i=0; i<n; i++)
            {
                attrName[0] = "";
                try {
                    b = HDFLibrary.GRattrinfo(id, i, attrName, attrInfo);
                    // mask off the litend bit
                    attrInfo[0] = attrInfo[0] & (~HDFConstants.DFNT_LITEND);
                } catch (HDFException ex)
                {
                    b = false;
                }

                if (!b) continue;

                long[] attrDims = {attrInfo[1]};
                Attribute attr = new Attribute(attrName[0], attrInfo[0], attrDims);;
                attributeList.add(attr);

                Object buf = H4Accessory.allocateArray(attrInfo[0], attrInfo[1]);
                try {
                    HDFLibrary.GRgetattr(id, i, buf);
                } catch (HDFException ex)
                {
                    buf = null;
                }

                if (buf != null)
                {
                    if (attrInfo[0] == HDFConstants.DFNT_CHAR ||
                        attrInfo[0] ==  HDFConstants.DFNT_UCHAR8)
                    {
                        buf = Dataset.byteToString((byte[])buf, attrInfo[1]);
                    }

                    attr.setValue(buf);
                }
            } // for (int i=0; i<n; i++)
        } finally {
            close(id);
        }

        return attributeList;
    }

    // ***** need to implement from DataFormat *****
    public void writeMetadata(Object info) throws HDFException {;}

    // ***** need to implement from DataFormat *****
    public void removeMetadata(Object info) throws HDFException {;}

    // ***** need to implement from HObejct *****
    public int open()
    {

        int id = -1;
        try {
            int index = HDFLibrary.GRreftoindex(grid, (short)oid[1]);
            id = HDFLibrary.GRselect(grid, index);
        } catch (HDFException ex)
        {
            id = -1;
        }

        return id;
    }

    // ***** need to implement from HObejct *****
    public static void close(int grid)
    {
        try { HDFLibrary.GRendaccess(grid); }
        catch (HDFException ex) {;}
    }

    // ***** need to implement from Dataset *****
    public void init()
    {
        int id = open();
        String[] objName = {""};
        int[] grInfo = new int[4]; //ncomp, data_type, interlace and num_attrs
        int[] idims = new int[2];
        try {
            HDFLibrary.GRgetiminfo(id, objName, grInfo, idims);
            // mask off the litend bit

            grInfo[1] = grInfo[1] & (~HDFConstants.DFNT_LITEND);
            datatype = grInfo[1];
        } catch (HDFException ex) {}
        finally {
            close(id);
        }

        if (idims == null)
            return;

        rank = 2; // support only two dimensional raster image
        ncomp = grInfo[0];
        dims = new long[rank];
        startDims = new long[rank];
        selectedDims = new long[rank];
        for (int i=0; i<rank; i++)
        {
            startDims[i] = 0;
            selectedDims[i] = (long)idims[i];
            dims[i] = (long)idims[i];
        }
    }

    // ***** need to implement from ScalarDS *****
    public byte[][] getPalette()
    {
        if (palette != null)
            return palette;

        int id = open();
        if (id < 0)
            return null;

        // get palette info.
        int lutid  = -1;
        int[] lutInfo = new int[4]; //ncomp, datatype, interlace, num_entries
        try {
            // find the first palette.
            // Todo: get all the palettes
            lutid = HDFLibrary.GRgetlutid(id, 0);
            HDFLibrary.GRgetlutinfo(lutid, lutInfo);
        } catch (HDFException ex)
        {
            close(id);
            return null;
        }

        // check if there is palette data. HDFLibrary.GRgetlutinfo() sometimes
        // return true even if there is no palette data, and check if it is a
        // RGB with 256 colors
        if ((lutInfo[0] != 3) || (lutInfo[2] < 0) | (lutInfo[3] != 256))
        {
            close(id);
            return null;
        }

        // read palette data
        boolean b = false;
        byte[] pal = new byte[3*256];
        try
        {
            HDFLibrary.GRreqlutil(id, lutInfo[2]);
            b = HDFLibrary.GRreadlut(lutid, pal);
        } catch (HDFException ex) {
            b = false;
        }

        if (!b)
        {
            close(id);
            return null;
        }

        palette = new byte[3][256];
        if (lutInfo[2] == HDFConstants.MFGR_INTERLACE_PIXEL)
        {
            // color conponents are arranged in RGB, RGB, RGB, ...
            for (int i=0; i<256; i++)
            {
                palette[0][i] = pal[i*3];
                palette[1][i] = pal[i*3+1];
                palette[2][i] = pal[i*3+2];
            }
        }
        else
        {
            for (int i=0; i<256; i++)
            {
                palette[0][i] = pal[i];
                palette[1][i] = pal[256+i];
                palette[2][i] = pal[512+i];
            }
        }


        close(id);
        return palette;
    }

    /**
     * Returns the width of this image.
     */
    public int getWidth()
    {
        if (selectedDims == null)
            return 0;

        return (int)selectedDims[0]; // the first dimension is the width
    }

    /**
     * Returns the height of this image.
     */
    public int getHeight()
    {
        if (selectedDims == null)
            return 0;

        return (int)selectedDims[1]; // the second dimension is the width
    }

    /**
     * Returns the interlace of this image.
     */
    public int getInterlace()
    {
        return interlace;
    }

    /**
     * Returns the number of components of this image data.
     */
    public int getComponentCount()
    {
        return ncomp;
    }

}
