/*****************************************************************************
 * Copyright by The HDF Group.                                               *
 * Copyright by the Board of Trustees of the University of Illinois.         *
 * All rights reserved.                                                      *
 *                                                                           *
 * This file is part of the HDF Java Products distribution.                  *
 * The full copyright notice, including terms governing use, modification,   *
 * and redistribution, is contained in the files COPYING and Copyright.html. *
 * COPYING can be found at the root of the source code distribution tree.    *
 * Or, see http://hdfgroup.org/products/hdf-java/doc/Copyright.html.         *
 * If you do not have access to either file, you may request a copy from     *
 * help@hdfgroup.org.                                                        *
 ****************************************************************************/

package ncsa.hdf.view;

import java.awt.image.*;
import java.io.*;
import ncsa.hdf.object.*;
import java.awt.Image;
import java.awt.Toolkit;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import javax.swing.tree.DefaultMutableTreeNode;
import com.sun.image.codec.jpeg.*;

/**
 * The "Tools" class contains various of tools for HDF files such as jpeg to
 * HDF converter.
 *    
 * @author Peter X. Cao
 * @version 2.4 9/6/2007
 */
public final class Tools
{
    public static final long MAX_INT8    = 127;
    public static final long MAX_UINT8   = 255;
    public static final long MAX_INT16   = 32767;
    public static final long MAX_UINT16  = 65535;
    public static final long MAX_INT32   = 2147483647;
    public static final long MAX_UINT32  = 4294967295L;
    public static final long MAX_INT64   = 9223372036854775807L;
     
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

        if (imgFileName == null) {
            throw new NullPointerException("The source image file is null.");
        } else if (!(imgFile = new File(imgFileName)).exists()) {
            throw new NullPointerException("The source image file does not exist.");
        } else if (hFileName == null) {
            throw new NullPointerException("The target HDF file is null.");
        }

