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
import java.awt.Choice;
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

    private String H4toH5Path, UGPath;
    private JTextField H4toH5Field, UGField;
    private Choice fontChoice;
    private Choice delimiterChoice;
    private final String rootDir;

    private final int fontSize;

    private boolean isFontChanged;

    /** constructs an UserOptionsDialog.
     * @param view The HDFView.
     */
    public UserOptionsDialog(ViewManager view, String viewroot)
    {
        super ((Frame)view, "User Options", true);

        viewer = view;
        rootDir = viewroot;
        isFontChanged = false;
        fontSize = ViewProperties.getFontSizeInt();

        JPanel contentPane = (JPanel)getContentPane();
        contentPane.setLayout(new BorderLayout(5,5));
        contentPane.setBorder(BorderFactory.createEmptyBorder(15,5,5,5));
        contentPane.setPreferredSize(new Dimension(550, 200));

        JPanel centerP = new JPanel();
        centerP.setLayout(new BorderLayout(5,5));
        centerP.setBorder(new TitledBorder(""));

        JPanel p = new JPanel();
        JLabel label = null;
        p.setLayout(new GridLayout(3,1,10,10));
        p.add(label = new JLabel("User's Guide:"));
        //p.add(label = new JLabel("H4toH5 Converter:"));
        p.add(label = new JLabel("Font Size:"));
        p.add(label = new JLabel("Data Delimiter:"));
        centerP.add("West", p);

        p = new JPanel();
        p.setLayout(new GridLayout(3,1,10,10));
        p.add(UGField = new JTextField(ViewProperties.getUsersGuide()));
        //p.add(H4toH5Field = new JTextField(ViewProperties.getH4toH5()));
        p.add(fontChoice = new Choice());
        p.add(delimiterChoice = new Choice());
        for (int i=0; i<6; i++)
        {
            fontChoice.add(String.valueOf(10+2*i));
        }
        fontChoice.select(ViewProperties.getFontSize());
        delimiterChoice.add(ViewProperties.DELIMITER_TAB);
        delimiterChoice.add(ViewProperties.DELIMITER_COMMA);
        delimiterChoice.add(ViewProperties.DELIMITER_SPACE);
        delimiterChoice.add(ViewProperties.DELIMITER_COLON);
        delimiterChoice.add(ViewProperties.DELIMITER_SEMI_COLON);
        delimiterChoice.select(ViewProperties.getDataDelimiter());
        centerP.add("Center", p);

        p = new JPanel();
        p.setLayout(new GridLayout(4,1,10,10));
        JButton b = new JButton("Browse...");
        b.setActionCommand("Browse UG");
        b.addActionListener(this);
        p.add(b);
        b = new JButton("Browse...");
        b.setActionCommand("Browse h4toh5");
        b.addActionListener(this);
        //p.add(b);
        centerP.add("East", p);

        p = new JPanel();
        b = new JButton("   Ok   ");
        b.setActionCommand("Set options");
        b.addActionListener(this);
        p.add(b);
        b = new JButton("Cancel");
        b.setActionCommand("Cancel");
        b.addActionListener(this);
        p.add(b);

        String propertyFile = ViewProperties.getPropertyFile();
        contentPane.add("North", new JLabel(propertyFile));
        contentPane.add("South", p);
        contentPane.add("Center", centerP);

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
            UGPath = fname;
            UGField.setText(fname);
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
        UGPath = UGField.getText();
        if (UGPath != null && UGPath.length()>0)
            ViewProperties.setUsersGuide(UGPath);

        //H4toH5Path = H4toH5Field.getText();
        //if (H4toH5Path != null && H4toH5Path.length()>0)
        //    ViewProperties.setH4toH5(H4toH5Path);

        ViewProperties.setFontSize(fontChoice.getSelectedItem());
        ViewProperties.setDataDelimiter(delimiterChoice.getSelectedItem());

        isFontChanged = (fontSize != ViewProperties.getFontSizeInt());
    }

    public boolean isFontChanged() { return isFontChanged; }

}


