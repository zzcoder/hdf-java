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
import java.awt.Color;
import java.awt.Frame;
import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Dimension;
import java.awt.BorderLayout;
import java.awt.Toolkit;
import java.io.File;
import java.util.List;
import java.util.Iterator;
import ncsa.hdf.object.*;

/**
 * NewFileDialog shows a message dialog requesting user input for creating a
 * new HDF4/5 file.
 * <p>
 * @version 1.3.0 04/23/2002
 * @author Peter X. Cao
 */
public class NewFileDialog extends JDialog
implements ActionListener
{
    /** TextField for entering the name of the target file. */
    private JTextField fileInputField;

    /** flag if the new file is an HDF5 */
    private String fileType;

    /** The current working directory*/
    private String currentDir;

    /** The view working directory*/
    private String viewDir;

    private boolean fileCreated;

    private List fileList;

    private final Toolkit toolkit;

    /** constructs an NewFileDialog.
     * @param owner The owner of the dialog.
     * @param dir The default directory of the new file.
     * @param type The type of file format.
     * @param openFiles The list of current open files.
     *        It is used to make sure the new file cannot be any file in use.
     */
    public NewFileDialog(
        Frame owner,
        String dir,
        String type,
        List openFiles)
    {
        super (owner, "New File...", true);

        currentDir = dir;
        viewDir = dir;
        fileType = type;
        fileCreated = false;
        fileList = openFiles;
        toolkit = Toolkit.getDefaultToolkit();
        setTitle("New "+ fileType + " File ...");

        if (currentDir != null) currentDir += File.separator;
        else currentDir = "";

        fileInputField = new JTextField(currentDir+"untitled."+fileType.toLowerCase());

        // layout the components
        JPanel contentPane = (JPanel)getContentPane();
        contentPane.setLayout(new BorderLayout(5,5));
        contentPane.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
        contentPane.setPreferredSize(new Dimension(400, 80));

        // add the top panel for enter file name
        JPanel p = new JPanel();
        p.setLayout(new BorderLayout(5,5));
        p.add("West",new JLabel("File name: "));
        p.add("Center", fileInputField);

        contentPane.add(p, BorderLayout.CENTER);

        JButton okButton = new JButton("   Ok   ");
        okButton.setMnemonic(KeyEvent.VK_O);
        okButton.setActionCommand("Ok");
        okButton.addActionListener(this);

        JButton cancelButton = new JButton("Cancel");
        cancelButton.setMnemonic(KeyEvent.VK_C);
        cancelButton.setActionCommand("Cancel");
        cancelButton.addActionListener(this);

        JButton jb = new JButton("Browse...");
        jb.setMnemonic(KeyEvent.VK_B);
        jb.setActionCommand("Browse file");
        jb.addActionListener(this);

        p = new JPanel();
        p.add(okButton);
        p.add(cancelButton);
        p.add(jb);

        contentPane.add(p, BorderLayout.SOUTH);

        Point l = owner.getLocation();
        l.x += 250;
        l.y += 80;
        setLocation(l);
        pack();
    }

    public void actionPerformed(ActionEvent e)
    {
        Object source = e.getSource();
        String cmd = e.getActionCommand();

        if (cmd.equals("Ok"))
        {
            fileCreated = createNewFile();

            if (fileCreated)
                dispose();
        }
        else if (cmd.equals("Cancel"))
        {
            fileCreated = false;
            dispose();
        }
        else if (cmd.equals("Browse file"))
        {
            final JFileChooser fchooser = new JFileChooser(currentDir);
            int returnVal = fchooser.showOpenDialog(this);

            if(returnVal != JFileChooser.APPROVE_OPTION)
                return;

            File choosedFile = fchooser.getSelectedFile();
            if (choosedFile == null)
                return;

            currentDir = choosedFile.getPath();
            String fname = choosedFile.getAbsolutePath();

            if (fname == null) return;

            fileInputField.setText(fname);
        }
    }

    /** create a new HDF file with default file creation properties */
    private boolean createNewFile()
    {
        String fname = fileInputField.getText();

        if (fname == null)
        {
            return false;
        }

        fname = fname.trim();
        if (fname == null || fname.length()==0)
        {
            return false;
        }

        File f = new File(fname);

        if (f.isDirectory())
        {
            toolkit.beep();
            JOptionPane.showMessageDialog(this,
                "File is a directory.",
                this.getTitle(),
                JOptionPane.ERROR_MESSAGE);
            return false;
        }

        File pfile = f.getParentFile();
        if (pfile == null)
        {
            fname = viewDir + File.separator + fname;
            f = new File(fname);
        }
        else if ( !pfile.exists())
        {
            toolkit.beep();
            JOptionPane.showMessageDialog(this,
                "File path does not exist at\n"+pfile.getPath(),
                this.getTitle(),
                JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // check if the file is in use
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
                        "Unable to create the new file. \nThe file is being used.",
                        getTitle(),
                        JOptionPane.ERROR_MESSAGE);
                    return false;
                }
            }
        }

        int newFileFlag = -1;
        if (f.exists())
        {
            newFileFlag = JOptionPane.showConfirmDialog(this,
                "File exists. Do you want to replace it ?",
                this.getTitle(),
                JOptionPane.YES_NO_OPTION);
            if (newFileFlag == JOptionPane.NO_OPTION)
                return false;
        }

        currentDir = f.getParent();
        try
        {
            FileFormat.getFileFormat(fileType).create(fname);
        } catch (Exception ex)
        {
            toolkit.beep();
            JOptionPane.showMessageDialog(this,
                ex,
                this.getTitle(),
                JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return true;
    }

    public boolean isFileCreated()
    {
        return fileCreated;
    }

    public String getFile()
    {
        return fileInputField.getText();
    }
}


