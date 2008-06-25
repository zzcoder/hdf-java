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
import java.io.RandomAccessFile;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.File;
import javax.swing.*;
import java.awt.Frame;
import java.awt.Point;
import java.awt.Dimension;
import java.awt.BorderLayout;
import java.awt.GridLayout;
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
            int answer = JOptionPane.showConfirmDialog(owner,
                "Cannot find iRODS server.\nDo you want to enter server information?\n\n",
                "Server Error",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.ERROR_MESSAGE);
            if (answer == JOptionPane.YES_OPTION) {
                DefaultServerDialog dsd = new DefaultServerDialog(owner);
                if (dsd.getHost() != null) {
                    try {(new File(System.getProperty("user.home")+"/.irods")).mkdir();}
                    catch (Exception ex2) {}
                    PrintWriter out = new PrintWriter(new BufferedWriter
                       (new FileWriter(System.getProperty("user.home")+"/.irods/.irodsEnv")));
                    out.println("irodsHost '"+dsd.getHost()+"'");
                    out.println("irodsPort " +dsd.getPort());
                    out.println("irodsUserName '"+dsd.getUser()+"'");
                    out.println("irodsHome '"+dsd.getHome()+"'");
                    out.println("irodsCwd '"+dsd.getHome()+"'");
                    out.println("irodsZone '"+dsd.getZone()+"'");
                    out.println("irodsDefResource '"+dsd.getResource()+"'");
                    out.flush();
                    out.close();
                    H5SRB.callServerInit(dsd.getPassword());
                    srvInfo = H5SRB.getServerInfo();
                    remoteHomeDir = srvInfo[5];
                }
            }
            else {
                dispose();
                return;
            }
        }

        boolean isServerInitialized = false;
        try { 
            if (srvInfo[12] == null || srvInfo[12].length() < 1)
                srvInfo[12] = System.getProperty("user.home")+"/.irods/.irodsA";

            RandomAccessFile raf = new RandomAccessFile(srvInfo[12], "r");

            if (raf.length() > 1)
                isServerInitialized = true;
        } catch (FileNotFoundException ex) { isServerInitialized = false; }

        if (!isServerInitialized) {
            String passwd = JOptionPane.showInputDialog(owner, "Please enter the password for "+ srvInfo[0]);
            if (passwd!= null && passwd.length()>0) {
                passwd = passwd.trim();
                try {
                    H5SRB.callServerInit(passwd);
                    isServerInitialized = true;
                } catch (Exception ex ) { 
                    isServerInitialized = false; 
                }
            }
        }
        
        if (!isServerInitialized) {
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
        
        // layout the components
        JPanel contentPane = (JPanel)getContentPane();
        contentPane.setLayout(new BorderLayout(5,5));
        contentPane.setBorder(BorderFactory.createEmptyBorder(15,5,5,5));

        if (fileList.size() > 0) {
            String remoteFile = null;
            Object[] remoteFiles = fileList.toArray();
            int idx0 = remoteHomeDir.length();
            for (int i=0; i<remoteFiles.length; i++) {
                remoteFile = (String)remoteFiles[i];
                if (remoteFile.startsWith(remoteHomeDir))
                    remoteFile = remoteFile.substring(idx0);
                    remoteFiles[i] = remoteFile;
            }
            fileTree = new JTree(remoteFiles);
        }
        else
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

        if (remoteHomeDir != null)
            setTitle("Open File from SRB ... "+remoteHomeDir);

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
                if (fname != null && fname.length()>0) {
                    int index = fname.indexOf(fileFieldSeparator);
                    if (index > 0)
                        fname = fname.substring(0, index);
                    fname = remoteHomeDir+fname;
                    openFile(fname);
                }
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

    private class DefaultServerDialog extends JDialog implements ActionListener
    {
        Frame owner;
        JTextField[] srbFields = null;
        String[] serverInfo = null;

        public DefaultServerDialog(Frame owner) throws Throwable
        {
            super (owner, "Default iRODS Server ...", true);
            this.owner = owner;
        
            srbFields = new JTextField[7];

            JPanel contentPane = (JPanel)getContentPane();
            contentPane.setLayout(new BorderLayout(5,5));
            contentPane.setBorder(BorderFactory.createEmptyBorder(15,5,5,5));

            JPanel cpl = new JPanel(); /* left panel */
            cpl.setLayout(new GridLayout(7,1,5,5));
            cpl.add(new JLabel("Host Machine: ", JLabel.RIGHT));
            cpl.add(new JLabel("Port Number: ", JLabel.RIGHT));
            cpl.add(new JLabel("User Name: ", JLabel.RIGHT));
            cpl.add(new JLabel("Password: ", JLabel.RIGHT));
            cpl.add(new JLabel("Home Directory: ", JLabel.RIGHT));
            cpl.add(new JLabel("Zone Name: ", JLabel.RIGHT));
            cpl.add(new JLabel(" Default Storage Resource: ", JLabel.RIGHT));
            contentPane.add(cpl, BorderLayout.WEST);

            JPanel cpc = new JPanel(); /* center panel */
            cpc.setLayout(new GridLayout(7,1,5,5));
            cpc.add(srbFields[0] = new JTextField());
            cpc.add(srbFields[1] = new JTextField("1247"));
            cpc.add(srbFields[2] = new JTextField("rods"));
            cpc.add(srbFields[3] = new JTextField(""));
            cpc.add(srbFields[4] = new JTextField("/tempZone/home/rods"));
            cpc.add(srbFields[5] = new JTextField("tempZone"));
            cpc.add(srbFields[6] = new JTextField("demoResc"));
            contentPane.add(cpc, BorderLayout.CENTER);

            JPanel cpb = new JPanel(); /* bottom panel */
            JButton rodsButton = new JButton("Ok");
            rodsButton.addActionListener(this);
            rodsButton.setActionCommand("Add iRODS connection");
            cpb.add(rodsButton);
            rodsButton = new JButton("Cancel");
            rodsButton.addActionListener(this);
            rodsButton.setActionCommand("Cancel iRODS connection");
            cpb.add(rodsButton);
            contentPane.add(cpb, BorderLayout.SOUTH);
            contentPane.setPreferredSize(new Dimension(500, 300));

            Point l = owner.getLocation();
            l.x += 100;
            l.y += 60;
            setLocation(l);

            pack();
            setVisible(true);
        }

        public void actionPerformed(ActionEvent e)
        {
            Object source = e.getSource();
            String cmd = e.getActionCommand();
           
            if (cmd.equals("Add iRODS connection")) {
                serverInfo = new String[7];
                for (int i = 0; i<7; i++) {
                    serverInfo[i] = srbFields[i].getText();
                    if (serverInfo[i] != null)
                        serverInfo[i] = serverInfo[i].trim();
                    if (serverInfo[i] == null || serverInfo[i].length() <1) {
                        JOptionPane.showMessageDialog(owner, "Invalid server information.\n", "Server Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                }
                dispose();
            } 
            else if (cmd.equals("Cancel iRODS connection")) {
                serverInfo = null;
                dispose();
            }
        }

        public String getHost() {
            if (serverInfo!= null)
                return serverInfo[0];
            else 
                return null;
        }

        public String getPort() {
            if (serverInfo!= null)
                return serverInfo[1];
            else 
                return null;
        }

        public String getUser() {
            if (serverInfo!= null)
                return serverInfo[2];
            else
                return null;
        }

        public String getPassword() {
            if (serverInfo!= null)
                return serverInfo[3];
            else
                return null;
        }

        public String getHome() {
            if (serverInfo!= null)
                return serverInfo[4];
            else
                return null;
        }

        public String getZone() {
            if (serverInfo!= null)
                return serverInfo[5];
            else
                return null;
        }

        public String getResource() {
            if (serverInfo!= null)
                return serverInfo[6];
            else
                return null;
        }

    } /* private class DefaultServerDialog */
}


