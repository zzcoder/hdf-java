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
import java.util.List;
import java.lang.reflect.Array;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.MouseEvent;
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
    private JTextField cellValueField;

    /**
     * Constructs an TableView.
     * <p>
     * @param theView the main HDFView.
     */
    public TableView(ViewManager theView)
    {
        super();

        viewer = theView;

        HObject hobject = (HObject)viewer.getSelectedObject();
        if (hobject == null || !(hobject instanceof Dataset))
        {
             return;
        }

        dataset = (Dataset)hobject;

        this.setDefaultCloseOperation(JInternalFrame.DISPOSE_ON_CLOSE);
        this.setTitle("TableView - "+hobject.getPath()+hobject.getName());
        this.setName(hobject.getPath()+hobject.getName());

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
        cellValueField = new JTextField();
        cellValueField.setEditable(false);
        JPanel valuePane = new JPanel();
        valuePane.setLayout(new BorderLayout());
        valuePane.add(cellLabel, BorderLayout.WEST);
        valuePane.add (cellValueField, BorderLayout.CENTER);

        // add to the main panel
        JPanel contentPane = (JPanel)getContentPane();
        contentPane.setLayout(new BorderLayout());
        contentPane.add(valuePane, BorderLayout.NORTH);
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

        long[] start = dataset.getStartDims();
        long[] dims = dataset.getDims();
        int[] selectedIndex = dataset.getSelectedIndex();
        long idx = start[selectedIndex[2]];
        if (idx == 0)
            return; // current page is the first page

        start[selectedIndex[2]] -= 1;
        dataset.clearData();

        try { dataValue = dataset.read(); }
        catch (Exception ex)
        {
            dataValue = null;
            viewer.showStatus(ex.toString());
        }

        this.setTitle(
            "TableView - "+
            dataset.getPath()+dataset.getName()+
            " - Page "+String.valueOf(start[selectedIndex[2]]+1)+
            " of "+dims[selectedIndex[2]]);

        this.updateUI();
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

        start[selectedIndex[2]] += 1;
        dataset.clearData();

        try { dataValue = dataset.read(); }
        catch (Exception ex)
        {
            dataValue = null;
            viewer.showStatus(ex.toString());
        }

        this.setTitle(
            "TableView - "+
            dataset.getPath()+dataset.getName()+
            " - Page "+String.valueOf(start[selectedIndex[2]]+1)+
            " of "+dims[selectedIndex[2]]);

        this.updateUI();
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

        start[selectedIndex[2]] = 0;
        dataset.clearData();

        try { dataValue = dataset.read(); }
        catch (Exception ex)
        {
            dataValue = null;
            viewer.showStatus(ex.toString());
        }

        this.setTitle(
            "TableView - "+
            dataset.getPath()+dataset.getName()+
            " - Page "+String.valueOf(start[selectedIndex[2]]+1)+
            " of "+dims[selectedIndex[2]]);

        this.updateUI();
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

        start[selectedIndex[2]] = dims[selectedIndex[2]]-1;
        dataset.clearData();

        try { dataValue = dataset.read(); }
        catch (Exception ex)
        {
            dataValue = null;
            viewer.showStatus(ex.toString());
        }

        this.setTitle(
            "TableView - "+
            dataset.getPath()+dataset.getName()+
            " - Page "+String.valueOf(start[selectedIndex[2]]+1)+
            " of "+dims[selectedIndex[2]]);

        this.updateUI();
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
            viewer.showStatus("Cannot show line plot for the selected data.");
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
        try { dataValue = d.read(); }
        catch (Exception ex)
        {
            dataValue = null;
            viewer.showStatus(ex.toString());
        }

        if (dataValue == null)
            return null;

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
        int cols = d.getMemberCount();

        String[] columnNames = d.getMemberNames();

        dataValue = null;
        try { dataValue = d.read(); }
        catch (Exception ex)
        {
            dataValue = null;
            viewer.showStatus(ex.toString());
        }

        if (dataValue == null ||
           !(dataValue instanceof List) )
            return null;

        DefaultTableModel tableModel =  new DefaultTableModel(
            columnNames,
            rows)
        {
            List list = (List)dataValue;
            int orders[] = ((CompoundDS)dataset).getMemberOrders();
            StringBuffer stringBuffer = new StringBuffer();

            public Object getValueAt(int row, int column)
            {
                Object cellValue = null;
                Object colValue = list.get(column);
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
                        table.getColumnName(column)+
                        " = ");
                    cellValueField.setText(getValueAt(row, column).toString());
                }

                return super.isCellSelected(row, column);
            }
        };

        return theTable;
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
