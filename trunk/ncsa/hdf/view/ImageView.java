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

import javax.swing.*;
import ncsa.hdf.object.*;
import java.awt.image.*;
import java.awt.event.*;
import java.lang.reflect.Array;
import java.util.List;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.Rectangle;
import java.awt.Point;
import java.awt.Color;
import java.awt.Frame;

/**
 * ImageView displays an HDF dataset as an image.
 * <p>
 * A scalar dataset in HDF can be displayed in image or table. By default, an
 * HDF4 GR image and HDF5 image is displayed as an image. Other scalar datasets
 * are display in a two-dimensional table.
 * <p>
 * Users can also choose to display a scalar dataset as image. Currently verion
 * of the ImageView only supports 8-bit raster image with indexed RGB color
 * model of 256 colors or 24-bit true color raster image. Data of other type
 * will be converted to 8-bit integer. The simple linear conversion is used for
 * this purpose:
  <pre>
      y = f * (x - min),
      where y   = the value of 8-bit integer,
            x   = the value of original data,
            f   = 255/(max-min), conversion factor,
            max = the maximum of the original data,
            min = the minimum of the original data.
  </pre>
 * <p>
 * A default color table is provided for images without palette attached to it.
 * Current choice of default palettes include Gray, Rainbow, Nature and Wave.
 * For more infomation on palette, read
 * <a href="http://hdf.ncsa.uiuc.edu/HDF5/doc/ADGuide/ImageSpec.html">
 * HDF5 Image and Palette Specification </a>
 * <p>
 * @version 1.3.0 01/28/2002
 * @author Peter X. Cao
 */
