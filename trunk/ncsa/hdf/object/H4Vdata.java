/****************************************************************************
 * NCSA HDF                                                                 *
 * National Comptational Science Alliance                                   *
 * University of Illinois at Urbana-Champaign                               *
 * 605 E. Springfield, Champaign IL 61820                                   *
 *                                                                          *
 * For conditions of distribution and use, see the accompanying             *
 * java-hdf  COPYING file.                                                  *
 *                                                                          *
 ****************************************************************************/

package ncsa.hdf.object;

import java.util.*;
import ncsa.hdf.hdflib.*;

/**
 * H4Vdata describes a multi-dimension array of HDF4 vdata, inheriting CompoundDS.
 * <p>
 * A vdata is like a table that consists of a collection of records whose values
 * are stored in fixed-length fields. All records have the same structure and
 * all values in each field have the same data type. Vdatas are uniquely
 * identified by a name, a class, and a series of individual field names.
 * <p>
 * @version 1.0 12/12/2001
 * @author Peter X. Cao, NCSA
 */
public class H4Vdata extends CompoundDS
{
    /**
     * The list of attributes of this data object. Members of the list are
     * instance of Attribute.
     */
    private List attributeList;

    /**
     * Number of records of this Vdata table.
     */
    private int numberOfRecords;

    /**
     * The order of fields.
     */
    private int[] memberOrders;


    /**
     * Creates a H4Vdata object with specific name, path, and parent.
     * <p>
     * @param fid the file identifier.
     * @param filename the full path of the file that contains this data object.
     * @param name the name of this H4Vdata.
     * @param path the full path of this H4Vdata.
     * @param oid the unique identifier of this data object.
     */
    public H4Vdata(
        int fid,
        String filename,
        String name,
        String path,
        long[] oid)
    {
        super (fid, filename, name, path, oid);
        numberOfRecords = 0;
        numberOfMembers = 0;
        memberOrders = null;
    }

    // ***** need to implement from DataFormat *****
    public Object read() throws HDFException
    {
        //System.out.println("ncsa.hdf.object.H4Vdata.read()");
        if (data != null)
            return data; // data is loaded

        if (rank <=0 ) init();
        if (numberOfMembers <= 0)
            return null; // this Vdata does not have any filed

        int id = open();
        if (id < 0)
            return null;


        List list = new Vector(numberOfMembers);
        Object member_data = null;
        for (int i=0; i<numberOfMembers; i++)
        {
            try {
                // moves the access pointer to the start position
                HDFLibrary.VSseek(id, (int)startDims[0]);
                // Specify the fields to be accessed
                HDFLibrary.VSsetfields(id, memberNames[i]);
            } catch (HDFException ex) {
                continue;
            }

            member_data = H4Accessory.allocateArray(
                memberTypes[i],
                memberOrders[i]*(int)selectedDims[0]);
            if (member_data != null)
            {
                try {
                    int read_num = HDFLibrary.VSread(
                        id,
                        member_data,
                        (int)selectedDims[0],
                        HDFConstants.FULL_INTERLACE);
                        if (memberTypes[i] == HDFConstants.DFNT_CHAR ||
                            memberTypes[i] ==  HDFConstants.DFNT_UCHAR8)
                        {
                            member_data = Dataset.byteToString(
                                (byte[])member_data,
                                memberOrders[i]);
                        }

                    list.add(member_data);
                } catch (HDFException ex) {}
            }
        } // for (int i=0; i<numberOfMembers; i++)

        close(id);

        data = list;

        return data;
    }

    // ***** need to implement from DataFormat *****
    public boolean write() throws HDFException
    {
        return false;
    }

