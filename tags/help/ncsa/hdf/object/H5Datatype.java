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

import ncsa.hdf.hdf5lib.*;

/**
 * Datatype holds a (name, value) pair of HDF4/5 attribute.
 * <p>
 * @version 1.0 05/07/2002
 * @author Peter X. Cao, NCSA
 */
public class H5Datatype extends Datatype
{
    final static public int CLASS_REFERENCE = HDF5Constants.H5T_REFERENCE;

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
     * Allocate a one-dimensional array of byte, short, int, long, float, double,
     * or String to store data retrieved from an HDF5 file based on the given
     * HDF5 datatype and dimension sizes.
     * <p>
     * @param tid the datatype.
     * @param size the total size of the array.
     * @return the array object if successful and null otherwise.
     */
    public static Object allocateArray(int tid, int size)
    {
        Object data = null;

        if (size < 0)
            return null;

        // Scalar members have dimensionality zero, i.e. size =0
        // what can we do about it, set the size to 1
        if (size == 0) size = 1;

        // data type information
        int typeClass=-1, typeSize=-1, typeSign=-1;

        try
        {
            typeClass = H5.H5Tget_class(tid);
            typeSize = H5.H5Tget_size(tid);
            typeSign = H5.H5Tget_sign(tid);
        } catch (Exception ex) {}

        switch (typeClass)
        {
            case HDF5Constants.H5T_INTEGER:
                if (typeSize == 1)
                {
                    data = new byte[size];
                }
                else if (typeSize == 2)
                {
                    data = new short[size];
                }
                else if (typeSize == 4)
                {
                    data = new int[size];
                }
                else if (typeSize == 8)
                {
                    data = new long[size];
                }
                break;
            case HDF5Constants.H5T_FLOAT:
                if (typeSize == 4)
                {
                    data = new float[size];
                }
                else if (typeSize == 8)
                {
                    data = new double[size];
                }
                break;
            case HDF5Constants.H5T_STRING:
            case HDF5Constants.H5T_REFERENCE:
            case HDF5Constants.H5T_BITFIELD:
                data = new byte[size*typeSize];
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
        int native_type=-1, typeClass=-1, typeSize=-1, typeSign=-1;

        try
        {
            typeClass = H5.H5Tget_class(tid);
            typeSize = H5.H5Tget_size(tid);
            typeSign = H5.H5Tget_sign(tid);
        } catch (Exception ex) {}

        switch (typeClass)
        {
            case HDF5Constants.H5T_INTEGER:
                if (typeSize == 1) {
                    if (typeSign == HDF5Constants.H5T_SGN_NONE)
                        native_type = H5.J2C(
                            HDF5CDataTypes.JH5T_NATIVE_UINT8);
                    else
                        native_type = H5.J2C(
                            HDF5CDataTypes.JH5T_NATIVE_INT8);
                }
                else if (typeSize == 2) {
                    if (typeSign == HDF5Constants.H5T_SGN_NONE)
                        native_type = H5.J2C(
                            HDF5CDataTypes.JH5T_NATIVE_UINT16);
                    else
                        native_type = H5.J2C(
                            HDF5CDataTypes.JH5T_NATIVE_INT16);
                }
                else if (typeSize == 4) {
                    if (typeSign == HDF5Constants.H5T_SGN_NONE)
                        native_type = H5.J2C(
                            HDF5CDataTypes.JH5T_NATIVE_UINT32);
                    else
                        native_type = H5.J2C(
                            HDF5CDataTypes.JH5T_NATIVE_INT32);
                }
                else if (typeSize == 8) {
                    if (typeSign == HDF5Constants.H5T_SGN_NONE)
                        native_type = H5.J2C(
                            HDF5CDataTypes.JH5T_NATIVE_UINT64);
                    else
                        native_type = H5.J2C(
                            HDF5CDataTypes.JH5T_NATIVE_INT64);
                }
                break;
            case HDF5Constants.H5T_FLOAT:
                if (typeSize == 4)
                {
                    native_type = H5.J2C(
                        HDF5CDataTypes.JH5T_NATIVE_FLOAT);
                }
                else if (typeSize == 8) {
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
    public static final String getDatatypeDescription(int tid)
    {
        String description = "Unknown";

        // data type information
        int typeClass=-1, typeSize=-1, typeSign=-1;

        try
        {
            typeClass = H5.H5Tget_class(tid);
            typeSize = H5.H5Tget_size(tid);
            typeSign = H5.H5Tget_sign(tid);
        } catch (Exception ex) {}

        switch (typeClass)
        {
            case HDF5Constants.H5T_INTEGER:
                if (typeSize == 1)
                {
                    try {
                    if (H5.H5Tequal(tid, H5.J2C(HDF5CDataTypes.JH5T_NATIVE_UCHAR)))
                        description = "8-bit unsigned character";
                    else if (H5.H5Tequal(tid, H5.J2C(HDF5CDataTypes.JH5T_NATIVE_CHAR)))
                        description = "8-bit character";
                    else if (typeSign == HDF5Constants.H5T_SGN_NONE)
                        description = "8-bit unsigned integer";
                    else  description = "8-bit integer";
                    } catch (Exception ex) { description = "Unknown"; }
                }
                else if (typeSize == 2)
                {
                    if (typeSign == HDF5Constants.H5T_SGN_NONE)
                        description = "16-bit unsigned integer";
                    else
                        description = "16-bit integer";
                }
                else if (typeSize == 4)
                {
                    if (typeSign == HDF5Constants.H5T_SGN_NONE)
                        description = "32-bit unsigned integer";
                    else
                        description = "32-bit integer";
                }
                else if (typeSize == 8)
                {
                    if (typeSign == HDF5Constants.H5T_SGN_NONE)
                        description = "64-bit unsigned integer";
                    else
                        description = "64-bit integer";
                }
                break;
            case HDF5Constants.H5T_FLOAT:
                if (typeSize == 4)
                {
                    description = "32-bit floating-point";
                }
                else if (typeSize == 8)
                {
                    description = "64-bit floating-point";
                }
                break;
            case HDF5Constants.H5T_STRING:
                description = "String";
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
            int typeSign = H5.H5Tget_sign(datatype);
            if (typeSign == HDF5Constants.H5T_SGN_NONE)
                unsigned = true;
        } catch (Exception ex) {
            unsigned = false;
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
        try {
            switch (tclass)
            {
                case CLASS_INTEGER:
                    if (tsize == Datatype.NATIVE)
                    {
                        tid = H5.H5Tcopy(H5.J2C(HDF5CDataTypes.JH5T_NATIVE_INT));
                    } else
                    {
                        tid = H5.H5Tcopy(H5.J2C(HDF5CDataTypes.JH5T_NATIVE_INT8));
                        H5.H5Tset_size(tid, tsize);
                    }
                    if (torder == Datatype.ORDER_BE)
                        H5.H5Tset_order(tid, HDF5Constants.H5T_ORDER_BE);
                    else if (torder == Datatype.ORDER_LE)
                        H5.H5Tset_order(tid, HDF5Constants.H5T_ORDER_LE);
                    if (tsign == Datatype.SIGN_NONE)
                        H5.H5Tset_sign(tid, HDF5Constants.H5T_SGN_NONE);
                    break;
                case CLASS_FLOAT:
                    tid = H5.H5Tcopy(H5.J2C(HDF5CDataTypes.JH5T_NATIVE_FLOAT));
                    if (tsize != Datatype.NATIVE)
                        H5.H5Tset_size(tid, tsize);
                    if (torder == Datatype.ORDER_BE)
                        H5.H5Tset_order(tid, HDF5Constants.H5T_ORDER_BE);
                    else if (torder == Datatype.ORDER_LE)
                        H5.H5Tset_order(tid, HDF5Constants.H5T_ORDER_LE);
                    break;
                case CLASS_CHAR:
                    if (tsign == Datatype.SIGN_NONE)
                        tid = H5.H5Tcopy(H5.J2C(HDF5CDataTypes.JH5T_NATIVE_UCHAR));
                    else
                        tid = H5.H5Tcopy(H5.J2C(HDF5CDataTypes.JH5T_NATIVE_CHAR));
                    break;
                case CLASS_STRING:
                    tid = H5.H5Tcopy(H5.J2C(HDF5CDataTypes.JH5T_C_S1));
                    H5.H5Tset_size(tid, tsize);
                    H5.H5Tset_strpad(tid, HDF5Constants.H5T_STR_NULLPAD);
                    break;
                case CLASS_REFERENCE:
                    tid = H5.H5Tcopy(H5.J2C(HDF5CDataTypes.JH5T_STD_REF_OBJ));
                    break;
            } // switch (tclass)
        } catch (Exception ex) { tid = -1; }

        return tid;
    }
}
