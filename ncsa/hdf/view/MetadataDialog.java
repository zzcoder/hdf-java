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
import javax.swing.table.*;
import java.awt.Frame;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Point;
import java.util.List;
import java.lang.reflect.Array;

/**
 * MetadataDialog is an dialog window used to show data properties.
 * Data properties include attributes and general information such as
 * object type, data type and data space.
 * <p>
 * @version 1.3.0 1/26/2002
 * @author Peter X. Cao
 */
public class MetadataDialog extends JDialog
implements ActionListener
{
    /**
     * The main HDFView.
     */
    private final ViewManager viewer;

    /** The HDF data object */
    private HObject hObject;

    private JTabbedPane tabbedPane = null;
    private JTextArea attrContentArea;

    /**
     * Constructs a MetadataDialog with the given HDFView.
     */
    public MetadataDialog(ViewManager theview)
    {
        super((Frame)theview, false);
        setDefaultCloseOperation(JInternalFrame.DISPOSE_ON_CLOSE);

        viewer = theview;
        hObject = (HObject)theview.getSelectedObject();

        if (hObject == null)
            dispose();
        else
            setTitle("Properties - "+hObject.getPath()+hObject.getName());

        tabbedPane = new JTabbedPane();

        tabbedPane.addTab("General", createGeneralPropertyPanel());
        tabbedPane.addTab("Attributes", createAttributePanel());
        tabbedPane.setSelectedIndex(0);

        JPanel bPanel = new JPanel();
        JButton b = new JButton("  Close  ");
        b.setMnemonic(KeyEvent.VK_C);
        b.setActionCommand("Close");
        b.addActionListener(this);
        bPanel.add(b);

        //Add the tabbed pane to this panel.
        JPanel contentPane = (JPanel)getContentPane();
        contentPane.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));

        contentPane.setLayout(new BorderLayout());
        contentPane.add("Center", tabbedPane);
        contentPane.add("South", bPanel);

        // locate the H5Property dialog
        Point l = getParent().getLocation();
        l.x += 250;
        l.y += 80;
        setLocation(l);
        setSize(400, 500);
        validate();
    }

    public void actionPerformed(ActionEvent e)
    {
        Object source = e.getSource();
        String cmd = e.getActionCommand();

        if (cmd.equals("Close"))
        {
            dispose();
        }
    }

    /**
     * Creates a panel used to dispaly general information of HDF object.
     */
    private JPanel createGeneralPropertyPanel()
    {
        JPanel panel = new JPanel();
        panel.setLayout (new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10,0,0,0));

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BorderLayout());

        JPanel lp = new JPanel();
        lp.setLayout(new GridLayout(3,1));
        lp.add(new JLabel("Name: "));
        lp.add(new JLabel("Path: "));
        lp.add(new JLabel("Type: "));

        JPanel rp = new JPanel();
        rp.setLayout(new GridLayout(3,1));

        JTextField nameField = new JTextField(hObject.getName());
        nameField.setEditable(false);
        rp.add(nameField);

        JTextField pathField = new JTextField(hObject.getPath());
        pathField.setEditable(false);
        rp.add(pathField);

        String typeStr = "Unknown";
        if (hObject instanceof H4Group)
        {
            if (((H4Group)hObject).isRoot())
                typeStr = "HDF4 File";
            else
                typeStr = "HDF4 Vgroup";
        } else if (hObject instanceof H5Group)
        {
            if (((H5Group)hObject).isRoot())
                typeStr = "HDF5 File";
            else
                typeStr = "HDF5 Group";
        } else if (hObject instanceof H4GRImage)
        {
            typeStr = "HDF4 GR Image";
        } else if (hObject instanceof H4SDS)
        {
            typeStr = "HDF4 SDS";
        } else if (hObject instanceof H4Vdata)
        {
            typeStr = "HDF4 Vdata";
        } else if (hObject instanceof H5ScalarDS)
        {
            typeStr = "HDF5 Scalar Dataset";
        } else if (hObject instanceof H5CompoundDS)
        {
            typeStr = "HDF5 Compound Dataset";
        }
        JTextField typeField = new JTextField(typeStr);
        typeField.setEditable(false);
        rp.add(typeField);

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
            rowData[i][1] = theObj.getClass().getName().substring(16);
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
        panel.add(scroller, BorderLayout.CENTER);
        panel.setBorder(new TitledBorder("Group members"));

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
        if (d instanceof H5ScalarDS)
        {
            H5ScalarDS sd = (H5ScalarDS)d;
            typeStr = H5Accessory.getDatatypeDescription(sd.getDataType());
        } else if (d instanceof H5CompoundDS)
        {
            typeStr = "Compound";
        } else if (d instanceof H4Vdata)
        {
            typeStr = "Vdata";
        } else if (d instanceof H4GRImage || d instanceof H4SDS)
        {
            ScalarDS sd = (ScalarDS)d;
            typeStr = H4Accessory.getDatatypeDescription(sd.getDataType());
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

                for (int i=0; i<n; i++)
                {
                    rowData[i][0] = names[i];
                    rowData[i][2] = String.valueOf(orders[i]);
                    if (compound instanceof H4Vdata)
                        rowData[i][1] = H4Accessory.getDatatypeDescription(types[i]);
                    else
                        rowData[i][1] = H5Accessory.getDatatypeDescription(types[i]);
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

        return panel;
    }

    /**
     * Creates a panel used to display attribute information.
     */
    private JPanel createAttributePanel()
    {
        JPanel panel = new JPanel();
        panel.setLayout (new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10,0,0,0));

        List attrList = null;

        try {
            attrList = hObject.getMetadata();
        } catch (Exception ex) {
            viewer.showStatus(ex.toString());
            attrList = null;
        }

        if (attrList == null)
            return panel;

        int numAttributes = attrList.size();
        if (numAttributes <=0 )
            return panel;

        String[] columnNames = {"Name", "Value", "Type", "Array Size"};
        DefaultTableModel attrTableModel = new DefaultTableModel(
            columnNames,
            numAttributes);

        JTable attrTable = new JTable(attrTableModel)
        {
            public boolean isCellEditable(int row, int column)
            {
                    return false;
            }

            public boolean isCellSelected(int row, int col)
            {
                if (getSelectedRow()==row && getSelectedColumn()==col)
                {
                    attrContentArea.setText(getValueAt(row, col).toString());
                }

                return super.isCellSelected(row, col);
            }
        };
        attrTable.setRowSelectionAllowed(false);
        attrTable.setCellSelectionEnabled(true);
        attrTable.getTableHeader().setReorderingAllowed(false);

        JScrollPane scroller1 = new JScrollPane(attrTable);
        attrContentArea = new JTextArea();
        attrContentArea.setLineWrap(true);
        attrContentArea.setEditable(false);
        JScrollPane scroller2 = new JScrollPane(attrContentArea);
        JSplitPane splitPane = new JSplitPane(
            JSplitPane.VERTICAL_SPLIT,
            scroller2,
            scroller1);
        splitPane.setDividerLocation(50);
        panel.add("Center", splitPane);

        Attribute attr = null;
        String name, type, size;
        for (int i=0; i<numAttributes; i++)
        {
            attr = (Attribute)attrList.get(i);
            name = attr.getName();

            boolean isUnsigned = false;
            if (hObject instanceof H5ScalarDS ||
                hObject instanceof H5CompoundDS)
            {
                type = H5Accessory.getDatatypeDescription(attr.getType());
                isUnsigned = H5Accessory.isUnsigned(attr.getType());
            }
            else
            {
                type = H4Accessory.getDatatypeDescription(attr.getType());
                isUnsigned = H4Accessory.isUnsigned(attr.getType());
            }

            long dims[] = attr.getDataDims();
            size = String.valueOf(dims[0]);
            for (int j=1; j<dims.length; j++)
                size += " x " + dims[j];

            attrTable.setValueAt(name, i, 0);
            attrTable.setValueAt(attr.toString(", ", isUnsigned), i, 1);
            attrTable.setValueAt(type, i, 2);
            attrTable.setValueAt(size, i, 3);
        }  //for (int i=0; i<n; i++)

        return panel;
    }
}
