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
import javax.swing.JTable;

/**
 *
 * The table view interface for displaying data in table form
 *
 * @author Peter X. Cao
 * @version 1.0
 */
public abstract interface TableView extends DataView
{
    /** returns the table */
    public abstract JTable getTable();

    /** returns array of selected data */
    public abstract Object getSelectedData();

    /** Write the change of a dataset into file. */
    public abstract void updateValueInFile();


}