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
 * <p>Title: ncsa.hdf.view.TextView</p>
 * <p>The text view interface for displaying text data</p>
 * <p>Company: National Computational Science Alliance</p>
 * @author Peter X. Cao
 * @version 1.0
 */
public abstract interface TextView extends DataView {
    /** return array of the text in this textview */
    public abstract String[] getText();
}