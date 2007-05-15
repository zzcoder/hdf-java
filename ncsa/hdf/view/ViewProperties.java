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

import java.io.*;
import java.util.*;
import javax.swing.*;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.MalformedURLException;
import java.util.jar.JarFile;
import java.util.jar.JarEntry;
import ncsa.hdf.object.FileFormat;
import ncsa.hdf.object.HObject;

/**
 * The ViewProperties holds all the HDFView static information.
 * <p>
 * @version 1.3.0 01/10/2002
 * @author Peter X. Cao
 */
public class ViewProperties extends Properties
{
	public static final long serialVersionUID = HObject.serialVersionUID;

    /** the version of the HDFViewer */
    public static final String VERSION = "2.4";

    /** the maximum number of most recent files */
    public static final int MAX_RECENT_FILES = 15;

    /** the local property file name */
    private static final String USER_PROPERTY_FILE = ".hdfview"+VERSION;

    /** name of the tab delimiter */
    public static final String DELIMITER_TAB = "Tab";

    /** name of the tab delimiter */
    public static final String DELIMITER_COMMA = "Comma";

    /** name of the tab delimiter */
    public static final String DELIMITER_SPACE = "Space";

    /** name of the tab delimiter */
    public static final String DELIMITER_COLON = "Colon";

    /** name of the tab delimiter */
    public static final String DELIMITER_SEMI_COLON = "Semi-Colon";

    /** user's guide */
    private static String usersGuide = "http://hdf.ncsa.uiuc.edu/hdf-java-html/hdfview/UsersGuide/index.html";

    /** the font size */
    private static int fontSize = 12;

    /** the font type */
    private static String fontType = null;

    /** the full path of H4toH5 converter */
    private static String h4toh5 ="";

    /** data delimiter */
    private static String delimiter = DELIMITER_TAB;

    /** a list of most recent files */
    private static Vector mrf;

    /** the root directory of the HDFView */
    private static String rootDir;

    /** default starting file directory */
    private static String workDir = "user.dir";

    /** default HDF4 file extension */
    private static String fileExt = "hdf, h4, hdf4, h5, hdf5, he4, he5";

    private static ClassLoader extClassLoader=null;

    /** a list of srb accounts */
    private static Vector srbAccountList=new Vector(5);
    
    /** floag to indicate if auto contrast is used in image process. */
    private static boolean isAutoContrast = false;

    /**
     * Current Java application such as HDFView cannot handle files
     * with large number of objects such 1,000,000 objects.
     * max_members defines the maximum number of objects will be loaded
     * into memory.
     */
    private static int max_members = 10000; // 1,000 by default

    /**
     * Current Java application such as HDFView cannot handle files
     * with large number of objects such 1,000,000 objects.
     * start_members defines the starting index of objects will be loaded
     * into memory.
     */
    private static int start_members = 0; // 0 by default

    private static Icon hdfIcon, h4Icon, h5Icon, largeHdfIcon,
        blankIcon, helpIcon,
        fileopenIcon, filesaveIcon, filenewIcon, filecloseIcon,
        foldercloseIcon, folderopenIcon, foldercloseIconA, folderopenIconA,
        datasetIcon, imageIcon, tableIcon, textIcon,
        datasetIconA, imageIconA, tableIconA, textIconA,
        zoominIcon, zoomoutIcon, paletteIcon, chartIcon, brightIcon,
        copyIcon, cutIcon, pasteIcon,
        previousIcon, nextIcon, firstIcon, lastIcon,
        animationIcon, datatypeIcon, datatypeIconA, linkIcon;

    private static String propertyFile;

    /** a list of treeview module */
    private static Vector moduleListTreeView = new Vector(5);

    /** a list of metaview module */
    private static Vector moduleListMetaDataView = new Vector(5);

    /** a list of textview module */
    private static Vector moduleListTextView = new Vector(5);

    /** a list of tableview module */
    private static Vector moduleListTableView = new Vector(5);

    /** a list of imageview module */
    private static Vector moduleListImageView = new Vector(5);

    /** a list of paletteview module */
    private static Vector moduleListPaletteView = new Vector(5);

    /** a list of helpview module */
    private static Vector moduleListHelpView = new Vector(5);

