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

package test.ncsa.hdf.view;

import ncsa.hdf.view.*;
import ncsa.hdf.object.*;
import javax.swing.*;
import java.awt.image.*;
import java.awt.event.*;
import java.awt.Font;
import java.io.*;
import java.lang.reflect.Array;
import java.util.*;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Graphics2D;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.Rectangle;
import java.awt.Point;
import java.awt.Window;
import java.awt.Color;
import java.awt.Frame;
import java.awt.GraphicsEnvironment;
import java.awt.GraphicsDevice;
import java.awt.GraphicsConfiguration;
import java.awt.Transparency;

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
public class DefaultImageViewTest extends JInternalFrame
implements ImageView
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
    private boolean isHorizontalFlipped;

    /** Flag to indicate if the image is flipped vertically. */
    private boolean isVerticalFlipped;

    /** the number type of the image data */
    private char NT;

    /** the raw data of the image */
    private Object data;

    /** flag to indicate if the original data type is unsigned integer */
    private boolean isUnsigned;

    private final Toolkit toolkit;

    private double[] dataRange;

    private PaletteComponent paletteComponent;


    /**
     * Constructs an ImageView.
     * <p>
     * @param theView the main HDFView.
     */
    public DefaultImageViewTest(ViewManager theView)
    {
        super();

        setDefaultCloseOperation(JInternalFrame.DISPOSE_ON_CLOSE);
        setFrameIcon(ViewProperties.getImageIcon());

        viewer = theView;
        zoomFactor = 1.0f;
        indexedImageData = null;
        imagePalette = null;
        paletteComponent = null;
        isTrueColor = false;
        isPlaneInterlace = false;
        isHorizontalFlipped = false;
        isVerticalFlipped = false;
        isUnsigned = false;
        data = null;
        NT = 0;
        toolkit = Toolkit.getDefaultToolkit();
        dataRange = new double[2];
        dataRange[0] = dataRange[1] = 0;

        HObject hobject = (HObject)viewer.getTreeView().getCurrentObject();
        if (hobject == null || !(hobject instanceof ScalarDS)) {
            viewer.showStatus("Display data in image failed for - "+hobject);
            return;
        }

        dataset = (ScalarDS)hobject;
        JPanel contentPane = (JPanel)getContentPane();
        contentPane.setLayout(new BorderLayout());

        // add the text field to display pixel data
        contentPane.add(valueField=new JTextField(), BorderLayout.SOUTH);
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

        // add palette convas to show the palette
        if (imagePalette != null)
        {
            paletteComponent = new PaletteComponent(imagePalette, dataRange);
            contentPane.add (paletteComponent, BorderLayout.EAST);
        }

        // set title
        StringBuffer sb = new StringBuffer("ImageView - ");
        sb.append(dataset.getFile());
        sb.append(" - ");
        sb.append(hobject.getPath());
        sb.append(hobject.getName());

        frameTitle = sb.toString();
        setTitle(frameTitle);

        // setup subset information
        int rank = dataset.getRank();
        int[] selectedIndex = dataset.getSelectedIndex();
        long[] count = dataset.getSelectedDims();
        long[] stride = dataset.getStride();
        long[] dims = dataset.getDims();
        long[] start = dataset.getStartDims();
        int n = Math.min(3, rank);

        sb.append(" [ dims");
        sb.append(selectedIndex[0]);
        for (int i=1; i<n; i++)
        {
            sb.append("x");
            sb.append(selectedIndex[i]);
        }
        sb.append(", start");
        sb.append(start[selectedIndex[0]]);
        for (int i=1; i<n; i++)
        {
            sb.append("x");
            sb.append(start[selectedIndex[i]]);
        }
        sb.append(", count");
        sb.append(count[selectedIndex[0]]);
        for (int i=1; i<n; i++)
        {
            sb.append("x");
            sb.append(count[selectedIndex[i]]);
        }
        sb.append(", stride");
        sb.append(stride[selectedIndex[0]]);
        for (int i=1; i<n; i++)
        {
            sb.append("x");
            sb.append(stride[selectedIndex[i]]);
        }
        sb.append(" ] ");

        viewer.showStatus(sb.toString());
    }

    // Implementing DataView.
    public HObject getDataObject() {
        return dataset;
    }

    public byte[] getImageByteData() {
        return indexedImageData;
    }

    public void dispose()
    {
        // reload the data when it is displayed next time
        // because the display type (table or image) may be
        // different.
        if (!dataset.isImage()) dataset.clearData();

        data = null;
        image = null;
        indexedImageData = null;
        imageComponent = null;
        System.runFinalization();
        System.gc();

        viewer.removeDataView(this);

        super.dispose();
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
        isTrueColor = dataset.isTrueColor();

        String strValue = null;

        try
        {
            if (isTrueColor)
            {
                isPlaneInterlace = (dataset.getInterlace() ==ScalarDS.INTERLACE_PLANE);

                long[] selected = dataset.getSelectedDims();
                long[] start = dataset.getStartDims();
                int[] selectedIndex = dataset.getSelectedIndex();
                long[] stride = dataset.getStride();

                if (start.length > 2)
                {
                    start[selectedIndex[2]] = 0;
                    selected[selectedIndex[2]] = 3;
                    stride[selectedIndex[2]] = 1;
                }

                // reload data
                dataset.clearData();
                data = dataset.getData();

                // converts raw data to image data
                byte[] imageData = getBytes(data, dataRange);

                int w = dataset.getWidth();
                int h = dataset.getHeight();

                image = createTrueColorImage(imageData, isPlaneInterlace, w, h);
                imageData = null;
            }
            else
            {
                imagePalette = dataset.getPalette();
                if (imagePalette == null)
                {
                    imagePalette = createGrayPalette();
                    JOptionPane.showMessageDialog(
                        (Frame)viewer,
                        "No attached palette found, default grey palette is used.",
                        dataset.getName(),
                        JOptionPane.INFORMATION_MESSAGE);
                }
                data = dataset.getData();

                // converts raw data to image data
                indexedImageData = getBytes(data, dataRange);

                int w = dataset.getWidth();
                int h = dataset.getHeight();

                image = createIndexedImage(indexedImageData, imagePalette, w, h);
            }
        }
        catch (Throwable ex) {
            toolkit.beep();
            JOptionPane.showMessageDialog(this,
                ex.getMessage(),
                getTitle(),
                JOptionPane.ERROR_MESSAGE);
            return null;
        }

        // set number type, ...
        if (data != null)
        {
            isUnsigned = dataset.isUnsigned();
            String cname = data.getClass().getName();
            NT = cname.charAt(cname.lastIndexOf("[")+1);
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
/*
        if (imagePalette == null)
            return;

        double[][] paletteData = new double[3][256];
        int[] xRange = {0, 255};
        double[] yRange = {0, 255};
        //String[] lineLabels = {"Red", "Green", "Blue"};
        Color[] lineColors = {Color.red, Color.green, Color.blue};
        String title = "Image Palette - "+dataset.getPath()+dataset.getName();

        double d = 0;
        for (int i=0; i<3; i++)
        {
            for (int j=0; j<256; j++)
            {
                d = (double)imagePalette[i][j];
                if (d < 0) d += 256;
                paletteData[i][j] = d;
            }
        }

        PaletteView cv = new PaletteView(
            (Frame)viewer,
            title,
            Chart.LINEPLOT,
            paletteData,
            xRange,
            yRange);
        cv.setLineLabels(null);
        cv.setLineColors(lineColors);
        cv.setTypeToInteger();
        cv.show();
*/
    }

    // implementing ImageObserver
    public void showHistogram()
    {
        Rectangle rec = imageComponent.selectedArea;

        if (isTrueColor)
        {
            toolkit.beep();
            JOptionPane.showMessageDialog(this,
            "Unsupported operation: unable to draw histogram for true color image.",
            getTitle(),
            JOptionPane.ERROR_MESSAGE);
            return;
        }


        if( rec == null ||
            rec.getWidth()==0 ||
            rec.getHeight()== 0)
        {
            toolkit.beep();
            JOptionPane.showMessageDialog(this,
            "Select image area to draw histogram.",
            getTitle(),
            JOptionPane.ERROR_MESSAGE);
            return;
        }

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

        Chart cv = new Chart(
            (JFrame)viewer,
            title,
            Chart.HISTOGRAM,
            data,
            xRange,
            null);
        cv.show();
    }

    /**
     * Selects all whole image.
     * @throws Exception
     */
    public void selectAll() throws Exception
    {
        imageComponent.selectAll();
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
                isHorizontalFlipped = !isHorizontalFlipped;
            else
                isVerticalFlipped = !isVerticalFlipped;
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
    public void brightness(int level)
    {
        ImageFilter filter = new BrightnessFilter(level);

        if (filter == null)
            return;

        changeImageFilter(filter);
    }


    // implementing ImageObserver
    public void setValueVisible(boolean b)
    {
        valueField.setVisible(b);
        //updateUI(); bug !!! on Windows. gives NullPointerException at
        //javax.swing.plaf.basic.BasicInternalFrameUI$BorderListener.mousePressed(BasicInternalFrameUI.java:693)
    }

    /**
     *  Convert an array of raw data into array of a byte data.
     *  Byte data ranged from -128 to 127.
     *  <p>
     *  @param rawData The input raw data.
     *  @param minmax the range of the raw data.
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

        if (minmax == null)
        {
            minmax = new double[2];
            minmax[0] = minmax[1] = 0;
        }

        // no need for conversion
        if (dname == 'B')
        {
            minmax[0] = 0;
            minmax[1] = 255;
            return (byte[]) rawData;
        }

        int size = Array.getLength(rawData);
        byteData = new byte[size];
        boolean minmaxFound = !(minmax[0] == minmax[1]);

        switch (dname)
        {
            case 'S':
                short[] s = (short[])rawData;

                if (minmaxFound)
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

                if (minmaxFound)
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

                if (minmaxFound)
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

                if (minmaxFound)
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

                if (minmaxFound)
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

        minmax[0] = min;
        minmax[1] = max;

        return byteData;
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
            p[1][i] = (byte) ((1-Math.sin(((double)i/2.55-3.1)))*70+30);
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
        if (image == null)
            return false;

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

    /**
     * This method returns a buffered image with the contents of an image.
     * @param image the plain image object.
     * @return buffered image for the given image.
     */
    private BufferedImage toBufferedImage(Image image)
    {
        if (image == null)
            return null;

        if (image instanceof BufferedImage)
            return (BufferedImage)image;

        // !!!!!!!!!!!!!!!!!! NOTICE !!!!!!!!!!!!!!!!!!!!!
        // the following way of creating a buffered image is using
        // Component.createImage(). This method can be used only if the
        // component is visible on the screen. Also, this method returns
        // buffered images that do not support transparent pixels.
        // The buffered image created by this way works for package
        // com.sun.image.codec.jpeg.*
        // It does not work well with JavaTM Advanced Imaging
        // com.sun.media.jai.codec.*;
        // if the screen setting is less than 32-bit color
        int w = image.getWidth(null);
        int h = image.getHeight(null);
        BufferedImage bimage = (BufferedImage)createImage(w, h);
        Graphics g = bimage.createGraphics();
        g.drawImage(image, 0, 0, null);

/*
        // !!!!!!!!!!!!!!!!!! NOTICE !!!!!!!!!!!!!!!!!!!!!
        // An Image object cannot be converted to a BufferedImage object. The
        // closest equivalent is to create a buffered image and then draw the
        // image on the buffered image. The following way defines a method that
        // does this.
        // The buffered image created by this way does works with JavaTM Advanced Imaging
        // com.sun.media.jai.codec.*;
        // It does not work for the package
        // com.sun.image.codec.jpeg.*
        // if the image is a 24-bit true color image

        // Determine if the image has transparent pixels; for this method's
        boolean hasAlpha = hasAlpha(image);

        // Create a buffered image with a format that's compatible with the screen
        BufferedImage bimage = null;
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        try {
            // Determine the type of transparency of the new buffered image
            int transparency = Transparency.OPAQUE;
            if (hasAlpha) {
                transparency = Transparency.BITMASK;
            }

            // Create the buffered image
            GraphicsDevice gs = ge.getDefaultScreenDevice();
            GraphicsConfiguration gc = gs.getDefaultConfiguration();
            bimage = gc.createCompatibleImage(
                image.getWidth(null), image.getHeight(null), transparency);
            } catch (Exception e) {
            // The system does not have a screen
        }
        if (bimage == null)
        {
            // Create a buffered image using the default color model
            int type = BufferedImage.TYPE_INT_RGB;
            if (hasAlpha)
                type = BufferedImage.TYPE_INT_ARGB;

            bimage = new BufferedImage(image.getWidth(null), image.getHeight(null), type);
        }

        // Copy image to buffered image
        Graphics2D g = bimage.createGraphics();

        // Paint the image onto the buffered image
        g.drawImage(image, 0, 0, this);

*/

        g.dispose();
        return bimage;
    }

    /** Save the image to an image file.
     *  @param type the image type.
     *  @throws Exception
     */
    public void saveImageAs(String type) throws Exception
    {
        if (image == null)
            return;

        final JFileChooser fchooser = new JFileChooser(dataset.getFile());
        if (type.equals(FileFormat.FILE_TYPE_JPEG))
            fchooser.setFileFilter(DefaultFileFilter.getFileFilterJPEG());
        else if (type.equals(FileFormat.FILE_TYPE_TIFF))
            fchooser.setFileFilter(DefaultFileFilter.getFileFilterTIFF());
        else if (type.equals(FileFormat.FILE_TYPE_PNG))
            fchooser.setFileFilter(DefaultFileFilter.getFileFilterPNG());

        fchooser.changeToParentDirectory();
        fchooser.setDialogTitle("Save Current Image To "+type+" File --- "+dataset.getName());

        File choosedFile = new File(dataset.getName()+"."+type.toLowerCase());
        fchooser.setSelectedFile(choosedFile);

        int returnVal = fchooser.showSaveDialog(this);
        if(returnVal != JFileChooser.APPROVE_OPTION)
            return;

        choosedFile = fchooser.getSelectedFile();
        if (choosedFile == null) return;
        String fname = choosedFile.getAbsolutePath();

        if (choosedFile.exists())
        {
            int newFileFlag = JOptionPane.showConfirmDialog(this,
                "File exists. Do you want to replace it ?",
                this.getTitle(),
                JOptionPane.YES_NO_OPTION);
            if (newFileFlag == JOptionPane.NO_OPTION)
                return;
        }

        BufferedImage bi = null;
        try {
            bi = toBufferedImage(image);
        } catch (OutOfMemoryError err)
        {
            toolkit.beep();
            JOptionPane.showMessageDialog(this,
                err.getMessage(),
                getTitle(),
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        Tools.saveImageAs(bi, choosedFile, type);

        bi = null;

        viewer.showStatus("Current image saved to: "+fname);

        try {
            RandomAccessFile rf = new RandomAccessFile(choosedFile, "r");
            long size = rf.length();
            rf.close();
            viewer.showStatus("File size (bytes): "+size);
        } catch (Exception ex) {}
    }

    /**
     * Returns the selected data values.
     * @return the selected data object.
     */
    public Object getSelectedData()
    {
        Object selectedData = null;

        int cols = imageComponent.originalSelectedArea.width;
        int rows = imageComponent.originalSelectedArea.height;

        if (cols <=0 || rows <= 0)
            return null; // no data is selected

        int size = cols*rows;
        if (isTrueColor) size *= 3;

        if (NT == 'B')
            selectedData = new byte[size];
        else if (NT == 'S')
            selectedData = new short[size];
        else if (NT == 'I')
            selectedData = new int[size];
        else if (NT == 'J')
            selectedData = new long[size];
        else if (NT == 'F')
            selectedData = new float[size];
        else if (NT == 'D')
            selectedData = new double[size];
        else
            return null;

        int r0 = imageComponent.originalSelectedArea.y;
        int c0 = imageComponent.originalSelectedArea.x;
        int w = imageComponent.originalSize.width;
        int h = imageComponent.originalSize.height;

        // transfer location to the original coordinator
        if (isHorizontalFlipped)
            c0 = w - 1 - c0 - cols;

        if (isVerticalFlipped)
            r0 = h - 1 - r0 - rows;

        int idx_src=0, idx_dst=0;
        if (isTrueColor)
        {
            int imageSize = w*h;
            if (isPlaneInterlace)
            {
                for (int j=0; j<3; j++)
                {
                    int plane = imageSize*j;
                    for (int i=0; i<rows; i++)
                    {
                        idx_src = plane+(r0+i)*w+c0;
                        System.arraycopy(data, idx_src, selectedData, idx_dst, cols);
                        idx_dst += cols;
                    }
                }
            }
            else
            {
                int numberOfDataPoints = cols*3;
                for (int i=0; i<rows; i++)
                {
                    idx_src = (r0+i)*w+c0;
                    System.arraycopy(data, idx_src*3, selectedData, idx_dst, numberOfDataPoints);
                    idx_dst += numberOfDataPoints;
                }
            }
        }
        else // indexed image
        {
            for (int i=0; i<rows; i++)
            {
                idx_src = (r0+i)*w+c0;
                System.arraycopy(data, idx_src, selectedData, idx_dst, cols);
                idx_dst += cols;
            }
        }

        return selectedData;
    }

    /** returns the selected area of the image
     * @return the rectangle of the selected image area.
     */
    public Rectangle getSelectedArea()
    {
        return imageComponent.originalSelectedArea;
    }

    /** @return true if the image is a truecolor image. */
    public boolean isTrueColor() { return isTrueColor; }

    /** @return true if the image interlace is plance interlace. */
    public boolean isPlaneInterlace() { return isPlaneInterlace; }

    public void setImage(Image img) {
        imageComponent.setImage(img);
    }

    public byte[][] getPalette() { return imagePalette; }

    public void setPalette(byte[][] pal) {
        imagePalette = pal;
        paletteComponent.updatePalette(pal);
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
        theImage = Toolkit.getDefaultToolkit().createImage (
            new MemoryImageSource(w, h, dcm, packedImageData, 0, w));

        packedImageData = null;

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
                err.getMessage(),
                getTitle(),
                JOptionPane.ERROR_MESSAGE);
            status = false;
        }

        return status;
    }

    /** PaletteComponent draws the palette on the side of the image. */
    private class PaletteComponent extends JComponent
    {
        private Color[] colors = null;
        private double[] pixelData = null;
        private Dimension paintSize = null;
        java.text.DecimalFormat format;
        double[] dataRange = null;

        private PaletteComponent (byte[][] palette, double[] range)
        {
            paintSize = new Dimension(25, 2);
            format = new java.text.DecimalFormat("0.00E0");
            dataRange = range;

            if (palette != null && range !=null)
            {
                double ratio = (dataRange[1] - dataRange[0])/255;
                pixelData = new double[256];
                for (int i=0; i<256; i++)
                {
                    pixelData[i] = (dataRange[0] + ratio*i);
                }
            }

            setFont(new Font(((JFrame)viewer).getName(), Font.PLAIN, 10));

            updatePalette (palette);

            setPreferredSize(new Dimension(paintSize.width+50, paintSize.height*256));
            setVisible(true);
        }

        private void updatePalette (byte[][] palette)
        {
            if (palette != null && dataRange !=null)
            {
                colors = new Color[256];

                int r, g, b;
                for (int i=0; i<256; i++)
                {
                    r = (int)palette[0][i];
                    if (r < 0) r += 256;
                    g = (int)palette[1][i];
                    if (g < 0) g += 256;
                    b = (int)palette[2][i];
                    if (b < 0) b += 256;

                    colors[i] = new Color(r, g, b);
                }
            }

            repaint();
        }

        public void paint(Graphics g)
        {
            if (colors == null && pixelData == null)
                return;

            for (int i=0; i<256; i++)
            {
                g.setColor(colors[i]);
                g.fillRect(0, paintSize.height*i, paintSize.width, paintSize.height);
            }

            g.setColor(Color.black);
            for (int i=0; i<25; i++)
            {
                g.drawString(format.format(pixelData[i*10]), paintSize.width+5, 10+paintSize.height*i*10);
            }
            g.drawString(format.format(pixelData[255]), paintSize.width+5, paintSize.height*255);
        }

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
            if (isHorizontalFlipped)
                x = w - 1 - x;

            if (isVerticalFlipped)
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

        private void selectAll()
        {
            selectedArea.setBounds(0, 0, imageSize.width, imageSize.height);
            originalSelectedArea.setBounds(0, 0, originalSize.width, originalSize.height);
            repaint();
        }

        private long convertUnsignedPoint(int idx)
        {
            long l = 0;

            if (NT == 'B')
            {
                byte b = Array.getByte(data, idx);
                if (b<0)
                    l = b+256;
                else
                    l = b;
            }
            else if (NT == 'S')
            {
                short s = Array.getShort(data, idx);
                if (s<0)
                    l = s+65536;
                else
                    l = s;
            }
            else if (NT == 'I')
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
     * Makes an image filter for brightness.
     * @parma level the leve of brightness [-100 to 100]
     */
    private class BrightnessFilter extends RGBImageFilter
    {
        double level = 1.0;

        public BrightnessFilter(int plevel) {
            if (plevel == 0)
            {
                // the same
                level = 0;
            }
            else if (plevel > 0)
            {
                // brighter
                level = 1 + 2*(plevel/100.0);
            }
            else
            {
                // darker
                level = 1/(1 - 2*(plevel/100.0));
            }

            canFilterIndexColorModel = true;
        }

        public int filterRGB(int x, int y, int rgb)
        {
            if (level == 0)
                return rgb;

            int r = (rgb & 0x00ff0000) >> 16;
            int g = (rgb & 0x0000ff00) >> 8;
            int b = (rgb & 0x000000ff);
            r = Math.min(255, (int)(r*level));
            g = Math.min(255, (int)(g*level));
            b = Math.min(255, (int)(b*level));

            r = (r << 16) & 0x00ff0000;
            g = (g <<  8) & 0x0000ff00;
            b =  b        & 0x000000ff;

            return ((rgb & 0xff000000) | r | g | b);
        }
    }

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
         * @param theLevel the contour level.
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

        /** draw a contour line based on the current parameter---level, color
         * @param raster the data of the raster image.
         * @param pixels the pixel value of the image.
         * @param level the contour level.
         * @param color the color of the contour line.
         * @param w the width of the image.
         * @param h the height of the image.
         */
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
