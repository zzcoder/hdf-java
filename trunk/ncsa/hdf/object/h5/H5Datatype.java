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
 * Datatype encapsulates information of a datatype.
 * Information includes the class, size, endian of a datatype.
 * <p>
 * @version 1.0 05/07/2002
 * @author Peter X. Cao, NCSA
 */
public class H5Datatype extends Datatype
{
    /**
     * Create an Datatype with specified class, size, byte order and sign.
     * The following list a few example of how to create a Datatype.
     * <OL>
     * <LI>to create unsigned native integer<br>
     * H5Datatype type = new H5Dataype(CLASS_INTEGER, NATIVE, NATIVE, SIGN_NONE);
     * <LI>to create 16-bit signed integer with big endian<br>
     * H5Datatype type = new H5Dataype(CLASS_INTEGER, 2, ORDER_BE, NATIVE);
     * <LI>to create native float<br>
     * H5Datatype type = new H5Dataype(CLASS_FLOAT, NATIVE, NATIVE, -1);
     * <LI>to create 64-bit double<br>
     * H5Datatype type = new H5Dataype(CLASS_FLOAT, 8, NATIVE, -1);
     * </OL>
     * <p>
     * @param tclass the class of the datatype.
     * @param tsize the size of the datatype in bytes.
     * @param torder the order of the datatype.
     * @param tsign the sign of the datatype.
     */
    public H5Datatype(int tclass, int tsize, int torder, int tsign)
    {
        super(tclass, tsize, torder, tsign);
    }

    /**
     * Create a Datatype with a given HDF native datatype.
     * The following example constructs an H5Datatype from an HDF5 32-bit unsigned integer.
     * <pre>
     * int user_type = H5.J2C( HDF5CDataTypes.JH5T_NATIVE_UNINT32);
     * H5Datatype dtype = new H5atatype(user_type);
     * </pre>
     * which is equivalent to
     * <pre>
     * H5Datatype dtype = new H5atatype(CLASS_INTEGER, 4, NATIVE, SIGN_NONE);
     * </pre>
     * <p>
     * @param nativeID the hdf native datatype.
     */
    public H5Datatype(int nativeID)
    {
        super(nativeID);
    }