    /**
     * Creates a property list with given root directory of the HDFView.
     */
    public ViewProperties(String viewRoot)
    {
        super();
        rootDir = viewRoot;

        mrf = new Vector(MAX_RECENT_FILES+5);

        // find the property file
        String uh="", ud="", h5v="", fn;

        // look for the property file at the use home directory
        fn = USER_PROPERTY_FILE;
        uh = System.getProperty("user.home") + File.separator + fn;
        ud = System.getProperty("user.dir") + File.separator + fn;
        h5v = viewRoot + File.separator + "lib" + File.separator + fn;

        if ((new File(uh)).exists())
        {
            propertyFile = uh;
        }
        else if ((new File(ud)).exists())
        {
            propertyFile = ud;
        }
/*
        else if ((new File(h5v)).exists())
        {
            propertyFile = h5v;
        }
*/
        else // create new property file at user home directory
        {
            propertyFile = uh;
            File pFile = new File(uh);
            try { pFile.createNewFile(); }
            catch (Exception ex) { propertyFile = null; }
        }
    }

    /* the properties are sorted by keys */
    public synchronized Enumeration keys() {
        Enumeration keysEnum = super.keys();
        Vector keyList = new Vector(50);
        while(keysEnum.hasMoreElements()){
            keyList.add(keysEnum.nextElement());
        }
        Collections.sort(keyList);
        return keyList.elements();
    }

    /** load module classes */
    public static ClassLoader loadExtClass()
    {
        if (extClassLoader != null) {
            return extClassLoader;
        } else {
            extClassLoader = ClassLoader.getSystemClassLoader(); // default classloader
        }

        String rootPath = System.getProperty("hdfview.root");
        String dirname = rootPath+File.separator+"lib"+File.separator+"ext"+File.separator;
        File extdir = new File(dirname);
        String[] jars = extdir.list();

        if ((jars == null) || (jars.length <= 0)) {
            return extClassLoader;
        }

        Vector jarList = new Vector(50);
        Vector classList = new Vector(50);
        for (int i=0; i<jars.length; i++) {
            if (jars[i].endsWith(".jar")) {
                jarList.add(jars[i]);
                // add class names to the list of classes
                File tmpFile = new File(extdir, jars[i]);
                try {
                    JarFile jarFile = new JarFile(tmpFile, false, JarFile.OPEN_READ);
                    Enumeration emu = jarFile.entries();
                    while (emu.hasMoreElements()) {
                        JarEntry jarEntry = (JarEntry)emu.nextElement();
                        String entryName = jarEntry.getName();
                        int idx = entryName.indexOf(".class");
                        if ((idx>0) && (entryName.indexOf('$')<=0))
                        {
                            entryName = entryName.replace('/','.');
                            classList.add(entryName.substring(0, idx));
                        }
                    }
                } catch (Exception ex) {}
            } // if (jars[i].endsWith(".jar")) {
        } // for (int i=0; i<jars.length; i++) {

        int n = jarList.size();
        if (n <= 0) {
            return extClassLoader;
        }

        URL[] urls = new URL[n];
        for (int i=0; i<n; i++) {
            try {
                urls[i] = new URL("file:///"+rootPath + "/lib/ext/"+jarList.get(i));
            } catch (MalformedURLException mfu) {;}
        }

        //try { extClassLoader = new URLClassLoader(urls); }
        try { extClassLoader = URLClassLoader.newInstance(urls); }
        catch (Exception ex) {ex.printStackTrace();}

        // load user modules into their list
        n = classList.size();
        for (int i=0; i<n; i++) {
            String theName = (String)classList.get(i);
            try {
                // enables use of JHDF5 in JNLP (Web Start) applications, the system class loader with reflection first.
                Class theClass = null;
                try { theClass = Class.forName(theName); }
                catch (Exception ex)
                {
                    theClass = extClassLoader.loadClass(theName);
                }

                Class[] interfaces = theClass.getInterfaces();
                if (interfaces != null) {
                    for (int j=0; j<interfaces.length; j++) {
                        String intfaceName = interfaces[j].getName();
                        if ("ncsa.hdf.view.TreeView".equals(intfaceName)) {
                            moduleListTreeView.add(theName);
                            break;
                        } else if ("ncsa.hdf.view.MetaDataView".equals(intfaceName)) {
                            moduleListMetaDataView.add(theName);
                            break;
                        } else if ("ncsa.hdf.view.TextView".equals(intfaceName)) {
                            moduleListTextView.add(theName);
                            break;
                        } else if ("ncsa.hdf.view.TableView".equals(intfaceName)) {
                            moduleListTableView.add(theName);
                            break;
                        } else if ("ncsa.hdf.view.ImageView".equals(intfaceName)) {
                            moduleListImageView.add(theName);
                            break;
                        } else if ("ncsa.hdf.view.PaletteView".equals(intfaceName)) {
                            moduleListPaletteView.add(theName);
                            break;
                        } else if ("ncsa.hdf.view.HelpView".equals(intfaceName)) {
                            try  { moduleListHelpView.add(theClass.newInstance()); }
                            catch (Throwable err) {}
                            break;
                        }
                    } // for (int j=0; j<interfaces.length; j++) {
                } // if (interfaces != null) {
            } catch (Exception ex) {;}
        }
        return extClassLoader;
    }

