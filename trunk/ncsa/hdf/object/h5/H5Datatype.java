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

package ncsa.hdf.object.h5;

import ncsa.hdf.hdf5lib.*;
import ncsa.hdf.object.*;

/**
 * Datatype holds a (name, value) pair of HDF4/5 attribute.
 * <p>
 * @version 1.0 05/07/2002
 * @author Peter X. Cao, NCSA
 */
public class H5Datatype extends Datatype
{
    /**
     * Create an Datatype with specified class, size, byte order and sign.
     * <p>
     * @param tclass the class of the datatype.
     * @param tsize the size of the datatype.
     * @param torder the order of the datatype.
     * @param tsign the sign of the datatype.
     */
    public H5Datatype(int tclass, int tsize, int torder, int tsign)
    {
        super(tclass, tsize, torder, tsign);
    }

    /**
     * Create a Datatype with a given HDF native datatype.
     * <p>
     * @param nativeType the hdf native datatype.
     */
    public H5Datatype(int nativeType)
    {
        super(nativeType);
    }

    /**
     * Specify this datatype with a given HDF native datatype.
     */
    public void fromNative(int tid)
    {
        nativeType = tid;

        int tclass=-1, tsize=-1;

        try
        {
            tclass = H5.H5Tget_class(tid);
            tsize = H5.H5Tget_size(tid);
        } catch (Exception ex)
        {
            datatypeClass = CLASS_ERROR;
        }

        if (tclass == HDF5Constants.H5T_INTEGER)
        {
            datatypeClass = CLASS_INTEGER;
            try {
                int tsign = H5.H5Tget_sign(tid);
                if (tsign == HDF5Constants.H5T_SGN_NONE)
                    datatypeSign = SIGN_NONE;
            } catch (Exception ex) {}
        }
        else if (tclass == HDF5Constants.H5T_FLOAT)
            datatypeClass = CLASS_FLOAT;
        else if (tclass == HDF5Constants.H5T_STRING)
            datatypeClass = CLASS_STRING;
        else if (tclass == HDF5Constants.H5T_REFERENCE)
            datatypeClass = CLASS_REFERENCE;

        datatypeSize = tsize;
        datatypeOrder = NATIVE;
    }

    /**
     * Allocate a one-dimensional array of byte, short, int, long, float, double,
     * or String to store data retrieved from an HDF5 file based on the given
     * HDF5 datatype and dimension sizes.
     * <p>
     * @param tid the datatype.
     * @param size the total size of the array.
     * @return the array object if successful and null otherwise.
     */
    public static Object allocateArray(int tid, int size)
    throws OutOfMemoryError
    {
        Object data = null;

        if (size < 0)
            return null;

        // Scalar members have dimensionality zero, i.e. size =0
        // what can we do about it, set the size to 1
        if (size == 0) size = 1;

        // data type information
        int tclass=-1, tsize=-1, tsign=-1;

        try
        {
            tclass = H5.H5Tget_class(tid);
            tsize = H5.H5Tget_size(tid);
            tsign = H5.H5Tget_sign(tid);
        } catch (Exception ex) {}

        switch (tclass)
        {
            case HDF5Constants.H5T_INTEGER:
                if (tsize == 1)
                {
                    data = new byte[size];
                }
                else if (tsize == 2)
                {
                    data = new short[size];
                }
                else if (tsize == 4)
                {
                    data = new int[size];
                }
                else if (tsize == 8)
                {
                    data = new long[size];
                }
                break;
            case HDF5Constants.H5T_FLOAT:
                if (tsize == 4)
                {
                    data = new float[size];
                }
                else if (tsize == 8)
                {
                    data = new double[size];
                }
                break;
            case HDF5Constants.H5T_STRING:
            case HDF5Constants.H5T_REFERENCE:
            case HDF5Constants.H5T_BITFIELD:
                data = new byte[size*tsize];
                break;
            case HDF5Constants.H5T_ARRAY:
                // use the base datatype to define the array
                try {
                    int mn = H5.H5Tget_array_ndims(tid);
                    int[] marray = new int[mn];

                    H5.H5Tget_array_dims(tid, marray, null);
                    int asize = 1;
                    for (int j=0; j<mn; j++)
                    {
                        asize *= marray[j];
                    }
                    data =  allocateArray (
                        H5.H5Tget_super(tid),
                        size *asize);
                } catch (Exception ex) {}
                break;
            case HDF5Constants.H5T_COMPOUND:
            case HDF5Constants.H5T_VLEN:
            default:
                data = null;
                break;
        }

        return data;
    }