    // ***** need to implement from DataFormat *****
    public List getMetadata() throws HDFException
    {
        if (attributeList != null)
            return attributeList;

        int id = open();

        if (id < 0)
            return attributeList;

        int n=0;
        try {
            n = HDFLibrary.VSnattrs(id);

            if (n <=0 )
                return attributeList;

            attributeList = new Vector(n, 5);
            boolean b = false;
            String[] attrName = new String[1];
            int[] attrInfo = new int[3];
            for (int i=0; i<n; i++)
            {
                attrName[0] = "";
                try {
                    b = HDFLibrary.VSattrinfo(id, -1, i, attrName, attrInfo);
                    // mask off the litend bit
                    attrInfo[0] = attrInfo[0] & (~HDFConstants.DFNT_LITEND);
                } catch (HDFException ex)
                {
                    b = false;
                }

                if (!b) continue;

                long[] attrDims = {attrInfo[1]};
                Attribute attr = new Attribute(attrName[0], attrInfo[0], attrDims);;
                attributeList.add(attr);

                Object buf = H4Accessory.allocateArray(attrInfo[0], attrInfo[1]);
                try {
                    HDFLibrary.VSgetattr(id, -1, i, buf);
                } catch (HDFException ex)
                {
                    buf = null;
                }

                if (buf != null)
                {
                    if (attrInfo[0] == HDFConstants.DFNT_CHAR ||
                        attrInfo[0] ==  HDFConstants.DFNT_UCHAR8)
                    {
                        buf = Dataset.byteToString((byte[])buf, attrInfo[1]);
                    }

                    attr.setValue(buf);
                }
            } // for (int i=0; i<n; i++)
        } finally
        {
            close(id);
        }

        // todo: We shall also load attributes of fields

        return attributeList;
    }

    // ***** need to implement from DataFormat *****
    public boolean writeMetadata(Object info) throws HDFException
    {
        return false;
    }

    // ***** need to implement from DataFormat *****
    public boolean removeMetadata(Object info) throws HDFException
    {
        boolean b = true;

        return b;
    }

    // ***** need to implement from HObejct *****
    public int open()
    {
        int vsid = -1;
        long[] theOID = getOID();

        // try to open with read and write permission
        try {
            vsid = HDFLibrary.VSattach(getFID(), (int)theOID[1], "w");
        } catch (HDFException ex)
        {
            vsid = -1;
        }

        // try to open with read-only permission
        if (vsid < 0)
        {
            try {
                vsid = HDFLibrary.VSattach(getFID(), (int)theOID[1], "r");
            } catch (HDFException ex)
            {
                vsid = -1;
            }
        }

        return vsid;
    }

    // ***** need to implement from HObejct *****
    public static boolean close(int vsid)
    {
        boolean b = true;

        try {
            HDFLibrary.VSdetach(vsid);
        } catch (Exception ex) {
            b = false;
        }

        return b;
    }

    /**
     * Initializes the H4Vdata such as dimension sizes of this dataset.
     */
    public void init()
    {
        int id = open();
        if (id < 0) return;

        try {
            numberOfMembers = HDFLibrary.VFnfields(id);
            numberOfRecords = HDFLibrary.VSelts(id);
        } catch (HDFException ex) {
            numberOfMembers = 0;
            numberOfRecords = 0;
        }

        if (numberOfMembers <=0 || numberOfRecords <= 0)
        {
            // no table field is defined or no records
            close(id);
            return;
        }

        // a Vdata table is an one dimension array of records.
        // each record has the same fields
        rank = 1;
        dims = new long[1];
        dims[0] = (long)numberOfRecords;
        selectedDims = new long[1];
        selectedDims[0] = (long)numberOfRecords;
        startDims = new long[1];
        startDims[0] = 0;

        memberNames = new String[numberOfMembers];
        memberTypes = new int[numberOfMembers];
        memberOrders = new int[numberOfMembers];

        for (int i=0; i<numberOfMembers; i++)
        {
            try {
                memberNames[i] = HDFLibrary.VFfieldname(id, i);
                memberTypes[i] = HDFLibrary.VFfieldtype(id, i);
                // mask off the litend bit
                memberTypes[i] = memberTypes[i] & (~HDFConstants.DFNT_LITEND);
                memberOrders[i] = HDFLibrary.VFfieldorder(id, i);
            } catch (HDFException ex) {
                continue;
            }
        } // for (int i=0; i<numberOfMembers; i++)

        close(id);
    }

    /**
     * Returns the number of records.
     */
    public int getRecordCount()
    {
        return numberOfRecords;
    }

    /**
     * Returns the number of fields.
     */
    public int getFieldCount()
    {
        return numberOfMembers;
    }

    /**
     * Returns the orders of fields
     */
    public int[] getFieldOrders()
    {
        return memberOrders;
    }
}