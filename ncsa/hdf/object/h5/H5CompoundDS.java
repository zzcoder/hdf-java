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

import java.util.*;
import ncsa.hdf.hdf5lib.*;
import ncsa.hdf.hdf5lib.exceptions.*;
import ncsa.hdf.object.*;

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
 * <b>How to Select a Subset</b>
 * <p>
 * Dataset defines APIs for read, write and subet a dataset. No function is defined
 * to select a subset of a data array. The selection is done in an implicit way.
 * Function calls to dimension information such as getSelectedDims() return an array
 * of dimension values, which is a reference to the array in the dataset object.
 * Changes of the array outside the dataset object directly change the values of
 * the array in the dataset object. It is like pointers in C.
 * <p>
 *
 * The following is an example of how to make a subset. In the example, the dataset
 * is a 4-dimension with size of [200][100][50][10], i.e.
 * dims[0]=200; dims[1]=100; dims[2]=50; dims[3]=10; <br>
 * We want to select every other data points in dims[1] and dims[2]
 * <pre>
     int rank = dataset.getRank();   // number of dimension of the dataset
     long[] dims = dataset.getDims(); // the dimension sizes of the dataset
     long[] selected = dataset.getSelectedDims(); // the selected size of the dataet
     long[] start = dataset.getStartDims(); // the off set of the selection
     long[] stride = dataset.getStride(); // the stride of the dataset
     int[]  selectedIndex = dataset.getSelectedIndex(); // the selected dimensions for display

     // select dim1 and dim2 as 2D data for display,and slice through dim0
     selectedIndex[0] = 1;
     selectedIndex[1] = 2;
     selectedIndex[1] = 0;

     // reset the selection arrays
     for (int i=0; i<rank; i++) {
         start[i] = 0;
         selected[i] = 1;
         stride[i] = 1;
    }

    // set stride to 2 on dim1 and dim2 so that every other data points are selected.
    stride[1] = 2;
    stride[2] = 2;

    // set the selection size of dim1 and dim2
    selected[1] = dims[1]/stride[1];
    selected[2] = dims[1]/stride[2];

    // when dataset.read() is called, the slection above will be used since
    // the dimension arrays is passed by reference. Changes of these arrays
    // outside the dataset object directly change the values of these array
    // in the dataset object.

 * </pre>
 *
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
      * A list of names of all fields including nested fields.
      * The nested names are separated by ".". For example, if
      * compound dataset "A" has the following nested structure,
      * <pre>
      * A --> m01
      * A --> m02
      * A --> nest1 --> m11
      * A --> nest1 --> m12
      * A --> nest1 --> nest2 --> m21
      * A --> nest1 --> nest2 --> m22
      * i.e.
      * A = { m01, m02, nest1{m11, m12, nest2{ m21, m22}}}
      * </pre>
      * The flatNameList of compound dataset "A" will be
      * {m01, m02, nest1.m11, nest1.m12, nest1.nest2.m21, nest1.nest2.m22}
      *
      */
     private List flatNameList;

     /**
      * A list of datatypes of all fields including nested fields.
      * @see #flatNameList
      */
     private List flatTypeList;

    public H5CompoundDS(FileFormat fileFormat, String name, String path)
    {
        this(fileFormat, name, path, null);
    }

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

    // implementing Dataset
    public Datatype getDatatype()
    {
        if (datatype == null)
        {
            datatype = new H5Datatype(-1);
        }

        return datatype;
    }

    // implementing Dataset
    public byte[] readBytes() throws HDF5Exception
    {
        byte[] theData = null;

        if (rank <= 0) init();

        int did = open();
        int fspace=-1, mspace=-1, tid=-1;

        try
        {
            long[] lsize = {1};
            for (int j=0; j<selectedDims.length; j++)
                lsize[0] *= selectedDims[j];

            fspace = H5.H5Dget_space(did);
            mspace = H5.H5Screate_simple(1, lsize, null);

            // set the rectangle selection
            // HDF5 bug: for scalar dataset, H5Sselect_hyperslab gives core dump
            if (rank*dims[0] > 1)
            {
                H5.H5Sselect_hyperslab(
                    fspace,
                    HDF5Constants.H5S_SELECT_SET,
                    startDims,
                    selectedStride,
                    selectedDims,
                    null );   // set block to 1
            }

            tid = H5.H5Dget_type(did);
            int size = H5.H5Tget_size(tid)*(int)lsize[0];
            theData = new byte[size];
            H5.H5Dread(did, tid, mspace, fspace, HDF5Constants.H5P_DEFAULT, theData);
        }
        finally
        {
            if (fspace > 0) try { H5.H5Sclose(fspace); } catch (Exception ex2) {}
            if (mspace > 0) try { H5.H5Sclose(mspace); } catch (Exception ex2) {}
            try { H5.H5Tclose(tid); } catch (HDF5Exception ex2) {}
            close(did);
        }

        return theData;
    }

    /**
     * Loads the content of this data object into memory if the data of the
     * object is not loaded. If the content is already loaded, it returns the
     * content. It returns null if the data object has no content or it fails
     * to load the data content.
     * <p>
     * Data is read from file member by member and stored into a list by members.
     * Each member data is an one-dimension array. The array type is determined
     * by the datatype of the member. The data list contains only the data of
     * selected members.
     * <p>
     * For example, if compound dataset "A" has the following nested structure,
     * and memeber datatypes
     * <pre>
     * A --> m01 (int)
     * A --> m02 (float)
     * A --> nest1 --> m11 (char)
     * A --> nest1 --> m12 (String)
     * A --> nest1 --> nest2 --> m21 (long)
     * A --> nest1 --> nest2 --> m22 (double)
     * </pre>
     * read() returns a List contains arrays of int, float, char, Stirng, long and double.
     * @return the array of data List.
     *
     */
    public Object read() throws HDF5Exception
    {
        List list = null;

        Object member_data = null;
        String member_name = null;
        int member_tid=-1, member_class=-1, member_size=0, fspace=-1, mspace=-1;

        if (rank <= 0 ) init(); // read data informatin into memory

        if (numberOfMembers <= 0)
            return null; // this compound dataset does not have any member

        int did = open();

        // check is storage space is allocated
        try {
            if (H5.H5Dget_storage_size(did) <=0)
            {
                throw new HDF5Exception("Storage space is not allocated.");
            }
        } catch (Exception ex) {}

        // check is fill value is defined
        try {
            int plist = H5.H5Dget_create_plist(did);
            int[] fillValue = {0};
            H5.H5Pfill_value_defined(plist, fillValue);
            try { H5.H5Pclose(plist); } catch (Exception ex2) {}
            if (fillValue[0] == HDF5Constants.H5D_FILL_VALUE_UNDEFINED)
            {
                throw new HDF5Exception("Fill value is not defined.");
            }
        } catch (Exception ex) {}

        list = new Vector();

        try // to match finally for closing resources
        {
            long[] lsize = {1};
            for (int j=0; j<selectedDims.length; j++)
                lsize[0] *= selectedDims[j];

            fspace = H5.H5Dget_space(did);
            mspace = H5.H5Screate_simple(1, lsize, null);

            // set the rectangle selection
            // don't do hyber selection for one point dataset
            if (rank*dims[0] > 1)
            {
                H5.H5Sselect_hyperslab(
                    fspace,
                    HDF5Constants.H5S_SELECT_SET,
                    startDims,
                    selectedStride,
                    selectedDims,
                    null );   // set block to 1
            }

            // read each of member data into a byte array, then extract
            // it into its type such, int, long, float, etc.
            int n = flatNameList.size();
            for (int i=0; i<n; i++)
            {
                if (!isMemberSelected[i])
                    continue; // the field is not selected

                member_name = new String(memberNames[i]);
                member_tid = memberTypes[i];
                try { member_class = H5.H5Tget_class(member_tid);
                } catch (HDF5Exception ex) {}

                try {
                    member_size = H5.H5Tget_size(member_tid);
                    member_data = H5Datatype.allocateArray(member_tid, (int)lsize[0]);
                } catch (Exception ex) {member_data = null; }

                if (member_data == null)
                {
                    String[] nullValues = new String[(int)lsize[0]];
                    String errorStr = "not supported: "+ H5Datatype.getDatatypeDescription(member_tid);
                    for (int j=0; j<lsize[0]; j++) nullValues[j] = errorStr;
                    list.add(nullValues);
                    continue;
                }

                int arrayType = member_tid;
                int baseType = arrayType;
                if (member_class == HDF5Constants.H5T_ARRAY)
                {
                    // convert base type into its native base type
                    try
                    {
                        int mn = H5.H5Tget_array_ndims(member_tid);
                        int[] marray = new int[mn];
                        H5.H5Tget_array_dims(member_tid, marray, null);
                        baseType = H5.H5Tget_super(member_tid);
                        arrayType = H5.H5Tarray_create (
                            H5Datatype.toNative(baseType),
                            mn,
                            marray,
                            null);
                    } catch (HDF5Exception ex) {;}
                }

                int nested_tid = -1;
                boolean isVLEN = false;
                boolean is_variable_str = false;
                try {
                    // construct nested compound structure with a single field
                    String theName = member_name;
                    int tmp_tid = H5.H5Tcopy(arrayType);
                    int sep = member_name.lastIndexOf('.');
                    while (sep > 0) {
                        theName = member_name.substring(sep+1);
                        nested_tid = H5.H5Tcreate(HDF5Constants.H5T_COMPOUND, member_size);
                        H5.H5Tinsert(nested_tid, theName, 0, tmp_tid);
                        try {H5.H5Tclose(tmp_tid);} catch (Exception ex) {}
                        tmp_tid = nested_tid;
                        member_name = member_name.substring(0, sep);
                        sep = member_name.lastIndexOf('.');
                    }

                    nested_tid = H5.H5Tcreate(HDF5Constants.H5T_COMPOUND, member_size);
                    H5.H5Tinsert(nested_tid, member_name, 0, tmp_tid);
                    try { is_variable_str = H5.H5Tis_variable_str(tmp_tid); } catch (Exception ex) {}
                    try { isVLEN = (H5.H5Tget_class(tmp_tid)==HDF5Constants.H5T_VLEN); } catch (Exception ex) {}

                    try {H5.H5Tclose(tmp_tid);} catch (Exception ex) {}

                    if (isVLEN || is_variable_str)
                        H5.H5DreadVL( did, nested_tid, mspace, fspace, HDF5Constants.H5P_DEFAULT, (Object[])member_data);
                    else
                        H5.H5Dread( did, nested_tid, mspace, fspace, HDF5Constants.H5P_DEFAULT, member_data);
                } catch (HDF5Exception ex2)
                {
                    try { H5.H5Tclose(nested_tid); }
                    catch (HDF5Exception ex3) {}
                    if (fspace > 0) {
                        try { H5.H5Sclose(fspace); }  catch (HDF5Exception ex3) {}
                    }

                    String[] nullValues = new String[(int)lsize[0]];
                    for (int j=0; j<lsize[0]; j++) nullValues[j] = "*error*";
                    list.add(nullValues);
                    //isMemberSelected[i] = false; // do not display memeber without data
                    try { H5.H5Tclose(nested_tid); } catch (HDF5Exception ex3) {}
                    continue;
                }
                try { H5.H5Tclose(nested_tid); } catch (HDF5Exception ex2) {}

                if (!isVLEN)
                {
                    if (member_class == HDF5Constants.H5T_STRING) {
                        member_data = byteToString((byte[])member_data, member_size);
                    }
                    else if (member_class == HDF5Constants.H5T_REFERENCE) {
                        member_data = HDFNativeData.byteToLong((byte[])member_data);
                    }
                    else if (H5Datatype.isUnsigned(baseType)) {
                        member_data = Dataset.convertFromUnsignedC(member_data);
                    }
                }

                list.add(member_data);
            } // end of for (int i=0; i<num_members; i++)
        } finally
        {
            if (fspace > 0)
                try { H5.H5Sclose(fspace); } catch (Exception ex2) {}
            if (mspace > 0)
                try { H5.H5Sclose(mspace); } catch (Exception ex2) {}
            close(did);
        }

        return list;
    }

    //Implementing DataFormat
    public void write(Object buf) throws HDF5Exception
    {
        if (buf == null)
            return; // no data to write

        if (numberOfMembers <= 0)
            return; // this compound dataset does not have any member

        if (!(buf instanceof List))
            return;

        List list = (List)buf;
        Object member_data = null;
        String member_name = null;
        int member_tid=-1, member_class=-1, member_size=0, fspace=-1, mspace=-1;
        int did = open();

        try
        {
            long[] lsize = {1};
            for (int j=0; j<selectedDims.length; j++)
                lsize[0] *= selectedDims[j];

            fspace = H5.H5Dget_space(did);
            mspace = H5.H5Screate_simple(1, lsize, null);

            // set the rectangle selection
            H5.H5Sselect_hyperslab(
                fspace,
                HDF5Constants.H5S_SELECT_SET,
                startDims,
                selectedStride,
                selectedDims,
                null );   // set block to 1

            int idx = 0;
            boolean is_variable_str = false;
            boolean isVL = false;
            for (int i=0; i<numberOfMembers; i++)
            {
                if (!isMemberSelected[i])
                    continue; // the field is not selected

                member_name = memberNames[i];
                member_tid = memberTypes[i];
                member_data = list.get(idx++);
                try { member_class = H5.H5Tget_class(member_tid); } catch (HDF5Exception ex) {}

                try { is_variable_str = H5.H5Tis_variable_str(member_tid); } catch (Exception ex) {}
                try { isVL = (member_class==HDF5Constants.H5T_VLEN); } catch (Exception ex) {}

                if (member_data == null || is_variable_str || isVL)
                    continue;

                int arrayType = member_tid;
                int baseType = arrayType;
                if (member_class == HDF5Constants.H5T_ARRAY)
                {
                    try
                    {
                        int mn = H5.H5Tget_array_ndims(member_tid);
                        int[] marray = new int[mn];
                        H5.H5Tget_array_dims(member_tid, marray, null);
                        baseType = H5.H5Tget_super(member_tid);
                        arrayType = H5.H5Tarray_create (
                            H5Datatype.toNative(baseType),
                            mn,
                            marray,
                            null);
                    } catch (HDF5Exception ex) {}
                }

                int nested_tid = -1;
                try
                {
                    member_size = H5.H5Tget_size(member_tid);
                    // construct nested compound structure with a single field
                    String theName = member_name;
                    int tmp_tid = H5.H5Tcopy(arrayType);
                    int sep = member_name.lastIndexOf('.');

                    while (sep > 0)
                    {
                        theName = member_name.substring(sep+1);
                        nested_tid = H5.H5Tcreate(HDF5Constants.H5T_COMPOUND, member_size);
                        H5.H5Tinsert(nested_tid, theName, 0, tmp_tid);
                        try {H5.H5Tclose(tmp_tid);} catch (Exception ex) {}
                        tmp_tid = nested_tid;
                        member_name = member_name.substring(0, sep);
                        sep = member_name.lastIndexOf('.');
                    }

                    nested_tid = H5.H5Tcreate(HDF5Constants.H5T_COMPOUND, member_size);
                    H5.H5Tinsert(nested_tid, member_name, 0, tmp_tid);
                    try {H5.H5Tclose(tmp_tid);} catch (Exception ex) {}

                    Object tmpData = member_data;
                    if (H5Datatype.isUnsigned(baseType))
                        tmpData = convertToUnsignedC(buf);
                    else if (member_class == HDF5Constants.H5T_STRING) {
                        tmpData = stringToByte((String[])member_data, member_size);
                    }

                    // BUG!!! does not write nested data and no exception caght
                    // need to check if it is a java error or C library error
                    H5.H5Dwrite(
                        did,
                        nested_tid,
                        mspace,
                        fspace,
                        HDF5Constants.H5P_DEFAULT,
                        tmpData);
                } catch (HDF5Exception ex2)
                {
                    try { H5.H5Tclose(nested_tid); }
                    catch (HDF5Exception ex3) {}
                    if (fspace > 0) {
                        try { H5.H5Sclose(fspace); }
                        catch (HDF5Exception ex3) {}
                    }
                    continue;
                }
                try { H5.H5Tclose(nested_tid); } catch (HDF5Exception ex2) {}
            } // end of for (int i=0; i<num_members; i++)
        } finally
        {
            if (fspace > 0)
                try { H5.H5Sclose(fspace); } catch (Exception ex2) {}
            if (mspace > 0)
                try { H5.H5Sclose(mspace); } catch (Exception ex2) {}
            close(did);
        }

    }

    // To do: Implementing Dataset
    public Dataset copy(Group pgroup, String name, long[] dims, Object data)
    throws Exception { return null;}

    // Implementing DataFormat
    public List getMetadata() throws HDF5Exception
    {

        if (attributeList == null)
        {
            int did = open();
            attributeList = H5File.getAttribute(did);

            // get the compresson and chunk information
            int pid = H5.H5Dget_create_plist(did);
            if (H5.H5Pget_layout(pid) == HDF5Constants.H5D_CHUNKED)
            {
                if (rank <= 0) init();
                chunkSize = new long[rank];
                H5.H5Pget_chunk(pid, rank, chunkSize);
            }
            else chunkSize = null;

            int[] flags = {0, 0};
            int[] cd_nelmts = {2};
            int[] cd_values = {0,0};
            String[] cd_name ={"", ""};
            int nfilt = H5.H5Pget_nfilters(pid);
            int filter = -1;
            compression = "";

            for (int i=0; i<nfilt; i++)
            {
                if (i>0) compression += ", ";
                filter = H5.H5Pget_filter(pid, i, flags, cd_nelmts, cd_values, 120, cd_name);
                if (filter == HDF5Constants.H5Z_FILTER_DEFLATE)
                {
                    compression += "Deflate Level = "+cd_values[0];
                }
                else if (filter == HDF5Constants.H5Z_FILTER_FLETCHER32)
                {
                    compression += "Error detection filter";
                }
                else if (filter == HDF5Constants.H5Z_FILTER_SHUFFLE)
                {
                    compression += "Shuffle Nbytes = "+cd_values[0];
                }
                else if (filter == HDF5Constants.H5Z_FILTER_SZIP)
                {
                    compression += "SZIP: Pixels per block = "+cd_values[1];
                }
            } // for (int i=0; i<nfilt; i++)

            if (compression.length() == 0) compression = "NONE";

            try {
                int[] at = {0};
                H5.H5Pget_fill_time(pid, at);
                if (at[0] == HDF5Constants.H5D_ALLOC_TIME_EARLY)
                    compression += ", Allocation time: Early";
                else if (at[0] == HDF5Constants.H5D_ALLOC_TIME_INCR)
                    compression += ", Allocation time: Incremental";
                else if (at[0] == HDF5Constants.H5D_ALLOC_TIME_LATE)
                    compression += ", Allocation time: Late";
            } catch (Exception ex) { ;}

            if (pid >0) try {H5.H5Pclose(pid); } catch(Exception ex){}
            close(did);
        } // if (attributeList == null)

        return attributeList;
    }

    /**
     * Creates a new attribute and attached to this dataset if attribute does
     * not exist. Otherwise, just update the value of the attribute.
     *
     * <p>
     * @param info the atribute to attach
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
            attributeList = new Vector();
        else
            attrExisted = attributeList.contains(attr);

        getFileFormat().writeAttribute(this, attr, attrExisted);
        // add the new attribute into attribute list
        if (!attrExisted) attributeList.add(attr);
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
    public void close(int did)
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
        flatNameList = new Vector();
        flatTypeList = new Vector();

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
            for (int i=0; i<rank; i++)
            {
                startDims[i] = 0;
                selectedDims[i] = 1;
            }

            if (rank == 1)
            {
                selectedIndex[0] = 0;
                selectedDims[0] = dims[0];
            }
            else if (rank == 2)
            {
                selectedIndex[0] = 0;
                selectedIndex[1] = 1;
                selectedDims[0] = dims[0];
                selectedDims[1] = dims[1];
            }
            else if (rank > 2)
            {
                selectedIndex[0] = rank-2; // columns
                selectedIndex[1] = rank-1; // rows
                selectedIndex[2] = rank-3;
                selectedDims[rank-1] = dims[rank-1];
                selectedDims[rank-2] = dims[rank-2];
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

            extractCompoundInfo(tid, "", flatNameList, flatTypeList);
            numberOfMembers = flatNameList.size();

            memberNames = new String[numberOfMembers];
            memberTypes = new int[numberOfMembers];
            memberOrders = new int[numberOfMembers];
            isMemberSelected = new boolean[numberOfMembers];
            for (int i=0; i<numberOfMembers; i++)
            {
                isMemberSelected[i] = true;
                memberTypes[i] = ((Integer)flatTypeList.get(i)).intValue();
                memberNames[i] = (String)flatNameList.get(i);
                memberOrders[i] = 1;

                try { tclass = H5.H5Tget_class(memberTypes[i]); }
                catch (HDF5Exception ex ) {}

                if (tclass == HDF5Constants.H5T_ARRAY)
                {
                    int n = H5.H5Tget_array_ndims(memberTypes[i]);
                    int mdim[] = new int[n];
                    if (memberDims==null)
                        memberDims = new Object[numberOfMembers];
                    H5.H5Tget_array_dims(memberTypes[i], mdim, null);
                    memberDims[i] = mdim;
                    int tmptid = H5.H5Tget_super(memberTypes[i]);
                    memberOrders[i] = (int)(H5.H5Tget_size(memberTypes[i])/H5.H5Tget_size(tmptid));
                    try { H5.H5Tclose(tmptid); } catch (HDF5Exception ex) {}
                }
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

    /**
     * Sets the name of the data object.
     * <p>
     * @param newName the new name of the object.
     */
    public void setName (String newName) throws Exception
    {
        int linkType = HDF5Constants.H5G_LINK_HARD;

        String currentFullPath = getPath()+getName();
        String newFullPath = getPath()+newName;

        H5.H5Glink(getFID(), linkType, currentFullPath, newFullPath);
        H5.H5Gunlink(getFID(), currentFullPath);

        super.setName(newName);
    }

    /** extract compound information into flat structure
     * For example, compound data type "nest" has {nest1{a, b, c}, d, e}
     * then extractCompoundInfo() will put the names of nested compound
     * fields into flat structure of a list as
     * <pre>
     * nest.nest1.a
     * nest.nest1.b
     * nest.nest1.c
     * nest.d
     * nest.e
     * </pre>
     */
    private void extractCompoundInfo(int tid, String name, List names, List types)
    {
        int nMembers=0, mclass, mtype;
        String mname = null;

        try { nMembers = H5.H5Tget_nmembers(tid); }
        catch (Exception ex) { nMembers = 0; }

        if (nMembers <=0)
            return;

        for (int i=0; i<nMembers; i++)
        {
            int tmptid = -1;

            try {tmptid = H5.H5Tget_member_type(tid, i);}
            catch (Exception ex ) { continue; }

            mtype = H5Datatype.toNative(tmptid);

            try { H5.H5Tclose(tmptid); } catch (HDF5Exception ex) {}

            try { mclass = H5.H5Tget_class(mtype); }
            catch (HDF5Exception ex ) { continue; }

            mname = name+H5.H5Tget_member_name(tid, i);
            if (mclass == HDF5Constants.H5T_COMPOUND)
            {
                // nested compound
                extractCompoundInfo(mtype, mname+".", names, types);
                continue;
            }
            else if (mclass == HDF5Constants.H5T_ARRAY)
            {
                try {
                    tmptid = H5.H5Tget_super(mtype);
                    int tmpclass = H5.H5Tget_class(tmptid);

                    // cannot deal with ARRAY of COMPOUND or ARRAY
                    if (tmpclass == HDF5Constants.H5T_COMPOUND ||
                        tmpclass == HDF5Constants.H5T_ARRAY)
                        continue;
                } catch (Exception ex) {continue;}
            }

            names.add(mname);
            types.add(new Integer(mtype));
        } //for (int i=0; i<nMembers; i++)
    } //extractNestedCompoundInfo

    /**
     * creates a new compound dataset
     * @param name the name of the dataset
     * @param pgroup the parent group
     * @param dims the dimension size
     * @param memberNames the names of compound datatype
     * @param memberDatatypes the datatypes of the compound datatype
     * @param memberSizes the sizes of memeber array
     * @param data the initial data
     * @return the new compound dataset or null if failed
     * @throws Exception
     */
    public static Dataset create(
        String name,
        Group pgroup,
        long[] dims,
        String[] memberNames,
        Datatype[] memberDatatypes,
        int[] memberSizes,
        Object data) throws Exception
    {
        H5CompoundDS dataset = null;
        String fullPath = null;
        int did = -1;

        if (pgroup == null ||
            name == null ||
            dims == null ||
            memberNames == null ||
            memberDatatypes == null ||
            memberSizes == null)
            return null;

        H5File file = (H5File)pgroup.getFileFormat();
        if (file == null)
            return null;

        String path = HObject.separator;
        if (!pgroup.isRoot())
            path = pgroup.getPath()+pgroup.getName()+HObject.separator;
        fullPath = path +  name;

        int typeSize = 0;
        int nMembers = memberNames.length;
        int[] mTypes = new int[nMembers];
        for (int i=0; i<nMembers; i++)
        {
            if (memberSizes[i] > 1 && (memberDatatypes[i].getDatatypeClass() != Datatype.CLASS_STRING))
            {
                int[] mDim = {memberSizes[i]};
                mTypes[i] = H5.H5Tarray_create(memberDatatypes[i].toNative(),1, mDim, null);
            }
            else
                mTypes[i] = memberDatatypes[i].toNative();
            typeSize += H5.H5Tget_size(mTypes[i]);
        }

        int tid = H5.H5Tcreate(HDF5Constants.H5T_COMPOUND, typeSize);
        int offset = 0;
        for (int i=0; i<nMembers; i++)
        {
            H5.H5Tinsert(tid, memberNames[i], offset, mTypes[i]);
            offset += H5.H5Tget_size(mTypes[i]);
        }

        int rank = dims.length;
        int sid = H5.H5Screate_simple(rank, dims, null);

        int fid = file.open();
        did = H5.H5Dcreate(fid, fullPath, tid, sid, HDF5Constants.H5P_DEFAULT);

        try {H5.H5Sclose(sid);} catch (HDF5Exception ex) {};
        try {H5.H5Dclose(did);} catch (HDF5Exception ex) {};

        byte[] ref_buf = H5.H5Rcreate(
            fid,
            fullPath,
            HDF5Constants.H5R_OBJECT,
            -1);
        long l = HDFNativeData.byteToLong(ref_buf, 0);
        long[] oid = {l};
        dataset = new H5CompoundDS(file, name, path, oid);

        if (dataset != null)
        {
            pgroup.addToMemberList(dataset);

            if (data == null) {
                dataset.init();
                dataset.write(data);
            }
        }

        return dataset;
    }

}
