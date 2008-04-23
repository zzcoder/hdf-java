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
import edu.sdsc.grid.io.irods.*;

import ncsa.hdf.object.HObject;

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

    private JargonTree jargonTree;
    private ViewManager viewer;
    private TreeView treeView;
    private GeneralFileSystem fileSystem;
    private String accountInfo[];

    /** constructs a SRBFileDialog.
     * @param owner The owner of the dialog.
     * @param type the type of the conversion to perform.
     * @param openFiles The list of current open files.
      */
    public SRBFileDialog(Frame owner) throws Throwable
    {
        super (owner, "Open File from SRB ...", true);
        jargonTree = null;
        viewer = (ViewManager) owner;
        treeView = viewer.getTreeView();
        fileSystem = null;

        // layout the components
        JPanel contentPane = (JPanel)getContentPane();
        contentPane.setLayout(new BorderLayout(5,5));
        contentPane.setBorder(BorderFactory.createEmptyBorder(15,5,5,5));

        try {
            java.util.Vector remoteAccountList = ViewProperties.getSrbAccount();
            int n = 0;
            
            if ((remoteAccountList != null) && ((n=remoteAccountList.size())>0))
            {
                int idx = 0;
                if (n > 1) {
                    String srb_hosts[] = new String[n];
                    for (int i=0; i<n; i++) {
                        srb_hosts[i] = ((String[])remoteAccountList.get(i))[0];
                    }
                    String selection = (String)JOptionPane.showInputDialog(
                        this, "Select SRB/iRODS Server",
                        "SRB/iRODS Connection", JOptionPane.PLAIN_MESSAGE, null,
                        srb_hosts, srb_hosts[0]);
                    
                    if ((selection == null) || (selection.length()<1)) {
                        dispose();
                        return;
                    }
                        
                    for (int i=0; i<n; i++) {
                        if (srb_hosts[i].equals(selection)) {
                            idx = i;
                            break;
                        }
                    }
                } /* if (n > 1) */
                
                accountInfo = (String[])remoteAccountList.get(idx);
            }
                
            int port = Integer.parseInt(accountInfo[1]);
            
            /*
                SRBAccount/IRODSAccount:
                    String host,                    // accountInfo[0]
                    int port,                       // accountInfo[1]
                    String userName,                // accountInfo[2]
                    String password,                // accountInfo[3]
                    String homeDirectory,           // accountInfo[4]
                    String mdasDomainNamee/Zone,    // accountInfo[5]
                    String defaultStorageResource   // accountInfo[6]
            */
            //IRODSAccount remoteAccount = new IRODSAccount(
            RemoteAccount remoteAccounts[] = {
                new IRODSAccount(
                    accountInfo[0], 
                    port,
                    accountInfo[2], 
                    accountInfo[3], 
                    accountInfo[4], 
                    accountInfo[5], 
                    accountInfo[6]),
                new SRBAccount(
                    accountInfo[0], 
                    port,
                    accountInfo[2], 
                    accountInfo[3], 
                    accountInfo[4], 
                    accountInfo[5], 
                    accountInfo[6])};
            
            for (int i=0; i<remoteAccounts.length; i++) {
                try {
                    fileSystem = FileFactory.newFileSystem(remoteAccounts[i]);
                } catch (Exception ex) { continue; }
                break;
            }

            GeneralFile root = FileFactory.newFile( fileSystem, fileSystem.getHomeDirectory() );
         
            /* metadata does not work for iRODS
            String[] selectFieldNames = {
                SRBMetaDataSet.FILE_COMMENTS,
                GeneralMetaData.SIZE,
                GeneralMetaData.ACCESS_CONSTRAINT,
                UserMetaData.USER_NAME,
                SRBMetaDataSet.DEFINABLE_METADATA_FOR_FILES,
            };
            
            MetaDataSelect selects[] =	MetaDataSet.newSelection( selectFieldNames );
            jargonTree = new JargonTree(root, selects);
            */

            jargonTree = new JargonTree(root);
            jargonTree.useDefaultPopupMenu(false);
            jargonTree.setLargeModel(true);
            
            JScrollPane pane = new JScrollPane(jargonTree);
            pane.setPreferredSize(new Dimension( 600, 400 ));
            contentPane.add(pane, BorderLayout.CENTER);
        } catch (Throwable e) { e.printStackTrace(); throw e; }

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
        setVisible(true);
    }

    public void actionPerformed(ActionEvent e)
    {
        Object source = e.getSource();
        String cmd = e.getActionCommand();

        if (cmd.equals("Ok"))
        {
            if (jargonTree == null) {
                return;
            }

            try { 
                Object obj = jargonTree.getSelectionPath().getLastPathComponent();
                openFile((RemoteFile)obj);
            } catch (Exception ex) { 
                ex.printStackTrace(); 
                JOptionPane.showMessageDialog( (JFrame)viewer, "Invalid remote file",
                        getTitle(), JOptionPane.ERROR_MESSAGE);
            }
            dispose();
        }
        else if (cmd.equals("Cancel"))
        {
            dispose();
        }
    }

    private boolean openFile(RemoteFile remoteFile)
    {
        boolean retVal = true;
        
        if (remoteFile == null)
        {
            JOptionPane.showMessageDialog( (JFrame)viewer, "File is null",
                getTitle(), JOptionPane.ERROR_MESSAGE);
            return false;
        }

        H5SrbFile fileFormat = new H5SrbFile(remoteFile.getAbsolutePath());

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

        if ((fileRoot != null) && (treeRoot!=null))
        {
            treeModel.insertNodeInto(fileRoot, treeRoot, treeRoot.getChildCount());
            int currentRowCount = tree.getRowCount();
            if (currentRowCount>0) {
                tree.expandRow(tree.getRowCount()-1);
            }
         }

        return retVal;
    }


}


