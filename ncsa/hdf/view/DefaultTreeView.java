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

import ncsa.hdf.object.*;
import javax.swing.*;
import javax.swing.tree.*;
import java.util.*;
import java.io.*;
import java.lang.reflect.*;
import java.awt.Component;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Toolkit;
import java.awt.event.*;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */
public class DefaultTreeView extends JPanel
implements TreeView, ActionListener {

    /** the owner of this treeview */
    private ViewManager viewer;

    /**
     * The super root of tree: all open files start at this root.
     */
    private final TreeNode root;

    /**
     * The tree which holds file structures.
     */
    private final JTree tree;

    /**
     * The tree model
     */
    private final DefaultTreeModel treeModel;

    /** A list open files. */
    private final List fileList;

    private final Toolkit toolkit;

    /** Selected file */
    private FileFormat selectedFile;

    /** The current selected node. */
    private DefaultMutableTreeNode selectedNode;

    /** the current selected object */
    private HObject selectedObject;

    /** flag to indicate if the dataset is displayed as default */
    private boolean isDefaultDisplay;

    /**
     * The popup menu used to display user choice of actions on data object.
     */
    private final JPopupMenu popupMenu;

    /** a list of editing GUI components */
    private List editGUIs;

    /** the list of current selected objects */
    private List objectsToCopy;

    public DefaultTreeView(ViewManager theView) {
        viewer = theView;

        root = new DefaultMutableTreeNode() {
            public boolean isLeaf() { return false; }
        };

        fileList = new Vector();
        toolkit = Toolkit.getDefaultToolkit();
        editGUIs = new Vector();
        objectsToCopy = null;
        isDefaultDisplay = true;

        // initialize the tree and root
        treeModel = new DefaultTreeModel(root);
        tree = new JTree(treeModel);
        tree.setLargeModel(true);
        tree.setCellRenderer(new HTreeCellRenderer());
        tree.addMouseListener(new HTreeMouseAdapter());
        tree.setRootVisible(false);
        //tree.setShowsRootHandles(true);
        tree.setRowHeight(23);
        tree.setFont(tree.getFont().deriveFont(16f));

        // create the popupmenu
        popupMenu = createPopupMenu();

        // reset the scroll increament
        // layout GUI component
        this.setLayout( new BorderLayout() );
        this.add(tree, BorderLayout.CENTER);
    }

    /**
     * Insert a node into the tree.
     * @param node the node to insert.
     * @param pnode the parent node.
     */
    private void insertNode(TreeNode node, TreeNode pnode)
    {
        if (node == null || pnode==null)
            return;

        treeModel.insertNodeInto((DefaultMutableTreeNode)node,
            (DefaultMutableTreeNode)pnode,
            pnode.getChildCount());
    }

     /**
     * Checks if a file is already opoen.
     */
    private boolean isFileOpen(String filename)
    {
        boolean isOpen = false;

        // find the file by matching its file name and close the file
        FileFormat theFile = null;
        Iterator iterator = fileList.iterator();
        while(iterator.hasNext())
        {
            theFile = (FileFormat)iterator.next();
            if (theFile.getFilePath().equals(filename))
            {
                isOpen = true;
                break;
            }
        } // while(iterator.hasNext())

        return isOpen;
    }

    /** creates a popup menu for a right mouse click on a data object */
    private JPopupMenu createPopupMenu()
    {
        JPopupMenu menu = new JPopupMenu();
        JMenuItem item;

        JMenu newOjbectMenu = new JMenu("New");
        menu.add(newOjbectMenu);
        editGUIs.add(newOjbectMenu);

        item = new JMenuItem( "Group", ViewProperties.getFoldercloseIcon());
        item.addActionListener(this);
        item.setActionCommand("Add group");
        newOjbectMenu.add(item);

        item = new JMenuItem( "Dataset", ViewProperties.getDatasetIcon());
        item.addActionListener(this);
        item.setActionCommand("Add dataset");
        newOjbectMenu.add(item);

        item = new JMenuItem( "Image", ViewProperties.getImageIcon());
        item.addActionListener(this);
        item.setActionCommand("Add image");
        newOjbectMenu.add(item);

        menu.addSeparator();

        item = new JMenuItem( "Open");
        item.setMnemonic(KeyEvent.VK_O);
        item.addActionListener(this);
        item.setActionCommand("Open data");
        menu.add(item);

        item = new JMenuItem( "Open As");
        item.setMnemonic(KeyEvent.VK_A);
        item.addActionListener(this);
        item.setActionCommand("Open data as");
        menu.add(item);

        menu.addSeparator();

        item = new JMenuItem( "Copy");
        item.setMnemonic(KeyEvent.VK_C);
        item.addActionListener(this);
        item.setActionCommand("Copy object");
        menu.add(item);

        item = new JMenuItem( "Paste");
        item.setMnemonic(KeyEvent.VK_P);
        item.addActionListener(this);
        item.setActionCommand("Paste object");
        menu.add(item);
        editGUIs.add(item);

        item = new JMenuItem( "Delete");
        item.setMnemonic(KeyEvent.VK_D);
        item.addActionListener(this);
        item.setActionCommand("Cut object");
        menu.add(item);
        editGUIs.add(item);

        menu.addSeparator();

        item = new JMenuItem( "Save to");
        item.setMnemonic(KeyEvent.VK_S);
        item.addActionListener(this);
        item.setActionCommand("Save object to file");
        menu.add(item);

        item = new JMenuItem( "Rename");
        item.setMnemonic(KeyEvent.VK_R);
        item.addActionListener(this);
        item.setActionCommand("Rename object");
        menu.add(item);
        editGUIs.add(item);

        menu.addSeparator();

        item = new JMenuItem( "Show Properties");
        item.addActionListener(this);
        item.setActionCommand("Show object properties");
        menu.add(item);

        item = new JMenuItem( "Show Properties As");
        item.addActionListener(this);
        item.setActionCommand("Show object properties as");
        menu.add(item);

        return menu;
    }

    /** display the popupmenu of data properties */
    private void showPopupMenu(MouseEvent e)
    {
        int x = e.getX();
        int y = e.getY();

        HObject selectedObject = ((HObject)(selectedNode.getUserObject()));
        boolean isReadOnly = selectedObject.getFileFormat().isReadOnly();
        setEnabled(editGUIs, !isReadOnly);

        if (selectedObject instanceof Group)
        {
            popupMenu.getComponent(2).setEnabled(false); // "open" menuitem
            popupMenu.getComponent(3).setEnabled(false); // "open as" menuitem
        }
        else {
            popupMenu.getComponent(2).setEnabled(true);
            popupMenu.getComponent(3).setEnabled(true);
        }

        popupMenu.show((JComponent)e.getSource(), x, y);
    }

    /** disable/enable GUI components */
    private static void setEnabled(List list, boolean b)
    {
        Component item = null;
        Iterator it = list.iterator();
        while (it.hasNext())
        {
            item = (Component)it.next();
            item.setEnabled(b);
        }
    }

    /** Save the current file into HDF4.
     *  Since HDF4 does not support packing. The source file is
     *  copied into the new file with the exact same content.
     */
    private final void saveAsHDF4(FileFormat srcFile)
    {
        if(srcFile == null)
        {
            toolkit.beep();
            JOptionPane.showMessageDialog(
            this,
            "Select a file to save.",
            "HDFView",
            JOptionPane.ERROR_MESSAGE);
            return;
        }

        JFrame owner = (viewer == null) ? new JFrame() : (JFrame)viewer;
        String currentDir = srcFile.getParent();
        NewFileDialog dialog = new NewFileDialog(owner, currentDir, FileFormat.FILE_TYPE_HDF4, getCurrentFiles());
        dialog.show();

        if (!dialog.isFileCreated())
            return;

        String filename = dialog.getFile();

        // since cannot pack hdf4, simple copy the whole phyisical file
        int length = 0;
        int bsize = 512;
        byte[] buffer;
        BufferedInputStream bi = null;
        BufferedOutputStream bo = null;

        try {
            bi = new BufferedInputStream(new FileInputStream(srcFile.getFilePath()));
        }
        catch (Exception ex )
        {
            toolkit.beep();
            JOptionPane.showMessageDialog(
            this,
            ex.getMessage()+"\n"+filename,
            "HDFView",
            JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            bo = new BufferedOutputStream( new FileOutputStream (filename));
        }
        catch (Exception ex )
        {
            try { bi.close(); } catch (Exception ex2 ) {}
            toolkit.beep();
            JOptionPane.showMessageDialog(
            this,
            ex,
            "HDFView",
            JOptionPane.ERROR_MESSAGE);
            return;
        }

        buffer = new byte[bsize];
        try { length = bi.read(buffer,0,bsize); }
        catch (Exception ex ) { length = 0; }
        while ( length > 0 )
        {
            try {
            bo.write(buffer, 0, length);
            length = bi.read(buffer,0,bsize);
            }
            catch (Exception ex ) { length = 0; }
        }

        try { bo.flush(); } catch (Exception ex ) {}
        try { bi.close(); } catch (Exception ex ) {}
        try { bo.close(); } catch (Exception ex ) {}

        try {
            FileFormat newFile = openFile(filename, FileFormat.WRITE);
        } catch (Exception ex) {
            toolkit.beep();
            JOptionPane.showMessageDialog(
            this,
            ex.getMessage()+"\n"+filename,
            "HDFView",
            JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Copy the current file into a new file. The new file does not
     * include the inaccessible objects. Values of reference dataset
     * are not updated in the new file.
     */
    private void saveAsHDF5(FileFormat srcFile)
    {
        if (srcFile == null) {
            toolkit.beep();
            JOptionPane.showMessageDialog(
            this,
            "Select a file to save.",
            "HDFView",
            JOptionPane.ERROR_MESSAGE);
            return;
        }

        TreeNode root = srcFile.getRootNode();
        if(root == null)
        {
            toolkit.beep();
            JOptionPane.showMessageDialog(
            this,
            "The file is empty.",
            "HDFView",
            JOptionPane.ERROR_MESSAGE);
            return;
        }

        JFrame owner = (viewer == null) ? new JFrame() : (JFrame)viewer;
        NewFileDialog dialog = new NewFileDialog(owner, srcFile.getParent(),
            FileFormat.FILE_TYPE_HDF5, getCurrentFiles());
        dialog.show();

        if (!dialog.isFileCreated())
            return;

        String filename = dialog.getFile();

        List objList = new Vector();
        DefaultMutableTreeNode node = null;
        int n = root.getChildCount();
        for (int i=0; i<n; i++)
        {
            node = (DefaultMutableTreeNode)root.getChildAt(i);
            objList.add(node.getUserObject());
        }

        FileFormat newFile = null;
        try {
            newFile = openFile(filename, FileFormat.WRITE);
        } catch (Exception ex) {
            toolkit.beep();
            JOptionPane.showMessageDialog(
            this,
            ex.getMessage()+"\n"+filename,
            "HDFView",
            JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (newFile == null)
            return;

        TreeNode pnode = newFile.getRootNode();

        pasteObject(objList, pnode, newFile);

        Group srcGroup = (Group) ((DefaultMutableTreeNode)root).getUserObject();
        Group dstGroup = (Group) ((DefaultMutableTreeNode)newFile.getRootNode()).getUserObject();
        Object[] parameter = new Object[2];
        Class classHOjbect = null;
        Class[] parameterClass = new Class[2];
        Method method = null;

        // copy attributes of the root group
        try {
            parameter[0] = srcGroup;
            parameter[1] = dstGroup;
            classHOjbect = Class.forName("ncsa.hdf.object.HObject");
            parameterClass[0] = parameterClass[1] = classHOjbect;
            method = newFile.getClass().getMethod("copyAttributes", parameterClass);
            method.invoke(newFile, parameter);
        } catch (Exception ex) {
            toolkit.beep();
            JOptionPane.showMessageDialog(
            this,
            ex,
            "HDFView",
            JOptionPane.ERROR_MESSAGE);
        }

        // update reference datasets
        parameter[0] = srcGroup.getFileFormat();
        parameter[1] = newFile;
        parameterClass[0] = parameterClass[1] = parameter[0].getClass();
        try {
            method = newFile.getClass().getMethod("updateReferenceDataset", parameterClass);
            method.invoke(newFile, parameter);
        } catch (Exception ex) {
            toolkit.beep();
            JOptionPane.showMessageDialog(
            this,
            ex,
            "HDFView",
            JOptionPane.ERROR_MESSAGE);
        }
    }

    /** copy selected objects */
    private void copyObject()
    {
        objectsToCopy = getSelectedObjects();
    }

    /** paste selected objects */
    private void pasteObject()
    {
        TreeNode pnode = selectedNode;

        if (objectsToCopy == null ||
            objectsToCopy.size() <=0 ||
            pnode == null)
            return;

        FileFormat srcFile = ((HObject)objectsToCopy.get(0)).getFileFormat();
        FileFormat dstFile = getSelectedFile();
        FileFormat h5file = FileFormat.getFileFormat(FileFormat.FILE_TYPE_HDF5);
        FileFormat h4file = FileFormat.getFileFormat(FileFormat.FILE_TYPE_HDF4);

        if (srcFile == null)
        {
            toolkit.beep();
            JOptionPane.showMessageDialog(
                this,
                "Source file is null.",
                "HDFView",
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        else if (dstFile == null)
        {
            toolkit.beep();
            JOptionPane.showMessageDialog(
                this,
                "Destination file is null.",
                "HDFView",
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        else if (srcFile.isThisType(h4file) && dstFile.isThisType(h5file))
        {
            toolkit.beep();
            JOptionPane.showMessageDialog(
                this,
                "Unsupported operation: cannot copy HDF4 object to HDF5 file",
                "HDFView",
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        else if (srcFile.isThisType(h5file) && dstFile.isThisType(h4file))
        {
            toolkit.beep();
            JOptionPane.showMessageDialog(
                this,
                "Unsupported operation: cannot copy HDF5 object to HDF4 file",
                "HDFView",
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (pnode.isLeaf()) pnode = pnode.getParent();
        Group pgroup = (Group)((DefaultMutableTreeNode)pnode).getUserObject();
        String fullPath = pgroup.getPath()+pgroup.getName();
        if (pgroup.isRoot()) fullPath = HObject.separator;

        String msg = "";
        int msgType = JOptionPane.QUESTION_MESSAGE;
        if (srcFile.isThisType(h4file))
        {
            msg = "WARNING: object can not be deleted after it is copied.\n\n";
            msgType = JOptionPane.WARNING_MESSAGE;
        }

        msg += "Do you want to copy the selected object(s) to \nGroup: "+
            fullPath + "\nFile: "+ dstFile.getFilePath();

        int op = JOptionPane.showConfirmDialog(this,
            msg,
            "Copy object",
            JOptionPane.YES_NO_OPTION,
            msgType);

        if (op == JOptionPane.NO_OPTION)
            return;

        pasteObject(objectsToCopy, pnode, dstFile);

        objectsToCopy = null;
    }

    /** paste selected objects */
    private void pasteObject(List objList, TreeNode pnode, FileFormat dstFile)
    {
        if (objList == null ||
            objList.size() <=0 ||
            pnode == null)
            return;

        FileFormat srcFile = ((HObject)objList.get(0)).getFileFormat();
        Group pgroup = (Group)((DefaultMutableTreeNode)pnode).getUserObject();

        HObject theObj=null;
        TreeNode newNode = null;
        Iterator iterator = objList.iterator();
        while (iterator.hasNext())
        {
            newNode = null;
            theObj = (HObject)iterator.next();

            if ( (theObj instanceof Group) && ((Group)theObj).isRoot())
            {
                toolkit.beep();
                JOptionPane.showMessageDialog(
                this,
                "Unsupported operation: cannot copy the root group",
                "HDFView",
                JOptionPane.ERROR_MESSAGE);
                return;
            }

            // check if it creates infinite loop
            Group pg = pgroup;
            while (!pg.isRoot())
            {
                if ( theObj.equals(pg))
                {
                    toolkit.beep();
                    JOptionPane.showMessageDialog(
                    this,
                    "Unsupported operation: cannot copy a group to itself.",
                    "HDFView",
                    JOptionPane.ERROR_MESSAGE);
                    return;
                }
                pg = pg.getParent();
            }

            try {
                newNode = dstFile.copy(theObj, pgroup);
            } catch (Exception ex)
            {
                toolkit.beep();
                JOptionPane.showMessageDialog(
                this,
                ex,
                "HDFView",
                JOptionPane.ERROR_MESSAGE);
                //newNode = null;
            }

            // add the node to the tree
            if (newNode != null)
                insertNode(newNode, pnode);

        } // while (iterator.hasNext())
    }

    private void removeSelectedObjects()
    {
        FileFormat theFile = getSelectedFile();
        if (theFile.isThisType(FileFormat.getFileFormat(FileFormat.FILE_TYPE_HDF4)))
        {
            toolkit.beep();
            JOptionPane.showMessageDialog(
                this,
                "Unsupported operation: cannot delete HDF4 object.",
                "HDFView",
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        List objs = getSelectedObjects();
        if (objs == null || objs.size()<=0)
            return;

        int op = JOptionPane.showConfirmDialog(this,
            "Do you want to remove all the selected object(s) ?",
            "Remove object",
            JOptionPane.YES_NO_OPTION);

        if (op == JOptionPane.NO_OPTION)
            return;

        Iterator it = objs.iterator();
        String frameName = "";
        HObject theObj = null;

        while (it.hasNext()) {
            theObj = (HObject)it.next();

            // cannot delete root
            if (theObj instanceof Group) {
                Group g = (Group)theObj;
                if (g.isRoot()) {
                    toolkit.beep();
                    JOptionPane.showMessageDialog(
                        this,
                        "Unsupported operation: cannot delete the file root.",
                        "HDFView",
                        JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }

            try {
                theFile.delete(theObj);
            } catch (Exception ex) {
                toolkit.beep();
                JOptionPane.showMessageDialog(
                    this,
                    ex,
                    "HDFView",
                    JOptionPane.ERROR_MESSAGE);
            }

            if (theObj.equals(selectedObject))
                selectedObject = null;

            DataView dataView = viewer.getDataView(theObj);
            if (dataView != null) {
                dataView.dispose(); // close the opened data
            }
        } //  while (it.hasNext())

        removeSelectedNodes();
    }

    /** remove selected nodes from the tree */
    private void removeSelectedNodes()
    {
        TreePath[] currentSelections = tree.getSelectionPaths();

        if (currentSelections == null || currentSelections.length <=0)
            return;

        for (int i=0; i< currentSelections.length; i++) {
            DefaultMutableTreeNode currentNode = (DefaultMutableTreeNode) (currentSelections[i].getLastPathComponent());
            DefaultMutableTreeNode parentNode = (DefaultMutableTreeNode) (currentNode.getParent());
            if (parentNode != null) {
                treeModel.removeNodeFromParent(currentNode);

                // add the two lines to fix bug in HDFView 1.2. Delete a subgroup and
                // then copy the group to another group, the deleted group still exists.
                Group pgroup = (Group)parentNode.getUserObject();
                pgroup.removeFromMemberList((HObject)currentNode.getUserObject());

                if (currentNode.equals(selectedNode)) {
                    selectedNode = null;
                    selectedFile = null;
                }
            } // if (parentNode != null) {
        } // for (int i=0; i< currentSelections.length; i++) {
    }

    /**
     * Returns a list of all user objects that traverses the subtree rooted
     * at this node in breadth-first order..
     * @param node the node to start with.
     */
    private final List breadthFirstUserObjects(TreeNode node)
    {
        if (node == null)
            return null;

        Vector list = new Vector();
        DefaultMutableTreeNode theNode = null;
        Enumeration enum = ((DefaultMutableTreeNode)node).breadthFirstEnumeration();
        while(enum.hasMoreElements())
        {
            theNode = (DefaultMutableTreeNode)enum.nextElement();
            list.add(theNode.getUserObject());
        }

        return list;
    }

    private void addGroup()
    {
        if (selectedObject == null || selectedNode == null)
            return;

       Group pGroup = null;
        if (selectedObject instanceof Group)
            pGroup = (Group)selectedObject;
        else
            pGroup = (Group)((DefaultMutableTreeNode)selectedNode.getParent()).getUserObject();

        NewGroupDialog dialog = new NewGroupDialog(
            (JFrame)viewer,
            pGroup,
            breadthFirstUserObjects(selectedObject.getFileFormat().getRootNode()));
        dialog.show();

        HObject obj = (HObject)dialog.getObject();
        if (obj == null)
            return;

        Group pgroup = dialog.getParentGroup();
        try { this.addObject(obj, pgroup); }
        catch (Exception ex) {
            toolkit.beep();
            JOptionPane.showMessageDialog(
                this,
                ex,
                "HDFView",
                JOptionPane.ERROR_MESSAGE);
            return;
        }
    }

    private void addDataset()
    {
        if (selectedObject == null || selectedNode == null)
            return;

        Group pGroup = null;
        if (selectedObject instanceof Group)
            pGroup = (Group)selectedObject;
        else
            pGroup = (Group)((DefaultMutableTreeNode)selectedNode.getParent()).getUserObject();

        NewDatasetDialog dialog = new NewDatasetDialog(
            (JFrame)viewer,
            pGroup,
            breadthFirstUserObjects(selectedObject.getFileFormat().getRootNode()));
        dialog.show();

        HObject obj = (HObject)dialog.getObject();
        if (obj == null)
            return;

        Group pgroup = dialog.getParentGroup();
        try { addObject(obj, pgroup); }
        catch (Exception ex) {
            toolkit.beep();
            JOptionPane.showMessageDialog(
                this,
                ex,
                "HDFView",
                JOptionPane.ERROR_MESSAGE);
            return;
        }
    }

    private void addImage()
    {
        if (selectedObject == null || selectedNode == null)
            return;

        Group pGroup = null;
        if (selectedObject instanceof Group)
            pGroup = (Group)selectedObject;
        else
            pGroup = (Group)((DefaultMutableTreeNode)selectedNode.getParent()).getUserObject();

        NewImageDialog dialog = new NewImageDialog(
            (JFrame)viewer,
            pGroup,
            breadthFirstUserObjects(selectedObject.getFileFormat().getRootNode()));
        dialog.show();

        HObject obj = (HObject)dialog.getObject();
        if (obj == null)
            return;

        Group pgroup = dialog.getParentGroup();
        try { this.addObject(obj, pgroup); }
        catch (Exception ex) {
            toolkit.beep();
            JOptionPane.showMessageDialog(
                this,
                ex,
                "HDFView",
                JOptionPane.ERROR_MESSAGE);
            return;
        }
    }

    private void renameObject()
    {
        if (selectedObject == null)
            return;

        if (selectedObject instanceof Group &&
            ((Group)selectedObject).isRoot())
        {
            toolkit.beep();
            JOptionPane.showMessageDialog(this,
                "Cannot rename the root.", "HDFView", JOptionPane.ERROR_MESSAGE);
            return;
        }

        boolean isH4 = selectedObject.getFileFormat().isThisType(FileFormat.getFileFormat(FileFormat.FILE_TYPE_HDF4));

        if (isH4)
        {
            toolkit.beep();
            JOptionPane.showMessageDialog(this, "Cannot rename HDF4 object.",
                "HDFView", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String oldName = selectedObject.getName();
        String newName = JOptionPane.showInputDialog(this, "Rename \""+ oldName + "\" to:",
                "Rename...", JOptionPane.INFORMATION_MESSAGE);

        if (newName == null)
            return;

        newName = newName.trim();
        if (newName == null ||
            newName.length()==0 ||
            newName.equals(oldName))
         return;

        try { selectedObject.setName(newName); }
        catch (Exception ex)
        {
            toolkit.beep();
            JOptionPane.showMessageDialog(this, ex.getMessage(), "HDFView", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Implementing java.io.ActionListener
    public void actionPerformed(ActionEvent e)
    {
        String cmd = e.getActionCommand();

        if (cmd.equals("Add group")) {
            addGroup();
        }
        else if (cmd.equals("Add dataset")) {
            addDataset();
        }
        else if (cmd.equals("Add image")) {
            addImage();
        }
        else if (cmd.startsWith("Open data"))
        {
            if (cmd.equals("Open data"))
                isDefaultDisplay = true;
            else
                isDefaultDisplay = false;

            try { showDataContent(selectedObject); }
            catch (Throwable err)
            {
                toolkit.beep();
                JOptionPane.showMessageDialog(this,
                    err,
                    "HDFView",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
        else if (cmd.equals("Copy object")) {
            copyObject();
        }
        else if (cmd.equals("Paste object")) {
            pasteObject();
        }
        else if (cmd.equals("Cut object")) {
            removeSelectedObjects();
        }
        else if (cmd.equals("Save object to file")) {
            if (selectedObject == null)
                return;

            if (selectedObject instanceof Group &&
                ((Group)selectedObject).isRoot()) {
                toolkit.beep();
                JOptionPane.showMessageDialog(
                    this,
                    "Cannot save the root group.\nUse \"Save As\" from file menu to save the whole file",
                    "HDFView",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }

            String filetype = FileFormat.FILE_TYPE_HDF4;
            boolean isH5 = selectedObject.getFileFormat().isThisType(FileFormat.getFileFormat(FileFormat.FILE_TYPE_HDF5));
            if (isH5) filetype = FileFormat.FILE_TYPE_HDF5;

            NewFileDialog dialog = new NewFileDialog(
                (JFrame)viewer,
                selectedObject.getFileFormat().getParent(),
                filetype,
                fileList);
            dialog.show();

            if (!dialog.isFileCreated()) {
                return;
            }

            String filename = dialog.getFile();
            FileFormat dstFile = null;
            try {
                dstFile = openFile(filename, FileFormat.WRITE);
            } catch (Exception ex)
            {
                toolkit.beep();
                JOptionPane.showMessageDialog(
                    this,
                    ex.getMessage()+"\n"+filename,
                    "HDFView",
                    JOptionPane.ERROR_MESSAGE);
            }
            List objList = new Vector(2);
            objList.add(selectedObject);
            pasteObject(objList, dstFile.getRootNode(), dstFile);
        }
        else if (cmd.equals("Rename object")) {
            renameObject();
        }
        else if (cmd.startsWith("Show object properties")) {
            if (cmd.equals("Show object properties"))
                isDefaultDisplay = true;
            else
                isDefaultDisplay = false;

            try {
                MetaDataView theView = showMetaData(selectedObject);
            }
            catch (Exception ex) {
                toolkit.beep();
                JOptionPane.showMessageDialog(
                    this,
                    ex,
                    "HDFView",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Opens a file and retrieves the file structure of the file.
     * It also can be used to create a new file by setting the accessID to
     * FileFormat.CREATE.
     *
     * <p>
     * Subclasses must implement this function to take appropriate steps to
     * open a file.</p>
     *
     * @param filename the name of the file to open.
     * @param accessID identifier for the file access. Valid value of accessID is:
     * <ul>
     * <li>FileFormat.READ --- allow read-only access to file.</li>
     * <li>FileFormat.WRITE --- allow read and write access to file.</li>
     * <li>FileFormat.CREATE --- create a new file.</li>
     * </ul>
     *
     * @return the FileFormat of this file if successful; otherwise returns null.
     */
    public FileFormat openFile(String filename, int accessID)
        throws Exception {
        FileFormat fileFormat = null;
        MutableTreeNode fileRoot = null;
        String msg = "";

        if (isFileOpen(filename))
            throw new UnsupportedOperationException("File is in use.");

/*
        boolean ish4 = DefaultFileFilter.isHDF4(filename);
        boolean ish5=DefaultFileFilter.isHDF5(filename);

        if (ish4 && (FileFormat.getFileFormat(FileFormat.FILE_TYPE_HDF4) == null))
            throw new UnsupportedOperationException("HDF4 is not supported.");

        if (ish5 && (FileFormat.getFileFormat(FileFormat.FILE_TYPE_HDF5) == null))
            throw new UnsupportedOperationException("HDF5 is not supported.");
*/

        Enumeration keys = FileFormat.getFileFormatKeys();
        String theKey = null;
        while (keys.hasMoreElements()) {
            theKey = (String)keys.nextElement();
            if (theKey.equals(FileFormat.FILE_TYPE_HDF4) ||
                theKey.equals(FileFormat.FILE_TYPE_HDF5))
                continue;

            FileFormat theformat = FileFormat.getFileFormat(theKey);
            if (theformat.isThisType(filename)) {
                fileFormat = theformat.open(filename, accessID);
                break;
            }
        }

        // search for HDF4 and HDF5 last. File format built on hdf4 or hdf5
        // will be chosen first.
        if (fileFormat == null) {
            FileFormat theformat = FileFormat.getFileFormat(FileFormat.FILE_TYPE_HDF4);
            if (theformat.isThisType(filename)) {
                fileFormat = theformat.open(filename, accessID);
            } else {
                theformat = FileFormat.getFileFormat(FileFormat.FILE_TYPE_HDF5);
                if (theformat.isThisType(filename)) {
                    fileFormat = theformat.open(filename, accessID);
                }
            }
        }

        if (fileFormat == null) {
            throw new java.io.IOException("Unsupported fileformat - "+filename);
        }

        try {
            fileFormat.setMaxMembers(ViewProperties.getMaxMembers());
            fileFormat.setStartMembers(ViewProperties.getStartMembers());
            fileFormat.open();
        } finally { ; }

        fileRoot = (MutableTreeNode)fileFormat.getRootNode();
        if (fileRoot != null) {
            insertNode(fileRoot, root);

            int currentRowCount = tree.getRowCount();
            if (currentRowCount>0) tree.expandRow(tree.getRowCount()-1);

            fileList.add(fileFormat);
        }

        return fileFormat;
    }

    /**
     * close a file
     * @param file the file to close
     */
    public void closeFile(FileFormat file)
        throws Exception {
        // find the file node in the tree and removed it from the tree.
        FileFormat theFile = null;
        DefaultMutableTreeNode theNode = null;
        Enumeration enumeration = root.children();
        while(enumeration.hasMoreElements())
        {
            theNode = (DefaultMutableTreeNode)enumeration.nextElement();
            Group g = (Group)theNode.getUserObject();
            theFile = g.getFileFormat();

            if (theFile.equals(file)) {
                treeModel.removeNodeFromParent(theNode);
                try { theFile.close(); } catch (Exception ex) {}
                fileList.remove(theFile);
                if (theFile.equals(selectedFile)) {
                    selectedFile = null;
                    selectedNode = null;
                }
                break;
            }
        } //while(enumeration.hasMoreElements())
    }

    /**
     * save a file
     * @param file the file to save
     */
    public void saveFile(FileFormat file) throws Exception {
        if (file == null) {
            toolkit.beep();
            JOptionPane.showMessageDialog(
            this,
            "Select a file to save.",
            "HDFView",
            JOptionPane.ERROR_MESSAGE);
            return;
        }

        boolean isH4 = file.isThisType(FileFormat.getFileFormat(FileFormat.FILE_TYPE_HDF4));
        boolean isH5 = file.isThisType(FileFormat.getFileFormat(FileFormat.FILE_TYPE_HDF5));

        if (isH5)
            saveAsHDF5(file);
        else if (isH4)
            saveAsHDF4(file);
        else {
            toolkit.beep();
            JOptionPane.showMessageDialog(
            this,
            "Saving file is not supported for this file type",
            "HDFView",
            JOptionPane.ERROR_MESSAGE);
            return;
        }
    }


    /**
     * Gets the selected the file.
     * When multiple files are open, we need to know which file is currently
     * selected.
     *
     * @return the FileFormat of the selected file.
     */
    public FileFormat getSelectedFile() {
        return selectedFile;
    }

    /**
     * Gets a list of selected object in the tree.
     * Obtaining a list of current selected objects is necessary for copy/paste/delete
     * objects.
     *
     * @return a list of selected object in the tree.
     */
    public List getSelectedObjects() {
        TreePath[] paths = tree.getSelectionPaths();
        if (paths == null || paths.length <=0)
            return null;

        List objs = new Vector();
        HObject theObject = null;
        DefaultMutableTreeNode currentNode = null;
        for (int i=0; i<paths.length; i++)
        {
            currentNode = (DefaultMutableTreeNode) (paths[i].getLastPathComponent());
            theObject = (HObject)currentNode.getUserObject();
            if (theObject != null)
                objs.add(theObject);
        }

        return objs;
    }

    /**
     * @return the current selected object in the tree.
     */
    public HObject getCurrentObject() {
        return selectedObject;
    }

    /**
     * Dispaly the content of a data object.
     * @param dataObject the data object
     * @return the dataview that displays the data content
     * @throws Exception
     */
    public DataView showDataContent(HObject dataObject)
        throws Exception {

        if (dataObject == null ||!(dataObject instanceof Dataset))
            return null; // can only display dataset

        Dataset d = (Dataset)dataObject;
        if (d.getRank() <= 0) d.init();
        boolean isText = (d instanceof ScalarDS && ((ScalarDS)d).isText());
        boolean isImage = (d instanceof ScalarDS && ((ScalarDS)d).isImage());
        boolean isDisplayTypeChar = false;
        boolean isTransposed = false;
        String dataViewName = null;

        JInternalFrame theFrame = (JInternalFrame)viewer.getDataView(d);
        if (isDefaultDisplay) {

            if (theFrame != null) {
                theFrame.toFront();
                return null;
            }

            if (isText) {
                dataViewName = (String)HDFView.getListOfTextView().get(0);
            }
            else if (isImage) {
                dataViewName = (String)HDFView.getListOfImageView().get(0);
            }
            else {
                dataViewName = (String)HDFView.getListOfTableView().get(0);
            }
        } else {
            // make new selection and reload the dataset
            DataOptionDialog dialog = new DataOptionDialog(viewer, d);
            dialog.show();
            if (dialog.isCancelled())
                return null;

            if (theFrame != null) // discard the displayed data
                ((DataView)theFrame).dispose();
            isImage = dialog.isImageDisplay();
            isDisplayTypeChar = dialog.isDisplayTypeChar();
            isTransposed = dialog.isTransposed();
            dataViewName = dialog.getDataViewName();
        }

        Object theView = null;
        Class theClass = ViewProperties.loadExtClass().loadClass(dataViewName);
        Object[] initargs = {viewer};
        if (dataViewName.startsWith("ncsa.hdf.view.DefaultTableView")) {
            Object[] tmpargs = {viewer, new Boolean(isDisplayTypeChar), new Boolean(isTransposed)};
            initargs = tmpargs;
        }
        theView = Tools.newInstance(theClass, initargs);
        viewer.addDataView((DataView)theView);

        return (DataView)theView;
    }

    /**
     * Displays the meta data of a data object.
     * @param dataObject teh data object
     * @return the MetaDataView that displays the MetaData of the data object
     * @throws Exception
     */
    public MetaDataView showMetaData(HObject dataObject)
        throws Exception {

        if (dataObject == null)
            return null;

        List metaDataViewList = HDFView.getListOfMetaDataView();
        if (metaDataViewList == null || metaDataViewList.size() <=0)
            return null;

        int n = metaDataViewList.size();
        Class viewClass = null;
        String className = (String)metaDataViewList.get(0);

        if (!isDefaultDisplay && n>1) {
            className = (String) JOptionPane.showInputDialog (
                this,
                "Select MetaDataView",
                "HDFView",
                JOptionPane.INFORMATION_MESSAGE,
                null,
                metaDataViewList.toArray(),
                className);
        }

        Class theClass = ViewProperties.loadExtClass().loadClass(className);
        Object[] initargs = {viewer};
        MetaDataView dataView = (MetaDataView)Tools.newInstance(theClass, initargs);

        return dataView;
    }

    /**
     * Adds a new data object to the file.
     * @param newObject the new object to add.
     * @param parentGroup the parent group the object is to add to.
     * @throws Exception
     */
    public void addObject(HObject newObject, Group parentGroup)
        throws Exception {
        if (newObject == null || parentGroup==null)
            return;

        TreeNode pnode = findTreeNode(parentGroup);
        TreeNode newnode = null;
        if (newObject instanceof Group) {
            newnode = new DefaultMutableTreeNode(newObject) {
                public boolean isLeaf() { return false; }
            };
        } else {
            newnode = new DefaultMutableTreeNode(newObject);
        }

        treeModel.insertNodeInto((DefaultMutableTreeNode)newnode,
            (DefaultMutableTreeNode)pnode, pnode.getChildCount());
    }

    /**
     * Returns the JTree which holds the file structure.
     * @return the JTree which holds the file structure.
     */
    public JTree getTree(){
        return tree;
    }

    /**
     * Returns the list of current open files..
     */
    public List getCurrentFiles() {
        return fileList;
    }

    /**
     * Returns the tree node that contains the given data object.
     */
    public TreeNode findTreeNode(HObject obj)
    {
        if (obj == null)
            return null;

        TreeNode theFileRoot = obj.getFileFormat().getRootNode();
        if (theFileRoot == null)
            return null;

        DefaultMutableTreeNode theNode = null;
        HObject theObj = null;
        Enumeration enum = ((DefaultMutableTreeNode)theFileRoot).breadthFirstEnumeration();
        while(enum.hasMoreElements())
        {
            theNode = (DefaultMutableTreeNode)enum.nextElement();
            theObj = (HObject)theNode.getUserObject();
            if (theObj == null)
                continue;
            else if (theObj.equals(obj))
                return theNode;
        }

        return null;
    }

    /**
     * This class is used to change the default icons for tree nodes.
     * @see javax.swing.tree.DefaultTreeCellRenderer
     */
    private class HTreeCellRenderer extends DefaultTreeCellRenderer
    {
        private Icon datasetIcon, imageIcon, tableIcon, textIcon,
            h4Icon, h5Icon, openFolder, closeFolder;

        private HTreeCellRenderer()
        {
            super();

            openFolder = ViewProperties.getFolderopenIcon();
            closeFolder = ViewProperties.getFoldercloseIcon();
            datasetIcon = ViewProperties.getDatasetIcon();
            imageIcon = ViewProperties.getImageIcon();
            h4Icon = ViewProperties.getH4Icon();
            h5Icon = ViewProperties.getH5Icon();
            tableIcon = ViewProperties.getTableIcon();
            textIcon = ViewProperties.getTextIcon();

            if (openFolder != null)
                openIcon = openFolder;
            else
                openFolder = this.openIcon;

            if (closeFolder != null)
                closedIcon = closeFolder;
            else
                closeFolder = closedIcon;

            if (datasetIcon == null)
                datasetIcon = leafIcon;

            if (imageIcon == null)
                imageIcon = leafIcon;

            if (tableIcon == null)
                tableIcon = leafIcon;

            if (textIcon == null)
                textIcon = leafIcon;

            if (h4Icon == null)
                h4Icon = leafIcon;

            if (h5Icon == null)
                h5Icon = leafIcon;

        }

        public Component getTreeCellRendererComponent(
            JTree tree,
            Object value,
            boolean selected,
            boolean expanded,
            boolean leaf,
            int row,
            boolean hasFocus)
        {
            Object theObject = ((DefaultMutableTreeNode)value).getUserObject();
            if (theObject instanceof ScalarDS)
            {
                ScalarDS sd = (ScalarDS)theObject;
                if (sd.isImage())
                    leafIcon = imageIcon;
                else if (sd.isText())
                    leafIcon = textIcon;
                else
                    leafIcon = datasetIcon;
            }
            else if (theObject instanceof CompoundDS) {
                leafIcon = tableIcon;
            }
            else if (theObject instanceof Group) {
                Group g = (Group)theObject;
                if (g.isRoot() &&
                    g.getFileFormat().isThisType(FileFormat.getFileFormat(FileFormat.FILE_TYPE_HDF5)))
                {
                        openIcon = closedIcon = h5Icon;
                }
                else if (g.isRoot() &&
                    g.getFileFormat().isThisType(FileFormat.getFileFormat(FileFormat.FILE_TYPE_HDF4)))
                {
                        openIcon = closedIcon = h4Icon;
                }
                else
                {
                    openIcon = openFolder;
                    closedIcon = closeFolder;
                }
            }

            return super.getTreeCellRendererComponent(
                tree,
                value,
                selected,
                expanded,
                leaf,
                row,
                hasFocus);
        }
    } // private class HTreeCellRenderer

    /**
     * Handle mouse clicks on data object in the tree view.
     * A right mouse-click to show the popup menu for user choice.
     * A doulbe left-mouse-click to display the data content.
     * A single left-mouse-click to select the current data object.
     */
    private class HTreeMouseAdapter extends MouseAdapter
    {
        public void mousePressed(MouseEvent e)
        {
            TreePath selPath = tree.getPathForLocation(e.getX(), e.getY());

            if (selPath == null)
                return;

            selectedNode = (DefaultMutableTreeNode)selPath.getLastPathComponent();
            selectedObject = ((HObject)(selectedNode.getUserObject()));
            FileFormat theFile = selectedObject.getFileFormat();
            if (!theFile.equals(selectedFile))
            {
                // a different file is selected, handle only one file a time
                selectedFile = theFile;
                tree.clearSelection();
                tree.setSelectionPath(selPath);
            }

            int mask = e.getModifiers();

            // right click to show popup menu
            if (mask == MouseEvent.META_MASK)
            {
                int selRow = tree.getRowForLocation(e.getX(), e.getY());

                if (!tree.isRowSelected(selRow))
                {
                    // reselect the node
                    tree.clearSelection();
                    tree.setSelectionRow(selRow);
                }
                showPopupMenu(e);
            }
            // double click to open data content
            else if (e.getClickCount() == 2)
            {
                isDefaultDisplay = true;
                try { showDataContent(selectedObject); }
                catch (Exception ex) {}
            }
        } // public void mousePressed(MouseEvent e)
    } // private class HTreeMouseAdapter extends MouseAdapter
}
