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
import java.util.Properties;
import java.util.Vector;
import java.awt.Color;
import java.awt.SystemColor;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.MalformedURLException;

/**
 * The ViewProperties holds all the HDFView static information.
 * <p>
 * @version 1.3.0 01/10/2002
 * @author Peter X. Cao
 */
public class ViewProperties extends Properties
{
    /** the version of the HDFViewer */
    public static final String VERSION = "1.2";

    /** the local property file name */
    public static final String USER_PROPS = "hdfview.props";

    /** the maximum number of most recent files */
    public static final int MAX_RECENT_FILES = 15;

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
    private static String fontSizeStr = "12";

    /** the font size */
    private static int fontSize = 12;

    /** the full path of H4toH5 converter */
    private static String h4toh5 ="";

    /** data delimiter */
    private static String delimiter = DELIMITER_TAB;

    /** a list of most recent files */
    private static Vector mrf;

    /** the root directory of the HDFView */
    private static String rootDir;

    private static Icon hdfIcon, h4Icon, h5Icon, largeHdfIcon,
        blankIcon, helpIcon,
        fileopenIcon, filesaveIcon, filenewIcon, filecloseIcon,
        foldercloseIcon, folderopenIcon,
        datasetIcon, imageIcon, tableIcon, textIcon,
        zoominIcon, zoomoutIcon, paletteIcon, chartIcon,
        copyIcon, cutIcon, pasteIcon,
        previousIcon, nextIcon, firstIcon, lastIcon;

    private static String propertyFile;

    /**
     * Creates a property list with given root directory of the HDFView.
     */
    public ViewProperties(String viewRoot)
    {
        super();
        mrf = new Vector();
        rootDir = viewRoot;

        // find the property file
        String uh="", ud="", h5v="", fn;

        // look for the property file at the use home directory
        fn = ViewProperties.USER_PROPS;
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
        else if ((new File(h5v)).exists())
        {
            propertyFile = h5v;
        }
        else // create new property file at user home directory
        {
            propertyFile = uh;
            File pFile = new File(uh);
            try { pFile.createNewFile(); }
            catch (Exception ex) { propertyFile = null; }
        }
    }

    /** returns the root directory where the HDFView is installed. */
    public static String getViewRoot() { return rootDir; }

    public static Icon getFoldercloseIcon() { return foldercloseIcon; }

    public static Icon getFolderopenIcon() { return folderopenIcon; }

    public static Icon getHdfIcon() { return hdfIcon; }

    public static Icon getH4Icon() { return h4Icon; }

    public static Icon getH5Icon() { return h5Icon; }

    public static Icon getDatasetIcon() { return datasetIcon; }

    public static Icon getFileopenIcon() { return fileopenIcon; }

    public static Icon getFilesaveIcon() { return filesaveIcon; }

    public static Icon getFilenewIcon() { return filenewIcon; }

    public static Icon getFilecloseIcon() { return filecloseIcon; }

    public static Icon getPaletteIcon() { return paletteIcon; }

    public static Icon getImageIcon() { return imageIcon; }

    public static Icon getTableIcon() { return tableIcon; }

    public static Icon getTextIcon() { return textIcon; }

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

