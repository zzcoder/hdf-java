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

import ncsa.hdf.hdflib.*;

/**
 * Datatype holds a (name, value) pair of HDF4/5 attribute.
 * <p>
 * @version 1.0 05/07/2002
 * @author Peter X. Cao, NCSA
 */
public class H4Datatype extends Datatype
{
    /**
     * Create an Datatype with specified class, size, byte order and sign.
     * <p>
     * @param tclass the class of the datatype.
     * @param tsize the size of the datatype.
     * @param torder the order of the datatype.
     * @param tsign the sign of the datatype.
     */
    public H4Datatype(int tclass, int tsize, int torder, int tsign)
    {
        super(tclass, tsize, torder, tsign);
    }

    /**
     *  Allocate a 1D array large enough to hold a multidimensional
     *  array of 'datasize' elements of 'datatype' numbers.
     *
     *  @param datatype  the data type
     *  @param datasize  the size of the data array
     *  @return an array of 'datasize' numbers of datatype.
     */
    public static final Object allocateArray(int datatype, int datasize)
    {
        if (datasize <= 0)
            return null;

        Object data = null;

        switch(datatype)
        {
            case HDFConstants.DFNT_CHAR:
            case HDFConstants.DFNT_UCHAR8:
            case HDFConstants.DFNT_UINT8:
            case HDFConstants.DFNT_INT8:
                data = new byte[datasize];
                break;
            case HDFConstants.DFNT_INT16:
            case HDFConstants.DFNT_UINT16:
                data = new short[datasize];
                break;
            case HDFConstants.DFNT_INT32:
            case HDFConstants.DFNT_UINT32:
                data = new int[datasize];
                break;
            case HDFConstants.DFNT_INT64:
            case HDFConstants.DFNT_UINT64:
                data = new long[datasize];
                break;
            case HDFConstants.DFNT_FLOAT32:
                data = new float[datasize];
                break;
            case HDFConstants.DFNT_FLOAT64:
                data = new double[datasize];
                break;
            default:
                data = null;
                break;
        }

        return data;
    }

    /**
     *  Returns the short description of datatype.
     *  <p>
     *  @param datatype  the data type.
     */
    public static final String getDatatypeDescription(int datatype)
    {
        String description = "Unknown";

        switch(datatype)
        {
            case HDFConstants.DFNT_CHAR:
                description = "8-bit character";
                break;
            case HDFConstants.DFNT_UCHAR8:
                description = "8-bit unsigned character";
                break;
            case HDFConstants.DFNT_UINT8:
                description = "8-bit unsigned integer";
                break;
            case HDFConstants.DFNT_INT8:
                description = "8-bit integer";
                break;
            case HDFConstants.DFNT_INT16:
                description = "16-bit integer";
                break;
            case HDFConstants.DFNT_UINT16:
                description = "16-bit unsigned integer";
                break;
            case HDFConstants.DFNT_INT32:
                description = "32-bit integer";
                break;
            case HDFConstants.DFNT_UINT32:
                description = "32-bit unsigned integer";
                break;
            case HDFConstants.DFNT_INT64:
                description = "64-bit integer";
                break;
            case HDFConstants.DFNT_UINT64:
                description = "64-bit unsigned integer";
                break;
            case HDFConstants.DFNT_FLOAT32:
                description = "32-bit floating-point";
                break;
            case HDFConstants.DFNT_FLOAT64:
                description = "64-bit floating-point";
                break;
            default:
                description = "Unknown";
                break;
        }

        return description;
    }

    /**
     *  Checks if the datatype is an unsigned integer.
     *  <p>
     *  @param datatype  the data type.
     *  @return True is the datatype is an unsigned integer; otherwise returns false.
     */
    public static final boolean isUnsigned(int datatype)
    {
        boolean unsigned = false;;

        switch(datatype)
        {
            case HDFConstants.DFNT_UCHAR8:
            case HDFConstants.DFNT_UINT8:
            case HDFConstants.DFNT_UINT16:
            case HDFConstants.DFNT_UINT32:
            case HDFConstants.DFNT_UINT64:
                unsigned = true;
                break;
            default:
                unsigned = false;
                break;
        }

        return unsigned;
    }

    public int toNative()
    {
        int tid = -1;
        int tclass = getDatatypeClass();
        int tsize = getDatatypeSize();
        int torder = getDatatypeOrder();
        int tsign = getDatatypeSign();

        // figure the datatype
        switch (tclass)
        {
            case Datatype.CLASS_INTEGER:
                if (tsize == 1)
                {
                    if (tsign == Datatype.SIGN_NONE)
                        tid = HDFConstants.DFNT_UINT8;
                    else
                        tid = HDFConstants.DFNT_INT8;
                }
                else if (tsize == 2)
                {
                    if (tsign == Datatype.SIGN_NONE)
                        tid = HDFConstants.DFNT_UINT16;
                    else
                        tid = HDFConstants.DFNT_INT16;
                }
                else if (tsize == 8)
                {
                    if (tsign == Datatype.SIGN_NONE)
                        tid = HDFConstants.DFNT_UINT64;
                    else
                        tid = HDFConstants.DFNT_INT64;
                }
                else
                {
                    if (tsign == Datatype.SIGN_NONE)
                        tid = HDFConstants.DFNT_UINT32;
                    else
                        tid = HDFConstants.DFNT_INT32;
                }
                break;
            case Datatype.CLASS_FLOAT:
                if (tsize == Datatype.NATIVE)
                    tid = HDFConstants.DFNT_FLOAT;
                else if (tsize == 4)
                    tid = HDFConstants.DFNT_FLOAT32;
                else if (tsize == 8)
                    tid = HDFConstants.DFNT_FLOAT64;
                break;
            case Datatype.CLASS_CHAR:
                if (tsign == Datatype.SIGN_NONE)
                    tid = HDFConstants.DFNT_UCHAR;
                else
                    tid = HDFConstants.DFNT_CHAR;
                break;
            case Datatype.CLASS_STRING:
                    tid = HDFConstants.DFNT_CHAR;
                break;
        }

        return tid;
    }

}
