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
import ncsa.hdf.hdf5lib.*;
import ncsa.hdf.hdf5lib.exceptions.*;

/**
 * The H5Accessory class provides common static methods to HDF5 objects.
 * <p>
 * @version 1.0 12/12/2001
 * @author Peter X. Cao, NCSA
 */
public final class H5Accessory
{
    /**
     * Returns a list of attriubtes for the given object location.
     * <p>
     * @param objID the object identifier.
     * @retun the list of attriubtes of the object.
     */
    public static List getAttribute(int objID) throws HDF5Exception
    {
        List attributeList = null;

        int aid=-1, sid=-1, tid=-1, n=0;

        n = H5.H5Aget_num_attrs(objID);
        if (n <= 0) return null; // no attribute attached to this object

        attributeList = new Vector(n, 5);
        for (int i=0; i<n; i++)
        {
            try
            {
                aid = H5.H5Aopen_idx(objID, i);
                sid = H5.H5Aget_space(aid);
                long dims[] = null;
                int rank = H5.H5Sget_simple_extent_ndims(sid);
                if (rank == 0)
                {
                    // for scalar data, rank=0
                    rank = 1;
                    dims = new long[1];
                    dims[0] = 1;
                }
                else
                {
                    dims = new long[rank];
                    H5.H5Sget_simple_extent_dims(sid, dims, null);
                }

                String[] nameA = {""};
                H5.H5Aget_name(aid, 80, nameA);

                tid = H5.H5Aget_type(aid);
                int nativeType = toNativeType(tid);
                Attribute attr = new Attribute(nameA[0], nativeType, dims);

                long lsize = 1;
                for (int j=0; j<dims.length; j++)
                    j *= dims[j];

                Object value = allocateArray(nativeType, (int)lsize);

                if (H5.H5Tget_class(nativeType)==HDF5Constants.H5T_ARRAY)
                    H5.H5Aread(aid, toNativeType(H5.H5Tget_super(nativeType)), value);
                else
                    H5.H5Aread(aid, nativeType, value);

                int typeClass = H5.H5Tget_class(nativeType);
                if (typeClass==HDF5Constants.H5T_STRING)
                    value = Dataset.byteToString((byte[])value, H5.H5Tget_size(nativeType));
                else if (typeClass == HDF5Constants.H5T_REFERENCE)
                    value = HDFNativeData.byteToLong((byte[])value);

                attr.setValue(value);
                attributeList.add(attr);
            } catch (HDF5Exception ex) {}

            try { H5.H5Tclose(tid); } catch (HDF5Exception ex) {}
            try { H5.H5Sclose(sid); } catch (HDF5Exception ex) {}
            try { H5.H5Aclose(aid); } catch (HDF5Exception ex) {}
        }

        return attributeList;
    }

    /**
     * Creates a new attribute and attached to the object if attribute does
     * not exist. Otherwise, just update the value of the attribute.
     *
     * <p>
     * @param objID the object identifier.
     * @param attr the atribute to attach.
     * @param attrExisted The indicator if the given attribute exists.
     * @return true if successful and false otherwise.
     */
    public static boolean writeAttribute(int objID, Attribute attr,
        boolean attrExisted) throws HDF5Exception
    {
        String name = attr.getName();
        int tid=-1, sid=-1, aid=-1;

        try
        {
            tid = H5.H5Tcopy(attr.getType());
            sid = H5.H5Screate_simple(attr.getRank(), attr.getDataDims(), null);

            if (attrExisted)
                aid = H5.H5Aopen_name(objID, name);
            else
                aid = H5.H5Acreate(objID, name, tid, sid, HDF5Constants.H5P_DEFAULT);

            // update value of the attribute
            H5.H5Awrite(aid, tid, attr.getValue());
        } finally
        {
            try { H5.H5Tclose(tid); } catch (HDF5Exception ex) {}
            try { H5.H5Sclose(sid); } catch (HDF5Exception ex) {}
            try { H5.H5Aclose(aid); } catch (HDF5Exception ex) {}
        }

        return true;
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

}
