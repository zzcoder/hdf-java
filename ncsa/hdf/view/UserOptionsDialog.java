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

import javax.swing.*;
import java.io.*;
import java.util.*;
import java.awt.event.*;
import javax.swing.event.*;
import javax.swing.border.*;

import ncsa.hdf.object.HObject;

import java.awt.Color;
import java.awt.Frame;
import java.awt.Dimension;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.GraphicsEnvironment;
import java.awt.Point;

/** UserOptionsDialog displays components for choosing user options. */
public class UserOptionsDialog extends JDialog
implements ActionListener, ListSelectionListener
{
	public static final long serialVersionUID = HObject.serialVersionUID;

    /**
     * The main HDFView.
     */
    private final JFrame viewer;

    private String H4toH5Path;
    private JTextField H4toH5Field, UGField, workField, fileExtField,
            maxMemberField, startMemberField;
    private JComboBox fontSizeChoice, fontTypeChoice, delimiterChoice;
    private JComboBox choiceTreeView, choiceMetaDataView, choiceTextView,
            choiceTableView, choiceImageView, choicePaletteView;
    private String rootDir, workDir;
    private JCheckBox checkCurrentUserDir;
    private JButton currentDirButton;

    private int fontSize;

    private boolean isFontChanged;

    private boolean isUserGuideChanged;

    private boolean isWorkDirChanged;

    /** a list of tree view implementation. */
    private static Vector treeViews;

    /** a list of image view implementation. */
    private static Vector imageViews;

    /** a list of tree table implementation. */
    private static Vector tableViews;

    /** a list of Text view implementation. */
    private static Vector textViews;

    /** a list of metadata view implementation. */
    private static Vector metaDataViews;

    /** a list of palette view implementation. */
    private static Vector paletteViews;

    private JList srbJList;

    private JTextField srbFields[];

    private Vector srbVector;

    /** constructs an UserOptionsDialog.
     * @param view The HDFView.
     */
    public UserOptionsDialog(JFrame view, String viewroot)
    {
        super ((Frame)view, "User Options", true);

        viewer = view;
        rootDir = viewroot;
        isFontChanged = false;
        isUserGuideChanged = false;
        isWorkDirChanged = false;
        srbJList = null;
        fontSize = ViewProperties.getFontSize();
        workDir = ViewProperties.getWorkDir();
        if (workDir == null) workDir = rootDir;
        treeViews = ViewProperties.getTreeViewList();
        metaDataViews = ViewProperties.getMetaDataViewList();
        textViews = ViewProperties.getTextViewList();
        tableViews = ViewProperties.getTableViewList();
        imageViews = ViewProperties.getImageViewList();
        paletteViews = ViewProperties.getPaletteViewList();
        srbVector = ViewProperties.getSrbAccount();

        JPanel contentPane = (JPanel)getContentPane();
        contentPane.setLayout(new BorderLayout(8,8));
        contentPane.setBorder(BorderFactory.createEmptyBorder(15,5,5,5));
        
        int w = 600 + (ViewProperties.getFontSize()-12)*15;
        int h = 500 + (ViewProperties.getFontSize()-12)*15;
        contentPane.setPreferredSize(new Dimension(w, h));

        JTabbedPane tabbedPane = new JTabbedPane();

        tabbedPane.addTab("General Setting", createGeneralOptionPanel());
        tabbedPane.addTab("Default Module", createModuleOptionPanel());

        try {
             Class.forName("ncsa.hdf.srb.SRBFileDialog");
             tabbedPane.addTab("SRB Connection", createSrbConnectionPanel());
        } catch (Exception ex) {;}

        tabbedPane.setSelectedIndex(0);

        JPanel buttonP = new JPanel();
        JButton b = new JButton("   Ok   ");
        b.setActionCommand("Set options");
        b.addActionListener(this);
        buttonP.add(b);
        b = new JButton("Cancel");
        b.setActionCommand("Cancel");
        b.addActionListener(this);
        buttonP.add(b);

        contentPane.add("Center", tabbedPane);
        contentPane.add("South", buttonP);

        // locate the H5Property dialog
        Point l = getParent().getLocation();
        l.x += 250;
        l.y += 80;
        setLocation(l);
        validate();
        pack();
    }

    public void setVisible(boolean b) {
    	if (b) { // reset flags
    		isFontChanged = false;
    		isUserGuideChanged = false;
    		isWorkDirChanged = false;    
            fontSize = ViewProperties.getFontSize();
            workDir = ViewProperties.getWorkDir();
            if (workDir == null) workDir = rootDir;
    	}
    	super.setVisible(b);
    }
    
    private JPanel createGeneralOptionPanel() 
    {
        String[] fontSizeChoices = {"12", "14", "16", "18", "20", "22", "24", "26", "28", "30", "32", "34", "36", "48"};
        fontSizeChoice = new JComboBox(fontSizeChoices);
        fontSizeChoice.setSelectedItem(String.valueOf(ViewProperties.getFontSize()));

        String[] fontNames = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
        String fname = ViewProperties.getFontType();
        fontTypeChoice = new JComboBox(fontNames);

        boolean isFontValid = false;
        if (fontNames != null)
        {
            for (int i=0; i<fontNames.length; i++)
            {
                if (fontNames[i].equalsIgnoreCase(fname))
                    isFontValid = true;
            }
        }
        if (!isFontValid)
        {
            fname =((JFrame)viewer).getFont().getFamily();
            ViewProperties.setFontType(fname);
        }
        fontTypeChoice.setSelectedItem(fname);

        String[] delimiterChoices = {
            ViewProperties.DELIMITER_TAB,
            ViewProperties.DELIMITER_COMMA,
            ViewProperties.DELIMITER_SPACE,
            ViewProperties.DELIMITER_COLON,
            ViewProperties.DELIMITER_SEMI_COLON};

        delimiterChoice = new JComboBox(delimiterChoices);
        delimiterChoice.setSelectedItem(ViewProperties.getDataDelimiter());

        JPanel centerP = new JPanel();
        centerP.setLayout(new GridLayout(7,1,10,10));
        centerP.setBorder(new SoftBevelBorder(BevelBorder.LOWERED));

        JPanel p0 = new JPanel();
        p0.setLayout(new BorderLayout());
        p0.add(checkCurrentUserDir=new JCheckBox("\"user.dir\"         or", false), BorderLayout.WEST);
        checkCurrentUserDir.addActionListener(this);
        checkCurrentUserDir.setActionCommand("Set current dir to user.dir");
        p0.add(workField = new JTextField(workDir), BorderLayout.CENTER);
        JButton b = new JButton("Browse...");
        currentDirButton = b;
        b.setActionCommand("Browse current dir");
        b.addActionListener(this);
        p0.add(b, BorderLayout.EAST);
        TitledBorder tborder = new TitledBorder("Default Working Directory");
        tborder.setTitleColor(Color.darkGray);
        p0.setBorder(tborder);
        centerP.add(p0);

        p0 = new JPanel();
        p0.setLayout(new BorderLayout());
        p0.add(new JLabel("User's Guide:  "), BorderLayout.WEST);
        p0.add(UGField = new JTextField(ViewProperties.getUsersGuide()), BorderLayout.CENTER);
        b = new JButton("Browse...");
        b.setActionCommand("Browse UG");
        b.addActionListener(this);
        p0.add(b, BorderLayout.EAST);
        tborder = new TitledBorder("Help Document");
        tborder.setTitleColor(Color.darkGray);
        p0.setBorder(tborder);
        centerP.add(p0);

        p0 = new JPanel();
        p0.setLayout(new BorderLayout());
        p0.add(new JLabel("Extension: "), BorderLayout.WEST);
        p0.add(fileExtField=new JTextField(ViewProperties.getFileExtension()), BorderLayout.CENTER);
        tborder = new TitledBorder("File Extension");
        tborder.setTitleColor(Color.darkGray);
        p0.setBorder(tborder);
        centerP.add(p0);

        p0 = new JPanel();
        p0.setLayout(new GridLayout(1,2,8,8));
        JPanel p00 = new JPanel();
        p00.setLayout(new BorderLayout());
        p00.add(new JLabel("Font Size: "), BorderLayout.WEST);
        p00.add(fontSizeChoice, BorderLayout.CENTER);
        p0.add(p00);
        p00 = new JPanel();
        p00.setLayout(new BorderLayout());
        p00.add(new JLabel("Font Type: "), BorderLayout.WEST);
        p00.add(fontTypeChoice, BorderLayout.CENTER);
        p0.add(p00);
        tborder = new TitledBorder("Text Font");
        tborder.setTitleColor(Color.darkGray);
        p0.setBorder(tborder);
        centerP.add(p0);

        p0 = new JPanel();
        p0.setLayout(new BorderLayout());
        p0.add(new JLabel("Data Delimiter:  "), BorderLayout.WEST);
        p0.add(delimiterChoice, BorderLayout.CENTER);
        tborder = new TitledBorder("Text Data Input/Output");
        tborder.setTitleColor(Color.darkGray);
        p0.setBorder(tborder);
        centerP.add(p0);

        p0 = new JPanel();
        p0.setLayout(new GridLayout(1,2,8,8));
        p00 = new JPanel();
        p00.setLayout(new BorderLayout());
        p00.add(new JLabel("Max Members: "), BorderLayout.WEST);

        p00.add(maxMemberField=new JTextField(String.valueOf(
                ViewProperties.getMaxMembers())), BorderLayout.CENTER);
        p0.add(p00);
        p00 = new JPanel();
        p00.setLayout(new BorderLayout());
        p00.add(new JLabel("Start Member: "), BorderLayout.WEST);
        p00.add(startMemberField=new JTextField(String.valueOf(
                ViewProperties.getStartMembers())), BorderLayout.CENTER);
        p0.add(p00);
        tborder = new TitledBorder("Max Number of Members to Load in Each Group");
        tborder.setTitleColor(Color.darkGray);
        p0.setBorder(tborder);
        centerP.add(p0);

        return centerP;
    }

    private JPanel createModuleOptionPanel() {
        choiceTreeView = new JComboBox(treeViews);
        choiceTableView = new JComboBox(tableViews);
        choiceTextView = new JComboBox(textViews);
        choiceImageView = new JComboBox(imageViews);
        choiceMetaDataView = new JComboBox(metaDataViews);
        choicePaletteView = new JComboBox(paletteViews);


        JPanel moduleP = new JPanel();
        moduleP.setLayout(new GridLayout(6,1,10,10));
        moduleP.setBorder(new SoftBevelBorder(BevelBorder.LOWERED));

        JPanel treeP = new JPanel();
        TitledBorder tborder = new TitledBorder("TreeView");
        tborder.setTitleColor(Color.darkGray);
        treeP.setBorder(tborder);
        moduleP.add(treeP);
        treeP.setLayout(new BorderLayout(5,5));
        treeP.add(choiceTreeView, BorderLayout.CENTER);

        JPanel attrP = new JPanel();
        tborder = new TitledBorder("MetaDataView");
        tborder.setTitleColor(Color.darkGray);
        attrP.setBorder(tborder);
        moduleP.add(attrP);
        attrP.setLayout(new BorderLayout(5,5));
        attrP.add(choiceMetaDataView, BorderLayout.CENTER);

        JPanel textP = new JPanel();
        tborder = new TitledBorder("TextView");
        tborder.setTitleColor(Color.darkGray);
        textP.setBorder(tborder);
        moduleP.add(textP);
        textP.setLayout(new BorderLayout(5,5));
        textP.add(choiceTextView, BorderLayout.CENTER);

        JPanel tableP = new JPanel();
        tborder = new TitledBorder("TableView");
        tborder.setTitleColor(Color.darkGray);
        tableP.setBorder(tborder);
        moduleP.add(tableP);
        tableP.setLayout(new BorderLayout(5,5));
        tableP.add(choiceTableView, BorderLayout.CENTER);

        JPanel imageP = new JPanel();
        tborder = new TitledBorder("ImageView");
        tborder.setTitleColor(Color.darkGray);
        imageP.setBorder(tborder);
        moduleP.add(imageP);
        imageP.setLayout(new BorderLayout(5,5));
        imageP.add(choiceImageView, BorderLayout.CENTER);

        JPanel palP = new JPanel();
        tborder = new TitledBorder("PaletteView");
        tborder.setTitleColor(Color.darkGray);
        palP.setBorder(tborder);
        moduleP.add(palP);
        palP.setLayout(new BorderLayout(5,5));
        palP.add(choicePaletteView, BorderLayout.CENTER);

        return moduleP;
    }

    private JPanel createSrbConnectionPanel() {
        JPanel p = new JPanel();
        p.setLayout(new BorderLayout(5,5));
        TitledBorder tborder = new TitledBorder("SRB Connections");
        tborder.setTitleColor(Color.darkGray);
        p.setBorder(tborder);

        DefaultListModel listModel = new DefaultListModel();
        srbJList = new JList(listModel);
        srbJList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        srbJList.addListSelectionListener(this);

        srbFields = new JTextField[7];

        if (srbVector!= null) {
            int n=srbVector.size();

            String srbaccount[] = null;
            for (int i=0; i<n; i++) {
                srbaccount = (String[])srbVector.get(i);
                if (srbaccount != null)
                    listModel.addElement(srbaccount[0]);
            }
        }

        JPanel cp = new JPanel();
        cp.setLayout(new BorderLayout(5,5));

        JPanel cpc = new JPanel();
        cpc.setLayout(new GridLayout(7,1,5,5));
        cpc.add(srbFields[0] = new JTextField());
        cpc.add(srbFields[1] = new JTextField());
        cpc.add(srbFields[2] = new JTextField());
        cpc.add(srbFields[3] = new JTextField());
        cpc.add(srbFields[4] = new JTextField());
        cpc.add(srbFields[5] = new JTextField());
        cpc.add(srbFields[6] = new JTextField());
        cp.add(cpc, BorderLayout.CENTER);

        JPanel cpl = new JPanel();
        cpl.setLayout(new GridLayout(7,1,5,5));
        cpl.add(new JLabel("Host Machine: ", JLabel.RIGHT));
        cpl.add(new JLabel("Port Number: ", JLabel.RIGHT));
        cpl.add(new JLabel("User Name: ", JLabel.RIGHT));
        cpl.add(new JLabel("Password: ", JLabel.RIGHT));
        cpl.add(new JLabel("Home Directory: ", JLabel.RIGHT));
        cpl.add(new JLabel("mdas Domain Name: ", JLabel.RIGHT));
        cpl.add(new JLabel(" Default Storage Resource: ", JLabel.RIGHT));
        cp.add(cpl, BorderLayout.WEST);

        JPanel lp = new JPanel();
        lp.setLayout(new BorderLayout(5,5));
        JPanel lpb = new JPanel();
        JButton add = new JButton("Add");
        add.addActionListener(this);
        add.setActionCommand("Add srb connsction");
        lpb.add(add);
        JButton del = new JButton("Delete");
        del.addActionListener(this);
        del.setActionCommand("Delete srb connsction");
        lpb.add(del);
        lp.add(lpb, BorderLayout.SOUTH);
        JScrollPane listScroller = new JScrollPane(srbJList);
        int w = 120 + (ViewProperties.getFontSize()-12)*10;
        int h = 200 + (ViewProperties.getFontSize()-12)*15;
        listScroller.setPreferredSize(new Dimension(w, h));
        lp.add(listScroller, BorderLayout.CENTER);

        /* padding */
        JPanel sp = new JPanel();
        sp.setLayout(new GridLayout(3,1,5,15));
        sp.add(new JLabel(" "));

        p.add(cp, BorderLayout.CENTER);
        p.add(lp, BorderLayout.WEST);
        p.add(sp, BorderLayout.SOUTH);

        if (srbVector !=null && srbVector.size()>0)
            srbJList.setSelectedIndex(0);

        return p;
    }

    public void actionPerformed(ActionEvent e)
    {
        Object source = e.getSource();
        String cmd = e.getActionCommand();

        if (cmd.equals("Set options"))
        {
            setUserOptions();
            setVisible(false);
        }
        else if (cmd.equals("Cancel"))
        {
            isFontChanged = false;
            setVisible(false);
        }
        else if (cmd.equals("Set current dir to user.dir"))
        {
            boolean isCheckCurrentUserDirSelected = checkCurrentUserDir.isSelected();
            workField.setEnabled(!isCheckCurrentUserDirSelected);
            currentDirButton.setEnabled(!isCheckCurrentUserDirSelected);
        }
        else if (cmd.equals("Browse UG"))
        {
            final JFileChooser fchooser = new JFileChooser(rootDir);
            int returnVal = fchooser.showOpenDialog(this);

            if(returnVal != JFileChooser.APPROVE_OPTION)
                return;

            File choosedFile = fchooser.getSelectedFile();
            if (choosedFile == null)
                return;

            String fname = choosedFile.getAbsolutePath();
            if (fname == null) return;
            UGField.setText(fname);
        }
        else if (cmd.equals("Browse current dir"))
        {
            final JFileChooser fchooser = new JFileChooser(rootDir);
            fchooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            int returnVal = fchooser.showDialog(this, "Select");

            if(returnVal != JFileChooser.APPROVE_OPTION)
                return;

            File choosedFile = fchooser.getSelectedFile();
            if (choosedFile == null)
                return;

            String fname = choosedFile.getAbsolutePath();
            if (fname == null) return;
            workField.setText(fname);
        }
        else if (cmd.equals("Browse h4toh5"))
        {
            final JFileChooser fchooser = new JFileChooser(rootDir);
            int returnVal = fchooser.showOpenDialog(this);

            if(returnVal != JFileChooser.APPROVE_OPTION)
                return;

            File choosedFile = fchooser.getSelectedFile();
            if (choosedFile == null)
                return;

            String fname = choosedFile.getAbsolutePath();
            if (fname == null) return;
            H4toH5Path = fname;
            H4toH5Field.setText(fname);
        }
        else if (cmd.startsWith("Add Module")) {
            String newModule = JOptionPane.showInputDialog(this,
                "Type the full path of the new module:",
                cmd,
                JOptionPane.PLAIN_MESSAGE);

            if (newModule == null || newModule.length()<1)
                return;

            // enables use of JHDF5 in JNLP (Web Start) applications, the system class loader with reflection first.
            try { Class.forName(newModule); }
            catch (Exception ex) {
            try {ViewProperties.loadExtClass().loadClass(newModule); }
            catch(ClassNotFoundException ex2) {
                JOptionPane.showMessageDialog(
                    this,
                    "Cannot find module:\n "+newModule+
                    "\nPlease check the module name and classpath.",
                    "HDFView",
                    JOptionPane.ERROR_MESSAGE);
                return;
            } }

            if (cmd.endsWith("TreeView") && !treeViews.contains(newModule)) {
                treeViews.add(newModule);
                choiceTreeView.addItem(newModule);
            }
            else if (cmd.endsWith("MetadataView") && !metaDataViews.contains(newModule)) {
                metaDataViews.add(newModule);
                choiceMetaDataView.addItem(newModule);
            }
            else if (cmd.endsWith("TextView") && !textViews.contains(newModule)) {
                textViews.add(newModule);
                choiceTextView.addItem(newModule);
            }
            else if (cmd.endsWith("TableView") && !tableViews.contains(newModule)) {
                tableViews.add(newModule);
                choiceTableView.addItem(newModule);
            }
            else if (cmd.endsWith("ImageView") && !imageViews.contains(newModule)) {
                imageViews.add(newModule);
                choiceImageView.addItem(newModule);
            }
            else if (cmd.endsWith("PaletteView") && !paletteViews.contains(newModule)) {
                paletteViews.add(newModule);
                choicePaletteView.addItem(newModule);
            }
        }
        else if (cmd.startsWith("Delete Module")) {
            JComboBox theChoice = (JComboBox)source;

            if (theChoice.getItemCount() == 1) {
                JOptionPane.showMessageDialog(
                    this,
                    "Cannot delete the last module.",
                    cmd,
                    JOptionPane.ERROR_MESSAGE);
                return;
            }

            int reply = JOptionPane.showConfirmDialog(this,
                "Do you want to delete the selected module?",
                cmd,
                JOptionPane.YES_NO_OPTION);
            if (reply == JOptionPane.NO_OPTION)
                return;

            String moduleName = (String)theChoice.getSelectedItem();
            theChoice.removeItem(moduleName);
            if (cmd.endsWith("TreeView")) {
                treeViews.remove(moduleName);
            }
            else if (cmd.endsWith("MetadataView")) {
                metaDataViews.remove(moduleName);
            }
            else if (cmd.endsWith("TextView")) {
                textViews.remove(moduleName);
            }
            else if (cmd.endsWith("TableView")) {
                tableViews.remove(moduleName);
            }
            else if (cmd.endsWith("ImageView")) {
                imageViews.remove(moduleName);
            }
            else if (cmd.endsWith("PaletteView")) {
                paletteViews.remove(moduleName);
            }
        }
        else if (cmd.equals("Add srb connsction"))
        {
            String srbaccount[] = new String[7];
            for (int i=0; i<7; i++) {
                srbaccount[i] = srbFields[i].getText();
                if (srbaccount[i] == null) return;
            }
            DefaultListModel lm = (DefaultListModel)srbJList.getModel();
            if (!lm.contains(srbaccount[0]))
            {
                srbVector.add(srbaccount);
                lm.addElement(srbaccount[0]);
                srbJList.setSelectedValue(srbaccount[0], true);
            }
        }
        else if (cmd.equals("Delete srb connsction"))
        {
            int n = srbJList.getSelectedIndex();
            if (n<0) return;

            int resp = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to delete the following SRB connection?\n"+
                    "            \""+srbJList.getSelectedValue()+"\"",
                    "Delete SRB Connection", JOptionPane.YES_NO_OPTION);
            if (resp == JOptionPane.NO_OPTION) return;

            DefaultListModel lm = (DefaultListModel)srbJList.getModel();
            lm.removeElementAt(n);
            srbVector.remove(n);
            for (int i=0; i<7; i++) srbFields[i].setText("");
        }
    }

    public void valueChanged(ListSelectionEvent e)
    {
        Object src = e.getSource();

        if (!src.equals(srbJList))
            return;

        int n = srbJList.getSelectedIndex();
        if ( n<0 ) return;

        String srbaccount[] = (String[])srbVector.get(n);
        if (srbaccount == null) return;

        n = Math.min(7, srbaccount.length);
        for (int i=0; i<n; i++)
            srbFields[i].setText(srbaccount[i]);
    }

    private void setUserOptions()
    {
        String UGPath = UGField.getText();
        if (UGPath != null && UGPath.length()>0)
        {
            UGPath = UGPath.trim();
            isUserGuideChanged = !UGPath.equals(ViewProperties.getUsersGuide());
            ViewProperties.setUsersGuide(UGPath);
        }

        String workPath = workField.getText();
        if (checkCurrentUserDir.isSelected())
            workPath = "user.dir";
        if (workPath != null && workPath.length()>0)
        {
            workPath = workPath.trim();
            isWorkDirChanged = !workPath.equals(ViewProperties.getWorkDir());
            ViewProperties.setWorkDir(workPath);
        }

        String ext = fileExtField.getText();
        if (ext != null && ext.length()>0)
        {
            ext = ext.trim();
            ViewProperties.setFileExtension(ext);
        }

        // set font size
        int fsize = 12;
        try {
            fsize = Integer.parseInt((String)fontSizeChoice.getSelectedItem());
            ViewProperties.setFontSize(fsize);

            if ((fontSize != ViewProperties.getFontSize()))
                isFontChanged = true;
        } catch (Exception ex) {}

        // set font type
        String ftype = (String)fontTypeChoice.getSelectedItem();
        if (!ftype.equalsIgnoreCase(ViewProperties.getFontType()))
        {
            isFontChanged = true;
            ViewProperties.setFontType(ftype);
        }
        
        // set data delimiter
        ViewProperties.setDataDelimiter((String)delimiterChoice.getSelectedItem());

        try {
            int maxsize = Integer.parseInt(maxMemberField.getText());
            ViewProperties.setMaxMembers(maxsize);
        } catch (Exception ex) {}

        try {
            int startsize = Integer.parseInt(startMemberField.getText());
            ViewProperties.setStartMembers(startsize);
        } catch (Exception ex) {}

        Vector[] moduleList = {treeViews, metaDataViews, textViews,
            tableViews, imageViews, paletteViews};
        JComboBox[] choiceList = {choiceTreeView, choiceMetaDataView, choiceTextView,
            choiceTableView, choiceImageView, choicePaletteView};
        for (int i=0; i<6; i++) {
            Object theModule = choiceList[i].getSelectedItem();
            moduleList[i].remove(theModule);
            moduleList[i].add(0, theModule);
        }
    }

    public boolean isFontChanged() { return isFontChanged; }

    public boolean isUserGuideChanged() { return isUserGuideChanged; }

    public boolean isWorkDirChanged() { return isWorkDirChanged; }
}


