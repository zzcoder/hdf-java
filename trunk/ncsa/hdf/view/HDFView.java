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
import java.awt.event.*;
import java.io.*;
import java.util.*;
import javax.swing.text.*;
import javax.swing.event.*;
import javax.swing.text.html.*;
import javax.swing.tree.*;
import java.net.URL;
import java.lang.reflect.*;
import java.awt.Event;
import java.awt.Insets;
import java.awt.Dimension;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.Image;
import java.awt.Font;

/**
 * HDFView is the main class of this HDF visual tool.
 * It is used to layout the graphical components of the hdfview. The major GUI
 * components of the HDFView include Menubar, Toolbar, TreeView, ContentView,
 * and MessageArea.
 * <p>
 * The HDFView is designed in such a way that it does not have direct access to
 * the HDF library. All the HDF library access is done through HDF objects.
 * Therefore, the HDFView package depends on the object package but not the
 * library package. The source code of the view package (ncsa.hdf.view) should
 * be complied with the library package (ncsa.hdf.hdflib and ncsa.hdf.hdf5lib).
 * <p>
 * @version 1.3.0 01/10/2002
 * @author Peter X. Cao
 */
public final class HDFView extends JFrame
implements ViewManager, HyperlinkListener
{
    private static final String aboutHDFView =
        "HDF Viewer, "+ "Version "+ViewProperties.VERSION+"\n"+
        "Copyright "+'\u00a9'+" 2001-2002 University of Illinois.\n"+
        "All rights reserved.";

    /** the directory where the HDFView is installed */
    private String rootDir;

    /** the current working directory */
    private String currentDir;

    /** the current working file */
    private String currentFile;

    /** the view properties */
    private ViewProperties props;

    /** the current selected object */
    private HObject selectedObject;

    /** the list of current selected objects */
    private List objectsToCopy;

    /** the list of most recent files */
    private Vector recentFiles;

    /** GUI component: the TreeView */
    private final TreeView treeView;

    /** GUI component: the panel which is used to display the data content */
    private final JDesktopPane contentPane;

    /** GUI component: the text area for showing status message */
    private final JTextArea statusArea;

    /** GUI component: a list of current data windwos */
    private final JMenu windowMenu;

    /** GUI component: a list of current data object */
    private final JMenu objectMenu;

    /** GUI component: file menu on the menubar */
    private final JMenu fileMenu;

    /** GUI component: window to show the Users' Guide */
    private final JFrame usersGuideWindow;

    /** GUI component: editorPane to show the Users' Guide */
    private final JEditorPane usersGuideEditorPane;

    /** the string buffer holding the status message */
    private final StringBuffer message;

    /** The list of GUI components related to image */
    private final Vector imageGUIs;

    /** The list of GUI components related to table */
    private final Vector tableGUIs;

    /** The list of GUI components related to 3D datasets */
    private final Vector d3GUIs;

    /** The list of GUI components related to editing */
    private final Vector editGUIs;

    /** The list of GUI components related to HDF5 */
    private final Vector h5GUIs;

    /** The list of GUI components related to HDF4 */
    private final Vector h4GUIs;

    /** The URL of the User's Guide. */
    private URL usersGuideURL;

    /** The previous URL of the User's Guide. */
    private URL previousUsersGuideURL;

    /** the text field to display the current ug link */
    private JTextField ugField;

    /** The previously visited URLs for back action. */
    private Stack visitedUsersGuideURLs;

    /** The back button for users guide. */
    private JButton usersGuideBackButton;

    /** Check to show image value */
    private JCheckBoxMenuItem imageValueCheckBox;

    private JButton chartIcon, paletteIcon;

    private int frameOffset;

    /**
     * file access id.
     */
    private int fileAccessID;

    private final Toolkit toolkit;

    /**
     * Constructs the HDFView with a given root directory, where the
     * HDFView is installed, and opens the given file in the viewer.
     * <p>
     * @param root the directory where the HDFView is installed.
     * @param filename the file to open.
     */
    public HDFView(String root, String workDir, String filename)
    {
        super("HDFView");

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        String lf = UIManager.getSystemLookAndFeelClassName();
        if ( lf != null )
        {
            try	{ UIManager.setLookAndFeel( lf ); }
            catch ( Exception ex ) { ex.printStackTrace(); }
        }

        rootDir = root;
        currentDir = workDir;
        currentFile = null;
        selectedObject = null;
        objectsToCopy = null;
        usersGuideURL = null;
        frameOffset = 0;
        fileAccessID = FileFormat.WRITE;
        toolkit = Toolkit.getDefaultToolkit();

        imageGUIs = new Vector();
        d3GUIs = new Vector();
        editGUIs = new Vector();
        tableGUIs = new Vector();
        h4GUIs = new Vector();
        h5GUIs = new Vector();

        // load the view properties
        ViewProperties.loadIcons(rootDir);
        props = new ViewProperties(rootDir);
        try { props.load(); } catch (Exception ex){System.out.println(ex);}
        recentFiles = props.getMRF();

        // initialize GUI components
        statusArea = new JTextArea();
        statusArea.setEditable(false);
        statusArea.setLineWrap(true);
        message = new StringBuffer();
        showStatus("HDFView root - "+rootDir);
        showStatus("User property file - "+props.getPropertyFile());

        // setup the Users guide window
        usersGuideWindow = new JFrame("HDFView User's Guide");
        usersGuideEditorPane = new JEditorPane();

        contentPane = new JDesktopPane()
        {
            public void moveToFront(Component c)
            {
                super.moveToFront(c);

                // enable or disable image GUI components
                if (c instanceof ImageView)
                {
                    HDFView.setEnabled(imageGUIs, true);
                    HObject obj = (HObject) ((DataObserver)c).getDataObject();
                    FileFormat theFile = obj.getFileFormat();
                    HDFView.setEnabled(editGUIs, !theFile.isReadOnly());
                    HDFView.setEnabled(tableGUIs, false);

                    ImageView imgv = (ImageView)c;
                    imgv.setValueVisible(imageValueCheckBox.getState());

                    if (imgv.isTrueColor())
                    {
                        chartIcon.setEnabled(false);
                        paletteIcon.setEnabled(false);
                    }
                    else
                    {
                        chartIcon.setEnabled(true);
                        paletteIcon.setEnabled(true);
                    }
                }
                else if (c instanceof TableView)
                {
                    HDFView.setEnabled(tableGUIs, true);
                    HObject obj = (HObject) ((DataObserver)c).getDataObject();
                    FileFormat theFile = obj.getFileFormat();
                    HDFView.setEnabled(editGUIs, !theFile.isReadOnly());
                    HDFView.setEnabled(imageGUIs, false);
                    chartIcon.setEnabled(true);
                }
                else
                {
                    HDFView.setEnabled(tableGUIs, false);
                    HDFView.setEnabled(imageGUIs, false);
                    chartIcon.setEnabled(false);
                    paletteIcon.setEnabled(false);
                }

                // enable or disable 3D GUI components
                boolean is3D = false;
                if (c instanceof DataObserver)
                {
                    DataObserver dataObs = (DataObserver)c;
                    HObject hObj = (HObject)dataObs.getDataObject();
                    if (hObj instanceof Dataset)
                    {
                        Dataset ds = (Dataset)hObj;

                        is3D = (ds.getRank() > 2);

                        // disable 3D components for true color image
                        if (c instanceof ImageObserver &&
                            hObj instanceof ScalarDS && is3D)
                        {
                            ScalarDS sd = (ScalarDS)hObj;
                            is3D = !sd.isTrueColor();
                        }
                    }
                }

                HDFView.setEnabled(d3GUIs, is3D);

                // disable object menu items. Working on the data content
                Component[] menuItems = objectMenu.getMenuComponents();
                for (int i=0; i<menuItems.length; i++)
                {
                    menuItems[i].setEnabled(false);
                }
                // deselect tree node
                treeView.clearSelection();

            } //public void moveToFront(Component c)
        };

        //        contentPane.setDragMode(JDesktopPane.OUTLINE_DRAG_MODE);
        // BUG on JDK1.4.1_01. The OUTLINE_DRAG_MODE flashes window and cause
        // slow display of the window as the window is moved. OK for JDK1.3.1
        treeView = new TreeView(this);
        windowMenu = new JMenu( "Window" );
        objectMenu = new JMenu( "Object" );
        fileMenu = new JMenu( "File" );

        createMainWindow();

        // disable image and 3D GUI components
        setEnabled(imageGUIs, false);
        setEnabled(d3GUIs, false);
        setEnabled(editGUIs, false);
        setEnabled(tableGUIs, false);
        chartIcon.setEnabled(false);

        Component[] menuItems = windowMenu.getMenuComponents();
        for (int i=0; i<6; i++)
        {
            menuItems[i].setEnabled(false);
        }

        menuItems = objectMenu.getMenuComponents();
        for (int i=0; i<menuItems.length; i++)
        {
            menuItems[i].setEnabled(false);
        }

        File theFile = new File(filename);

        if (theFile.exists())
        {
            if (theFile.isFile())
            {
                currentDir = theFile.getParentFile().getAbsolutePath();
                currentFile = theFile.getAbsolutePath();

                try {
                    treeView.openFile(filename, fileAccessID);
                     try { updateRecentFiles(filename); } catch (Exception ex) {}
                } catch (Exception ex)
                {
                    showStatus(ex.toString());
                }
            }
            else
            {
                this.currentDir = theFile.getAbsolutePath();;
            }
        }

        if (FileFormat.getFileFormat(FileFormat.FILE_TYPE_HDF4) == null)
            setEnabled(h4GUIs, false);

        if (FileFormat.getFileFormat(FileFormat.FILE_TYPE_HDF5) == null)
            setEnabled(h5GUIs, false);
    }

    /**
     * The starting point of this application.
     * <pre>
     * Usage: java(w)
     *        -Dncsa.hdf.hdf5lib.H5.hdf5lib="your HDF5 library path"
     *        -Dncsa.hdf.hdflib.HDFLibrary.hdflib="your HDF4 library path"
     *        -root "the directory where the HDFView is installed"
     *        [filename] "the file to open"
     * </pre>
     */
    public static void main( String args[] )
    {
        String rootDir = System.getProperty("user.dir");
        String workDir = rootDir;

        boolean backup = false;
        File tmpFile = null;
        int i = 0;
        for ( i = 0; i < args.length; i++)
        {
            if ("-root".equalsIgnoreCase(args[i]))
            {
                try {
                    tmpFile = new File(args[++i]);
                    backup = false;
                    if (tmpFile.isDirectory())
                    {
                        rootDir = tmpFile.getPath();
                    }
                    else if (tmpFile.isFile())
                    {
                        rootDir = tmpFile.getParent();
                    }
                } catch (Exception e) {}
            } else {
                backup = true;
            }
        }

        if (backup)
            i--;

        String filename = "";
        if (i>=0 && i < args.length && args[i] != null) {
            filename = args[i];
        }

        HDFView frame = new HDFView(rootDir, workDir, filename);
        frame.pack();
        frame.setVisible(true);
     }


    // Implementing ViewManager
    public Object getSelectedObject()
    {
        return selectedObject;
    }

    // Implementing ViewManager
    public void setSelectedObject(Object data)
    {
        selectedObject = (HObject)data;

        boolean noSelect = (selectedObject == null);

        Component[] menuItems = objectMenu.getMenuComponents();
        for (int i=0; i<menuItems.length; i++)
        {
            menuItems[i].setEnabled(!noSelect);
        }

        if (!noSelect)
        {
            currentFile = selectedObject.getFile();
            setTitle("HDFView - "+currentFile);
            setEnabled(editGUIs, !selectedObject.getFileFormat().isReadOnly());
            setEnabled(imageGUIs, false);
            setEnabled(tableGUIs, false);
        }

        JInternalFrame frame = contentPane.getSelectedFrame();

        if (frame != null)
        {
            try { frame.setSelected(false); }
            catch (Exception ex) {}
        }
    }

    // To do: Implementing ViewManager
    public void showDataContent(boolean isDefaultDisplay)
    {
        if (selectedObject == null ||
            !(selectedObject instanceof Dataset))
            return;

        // check if the data content is already displayed
        JInternalFrame[] frames = contentPane.getAllFrames();
        JInternalFrame theFrame = null;
        if (frames != null)
        {
            for (int i=0; i<frames.length; i++)
            {
                if ( !(frames[i] instanceof DataObserver) )
                    continue;

                HObject obj = (HObject)(((DataObserver)frames[i]).getDataObject());
                if (selectedObject.equals(obj))
                {
                    theFrame = frames[i];
                    break; // data is already displayed
                }
            }
        }

        Dataset d = (Dataset)selectedObject;
        if (d.getRank() <= 0) d.init();
        boolean isText = (d instanceof ScalarDS && ((ScalarDS)d).isText());
        boolean isImage = (d instanceof ScalarDS && ((ScalarDS)d).isImage());

        if (isDefaultDisplay)
        {
            if (theFrame != null)
            {
                theFrame.toFront();

                try {
                    theFrame.setSelected(true);
                } catch (java.beans.PropertyVetoException e) {}

                return;
            }
        }
        else
        {
            // make new selection and reload the dataset
            DataOptionDialog dialog = new DataOptionDialog(this);
            dialog.show();
            if (dialog.isCancelled())
                return;

            if (theFrame != null) // discard the displayed data
                ((DataObserver)theFrame).dispose();
            isImage = dialog.isImageDisplay();
        }

        // try to see if can open the data content
        if (selectedObject instanceof Dataset)
        {
            Dataset data = (Dataset)selectedObject;
            try { data.getData(); }
            catch (Throwable ex) {
                toolkit.beep();
                JOptionPane.showMessageDialog(this,
                    ex.getMessage(),
                    getTitle(),
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        if (isImage)
        {
            ImageView dataView = new ImageView(this);
            if (dataView.getImage() != null)
                addDataWindow(dataView);
        }
        else if (isText)
        {
            TextView dataView = new TextView(this);
            if (dataView.getText() != null)
                addDataWindow(dataView);
        }
        else
        {
            TableView dataView = new TableView(this);
            if (dataView.getTable() != null)
                addDataWindow(dataView);
        }
    }

    // To do: Implementing ViewManager
    public void showDataInfo()
    {
        if (selectedObject == null)
            return;

       MetadataDialog dialog = new MetadataDialog(this);
       dialog.show();
    }

    // To do: Implementing ViewManager
    public void showStatus(String msg)
    {
        message.append(msg);
        message.append("\n");
        statusArea.setText(message.toString());
    }

    // To do: Implementing java.io.ActionListener
    public void actionPerformed(ActionEvent e)
    {
        String cmd = e.getActionCommand();

        if (cmd.equals("Exit"))
        {
            dispose();  // terminate the application
        }
        else if (cmd.equals("Open file"))
        {
            fileAccessID = FileFormat.WRITE;
            JFileChooser fchooser = new JFileChooser(currentDir);
            fchooser.setFileFilter(DefaultFileFilter.getFileFilterHDF());

            int returnVal = fchooser.showOpenDialog(this);
            if(returnVal != JFileChooser.APPROVE_OPTION)
                return;

            File choosedFile = fchooser.getSelectedFile();
            if (choosedFile == null)
                return;

            if (choosedFile.isDirectory())
                currentDir = choosedFile.getPath();
            else
                currentDir = choosedFile.getParent();

            String filename = choosedFile.getAbsolutePath();

            try {
                treeView.openFile(filename, fileAccessID);
                try {  try { updateRecentFiles(filename); } catch (Exception ex) {} } catch (Exception ex) {}
            } catch (Exception ex)
            {
                String msg = "Failed to open file "+filename+"\n"+ex.getMessage();
                if (!(ex instanceof UnsupportedOperationException))
                    msg +="\n\nTry open file read-only";
                toolkit.beep();
                JOptionPane.showMessageDialog(
                    this,
                    msg,
                    getTitle(),
                    JOptionPane.ERROR_MESSAGE);
            }
        }
        else if (cmd.equals("Open file read-only"))
        {
            fileAccessID = FileFormat.READ;

            JFileChooser fchooser = new JFileChooser(currentDir);
            fchooser.setFileFilter(DefaultFileFilter.getFileFilterHDF());

            int returnVal = fchooser.showOpenDialog(this);
            if(returnVal != JFileChooser.APPROVE_OPTION)
                return;

            File choosedFile = fchooser.getSelectedFile();
            if (choosedFile == null)
                return;

            if (choosedFile.isDirectory())
                currentDir = choosedFile.getPath();
            else
                currentDir = choosedFile.getParent();

            String filename = choosedFile.getAbsolutePath();

            try {
                treeView.openFile(filename, fileAccessID);
                try {  try { updateRecentFiles(filename); } catch (Exception ex) {} } catch (Exception ex) {}
            } catch (Exception ex)
            {
                toolkit.beep();
                JOptionPane.showMessageDialog(
                    this,
                    "Failed to open file "+filename+"\n"+ex.getMessage(),
                    getTitle(),
                    JOptionPane.ERROR_MESSAGE);
            }
        }
        else if (cmd.equals("New HDF5 file"))
        {
            NewFileDialog dialog = new NewFileDialog(
                this,
                currentDir,
                FileFormat.FILE_TYPE_HDF5,
                treeView.getOpenFiles());
            dialog.show();

            if (!dialog.isFileCreated())
                return;

            String filename = dialog.getFile();
            try {
                treeView.openFile(filename, fileAccessID);
                try {  try { updateRecentFiles(filename); } catch (Exception ex) {} } catch (Exception ex) {}
            } catch (Exception ex)
            {
                toolkit.beep();
                JOptionPane.showMessageDialog(
                    this,
                    ex.getMessage()+"\n"+filename,
                    getTitle(),
                    JOptionPane.ERROR_MESSAGE);
            }
        }
        else if (cmd.equals("New HDF4 file"))
        {
            NewFileDialog dialog = new NewFileDialog(
                this,
                currentDir,
                FileFormat.FILE_TYPE_HDF4,
                treeView.getOpenFiles());
            dialog.show();

            if (!dialog.isFileCreated())
                return;

            String filename = dialog.getFile();
            try {
                treeView.openFile(filename, fileAccessID);
                 try { updateRecentFiles(filename); } catch (Exception ex) {}
            } catch (Exception ex)
            {
                toolkit.beep();
                JOptionPane.showMessageDialog(
                    this,
                    ex.getMessage()+"\n"+filename,
                    getTitle(),
                    JOptionPane.ERROR_MESSAGE);
            }
        }
        else if (cmd.equals("Save object to file"))
        {
            if (selectedObject == null)
                return;

            if (selectedObject instanceof Group &&
                ((Group)selectedObject).isRoot())
                return;

            String filetype = FileFormat.FILE_TYPE_HDF4;
            boolean isH5 = selectedObject.getFileFormat().isThisType(FileFormat.getFileFormat(FileFormat.FILE_TYPE_HDF5));
            if (isH5) filetype = FileFormat.FILE_TYPE_HDF5;

            NewFileDialog dialog = new NewFileDialog(
                this,
                currentDir,
                filetype,
                treeView.getOpenFiles());
            dialog.show();

            if (!dialog.isFileCreated())
            {
                toolkit.beep();
                JOptionPane.showMessageDialog(
                    this,
                    "Failed to create the new file.",
                    getTitle(),
                    JOptionPane.ERROR_MESSAGE);
                return;
            }

            String filename = dialog.getFile();
            FileFormat dstFile = null;
            try {
                dstFile = treeView.openFile(filename, fileAccessID);
                 try { updateRecentFiles(filename); } catch (Exception ex) {}
            } catch (Exception ex)
            {
                toolkit.beep();
                JOptionPane.showMessageDialog(
                    this,
                    ex.getMessage()+"\n"+filename,
                    getTitle(),
                    JOptionPane.ERROR_MESSAGE);
            }
            List objList = new Vector(2);
            objList.add(selectedObject);
            pasteObject(objList, dstFile.getRootNode(), dstFile);
        }
        else if (cmd.equals("recent.file"))
        {
            JMenuItem mi = (JMenuItem)e.getSource();
            fileAccessID = FileFormat.WRITE;

            String filename = mi.getName();
            try {
                treeView.openFile(filename, fileAccessID);
                 try { updateRecentFiles(filename); } catch (Exception ex) {}
            } catch (Exception ex)
            {
                String msg = "Failed to open file "+filename+"\n"+ex.getMessage();
                if (!(ex instanceof UnsupportedOperationException))
                    msg +="\n\nTry open file read-only";
                toolkit.beep();
                JOptionPane.showMessageDialog(
                    this,
                    msg,
                    getTitle(),
                    JOptionPane.ERROR_MESSAGE);
            }
        }
        else if (cmd.equals("Close file"))
        {
            // close all the data windows of this file
            JInternalFrame[] frames = contentPane.getAllFrames();
            if (frames != null)
            {
                for (int i=0; i<frames.length; i++)
                {
                    HObject obj = (HObject)(((DataObserver)frames[i]).getDataObject());
                    if ( obj.getFile().equals(currentFile))
                    {
                        frames[i].dispose();
                        frames[i] = null;
                    }
                }
            }

            if (contentPane.getComponentCount() <=0)
            {
                // disable image and 3D GUI components if there is no data content window
                setEnabled(imageGUIs, false);
                setEnabled(d3GUIs, false);
                setEnabled(tableGUIs, false);
                chartIcon.setEnabled(false);
            }

            setSelectedObject(null);
            treeView.closeFile(currentFile);
        }
        else if (cmd.equals("Close all file"))
        {
            closeAllWindow();
            setSelectedObject(null);
            treeView.closeFile();
        }
        else if (cmd.equals("Save as HDF"))
        {
            FileFormat selectedFile = treeView.getSelectedFile();

            if (selectedFile == null)
            {
                toolkit.beep();
                JOptionPane.showMessageDialog(
                this,
                "Select a file to save.",
                getTitle(),
                JOptionPane.ERROR_MESSAGE);
                return;
            }

            boolean isH5 = selectedFile.isThisType(FileFormat.getFileFormat(FileFormat.FILE_TYPE_HDF5));
            if (isH5) saveAsHDF5();
            else saveAsHDF4();
        }
        else if (cmd.equals("Open data"))
        {
            try { showDataContent(true); }
            catch (OutOfMemoryError err)
            {
                toolkit.beep();
                JOptionPane.showMessageDialog(this,
                    err.getMessage(),
                    getTitle(),
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
        else if (cmd.equals("Open data as"))
        {
            try { showDataContent(false); }
            catch (OutOfMemoryError err)
            {
                toolkit.beep();
                JOptionPane.showMessageDialog(this,
                    err.getMessage(),
                    getTitle(),
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
        else if (cmd.equals("Copy object"))
        {
            copyObject();
        }
        else if (cmd.equals("Paste object"))
        {
            pasteObject();
        }
        else if (cmd.equals("Cut object"))
        {
            removeSelectedObjects();
        }
        else if (cmd.equals("Add group"))
        {
            addGroup();
        }
        else if (cmd.equals("Add dataset"))
        {
            addDataset();
        }
        else if (cmd.equals("Add image"))
        {
            addImage();
        }
        else if (cmd.equals("Show object properties"))
        {
            showDataInfo();
        }
        else if (cmd.equals("Save table as text"))
        {
            JInternalFrame frame = contentPane.getSelectedFrame();
            if (frame != null && frame instanceof TableView)
            {
                TreePath[] paths = treeView.getSelectionPaths();
                treeView.clearSelection();
                try { ((TableView)frame).saveAsText(); }
                catch (Exception ex)
                {
                    toolkit.beep();
                    JOptionPane.showMessageDialog(this, ex.getMessage(), getTitle(), JOptionPane.ERROR_MESSAGE);
                } finally
                {
                    treeView.setSelectionPaths(paths);
                }
            }
        }
        else if (cmd.equals("Copy data"))
        {
            JInternalFrame frame = contentPane.getSelectedFrame();
            if (frame != null && frame instanceof TableView)
            {
                ((TableView)frame).copyData();
            }
        }
        else if (cmd.equals("Paste data"))
        {
            JInternalFrame frame = contentPane.getSelectedFrame();
            if (frame != null && frame instanceof TableView)
            {
                ((TableView)frame).pasteData();
            }
        }
        else if (cmd.equals("Import data from file"))
        {
            JInternalFrame frame = contentPane.getSelectedFrame();
            if (frame != null && frame instanceof TableView)
            {
                JFileChooser fchooser = new JFileChooser(currentDir);
                fchooser.setFileFilter(DefaultFileFilter.getFileFilterText());
                int returnVal = fchooser.showOpenDialog(this);
                if(returnVal != JFileChooser.APPROVE_OPTION) return;
                File choosedFile = fchooser.getSelectedFile();
                if (choosedFile == null) return;
                if (choosedFile.isDirectory()) currentDir = choosedFile.getPath();
                else currentDir = choosedFile.getParent();
                String txtFile = choosedFile.getAbsolutePath();

                ((TableView)frame).importTextData(txtFile);
            }
        }
        else if (cmd.equals("Write selection to dataset"))
        {
            JInternalFrame frame = contentPane.getSelectedFrame();
            if (frame == null ||  !(frame instanceof TableView))
                return;

            TableView theTable = (TableView)frame;
            JTable jtable = theTable.getTable();
            if (jtable.getSelectedColumnCount() <=0 ||
                jtable.getSelectedRowCount() <=0 )
                return;

            Dataset dataset = (Dataset)theTable.getDataObject();
            TreeNode node = treeView.findTreeNode(dataset);
            Group pGroup = (Group)((DefaultMutableTreeNode)node.getParent()).getUserObject();
            TreeNode root = treeView.findRootNode(dataset.getFileFormat());

            NewDatasetDialog dialog = new NewDatasetDialog(
                this,
                pGroup,
                treeView.breadthFirstUserObjects(root),
                theTable);
            dialog.show();

            HObject obj = (HObject)dialog.getObject();
            if (obj != null)
            {
                Group pgroup = dialog.getParentGroup();
                TreeNode pnode = treeView.findTreeNode(pgroup);
                TreeNode newnode = new DefaultMutableTreeNode(obj);
                treeView.insertNode(newnode, pnode);
            }
        }
        else if (cmd.equals("Save dataset"))
        {
            JInternalFrame frame = contentPane.getSelectedFrame();
            if (frame != null && frame instanceof TableView)
            {
                try { ((TableView)frame).updateValueInFile(); }
                catch (Exception ex)
                {
                    toolkit.beep();
                    JOptionPane.showMessageDialog(this, ex.getMessage(), getTitle(), JOptionPane.ERROR_MESSAGE);
                }
            }
        }
        else if (cmd.equals("Select all data"))
        {
            JInternalFrame frame = contentPane.getSelectedFrame();
            if (frame != null)
            {
                try {
                    if ( frame instanceof TableView)
                        ((TableView)frame).selectAll();
                    else if  ( frame instanceof ImageView)
                        ((ImageView)frame).selectAll();
                }
                catch (Exception ex)
                {
                    toolkit.beep();
                    JOptionPane.showMessageDialog(this, ex.getMessage(), getTitle(), JOptionPane.ERROR_MESSAGE);
                }
            }
        }
        else if (cmd.equals("Show statistics"))
        {
            JInternalFrame frame = contentPane.getSelectedFrame();
            if (frame != null && frame instanceof TableView)
            {
                try { ((TableView)frame).showStatistics(); }
                catch (Exception ex)
                {
                    toolkit.beep();
                    JOptionPane.showMessageDialog(this, ex.getMessage(), getTitle(), JOptionPane.ERROR_MESSAGE);
                }
            }
        }
        else if (cmd.equals("Math conversion"))
        {
            JInternalFrame frame = contentPane.getSelectedFrame();
            if (frame != null && frame instanceof TableView)
            {
                try { ((TableView)frame).mathConversion(); }
                catch (Exception ex)
                {
                    toolkit.beep();
                    JOptionPane.showMessageDialog(this, ex.getMessage(), getTitle(), JOptionPane.ERROR_MESSAGE);
                }
            }
        }
        else if (cmd.startsWith("Save image as "))
        {
            JInternalFrame frame = contentPane.getSelectedFrame();
            if (frame != null && frame instanceof ImageView)
            {
                String filetype = null;
                if (cmd.equals("Save image as jpeg"))
                    filetype = FileFormat.FILE_TYPE_JPEG;
                else if (cmd.equals("Save image as tiff"))
                    filetype = FileFormat.FILE_TYPE_TIFF;
                else if (cmd.equals("Save image as png"))
                    filetype = FileFormat.FILE_TYPE_PNG;

                TreePath[] paths = treeView.getSelectionPaths();
                treeView.clearSelection();
                try { ((ImageView)frame).saveImageAs(filetype); }
                catch (Exception ex)
                {
                    toolkit.beep();
                    JOptionPane.showMessageDialog(this, ex.getMessage(), getTitle(), JOptionPane.ERROR_MESSAGE);
                } finally
                {
                    treeView.setSelectionPaths(paths);
                }
            }
        }
        else if (cmd.equals("Write selection to image"))
        {
            JInternalFrame frame = contentPane.getSelectedFrame();
            if (frame == null ||  !(frame instanceof ImageView))
                return;

            ImageView theImage = (ImageView)frame;
            if (theImage.getSelectedArea().width <=0 ||
                theImage.getSelectedArea().height <= 0)
                return;

            Dataset dataset = (Dataset)theImage.getDataObject();
            TreeNode node = treeView.findTreeNode(dataset);
            Group pGroup = (Group)((DefaultMutableTreeNode)node.getParent()).getUserObject();
            TreeNode root = treeView.findRootNode(dataset.getFileFormat());

            NewDatasetDialog dialog = new NewDatasetDialog(
                this,
                pGroup,
                treeView.breadthFirstUserObjects(root),
                theImage);
            dialog.show();

            HObject obj = (HObject)dialog.getObject();
            if (obj != null)
            {
                Group pgroup = dialog.getParentGroup();
                TreeNode pnode = treeView.findTreeNode(pgroup);
                TreeNode newnode = new DefaultMutableTreeNode(obj);
                treeView.insertNode(newnode, pnode);
            }
        }
        else if (cmd.equals("Zoom in"))
        {
            JInternalFrame frame = contentPane.getSelectedFrame();
            if (frame != null && frame instanceof ImageObserver)
            {
                ((ImageObserver)frame).zoomIn();
            }
        }
        else if (cmd.equals("Zoom out"))
        {
            JInternalFrame frame = contentPane.getSelectedFrame();
            if (frame != null && frame instanceof ImageObserver)
            {
                ((ImageObserver)frame).zoomOut();
            }
        }
        else if (cmd.equals("Show palette"))
        {
            JInternalFrame frame = contentPane.getSelectedFrame();
            if (frame != null && frame instanceof ImageObserver)
            {
                ((ImageObserver)frame).showColorTable();
            }
        }
        else if (cmd.equals("Flip horizontal"))
        {
            JInternalFrame frame = contentPane.getSelectedFrame();
            if (frame != null && frame instanceof ImageObserver)
            {
                ((ImageObserver)frame).flip(ImageView.FLIP_HORIZONTAL);
            }
        }
        else if (cmd.equals("Flip vertical"))
        {
            JInternalFrame frame = contentPane.getSelectedFrame();
            if (frame != null && frame instanceof ImageObserver)
            {
                ((ImageObserver)frame).flip(ImageView.FLIP_VERTICAL);
            }
        }
        else if (cmd.equals("Show image value"))
        {
            JInternalFrame frame = contentPane.getSelectedFrame();
            if (frame != null && frame instanceof ImageObserver)
            {
                boolean b = imageValueCheckBox.getState();
                ((ImageObserver)frame).setValueVisible(b);
            }
        }
        else if (cmd.startsWith("Contour"))
        {
            JInternalFrame frame = contentPane.getSelectedFrame();
            if (frame != null && frame instanceof ImageObserver)
            {
                int level = Integer.parseInt(cmd.substring(cmd.length()-1));
                ((ImageObserver)frame).contour(level);
            }
        }
        else if (cmd.equals("Show chart"))
        {
            JInternalFrame frame = contentPane.getSelectedFrame();
            if (frame != null)
            {
                if (frame instanceof ImageObserver)
                {
                    ((ImageObserver)frame).showHistogram();
                }
                else if (frame instanceof TableObserver)
                {
                    ((TableObserver)frame).showLineplot();
                }
            }
        }
        else if (cmd.equals("First page"))
        {
            JInternalFrame frame = contentPane.getSelectedFrame();
            if (frame != null && frame instanceof DataObserver)
            {
                ((DataObserver)frame).firstPage();
            }
        }
        else if (cmd.equals("Previous page"))
        {
            JInternalFrame frame = contentPane.getSelectedFrame();
            if (frame != null && frame instanceof DataObserver)
            {
                ((DataObserver)frame).previousPage();
            }
        }
        else if (cmd.equals("Next page"))
        {
            JInternalFrame frame = contentPane.getSelectedFrame();
            if (frame != null && frame instanceof DataObserver)
            {
                ((DataObserver)frame).nextPage();
            }
        }
        else if (cmd.equals("Last page"))
        {
            JInternalFrame frame = contentPane.getSelectedFrame();
            if (frame != null && frame instanceof DataObserver)
            {
                ((DataObserver)frame).lastPage();
            }
        }
        else if (cmd.equals("Cascade all windows"))
        {
            cascadeWindow();
        }
        else if (cmd.equals("Tile all windows"))
        {
            tileWindow();
        }
        else if (cmd.equals("Close a window"))
        {
            JInternalFrame frame = contentPane.getSelectedFrame();

            if (frame != null)
            {
                frame.dispose();
            }
        }
        else if (cmd.equals("Close all windows"))
        {
            closeAllWindow();
        }
        else if (cmd.startsWith("SHOW WINDOW"))
        {
            // a window is selected to be shown at the front
            showWindow(cmd);
        }
        else if (cmd.startsWith("Convert image file:"))
        {
            String typeFrom=null, typeTo =null;

            if (cmd.equals("Convert image file: JPEG to HDF5"))
            {
                typeFrom = FileFormat.FILE_TYPE_JPEG;
                typeTo = FileFormat.FILE_TYPE_HDF5;
            }
            else if (cmd.equals("Convert image file: TIFF to HDF5"))
            {
                typeFrom = FileFormat.FILE_TYPE_TIFF;
                typeTo = FileFormat.FILE_TYPE_HDF5;
            }
            else if (cmd.equals("Convert image file: PNG to HDF5"))
            {
                typeFrom = FileFormat.FILE_TYPE_PNG;
                typeTo = FileFormat.FILE_TYPE_HDF5;
            }
            else if (cmd.equals("Convert image file: JPEG to HDF4"))
            {
                typeFrom = FileFormat.FILE_TYPE_JPEG;
                typeTo = FileFormat.FILE_TYPE_HDF4;
            }
            else if (cmd.equals("Convert image file: TIFF to HDF4"))
            {
                typeFrom = FileFormat.FILE_TYPE_TIFF;
                typeTo = FileFormat.FILE_TYPE_HDF4;
            }
            else if (cmd.equals("Convert image file: PNG to HDF4"))
            {
                typeFrom = FileFormat.FILE_TYPE_PNG;
                typeTo = FileFormat.FILE_TYPE_HDF4;
            }
            else
                return;

            FileConversionDialog dialog = new FileConversionDialog(
                this,
                typeFrom,
                typeTo,
                currentDir,
                treeView.getOpenFiles());
            dialog.show();

            if (dialog.isFileConverted())
            {
                String filename = dialog.getConvertedFile();
                File theFile = new File(filename);

                if (!theFile.exists() || !theFile.exists())
                    return;

                currentDir = theFile.getParentFile().getAbsolutePath();
                currentFile = theFile.getAbsolutePath();

                try {
                    treeView.openFile(filename, FileFormat.WRITE);
                     try { updateRecentFiles(filename); } catch (Exception ex) {}
                } catch (Exception ex)
                {
                    showStatus(ex.toString());
                }
            }
        }
        else if (cmd.equals("User options"))
        {
            UserOptionsDialog dialog = new UserOptionsDialog(this, rootDir);
            dialog.show();

            if (dialog.isFontChanged())
            {
                int fsize = ViewProperties.getFontSizeInt();
                treeView.setTreeFontSize(fsize);
            }

            if (dialog.isUserGuideChanged())
            {
                //update the UG path
                String ugPath = ViewProperties.getUsersGuide();
                try {
                    usersGuideURL = new URL(ugPath);
                } catch (Exception e2) {
                    showStatus(e2.toString());
                    return;
                }

                visitedUsersGuideURLs.clear();
                try {
                    usersGuideEditorPane.setPage(usersGuideURL);
                    ugField.setText(ugPath);
                } catch (IOException e2) {
                    showStatus(e2.toString());
                }
            }
        }
        else if (cmd.equals("Users guide"))
        {
            if (usersGuideURL != null)
                usersGuideWindow.show();
        }
        else if (cmd.equals("Close users guide"))
        {
            if (usersGuideURL != null)
                usersGuideWindow.hide();
        }
        else if (cmd.equals("Users guide home"))
        {
            HyperlinkEvent linkEvent = new HyperlinkEvent(
                usersGuideEditorPane,
                HyperlinkEvent.EventType.ACTIVATED,
                usersGuideURL);

            hyperlinkUpdate(linkEvent);
        }
        else if (cmd.equals("Users guide back"))
        {
            HyperlinkEvent linkEvent = new HyperlinkEvent(
                usersGuideEditorPane,
                HyperlinkEvent.EventType.ACTIVATED,
                (URL)visitedUsersGuideURLs.pop());

            hyperlinkUpdate(linkEvent);

            // hyperlinkUpdate will push the popped link back into the stack
            visitedUsersGuideURLs.pop();

            if (visitedUsersGuideURLs.empty())
            {
                usersGuideBackButton.setEnabled(false);
            }
        }
        else if (cmd.equals("HDF4 library"))
        {
            FileFormat thefile = FileFormat.getFileFormat(FileFormat.FILE_TYPE_HDF4);
            if (thefile == null)
                return;

            JOptionPane.showMessageDialog(
                this,
                thefile.getLibversion(),
                "HDFView",
                JOptionPane.PLAIN_MESSAGE,
                ViewProperties.getLargeHdfIcon());
        }
        else if (cmd.equals("HDF5 library"))
        {
            FileFormat thefile = FileFormat.getFileFormat(FileFormat.FILE_TYPE_HDF5);
            if (thefile == null)
                return;

            JOptionPane.showMessageDialog(
                this,
                thefile.getLibversion(),
                "HDFView",
                JOptionPane.PLAIN_MESSAGE,
                ViewProperties.getLargeHdfIcon());
        }
        else if (cmd.equals("About"))
        {
            JOptionPane.showMessageDialog(
                this,
                aboutHDFView,
                "HDFView",
                JOptionPane.PLAIN_MESSAGE,
                ViewProperties.getLargeHdfIcon());
        }
    }

    public void hyperlinkUpdate(HyperlinkEvent e)
    {
        if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED)
        {
            JEditorPane pane = (JEditorPane) e.getSource();

            try
            {
                URL currentURL = e.getURL();
                String htmlDoc = currentURL.getFile();
                htmlDoc = htmlDoc.toLowerCase();

                // only support html files
                if (htmlDoc.length()<=1 ||
                    !(htmlDoc.endsWith("html") ||
                    htmlDoc.endsWith("htm")))
                return;

                pane.setPage(currentURL);
                if(visitedUsersGuideURLs.isEmpty())
                    usersGuideBackButton.setEnabled(true);
                visitedUsersGuideURLs.push(previousUsersGuideURL);
                previousUsersGuideURL = currentURL;
                ugField.setText(currentURL.toString());
            } catch (Throwable t)
            {
                try {pane.setPage(previousUsersGuideURL);}
                catch (Throwable t2) {}
                showStatus(t.toString());
            }

        }
    }

    public void dispose()
    {
        try { super.dispose(); }
        catch (Exception ex ) {}

        // save the current user properties into property file
        try { props.save() ; }
        catch (Exception ex) {}

        // close all open files
        treeView.closeFile();

        System.exit(0);

    }

    /** Returns a list of current open file */
    public List getOpenFiles()
    {
        return treeView.getOpenFiles();
    }

    /**
     * Creates and lays out GUI compoents.
     * ||=========||=============================||
     * ||         ||                             ||
     * ||         ||                             ||
     * || TreeView||       ContentPane           ||
     * ||         ||                             ||
     * ||=========||=============================||
     * ||            Message Area                ||
     * ||========================================||
     */
    private void createMainWindow()
    {
        // create splitpane to separate treeview and the contentpane
        JScrollPane treeScroller = new JScrollPane(treeView);
        JScrollPane contentScroller = new JScrollPane(contentPane);
        JSplitPane topSplitPane = new JSplitPane(
            JSplitPane.HORIZONTAL_SPLIT,
            treeScroller,
            contentScroller);
        topSplitPane.setDividerLocation(200);

        // create splitpane to separate message area and treeview-contentpane
        JScrollPane msgScroller = new JScrollPane(statusArea);
        topSplitPane.setBorder(null); // refer to Java bug #4131528
        JSplitPane splitPane = new JSplitPane(
            JSplitPane.VERTICAL_SPLIT,
            topSplitPane,
            msgScroller);

        // set the window size
        //float inset = 0.17f; // for UG only.
        float inset = 0.07f;
        Dimension d = toolkit.getScreenSize();
        d.width = Math.max(400, (int)((1-2*inset)*d.width));
        d.height = Math.max(300, (int)((1-2*inset)*d.height));

        splitPane.setDividerLocation(d.height-100);

        int x0 = Math.max(10, (int)(inset*d.width));
        int y0 = 10;//Math.max(10, (int)(inset*d.height));
        this.setLocation(x0, y0);

        try {
            this.setIconImage(((ImageIcon)ViewProperties.getHdfIcon()).getImage());
        } catch (Exception ex ) {}

        this.setJMenuBar(createMenuBar());

        JPanel mainPane = (JPanel)getContentPane();
        mainPane.setLayout(new BorderLayout());
        mainPane.add(createToolBar(), BorderLayout.NORTH);
        mainPane.add(splitPane, BorderLayout.CENTER);
        mainPane.setPreferredSize(d);

        createUsersGuidePane();
    }

    private JMenuBar createMenuBar()
    {
        JMenuBar mbar = new JMenuBar();
        JMenu menu = null;
        JMenuItem item;

        // add file menu
        fileMenu.setMnemonic('f');
        mbar.add(fileMenu);

        item = new JMenuItem( "Open File");
        item.setMnemonic(KeyEvent.VK_O);
        item.addActionListener(this);
        item.setActionCommand("Open file");
        item.setAccelerator( KeyStroke.getKeyStroke(KeyEvent.VK_O, Event.CTRL_MASK, true));
        fileMenu.add(item);

        fileMenu.addSeparator();

        item = new JMenuItem( "Open Read-Only");
        item.setMnemonic(KeyEvent.VK_R);
        item.addActionListener(this);
        item.setActionCommand("Open file read-only");
        fileMenu.add(item);

        fileMenu.addSeparator();

        JMenu imageSubmenu = new JMenu("Import JPEG To");
        item = new JMenuItem( "HDF4");
        item.setActionCommand("Convert image file: JPEG to HDF4");
        item.addActionListener(this);
        h4GUIs.add(item);
        imageSubmenu.add(item);
        item = new JMenuItem( "HDF5");
        item.setActionCommand("Convert image file: JPEG to HDF5");
        item.addActionListener(this);
        h5GUIs.add(item);
        imageSubmenu.add(item);
        fileMenu.add(imageSubmenu);

/*
        imageSubmenu = new JMenu("Import TIFF To");
        item = new JMenuItem( "HDF4");
        item.setActionCommand("Convert image file: TIFF to HDF4");
        item.addActionListener(this);
        imageSubmenu.add(item);
        item = new JMenuItem( "HDF5");
        item.setActionCommand("Convert image file: TIFF to HDF5");
        item.addActionListener(this);
        imageSubmenu.add(item);
        fileMenu.add(imageSubmenu);

        imageSubmenu = new JMenu("Import PNG To");
        item = new JMenuItem( "HDF4");
        item.setActionCommand("Convert image file: PNG to HDF4");
        item.addActionListener(this);
        imageSubmenu.add(item);
        item = new JMenuItem( "HDF5");
        item.setActionCommand("Convert image file: PNG to HDF5");
        item.addActionListener(this);
        imageSubmenu.add(item);
        fileMenu.add(imageSubmenu);
*/
        fileMenu.addSeparator();

        JMenu newFileMenu = new JMenu("New");
        item = new JMenuItem( "HDF4");
        item.setActionCommand("New HDF4 file");
        item.setMnemonic(KeyEvent.VK_4);
        item.addActionListener(this);
        h4GUIs.add(item);
        newFileMenu.add(item);
        item = new JMenuItem( "HDF5");
        item.setActionCommand("New HDF5 file");
        item.setMnemonic(KeyEvent.VK_5);
        item.addActionListener(this);
        h5GUIs.add(item);
        newFileMenu.add(item);
        fileMenu.add(newFileMenu);

        fileMenu.addSeparator();

        item = new JMenuItem( "Close");
        item.setMnemonic(KeyEvent.VK_C);
        item.addActionListener(this);
        item.setActionCommand("Close file");
        fileMenu.add(item);

        item = new JMenuItem( "Close All");
        item.setMnemonic(KeyEvent.VK_A);
        item.addActionListener(this);
        item.setActionCommand("Close all file");
        fileMenu.add(item);

        fileMenu.addSeparator();

        item = new JMenuItem( "Save As");
        item.setMnemonic(KeyEvent.VK_S);
        item.addActionListener(this);
        item.setActionCommand("Save as HDF");
        fileMenu.add(item);

        fileMenu.addSeparator();

        item = new JMenuItem( "Exit");
        item.setMnemonic(KeyEvent.VK_E);
        item.addActionListener(this);
        item.setActionCommand("Exit");
        item.setAccelerator(
            KeyStroke.getKeyStroke(KeyEvent.VK_Q, Event.CTRL_MASK, true));
        fileMenu.add(item);

        fileMenu.addSeparator();

        // add recent files
        if (recentFiles != null)
        {
            String theFile = null;
            String txtName = null;

            int size = recentFiles.size();
            for (int i=0; i<size; i++)
            {
                theFile = (String)recentFiles.get(i);
                txtName = theFile;
                if (txtName.length() > 35)
                    txtName = txtName.substring(0,10) + "....." + txtName.substring(txtName.length()-20);
                item = new JMenuItem(txtName);
                item.setName(theFile);
                item.addActionListener(this);
                item.setActionCommand("recent.file");
                fileMenu.add(item);
            }
        }

        // add edit menu
        objectMenu.setMnemonic('O');
        mbar.add(objectMenu);

        item = new JMenuItem( "Open");
        item.setMnemonic(KeyEvent.VK_O);
        item.addActionListener(this);
        item.setActionCommand("Open data");
        objectMenu.add(item);

        item = new JMenuItem( "Open As");
        item.setMnemonic(KeyEvent.VK_A);
        item.addActionListener(this);
        item.setActionCommand("Open data as");
        objectMenu.add(item);

        objectMenu.addSeparator();

        item = new JMenuItem( "Save to File");
        item.setMnemonic(KeyEvent.VK_S);
        item.addActionListener(this);
        item.setActionCommand("Save object to file");
        objectMenu.add(item);

        objectMenu.addSeparator();

        JMenu newOjbectMenu = new JMenu("New");
        editGUIs.add(newOjbectMenu);
        objectMenu.add(newOjbectMenu);

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

        objectMenu.addSeparator();

        item = new JMenuItem( "Copy");
        item.setMnemonic(KeyEvent.VK_C);
        item.addActionListener(this);
        editGUIs.add(item);
        item.setActionCommand("Copy object");
        objectMenu.add(item);

        item = new JMenuItem( "Paste");
        item.setMnemonic(KeyEvent.VK_P);
        item.addActionListener(this);
        editGUIs.add(item);
        item.setActionCommand("Paste object");
        objectMenu.add(item);

        item = new JMenuItem( "Delete");
        item.setMnemonic(KeyEvent.VK_D);
        item.addActionListener(this);
        item.setActionCommand("Cut object");
        editGUIs.add(item);
        objectMenu.add(item);

        item = new JMenuItem( "Rename");
        item.setMnemonic(KeyEvent.VK_R);
        item.addActionListener(this);
        editGUIs.add(item);
        item.setActionCommand("Rename object");

        // rename is not supported by this version
        //objectMenu.add(item);

        objectMenu.addSeparator();

        item = new JMenuItem( "Properties");
        item.setMnemonic(KeyEvent.VK_E);
        item.addActionListener(this);
        item.setActionCommand("Show object properties");
        objectMenu.add(item);

        // add table menu
        menu = new JMenu("Table");
        menu.setMnemonic('T');
        mbar.add(menu);

        item = new JMenuItem( "Save As Text");
        item.setMnemonic(KeyEvent.VK_T);
        item.addActionListener(this);
        item.setActionCommand("Save table as text");
        menu.add(item);
        tableGUIs.add(item);

        menu.addSeparator();

        item = new JMenuItem( "Copy Data");
        item.setMnemonic(KeyEvent.VK_C);
        item.addActionListener(this);
        item.setActionCommand("Copy data");
        menu.add(item);
        tableGUIs.add(item);

        item = new JMenuItem( "Paste Data");
        item.setMnemonic(KeyEvent.VK_P);
        item.addActionListener(this);
        item.setActionCommand("Paste data");
        menu.add(item);
        tableGUIs.add(item);
        editGUIs.add(item);

        menu.addSeparator();

        item = new JMenuItem( "Import Data from File");
        item.setMnemonic(KeyEvent.VK_I);
        item.addActionListener(this);
        item.setActionCommand("Import data from file");
        menu.add(item);
        tableGUIs.add(item);
        editGUIs.add(item);

        menu.addSeparator();

        item = new JMenuItem( "Write Selection to New Dataset");
        item.setMnemonic(KeyEvent.VK_W);
        item.addActionListener(this);
        item.setActionCommand("Write selection to dataset");
        menu.add(item);
        tableGUIs.add(item);
        editGUIs.add(item);

        item = new JMenuItem( "Update Change in File");
        item.setMnemonic(KeyEvent.VK_U);
        item.addActionListener(this);
        item.setActionCommand("Save dataset");
        menu.add(item);
        tableGUIs.add(item);
        editGUIs.add(item);

        menu.addSeparator();

        item = new JMenuItem( "Select All");
        item.setMnemonic(KeyEvent.VK_A);
        item.addActionListener(this);
        item.setActionCommand("Select all data");
        menu.add(item);
        tableGUIs.add(item);
        menu.addSeparator();

        item = new JMenuItem( "Show Statistics");
        item.setMnemonic(KeyEvent.VK_S);
        item.addActionListener(this);
        item.setActionCommand("Show statistics");
        menu.add(item);
        tableGUIs.add(item);

        item = new JMenuItem( "Math Conversion");
        item.setMnemonic(KeyEvent.VK_M);
        item.addActionListener(this);
        item.setActionCommand("Math conversion");
        menu.add(item);
        tableGUIs.add(item);
        editGUIs.add(item);

        // add image menu
        menu = new JMenu("Image");
        menu.setMnemonic('I');
        mbar.add(menu);

        item = new JMenuItem( "Save Image As JPEG");
        item.addActionListener(this);
        item.setActionCommand("Save image as jpeg");
        menu.add(item);
        imageGUIs.add(item);
/*
        item = new JMenuItem( "TIFF");
        item.addActionListener(this);
        item.setActionCommand("Save image as tiff");
        imgsubmenu.add(item);

        item = new JMenuItem( "PNG");
        item.addActionListener(this);
        item.setActionCommand("Save image as png");
        imgsubmenu.add(item);
*/
        menu.addSeparator();

        item = new JMenuItem( "Write Selection to New Image");
        item.setMnemonic(KeyEvent.VK_W);
        item.addActionListener(this);
        item.setActionCommand("Write selection to image");
        menu.add(item);
        imageGUIs.add(item);
        editGUIs.add(item);

        menu.addSeparator();

        item = new JMenuItem( "Zoom In");
        item.setMnemonic(KeyEvent.VK_I);
        item.addActionListener(this);
        item.setActionCommand("Zoom in");
        menu.add(item);
        imageGUIs.add(item);

        item = new JMenuItem( "Zoom Out");
        item.setMnemonic(KeyEvent.VK_O);
        item.addActionListener(this);
        item.setActionCommand("Zoom out");
        menu.add(item);
        imageGUIs.add(item);

        menu.addSeparator();

        item = new JMenuItem( "Select All");
        item.setMnemonic(KeyEvent.VK_A);
        item.addActionListener(this);
        item.setActionCommand("Select all data");
        menu.add(item);
        imageGUIs.add(item);

        menu.addSeparator();

        item = new JMenuItem( "Show Palette");
        item.setMnemonic(KeyEvent.VK_P);
        item.addActionListener(this);
        item.setActionCommand("Show palette");
        menu.add(item);
        imageGUIs.add(item);

        item = new JMenuItem( "Show Chart");
        item.setMnemonic(KeyEvent.VK_C);
        item.addActionListener(this);
        item.setActionCommand("Show chart");
        menu.add(item);
        imageGUIs.add(item);

        menu.addSeparator();

        imageValueCheckBox = new JCheckBoxMenuItem( "Show Value", false);
        imageValueCheckBox.addActionListener(this);
        imageValueCheckBox.setActionCommand("Show image value");
        menu.add(imageValueCheckBox);
        imageGUIs.add(imageValueCheckBox);

        menu.addSeparator();

        item = new JMenuItem( "Flip Horizontal");
        item.setMnemonic(KeyEvent.VK_H);
        item.addActionListener(this);
        item.setActionCommand("Flip horizontal");
        menu.add(item);
        imageGUIs.add(item);

        item = new JMenuItem( "Flip Vertical");
        item.setMnemonic(KeyEvent.VK_V);
        item.addActionListener(this);
        item.setActionCommand("Flip vertical");
        menu.add(item);
        imageGUIs.add(item);

        menu.addSeparator();

        JMenu contourMenu = new JMenu("Contour");
        for (int i=3; i<10; i++)
        {
            item = new JMenuItem( String.valueOf(i));
            item.addActionListener(this);
            item.setActionCommand("Contour "+i);
            contourMenu.add(item);
        }
        menu.add(contourMenu);
        imageGUIs.add(contourMenu);

        // add window menu
        windowMenu.setMnemonic('w');
        mbar.add(windowMenu);

        item = new JMenuItem( "Cascade");
        item.setMnemonic(KeyEvent.VK_C);
        item.setActionCommand("Cascade all windows");
        item.addActionListener(this);
        windowMenu.add(item);

        item = new JMenuItem( "Tile");
        item.setMnemonic(KeyEvent.VK_T);
        item.setActionCommand("Tile all windows");
        item.addActionListener(this);
        windowMenu.add(item);

        windowMenu.addSeparator();

        item = new JMenuItem( "Close Window");
        item.setMnemonic(KeyEvent.VK_W);
        item.setActionCommand("Close a window");
        item.addActionListener(this);
        windowMenu.add(item);

        item = new JMenuItem( "Close All");
        item.setMnemonic(KeyEvent.VK_A);
        item.setActionCommand("Close all windows");
        item.addActionListener(this);
        windowMenu.add(item);

        windowMenu.addSeparator();

        // add tool menu
        menu = new JMenu( "Tools" );
        menu.setMnemonic('l');
        mbar.add(menu);

        imageSubmenu = new JMenu("Import JPEG To");
        item = new JMenuItem( "HDF4");
        item.setActionCommand("Convert image file: JPEG to HDF4");
        item.addActionListener(this);
        h4GUIs.add(item);
        imageSubmenu.add(item);
        item = new JMenuItem( "HDF5");
        item.setActionCommand("Convert image file: JPEG to HDF5");
        item.addActionListener(this);
        h5GUIs.add(item);
        imageSubmenu.add(item);
        menu.add(imageSubmenu);
/*
        imageSubmenu = new JMenu("Import TIFF To");
        item = new JMenuItem( "HDF4");
        item.setActionCommand("Convert image file: TIFF to HDF4");
        item.addActionListener(this);
        imageSubmenu.add(item);
        item = new JMenuItem( "HDF5");
        item.setActionCommand("Convert image file: TIFF to HDF5");
        item.addActionListener(this);
        imageSubmenu.add(item);
        menu.add(imageSubmenu);

        imageSubmenu = new JMenu("Import PNG To");
        item = new JMenuItem( "HDF4");
        item.setActionCommand("Convert image file: PNG to HDF4");
        item.addActionListener(this);
        imageSubmenu.add(item);
        item = new JMenuItem( "HDF5");
        item.setActionCommand("Convert image file: PNG to HDF5");
        item.addActionListener(this);
        imageSubmenu.add(item);
        menu.add(imageSubmenu);
*/
        menu.addSeparator();

        item = new JMenuItem( "User Options");
        item.setMnemonic(KeyEvent.VK_O);
        item.setActionCommand("User options");
        item.addActionListener(this);
        menu.add(item);

        // add help menu
        menu = new JMenu("Help");
        menu.setMnemonic('H');
        mbar.add(menu);

        item = new JMenuItem( "User's Guide");
        item.setMnemonic(KeyEvent.VK_U);
        item.setActionCommand("Users guide");
        item.addActionListener(this);
        menu.add(item);

        menu.addSeparator();

        item = new JMenuItem( "HDF4 Library Version");
        item.setMnemonic(KeyEvent.VK_4);
        item.setActionCommand("HDF4 library");
        item.addActionListener(this);
        h4GUIs.add(item);
        menu.add(item);

        item = new JMenuItem( "HDF5 Library Version");
        item.setMnemonic(KeyEvent.VK_5);
        item.setActionCommand("HDF5 library");
        item.addActionListener(this);
        h5GUIs.add(item);
        menu.add(item);

        menu.addSeparator();

        item = new JMenuItem( "About...");
        item.setMnemonic(KeyEvent.VK_A);
        item.setActionCommand("About");
        item.addActionListener(this);
        menu.add(item);

        return mbar;
    }

    private JToolBar createToolBar()
    {
        JToolBar tbar = new JToolBar();
        tbar.setFloatable(false);

        // open file button
        JButton button = new JButton(props.getFileopenIcon() );
        tbar.add( button );
        button.setToolTipText( "Open" );
        button.setMargin( new Insets( 0, 0, 0, 0 ) );
        button.addActionListener( this );
        button.setActionCommand( "Open file" );

        // close file button
        button = new JButton(props.getFilecloseIcon() );
        tbar.add( button );
        button.setToolTipText( "Close" );
        button.setMargin( new Insets( 0, 0, 0, 0 ) );
        button.addActionListener( this );
        button.setActionCommand( "Close file" );

        tbar.addSeparator(new Dimension(20, 20));

        // cahrt button
        button = new JButton( props.getChartIcon() );
        tbar.add( button );
        button.setToolTipText( "Chart" );
        button.setMargin( new Insets( 0, 0, 0, 0 ) );
        button.addActionListener( this );
        button.setActionCommand( "Show chart" );
        chartIcon = button;

        tbar.addSeparator(new Dimension(20, 20));

        // zoom in button
        button = new JButton( props.getZoominIcon() );
        tbar.add( button );
        button.setToolTipText( "Zoom In" );
        button.setMargin( new Insets( 0, 0, 0, 0 ) );
        button.addActionListener( this );
        button.setActionCommand( "Zoom in" );
        imageGUIs.add(button);

        // zoom out button
        button = new JButton( props.getZoomoutIcon() );
        tbar.add( button );
        button.setToolTipText( "Zoom Out" );
        button.setMargin( new Insets( 0, 0, 0, 0 ) );
        button.addActionListener( this );
        button.setActionCommand( "Zoom out" );
        imageGUIs.add(button);

        // palette button
        button = new JButton( props.getPaletteIcon() );
        tbar.add( button );
        button.setToolTipText( "Palette" );
        button.setMargin( new Insets( 0, 0, 0, 0 ) );
        button.addActionListener( this );
        button.setActionCommand( "Show palette" );
        imageGUIs.add(button);
        paletteIcon = button;

        tbar.addSeparator(new Dimension(20, 20));

        // first button
        button = new JButton( props.getFirstIcon() );
        tbar.add( button );
        button.setToolTipText( "First" );
        button.setMargin( new Insets( 0, 0, 0, 0 ) );
        button.addActionListener( this );
        button.setActionCommand( "First page" );
        d3GUIs.add(button);

        // previous button
        button = new JButton( props.getPreviousIcon() );
        tbar.add( button );
        button.setToolTipText( "Previous" );
        button.setMargin( new Insets( 0, 0, 0, 0 ) );
        button.addActionListener( this );
        button.setActionCommand( "Previous page" );
        d3GUIs.add(button);

        // next button
        button = new JButton( props.getNextIcon() );
        tbar.add( button );
        button.setToolTipText( "Next" );
        button.setMargin( new Insets( 0, 0, 0, 0 ) );
        button.addActionListener( this );
        button.setActionCommand( "Next page" );
        d3GUIs.add(button);

        // last button
        button = new JButton( props.getLastIcon() );
        tbar.add( button );
        button.setToolTipText( "Last" );
        button.setMargin( new Insets( 0, 0, 0, 0 ) );
        button.addActionListener( this );
        button.setActionCommand( "Last page" );
        d3GUIs.add(button);

        tbar.addSeparator(new Dimension(20, 20));

        // help button
        button = new JButton( props.getHelpIcon() );
        tbar.add( button );
        button.setToolTipText( "Help" );
        button.setMargin( new Insets( 0, 0, 0, 0 ) );
        button.addActionListener( this );
        button.setActionCommand( "Users guide" );

        tbar.addSeparator(new Dimension(20, 20));

        // HDF4 Library Version button
        button = new JButton( props.getH4Icon() );
        tbar.add( button );
        button.setToolTipText( "HDF4 Library Version" );
        button.setMargin( new Insets( 0, 0, 0, 0 ) );
        button.addActionListener( this );
        button.setActionCommand( "HDF4 library" );
        if (FileFormat.getFileFormat(FileFormat.FILE_TYPE_HDF4) == null)
            button.setEnabled(false);

        // HDF5 Library Version button
        button = new JButton( props.getH5Icon() );
        tbar.add( button );
        button.setToolTipText( "HDF5 Library Version" );
        button.setMargin( new Insets( 0, 0, 0, 0 ) );
        button.addActionListener( this );
        button.setActionCommand( "HDF5 library" );
        if (FileFormat.getFileFormat(FileFormat.FILE_TYPE_HDF5) == null)
            button.setEnabled(false);

        return tbar;
    }

    /** add/update a file to the most recent file list */
    private void updateRecentFiles(String newFile)
    {
        if (recentFiles == null)
            return;

        // if the exists in the recent file list, remvoe it first
        // and add it to the bottom to keep the recent file list sorted.
        if (recentFiles.contains(newFile))
            recentFiles.remove(newFile);

        recentFiles.insertElementAt(newFile, 0);
        JMenuItem fileItem=null, theItem=null;
        int n = fileMenu.getItemCount();
        for (int i=0; i<n; i++)
        {
            theItem = fileMenu.getItem(i);

            if (theItem != null && newFile.equals(theItem.getName()))
            {
                fileItem = theItem;
                break;
            }
        }

        if (fileItem != null)
        {
            // file is listed in the recent files, rearrange it
            fileMenu.remove(fileItem);
            fileMenu.add(fileItem);
        }
        else
        {
            // add a new item to the menu
            String txt = newFile;
            if (txt.length() > 35)
                txt = txt.substring(0, 10)+"....."+txt.substring(txt.length()-20);
            fileItem = new JMenuItem(txt);
            fileItem.setName(newFile);
            fileItem.addActionListener(this);
            fileItem.setActionCommand("recent.file");
            fileMenu.add(fileItem);
            n++;
        }

        // rearrange the menu items to put the most recent on the top
        int size = recentFiles.size()-1;
        for (int i=0; i<size-1; i++)
        {
            fileMenu.add(fileMenu.getItem(n-i-2), n-i-1);
        }
        fileMenu.add(fileItem, n-size-1);

    }

    /**
     *  Adds a window from window list by its name.
     *  <p>
     *  @param frame the window to be added.
     */
    private void addDataWindow(JInternalFrame frame)
    {
        contentPane.add(frame);

        String fullPath = selectedObject.getPath()+selectedObject.getName();
        String cmd = "SHOW WINDOW"+selectedObject.getFID() + fullPath; // make the window to be uniquie: fid+path


        frame.setName(cmd); // data windows are identified by full path the file id
        frame.setMaximizable(true);
        frame.setClosable(true);
        frame.setResizable(true);

        JMenuItem item = new JMenuItem( fullPath );
        item.setActionCommand(cmd);
        item.addActionListener(this);

        if (windowMenu.getMenuComponentCount() == 6)
        {
            Component[] menuItems = windowMenu.getMenuComponents();
            for (int i=0; i<6; i++)
            {
                menuItems[i].setEnabled(true);
            }
        }

        windowMenu.add(item);

        frame.setLocation(frameOffset, frameOffset);
        if (frameOffset < 60) frameOffset += 15;
        else frameOffset = 0;

        Dimension d = contentPane.getSize();
        frame.setSize(d.width-60, d.height-60);

        frame.show();
   }

    private void addGroup()
    {
        if (selectedObject == null)
            return;

       Group pGroup = null;
        if (selectedObject instanceof Group)
            pGroup = (Group)selectedObject;
        else
        {
            TreeNode node = treeView.getSelectedNode();
            pGroup = (Group)((DefaultMutableTreeNode)node.getParent()).getUserObject();
        }

        NewGroupDialog dialog = new NewGroupDialog(
            this,
            pGroup,
            treeView.breadthFirstUserObjects(treeView.getSelectedRootNode()));
        dialog.show();

        HObject obj = (HObject)dialog.getObject();
        if (obj != null)
        {
            Group pgroup = dialog.getParentGroup();
            TreeNode pnode = treeView.findTreeNode(pgroup);
            TreeNode newnode = new DefaultMutableTreeNode(obj)
            {
                public boolean isLeaf() { return false; }
            };

            treeView.insertNode(newnode, pnode);
        }
    }

    private void addDataset()
    {
        if (selectedObject == null)
            return;

        Group pGroup = null;
        if (selectedObject instanceof Group)
            pGroup = (Group)selectedObject;
        else
        {
            TreeNode node = treeView.getSelectedNode();
            pGroup = (Group)((DefaultMutableTreeNode)node.getParent()).getUserObject();
        }

        NewDatasetDialog dialog = new NewDatasetDialog(
            this,
            pGroup,
            treeView.breadthFirstUserObjects(treeView.getSelectedRootNode()));
        dialog.show();

        HObject obj = (HObject)dialog.getObject();
        if (obj != null)
        {
            Group pgroup = dialog.getParentGroup();
            TreeNode pnode = treeView.findTreeNode(pgroup);
            TreeNode newnode = new DefaultMutableTreeNode(obj);
            treeView.insertNode(newnode, pnode);
        }
    }

    private void addImage()
    {
        if (selectedObject == null)
            return;

        Group pGroup = null;
        if (selectedObject instanceof Group)
            pGroup = (Group)selectedObject;
        else
        {
            TreeNode node = treeView.getSelectedNode();
            pGroup = (Group)((DefaultMutableTreeNode)node.getParent()).getUserObject();
        }

        NewImageDialog dialog = new NewImageDialog(
            this,
            pGroup,
            treeView.breadthFirstUserObjects(treeView.getSelectedRootNode()));
        dialog.show();

        HObject obj = (HObject)dialog.getObject();
        if (obj != null)
        {
            Group pgroup = dialog.getParentGroup();
            TreeNode pnode = treeView.findTreeNode(pgroup);
            TreeNode newnode = new DefaultMutableTreeNode(obj);
            treeView.insertNode(newnode, pnode);
        }
    }

    /** Save the current file into HDF4.
     *  Since HDF4 does not support packing. The source file is
     *  copied into the new file with the exact same content.
     */
    private void saveAsHDF4()
    {
        FileFormat srcFile = treeView.getSelectedFile();
        if(srcFile == null)
        {
            toolkit.beep();
            JOptionPane.showMessageDialog(
            this,
            "Select a file to save.",
            getTitle(),
            JOptionPane.ERROR_MESSAGE);
            return;
        }

        NewFileDialog dialog = new NewFileDialog(this, currentDir, FileFormat.FILE_TYPE_HDF4, treeView.getOpenFiles());
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
            getTitle(),
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
            ex.getMessage()+"\n"+filename,
            getTitle(),
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
            treeView.openFile(filename, fileAccessID);
             try { updateRecentFiles(filename); } catch (Exception ex) {}
        } catch (Exception ex)
        {
            toolkit.beep();
            JOptionPane.showMessageDialog(
            this,
            ex.getMessage()+"\n"+filename,
            getTitle(),
            JOptionPane.ERROR_MESSAGE);
            return;
        }
    }

    /**
     * Copy the current file into a new file. The new file does not
     * include the inaccessible objects. Values of reference dataset
     * are not updated in the new file.
     */
    private void saveAsHDF5()
    {
        TreeNode root = treeView.getSelectedRootNode();
        if(root == null)
        {
            toolkit.beep();
            JOptionPane.showMessageDialog(
            this,
            "Select a file to save.",
            getTitle(),
            JOptionPane.ERROR_MESSAGE);
            return;
        }

        NewFileDialog dialog = new NewFileDialog(this, currentDir,
            FileFormat.FILE_TYPE_HDF5, treeView.getOpenFiles());
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
            newFile = treeView.openFile(filename, fileAccessID);
             try { updateRecentFiles(filename); } catch (Exception ex) {}
        } catch (Exception ex)
        {
            toolkit.beep();
            JOptionPane.showMessageDialog(
            this,
            ex.getMessage()+"\n"+filename,
            getTitle(),
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
        } catch (Exception ex) {showStatus(ex.toString());}

        // update reference datasets
        parameter[0] = srcGroup.getFileFormat();
        parameter[1] = newFile;
        parameterClass[0] = parameterClass[1] = parameter[0].getClass();
        try {
            method = newFile.getClass().getMethod("updateReferenceDataset", parameterClass);
            method.invoke(newFile, parameter);
        } catch (Exception ex) {showStatus(ex.toString());}
    }

    /** copy selected objects */
    private void copyObject()
    {
        objectsToCopy = treeView.getSelectedObjects();
    }

    /** paste selected objects */
    private void pasteObject()
    {
        TreeNode pnode = treeView.getSelectedNode();

        if (objectsToCopy == null ||
            objectsToCopy.size() <=0 ||
            pnode == null)
            return;

        FileFormat srcFile = ((HObject)objectsToCopy.get(0)).getFileFormat();
        FileFormat dstFile = treeView.getSelectedFile();
        FileFormat h5file = FileFormat.getFileFormat(FileFormat.FILE_TYPE_HDF5);
        FileFormat h4file = FileFormat.getFileFormat(FileFormat.FILE_TYPE_HDF4);

        if (srcFile == null)
        {
            toolkit.beep();
            JOptionPane.showMessageDialog(
                this,
                "Source file is null.",
                getTitle(),
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        else if (dstFile == null)
        {
            toolkit.beep();
            JOptionPane.showMessageDialog(
                this,
                "Destination file is null.",
                getTitle(),
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        else if (srcFile.isThisType(h4file) && dstFile.isThisType(h5file))
        {
            toolkit.beep();
            JOptionPane.showMessageDialog(
                this,
                "Unsupported operation: cannot copy HDF4 object to HDF5 file",
                getTitle(),
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        else if (srcFile.isThisType(h5file) && dstFile.isThisType(h4file))
        {
            toolkit.beep();
            JOptionPane.showMessageDialog(
                this,
                "Unsupported operation: cannot copy HDF5 object to HDF4 file",
                getTitle(),
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
                getTitle(),
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
                    getTitle(),
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
                ex.getMessage(),
                getTitle(),
                JOptionPane.ERROR_MESSAGE);
                //newNode = null;
            }

            // add the node to the tree
            if (newNode != null)
                treeView.insertNode(newNode, pnode);

        } // while (iterator.hasNext())
    }

    private void removeSelectedObjects()
    {
        FileFormat theFile = treeView.getSelectedFile();
        if (theFile.isThisType(FileFormat.getFileFormat(FileFormat.FILE_TYPE_HDF4)))
        {
            toolkit.beep();
            JOptionPane.showMessageDialog(
                this,
                "Unsupported operation: cannot delete HDF4 object.",
                getTitle(),
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        List objs = treeView.getSelectedObjects();
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

        Component[] clist = contentPane.getComponents();
        JInternalFrame jif = null;
        while (it.hasNext())
        {
            theObj = (HObject)it.next();

            // cannot delete root
            if (theObj instanceof Group)
            {
                Group g = (Group)theObj;
                if (g.isRoot())
                {
                    toolkit.beep();
                    JOptionPane.showMessageDialog(
                        this,
                        "Unsupported operation: cannot delete the file root.",
                        getTitle(),
                        JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }

            try
            {
                theFile.delete(theObj);

                // remove data windows of this object
                if (clist != null)
                {
                    frameName = "SHOW WINDOW"+theObj.getFID()+theObj.getPath()+theObj.getName();
                    for (int i=0; i<clist.length; i++)
                    {
                        jif = (JInternalFrame)clist[i];
                        if (jif.getName().startsWith(frameName)) // if it is a group, remove all the data windows belongs to the group
                        {
                            ((DataObserver)jif).dispose();
                        }
                    }
                }
            } catch (Exception ex)
            {
                toolkit.beep();
                JOptionPane.showMessageDialog(
                    this,
                    ex.getMessage(),
                    getTitle(),
                    JOptionPane.ERROR_MESSAGE);
            }

            if (theObj.equals(selectedObject))
                setSelectedObject(null);

        } //  while (it.hasNext())

        treeView.removeSelectedNodes();
    }


    // Implementing ViewManager.
    public void contentFrameWasRemoved(String name)
    {
        // remove the window name from the window menu.
        int n = windowMenu.getItemCount();
        JMenuItem theItem = null;
        for (int i=6; i<n; i++)
        {
            theItem = windowMenu.getItem(i);

            if (theItem == null) continue;

            if (theItem.getActionCommand().equals(name))
            {
                windowMenu.remove(i);
                theItem = null;
                break;
            }
        }

        if (windowMenu.getMenuComponentCount() == 6)
        {
            Component[] menuItems = windowMenu.getMenuComponents();
            for (int i=0; i<6; i++)
            {
                menuItems[i].setEnabled(false);
            }
        }

        // disable image and 3D GUI components if there is no data content window
        if (contentPane.getSelectedFrame() == null)
        {
            setEnabled(imageGUIs, false);
            setEnabled(d3GUIs, false);
            setEnabled(tableGUIs, false);
        }
    }

    /** disable/enable GUI components */
    private static void setEnabled(Vector list, boolean b)
    {
        Component item = null;
        Iterator it = list.iterator();
        while (it.hasNext())
        {
            item = (Component)it.next();
            item.setEnabled(b);
        }
    }

    /** Bring the window to the front.
     *  <p>
     *  @param name the name of the window to show.
     */
    private void showWindow(String name)
    {
        JInternalFrame jif = null;
        Component[] clist = contentPane.getComponents();
        if (clist == null)  return;

        for (int i=0; i<clist.length; i++)
        {
            jif = (JInternalFrame)clist[i];
            if (jif.getName().equals(name))
            {
                contentPane.setSelectedFrame(jif);
                jif.toFront();//.show();
                break;
            }
        }
    }

    /** Cascade all windows. */
    private void cascadeWindow()
    {
        int y=2, x=2;
        JInternalFrame jif = null;
        Component[] clist = contentPane.getComponents();

        if (clist == null || clist.length <=0 )
            return;

        Dimension d = contentPane.getSize();
        int w = Math.max(50, d.width-100);
        int h = Math.max(50, d.height-100);

        for (int i=0; i<clist.length; i++)
        {
            jif = (JInternalFrame)clist[i];
            jif.setBounds(x, y, w, h);
            contentPane.moveToFront(jif);
            x += 20;
            y += 20;
        }
    }

    /** Tile all windows. */
    private void tileWindow()
    {
        int y=0, x=0, idx=0;
        JInternalFrame jif = null;
        Component[] clist = contentPane.getComponents();

        if (clist == null || clist.length <=0 )
            return;

        int n = clist.length;
        int cols = (int)Math.sqrt(n);
        int rows = (int)Math.ceil((double)n/(double)cols);

        Dimension d = contentPane.getSize();
        int w = d.width/cols;
        int h = d.height/rows;

        for (int i=0; i<rows; i++)
        {
            x = 0;
            for (int j=0; j<cols; j++)
            {
                idx = i*cols+j;
                if (idx >= n) return;

                jif = (JInternalFrame)clist[idx];
                jif.setBounds(x, y, w, h);
                x += w;
            }
            y += h;
        }
    }

    /** Closes all windows. */
    private void closeAllWindow()
    {
        JInternalFrame jif = null;
        Component[] clist = contentPane.getComponents();

        if (clist == null || clist.length <=0 )
            return;

        for (int i=0; i<clist.length; i++)
        {
            jif = (JInternalFrame)clist[i];
            jif.dispose();
            jif = null;
        }
    }

    /**
     * Create GUI components to show the users guide.
     */
    private void createUsersGuidePane()
    {
        String ugPath = ViewProperties.getUsersGuide();
        try {
            usersGuideURL = new URL(ugPath);
        } catch (Exception e) {
            usersGuideURL = null;
            showStatus(e.toString());
        }

        if (usersGuideURL == null)
        {
            try {
                ugPath = "file:"
                + rootDir
                + System.getProperty("file.separator")
                + "docs"
                + System.getProperty("file.separator")
                + "UsersGuide"
                + System.getProperty("file.separator")
                + "index.html";
                ViewProperties.setUsersGuide(ugPath);
                usersGuideURL = new URL(ugPath);
                /* ...  use the URL to initialize the editor pane  ... */
            } catch (Exception e) {
                usersGuideURL = null;
                showStatus(e.toString());
            }
        }

        if (usersGuideURL == null)
            return;
        else
            showStatus(usersGuideURL.toString());

        previousUsersGuideURL = usersGuideURL;
        visitedUsersGuideURLs = new Stack();

        // set up the usersGuide window
        usersGuideWindow.setLocation(20, 20);
        usersGuideWindow.setSize(600, 650);
        ((JPanel)usersGuideWindow.getContentPane()).setPreferredSize(new Dimension(500, 600));

        try {
            Image helpImage = ((ImageIcon)ViewProperties.getHelpIcon()).getImage();
            usersGuideWindow.setIconImage(helpImage);
        }
        catch (Exception ex ) {}

        JToolBar tbar = new JToolBar();

        // home button
        JButton button = new JButton( props.getFirstIcon() );
        tbar.add( button );
        button.setToolTipText( "Home" );
        button.setMargin( new Insets( 0, 0, 0, 0 ) );
        button.addActionListener( this );
        button.setActionCommand( "Users guide home" );

        // back button
        button = new JButton( props.getPreviousIcon() );
        tbar.add( button );
        button.setToolTipText( "Back" );
        button.setMargin( new Insets( 0, 0, 0, 0 ) );
        button.addActionListener( this );
        button.setActionCommand( "Users guide back" );
        button.setEnabled(false);
        usersGuideBackButton = button;

        button = new JButton( "Close" );
        tbar.addSeparator();
        tbar.addSeparator();
        tbar.add( button );
        button.setMargin( new Insets( 0, 0, 0, 0 ) );
        button.addActionListener( this );
        button.setActionCommand( "Close users guide" );

        usersGuideEditorPane.setEditable(false);
        try {
            usersGuideEditorPane.setPage(usersGuideURL);
        } catch (IOException e) {
            showStatus(e.toString());
        }
        usersGuideEditorPane.addHyperlinkListener(this);

        JScrollPane editorScrollPane = new JScrollPane(usersGuideEditorPane);
        JPanel contentPane = (JPanel)usersGuideWindow.getContentPane();
        contentPane.setLayout(new BorderLayout());
        JPanel northP = new JPanel();
        northP.setLayout(new GridLayout(2,1));
        northP.add(tbar);
        northP.add(ugField = new JTextField());
        ugField.setEditable(false);
        if (usersGuideURL != null) ugField.setText(usersGuideURL.toString());
        contentPane.add (northP, BorderLayout.NORTH);
        contentPane.add (editorScrollPane, BorderLayout.CENTER);
    }

}
