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

/**
 * The ViewProperties holds all the HDFView static information.
 * <p>
 * @version 1.3.0 01/10/2002
 * @author Peter X. Cao
 */
public class ViewProperties extends Properties
{
    /** the version of the HDFViewer */
    public static final String VERSION = "1.0";

    /** the local property file name */
    public static final String USER_PROPS = "hdfview.props";

    /** the maximum number of most recent files */
    public static final int MAX_RECENT_FILES = 10;

    /** name of default Gray palette */
    public static final String PALETTE_GRAY = "Gray";

    /** name of default Rainbow palette */
    public static final String PALETTE_RAINBOW = "Rainbow";

    /** name of default Nature palette */
    public static final String PALETTE_NATURE = "Nature";

    /** name of default Wave palette */
    public static final String PALETTE_WAVE = "Wave";

    /** name of the tab delimiter */
    public static final String DELIMITER_TAB = "Tab";

    /** name of the tab delimiter */
    public static final String DELIMITER_COMMA = "Comma";

    /** name of the tab delimiter */
    public static final String DELIMITER_SPACE = "Space";

    /** name of the tab delimiter */
    public static final String DELIMITER_COLON = "Colon";

    /** browser path */
    private static String browserPath = "";

    /** user's guide */
    private static String usersGuide = "";

    /** the full path of H4toH5 converter */
    private static String h4toh5 ="";

    /** name of the default palette */
    private static String defaultPalette = PALETTE_GRAY;

    /** data delimiter */
    private static String delimiter = DELIMITER_TAB;

    /** a list of most recent files */
    private static Vector mrf;

    private static Icon hdfIcon, largeHdfIcon, blankIcon,
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

    public static Icon getFoldercloseIcon() { return foldercloseIcon; }

    public static Icon getFolderopenIcon() { return folderopenIcon; }

    public static Icon getHdfIcon() { return hdfIcon; }

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
            url = new URL("file:"+rootPath+"/lib/hdfview.jar");
            url2 = new URL("file:"+rootPath+"/");
            url3 = new URL("file:"+rootPath+"/src/");
        } catch (java.net.MalformedURLException mfu) {;}

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
    public void load()
    {
        if (propertyFile == null)
            return;

        try {
            FileInputStream fis = new FileInputStream(propertyFile);
            load(fis);
            fis.close();
        } catch (Exception e) {;}

        delimiter = (String)get("data.delimiter");
        browserPath = (String)get("browser.path");
        usersGuide = (String)get("users.guide");
        defaultPalette = (String)get("default.palette");
        h4toh5 = (String)get("h4toh5.converter");

        if (delimiter == null ) delimiter = DELIMITER_TAB;
        if (defaultPalette == null ) defaultPalette = PALETTE_GRAY;

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

        if (browserPath!=null)
            put("browser.path", browserPath);

        if (usersGuide != null)
            put("users.guide", usersGuide);

        if (defaultPalette != null)
            put("default.palette", defaultPalette);

        if (h4toh5 != null)
            put("h4toh5.converter", h4toh5);

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

    /** return the name of the default palette: RAINBOW or GRAY */
    public static String getDefaultPalette() { return defaultPalette; }

    /** returns the delimiter of data values */
    public static String getDataDelimiter() { return delimiter; }

    /** returns the path of browser */
    public static String getBrowserPath() { return browserPath; };

    /** return the path of the H5View uers guide */
    public static String getUsersGuide() { return usersGuide; };

    /** returns the path of the H5toH5 converter */
    public static String getH4toH5() { return h4toh5; };

    /** returns the list of most recent files */
    public static Vector getMRF(){ return mrf;}

    /** set the default palette to gray */
    public static void setDefaultPalette(String p) { defaultPalette = p; };

    /** set the path of browser */
    public static void setBrowserPath( String bPath) { browserPath = bPath; };

    /** set the path of H5View User's guide */
    public static void setUsersGuide( String ug) { usersGuide = ug; };

    /** set the path of the H5to H5 converter */
    public static void setH4toH5( String tool) { h4toh5 = tool; };

    /** set the delimiter of data values */
    public static void setDataDelimiter(String delim) { delimiter = delim; }
}
