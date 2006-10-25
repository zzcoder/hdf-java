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


/**
 *
 * The text view interface for displaying text data
 *
 * @author Peter X. Cao
 * @version 1.0
 */
public abstract interface TextView extends DataView {
    /** return array of the text in this textview */
    public abstract String[] getText();

    /** Write the change of a dataset into file. */
    public abstract void updateValueInFile();

}