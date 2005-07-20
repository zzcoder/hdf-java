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

package ncsa.hdf.srb;

import java.awt.event.*;
import javax.swing.*;
import java.awt.Frame;
import java.awt.Point;
import java.awt.Dimension;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.util.List;
import javax.swing.tree.*;

import ncsa.hdf.object.*;
import ncsa.hdf.view.*;

import edu.sdsc.grid.gui.*;
import edu.sdsc.grid.io.*;
import edu.sdsc.grid.io.*;
import edu.sdsc.grid.io.srb.*;
import edu.sdsc.grid.io.local.*;

import ncsa.hdf.srb.obj.*;

/**
 * FileConversionDialog shows a message dialog requesting user input for converting
 * files.
 * <p>
 * @version 1.3.0 09/23/2002
 * @author Peter X. Cao
 */
public class SRBFileDialog extends JDialog
implements ActionListener
{
    private static final boolean DEBUG = true;

    private JargonGui treeSrb;
    private ViewManager viewer;
    private TreeView treeView;
    private SRBFileSystem srbFileSystem;

    /** constructs a SRBFileDialog.
     * @param owner The owner of the dialog.
     * @param type the type of the conversion to perform.
     * @param openFiles The list of current open files.
      */
    public SRBFileDialog( Frame owner)
    {
        super (owner, "Open File from SRB ...", true);
        treeSrb = null;
        viewer = (ViewManager) owner;
        treeView = viewer.getTreeView();
        srbFileSystem = null;

        // layout the components
        JPanel contentPane = (JPanel)getContentPane();
        contentPane.setLayout(new BorderLayout(5,5));
        contentPane.setBorder(BorderFactory.createEmptyBorder(15,5,5,5));

        try {
            srbFileSystem = new SRBFileSystem();
            GeneralFile root = FileFactory.newFile( srbFileSystem, srbFileSystem.getHomeDirectory() );
            String[] selectFieldNames = {
                SRBMetaDataSet.FILE_COMMENTS,
                SRBMetaDataSet.SIZE,
                SRBMetaDataSet.ACCESS_CONSTRAINT,
                SRBMetaDataSet.USER_NAME,
                SRBMetaDataSet.DEFINABLE_METADATA_FOR_FILES,
            };
            MetaDataSelect selects[] =	MetaDataSet.newSelection( selectFieldNames );
            treeSrb = new JargonGui(root, selects);
            treeSrb.useDefaultPopupMenu(false);
            treeSrb.setLargeModel(true);
            JScrollPane pane = new JScrollPane(treeSrb);
            pane.setPreferredSize(new Dimension( 600, 400 ));
            contentPane.add(pane, BorderLayout.CENTER);
        } catch (Throwable e) {
            e.printStackTrace();
            System.out.println(((SRBException)e).getStandardMessage());
        }

        JButton okButton = new JButton("   Ok   ");
        okButton.setMnemonic(KeyEvent.VK_O);
        okButton.setActionCommand("Ok");
        okButton.addActionListener(this);

        JButton cancelButton = new JButton("Cancel");
        cancelButton.setMnemonic(KeyEvent.VK_C);
        cancelButton.setActionCommand("Cancel");
        cancelButton.addActionListener(this);


        JPanel p = new JPanel();
        p.add(okButton);
        p.add(cancelButton);

        contentPane.add(p, BorderLayout.SOUTH);

        Point l = owner.getLocation();
        l.x += 250;
        l.y += 120;
        setLocation(l);
        pack();
    }

    public void actionPerformed(ActionEvent e)
    {
        Object source = e.getSource();
        String cmd = e.getActionCommand();

        if (cmd.equals("Ok"))
        {
            if (treeSrb == null)
                return;

            Object obj = treeSrb.getSelectionPath().getLastPathComponent();
            if ( (obj instanceof SRBFile) && (openFile((SRBFile)obj)) )
                hide();
        }
        else if (cmd.equals("Cancel"))
        {
            hide();
        }
    }

    private boolean openFile(SRBFile srbFile)
    {
        boolean retVal = true;

        if (srbFile == null)
        {
            JOptionPane.showMessageDialog( (JFrame)viewer, "File is null",
                getTitle(), JOptionPane.ERROR_MESSAGE);
            return false;
        }

        H5SrbFile fileFormat = new H5SrbFile(srbFile.getAbsolutePath());
        try {
            fileFormat.open();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog( (JFrame)viewer, ex,
                getTitle(), JOptionPane.ERROR_MESSAGE);
            return false;
        }

        JTree tree = treeView.getTree();
        DefaultTreeModel treeModel = (DefaultTreeModel)tree.getModel();
        MutableTreeNode fileRoot = (MutableTreeNode)fileFormat.getRootNode();
        MutableTreeNode treeRoot = (MutableTreeNode)treeModel.getRoot();

        if (fileRoot != null && treeRoot!=null)
        {
            treeModel.insertNodeInto(fileRoot, (MutableTreeNode)treeRoot, treeRoot.getChildCount());
            int currentRowCount = tree.getRowCount();
            if (currentRowCount>0) tree.expandRow(tree.getRowCount()-1);
         }

        return retVal;
    }


}


