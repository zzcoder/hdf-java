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
import java.awt.event.*;
import javax.swing.*;
import java.awt.image.*;
import java.awt.Color;
import java.awt.Image;
import java.awt.Point;
import java.awt.Dimension;
import java.awt.Window;
import java.awt.BorderLayout;
import java.awt.Toolkit;
import java.awt.Graphics;

/**
 * To view and change palette.
 */
public class DefaultPaletteView extends JDialog
implements PaletteView, MouseListener, MouseMotionListener, ActionListener
{
    private JRadioButton checkRed, checkGreen, checkBlue;
    /** Panel that draws plot of data values. */
    private ChartPanel chartP;
    private int x0, y0; // starting point of mouse drag
    private Image originalImage, currentImage;
    boolean isPaletteChanged = false;
    byte[][] palette;
    private ScalarDS dataset;
    private ImageView imageView;
    private int[][] paletteData;

    private final Color[] lineColors = {Color.red, Color.green, Color.blue};
    private final String lineLabels[] ={"Red", "Green", "Blue"};

    public DefaultPaletteView(ImageView theImageView)
    {
        super();
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        imageView = theImageView;
        dataset = (ScalarDS)imageView.getDataObject();
        setModal(true);

        chartP = new ChartPanel();
        chartP.setBackground(Color.white);

        paletteData = new int[3][256];
        byte[][] imagePalette = imageView.getPalette();
        int[] xRange = {0, 255};
        double[] yRange = {0, 255};
        this.setTitle("Image Palette - "+dataset.getPath()+dataset.getName());

        int d = 0;
        for (int i=0; i<3; i++)
        {
            for (int j=0; j<256; j++)
            {
                d = (int)imagePalette[i][j];
                if (d < 0) d += 256;
                paletteData[i][j] = d;
            }
        }

        imageView = theImageView;
        chartP.addMouseListener(this);
        chartP.addMouseMotionListener(this);

        x0 = y0 = 0;
        originalImage = currentImage = imageView.getImage();
        palette = new byte[3][256];

        createUI();
        show();
    }

    /** returns the data object displayed in this data viewer */
    public HObject getDataObject() {
        return dataset;
    }

    /**
    *  Creates and layouts GUI componentes.
    */
    private void createUI()
    {
        Window owner = getOwner();

        JPanel contentPane = (JPanel)getContentPane();
        contentPane.setLayout(new BorderLayout(5, 5));
        contentPane.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
        contentPane.setPreferredSize(new Dimension(640, 400));

        contentPane.add(chartP, BorderLayout.CENTER);

        JButton button = new JButton("  Ok  ");
        button.addActionListener(this);
        button.setActionCommand("Ok");
        JPanel buttonP = new JPanel();
        buttonP.add(button);
        button = new JButton("Cancel");
        button.addActionListener(this);
        button.setActionCommand("Cancel");
        buttonP.add(button);
        button = new JButton("Preview");
        button.addActionListener(this);
        button.setActionCommand("Preview");
        buttonP.add(button);

        JPanel bottomP = new JPanel();
        bottomP.setLayout(new BorderLayout());
        bottomP.add(buttonP, BorderLayout.EAST);

        checkRed = new JRadioButton("Red");
        checkRed.setForeground(Color.red);
        checkGreen = new JRadioButton("Green");
        checkGreen.setForeground(Color.green);
        checkBlue = new JRadioButton("Blue");
        checkBlue.setForeground(Color.blue);
        checkRed.setSelected(true);
        ButtonGroup bgroup = new ButtonGroup();
        bgroup.add(checkRed);
        bgroup.add(checkGreen);
        bgroup.add(checkBlue);
        JPanel checkP = new JPanel();
        checkP.add(checkRed);
        checkP.add(checkGreen);
        checkP.add(checkBlue);
        bottomP.add(checkP, BorderLayout.WEST);

        contentPane.add(bottomP, BorderLayout.SOUTH);

        Point l = owner.getLocation();
        l.x += 250;
        l.y += 200;
        setLocation(l);
        pack();
    }

    public void actionPerformed(ActionEvent e)
    {
        String cmd = e.getActionCommand();

        if (cmd.equals("Ok"))
        {
            if (isPaletteChanged)
            {
                this.updatePalette();
                isPaletteChanged = false;
                imageView.setImage(currentImage);
                imageView.setPalette(palette);
            }
            super.dispose();
        }
        else if (cmd.equals("Cancel"))
        {
            imageView.setImage(originalImage);
            super.dispose();
        }
        if (cmd.equals("Preview"))
        {
            this.updatePalette();
            imageView.setImage(currentImage);
        }
    }

    private void updatePalette()
    {
        for (int i=0; i<256; i++)
        {
            palette[0][i] = (byte)paletteData[0][i];
            palette[1][i] = (byte)paletteData[1][i];
            palette[2][i] = (byte)paletteData[2][i];
        }

        IndexColorModel colorModel = new IndexColorModel (
            8,           // bits - the number of bits each pixel occupies
            256,         // size - the size of the color component arrays
            palette[0],  // r - the array of red color components
            palette[1],  // g - the array of green color components
            palette[2]); // b - the array of blue color components

        int w = dataset.getWidth();
        int h = dataset.getHeight();
        currentImage = Toolkit.getDefaultToolkit().createImage (new MemoryImageSource
            (w, h, colorModel, imageView.getImageByteData(), 0, w));
    }

    public void mouseClicked(MouseEvent e){} // MouseListener
    public void mouseReleased(MouseEvent e) {} // MouseListener
    public void mouseEntered(MouseEvent e) {} // MouseListener
    public void mouseExited(MouseEvent e)  {} // MouseListener
    public void mouseMoved(MouseEvent e) {} // MouseMotionListener

    // implementing MouseListener
    public void mousePressed(MouseEvent e)
    {
        //x0 = e.getX()-40; // takes the horizontal gap
        //if (x0 < 0) x0 = 0;
        //y0 = e.getY()+20;
    }

    // implementing MouseMotionListener
    public void mouseDragged(MouseEvent e)
    {
        int x1 = e.getX()-40;// takes the vertical gap
        if (x1< 0) x1 = 0;
        int y1 = e.getY()+20;

        Dimension d = chartP.getSize();
        double ry = 255/(double)d.height;
        double rx = 255/(double)d.width;

        int lineIdx = 0;
        if (checkGreen.isSelected())
            lineIdx = 1;
        else if (checkBlue.isSelected())
            lineIdx = 2;

        int idx = 0;
        double b = (double)(y1-y0)/(double)(x1-x0);
        double a = y0-b*x0;
        double value = y0*ry;
        int i0 = Math.min(x0, x1);
        int i1 =  Math.max(x0, x1);
        for (int i=i0; i<i1; i++)
        {
            idx = (int)(rx*i);
            if (idx > 255) continue;
            value = 255-(a+b*i)*ry;
            if (value < 0) value = 0;
            else if (value > 255) value = 255;
            paletteData[lineIdx][idx] = (int)value;
        }

        chartP.repaint();
        isPaletteChanged = true;
    }

    /** The canvas that paints the data lines. */
    public final class ChartPanel extends JComponent
    {
        /**
        * Paints the plot components.
        */
        public void paint(Graphics g)
        {
            Dimension d = getSize();
            int gap = 20;
            int legendSpace = 60;
            int h = d.height - gap;
            int w = d.width - 3*gap - legendSpace;

            // draw the X axis
            g.drawLine(2*gap, h, w+2*gap, h);

            // draw the Y axis
            g.drawLine(2*gap, h, 2*gap, 0);

            // draw X and Y labels: 10 labels for x and y
            int dh = h/10;
            int dw = w/10;
            int dx = 25;
            double dy = 25;
            int xp=2*gap, yp=0, x=0, x0, y0, x1, y1;
            double y = 0;

            // draw X and Y grid labels
            g.drawString(String.valueOf((int)y), 0, h+8);
            g.drawString(String.valueOf(x), xp-5, h+gap);
            for (int i=0; i<10; i++)
            {
                xp += dw;
                yp += dh;
                x += dx;
                y += dy;
                g.drawLine(xp, h, xp, h-5);
                g.drawLine(2*gap, h-yp, 2*gap+5, h-yp);
                g.drawString(String.valueOf((int)y), 0, h-yp+8);
                g.drawString(String.valueOf(x), xp-5, h+gap);
            }

            Color c = g.getColor();
            for (int i=0; i<3; i++) {
                g.setColor(lineColors[i]);

                // set up the line data for drawing one line a time
                for (int j=0; j<255; j++)
                {
                    x0 = (int)(w*j/255) + 2*gap;
                    y0 = (int)(h - h*paletteData[i][j]/255);
                    x1 = (int)(w*(j+1)/255) + 2*gap;
                    y1 = (int)(h - h*(paletteData[i][j+1])/255);
                    g.drawLine(x0, y0, x1, y1);
                }

                x0 = w+legendSpace;
                y0 = gap+gap*i;
                g.drawLine(x0, y0, x0+7, y0);
                g.drawString(lineLabels[i], x0+10, y0+3);
            }

            g.setColor(c); // set the color back to its default

            // draw a box on the legend
            g.drawRect(w+legendSpace-10, 10, legendSpace, 10*gap);
        } // public void paint(Graphics g)

    } // private class ChartPanel extends Canvas

} // private class PaletteView extends ChartView

