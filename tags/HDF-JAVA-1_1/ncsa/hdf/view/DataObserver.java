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
 * This interface describes the communication between the data viewer and other
 * GUI components which handle user input/output operations performed on data
 * objects.
 * <p>
 * @version 1.3.0 1/26/2002
 * @author Peter X. Cao
 */
public interface DataObserver
{
    /** Returns the data object of this DataObserver. */
    public abstract Object getDataObject();

    /** Displays the previous data sheet of 3D dataset. */
    public abstract void previousPage();

    /** Displays the next data sheet of 3D dataset. */
    public abstract void nextPage();

    /** Displays the first data sheet of 3D dataset. */
    public abstract void firstPage();

    /** Displays the last data sheet of 3D dataset. */
    public abstract void lastPage();

    /** Disposes of this dataobserver. */
    public void dispose();

}
