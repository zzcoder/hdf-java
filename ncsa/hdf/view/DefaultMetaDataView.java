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
import java.awt.event.*;
import javax.swing.border.*;
import javax.swing.event.*;
import javax.swing.table.*;
import javax.swing.tree.*;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.Font;
import java.awt.Insets;
import java.awt.Dimension;
import java.util.*;
import java.io.File;
import java.lang.reflect.Array;

/**
 * DefaultMetadataView is an dialog window used to show data properties.
 * Data properties include attributes and general information such as
 * object type, data type and data space.
 * <p>
 * @version 1.3.0 1/26/2002
 * @author Peter X. Cao
 */
public class DefaultMetaDataView extends JDialog
implements ActionListener, MetaDataView
{
    /**
     * The main HDFView.
     */
    private ViewManager viewer;

    /** The HDF data object */
    private HObject hObject;

    private JTabbedPane tabbedPane = null;
    private JTextArea attrContentArea;
    private JTable attrTable; // table to hold a list of attributes
    private DefaultTableModel attrTableModel;
    private JLabel attrNumberLabel;
    private int numAttributes;
    private boolean isH5;
    private byte[] userBlock;
    private JTextArea userBlockArea;

    /**
     * Constructs a DefaultMetadataView with the given HDFView.
     */
    public DefaultMetaDataView(ViewManager theView) {
        super((JFrame)theView, false);
        setDefaultCloseOperation(JInternalFrame.DISPOSE_ON_CLOSE);

        viewer = theView;
        hObject = viewer.getTreeView().getCurrentObject();
        numAttributes = 0;
        userBlock = null;
        userBlockArea = null;

        if (hObject == null) dispose();

        else if ( hObject.getPath()== null)
            setTitle("Properties - "+hObject.getName());
        else
            setTitle("Properties - "+hObject.getPath()+hObject.getName());

        isH5 = hObject.getFileFormat().isThisType(FileFormat.getFileFormat(FileFormat.FILE_TYPE_HDF5));

        tabbedPane = new JTabbedPane();

        tabbedPane.addTab("General", createGeneralPropertyPanel());
        tabbedPane.addTab("Attributes", createAttributePanel());

        boolean isRoot = (hObject instanceof Group && ((Group)hObject).isRoot());
        if (isH5 && isRoot)
        {
            // add panel to display user block
            tabbedPane.addTab("User Block", createUserBlockPanel());
        }
        tabbedPane.setSelectedIndex(0);

        JPanel bPanel = new JPanel();
        JButton b = new JButton("  Close  ");
        b.setMnemonic(KeyEvent.VK_C);
        b.setActionCommand("Close");
        b.addActionListener(this);
        bPanel.add(b);

        //Add the tabbed pane to this panel.
        JPanel contentPane = (JPanel)getContentPane();
        contentPane.setLayout(new BorderLayout());
        contentPane.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
        contentPane.setPreferredSize(new Dimension(620, 400));

        contentPane.add("Center", tabbedPane);
        contentPane.add("South", bPanel);

        // locate the H5Property dialog
        Point l = getParent().getLocation();
        l.x += 250;
        l.y += 80;
        setLocation(l);
        pack();
        show();
    }

    public void actionPerformed(ActionEvent e)
    {
        Object source = e.getSource();
        String cmd = e.getActionCommand();

        if (cmd.equals("Close")) {
            dispose();
        }
        else if (cmd.equals("Add attribute")) {
            addAttribute(hObject);
        }
        else if (cmd.equals("Delete attribute")) {
            deleteAttribute(hObject);
        }
        else if (cmd.equals("Display user block as")) {
            int type = 0;
            String typeName = (String)((JComboBox)source).getSelectedItem();
            if (typeName.equalsIgnoreCase("Text"))
                type = 0;
            else if (typeName.equalsIgnoreCase("Binary"))
                type = 2;
            else if (typeName.equalsIgnoreCase("Octal"))
                type = 8;
            else if (typeName.equalsIgnoreCase("Hexadecimal"))
                type = 16;
            else if (typeName.equalsIgnoreCase("Decimal"))
                type = 10;

            showUserBlockAs(type);
        }
    }

    /** returns the data object displayed in this data viewer */
    public HObject getDataObject() {
        return hObject;
    }

    /** Disposes of this dataobserver. */
    public void dispose() {
        super.dispose();
    }

    /** add an attribute to a data object.*/
    public Attribute addAttribute(HObject obj) {
        if (obj == null) return null;

        NewAttributeDialog  dialog = new NewAttributeDialog(this, obj);
        dialog.show();

        Attribute attr = dialog.getAttribute();
        if (attr == null)
            return null;

        String rowData[] = new String[4]; // name, value, type, size
        boolean isUnsigned = false;

        rowData[0] = attr.getName();
        rowData[2] = attr.getType().getDatatypeDescription();
        isUnsigned = attr.getType().isUnsigned();

        rowData[1] = attr.toString(", ");

        long dims[] = attr.getDataDims();

        rowData[3] = String.valueOf(dims[0]);
        for (int j=1; j<dims.length; j++)
            rowData[3] += " x " + dims[j];

        attrTableModel.addRow(rowData);
        attrTableModel.fireTableRowsInserted(
            attrTableModel.getRowCount()-1, attrTableModel.getRowCount()-1);
        numAttributes++;
        attrContentArea.setText("");
        attrNumberLabel.setText("Number of attributes = "+numAttributes);

        return attr;
    }

    /** delete an attribribute from a data object.*/
    public Attribute deleteAttribute(HObject obj)
    {
        if (obj == null)
            return null;

        int idx = attrTable.getSelectedRow();
        if (idx < 0)
        {
            JOptionPane.showMessageDialog(getOwner(),
                "No attribute is selected.",
                getTitle(),
                JOptionPane.ERROR_MESSAGE);
            return null;
        }

        int option = JOptionPane.showConfirmDialog(this,
                "Do you want to delete selected attributes?",
                getTitle(),
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (option == JOptionPane.NO_OPTION) {
            return null;
        }

        List attrList = null;
        try { attrList = obj.getMetadata(); }
        catch (Exception ex) { attrList = null; }

        if (attrList == null)
            return null;

        Attribute attr = (Attribute)attrList.get(idx);
        try { obj.removeMetadata(attr); }
        catch (Exception ex) { ; }

        attrTableModel.removeRow(idx);
        numAttributes--;
        attrTableModel.fireTableRowsDeleted(idx, idx);

        attrContentArea.setText("");
        attrNumberLabel.setText("Number of attributes = "+numAttributes);

        return attr;
    }

    /**
     * Creates a panel used to dispaly general information of HDF object.
     */
    private JPanel createGeneralPropertyPanel()
    {
        JPanel panel = new JPanel();
        panel.setLayout (new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10,0,0,0));
        boolean isRoot = (hObject instanceof Group && ((Group)hObject).isRoot());
        FileFormat theFile = hObject.getFileFormat();

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BorderLayout());

        JPanel lp = new JPanel();
        lp.setLayout(new GridLayout(4,1));

        if (isRoot)
        {
            lp.add(new JLabel("File Name: "));
            lp.add(new JLabel("File Path: "));
            lp.add(new JLabel("File Type: "));
        }
        else
        {
            lp.add(new JLabel("Name: "));
            lp.add(new JLabel("Path: "));
            lp.add(new JLabel("Type: "));
        }

        if (isH5)
            lp.add(new JLabel("Object ID: "));
        else
            lp.add(new JLabel("Ref, Tag: "));

        JPanel rp = new JPanel();
        rp.setLayout(new GridLayout(4,1));

        JTextField nameField = new JTextField(hObject.getName());
        nameField.setEditable(false);
        rp.add(nameField);

        JTextField pathField = new JTextField();
        if (isRoot)
            pathField.setText((new File(hObject.getFile())).getParent());
        else
            pathField.setText(hObject.getPath());
        pathField.setEditable(false);
        rp.add(pathField);

        String typeStr = "Unknown";
        String fileInfo = "";
        if (isRoot)
        {
            long size = 0;
            try { size = (new File(hObject.getFile())).length(); }
            catch (Exception ex) { size = -1; }
            size /= 1024;

            int groupCount=0, datasetCount=0;
            DefaultMutableTreeNode root = (DefaultMutableTreeNode)theFile.getRootNode();
            DefaultMutableTreeNode theNode = null;
            Enumeration enum = root.depthFirstEnumeration();
            while(enum.hasMoreElements())
            {
                theNode = (DefaultMutableTreeNode)enum.nextElement();
                if (theNode.getUserObject() instanceof Group)
                    groupCount++;
                else
                    datasetCount++;
            }

            fileInfo = "size="+size+"K,  groups="+groupCount+ ",  datasets="+datasetCount;
        }

        if (isRoot)
        {
            if (isH5) typeStr = "HDF5,  "+fileInfo;
            else typeStr = "HDF4,  "+fileInfo;
        }
        else if (isH5)
        {
            if (hObject instanceof Group)
                typeStr = "HDF5 Group";
            else if (hObject instanceof ScalarDS)
                typeStr = "HDF5 Scalar Dataset";
            else if (hObject instanceof CompoundDS)
                typeStr = "HDF5 Compound Dataset";
        }
        else
        {
            if (hObject instanceof Group)
                typeStr = "HDF4 Group";
            else if (hObject instanceof ScalarDS)
            {
                ScalarDS ds = (ScalarDS)hObject;
                if (ds.isImage())
                    typeStr = "HDF4 Raster Image";
                else
                    typeStr = "HDF4 SDS";
            }
            else if (hObject instanceof CompoundDS)
                typeStr = "HDF4 Vdata";
        }

        JTextField typeField = new JTextField(typeStr);
        typeField.setEditable(false);
        rp.add(typeField);

        String oidStr = null;
        long[] OID = hObject.getOID();
        if (OID != null)
        {
            oidStr = String.valueOf(OID[0]);
            for (int i=1; i<OID.length; i++)
                oidStr += ", "+ OID[i];
        }
        JTextField oidField = new JTextField(oidStr);
        oidField.setEditable(false);
        rp.add(oidField);

        JPanel tmpP = new JPanel();
        tmpP.setLayout(new BorderLayout());
        tmpP.add("West", lp);
        tmpP.add("Center", rp);
        tmpP.setBorder(new TitledBorder(""));

        topPanel.add("North", new JLabel(""));
        topPanel.add("Center", tmpP);

        JPanel infoPanel = null;
        if (hObject instanceof Group)
            infoPanel = createGroupInfoPanel((Group)hObject);
        else if (hObject instanceof Dataset)
            infoPanel= createDatasetInfoPanel((Dataset)hObject);

        panel.add(topPanel, BorderLayout.NORTH);
        if (infoPanel != null) panel.add(infoPanel, BorderLayout.CENTER);

        return panel;
    }

    /**
     * Creates a panel used to display HDF group information.
     */
    private JPanel createGroupInfoPanel(Group g)
    {
        JPanel panel = new JPanel();

        List mlist = g.getMemberList();
        if (mlist == null)
            return panel;

        int n = mlist.size();
        if (n<=0)
            return panel;

        String rowData[][] = new String[n][2];
        for (int i=0; i<n; i++)
        {
            HObject theObj = (HObject)mlist.get(i);
            rowData[i][0] = theObj.getName();
            if (theObj instanceof Group)
                rowData[i][1] = "Group";
            else if (theObj instanceof Dataset)
                rowData[i][1] = "Dataset";
        }

        String[] columnNames = {"Name", "Type"};
        JTable table = new JTable(rowData, columnNames)
        {
            public boolean isCellEditable(int row, int column)
            {
                return false;
            }
        };
        table.setCellSelectionEnabled(false);
        table.getTableHeader().setReorderingAllowed(false);
        JScrollPane scroller = new JScrollPane(table);

        panel.setLayout (new BorderLayout());
        if (g.getNumberOfMembersInFile() < ViewProperties.getMaxMembers())
            panel.add(new JLabel("Number of members: "+n), BorderLayout.NORTH);
        else
            panel.add(new JLabel("Number of members: "+n+
            " (in memory), "+g.getNumberOfMembersInFile()+" (in file)"), BorderLayout.NORTH);
        panel.add(scroller, BorderLayout.CENTER);
        panel.setBorder(new TitledBorder("Group Members"));

        return panel;
    }

    /**
     * Creates a panel used to display HDF dataset information.
     */
    private JPanel createDatasetInfoPanel(Dataset d)
    {
        JPanel lp = new JPanel();
        lp.setLayout(new GridLayout(3,1));
        lp.add(new JLabel("No. of Dimension(s): "));
        lp.add(new JLabel("Dimension Size(s): "));
        lp.add(new JLabel("Data Type: "));

        JPanel rp = new JPanel();
        rp.setLayout(new GridLayout(3,1));

        if (d.getRank() <= 0) d.init();
        JTextField txtf = new JTextField(""+d.getRank());
        txtf.setEditable(false);
        rp.add(txtf);

        String dimStr = null;
        long dims[] = d.getDims();
        if (dims != null)
        {
            StringBuffer sb = new StringBuffer();
             sb.append(dims[0]);
            for (int i=1; i<dims.length; i++)
            {
                sb.append(" x ");
                sb.append(dims[i]);
            }
            dimStr = sb.toString();
        }
        txtf = new JTextField(dimStr);
        txtf.setEditable(false);
        rp.add(txtf);

        String typeStr = null;
        if (d instanceof ScalarDS)
        {
            ScalarDS sd = (ScalarDS)d;
            typeStr = sd.getDatatype().getDatatypeDescription();
        } else if (d instanceof CompoundDS)
        {
            if (isH5) typeStr = "Compound";
            else typeStr = "Vdata";
        }

        txtf = new JTextField(typeStr);
        txtf.setEditable(false);
        rp.add(txtf);

        JPanel infoP = new JPanel();
        infoP.setLayout(new BorderLayout());
        infoP.add(lp, BorderLayout.WEST);
        infoP.add(rp, BorderLayout.CENTER);
        infoP.setBorder(new TitledBorder(""));

        JPanel panel = new JPanel();
        panel.setLayout (new BorderLayout());
        panel.add(infoP, BorderLayout.NORTH);
        panel.setBorder(new TitledBorder("Dataspace and Datatype"));

        // add compound datatype information
        if (d instanceof CompoundDS)
        {
            CompoundDS compound = (CompoundDS)d;

            int n = compound.getMemberCount();
            if (n > 0)
            {
                String rowData[][] = new String[n][3];
                String names[] = compound.getMemberNames();
                int types[] = compound.getMemberTypes();
                int orders[] = compound.getMemberOrders();

                Datatype datatype = compound.getDatatype();
                for (int i=0; i<n; i++)
                {
                    rowData[i][0] = names[i];
                    rowData[i][2] = String.valueOf(orders[i]);
                    datatype.fromNative(types[i]);
                    rowData[i][1] = datatype.getDatatypeDescription();
                }

                String[] columnNames = {"Name", "Type", "Array Size"};
                JTable table = new JTable(rowData, columnNames)
                {
                    public boolean isCellEditable(int row, int column)
                    {
                        return false;
                    }
                };
                table.setCellSelectionEnabled(false);
                table.getTableHeader().setReorderingAllowed(false);
                panel.add(new JScrollPane(table), BorderLayout.CENTER);
            } // if (n > 0)
        } // if (d instanceof Compound)

        // add compression and data lauoyt information
        try { d.getMetadata(); } catch (Exception ex) {}
        String chunkInfo = "";
        long[] chunks = d.getChunkSize();
        if (chunks == null)
            chunkInfo = "NONE";
        else
        {
            int n = chunks.length;
            chunkInfo = String.valueOf(chunks[0]);
            for (int i=1; i<n; i++)
            {
                chunkInfo += " X " + chunks[i];
            }
        }

        JPanel bPanel = new JPanel();
        bPanel.setBorder(new TitledBorder(""));
        bPanel.setLayout(new BorderLayout());
        lp = new JPanel();
        lp.setLayout(new GridLayout(2,1));
        lp.add(new JLabel("Chunking: "));
        lp.add(new JLabel("Compression: "));
        bPanel.add(lp, BorderLayout.WEST);

        rp = new JPanel();
        rp.setLayout(new GridLayout(2,1));
        rp.add(new JLabel(chunkInfo));
        rp.add(new JLabel(d.getCompression()));
        bPanel.add(rp, BorderLayout.CENTER);

        // getChunkInfo does not work for HDF4 files
        if (isH5)
            panel.add(bPanel, BorderLayout.SOUTH);

        return panel;
    }

    /**
     * Creates a panel used to display attribute information.
     */
    private JPanel createAttributePanel()
    {
        JPanel panel = new JPanel();
        panel.setLayout (new BorderLayout());
        //panel.setBorder(BorderFactory.createEmptyBorder(10,0,0,0));

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BorderLayout());
        attrNumberLabel = new JLabel("Number of attributes = 0");
        topPanel.add(attrNumberLabel, BorderLayout.WEST);

        FileFormat theFile = hObject.getFileFormat();
        JPanel bPanel = new JPanel();
        JButton b = new JButton(" Add ");
        b.setMnemonic('A');
        b.addActionListener(this);
        b.setActionCommand("Add attribute");
        bPanel.add(b);
        b.setEnabled(!theFile.isReadOnly());

        if (isH5)
        {
            // deleting is not supported by HDF4
            b = new JButton("Delete");
            b.setMnemonic('D');
            b.addActionListener(this);
            b.setActionCommand("Delete attribute");
            bPanel.add(b);
            b.setEnabled(!theFile.isReadOnly());
        }
        else
        {
            // cannot add attribute to HDF4 froup
            if (hObject instanceof Group &&
                ((Group)hObject).isRoot())
            {
                b.setEnabled(false);
            }
        }

        topPanel.add(bPanel, BorderLayout.EAST);

        panel.add(topPanel, BorderLayout.NORTH);

        List attrList = null;

        try {
            attrList = hObject.getMetadata();
        } catch (Exception ex) {
             attrList = null;
        }
        if (attrList != null) numAttributes = attrList.size();

        String[] columnNames = {"Name", "Value", "Type", "Array Size"};
        attrTableModel = new DefaultTableModel(columnNames, numAttributes);

        attrTable = new JTable(attrTableModel)
        {
            public boolean isCellEditable(int row, int column)
            {
                return (column == 1); // only value can be changed
            }

            public void editingStopped(ChangeEvent e)
            {
                int row = getEditingRow();
                int col = getEditingColumn();
                String oldValue = (String)getValueAt(row, col);

                super.editingStopped(e);

                Object source = e.getSource();

                if (source instanceof CellEditor)
                {
                    CellEditor editor = (CellEditor)source;
                    String newValue = (String)editor.getCellEditorValue();
                    setValueAt(oldValue, row, col); // set back to what it is
                    updateAttributeValue(newValue, row, col);
                }
            }

            public boolean isCellSelected(int row, int col)
            {
                if (getSelectedRow()==row && getSelectedColumn()==col)
                {
                    Object attrV = getValueAt(row, col);
                    if (attrV != null)
                        attrContentArea.setText(attrV.toString());
                }

                return super.isCellSelected(row, col);
            }
        };
        attrTable.setRowSelectionAllowed(false);
        attrTable.setCellSelectionEnabled(true);
        attrTable.getTableHeader().setReorderingAllowed(false);
        attrTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scroller1 = new JScrollPane(attrTable);
        attrContentArea = new JTextArea();
        String ftype = ViewProperties.getFontType();
        int fsize = ViewProperties.getFontSize();
        Font font = null;
        try { font = new Font(ftype, Font.PLAIN, fsize); }
        catch (Exception ex) {font = null; }
        if (font != null) attrContentArea.setFont(font);
        attrContentArea.setLineWrap(true);
        attrContentArea.setEditable(false);
        Insets m = new Insets(5,5,5,5);
        attrContentArea.setMargin(m);

        JScrollPane scroller2 = new JScrollPane(attrContentArea);
        JSplitPane splitPane = new JSplitPane(
            JSplitPane.VERTICAL_SPLIT,
            scroller1,
            scroller2);

        // set the divider location
        int h = Math.min((numAttributes+2)*attrTable.getRowHeight(),
                 scroller1.getPreferredSize().height-40);
        splitPane.setDividerLocation(h);
        panel.add(splitPane, BorderLayout.CENTER);

        if (attrList == null)
            return panel;

        Attribute attr = null;
        String name, type, size;
        attrNumberLabel.setText("Number of attributes = "+numAttributes);

        for (int i=0; i<numAttributes; i++)
        {
            attr = (Attribute)attrList.get(i);
            name = attr.getName();

            boolean isUnsigned = false;
            type = attr.getType().getDatatypeDescription();
            isUnsigned = attr.getType().isUnsigned();

            long dims[] = attr.getDataDims();
            size = String.valueOf(dims[0]);
            for (int j=1; j<dims.length; j++)
                size += " x " + dims[j];

            attrTable.setValueAt(name, i, 0);
            attrTable.setValueAt(attr.toString(", "), i, 1);
            attrTable.setValueAt(type, i, 2);
            attrTable.setValueAt(size, i, 3);
        }  //for (int i=0; i<n; i++)

        return panel;
    }

    /**
     * Creates a panel used to display HDF5 user block.
     */
    private JPanel createUserBlockPanel()
    {
        JPanel panel = new JPanel();

        userBlock = DefaultFileFilter.getHDF5UserBlock(hObject.getFile());

        panel.setLayout (new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10,0,0,0));
        userBlockArea = new JTextArea();
        userBlockArea.setEditable(false);
        userBlockArea.setLineWrap(true);
        Insets m = new Insets(5,5,5,5);
        userBlockArea.setMargin(m);

        String ftype = ViewProperties.getFontType();
        int fsize = ViewProperties.getFontSize();
        Font font = null;
        try { font = new Font(ftype, Font.PLAIN, fsize); }
        catch (Exception ex) {font = null; }
        if (font != null) userBlockArea.setFont(font);

        String[] displayChoices = {"Text", "Binary", "Octal", "Hexadecimal", "Decimal"};
        JComboBox userBlockDisplayChoice = new JComboBox(displayChoices);
        userBlockDisplayChoice.setSelectedIndex(0);
        userBlockDisplayChoice.addActionListener(this);
        userBlockDisplayChoice.setEditable(false);
        userBlockDisplayChoice.setActionCommand("Display user block as");

        JPanel topPane = new JPanel();
        topPane.setLayout(new BorderLayout(10,10));
        topPane.add(new JLabel("Display As:"), BorderLayout.WEST);
        topPane.add(userBlockDisplayChoice, BorderLayout.CENTER);
        JLabel sizeLabel = new JLabel("Header Size (Bytes): 0");
        topPane.add(sizeLabel, BorderLayout.EAST);

        JScrollPane scroller = new JScrollPane(userBlockArea);
        panel.add(topPane, BorderLayout.NORTH);
        panel.add(scroller, BorderLayout.CENTER);

        int headSize = 0;
        if (userBlock != null)
        {
            headSize = showUserBlockAs(0);
            sizeLabel.setText("Header Size (Bytes): "+headSize);
        }
        else
            userBlockDisplayChoice.setEnabled(false);

        return panel;
    }

    private int showUserBlockAs(int radix)
    {
        int headerSize = 0;

        if (userBlock == null)
            return 0;

        String userBlockInfo = null;
        if (radix==2|| radix==8 || radix ==16 || radix==10)
        {
            StringBuffer sb = new StringBuffer();
            for (headerSize=0; headerSize<userBlock.length; headerSize++)
            {
                int intValue = (int)userBlock[headerSize];
                if (intValue<0)
                    intValue +=256;
                else if (intValue == 0)
                    break; // null end
                sb.append(Integer.toString(intValue, radix));
                sb.append(" ");
            }
            userBlockInfo = sb.toString();
        } else
        {
            userBlockInfo = new String(userBlock).trim();
            if (userBlockInfo != null)
                headerSize = userBlockInfo.length();
        }

        userBlockArea.setText(userBlockInfo);

        return headerSize;
    }

    /** update attribute value. Currently can only update single data point.
     *  @param newValue the string of the new value.
     *  @param row the row number of the selected cell.
     *  @param col the column number of the selected cell.
     */
    private void updateAttributeValue(String newValue, int row, int col)
    {
        if (col != 1)
            return; // can only change attribute value

        String attrName = (String)attrTable.getValueAt(row, 0);

        List attrList = null;
        try { attrList = hObject.getMetadata(); }
        catch (Exception ex)
        {
            JOptionPane.showMessageDialog(getOwner(),
                ex.getMessage(),
                getTitle(),
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        Attribute attr = (Attribute)attrList.get(row);
        Object data = attr.getValue();
        if (data == null) return;

        int array_length = Array.getLength(data);
        StringTokenizer st = new StringTokenizer(newValue, ",");
        if (st.countTokens() < array_length)
        {
            JOptionPane.showMessageDialog(getOwner(),
                "More data value needed: "+newValue,
                getTitle(),
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        char NT = ' ';
        String cName = data.getClass().getName();
        int cIndex = cName.lastIndexOf("[");
        if (cIndex >= 0 ) NT = cName.charAt(cIndex+1);
        boolean isUnsigned = attr.isUnsigned();

        double d = 0;
        String theToken = null;
        long max=0, min=0;
        for (int i=0; i<array_length; i++)
        {
            max = min = 0;

            theToken = st.nextToken().trim();
            try {
                if (!(Array.get(data, i) instanceof String))
                    d = Double.parseDouble(theToken);
            }
            catch (NumberFormatException ex)
            {
                JOptionPane.showMessageDialog(
                    getOwner(),
                    ex.getMessage(),
                    getTitle(),
                    JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (isUnsigned && d < 0)
            {
                JOptionPane.showMessageDialog(
                    getOwner(),
                    "Negative value for unsigned integer: "+newValue,
                    getTitle(),
                    JOptionPane.ERROR_MESSAGE);
                return;
            }

            switch (NT)
            {
                case 'B':
                {
                    if (isUnsigned)
                    {
                        min = 0;
                        max = 255;
                    }
                    else
                    {
                        min = Byte.MIN_VALUE;
                        max = Byte.MAX_VALUE;
                    }

                    if (d > max || d < min)
                        JOptionPane.showMessageDialog(
                            getOwner(),
                            "Data is out of range["+min+", "+max+"]: "+newValue,
                            getTitle(),
                            JOptionPane.ERROR_MESSAGE);
                    else
                        Array.setByte  (data, i, (byte)d);
                    break;
                }
                case 'S':
                {
                    if (isUnsigned)
                    {
                        min = 0;
                        max = 65535;
                    }
                    else
                    {
                        min = Short.MIN_VALUE;
                        max = Short.MAX_VALUE;
                    }

                    if (d > max || d < min)
                        JOptionPane.showMessageDialog(
                            getOwner(),
                            "Data is out of range["+min+", "+max+"]: "+newValue,
                            getTitle(),
                            JOptionPane.ERROR_MESSAGE);
                    else
                        Array.setShort (data, i, (short)d);
                    break;
                }
                case 'I':
                {
                    if (isUnsigned)
                    {
                        min = 0;
                        max = 4294967295L;
                    }
                    else
                    {
                        min = Integer.MIN_VALUE;
                        max = Integer.MAX_VALUE;
                    }

                    if (d > max || d < min)
                        JOptionPane.showMessageDialog(
                            getOwner(),
                            "Data is out of range["+min+", "+max+"]: "+newValue,
                            getTitle(),
                            JOptionPane.ERROR_MESSAGE);
                    else
                        Array.setInt   (data, i, (int)d);
                    break;
                }
                case 'J':
                    Array.setLong  (data, i, (long)d); break;
                case 'F':
                    Array.setFloat (data, i, (float)d); break;
                case 'D':
                default:  Array.set      (data, i, newValue); break;
            }
        }

        try {
            hObject.getFileFormat().writeAttribute(hObject, attr, true);
        } catch (Exception ex)
        {
            JOptionPane.showMessageDialog(
                getOwner(),
                ex.getMessage(),
                getTitle(),
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        // update the attribute table
        attrTable.setValueAt(attr.toString(", "), row, 1);
    }

}