    /**
     * Return the HDF5 native datatype based on the HDF5 datatype on disk
     * <p>
     * @param tid the datatype on disk.
     * @return the native datatype if successful and negative otherwise.
     */
    public static int toNativeType(int tid)
    {
        // data type information
        int native_type=-1, tclass=-1, tsize=-1, tsign=-1;

        try
        {
            tclass = H5.H5Tget_class(tid);
            tsize = H5.H5Tget_size(tid);
            tsign = H5.H5Tget_sign(tid);
        } catch (Exception ex) {}

        switch (tclass)
        {
            case HDF5Constants.H5T_INTEGER:
                if (tsize == 1) {
                    if (tsign == HDF5Constants.H5T_SGN_NONE)
                        native_type = H5.J2C(
                            HDF5CDataTypes.JH5T_NATIVE_UINT8);
                    else
                        native_type = H5.J2C(
                            HDF5CDataTypes.JH5T_NATIVE_INT8);
                }
                else if (tsize == 2) {
                    if (tsign == HDF5Constants.H5T_SGN_NONE)
                        native_type = H5.J2C(
                            HDF5CDataTypes.JH5T_NATIVE_UINT16);
                    else
                        native_type = H5.J2C(
                            HDF5CDataTypes.JH5T_NATIVE_INT16);
                }
                else if (tsize == 4) {
                    if (tsign == HDF5Constants.H5T_SGN_NONE)
                        native_type = H5.J2C(
                            HDF5CDataTypes.JH5T_NATIVE_UINT32);
                    else
                        native_type = H5.J2C(
                            HDF5CDataTypes.JH5T_NATIVE_INT32);
                }
                else if (tsize == 8) {
                    if (tsign == HDF5Constants.H5T_SGN_NONE)
                        native_type = H5.J2C(
                            HDF5CDataTypes.JH5T_NATIVE_UINT64);
                    else
                        native_type = H5.J2C(
                            HDF5CDataTypes.JH5T_NATIVE_INT64);
                }
                break;
            case HDF5Constants.H5T_FLOAT:
                if (tsize == 4)
                {
                    native_type = H5.J2C(
                        HDF5CDataTypes.JH5T_NATIVE_FLOAT);
                }
                else if (tsize == 8) {
                    native_type = H5.J2C(
                        HDF5CDataTypes.JH5T_NATIVE_DOUBLE);
                }
                break;
            default:
                try { native_type = H5.H5Tcopy(tid); }
                catch (Exception ex) {}
                break;
        }

        return native_type;
    }

    /**
     *  Returns the short description of datatype.
     *  <p>
     *  @param tid  the data type.
     */
    public static final int getDatatypeSize(int tid)
    {
        String description = "Unknown";

        // data type information
        int tsize=-1;

        try {
            tsize = H5.H5Tget_size(tid);
        } catch (Exception ex) {tsize = -1; }

         return tsize;
    }

    // implementing Datatype
    public String getDatatypeDescription()
    {
        return getDatatypeDescription(toNative());
    }

