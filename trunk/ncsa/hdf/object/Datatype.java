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
 * Datatype holds a (name, value) pair of HDF4/5 attribute.
 * <p>
 * @version 1.0 05/07/2002
 * @author Peter X. Cao, NCSA
 */
public abstract class Datatype
{
    final static public int CLASS_INTEGER = 0;
    final static public int CLASS_FLOAT = 1;
    final static public int CLASS_CHAR = 2;
    final static public int CLASS_STRING = 3;
    final static public int CLASS_REFERENCE = 7;
    final static public int CLASS_ERROR = -1;

    final static public int NATIVE = 10;
    final static public int ORDER_BE = 11;
    final static public int ORDER_LE = 12;
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
     * The native datatype.
     */
    protected int nativeType;


    /**
     * Create an Datatype with specified class, size, byte order and sign.
     * <p>
     * @param tclass the class of the datatype.
     * @param tsize the size of the datatype.
     * @param torder the order of the datatype.
     * @param tsign the sign of the datatype.
     */
    public Datatype(int tclass, int tsize, int torder, int tsign)
    {
        datatypeClass = tclass;
        datatypeSize = tsize;
        datatypeOrder = torder;
        datatypeSign = tsign;
        nativeType = -1;
    }

    /**
     * Create a Datatype with a given HDF native datatype.
     * <p>
     * @param nativeType the hdf native datatype.
     */
    public Datatype(int type)
    {
        datatypeClass = CLASS_ERROR;
        datatypeSize = NATIVE;
        datatypeOrder = NATIVE;
        datatypeSign = NATIVE;
        nativeType = type;

        fromNative(nativeType);
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
     * Converts this datatype to HDF native datatype.
     */
    public abstract int toNative();

    /**
     * Specify this datatype with a given HDF native datatype.
     */
    public abstract void fromNative(int nativeType);

    /**
     *  Returns the short description of this datatype.
     */
    public abstract String getDatatypeDescription();

    /**
     *  Checks if this datatype is an unsigned integer.
     *  @return True is the datatype is an unsigned integer; otherwise returns false.
     */
    public abstract boolean isUnsigned();

}
