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

import java.awt.Component;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Toolkit;
import java.awt.Frame;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.tree.*;
import ncsa.hdf.object.*;

/**
 * TreeView displays the structure of an HDF4/5 file in a tree with data
 * groups and data objects represented as conventional folders and icons.
 * Users can easily expand or collapse folders to navigate the hierarchical
 * structure of the HDF file. The HDF4/5 group structure is a direct graph.
 * To break loops of the graph, the TreeView disables the groups with loops.
 * So that folders of looped groups cannot be expanded.
 * <p>
 * The TreeView allows users to browse through any HDF4/5 file; starting with
 * all top-level objects in the file's hierarchy. The TreeView allows a user
 * to descend through the hierarchy and navigate among the file's data objects.
 * The content of a data object is loaded only when the object is selected,
 * providing interactive and efficient access to HDF files.
 * <p>
 * The TreeView is used to perform editing action on the file structure and
 * update the change in file and memory. Users can add new data object
 * or delete existing data object/group from the tree. After an object is
 * successfully added or deleted from the file, the change of the structure
 * will also reflect in TreeView.
 *
 * @version 1.3.0 01/10/2002
 * @author Peter X. Cao
 * @see ncsa.hdf.view.ViewManager
 */
public class TreeView extends JPanel
implements ActionListener
{
    /**
     * The main HDFView.
     */
    private final ViewManager viewer;

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

    /**
     * A list open files.
     */
    private final List fileList;

    /**
     * Selected file
     */
    private FileFormat selectedFile;

    /**
     * The current selected node.
     */
    private DefaultMutableTreeNode selectedNode;

    private final Toolkit toolkit;

    /**
     * The popup menu used to display user choice of actions on data object.
     */
    private final JPopupMenu popupMenu;

    /**
     * Constructs a treeview.
     * <p>
     * @param theView the main HDFView.
     */
    public TreeView(ViewManager theView)
    {
        this.viewer = theView;

        root = new DefaultMutableTreeNode()
        {
            public boolean isLeaf() { return false; }
        };

        fileList = new Vector();
        toolkit = Toolkit.getDefaultToolkit();

        // initialize the tree and root
        treeModel = new DefaultTreeModel(root);
        tree = new JTree(treeModel);
        tree.setLargeModel(true);
        tree.setCellRenderer(new HTreeCellRenderer());
        tree.addMouseListener(new HTreeMouseAdapter());
        tree.setRootVisible(false);
        //tree.setShowsRootHandles(true);
        tree.setRowHeight(23);
        int fsize = ViewProperties.getFontSizeInt();
        if (fsize >= 10 && fsize <=20)
        {
            Font font = tree.getFont();
            Font newFont = new Font(font.getName(), font.getStyle(), fsize);
            tree.setFont(newFont);
        }

        // create the popupmenu
        popupMenu = createPopupMenu();

        // reset the scroll increament

        // layout GUI component
        this.setLayout( new BorderLayout() );
        this.add(tree, BorderLayout.CENTER);
    }

    // Implementing java.io.ActionListener
    public void actionPerformed(ActionEvent e)
    {
       Object src = e.getSource();
        if (src instanceof JMenuItem)
        {
            JMenuItem mitem = (JMenuItem)src;
            java.awt.Container pitem = mitem.getParent();
            if (pitem instanceof JPopupMenu)
                viewer.actionPerformed(e);
        }
    }

    /**
     * Opens a file and retrieves the structure of the file.
     * Add the root node of the file to the super root of the tree view.
     * <p>
     * @param filename the name of the file to open.
     * @param flag file access id.
     */
    public FileFormat openFile(String filename, int flag) throws Exception
    {
        FileFormat fileFormat = null;
        MutableTreeNode fileRoot = null;
        String msg = "";

        if (isFileOpen(filename))
        {
            throw new java.io.IOException("File is already open - "+filename);
        }

        Iterator iterator = FileFormat.iterator();
        while (iterator.hasNext())
        {
            FileFormat theformat = (FileFormat)iterator.next();
            if (theformat.isThisType(filename))
            {
                fileFormat = theformat.open(filename, flag);
                break;
            }
        }

        if (fileFormat == null)
        {
            throw new java.io.IOException("Open file failed - "+filename);
        }

        fileFormat.open();
        fileRoot = (MutableTreeNode)fileFormat.getRootNode();
        if (fileRoot != null)
        {
            int[] childIndices = {root.getChildCount()};
            ((DefaultMutableTreeNode)root).add(fileRoot);
            treeModel.nodesWereInserted(root, childIndices);
            tree.expandRow(tree.getRowCount()-1);
            fileList.add(fileFormat);
            viewer.showStatus(filename);
        }

        return fileFormat;
    }

    /**
     * Closes a file.
     * <p>
     * @param filename the name of the file to close.
     */
    public void closeFile(String filename)
    {
        // find the file by matching its file name and close the file
        FileFormat theFile = null;
        Iterator iterator = fileList.iterator();
        while(iterator.hasNext())
        {
            theFile = (FileFormat)iterator.next();
            if (theFile.getFilePath().equals(filename))
            {
                try {
                    theFile.close();
                    fileList.remove(theFile);
                    if (theFile.equals(selectedFile))
                    {
                        selectedFile = null;
                        selectedNode = null;
                    }
                } catch (Exception ex) {
                    viewer.showStatus(ex.toString());
                }
                break;
            }
        } // while(iterator.hasNext())

        // find the file node in the tree and removed it from the tree.
        DefaultMutableTreeNode theNode = null;
        Enumeration enumeration = root.children();
        while(enumeration.hasMoreElements())
        {
            theNode = (DefaultMutableTreeNode)enumeration.nextElement();
            Group g = (Group)theNode.getUserObject();
            if (g.getFile().equals(filename))
            {
                treeModel.removeNodeFromParent(theNode);
                break;
            }
        } //while(enumeration.hasMoreElements())


        Runtime.getRuntime().gc();
    }

    /**
     * Closes all open files.
     */
    public void closeFile()
    {
        FileFormat theFile = null;
        Iterator iterator = fileList.iterator();

        // find the file by matching its file name and close the file
        while(iterator.hasNext())
        {
            theFile = (FileFormat)iterator.next();
            try { theFile.close(); }
            catch (Exception ex) {
                viewer.showStatus(ex.toString());
            }
        }

        try { fileList.clear(); }
        catch (Exception ex) {}

        selectedFile = null;
        selectedNode = null;

        // remove all files from the tree view
        ((DefaultMutableTreeNode)root).removeAllChildren();
        treeModel.reload();

        Runtime.getRuntime().gc();
    }

    /**
     * Returns the list of current open files..
     */
    public List getOpenFiles()
    {
        return fileList;
    }

    /**
     * Returns the selected node.
     */
    public MutableTreeNode getSelectedNode()
    {
        return selectedNode;
    }

    /**
     * Returns the selected nodes.
     */
    public TreePath[] getSelectionPaths()
    {
        return tree.getSelectionPaths();
    }

    /**
     * Clears the selection.
     */
    public void clearSelection()
    {
        tree.clearSelection();
    }

    /**
     * Selects the nodes identified by the specified array of paths
     */
    public void setSelectionPaths(TreePath[] paths)
    {
        tree.setSelectionPaths(paths);
    }

    /**
     * Returns the selected file.
     */
    public FileFormat getSelectedFile()
    {
        return selectedFile;
    }


    /**
     * Returns the selected root node: a child node of the super root.
     */
    public TreeNode getSelectedRootNode()
    {
        if (selectedNode == null)
            return null;

        TreeNode theNode = selectedNode;
        TreeNode pnode = theNode.getParent();
        while (!pnode.equals(root))
        {
            theNode = pnode;
            pnode = theNode.getParent();
        }

        return theNode;
    }

    /**
     * Insert a node into the tree.
     * @param node the node to insert.
     * @param pnode the parent node.
     */
    public void insertNode(TreeNode node, TreeNode pnode)
    {
        if (node == null || pnode==null)
            return;

        treeModel.insertNodeInto((DefaultMutableTreeNode)node,
            (DefaultMutableTreeNode)pnode,
            pnode.getChildCount());
    }


    /**
     * Returns a list of all user objects that traverses the subtree rooted
     * at this node in depth-first order..
     * @param node the node to start with.
     */
    public static List depthFirstUserObjects(TreeNode node)
    {
        if (node == null)
            return null;

        Vector list = new Vector();
        DefaultMutableTreeNode theNode = null;
        Enumeration enum = ((DefaultMutableTreeNode)node).depthFirstEnumeration();
        while(enum.hasMoreElements())
        {
            theNode = (DefaultMutableTreeNode)enum.nextElement();
            list.add(theNode.getUserObject());
        }

        return list;
    }

    /**
     * Returns a list of all user objects that traverses the subtree rooted
     * at this node in breadth-first order..
     * @param node the node to start with.
     */
    public static List breadthFirstUserObjects(TreeNode node)
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

    /**
     * set the tree font.
     */
    public void setTreeFontSize(int fsize)
    {
        if (fsize >= 10 && fsize <=20)
        {
            Font font = tree.getFont();
            Font newFont = new Font(font.getName(), font.getStyle(), fsize);
            tree.setFont(newFont);
        }
    }

    /** Returns a list of all selected objects.
     */
    public List getSelectedObjects()
    {
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

    /** remove selected nodes from the tree */
    public void removeSelectedNodes()
    {
        TreePath[] currentSelections = tree.getSelectionPaths();

        if (currentSelections == null || currentSelections.length <=0)
            return;

        for (int i=0; i< currentSelections.length; i++)
        {
            DefaultMutableTreeNode currentNode = (DefaultMutableTreeNode) (currentSelections[i].getLastPathComponent());
            DefaultMutableTreeNode parentNode = (DefaultMutableTreeNode) (currentNode.getParent());
            if (parentNode != null)
            {
                treeModel.removeNodeFromParent(currentNode);

                // add the two lines to fix bug in HDFView 1.2. Delete a subgroup and
                // then copy the group to another group, the deleted group still exists.
                Group pgroup = (Group)parentNode.getUserObject();
                pgroup.removeFromMemberList((HObject)currentNode.getUserObject());

                if (currentNode.equals(selectedNode))
                {
                    selectedNode = null;
                    selectedFile = null;
                }
            }
        }
    }

    /**
     * Returns the tree node for the given data object.
     */
    public TreeNode findTreeNode(HObject obj)
    {
        if (root == null)
            return null;

        DefaultMutableTreeNode theNode = null;
        HObject theObj = null;
        Enumeration enum = ((DefaultMutableTreeNode)root).breadthFirstEnumeration();
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
     * Returns the root node that contains the given FileFormat.
     */
    public TreeNode findRootNode(FileFormat file)
    {
        if (root == null)
            return null;

        HObject theObj = null;
        FileFormat theFile = null;
        DefaultMutableTreeNode theNode = null;
        Enumeration enum = root.children();
        while(enum.hasMoreElements())
        {
            theNode = (DefaultMutableTreeNode)enum.nextElement();
            theFile = ((HObject)theNode.getUserObject()).getFileFormat();
            if (file.equals(theFile))
                return theNode;
        }

        return null;
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

        item = new JMenuItem( "Save to File");
        item.setMnemonic(KeyEvent.VK_S);
        item.addActionListener(this);
        item.setActionCommand("Save object to file");
        menu.add(item);

        menu.addSeparator();

        JMenu newOjbectMenu = new JMenu("New");
        menu.add(newOjbectMenu);

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

        item = new JMenuItem( "Delete");
        item.setMnemonic(KeyEvent.VK_D);
        item.addActionListener(this);
        item.setActionCommand("Cut object");
        menu.add(item);

        menu.addSeparator();

        item = new JMenuItem( "Properties");
        item.setMnemonic(KeyEvent.VK_P);
        item.addActionListener(this);
        item.setActionCommand("Show object properties");
        menu.add(item);

        return menu;
    }

    /** display the popupmenu of data properties */
    private void showPopupMenu(MouseEvent e)
    {
        int x = e.getX();
        int y = e.getY();

        HObject selectedObject = ((HObject)(selectedNode.getUserObject()));
        if (selectedObject instanceof Group)
        {
            popupMenu.getComponent(0).setEnabled(false);
            popupMenu.getComponent(1).setEnabled(false);
        }
        else
        {
            popupMenu.getComponent(0).setEnabled(true);
            popupMenu.getComponent(1).setEnabled(true);
        }

        if (selectedObject.getFileFormat().isReadOnly())
        {
            popupMenu.getComponent(3).setEnabled(false);
            popupMenu.getComponent(5).setEnabled(false);
            popupMenu.getComponent(7).setEnabled(false);
            popupMenu.getComponent(8).setEnabled(false);
            popupMenu.getComponent(9).setEnabled(false);
        }
        else
        {
            popupMenu.getComponent(3).setEnabled(true);
            popupMenu.getComponent(5).setEnabled(true);
            popupMenu.getComponent(7).setEnabled(true);
            popupMenu.getComponent(8).setEnabled(true);
            popupMenu.getComponent(9).setEnabled(true);
        }

        popupMenu.show((JComponent)e.getSource(), x, y);
    }

    /**
     * This class is used to change the default icons for tree nodes.
     * @see javax.swing.tree.DefaultTreeCellRenderer
     */
    private class HTreeCellRenderer extends DefaultTreeCellRenderer
    {
        /**
         * Icon used to show dataset nodes.
         */
        private Icon datasetIcon;

        /**
         * Icon used to show image nodes.
         */
        private Icon imageIcon;

        /**
         * Icon used to show table nodes.
         */
        private Icon tableIcon;

        /**
         * Icon used to show hdf root nodes.
         */
        private Icon h4Icon, h5Icon;

        private Icon openFolder, closeFolder;

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
                else
                    leafIcon = datasetIcon;
            }
            else if (theObject instanceof CompoundDS)
            {
                leafIcon = tableIcon;
            }
            else if (theObject instanceof Group)
            {
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
    }

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
            HObject selectedObject = ((HObject)(selectedNode.getUserObject()));
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
                tree.clearSelection();
                tree.setSelectionRow(selRow);
                viewer.setSelectedObject(selectedObject);
                showPopupMenu(e);
            }
            // double click to open data content
            else if (e.getClickCount() == 2)
            {
                viewer.setSelectedObject(selectedObject);
                viewer.showDataContent(true);
            }
            // single click to select the current data object
            else if (e.getClickCount() == 1)
            {
                super.mousePressed(e);
                viewer.setSelectedObject(selectedObject);
            }
        } // public void mousePressed(MouseEvent e)
    }

}
