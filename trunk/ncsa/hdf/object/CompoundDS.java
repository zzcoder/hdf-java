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

import java.util.*;

/**
 * CompoundDS is the superclass for HDF4 and HDF5 Compound Dataset.
 * <p>
 * A compound datatype is a collection of one or more atomic types or small
 * arrays of such types. Each member of a compound type has a name which is
 * unique within that type, and a datatype of that member in a compound datum
 * <p>
 * @version 1.0 12/12/2001
 * @author Peter X. Cao, NCSA
 */
public abstract class CompoundDS extends Dataset
{
    /**
     * The number of members of this compound dataset.
     */
    protected int numberOfMembers;

    /**
     * The names of members of this compound dataset.
     */
    protected String[] memberNames;

    /**
     * The data types of the members of this compound dataset.
     */
    protected int[] memberTypes;

    /**
     * Creates a CompoundDS object with specific name and path.
     * <p>
     * @param fileFormat the HDF file.
     * @param name the name of this CompoundDS.
     * @param path the full path of this CompoundDS.
     * @param oid the unique identifier of this data object.
     */
    public CompoundDS(
        FileFormat fileFormat,
        String name,
        String path,
        long[] oid)
    {
        super (fileFormat, name, path, oid);

        numberOfMembers = 0;
        memberNames = null;
        memberTypes = null;
    }

    /**
     * Returns the number of members of this compound dataset.
     */
    public final int getMemberCount()
    {
        return numberOfMembers;
    }

    /**
     * Returns the names of the members of the datasets
     */
    public final String[] getMemberNames()
    {
        return memberNames;
    }

    /**
     * Returns the data types of the members of this compound dataset.
     */
    public final int[] getMemberTypes()
    {
        return memberTypes;
    }

}