    public static void loadIcons(String rootPath)
    {
        URL url= null, url2=null, url3=null;

        try {
            url = new URL("file:"+rootPath+"/lib/jhdfview.jar");
        } catch (MalformedURLException mfu) {;}

        try {
            url2 = new URL("file:"+rootPath+"/");
        } catch (MalformedURLException mfu) {;}

        try {
            url3 = new URL("file:"+rootPath+"/src/");
        } catch (MalformedURLException mfu) {;}

        URL uu[] = {url, url2, url3};
        URLClassLoader cl = new URLClassLoader(uu);
        URL u = null;

        // load icon images
        if (hdfIcon == null) {
            u = cl.findResource("ncsa/hdf/view/icons/hdf.gif");
            if (u != null) {
                hdfIcon = new ImageIcon (u);
            }
        }

        if (h4Icon == null) {
            u = cl.findResource("ncsa/hdf/view/icons/hdf4.gif");
            if (u != null) {
                h4Icon = new ImageIcon (u);
            }
        }

        if (h5Icon == null) {
            u = cl.findResource("ncsa/hdf/view/icons/hdf5.gif");
            if (u != null) {
                h5Icon = new ImageIcon (u);
            }
        }

        if (foldercloseIcon == null) {
            u = cl.findResource("ncsa/hdf/view/icons/folderclose.gif");
            if (u != null) {
                foldercloseIcon = new ImageIcon (u);
            }
        }

        if (folderopenIcon == null) {
            u = cl.findResource("ncsa/hdf/view/icons/folderopen.gif");
            if (u != null) {
                folderopenIcon = new ImageIcon (u);
            }
        }

        if (datasetIcon == null) {
            u = cl.findResource("ncsa/hdf/view/icons/dataset.gif");
            if (u != null) {
                datasetIcon = new ImageIcon (u);
            }
        }

        if (fileopenIcon == null) {
            u = cl.findResource("ncsa/hdf/view/icons/fileopen.gif");
            if (u != null) {
                fileopenIcon = new ImageIcon (u);
            }
        }

        if (filesaveIcon == null) {
            u = cl.findResource("ncsa/hdf/view/icons/filesave.gif");
            if (u != null) {
                filesaveIcon = new ImageIcon (u);
            }
        }

        if (filenewIcon == null) {
            u = cl.findResource("ncsa/hdf/view/icons/filenew.gif");
            if (u != null) {
                filenewIcon = new ImageIcon (u);
            }
        }

        if (filecloseIcon == null) {
            u = cl.findResource("ncsa/hdf/view/icons/fileclose.gif");
            if (u != null) {
                filecloseIcon = new ImageIcon (u);
            }
        }

        if (paletteIcon == null) {
            u = cl.findResource("ncsa/hdf/view/icons/palette.gif");
            if (u != null) {
                paletteIcon = new ImageIcon (u);
            }
        }

        if (imageIcon == null) {
            u = cl.findResource("ncsa/hdf/view/icons/image.gif");
            if (u != null) {
                imageIcon = new ImageIcon (u);
            }
        }

        if (tableIcon == null) {
            u = cl.findResource("ncsa/hdf/view/icons/table.gif");
            if (u != null) {
                tableIcon = new ImageIcon (u);
            }
        }

        if (textIcon == null) {
            u = cl.findResource("ncsa/hdf/view/icons/text.gif");
            if (u != null) {
                textIcon = new ImageIcon (u);
            }
        }

        if (zoominIcon == null) {
            u = cl.findResource("ncsa/hdf/view/icons/zoomin.gif");
            if (u != null) {
                zoominIcon = new ImageIcon (u);
            }
        }

        if (zoomoutIcon == null) {
            u = cl.findResource("ncsa/hdf/view/icons/zoomout.gif");
            if (u != null) {
                zoomoutIcon = new ImageIcon (u);
            }
        }

        if (blankIcon == null) {
            u = cl.findResource("ncsa/hdf/view/icons/blank.gif");
            if (u != null) {
                blankIcon = new ImageIcon (u);
            }
        }

        if (helpIcon == null) {
            u = cl.findResource("ncsa/hdf/view/icons/help.gif");
            if (u != null) {
                helpIcon = new ImageIcon (u);
            }
        }

        if (copyIcon == null) {
            u = cl.findResource("ncsa/hdf/view/icons/copy.gif");
            if (u != null) {
                copyIcon = new ImageIcon (u);
            }
        }

        if (cutIcon == null) {
            u = cl.findResource("ncsa/hdf/view/icons/cut.gif");
            if (u != null) {
                cutIcon = new ImageIcon (u);
            }
        }

        if (pasteIcon == null) {
            u = cl.findResource("ncsa/hdf/view/icons/paste.gif");
            if (u != null) {
                pasteIcon = new ImageIcon (u);
            }
        }

        if (largeHdfIcon == null) {
            u = cl.findResource("ncsa/hdf/view/icons/hdf_large.gif");
            if (u != null) {
                largeHdfIcon = new ImageIcon (u);
            }
        }

        if (previousIcon == null) {
            u = cl.findResource("ncsa/hdf/view/icons/previous.gif");
            if (u != null) {
                previousIcon = new ImageIcon (u);
            }
        }

        if (nextIcon == null) {
            u = cl.findResource("ncsa/hdf/view/icons/next.gif");
            if (u != null) {
                nextIcon = new ImageIcon (u);
            }
        }

        if (firstIcon == null) {
            u = cl.findResource("ncsa/hdf/view/icons/first.gif");
            if (u != null) {
                firstIcon = new ImageIcon (u);
            }
        }

        if (lastIcon == null) {
            u = cl.findResource("ncsa/hdf/view/icons/last.gif");
            if (u != null) {
                lastIcon = new ImageIcon (u);
            }
        }

        if (chartIcon == null) {
            u = cl.findResource("ncsa/hdf/view/icons/chart.gif");
            if (u != null) {
                chartIcon = new ImageIcon (u);
            }
        }
    }

