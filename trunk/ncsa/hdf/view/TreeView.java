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
     * The current selected data object.
     */
    private HObject selectedObject;

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

        // initialize the tree and root
        treeModel = new DefaultTreeModel(root);
        tree = new JTree(treeModel);
        tree.setCellRenderer(new HTreeCellRenderer());
        tree.addMouseListener(new HTreeMouseAdapter());
        tree.setRowHeight(20);
        tree.setRootVisible(false);

        // create the popupmenu
        popupMenu = createPopupMenu();

        // layout GUI component
        this.setLayout( new BorderLayout() );
        this.add(tree, BorderLayout.CENTER);
    }

    // Implementing java.io.ActionListener
    public void actionPerformed(ActionEvent e)
    {
        String cmd = e.getActionCommand();

        if (cmd.equals("Open data"))
        {
            viewer.showDataContent(true);
        }
        else if (cmd.equals("Open data as"))
        {
            viewer.showDataContent(false);
        }
        else if (cmd.equals("Show object properties"))
        {
            viewer.showDataInfo();
        }
    }

    /**
     * Opens a file and retrieves the structure of the file.
     * Add the root node of the file to the super root of the tree view.
     * <p>
     * @param filename the name of the file to open.
     */
    public void openFile(String filename) throws Exception
    {
        FileFormat fileFormat = null;
        MutableTreeNode fileRoot = null;
        String msg = "";

        if (isFileOpen(filename))
        {
            throw new java.io.IOException("File is already open - "+filename);
        }

        if (H4File.isThisType(filename))
        {
            fileFormat = new H4File(filename);
        }
        else if (H5File.isThisType(filename))
        {
            fileFormat = new H5File(filename);
        }
        else
        {
            throw new UnsupportedOperationException("File format not supported.");
        }

        if (fileFormat != null)
        {
            fileFormat.open();
            fileRoot = fileFormat.getRootNode();
            if (fileRoot != null)
            {
                int[] childIndices = {root.getChildCount()};
                ((DefaultMutableTreeNode)root).add(fileRoot);
                treeModel.nodesWereInserted(root, childIndices);
                tree.expandRow(tree.getRowCount()-1);
                fileList.add(fileFormat);
                viewer.showStatus(filename);
            }
        }
        else
        {
            throw new java.io.IOException("Open file failed - "+filename);
        }
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

        // remove all files from the tree view
        ((DefaultMutableTreeNode)root).removeAllChildren();
        treeModel.reload();
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
        private Icon hdfIcon;

        private Icon openFolder, closeFolder;

        public HTreeCellRenderer()
        {
            super();

            openFolder = ViewProperties.getFolderopenIcon();
            closeFolder = ViewProperties.getFoldercloseIcon();
            datasetIcon = ViewProperties.getDatasetIcon();
            imageIcon = ViewProperties.getImageIcon();
            hdfIcon = ViewProperties.getHdfIcon();
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

            if (hdfIcon == null)
                hdfIcon = leafIcon;
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
                if (g.isRoot())
                {
                    openIcon = hdfIcon;
                    closedIcon = hdfIcon;
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

            DefaultMutableTreeNode node =
                (DefaultMutableTreeNode)selPath.getLastPathComponent();
            selectedObject = ((HObject)(node.getUserObject()));

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
