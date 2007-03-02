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

 import java.lang.reflect.*;


/**
 * An attribute is a (name, value) pair of metadata attached to primary
 * data objects such as datasets, groups or named datatypes. The value field can
 * be a scalar data point or an array of atomic datatype or compound datatype.
 * <p>
 * The requirement information of an attribute includes the name, datatype and
 * dataspace. The following is an example of creating an attribute of one dimension
 * integer array of size two.
 *
 * <pre>
 * // Example of creatinge a new attribute
 *
 * // The name of the new attribute
 * String name = "Data range";
 *
 * // Creating an unsigned 1-byte integer datatype
 * Datatype type = new Datatype(Datatype.CLASS_INTEGER, // class
 *                              1,                      // size in bytes
 *                              Datatype.ORDER_LE,      // byte order
 *                              Datatype.SIGN_NONE);    // signed or unsigned
 *
 * // 1-D array of size two
 * long[] dims = {2};
 *
 * // The value of the attribute
 * int[] value = {0, 255};
 *
 * // Create a new attribute
 * Attribute dataRange = new Attribute(name, type, dims);
 *
 * // Set the attribute value
 * dataRange.setValue(value);
 *
 * // See FileFormat.writeAttribute() for how to attach an attribute to an object, 
 * @see ncsa.hdf.object.FileFormat#writeAttribute(HObject, Attribute, boolean)
 * </pre>
 *
 * @see ncsa.hdf.object.Datatype
 *
 * @version 1.0 12/12/2001
 * @author Peter X. Cao, NCSA
 */
public class Attribute implements Metadata
{
	public static final long serialVersionUID = HObject.serialVersionUID;

    /** The name of the attribute. */
    private final String name;

    /** The datatype of the attribute. */
    private final Datatype type;

    /** The rank of the data value of the attribute. */
    private int rank;

    /** The dimension sizes of the attribute. */
    private long[] dims;

    /** The value of the attribute. */
    private Object value;

    /** Flag to indicate if the datatype is an unsigned integer. */
    private boolean isUnsigned;

    /**
     * Create an attribute with specified name, data type and dimension sizes.
     * 
     * For scalar attribute, the dimension size can be either an array of size one
     * or null, and the rank can be either 1 or zero. Attribute is a general class
     * and is independent of file format, e.g., the implementation of attribute
     * applies to both HDF4 and HDF5.
     * <p>
     * The following example creates a string attribute with the name "CLASS" and value "IMAGE".
     * <pre>
        long[] attrDims = {1};
        String attrName = "CLASS";
        String[] classValue = {"IMAGE"};
        Datatype attrType = new H5Datatype(Datatype.CLASS_STRING, classValue[0].length()+1, -1, -1);
        Attribute attr = new Attribute(attrName, attrType, attrDims);
        attr.setValue(classValue);
     * </pre>
     * 
     * @param attrName the name of the attribute.
     * @param attrType the datatype of the attribute.
     * @param attrDims the dimension sizes of the attribute, null for scalar attribute
     *
     * @see ncsa.hdf.object.Datatype
     */
    public Attribute(String attrName, Datatype attrType, long[] attrDims)
    {
        name = attrName;
        type = attrType;
        dims = attrDims;
        value = null;
        rank = 0;

        if (dims != null)
            rank = dims.length;

        isUnsigned = (type.getDatatypeSign()==Datatype.SIGN_NONE);
    }

    /**
     * Returns the value of the attribute.
     * For atomic datatype, this will be an 1D array of integers, floats and strings.
     * For compound datatype, it will be an 1D array of strings with field members
     * separated by comma. For example, "{0, 10.5}, {255, 20.0}, {512, 30.0}" is a cmpound 
     * attribute of {int, float} of three data points.  
     * 
     * @return the value of the attribute, or null if failed to retrieve data from file.
     */
    public Object getValue()
    {
        return value;
    }