    /** returns the root directory where the HDFView is installed. */
    public static String getViewRoot() { return rootDir; }

    public static Icon getFoldercloseIcon() { return foldercloseIcon; }

    public static Icon getFoldercloseIconA() { return foldercloseIconA; }

    public static Icon getFolderopenIcon() { return folderopenIcon; }

    public static Icon getFolderopenIconA() { return folderopenIconA; }

    public static Icon getHdfIcon() { return hdfIcon; }

    public static Icon getH4Icon() { return h4Icon; }

    public static Icon getH5Icon() { return h5Icon; }

    public static Icon getDatasetIcon() { return datasetIcon; }

    public static Icon getDatasetIconA() { return datasetIconA; }

    public static Icon getDatatypeIcon() { return datatypeIcon; }

    public static Icon getDatatypeIconA() { return datatypeIconA; }

    public static Icon getLinkIcon() { return linkIcon; }

    public static Icon getFileopenIcon() { return fileopenIcon; }

    public static Icon getFilesaveIcon() { return filesaveIcon; }

    public static Icon getFilenewIcon() { return filenewIcon; }

    public static Icon getFilecloseIcon() { return filecloseIcon; }

    public static Icon getPaletteIcon() { return paletteIcon; }

    public static Icon getBrightIcon() { return brightIcon; }

    public static Icon getImageIcon() { return imageIcon; }

    public static Icon getTableIcon() { return tableIcon; }

    public static Icon getTextIcon() { return textIcon; }

    public static Icon getImageIconA() { return imageIconA; }

    public static Icon getTableIconA() { return tableIconA; }

    public static Icon getTextIconA() { return textIconA; }

    public static Icon getZoominIcon() { return zoominIcon; }

    public static Icon getZoomoutIcon() { return zoomoutIcon; }

    public static Icon getBlankIcon() { return blankIcon; }

    public static Icon getHelpIcon() { return helpIcon; }

    public static Icon getCopyIcon() { return copyIcon; }

    public static Icon getCutIcon() { return cutIcon; }

    public static Icon getPasteIcon() { return pasteIcon; }

    public static Icon getLargeHdfIcon() { return largeHdfIcon; }

    public static Icon getPreviousIcon() { return previousIcon; }

    public static Icon getNextIcon() { return nextIcon; }

    public static Icon getFirstIcon() { return firstIcon; }

    public static Icon getLastIcon() { return lastIcon; }

    public static Icon getChartIcon() { return chartIcon; }

    public static Icon getAnimationIcon() { return animationIcon; }

