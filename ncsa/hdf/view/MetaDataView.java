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
 *The metadata view interface for displaying metadata information
 *
 * @author Peter X. Cao
 * @version 1.0
 */
public abstract interface MetaDataView extends DataView
{
    /** add an attribute to a data object.*/
    public abstract Attribute addAttribute(HObject obj);

    /** delete an attribribute from a data object.*/
    public abstract Attribute deleteAttribute(HObject obj);

}