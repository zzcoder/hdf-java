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

import ncsa.hdf.object.*;
import javax.swing.*;
import javax.swing.tree.*;
import javax.swing.border.*;
import javax.print.*;
import javax.print.attribute.*;
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
import java.awt.Insets;

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
public class DefaultImageView extends JInternalFrame
implements ImageView, ActionListener
{
    /** Horizontal direction to flip an image. */
    public static final int FLIP_HORIZONTAL = 0;

    /** Vertical direction to flip an image. */
    public static final int FLIP_VERTICAL   = 1;

    /** ROTATE IMAGE 90 DEGREE CLOCKWISE. */
    public static final int ROTATE_CW_90 = 10;

    /** ROTATE IMAGE COUNTER CLOCKWISE 90 DEGREE. */
    public static final int ROTATE_CCW_90   = 11;

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

    /** Flag to indicate if the image is a 3D */
    private boolean is3D;

    /** Flag to indicate if the image is plane interleaved */
    private boolean isPlaneInterlace;

    private boolean isHorizontalFlipped = false;

    private boolean isVerticalFlipped = false;

    private int rotateCount = 0;

    /** the number type of the image data */
    private char NT;

    /** the raw data of the image */
    private Object data;

    /** flag to indicate if the original data type is unsigned integer */
    private boolean isUnsigned;

    private final Toolkit toolkit;

    private double[] dataRange;

    private PaletteComponent paletteComponent;

    private String palName = null;

    private int animationSpeed = 2;

    private List rotateRelatedItems;


    /**
     * Constructs an ImageView.
     * <p>
     * @param theView the main HDFView.
     */
    public DefaultImageView(ViewManager theView)
    {
        this(theView, Boolean.FALSE);
    }

    /**
     * Constructs an ImageView.
     * <p>
     * @param theView the main HDFView.
     */
    public DefaultImageView(ViewManager theView, Boolean isTransposed)
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
        is3D = false;
        isPlaneInterlace = false;
        isUnsigned = false;
        data = null;
        NT = 0;
        toolkit = Toolkit.getDefaultToolkit();
        dataRange = new double[2];
        dataRange[0] = dataRange[1] = 0;
        rotateRelatedItems = new Vector();

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
        valueField.setVisible(false);

        if (image == null)
            getImage();

        if (image == null) {
            viewer.showStatus("Loading image failed - "+dataset.getName());
            return;
        }

        imageComponent = new ImageComponent(image);
        JScrollPane scroller = new JScrollPane(imageComponent);
        scroller.getVerticalScrollBar().setUnitIncrement(50);
        scroller.getHorizontalScrollBar().setUnitIncrement(50);
        contentPane.add (scroller, BorderLayout.CENTER);

        // add palette convas to show the palette
        if (imagePalette != null) {
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

        setJMenuBar(createMenuBar());
        viewer.showStatus(sb.toString());

        // tanspose the image
        if (isTransposed.booleanValue()) {
            rotate(ROTATE_CW_90);
            rotateCount++;
            n = rotateRelatedItems.size();
            for (int i=0; i< n; i++) {
                ((javax.swing.JComponent)rotateRelatedItems.get(i)).setEnabled(false);
            }
        }
    }

    private JMenuBar createMenuBar() {
        JMenuBar bar = new JMenuBar();
        JButton button;
        boolean isEditable = !dataset.getFileFormat().isReadOnly();

        JMenu menu = new JMenu("Image", false);
        menu.setMnemonic('I');
        bar.add(menu);

        JMenuItem item = new JMenuItem( "Save Image As JPEG");
        item.addActionListener(this);
        item.setActionCommand("Save image as jpeg");
        menu.add(item);

        menu.addSeparator();

        item = new JMenuItem( "Write Selection to Image");
        item.addActionListener(this);
        item.setActionCommand("Write selection to image");
        item.setEnabled(isEditable);
        rotateRelatedItems.add(item);
        menu.add(item);

        menu.addSeparator();

        item = new JMenuItem( "Change Palette");
        item.addActionListener(this);
        item.setActionCommand("Edit palette");
        item.setEnabled(!isTrueColor);
        menu.add(item);

        item = new JMenuItem( "Show Histogram");
        item.addActionListener(this);
        item.setActionCommand("Show chart");
        item.setEnabled(!isTrueColor);
        rotateRelatedItems.add(item);
        menu.add(item);

        menu.addSeparator();

        item = new JMenuItem( "Zoom In");
        item.addActionListener(this);
        item.setActionCommand("Zoom in");
        menu.add(item);

        item = new JMenuItem( "Zoom Out");
        item.addActionListener(this);
        item.setActionCommand("Zoom out");
        menu.add(item);

        menu.addSeparator();

        JMenu imageMenu = new JMenu("Flip");
        menu.add(imageMenu);

        item = new JMenuItem( "Flip Horizontal");
        item.addActionListener(this);
        item.setActionCommand("Flip horizontal");
        imageMenu.add(item);

        item = new JMenuItem( "Flip Vertical");
        item.addActionListener(this);
        item.setActionCommand("Flip vertical");
        imageMenu.add(item);

        imageMenu = new JMenu("Rotate Image");
        menu.add(imageMenu);

        char t= 186;
        item = new JMenuItem( "90"+t+" CW");
        item.addActionListener(this);
        item.setActionCommand("Rotate clockwise");
        imageMenu.add(item);

        item = new JMenuItem( "90"+t+" CCW");
        item.addActionListener(this);
        item.setActionCommand("Rotate counter clockwise");
        imageMenu.add(item);

        menu.addSeparator();

        item = new JMenuItem( "Brightness");
        item.addActionListener(this);
        item.setActionCommand("Brightness");
        menu.add(item);

        JMenu contourMenu = new JMenu("Contour");
        for (int i=3; i<10; i++)
        {
            item = new JMenuItem( String.valueOf(i));
            item.addActionListener(this);
            item.setActionCommand("Contour "+i);
            contourMenu.add(item);
        }
        menu.add(contourMenu);
        menu.addSeparator();

        item = new JMenuItem( "Show Animation");
        item.addActionListener(this);
        item.setActionCommand("Show animation");
        item.setEnabled(is3D);
        menu.add(item);

        JMenu animationMenu = new JMenu("Animation (frames/second)");
        for (int i=2; i<12; i=i+2) {
            item = new JMenuItem( String.valueOf(i));
            item.addActionListener(this);
            item.setActionCommand("Animation speed "+i);
            animationMenu.add(item);
        }
        animationMenu.setEnabled(is3D);
        menu.add(animationMenu);
        menu.addSeparator();

        JCheckBoxMenuItem imageValueCheckBox = new JCheckBoxMenuItem( "Show Value", false);
        imageValueCheckBox.addActionListener(this);
        imageValueCheckBox.setActionCommand("Show image value");
        rotateRelatedItems.add(imageValueCheckBox);
        menu.add(imageValueCheckBox);

        menu.addSeparator();

        item = new JMenuItem( "Select All");
        item.addActionListener(this);
        item.setActionCommand("Select all data");
        menu.add(item);

        menu.addSeparator();

        item = new JMenuItem( "Close");
        item.addActionListener(this);
        item.setActionCommand("Close");
        menu.add(item);

        bar.add( new JLabel("     ") );

        // add icons to the menubar

        Insets margin = new Insets( 0, 2, 0, 2 );

        // chart button
        button = new JButton( ViewProperties.getChartIcon() );
        bar.add( button );
        button.setToolTipText( "Histogram" );
        button.setMargin( margin );
        button.addActionListener( this );
        button.setActionCommand( "Show chart" );
        button.setEnabled(!isTrueColor);

        // palette button
        button = new JButton( ViewProperties.getPaletteIcon() );
        bar.add( button );
        button.setToolTipText( "Palette" );
        button.setMargin( margin );
        button.addActionListener( this );
        button.setActionCommand( "Edit palette" );
        button.setEnabled(!isTrueColor);

        button = new JButton(ViewProperties.getZoominIcon());
        bar.add(button);
        button.addActionListener(this);
        button.setMargin( margin );
        button.setActionCommand("Zoom in");
        button.setToolTipText("Zoom In");

        // zoom out button
        button = new JButton( ViewProperties.getZoomoutIcon() );
        bar.add( button );
        button.setToolTipText( "Zoom Out" );
        button.setMargin( margin );
        button.addActionListener( this );
        button.setActionCommand( "Zoom out" );

        if (is3D) {
            bar.add( new JLabel("     ") );

            // first button
            button = new JButton( ViewProperties.getFirstIcon() );
            bar.add( button );
            button.setToolTipText( "First" );
            button.setMargin( margin );
            button.addActionListener( this );
            button.setActionCommand( "First page" );

            // previous button
            button = new JButton( ViewProperties.getPreviousIcon() );
            bar.add( button );
            button.setToolTipText( "Previous" );
            button.setMargin( margin );
            button.addActionListener( this );
            button.setActionCommand( "Previous page" );

            // next button
            button = new JButton( ViewProperties.getNextIcon() );
            bar.add( button );
            button.setToolTipText( "Next" );
            button.setMargin( margin );
            button.addActionListener( this );
            button.setActionCommand( "Next page" );

            // last button
            button = new JButton( ViewProperties.getLastIcon() );
            bar.add( button );
            button.setToolTipText( "Last" );
            button.setMargin( margin );
            button.addActionListener( this );
            button.setActionCommand( "Last page" );

            button = new JButton( ViewProperties.getAnimationIcon() );
            bar.add( button );
            button.setToolTipText( "Animation" );
            button.setMargin( margin );
            button.addActionListener( this );
            button.setActionCommand( "Show animation" );
        }

        return bar;
    }

    // Implementing DataObserver.
    private void previousPage()
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
    private void nextPage()
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
    private void firstPage()
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
    private void lastPage()
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

    public Image getImage()
    {
        if (image != null)
            return image;

        int rank = dataset.getRank();
        if (rank <=0) dataset.init();
        isTrueColor = dataset.isTrueColor();
        is3D = (dataset.getRank() > 2) && !((ScalarDS)dataset).isTrueColor();

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
                byte[] imageData = Tools.getBytes(data, dataRange);

                int w = dataset.getWidth();
                int h = dataset.getHeight();

                image = createTrueColorImage(imageData, isPlaneInterlace, w, h);
                imageData = null;
            } else {
                imagePalette = dataset.getPalette();
                if (imagePalette == null) {
                    imagePalette = Tools.createGrayPalette();

                    viewer.showStatus("\nNo attached palette found, default grey palette is used to display image");
/*
                    JOptionPane.showMessageDialog(
                        (Frame)viewer,
                        "No attached palette found, default grey palette is used.",
                        dataset.getName(),
                        JOptionPane.INFORMATION_MESSAGE);
*/
                }
                data = dataset.getData();

                // converts raw data to image data
                indexedImageData = Tools.getBytes(data, dataRange);

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
    private void zoomIn()
    {
        if (zoomFactor >= 1)
            zoomTo(zoomFactor+1.0f);
        else
            zoomTo(zoomFactor +0.125f);
    }

    // implementing ImageObserver
    private void zoomOut()
    {
        if (zoomFactor > 1)
            zoomTo(zoomFactor-1.0f);
        else
            zoomTo(zoomFactor-0.125f);
    }

    // implementing ImageObserver
    private void zoomTo(float zf)
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
    private void showColorTable()
    {
        if (imagePalette == null)
            return;

        String viewName = (String)HDFView.getListOfPaletteView().get(0);

        try {
            Class theClass =  Class.forName(viewName);
            Object[] initargs = {this};
            PaletteView theView = (PaletteView)Tools.newInstance(theClass, initargs);
        } catch (Exception ex) {}
     }

    // implementing ImageObserver
    private void showHistogram()
    {
        Rectangle rec = imageComponent.selectedArea;

        if (isTrueColor) {
            toolkit.beep();
            JOptionPane.showMessageDialog(this,
            "Unsupported operation: unable to draw histogram for true color image.",
            getTitle(),
            JOptionPane.ERROR_MESSAGE);
            return;
        }


        if( rec == null ||
            rec.getWidth()<=0 ||
            rec.getHeight()<= 0)
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
    private void selectAll() throws Exception {
        imageComponent.selectAll();
    }

    // implementing ImageObserver
    private void flip(int direction)
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
    private void rotate(int direction)
    {
        Rotate90Filter filter = new Rotate90Filter(direction);
        changeImageFilter(filter);
    }


    // implementing ImageObserver
    private void contour(int level)
    {
        ImageFilter filter = new ContourFilter(level);

        if (filter == null)
            return;

        changeImageFilter(filter);
    }

    // implementing ImageObserver
    private void brightness(int level)
    {
        ImageFilter filter = new BrightnessFilter(level);

        if (filter == null)
            return;

        changeImageFilter(filter);
    }


    // implementing ImageObserver
    private void setValueVisible(boolean b)
    {
        valueField.setVisible(b);
        validate();
        //updateUI(); //bug !!! on Windows. gives NullPointerException at
        //javax.swing.plaf.basic.BasicInternalFrameUI$BorderListener.mousePressed(BasicInternalFrameUI.java:693)
    }


    /**
     * This method returns true if the specified image has transparent pixels.
     * @param image the image to be check if has alpha.
     * @return true if the image has alpha setting.
     */
    private boolean hasAlpha(Image image)
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
    private void saveImageAs(String type) throws Exception
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

    public void actionPerformed(ActionEvent e)
    {
        Object source = e.getSource();
        String cmd = e.getActionCommand();

        if (cmd.equals("Close")) {
            dispose();  // terminate the application
        }
        else if (cmd.startsWith("Save image as "))
        {
            String filetype = null;
            if (cmd.equals("Save image as jpeg"))
                filetype = FileFormat.FILE_TYPE_JPEG;
            else if (cmd.equals("Save image as tiff"))
                filetype = FileFormat.FILE_TYPE_TIFF;
            else if (cmd.equals("Save image as png"))
                filetype = FileFormat.FILE_TYPE_PNG;

            try { saveImageAs(filetype); }
            catch (Exception ex) {
                    toolkit.beep();
                    JOptionPane.showMessageDialog(this, ex.getMessage(), getTitle(), JOptionPane.ERROR_MESSAGE);
            }
        }
        else if (cmd.equals("Write selection to image"))
        {
            if (getSelectedArea().width <=0 ||
                getSelectedArea().height <= 0)
            {
                JOptionPane.showMessageDialog(
                        this,
                        "Select image area to write.",
                        "HDFView",
                        JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            TreeView treeView = viewer.getTreeView();
            TreeNode node = treeView.findTreeNode(dataset);
            Group pGroup = (Group)((DefaultMutableTreeNode)node.getParent()).getUserObject();
            TreeNode root = dataset.getFileFormat().getRootNode();

            if (root == null)
                return;

            Vector list = new Vector();
            DefaultMutableTreeNode theNode = null;
            Enumeration enum = ((DefaultMutableTreeNode)root).depthFirstEnumeration();
            while(enum.hasMoreElements()) {
                theNode = (DefaultMutableTreeNode)enum.nextElement();
                list.add(theNode.getUserObject());
            }

            NewDatasetDialog dialog = new NewDatasetDialog(
                (JFrame)viewer,
                pGroup,
                list,
                this);
            dialog.show();

            HObject obj = (HObject)dialog.getObject();
            if (obj != null) {
                Group pgroup = dialog.getParentGroup();
                try { treeView.addObject(obj, pgroup); }
                catch (Exception ex) {}
            }
        }
        else if (cmd.equals("Zoom in")) {
            zoomIn();
        }
        else if (cmd.equals("Zoom out")) {
            zoomOut();
        }
        else if (cmd.equals("Edit palette")) {
            showColorTable();
        }
        else if (cmd.equals("Flip horizontal")) {
            flip(FLIP_HORIZONTAL);
        }
        else if (cmd.equals("Flip vertical")) {
            flip(FLIP_VERTICAL);
        }
        else if (cmd.startsWith("Rotate")) {
            if (cmd.equals("Rotate clockwise")) {
                rotate(ROTATE_CW_90);
                rotateCount++;
                if (rotateCount == 4) rotateCount = 0;
            }
            else {
                rotate(ROTATE_CCW_90);
                rotateCount--;
                if (rotateCount == -4) rotateCount = 0;
            }

            int n = rotateRelatedItems.size();
            for (int i=0; i< n; i++) {
                boolean itemState =  (rotateCount == 0);
                ((javax.swing.JComponent)rotateRelatedItems.get(i)).setEnabled(itemState);
            }
        }
        else if (cmd.equals("Show image value")) {
            boolean b = ((JCheckBoxMenuItem)source).getState();
            setValueVisible(b);
        }
        else if (cmd.startsWith("Show animation"))
        {
            new Animation((JFrame)viewer, dataset);
        }
        else if (cmd.startsWith("Animation speed"))
        {
            animationSpeed = Integer.parseInt((cmd.substring(cmd.length()-2)).trim());
        }

        else if (cmd.startsWith("Contour"))
        {
            int level = Integer.parseInt(cmd.substring(cmd.length()-1));
            contour(level);
        }
        else if (cmd.startsWith("Brightness"))
        {
            String strLevel = (String)JOptionPane.showInputDialog(this,
                    "Enter level of brightness [-100, 100]\n-100 is the darkest and 100 is brightest",
                    "Image Brightness",
                    JOptionPane.INFORMATION_MESSAGE, null, null, "0");
            if (strLevel == null)
                return;

            int level = 0;
            try { level = Integer.parseInt(strLevel.trim()); }
            catch (Exception ex) { level = 0; }

            if (level == 0)
                return;
            else if (level > 100 || level < -100) {
                JOptionPane.showMessageDialog(this,
                    "Brightness level must be between -100 and 100: "+level,
                    "Image Brightness",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            brightness(level);
        }
        else if (cmd.equals("Show chart")) {
            showHistogram();
        }
        else if (cmd.equals("First page")) {
            firstPage();
        }
        else if (cmd.equals("Previous page")) {
            previousPage();
        }
        else if (cmd.equals("Next page")) {
            nextPage();
        }
        else if (cmd.equals("Last page")) {
            lastPage();
        }
        else if (cmd.equals("Select all data")) {
            try { selectAll(); }
            catch (Exception ex) {
                toolkit.beep();
                JOptionPane.showMessageDialog((JFrame)viewer,
                        ex,
                        getTitle(),
                        JOptionPane.ERROR_MESSAGE);
            }
        }
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

    // Implementing DataView.
    public HObject getDataObject() {
        return dataset;
    }

    public byte[] getImageByteData() {
        return indexedImageData;
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
    public Rectangle getSelectedArea() {
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

    private class Rotate90Filter extends ImageFilter {
        private ColorModel defaultRGB = ColorModel.getRGBdefault();

        private double coord[] = new double[2];

        private int raster[];
        private int xoffset, yoffset;
        private int srcW, srcH;
        private int dstW, dstH;
        private int direction;

        public Rotate90Filter(int dir) {
            direction = dir;
        }

        public void transform(double x, double y, double[] retcoord) {
            if (direction == ROTATE_CW_90) {
                retcoord[0] =  -y;
                retcoord[1] = x;
            } else {
                retcoord[0] = y;
                retcoord[1] = -x;
            }
        }

        public void itransform(double x, double y, double[] retcoord) {
            if (direction == ROTATE_CCW_90) {
                retcoord[0] =  -y;
                retcoord[1] = x;
            } else {
                retcoord[0] = y;
                retcoord[1] = -x;
            }
        }

        public void transformBBox(Rectangle rect) {
            double minx = Double.POSITIVE_INFINITY;
            double miny = Double.POSITIVE_INFINITY;
            double maxx = Double.NEGATIVE_INFINITY;
            double maxy = Double.NEGATIVE_INFINITY;
            for (int y = 0; y <= 1; y++) {
                for (int x = 0; x <= 1; x++) {
                    transform(rect.x + x * rect.width, rect.y + y * rect.height, coord);
                    minx = Math.min(minx, coord[0]);
                    miny = Math.min(miny, coord[1]);
                    maxx = Math.max(maxx, coord[0]);
                    maxy = Math.max(maxy, coord[1]);
                }
            }
            rect.x = (int) Math.floor(minx);
            rect.y = (int) Math.floor(miny);
            rect.width = (int) Math.ceil(maxx) - rect.x + 1;
            rect.height = (int) Math.ceil(maxy) - rect.y + 1;
        }

        public void setDimensions(int width, int height) {
            Rectangle rect = new Rectangle(0, 0, width, height);
            transformBBox(rect);
            xoffset = -rect.x;
            yoffset = -rect.y;
            srcW = width;
            srcH = height;
            dstW = rect.width;
            dstH = rect.height;
            raster = new int[srcW * srcH];
            consumer.setDimensions(dstW, dstH);
        }

        public void setProperties(Hashtable props) {
            props = (Hashtable) props.clone();
            Object o = props.get("filters");
            if (o == null) {
                props.put("filters", toString());
            } else if (o instanceof String) {
                props.put("filters", ((String) o)+toString());
            }
            consumer.setProperties(props);
        }

        public void setColorModel(ColorModel model) {
            consumer.setColorModel(defaultRGB);
        }

        public void setHints(int hintflags) {
            consumer.setHints(TOPDOWNLEFTRIGHT
                | COMPLETESCANLINES
                | SINGLEPASS
                | (hintflags & SINGLEFRAME));
        }

        public void setPixels(int x, int y, int w, int h, ColorModel model, byte pixels[], int off, int scansize) {
            int srcoff = off;
            int dstoff = y * srcW + x;
            for (int yc = 0; yc < h; yc++) {
                for (int xc = 0; xc < w; xc++) {
                    raster[dstoff++] = model.getRGB(pixels[srcoff++] & 0xff);
                }
                srcoff += (scansize - w);
                dstoff += (srcW - w);
            }
        }

        public void setPixels(int x, int y, int w, int h, ColorModel model, int pixels[], int off, int scansize) {
            int srcoff = off;
            int dstoff = y * srcW + x;
            if (model == defaultRGB) {
                for (int yc = 0; yc < h; yc++) {
                    System.arraycopy(pixels, srcoff, raster, dstoff, w);
                    srcoff += scansize;
                    dstoff += srcW;
                }
            } else {
                for (int yc = 0; yc < h; yc++) {
                    for (int xc = 0; xc < w; xc++) {
                        raster[dstoff++] = model.getRGB(pixels[srcoff++]);
                    }
                    srcoff += (scansize - w);
                    dstoff += (srcW - w);
                }
            }
        }

        public void imageComplete(int status) {
            if (status == IMAGEERROR || status == IMAGEABORTED) {
                consumer.imageComplete(status);
                return;
            }
            int pixels[] = new int[dstW];
            for (int dy = 0; dy < dstH; dy++) {
                itransform(0 - xoffset, dy - yoffset, coord);
                double x1 = coord[0];
                double y1 = coord[1];
                itransform(dstW - xoffset, dy - yoffset, coord);
                double x2 = coord[0];
                double y2 = coord[1];
                double xinc = (x2 - x1) / dstW;
                double yinc = (y2 - y1) / dstW;
                for (int dx = 0; dx < dstW; dx++) {
                    int sx = (int) Math.round(x1);
                    int sy = (int) Math.round(y1);
                    if (sx < 0 || sy < 0 || sx >= srcW || sy >= srcH) {
                        pixels[dx] = 0;
                    } else {
                        pixels[dx] = raster[sy * srcW + sx];
                    }
                    x1 += xinc;
                    y1 += yinc;
                }
                consumer.setPixels(0, dy, dstW, 1, defaultRGB, pixels, 0, dstW);
            }
            consumer.imageComplete(status);
        }
    } // private class RotateFilter

    /**
     * Makes animaion for 3D images.
     */
    private class Animation extends JDialog implements ActionListener, Runnable
    {
        private final int MAX_IMAGE_SIZE = 300;

        private Image[] frames = null; // a list of images for animation
        private JComponent canvas = null; // canvas to draw the image
        private Thread engine = null; // Thread animating the images
        private int numberOfImages = 0;
        private int currentFrame = 0;
        private int sleepTime = 500;
        private Image offScrImage;  // Offscreen image
        private Graphics offScrGC; // Offscreen graphics context
        private JFrame owner;
        private int x0=0, y0=0; // offset of the image drawing

        public Animation(JFrame theOwner, ScalarDS dataset) {
            super(theOwner, "Animation", true);
            owner = theOwner;
            setDefaultCloseOperation(JInternalFrame.DISPOSE_ON_CLOSE);

            long[] dims = dataset.getDims();
            long[] stride = dataset.getStride();
            long[] start = dataset.getStartDims();
            long[] selected = dataset.getSelectedDims();
            int[] selectedIndex = dataset.getSelectedIndex();
            int rank = dataset.getRank();
            if (animationSpeed != 0)
                sleepTime = 1000/animationSpeed;

            // back up the sart and selected size
            long[] tstart = new long[rank];
            long[] tselected = new long[rank];
            long[] tstride = new long[rank];
            System.arraycopy(start, 0, tstart, 0, rank);
            System.arraycopy(selected, 0, tselected, 0, rank);
            System.arraycopy(stride, 0, tstride, 0, rank);

            int stride_n = 1;
            int max_size = (int)Math.max(selected[selectedIndex[0]], selected[selectedIndex[1]]);
            if (max_size > MAX_IMAGE_SIZE) {
                stride_n = max_size/MAX_IMAGE_SIZE;
            }

            start[selectedIndex[0]] = 0;
            start[selectedIndex[1]] = 0;
            start[selectedIndex[2]] = 0;
            selected[selectedIndex[0]] = dims[selectedIndex[0]]/stride_n;
            selected[selectedIndex[1]] = dims[selectedIndex[1]]/stride_n;
            selected[selectedIndex[2]] = 1;
            stride[selectedIndex[0]] = stride_n;
            stride[selectedIndex[1]] = stride_n;
            stride[selectedIndex[2]] = 1;

            Object data3d = null;
            byte[] byteData = null;
            int h = (int)selected[selectedIndex[0]];
            int w = (int)selected[selectedIndex[1]];
            numberOfImages = (int)dims[selectedIndex[2]];
            frames = new Image[numberOfImages];
            int size = w*h;
            try {
                for (int i=0; i<numberOfImages; i++) {
                    start[selectedIndex[2]] = i;
                    try { data3d = dataset.read(); }
                    catch (Throwable err) { continue;}
                    byteData = Tools.getBytes(data3d, dataRange);
                    frames[i] = createIndexedImage(byteData, imagePalette, w, h);
                }
            } finally {
                // set back to original state
                System.arraycopy(tstart, 0, start, 0, rank);
                System.arraycopy(tselected, 0, selected, 0, rank);
                System.arraycopy(tstride, 0, stride, 0, rank);
            }

            offScrImage = owner.createImage(w, h);
            offScrGC = offScrImage.getGraphics();
            x0 = Math.max((MAX_IMAGE_SIZE-w)/2, 0);
            y0 = Math.max((MAX_IMAGE_SIZE-h)/2, 0);

            canvas = new JComponent() {
                public void paint(Graphics g) {
                    if (offScrGC == null || frames==null)
                        return;
                    offScrGC.drawImage(frames[currentFrame],0,0,owner);
                    g.drawImage(offScrImage,x0,y0,owner);
                }
            };

            JPanel contentPane = (JPanel)getContentPane();
            contentPane.add(canvas);
            contentPane.setPreferredSize(new Dimension(MAX_IMAGE_SIZE, MAX_IMAGE_SIZE));

            Point l = getParent().getLocation();
            Dimension d = getParent().getPreferredSize();

            l.x += 300;
            l.y += 200;
            setLocation(l);

            start();

            pack();
            show();
        }

        public void actionPerformed(ActionEvent e)
        {
            Object source = e.getSource();
            String cmd = e.getActionCommand();

            if (cmd.equals("Close animation")) {
                this.dispose();  // terminate the animation
            }
        }

        public void dispose() {
            super.dispose();
            engine = null;
            frames = null;
        }

        /**
         * No need to clear anything; just paint.
         */
        public void update(Graphics g) {
            paint(g);
        }

        /**
         * Paint the current frame
         */
        public void paint(Graphics g) {
            canvas.paint(g);
        }

        /**
         * Run the animation. This method is called by class Thread.
         * @see java.lang.Thread
         */
        public void run() {
            Thread me = Thread.currentThread();

            if (frames == null || canvas == null)
                return;

            while (me == engine) {

                synchronized(this) {
                    repaint();

                    // Pause for duration or longer if user paused
                    try {
                        Thread.sleep(sleepTime);
                    } catch (InterruptedException e) {}

                    currentFrame++;
                    if (currentFrame >= numberOfImages)
                        currentFrame = 0;
                }



            }
        }

        /**
         * Start the applet by forking an animation thread.
         */
        private void start() {
            engine = new Thread(this);
            engine.start();
        }



    } // private class Animation extends JDialog
}