    public static void loadIcons(String rootPath)
    {
        URL u = null;
        ClassLoader classLoader = ClassLoader.getSystemClassLoader();

        // load icon images
        if (hdfIcon == null) {
            u = classLoader.getResource("ncsa/hdf/view/icons/hdf.gif");
            if (u != null) {
                hdfIcon = new ImageIcon (u);
            }
        }

        if (h4Icon == null) {
            u = classLoader.getResource("ncsa/hdf/view/icons/hdf4.gif");
            if (u != null) {
                h4Icon = new ImageIcon (u);
            }
        }

        if (h5Icon == null) {
            u = classLoader.getResource("ncsa/hdf/view/icons/hdf5.gif");
            if (u != null) {
                h5Icon = new ImageIcon (u);
            }
        }

        if (foldercloseIcon == null) {
            u = classLoader.getResource("ncsa/hdf/view/icons/folderclose.gif");
            if (u != null) {
                foldercloseIcon = new ImageIcon (u);
            }
        }

        if (foldercloseIconA == null) {
            u = classLoader.getResource("ncsa/hdf/view/icons/foldercloseA.gif");
            if (u != null) {
                foldercloseIconA = new ImageIcon (u);
            }
        }

        if (folderopenIcon == null) {
            u = classLoader.getResource("ncsa/hdf/view/icons/folderopen.gif");
            if (u != null) {
                folderopenIcon = new ImageIcon (u);
            }
        }

        if (folderopenIconA == null) {
            u = classLoader.getResource("ncsa/hdf/view/icons/folderopenA.gif");
            if (u != null) {
                folderopenIconA = new ImageIcon (u);
            }
        }

        if (datasetIcon == null) {
            u = classLoader.getResource("ncsa/hdf/view/icons/dataset.gif");
            if (u != null) {
                datasetIcon = new ImageIcon (u);
            }
        }

        if (datasetIconA == null) {
            u = classLoader.getResource("ncsa/hdf/view/icons/datasetA.gif");
            if (u != null) {
                datasetIconA = new ImageIcon (u);
            }
        }

        if (datatypeIcon == null) {
            u = classLoader.getResource("ncsa/hdf/view/icons/datatype.gif");
            if (u != null) {
                datatypeIcon = new ImageIcon (u);
            }
        }

        if (datatypeIconA == null) {
            u = classLoader.getResource("ncsa/hdf/view/icons/datatypeA.gif");
            if (u != null) {
                datatypeIconA = new ImageIcon (u);
            }
        }

        if (linkIcon == null) {
            u = classLoader.getResource("ncsa/hdf/view/icons/link.gif");
            if (u != null) {
                linkIcon = new ImageIcon (u);
            }
        }

        if (fileopenIcon == null) {
            u = classLoader.getResource("ncsa/hdf/view/icons/fileopen.gif");
            if (u != null) {
                fileopenIcon = new ImageIcon (u);
            }
        }

        if (filesaveIcon == null) {
            u = classLoader.getResource("ncsa/hdf/view/icons/filesave.gif");
            if (u != null) {
                filesaveIcon = new ImageIcon (u);
            }
        }

        if (filenewIcon == null) {
            u = classLoader.getResource("ncsa/hdf/view/icons/filenew.gif");
            if (u != null) {
                filenewIcon = new ImageIcon (u);
            }
        }

        if (filecloseIcon == null) {
            u = classLoader.getResource("ncsa/hdf/view/icons/fileclose.gif");
            if (u != null) {
                filecloseIcon = new ImageIcon (u);
            }
        }

        if (paletteIcon == null) {
            u = classLoader.getResource("ncsa/hdf/view/icons/palette.gif");
            if (u != null) {
                paletteIcon = new ImageIcon (u);
            }
        }
        if (brightIcon == null) {
            u = classLoader.getResource("ncsa/hdf/view/icons/brightness.gif");
            if (u != null) {
                brightIcon = new ImageIcon (u);
            }
        }

        if (imageIcon == null) {
            u = classLoader.getResource("ncsa/hdf/view/icons/image.gif");
            if (u != null) {
                imageIcon = new ImageIcon (u);
            }
        }

        if (imageIconA == null) {
            u = classLoader.getResource("ncsa/hdf/view/icons/imageA.gif");
            if (u != null) {
                imageIconA = new ImageIcon (u);
            }
        }

        if (tableIcon == null) {
            u = classLoader.getResource("ncsa/hdf/view/icons/table.gif");
            if (u != null) {
                tableIcon = new ImageIcon (u);
            }
        }

        if (tableIconA == null) {
            u = classLoader.getResource("ncsa/hdf/view/icons/tableA.gif");
            if (u != null) {
                tableIconA = new ImageIcon (u);
            }
        }

        if (textIcon == null) {
            u = classLoader.getResource("ncsa/hdf/view/icons/text.gif");
            if (u != null) {
                textIcon = new ImageIcon (u);
            }
        }

        if (textIconA == null) {
            u = classLoader.getResource("ncsa/hdf/view/icons/textA.gif");
            if (u != null) {
                textIconA = new ImageIcon (u);
            }
        }

        if (zoominIcon == null) {
            u = classLoader.getResource("ncsa/hdf/view/icons/zoomin.gif");
            if (u != null) {
                zoominIcon = new ImageIcon (u);
            }
        }

        if (zoomoutIcon == null) {
            u = classLoader.getResource("ncsa/hdf/view/icons/zoomout.gif");
            if (u != null) {
                zoomoutIcon = new ImageIcon (u);
            }
        }

        if (blankIcon == null) {
            u = classLoader.getResource("ncsa/hdf/view/icons/blank.gif");
            if (u != null) {
                blankIcon = new ImageIcon (u);
            }
        }

        if (helpIcon == null) {
            u = classLoader.getResource("ncsa/hdf/view/icons/help.gif");
            if (u != null) {
                helpIcon = new ImageIcon (u);
            }
        }

        if (copyIcon == null) {
            u = classLoader.getResource("ncsa/hdf/view/icons/copy.gif");
            if (u != null) {
                copyIcon = new ImageIcon (u);
            }
        }

        if (cutIcon == null) {
            u = classLoader.getResource("ncsa/hdf/view/icons/cut.gif");
            if (u != null) {
                cutIcon = new ImageIcon (u);
            }
        }

        if (pasteIcon == null) {
            u = classLoader.getResource("ncsa/hdf/view/icons/paste.gif");
            if (u != null) {
                pasteIcon = new ImageIcon (u);
            }
        }

        if (largeHdfIcon == null) {
            u = classLoader.getResource("ncsa/hdf/view/icons/hdf_large.gif");
            if (u != null) {
                largeHdfIcon = new ImageIcon (u);
            }
        }

        if (previousIcon == null) {
            u = classLoader.getResource("ncsa/hdf/view/icons/previous.gif");
            if (u != null) {
                previousIcon = new ImageIcon (u);
            }
        }

        if (nextIcon == null) {
            u = classLoader.getResource("ncsa/hdf/view/icons/next.gif");
            if (u != null) {
                nextIcon = new ImageIcon (u);
            }
        }

        if (firstIcon == null) {
            u = classLoader.getResource("ncsa/hdf/view/icons/first.gif");
            if (u != null) {
                firstIcon = new ImageIcon (u);
            }
        }

        if (lastIcon == null) {
            u = classLoader.getResource("ncsa/hdf/view/icons/last.gif");
            if (u != null) {
                lastIcon = new ImageIcon (u);
            }
        }

        if (chartIcon == null) {
            u = classLoader.getResource("ncsa/hdf/view/icons/chart.gif");
            if (u != null) {
                chartIcon = new ImageIcon (u);
            }
        }

        if (animationIcon == null) {
            u = classLoader.getResource("ncsa/hdf/view/icons/animation.gif");
            if (u != null) {
                animationIcon = new ImageIcon (u);
            }
        }
    }

