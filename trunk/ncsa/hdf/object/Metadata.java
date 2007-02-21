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

 package ncsa.hdf.object;

/**
 * Metadata is a general interface for metadata attached to data objects.
 * Metadata contains supporting information attacehd to a primary data or
 * component. Particular implementations of Metadata often provide additional
 * context-specific objects as well.
 * <p>
 * @version 1.0 12/12/2001
 * @author Peter X. Cao, NCSA
 */
public interface Metadata extends java.io.Serializable
{
    /**
     * Returns the value of this Metadata.
     */
    public abstract Object getValue();

    /**
     * Sets the value of this Metadata.
     */
    public abstract void setValue(Object value);
}
