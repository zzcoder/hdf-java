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
 import ncsa.hdf.hdf5lib.*;
 import ncsa.hdf.hdf5lib.exceptions.*;

/**
 * Attribute holds a (name, value) pair of HDF4/5 attribute.
 * <p>
 * @version 1.0 12/12/2001
 * @author Peter X. Cao, NCSA
 */
public class Attribute implements java.io.Serializable
{
    /**
     * The name of the attribute.
     */
    private final String name;

    /**
     * The datatype of this attribute.
     */
    private final int datatype;

    /**
     * The rank of the data value of this attribute.
     */
    private final int dataRank;

    /**
     * The dimension sizes of the data value of this attribute.
     */
    private final long[] dataDims;

    /**
     * The data value of this attribute.
     */
    private Object value;


    /**
     * Create a new attribute with the given (name, value) pair.
     * Attribute is independent of dataformat, i.e., this implementation of
     * attribute applies to both HDF4 and HDF5.
     * <p>
     * @param name the name of the attribute.
     * @param value the value of the attribute.
     */
    public Attribute(String name, Object value)
    {
        this.name = name;
        this.value = value;

        this.datatype = -1;
        this.dataDims = null;
        this.dataRank = 0;
    }

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
     */
    public Attribute(String name, int type, long[] dims)
    {
        this.name = name;
        this.datatype = type;
        this.dataDims = dims;

        if (dims != null)
            this.dataRank = dims.length;
        else
            this.dataRank = 0;

        this.value = null;
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
        return dataRank;
    }

    /**
     * Returns the dimension sizes of the data value of this attribute.
     */
    public long[] getDataDims()
    {
        return dataDims;
    }

    /**
     * Returns the datatype of the attribute.
     */
    public int getType()
    {
        return datatype;
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
    public void setValue(Object value)
    {
        this.value = value;
    }

    /**
     * Returns the string representation of this data object.
     * The String consists of the name and path of the data object.
     */
    public String toString()
    {
        String valStr = null;
        if (value != null)
        {
            if (value.getClass().isArray())
            {
                StringBuffer sb = new StringBuffer();
                int n = Array.getLength(value);
                for (int i=0; i<n-1; i++)
                {
                    sb.append(Array.get(value, i));
                    sb.append(", ");
                }
                sb.append(Array.get(value, n-1));
                valStr = sb.toString();
            }
            else
                valStr = value.toString();
        }

        return "[Type: Attribute], [Name: "+name+"], [Value: "+valStr+"]";
    }

}
