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

import java.awt.Image;

/**
 * An interface for receiving notifications about Image information as the
 * Image is constructed and modified.
 * <p>
 * @version 1.3.0 1/26/2002
 * @author Peter X. Cao
 */
public interface ImageObserver extends DataObserver
{
    /**
     * Returns the image contained in this DataObserver.
     */
    public abstract Image getImage();

    /** Zoom in image up to maximum of 800%. */
    public abstract void zoomIn();

    /** Zoom out image up to minimum of 12.5%. */
    public abstract void zoomOut();

    /** Zoom image by specified zoom factor.
     *  <p>
     *  @param zf the zooming factor range from 12.5% to 800%.
     */
    public abstract void zoomTo(float zf);

    /** Flip an image horizontally or vertically.
     *  <p>
     *  @param direction the direction to flip the image.
     */
    public abstract void flip(int direction);

    /** Display an image contour.
     *  <p>
     *  @param level the contour level.
     */
    public abstract void contour(int level);

    /** Change the brightness of the image.
     *  <p>
     *  @param level the brightness level.
     */
    public abstract void brightness(int level);

    /** Display the color table of this image. */
    public abstract void showColorTable();

    /** Draws a histogram of the selected image area. */
    public abstract void showHistogram();

    /** Sets whether or not to display point value of the image. */
    public abstract void setValueVisible(boolean b);
}