public class ImageView extends JInternalFrame
implements ImageObserver
{
    /**
     * The main HDFView.
     */
    private final ViewManager viewer;

    /**
     * The Scalar Dataset.
     */
    private ScalarDS dataset;

    /**
     * The JComponent containing the image.
     */
    private ImageComponent imageComponent;

    /**
     * The image contained in the ImageView.
     */
    private Image image;

    /**
     * The zooming factor of this image.
     */
    private float zoomFactor;

    /**
     * The byte data array of an indexed image.
     */
    private byte[] indexedImageData;

    /**
     * The color table of the image.
     */
    byte[][] imagePalette;

    /**
     * Constructs an ImageView.
     * <p>
     * @param theView the main HDFView.
     */
    public ImageView(ViewManager theView)
    {
        super();

        viewer = theView;
        zoomFactor = 1.0f;
        indexedImageData = null;
        imagePalette = null;

        HObject hobject = (HObject)viewer.getSelectedObject();
        if (hobject == null || !(hobject instanceof ScalarDS))
        {
            viewer.showStatus("Display data in image failed for - "+hobject);
            return;
        }

        dataset = (ScalarDS)hobject;

        this.setDefaultCloseOperation(JInternalFrame.DISPOSE_ON_CLOSE);
        this.setTitle("ImageView - "+hobject.getPath()+hobject.getName());
        this.setName(hobject.getPath()+hobject.getName());
        this.setFrameIcon(ViewProperties.getImageIcon());

        if (image == null)
            getImage();

        if (image == null)
        {
            viewer.showStatus("Loading image failed - "+dataset.getName());
            return;
        }

        imageComponent = new ImageComponent(image);
        JScrollPane scroller = new JScrollPane(imageComponent);
        scroller.getVerticalScrollBar().setUnitIncrement(50);
        scroller.getHorizontalScrollBar().setUnitIncrement(50);

        JPanel contentPane = (JPanel)getContentPane();
        contentPane.setLayout(new BorderLayout());
        contentPane.add (scroller, BorderLayout.CENTER);
    }

    public void dispose()
    {
        super.dispose();
        viewer.contentFrameWasRemoved(getName());
    }

    // Implementing DataObserver.
    public Object getDataObject()
    {
        return dataset;
    }

    // Implementing DataObserver.
    public void previousPage()
    {
        int rank = dataset.getRank();

        if (rank < 3)
            return;

        int[] selectedIndex = dataset.getSelectedIndex();
        long[] selectedDims = dataset.getSelectedDims();

        if (selectedDims[selectedIndex[2]] >1 )
            return; // it is a true color image with three color components

        long[] start = dataset.getStartDims();
        long[] dims = dataset.getDims();
        long idx = start[selectedIndex[2]];
        if (idx == 0)
            return; // current page is the first page

        start[selectedIndex[2]] -= 1;
        dataset.clearData();
        this.image = null;

        imageComponent.setImage(getImage());

        this.setTitle(
            "ImageView - "+
            dataset.getPath()+dataset.getName()+
            " - Image "+String.valueOf(start[selectedIndex[2]]+1)+
            " of "+dims[selectedIndex[2]]);

        this.updateUI();
    }

    // Implementing DataObserver.
    public void nextPage()
    {
        int rank = dataset.getRank();

        if (rank < 3)
            return;

        int[] selectedIndex = dataset.getSelectedIndex();
        long[] selectedDims = dataset.getSelectedDims();

        if (selectedDims[selectedIndex[2]] >1 )
            return; // it is a true color image with three color components

        long[] start = dataset.getStartDims();
        long[] dims = dataset.getDims();
        long idx = start[selectedIndex[2]];
        if (idx == dims[selectedIndex[2]]-1)
            return; // current page is the last page

        start[selectedIndex[2]] += 1;
        dataset.clearData();
        this.image = null;

        imageComponent.setImage(getImage());

        this.setTitle(
            "ImageView - "+
            dataset.getPath()+dataset.getName()+
            " - Image "+String.valueOf(start[selectedIndex[2]]+1)+
            " of "+dims[selectedIndex[2]]);

        this.updateUI();
    }

    // Implementing DataObserver.
    public void firstPage()
    {
        int rank = dataset.getRank();

        if (rank < 3)
            return;

        int[] selectedIndex = dataset.getSelectedIndex();
        long[] selectedDims = dataset.getSelectedDims();

        if (selectedDims[selectedIndex[2]] >1 )
            return; // it is a true color image with three color components

        long[] start = dataset.getStartDims();
        long[] dims = dataset.getDims();
        long idx = start[selectedIndex[2]];
        if (idx == 0)
            return; // current page is the first page

        start[selectedIndex[2]] = 0;
        dataset.clearData();
        this.image = null;

        imageComponent.setImage(getImage());

        this.setTitle(
            "ImageView - "+
            dataset.getPath()+dataset.getName()+
            " - Image "+String.valueOf(start[selectedIndex[2]]+1)+
            " of "+dims[selectedIndex[2]]);

        this.updateUI();
    }

    // Implementing DataObserver.
    public void lastPage()
    {
        int rank = dataset.getRank();

        if (rank < 3)
            return;

        int[] selectedIndex = dataset.getSelectedIndex();
        long[] selectedDims = dataset.getSelectedDims();

        if (selectedDims[selectedIndex[2]] >1 )
            return; // it is a true color image with three color components

        long[] start = dataset.getStartDims();
        long[] dims = dataset.getDims();
        long idx = start[selectedIndex[2]];
        if (idx == dims[selectedIndex[2]]-1)
            return; // current page is the last page

        start[selectedIndex[2]] = dims[selectedIndex[2]]-1;
        dataset.clearData();
        this.image = null;

        imageComponent.setImage(getImage());

        this.setTitle(
            "ImageView - "+
            dataset.getPath()+dataset.getName()+
            " - Image "+String.valueOf(start[selectedIndex[2]]+1)+
            " of "+dims[selectedIndex[2]]);

        this.updateUI();
    }

    // implementing ImageObserver
    public Image getImage()
    {
        if (this.image != null)
            return this.image;

        int rank = dataset.getRank();
        if (rank <=0) dataset.init();

        boolean isH4TrueColor = false;
        boolean isH5TrueColor = false;
        String strValue = null;

        if (dataset instanceof H4GRImage)
        {
            H4GRImage h4image = (H4GRImage)dataset;
            int n = h4image.getComponentCount();
            if (n >= 3)
                isH4TrueColor = true;
        }
        else if (dataset instanceof H5ScalarDS)
        {
            List list = null;
            try { list = dataset.getMetadata(); }
            catch (Exception ex) { viewer.showStatus(ex.toString()); }

            if (list != null)
            {
                int n = list.size();
                for (int i=0; i<n; i++)
                {
                    Attribute attr = (Attribute)list.get(i);
                    String name = attr.getName();
                    Object value = attr.getValue();

                    if (value.getClass().isArray())
                        strValue = Array.get(value, 0).toString();
                    else
                        strValue = value.toString();

                    // it is a true color image at one of three cases:
                    // 1) IMAGE_SUBCLASS = IMAGE_TRUECOLOR,
                    // 2) INTERLACE_MODE = INTERLACE_PIXEL,
                    // 3) INTERLACE_MODE = INTERLACE_PLANE
                    if ( (ScalarDS.IMAGE_SUBCLASS.equals(name) &&
                          ScalarDS.IMAGE_TRUECOLOR.equals(strValue)) ||
                         (ScalarDS.INTERLACE_MODE.equals(name) &&
                          ScalarDS.INTERLACE_PIXEL.equals(strValue)) ||
                         (ScalarDS.INTERLACE_MODE.equals(name) &&
                          ScalarDS.INTERLACE_PLANE.equals(strValue)) )
                    {
                        isH5TrueColor = true;
                        break;
                    }
                } // for (int i=0; i<n; i++)
            } // if (list != null)
        } //else if (dataset instanceof H5ScalarDS)

        if (isH4TrueColor)
        {
            H4GRImage h4i = (H4GRImage)dataset;
            this.image = createTrueColorImage(h4i.getInterlace());
        }
        else if (isH5TrueColor)
        {
            if (setH5ImageDimension(strValue))
                this.image = createTrueColorImage(strValue);
        }
        else
            this.image = createIndexedImage();

        return this.image;
    }

    // implementing ImageObserver
    public void zoomIn()
    {
        if (zoomFactor >= 1)
            zoomTo(zoomFactor+1.0f);
        else
            zoomTo(zoomFactor +0.125f);
    }

    // implementing ImageObserver
    public void zoomOut()
    {
        if (zoomFactor > 1)
            zoomTo(zoomFactor-1.0f);
        else
            zoomTo(zoomFactor-0.125f);
    }

    // implementing ImageObserver
    public void zoomTo(float zf)
    {
        if (zf >= 8)
            zoomFactor = 8;
        else if (zf <= 0.125)
            zoomFactor = 0.125f;
        else
            zoomFactor = zf;

        Dimension imageSize = new Dimension(
                (int)(imageComponent.originalSize.width*zoomFactor),
                (int)(imageComponent.originalSize.height*zoomFactor));

        imageComponent.setImageSize(imageSize);
        updateUI();

         if (zoomFactor>0.99 && zoomFactor<1.01)
        {
            setTitle("ImageView - "+dataset.getPath()+dataset.getName());
        }
        else
        {
            setTitle("ImageView - "+dataset.getPath()+dataset.getName()+
            " - "+100*zoomFactor+"%");
        }
    }

    // implementing ImageObserver
    public void showColorTable()
    {
        if (imagePalette == null)
            return;

        double[][] data = new double[3][256];
        int[] xRange = {0, 255};
        double[] yRange = {0, 255};
        String[] lineLabels = {"Red", "Green", "Blue"};
        Color[] lineColors = {Color.red, Color.green, Color.blue};
        String title = "Image Palette - "+dataset.getPath()+dataset.getName();

        double d = 0;
        for (int i=0; i<3; i++)
        {
            for (int j=0; j<256; j++)
            {
                d = (double)imagePalette[i][j];
                if (d < 0) d += 256;
                data[i][j] = d;
            }
        }

        ChartView cv = new ChartView(
            (Frame)viewer,
            title,
            ChartView.LINEPLOT,
            data,
            xRange,
            yRange);
        cv.setLineLabels(lineLabels);
        cv.setLineColors(lineColors);
        cv.setTypeToInteger();
        cv.show();
    }

    // implementing ImageObserver
    public void showHistogram()
    {
        Rectangle rec = imageComponent.selectedArea;
        if (indexedImageData == null ||
            rec == null ||
            rec.getWidth()==0 ||
            rec.getHeight()== 0)
            return;

        double data[][] = new double[1][256];
        for (int i=0; i<256; i++)
            data[0][i] = 0.0;

        int w = dataset.getWidth();
        int x0 = rec.x;
        int y0 = rec.y;
        int x = x0 + rec.width;
        int y = y0 + rec.height;
        int arrayIndex = 0;
        for (int i=y0; i<y; i++)
        {
            for (int j=x0; j<x; j++)
            {
                arrayIndex = (int)indexedImageData[i*w+j];
                if (arrayIndex < 0) arrayIndex += 256;
                data[0][arrayIndex] += 1.0;
            }
        }

        String title = "Histogram - " +
            dataset.getPath()+dataset.getName() +
            " - by pixel index";
        int[] xRange = {0, 255};

        ChartView cv = new ChartView(
            (Frame)viewer,
            title,
            ChartView.HISTOGRAM,
            data,
            xRange,
            null);
        cv.show();
    }

    /**
     *  Convert an array of raw data into array of a byte data.
     *  Byte data ranged from -128 to 127.
     *  <p>
     *  @param rawData The input raw data.
     *  @return the byte array of pixel data.
     */
    public static byte[] getBytes(Object rawData, double[] minmax)
    {
        byte[] byteData = null;

        // no pnput data
        if (rawData == null)
            return null;

        // input data is not an array
        if (!rawData.getClass().isArray())
            return null;


        double min=Double.MAX_VALUE, max=-Double.MAX_VALUE, ratio=1.0d;
        String cname = rawData.getClass().getName();
        char dname = cname.charAt(cname.lastIndexOf("[")+1);

        // no need for conversion
        if (dname == 'B')
            return (byte[]) rawData;

        int size = Array.getLength(rawData);
        byteData = new byte[size];

        switch (dname)
        {
            case 'S':
                short[] s = (short[])rawData;

                if (minmax != null)
                {
                    min = minmax[0];
                    max = minmax[1];
                }
                else
                {
                    // search for the minimum and maximum of the raw data
                    for (int i=0; i<size; i++)
                    {
                        min = Math.min(min, s[i]);
                        max = Math.max(max, s[i]);
                    }
                }

                // converts the data based on the ratio to support only 256 colors
                ratio = (min == max) ? 1.00d : (double)(255.00/(max-min));
                for (int i=0; i<size; i++)
                {
                    byteData[i] = (byte)((s[i]-min)*ratio);
                }

                break;

            case 'I':
                int[] ia = (int[])rawData;

                if (minmax != null)
                {
                    min = minmax[0];
                    max = minmax[1];
                }
                else
                {
                    // search for the minimum and maximum of the raw data
                    for (int i=0; i<size; i++)
                    {
                        min = Math.min(min, ia[i]);
                        max = Math.max(max, ia[i]);
                    }
                }

                // converts the data based on the ratio to support only 256 colors
                ratio = (min == max) ? 1.00d : (double)(255.00/(max-min));
                for (int i=0; i<size; i++)
                {
                    byteData[i] = (byte)((ia[i] - min)*ratio);
                }

                break;

            case 'J':
                long[] l = (long[])rawData;

                if (minmax != null)
                {
                    min = minmax[0];
                    max = minmax[1];
                }
                else
                {
                    // search for the minimum and maximum of the raw data
                    for (int i=0; i<size; i++)
                    {
                        min = Math.min(min, l[i]);
                        max = Math.max(max, l[i]);
                    }
                }

                // converts the data based on the ratio to support only 256 colors
                ratio = (min == max) ? 1.00d : (double)(255.00/(max-min));
                for (int i=0; i<size; i++)
                {
                    byteData[i] = (byte)((l[i]-min)*ratio);
                }

                break;

            case 'F':
                float[] f = (float[])rawData;

                if (minmax != null)
                {
                    min = minmax[0];
                    max = minmax[1];
                }
                else
                {
                    // search for the minimum and maximum of the raw data
                    for (int i=0; i<size; i++)
                    {
                        min = Math.min(min, f[i]);
                        max = Math.max(max, f[i]);
                    }
                }

                // converts the data based on the ratio to support only 256 colors
                ratio = (min == max) ? 1.00d : (double)(255.00/(max-min));
                for (int i=0; i<size; i++)
                {
                    byteData[i] = (byte)((f[i]-min)*ratio);
                }

                break;

            case 'D':
                double[] d = (double[])rawData;

                if (minmax != null)
                {
                    min = minmax[0];
                    max = minmax[1];
                }
                else
                {
                    // search for the minimum and maximum of the raw data
                    for (int i=0; i<size; i++)
                    {
                        min = Math.min(min, d[i]);
                        max = Math.max(max, d[i]);
                    }
                }

                // converts the data based on the ratio to support only 256 colors
                ratio = (min == max) ? 1.00d : (double)(255.00/(max-min));
                for (int i=0; i<size; i++)
                {
                    byteData[i] = (byte)((d[i]-min)*ratio);
                }

                break;

            default:
                byteData = null;
                break;
        } // switch (dname)

        return byteData;
    }

    /**
     *  Creates the gray palette of the indexed 256-color table.
     *  <p>
     *  The palette values are stored in a two-dimensional byte array and arrange
     *  by color components of red, green and blue. palette[][] = byte[3][256],
     *  where, palette[0][], palette[1][] and palette[2][] are the red, green and
     *  blue components respectively.
     */
    public static byte[][] createGrayPalette()
    {
        byte[][] p = new byte[3][256];

        for (int i=0; i<256; i++)
        {
            p[0][i] = p[1][i] = p[2][i] = (byte)i;
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
     */
    public static byte[][] createRainbowPalette()
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
     */
    public static byte[][] createNaturePalette()
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
     */
    public static byte[][] createWavePalette()
    {
        byte[][] p = new byte[3][256];

        for (int i=1; i<255; i++)
        {
            p[0][i] = (byte) ((Math.sin(((double)i/40-3.2))+1)*128);
            p[1][i] = (byte) ((1-Math.sin(((double)i/2.55-3.1)))*70+30);
            p[2][i] = (byte) ((1-Math.sin(((double)i/40-3.1)))*128);
        }

        p[0][0] = p[1][0] = p[2][0] = 0;
        p[0][255] = p[1][255] = p[2][255] = (byte)255;

        return p;
    }

    /** Creates a RGB indexed image of 256 colors. */
    private Image createIndexedImage()
    {
        Image theImage = null;

        imagePalette = dataset.getPalette();
        if (imagePalette == null)
            imagePalette = createGrayPalette();

        IndexColorModel colorModel = new IndexColorModel (
            8,                // bits - the number of bits each pixel occupies
            256,              // size - the size of the color component arrays
            imagePalette[0],  // r - the array of red color components
            imagePalette[1],  // g - the array of green color components
            imagePalette[2]); // b - the array of blue color components

        Object rawData = null;
        try { rawData = dataset.read(); }
        catch (Throwable ex) {
            viewer.showStatus(ex.toString());
            return null;
        }

        // converts raw data to image data
        indexedImageData = getBytes(rawData, null);

        //the followint code creates an WImage instead of a bufferedimate
        Toolkit toolkit = Toolkit.getDefaultToolkit();

        int w = dataset.getWidth();
        int h = dataset.getHeight();
        theImage = toolkit.createImage (new MemoryImageSource
            (w, h, colorModel, indexedImageData, 0, w));

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
     * @param interlace the interlace mode of color components.
     */
    private Image createTrueColorImage(String interlace)
    {
        Image theImage = null;

        Object rawData = null;
        try { rawData = dataset.read(); }
        catch (Throwable ex) {
            viewer.showStatus(ex.toString());
            return null;
        }

        // converts raw data to image data
        byte[] imageData = getBytes(rawData, null);

        int w = dataset.getWidth();
        int h = dataset.getHeight();
        int imgSize = w*h;
        int packedImageData[] = new int[imgSize];
        int pixel=0, idx=0, r=0, g=0, b=0;
        boolean isPlaneInterlace = interlace.equals(ScalarDS.INTERLACE_PLANE);
        for (int i=0; i<h; i++)
        {
            for (int j=0; j<w; j++)
            {
                pixel = r = g = b = 0;
                if (isPlaneInterlace)
                {
                    r = (int) imageData[idx];
                    g = (int) imageData[imgSize+idx];
                    b = (int) imageData[imgSize*2+idx];
                }
                else
                {
                    r = (int) imageData[idx*3];
                    g = (int) imageData[idx*3+1];
                    b = (int) imageData[idx*3+2];
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

        Toolkit toolkit = Toolkit.getDefaultToolkit();
        DirectColorModel dcm = (DirectColorModel)ColorModel.getRGBdefault();
        theImage = toolkit.createImage (new MemoryImageSource
            (w, h, dcm, packedImageData, 0, w));

        return theImage;
    }

    /**
     * Specify orders for the HDF5 image dimensions as:
       <pre>
           INTERLACE_PIXEL = [height][width][pixel components]
           INTERLACE_PLANE = [pixel components][height][width]
       </pre>
     * <p>
     * @param interlace the name of the interlace.
     */
    private boolean setH5ImageDimension(String interlace)
    {
        long selectedDims[] = dataset.getSelectedDims();
        if (selectedDims == null || selectedDims.length <3)
        {
            // requires at least three dimensions
            viewer.showStatus("The dataset does not have three dimensions.");
            return false;
        }

        if (interlace.equals(ScalarDS.INTERLACE_PLANE))
        {
            // plane interlace
            long dims[] = dataset.getDims();
            if (dims[0] < 3)
            {
                // requires three color components
                viewer.showStatus("The dataset does not have three color components.");
                return false;
            }

            selectedDims[0] = 3;
            int selectedIndex[] = dataset.getSelectedIndex();
            selectedIndex[0] = 1; // index for height
            selectedIndex[1] = 2; // index for width
            selectedIndex[2] = 0; // index for depth
        }
        else
        {
            // pixel interlace
            long dims[] = dataset.getDims();
            if (dims[2] < 3)
            {
                // requires three color components
                viewer.showStatus("The dataset does not have three color components.");
                return false;
            }

            int selectedIndex[] = dataset.getSelectedIndex();
            selectedDims[2] = 3;
            if (selectedIndex[0] != 0)
            {
                //change the default initialization
                // for three, the default selection is
                // selectedDims[2] = dims[2]
                // selectedDims[1] = dims[1]
                // selectedDims[0] = 1; which is not correct for true color images.
                selectedDims[0] = dims[0];
                selectedDims[1] = dims[1];
            }
            selectedIndex[0] = 0; // index for height
            selectedIndex[1] = 1; // index for width
            selectedIndex[2] = 2; // index for depth
        }

        return true;
    }

    /** ImageComponent draws the image. */
    private class ImageComponent extends JComponent
    implements MouseListener, MouseMotionListener
    {
        private Dimension originalSize, imageSize;
        private Image image;
        private Point startPosition; // mouse clicked position
        private Rectangle selectedArea;

        public ImageComponent (Image img)
        {
            image = img;
            imageSize = new Dimension(image.getWidth(this), image.getHeight(this));
            originalSize = imageSize;
            selectedArea = new Rectangle();
            setPreferredSize(imageSize);

            addMouseListener(this);
            addMouseMotionListener(this);
        }

        public void paint(Graphics g)
        {
            g.drawImage(image, 0, 0, imageSize.width, imageSize.height, this);
            int w = selectedArea.width;
            int h = selectedArea.height;
            if (w>0 && h >0)
            {
                g.setColor(Color.red);
                g.drawRect(selectedArea.x, selectedArea.y, w, h);
            }
        }

        public void mousePressed(MouseEvent e)
        {
            startPosition = e.getPoint();
            selectedArea.setBounds(startPosition.x, startPosition.y, 0, 0);
        }

        public void mouseClicked(MouseEvent e)
        {
            startPosition = e.getPoint();
            selectedArea.setBounds(startPosition.x, startPosition.y, 0, 0);
            repaint();
        }

        public void mouseDragged(MouseEvent e)
        {
            Point p0 = startPosition;
            Point p1 = e.getPoint();

            int x0 = Math.max(0, Math.min(p0.x, p1.x));
            int y0 = Math.max(0, Math.min(p0.y, p1.y));
            int x1 = Math.min(imageSize.width, Math.max(p0.x, p1.x));
            int y1 = Math.min(imageSize.height, Math.max(p0.y, p1.y));

            int w = x1 - x0;
            int h = y1 - y0;
            selectedArea.setBounds(x0, y0, w, h);

            repaint();
        }

        public void mouseReleased(MouseEvent e) {}
        public void mouseEntered(MouseEvent e) {}
        public void mouseExited(MouseEvent e)  {}
        public void mouseMoved(MouseEvent e) {}

        private void setImageSize(Dimension size)
        {
            double zf = size.getWidth()/imageSize.getWidth();

            imageSize = size;
            setPreferredSize(imageSize);

            repaint();
        }

        private void setImage(Image img)
        {
            image = img;
            imageSize = new Dimension(image.getWidth(this), image.getHeight(this));
            originalSize = imageSize;
            selectedArea.setSize(0, 0);
            setPreferredSize(imageSize);

            repaint();
        }

    } // private class ImageComponent extends JComponent

}