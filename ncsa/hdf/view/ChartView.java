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

import java.awt.event.*;
import javax.swing.*;
import java.lang.reflect.Array;
import java.awt.Color;
import java.awt.Frame;
import java.awt.Window;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Dimension;
import java.awt.BorderLayout;

/**
 * ChartView displays histogram/line chart of selected row/column of table
 * data or image. There are two types of chart, histogram and line plot.
 * <p>
 * @version 1.3.0 02/07/2002
 * @author Peter X. Cao
 */
public class ChartView extends JDialog
implements ActionListener
{
    /** histogram style chart */
    public static final int HISTOGRAM = 0;

    /** line style chart */
    public static final int LINEPLOT = 1;

    /** The default colors of lines for selected columns */
    public static final Color[] LINE_COLORS = {
        Color.black, Color.red, Color.green.darker(), Color.blue, Color.magenta,
        Color.pink, Color.yellow, Color.orange, Color.gray, Color.cyan};

    /** the data values of line points or histogram */
    protected double data[][];

    /** Panel that draws plot of data values. */
    protected ChartPanel chartP;

    /** number of data points */
    protected int numberOfPoints;

    /** the style of chart: histogram or line */
    private int chartStyle;

    /** the text label of Y axis */
    private String ylabel;

    /** the text label of X axis */
    private String xlabel;

    /** the maximum value of the Y axis */
    private double ymax;

    /** the minumum value of the Y axis */
    private double ymin;

    /** the maximum value of the X axis */
    private int xmax;

    /** the minumum value of the X axis */
    private int xmin;

    /** line labels */
    private String lineLabels[];

    /** line colors */
    private Color lineColors[];

    /** maximum width of the x labels */
    private int xwidth;

    /** number of lines */
    private int numberOfLines;

    /**
     * True if the original data is integer (byte, short, integer, long).
     */
    private boolean isInteger;

    /**
     *  Constructs a new ChartView given data and data ranges.
     *  <p>
     *  @param owner the owner frame of this dialog.
     *  @parem title the title of this dialog.
     *  @param style the style of the chart. Legal values are:
     *  @param data the two dimensional data array: data[linenumber][datapoints]
     *  @param xRange the range of the X values, xRange[0]=xmin, xRange[1]=xmax.
     *  @param yRange the range of the Y values, yRange[0]=ymin, yRange[1]=ymax.
     *  <ul>
     *      <li>ncsa.hdf.view.ChartView.HISTOGRAM
     *      <li>ncsa.hdf.view.ChartView.LINE
     *  </ul>
     */
    public ChartView (Frame owner, String title, int style,
        double[][] data, int[] xRange, double[] yRange)
    {
        super(owner, title, false);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        if (data == null)
            return;

        this.chartStyle = style;
        this.data = data;

        if (style == HISTOGRAM)
            isInteger = true;
        else
            isInteger = false;

        if (xRange != null)
        {
            this.xmin = xRange[0];
            this.xmax = xRange[1];
        }
        else
        {
            this.xmin = 0;
            this.xmax = data[0].length-1;
        }

        this.xwidth = Math.max(String.valueOf(xmin).length(),
            String.valueOf(xmax).length()*getFont().getSize());

        this.xlabel = "X";
        this.ylabel = "Y";
        this.numberOfLines = Array.getLength(data);
        this.numberOfPoints = Array.getLength(data[0]);
        this.lineColors = LINE_COLORS;

        if (yRange != null)
        {
            // data range is given
            this.ymin = yRange[0];
            this.ymax = yRange[1];
        }
        else
        {
            // search data range from the data
            findDataRange();
        }

        chartP = new ChartPanel();
        chartP.setBackground(Color.white);

        createUI();
    }

    /**
     *  Creates and layouts GUI componentes.
     */
    protected void createUI()
    {
        Window owner = getOwner();

        JPanel contentPane = (JPanel)getContentPane();
        contentPane.setLayout(new BorderLayout(5, 5));
        contentPane.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
        contentPane.setPreferredSize(new Dimension(640, 400));

        contentPane.add(chartP, BorderLayout.CENTER);

        JButton button = new JButton("Close");
        button.addActionListener(this);
        button.setActionCommand("Close");
        JPanel tmp = new JPanel();
        tmp.add(button);
        contentPane.add(tmp, BorderLayout.SOUTH);

        Point l = owner.getLocation();
        l.x += 220;
        l.y += 100;
        setLocation(l);
        pack();
    }

    public void actionPerformed(ActionEvent e)
    {
        String cmd = e.getActionCommand();

        if (cmd.equals("Close"))
        {
            dispose();
        }
    }

    /** Sets the color of each line of a line plot */
    public void setLineColors(Color c[]) { lineColors = c; }

    /** Sets the labels of each line. */
    public void setLineLabels(String l[]) { lineLabels = l; }

    /** Sets the label of the X axis. */
    public void setXlabel(String label) { xlabel = label; }

    /** Sets the label of the Y axis. */
    public void setYlabel (String label ) { ylabel = label; }

    /** Set the data type of the plot data to be integer. */
    public void setTypeToInteger() { isInteger = true; }

    /** find and set the minimum and maximum values of the data */
    private void findDataRange()
    {
        if (data == null)
            return;

        ymin = ymax = data[0][0];
        for (int i=0; i<numberOfLines; i++)
        {
            for (int j=0; j<numberOfPoints; j++)
            {
                if (data[i][j] < ymin)
                    ymin = data[i][j];

                if (data[i][j] > ymax)
                    ymax = data[i][j];
            }
        }
    }

    /** The canvas that paints the data lines. */
    public final class ChartPanel extends JComponent
    {
        /**
        * Paints the plot components.
        */
        public void paint(Graphics g)
        {
            if ( numberOfLines <= 0 )
                return; // no data

            Dimension d = getSize();
            int gap = 20;
            int legendSpace = 0;
            if (chartStyle == LINEPLOT && lineLabels != null)
                legendSpace = 60;

            int h = d.height - gap;
            int w = d.width - 3*gap - legendSpace;

            // draw the X axis
            g.drawLine(2*gap, h, w+2*gap, h);

            // draw the Y axis
            g.drawLine(2*gap, h, 2*gap, 0);

            // draw X and Y labels: 10 labels for x and y
            int dh = h/10;
            int dw = w/10;
            int dx = Math.round((float)(xmax - xmin + 0.5) / 10);
            double dy = (ymax - ymin) / 10;
            if (dy > 1) dy = Math.round(10*dy)/10;
            int xp=2*gap, yp=0, x=xmin, x0, y0, x1, y1;
            double y = ymin;

            // draw X and Y grid labels
            if (isInteger)
                g.drawString(String.valueOf((int)y), 0, h+8);
            else
                g.drawString(String.valueOf((float)y), 0, h+8);

            g.drawString(String.valueOf(x), xp-5, h+gap);
            for (int i=0; i<10; i++)
            {
                xp += dw;
                yp += dh;
                x += dx;
                y += dy;
                g.drawLine(xp, h, xp, h-5);
                g.drawLine(2*gap, h-yp, 2*gap+5, h-yp);
                if (isInteger)
                    g.drawString(String.valueOf((int)y), 0, h-yp+8);
                else
                    g.drawString(String.valueOf((float)y), 0, h-yp+8);
                g.drawString(String.valueOf(x), xp-5, h+gap);
            }

            Color c = g.getColor();
            if (chartStyle == LINEPLOT)
            {
                // draw lines for selected spreadsheet columns

                for (int i=0; i<numberOfLines; i++)
                {
                    if (lineColors != null && lineColors.length>= numberOfLines)
                        g.setColor(lineColors[i]);

                    // set up the line data for drawing one line a time
                    for (int j=0; j<numberOfPoints-1; j++)
                    {
                        x0 = (int)(w*(j - xmin)/(xmax-xmin)) + 2*gap;
                        y0 = (int)(h - h*(data[i][j]-ymin)/(ymax-ymin));
                        x1 = (int)(w*(j+1 - xmin)/(xmax-xmin)) + 2*gap;
                        y1 = (int)(h - h*(data[i][j+1]-ymin)/(ymax-ymin));
                        g.drawLine(x0, y0, x1, y1);
                    }

                    // draw line legend
                    if (lineLabels != null && lineLabels.length>= numberOfLines)
                    {
                        x0 = w+legendSpace;
                        y0 = gap+gap*i;
                        g.drawLine(x0, y0, x0+7, y0);
                        g.drawString(lineLabels[i], x0+10, y0+3);
                    }
                }

                g.setColor(c); // set the color back to its default

                // draw a box on the legend
                if (lineLabels != null && lineLabels.length>= numberOfLines)
                    g.drawRect(w+legendSpace-10, 10, legendSpace, 10*gap);

            } // if (chartStyle == LINEPLOT)
            else if (chartStyle == HISTOGRAM)
            {
                // draw histogram for selected image area
                xp=2*gap;
                yp=0;
                g.setColor(Color.blue);
                int barWidth = w/numberOfPoints;
                if (barWidth <=0 ) barWidth = 1;

                for (int j=0; j<numberOfPoints; j++)
                {
                    xp = 2*gap+(int)(w*(j - xmin)/(xmax-xmin));
                    yp = h-(int)(h*(data[0][j]-ymin)/(ymax-ymin));
                    g.drawLine(xp, h, xp, yp);
                    g.drawLine(xp+1, h, xp+1, yp);
                    g.drawLine(xp+2, h, xp+2, yp);
                }

                g.setColor(c); // set the color back to its default
            } // else if (chartStyle == HISTOGRAM)
        } // public void paint(Graphics g)
    } // private class ChartPanel extends Canvas

}
