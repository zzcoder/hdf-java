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
import java.io.*;
import java.lang.reflect.Array;
import java.util.*;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.Rectangle;
import java.awt.Point;
import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics2D;
import com.sun.image.codec.jpeg.*;

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
    /** Horizontal direction to flip an image. */
    public static final int FLIP_HORIZONTAL = 0;

    /** Vertical direction to flip an image. */
    public static final int FLIP_VERTICAL   = 1;

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
    private byte[][] imagePalette;

    /**
     * The title of this imageview.
     */
    private String frameTitle;

    /** TextField to show the image value. */
    private JTextField valueField;

    /** Flag to indicate if the image is a true color image */
    private boolean isTrueColor;

    /** Flag to indicate if the image is plane interleaved */
    private boolean isPlaneInterlace;

    /** Flag to indicate if the image is flipped horizontally. */
    private boolean horizontalFlipped;

    /** Flag to indicate if the image is flipped vertically. */
    private boolean verticalFlipped;

    /** the number type of the image data */
    private char nt;

    /** the raw data of the image */
    private Object data;

    /** flag to indicate if the original data type is unsigned integer */
    private boolean isUnsigned;

    private final Toolkit toolkit;

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
        isTrueColor = false;
        isPlaneInterlace = false;
        horizontalFlipped = false;
        verticalFlipped = false;
        isUnsigned = false;
        data = null;
        nt = 0;
        toolkit = Toolkit.getDefaultToolkit();

        HObject hobject = (HObject)viewer.getSelectedObject();
        if (hobject == null || !(hobject instanceof ScalarDS))
        {
            viewer.showStatus("Display data in image failed for - "+hobject);
            return;
        }

        dataset = (ScalarDS)hobject;
        String fname = new java.io.File(hobject.getFile()).getName();
        frameTitle = "ImageView - "+fname+" - " +hobject.getPath()+hobject.getName();

        this.setDefaultCloseOperation(JInternalFrame.DISPOSE_ON_CLOSE);
        this.setTitle(frameTitle);
        this.setFrameIcon(ViewProperties.getImageIcon());

        JPanel contentPane = (JPanel)getContentPane();
        contentPane.setLayout(new BorderLayout());

        contentPane.add(valueField=new JTextField(), BorderLayout.SOUTH);
        valueField.setVisible(false);
        valueField.setEditable(false);

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
        contentPane.add (scroller, BorderLayout.CENTER);

        // reset the title for 3D images
        if (dataset.getRank() > 2 && !isTrueColor)
        {
            long[] start = dataset.getStartDims();
            int[] selectedIndex = dataset.getSelectedIndex();
            long[] dims = dataset.getDims();
            setTitle( frameTitle+ " - Image "+String.valueOf(start[selectedIndex[2]]+1)+ " of "+dims[selectedIndex[2]]);
        }

    }

    public void dispose()
    {
        super.dispose();

        // reload the data when it is displayed next time
        // because the display type (table or image) may be
        // different.
        if (!dataset.isImage())
            dataset.clearData();

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

        gotoPage(start[selectedIndex[2]]-1);
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

        gotoPage(start[selectedIndex[2]]+1);
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

        gotoPage(0);
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

        gotoPage(dims[selectedIndex[2]]-1);
    }

    // implementing ImageObserver
    public Image getImage()
    {
        if (image != null)
            return image;

        int rank = dataset.getRank();
        if (rank <=0) dataset.init();

        isTrueColor = false;
        String strValue = null;

        if (dataset instanceof H4GRImage)
        {
            H4GRImage h4image = (H4GRImage)dataset;
            int n = h4image.getComponentCount();
            if (n >= 3) isTrueColor = true;
        }
        else if (dataset instanceof H5ScalarDS)
        {
            int interlace = dataset.getInterlace();
            isTrueColor = (interlace == ScalarDS.INTERLACE_PIXEL || interlace == ScalarDS.INTERLACE_PLANE);
        }

        if (isTrueColor)
            image = createTrueColorImage();
        else
            image = createIndexedImage();

        // set number type, ...
        if (data != null)
        {
            isUnsigned = dataset.isUnsigned();
            String cname = data.getClass().getName();
            nt = cname.charAt(cname.lastIndexOf("[")+1);
        }

        return image;
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
        if (zf > 8)
            zoomFactor = 8;
        else if (zf < 0.125)
            zoomFactor = 0.125f;
        else
            zoomFactor = zf;

        Dimension imageSize = new Dimension(
                (int)(imageComponent.originalSize.width*zoomFactor),
                (int)(imageComponent.originalSize.height*zoomFactor));

        imageComponent.setImageSize(imageSize);
        updateUI();

         if (zoomFactor>0.99 && zoomFactor<1.01)
            setTitle(frameTitle);
        else
            setTitle(frameTitle+ " - "+100*zoomFactor+"%");
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
        int x0 = (int)(rec.x/zoomFactor);
        int y0 = (int)(rec.y/zoomFactor);
        int x = x0 + (int)(rec.width/zoomFactor);
        int y = y0 + (int)(rec.height/zoomFactor);
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

    // implementing ImageObserver
    public void flip(int direction)
    {
        ImageFilter filter = new FlipFilter(direction);

        if (filter == null)
            return;

        if (changeImageFilter(filter))
        {
            // taggle flip flag
            if (direction == FLIP_HORIZONTAL)
                horizontalFlipped = !horizontalFlipped;
            else
                verticalFlipped = !verticalFlipped;
        }
    }

    // implementing ImageObserver
    public void contour(int level)
    {
        ImageFilter filter = new ContourFilter(level);

        if (filter == null)
            return;

        changeImageFilter(filter);
    }

    // implementing ImageObserver
    public void setValueVisible(boolean b)
    {
        valueField.setVisible(b);
        this.updateUI();
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
     *  Creates the rainbow palette of the indexed 256-color table.
     *  <p>
     *  The palette values are stored in a two-dimensional byte array and arrange
     *  by color components of red, green and blue. palette[][] = byte[3][256],
     *  where, palette[0][], palette[1][] and palette[2][] are the red, green and
     *  blue components respectively.
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
     */
    public static final byte[][] createWavePalette()
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

    /** Save image ad JPEG. */
    public void saveAsJPEG() throws Exception
    {
        if (image == null)
            return;

        final JFileChooser fchooser = new JFileChooser(dataset.getFile());
        fchooser.changeToParentDirectory();
        int returnVal = fchooser.showSaveDialog(this);

        if(returnVal != JFileChooser.APPROVE_OPTION)
            return;

        File choosedFile = fchooser.getSelectedFile();
        if (choosedFile == null)
            return;
        String fname = choosedFile.getAbsolutePath();

        // check if the file is in use
        List fileList = ((HDFView)viewer).getOpenFiles();
        if (fileList != null)
        {
            FileFormat theFile = null;
            Iterator iterator = fileList.iterator();
            while(iterator.hasNext())
            {
                theFile = (FileFormat)iterator.next();
                if (theFile.getFilePath().equals(fname))
                {
                    toolkit.beep();
                    JOptionPane.showMessageDialog(this,
                        "Unable to save data to file \""+fname+"\". \nThe file is being used.",
                        getTitle(),
                        JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
        }

        if (choosedFile.exists())
        {
            int newFileFlag = JOptionPane.showConfirmDialog(this,
                "File exists. Do you want to replace it ?",
                this.getTitle(),
                JOptionPane.YES_NO_OPTION);
            if (newFileFlag == JOptionPane.NO_OPTION)
                return;
        }

        int w = image.getWidth(this);
        int h = image.getHeight(this);
        BufferedImage bi = (BufferedImage)createImage(w, h);
        Graphics2D big = bi.createGraphics();
        big.drawImage(image, 0, 0, w, h, this);

        BufferedOutputStream out = new BufferedOutputStream(
            new FileOutputStream(choosedFile));

        JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
        encoder.encode(bi);

        out.flush();
        out.close();
    }

    private void gotoPage(long idx)
    {
        if (dataset.getRank() < 3) return;

        long[] start = dataset.getStartDims();
        int[] selectedIndex = dataset.getSelectedIndex();
        long[] dims = dataset.getDims();

        start[selectedIndex[2]] = idx;
        dataset.clearData();
        image = null;
        imageComponent.setImage(getImage());

        setTitle( frameTitle+ " - Image "+String.valueOf(idx+1)+ " of "+dims[selectedIndex[2]]);
        updateUI();
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

        try { data = dataset.read(); }
        catch (Throwable ex) {
            toolkit.beep();
            JOptionPane.showMessageDialog(this,
                ex,
                getTitle(),
                JOptionPane.ERROR_MESSAGE);
            return null;
        }

        // converts raw data to image data
        indexedImageData = getBytes(data, null);

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
     */
    private Image createTrueColorImage()
    {
        Image theImage = null;
        isPlaneInterlace = (dataset.getInterlace() ==ScalarDS.INTERLACE_PLANE);

        long[] selected = dataset.getSelectedDims();
        long[] start = dataset.getStartDims();
        int[] selectedIndex = dataset.getSelectedIndex();
        long[] stride = dataset.getStride();
        start[selectedIndex[2]] = 0;
        selected[selectedIndex[2]] = 3;
        if (stride != null) stride[selectedIndex[2]] = 1;
        dataset.clearData();

        try { data = dataset.read(); }
        catch (Throwable ex) {
            toolkit.beep();
            JOptionPane.showMessageDialog(this,
                ex,
                getTitle(),
                JOptionPane.ERROR_MESSAGE);
            return null;
        }

        // converts raw data to image data
        byte[] imageData = getBytes(data, null);

        int w = dataset.getWidth();
        int h = dataset.getHeight();
        int imgSize = w*h;
        int packedImageData[] = new int[imgSize];
        int pixel=0, idx=0, r=0, g=0, b=0;
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

        DirectColorModel dcm = (DirectColorModel)ColorModel.getRGBdefault();
        theImage = toolkit.createImage (new MemoryImageSource
            (w, h, dcm, packedImageData, 0, w));

        return theImage;
    }

    private boolean changeImageFilter(ImageFilter filter)
    {
        boolean status = true;
        ImageProducer imageProducer = image.getSource();

        try {
            image = createImage(new FilteredImageSource(imageProducer,filter));
            imageComponent.setImage(image);
            zoomTo(zoomFactor);
        } catch (Throwable err)
        {
            toolkit.beep();
            JOptionPane.showMessageDialog(this,
                err,
                getTitle(),
                JOptionPane.ERROR_MESSAGE);
            status = false;
        }

        return status;
    }

    /** ImageComponent draws the image. */
    private class ImageComponent extends JComponent
        implements MouseListener, MouseMotionListener
    {
        private Dimension originalSize, imageSize;
        private Image image;
        private Point startPosition; // mouse clicked position
        private Rectangle selectedArea, originalSelectedArea;
        private StringBuffer strBuff; // to hold display value

        private ImageComponent (Image img)
        {
            image = img;
            imageSize = new Dimension(image.getWidth(this), image.getHeight(this));
            originalSize = imageSize;
            selectedArea = new Rectangle();
            originalSelectedArea = new Rectangle();
            setPreferredSize(imageSize);
            strBuff = new StringBuffer();

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
            originalSelectedArea.setBounds(
                (int)(x0/zoomFactor),
                (int)(y0/zoomFactor),
                (int)(w/zoomFactor),
                (int)(h/zoomFactor));

            repaint();
        }

        public void mouseReleased(MouseEvent e) {}
        public void mouseEntered(MouseEvent e) {}
        public void mouseExited(MouseEvent e)  {}

        public void mouseMoved(MouseEvent e)
        {
            if (!valueField.isVisible())
                return;

            if (data == null)
                return;

            int x = (int) (e.getX()/zoomFactor);
            int w = originalSize.width;
            if (x < 0 || x >= w)
                return; // out of image bound

            int y = (int) (e.getY()/zoomFactor);
            int h = originalSize.height;
            if (y < 0 || y >= h)
                return; // out of image bound

            // transfer location to the original coordinator
            if (horizontalFlipped)
                x = w - 1 - x;

            if (verticalFlipped)
                y = h - 1 - y;

            strBuff.setLength(0); // reset the string buffer
            strBuff.append("x=");
            strBuff.append(x);
            strBuff.append(",   y=");
            strBuff.append(y);
            strBuff.append(",   value=");

            if (isTrueColor)
            {
                strBuff.append("(");
                if (isPlaneInterlace)
                {
                    if (isUnsigned)
                    {
                        strBuff.append(convertUnsignedPoint(y*w+x));
                        strBuff.append(", ");
                        strBuff.append(convertUnsignedPoint(w*h+y*w+x));
                        strBuff.append(", ");
                        strBuff.append(convertUnsignedPoint(2*w*h+y*w+x));
                    }
                    else
                    {
                        strBuff.append(Array.get(data, y*w+x));
                        strBuff.append(", ");
                        strBuff.append(Array.get(data, w*h+y*w+x));
                        strBuff.append(", ");
                        strBuff.append(Array.get(data, 2*w*h+y*w+x));
                    }
                }
                else
                {
                    if (isUnsigned)
                    {
                        strBuff.append(convertUnsignedPoint(3*(y*w+x)));
                        strBuff.append(", ");
                        strBuff.append(convertUnsignedPoint(3*(y*w+x)+1));
                        strBuff.append(", ");
                        strBuff.append(convertUnsignedPoint(3*(y*w+x)+2));
                    }
                    else
                    {
                        strBuff.append(Array.get(data, 3*(y*w+x)));
                        strBuff.append(", ");
                        strBuff.append(Array.get(data, 3*(y*w+x)+1));
                        strBuff.append(", ");
                        strBuff.append(Array.get(data, 3*(y*w+x)+2));
                    }
                }
                strBuff.append(")");
            }
            else
            {
                if (isUnsigned)
                    strBuff.append(convertUnsignedPoint(y*w+x));
                else
                    strBuff.append(Array.get(data, y*w+x));
            }

            valueField.setText(strBuff.toString());
        }

        private long convertUnsignedPoint(int idx)
        {
            long l = 0;

            if (nt == 'B')
            {
                byte b = Array.getByte(data, idx);
                if (b<0)
                    l = b+256;
                else
                    l = b;
            }
            else if (nt == 'S')
            {
                short s = Array.getShort(data, idx);
                if (s<0)
                    l = s+65536;
                else
                    l = s;
            }
            else if (nt == 'I')
            {
                int i = Array.getInt(data, idx);
                if (i<0)
                    l = i+4294967296L;
                else
                    l = i;
            }

            return l;
        }

        private void setImageSize(Dimension size)
        {
            imageSize = size;
            setPreferredSize(imageSize);

            int w = selectedArea.width;
            int h = selectedArea.height;
            if (w>0 && h >0)
            {
                // use fixed aelected area to reduce the rounding error
                selectedArea.setBounds(
                    (int)(originalSelectedArea.x*zoomFactor),
                    (int)(originalSelectedArea.y*zoomFactor),
                    (int)(originalSelectedArea.width*zoomFactor),
                    (int)(originalSelectedArea.height*zoomFactor)
                );
            }

            repaint();
        }

        private void setImage(Image img)
        {
            image = img;
            imageSize = new Dimension(image.getWidth(this), image.getHeight(this));
            originalSize = imageSize;
            selectedArea.setSize(0, 0);
            setPreferredSize(imageSize);

            setImageSize(new Dimension(
                (int)(originalSize.width*zoomFactor),
                (int)(originalSize.height*zoomFactor)));

            repaint();
        }

    } // private class ImageComponent extends JComponent

    /**
     * FlipFileter creates image filter to flip image horizontally or
     * vertically.
     */
    private class FlipFilter extends ImageFilter
    {
        /** flip direction */
        private int direction;

        /** pixel value */
        private int raster[] = null;

        /** width & height */
        private int imageWidth, imageHeight;

        /**
         * Constructs an image filter to flip horizontally or vertically.
         * <p>
         * @param d the flip direction.
         */
        private FlipFilter(int d)
        {
            if (d < FLIP_HORIZONTAL)
                d = FLIP_HORIZONTAL;
            else if (d > FLIP_VERTICAL)
                d = FLIP_VERTICAL;

            direction = d;
        }

        public void setDimensions(int w, int h)
        {
            imageWidth = w;
            imageHeight = h;

            // specify the raster
            if (raster == null)
                raster = new int[imageWidth*imageHeight];

            consumer.setDimensions(imageWidth, imageHeight);
        }

        public void setPixels(int x, int y, int w, int h, ColorModel model,
            byte pixels[], int off, int scansize)
        {
            int srcoff = off;
            int dstoff = y * imageWidth + x;
            for (int yc = 0; yc < h; yc++)
            {
                for (int xc = 0; xc < w; xc++)
                {
                    raster[dstoff++] = model.getRGB(pixels[srcoff++] & 0xff);
                }

                srcoff += (scansize - w);
                dstoff += (imageWidth - w);
            }
        }

        public void setPixels(int x, int y, int w, int h, ColorModel model,
            int pixels[], int off, int scansize)
        {
            int srcoff = off;
            int dstoff = y * imageWidth + x;

            for (int yc = 0; yc < h; yc++)
            {
                for (int xc = 0; xc < w; xc++)
                {
                    raster[dstoff++] = model.getRGB(pixels[srcoff++]);
                }
                srcoff += (scansize - w);
                dstoff += (imageWidth - w);
            }
        }

        public void imageComplete(int status)
        {
            if (status == IMAGEERROR || status == IMAGEABORTED)
            {
                consumer.imageComplete(status);
                return;
            }

            int pixels[] = new int[imageWidth];
            for (int y = 0; y < imageHeight; y++)
            {
                if (direction == FLIP_VERTICAL )
                {
                    // grab pixel values of the target line ...
                    int pos = (imageHeight-1-y)*imageWidth;
                    for (int kk=0; kk<imageWidth; kk++)
                        pixels[kk] = raster[pos+kk];
                }
                else
                {
                    int pos = y*imageWidth;
                    for (int kk=0; kk<imageWidth; kk++)
                        pixels[kk] = raster[pos+kk];

                    // swap the pixel values of the target line
                    int hw = imageWidth/2;
                    for (int  kk=0; kk<hw; kk++)
                    {
                        int tmp = pixels[kk];
                        pixels[kk]  = pixels[imageWidth-kk-1];
                        pixels[imageWidth-kk-1] = tmp;
                    }
                }

                // consumer it ....
                consumer.setPixels(0, y, imageWidth, 1,
                    ColorModel.getRGBdefault(), pixels, 0, imageWidth);
            } // for (int y = 0; y < imageHeight; y++)

            // complete ?
            consumer.imageComplete(status);
        }
    } // private class FlipFilter extends ImageFilter

    /**
     * Makes an image filter for contour.
     */
    private class ContourFilter extends ImageFilter
    {
        // default color model
        private ColorModel defaultRGB;

        // contour level
        int	level;

        // the table of the contour levels
        int	levels[];

        // colors for drawable contour line
        int[] levelColors;

        // default RGB

        // pixel value
        private int raster[] = null;

        // width & height
        private int imageWidth, imageHeight;

        /**
         * Create an contour filter for a given level contouring.
         */
        private ContourFilter(int theLevel)
        {
            defaultRGB = ColorModel.getRGBdefault();

            levelColors = new int[9];

            if (theLevel < 1) theLevel = 1;
            else if (theLevel > 9) theLevel = 9;

            level = theLevel;
            levels = new int[level];

            levelColors[0] = Color.white.getRGB();
            levelColors[1] = Color.red.getRGB();
            levelColors[2] = Color.yellow.getRGB();
            levelColors[3] = Color.blue.getRGB();
            levelColors[4] = Color.orange.getRGB();
            levelColors[5] = Color.green.getRGB();
            levelColors[6] = Color.cyan.getRGB();
            levelColors[7] = Color.pink.getRGB();
            levelColors[8] = Color.gray.getRGB();

            int dx  = 255/level;
            for (int i=0; i<level; i++)
                levels[i] = (i+1)*dx;
        }

        public void setDimensions(int width, int height)
        {
            this.imageWidth = width;
            this.imageHeight= height;

            // specify the raster
            if (raster == null)
                raster = new int[imageWidth*imageHeight];

            consumer.setDimensions(width, height);
        }

        public void setPixels(int x, int y, int w, int h,
            ColorModel model, byte pixels[], int off, int scansize)
        {
            int rgb = 0;
            int srcoff = off;
            int dstoff = y * imageWidth + x;

            for (int yc = 0; yc < h; yc++)
            {
                for (int xc = 0; xc < w; xc++)
                {
                    rgb = model.getRGB(pixels[srcoff++] & 0xff);
                    raster[dstoff++] = (((rgb >> 16) & 0xff) +
                        ((rgb >> 8) & 0xff) +
                        (rgb & 0xff))/3;
                }
                srcoff += (scansize - w);
                dstoff += (imageWidth - w);
            }

        }

        public void setPixels(int x, int y, int w, int h,
            ColorModel model, int pixels[], int off, int scansize)
        {
            int rgb = 0;
            int srcoff = off;
            int dstoff = y * imageWidth + x;

            for (int yc = 0; yc < h; yc++)
            {
                for (int xc = 0; xc < w; xc++)
                {
                    rgb = model.getRGB(pixels[srcoff++] & 0xff);
                    raster[dstoff++] = (((rgb >> 16) & 0xff) +
                        ((rgb >> 8) & 0xff) +
                        (rgb & 0xff))/3;
                }

                srcoff += (scansize - w);
                dstoff += (imageWidth - w);
            }
        }

        public void imageComplete(int status)
        {
            if (status == IMAGEERROR ||
                status == IMAGEABORTED)
            {
                consumer.imageComplete(status);
                return;
            }

            int pixels[] = new int[imageWidth*imageHeight];
            for (int z=0; z<levels.length; z++)
            {
                int currentLevel = levels[z];
                int color = levelColors[z];

                setContourLine(raster, pixels, currentLevel, color, imageWidth, imageHeight);
            }

            int line[] = new int[imageWidth];
            for (int y = 0; y < imageHeight; y++)
            {
                for (int x=0; x < imageWidth; x++)
                    line[x] = pixels[y*imageWidth+x];

                consumer.setPixels(0, y, imageWidth, 1, defaultRGB, line, 0, imageWidth);
            }  // for (int y = 0; y < imageHeight; y++) {

            // complete ?
            consumer.imageComplete(status);
        }

        /** draw a contour line based on the current parameter---level, color */
        private void setContourLine(int[] raster, int[] pixels,
                int level, int color, int w, int h)
        {
            int p = 0;   // entrance point
            int q = p + (w*h-1);   // bottom right point
            int u = 0 + (w-1);     // top right point

            // first round
            while(true)
            {
                while ( p < u )
                {
                    int rgb = raster[p];
                    if (rgb < level)
                    {
                        while ((raster[p] < level)&&(p < u))
                            p++;
                        if (raster[p] >= level)
                        pixels[p] = color;
                    }
                    else if (rgb == level)
                    {
                        while ((raster[p] == level)&&(p < u))
                            p++;
                        if ((raster[p] < level)  || (raster[p] > level))
                            pixels[p] = color;
                    }
                    else
                    {
                        while ((raster[p] > level)&&(p < u))
                            p++;
                        if ((raster[p] <= level))
                            pixels[p] = color;
                    }
                }

                if (u == q)
                    break;
                else
                {
                    u += w;
                    p++;
                }
            }
        }
    } // private class ContourFilter extends ImageFilter

}