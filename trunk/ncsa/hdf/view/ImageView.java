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
import java.awt.Rectangle;
import java.awt.Image;

/**
 *
 * <p>Title: ncsa.hdf.view.ImageView</p>
 * <p>The image view interface for displaying image object</p>
 * <p>Company: National Computational Science Alliance</p>
 * @author Peter X. Cao
 * @version 1.0
 */
public abstract interface ImageView extends DataView
{
    /** returns the selected area of the image
     * @return the rectangle of the selected image area.
     */
    public abstract Rectangle getSelectedArea();

    /** @return true if the image is a truecolor image. */
    public abstract boolean isTrueColor() ;

    /** @return true if the image interlace is plance interlace. */
    public abstract boolean isPlaneInterlace() ;

    /** returns array of selected data */
    public abstract Object getSelectedData();

    /** returns the image displayed in this imageView */
    public abstract Image getImage();

    /** sets the image */
    public abstract void setImage(Image img);

    /** returns the palette of the image*/
    public abstract byte[][] getPalette();

    /** sets the image palette*/
    public abstract void setPalette(byte[][] palette);

    /** returns the byte array of the image data */
    public abstract byte[] getImageByteData();

}