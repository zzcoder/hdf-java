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

/**
 * <p>Title: HDFView</p>
 * <p>Description: HDFView is the main class of this HDF visual tool.
 * It is used to layout the graphical components of the hdfview. The major GUI
 * components of the HDFView include Menubar, Toolbar, TreeView, ContentView,
 * and MessageArea.
 *
 * The HDFView is designed in such a way that it does not have direct access to
 * the HDF library. All the HDF library access is done through HDF objects.
 * Therefore, the HDFView package depends on the object package but not the
 * library package. The source code of the view package (ncsa.hdf.view) should
 * be complied with the library package (ncsa.hdf.hdflib and ncsa.hdf.hdf5lib).
 * </p>
 *
 * <p>Company: NCSA, University of Illinois at Urbana-Champaign</p>
 * @author Peter X. Cao
 * @version 1.0, 06/20/2003
 */

public class HDFView extends JFrame
    implements ViewManager, ActionListener, HyperlinkListener, ChangeListener {
    /** tag for TreeView*/
    public static final int MODULE_TREEVIEW = 100;

    /** tag for imageView*/
    public static final int MODULE_IMAGEVIEW = 101;

    /** tag for tableView*/
    public static final int MODULE_TABLEVIEW = 102;

    /** tag for textView*/
    public static final int MODULE_TEXTVIEW = 103;

    /** tag for MetadataView*/
    public static final int MODULE_METADATAVIEW = 104;

    /** tag for paletteView*/
    public static final int MODULE_PALETTEVIEW = 105;

    /** a list of tree view implementation. */
    private static List treeViews;

    /** a list of image view implementation. */
    private static List imageViews;

    /** a list of tree table implementation. */
    private static List tableViews;

    /** a list of Text view implementation. */
    private static List textViews;

    /** a list of metadata view implementation. */
    private static List metaDataViews;

    /** a list of palette view implementation. */
    private static List paletteViews;

    /** a list of help view implementation. */
    private static List helpViews;

    private static final String aboutHDFView =
        "HDF Viewer, "+ "Version "+ViewProperties.VERSION+"\n"+
        "For "+System.getProperty("os.name")+"\n\n"+
        "Copyright "+'\u00a9'+" 2001-2004 University of Illinois.\n"+
        "All rights reserved.";

    private static final String JAVA_COMPILER = "JDK 1.4.1_01-b01";

    /** the directory where the HDFView is installed */
    private String rootDir;

    /** the current working directory */
    private String currentDir;

    /** the current working file */
    private String currentFile;

    /** the view properties */
    private ViewProperties props;

    /** the list of most recent files */
    //private Vector recentFiles;

    /** GUI component: the TreeView */
    private TreeView treeView;

    /** The offset when a new dataview is added into the main window. */
    private int frameOffset;

    /** GUI component: the panel which is used to display the data content */
    private final JDesktopPane contentPane;

    /** GUI component: the text area for showing status message */
    private final JTextArea statusArea;

    /** GUI component: the text area for quick attribute view */
    private final JTextArea attributeArea;

    // create tab pane to display attributes and status information
    private final JTabbedPane infoTabbedPane;

    /** GUI component: a list of current data windwos */
    private final JMenu windowMenu;

    /** GUI component: file menu on the menubar */
    private final JMenu fileMenu;

    /** GUI component: window to show the Users' Guide */
    private final JFrame usersGuideWindow;

    /** GUI component: editorPane to show the Users' Guide */
    private final JEditorPane usersGuideEditorPane;

    /** the string buffer holding the status message */
    private final StringBuffer message;

    /** the string buffer holding the meadata information */
    private final StringBuffer metadata;

    private final Toolkit toolkit;

    /** The list of GUI components related to editing */
    private final List editGUIs;

    /** The list of GUI components related to HDF5 */
    private final List h5GUIs;

    /** The list of GUI components related to HDF4 */
    private final List h4GUIs;

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

    /** to add and display url */
    private JComboBox urlBar;

    /** dialog to display srb files */
    private JDialog srbFileDialog;

    /**
     * Constructs the HDFView with a given root directory, where the
     * HDFView is installed, and opens the given file in the viewer.
     * <p>
     * @param root the directory where the HDFView is installed.
     * @param filename the file to open.
     */
    public HDFView(String root, String filename)
    {
        super("HDFView");

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // set the module class jar files to the class path

        rootDir = root;
        currentFile = null;
        frameOffset = 0;
        srbFileDialog = null;
        toolkit = Toolkit.getDefaultToolkit();
        ViewProperties.loadExtClass();

        editGUIs = new Vector();
        h4GUIs = new Vector();
        h5GUIs = new Vector();

        // load the view properties
        ViewProperties.loadIcons(rootDir);
        props = new ViewProperties(rootDir);
        try
        {
            props.load();
            java.awt.Font font = null;
            String ftype = props.getFontType();
            int fsize = props.getFontSize();
            try { font = new java.awt.Font(ftype, java.awt.Font.PLAIN, fsize); }
            catch (Exception ex) { font = null; }

            if (font != null)
            {
                UIDefaults uiDefaults = UIManager.getDefaults();
                uiDefaults.put("TextArea.font", font);
                uiDefaults.put("TextPane.font", font);
                uiDefaults.put("Table.font", font);
                uiDefaults.put("Tree.font", font);
                uiDefaults.put("TreeNode.font", font);
            }
        } catch (Exception ex){;}
        //recentFiles = props.getMRF();
        currentDir = ViewProperties.getWorkDir();
        if (currentDir == null) currentDir = System.getProperty("user.dir");

        treeViews = props.getTreeViewList();
        metaDataViews = props.getMetaDataViewList();
        textViews = props.getTextViewList();
        tableViews = props.getTableViewList();
        imageViews = props.getImageViewList();
        paletteViews = props.getPaletteViewList();
        helpViews = props.getHelpViewList();

        // initialize GUI components
        statusArea = new JTextArea();
        statusArea.setEditable(false);
        statusArea.setBackground(new java.awt.Color(240, 240, 240));
        statusArea.setLineWrap(true);
        message = new StringBuffer();
        metadata = new StringBuffer();
        showStatus("HDFView root - "+rootDir);
        showStatus("User property file - "+props.getPropertyFile());

        attributeArea = new JTextArea();
        attributeArea.setEditable(false);
        attributeArea.setBackground(new java.awt.Color(240, 240, 240));
        attributeArea.setLineWrap(true);

        // create tab pane to display attributes and status information
        infoTabbedPane = new JTabbedPane(JTabbedPane.BOTTOM);
        infoTabbedPane.addChangeListener(this);

        // setup the Users guide window
        usersGuideWindow = new JFrame("HDFView User's Guide");
        usersGuideEditorPane = new JEditorPane();

        contentPane = new JDesktopPane();
        windowMenu = new JMenu( "Window" );
        fileMenu = new JMenu( "File" );

        String className = (String)treeViews.get(0);
        // enables use of JHDF5 in JNLP (Web Start) applications, the system class loader with reflection first.
        Class theClass = null;
        try { theClass = Class.forName(className); }
        catch (Exception ex)
        {
            try { theClass = ViewProperties.loadExtClass().loadClass(className); }
            catch (Exception ex2)
            {theClass = null;}
        }

        if (theClass != null) {
            try {
                Class[] paramClass = {Class.forName("ncsa.hdf.view.ViewManager")};
                Constructor constructor = theClass.getConstructor(paramClass);
                Object[] paramObj = {this};
                treeView = (TreeView)constructor.newInstance(paramObj);
            } catch (Exception ex) { treeView = null; }
        }

        createMainWindow();

        File theFile = new File(filename);
        if (theFile.exists()) {
            if (theFile.isFile()) {
                currentDir = theFile.getParentFile().getAbsolutePath();
                currentFile = theFile.getAbsolutePath();

                try {
                    treeView.openFile(filename, FileFormat.WRITE);
                    try {
                        urlBar.removeItem(filename);
                        urlBar.insertItemAt(filename, 0);
                        urlBar.setSelectedIndex(0);
                        } catch (Exception ex2 ) {}
                } catch (Exception ex) {
                    showStatus(ex.toString());
                }
            }
            else {
                currentDir = theFile.getAbsolutePath();;
            }
        }

        if (FileFormat.getFileFormat(FileFormat.FILE_TYPE_HDF4) == null)
            setEnabled(h4GUIs, false);

        if (FileFormat.getFileFormat(FileFormat.FILE_TYPE_HDF5) == null)
            setEnabled(h5GUIs, false);

    }

    /**
     * Creates and lays out GUI compoents.
     * <pre>
     * ||=========||=============================||
     * ||         ||                             ||
     * ||         ||                             ||
     * || TreeView||       ContentPane           ||
     * ||         ||                             ||
     * ||=========||=============================||
     * ||            Message Area                ||
     * ||========================================||
     * </pre>
     */
    private void createMainWindow()
    {
        // create splitpane to separate treeview and the contentpane
        JScrollPane treeScroller = new JScrollPane((Component)treeView);
        JScrollPane contentScroller = new JScrollPane(contentPane);
        JSplitPane topSplitPane = new JSplitPane(
            JSplitPane.HORIZONTAL_SPLIT,
            treeScroller,
            contentScroller);
        topSplitPane.setDividerLocation(200);


        infoTabbedPane.addTab("Log Info", new JScrollPane(statusArea));
        infoTabbedPane.addTab("Metadata ", new JScrollPane(attributeArea));

        // create splitpane to separate message area and treeview-contentpane
        topSplitPane.setBorder(null); // refer to Java bug #4131528
        JSplitPane splitPane = new JSplitPane(
            JSplitPane.VERTICAL_SPLIT,
            topSplitPane,
            infoTabbedPane);

        // set the window size
        //float inset = 0.17f; // for UG only.
        float inset = 0.07f;
        Dimension d = toolkit.getScreenSize();
        d.width = Math.max(400, (int)((1-2*inset)*d.width));
        d.height = Math.max(300, (int)((1-2*inset)*d.height));

        splitPane.setDividerLocation(d.height-180);

        int x0 = Math.max(10, (int)(inset*d.width));
        int y0 = 10;//Math.max(10, (int)(inset*d.height));
        this.setLocation(x0, y0);

        try {
            this.setIconImage(((ImageIcon)ViewProperties.getHdfIcon()).getImage());
        } catch (Exception ex ) {}

        this.setJMenuBar(createMenuBar());
        JToolBar toolBar = createToolBar();

        /** create URL address bar */
        urlBar = new JComboBox(props.getMRF());
        urlBar.setMaximumRowCount(ViewProperties.MAX_RECENT_FILES);
        urlBar.setEditable(true);
        urlBar.addActionListener(this);
        urlBar.setActionCommand("Open file: from file bar");
        urlBar.setSelectedIndex(-1);
        //urlBar.addItem("http://hdf.ncsa.uiuc.edu/hdf-java-html/hdf5_test.h5");

        JPanel urlPane = new JPanel();
        urlPane.setLayout(new BorderLayout());
        JButton b = new JButton("File/URL");
        urlPane.add(b, BorderLayout.WEST);
        b.addActionListener(this);
        b.setActionCommand("Popup URL list");
        urlPane.add(urlBar, BorderLayout.CENTER);
        JPanel toolPane = new JPanel();
        toolPane.setLayout(new GridLayout(2,1,0,0));
        toolPane.add(toolBar);
        toolPane.add(urlPane);

        JPanel mainPane = (JPanel)getContentPane();
        mainPane.setLayout(new BorderLayout());
        mainPane.add(toolPane, BorderLayout.NORTH);
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

        item = new JMenuItem( "Open");
        item.setMnemonic(KeyEvent.VK_O);
        item.addActionListener(this);
        item.setActionCommand("Open file");
        item.setAccelerator( KeyStroke.getKeyStroke(KeyEvent.VK_O, Event.CTRL_MASK, true));
        fileMenu.add(item);

        item = new JMenuItem( "Open Read-Only");
        item.setMnemonic(KeyEvent.VK_R);
        item.addActionListener(this);
        item.setActionCommand("Open file read-only");
        fileMenu.add(item);

        item = new JMenuItem( "Open from SRB");
        item.setMnemonic(KeyEvent.VK_S);
        item.addActionListener(this);
        item.setActionCommand("Open from srb");
        fileMenu.add(item);
        try { Class.forName("ncsa.hdf.srb.SRBFileDialog"); }
        catch (Exception ex) {item.setEnabled(false);}

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
        item.setActionCommand("Save current file as");
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
        /*
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
                item.setActionCommand("Open file: recent file");
                fileMenu.add(item);
            }
        }
        */

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
        menu.setMnemonic('T');
        mbar.add(menu);

        JMenu imageSubmenu = new JMenu("Convert JPEG To");
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

        menu.addSeparator();

        item = new JMenuItem( "User Options");
        item.setMnemonic(KeyEvent.VK_O);
        item.setActionCommand("User options");
        item.addActionListener(this);
        menu.add(item);

        menu.addSeparator();

        item = new JMenuItem( "Register File Format");
        item.setMnemonic(KeyEvent.VK_R);
        item.setActionCommand("Register file format");
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

        if (helpViews != null && helpViews.size() > 0)
        {
            int n = helpViews.size();
            for (int i=0; i<n; i++)
            {
                HelpView theView = (HelpView)helpViews.get(i);
                item = new JMenuItem( theView.getLabel());
                item.setActionCommand(theView.getActionCommand());
                item.addActionListener(this);
                menu.add(item);
            }
            menu.addSeparator();
        }

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

        item = new JMenuItem( "Java Version");
        item.setMnemonic(KeyEvent.VK_5);
        item.setActionCommand("Java version");
        item.addActionListener(this);
        menu.add(item);

        menu.addSeparator();

        item = new JMenuItem( "Supported File Formats");
        item.setMnemonic(KeyEvent.VK_L);
        item.setActionCommand("File format list");
        item.addActionListener(this);
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

        // help button
        button = new JButton( props.getHelpIcon() );
        tbar.add( button );
        button.setToolTipText( "Help" );
        button.setMargin( new Insets( 0, 0, 0, 0 ) );
        button.addActionListener( this );
        button.setActionCommand( "Users guide" );

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

    /**
     * Create GUI components to show the users guide.
     */
    private void createUsersGuidePane()
    {
        String ugPath = ViewProperties.getUsersGuide();

        try {
            usersGuideURL = new URL(ugPath);
            usersGuideEditorPane.setPage(usersGuideURL);
        } catch (Exception e) {
            usersGuideURL = null;
            showStatus(e.toString());
        }

        if (usersGuideURL == null)
        {
            String fileSeparator = System.getProperty("file.separator");
            try {
                ugPath = "file:"  + rootDir
                + fileSeparator  + "UsersGuide" + fileSeparator + "index.html";
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

    // To do: Implementing java.io.ActionListener
    public void actionPerformed(ActionEvent e)
    {
        String cmd = e.getActionCommand();

        if (cmd.equals("Exit")) {
            dispose();  // terminate the application
        }
        else if (cmd.startsWith("Open file"))
        {
            int fileAccessID = FileFormat.WRITE;
            String filename = null;

            if (cmd.equals("Open file: from file bar"))
            {
                filename = (String) urlBar.getSelectedItem();
                if (filename == null)
                   return;

                // local file
                if (!(filename.startsWith("http://") || filename.startsWith("ftp://")))
                {
                    File tmpFile = new File(filename);
                    if (tmpFile.exists() && tmpFile.isDirectory())
                    {
                        currentDir = filename;
                        filename = openLocalFile();
                    }
                }
            }
            else if (cmd.equals("Open file read-only"))
            {
                fileAccessID = FileFormat.READ;
                filename = openLocalFile();
            }
            else if (cmd.startsWith("Open file://"))
            {
                filename = cmd.substring(12);
            }
            else
                filename = openLocalFile();

            if (filename == null)
                return;

            if (filename.startsWith("http://") || filename.startsWith("ftp://") )
            {
                filename = openRemoteFile(filename);
            }

            if (filename == null ||
                filename.length() < 1 ||
                filename.equals(currentFile))
                return;

            currentFile = filename;
            try {
                urlBar.removeItem(filename);
                urlBar.insertItemAt(filename, 0);
                urlBar.setSelectedIndex(0);
            } catch (Exception ex ) {}

            try {
                treeView.openFile(filename, fileAccessID);
            } catch (Throwable ex)
            {
                try {
                    treeView.openFile(filename, FileFormat.READ);
                } catch (Throwable ex2)
                {
                    String msg = "Failed to open file "+filename+"\n"+ex2;
                    toolkit.beep();
                    JOptionPane.showMessageDialog( this, msg, getTitle(), JOptionPane.ERROR_MESSAGE);
                }
            }
        }
        else if (cmd.equals ("Open from srb"))
        {
            try { openFromSRB(); }
            catch (Exception ex)
            {
                toolkit.beep();
                JOptionPane.showMessageDialog(
                    this,
                    ex,
                    getTitle(),
                    JOptionPane.ERROR_MESSAGE);
            }
        }
        else if (cmd.startsWith("New HDF"))
        {
            String ftype = FileFormat.FILE_TYPE_HDF5;
            if (cmd.equals("New HDF4 file"))
                ftype = FileFormat.FILE_TYPE_HDF4;

            NewFileDialog dialog = new NewFileDialog(this, currentDir, ftype, treeView.getCurrentFiles());
            //dialog.show();

            if (!dialog.isFileCreated()) return;
            String filename = dialog.getFile();
            if (filename == null) return;

            try {
                treeView.openFile(filename, FileFormat.WRITE);
                currentFile = filename;
                try {
                    urlBar.removeItem(filename);
                    urlBar.insertItemAt(filename, 0);
                    urlBar.setSelectedIndex(0);
                } catch (Exception ex2 ) {}
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
        else if (cmd.equals("Close file"))
        {
            FileFormat theFile = treeView.getSelectedFile();
            if (theFile == null)
            {
                toolkit.beep();
                JOptionPane.showMessageDialog( this, "Select a file to close", getTitle(), JOptionPane.ERROR_MESSAGE);
                return;
            }

            // close all the data windows of this file
            JInternalFrame[] frames = contentPane.getAllFrames();
            if (frames != null)
            {
                for (int i=0; i<frames.length; i++)
                {
                    HObject obj = (HObject)(((DataView)frames[i]).getDataObject());
                    if ( obj.getFileFormat().equals(theFile))
                    {
                        frames[i].dispose();
                        frames[i] = null;
                    }
                }
            }

            String fname = (String) urlBar.getSelectedItem();
            if (theFile.getFilePath().equals(fname))
            {
                currentFile = null;
                urlBar.setSelectedIndex(-1);
            }

            try { treeView.closeFile(theFile); }
            catch (Exception ex) {;}
            theFile = null;
            System.gc();
        }
        else if (cmd.equals("Close all file"))
        {
            closeAllWindow();
            List files = treeView.getCurrentFiles();

            while(!files.isEmpty()) {
                try { treeView.closeFile((FileFormat)files.get(0)); }
                catch (Exception ex) {}
            }
            currentFile = null;
        }
        else if (cmd.equals("Save current file as"))
        {
            try {
                treeView.saveFile(treeView.getSelectedFile());
                /*
                List list = treeView.getCurrentFiles();
                if (list != null && list.size()>0) {
                    FileFormat newFile = (FileFormat)list.get(list.size()-1);
                    try {
                        String filename = newFile.getFilePath();
                        urlBar.removeItem(filename);
                        urlBar.insertItemAt(filename, 0);
                        urlBar.setSelectedIndex(0);
                    } catch (Exception ex2 ) {}
                }
                */
            }
            catch (Exception ex) {
                toolkit.beep();
                JOptionPane.showMessageDialog(
                    this, ex, getTitle(), JOptionPane.ERROR_MESSAGE);
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
                treeView.getCurrentFiles());
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
                    try {
                        urlBar.removeItem(filename);
                        urlBar.insertItemAt(filename, 0);
                        urlBar.setSelectedIndex(0);
                    } catch (Exception ex2 ) {}
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

            if (dialog.isWorkDirChanged())
            {
                currentDir = ViewProperties.getWorkDir();
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
        else if (cmd.equals("Register file format")) {
            String msg = "Register a new file format by \nKEY:FILE_FORMAT:FILE_EXTENSION\n"+
                "where, KEY: the unique identifier for the file format"+
                "\n           FILE_FORMAT: the full class name of the file format"+
                "\n           FILE_EXTENSION: the file extension for the file format"+
                "\n\nFor example, the following line registers HDF4 file format:"+
                "\nHDF:ncsa.hdf.object.h4.H4File:hdf\n\n";
            String str = JOptionPane.showInputDialog(this, msg);
            if (str == null || str.length()<1)
                return;

            int idx1 = str.indexOf(':');
            int idx2 = str.lastIndexOf(':');

            if (idx1<0 || idx2<=idx1) {
                JOptionPane.showMessageDialog(
                        this, "Failed to register "+str +"\n\nMust in the form of KEY:FILE_FORMAT:FILE_EXTENSION",
                        "Register File Format",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            String key = str.substring(0, idx1);
            String className = str.substring(idx1+1, idx2);
            String extension = str.substring(idx2+1);

            // check is the file format has been registered or the key is taken.
            String theKey = null;
            String theClassName = null;
            Enumeration enum = FileFormat.getFileFormatKeys();
            while (enum.hasMoreElements()) {
                theKey = (String)enum.nextElement();
                if (theKey.endsWith(key)) {
                    JOptionPane.showMessageDialog(
                        this,
                        "Invalid key: "+key+" is taken.",
                        "Register File Format",
                        JOptionPane.ERROR_MESSAGE);
                    return;
                }

                theClassName = FileFormat.getFileFormat(theKey).getClass().getName();
                if (theClassName.endsWith(className)) {
                    JOptionPane.showMessageDialog(
                        this,
                        "The file format has already been registered: "+className,
                        "Register File Format",
                        JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }

            // enables use of JHDF5 in JNLP (Web Start) applications, the system class loader with reflection first.
            Class theClass = null;
            try { theClass = Class.forName(className); }
            catch (Exception ex)
            {
                try { theClass = ViewProperties.loadExtClass().loadClass(className); }
                catch (Exception ex2) {theClass = null;}
            }
            if (theClass == null)
                return;

            try {
                Object theObject = theClass.newInstance();
                if (theObject instanceof FileFormat)
                    FileFormat.addFileFormat(key, (FileFormat)theObject);
            } catch (Throwable ex) {
                JOptionPane.showMessageDialog(
                        this, "Failed to register "+str +"\n\n"+ex,
                        "Register File Format",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (extension != null && extension.length()>0)
            {
               extension = extension.trim();
               String ext = ViewProperties.getFileExtension();
               ext += ", "+extension;
               ViewProperties.setFileExtension(ext);
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
        else if (cmd.equals("Java version"))
        {
            String info = "Compiled at "+JAVA_COMPILER+
                "\nRunning at "+System.getProperty("java.vm.version");
            JOptionPane.showMessageDialog(
                this,
                info,
                "HDFView",
                JOptionPane.PLAIN_MESSAGE,
                ViewProperties.getLargeHdfIcon());
        }
        else if (cmd.equals("File format list"))
        {
            FileFormat[] fileformats = FileFormat.getFileFormats();
            if (fileformats == null || fileformats.length <=0)
                return;

            String str = "\nSupported File Formats: \n";
            for (int i=0; i<fileformats.length; i++)
                str += "        "+fileformats[i].getClass().getName() + "\n";

            JOptionPane.showMessageDialog(
                this,
                str,
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
        else if (cmd.equals("Popup URL list"))
        {
            urlBar.setPopupVisible(true);
        } else
        {
            if (helpViews == null || helpViews.size() <= 0)
                return;

            // try if one of the user help information;
            int n = helpViews.size();
            for (int i=0; i<n; i++)
            {
                HelpView theView = (HelpView)helpViews.get(i);
                if (cmd.equals(theView.getActionCommand()))
                {
                    theView.show();
                    break;
                }
            } // for (int i=0; i<n; i++)
        }
    }

    public void hyperlinkUpdate(HyperlinkEvent e) {
        if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
            JEditorPane pane = (JEditorPane) e.getSource();

            try {
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
            } catch (Throwable t) {
                try {pane.setPage(previousUsersGuideURL);}
                catch (Throwable t2) {}
                showStatus(t.toString());
            }
        }
    }

    public void stateChanged(ChangeEvent e)
    {
        Object src = e.getSource();

        if (src.equals(infoTabbedPane))
        {
            int idx = infoTabbedPane.getSelectedIndex();
            if (idx == 1)
            {
                // meta info pane is selected
                attributeArea.setText("");
                showMetaData(treeView.getCurrentObject());
            }
        }
    }

    public void dispose()
    {
        try { props.save() ; }
        catch (Exception ex) {}

        try { closeAllWindow(); }
        catch (Exception ex) {}

        // close all open files
        try {
        List filelist = treeView.getCurrentFiles();
        if (filelist != null && filelist.size()>0) {
            Object[] files = filelist.toArray();
            int n = files.length;
            for (int i=0; i<n; i++) {
                try { treeView.closeFile((FileFormat)files[i]); }
                catch (Throwable ex) {continue;}
            }
        }
        } catch (Exception ex) {}

        try { super.dispose(); }
        catch (Exception ex ) {}

        System.exit(0);
    }

    /** data content is displayed, and add the dataview to the main windows */
    public void addDataView(DataView dataView) {
        if (dataView == null)
            return;

        if (!(dataView instanceof JInternalFrame))
        {
            toolkit.beep();
            JOptionPane.showMessageDialog(
                this,
                "Unsupported DataView: the dataview is not a JInternalFrame.",
                getTitle(),
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        // check if the data content is already displayed
        JInternalFrame[] frames = contentPane.getAllFrames();
        JInternalFrame theFrame = null;
        if (frames != null)
        {
            // test if the data is already displayed
            for (int i=0; i<frames.length; i++) {
                if ( dataView.equals(frames[i]) ) {
                    theFrame = frames[i];
                    break;
                }
            }
        }

        if (theFrame != null) {
            // Data is already displayed. Just bring the dataview to the front
            theFrame.toFront();
            try {
                theFrame.setSelected(true);
            } catch (java.beans.PropertyVetoException e) {}

            return;
        }

        JInternalFrame frame = (JInternalFrame)dataView;
        contentPane.add(frame);
        HObject dataObject = dataView.getDataObject();

        String fullPath = dataObject.getPath()+dataObject.getName();
        String cmd = "SHOW WINDOW"+dataObject.getFID() + fullPath; // make the window to be uniquie: fid+path

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

    /** data content is closed, and remove the dataview from the main window */
    public void removeDataView(DataView dataView) {
        if (!(dataView instanceof JInternalFrame))
        {
            toolkit.beep();
            JOptionPane.showMessageDialog(
                this,
                "The dataview is not a JInternalFrame.",
                getTitle(),
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        JInternalFrame frame = (JInternalFrame)dataView;
        String name = frame.getName();

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
    }

    public TreeView getTreeView() { return treeView; }

    /** Tree mouse event fired */
    public void mouseEventFired(java.awt.event.MouseEvent e)
    {
        Object src = e.getSource();
        if ((src instanceof JTree) && infoTabbedPane.getSelectedIndex()==1)
        {
            HObject obj = treeView.getCurrentObject();
            if (obj == null)
                return;

            urlBar.setSelectedItem(obj.getFile());
            showMetaData(obj);
        }
    }

    private void showMetaData(HObject obj)
    {
        if (obj == null)
            return;

        metadata.setLength(0);
        metadata.append(obj.getName());

        if (obj instanceof Group)
        {
            Group g = (Group)obj;
            metadata.append("\n    Group size = ");
            metadata.append(g.getMemberList().size());
        }
        else if (obj instanceof Dataset)
        {
            Dataset d = (Dataset)obj;
            if (d.getRank() <= 0)
                d.init();

            metadata.append("\n    ");
            if (d instanceof ScalarDS)
                metadata.append(((ScalarDS)d).getDatatype().getDatatypeDescription());
            else if (d instanceof CompoundDS)
                metadata.append("Compound/Vdata");
            metadata.append(",    ");

            long dims[] = d.getDims();

           if (dims != null)
            {
                 metadata.append(dims[0]);
                for (int i=1; i<dims.length; i++)
                {
                    metadata.append(" x ");
                    metadata.append(dims[i]);
                }
            }
        } // else if (obj instanceof Dataset)

        List attrList = null;
        try { attrList = obj.getMetadata(); } catch (Exception ex) {}

        if (attrList == null)
            metadata.append("\n    Number of attributes = 0");
        else
        {
            int n = attrList.size();
            metadata.append("\n    Number of attributes = ");
            metadata.append(n);

            for (int i=0; i<n; i++)
            {
                Object attrObj = attrList.get(i);
                if (!(attrObj instanceof Attribute))
                    continue;
                Attribute attr = (Attribute)attrObj;
                metadata.append("\n        ");
                metadata.append(attr.getName());
                metadata.append(" = ");
                metadata.append(attr.toString(","));
            }
        }

        attributeArea.setText(metadata.toString());
        attributeArea.setCaretPosition(0);
    }

    /** Returns DataView contains the specified data object.
     * It is useful to avoid redundant display of data object that is opened already.
     * @param dataObject the whose presence in the main view is to be tested.
     * @return DataView contains the specified data object, null if the data object
     * is not displayed.
     */
    public DataView getDataView(HObject dataObject) {
        if (dataObject == null)
            return null;

        // check if the data content is already displayed
        JInternalFrame[] frames = contentPane.getAllFrames();
        JInternalFrame theFrame = null;

        if (frames == null)
            return null;

        HObject obj = null;
        for (int i=0; i<frames.length; i++) {
            if ( !(frames[i] instanceof DataView) )
                continue;

            obj = (HObject)(((DataView)frames[i]).getDataObject());
            if (dataObject.equals(obj)) {
                theFrame = frames[i];
                break; // data is already displayed
            }
        }

        return (DataView)theFrame;
    }

    /** Returns a list of all open DataViews
     */
    public List getDataViews()
    {
        // check if the data content is already displayed
        JInternalFrame[] frames = contentPane.getAllFrames();
        JInternalFrame theFrame = null;

        if (frames == null || frames.length<=0)
            return null;

        Vector views = new Vector(frames.length);
        HObject obj = null;
        for (int i=0; i<frames.length; i++)
        {
            if ( !(frames[i] instanceof DataView) )
                continue;
            else
                views.add(frames[i]);
        }

        return views;
    }

    /**
     * @return a list of treeview implementations.
     */
    public static final List getListOfTreeView() {
        return treeViews;
    }

    /**
     * @return a list of imageview implementations.
     */
    public static final List getListOfImageView() {
        return imageViews;
    }

    /**
     * @return a list of tableview implementations.
     */
    public static final List getListOfTableView() {
        return tableViews;
    }

    /**
     * @return a list of textview implementations.
     */
    public static final List getListOfTextView() {
        return textViews;
    }

    /**
     * @return a list of metaDataview implementations.
     */
    public static final List getListOfMetaDataView() {
        return metaDataViews;
    }

    /**
     * @return a list of paletteview implementations.
     */
    public static final List getListOfPaletteView() {
        return paletteViews;
    }

    /**
     * Display feedback message.
     * @param msg the message to display.
     */
    public void showStatus(String msg) {
        message.append(msg);
        message.append("\n");
        statusArea.setText(message.toString());
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
/*
try {
Group g = (Group)FileFormat.getHObject("e:\\hdf-files\\test.h5#//");
System.out.println(g);
if (g != null) {
    List m = g.getMemberList();
    Iterator it = m.iterator();
    while (it.hasNext())
        System.out.println(it.next());
}
} catch (Exception ex) {System.out.println(ex);}
*/

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
            } else if ("-java.vm.version".equalsIgnoreCase(args[i]))
            {
                String info = "Compiled at "+JAVA_COMPILER+
                    "\nRunning at "+System.getProperty("java.vm.version");
                JOptionPane.showMessageDialog(
                new JFrame(),
                info,
                "HDFView",
                JOptionPane.PLAIN_MESSAGE,
                ViewProperties.getLargeHdfIcon());
                System.exit(0);
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

        HDFView frame = new HDFView(rootDir, filename);
        frame.pack();
        frame.setVisible(true);
     }

     /** choose local file */
     private String openLocalFile()
     {
         JFileChooser fchooser = new JFileChooser(currentDir);
         fchooser.setFileFilter(DefaultFileFilter.getFileFilter());

         int returnVal = fchooser.showOpenDialog(this);
         if(returnVal != JFileChooser.APPROVE_OPTION)
             return null;

        File choosedFile = fchooser.getSelectedFile();
        if (choosedFile == null)
            return null;

        if (choosedFile.isDirectory())
            currentDir = choosedFile.getPath();
        else
            currentDir = choosedFile.getParent();

        return choosedFile.getAbsolutePath();
    }


     /** load remote file and save it to local temporary directory*/
     private String openRemoteFile(String urlStr)
     {
         String localFile = null;

         if (urlStr == null)
             return null;

         if (urlStr.startsWith("http://"))
             localFile = urlStr.substring(7);
         else if (urlStr.startsWith("ftp://"))
             localFile = urlStr.substring(6);
         else
             return null;

         localFile = localFile.replace('/', '@');
         localFile = localFile.replace('\\', '@');

         // search the local file cache
        String tmpDir = System.getProperty("java.io.tmpdir");
        localFile =   tmpDir + localFile;

        File tmpFile = new File( localFile);
        if (tmpFile.exists())
            return localFile;

        URL url = null;

        try { url = new URL(urlStr); }
        catch (Exception ex)
        {
            url = null;
            toolkit.beep();
            JOptionPane.showMessageDialog(
                this,
                ex,
                getTitle(),
                JOptionPane.ERROR_MESSAGE);
            return null;
        }

        BufferedInputStream in = null;
        BufferedOutputStream out = null;
        try
        {
            in = new BufferedInputStream(url.openStream());
            out = new BufferedOutputStream(new FileOutputStream(tmpFile));
        }
        catch (Exception ex)
        {
            in = null;
            toolkit.beep();
            JOptionPane.showMessageDialog(
                this,
                ex,
                getTitle(),
                JOptionPane.ERROR_MESSAGE);
            try { in.close();} catch (Exception ex2) {}
            try { out.close();} catch (Exception ex2) {}
            return null;
        }

        setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.WAIT_CURSOR));
        byte[] buff = new byte[512]; // set the default buff size to 512
        try {
            int n = 0;
            while ((n = in.read(buff)) > 0)
                out.write(buff, 0, n);
        } catch (Exception ex) {}

        try { in.close();} catch (Exception ex2) {}
        try { out.close();} catch (Exception ex2) {}

        setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.DEFAULT_CURSOR));

         return localFile;
     }

     /** open file from SRB server */
     private void openFromSRB()
     {
        Class theClass = null;
        try { theClass = Class.forName("ncsa.hdf.srb.SRBFileDialog"); }
        catch (Exception ex) {theClass = null;showStatus(ex.toString());}
        if (theClass == null) return;

        try {
            boolean mode = true;
            Class[] paramClass = {Class.forName("java.awt.Frame")};
            Constructor constructor = theClass.getConstructor(paramClass);
            Object[] paramObj = {(java.awt.Frame)this};
            srbFileDialog = (JDialog)constructor.newInstance(paramObj);
        } catch (Exception ex) { srbFileDialog = null;showStatus(ex.toString()); }
        if (srbFileDialog == null) return;

        srbFileDialog.show();
     }

}
