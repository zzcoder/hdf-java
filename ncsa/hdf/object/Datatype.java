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
    final static public int NATIVE = 10000;
    final static public int CLASS_INTEGER = 10001;
    final static public int CLASS_FLOAT = 10002;
    final static public int CLASS_CHAR = 10003;
    final static public int CLASS_STRING = 10004;
    final static public int ORDER_BE = 10010;
    final static public int ORDER_LE = 10011;
    final static public int SIGN_NONE = 10020;

    /**
     * The class of the datatype.
     */
    private int datatypeClass;

    /**
     * The size of the datatype.
     */
    private int datatypeSize;

    /**
     * The byte order of the datatype.
     */
    private int datatypeOrder;

    /**
     * The sign of the datatype.
     */
    private int datatypeSign;

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
     * Converts this datatype to native HDF datatype.
     */
    public abstract int toNative();
}
