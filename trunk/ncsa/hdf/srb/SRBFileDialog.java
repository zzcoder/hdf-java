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
import java.util.Vector;

import ncsa.hdf.view.*;

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

    private JTree fileTree;
    private ViewManager viewer;
    private TreeView treeView;
    private Vector fileList;
    private String fileFieldSeparator;
    private String remoteHomeDir;
    
    /*
    srvInfo[0] = rodsUserName
    srvInfo[1] = rodsHost
    srvInfo[2] = rodsPort
    srvInfo[3] = xmsgHost
    srvInfo[4] = xmsgPort
    srvInfo[5] = rodsHome
    srvInfo[6] = rodsCwd
    srvInfo[7] = rodsAuthScheme
    srvInfo[8] = rodsDefResource
    srvInfo[9] = rodsZone
    */
    private String srvInfo[];

    /** constructs a SRBFileDialog.
     * @param owner The owner of the dialog.
     * @param type the type of the conversion to perform.
     * @param openFiles The list of current open files.
      */
    public SRBFileDialog(Frame owner) throws Throwable
    {
        super (owner, "Open File from SRB ...", true);
        fileTree = null;
        viewer = (ViewManager) owner;
        treeView = viewer.getTreeView();
        fileList = new Vector(50);

        fileFieldSeparator = "::";
        remoteHomeDir = "/tempZone/home/rods";

        try {
            fileFieldSeparator = H5SRB.getFileFieldSeparator();
            srvInfo = H5SRB.getServerInfo();
            remoteHomeDir = srvInfo[5];
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(owner, "Cannot get server information.", 
                "Server Error", JOptionPane.ERROR_MESSAGE);
            dispose();
            return;
        }

        boolean isConnected = false;
        try {
            H5SRB.makeConnection(null);
            isConnected = true;
        } catch (Exception ex ) { isConnected = false; }

        if (!isConnected) {
            String passwd = JOptionPane.showInputDialog(owner, "Please enter the password for "+ srvInfo[0]);
            if (passwd!= null && passwd.length()>0) {
                try {
                    H5SRB.makeConnection(passwd);
                } catch (Exception ex ) { 
                    isConnected = false; 
                }
            }
        }
        
        if (!isConnected) {
            JOptionPane.showMessageDialog(owner, "Cannot make connection to "+srvInfo[1], 
                "Server Error", JOptionPane.ERROR_MESSAGE);
            dispose();
            return;
        }

        try {
            H5SRB.getFileList(fileList);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(owner, "Cannot get file list from "+srvInfo[1], 
                "Server Error", JOptionPane.ERROR_MESSAGE);
            dispose();
            return;
        }
        
        /* for test only 
        fileList.add("/tempZone/home/rods/comp3.h5 :: 2.3K");
        fileList.add("/tempZone/home/rods/hdf5_test.h5 :: 1.7M");
        fileList.add("/tempZone/home/rods/linktst.h5 :: 1.3K");
        fileList.add("/tempZone/home/rods/test.h5 :: 23.4K");
        fileList.add("/tempZone/home/rods/weather.h5 :: 5.1M");
        fileList.add("/tempZone/home/rods/willowfire_ca.h5 :: 349.8K");
        fileList.add("/tempZone/home/rods/large_files/MOD021KM.A2001062.1835.002.2001066052702.h5 :: 327.5M");
        fileList.add("/tempZone/home/rods/large_files/all_maps.h5 :: 864.2M");
        fileList.add("/tempZone/home/rods/large_files/flash_io_test.h5 :: 60.3M");
        */
        
        // layout the components
        JPanel contentPane = (JPanel)getContentPane();
        contentPane.setLayout(new BorderLayout(5,5));
        contentPane.setBorder(BorderFactory.createEmptyBorder(15,5,5,5));

       fileTree = new JTree(fileList);
            
       JScrollPane pane = new JScrollPane(fileTree);
       pane.setPreferredSize(new Dimension( 600, 400 ));
       contentPane.add(pane, BorderLayout.CENTER);

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
            if (fileTree == null) {
                return;
            }

            try { 
                DefaultMutableTreeNode treeNode = (DefaultMutableTreeNode)fileTree.getSelectionPath().getLastPathComponent();
                String fname = (String) treeNode.getUserObject();
                int index = fname.indexOf(fileFieldSeparator);
                if (index > 0)
                    fname = fname.substring(0, index);
                openFile(fname);
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
    
    private boolean openFile(String remoteFile)
    {
        boolean retVal = true;
        
        if (remoteFile == null)
        {
            JOptionPane.showMessageDialog( (JFrame)viewer, "File is null",
                getTitle(), JOptionPane.ERROR_MESSAGE);
            return false;
        } else
        remoteFile = remoteFile.trim();

        H5SrbFile fileFormat = new H5SrbFile(remoteFile);
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

    public @interface Test { }

    @Test
    public void testFileList() throws Exception
    {
        Vector flist = new Vector(50);

        System.out.println(H5SRB.getFileFieldSeparator());

        System.out.println("Server information:");
        String srvInfo[] = H5SRB.getServerInfo();
        for (int i=0; i<srvInfo.length; i++) {
            System.out.println("\t"+srvInfo[i]);
        }
        System.out.println();

        H5SRB.getFileList(flist);
        int n = flist.size();

        for (int i=0; i<n; i++)
            System.out.println(flist.elementAt(i));
    }

    @Test
    public void testFileContent(String filename) throws Exception
    {
       int fid = 0;
       H5SrbFile srbFile = new H5SrbFile(filename);
        System.out.println(filename);

        try {
            fid = srbFile.open();
        } catch (Throwable err) {
            err.printStackTrace();
        }

        if (fid <=0) {
            System.out.println("Failed to open file from server: fid="+fid);
            return;
        }

        TreeNode root = srbFile.getRootNode();
        if (root == null)  {
            System.out.println("Failed to open file from server: root=nul: root=nulll");
            return;
        }

        java.util.Enumeration objs = root.children() ;
        while(objs.hasMoreElements())
            System.out.println(objs.nextElement());

    }
}