        if (!fromType.equals(FileFormat.FILE_TYPE_JPEG)) {
            throw new UnsupportedOperationException("Unsupported image type.");
        } else if ( !(toType.equals(FileFormat.FILE_TYPE_HDF4)
             || toType.equals(FileFormat.FILE_TYPE_HDF5 ))) {
            throw new UnsupportedOperationException("Unsupported destination file type.");
        }

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
        } else {
            thefile = null;
        }

        if (thefile != null)
        {
            newfile = thefile.createInstance(hFileName, FileFormat.CREATE);
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
        if (image == null) {
            throw new NullPointerException("The source image is null.");
        }

        if (!type.equals(FileFormat.FILE_TYPE_JPEG)) {
            throw new UnsupportedOperationException("Unsupported image type.");
        }
        BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(file));

        JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
        encoder.encode(image);

        try { out.close(); } catch (Exception ex) {}
    }


    /**
     *  Creates the gray palette of the indexed 256-color table.
     *  <p>
     *  The palette values are stored in a two-dimensional byte array and arrange
     *  by color components of red, green and blue. palette[][] = byte[3][256],
     *  where, palette[0][], palette[1][] and palette[2][] are the red, green and
     *  blue components respectively.
     *  @return the gray palette in the form of byte[3][256]
     */
    public static final byte[][] createGrayPalette()
    {
        byte[][] p = new byte[3][256];

        for (int i=0; i<256; i++)
        {
            p[0][i] = p[1][i] = p[2][i] = (byte)i;
        }

        return p;
    }

    /**
     *  Creates the reverse gray palette of the indexed 256-color table.
     *  <p>
     *  The palette values are stored in a two-dimensional byte array and arrange
     *  by color components of red, green and blue. palette[][] = byte[3][256],
     *  where, palette[0][], palette[1][] and palette[2][] are the red, green and
     *  blue components respectively.
     *  @return the gray palette in the form of byte[3][256]
     */
    public static final byte[][] createReverseGrayPalette()
    {
        byte[][] p = new byte[3][256];

        for (int i=0; i<256; i++)
        {
            p[0][i] = p[1][i] = p[2][i] = (byte)(255-i);
        }

        return p;
    }


    /**
     *  Creates the gray wave palette of the indexed 256-color table.
     *  <p>
     *  The palette values are stored in a two-dimensional byte array and arrange
     *  by color components of red, green and blue. palette[][] = byte[3][256],
     *  where, palette[0][], palette[1][] and palette[2][] are the red, green and
     *  blue components respectively.
     *  @return the gray palette in the form of byte[3][256]
     */
    public static final byte[][] createGrayWavePalette()
    {
        byte[][] p = new byte[3][256];

        for (int i=0; i<256; i++)
        {
            p[0][i] = p[1][i] = p[2][i] = (byte) (255/2 + (255/2)*Math.sin((i-32)/20.3));
        }

        return p;
    }


    /**
     *  Creates the rainbow palette of the indexed 256-color table.
     *  <p>
     *  The palette values are stored in a two-dimensional byte array and arrange
     *  by color components of red, green and blue. palette[][] = byte[3][256],
     *  where, palette[0][], palette[1][] and palette[2][] are the red, green and
     *  blue components respectively.
     *  @return the rainbow palette in the form of byte[3][256]
     */
    public static final byte[][] createRainbowPalette()
    {
        byte r, g, b;
        byte[][] p = new byte[3][256];

        for (int i=1; i<255; i++)
        {
            if (i<=29)
            {
                r = (byte)(129.36-i*4.36);
                g = 0;
                b = (byte)255;
            }
            else if (i<=86)
            {
                r = 0;
                g = (byte)(-133.54+i*4.52);
                b = (byte)255;
            }
            else if (i<=141)
            {
                r = 0;
                g = (byte)255;
                b = (byte)(665.83-i*4.72);
            }
            else if (i<=199)
            {
                r = (byte)(-635.26+i*4.47);
                g = (byte)255;
                b = 0;
            }
            else
            {
                r = (byte)255;
                g = (byte)(1166.81-i*4.57);
                b = 0;
            }

            p[0][i] = r;
            p[1][i] = g;
            p[2][i] = b;
        }

        p[0][0] = p[1][0] = p[2][0] = 0;
        p[0][255] = p[1][255] = p[2][255] = (byte)255;

        return p;
    }

    /**
     *  Creates the nature palette of the indexed 256-color table.
     *  <p>
     *  The palette values are stored in a two-dimensional byte array and arrange
     *  by color components of red, green and blue. palette[][] = byte[3][256],
     *  where, palette[0][], palette[1][] and palette[2][] are the red, green and
     *  blue components respectively.
     *  @return the nature palette in the form of byte[3][256]
     */
    public static final byte[][] createNaturePalette()
    {
        byte[][] p = new byte[3][256];

        for (int i=1; i<210; i++)
        {
            p[0][i] = (byte) ((Math.sin((double)(i-5)/16)+1)*90);
            p[1][i] = (byte) ((1-Math.sin((double)(i-30)/12))*64*(1-(double)i/255)+128-i/2);
            p[2][i] = (byte) ((1-Math.sin((double)(i-8)/9))*110+30);
        }

        for (int i=210; i<255; i++)
        {
            p[0][i] = (byte)80;
            p[1][i] = (byte)0;
            p[2][i] = (byte)200;
        }

        p[0][0] = p[1][0] = p[2][0] = 0;
        p[0][255] = p[1][255] = p[2][255] = (byte)255;

        return p;
    }

    /**
     *  Creates the wave palette of the indexed 256-color table.
     *  <p>
     *  The palette values are stored in a two-dimensional byte array and arrange
     *  by color components of red, green and blue. palette[][] = byte[3][256],
     *  where, palette[0][], palette[1][] and palette[2][] are the red, green and
     *  blue components respectively.
     *  @return the wave palette in the form of byte[3][256]
     */
    public static final byte[][] createWavePalette()
    {
        byte[][] p = new byte[3][256];

        for (int i=1; i<255; i++)
        {
            p[0][i] = (byte) ((Math.sin(((double)i/40-3.2))+1)*128);
            p[1][i] = (byte) ((1-Math.sin((i/2.55-3.1)))*70+30);
            p[2][i] = (byte) ((1-Math.sin(((double)i/40-3.1)))*128);
        }

        p[0][0] = p[1][0] = p[2][0] = 0;
        p[0][255] = p[1][255] = p[2][255] = (byte)255;

        return p;
    }

    /**
     * This method returns true if the specified image has transparent pixels.
     * @param image the image to be check if has alpha.
     * @return true if the image has alpha setting.
     */
    public static boolean hasAlpha(Image image)
    {
        if (image == null) {
            return false;
        }

        // If buffered image, the color model is readily available
        if (image instanceof BufferedImage)
        {
            BufferedImage bimage = (BufferedImage)image;
            return bimage.getColorModel().hasAlpha();
        }

        // Use a pixel grabber to retrieve the image's color model;
        // grabbing a single pixel is usually sufficient
        PixelGrabber pg = new PixelGrabber(image, 0, 0, 1, 1, false);
        try { pg.grabPixels(); } catch (InterruptedException e) {}
        ColorModel cm = pg.getColorModel();

        return cm.hasAlpha();
    }

    /** Creates a RGB indexed image of 256 colors.
     *  @param imageData the byte array of the image data.
     *  @param palette the color lookup table.
     *  @param w the width of the image.
     *  @param h the height of the image.
     *  @return the image.
     */
    public static Image createIndexedImage(byte[] imageData, byte[][] palette, int w, int h)
    {
        Image theImage = null;

        IndexColorModel colorModel = new IndexColorModel (
            8,           // bits - the number of bits each pixel occupies
            256,         // size - the size of the color component arrays
            palette[0],  // r - the array of red color components
            palette[1],  // g - the array of green color components
            palette[2]); // b - the array of blue color components

        theImage = Toolkit.getDefaultToolkit().createImage (
            new MemoryImageSource(w, h, colorModel, imageData, 0, w));

        return theImage;
    }

    /**
     *  Creates a true color image.
     *  <p>
     *  DirectColorModel is used to construct the image from raw data. The
     *  DirectColorModel model is similar to an X11 TrueColor visual, which has
     *  the following parameters: <br>
        <pre>
            Number of bits:        32
            Red mask:              0x00ff0000
            Green mask:            0x0000ff00
            Blue mask:             0x000000ff
            Alpha mask:            0xff000000
            Color space:           sRGB
            isAlphaPremultiplied:  False
            Transparency:          Transparency.TRANSLUCENT
            transferType:          DataBuffer.TYPE_INT
        </pre>
     * <p>
     * The data may be arranged in one of two ways: by pixel or by plane. In both
     * cases, the dataset will have a dataspace with three dimensions, height,
     * width, and components.
     * <p>
     * For HDF4, the interlace modes specify orders for the dimensions as:
       <pre>
           INTERLACE_PIXEL = [width][height][pixel components]
           INTERLACE_PLANE = [pixel components][width][height]
       </pre>
     * <p>
     * For HDF5, the interlace modes specify orders for the dimensions as:
       <pre>
           INTERLACE_PIXEL = [height][width][pixel components]
           INTERLACE_PLANE = [pixel components][height][width]
       </pre>
     * <p>
     *  @param imageData the byte array of the image data.
     *  @param planeInterlace flag if the image is plane intelace.
     *  @param w the width of the image.
     *  @param h the height of the image.
     *  @return the image.
     */
    public static Image createTrueColorImage(byte[] imageData, boolean planeInterlace, int w, int h)
    {
        Image theImage = null;
        int imgSize = w*h;
        int packedImageData[] = new int[imgSize];
        int pixel=0, idx=0, r=0, g=0, b=0;
        for (int i=0; i<h; i++)
        {
            for (int j=0; j<w; j++)
            {
                pixel = r = g = b = 0;
                if (planeInterlace)
                {
                    r = imageData[idx];
                    g = imageData[imgSize+idx];
                    b = imageData[imgSize*2+idx];
                }
                else
                {
                    r = imageData[idx*3];
                    g = imageData[idx*3+1];
                    b = imageData[idx*3+2];
                }

                r = (r << 16) & 0x00ff0000;
                g = (g <<  8) & 0x0000ff00;
                b =  b        & 0x000000ff;

                // bits packed into alpha (1), red (r), green (g) and blue (b)
                // as 11111111rrrrrrrrggggggggbbbbbbbb
                pixel = 0xff000000 | r | g | b;
                packedImageData[idx++] = pixel;
            } //for (int j=0; j<w; j++)
        } // for (int i=0; i<h; i++)

        DirectColorModel dcm = (DirectColorModel)ColorModel.getRGBdefault();
        theImage = Toolkit.getDefaultToolkit().createImage (
            new MemoryImageSource(w, h, dcm, packedImageData, 0, w));

        packedImageData = null;

        return theImage;
    }

    /**
     *  Convert an array of raw data into array of a byte data.
     *  Byte data ranged from -128 to 127.
     *  <p>
     *  @param rawData The input raw data.
     *  @param minmax the range of the raw data.
     *  @return the byte array of pixel data.
     */
    public static byte[] getBytes(Object rawData, double[] minmax, byte[] byteData)
    {
        return Tools.getBytes(rawData, minmax, -1, -1, false, null, byteData);
    }
    public static byte[] getBytes(Object rawData, double[] minmax, int w, int h, boolean isTransposed, byte[] byteData)
    {
        return Tools.getBytes(rawData, minmax, w, h, isTransposed, null, byteData);
    }
    public static byte[] getBytes(Object rawData, double[] minmax, Object fillValue, byte[] byteData)
    {
        return Tools.getBytes(rawData, minmax, -1, -1, false, fillValue, byteData);
    }

    /**
     *  Convert an array of raw data into array of a byte data.
     *  Byte data ranged from -128 to 127.
     *  <p>
     *  @param rawData The input raw data.
     *  @param minmax the range of the raw data.
     *  @param isTransposed if the data is transposeed
     *  @return the byte array of pixel data.
     */
    public static byte[] getBytes(Object rawData, double[] minmax, int w, int h,
           boolean isTransposed, Object fillValue, byte[] byteData)
    {
        // no pnput data
        if (rawData == null) {
            return null;
        }

        // input data is not an array
        if (!rawData.getClass().isArray()) {
            return null;
        }

        double min=Double.MAX_VALUE, max=-Double.MAX_VALUE, ratio=1.0d;
        String cname = rawData.getClass().getName();
        char dname = cname.charAt(cname.lastIndexOf("[")+1);

        if (minmax == null)
        {
            minmax = new double[2];
            minmax[0] = minmax[1] = 0;
        }

        // no need for conversion
        if ((dname == 'B') && !isTransposed)
        {
            byteData = (byte[]) rawData;
            minmax[0] = 0;
            minmax[1] = 255;
            return byteData;
        }

        int size = Array.getLength(rawData);
        if ((byteData == null) || (size != byteData.length)) {
            byteData = new byte[size]; // reuse the old buffer
        }
        
        if (minmax[0] == minmax[1]) {
            Tools.findMinMax(rawData,  minmax);
        }
        min = minmax[0]; 
        max = minmax[1];

        switch (dname)
        {
            case 'B':
                byte[] b = (byte[])rawData;

                if (isTransposed)
                {
                    for (int i=0; i<h; i++)
                    {
                        for (int j=0; j<w; j++) {
                            byteData[i*w+j] = b[j*h+i];
                        }
                    }
                }
                break;

            case 'S':
                short[] s = (short[])rawData;

                // set fill value to zero
                if (fillValue != null)
                {
                    short fvalue = ((short[])fillValue)[0];
                    if (fvalue != 0) {
                        for (int i=0; i<size; i++) {
                            if (fvalue == s[i]) {
                                s[i] = 0;
                            }
                        }
                    }
                }

                // converts the data based on the ratio to support only 256 colors
                ratio = (min == max) ? 1.00d : (double)(255.00/(max-min));
                if (isTransposed)
                {
                    for (int i=0; i<h; i++)
                    {
                        for (int j=0; j<w; j++) {
                            byteData[i*w+j] = (byte)((s[j*h+i]-min)*ratio);
                        }
                    }
                }
                else {
                    for (int i=0; i<size; i++)
                    {
                        byteData[i] = (byte)((s[i]-min)*ratio);
                    }
                }

                break;

            case 'I':
                int[] ia = (int[])rawData;

                // set fill value to zero
                if (fillValue != null)
                {
                    int fvalue = ((int[])fillValue)[0];
                    if (fvalue != 0) {
                        for (int i=0; i<size; i++) {
                            if (fvalue == ia[i]) {
                                ia[i] = 0;
                            }
                        }
                    }
                }

                // converts the data based on the ratio to support only 256 colors
                ratio = (min == max) ? 1.00d : (double)(255.00/(max-min));
                if (isTransposed)
                {
                    for (int i=0; i<h; i++)
                    {
                        for (int j=0; j<w; j++) {
                            byteData[i*w+j] = (byte)((ia[j*h+i]-min)*ratio);
                        }
                    }
                }
                else {
                    for (int i=0; i<size; i++) {
                        byteData[i] = (byte)((ia[i] - min)*ratio);
                    }
                }

                break;

            case 'J':
                long[] l = (long[])rawData;

                // set fill value to zero
                if (fillValue != null)
                {
                    long fvalue = ((long[])fillValue)[0];
                    if (fvalue != 0) {
                        for (int i=0; i<size; i++) {
                            if (fvalue == l[i]) {
                                l[i] = 0;
                            }
                        }
                    }
                }

                // converts the data based on the ratio to support only 256 colors
                ratio = (min == max) ? 1.00d : (double)(255.00/(max-min));
                if (isTransposed)
                {
                    for (int i=0; i<h; i++)
                    {
                        for (int j=0; j<w; j++) {
                            byteData[i*w+j] = (byte)((l[j*h+i]-min)*ratio);
                        }
                    }
                } else {
                    for (int i=0; i<size; i++)
                    {
                        byteData[i] = (byte)((l[i]-min)*ratio);
                    }
                }

                break;

            case 'F':
                float[] f = (float[])rawData;

                // set fill value to zero
                if (fillValue != null)
                {
                    float fvalue = ((float[])fillValue)[0];
                    if (fvalue != 0) {
                        for (int i=0; i<size; i++) {
                            if (fvalue == f[i]) {
                                f[i] = 0;
                            }
                        }
                    }
                }

                // converts the data based on the ratio to support only 256 colors
                ratio = (min == max) ? 1.00d : (double)(255.00/(max-min));
                if (isTransposed)
                {
                    for (int i=0; i<h; i++)
                    {
                        for (int j=0; j<w; j++) {
                            byteData[i*w+j] = (byte)((f[j*h+i]-min)*ratio);
                        }
                    }
                } else {
                    for (int i=0; i<size; i++)
                    {
                        byteData[i] = (byte)((f[i]-min)*ratio);
                    }
                }

                break;

            case 'D':
                double[] d = (double[])rawData;

                // set fill value to zero
                if (fillValue != null)
                {
                    double fvalue = ((double[])fillValue)[0];
                    if (fvalue != 0) {
                        for (int i=0; i<size; i++) {
                            if (fvalue == d[i]) {
                                d[i] = 0;
                            }
                        }
                    }
                }

                // converts the data based on the ratio to support only 256 colors
                ratio = (min == max) ? 1.00d : (double)(255.00/(max-min));
                if (isTransposed)
                {
                    for (int i=0; i<h; i++)
                    {
                        for (int j=0; j<w; j++) {
                            byteData[i*w+j] = (byte)((d[j*h+i]-min)*ratio);
                        }
                    }
                } else {
                    for (int i=0; i<size; i++)
                    {
                        byteData[i] = (byte)((d[i]-min)*ratio);
                    }
                }

                break;

            default:
                byteData = null;
                break;
        } // switch (dname)

        return byteData;
    }

    /** Create and initialize a new instance of the given class.
     * @param initargs - array of objects to be passed as arguments
     * @return a new instance of the given class.
     */
    public static Object newInstance(Class cls, Object[] initargs)
       throws Exception {
        Object instance = null;

        if (cls == null) {
            return null;
        }

        if ((initargs == null) || (initargs.length == 0)) {
            instance = cls.newInstance();
        } else {
            Constructor[] constructors = cls.getConstructors();
            if ((constructors == null) || (constructors.length == 0)) {
                return null;
            }

            boolean isConstructorMatched = false;
            Constructor constructor = null;
            Class[] params = null;
            int m = constructors.length;
            int n = initargs.length;
            for (int i=0; i<m; i++) {
                constructor = constructors[i];
                params = constructor.getParameterTypes();
                if (params.length == n) {
                    // check if all the parameters are matched
                    isConstructorMatched = params[0].isInstance(initargs[0]);
                    for (int j=1; j<n; j++) {
                        isConstructorMatched = isConstructorMatched && params[j].isInstance(initargs[j]);
                    }

                    if (isConstructorMatched) {
                        instance = constructor.newInstance(initargs);
                        break;
                    }
                }
            } // for (int i=0; i<m; i++) {
        }

        return instance;
    }
    
    /**
     * Computes autocontrast parameters (gain equates to contrast and bias equates to brightness) for integers.
     * <p>
     * The computation is based on the following scaling
     * <pre>
     *      int_8       [0, 127]
     *      uint_8      [0, 255]
     *      int_16      [0, 32767]
     *      uint_16     [0, 65535]
     *      int_32      [0, 2147483647]
     *      uint_32     [0, 4294967295]
     *      int_64      [0, 9223372036854775807]
     *      uint_64     [0, 18446744073709551615] // Not supported.
     * </pre>
     *
     * @param data the raw data array of signed/unsigned integers
     * @param params the auto gain parameter. params[0]=gain, params[1]=bias
     * @param isUnsigned the flag to indicate if the data array is unsiged integer
     * @return non-negative if successful; otherwise, returns negative
     */
    public static int autoContrastCompute(Object data,  double[] params, boolean isUnsigned)
    {
    	int retval = 1;
        long maxDataValue = 255;
    	double[] minmax = new double[2];
    	
        // check parameters
    	if ((data == null) || 
            (params == null) || 
            (Array.getLength(data)<=0) || 
            (params.length<2)) {
            return -1;
        }
    	
    	retval = autoContrastComputeMinMax(data, minmax);
    	
    	// force the min_max method so we can look at the target grids data sets
    	if ( (retval < 0) || (minmax[1] - minmax[0] < 10) ) {
            retval = findMinMax(data, minmax);
        }
    	
        if (retval < 0) {
            return -1;
        }
        
        String cname = data.getClass().getName();
        char dname = cname.charAt(cname.lastIndexOf("[")+1);
        switch (dname)
        {
            case 'B': 
                maxDataValue = MAX_INT8;
                break;
            case 'S':
                maxDataValue = MAX_INT16;
                if (isUnsigned) {
                    maxDataValue = MAX_UINT8; // upgaded from unsigned byte
                }
                 break;
            case 'I':
                maxDataValue = MAX_INT32;
                if (isUnsigned) {
                    maxDataValue = MAX_UINT16; // upgaded from unsigned short
                }
                break;
            case 'J':
                maxDataValue = MAX_INT64;
                if (isUnsigned) {
                    maxDataValue = MAX_UINT32; // upgaded from unsigned int
                }
                 break;
            default:
                retval = -1;
                break;
        } // switch (dname)
        
        if (minmax[0]==minmax[1]) {
            params[0] = 1.0;
            params[1] = 0.0;
        } else {
            // This histogram method has a tendency to stretch the 
            // range of values to be a bit too big, so we can 
            // account for this by adding and subtracting some percent 
            // of the difference to the max/min values 
            // to prevent the gain from going too high. 
            double diff = minmax[1] - minmax[0]; 
            double newmax = (minmax[1] + (diff * 0.1)); 
            double newmin = (minmax[0] - (diff * 0.1)); 
            
            if (newmax <= maxDataValue) {
                minmax[1] = newmax;
            }
            
            if (newmin >=0) {
                minmax[0] = newmin;
            }

            params[0] = maxDataValue / (minmax[1]-minmax[0]);
            params[1] = -minmax[0];
         }

    	return retval;
    }

    /**
     * Apply autocontrast parameters in place (destructive)
     *
     * @param data_in the original data array of signed/unsigned integers
     * @param data_out the converted data array of signed/unsigned integers
     * @param params the auto gain parameter. params[0]=gain, params[1]=bias
     * @param isUnsigned the flag to indicate if the data array is unsiged integer
     * @return the data array with the auto contrast conversion; otherwise, returns null
     */
    public static Object autoContrastApply(Object data_in, Object data_out,  double[] params, boolean isUnsigned)
    {
        int retval=1, size=0;
      
        if ((data_in==null) || (params==null) || (params.length<2)) {
            return null;
        }
        
        // input and output array must be the same size
        size = Array.getLength(data_in);
        if ( (data_out != null) && (size != Array.getLength(data_out))) {
            return null;
        }
        
        double gain = params[0]; 
        double bias = params[1]; 
        double value; 
        
        String cname = data_in.getClass().getName();
        char dname = cname.charAt(cname.lastIndexOf("[")+1);
        switch (dname)
        {
            case 'B': 
                byte[] b_in = (byte[])data_in;
                if (data_out == null) {
                    data_out = new byte[size];
                }
                byte[] b_out = (byte[])data_out;
                byte b_max = (byte)MAX_INT8;
                
                for( int i = 0; i<size; i++ ) { 
                    value = (b_in[i] + bias) * gain; 
                    if( value < 0.0 ) {
                        b_out[i] = 0;
                    } else if( value > b_max ) {
                        b_out[i] = b_max;
                    } else {
                        b_out[i] = (byte)value;
                    } 
                }
                break;
            case 'S':
                short[] s_in = (short[])data_in;
                if (data_out == null) {
                    data_out = new short[size];
                }
                short[] s_out = (short[])data_out;
                short s_max = (short)MAX_INT16;
                if (isUnsigned) {
                    s_max = (short)MAX_UINT8; // upgaded from unsigned byte
                }
                
                for( int i = 0; i<size; i++ ) { 
                    value = (s_in[i] + bias) * gain; 
                    if( value < 0.0 ) {
                        s_out[i] = 0;
                    } else if( value > s_max ) {
                        s_out[i] = s_max;
                    } else {
                        s_out[i] = (short)value;
                    } 
                }
                 break;
            case 'I':
                int[] i_in = (int[])data_in;
                if (data_out == null) {
                    data_out = new int[size];
                }
                int[] i_out = (int[])data_out;
                int i_max = (int)MAX_INT32;
                if (isUnsigned) {
                    i_max = (int)MAX_UINT16; // upgaded from unsigned short
                }
                
                for( int i = 0; i<size; i++ ) { 
                    value = (i_in[i] + bias) * gain; 
                    if( value < 0.0 ) {
                        i_out[i] = 0;
                    } else if( value > i_max ) {
                        i_out[i] = i_max;
                    } else {
                        i_out[i] = (int)value;
                    } 
                }
                break;
            case 'J':
                long[] l_in = (long[])data_in;
                if (data_out == null) {
                    data_out = new long[size];
                }
                long[] l_out = (long[])data_out;
                long l_max = MAX_INT64;
                if (isUnsigned) {
                    l_max = MAX_UINT32; // upgaded from unsigned int
                }
                
                for( int i = 0; i<size; i++ ) { 
                    value = (l_in[i] + bias) * gain; 
                    if( value < 0.0 ) {
                        l_out[i] = 0;
                    } else if( value > l_max ) {
                        l_out[i] = l_max;
                    } else {
                        l_out[i] = (long)value;
                    } 
                }
                 break;
            default:
                retval = -1;
                break;
        } // switch (dname)
        
        return data_out; 
    } 

    /** 
     * Auto-ranging of gain/bias sliders 
     * 
     * Given the results of autogaining an image, compute reasonable 
     * min and max values for gain/bias sliders. 
     * 
     * @param params the auto gain parameter: params[0]=gain, params[1]=bias
     * @param gain the range of the gain: gain[0]=min, gain[1]=mas
     * @param bias the range of the bias: bias[0]=min, bias[1]=max
     * @return non-negative if successful; otherwise, returns negative
    */ 
    public static int autoContrastComputeSliderRange( double[] params, double[] gain, double[] bias) 
    { 
        if ((params == null) || (gain == null) || (bias == null) ||
            (params.length<2) || (gain.length<2) || (bias.length<2)) {
            return -1;
        }
        
        gain[0] = 0; 
        gain[1] = params[0] * 3.0; 
      
        bias[1] = 256.0; 
        if ((params[1] >= 0.001) || (params[1] <= -0.001) ) {
            bias[1] = Math.abs( params[1] ) * 3.0;
        } 
        bias[0] = -bias[1]; 
       
        return 1; 
    } 

    /**
     * Converts image raw data to bytes.
     * <p>
     * The integer data is converted to byte data based on the following rule
     * <pre>       
        uint_8       x
        int_8       (x & 0x7F) << 1
        uint_16     (x >> 8) & 0xFF
        int_16      (x >> 7) & 0xFF
        uint_32     (x >> 24) & 0xFF
        int_32      (x >> 23) & 0xFF
        uint_64     (x >> 56) & 0xFF
        int_64      (x >> 55) & 0xFF
     * </pre>
     *
     * @param src the source data array of signed integers or unsigned shorts
     * @param dst the destination data array of bytes
     * @param isUnsigned the flag to indicate if the data array is unsiged integer
     * @return non-negative if successful; otherwise, returns negative
     */
    public static int autoContrastConvertImageBuffer(Object src,  byte[] dst, boolean isUnsigned)
    {
        int retval=0;
        
        if ((src==null) || (dst==null) || (dst.length != Array.getLength(src))) {
            return -1;
        }
        
        int size = dst.length;
        String cname = src.getClass().getName();
        char dname = cname.charAt(cname.lastIndexOf("[")+1);
        switch (dname)
        {
            case 'B': 
                byte[] b_src = (byte[])src;
                if (isUnsigned) {
                    for( int i = 0; i<size; i++ ) {
                        dst[i] = b_src[i];
                    }
                }
                else {
                    for( int i = 0; i<size; i++ ) {
                        dst[i] = (byte) ((b_src[i] & 0x7F) << 1);
                    }
                 }
                break;
            case 'S':
                short[] s_src = (short[])src;
                if (isUnsigned) { // upgaded from unsigned byte
                    for( int i = 0; i<size; i++ ) {
                        dst[i] = (byte) s_src[i];
                    } 
                } else {
                    for( int i = 0; i<size; i++ ) {
                        dst[i] = (byte) ((s_src[i] >> 7) & 0xFF);
                    } 
                }
                 break;
            case 'I':
                int[] i_src = (int[])src;
                if (isUnsigned) { // upgaded from unsigned short
                    for( int i = 0; i<size; i++ ) {
                        dst[i] = (byte) ((i_src[i] >> 8) & 0xFF);
                    } 
                } else {
                    for( int i = 0; i<size; i++ ) {
                        dst[i] = (byte) ((i_src[i] >> 23) & 0xFF);
                    } 
                }
                break;
            case 'J':
                long[] l_src = (long[])src;
                if (isUnsigned) { // upgaded from unsigned int
                    for( int i = 0; i<size; i++ ) {
                        dst[i] = (byte) ((l_src[i] >> 24) & 0xFF);
                    } 
                } else {
                    for( int i = 0; i<size; i++ ) {
                        dst[i] = (byte) ((l_src[i] >> 55) & 0xFF);
                    } 
                }
                break;
            default:
                retval = -1;
                break;
        } // switch (dname)
        
        return retval; 
    } 


    /**
     * Computes autocontrast parameters by
     * <pre>
     *    min = mean - 3 * std.dev 
	 *    max = mean + 3 * std.dev 
     * </pre>
     *
     * @param data the raw data array
     * @param minmax the min and max values.
     * @return non-negative if successful; otherwise, returns negative
     */
    public static int autoContrastComputeMinMax(Object data, Object minmax)
    {
    	int retval = 1;
    	double[] avgstd = new double[2];

    	if ((data == null) || (minmax == null) || (Array.getLength(data)<=0) || (Array.getLength(minmax)<2)) {
            return -1;
        }
    	
    	retval = computeStatistics(data, avgstd);
    	if (retval < 0) {
            return retval;
        }
    	
    	double min = avgstd[0] - 3.0*avgstd[1];
    	double max = avgstd[0] + 3.0*avgstd[1];
    	
        String cname = minmax.getClass().getName();
        char dname = cname.charAt(cname.lastIndexOf("[")+1);
        switch (dname)
        {
            case 'B': 
                byte[] b = (byte[])minmax;
                b[0] = (byte)min; b[1] = (byte)max;
                break;
            case 'S':
                short[] s = (short[])minmax;
                s[0] = (short)min; s[1] = (short)max;
                break;
            case 'I':
                int[] ia = (int[])minmax;
                ia[0] = (int)min; ia[1] = (int)max;
                break;
            case 'J':
                long[] l = (long[])minmax;
                l[0] = (long)min; l[1] = (long)max;
                break;
            case 'F':
                float[] f = (float[])minmax;
                f[0] = (float)min; f[1] = (float)max;
                break;
            case 'D':
                double[] d = (double[])minmax;
                d[0] = min; d[1] = max;
                break;
            default:
                    retval = -1;
                    break;
        } // switch (dname)
    	
    	return retval;
    }

    /**
     * Finds the min and max values of the data array
     *
     * @param data the raw data array
     * @param minmax the mmin and max values of the array.
     * @return non-negative if successful; otherwise, returns negative
     */
    public static int findMinMax(Object data, double[] minmax)
    {
    	int retval = 1;
    	
    	if ((data == null) || (minmax == null) || (Array.getLength(data)<=0) || (Array.getLength(minmax)<2)) {
            return -1;
        }
    	
        int n = Array.getLength(data);

        String cname = data.getClass().getName();
        char dname = cname.charAt(cname.lastIndexOf("[")+1);
         switch (dname)
        {
            case 'B': 
                byte[] b = (byte[])data;
                minmax[0] = minmax[1] = b[0];
                for (int i=1; i<n; i++) {
                	if (minmax[0]>b[i]) {
                        minmax[0] = b[i];
                    }
                	if (minmax[1]<b[i]) {
                        minmax[1] = b[i];
                    }
                }
                break;
            case 'S':
                short[] s = (short[])data;
                minmax[0] = minmax[1] = s[0];
                for (int i=1; i<n; i++) {
                    if (minmax[0]>s[i]) {
                        minmax[0] = s[i];
                    }
                    if (minmax[1]<s[i]) {
                        minmax[1] = s[i];
                    }
                }
                break;
            case 'I':
                int[] ia = (int[])data;
                minmax[0] = minmax[1] = ia[0];
                for (int i=1; i<n; i++) {
                    if (minmax[0]>ia[i]) {
                        minmax[0] = ia[i];
                    }
                    if (minmax[1]<ia[i]) {
                        minmax[1] = ia[i];
                    }
                }
                break;
            case 'J':
            	long[] l = (long[])data;
                minmax[0] = minmax[1] = l[0];
                for (int i=1; i<n; i++) {
                    if (minmax[0]>l[i]) {
                        minmax[0] = l[i];
                    }
                    if (minmax[1]<l[i]) {
                        minmax[1] = l[i];
                    }
                }
                break;
            case 'F':
                float[] f = (float[])data;
                minmax[0] = minmax[1] = f[0];
                for (int i=1; i<n; i++) {
                    if (minmax[0]>f[i]) {
                        minmax[0] = f[i];
                    }
                    if (minmax[1]<f[i]) {
                        minmax[1] = f[i];
                    }
                }
                break;
            case 'D':
                double[] d = (double[])data;
                minmax[0] = minmax[1] = d[0];
                for (int i=1; i<n; i++) {
                    if (minmax[0]>d[i]) {
                        minmax[0] = d[i];
                    }
                    if (minmax[1]<d[i]) {
                        minmax[1] = d[i];
                    }
                }
                break;
            default:
            	retval = -1;
                break;
        } // switch (dname)
        
    	return retval;
    }
    
    /**
     * Computes mean and standard deviation of a data array
     *
     * @param data the raw data array
     * @param avgstd the statistics: avgstd[0]=mean and avgstd[1]=stdev.
     * @return non-negative if successful; otherwise, returns negative
     */
    public static int computeStatistics(Object data, double[] avgstd)
    {
    	int retval = 1;
    	
    	double sum=0, avg=0.0, var=0.0, diff=0.0;

    	if ((data == null) || (avgstd == null) || (Array.getLength(data)<=0) || (Array.getLength(avgstd)<2)) {
            return -1;
        }
    	
        int n = Array.getLength(data);

        String cname = data.getClass().getName();
        char dname = cname.charAt(cname.lastIndexOf("[")+1);
    	
        switch (dname)
        {
            case 'B': 
                byte[] b = (byte[])data;
                for (int i=0; i<n; i++) {
                	sum += b[i];
                }
                
                avg = sum / n;
                for (int i=0; i<n; i++) {
                	diff = b[i] - avg;
                	var += diff * diff;
                }
                break;
            case 'S':
                short[] s = (short[])data;
                for (int i=0; i<n; i++) {
                    sum += s[i];
                }
                
                avg = sum / n;
                for (int i=0; i<n; i++) {
                	diff = s[i] - avg;
                	var += diff * diff;
                }
                break;
            case 'I':
                int[] ia = (int[])data;
                for (int i=0; i<n; i++) {
                    sum += ia[i];
                }
                
                avg =  sum / n;
                for (int i=0; i<n; i++) {
                	diff = ia[i] - avg;
                	var += diff * diff;
                }
                break;
            case 'J':
            	long[] l = (long[])data;
                for (int i=0; i<n; i++) {
                    sum += l[i];
                }
                
                avg = sum / n;
                for (int i=0; i<n; i++) {
                	diff = l[i] - avg;
                	var += diff * diff;
                }
                break;
            case 'F':
                float[] f = (float[])data;
                for (int i=0; i<n; i++) {
                    sum += f[i];
                }
                
                avg = sum / n;
                for (int i=0; i<n; i++) {
                	diff = f[i] - avg;
                	var += diff * diff;
                }
                break;
            case 'D':
                double[] d = (double[])data;
                for (int i=0; i<n; i++) {
                    sum += d[i];
                }
                
                avg = sum / n;
                for (int i=0; i<n; i++) {
                	diff = d[i] - avg;
                	var += diff * diff;
                }
                break;
            default:
            	retval = -1;
                break;
        } // switch (dname)
        
        avgstd[0] = avg;
        avgstd[1] = Math.sqrt(var/(n-1));
    	
    	return retval;
    }


}