    /**
     * Sets the value of the attribute.
     * It returns null if failed to retrieve the name from file.
     *
     * @param theValue The value of the attribute to set
     */
    public void setValue(Object theValue)
    {
        value = theValue;
    }

    /**
     * Returns the name of the attribute.
     * 
     * @return the name of the attribute.
     */
    public String getName()
    {
        return name;
    }

    /**
     * Returns the rank (number of dimensions) of the attribute.
     * It returns a negative number if failed to retrieve the dimension 
     * information from file.
     * 
     * @return the number of dimensions of the attribute.
     */
    public int getRank()
    {
        return rank;
    }

    /**
     * Returns the dimension sizes of the data value of the attribute.
     * It returns null if failed to retrieve the dimension information from file.
     * 
     * @return the dimension sizes of the attribute. 
     */
    public long[] getDataDims()
    {
        return dims;
    }

    /**
     * Returns the datatype of the attribute.
     * It returns null if failed to retrieve the datatype information from file.
     * 
     * @return the datatype of the attribute.
     */
    public Datatype getType()
    {
        return type;
    }

    /**
     * Checks if the data type of this attribute is an unsigned integer.
     * 
     * @return true if the data type of the attribute is an unsigned integer; otherwise returns false.
     */
    public boolean isUnsigned()
    {
        return isUnsigned;
    }

    /**
     * Returns a string representation of the data value of the attribute.
     * For example, "0, 255". 
     * <p>
     * For compound datatype, it will be an 1D array of strings with field members
     * separated by comma. For example, "{0, 10.5}, {255, 20.0}, {512, 30.0}" is a cmpound 
     * attribute of {int, float} of three data points.
     * <p>
     * @param delimiter The delimiter to separate individual data point. 
     *        It can be comma, semicolon, tab or space. 
     *        For example, to String(",") will separate data by comma.
     *        
     * @return the string representation of the data values.
     */
    public String toString(String delimiter)
    {
        if (value == null)
            return null;

        Class valClass = value.getClass();

        if (!valClass.isArray())
            return value.toString();

        // attribute value is an array
        StringBuffer sb = new StringBuffer();
        int n = Array.getLength(value);
        if (isUnsigned)
        {
            long maxValue = 0;

            String cname = valClass.getName();
            char dname = cname.charAt(cname.lastIndexOf("[")+1);

            switch (dname)
            {
                case 'B':
                    byte[] barray = (byte[])value;
                    short sValue = barray[0];
                    if (sValue < 0) sValue += 256;
                    sb.append(sValue);
                    for (int i=1; i<n; i++)
                    {
                        sb.append(delimiter);
                        sValue = barray[i];
                        if (sValue < 0) sValue += 256;
                        sb.append(sValue);
                    }
                    break;
                case 'S':
                    short[] sarray = (short[])value;
                    int iValue = sarray[0];
                    if (iValue < 0) iValue += 65536;
                    sb.append(iValue);
                    for (int i=1; i<n; i++)
                    {
                        sb.append(delimiter);
                        iValue = sarray[i];
                        if (iValue < 0) iValue += 65536;
                        sb.append(iValue);
                    }
                    break;
                case 'I':
                    int[] iarray = (int[])value;
                    long lValue = iarray[0];
                    if (lValue < 0) lValue += 4294967296L;
                    sb.append(lValue);
                    for (int i=1; i<n; i++)
                    {
                        sb.append(delimiter);
                        lValue = iarray[i];
                        if (lValue < 0) lValue += 4294967296L;
                        sb.append(lValue);
                    }
                    break;
                default:
                    sb.append(Array.get(value, 0));
                    for (int i=1; i<n; i++)
                    {
                        sb.append(delimiter);
                        sb.append(Array.get(value, i));
                    }
                    break;
            }
        }
        else
        {
            sb.append(Array.get(value, 0));
            for (int i=1; i<n; i++)
            {
                sb.append(delimiter);
                sb.append(Array.get(value, i));
            }
        }

        return sb.toString();
    }
}