    /** Load user properties from property file */
    public void load() throws Exception
    {
        if (propertyFile == null) {
            return;
        }

        loadExtClass();

        try {
            FileInputStream fis = new FileInputStream(propertyFile);
            load(fis);
            fis.close();
        } catch (Exception e) {;}

        // add fileformat modules
        Enumeration local_enum = this.keys();
        String theKey = null;
        String fExt = null;
        while (local_enum.hasMoreElements()) {
            theKey = (String)local_enum.nextElement();
            if (theKey.startsWith("module.fileformat")) {
                fExt = theKey.substring(18);
                try {
                    // enables use of JHDF5 in JNLP (Web Start) applications, the system class loader with reflection first.
                    String className = (String)get(theKey);
                    Class theClass = null;
                    try { theClass = Class.forName(className); }
                    catch (Exception ex)
                    {
                        try { theClass = extClassLoader.loadClass(className); }
                    catch (Exception ex2) { }
                }

                    Object theObject = theClass.newInstance();
                    if (theObject instanceof FileFormat) {
                        FileFormat.addFileFormat(fExt, (FileFormat)theObject);
                    }
                } catch (Throwable err) {;}
            }
        }

        String str = (String)get("users.guide");
        if (str != null)
        {
            String tmpUG = str.toLowerCase();
            if (tmpUG.startsWith("file:") || tmpUG.startsWith("http:")) {
                usersGuide = str;
            } else
            {
                File tmpFile = new File(str);
                if (tmpFile.exists()) {
                    usersGuide = "file:"+str;
                } else {
                    usersGuide = "http://"+str;
                }
            }
        }
        
        str = (String)get("image.autocontrast");
        if (str != null) {
            isAutoContrast = ("true".equalsIgnoreCase(str));
        }

        str = (String)get("data.delimiter");
        if ((str != null) && (str.length()>0)) {
            delimiter = str;
        }

        str = (String)get("h4toh5.converter");
        if ((str != null) && (str.length()>0)) {
            h4toh5 = str;
        }

        str = (String)get("work.dir");
        if ((str != null) && (str.length()>0)) {
            workDir = str;
        }

        str = (String)get("file.extension");
        if ((str != null) && (str.length()>0))
        {
            fileExt = str;
            FileFormat.addFileExtension(fileExt);
        }

        str = (String)get("font.size");
        if ((str != null) && (str.length()>0))
        {
            try { fontSize = Integer.parseInt(str); }
            catch (Exception ex) {}
        }

        str = (String)get("font.type");
        if ((str != null) && (str.length()>0)) {
            fontType = str.trim();
        }

        str = (String)get("max.members");
        if ((str != null) && (str.length()>0)) {
            try { max_members = Integer.parseInt(str); }
            catch (Exception ex) {}
        }

        // load the most recent file list from the property file
        String theFile = null;
        for (int i=0; i<MAX_RECENT_FILES; i++) {
            theFile = getProperty("recent.file"+i);
            if ((theFile != null) &&
                !mrf.contains(theFile))
            {
                if (theFile.startsWith("http://") ||
                    theFile.startsWith("ftp://") ||
                    (new File(theFile)).exists()) {
                    mrf.addElement(theFile);
                }
            }
            else {
                this.remove("recent.file"+i);
            }
        }

        // load srb account
        str=null;
        String srbaccount[] = new String[7];
        for (int i=0; i<MAX_RECENT_FILES; i++)
        {
            if ( null == (srbaccount[0] = getProperty("srbaccount"+i+".host"))) {
                continue;
            }
            if ( null == (srbaccount[1] = getProperty("srbaccount"+i+".port"))) {
                continue;
            }
            if ( null == (srbaccount[2] = getProperty("srbaccount"+i+".user"))) {
                continue;
            }
            if ( null == (srbaccount[3] = getProperty("srbaccount"+i+".password"))) {
                continue;
            }
            if ( null == (srbaccount[4] = getProperty("srbaccount"+i+".home"))) {
                continue;
            }
            if ( null == (srbaccount[5] = getProperty("srbaccount"+i+".domain"))) {
                continue;
            }
            if ( null == (srbaccount[6] = getProperty("srbaccount"+i+".resource"))) {
                continue;
            }
            srbAccountList.add(srbaccount);
            srbaccount = new String[7];
        }

        String[] keys = {"module.treeview", "module.metadataview", "module.textview",
            "module.tableview", "module.imageview", "module.paletteview"};
        Vector[] moduleList = {moduleListTreeView, moduleListMetaDataView, moduleListTextView,
            moduleListTableView,moduleListImageView, moduleListPaletteView};
        String[] moduleNames = {
            "ncsa.hdf.view.DefaultTreeView", "ncsa.hdf.view.DefaultMetaDataView",
            "ncsa.hdf.view.DefaultTextView", "ncsa.hdf.view.DefaultTableView",
            "ncsa.hdf.view.DefaultImageView", "ncsa.hdf.view.DefaultPaletteView"};

        // add default implementation of modules
        for (int i=0; i<6; i++) {
            if (!moduleList[i].contains(moduleNames[i])) {
                moduleList[i].addElement(moduleNames[i]);
            }
        }

        // set default selection of data views
        for (int i=0; i<6; i++) {
            Vector theList = moduleList[i];
            str = (String)get(keys[i]);
            if (!theList.contains(str)) {
                str = moduleNames[i];
            }
            theList.remove(str);
            theList.add(0, str);
        }
    }