    /**
     * Translate HDF5 datatype identifier into H5Datatype.
     * The following example constructs an H5Datatype from an HDF5 32-bit unsigned integer.
     * <pre>
     * int user_type = H5.J2C( HDF5CDataTypes.JH5T_NATIVE_UNINT32);
     * H5Datatype dtype = new H5atatype(user_type);
     * </pre>
     * which is equivalent to
     * <pre>
     * H5Datatype dtype = new H5atatype(CLASS_INTEGER, 4, NATIVE, SIGN_NONE);
     * </pre>
     * <p>
     * @param nativeID the hdf native datatype.
     */
    public void fromNative(int tid)
    {
        nativeID = tid;

        int tclass=-1, tsize=-1;

        try
        {
            tclass = H5.H5Tget_class(tid);
            tsize = H5.H5Tget_size(tid);
        } catch (Exception ex)
        {
            datatypeClass = CLASS_NO_CLASS;
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
     * Allocate an one-dimensional array of byte, short, int, long, float, double,
     * or String to store data retrieved from an HDF5 file based on the given
     * HDF5 datatype and dimension sizes.
     * <p>
     * @param tid the datatype.
     * @param size the total size of the array.
     * @return the array object if successful and null otherwise.
     */
    public static Object allocateArray(int tid, int size) throws OutOfMemoryError
    {
        Object data = null;
        boolean isVL = false;
        boolean is_variable_str = false;

        if (size < 0) return null;

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

        try { is_variable_str = H5.H5Tis_variable_str(tid); } catch (Exception ex) {}
        try { isVL = (tclass==HDF5Constants.H5T_VLEN); } catch (Exception ex) {}

        if (is_variable_str || isVL)
        {
            data = new String[size];
            for (int i=0; i<size; i++) ((String[])data)[i] = "";
        }
        else if (tclass == HDF5Constants.H5T_INTEGER)
        {
            if (tsize == 1) data = new byte[size];
            else if (tsize == 2) data = new short[size];
            else if (tsize == 4) data = new int[size];
            else if (tsize == 8) data = new long[size];
        }
        else if (tclass == HDF5Constants.H5T_ENUM)
            data = new int[size];
        else if (tclass == HDF5Constants.H5T_FLOAT)
        {
            if (tsize == 4) data = new float[size];
            else if (tsize == 8) data = new double[size];
        }
        else if (tclass == HDF5Constants.H5T_STRING ||
            tclass == HDF5Constants.H5T_REFERENCE ||
            tclass == HDF5Constants.H5T_BITFIELD)
        {
            data = new byte[size*tsize];
        }
        else if (tclass == HDF5Constants.H5T_ARRAY)
        {
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
        }
        else {
            data = null;
        }

        return data;
    }

    /**
     * Return the HDF5 native datatype based on the HDF5 datatype on disk
     * For example, a HDF5 datatype created from<br>
     * <pre>
     * H5Dataype dtype = new H5Datatype(CLASS_INTEGER, 4, NATIVE, SIGN_NONE);
     * int type = dtype.toNative();
     * </pre>
     * here "type" will be the HDF5 datatype id of a 32-bit unsigned integer,
     * which is equivalent to
     * <pre>
     * int type = H5.J2C( HDF5CDataTypes.JH5T_NATIVE_UNINT32);
     * </pre>
     * <p>
     * @param tid the datatype on disk.
     * @return the native datatype if successful, and negative otherwise.
     */
    public static int toNative(int tid)
    {
        // data type information
        int native_type=-1;

        try {
            native_type = H5.H5Tget_native_type(tid);
        } catch (Exception ex) {}

        return native_type;
    }

    /**
     *  Returns the size of this datatype in bytes.
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

        if (tclass == HDF5Constants.H5T_INTEGER)
        {
            if (tsize == 1)
            {
                try {
                if (H5.H5Tequal(tid, HDF5Constants.H5T_NATIVE_UCHAR))
                    description = "8-bit unsigned character";
                else if (H5.H5Tequal(tid, HDF5Constants.H5T_NATIVE_CHAR))
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
        }
        else if (tclass == HDF5Constants.H5T_FLOAT)
        {
            if (tsize == 4)
            {
                description = "32-bit floating-point";
            }
            else if (tsize == 8)
            {
                description = "64-bit floating-point";
            }
        }
        else if (tclass == HDF5Constants.H5T_STRING)
        {
            try {
                if ( H5.H5Tis_variable_str(tid ))
                    description = "String, length = variable";
                else
                    description = "String, length = "+H5.H5Tget_size(tid);
            }
            catch (Exception ex)
            {
                description = "String";
            }
        }
        else if (tclass == HDF5Constants.H5T_REFERENCE)
        {
            description = "Object reference";
        }
        else if (tclass == HDF5Constants.H5T_BITFIELD)
        {
            description = "Bitfield";
        }
        else if (tclass == HDF5Constants.H5T_ENUM)
        {
            description = "enum";
            String enames = " (";
            int evalue[] = {-1};
            try {
                int n = H5.H5Tget_nmembers(tid );
                for (int i=0; i<n; i++)
                {
                    H5.H5Tget_member_value(tid, i, evalue);
                    enames += H5.H5Tget_member_name(tid, i);
                    enames += "="+evalue[0]+"  ";
                }
                enames += ")";
                description += enames;
            } catch (Exception ex) {}

        }
        else if (tclass == HDF5Constants.H5T_ARRAY)
        {
            description = "Array of ";
            // use the base datatype to define the array
            try {
                description += getDatatypeDescription(H5.H5Tget_super(tid));
            } catch (Exception ex) {}
        }
        else if (tclass == HDF5Constants.H5T_COMPOUND)
        {
            description = "Compound ";
            try {
                description += "{";
                int n = H5.H5Tget_nmembers(tid );
                int mtid = 0;
                try { H5.H5Tclose(mtid); } catch (Exception ex2) {;}
                for (int i=0; i<n; i++)
                {
                    mtid = H5.H5Tget_member_type(tid, i);
                    description += getDatatypeDescription(mtid) +", ";
                    try { H5.H5Tclose(mtid); } catch (Exception ex2) {;}
                }
                description += "}";
            } catch (Exception ex) {;}
        }
        else if (tclass == HDF5Constants.H5T_VLEN)
        {
            try { description = "Variable-length of " +getDatatypeDescription(H5.H5Tget_super(tid));}
            catch (Exception ex) {description = "Variable-length";}
        }
        else description = "Unknown";

        return description;
    }

    // implementing Datatype
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

    // implementing Datatype
    public int toNative()
    {

        if (nativeID >=0 )
            return nativeID;

        int tid = -1;

        // figure the datatype
        try {
            switch (datatypeClass)
            {
                case CLASS_INTEGER:
                    if (datatypeSize == 1)
                        tid = H5.H5Tcopy(HDF5Constants.H5T_NATIVE_INT8);
                    else if (datatypeSize == 2)
                        tid = H5.H5Tcopy(HDF5Constants.H5T_NATIVE_INT16);
                    else if (datatypeSize == 4)
                        tid = H5.H5Tcopy(HDF5Constants.H5T_NATIVE_INT32);
                    else if (datatypeSize == 8)
                        tid = H5.H5Tcopy(HDF5Constants.H5T_NATIVE_INT64);
                    else
                        tid = H5.H5Tcopy(HDF5Constants.H5T_NATIVE_INT);

                    if (datatypeOrder == Datatype.ORDER_BE)
                        H5.H5Tset_order(tid, HDF5Constants.H5T_ORDER_BE);
                    else if (datatypeOrder == Datatype.ORDER_LE)
                        H5.H5Tset_order(tid, HDF5Constants.H5T_ORDER_LE);

                    if (datatypeSign == Datatype.SIGN_NONE)
                        H5.H5Tset_sign(tid, HDF5Constants.H5T_SGN_NONE);
                    break;
                case CLASS_FLOAT:
                    if (datatypeSize == 8)
                        tid = H5.H5Tcopy(HDF5Constants.H5T_NATIVE_DOUBLE);
                    else
                        tid = H5.H5Tcopy(HDF5Constants.H5T_NATIVE_FLOAT);

                    if (datatypeOrder == Datatype.ORDER_BE)
                        H5.H5Tset_order(tid, HDF5Constants.H5T_ORDER_BE);
                    else if (datatypeOrder == Datatype.ORDER_LE)
                        H5.H5Tset_order(tid, HDF5Constants.H5T_ORDER_LE);

                    break;
                case CLASS_CHAR:
                    if (datatypeSign == Datatype.SIGN_NONE)
                        tid = H5.H5Tcopy(HDF5Constants.H5T_NATIVE_UCHAR);
                    else
                        tid = H5.H5Tcopy(HDF5Constants.H5T_NATIVE_CHAR);
                    break;
                case CLASS_STRING:
                    tid = H5.H5Tcopy(HDF5Constants.H5T_C_S1);
                    H5.H5Tset_size(tid, datatypeSize);
                    H5.H5Tset_strpad(tid, HDF5Constants.H5T_STR_NULLPAD);
                    break;
                case CLASS_REFERENCE:
                    tid = H5.H5Tcopy(HDF5Constants.H5T_STD_REF_OBJ);
                    break;
            } // switch (tclass)
        } catch (Exception ex) { tid = -1; }

        return (nativeID = tid);
    }
}
