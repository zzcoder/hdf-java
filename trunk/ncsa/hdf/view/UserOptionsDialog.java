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
import javax.swing.border.*;
import java.awt.Color;
import java.awt.Frame;
import java.awt.Dimension;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.GraphicsEnvironment;
import java.awt.Toolkit;
import java.awt.Point;
import java.awt.FileDialog;

/** UserOptionsDialog displays components for choosing user options. */
public class UserOptionsDialog extends JDialog
implements ActionListener
{
    /**
     * The main HDFView.
     */
    private final ViewManager viewer;

    private String H4toH5Path;
    private JTextField H4toH5Field, UGField, workField, h4ExtField, h5ExtField,
            maxMemberField, startMemberField;
    private JComboBox fontSizeChoice, fontTypeChoice, delimiterChoice;
    private String rootDir, workDir;
    private JCheckBox checkCurrentUserDir;
    private JButton currentDirButton;

    private final int fontSize;

    private boolean isFontChanged;

    private boolean isUserGuideChanged;

    private boolean isWorkDirChanged;

    /** constructs an UserOptionsDialog.
     * @param view The HDFView.
     */
    public UserOptionsDialog(ViewManager view, String viewroot)
    {
        super ((Frame)view, "User Options", true);

        viewer = view;
        rootDir = viewroot;
        isFontChanged = false;
        isUserGuideChanged = false;
        isWorkDirChanged = false;
        fontSize = ViewProperties.getFontSize();
        workDir = ViewProperties.getWorkDir();
        if (workDir == null) workDir = rootDir;

        String[] fontSizeChoices = {"10", "12", "14", "16", "18", "20"};
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

        JPanel contentPane = (JPanel)getContentPane();
        contentPane.setLayout(new BorderLayout(8,8));
        contentPane.setBorder(BorderFactory.createEmptyBorder(15,5,5,5));
        contentPane.setPreferredSize(new Dimension(400, 450));

        JPanel centerP = new JPanel();
        centerP.setLayout(new GridLayout(6,1,8,15));
        centerP.setBorder(new SoftBevelBorder(BevelBorder.LOWERED));

        JPanel p0 = new JPanel();
        p0.setLayout(new BorderLayout());
        p0.add(checkCurrentUserDir=new JCheckBox("Working Dir ", false) , BorderLayout.WEST);
        checkCurrentUserDir.addActionListener(this);
        checkCurrentUserDir.setActionCommand("Set to user.dir");
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
        p0.setLayout(new GridLayout(1,2,8,8));
        JPanel p00 = new JPanel();
        p00.setLayout(new BorderLayout());
        p00.add(new JLabel("HDF5: "), BorderLayout.WEST);
        p00.add(h5ExtField=new JTextField(ViewProperties.getH5Extension()), BorderLayout.CENTER);
        p0.add(p00);
        p00 = new JPanel();
        p00.setLayout(new BorderLayout());
        p00.add(new JLabel("HDF4: "), BorderLayout.WEST);
        p00.add(h4ExtField=new JTextField(ViewProperties.getH4Extension()), BorderLayout.CENTER);
        p0.add(p00);
        tborder = new TitledBorder("HDF File Extension");
        tborder.setTitleColor(Color.darkGray);
        p0.setBorder(tborder);
        centerP.add(p0);

        p0 = new JPanel();
        p0.setLayout(new GridLayout(1,2,8,8));
        p00 = new JPanel();
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
        tborder = new TitledBorder("Max Number of Member Objects to Load in Each Group");
        tborder.setTitleColor(Color.darkGray);
        p0.setBorder(tborder);
        centerP.add(p0);

        JPanel p = new JPanel();
        b = new JButton("   Ok   ");
        b.setActionCommand("Set options");
        b.addActionListener(this);
        p.add(b);
        b = new JButton("Cancel");
        b.setActionCommand("Cancel");
        b.addActionListener(this);
        p.add(b);

        String propertyFile = ViewProperties.getPropertyFile();
        contentPane.add(new JLabel(propertyFile), BorderLayout.NORTH);
        contentPane.add(p, BorderLayout.SOUTH);
        contentPane.add(centerP, BorderLayout.CENTER);

        Point l =((Frame)viewer).getLocation();
        l.x += 150;
        l.y += 100;
        setLocation(l);
        validate();
        pack();
    }

    public void actionPerformed(ActionEvent e)
    {
        Object source = e.getSource();
        String cmd = e.getActionCommand();

        if (cmd.equals("Set options"))
        {
            setUserOptions();
            dispose();
        }
        else if (cmd.equals("Cancel"))
        {
            isFontChanged = false;
            dispose();
        }
        else if (cmd.equals("Set to user.dir"))
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

        String ext = h4ExtField.getText();
        if (ext != null && ext.length()>0)
        {
            ext = ext.trim();
            ViewProperties.setH4Extension(ext);
        }

        ext = h5ExtField.getText();
        if (ext != null && ext.length()>0)
        {
            ext = ext.trim();
            ViewProperties.setH5Extension(ext);
        }

        // set font size
        try {
            int fsize = Integer.parseInt((String)fontSizeChoice.getSelectedItem());
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

    }

    public boolean isFontChanged() { return isFontChanged; }

    public boolean isUserGuideChanged() { return isUserGuideChanged; }

    public boolean isWorkDirChanged() { return isWorkDirChanged; }
}


