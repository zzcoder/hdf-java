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

import java.util.*;
import ncsa.hdf.hdf5lib.*;
import ncsa.hdf.hdf5lib.exceptions.*;

/**
 * H5CompoundDS describes a multi-dimension array of HDF5 compound dataset,
 * inheriting CompoundDS.
 * <p>
 * A HDF5 compound datatype is similar to a struct in C or a common block in
 * Fortran: it is a collection of one or more atomic types or small arrays of
 * such types. Each member of a compound type has a name which is unique within
 * that type, and a byte offset that determines the first byte (smallest byte
 * address) of that member in a compound datum.
 * <p>
 * There are two basic types of compound datasets, simple compound data and
 * nested compound data. Members of a simple compound dataset are scalar data or
 * array of scalar data. Members of a nested compound dataset can be compound or
 * array of compound data. <B>The current implementation of H5CompoundDS only
 * supports simple compound datasets.</B>
 * <p>
 * A special case of compound dataset is the one-dimension simple compound data.
 * An one-dimension HDF5 compound array is like an HDF5 Vdata table. Members of
 * the compound dataset are similar to fileds of HDF4 VData table.
 * <p>
 * ARRAY of compound dataset is not supported in this implementation. ARRAY of
 * compound dataset requires to read the whole data structure instead of read
 * field by field.
 * <p>
 * @version 1.0 12/12/2001
 * @author Peter X. Cao, NCSA
 */
public class H5CompoundDS extends CompoundDS
{
    /**
     * The list of attributes of this data object. Members of the list are
     * instance of Attribute.
     */
     private List attributeList;

    /**
     * Creates a H5CompoundDS object with specific name and path.
     * <p>
     * @param fileFormat the HDF file.
     * @param name the name of this H5CompoundDS.
     * @param path the full path of this H5CompoundDS.
     * @param oid the unique identifier of this data object.
     */
    public H5CompoundDS(
        FileFormat fileFormat,
        String name,
        String path,
        long[] oid)
    {
        super (fileFormat, name, path, oid);
    }

    /**
     * Loads the content of this data object into memory if the data of the
     * object is not loaded. If the content is already loaded, it returns the
     * content. It returns null if the data object has no content or it fails
     * to load the data content.
     * <p>
     * The current implementation only support one dimensional compound dataset.
     * Compound data are stored in a vector, where each element of the vector
     * represent a member data of the compound dataset.
     */
    public Object read() throws HDF5Exception
    {
        if (data != null)
            return data; // data is loaded

        List list = null;

        if (rank <= 0 )
            init(); // initialize the dimension information

        if (numberOfMembers <= 0)
            return null; // this compound dataset does not have any member

        list = new Vector(numberOfMembers);

        Object member_data = null;
        String member_name = null;
        int member_tid=-1, member_class=-1, member_size=0, fspace=-1, mspace=-1;
        int did = open();

        boolean isAllSelected = true;
        for (int i=0; i<rank; i++)
        {
            if (selectedDims[i] < dims[i])
            {
                isAllSelected = false;
                break;
            }
        }

        try
        {
            long[] lsize = {1};
            for (int j=0; j<selectedDims.length; j++)
                lsize[0] *= selectedDims[j];

            if (isAllSelected)
            {
                mspace = HDF5Constants.H5S_ALL;
                fspace = HDF5Constants.H5S_ALL;
            }
            else
            {
                fspace = H5.H5Dget_space(did);
                mspace = H5.H5Screate_simple(1, lsize, null);

                // set the rectangle selection
                H5.H5Sselect_hyperslab(
                    fspace,
                    HDF5Constants.H5S_SELECT_SET,
                    startDims,
                    null,     // set stride to 1
                    selectedDims,
                    null );   // set block to 1
            }

            for (int i=0; i<numberOfMembers; i++)
            {
                member_name = memberNames[i];
                member_tid = memberTypes[i];

                member_data = H5Accessory.allocateArray(member_tid, (int)lsize[0]);

                if (member_data == null)
                    continue;

                try { member_class = H5.H5Tget_class(member_tid);
                } catch (HDF5Exception ex) {}

                int arrayType = member_tid;
                int baseType = member_tid;
                if (member_class == HDF5Constants.H5T_ARRAY)
                {
                    try
                    {
                        int mn = H5.H5Tget_array_ndims(member_tid);
                        int[] marray = new int[mn];
                        H5.H5Tget_array_dims(member_tid, marray, null);
                        baseType = H5.H5Tget_super(member_tid);
                        arrayType = H5.H5Tarray_create (
                            H5Accessory.toNativeType(baseType),
                            mn,
                            marray,
                            null);
                    } catch (HDF5Exception ex) {}
                }

                int read_tid = -1;
                try
                {
                    member_size = H5.H5Tget_size(member_tid);
                    read_tid = H5.H5Tcreate(HDF5Constants.H5T_COMPOUND,member_size);
                    H5.H5Tinsert(read_tid, member_name, 0, arrayType);
                    H5.H5Dread(
                        did,
                        read_tid,
                        mspace,
                        fspace,
                        HDF5Constants.H5P_DEFAULT,
                        member_data);
                } catch (HDF5Exception ex2)
                {
                    try { H5.H5Tclose(read_tid); }
                    catch (HDF5Exception ex3) {}
                    if (fspace > 0) {
                        try { H5.H5Sclose(fspace); }
                        catch (HDF5Exception ex3) {}
                    }
                    continue;
                }
                try { H5.H5Tclose(read_tid); } catch (HDF5Exception ex2) {}

                if (member_class == HDF5Constants.H5T_STRING)
                    member_data = byteToString((byte[])member_data, member_size);
                else if (member_class == HDF5Constants.H5T_REFERENCE)
                    member_data = HDFNativeData.byteToLong((byte[])member_data);
                else if (H5Accessory.isUnsigned(baseType))
                    member_data = Dataset.convertFromUnsignedC(member_data);

                list.add(i, member_data);
            } // end of for (int i=0; i<num_members; i++)
        } finally
        {
            if (fspace > 0)
                try { H5.H5Sclose(fspace); } catch (Exception ex2) {}
            if (mspace > 0)
                try { H5.H5Sclose(mspace); } catch (Exception ex2) {}
            close(did);
        }

        return (data=list);
    }

