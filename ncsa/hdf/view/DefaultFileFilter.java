
package ncsa.hdf.view;

import java.io.File;
import java.util.Hashtable;
import java.util.Enumeration;
import javax.swing.*;
import javax.swing.filechooser.*;

/**
 * A convenience implementation of FileFilter that filters out
 * all files except for those type extensions that it knows about.
 *
 * Extensions are of the type ".foo", which is typically found on
 * Windows and Unix boxes, but not on Macinthosh. Case is ignored.
 *
 * Example - create a new filter that filerts out all files
 * but gif and jpg image files:
 *
 *     JFileChooser chooser = new JFileChooser();
 *     DefaultFileFilter filter = new DefaultFileFilter(
 *                   new String{"gif", "jpg"}, "JPEG & GIF Images")
 *     chooser.addChoosableFileFilter(filter);
 *     chooser.showOpenDialog(this);
 *
 * @version 1.10 02/06/02
 * @author Jeff Dinkins
 */
public class DefaultFileFilter extends FileFilter
{
    private static String TYPE_UNKNOWN = "Type Unknown";
    private static String HIDDEN_FILE = "Hidden File";

    private static FileFilter FILE_FILTER_HDF = null;
    private static FileFilter FILE_FILTER_JPEG = null;
    private static FileFilter FILE_FILTER_TIFF = null;
    private static FileFilter FILE_FILTER_PNG = null;
    private static FileFilter FILE_FILTER_TEXT = null;

    private Hashtable filters = null;
    private String description = null;
    private String fullDescription = null;
    private boolean useExtensionsInDescription = true;

    /**
     * Creates a file filter. If no filters are added, then all
     * files are accepted.
     *
     * @see #addExtension
     */
    public DefaultFileFilter() {
	this.filters = new Hashtable();
    }

    /**
     * Creates a file filter that accepts files with the given extension.
     * Example: new DefaultFileFilter("jpg");
     *
     * @see #addExtension
     */
    public DefaultFileFilter(String extension) {
	this(extension,null);
    }

    /**
     * Creates a file filter that accepts the given file type.
     * Example: new DefaultFileFilter("jpg", "JPEG Image Images");
     *
     * Note that the "." before the extension is not needed. If
     * provided, it will be ignored.
     *
     * @see #addExtension
     */
    public DefaultFileFilter(String extension, String description) {
	this();
	if(extension!=null) addExtension(extension);
 	if(description!=null) setDescription(description);
    }

    /**
     * Creates a file filter from the given string array.
     * Example: new DefaultFileFilter(String {"gif", "jpg"});
     *
     * Note that the "." before the extension is not needed adn
     * will be ignored.
     *
     * @see #addExtension
     */
    public DefaultFileFilter(String[] filters) {
	this(filters, null);
    }

    /**
     * Creates a file filter from the given string array and description.
     * Example: new DefaultFileFilter(String {"gif", "jpg"}, "Gif and JPG Images");
     *
     * Note that the "." before the extension is not needed and will be ignored.
     *
     * @see #addExtension
     */
    public DefaultFileFilter(String[] filters, String description) {
	this();
	for (int i = 0; i < filters.length; i++) {
	    // add filters one by one
	    addExtension(filters[i]);
	}
 	if(description!=null) setDescription(description);
    }

    /**
     * Return true if this file should be shown in the directory pane,
     * false if it shouldn't.
     *
     * Files that begin with "." are ignored.
     *
     * @see #getExtension
     * @see FileFilter#accepts
     */
    public boolean accept(File f) {
	if(f != null) {
	    if(f.isDirectory()) {
		return true;
	    }
	    String extension = getExtension(f);
	    if(extension != null && filters.get(getExtension(f)) != null) {
		return true;
	    };
	}
	return false;
    }

    /**
     * Return the extension portion of the file's name .
     *
     * @see #getExtension
     * @see FileFilter#accept
     */
     public String getExtension(File f) {
	if(f != null) {
	    String filename = f.getName();
	    int i = filename.lastIndexOf('.');
	    if(i>0 && i<filename.length()-1) {
		return filename.substring(i+1).toLowerCase();
	    };
	}
	return null;
    }

    /**
     * Adds a filetype "dot" extension to filter against.
     *
     * For example: the following code will create a filter that filters
     * out all files except those that end in ".jpg" and ".tif":
     *
     *   DefaultFileFilter filter = new DefaultFileFilter();
     *   filter.addExtension("jpg");
     *   filter.addExtension("tif");
     *
     * Note that the "." before the extension is not needed and will be ignored.
     */
    public void addExtension(String extension) {
	if(filters == null) {
	    filters = new Hashtable(5);
	}
	filters.put(extension.toLowerCase(), this);
	fullDescription = null;
    }