    /** Save user properties into property file */
    public void save()
    {
        if (propertyFile == null) {
            return;
        } else {
            clear();
        }

        // update data saving options
        if (delimiter == null) {
            put("data.delimiter", DELIMITER_TAB);
        } else {
            put("data.delimiter", delimiter);
        }

        if (usersGuide != null) {
            put("users.guide", usersGuide);
        }

        if (workDir != null) {
            put("work.dir", workDir);
        }

        if (fileExt != null) {
            put("file.extension", fileExt);
        }

        if (h4toh5 != null) {
            put("h4toh5.converter", h4toh5);
        }

        put("font.size", String.valueOf(fontSize));

        if (fontType != null) {
            put("font.type", fontType);
        }

        put("max.members", String.valueOf(max_members));
        
        put("image.autocontrast", String.valueOf(isAutoContrast));

        // save the list of most recent files
        String theFile;
        int size = mrf.size();
        int minSize = Math.min(size, MAX_RECENT_FILES);
        for (int i=0; i<minSize; i++) {
            theFile = (String)mrf.elementAt(i);
            if ((theFile != null) && (theFile.length()>0)) {
                put("recent.file"+i, theFile);
            }
        }

        // save srb account
        String srbaccount[]=null;
        size = srbAccountList.size();
        minSize = Math.min(size, MAX_RECENT_FILES);
        for (int i=0; i<minSize; i++) {
            srbaccount = (String[]) srbAccountList.get(i);
            if ((srbaccount[0]!=null) && (srbaccount[1]!=null) && (srbaccount[2]!=null) &&
                (srbaccount[3]!=null) && (srbaccount[4]!=null) && (srbaccount[5]!=null) &&
                (srbaccount[6]!=null))
            {
                put("srbaccount"+i+".host", srbaccount[0]);
                put("srbaccount"+i+".port", srbaccount[1]);
                put("srbaccount"+i+".user", srbaccount[2]);
                put("srbaccount"+i+".password", srbaccount[3]);
                put("srbaccount"+i+".home", srbaccount[4]);
                put("srbaccount"+i+".domain", srbaccount[5]);
                put("srbaccount"+i+".resource", srbaccount[6]);
            }
        }

        // save default modules
        String moduleName = (String)moduleListTreeView.elementAt(0);
        if ((moduleName !=null) && (moduleName.length()>0)) {
            put("module.treeview", moduleName);
        }

        moduleName = (String)moduleListMetaDataView.elementAt(0);
        if ((moduleName !=null) && (moduleName.length()>0)) {
            put("module.metadataview", moduleName);
        }

        moduleName = (String)moduleListTextView.elementAt(0);
        if ((moduleName !=null) && (moduleName.length()>0)) {
            put("module.textview", moduleName);
        }

        moduleName = (String)moduleListTableView.elementAt(0);
        if ((moduleName !=null) && (moduleName.length()>0)) {
            put("module.tableview", moduleName);
        }

        moduleName = (String)moduleListImageView.elementAt(0);
        if ((moduleName !=null) && (moduleName.length()>0)) {
            put("module.imageview", moduleName);
        }

        moduleName = (String)moduleListPaletteView.elementAt(0);
        if ((moduleName !=null) && (moduleName.length()>0)) {
            put("module.paletteview", moduleName);
        }

        //save the currenent supported fileformat
        Enumeration keys = FileFormat.getFileFormatKeys();
        String theKey = null;
        while (keys.hasMoreElements()) {
            theKey = (String)keys.nextElement();
            FileFormat theformat = FileFormat.getFileFormat(theKey);
            put("module.fileformat."+theKey, theformat.getClass().getName());
        }

        try {
            FileOutputStream fos = new FileOutputStream(propertyFile);
            store(fos, "User properties modified on ");
            fos.close();
        } catch (Exception e) {;}
    }