    // To do: Implementing DataFormat
    public void write() throws HDF5Exception {;}

    // Implementing DataFormat
    public List getMetadata() throws HDF5Exception
    {

        if (attributeList == null)
        {
            int did = open();
            attributeList = H5Accessory.getAttribute(did);
            close(did);
        }

        return attributeList;
    }

    /**
     * Creates a new attribute and attached to this dataset if attribute does
     * not exist. Otherwise, just update the value of the attribute.
     *
     * <p>
     * @param info the atribute to attach
     */
    public void writeMetadata(Object info) throws HDF5Exception
    {
        // only attribute metadata is supported.
        if (!(info instanceof Attribute))
            return;

        Attribute attr = (Attribute)info;
        String name = attr.getName();
        List attrList = getMetadata();
        boolean attrExisted = attrList.contains(attr);

        int did = open();
        try {
            H5Accessory.writeAttribute(did, attr, attrExisted);
            // add the new attribute into attribute list
            if (!attrExisted) attrList.add(attr);
        } finally
        {
            close(did);
        }
    }

    /**
     * Deletes an attribute from this dataset.
     * <p>
     * @param info the attribute to delete.
     */
    public void removeMetadata(Object info) throws HDF5Exception
    {
        // only attribute metadata is supported.
        if (!(info instanceof Attribute))
            return;

        Attribute attr = (Attribute)info;
        int did = open();
        try {
            H5.H5Adelete(did, attr.getName());
            List attrList = getMetadata();
            attrList.remove(attr);
        } finally {
            close(did);
        }
    }

    // Implementing HObject
    public int open()
    {
        int did = -1;

        try
        {
            did = H5.H5Dopen(getFID(), getPath()+getName());
        } catch (HDF5Exception ex)
        {
            did = -1;
        }

        return did;
    }

    // Implementing HObject
    public static void close(int did)
    {
        try { H5.H5Dclose(did); }
        catch (HDF5Exception ex) { ; }
    }

    /**
     * Retrieve and initialize dimensions and member information.
     */
    public void init()
    {
        int did=-1, sid=-1, tid=-1, tclass=-1;
        int fid = getFID();
        String fullName = getPath()+getName();

        try {
            did = H5.H5Dopen(fid, fullName);
            sid = H5.H5Dget_space(did);
            rank = H5.H5Sget_simple_extent_ndims(sid);

            if (rank == 0)
            {
                // a scalar data point
                rank = 1;
                dims = new long[1];
                dims[0] = 1;
            }
            else
            {
                dims = new long[rank];
                H5.H5Sget_simple_extent_dims(sid, dims, null);
            }

            startDims = new long[rank];
            selectedDims = new long[rank];
            selectedIndex[0] = 0; // selected the first dimension by default

            // for default, only select 1D for compound dataset
            selectedDims[selectedIndex[0]] = dims[selectedIndex[0]];
            for (int i=1; i<rank; i++)
            {
                startDims[i] = 0;
                selectedDims[i] = 1;
            }

            // initialize member information
            tid= H5.H5Dget_type(did);
            tclass = H5.H5Tget_class(tid);
            if (tclass == HDF5Constants.H5T_ARRAY)
            {
                int tmptid = tid;
                tid = H5.H5Tget_super(tmptid);
                try { H5.H5Tclose(tmptid); } catch (HDF5Exception ex) {}
            }

            numberOfMembers = H5.H5Tget_nmembers(tid);
            memberNames = new String[numberOfMembers];
            memberTypes = new int[numberOfMembers];
            memberOrders = new int[numberOfMembers];
            for (int i=0; i<numberOfMembers; i++)
            {
                int mtid = H5.H5Tget_member_type(tid, i);
                memberTypes[i] = H5Accessory.toNativeType(mtid);
                memberNames[i] = H5.H5Tget_member_name(tid, i);
                memberOrders[i] = 1;

                try { tclass = H5.H5Tget_class(mtid); }
                catch (HDF5Exception ex ) {}

                if (tclass == HDF5Constants.H5T_ARRAY)
                {
                    int tmptid = H5.H5Tget_super(mtid);
                    memberOrders[i] = (int)(H5.H5Tget_size(mtid)/H5.H5Tget_size(tmptid));
                    try { H5.H5Tclose(tmptid); } catch (HDF5Exception ex) {}
                }

                try { H5.H5Tclose(mtid); } catch (HDF5Exception ex2) {}
            }
        } catch (HDF5Exception ex)
        {
            numberOfMembers = 0;
            memberNames = null;
            memberTypes = null;
            memberOrders = null;
        }
        finally
        {
            try { H5.H5Tclose(tid); } catch (HDF5Exception ex2) {}
            try { H5.H5Sclose(sid); } catch (HDF5Exception ex2) {}
            try { H5.H5Dclose(did); } catch (HDF5Exception ex2) {}
        }
    }

}