    /** Load user properties from property file */
    public void load() throws Exception
    {
        if (propertyFile == null)
            return;

        try {
            FileInputStream fis = new FileInputStream(propertyFile);
            load(fis);
            fis.close();
        } catch (Exception e) {;}

        String str = (String)get("users.guide");

        if (str != null)
        {
            String tmpUG = str.toLowerCase();
            if (tmpUG.startsWith("file:") || tmpUG.startsWith("http:"))
                usersGuide = str;
        }
        else
        {
            File tmpFile = new File(str);
            if (tmpFile.exists())
                usersGuide = "file:"+str;
            else
                usersGuide = "http://"+str;
        }

        str = (String)get("data.delimiter");
        if (str != null && str.length()>0)
            delimiter = str;

        str = (String)get("h4toh5.converter");
        if (str != null && str.length()>0)
            h4toh5 = str;

        str = (String)get("font.size");
        if (str != null && str.length()>0)
            setFontSize(str);

        // load the most recent file list from the property file
        if (mrf != null)
        {
            String theFile;
            for (int i=0; i<MAX_RECENT_FILES; i++)
            {
                theFile = getProperty("recent.file"+i);
                if (theFile != null &&
                    !mrf.contains(theFile) &&
                    (new File(theFile)).exists())
                {
                    mrf.addElement(theFile);
                }
                else
                {
                    this.remove("recent.file"+i);
                }
            }
        }
    }

    /** Save user properties into property file */
    public void save()
    {
        if (propertyFile == null)
            return;

        // update data saving options
        if (delimiter == null)
            put("data.delimiter", DELIMITER_TAB);
        else
            put("data.delimiter", delimiter);

        if (usersGuide != null)
            put("users.guide", usersGuide);

        if (h4toh5 != null)
            put("h4toh5.converter", h4toh5);

        if (fontSizeStr != null)
            put("font.size", fontSizeStr);

        if (mrf != null)
        {
            String theFile;
            int size = mrf.size();
            int minSize = Math.min(size, MAX_RECENT_FILES);

            for (int i=0; i<minSize; i++)
            {
                theFile = (String)mrf.elementAt(size-minSize+i);
                if (theFile != null && theFile.length()>0)
                 put("recent.file"+i, theFile);
            }
        }

        try {
            FileOutputStream fos = new FileOutputStream(propertyFile);
            store(fos, "User properties modified on ");
            fos.close();
        } catch (Exception e) {;}
    }

    /** returns the name of the user property file */
    public static String getPropertyFile(){ return propertyFile;}

    /** returns the maximum number of the most recent file */
    public static int getMaxRecentFiles() { return MAX_RECENT_FILES; }

    /** return the path of the H5View uers guide */
    public static String getUsersGuide() { return usersGuide; };

    /** returns the delimiter of data values */
    public static String getDataDelimiter() { return delimiter; }

    /** returns the font size */
    public static String getFontSize() { return fontSizeStr; }

    /** returns the font size */
    public static int getFontSizeInt() { return fontSize; }

    /** sets the font size */
    public static void setFontSize(String fsize) {
        try { fontSize = Integer.parseInt(fsize); }
        catch (Exception ex ) {fontSize = 12;}

        fontSize = (fontSize/2)*2;
        if (fontSize > 20 || fontSize < 10)
            fontSize = 12;

        fontSizeStr = String.valueOf(fontSize);
    }

    /** returns the path of the H5toH5 converter */
    public static String getH4toH5() { return h4toh5; };

    /** returns the list of most recent files */
    public static Vector getMRF(){ return mrf;}

    /** set the path of H5View User's guide */
    public static void setUsersGuide( String str)
    {
        if (str == null || str.length()<=0)
            return;

        String tmpUG = str.toLowerCase();
        if (tmpUG.startsWith("file:") || tmpUG.startsWith("http:"))
            usersGuide = str;
        else
        {
            File tmpFile = new File(str);
            if (tmpFile.exists())
                usersGuide = "file:"+str;
            else
                usersGuide = "http://"+str;
        }
    }

    /** set the path of the H5to H5 converter */
    public static void setH4toH5( String tool) { h4toh5 = tool; };

    /** set the delimiter of data values */
    public static void setDataDelimiter(String delim) { delimiter = delim; }
}