    /**
     * Returns the human readable description of this filter. For
     * example: "JPEG and GIF Image Files (*.jpg, *.gif)"
     *
     * @see setDescription
     * @see setExtensionListInDescription
     * @see isExtensionListInDescription
     * @see FileFilter#getDescription
     */
    public String getDescription() {
	if(fullDescription == null) {
	    if(description == null || isExtensionListInDescription()) {
 		fullDescription = description==null ? "(" : description + " (";
		// build the description from the extension list
		Enumeration extensions = filters.keys();
		if(extensions != null) {
		    fullDescription += "." + (String) extensions.nextElement();
		    while (extensions.hasMoreElements()) {
			fullDescription += ", " + (String) extensions.nextElement();
		    }
		}
		fullDescription += ")";
	    } else {
		fullDescription = description;
	    }
	}
	return fullDescription;
    }

    /**
     * Sets the human readable description of this filter. For
     * example: filter.setDescription("Gif and JPG Images");
     *
     * @see setDescription
     * @see setExtensionListInDescription
     * @see isExtensionListInDescription
     */
    public void setDescription(String description) {
	this.description = description;
	fullDescription = null;
    }

    /**
     * Determines whether the extension list (.jpg, .gif, etc) should
     * show up in the human readable description.
     *
     * Only relevent if a description was provided in the constructor
     * or using setDescription();
     *
     * @see getDescription
     * @see setDescription
     * @see isExtensionListInDescription
     */
    public void setExtensionListInDescription(boolean b) {
	useExtensionsInDescription = b;
	fullDescription = null;
    }

    /**
     * Returns whether the extension list (.jpg, .gif, etc) should
     * show up in the human readable description.
     *
     * Only relevent if a description was provided in the constructor
     * or using setDescription();
     *
     * @see getDescription
     * @see setDescription
     * @see setExtensionListInDescription
     */
    public boolean isExtensionListInDescription() {
	return useExtensionsInDescription;
    }

    /** Return a file filter for HDF4/5 file. */
    public static FileFilter getFileFilterHDF()
    {
        if (FILE_FILTER_HDF != null)
            return FILE_FILTER_HDF;

        DefaultFileFilter filter = new DefaultFileFilter();
        filter.addExtension("hdf");
        filter.addExtension("h5");
        filter.addExtension("h4");
        filter.addExtension("hdf4");
        filter.addExtension("hdf5");
        filter.setDescription("HDF4 & HDF5");
        FILE_FILTER_HDF = filter;

        return FILE_FILTER_HDF;
    }

    /** Return a file filter for JPEG image file. */
    public static FileFilter getFileFilterJPEG()
    {
        if (FILE_FILTER_JPEG != null)
            return FILE_FILTER_JPEG;

        DefaultFileFilter filter = new DefaultFileFilter();
        filter.addExtension("jpg");
        filter.addExtension("jpeg");
        filter.setDescription("JPEG images");
        FILE_FILTER_JPEG = filter;

        return FILE_FILTER_JPEG;
    }

    /** Return a file filter for TIFF image file. */
    public static FileFilter getFileFilterTIFF()
    {
        if (FILE_FILTER_TIFF != null)
            return FILE_FILTER_TIFF;

        DefaultFileFilter filter = new DefaultFileFilter();
        filter.addExtension("tif");
        filter.addExtension("tiff");
        filter.setDescription("TIFF images");
        FILE_FILTER_TIFF = filter;

        return FILE_FILTER_TIFF;
    }

    /** Return a file filter for PNG image file. */
    public static FileFilter getFileFilterPNG()
    {
        if (FILE_FILTER_PNG != null)
            return FILE_FILTER_PNG;

        DefaultFileFilter filter = new DefaultFileFilter();
        filter.addExtension("png");
        filter.setDescription("PNG images");
        FILE_FILTER_PNG = filter;

        return FILE_FILTER_PNG;
    }

    /** Return a file filter for text file. */
    public static FileFilter getFileFilterText()
    {
        if (FILE_FILTER_TEXT != null)
            return FILE_FILTER_TEXT;

        DefaultFileFilter filter = new DefaultFileFilter();
        filter.addExtension("txt");
        filter.addExtension("text");
        filter.setDescription("Text");
        FILE_FILTER_TEXT = filter;

        return FILE_FILTER_TEXT;
    }

}
