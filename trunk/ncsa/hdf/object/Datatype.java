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
 * Datatype encapsulates information of a datatype.
 * Information includes the class, size, endian of a datatype.
 * <p>
 * @version 1.0 05/07/2002
 * @author Peter X. Cao, NCSA
 */
public abstract class Datatype
{
    /** integer datatype: byte, short, int, long */
    final static public int CLASS_INTEGER = 0;

    /** float datatype: float, double */
    final static public int CLASS_FLOAT = 1;

    /** char datatype: char */
    final static public int CLASS_CHAR = 2;

    /** string datatype: String */
    final static public int CLASS_STRING = 3;

    /** object reference: for HDF5 object reference*/
    final static public int CLASS_REFERENCE = 7;

    /** unknown datatype*/
    final static public int CLASS_ERROR = -1;

    /** default data size, endian, order */
    final static public int NATIVE = 10;

    /** big endian byte order*/
    final static public int ORDER_BE = 11;

    /** little endian byte order */
    final static public int ORDER_LE = 12;

    /** unsigned integer*/
    final static public int SIGN_NONE = 13;

    /**
     * The class of the datatype.
     */
    protected int datatypeClass;

    /**
     * The size (in bytes)  of the datatype.
     */
    protected int datatypeSize;

    /**
     * The byte order of the datatype.
     */
    protected int datatypeOrder;

    /**
     * The sign of the datatype.
     */
    protected int datatypeSign;

    /**
     * Datatype identifier of the implementing class of this datatype.
     */
    protected int nativeID;


    /**
     * Create an Datatype with specified class, size, byte order and sign.
     * The following list a few example of how to create a Datatype.
     * <OL>
     * <LI>to create unsigned native integer<br>
     * Datatype type = new Dataype(CLASS_INTEGER, NATIVE, NATIVE, SIGN_NONE);
     * <LI>to create 16-bit signed integer with big endian<br>
     * Datatype type = new Dataype(CLASS_INTEGER, 2, ORDER_BE, NATIVE);
     * <LI>to create native float<br>
     * Datatype type = new Dataype(CLASS_FLOAT, NATIVE, NATIVE, -1);
     * <LI>to create 64-bit double<br>
     * Datatype type = new Dataype(CLASS_FLOAT, 8, NATIVE, -1);
     * </OL>
     * @param tclass the class of the datatype.
     * @param tsize the size of the datatype in byptes.
     * @param torder the order of the datatype.
     * @param tsign the sign of the datatype.
     */
    public Datatype(int tclass, int tsize, int torder, int tsign)
    {
        datatypeClass = tclass;
        datatypeSize = tsize;
        datatypeOrder = torder;
        datatypeSign = tsign;
        nativeID = -1;
    }

    /**
     * Create a Datatype with a given identifier of user defined datatype.
     * For example, if the type identifier is a 32-bit unsigned integer created
     * from HDF5,
     * <pre>
     * int user_type = H5.J2C( HDF5CDataTypes.JH5T_NATIVE_UNINT32);
     * Datatype dtype = new Datatype(user_type);
     * </pre>
     * will construct a datatype equivalent to
     * new Datatype(CLASS_INTEGER, 4, NATIVE, SIGN_NONE);
     * <p>
     * @see #fromNative(int nativeID)
     * @param type the identifier of user defined datatype.
     */
    public Datatype(int type)
    {
        this(CLASS_ERROR, NATIVE, NATIVE, NATIVE);

        nativeID = type;
        fromNative(nativeID);
    }

    /**
     * Returns the class of the datatype.
     */
    public int getDatatypeClass()
    {
        return datatypeClass;
    }

    /**
     * Returns the size of the datatype.
     */
    public int getDatatypeSize()
    {
        return datatypeSize;
    }

    /**
     * Returns the Order of the datatype.
     */
    public int getDatatypeOrder()
    {
        return datatypeOrder;
    }

    /**
     * Returns the sign of the datatype.
     */
    public int getDatatypeSign()
    {
        return datatypeSign;
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
    public abstract int toNative();

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
    public abstract void fromNative(int nativeID);

    /**
     *  Returns a short text description of this datatype.
     */
    public abstract String getDatatypeDescription();

    /**
     *  Checks if this datatype is an unsigned integer.
     *  @return True is the datatype is an unsigned integer; otherwise returns false.
     */
    public abstract boolean isUnsigned();

}