    /** returns the name of the user property file */
    public static String getPropertyFile(){ return propertyFile;}

    /** returns the default work directory, where the open file starts.*/
    public static String getWorkDir() {
        if (workDir.equals("user.dir")) {
            workDir = System.getProperty("user.dir");
        }

        return workDir;
    }

    /** returns the maximum number of the most recent file */
    public static int getMaxRecentFiles() { return MAX_RECENT_FILES; }

    /** return the path of the H5View uers guide */
    public static String getUsersGuide() { return usersGuide; };

    /** returns the delimiter of data values */
    public static String getDataDelimiter() { return delimiter; }

    /** returns the font size */
    public static int getFontSize() { return fontSize; }

    /** returns the font type */
    public static String getFontType()  { return fontType; }

    /** gets the file extensions of supported file formats */
    public static String getFileExtension() { return fileExt; }

    /** sets the font size */
    public static void setFontSize(int fsize) {
        fontSize = (fsize/2)*2;

       if(fontSize < 8) {
        fontSize = 8;
    }
    }

    /** sets the font size */
    public static void setFontType(String ftype)
    {
        if (ftype != null) {
            fontType = ftype.trim();
        }
    }

    /** returns the path of the H5toH5 converter */
    public static String getH4toH5() { return h4toh5; };

