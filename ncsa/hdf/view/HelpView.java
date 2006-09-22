/****************************************************************************
 * NCSA HDF                                                                 *
 * National Computational Science Alliance                                   *
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
 * The helpview interface for displaying user help information
 *
 * @author Peter X. Cao
 * @version 1.0
 */
public abstract interface HelpView {
    /** display help informaion */
    public abstract void show();

    /** Returns the HelpView's label, which is used to displayed in the HDFVIew help menu. */
    public abstract String getLabel();

    /** Returns the action command for this HelpView. */
    public abstract String getActionCommand();
}
