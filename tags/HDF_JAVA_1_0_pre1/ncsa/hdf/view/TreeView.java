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

import javax.swing.*;


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
 * @version 1.3.0 10/26/2001
 * @author Peter X. Cao
 * @see ncsa.hdf.view.ViewManager
 */
public class TreeView extends JPanel
{
    public TreeView()
    {
    }
}
