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
import java.io.*;
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
    public SRBFileDialog(Frame owner)
    {
        super (owner, "Open File from SRB ...", true);

        if (!H5SRB.isSupported()) {
            JOptionPane.showMessageDialog(owner, "iRODS is not supported.", 
                "Server Error", JOptionPane.ERROR_MESSAGE);
            dispose();
            return;
        }
        
        fileTree = null;
        viewer = (ViewManager) owner;
        treeView = viewer.getTreeView();
        fileList = new Vector(50);

        fileFieldSeparator = "::";
        remoteHomeDir = "/tempZone/home/rods";

        boolean isServerSetup = true;
        try {
            fileFieldSeparator = H5SRB.getFileFieldSeparator();
            srvInfo = H5SRB.getServerInfo();
            remoteHomeDir = srvInfo[5];
        } catch (Throwable ex) {
        	ex.printStackTrace();
        	isServerSetup = false;
        }
        
        if (!isServerSetup) {
            int answer = JOptionPane.showConfirmDialog(owner,
                "Cannot find iRODS server.\nDo you want to enter server information?\n\n",
                "Server Error",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.ERROR_MESSAGE);
            if (answer == JOptionPane.NO_OPTION) {
                dispose();
            	return;
            }

            DefaultServerDialog dsd = new DefaultServerDialog(owner);
            if (dsd.getHost() != null) {
                try {
                	File irodsdir = new File(System.getProperty("user.home")+"/.irods");
                	if (!irodsdir.exists())
                		irodsdir.mkdir();
                    PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(System.getProperty("user.home")+"/.irods/.irodsEnv")));
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
                } catch (Throwable ex2) { ex2.printStackTrace();}
            }
        }

        boolean isServerInitialized = false;
        try { 
            if (srvInfo[12] == null || srvInfo[12].length() < 1)
                srvInfo[12] = System.getProperty("user.home")+"/.irods/.irodsA";

            RandomAccessFile raf = new RandomAccessFile(srvInfo[12], "r");

            if (raf.length() > 1)
                isServerInitialized = true;
        } catch (Exception ex) { isServerInitialized = false; }
        
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

        fileTree = createFileTree(fileList, remoteHomeDir);

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
            this.setVisible(false);
        }
        else if (cmd.equals("Cancel"))
        {
            this.setVisible(false);
        }
    }
    
    private JTree createFileTree(Vector<String> list, String iHomeDir) 
    {
    	JTree tree = null;
    	int n = 0;
        int idx = iHomeDir.length();
    	
    	if (list == null || (n=list.size())<1)
    		return (new JTree()); // empty tree
    	
    	/*
        String remoteFile = null;
        for (int i=0; i<n; i++) {
        	remoteFile = list.get(i);
        }
        */
    	
        Object[] remoteFiles = list.toArray();

        if (list.size() > 0) {
            String remoteFile = null;
             for (int i=0; i<remoteFiles.length; i++) {
                remoteFile = (String)remoteFiles[i];
                if (remoteFile.startsWith(iHomeDir))
                    remoteFile = remoteFile.substring(idx);
                    remoteFiles[i] = remoteFile;
            }
            tree = new JTree(remoteFiles);
        }
        
        return tree;
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
            
            treeView.getCurrentFiles().add(fileFormat);
            this.setName(remoteFile);
         }

        return retVal;
    }

    private class DefaultServerDialog extends JDialog implements ActionListener
    {
        Frame owner;
        JTextField[] srbFields = null;
        String[] serverInfo = {
        		"server.machine.name",
        		"1247",
        		"rods",
        		"",
        		"/tempZone/home/rods",
        		"tempZone",
        		"demoResc"};

        public DefaultServerDialog(Frame owner)
        {
            super (owner, "Default iRODS Server ...", true);
            this.owner = owner;
        
            serverInfo = new String[7];
            srbFields = new JTextField[7];
            
            String tmpFname = System.getProperty("user.home")+"/.irods/.irodsEnv";
            File tmpFile = new File (tmpFname);
            if (tmpFile.exists() && tmpFile.isFile()) {
            	try {
                	BufferedReader in = new BufferedReader(new FileReader(tmpFile));
                	String line = in.readLine();
                	while (line != null) {
                		line = line.replace('\'', ' ');
                		if (line.startsWith("irodsHost "))
                			serverInfo[0] = line.substring("irodsHost ".length());
                		else if (line.startsWith("irodsPort "))
                			serverInfo[1] = line.substring("irodsPort ".length());
                		else if (line.startsWith("irodsUserName "))
                			serverInfo[2] = line.substring("irodsUserName ".length());
                		else if (line.startsWith("irodsHome "))
                			serverInfo[4] = line.substring("irodsHome ".length());
                		else if (line.startsWith("irodsZone "))
                			serverInfo[5] = line.substring("irodsZone ".length());
                		else if (line.startsWith("irodsDefResource "))
                			serverInfo[6] = line.substring("irodsDefResource ".length());
                		line = in.readLine();
                	}
            	} catch (Exception ex) {}
            }
            
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
            cpl.add(new JLabel("Default Storage Resource: ", JLabel.RIGHT));
            contentPane.add(cpl, BorderLayout.WEST);

            JPanel cpc = new JPanel(); /* center panel */
            cpc.setLayout(new GridLayout(7,1,5,5));
            cpc.add(srbFields[0] = new JTextField(serverInfo[0]));
            cpc.add(srbFields[1] = new JTextField(serverInfo[1]));
            cpc.add(srbFields[2] = new JTextField(serverInfo[2]));
            cpc.add(srbFields[3] = new JTextField(serverInfo[3]));
            cpc.add(srbFields[4] = new JTextField(serverInfo[4]));
            cpc.add(srbFields[5] = new JTextField(serverInfo[5]));
            cpc.add(srbFields[6] = new JTextField(serverInfo[6]));
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


