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
import javax.swing.tree.*;

import ncsa.hdf.view.*;

import edu.sdsc.grid.gui.*;
import edu.sdsc.grid.io.*;
import edu.sdsc.grid.io.srb.*;

import ncsa.hdf.object.HObject;
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
	public static final long serialVersionUID = HObject.serialVersionUID;

    private static final boolean DEBUG = true;

    private JargonGui treeSrb;
    private ViewManager viewer;
    private TreeView treeView;
    private SRBFileSystem srbFileSystem;
    private String srbInfo[];

    /** constructs a SRBFileDialog.
     * @param owner The owner of the dialog.
     * @param type the type of the conversion to perform.
     * @param openFiles The list of current open files.
      */
    public SRBFileDialog(Frame owner) throws Throwable
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
            java.util.Vector srbList = ViewProperties.getSrbAccount();
            int n = 0;
            if (srbList != null && (n=srbList.size())>0)
            {
                int idx = 0;
                if (n > 1) {
                    String srb_hosts[] = new String[n];
                    for (int i=0; i<n; i++)
                        srb_hosts[i] = ((String[])srbList.get(i))[0];
                    String selection = (String)JOptionPane.showInputDialog(
                        this, "Select SRB Server Connection",
                        "SRB Connection", JOptionPane.PLAIN_MESSAGE, null,
                        srb_hosts, srb_hosts[0]);
                    for (int i=0; i<n; i++) {
                        if (srb_hosts[i].equals(selection)) {
                            idx = i;
                            break;
                        }
                    }
                } /* if (n > 1) */
                String srbInfo[] = (String[])srbList.get(idx);
                int port = Integer.parseInt(srbInfo[1]);
                SRBAccount srbAccount = new SRBAccount(srbInfo[0], port,
                    srbInfo[2], srbInfo[3], srbInfo[4], srbInfo[5], srbInfo[6]);
                srbFileSystem = new SRBFileSystem(srbAccount);
            }

            if (srbFileSystem == null) // using default connection at .srb directory
            {
                srbFileSystem = new SRBFileSystem();
            }

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
        } catch (Throwable e) { throw e; }

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
                dispose();
        }
        else if (cmd.equals("Cancel"))
        {
            dispose();
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

        String srbInfo[] = new String[5];
        SRBAccount srbAccount = (SRBAccount)srbFile.getFileSystem().getAccount();
        srbInfo[0] = srbAccount.getHost();
        srbInfo[1] = String.valueOf(srbAccount.getPort());
        srbInfo[2] = srbAccount.getPassword();
        srbInfo[3] = srbAccount.getUserName();
        srbInfo[4] = srbAccount.getDomainName();

        H5SrbFile fileFormat = new H5SrbFile(srbInfo, srbFile.getAbsolutePath());

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


