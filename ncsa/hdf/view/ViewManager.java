/****************************************************************************
 * NCSA HDF                                                                 *
 * National Comptational Science Alliance                                   *
 * University of Illinois at Urbana-Champaign                               *
 * 605 E. Springfield, Champaign IL 61820                                   *
 *                                                                          *
 * For conditions of distribution and use, see the accompanying             *
 * java-hdf4&5 COPYING file.                                                *
 *                                                                          *
 ****************************************************************************/

 package ncsa.hdf.view;

/**
 * This interface describes the communication between the main viewer and other
 * GUI components which handle user input/output operations performed on data
 * objects. For example, when user click on an dataset in the TreeView to display
 * the data content. The action is passed to the ViewManager. The Viewmanager
 * fetch the data and create a spreadsheet to display the data. The feedback of
 * the user action is passed to the ViewManager.
 * <p>
 * The interface is implemented by HDFView.
 *
 * @version 1.3.0 10/26/2001
 * @author Peter X. Cao
 */
public interface ViewManager
{
    /**
     * Returns the the current working data object or null if the current
     * data object does not exist. The currentdata object along with the current
     * working file is uniquely identified by other GUI components.
     */
    public abstract Object getCurrentObject();

    /**
     * Displays the content of current selected data object. If the data of the
     * object is not loaded, it first loads the data from file into memory and
     * then display it. If the data is already loaded or displayed, it brings
     * the displayed the data to the front. If the object has no data or is
     * null or the Viewer does not support the type of the data, it will give
     * warning message to the user.
     * <p>
     * @param data the data object to be displayed
     */
    public abstract void showDataContent(Object data);

    /**
     * Displays the metadata such as attributes and datatype of current selected
     * data object. If the metadata of the object is not loaded, it first loads
     * the metadata from file into memory and then display it. If the object has
     * no metadata or the object is null, it will give warning message.
     * <p>
     * @param data the data object to be displayed
     */
    public abstract void showDataInfo(Object data);

    /**
     * Displays feedback message in "status window" such as error message and
     * warning message, and inform users of its current state.
     */
    public abstract void showStatus(String msg);
}