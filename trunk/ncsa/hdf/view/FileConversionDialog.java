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
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.io.File;
import java.util.List;
import java.util.Iterator;
import ncsa.hdf.object.*;

/**
 * FileConversionDialog shows a message dialog requesting user input for converting
 * files.
 * <p>
 * @version 1.3.0 09/23/2002
 * @author Peter X. Cao
 */
public class FileConversionDialog extends JDialog
implements ActionListener
{
    private int fileTypeFrom, fileTypeTo;

    private JTextField srcFileField, dstFileField;

    private boolean isConverted ;

    private String convertedFile;

    private String toFileExtension;

    private List fileList;

    private String currentDir;

    private final Toolkit toolkit;

    /** constructs a FileConversionDialog.
     * @param owner The owner of the dialog.
     * @param type the type of the conversion to perform.
     * @param openFiles The list of current open files.
      */
    public FileConversionDialog(
        Frame owner,
        int typeFrom,
        int typeTo,
        String dir,
        List openFiles)
    {
        super (owner, "Convert File...", true);

        fileTypeFrom = typeFrom;
        fileTypeTo = typeTo;
        isConverted = false;
        fileList = openFiles;
        toFileExtension = "";
        currentDir = dir;
        toolkit = Toolkit.getDefaultToolkit();

        String fromName = "Source";
        if (fileTypeFrom == Tools.FILE_TYPE_JPEG && fileTypeTo == Tools.FILE_TYPE_HDF5)
        {
            toFileExtension = ".h5";
            setTitle("Convert JPEG to HDF5 ...");
            fromName = "JPEG";
        }
        else if (fileTypeFrom == Tools.FILE_TYPE_JPEG && fileTypeTo == Tools.FILE_TYPE_HDF4)
        {
            toFileExtension = ".hdf";
            setTitle("Convert JPEG to HDF4 ...");
            fromName = "JPEG";
        }
        else if (fileTypeFrom == Tools.FILE_TYPE_TIFF && fileTypeTo == Tools.FILE_TYPE_HDF5)
        {
            toFileExtension = ".h5";
            setTitle("Convert TIFF to HDF5 ...");
            fromName = "TIFF";
        }
        else if (fileTypeFrom == Tools.FILE_TYPE_TIFF && fileTypeTo == Tools.FILE_TYPE_HDF4)
        {
            toFileExtension = ".hdf";
            setTitle("Convert TIFF to HDF4 ...");
            fromName = "TIFF";
        }
        else if (fileTypeFrom == Tools.FILE_TYPE_PNG && fileTypeTo == Tools.FILE_TYPE_HDF5)
        {
            toFileExtension = ".h5";
            setTitle("Convert PNG to HDF5 ...");
            fromName = "PNG";
        }
        else if (fileTypeFrom == Tools.FILE_TYPE_PNG && fileTypeTo == Tools.FILE_TYPE_HDF4)
        {
            toFileExtension = ".hdf";
            setTitle("Convert PNG to HDF4 ...");
            fromName = "PNG";
        }

        // layout the components
        JPanel contentPane = (JPanel)getContentPane();
        contentPane.setLayout(new BorderLayout(5,5));
        contentPane.setBorder(BorderFactory.createEmptyBorder(15,5,5,5));
        contentPane.setPreferredSize(new Dimension(450, 120));

        // add the top panel for enter file name
        JPanel p = new JPanel();
        p.setLayout(new BorderLayout(5,5));

        JPanel p0 = new JPanel();
        p0.setLayout(new GridLayout(2, 1, 5, 5));
        p0.add(new JLabel(fromName +" File: "));
        p0.add(new JLabel("HDF File: "));
        p.add(p0, BorderLayout.WEST);

        p0 = new JPanel();
        p0.setLayout(new GridLayout(2, 1, 5, 5));
        p0.add(srcFileField = new JTextField());
        p0.add(dstFileField = new JTextField());
        p.add(p0, BorderLayout.CENTER);

        p0 = new JPanel();
        p0.setLayout(new GridLayout(2, 1, 5, 5));
        JButton jButton = new JButton("Browse...");
        jButton.setActionCommand("Browse source file");
        jButton.addActionListener(this);
        p0.add(jButton);
        jButton = new JButton("Browse...");
        jButton.setActionCommand("Browse target file");
        jButton.addActionListener(this);
        p0.add(jButton);
        p.add(p0, BorderLayout.EAST);

        contentPane.add(p, BorderLayout.CENTER);

        JButton okButton = new JButton("   Ok   ");
        okButton.setMnemonic(KeyEvent.VK_O);
        okButton.setActionCommand("Ok");
        okButton.addActionListener(this);

        JButton cancelButton = new JButton("Cancel");
        cancelButton.setMnemonic(KeyEvent.VK_C);
        cancelButton.setActionCommand("Cancel");
        cancelButton.addActionListener(this);


        p = new JPanel();
        p.add(okButton);
        p.add(cancelButton);

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
            isConverted = convert();

            if (isConverted)
                dispose();
        }
        else if (cmd.equals("Cancel"))
        {
            isConverted = false;
            convertedFile = null;
            dispose();
        }
        else if (cmd.equals("Browse source file"))
        {
            final JFileChooser fchooser = new JFileChooser(currentDir);
            if (fileTypeFrom == Tools.FILE_TYPE_JPEG)
                fchooser.setFileFilter(DefaultFileFilter.getFileFilterJPEG());
            else if (fileTypeFrom == Tools.FILE_TYPE_TIFF)
                fchooser.setFileFilter(DefaultFileFilter.getFileFilterTIFF());
            else if (fileTypeFrom == Tools.FILE_TYPE_PNG)
                fchooser.setFileFilter(DefaultFileFilter.getFileFilterPNG());

            int returnVal = fchooser.showOpenDialog(this);

            if(returnVal != JFileChooser.APPROVE_OPTION)
                return;

            File choosedFile = fchooser.getSelectedFile();
            if (choosedFile == null)
                return;

            String fname = choosedFile.getAbsolutePath();

            if (fname == null) return;

            currentDir = choosedFile.getParent();
            srcFileField.setText(fname);
            dstFileField.setText(fname+toFileExtension);
        }
        else if (cmd.equals("Browse target file"))
        {
            final JFileChooser fchooser = new JFileChooser();
            int returnVal = fchooser.showOpenDialog(this);

            if(returnVal != JFileChooser.APPROVE_OPTION)
                return;

            File choosedFile = fchooser.getSelectedFile();
            if (choosedFile == null)
                return;

            String fname = choosedFile.getAbsolutePath();

            if (fname == null) return;

            dstFileField.setText(fname);
        }
    }

    /** convert file */
    private boolean convert()
    {
        boolean converted = false;
        String srcFile = srcFileField.getText();
        String dstFile = dstFileField.getText();

        if (srcFile == null || dstFile == null)
        {
            return false;
        }

        srcFile = srcFile.trim();
        dstFile = dstFile.trim();
        if (srcFile == null || srcFile.length()<=0 ||
            dstFile == null || dstFile.length()<=0)
        {
            return false;
        }

        // verify the source file
        File f = new File(srcFile);
        if (!f.exists())
        {
            toolkit.beep();
            JOptionPane.showMessageDialog(this,
                "Source file does not exist.",
                this.getTitle(),
                JOptionPane.ERROR_MESSAGE);
            return false;
        }
        else if (f.isDirectory())
        {
            toolkit.beep();
            JOptionPane.showMessageDialog(this,
                "Source file is a directory.",
                this.getTitle(),
                JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // verify target file
        String srcPath = f.getParent();
        f = new File(dstFile);
        File pfile = f.getParentFile();
        if (pfile == null)
        {
            dstFile = srcPath + File.separator + dstFile;
            f = new File(dstFile);
        }
        else if ( !pfile.exists())
        {
            toolkit.beep();
            JOptionPane.showMessageDialog(this,
                "Destination file path does not exist at\n"+pfile.getPath(),
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
                if (theFile.getFilePath().equals(dstFile))
                {
                    toolkit.beep();
                    JOptionPane.showMessageDialog(this,
                        "The destination file is being used.",
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
                "Destination file exists. Do you want to replace it ?",
                this.getTitle(),
                JOptionPane.YES_NO_OPTION);
            if (newFileFlag == JOptionPane.NO_OPTION)
                return false;
        }

        try
        {
            Tools.convertImageToHDF(srcFile, dstFile, fileTypeFrom, fileTypeTo);
            convertedFile = dstFile;
            converted = true;
        } catch (Exception ex)
        {
            convertedFile = null;
            converted = false;
            toolkit.beep();
            JOptionPane.showMessageDialog(this,
                ex,
                this.getTitle(),
                JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return converted;
    }

    public boolean isFileConverted()
    {
        return isConverted;
    }

    public String getConvertedFile()
    {
        return convertedFile;
    }
}


