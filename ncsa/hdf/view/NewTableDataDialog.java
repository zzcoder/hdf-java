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
import javax.swing.event.*;
import javax.swing.text.*;
import javax.swing.text.html.*;
import javax.swing.*;
import javax.swing.table.*;
import javax.swing.border.TitledBorder;
import java.awt.Color;
import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Dimension;
import java.awt.Component;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.io.File;
import java.util.*;
import ncsa.hdf.object.*;
import java.net.URL;
import java.net.URLClassLoader;
import ncsa.hdf.object.*;

/**
 * NewTableDataDialog shows a message dialog requesting user input for creating
 * a new HDF4/5 dataset.
 * <p>
 * @version 1.3.0 04/25/2002
 * @author Peter X. Cao
 */
public class NewTableDataDialog extends JDialog
implements ActionListener
{
    private static final String[] DATATYPE_NAMES = {
        "byte (8-bit)",                // 0
        "short (16-bit)",              // 1
        "int (32-bit)",                // 2
        "unsigned byte (8-bit)",       // 3
        "unsigned short (16-bit)",     // 4
        "unsigned int (32-bit)",       // 5
        "long (64-bit)",               // 6
        "float",                       // 7
        "double",                      // 8
        "string"                       // 9
    };

    private FileFormat fileformat;

    private JTextField nameField, dimsField;

    private JComboBox parentChoice;

    private JComboBox nFieldBox;

    private boolean isH5;

    /** a list of current groups */
    private List groupList;

    private HObject newObject;

    private final Toolkit toolkit;

    private final DataView dataView;

    private int numberOfMembers;

    private JTable table;

    private DefaultTableModel tableModel;

    private RowEditorModel rowEditorModel;

    private DefaultCellEditor cellEditor;

    /** Constructs NewTableDataDialog with specified list of possible parent groups.
     *  @param owner the owner of the input
     *  @param pGroup the parent group which the new group is added to.
     *  @param objs the list of all objects.
     */
    public NewTableDataDialog(JFrame owner, Group pGroup, List objs)
    {
        super (owner, "New Compound Dataset...", true);

        newObject = null;
        dataView = null;
        numberOfMembers = 5;
        fileformat = pGroup.getFileFormat();

        JComboBox cb = new JComboBox(DATATYPE_NAMES);
        cellEditor = new DefaultCellEditor(cb);
        rowEditorModel = new RowEditorModel(numberOfMembers, cellEditor);
        String[] colNames = {"Name", "Datatype", "Array size / String length"};
        tableModel =  new DefaultTableModel( colNames, numberOfMembers);
        table = new JTable(tableModel)
        {
            RowEditorModel rm = rowEditorModel;
            public TableCellEditor getCellEditor(int row, int col)
            {
                TableCellEditor cellEditor = rm.getEditor(row);

                if (cellEditor==null || !(col==1))
                   cellEditor =  super.getCellEditor(row,col);

                return cellEditor;
            }
        };
        table.setRowSelectionAllowed(false);
        table.setColumnSelectionAllowed(false);

        toolkit = Toolkit.getDefaultToolkit();
        isH5 = pGroup.getFileFormat().isThisType(FileFormat.getFileFormat(FileFormat.FILE_TYPE_HDF5));

        parentChoice = new JComboBox();
        String[] memberSizes = new String[100];
        for (int i=0; i<100; i++) memberSizes[i] = String.valueOf(i+1);

        nFieldBox = new JComboBox(memberSizes);
        nFieldBox.setEditable(true);
        nFieldBox.addActionListener(this);
        nFieldBox.setActionCommand("Change number of members");
        nFieldBox.setSelectedItem(String.valueOf(numberOfMembers));

        groupList = new Vector();
        Object obj = null;
        Iterator iterator = objs.iterator();
        while (iterator.hasNext())
        {
            obj = iterator.next();
            if (obj instanceof Group)
            {
                Group g = (Group)obj;
                groupList.add(obj);
                if (g.isRoot())
                    parentChoice.addItem(HObject.separator);
                else
                    parentChoice.addItem(g.getPath()+g.getName()+HObject.separator);
            }
        }

        if (pGroup.isRoot())
            parentChoice.setSelectedItem(HObject.separator);
        else
            parentChoice.setSelectedItem(pGroup.getPath()+pGroup.getName()+HObject.separator);

        JPanel contentPane = (JPanel)getContentPane();
        contentPane.setLayout(new BorderLayout(5,5));
        contentPane.setBorder(BorderFactory.createEmptyBorder(15,5,5,5));
        contentPane.setPreferredSize(new Dimension(550, 300));

        JButton okButton = new JButton("   Ok   ");
        okButton.setActionCommand("Ok");
        okButton.setMnemonic(KeyEvent.VK_O);
        okButton.addActionListener(this);

        JButton cancelButton = new JButton("Cancel");
        cancelButton.setMnemonic(KeyEvent.VK_C);
        cancelButton.setActionCommand("Cancel");
        cancelButton.addActionListener(this);

        // set NAME and PARENT GROUP panel
        JPanel namePanel = new JPanel();
        namePanel.setLayout(new BorderLayout(5,5));
        JPanel tmpP = new JPanel();
        tmpP.setLayout(new GridLayout(2,1));
        tmpP.add(new JLabel("   Dataset name: "));
        tmpP.add(new JLabel("   Parent group: "));
        namePanel.add(tmpP, BorderLayout.WEST);
        tmpP = new JPanel();
        tmpP.setLayout(new GridLayout(2,1));
        tmpP.add(nameField=new JTextField());
        tmpP.add(parentChoice);
        namePanel.add(tmpP, BorderLayout.CENTER);

        // table size panel
        JPanel tabelSizePanel = new JPanel();
        tabelSizePanel.setLayout(new GridLayout(1,4));
        tabelSizePanel.add(new JLabel(" Number of Members:"));
        tabelSizePanel.add(nFieldBox);
        String labelStr = "    Number of Records:";
        if (isH5) labelStr = "   Size of Dimensions:";
        tabelSizePanel.add(new JLabel(labelStr));
        tabelSizePanel.add(dimsField=new JTextField("e.g., 5, 50x10, etc."));
        TitledBorder border = new TitledBorder("Dataset Size");
        border.setTitleColor(new Color(100, 100, 200));
        tabelSizePanel.setBorder(border);

        tmpP = new JPanel();
        tmpP.setLayout(new BorderLayout(5,5));
        tmpP.add(namePanel, BorderLayout.CENTER);
        tmpP.add(tabelSizePanel, BorderLayout.SOUTH);

        contentPane.add(tmpP, BorderLayout.NORTH);

        JScrollPane scroller = new JScrollPane(table);
        border = new TitledBorder("Compound Datatype Properties");
        border.setTitleColor(new Color(100, 100, 200));
        scroller.setBorder(border);
        contentPane.add(scroller, BorderLayout.CENTER);

        // set OK and CANCEL buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);
        contentPane.add(buttonPanel, BorderLayout.SOUTH);

        // locate the H5Property dialog
        Point l = owner.getLocation();
        l.x += 250;
        l.y += 120;
        setLocation(l);
        validate();
        pack();
    }

    public void actionPerformed(ActionEvent e)
    {
        Object source = e.getSource();
        String cmd = e.getActionCommand();

        if (cmd.equals("Ok"))
        {
            try { newObject = createCompoundDS(); }
            catch (Exception ex)
            {
                JOptionPane.showMessageDialog(this,
                    ex,
                    getTitle(),
                    JOptionPane.ERROR_MESSAGE);
            }

            if (newObject != null)
                dispose();
        }
        else if (cmd.equals("Cancel"))
        {
            newObject = null;
            dispose();
        }
        else if (cmd.equals("Change number of members"))
        {
            int n = 0;

            try { n = Integer.valueOf((String)nFieldBox.getSelectedItem()).intValue(); }
            catch (Exception ex) {}

            if (n == numberOfMembers)
                return;

            tableModel.setRowCount(n);
            for (int i=numberOfMembers; i<n; i++)
            {
                rowEditorModel.addEditorForRow(i, cellEditor);
            }
            numberOfMembers = n;
        }
    }

    private HObject createCompoundDS() throws Exception
    {
        HObject obj = null;

        String dname = nameField.getText();
        if (dname == null || dname.length()<=0)
            throw new IllegalArgumentException("Dataset name is empty");

        Group pgroup = (Group)groupList.get(parentChoice.getSelectedIndex());
        if (pgroup == null)
            throw new IllegalArgumentException("Invalid parent group");

        String dimStr = dimsField.getText();
        if (dimStr == null || dimStr.length() <=0)
            throw new IllegalArgumentException("Invalid dimension size");

        StringTokenizer st = new StringTokenizer(dimStr, "x");
        int n = st.countTokens();
        int idx = 0;
        long[] dims = new long[n];
        while (st.hasMoreTokens())
        {
            try { dims[idx++] = Long.valueOf(st.nextToken().trim()).longValue(); }
            catch (Exception ex)
            {
                throw new IllegalArgumentException("Invalid dimension size: "+dimStr);
            }
        }

        n = table.getRowCount();
        if (n<=0) return null;

        String[] mNames = new String[n];
        Datatype[] mDatatypes = new Datatype[n];
        int[] mOrders = new int[n];

        for (int i=0; i<n; i++)
        {
            String name = (String)table.getValueAt(i, 0);
            if (name == null || name.length() <=0)
                throw new IllegalArgumentException("Member name is empty");
            mNames[i] = name;

            int order = 1;
            String orderStr = (String)table.getValueAt(i, 2);
            if (orderStr != null)
                order = Integer.valueOf(orderStr).intValue();
            mOrders[i] = order;

            String typeName = (String)table.getValueAt(i, 1);
            Datatype type = null;
            if (DATATYPE_NAMES[0].equals(typeName))
            {
                type = fileformat.createDatatype(Datatype.CLASS_INTEGER, 1, Datatype.NATIVE, Datatype.NATIVE);
            }
            else if (DATATYPE_NAMES[1].equals(typeName))
            {
                type = fileformat.createDatatype(Datatype.CLASS_INTEGER, 2, Datatype.NATIVE, Datatype.NATIVE);
            }
            else if (DATATYPE_NAMES[2].equals(typeName))
            {
                type = fileformat.createDatatype(Datatype.CLASS_INTEGER, 4, Datatype.NATIVE, Datatype.NATIVE);
            }
            else if (DATATYPE_NAMES[3].equals(typeName))
            {
                type = fileformat.createDatatype(Datatype.CLASS_INTEGER, 1, Datatype.NATIVE, Datatype.SIGN_NONE);
            }
            else if (DATATYPE_NAMES[4].equals(typeName))
            {
                type = fileformat.createDatatype(Datatype.CLASS_INTEGER, 2, Datatype.NATIVE, Datatype.SIGN_NONE);
            }
            else if (DATATYPE_NAMES[5].equals(typeName))
            {
                type = fileformat.createDatatype(Datatype.CLASS_INTEGER, 4, Datatype.NATIVE, Datatype.SIGN_NONE);
            }
            else if (DATATYPE_NAMES[6].equals(typeName))
            {
                type = fileformat.createDatatype(Datatype.CLASS_INTEGER, 8, Datatype.NATIVE, Datatype.NATIVE);
            }
            else if (DATATYPE_NAMES[7].equals(typeName))
            {
                type = fileformat.createDatatype(Datatype.CLASS_FLOAT, 4, Datatype.NATIVE, Datatype.NATIVE);
            }
            else if (DATATYPE_NAMES[8].equals(typeName))
            {
                type = fileformat.createDatatype(Datatype.CLASS_FLOAT, 8, Datatype.NATIVE, Datatype.NATIVE);
            }
            else if (DATATYPE_NAMES[9].equals(typeName))
            {
                type = fileformat.createDatatype(Datatype.CLASS_STRING, order, Datatype.NATIVE, Datatype.NATIVE);
            }
            else
            {
                throw new IllegalArgumentException("Invalid data type.");
            }
            mDatatypes[i] = type;
        } // for (int i=0; i<n; i++)

        obj = fileformat.createCompoundDS(dname, pgroup, dims, mNames, mDatatypes, mOrders, null);

        return obj;
    }

    /** Returns the new dataset created. */
    public DataFormat getObject() { return newObject; }

    /** Returns the parent group of the new dataset. */
    public Group getParentGroup() {
        return (Group)groupList.get(parentChoice.getSelectedIndex());
    }

    private class RowEditorModel
    {
        private Hashtable data;

        public RowEditorModel()
        {
            data = new Hashtable();
        }

        // all rows has the same cell editor
        public RowEditorModel(int rows, TableCellEditor ed)
        {
            data = new Hashtable();
            for (int i=0; i<rows; i++)
                data.put(new Integer(i), ed);
        }

        public void addEditorForRow(int row, TableCellEditor e )
        {
            data.put(new Integer(row), e);
        }

        public void removeEditorForRow(int row)
        {
            data.remove(new Integer(row));
        }

        public TableCellEditor getEditor(int row)
        {
            return (TableCellEditor)data.get(new Integer(row));
        }
    }
}
