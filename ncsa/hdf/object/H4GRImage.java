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
     * The number of components in the raster image
     */
    private int ncomp;

    private boolean unsignedConverted;

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
        unsignedConverted = false;

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
            data = H4Datatype.allocateArray(datatype, datasize);

            if (data != null)
            {
                int[] start = {(int)startDims[0], (int)startDims[1]};
                int[] select = {(int)selectedDims[0], (int)selectedDims[1]};

                int[] stride = null;
                if (selectedStride != null)
                {
                    stride = new int[rank];
                    for (int i=0; i<rank; i++)
                        stride[i] = (int)selectedStride[i];
                }

                HDFLibrary.GRreadimage(id, start, stride, select, data);
            }
        } finally
        {
            close(id);
        }

        return data;
    }

    // Implementing DataFormat
    public void write() throws HDFException
    {
        if (data == null)
            return;

        int id = open();
        if (id < 0) return;

        int[] select = new int[rank];
        int[] start = new int[rank];
        for (int i=0; i<rank; i++)
        {
            select[i] = (int)selectedDims[i];
            start[i] = (int)startDims[i];
        }

        int[] stride = null;
        if (selectedStride != null)
        {
            stride = new int[rank];
            for (int i=0; i<rank; i++)
                stride[i] = (int)selectedStride[i];
        }

        Object tmpData = null;
        try {
            if ( isUnsigned && unsignedConverted)
                tmpData = convertToUnsignedC(data);
            else
                tmpData = data;
            HDFLibrary.GRwriteimage(id, start, stride, select, tmpData);
        } finally
        {
            tmpData = null;
            close(id);
        }
    }

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

                Object buf = H4Datatype.allocateArray(attrInfo[0], attrInfo[1]);
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
    public void writeMetadata(Object info) throws HDFException
    {
        // only attribute metadata is supported.
        if (!(info instanceof Attribute))
            return;

        H4File.writeAttribute(this, (Attribute)info);

        if (attributeList == null)
            attributeList = new Vector();

        attributeList.add(info);
    }

    // ***** need to implement from DataFormat *****
    public void removeMetadata(Object info) throws HDFException {;}

    // Implementing HObject.
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

    // Implementing HObject.
    public void close(int grid)
    {
        try { HDFLibrary.GRendaccess(grid); }
        catch (HDFException ex) {;}
    }

    // Implementing Dataset.
    public void init()
    {
        int id = open();
        String[] objName = {""};
        int[] grInfo = new int[4]; //ncomp, data_type, interlace and num_attrs
        int[] idims = new int[3];
        try {
            HDFLibrary.GRgetiminfo(id, objName, grInfo, idims);
            // mask off the litend bit
            grInfo[1] = grInfo[1] & (~HDFConstants.DFNT_LITEND);
            datatype = grInfo[1];
        } catch (HDFException ex) {}
        finally {
            close(id);
        }

        isUnsigned = H4Datatype.isUnsigned(datatype);

        if (idims == null)
            return;

        ncomp = grInfo[0];
        interlace = grInfo[2];
        rank = 2; // support only two dimensional raster image

        // data in HDF4 GR image is arranged as dim[0]=width, dim[1]=height.
        // other image data is arranged as dim[0]=height, dim[1]=width.
        selectedIndex[0] = 1;
        selectedIndex[1] = 0;

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

    // Implementing ScalarDS
    public void convertFromUnsignedC()
    {
        if (data != null && isUnsigned && !unsignedConverted)
        {
            data = convertFromUnsignedC(data);
            unsignedConverted = true;
        }
    }

    // Implementing ScalarDS
    public void convertToUnsignedC()
    {
        if (data != null && isUnsigned)
        {
            data = convertToUnsignedC(data);
        }
    }

    /**
     * Returns the number of components of this image data.
     */
    public int getComponentCount()
    {
        return ncomp;
    }

}
