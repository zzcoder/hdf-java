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

import javax.swing.JTable;

/**
 * An interface for receiving notifications about Table information as the
 * Table is constructed and modified.
 * <p>
 * @version 1.3.0 1/26/2002
 * @author Peter X. Cao
 */
public interface TableObserver extends DataObserver
{
    /** Returns the table of this table observer. */
    public abstract JTable getTable();

    /** Draws a line plot of the selected table data. */
    public abstract void drawLineplot();
}
