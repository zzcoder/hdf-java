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
import com.sun.image.codec.jpeg.*;

/**
 * The "Tools" class contains various of tools for HDF files such as jpeg to
 * HDF converter.
 * <p>
 * @author Peter X. Cao
 */
public final class Tools
{
    /** Converts an image file into HDF4/5 file.
     *  @param imgFileName the input image file.
     *  @param hFileName the name of the HDF4/5 file.
     *  @param fromType the type of image.
     *  @param toType the type of file converted to.
     */
    public static void convertImageToHDF(String imgFileName, String hFileName,
        String fromType, String toType) throws Exception
    {
        File imgFile = null;

        if (imgFileName == null)
            throw new NullPointerException("The source image file is null.");
        else if (!(imgFile = new File(imgFileName)).exists())
            throw new NullPointerException("The source image file does not exist.");
        else if (hFileName == null)
            throw new NullPointerException("The target HDF file is null.");

        if (!fromType.equals(FileFormat.FILE_TYPE_JPEG))
            throw new UnsupportedOperationException("Unsupported image type.");
        else if ( !(toType.equals(FileFormat.FILE_TYPE_HDF4)
             || toType.equals(FileFormat.FILE_TYPE_HDF5 )))
             throw new UnsupportedOperationException("Unsupported destination file type.");

        BufferedInputStream in = new BufferedInputStream(new FileInputStream(imgFileName));

        JPEGImageDecoder decoder = JPEGCodec.createJPEGDecoder(in);
        BufferedImage image = decoder.decodeAsBufferedImage();
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
        int rgb = 0;
        for (int i=0; i<h; i++)
        {
            for (int j=0; j<w; j++)
            {
                rgb = image.getRGB(j, i);
                data[idx++] = (byte) (rgb >> 16);
                data[idx++] = (byte) (rgb >> 8);
                data[idx++] = (byte) rgb;
            }
        }

        int fid = -1;
        long[] dims = null;
        Datatype type = null;
        Group pgroup = null;
        Dataset dataset = null;
        String imgName = imgFile.getName();
        FileFormat newfile=null, thefile=null;
        if (toType.equals(FileFormat.FILE_TYPE_HDF5))
        {
            thefile = FileFormat.getFileFormat(FileFormat.FILE_TYPE_HDF5);
            long[] h5dims = {h, w, 3}; // RGB pixel interlace
            dims = h5dims;
        }
        else if (toType.equals(FileFormat.FILE_TYPE_HDF4))
        {
            thefile = FileFormat.getFileFormat(FileFormat.FILE_TYPE_HDF4);
            long[] h4dims = {w, h, 3}; // RGB pixel interlace
            dims = h4dims;
        }
        else
            thefile = null;

        if (thefile != null)
        {
            newfile = thefile.create(hFileName);
            fid = newfile.open();
            pgroup = (Group)((DefaultMutableTreeNode) newfile.getRootNode()).getUserObject();
            type = newfile.createDatatype(Datatype.CLASS_CHAR, 1, Datatype.NATIVE, Datatype.SIGN_NONE);
            dataset = newfile.createImage(imgName, pgroup, type,
                dims, null, null, -1, 3, ScalarDS.INTERLACE_PIXEL, data);
            newfile.close();
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
    public static void saveImageAs(BufferedImage image, File file, String type)
    throws Exception
    {
        if (image == null)
            throw new NullPointerException("The source image is null.");

        if (!type.equals(FileFormat.FILE_TYPE_JPEG))
            throw new UnsupportedOperationException("Unsupported image type.");

        BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(file));
        JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
        encoder.encode(image);

        try { out.close(); } catch (Exception ex) {}
    }

}