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

package ncsa.hdf.view;

import java.awt.image.*;
import java.io.*;
import ncsa.hdf.object.*;
import javax.swing.tree.DefaultMutableTreeNode;
import com.sun.media.jai.codec.*;

/**
 * The "Tools" class contains various of tools for HDF files such as jpeg to
 * HDF converter.
 * <p>
 * @author Peter X. Cao
 */
public final class Tools
{
    /** tag for JPEG image.*/
    public static final int FILE_TYPE_JPEG = 0;

    /** tag for TIFF image. */
    public static final int FILE_TYPE_TIFF = 1;

    /** tag for PNG image. */
    public static final int FILE_TYPE_PNG = 2;

    /** tag for HDF4 file. */
    public static final int FILE_TYPE_HDF4 = 3;

    /** tag for HDF5 file. */
    public static final int FILE_TYPE_HDF5 = 4;

    /** Converts an image file into HDF4/5 file.
     *  @param imgFileName the input JPEG file.
     *  @param hFileName the name of the HDF4/5 file.
     *  @param fromType the type of image.
     *  @param toType the type of file converted to.
     */
    public static void convertImageToHDF(String imgFileName, String hFileName,
        int fromType, int toType) throws Exception
    {
        File imgFile = null;

        if (imgFileName == null)
            throw new NullPointerException("The source image file is null.");
        else if (!(imgFile = new File(imgFileName)).exists())
            throw new NullPointerException("The source image file does not exist.");
        else if (hFileName == null)
            throw new NullPointerException("The target HDF file is null.");

        ImageDecodeParam param = null;
        String name = null;

        if (fromType == FILE_TYPE_JPEG)
        {
            name = "jpeg";
            param = new JPEGDecodeParam();
        }
        else if (fromType == FILE_TYPE_TIFF)
        {
            name = "tiff";
            param = new TIFFDecodeParam();
        }
        else if (fromType == FILE_TYPE_PNG)
        {
            name = "png";
            param = new PNGDecodeParam();
        }
        else
        {
            throw new UnsupportedOperationException("Unsupported source image type.");
        }

        if ( !(toType == FILE_TYPE_HDF4 || toType == FILE_TYPE_HDF5 ))
             throw new UnsupportedOperationException("Unsupported destination file type.");

        BufferedInputStream in = new BufferedInputStream(new FileInputStream(imgFileName));
        ImageDecoder decoder = ImageCodec.createImageDecoder(name, in, param);
        Raster image = decoder.decodeAsRaster();
        in.close();

        int h = image.getHeight();
        int w = image.getWidth();
        byte[] data = null;

        try { data = new byte[3*h*w]; }
        catch (OutOfMemoryError err)
        {
            throw new RuntimeException("Out of memory error.") ;
        }

        int idx=0;
        int[] pixel = new int[3];
        for (int i=0; i<h; i++)
        {
            for (int j=0; j<w; j++)
            {
                image.getPixel(j, i, pixel);
                data[idx++] = (byte) pixel[0];
                data[idx++] = (byte) pixel[1];
                data[idx++] = (byte) pixel[2];
            }
        }

        int fid = -1;
        Datatype type = null;
        Group pgroup = null;
        Dataset dataset = null;
        String imgName = imgFile.getName();
        if (toType==FILE_TYPE_HDF5)
        {
            long[] h5dims = {h, w, 3}; // RGB pixel interlace
            H5File.create(hFileName);
            H5File h5file = new H5File(hFileName, H5File.WRITE);
            fid = h5file.open();
            pgroup = (Group)((DefaultMutableTreeNode) h5file.getRootNode()).getUserObject();
            type = new H5Datatype(Datatype.CLASS_CHAR, 1, Datatype.NATIVE, Datatype.SIGN_NONE);
            dataset = H5ScalarDS.create(h5file, imgName, pgroup, type, h5dims, null, null, -1, data);
            try { H5File.createImageAttributes(dataset, ScalarDS.INTERLACE_PIXEL); } catch (Exception ex) {}
            h5file.close();
        }
        else if (toType==FILE_TYPE_HDF4)
        {
            long[] h4dims = {w, h, 3}; // RGB pixel interlace
            H4File.create(hFileName);
            H4File h4file = new H4File(hFileName, H4File.WRITE);
            fid = h4file.open();
            pgroup = (Group)((DefaultMutableTreeNode) h4file.getRootNode()).getUserObject();
            type = new H4Datatype(Datatype.CLASS_CHAR, 1, Datatype.NATIVE, Datatype.SIGN_NONE);
            dataset = H4GRImage.create(h4file, imgName, pgroup, type, h4dims, null, null, -1, 3, ScalarDS.INTERLACE_PIXEL, data);
            h4file.close();
        }

        // clean up memory
        data = null;
        image = null;
        Runtime.getRuntime().gc();
    }

    /** Save a BufferedImage into an image file.
     *  @param image the BufferedImage to save.
     *  @param file the image file.
     *  @param type the image type.
     */
    public static void saveImageAs(BufferedImage image, File file, int type)
    throws Exception
    {
        ImageEncodeParam param = null;
        String name = null;

        if (type == FILE_TYPE_JPEG)
        {
            name = "jpeg";
            param = new JPEGEncodeParam();
        }
        else if (type == FILE_TYPE_TIFF)
        {
            name = "tiff";
            param = new TIFFEncodeParam();
        }
        else if (type == FILE_TYPE_PNG)
        {
            name = "png";
            param = new PNGEncodeParam.RGB();
        }
        else
        {
            throw new UnsupportedOperationException("Unsupported image type.");
        }

        BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(file));
        ImageEncoder encoder = ImageCodec.createImageEncoder(name, out, param);
        encoder.encode(image.getData(), image.getColorModel());

        out.flush();
        out.close();
    }

}