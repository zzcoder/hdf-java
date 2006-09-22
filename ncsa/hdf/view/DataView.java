/****************************************************************************
 * NCSA HDF                                                                 *
 * National Computational Science Alliance                                  *
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
 * The data view interface for displaying data object
 *
 * @author Peter X. Cao
 * @version 1.0
 */
public abstract interface DataView
{
    /** returns the data object displayed in this data viewer */
    public abstract HObject getDataObject();

    /** Disposes this datao viewer */
    public abstract void dispose();

}