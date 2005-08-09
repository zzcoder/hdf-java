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

package ncsa.hdf.srb.obj;

import ncsa.hdf.object.*;

public class H5SrbDatatype extends Datatype
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
    public H5SrbDatatype(int tclass, int tsize, int torder, int tsign)
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
    public H5SrbDatatype(int nativeID)
    {
        super(nativeID);
    }

    /**
     * Converts this datatype to a user defined datatype.
     * Subclasses must implement it so that this datatype will be converted.
     * <p>
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
     *
     * @return the identifier of the user defined datatype.
     */
    public int toNative() { return -1; }

    /**
     * Specify this datatype with a given id of a user defined datatype.
     * Subclasses must implement it so that this datatype will be converted.
     * <p>
     * For example, if the type identifier is a 32-bit unsigned integer created
     * from HDF5,
     * <pre>
     * int user_type = H5.J2C( HDF5CDataTypes.JH5T_NATIVE_UNINT32);
     * Datatype dtype = new Datatype(user_type);
     * </pre>
     * will construct a datatype equivalent to
     * new Datatype(CLASS_INTEGER, 4, NATIVE, SIGN_NONE);
     * <p>
     * @param nativeID the identifier of user defined datatype.
     */
    public void fromNative(int nativeID) {
        datatypeClass = nativeID;
    }

    /**
     *  Checks if this datatype is an unsigned integer.
     *  @return True is the datatype is an unsigned integer; otherwise returns false.
     */
    public boolean isUnsigned() { return false; }

    /**
     *  Returns a short text description of this datatype.
     */
    public String getDatatypeDescription()
    {
        String description = "Unknown";

        switch (datatypeClass)
        {
            case CLASS_INTEGER:
                description = "Integer";
                break;
            case CLASS_FLOAT:
                description = "Float";
                break;
            case CLASS_STRING:
                description = "String";
                break;
            case CLASS_REFERENCE:
                description = "Object reference";
                break;
            case CLASS_BITFIELD:
                description = "Bitfield";
                break;
            case CLASS_ENUM:
                description = "enum";
                break;
            case CLASS_ARRAY:
                description = "Array";
                break;
            case CLASS_COMPOUND:
                description = "Compound ";
                break;
            case CLASS_VLEN:
                description = "Variable-length";
                break;
            default:
                description = "Unknown";
                break;
        }

        return description;
    }

}
