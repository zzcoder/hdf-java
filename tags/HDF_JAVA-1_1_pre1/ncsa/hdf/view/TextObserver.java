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
 * An interface for receiving notifications about Text information as the
 * Text is constructed and modified.
 * <p>
 * @version 1.3.0 1/26/2002
 * @author Peter X. Cao
 */
public interface TextObserver extends DataObserver
{
    /**
     * Returns the String text.
     */
    public abstract String[] getText();
}
