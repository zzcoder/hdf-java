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
  *An attribute is a (name, value) pair metadata that are attached to primary
 * datasets and groups. The value field can be a scalar data point or an arrya
 * of native datatype or compound data type.
 * <p>
 * The following is an example of how to create an attribute of one dimension
 * integer array of size two.
 *
 * <pre>
 * // Create an attribute
 * String name = "data range";
 * Datatype type = new Datatype(Datatype.CLASS_INTEGER, Datatype.NATIVE,
 *     Datatype.NATIVE, Datatype.NATIVE);
 * long[] space = {2};
 * int[] value = {0, 255};
 *
 * Attribute dataRange = new Attribute(name, type, space);
 * dataRange.setValue(value);
 *
 * </pre>
 *
 * @see ncsa.hdf.object.Datatype
 *
 * @version 1.0 12/12/2001
 * @author Peter X. Cao, NCSA
 */
public class Attribute implements Metadata
{
    /** the default length of a string attribute */
    public static final int DEFAULT_STRING_ATTRIBUTE_LENGTH = 256;

    /**
     * The name of the attribute.
     */
    private final String name;

    /**
     * The datatype of the attribute.
     */
    private final Datatype type;

    /**
     * The rank of the data value of this attribute.
     * An attribute can have multi dimensions.
     */
    private int rank;

    /**
     * The dimension sizes of the data value of this attribute.
     */
    private long[] dims;

    /**
     * The data value of this attribute.
     */
    private Object value;

    /** flag to indicate if the data type is an unsigned integer. */
    private boolean isUnsigned;

    /**
     * Create an attribute with specified name, data type and dimension sizes.
     * For scalar attribute, the dimension size can either an array of size one
     * or null. The rank can be either 1 or zero. Attribute is independent of
     * dataformat, i.e., this implementation of attribute applies to both HDF4
     * and HDF5.
     * <p>
     * @param name the name of the attribute.
     * @param type the data type of the attribute.
     * @param dims the dimension sizes of the data of the attribute.
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
     * Returns the value of this attriubte from file.
     */
    public Object getValue()
    {
        return value;
    }

    /**
     * Sets the value of this attribute.
     */
    public void setValue(Object theValue)
    {
        value = theValue;
    }

    /**
     * Returns the name of this attribute.
     */
    public String getName()
    {
        return name;
    }

    /**
     * Returns the rank of the data value of this attribute.
     */
    public int getRank()
    {
        return rank;
    }

    /**
     * Returns the dimension sizes of the data value of this attribute.
     */
    public long[] getDataDims()
    {
        return dims;
    }

    /**
     * Returns the datatype of the attribute.
     */
    public Datatype getType()
    {
        return type;
    }

    /**
     * Check the data type of the attribute is unsigned.
     */
    public boolean isUnsigned()
    {
        return isUnsigned;
    }

    /**
     * Returns the string representation of the value of this attribute.
     * <p>
     * @param delimiter the delimiter to separate individual data points.
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

    /**
     * Returns the string representation of this attribute.
     * The String consists of the name and path of the data object.
     */
    public String toString()
    {
        return "[Type: Attribute], [Name: "+name+"]";
    }

}
