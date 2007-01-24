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
import ncsa.hdf.hdf5lib.exceptions.*;
import ncsa.hdf.object.*;

import java.util.*;

/**
 * H5Datatype defines methods to map between common Datatype object and a specific HDF5 dataype identifier.
 * <p>
 * This class provides several methods to convert an HDF5 dataype identifier to
 * a dataype object, and vice versa. A dataype object is described by four basic fields: 
 * datatype class, size, byte order, and sign while an HDF5 dataype is presented by a datetype
 * identifier. 
 * <p>
 * @version 1.0 05/07/2002
 * @author Peter X. Cao, NCSA
 */
public class H5Datatype extends Datatype
{
	public static final long serialVersionUID = HObject.serialVersionUID;

   /**
     * The list of attributes of this data object.
     */
     private List attributeList;

     /** Flag to indicate if this datatype is a named datatype */
     private boolean isNamed=false;

     /**
      * Constrcuts an H5Datatype object for a given file, dataset name, group path
      * and object identifer.
      * <p>
      * The datatype object represents an existing named datatype in file. For example, 
      * new H5Datatype(file, "dtype1", "/g0/") constructs a datatype object that corresponds to
      * the dataset,"dset1", at group "/g0/".
      * <p>
      * The object identifier is a one-element long array that holds the object reference of
      * the dataset. For a given file, object reference uniquely identifies the object.
      * <p>
      * For a given name and path, the object ID is obtained from
      * byte[] ref_buf = H5.H5Rcreate(fid, path+name, HDF5Constants.H5R_OBJECT, -1);
      * <p>
      * @param fileFormat the file that contains the dataset.
      * @param name the name of the dataset such as "dset1".
      * @param path the group path to the dataset such as "/g0/".
      * @param oid the unique identifier of this data object. if oid is null, the object ID 
      *        is automatically obtained by H5.H5Rcreate() for the given name and path.
      */
    public H5Datatype(
        FileFormat fileFormat,
        String name,
        String path,
        long[] oid)
    {
        super (fileFormat, name, path, oid);

        try
        {
            nativeID = H5.H5Topen(getFID(), getPath()+getName());
            fromNative(nativeID);
            hasAttribute = (H5.H5Aget_num_attrs(nativeID)>0);
            isNamed = true;
            H5.H5Tclose(nativeID);
            nativeID = -1;
        } catch (HDF5Exception ex) {}
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
    public H5Datatype(int tclass, int tsize, int torder, int tsign)
    {
        super(tclass, tsize, torder, tsign);
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
     * @param nativeID the native datatype identifier.
     */
    public H5Datatype(int nativeID)
    {
        super(nativeID);
    }
    
    /**
     * Specify this datatype with a given id of a user defined datatype.
     * Subclasses must implement it so that this datatype will be converted.
     * <p>
     * For example, if the type identifier is a 32-bit unsigned integer
     * <pre>
     * int tid = H5.H5Tcopy( HDF5Constants.H5T_NATIVE_UNINT32);
     * Datatype dtype = new Datatype(tid);
     * </pre>
     * will construct a datatype equivalent to
     * new Datatype(CLASS_INTEGER, 4, NATIVE, SIGN_NONE);
     * <p>
     * @param tid the identifier of user defined datatype.
     */
    public void fromNative(int tid)
    {
        nativeID = tid;

        int tclass=-1, tsize=-1;

        try
        {
            tclass = H5.H5Tget_class(tid);
            if (tclass == HDF5Constants.H5T_ARRAY)
                tclass = H5.H5Tget_class(H5.H5Tget_super(tid));
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
        else if (tclass == HDF5Constants.H5T_ENUM)
        {
            datatypeClass = CLASS_ENUM;
            try {
                int nMember = H5.H5Tget_nmembers(tid);
                String name = null;
                int[] val = new int[1];
                String enumStr = "(";
                for (int i=0; i<nMember; i++)
                {
                    name = H5.H5Tget_member_name(tid, i);
                    H5.H5Tget_member_value(tid, i, val);
                    enumStr += name+"="+val[0]+", ";
                }
                enumMembers = enumStr+"0";
            } catch (Exception ex) {}
        }

        datatypeSize = tsize;
        datatypeOrder = NATIVE;
    }

    /**
     * Allocates an one-dimensional array of byte, short, int, long, float, double,
     * or String to store data retrieved from file based on the given datatype and
     * dimension sizes.
     *
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
        else if (tclass == HDF5Constants.H5T_ENUM) {
            // can be any integer
            // data = new int[size];
            try { data =  allocateArray ( H5.H5Tget_super(tid), size); }
            catch (Exception ex) {}
        }
        else if (tclass == HDF5Constants.H5T_FLOAT)
        {
            if (tsize == 4) data = new float[size];
            else if (tsize == 8) data = new double[size];
        }
        else if ( tclass == HDF5Constants.H5T_STRING ||
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
     * Specify this datatype with a given id of a user defined datatype.
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
     * @param tid the identifier of user defined datatype.
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
     *
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

    /**
     *  Returns the short description of the datatype.
     */
    public String getDatatypeDescription()
    {
        return getDatatypeDescription(toNative());
    }

    /**
     *  Returns the short description of a specific datatype.
     * @param tid the HDF5 datatype identifier
     * @return the string description of the datatype
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
        } catch (Exception ex) {;}

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
            boolean is_reg_ref = false;
            try {is_reg_ref=H5.H5Tequal(tid, HDF5Constants.H5T_STD_REF_DSETREG);}
            catch (Exception ex) {}

            if (is_reg_ref)
                description = "Dataset region reference";
            else
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
            int[] evalue= {0};
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

    /**
     *
     * @return true if this datatype is unsigned; otherwise returns false.
     */
    public boolean isUnsigned()
    {
        return isUnsigned(toNative());
    }

    /**
     *  Checks if a specific datatype is an unsigned integer.
     *  <p>
     *  @param datatype  the data type to be checked.
     *  @return true is the datatype is an unsigned integer; otherwise returns false.
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

    /**
     * Converts this datatype to a native datatype.
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
    public int toNative()
    {
        if (nativeID >=0 )
            return nativeID;
        else if (isNamed) {
            try {nativeID = H5.H5Topen(getFID(), getPath()+getName());}
            catch (Exception ex) {nativeID = -1;}
        }

        if (nativeID >=0 )
            return nativeID;

        int tid = -1;

        // figure the datatype
        try {
            switch (datatypeClass)
            {
                case CLASS_INTEGER:
                case CLASS_ENUM:
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

        // set up enum members
        if (datatypeClass == CLASS_ENUM) {
            int ptid = tid;
            try {
                tid = H5.H5Tenum_create(ptid);
                String memstr, memname;
                int memval=0, idx;
                StringTokenizer token;

                // using "0" and "1" as default
                if (enumMembers == null)
                    token = new StringTokenizer("0,1", ",");
                else
                    token = new StringTokenizer(enumMembers, ",");

                while (token.hasMoreTokens()) {
                    memstr = token.nextToken();
                    if (memstr==null || memstr.length()<1)
                        continue;

                    idx = memstr.indexOf('=');
                    if (idx>0) {
                        memname = memstr.substring(0, idx);
                        memval = Integer.parseInt(memstr.substring(idx+1));
                    } else {
                        memname = memstr;
                        memval++;
                    }
                    H5.H5Tenum_insert(tid, memname, memval);
                }
            } catch (Exception ex) { tid = -1; ex.printStackTrace();}

            try { H5.H5Tclose(ptid); } catch (Exception ex) {}
        }

        return (nativeID = tid);
    }

    /**
     * Opens access to this named datatype
     * @return the datatype identifier if successful; otherwise returns negative value.
     */
    public int open()
    {
        int tid = -1;

        try
        {
            tid = H5.H5Topen(getFID(), getPath()+getName());
        } catch (HDF5Exception ex)
        {
            tid = -1;
        }

        return tid;
    }

    /**
     * Closes a specific datatype
     * @param tid the datatype to close
     */
    public void close(int tid)    {
        try { H5.H5Tclose(tid); }
        catch (HDF5Exception ex) {;}
    }

    /**
     * Read and returns a list of attributes of from file into memory if the attributes
     * are not in memory. If the attributes are in memory, it returns the attributes.
     * The attributes are stored as a collection in a List.
     *
     * @return the list of attributes.
     * @see <a href="http://java.sun.com/j2se/1.5.0/docs/api/java/util/List.html">java.util.List</a>
     */
    public List getMetadata() throws HDF5Exception
    {
        // load attributes first
        if (attributeList == null)
        {
            int tid = open();
            
            try {
                attributeList = H5File.getAttribute(tid);
            } catch (Exception ex) {}
            finally  {
                close(tid);
            }
        } // if (attributeList == null)

        return attributeList;
    }

    /**
     * Creates and attaches a new attribute if the attribute does not exist.
     * Otherwise, writes the value of the attribute in file.
     *
     * <p>
     * @param info the attribute to attach
     */
    public void writeMetadata(Object info) throws Exception
    {
        // only attribute metadata is supported.
        if (!(info instanceof Attribute))
            return;

        boolean attrExisted = false;
        Attribute attr = (Attribute)info;
        String name = attr.getName();

        if (attributeList == null)
            attributeList = new Vector(10);
        else
            attrExisted = attributeList.contains(attr);

        getFileFormat().writeAttribute(this, attr, attrExisted);

        // add the new attribute into attribute list
        if (!attrExisted) attributeList.add(attr);
    }

    /**
     * Deletes an attribute from this datatype.
     * <p>
     * @param info the attribute to delete.
     */
    public void removeMetadata(Object info) throws HDF5Exception
    {
        // only attribute metadata is supported.
        if (!(info instanceof Attribute))
            return;

        Attribute attr = (Attribute)info;
        int tid = open();
        try {
            H5.H5Adelete(tid, attr.getName());
            List attrList = getMetadata();
            attrList.remove(attr);
        } finally {
            close(tid);
        }
    }
}