     /**
     *  Returns the short description of a given datatype.
     */
    public static final String getDatatypeDescription(int tid)
    {
        String description = "Unknown";

        // data type information
        int tclass=-1, tsize=-1, tsign=-1;

        try
        {
            tclass = H5.H5Tget_class(tid);
            tsize = H5.H5Tget_size(tid);
            tsign = H5.H5Tget_sign(tid);
        } catch (Exception ex) {}

        switch (tclass)
        {
            case HDF5Constants.H5T_INTEGER:
                if (tsize == 1)
                {
                    try {
                    if (H5.H5Tequal(tid, H5.J2C(HDF5CDataTypes.JH5T_NATIVE_UCHAR)))
                        description = "8-bit unsigned character";
                    else if (H5.H5Tequal(tid, H5.J2C(HDF5CDataTypes.JH5T_NATIVE_CHAR)))
                        description = "8-bit character";
                    else if (tsign == HDF5Constants.H5T_SGN_NONE)
                        description = "8-bit unsigned integer";
                    else  description = "8-bit integer";
                    } catch (Exception ex) { description = "Unknown"; }
                }
                else if (tsize == 2)
                {
                    if (tsign == HDF5Constants.H5T_SGN_NONE)
                        description = "16-bit unsigned integer";
                    else
                        description = "16-bit integer";
                }
                else if (tsize == 4)
                {
                    if (tsign == HDF5Constants.H5T_SGN_NONE)
                        description = "32-bit unsigned integer";
                    else
                        description = "32-bit integer";
                }
                else if (tsize == 8)
                {
                    if (tsign == HDF5Constants.H5T_SGN_NONE)
                        description = "64-bit unsigned integer";
                    else
                        description = "64-bit integer";
                }
                break;
            case HDF5Constants.H5T_FLOAT:
                if (tsize == 4)
                {
                    description = "32-bit floating-point";
                }
                else if (tsize == 8)
                {
                    description = "64-bit floating-point";
                }
                break;
            case HDF5Constants.H5T_STRING:
                try {
                    description = "String, length="+H5.H5Tget_size(tid);
                }
                catch (Exception ex)
                {
                    description = "String";
                }
                break;
            case HDF5Constants.H5T_REFERENCE:
                description = "Object reference";
                break;
            case HDF5Constants.H5T_BITFIELD:
                description = "Bitfield";
                break;
            case HDF5Constants.H5T_ARRAY:
                description = "Array of ";
                // use the base datatype to define the array
                try {
                    description += getDatatypeDescription(H5.H5Tget_super(tid));
                } catch (Exception ex) {}
                break;
            case HDF5Constants.H5T_COMPOUND:
                description = "Compound";
                break;
            case HDF5Constants.H5T_VLEN:
                description = "Variable-length dataype";
                break;
            default:
                description = "Unknown";
                break;
        }

        return description;
    }

    public boolean isUnsigned()
    {
        return isUnsigned(toNative());
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

        try
        {
            int tsign = H5.H5Tget_sign(datatype);
            if (tsign == HDF5Constants.H5T_SGN_NONE)
                unsigned = true;
        } catch (Exception ex) {
            unsigned = false;
        }

        return unsigned;
    }

    public int toNative()
    {
        if (nativeType >=0 )
            return nativeType;

        int tid = -1;

        // figure the datatype
        try {
            switch (datatypeClass)
            {
                case CLASS_INTEGER:
                    if (datatypeSize == Datatype.NATIVE)
                    {
                        tid = H5.H5Tcopy(H5.J2C(HDF5CDataTypes.JH5T_NATIVE_INT));
                    } else
                    {
                        tid = H5.H5Tcopy(H5.J2C(HDF5CDataTypes.JH5T_NATIVE_INT8));
                        H5.H5Tset_size(tid, datatypeSize);
                    }

                    if (datatypeOrder == Datatype.ORDER_BE)
                        H5.H5Tset_order(tid, HDF5Constants.H5T_ORDER_BE);
                    else if (datatypeOrder == Datatype.ORDER_LE)
                        H5.H5Tset_order(tid, HDF5Constants.H5T_ORDER_LE);

                    if (datatypeSign == Datatype.SIGN_NONE)
                        H5.H5Tset_sign(tid, HDF5Constants.H5T_SGN_NONE);
                    break;
                case CLASS_FLOAT:
                    tid = H5.H5Tcopy(H5.J2C(HDF5CDataTypes.JH5T_NATIVE_FLOAT));
                    if (datatypeSize != Datatype.NATIVE)
                        H5.H5Tset_size(tid, datatypeSize);

                    if (datatypeOrder == Datatype.ORDER_BE)
                        H5.H5Tset_order(tid, HDF5Constants.H5T_ORDER_BE);
                    else if (datatypeOrder == Datatype.ORDER_LE)
                        H5.H5Tset_order(tid, HDF5Constants.H5T_ORDER_LE);

                    break;
                case CLASS_CHAR:
                    if (datatypeSign == Datatype.SIGN_NONE)
                        tid = H5.H5Tcopy(H5.J2C(HDF5CDataTypes.JH5T_NATIVE_UCHAR));
                    else
                        tid = H5.H5Tcopy(H5.J2C(HDF5CDataTypes.JH5T_NATIVE_CHAR));
                    break;
                case CLASS_STRING:
                    tid = H5.H5Tcopy(H5.J2C(HDF5CDataTypes.JH5T_C_S1));
                    H5.H5Tset_size(tid, datatypeSize);
                    H5.H5Tset_strpad(tid, HDF5Constants.H5T_STR_NULLPAD);
                    break;
                case CLASS_REFERENCE:
                    tid = H5.H5Tcopy(H5.J2C(HDF5CDataTypes.JH5T_STD_REF_OBJ));
                    break;
            } // switch (tclass)
        } catch (Exception ex) { tid = -1; }

        return (nativeType = tid);
    }
}
