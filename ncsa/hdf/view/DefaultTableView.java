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
import javax.swing.tree.*;
import java.awt.event.*;
import javax.swing.border.*;
import java.awt.datatransfer.*;
import java.util.*;
import java.io.*;
import java.lang.reflect.Array;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.Toolkit;
import java.awt.Color;
import java.awt.Insets;
import java.text.DecimalFormat;

/**
 * TableView displays an HDF dataset as a two-dimensional table.
 * <p>
 * @version 1.3.0 1/28/2002
 * @author Peter X. Cao
 */
public class DefaultTableView extends JInternalFrame
implements TableView, ActionListener
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

    private boolean isReadOnly;

    private boolean isDisplayTypeChar;

    private boolean isTransposed;

    private JCheckBoxMenuItem checkScientificNotation;

     /**
     * Constructs an TableView.
     * <p>
     * @param theView the main HDFView.
     */
    public DefaultTableView(ViewManager theView) {
        this(theView, Boolean.FALSE, Boolean.FALSE);
    }

    /**
     * Constructs an TableView.
     * <p>
     * @param theView the main HDFView.
     */
    public DefaultTableView(ViewManager theView, Boolean isDisplayChar, Boolean transposed)
    {
        super();

        setDefaultCloseOperation(JInternalFrame.DISPOSE_ON_CLOSE);

        viewer = theView;
        toolkit = Toolkit.getDefaultToolkit();
        isValueChanged = false;
        isReadOnly = false;
        isTransposed = transposed.booleanValue();

        HObject hobject = (HObject)viewer.getTreeView().getCurrentObject();
        if (hobject == null || !(hobject instanceof Dataset)) {
             return;
        }

        dataset = (Dataset)hobject;
        isReadOnly = dataset.getFileFormat().isReadOnly();

        // cannot edit hdf4 vdata
        if (dataset.getFileFormat().isThisType(FileFormat.getFileFormat(FileFormat.FILE_TYPE_HDF4)) &&
            dataset instanceof CompoundDS)
            isReadOnly = true;

        // disable edit feature for szip compression when encode is not enabled
        if (!isReadOnly)
        {
            String compression = dataset.getCompression();
            if (compression != null && compression.startsWith("SZIP"))
            {
                if (!compression.endsWith(Dataset.H5Z_FILTER_CONFIG_ENCODE_ENABLED))
                    isReadOnly = true;
            }
        }

        isDisplayTypeChar = (isDisplayChar.booleanValue() && dataset.getDatatype().getDatatypeSize()==1);

        // create the table and its columnHeader
        if (dataset instanceof CompoundDS)
        {
            isTransposed = false; // disable transpose for compound dataset
            this.setFrameIcon(ViewProperties.getTableIcon());
            table = createTable((CompoundDS)dataset);
        }
        else /*if (dataset instanceof ScalarDS) */
        {
            this.setFrameIcon(ViewProperties.getDatasetIcon());
            table = createTable(dataset);
        }

        if (table == null)
        {
            viewer.showStatus("Creating table failed - "+dataset.getName());
            return;
        }

        ColumnHeader columnHeaders = new ColumnHeader(table);
        table.setTableHeader(columnHeaders);
        table.setCellSelectionEnabled(true);
        table.setAutoResizeMode( JTable.AUTO_RESIZE_OFF );
        table.setGridColor(Color.gray);

        // add the table to a scroller
        JScrollPane scrollingTable = new JScrollPane(table);
        scrollingTable.getVerticalScrollBar().setUnitIncrement(100);
        scrollingTable.getHorizontalScrollBar().setUnitIncrement(100);

        // create row headers and add it to the scroller
        RowHeader rowHeaders = new RowHeader( table );

        JViewport viewp = new JViewport();
        viewp.add( rowHeaders );
        viewp.setPreferredSize( rowHeaders.getPreferredSize() );
        scrollingTable.setRowHeader( viewp );

        cellLabel = new JLabel("");
        cellLabel.setBorder(new EtchedBorder(EtchedBorder.LOWERED) );
        Dimension dim = cellLabel.getPreferredSize();
        dim.width = 75;
        cellLabel.setPreferredSize( dim );
        cellLabel.setHorizontalAlignment(JLabel.RIGHT);

        cellValueField = new JTextArea();
        cellValueField.setLineWrap(true);
        cellValueField.setWrapStyleWord(true);
        cellValueField.setEditable(false);
        cellValueField.setBackground(new Color(255, 255, 240));

        JScrollPane scrollingcellValue = new JScrollPane(cellValueField);
        scrollingcellValue.getVerticalScrollBar().setUnitIncrement(50);
        scrollingcellValue.getHorizontalScrollBar().setUnitIncrement(50);

        JPanel valuePane = new JPanel();
        valuePane.setLayout(new BorderLayout());
        valuePane.add(cellLabel, BorderLayout.WEST);
        valuePane.add (scrollingcellValue, BorderLayout.CENTER);

/*
        // add to the main panel
        JPanel contentPane = (JPanel)getContentPane();
        contentPane.setLayout(new BorderLayout());
        contentPane.add(valuePane, BorderLayout.SOUTH);
        contentPane.add (scroller, BorderLayout.CENTER);
*/

        JSplitPane splitPane = new JSplitPane(
            JSplitPane.VERTICAL_SPLIT,
            valuePane, scrollingTable);
        splitPane.setDividerLocation(25);
        JPanel contentPane = (JPanel)getContentPane();
        contentPane.add(splitPane);

        // set title
        StringBuffer sb = new StringBuffer("TableView - ");
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

        if (rank > 2)
        {
            // reset the title for 3D dataset
            setTitle( "Page "+String.valueOf(start[selectedIndex[2]]+1)+ " of "+dims[selectedIndex[2]] + "    " + frameTitle);
        }

        setJMenuBar(createMenuBar());
        viewer.showStatus(sb.toString());
    }

    private JMenuBar createMenuBar() {
        JMenuBar bar = new JMenuBar();
        JButton button;
        boolean isEditable = !isReadOnly;

        JMenu menu = new JMenu("Table", false);
        menu.setMnemonic('T');
        bar.add(menu);

        JMenuItem item = new JMenuItem( "Save As Text");
        //item.setMnemonic(KeyEvent.VK_T);
        item.addActionListener(this);
        item.setActionCommand("Save table as text");
        menu.add(item);

        menu.addSeparator();

        item = new JMenuItem( "Copy Data");
        //item.setMnemonic(KeyEvent.VK_C);
        item.addActionListener(this);
        item.setActionCommand("Copy data");
        menu.add(item);

        item = new JMenuItem( "Paste Data");
        //item.setMnemonic(KeyEvent.VK_P);
        item.addActionListener(this);
        item.setActionCommand("Paste data");
        item.setEnabled(isEditable);
        menu.add(item);

        menu.addSeparator();

        item = new JMenuItem( "Import Data from File");
        //item.setMnemonic(KeyEvent.VK_I);
        item.addActionListener(this);
        item.setActionCommand("Import data from file");
        item.setEnabled(isEditable);
        menu.add(item);

        menu.addSeparator();

        item = new JMenuItem( "Write Selection to New Dataset");
        //item.setMnemonic(KeyEvent.VK_W);
        item.addActionListener(this);
        item.setActionCommand("Write selection to dataset");
        item.setEnabled(isEditable && (dataset instanceof ScalarDS));
        menu.add(item);


        item = new JMenuItem( "Save Change to File");
        //item.setMnemonic(KeyEvent.VK_U);
        item.addActionListener(this);
        item.setActionCommand("Save dataset");
        item.setEnabled(isEditable);
        menu.add(item);

        menu.addSeparator();

        item = new JMenuItem( "Select Whole Page");
        //item.setMnemonic(KeyEvent.VK_A);
        item.addActionListener(this);
        item.setActionCommand("Select all data");
        menu.add(item);

        menu.addSeparator();

        item = new JMenuItem( "Show Lineplot");
        item.addActionListener(this);
        item.setActionCommand("Show chart");
        menu.add(item);

        item = new JMenuItem( "Show Statistics");
        //item.setMnemonic(KeyEvent.VK_S);
        item.addActionListener(this);
        item.setActionCommand("Show statistics");
//        item.setEnabled(dataset instanceof ScalarDS);
        menu.add(item);

        item = new JMenuItem( "Math Conversion");
        //item.setMnemonic(KeyEvent.VK_M);
        item.addActionListener(this);
        item.setActionCommand("Math conversion");
        item.setEnabled(isEditable);
//        item.setEnabled(isEditable && (dataset instanceof ScalarDS));
        menu.add(item);

        menu.addSeparator();

        checkScientificNotation = new JCheckBoxMenuItem("Scientific Notation", false);
        item = checkScientificNotation;
        item.addActionListener(this);
        item.setActionCommand("Scientific Notation");
        if (dataset instanceof ScalarDS)
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
        button.setToolTipText( "Line Plot" );
        button.setMargin( margin );
        button.addActionListener( this );
        button.setActionCommand( "Show chart" );

        boolean is3D = (dataset.getRank() > 2);
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
        }

        return bar;
    }

    public void actionPerformed(ActionEvent e)
    {
        Object source = e.getSource();
        String cmd = e.getActionCommand();

        if (cmd.equals("Close")) {
            dispose();  // terminate the application
        }
        else if (cmd.equals("Save table as text")) {
            try { saveAsText(); }
            catch (Exception ex) {
                toolkit.beep();
                JOptionPane.showMessageDialog((JFrame)viewer,
                        ex,
                        getTitle(),
                        JOptionPane.ERROR_MESSAGE);
            }
        }
        else if (cmd.equals("Copy data")) {
            copyData();
        }
        else if (cmd.equals("Paste data")) {
            pasteData();
        }
        else if (cmd.equals("Import data from file")) {
            String currentDir = dataset.getFileFormat().getParent();
            JFileChooser fchooser = new JFileChooser(currentDir);
            fchooser.setFileFilter(DefaultFileFilter.getFileFilterText());
            int returnVal = fchooser.showOpenDialog(this);

            if(returnVal != JFileChooser.APPROVE_OPTION)
                return;

            File choosedFile = fchooser.getSelectedFile();
            if (choosedFile == null)
                return;

            String txtFile = choosedFile.getAbsolutePath();
            importTextData(txtFile);
        }
        else if (cmd.equals("Write selection to dataset"))
        {
            JTable jtable = getTable();
            if (jtable.getSelectedColumnCount() <=0 ||
                jtable.getSelectedRowCount() <=0 )
            {
                JOptionPane.showMessageDialog(
                        this,
                        "Select table cells to write.",
                        "HDFView",
                        JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            TreeView treeView = viewer.getTreeView();
            TreeNode node = viewer.getTreeView().findTreeNode(dataset);
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
        else if (cmd.equals("Save dataset")) {
            try { updateValueInFile(); }
            catch (Exception ex) {
                toolkit.beep();
                JOptionPane.showMessageDialog((JFrame)viewer,
                        ex,
                        getTitle(),
                        JOptionPane.ERROR_MESSAGE);
            }
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
        else if (cmd.equals("Show chart")) {
            showLineplot();
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
        else if (cmd.equals("Show statistics")) {
            try { showStatistics(); }
            catch (Exception ex) {
                toolkit.beep();
                JOptionPane.showMessageDialog((JFrame)viewer,
                        ex,
                        getTitle(),
                        JOptionPane.ERROR_MESSAGE);
            }
        }
        else if (cmd.equals("Math conversion")) {
            try { mathConversion(); }
            catch (Exception ex) {
                toolkit.beep();
                JOptionPane.showMessageDialog((JFrame)viewer,
                        ex,
                        getTitle(),
                        JOptionPane.ERROR_MESSAGE);
            }
        }
        else if (cmd.equals("Scientific Notation"))
        {
             this.updateUI();
        }
    }

    // Implementing DataView.
    public HObject getDataObject() {
        return dataset;
    }

    public void dispose()
    {
        if (isValueChanged) updateValueInFile();

        if (dataset instanceof ScalarDS)
        {
            ScalarDS sds = (ScalarDS)dataset;
            // reload the data when it is displayed next time
            // because the display type (table or image) may be
            // different.

            if (sds.isImage()) sds.clearData();

            dataValue = null;
            table = null;
        }

        viewer.removeDataView(this);
        super.dispose();
    }

    // Implementing DataObserver.
    private void previousPage()
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
    private void nextPage()
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
    private void firstPage()
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
    private void lastPage()
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
    public JTable getTable() {
        return table;
    }

    // Implementing TableObserver.
    private void showLineplot()
    {
        int[] rows = table.getSelectedRows();
        int[] cols = table.getSelectedColumns();

        if (rows == null ||
            cols == null ||
            rows.length <=0 ||
            cols.length  <=0 )
        {
            toolkit.beep();
            JOptionPane.showMessageDialog(this,
            "Select rows/columns to draw line plot.",
            getTitle(),
            JOptionPane.ERROR_MESSAGE);
            return;
        }
/*
Object[] options = { "OK", "CANCEL" };
JOptionPane.showOptionDialog(null, "Click OK to continue", "Warning",
JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE,
null, options, options[0]);
*/
        Object[] options = { "Column", "Row", "Cancel" };
        int option = JOptionPane.showOptionDialog(
                this,
                "Do you want to draw line plot by column or row?",
                getTitle(),
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]);

        if (option == 2)
            return; // cancel the line plot action

        boolean isRowPlot = (option == 1);

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
            data = new double[nLines][cols.length];
            xRange[1] = cols.length-1;
            for (int i=0; i<nLines; i++)
            {
                lineLabels[i] = "Row"+String.valueOf(rows[i]+1);
                for (int j=0; j<cols.length; j++)
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

        Chart cv = new Chart(
            (JFrame)viewer,
            title,
            Chart.LINEPLOT,
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
     * Returns the selected data values.
     */
    public Object getSelectedData()
    {
        if (dataset instanceof CompoundDS)
            return getSelectedCompoundData();
        else
            return getSelectedScalarData();
    }

    /**
     * Returns the selected data values.
     */
    private Object getSelectedScalarData()
    {
        Object selectedData = null;

        int cols = table.getSelectedColumnCount();
        int rows = table.getSelectedRowCount();

        if (cols <=0 || rows <= 0)
        {
            return null;
        }

        if (table.getColumnCount() == cols &&
            table.getRowCount() == rows)
            return dataValue;

        int size = cols*rows;
        int nt = NT;
        if (nt == 'B')
            selectedData = new byte[size];
        else if (nt == 'S')
            selectedData = new short[size];
        else if (nt == 'I')
            selectedData = new int[size];
        else if (nt == 'J')
            selectedData = new long[size];
        else if (nt == 'F')
            selectedData = new float[size];
        else if (nt == 'D')
            selectedData = new double[size];
        else
        {
            toolkit.beep();
            JOptionPane.showMessageDialog(this,
            "Unsupported data type.",
            getTitle(),
            JOptionPane.ERROR_MESSAGE);
            return null;
        }

        int r0 = table.getSelectedRow();
        int c0 = table.getSelectedColumn();
        int w = table.getColumnCount();
        int idx_src=0, idx_dst=0;
        for (int i=0; i<rows; i++)
        {
            idx_src = (r0+i)*w+c0;
            System.arraycopy(dataValue, idx_src, selectedData, idx_dst, cols);
            idx_dst += cols;
        }

        return selectedData;
    }

    /**
     * Returns the selected data values.
     */
    private Object getSelectedCompoundData()
    {
        Object selectedData = null;

        int cols = table.getSelectedColumnCount();
        int rows = table.getSelectedRowCount();

        if (cols <=0 || rows <= 0)
        {
            toolkit.beep();
            JOptionPane.showMessageDialog(this,
            "No data is selected.",
            getTitle(),
            JOptionPane.ERROR_MESSAGE);
            return null;
        }

        Object colData = null;
        try {
            colData = ((List)dataset.getData()).get(table.getSelectedColumn());
        } catch (Exception ex) {return null;}

        int size = Array.getLength(colData);
        String cName = colData.getClass().getName();
        int cIndex = cName.lastIndexOf("[");
        char nt = ' ';
        if (cIndex >= 0 ) nt = cName.charAt(cIndex+1);

        if (nt == 'B')
            selectedData = new byte[size];
        else if (nt == 'S')
            selectedData = new short[size];
        else if (nt == 'I')
            selectedData = new int[size];
        else if (nt == 'J')
            selectedData = new long[size];
        else if (nt == 'F')
            selectedData = new float[size];
        else if (nt == 'D')
            selectedData = new double[size];
        else
        {
            toolkit.beep();
            JOptionPane.showMessageDialog(this,
            "Unsupported data type.",
            getTitle(),
            JOptionPane.ERROR_MESSAGE);
            return null;
        }

        System.arraycopy(colData, 0, selectedData, 0, size);

        return selectedData;
    }

    /**
     * Creates a JTable to hold a scalar dataset.
     */
    private JTable createTable(Dataset d)
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
        if (isTransposed)
        {
            cols = (int)dims[0];
            rows = 1;
            if (rank > 1)
            {
                cols = d.getHeight();
                rows = d.getWidth();
            }
        }
        else
        {
            rows = (int)dims[0];
            cols = 1;
            if (rank > 1)
            {
                rows = d.getHeight();
                cols = d.getWidth();
            }
        }

        dataValue = null;
        try {
            d.getData();
            if (d instanceof ScalarDS) {
                ((ScalarDS)d).convertFromUnsignedC();
            }
            dataValue = d.getData();
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
        boolean isVL = cName.startsWith("[Ljava.lang.String;");

        // convert nubmerical data into char
        // only possible cases are byte[] and short[] (converted from unsigned byte)
        if (isDisplayTypeChar && (NT == 'B' || NT == 'S'))
        {
            int n = Array.getLength(dataValue);
            char[] charData = new char[n];
            for (int i=0; i<n; i++)
            {
                if (NT == 'B')
                    charData[i] = (char)Array.getByte(dataValue, i);
                else if (NT == 'S')
                    charData[i] = (char)Array.getShort(dataValue, i);
            }

            dataValue = charData;
        }

        String columnNames[] = new String[cols];
        long[] startArray = dataset.getStartDims();
        long[] strideArray = dataset.getStride();
        int[] selectedIndex = dataset.getSelectedIndex();
        int start = 0;
        int stride = 1;

        if (rank > 1)
        {
            start = (int)startArray[selectedIndex[1]];
            stride = (int)strideArray[selectedIndex[1]];
        }

        for (int i=0; i<cols; i++)
        {
            columnNames[i] = String.valueOf(start+(i+1)*stride);
        }

        DefaultTableModel tableModel =  new DefaultTableModel(
            columnNames,
            rows)
        {
            Object theValue = null;
            DecimalFormat sf = new DecimalFormat ("0.0###E0#");

            public Object getValueAt(int row, int column)
            {
                if (isTransposed)
                    theValue = Array.get(dataValue, column*getRowCount()+row);
                else
                    theValue = Array.get(dataValue, row*getColumnCount()+column);

                if (checkScientificNotation.isSelected())
                    return sf.format(theValue);
                else
                    return theValue;
            }
        };

        theTable = new JTable(tableModel)
        {
            public boolean isCellEditable( int row, int col )
            {
                if (isReadOnly || isDisplayTypeChar)
                    return false;
                else
                    return true;
            }

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

                    try {
                        updateValueInMemory(cellValue, row, col);
                    }
                    catch (Exception ex)
                    {
                        toolkit.beep();
                        JOptionPane.showMessageDialog(
                        this,
                        ex,
                        getTitle(),
                        JOptionPane.ERROR_MESSAGE);
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
                        "  =  ");
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

        dataValue = null;
        try { dataValue = d.getData(); }
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

        String[] subColumnNames = columnNames;
        int columns = d.getWidth();
        if (columns > 1) {
            // multi-dimension compound dataset
            subColumnNames = new String[columns*columnNames.length];
            int halfIdx = columnNames.length/2;
            for (int i=0; i<columns; i++) {
                for (int j=0; j<columnNames.length; j++) {
                    if (j == halfIdx)
                        subColumnNames[i*columnNames.length+j] = i+"\n "+columnNames[j];
                    else
                        subColumnNames[i*columnNames.length+j] = " \n "+columnNames[j];
                }
            }
        }

        DefaultTableModel tableModel =  new DefaultTableModel(
            subColumnNames,
            rows)
        {
            List list = (List)dataValue;
            CompoundDS compound = (CompoundDS)dataset;
            int orders[] = compound.getSelectedMemberOrders();
            StringBuffer stringBuffer = new StringBuffer();
            int nFields = list.size();
            int nRows = getRowCount();
            int nSubColumns = (nFields>0) ? getColumnCount()/nFields : 0;

            public Object getValueAt(int row, int col)
            {
                int column = col;
                int rowIdx = 0;

                if (nSubColumns > 1) // multi-dimension compound dataset
                {
                    int colIdx = col/nFields;
                    column = col - colIdx*nFields;
                    int n = orders[column];
                    rowIdx = row*orders[column] + colIdx*nRows*orders[column];
                }
                else {
                    rowIdx = row*orders[column];
                }

                Object colValue = list.get(column);
                if (colValue == null) return "Null";

                stringBuffer.setLength(0); // clear the old string
                int[] mdim = compound.getMemeberDims(column);
                if (mdim == null) {
                    stringBuffer.append(Array.get(colValue, rowIdx));
                    for (int i=1; i<orders[column]; i++) {
                        stringBuffer.append(", ");
                        stringBuffer.append(Array.get(colValue, rowIdx+i));
                    }
                } else {
                    int i = 0;
                    for (int j=0; j<mdim.length; j++) {
                        if (j>0) stringBuffer.append("\n");
                        stringBuffer.append(Array.get(colValue, rowIdx+i++));
                        for (int k=1; k<mdim[j]; k++) {
                            stringBuffer.append(", ");
                            stringBuffer.append(Array.get(colValue, rowIdx+i++));
                        }
                    }
                }

                return stringBuffer;
            }
        };

        theTable = new JTable(tableModel)
        {
            int lastSelectedRow = -1;
            int lastSelectedColumn = -1;

            public boolean isCellEditable(int row, int column)
            {
                // TODO: disable the editing feature. HDF4 has bug: cannot write vdata by field

                return !isReadOnly;
            }

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
                        toolkit.beep();
                        JOptionPane.showMessageDialog(
                        this,
                        ex,
                        getTitle(),
                        JOptionPane.ERROR_MESSAGE);
                    }
                } // if (source instanceof CellEditor)
            }

            public boolean isCellSelected(int row, int column)
            {
                if (lastSelectedRow == row &&
                    lastSelectedColumn == column)
                    return super.isCellSelected(row, column);

                lastSelectedRow = row;
                lastSelectedColumn = column;
                if (getSelectedRow()==row && getSelectedColumn()==column)
                {
                    cellLabel.setText(
                        String.valueOf(row+1)+
                        ", "+
                        String.valueOf(column+1)+
                        "  =  ");
                    cellValueField.setText(getValueAt(row, column).toString());
                }

                return super.isCellSelected(row, column);
            }
        };

        if (columns > 1) {
            // multi-dimension compound dataset
            MultiLineHeaderRenderer renderer = new MultiLineHeaderRenderer(
                    columns, columnNames.length);
            Enumeration enum = theTable.getColumnModel().getColumns();
            while (enum.hasMoreElements())
            {
                ((TableColumn)enum.nextElement()).setHeaderRenderer(renderer);
            }
        }

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
            dataValue = dataset.getData();
            if (dataset instanceof ScalarDS)
            {
                ((ScalarDS)dataset).convertFromUnsignedC();
                dataValue = dataset.getData();
            }
        }
        catch (Exception ex)
        {
            dataValue = null;
            JOptionPane.showMessageDialog(this, ex, getTitle(), JOptionPane.ERROR_MESSAGE);
            return;
        }

        setTitle( "Page "+String.valueOf(start[selectedIndex[2]]+1)+ " of "+dims[selectedIndex[2]] + "    " + frameTitle);        table.clearSelection();
        updateUI();
    }

    /** copy data from the spreadsheet to the system clipboard. */
    private void copyData()
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
    private void pasteData()
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
            toolkit.beep();
            JOptionPane.showMessageDialog(
                this,
                ex,
                getTitle(),
                JOptionPane.ERROR_MESSAGE);
        }

        table.updateUI();
    }

    /**
     *  import data values from text file.
     */
    private void importTextData(String fname)
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

        BufferedReader in = null;
        try { in = new BufferedReader(new FileReader(fname));
        } catch (FileNotFoundException ex) { return; }

        String line = null;
        StringTokenizer tokenizer1=null, tokenizer2=null;

        try { line = in.readLine(); }
        catch (IOException ex)
        {
            try { in.close(); } catch (IOException ex2) {}
            return;
        }

        int size = Array.getLength(dataValue);
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

        String token=null;
        while (line != null && index < size)
        {
            tokenizer1 = new StringTokenizer(line);
            while (tokenizer1.hasMoreTokens() && index < size)
            {
                tokenizer2 = new StringTokenizer(tokenizer1.nextToken(), delimiter);
                while (tokenizer2.hasMoreTokens() && index < size)
                {
                    token = tokenizer2.nextToken();
                    try { updateValueInMemory(token, r, c); }
                    catch (Exception ex)
                    {
                        toolkit.beep();
                        JOptionPane.showMessageDialog(
                        this,
                        ex,
                        getTitle(),
                        JOptionPane.ERROR_MESSAGE);
                        try { in.close(); } catch (IOException ex2) {}
                        return;
                    }
                    index++;
                    c++;

                    if (c >= cols)
                    {
                        c = 0;
                        r++;
                    }
                } // while (tokenizer2.hasMoreTokens() && index < size)
            } // while (tokenizer1.hasMoreTokens() && index < size)
            try { line = in.readLine(); }
            catch (IOException ex) { line = null; }
        }
        try { in.close(); } catch (IOException ex) {}

        table.updateUI();
    }

    /** Save data as text. */
    private void saveAsText() throws Exception
    {
        final JFileChooser fchooser = new JFileChooser(dataset.getFile());
        fchooser.setFileFilter(DefaultFileFilter.getFileFilterText());
        fchooser.changeToParentDirectory();
        fchooser.setDialogTitle("Save Current Data To Text File --- "+dataset.getName());

        File choosedFile = new File(dataset.getName()+".txt");;
        fchooser.setSelectedFile(choosedFile);
        int returnVal = fchooser.showSaveDialog(this);

        if(returnVal != JFileChooser.APPROVE_OPTION)
            return;

        choosedFile = fchooser.getSelectedFile();
        if (choosedFile == null)
            return;
        String fname = choosedFile.getAbsolutePath();

        // check if the file is in use
        List fileList = viewer.getTreeView().getCurrentFiles();
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

        viewer.showStatus("Data save to: "+fname);

        try {
            RandomAccessFile rf = new RandomAccessFile(choosedFile, "r");
            long size = rf.length();
            rf.close();
            viewer.showStatus("File size (bytes): "+size);
        } catch (Exception ex) {}
    }

    /** update dataset value in file.
     *  The change will go to file.
     */
    public void updateValueInFile()
    {
        if (isReadOnly) return;

        //if (!(dataset instanceof ScalarDS) || !isValueChanged)
        if (!isValueChanged)
            return;

        int op = JOptionPane.showConfirmDialog(this,
            "\""+ dataset.getName() +"\" has changed.\n"+
            "Do you want to save the changes?",
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

    /**
     * Selects all rows, columns, and cells in the table.
     */
    private void selectAll() throws Exception
    {
        table.selectAll();
    }

    /**
     * Show basic statistics, such as min, max, average and variance.
     */
    private void showStatistics() throws Exception
    {
        //if (!(dataset instanceof ScalarDS)) return;

        Object theData = getSelectedData();

        if (dataset instanceof CompoundDS)
        {
            int cols = table.getSelectedColumnCount();
            //if (!(dataset instanceof ScalarDS))  return;
            if ((dataset instanceof CompoundDS) && (cols>1))
            {
                JOptionPane.showMessageDialog(this,
                        "Please select one colunm a time for compound dataset.",
                        getTitle(),
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            else if (theData == null)
            {
                JOptionPane.showMessageDialog(this,
                        "Select a column to show statistics.",
                        getTitle(),
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
        } else if (theData == null ||
            Array.getLength(theData)==1)
            theData = dataValue;

        String statistics = calculateStatistics(theData);
        theData = null;
        System.gc();

        JOptionPane.showMessageDialog(this, statistics, "Statistics", JOptionPane.INFORMATION_MESSAGE);
    }

    private String calculateStatistics(Object theData) throws Exception
    {
        double min=0, max=0, mean=0, variance = 0, sum=0, sum2=0;
        int n = Array.getLength(theData);
        String cName = theData.getClass().getName();
        int cIndex = cName.lastIndexOf("[");
        char nt = ' ';
        if (cIndex >= 0 ) nt = cName.charAt(cIndex+1);

        switch (nt)
        {
            case ('B'):
            {
                byte[] bvalue = (byte[])theData;
                min = bvalue[0];
                max = bvalue[0];
                sum = bvalue[0];
                sum2 = bvalue[0]*bvalue[0];
                for (int i=1; i<n; i++)
                {
                    if (min > bvalue[i])
                        min = bvalue[i];
                    if (max < bvalue[i])
                        max = bvalue[i];
                    sum += bvalue[i];
                    sum2 += bvalue[i]*bvalue[i];
                }
                break;
            }
            case ('S'):
            {
                short[] svalue = (short[])theData;
                min = svalue[0];
                max = svalue[0];
                sum = svalue[0];
                sum2 = svalue[0]*svalue[0];
                for (int i=1; i<n; i++)
                {
                    if (min > svalue[i])
                        min = svalue[i];
                    if (max < svalue[i])
                        max = svalue[i];
                    sum += svalue[i];
                    sum2 += svalue[i]*svalue[i];
                }
                break;
            }
            case('I'):
            {
                int[] ivalue = (int[])theData;
                min = ivalue[0];
                max = ivalue[0];
                sum = ivalue[0];
                sum2 = ivalue[0]*ivalue[0];
                for (int i=1; i<n; i++)
                {
                    if (min > ivalue[i])
                        min = ivalue[i];
                    if (max < ivalue[i])
                        max = ivalue[i];
                    sum += ivalue[i];
                    sum2 += ivalue[i]*ivalue[i];
                }
                break;
            }
            case('J'):
            {
                long[] lvalue = (long[])theData;
                min = lvalue[0];
                max = lvalue[0];
                sum = lvalue[0];
                sum2 = lvalue[0]*lvalue[0];
                for (int i=1; i<n; i++)
                {
                    if (min > lvalue[i])
                        min = lvalue[i];
                    if (max < lvalue[i])
                        max = lvalue[i];
                    sum += lvalue[i];
                    sum2 += lvalue[i]*lvalue[i];
                }
                break;
            }
            case('F'):
            {
                float[] fvalue = (float[])theData;
                min = fvalue[0];
                max = fvalue[0];
                sum = fvalue[0];
                sum2 = fvalue[0]*fvalue[0];
                for (int i=1; i<n; i++)
                {
                    if (min > fvalue[i])
                        min = fvalue[i];
                    if (max < fvalue[i])
                        max = fvalue[i];
                    sum += fvalue[i];
                    sum2 += fvalue[i]*fvalue[i];
                }
                break;
            }
            case('D'):
            {
                double[] dvalue = (double[])theData;
                min = dvalue[0];
                max = dvalue[0];
                sum = dvalue[0];
                sum2 = dvalue[0]*dvalue[0];
                for (int i=1; i<n; i++)
                {
                    if (min > dvalue[i])
                        min = dvalue[i];
                    if (max < dvalue[i])
                        max = dvalue[i];
                    sum += dvalue[i];
                    sum2 += dvalue[i]*dvalue[i];
                }
                break;
            }
        }

        mean = sum/n;
        variance = sum2/n - mean*mean;

        StringBuffer sb = new StringBuffer();
        sb.append("\nMinimum                 = ");
        sb.append(Float.toString((float)min));
        sb.append("\nMaximum                 = ");
        sb.append(Float.toString((float)max));
        sb.append("\nMean                         = ");
        sb.append(Float.toString((float)mean));
        sb.append("\nStandard deviation = ");
        sb.append(Float.toString((float)(Math.sqrt(variance))));

        return sb.toString();
    }

    /**
     * Converting selected data based on predefined math functions.
     */
    private void mathConversion() throws Exception
    {
        if (isReadOnly)
            return;

        int cols = table.getSelectedColumnCount();
        //if (!(dataset instanceof ScalarDS))  return;
        if ((dataset instanceof CompoundDS) && (cols>1))
        {
            toolkit.beep();
            JOptionPane.showMessageDialog(this,
            "Please select one colunm a time for math conversion for compound dataset.",
            getTitle(),
            JOptionPane.ERROR_MESSAGE);
            return;
        }

        Object theData = getSelectedData();
        if (theData == null)
        {
            toolkit.beep();
            JOptionPane.showMessageDialog(this,
            "No data is selected.",
            getTitle(),
            JOptionPane.ERROR_MESSAGE);
            return;
        }

        MathConversionDialog dialog = new MathConversionDialog((JFrame)viewer, theData);
        dialog.show();

        if (dialog.isConverted())
        {
            if (dataset instanceof CompoundDS)
            {
                Object colData = null;
                try {
                    colData = ((List)dataset.getData()).get(table.getSelectedColumn());
                } catch (Exception ex) {;}

                if (colData != null)
                {
                    int size = Array.getLength(theData);
                    System.arraycopy(theData, 0, colData, 0, size);
                }
            }
            else
            {
                int rows = table.getSelectedRowCount();
                int r0 = table.getSelectedRow();
                int c0 = table.getSelectedColumn();
                int w = table.getColumnCount();
                int idx_src=0, idx_dst=0;
                for (int i=0; i<rows; i++)
                {
                    idx_dst = (r0+i)*w+c0;
                    System.arraycopy(theData, idx_src, dataValue, idx_dst, cols);
                    idx_src += cols;
                }
            }

            theData = null;
            System.gc();
            table.updateUI();
            isValueChanged = true;
        }

    }

    /** update cell value in memory.
     *  It does not change the dataset value in file.
     *  @param cellValue the string value of input.
     *  @param row the row of the editing cell.
     *  @param col the column of the editing cell.
     */
    private void updateValueInMemory(String cellValue, int row, int col)
    throws Exception
    {
        if (dataset instanceof ScalarDS)
            updateScalarData(cellValue, row, col);
        else if (dataset instanceof CompoundDS)
            updateCompoundData(cellValue, row, col);
    }

    /** update cell value in memory.
     *  It does not change the dataset value in file.
     *  @param cellValue the string value of input.
     *  @param row the row of the editing cell.
     *  @param col the column of the editing cell.
     */
    private void updateScalarData(String cellValue, int row, int col)
    throws Exception
    {
        if (!(dataset instanceof ScalarDS) ||
            cellValue == null ||
            (cellValue=cellValue.trim()) == null)
            return;

        int i = 0;
        if (isTransposed)
            i = col*table.getRowCount()+row;
        else
            i = row*table.getColumnCount()+col;

        ScalarDS sds = (ScalarDS)dataset;
        boolean isUnsigned = sds.isUnsigned();

        // check data range for undigned datatype
        if (isUnsigned)
        {
            long lvalue = -1;
            long maxValue = Long.MAX_VALUE;
            lvalue = Long.parseLong(cellValue);

            if (lvalue < 0)
            {
                throw new NumberFormatException("Negative value for unsigned integer: "+lvalue);
            }

            if (NT=='S') maxValue = 255;
            else if (NT=='I') maxValue = 65535;
            else if (NT=='J') maxValue = 4294967295L;

            if (lvalue < 0 || lvalue > maxValue)
            {
                throw new NumberFormatException("Data value is out of range: "+lvalue);
            }
        }

        if (NT == 'B')
        {
            byte value = 0;
            value = Byte.parseByte(cellValue);
            Array.setByte(dataValue, i, value);
        }
        else if (NT == 'S')
        {
            short value = 0;
            value = Short.parseShort(cellValue);
            Array.setShort(dataValue, i, value);
        }
        else if (NT == 'I')
        {
            int value = 0;
            value = Integer.parseInt(cellValue);
            Array.setInt(dataValue, i, value);
        }
        else if (NT == 'J')
        {
            long value = 0;
            value = Long.parseLong(cellValue);
            Array.setLong(dataValue, i, value);
        }
        else if (NT == 'F')
        {
            float value = 0;
            value = Float.parseFloat(cellValue);
            Array.setFloat(dataValue, i, value);
        }
        else if (NT == 'D')
        {
            double value = 0;
            value = Double.parseDouble(cellValue);
            Array.setDouble(dataValue, i, value);
        }

        isValueChanged = true;
    }

    private void updateCompoundData(String cellValue, int row, int col)
    throws Exception
    {
        if (!(dataset instanceof CompoundDS) ||
            cellValue == null ||
            (cellValue=cellValue.trim()) == null)
            return;

        CompoundDS cds = (CompoundDS)dataset;
        List cdata = (List)cds.getData();
        int orders[] = cds.getSelectedMemberOrders();
        int nFields = cdata.size();
        int nSubColumns = table.getColumnCount()/nFields;
        int nRows = table.getRowCount();
        int column = col;
        int offset = 0;
        int morder = 1;

        if (nSubColumns > 1) // multi-dimension compound dataset
        {
            int colIdx = col/nFields;
            column = col - colIdx*nFields;
            offset = row*orders[column] + colIdx*nRows*orders[column];
        }
        else {
            offset = row*orders[column];
        }
        morder = orders[column];

        Object mdata = cdata.get(column);

        // strings
        if (Array.get(mdata, 0) instanceof String)
        {
            Array.set(mdata, offset, cellValue);
            isValueChanged = true;
            return;
        }

        // numveric data
        char mNT = ' ';
        String cName = mdata.getClass().getName();
        int cIndex = cName.lastIndexOf("[");
        if (cIndex >= 0 ) mNT = cName.charAt(cIndex+1);

        StringTokenizer st = new StringTokenizer(cellValue, ",");
        if (st.countTokens() < morder)
        {
            toolkit.beep();
            JOptionPane.showMessageDialog(this,
            "Number of data point < "+morder+".",
            getTitle(),
            JOptionPane.ERROR_MESSAGE);
            return;
        }

        String token = "";
        isValueChanged = true;
        switch (mNT)
        {
            case 'B':
                byte bvalue = 0;
                for (int i=0; i<morder; i++)
                {
                    token = st.nextToken().trim();
                    bvalue = Byte.parseByte(token);
                    Array.setByte(mdata, offset+i, bvalue);
                }
                break;
            case 'S':
                short svalue = 0;
                for (int i=0; i<morder; i++)
                {
                    token = st.nextToken().trim();
                    svalue = Short.parseShort(token);
                    Array.setShort(mdata, offset+i, svalue);
                }
                break;
            case 'I':
                int ivalue = 0;
                for (int i=0; i<morder; i++)
                {
                    token = st.nextToken().trim();
                    ivalue = Integer.parseInt(token);
                    Array.setInt(mdata, offset+i, ivalue);
                }
                break;
            case 'J':
                long lvalue = 0;
                for (int i=0; i<morder; i++)
                {
                    token = st.nextToken().trim();
                    lvalue = Long.parseLong(token);
                    Array.setLong(mdata, offset+i, lvalue);
                }
                break;
            case 'F':
                float fvalue = 0;
                for (int i=0; i<morder; i++)
                {
                    token = st.nextToken().trim();
                    fvalue = Float.parseFloat(token);
                    Array.setFloat(mdata, offset+i, fvalue);
                }
                break;
            case 'D':
                double dvalue = 0;
                for (int i=0; i<morder; i++)
                {
                    token = st.nextToken().trim();
                    dvalue = Double.parseDouble(token);
                    Array.setDouble(mdata, offset+i, dvalue);
                }
                break;
            default:
                isValueChanged = false;
        }
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

/*
            if (e.getButton() != MouseEvent.BUTTON1)
            {
                int idx = columnAtPoint(e.getPoint());
                if (idx >= 0 && idx < parentTable.getColumnCount())
                    cellValueField.setText(parentTable.getColumnName(idx));

                return;
            }
*/

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

            long[] startArray = dataset.getStartDims();
            long[] strideArray = dataset.getStride();
            int[] selectedIndex = dataset.getSelectedIndex();
            int start = (int)startArray[selectedIndex[0]];
            int stride = (int)strideArray[selectedIndex[0]];

            // Store the parent table.
            parentTable = pTable;

            // Set the values of the row headers starting at 0.
            int n = parentTable.getRowCount();
            for ( int i = 0; i < n;  i++ )
            {
                setValueAt( new Integer(start+(i+1)*stride), i, 0 );
            }

            // Get the only table column.
            TableColumn col = getColumnModel().getColumn( 0 );

            // Use the cell renderer in the column.
            col.setCellRenderer( new RowHeaderRenderer() );
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

    /** RowHeaderRenderer is a custom cell renderer that displays cells as buttons. */
    private class RowHeaderRenderer extends JLabel implements TableCellRenderer
    {
        public RowHeaderRenderer()
        {
            super();
            setHorizontalAlignment(JLabel.CENTER);

            setOpaque(true);
            setBorder(UIManager.getBorder("TableHeader.cellBorder"));
            setBackground(Color.lightGray);
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
            setFont(table.getFont());

            if ( value != null )
                setText( value.toString() );

            return this;
        }
    }

    private class MultiLineHeaderRenderer extends JList implements TableCellRenderer
    {
        private final CompoundBorder subBorder = new CompoundBorder(
                new MatteBorder(1, 0, 1, 0, java.awt.Color.darkGray),
                new MatteBorder(1, 0, 1, 0, java.awt.Color.white));
        private final CompoundBorder majorBorder = new CompoundBorder(
                new MatteBorder(1, 1, 1, 0, java.awt.Color.darkGray),
                new MatteBorder(1, 2, 1, 0, java.awt.Color.white));
        Vector lines = new Vector();
        int nMajorcolumns = 1;
        int nSubcolumns = 1;

        public MultiLineHeaderRenderer(int majorColumns, int subColumns)
        {
            nMajorcolumns = majorColumns;
            nSubcolumns = subColumns;
            setOpaque(true);
            setForeground(UIManager.getColor("TableHeader.foreground"));
            setBackground(UIManager.getColor("TableHeader.background"));
        }

        public Component getTableCellRendererComponent(JTable table, Object value,
               boolean isSelected, boolean hasFocus, int row, int column)
        {
            setFont(table.getFont());
            String str = (value == null) ? "" : value.toString();
            BufferedReader br = new BufferedReader(new StringReader(str));
            String line;

            lines.clear();
            try {
                while ((line = br.readLine()) != null) {
                    lines.addElement(line);
                }
            } catch (IOException ex) {}

            if ((column/nSubcolumns)*nSubcolumns == column)
            {
                setBorder(majorBorder);
            }
            else {
                setBorder(subBorder);
            }
            setListData(lines);

            return this;
        }
    }
}
