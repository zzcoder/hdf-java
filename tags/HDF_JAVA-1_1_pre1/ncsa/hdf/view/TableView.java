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
import javax.swing.table.*;
import javax.swing.event.*;
import javax.swing.border.*;
import java.awt.datatransfer.*;
import java.util.*;
import java.io.*;
import java.lang.reflect.Array;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.MouseEvent;
import java.awt.Toolkit;
import javax.swing.plaf.metal.MetalBorders.TableHeaderBorder;

/**
 * TableView displays an HDF dataset as a two-dimensional table.
 * <p>
 * @version 1.3.0 1/28/2002
 * @author Peter X. Cao
 */
public class TableView extends JInternalFrame
implements TableObserver
{
    /**
     * The main HDFView.
     */
    private final ViewManager viewer;

    /** numberical data type.
      B = bypte array,
      S = short array,
      I = inte array,
      J = long array,
      F = float array, and
      D = double array.
     */
    private char NT = ' ';

    /**
     * The Scalar Dataset.
     */
    private Dataset dataset;

    /**
     * The value of the dataset.
     */
    private Object dataValue;

    /**
     * The table used to hold the table data.
     */
    private JTable table;

    /** Label to indicate the current cell location. */
    private JLabel cellLabel;

    /** Text field to display the value of of the current cell. */
    private JTextArea cellValueField;

    /**
     * The title of this imageview.
     */
    private String frameTitle;

    private boolean isValueChanged;

    private final Toolkit toolkit;

    /**
     * Constructs an TableView.
     * <p>
     * @param theView the main HDFView.
     */
    public TableView(ViewManager theView)
    {
        super();

        viewer = theView;
        toolkit = Toolkit.getDefaultToolkit();
        isValueChanged = false;

        HObject hobject = (HObject)viewer.getSelectedObject();
        if (hobject == null || !(hobject instanceof Dataset))
        {
             return;
        }

        dataset = (Dataset)hobject;
        String fname = new java.io.File(hobject.getFile()).getName();
        frameTitle = "TableView - "+fname+" - " +hobject.getPath()+hobject.getName();

        this.setDefaultCloseOperation(JInternalFrame.DISPOSE_ON_CLOSE);
        this.setTitle(frameTitle);

        // create the table and its columnHeader
        if (dataset instanceof ScalarDS)
        {
            this.setFrameIcon(ViewProperties.getDatasetIcon());
            table = createTable((ScalarDS)dataset);
        }
        else if (dataset instanceof CompoundDS)
        {
            this.setFrameIcon(ViewProperties.getTableIcon());
            table = createTable((CompoundDS)dataset);
        }

        if (table == null)
        {
            viewer.showStatus("Creating table failed - "+dataset.getName());
            return;
        }

        table.setCellSelectionEnabled(true);
        table.setAutoResizeMode( JTable.AUTO_RESIZE_OFF );
        table.setTableHeader(new ColumnHeader(table));

        // add the table to a scroller
        JScrollPane scroller = new JScrollPane(table);
        scroller.getVerticalScrollBar().setUnitIncrement(50);
        scroller.getHorizontalScrollBar().setUnitIncrement(50);

        // create row headers and add it to the scroller
        RowHeader rowHeaders = new RowHeader( table );
        JViewport viewp = new JViewport();
        viewp.add( rowHeaders );
        viewp.setPreferredSize( rowHeaders.getPreferredSize() );
        scroller.setRowHeader( viewp );

        cellLabel = new JLabel("");
        Dimension dim = cellLabel.getPreferredSize();
        dim.width = 60;
        cellLabel.setPreferredSize( dim );
        cellLabel.setHorizontalAlignment(JLabel.RIGHT);
        cellValueField = new JTextArea();
        cellValueField.setLineWrap(true);
        cellValueField.setWrapStyleWord(true);
        cellValueField.setEditable(false);
        JPanel valuePane = new JPanel();
        valuePane.setLayout(new BorderLayout());
        valuePane.add(cellLabel, BorderLayout.WEST);
        valuePane.add (new JScrollPane(cellValueField), BorderLayout.CENTER);

        // add to the main panel
        JPanel contentPane = (JPanel)getContentPane();
        contentPane.setLayout(new BorderLayout());
        contentPane.add(valuePane, BorderLayout.SOUTH);
        contentPane.add (scroller, BorderLayout.CENTER);

        // reset the title for 3D dataset
        if (dataset.getRank() > 2)
        {
            long[] start = dataset.getStartDims();
            int[] selectedIndex = dataset.getSelectedIndex();
            long[] dims = dataset.getDims();
            setTitle( frameTitle+ " - Page "+String.valueOf(start[selectedIndex[2]]+1)+ " of "+dims[selectedIndex[2]]);
        }
    }

    public void dispose()
    {
        if (isValueChanged) updateValueInFile();
        viewer.contentFrameWasRemoved(getName());
        super.dispose();
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

        long[] start = dataset.getStartDims();
        long[] dims = dataset.getDims();
        int[] selectedIndex = dataset.getSelectedIndex();
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

        long[] start = dataset.getStartDims();
        int[] selectedIndex = dataset.getSelectedIndex();
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

        long[] start = dataset.getStartDims();
        int[] selectedIndex = dataset.getSelectedIndex();
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

        long[] start = dataset.getStartDims();
        int[] selectedIndex = dataset.getSelectedIndex();
        long[] dims = dataset.getDims();
        long idx = start[selectedIndex[2]];
        if (idx == dims[selectedIndex[2]]-1)
            return; // current page is the last page

        gotoPage(dims[selectedIndex[2]]-1);
    }

    // Implementing TableObserver.
    public JTable getTable()
    {
        return table;
    }

    // Implementing TableObserver.
    public void showLineplot()
    {
        int[] rows = table.getSelectedRows();
        int[] cols = table.getSelectedColumns();

        if (rows == null ||
            cols == null ||
            rows.length <=0 ||
            cols.length  <=0 )
            return;

        int nrow = table.getRowCount();
        int ncol = table.getColumnCount();

        // figure out to plot data by row or by column
        // Plot data by rows if all columns are selected and part of
        // rows are selected, otherwise plot data by column
        double[][] data = null;
        int nLines = 0;
        int[] xRange = {0, 0};
        String title = "Lineplot - "+dataset.getPath()+dataset.getName();
        String[] lineLabels = null;
        double[] yRange = {Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY};
        boolean isRowPlot = (rows.length<nrow && cols.length==ncol);
        if (isRowPlot)
        {
            title +=" - by row";
            nLines = rows.length;
            if (nLines > 10)
            {
                toolkit.beep();
                nLines = 10;
                JOptionPane.showMessageDialog(this,
                "More than 10 rows are selected.\n"+
                "The first 10 rows will be displayed.",
                getTitle(),
                JOptionPane.WARNING_MESSAGE);
            }
            lineLabels = new String[nLines];
            data = new double[nLines][ncol];
            xRange[1] = ncol-1;
            for (int i=0; i<nLines; i++)
            {
                lineLabels[i] = "Row"+String.valueOf(rows[i]+1);
                for (int j=0; j<ncol; j++)
                {
                    try {
                        data[i][j] = Double.parseDouble(
                        table.getValueAt(rows[i], cols[j]).toString());
                        yRange[0] = Math.min(yRange[0], data[i][j]);
                        yRange[1] = Math.max(yRange[1], data[i][j]);
                    } catch (NumberFormatException ex)
                    {
                        data[i][j] = 0;
                    }
                } // for (int j=0; j<ncols; j++)
            } // for (int i=0; i<rows.length; i++)
        } // if (isRowPlot)
        else
        {
            title +=" - by column";
            nLines = cols.length;
            if (nLines > 10)
            {
                toolkit.beep();
                nLines = 10;
                JOptionPane.showMessageDialog(this,
                "More than 10 columns are selected.\n"+
                "The first 10 columns will be displayed.",
                getTitle(),
                JOptionPane.WARNING_MESSAGE);
            }
            lineLabels = new String[nLines];
            data = new double[nLines][rows.length];
            xRange[1] = rows.length-1;
            for (int j=0; j<nLines; j++)
            {
                lineLabels[j] = table.getColumnName(cols[j]);
                for (int i=0; i<rows.length; i++)
                {
                    try {
                        data[j][i] = Double.parseDouble(
                        table.getValueAt(rows[i], cols[j]).toString());
                        yRange[0] = Math.min(yRange[0], data[j][i]);
                        yRange[1] = Math.max(yRange[1], data[j][i]);
                    } catch (NumberFormatException ex)
                    {
                        data[j][i] = 0;
                    }
                } // for (int j=0; j<ncols; j++)
            } // for (int i=0; i<rows.length; i++)
        } // else

        if (yRange[0] == Double.POSITIVE_INFINITY ||
            yRange[1] == Double.NEGATIVE_INFINITY ||
            yRange[0] == yRange[1])
        {
            toolkit.beep();
            JOptionPane.showMessageDialog(
                this,
                "Cannot show line plot for the selected data. \n"+
                "Please check the data range.",
                getTitle(),
                JOptionPane.ERROR_MESSAGE);
            data = null;
            return;
        }

        ChartView cv = new ChartView(
            (Frame)viewer,
            title,
            ChartView.LINEPLOT,
            data,
            xRange,
            yRange);
        cv.setLineLabels(lineLabels);

        String cname = dataValue.getClass().getName();
        char dname = cname.charAt(cname.lastIndexOf("[")+1);
        if (dname == 'B' || dname == 'S' || dname == 'I' || dname == 'J')
            cv.setTypeToInteger();

        cv.show();
    }

    /**
     * Creates a JTable to hold a scalar dataset.
     */
    private JTable createTable(ScalarDS d)
    {
        JTable theTable = null;
        int rows=0, cols=0;

        int rank = d.getRank();
        if (rank <= 0)
        {
            d.init();
            rank = d.getRank();
        }
        long[] dims = d.getSelectedDims();

        // put one-dimensional data at one column table
        if (rank == 1)
        {
            rows = (int)dims[0];
            cols = 1;
        }
        else
        {
            rows = d.getHeight();
            cols = d.getWidth();
        }

        dataValue = null;
        try {
            d.read();
            d.convertFromUnsignedC();
            dataValue = d.read();
        }
        catch (Exception ex)
        {
            JOptionPane.showMessageDialog(
                this,
                ex,
                getTitle(),
                JOptionPane.ERROR_MESSAGE);
            dataValue = null;
        }

        if (dataValue == null)
            return null;

        String cName = dataValue.getClass().getName();
        int cIndex = cName.lastIndexOf("[");
        if (cIndex >= 0 ) NT = cName.charAt(cIndex+1);

        String columnNames[] = new String[cols];
        for (int i=0; i<cols; i++)
        {
            columnNames[i] = String.valueOf(i+1);
        }

        DefaultTableModel tableModel =  new DefaultTableModel(
            columnNames,
            rows)
        {
            public Object getValueAt(int row, int column)
            {
                return Array.get(dataValue, row*getColumnCount()+column);
            }
        };

        theTable = new JTable(tableModel)
        {
            public boolean isCellEditable( int row, int col ) { return true; }

            public void editingStopped(ChangeEvent e)
            {
                int row = getEditingRow();
                int col = getEditingColumn();
                super.editingStopped(e);

                Object source = e.getSource();

                if (source instanceof CellEditor)
                {
                    CellEditor editor = (CellEditor)source;
                    String cellValue = (String)editor.getCellEditorValue();

                    try { updateValueInMemory(cellValue, row, col); }
                    catch (Exception ex)
                    {
                        JOptionPane.showMessageDialog( this, ex, getTitle(), JOptionPane.ERROR_MESSAGE);
                    }
                } // if (source instanceof CellEditor)
            }

            public boolean isCellSelected(int row, int column)
            {
                if (getSelectedRow()==row && getSelectedColumn()==column)
                {
                    cellLabel.setText(
                        String.valueOf(row+1)+
                        ", "+
                        table.getColumnName(column)+
                        " = ");
                    cellValueField.setText(getValueAt(row, column).toString());
                }

                return super.isCellSelected(row, column);
            }
        };

        return theTable;
    }

    /**
     * Creates a JTable to hold a compound dataset.
     */
    private JTable createTable(CompoundDS d)
    {
        JTable theTable = null;

        int rank = d.getRank();
        if (rank <=0 ) d.init();

        int rows = d.getHeight();
        int cols = d.getSelectedMemberCount();
        String[] columnNames = new String[cols];

        int idx = 0;
        String[] columnNamesAll = d.getMemberNames();
        for (int i=0; i<columnNamesAll.length; i++)
        {
            if (d.isMemberSelected(i))
                columnNames[idx++] = columnNamesAll[i];
        }

        dataValue = null;
        try { dataValue = d.read(); }
        catch (Exception ex)
        {
            toolkit.beep();
            JOptionPane.showMessageDialog(
                this,
                ex,
                getTitle(),
                JOptionPane.ERROR_MESSAGE);
            dataValue = null;
        }

        if (dataValue == null ||
           !(dataValue instanceof List) )
            return null;

        DefaultTableModel tableModel =  new DefaultTableModel(
            columnNames,
            rows)
        {
            List list = (List)dataValue;
            int orders[] = ((CompoundDS)dataset).getSelectedMemberOrders();
            StringBuffer stringBuffer = new StringBuffer();

            public Object getValueAt(int row, int column)
            {
                Object cellValue = null;
                Object colValue = list.get(column);

                if (colValue == null)
                {
                    return "Null";
                }

                int cellSize = Array.getLength(colValue)/getRowCount();
                if (Array.getLength(colValue) == getRowCount())
                {
                    cellValue = Array.get(colValue, row);
                }
                else
                {
                    // cell value is an array
                    stringBuffer.setLength(0); // clear the old string
                    stringBuffer.append(Array.get(colValue, row));
                    int n = orders[column];

                    for (int i=1; i<n; i++)
                    {
                        stringBuffer.append(", ");
                        stringBuffer.append(Array.get(colValue, row*n+i));
                    }
                    cellValue = stringBuffer;
                }

                return cellValue;
            }
        };

        theTable = new JTable(tableModel)
        {
            public boolean isCellEditable(int row, int column)
            {
                    return false;
            }

            public boolean isCellSelected(int row, int column)
            {
                if (getSelectedRow()==row && getSelectedColumn()==column)
                {
                    cellLabel.setText(
                        String.valueOf(row+1)+
                        ", "+
                        String.valueOf(column+1)+
                        " = ");
                    cellValueField.setText(getValueAt(row, column).toString());
                }

                return super.isCellSelected(row, column);
            }
        };

        return theTable;
    }

    private void gotoPage(long idx)
    {
        if (dataset.getRank() < 3) return;

        if (isValueChanged) updateValueInFile();

        long[] start = dataset.getStartDims();
        int[] selectedIndex = dataset.getSelectedIndex();
        long[] dims = dataset.getDims();

        start[selectedIndex[2]] = idx;
        dataset.clearData();

        try {
            dataValue = dataset.read();
            if (dataset instanceof ScalarDS)
            {
                ((ScalarDS)dataset).convertFromUnsignedC();
                dataValue = dataset.read();
            }
        }
        catch (Exception ex)
        {
            dataValue = null;
            JOptionPane.showMessageDialog(this, ex, getTitle(), JOptionPane.ERROR_MESSAGE);
            return;
        }

        setTitle( frameTitle+ " - Page "+String.valueOf(idx+1)+ " of "+dims[selectedIndex[2]]);
        updateUI();
    }

    /** copy data from the spreadsheet to the system clipboard. */
    public void copyData()
    {
        StringBuffer sb = new StringBuffer();

        int r0 = table.getSelectedRow();     // starting row
        int c0 = table.getSelectedColumn();  // starting column

        if (r0<0 || c0 <0) return;

        int r1 = r0 + table.getSelectedRowCount();     // finish row
        int c1 = c0 + table.getSelectedColumnCount();  // finshing column

        for (int i=r0; i<r1; i++)
        {
            sb.append(table.getValueAt(i, c0).toString());
            for (int j=c0+1; j<c1; j++)
            {
                sb.append("\t");
                sb.append(table.getValueAt(i, j).toString());
            }
            sb.append("\n");
        }

        Clipboard cb =  Toolkit.getDefaultToolkit().getSystemClipboard();
        StringSelection contents = new StringSelection(sb.toString());
        cb.setContents(contents, null);
    }

    /** paste data from the system clipboard to the spreadsheet. */
    public void pasteData()
    {
        if (!(dataset instanceof ScalarDS))
            return; // does not support compound dataset in this version

        int pasteDataFlag = JOptionPane.showConfirmDialog(this,
            "Do you want to paste selected data ?",
            this.getTitle(),
            JOptionPane.YES_NO_OPTION);
        if (pasteDataFlag == JOptionPane.NO_OPTION)
            return;

        int cols = table.getColumnCount();
        int rows = table.getRowCount();
        int r0 = table.getSelectedRow();
        int c0 = table.getSelectedColumn();

        if (c0 < 0) c0 = 0;
        if (r0 < 0) r0 = 0;
        int r = r0;
        int c = c0;
        int index = r*cols+c;

        Clipboard cb =  Toolkit.getDefaultToolkit().getSystemClipboard();
        Transferable content = cb.getContents(this);
        String line = "";
        try {
            String s =(String)content.getTransferData(DataFlavor.stringFlavor);
            StringTokenizer st = new StringTokenizer(s, "\n");
            while (st.hasMoreTokens() && r < rows)
            {
                line = st.nextToken();

                StringTokenizer lt = new StringTokenizer(line);
                while (lt.hasMoreTokens() && c < cols)
                {
                    index = r*cols+c;
                    updateValueInMemory(lt.nextToken(), r, c);
                    c = c + 1;
                }
                r = r+1;
                c = c0;
            }
        }	catch (Throwable ex) {
            JOptionPane.showMessageDialog(this,
                ex,
                getTitle(),
                JOptionPane.ERROR_MESSAGE);
        }

        table.updateUI();
    }

    /** Save data as text. */
    public void saveAsText() throws Exception
    {
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

        PrintWriter out = new PrintWriter(
            new BufferedWriter(new FileWriter(choosedFile)));

        String delimiter = ViewProperties.getDataDelimiter();
        if (delimiter.equalsIgnoreCase(ViewProperties.DELIMITER_TAB))
            delimiter = "\t";
        else if (delimiter.equalsIgnoreCase(ViewProperties.DELIMITER_SPACE))
            delimiter = " ";
        else if (delimiter.equalsIgnoreCase(ViewProperties.DELIMITER_COMMA))
            delimiter = ",";
        else if (delimiter.equalsIgnoreCase(ViewProperties.DELIMITER_COLON))
            delimiter = ":";
        else if (delimiter.equalsIgnoreCase(ViewProperties.DELIMITER_SEMI_COLON))
            delimiter = ";";
        else
            delimiter = "\t";

        int cols = table.getColumnCount();
        int rows = table.getRowCount();

        for (int i=0; i<rows; i++)
        {
            out.print(table.getValueAt(i, 0));
            for (int j=1; j<cols; j++)
            {
                out.print(delimiter);
                out.print(table.getValueAt(i, j));
            }
            out.println();
        }

        out.flush();
        out.close();
    }

    /** update dataset value in file.
     *  The change will go to file.
     */
    public void updateValueInFile()
    {
        if (!(dataset instanceof ScalarDS))
            return;

        int op = JOptionPane.showConfirmDialog(this,
            "\""+ dataset.getName() +"\" has changed.\n"+
            "you want to save the changes?",
            getTitle(),
            JOptionPane.YES_NO_OPTION);

        if (op == JOptionPane.NO_OPTION)
            return;

        try { dataset.write(); }
        catch (Exception ex)
        {
            toolkit.beep();
            JOptionPane.showMessageDialog(
                this,
                ex,
                getTitle(),
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        isValueChanged = false;
    }

    /** update cell value in memory.
     *  It does not change the dataset value in file.
     *  @param cellValue the string value of input.
     *  @param row the row of the editing cell.
     *  @param col the column of the editing cell.
     */
    private void updateValueInMemory(String cellValue, int row, int col)
    throws NumberFormatException, IllegalArgumentException
    {
        int cols = table.getColumnCount();
        int i = row*cols+col;

        if (!(dataset instanceof ScalarDS))
            return;

        ScalarDS sds = (ScalarDS)dataset;
        boolean isUnsigned = sds.isUnsigned();
        Object data = null;
        try { data = sds.read(); }
        catch (Exception ex) { data = null; }

        if (data == null)
            return;

        // check data range for undigned datatype
        if (isUnsigned)
        {
            long lvalue = -1;
            long maxValue = Long.MAX_VALUE;
            lvalue = Long.parseLong(cellValue);

            if (lvalue < 0)
            {
                JOptionPane.showMessageDialog(
                    this,
                    "Negative value for unsigned integer.",
                    getTitle(),
                    JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (NT=='S') maxValue = 255;
            else if (NT=='I') maxValue = 65535;
            else if (NT=='J') maxValue = 4294967295L;

            if (lvalue < 0 || lvalue > maxValue)
            {
                JOptionPane.showMessageDialog(
                    this,
                    "Data value must be between 0 and "+maxValue,
                    getTitle(),
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        if (NT == 'B')
        {
            byte value = 0;
            value = Byte.parseByte(cellValue);
            Array.setByte(data, i, value);
        }
        else if (NT == 'S')
        {
            short value = 0;
            value = Short.parseShort(cellValue);
            Array.setShort(data, i, value);
        }
        else if (NT == 'I')
        {
            int value = 0;
            value = Integer.parseInt(cellValue);
            Array.setInt(data, i, value);
        }
        else if (NT == 'J')
        {
            long value = 0;
            value = Long.parseLong(cellValue);
            Array.setLong(data, i, value);
        }
        else if (NT == 'F')
        {
            float value = 0;
            value = Float.parseFloat(cellValue);
            Array.setFloat(data, i, value);
        }
        else if (NT == 'D')
        {
            double value = 0;
            value = Double.parseDouble(cellValue);
            Array.setDouble(data, i, value);
        }

        isValueChanged = true;
    }

    private class ColumnHeader extends JTableHeader
    {
        private int currentColumnIndex = -1;
        private int lastColumnIndex = -1;
        private JTable parentTable;

        public ColumnHeader(JTable theTable)
        {
            super(theTable.getColumnModel());

            parentTable = theTable;
            setReorderingAllowed(false);
        }

        protected void processMouseMotionEvent(MouseEvent e)
        {
            super.processMouseMotionEvent(e);

            if (e.getID() == MouseEvent.MOUSE_DRAGGED)
            {
                int colEnd = columnAtPoint(e.getPoint());

                if (colEnd < 0) colEnd = 0;
                if (currentColumnIndex < 0 ) currentColumnIndex = 0;

                parentTable.clearSelection();

                if (colEnd > currentColumnIndex)
                    parentTable.setColumnSelectionInterval(currentColumnIndex, colEnd);
                else
                    parentTable.setColumnSelectionInterval(colEnd, currentColumnIndex);
                parentTable.setRowSelectionInterval(0, parentTable.getRowCount()-1);
            }
        }

        protected void processMouseEvent(MouseEvent e)
        {
            super.processMouseEvent(e);

            int mouseID = e.getID();

            if (mouseID == MouseEvent.MOUSE_CLICKED)
            {
                if (currentColumnIndex < 0 )  return;

                if(e.isControlDown())
                {
                    // select discontinguous columns
                    parentTable.addColumnSelectionInterval(currentColumnIndex, currentColumnIndex);
                }
                else if (e.isShiftDown())
                {
                    // select continguous columns
                    if (lastColumnIndex < 0)
                        parentTable.addColumnSelectionInterval(0, currentColumnIndex);
                    else if (lastColumnIndex < currentColumnIndex)
                       parentTable.addColumnSelectionInterval(lastColumnIndex, currentColumnIndex);
                    else
                       parentTable.addColumnSelectionInterval(currentColumnIndex, lastColumnIndex);
                }
                else
                {
                    // clear old selection and set new column selection
                    parentTable.clearSelection();
                    parentTable.setColumnSelectionInterval(currentColumnIndex, currentColumnIndex);
                }

                lastColumnIndex = currentColumnIndex;
                parentTable.setRowSelectionInterval(0, parentTable.getRowCount()-1);
            }
            else if (mouseID == MouseEvent.MOUSE_PRESSED)
            {
                currentColumnIndex = columnAtPoint(e.getPoint());
            }
        }
    } // private class ColumnHeader

   /** RowHeader defines the row header component of the Spreadsheet. */
    private class RowHeader extends JTable
    {
        private int currentRowIndex = -1;
        private int lastRowIndex = -1;
        private JTable parentTable;

        public RowHeader( JTable pTable )
        {
            // Create a JTable with the same number of rows as
            // the parent table and one column.
            super( pTable.getRowCount(), 1 );

            // Store the parent table.
            parentTable = pTable;

            // Set the values of the row headers starting at 0.
            int n = parentTable.getRowCount();
            for ( int i = 0; i < n;  i++ )
            {
                setValueAt( new Integer(i+1), i, 0 );
            }

            // Create a button cell renderer.
            ButtonRenderer rend = new ButtonRenderer();

            // Set the row headers preferred width to that of the button.
            Dimension dim = getPreferredSize();
            dim.width = 60;
            setPreferredSize( dim );

            // Get the only table column.
            TableColumn col = getColumnModel().getColumn( 0 );

            // Use the cell renderer in the column.
            col.setCellRenderer( rend );
        }

        /** Overridden to return false since the headers are not editable. */
        public boolean isCellEditable( int row, int col )
        {
            return false;
        }

        /** This is called when the selection changes in the row headers. */
        public void valueChanged( ListSelectionEvent e )
        {
            if (parentTable == null)
                return;

            int rows[] = getSelectedRows();
            if (rows== null || rows.length == 0)
                return;

            parentTable.clearSelection();
            parentTable.setRowSelectionInterval(rows[0], rows[rows.length-1]);
            parentTable.setColumnSelectionInterval(0, parentTable.getColumnCount()-1);
        }

        protected void processMouseMotionEvent(MouseEvent e)
        {
            if (e.getID() == MouseEvent.MOUSE_DRAGGED)
            {
                int colEnd = rowAtPoint(e.getPoint());

                if (colEnd < 0) colEnd = 0;
                if (currentRowIndex < 0 ) currentRowIndex = 0;

                parentTable.clearSelection();

                if (colEnd > currentRowIndex)
                    parentTable.setRowSelectionInterval(currentRowIndex, colEnd);
                else
                    parentTable.setRowSelectionInterval(colEnd, currentRowIndex);

                parentTable.setColumnSelectionInterval(0, parentTable.getColumnCount()-1);
            }
        }

        protected void processMouseEvent(MouseEvent e)
        {
            int mouseID = e.getID();

            if (mouseID == MouseEvent.MOUSE_CLICKED)
            {
                if (currentRowIndex < 0 )  return;

                if(e.isControlDown())
                {
                    // select discontinguous rows
                    parentTable.addRowSelectionInterval(currentRowIndex, currentRowIndex);
                }
                else if (e.isShiftDown())
                {
                    // select continguous columns
                    if (lastRowIndex < 0)
                        parentTable.addRowSelectionInterval(0, currentRowIndex);
                    else if (lastRowIndex < currentRowIndex)
                       parentTable.addRowSelectionInterval(lastRowIndex, currentRowIndex);
                    else
                       parentTable.addRowSelectionInterval(currentRowIndex, lastRowIndex);
                }
                else
                {
                    // clear old selection and set new column selection
                    parentTable.clearSelection();
                    parentTable.setRowSelectionInterval(currentRowIndex, currentRowIndex);
                }

                lastRowIndex = currentRowIndex;

                parentTable.setColumnSelectionInterval(0, parentTable.getColumnCount()-1);
            }
            else if (mouseID == MouseEvent.MOUSE_PRESSED)
            {
                currentRowIndex = rowAtPoint(e.getPoint());
            }
        }
    }

    /** ButtonRenderer is a custom cell renderer that displays cells as buttons. */
    private class ButtonRenderer extends JButton implements TableCellRenderer
    {
        public ButtonRenderer()
        {
            super();
            setBorder(new TableHeaderBorder());
            setHorizontalAlignment(JButton.LEFT);
        }

        /** Configures the button for the current cell, and returns it. */
        public Component getTableCellRendererComponent (
            JTable table,
            Object value,
            boolean isSelected,
            boolean hasFocus,
            int row,
            int column )
        {
            if ( value != null )
                setText( value.toString() );

            return this;
        }
    }

}