    /** returns the list of most recent files */
    public static Vector getMRF(){ return mrf;}

    public static Vector getSrbAccount() { return srbAccountList; }

    /** returns a list of treeview modules */
    public static Vector getTreeViewList() { return moduleListTreeView; }

    /** returns a list of metadataview modules */
    public static Vector getMetaDataViewList() { return moduleListMetaDataView; }

    /** returns a list of textview modules */
    public static Vector getTextViewList() { return moduleListTextView; }

    /** returns a list of tableview modules */
    public static Vector getTableViewList() { return moduleListTableView; }

    /** returns a list of imageview modules */
    public static Vector getImageViewList() { return moduleListImageView; }

    /** returns a list of paletteview modules */
    public static Vector getPaletteViewList() { return moduleListPaletteView; }

    /** returns a list of helpview modules */
    public static Vector getHelpViewList() { return moduleListHelpView; }

    /** set the path of H5View User's guide */
    public static void setUsersGuide( String str)
    {
        if ((str == null) || (str.length()<=0)) {
            return;
        }

        String tmpUG = str.toLowerCase();
        if (tmpUG.startsWith("file:") || tmpUG.startsWith("http:")) {
            usersGuide = str;
        } else
        {
            File tmpFile = new File(str);
            if (tmpFile.exists()) {
                usersGuide = "file:"+str;
            } else {
                usersGuide = "http://"+str;
            }
        }
    }

    /** set the path of the H5to H5 converter */
    public static void setH4toH5( String tool) { h4toh5 = tool; }

    /** set the path of the default work directory */
    public static void setWorkDir( String wDir) { workDir = wDir; }

    /** set the file extension */
    public static void setFileExtension( String ext) { fileExt = ext; }

    /** set the delimiter of data values */
    public static void setDataDelimiter(String delim) { delimiter = delim; }

    /**
     * Current Java application such as HDFView cannot handle files
     * with large number of objects such 1,000,000 objects.
     * setMaxMembers() sets the maximum number of objects will be loaded
     * into memory.
     *
     * @param n the maximum number of objects to load into memory
     */
    public static void setMaxMembers(int n) { max_members = n; }

    /**
     * Current Java application such as HDFView cannot handle files
     * with large number of objects such 1,000,000 objects.
     * setStartMember() sets the starting index of objects will be loaded
     * into memory.
     *
     * @param idx the maximum number of objects to load into memory
     */
    public static void setStartMembers(int idx)
    {
        if (idx < 0) {
            idx = 0;
        }

        start_members = idx;
    }

    /**
     * Current Java application such as HDFView cannot handle files
     * with large number of objects such 1,000,000 objects.
     * getMaxMembers() returns the maximum number of objects will be loaded
     * into memory.
     */
    public static int getMaxMembers() { return max_members; }

    /**
     * Current Java application such as HDFView cannot handle files
     * with large number of objects such 1,000,000 objects.
     * getStartMembers() returns the starting index of objects will be loaded
     * into memory.
     */
    public static int getStartMembers() { return start_members; }
    
    /**
     * Returns true if auto contrast is used in image process.
     * 
     * @return true if auto contrast is used in image process; otherwise, returns false.
     */
    public static boolean isAutoContrast() { return isAutoContrast; }

    /**
     * Set the flag to indicate if auto contrast is used in image process.
     * 
     * @param b the flag to indicate if auto contrast is used in image process.
     */
    public static void setAutoContrast(boolean b) { isAutoContrast = b; }
    
}
