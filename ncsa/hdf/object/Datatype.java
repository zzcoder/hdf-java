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

import java.util.List;

/**
 * Datatype encapsulates information of a datatype.
 * Information includes the class, size, endian of a datatype.
 * <p>
 * @version 1.0 05/07/2002
 * @author Peter X. Cao, NCSA
 */
public abstract class Datatype extends HObject
{
    /* native for datatype size, order, and sign */
    final static public int NATIVE = -1;

    /* Classes of datatypes */
    final static public int CLASS_NO_CLASS         = -1;
    final static public int CLASS_INTEGER          = 0;
    final static public int CLASS_FLOAT            = 1;
    final static public int CLASS_CHAR             = 2;
    final static public int CLASS_STRING           = 3;
    final static public int CLASS_BITFIELD         = 4;
    final static public int CLASS_OPAQUE           = 5;
    final static public int CLASS_COMPOUND         = 6;
    final static public int CLASS_REFERENCE        = 7;
    final static public int CLASS_ENUM             = 8;
    final static public int CLASS_VLEN             = 9;
    final static public int CLASS_ARRAY            = 10;

    /* Byte orders of datatype */
    final static public int ORDER_LE         = 0;
    final static public int ORDER_BE         = 1;
    final static public int ORDER_VAX        = 2;
    final static public int ORDER_NONE       = 3;

    /* Sign of integers */
    final static public int SIGN_NONE         = 0;
    final static public int SIGN_2            = 1;
    final static public int NSGN             = 2;

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
     * The name=values pairs of enum members
     */
    protected String enumMembers;

    /**
     * Datatype identifier of the implementing class of this datatype.
     */
    protected int nativeID;

    /**
     * Contructs a named datatype with a given file, name and path.
     * <p>
     * @param fileFormat the HDF file.
     * @param name the name of the datatype.
     * @param path the full path of the datatype.
     * @param oid the unique identifier of the datatype.
     */
    public Datatype(
        FileFormat fileFormat,
        String name,
        String path,
        long[] oid)
    {
        super (fileFormat, name, path, oid);
    }


    /**
     * Constructs a Datatype with specified class, size, byte order and sign.
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
     * @param tclass the class of the datatype, e.g. CLASS_INTEGER, CLASS_FLOAT and etc.
     * @param tsize the size of the datatype in bytes, e.g. for a 32-bit integer, the size is 4.
     * @param torder the order of the datatype. Valid values are ORDER_LE, ORDER_BE, ORDER_VAX and ORDER_NONE
     * @param tsign the sign of the datatype. Valid values are SIGN_NONE, SIGN_2 and MSGN
     */
    public Datatype(int tclass, int tsize, int torder, int tsign)
    {
        datatypeClass = tclass;
        datatypeSize = tsize;
        datatypeOrder = torder;
        datatypeSign = tsign;
        nativeID = -1;
        enumMembers = null;
    }

    /**
     * Constructs a Datatype with a given native datatype identifier.
     * <p>
     * For example, if the datatype identifier is a 32-bit unsigned integer created
     * from HDF5,
     * <pre>
     * int tid = H5.H5Tcopy( HDF5Constants.H5T_NATIVE_UNINT32);
     * Datatype dtype = new Datatype(tid);
     * </pre>
     * will construct a datatype equivalent to
     * new Datatype(CLASS_INTEGER, 4, NATIVE, SIGN_NONE);
     * <p>
     * @see #fromNative(int nativeID)
     * @param type the native datatype identifier.
     */
    public Datatype(int type)
    {
        this(CLASS_NO_CLASS, NATIVE, NATIVE, NATIVE);

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
     * Sets the name=values pairs of enum members.
     * <p>For Example,
     * <pre>
     *     setEnumMembers("lowTemp=-40, highTemp=90")
     *         will set the value of enum member lowTemp to -40 and highTemp to 90.
     *     setEnumMembers("lowTemp, highTemp")
     *         will set enum members to defaults, i.e. lowTemp=0 and highTemp=1
     *     setEnumMembers("lowTemp=10, highTemp")
     *         will set enum member lowTemp to 10 and highTemp to 11.
     * </pre>
     * @param enumStr the name=values pairs of enum members
     */
    public final void setEnumMembers(String enumStr) { enumMembers = enumStr; }

    /**
     * Gets the name=values pairs of enum members.
     * <p>For Example,
     * <pre>
     *     setEnumMembers("lowTemp=-40, highTemp=90")
     *         will set the value of enum member lowTemp to -40 and highTemp to 90.
     *     setEnumMembers("lowTemp, highTemp")
     *         will set enum members to defaults, i.e. lowTemp=0 and highTemp=1
     *     setEnumMembers("lowTemp=10, highTemp")
     *         will set enum member lowTemp to 10 and highTemp to 11.
     * </pre>
     * @return the name=values pairs of enum members
     */
    public final String getEnumMembers() { return enumMembers; }

    /**
     * Converts this datatype to a native datatype.
     *
     * Subclasses must implement it so that this datatype will be converted accordingly.
     * <p>
     * For example, a HDF5 datatype created from<br>
     * <pre>
     * H5Dataype dtype = new H5Datatype(CLASS_INTEGER, 4, NATIVE, SIGN_NONE);
     * int tid = dtype.toNative();
     * </pre>
     * There "tid" will be the HDF5 datatype id of a 32-bit unsigned integer,
     * which is equivalent to
     * <pre>
     * int tid = H5.H5Tcopy( HDF5Constants.H5T_NATIVE_UNINT32);
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
     * int tid = H5.H5Tcopy( HDF5Constants.H5T_NATIVE_UNINT32);
     * Datatype dtype = new Datatype(tid);
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
    public String getDatatypeDescription()
    {
        String description = "Unknown";

        switch (datatypeClass)
        {
            case CLASS_INTEGER:
                if (datatypeSign == SIGN_NONE)
                    description = String.valueOf(datatypeSize*8) + "-bit unsigned integer";
                else
                    description = String.valueOf(datatypeSize*8) + "-bit integer";
                break;
            case CLASS_FLOAT:
                description = String.valueOf(datatypeSize*8) + "-bit floating-point";
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

    /**
     *  Checks if this datatype is an unsigned integer.
     *  @return True is the datatype is an unsigned integer; otherwise returns false.
     */
    public abstract boolean isUnsigned();

    /** Opens a named datatype. Sub-clases must replace this default implementation */
    public int open() { return -1; }

    /** Closes a named datatype. Sub-clases must replace this default implementation */
    public void close(int id) {};

    /**
     * Read the metadata such as attributes from file into memory if the metadata
     * is not in memory. If the metadata is in memory, it returns the metadata.
     * The metadata is stored as a collection of metadata in a List.
     *<p>
     * Sub-clases must replace this default implementation
     *
     * @return the list of metadata objects.
     * @see <a href="http://java.sun.com/j2se/1.5.0/docs/api/java/util/List.html">java.util.List</a>
     */
    public List getMetadata() throws Exception { return null; }

    /**
     * Writes a specific metadata into file. If the metadata exists, it
     * updates its value. If the metadata does not exists in file, it creates
     * the metadata and attaches it to the object.
     *<p>
     * Sub-clases must replace this default implementation
     *
     * @param info the metadata to write.
     */
    public void writeMetadata(Object info) throws Exception {;}

    /**
     * Deletes an existing metadata from this data object.
     *<p>
     * Sub-clases must replace this default implementation
     *
     * @param info the metadata to delete.
     */
    public void removeMetadata(Object info) throws Exception {;}

}
