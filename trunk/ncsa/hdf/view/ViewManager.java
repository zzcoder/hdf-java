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

/**
 *
 * <p>ViewManager</p>
 * <p>Description: defines a list of APIs for the main HDFView winodows</p>
 * <p>Company: NCSA, University of Illinois at Urbana-Champaign</p>
 * @author Peter X. Cao
 * @version 1.0, 06/10/2003
 */
public abstract interface ViewManager
{
    /** data content is displayed, and add the dataview to the main windows */
    public abstract void addDataView(DataView dataView);

    /** data content is closed, and remove the dataview from the main window */
    public abstract void removeDataView(DataView dataView);

    /** Returns DataView contains the specified data object.
     * It is useful to avoid redundant display of data object that is opened already.
     * @param dataObject the whose presence in the main view is to be tested.
     * @return DataView contains the specified data object, null if the data object
     * is not displayed.
     */
    public abstract DataView getDataView(HObject dataObject);

    /** display feedback message */
    public abstract void showStatus(String msg);

    /** returns the current treeView */
    public abstract TreeView getTreeView();

    /** Tree mouse event fired */
    public abstract void mouseEventFired(java.awt.event.MouseEvent e);
}