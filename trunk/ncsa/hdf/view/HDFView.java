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
import java.awt.Event;
import java.awt.Insets;
import java.awt.Dimension;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.BorderLayout;

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
implements ViewManager, ActionListener
{
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

    /** GUI component: file menu on the menubar */
    private final JMenu fileMenu;

    /** the string buffer holding the status message */
    private final StringBuffer message;

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
        String lf = UIManager.getSystemLookAndFeelClassName();
        if ( lf != null )
        {
            try	{
                UIManager.setLookAndFeel( lf );
            } catch ( Exception ex )	{
                ex.printStackTrace();
            }
        }

        this.rootDir = root;
        this.currentDir = root;
        this.currentFile = null;
        this.selectedObject = null;
        File theFile = new File(filename);
        if (theFile.exists())
        {
            if (theFile.isFile())
            {
                this.currentDir = theFile.getParentFile().getAbsolutePath();
                this.currentFile = theFile.getAbsolutePath();;
            }
            else
            {
                this.currentDir = theFile.getAbsolutePath();;
            }
        }

        // load the view properties
        ViewProperties.loadIcons(rootDir);
        props = new ViewProperties(rootDir);
        props.load();
        recentFiles = props.getMRF();

        // initialize GUI components
        statusArea = new JTextArea();
        statusArea.setEditable(false);
        statusArea.setLineWrap(true);
        message = new StringBuffer();
        contentPane = new JDesktopPane();
        contentPane.setDragMode(JDesktopPane.OUTLINE_DRAG_MODE);
        treeView = new TreeView(this);
        windowMenu = new JMenu( "Window" );
        fileMenu = new JMenu( "File" );

        createMainWindow();

        showStatus(props.getPropertyFile());
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
        String rootDir = System.getProperty("user.home");
        boolean isWindows = System.getProperty("os.name").startsWith("Windows");

        if (isWindows) {
            rootDir = System.getProperty("user.dir");
        }

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
                    if (tmpFile.isDirectory()) rootDir = tmpFile.getPath();
                    else rootDir = tmpFile.getParent();
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

        JFrame frame = new HDFView(rootDir, filename);
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

        if (selectedObject != null)
        {
            currentFile = selectedObject.getFile();
            this.setTitle("HDFView - "+currentFile);
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
                theFrame.show();
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
        else if (cmd.startsWith("Open file"))
        {
            final JFileChooser fchooser = new JFileChooser(currentDir);
            int returnVal = fchooser.showOpenDialog(this);

            if(returnVal != JFileChooser.APPROVE_OPTION)
                return;

            File choosedFile = fchooser.getSelectedFile();
            if (choosedFile == null)
                return;

            currentDir = choosedFile.getPath();
            String filename = choosedFile.getAbsolutePath();

            try {
                treeView.openFile(filename);
                addToRecentFiles(filename);
            } catch (Exception ex)
            {
                showStatus(ex.toString());
            }
        }
        else if (cmd.equals("recent.file"))
        {
            JMenuItem mi = (JMenuItem)e.getSource();

            String filename = mi.getText();
            try {
                treeView.openFile(filename);
            } catch (Exception ex)
            {
                showStatus(ex.toString());
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
                        frames[i].dispose();
                }
            }

            treeView.closeFile(currentFile);
        }
        else if (cmd.equals("Close all file"))
        {
            closeAllWindow();
            treeView.closeFile();
        }
        else if (cmd.equals("Open data"))
        {
            showDataContent(true);
        }
        else if (cmd.equals("Open data as"))
        {
            showDataContent(false);
        }
        else if (cmd.equals("Show object properties"))
        {
            showDataInfo();
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
        else if (cmd.startsWith(HObject.separator))
        {
            // a window is selected to be shown at the front
            showWindow(cmd);
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
        float inset = 0.07f;
        java.awt.Dimension d = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        d.width = Math.max(500, (int)((1-2*inset)*d.width));
        d.height = Math.max(400, (int)((1-2*inset)*d.height));

        splitPane.setDividerLocation(d.height-100);

        int x0 = Math.max(10, (int)(inset*d.width));
        int y0 = 10;//Math.max(10, (int)(inset*d.height));
        this.setLocation(x0, y0);

        try {
            this.setIconImage(((ImageIcon)ViewProperties.getHdfIcon()).getImage());
        } catch (Exception ex ) {}

        this.setJMenuBar(createMenuBar());

        JPanel mainPane = (JPanel)getContentPane();
        mainPane.setLayout(new java.awt.BorderLayout());
        mainPane.add(createToolBar(), java.awt.BorderLayout.NORTH);
        mainPane.add(splitPane, java.awt.BorderLayout.CENTER);
        mainPane.setPreferredSize(d);
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
        item.setAccelerator(
            KeyStroke.getKeyStroke(KeyEvent.VK_O, Event.CTRL_MASK, true));
        fileMenu.add(item);

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
            int size = recentFiles.size();
            for (int i=0; i<size; i++)
            {
                item = new JMenuItem((String)recentFiles.get(i));
                item.addActionListener(this);
                item.setActionCommand("recent.file");
                fileMenu.add(item);
            }
        }

        // add edit menu
        menu = new JMenu( "Object" );
        menu.setMnemonic('O');
        mbar.add(menu);

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

        item = new JMenuItem( "Close");
        item.setMnemonic(KeyEvent.VK_C);
        item.setActionCommand("Close a window");
        item.addActionListener(this);
        windowMenu.add(item);

        item = new JMenuItem( "Close All");
        item.setMnemonic(KeyEvent.VK_A);
        item.setActionCommand("Close all windows");
        item.addActionListener(this);
        windowMenu.add(item);

        windowMenu.addSeparator();

        return mbar;
    }

    private JToolBar createToolBar()
    {
        JToolBar tbar = new JToolBar();
        JButton button;

        // open file button
        button = new JButton(props.getFileopenIcon() );
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

        tbar.addSeparator();

        // zoom in button
        button = new JButton( props.getZoominIcon() );
        tbar.add( button );
        button.setToolTipText( "Zoom In" );
        button.setMargin( new Insets( 0, 0, 0, 0 ) );
        button.addActionListener( this );
        button.setActionCommand( "Zoom in" );

        // zoom out button
        button = new JButton( props.getZoomoutIcon() );
        tbar.add( button );
        button.setToolTipText( "Zoom Out" );
        button.setMargin( new Insets( 0, 0, 0, 0 ) );
        button.addActionListener( this );
        button.setActionCommand( "Zoom out" );

        // palette button
        button = new JButton( props.getPaletteIcon() );
        tbar.add( button );
        button.setToolTipText( "Palette" );
        button.setMargin( new Insets( 0, 0, 0, 0 ) );
        button.addActionListener( this );
        button.setActionCommand( "Show palette" );

        tbar.addSeparator();

        // first button
        button = new JButton( props.getFirstIcon() );
        tbar.add( button );
        button.setToolTipText( "First" );
        button.setMargin( new Insets( 0, 0, 0, 0 ) );
        button.addActionListener( this );
        button.setActionCommand( "First page" );

        // previous button
        button = new JButton( props.getPreviousIcon() );
        tbar.add( button );
        button.setToolTipText( "Previous" );
        button.setMargin( new Insets( 0, 0, 0, 0 ) );
        button.addActionListener( this );
        button.setActionCommand( "Previous page" );

        // next button
        button = new JButton( props.getNextIcon() );
        tbar.add( button );
        button.setToolTipText( "Next" );
        button.setMargin( new Insets( 0, 0, 0, 0 ) );
        button.addActionListener( this );
        button.setActionCommand( "Next page" );

        // last button
        button = new JButton( props.getLastIcon() );
        tbar.add( button );
        button.setToolTipText( "Last" );
        button.setMargin( new Insets( 0, 0, 0, 0 ) );
        button.addActionListener( this );
        button.setActionCommand( "Last page" );

        tbar.addSeparator();

        // cahrt button
        button = new JButton( props.getChartIcon() );
        tbar.add( button );
        button.setToolTipText( "Chart" );
        button.setMargin( new Insets( 0, 0, 0, 0 ) );
        button.addActionListener( this );
        button.setActionCommand( "Show chart" );

        return tbar;
    }

    /** add a file to the most recent file list */
    private void addToRecentFiles(String newFile)
    {
        if (recentFiles == null ||
            recentFiles.contains(newFile))
            return;

        recentFiles.addElement(newFile);

        // updates the menu
        JMenuItem fileItem = new JMenuItem(newFile);
        fileItem.addActionListener(this);
        fileItem.setActionCommand("recent.file");
        fileMenu.add(fileItem);
    }

    /**
     *  Adds a window from window list by its name.
     *  <p>
     *  @param frame the window to be added.
     */
    private void addDataWindow(JInternalFrame frame)
    {
        contentPane.add(frame);

        frame.setMaximizable(true);
        frame.setClosable(true);
        frame.setResizable(true);
        //try { frame.setMaximum(true); } catch (Exception ex) {}
        frame.setSize(contentPane.getSize());
        frame.show();

        String name = frame.getName();
        JMenuItem item = new JMenuItem( name );
        item.setActionCommand(name);
        item.addActionListener(this);

        windowMenu.add(item);
    }

    // Implementing ViewManager.
    public void contentFrameWasRemoved(String name)
    {
        // remove the window name from the window menu.
        int n = windowMenu.getItemCount();
        JMenuItem theItem = null;
        for (int i=0; i<n; i++)
        {
            theItem = windowMenu.getItem(i);

            if (theItem == null) continue;

            if (theItem.getText().equals(name))
            {
                windowMenu.remove(i);
                theItem = null;
                break;
            }
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
        for (int i=0; i<clist.length; i++)
        {
            jif = (JInternalFrame)clist[i];
            if (jif.getName().equals(name))
            {
                contentPane.setSelectedFrame(jif);
                jif.show();
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
            contentPane.remove(jif);
        }
    }

}
