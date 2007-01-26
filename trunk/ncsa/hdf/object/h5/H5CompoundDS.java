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

import java.lang.reflect.Array;

/**
 * The H5CompoundDS class defines an HDF5 dataset of compound datatypes.
 * <p>
 * An HDF5 dataset is an object composed of a collection of data elements, 
 * or raw data, and metadata that stores a description of the data elements, 
 * data layout, and all other information necessary to write, read, and 
 * interpret the stored data.
 * <p>
 * A HDF5 compound datatype is similar to a struct in C or a common block in
 * Fortran: it is a collection of one or more atomic types or small arrays of
 * such types. Each member of a compound type has a name which is unique within
 * that type, and a byte offset that determines the first byte (smallest byte
 * address) of that member in a compound datum. 
 * <p>
 * For more information on HDF5 datasets and datatypes, read the 
 * <a href="http://hdf.ncsa.uiuc.edu/HDF5/doc/UG/index.html">HDF5 User's Guide</a>.
 * <p>
 * There are two basic types of compound datasets: simple compound data and
 * nested compound data. Members of a simple compound dataset have atomic datatyes.
 * Members of a nested compound dataset are compound or array of compound data. 
 * <p>
 * Since Java does not understand C structures, we cannot directly read/write compound
 * data values as in the following C example.  
 * <pre>
    typedef struct s1_t {
        int    a;
        float  b;
        double c; 
        } s1_t;
    s1_t       s1[LENGTH];
    ...
    H5Dwrite(..., s1);
    H5Dread(..., s1);
 * </pre>
 * 
 * Values of compound data fields are stored in java.util.Vector object. We read and write
 * compound data by fields instead of compound structure. As for the example above, the 
 * java.util.Vector object has three elements: int[LENGTH], float[LENGTH] and double[LENGTH].
 * Since Java understands the primitive datatypes of int, float and double, we will be able
 * to read/write the compound data by field.
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
	public static final long serialVersionUID = HObject.serialVersionUID;

    /**
     * The list of attributes of this data object. Members of the list are
     * instance of Attribute.
     */
    private List attributeList;

    /**
     * A list of names of all fields including nested fields.
     * The nested names are separated by CompoundDs.separator. For example, if
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
     * {m01, m02, nest1[m11, nest1[m12, nest1[nest2[m21, nest1[nest2[m22}
     *
     */
    private List flatNameList;

    /**
     * A list of datatypes of all fields including nested fields.
     */
    private List flatTypeList;

    /**
     * Flag to indicate if this is a compound dataset created by the HDF5
     *  indexing APIs.
     */
    private boolean isIndexTable;

    /**
     * Constrcuts an HDF5 compound dataset object for a given file, dataset name and group path.
     * <p>
     * The dataset object represents an existing dataset in the file. For example, 
     * new H5CompoundDS(file, "dset1", "/g0/") constructs a dataset object that corresponds to
     * the dataset,"dset1", at group "/g0/".
     * <p>
     * @param fileFormat the file that contains the dataset.
     * @param name the name of the dataset such as "dset1".
     * @param path the group path to the dataset such as "/g0/".
     */
    public H5CompoundDS(FileFormat fileFormat, String name, String path)
    {
        this(fileFormat, name, path, null);
    }

    /**
     * Constrcuts an HDF5 compound dataset object for a given file, dataset name, group path
     * and object identifer.
     * <p>
     * The dataset object represents an existing dataset in the file. For example, 
     * new H5CompoundDS(file, "dset1", "/g0/") constructs a dataset object that corresponds to
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
    public H5CompoundDS(
        FileFormat fileFormat,
        String name,
        String path,
        long[] oid)
    {
        super (fileFormat, name, path, oid);

        if (oid == null && fileFormat != null) {
            // retrieve the object ID
            try {
                byte[] ref_buf = H5.H5Rcreate(fileFormat.getFID(), this.getFullName(), HDF5Constants.H5R_OBJECT, -1);
                this.oid = new long[1];
                this.oid[0] = HDFNativeData.byteToLong(ref_buf, 0);
             } catch (Exception ex) {}
        }

        int did = open();
        isIndexTable = false;
        try { hasAttribute = (H5.H5Aget_num_attrs(did)>0); }
        catch (Exception ex) {}
        close(did);
    }

    /** 
     * Returns the datatype object (H5Datatype) of this dataset. 
     * @return the datatype object (H5Datatype) of this dataset. 
     */
    public Datatype getDatatype()
    {
        if (datatype == null)
            datatype = new H5Datatype(-1);

        return datatype;
    }
    
    /*
     * (non-Javadoc)
     * @see ncsa.hdf.object.Dataset#clear()
     */
    public void clear() {
    		super.clear(); 
    		
    	if (attributeList != null)
    		((Vector)attributeList).setSize(0);
    }

    /*
     * (non-Javadoc)
     * @see ncsa.hdf.object.Dataset#readBytes()
     */
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
        } finally
        {
            if (fspace > 0) try { H5.H5Sclose(fspace); } catch (Exception ex2) {}
            if (mspace > 0) try { H5.H5Sclose(mspace); } catch (Exception ex2) {}
            try { H5.H5Tclose(tid); } catch (HDF5Exception ex2) {}
            close(did);
        }

        return theData;
    }

    /**
     * Reads the content (data values) of this dataset into memory if the data of the
     * object is not loaded. If the content is already loaded, it returns the
     * memory buffer of the data. It returns null if the data object has no data or it fails
     * to load the data.
     * <p>
     * Data is read by compound field members and stored into a vector.
     * Each element of the vector is an one-dimension array that contains the field data.
     * The array type is determined by the datatype of the compound field.
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
     * read() returns a Vector that contains six arrays: int[], float[], char[], Stirng[], 
     *        long[] and double[].
     * 
     * @return the array of data List.
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

        /* external files will need this */
        String pdir = this.getFileFormat().getAbsoluteFile().getParent();
        if (pdir == null) pdir = ".";
        H5.H5Dchdir_ext(pdir);

        int did = open();

        /*
        // we still need to allocate memory space even if the storage space 
        // is not allocated because applications such as HDFView will need 
        // to write data into the dataset
        //
        // check is storage space is allocated
        try {
            if (H5.H5Dget_storage_size(did) <=0) {
                throw new HDF5Exception("Storage space is not allocated.");
            }
        } catch (Exception ex) {;}
        */
        
        /*
        // check is fill value is defined
        try {
            int plist = H5.H5Dget_create_plist(did);
            int[] fillValue = {0};
            H5.H5Pfill_value_defined(plist, fillValue);
            try { H5.H5Pclose(plist); } catch (Exception ex2) {}
            if (fillValue[0] == HDF5Constants.H5D_FILL_VALUE_UNDEFINED)
                throw new HDF5Exception("Fill value is not defined.");
        } catch (Exception ex) {ex.printStackTrace();}
         */

        list = new Vector(flatNameList.size()+2);

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
                // select all members for index table
                if (isIndexTable)
                    isMemberSelected[i] = true;
                else if (!isMemberSelected[i])
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
                    int sep = member_name.lastIndexOf(separator);
                    while (sep > 0) {
                        theName = member_name.substring(sep+1);
                        nested_tid = H5.H5Tcreate(HDF5Constants.H5T_COMPOUND, member_size);
                        H5.H5Tinsert(nested_tid, theName, 0, tmp_tid);
                        try {H5.H5Tclose(tmp_tid);} catch (Exception ex) {}
                        tmp_tid = nested_tid;
                        member_name = member_name.substring(0, sep);
                        sep = member_name.lastIndexOf(separator);
                    }

                    nested_tid = H5.H5Tcreate(HDF5Constants.H5T_COMPOUND, member_size);
                    H5.H5Tinsert(nested_tid, member_name, 0, tmp_tid);

                    try { is_variable_str = H5.H5Tis_variable_str(tmp_tid); } catch (Exception ex) {}
                    try { isVLEN = (H5.H5Tdetect_class(tmp_tid, HDF5Constants.H5T_VLEN)); } catch (Exception ex) {}
                    try {H5.H5Tclose(tmp_tid);} catch (Exception ex) {}

                    if (isVLEN || is_variable_str)
                        H5.H5DreadVL( did, nested_tid, mspace, fspace, HDF5Constants.H5P_DEFAULT, (Object[])member_data);
                    else
                        H5.H5Dread( did, nested_tid, mspace, fspace, HDF5Constants.H5P_DEFAULT, member_data);
                } catch (HDF5Exception ex2)
                {
                    if (fspace > 0) {
                        try { H5.H5Sclose(fspace); }  catch (HDF5Exception ex3) {}
                    }

                    String[] nullValues = new String[(int)lsize[0]];
                    for (int j=0; j<lsize[0]; j++) nullValues[j] = "*error*";
                    list.add(nullValues);

                    try { H5.H5Tclose(nested_tid); } catch (HDF5Exception ex3) {}
                    continue;
                }

                try { H5.H5Tclose(nested_tid); } catch (HDF5Exception ex2) {}

                if (!(isVLEN || is_variable_str || isIndexTable))
                {
                    if (member_class == HDF5Constants.H5T_STRING && convertByteToString) {
                        member_data = byteToString((byte[])member_data, member_size);
                    }
                    else if (member_class == HDF5Constants.H5T_REFERENCE) {
                        member_data = HDFNativeData.byteToLong((byte[])member_data);
                    }
                    else if (H5Datatype.isUnsigned(baseType)) {
                        member_data = Dataset.convertFromUnsignedC(member_data);
                    }
                    else if (member_class == HDF5Constants.H5T_ENUM)
                    {
                        try {
                            String[] strs = enumToNames(member_tid, member_data);
                            if (strs != null) member_data = strs;
                        } catch (Exception ex) {}
                    }
                }

                list.add(member_data);
            } // end of for (int i=0; i<num_members; i++)

            /* TODO:
            if (isIndexTable) {
                try { queryIndexSpace(did, list); }
                catch (Exception ex) {}
            }
            */
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

    /**
     * Writes the given data buffer into this dataset in file.
     * <p>
     * The data buffer is a vector that contains the data values of compound fields. 
     * The data is written into file field by field.
     * @param buf The vector that contains the data values of compound fields.
     */
    public void write(Object buf) throws HDF5Exception
    {
        if (buf == null || numberOfMembers <= 0 ||
            !(buf instanceof List) || isIndexTable)
            return;

        List list = (List)buf;
        Object member_data = null;
        String member_name = null;
        int member_tid=-1, member_class=-1, member_size=0, fspace=-1, mspace=-1;
        int did = open();

        try
        {
            long[] lsize = {1};
            for (int j=0; j<selectedDims.length; j++) {
                lsize[0] *= selectedDims[j];
            }

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

                if ( member_data == null
                     || is_variable_str
                     || isVL
                     || (member_class== HDF5Constants.H5T_ENUM)
                    )
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
                        tmpData = convertToUnsignedC(member_data);
                    else if (member_class == HDF5Constants.H5T_STRING &&
                             (Array.get(member_data, 0) instanceof String)) {
                        tmpData = stringToByte((String[])member_data, member_size);
                    }

                    if (tmpData == null)
                        continue;

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

    /**
     * Reads attributes of this dataset from file into memory if the attributes
     * are not in memory. If the attributes are in memory, it returns the attributes.
     * The attributes (@see ncsa.hdf.object.Attribute) are stored in a vector.
     *
     * @return the vector that contains the attributes of the dataset.
     * @see <a href="http://java.sun.com/j2se/1.5.0/docs/api/java/util/List.html">java.util.List</a>
     */
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
                    compression += "GZIP: level = "+cd_values[0];
                }
                else if (filter == HDF5Constants.H5Z_FILTER_FLETCHER32)
                {
                    compression += "Error detection filter";
                }
                else if (filter == HDF5Constants.H5Z_FILTER_SHUFFLE)
                {
                    compression += "SHUFFLE: Nbytes = "+cd_values[0];
                }
                else if (filter == HDF5Constants.H5Z_FILTER_SZIP)
                {
                    compression += "SZIP: Pixels per block = "+cd_values[1];
                    int flag = -1;
                    try { flag = H5.H5Zget_filter_info(filter); }
                    catch (Exception ex) { flag = -1; }
                    if (flag==HDF5Constants.H5Z_FILTER_CONFIG_DECODE_ENABLED)
                        compression += ": "+Dataset.H5Z_FILTER_CONFIG_DECODE_ENABLED;
                    else if (flag==HDF5Constants.H5Z_FILTER_CONFIG_ENCODE_ENABLED ||
                             flag >= (HDF5Constants.H5Z_FILTER_CONFIG_ENCODE_ENABLED+HDF5Constants.H5Z_FILTER_CONFIG_DECODE_ENABLED))
                        compression += ": "+Dataset.H5Z_FILTER_CONFIG_ENCODE_ENABLED;
                }
            } // for (int i=0; i<nfilt; i++)

            if (compression.length() == 0) compression = "NONE";

            try {
                int[] at = {0};
                H5.H5Pget_fill_time(pid, at);
                compression += ",         Fill value allocation time: ";
                if (at[0] == HDF5Constants.H5D_ALLOC_TIME_EARLY)
                    compression += "Early";
                else if (at[0] == HDF5Constants.H5D_ALLOC_TIME_INCR)
                    compression += "Incremental";
                else if (at[0] == HDF5Constants.H5D_ALLOC_TIME_LATE)
                    compression += "Late";
            } catch (Exception ex) { ;}

            if (pid >0) try {H5.H5Pclose(pid); } catch(Exception ex){}
            close(did);
        } // if (attributeList == null)

        return attributeList;
    }

    /**
     * Creates and attaches a new attribute if the attribute does not exist in file.
     * If the attribute already exists in file, it writes the value of the attribute in file.
     *
     * @param info the attribute to write
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
     * Deletes an attribute from this dataset in file.
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

    /**
     * Opens an existing dataset for access in the file. 
     * The return value is a dataset identifier obtained by H5.H5Dopen(). This function is
     * needed to allows other objects to be able to access the dataset. For instance, H5File
     * class uses the open() function to obtain object identifier for 
     * copyAttributes(int src_id, int dst_id) and other purposes. The open() function should
     * be used in pair with close(int) function.
     * 
     * @see ncsa.hdf.object.h5.H5CompoundDS#close(int)
     *
     * @return the dataset identifier if successful; otherwise returns a negative value.
     */
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
    

    /**
     * Calls H5.H5Dclose(int) to close the specified dataset id.
     * The specified dataset id is obtained by the open() function.
     * 
     * @see ncsa.hdf.object.h5.H5CompoundDS#open()
     * 
     * @param did The identifier of the dataset to close access to
     */
    public void close(int did)
    {
        try { H5.H5Fflush(did, HDF5Constants.H5F_SCOPE_LOCAL); } catch (Exception ex) {}
        try { H5.H5Dclose(did); } catch (HDF5Exception ex) { ; }
    }

    /*
     * (non-Javadoc)
     * @see ncsa.hdf.object.Dataset#init()
     */
    public void init()
    {
        if (rank>0)
            return; // already called. Initialize only once

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

            /* check is it is an index table */
            int aid=-1, atid=-1, asid=-1;
            try
            {
                aid = H5.H5Aopen_name(did, "CLASS");
                atid = H5.H5Aget_type(aid);
                int aclass = H5.H5Tget_class(atid);
                if (aclass == HDF5Constants.H5T_STRING)
                {
                    int size = H5.H5Tget_size(atid);
                    byte[] attrValue = new byte[size];
                    H5.H5Aread(aid, atid, attrValue);
                    String strValue = new String(attrValue).trim();
                    isIndexTable = strValue.equalsIgnoreCase("INDEX");
                }
            } catch (Exception ex2) {}
            finally
            {
                try { H5.H5Sclose(asid); } catch (HDF5Exception ex) {;}
                try { H5.H5Tclose(atid); } catch (HDF5Exception ex) {;}
                try { H5.H5Aclose(aid); } catch (HDF5Exception ex) {;}
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
            close(did);
        }
    }

    /**
     * Renames the object in file.
     * <p>
     * This function calls H5.H5Gmove() to rename the object in file.
     * 
     * @param newName the new name of the object.
     */
    public void setName (String newName) throws Exception
    {
        String currentFullPath = getPath()+getName();
        String newFullPath = getPath()+newName;

        H5.H5Gmove(getFID(), currentFullPath, newFullPath);
        
        /*
        H5.H5Glink(getFID(), HDF5Constants.H5G_LINK_HARD, currentFullPath, newFullPath);
        H5.H5Gunlink(getFID(), currentFullPath);
        */

        super.setName(newName);
    }

    /**
     * Extracts compound information into flat structure.
     * <p>
     * For example, compound data type "nest" has {nest1{a, b, c}, d, e}
     * then extractCompoundInfo() will put the names of nested compound
     * fields into a flat list as
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
                extractCompoundInfo(mtype, mname+separator, names, types);
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
     * Creates a simple compound dataset in file
     * <p>
     * This functoin calls H5.H5Dcreate() to create a simple compound dataset in file.
     * For details, 
     * @see ncsa.hdf.object.h5.H5CompoundDS#create(String, Group, long[], long[], long[], int, String[], Datatype[], int[], int[][], Object)
     *
     * @param name the name of the dataset
     * @param pgroup the parent group
     * @param dims the dimension size
     * @param memberNames the names of compound datatype
     * @param memberDatatypes the datatypes of the compound datatype
     * @param memberSizes the sizes of memeber array
     * @param data the initial data
     * @return the new compound dataset if successful; otherwise returns null
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
        if (pgroup == null ||
            name == null ||
            dims == null ||
            memberNames == null ||
            memberDatatypes == null ||
            memberSizes == null)
            return null;

        int nMembers = memberNames.length;
        int memberRanks[] = new int[nMembers];
        int memberDims[][] = new int[nMembers][1];
        for (int i=0; i<nMembers; i++)
        {
            memberRanks[i] = 1;
            memberDims[i][0] = memberSizes[i];
        }

        return H5CompoundDS.create(name, pgroup, dims, memberNames,
               memberDatatypes, memberRanks, memberDims, data);
    }

    /**
     * Creates a simple compound dataset in file
     * <p>
     * This functoin calls H5.H5Dcreate() to create a simple compound dataset in file.
     * For details, 
     * @see ncsa.hdf.object.h5.H5CompoundDS#create(String, Group, long[], long[], long[], int, String[], Datatype[], int[], int[][], Object)
     *
     * @param name the name of the dataset
     * @param pgroup the parent group
     * @param dims the dimension size
     * @param memberNames the names of compound datatype
     * @param memberDatatypes the datatypes of the compound datatype
     * @param memberRanks the ranks of the members
     * @param memberDims the dim sizes of the members
     * @param data the initial data
     * @return the new compound dataset if successful; otherwise returns null
     */
    public static Dataset create(
        String name,
        Group pgroup,
        long[] dims,
        String[] memberNames,
        Datatype[] memberDatatypes,
        int[] memberRanks,
        int[][] memberDims,
        Object data) throws Exception
    {
        return H5CompoundDS.create(name, pgroup, dims, null, null, -1,
               memberNames, memberDatatypes, memberRanks, memberDims, data);
    }

    /**
     * Creates a simple compound dataset in file
     * <p>
     * This function provides an easy way to create a simple compound dataset
     * in file by hiding tedious details of creating a compound dataset from users.  
     * <p>
     * This functoin calls H5.H5Dcreate() to create a simple compound dataset in file.
     * Nested compound dataset is not supported. The required information to create
     * a compound dataset includes the name, the parent group  and data space of the dataset, 
     * the names, datatypes and data spaces of the compound fields. Other information such
     * as chunks, compression and the data buffer is optional.
     * <p>
     * The following example shows how to use this function to create a compound dataset
     * in file.
     * 
     * <pre>
        H5File file=null;
        String message = "";
        Group pgroup = null;
        int[] DATA_INT = new int[DIM_SIZE];
        float[] DATA_FLOAT = new float[DIM_SIZE];
        String[] DATA_STR = new String[DIM_SIZE];
        long[] DIMs = {50, 10};
        long[] CHUNKs = {25, 5};

        try {
            file = (H5File)H5FILE.open(fname, H5File.CREATE);
            file.open();
            pgroup = (Group)file.get("/");
        } catch (Exception ex) {}

        Vector data = new Vector();
        data.add(0, DATA_INT);
        data.add(1, DATA_FLOAT);
        data.add(2, DATA_STR);

        // create groups
        Datatype[]  mdtypes = new H5Datatype[3];
        String[] mnames = {"int", "float", "string"};
        Dataset dset = null;
        try {
            mdtypes[0] = new H5Datatype(Datatype.CLASS_INTEGER, -1, -1, -1);
            mdtypes[1] = new H5Datatype(Datatype.CLASS_FLOAT, -1, -1, -1);
            mdtypes[2] = new H5Datatype(Datatype.CLASS_STRING, STR_LEN, -1, -1);
            dset = file.createCompoundDS("/CompoundDS", pgroup, DIMs, null, CHUNKs, 9, mnames, mdtypes, null, data);
        } catch (Exception ex) { failed(message, ex, file); return 1;}
     * </pre>
     *
     * @param name the name of the dataset
     * @param pgroup the parent group
     * @param dims the dimension size
     * @param maxdims the max dimension sizes
     * @param chunks the chunk sizes
     * @param gzip the gzip compression level
     * @param memberNames the names of compound datatype
     * @param memberDatatypes the datatypes of the compound datatype
     * @param memberRanks the ranks of the members
     * @param memberDims the dim sizes of the members
     * @param data the initial data
     * 
     * @return the new compound dataset if successful; otherwise returns null
     */
    public static Dataset create(
        String name,
        Group pgroup,
        long[] dims,
        long[] maxdims,
        long[] chunks,
        int gzip,
        String[] memberNames,
        Datatype[] memberDatatypes,
        int[] memberRanks,
        int[][] memberDims,
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
            memberRanks == null ||
            memberDims == null)
            return null;

        H5File file = (H5File)pgroup.getFileFormat();
        if (file == null)
            return null;

        String path = HObject.separator;
        if (!pgroup.isRoot()) {
            path = pgroup.getPath()+pgroup.getName()+HObject.separator;
            if (name.endsWith("/"))
                name = name.substring(0, name.length()-1);
                int idx = name.lastIndexOf("/");
                if (idx >=0)
                    name = name.substring(idx+1);
        }

        fullPath = path +  name;

        int typeSize = 0;
        int nMembers = memberNames.length;
        int[] mTypes = new int[nMembers];
        int memberSize = 1;
        for (int i=0; i<nMembers; i++)
        {
            memberSize = 1;
            for (int j=0; j<memberRanks[i]; j++)
                memberSize *= memberDims[i][j];

            if (memberSize > 1 && (memberDatatypes[i].getDatatypeClass() != Datatype.CLASS_STRING))
                mTypes[i] = H5.H5Tarray_create(memberDatatypes[i].toNative(),memberRanks[i], memberDims[i], null);
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
        int sid = H5.H5Screate_simple(rank, dims, maxdims);

        // setup chunking and compression
        boolean isExtentable = false;
        if (maxdims != null)
        {
            for (int i=0; i<maxdims.length; i++)
            {
                if (maxdims[i] == 0)
                    maxdims[i] = dims[i];
                else if (maxdims[i] < 0)
                    maxdims[i] = HDF5Constants.H5S_UNLIMITED;

                if (maxdims[i] != dims[i])
                   isExtentable = true;
            }
        }
        // HDF 5 requires you to use chunking in order to define extendible
        // datasets. Chunking makes it possible to extend datasets efficiently,
        // without having to reorganize storage excessively
        if (chunks == null && isExtentable)
            chunks = dims;

        int plist = HDF5Constants.H5P_DEFAULT;
        if (chunks != null)
        {
            plist = H5.H5Pcreate (HDF5Constants.H5P_DATASET_CREATE);
            H5.H5Pset_layout(plist, HDF5Constants.H5D_CHUNKED);
            H5.H5Pset_chunk(plist, rank, chunks);
            
            // compression requires chunking
            if (gzip > 0)
                H5.H5Pset_deflate(plist, gzip);
        }
        
        int fid = file.open();
        did = H5.H5Dcreate(fid, fullPath, tid, sid, plist);

        //for (int i=0; i<nMembers; i++)
        //    try { H5.H5Tclose(mTypes[i]);} catch (HDF5Exception ex) {};
        
        try {H5.H5Pclose(plist);} catch (HDF5Exception ex) {};
        try {H5.H5Sclose(sid);} catch (HDF5Exception ex) {};
        try {H5.H5Tclose(tid);} catch (HDF5Exception ex) {};
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

            if (data != null) {
                dataset.init();
                long selected[] = dataset.getSelectedDims();
                for (int i=0; i<rank; i++) selected[i] = dims[i];
                dataset.write(data);
            }
        }

        return dataset;
    }

    /**
     * Converts enum values to string names
     *
     * @param tid the datatype idenfifier of the enum
     * @param in the input array of enum values
     * @return the string array of enum names if successful; otherwise return null;
     */
    private String[] enumToNames(int tid, Object in) throws Exception
    {
        int size = 0;

        if (in == null || (size = Array.getLength(in)) <=0)
            return null;

        int n = H5.H5Tget_nmembers(tid);
        if (n <=0 ) return null;

        String[] out = new String[size];
        String[] names = new String[n];
        int[] values = new int[n];
        int[] theValue = {0};

        for (int i=0; i<n; i++)
        {
            names[i] = H5.H5Tget_member_name(tid, i);
            H5.H5Tget_member_value(tid, i, theValue);
            values[i] = theValue[0];
        }

        int val = -1;
        int tsize = H5.H5Tget_size(tid);
        for (int i=0; i<size; i++)
        {
            val = Array.getInt(in, i);
            for (int j=0; j<n; j++)
            {
                if (val == values[j])
                {
                    out[i] = names[j];
                    break;
                }
            }
        } //for (int i=0; i<values.length; i++)

        return out;
    }

    /**
     * Checks if it is a string for the given datatype identifier.
     * 
     * This function uses (HDF5Constants.H5T_STRING == H5.H5Tget_class(tid) )
     * to check if the given datatype is a string.
     * 
     * @param tid The data type id to check.
     * 
     * @return true if the datatype is a string; otherwise returns flase.
     */
    public boolean isString(int tid)
    {
        boolean b = false;
        try { b = (HDF5Constants.H5T_STRING == H5.H5Tget_class(tid) ); }
        catch (Exception ex) { b = false; }

        return b;
    }

    /**
     * Returns the size (in bytes) of a given datatype identifier.
     * 
     * This function calls H5.H5Tget_size(tid) to get the size of
     * the datatype.
     * 
     * @param tid The data type identifier.
     * 
     * @return The size of the datatype in bytes.
     */
    public int getSize(int tid)
    {
        int tsize = -1;

        try { tsize = H5.H5Tget_size(tid); }
        catch (Exception ex) { tsize = -1; }

        return tsize;
    }
   
}
