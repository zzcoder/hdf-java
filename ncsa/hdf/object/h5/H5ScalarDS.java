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
 * H5ScalarDS describes a multi-dimension array of HDF5 scalar or atomic data
 * types, such as byte, int, short, long, float, double and string, and
 * operations performed on the scalar dataset.
 * <p>
 * The library predefines a modest number of datatypes. For details, read
 * <a href="http://hdf.ncsa.uiuc.edu/HDF5/doc/Datatypes.html">The Datatype Interface (H5T).</a>
 * <p>
 * <b>How to Select a Subset</b>
 * <p>
 * Dataset defines APIs for read, write and subset a dataset. No function is defined
 * to select a subset of a data array. The selection is done in an implicit way.
 * Function calls to dimension information such as getSelectedDims() returns an array
 * of dimension values, which is a reference to the array in the dataset object.
 * Changes of the dimesion array outside the dataset object directly change the values of
 * the array in the dataset object. It is like pointers in C.
 * <p>
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
public class H5ScalarDS extends ScalarDS
{
	public static final long serialVersionUID = HObject.serialVersionUID;

    /**
     * The list of attributes of this data object. Members of the list are
     * instance of Attribute.
     */
     private List attributeList;

     /** The byte array containing references of palettes.
      * Each reference requires  eight bytes storage. Therefore, the array length
      * is 8*numberOfPalettes.
     */
     private byte[] paletteRefs;

     /** flag to indicate if the dataset is a variable length */
     private boolean isVLEN = false;

    /**
     * Constructs an H5ScalarDS object with specific name and path.
     * <p>
     * @param fileFormat the HDF file.
     * @param name the name of this H5ScalarDS.
     * @param path the full path of this H5ScalarDS.
     */
    public H5ScalarDS(FileFormat fileFormat, String name, String path)
    {
        this(fileFormat, name, path, null);
    }

    /**
     * Constructs an H5ScalarDS object with specific name and group path.
     * <p>
     * @param fileFormat the HDF file.
     * @param name the name of this H5ScalarDS.
     * @param path the full path of this H5ScalarDS.
     * @param oid the unique identifier of this data object, or null is the oid is unknown.
     */
    public H5ScalarDS(
        FileFormat fileFormat,
        String name,
        String path,
        long[] oid)
    {
        super (fileFormat, name, path, oid);
        unsignedConverted = false;
        paletteRefs = null;

        // test if it is an image
        int did = open();
        int aid=-1, atid=-1, tid=0, asid=-1;
        try
        {
            hasAttribute = (H5.H5Aget_num_attrs(did)>0);
            tid= H5.H5Dget_type(did);
            int tclass = H5.H5Tget_class(tid);
            isText = (tclass==HDF5Constants.H5T_STRING);
            isVLEN = (tclass==HDF5Constants.H5T_VLEN || H5.H5Tis_variable_str(tid));

            // try to find out if the dataset is an image
            aid = H5.H5Aopen_name(did, "CLASS");
            atid = H5.H5Aget_type(aid);
            int aclass = H5.H5Tget_class(atid);
            if (aclass == HDF5Constants.H5T_STRING)
            {
                int size = H5.H5Tget_size(atid);
                byte[] attrValue = new byte[size];
                H5.H5Aread(aid, atid, attrValue);
                String strValue = new String(attrValue).trim();
                isImage = strValue.equalsIgnoreCase("IMAGE");
                isImageDisplay = isImage;
            }
        } catch (Exception ex) {}
        finally
        {
            try { H5.H5Sclose(asid); } catch (HDF5Exception ex) {;}
            try { H5.H5Tclose(atid); } catch (HDF5Exception ex) {;}
            try { H5.H5Aclose(aid); } catch (HDF5Exception ex) {;}
            try { H5.H5Tclose(tid); } catch (HDF5Exception ex) {;}
        }

        // retrieve the IMAGE_MINMAXRANGE
        int anativeType = -1;
        try
        {
            // try to find out if the dataset is an image
            aid = H5.H5Aopen_name(did, "IMAGE_MINMAXRANGE");
            if (aid > 0)
            {
                atid = H5.H5Aget_type(aid);
                anativeType = H5.H5Tget_native_type(atid);
                asid = H5.H5Aget_space(aid);
                long adims[] = null;

                int arank = H5.H5Sget_simple_extent_ndims(asid);
                if (arank > 0)
                {
                    adims = new long[rank];
                    H5.H5Sget_simple_extent_dims(asid, adims, null);
                }

                // retrieve the attribute value
                long lsize = 1;
                for (int j=0; j<adims.length; j++) lsize *= adims[j];
                Object avalue = H5Datatype.allocateArray(anativeType, (int)lsize);
                if (avalue != null)
                {
                    H5.H5Aread(aid, anativeType, avalue);
                    double x0=0, x1=0;
                    try {
                        x0 = Double.valueOf(java.lang.reflect.Array.get(avalue, 0).toString()).doubleValue();
                        x1 = Double.valueOf(java.lang.reflect.Array.get(avalue, 1).toString()).doubleValue();
                    } catch (Exception ex2) { x0=x1=0;}
                    if (x1 > x0)
                    {
                        imageDataRange = new double[2];
                        imageDataRange[0] = x0;
                        imageDataRange[1] = x1;
                    }
                }

            } // if (aid > 0)
        } catch (Exception ex) {}
        finally
        {
            try { H5.H5Tclose(anativeType); } catch (HDF5Exception ex) {;}
            try { H5.H5Tclose(atid); } catch (HDF5Exception ex) {;}
            try { H5.H5Sclose(asid); } catch (HDF5Exception ex) {;}
            try { H5.H5Aclose(aid); } catch (HDF5Exception ex) {;}
        }
        
        close(did);

    }
    

    /*
     * (non-Javadoc)
     * @see ncsa.hdf.object.Dataset#init()
     */
    public void init()
    {
        if (rank>0)
            return; // already called. Initialize only once

        int did=-1, sid=-1, tid=-1, pid=-1;

        did = open();
        paletteRefs = getPaletteRefs(did);

        try {
            sid = H5.H5Dget_space(did);
            tid= H5.H5Dget_type(did);
            rank = H5.H5Sget_simple_extent_ndims(sid);
            isText = (H5.H5Tget_class(tid)==HDF5Constants.H5T_STRING);

            if (rank == 0) {
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
        } catch (HDF5Exception ex) {}
        finally
        {
            try { nativeDatatype = H5.H5Tget_native_type(tid); } catch (HDF5Exception ex2) {}
            try { H5.H5Tclose(tid); } catch (HDF5Exception ex2) {}
            try { H5.H5Sclose(sid); } catch (HDF5Exception ex2) {}
        }

        isUnsigned = H5Datatype.isUnsigned(nativeDatatype);

        int aid=-1, atid=-1;

        if (!isImage)
        {
            try
            {
                // try to find out if the image type
                aid = H5.H5Aopen_name(did, "CLASS");
                atid = H5.H5Aget_type(aid);
                int size = H5.H5Tget_size(atid);
                byte[] attrValue = new byte[size];
                H5.H5Aread(aid, atid, attrValue);
                String strValue = new String(attrValue).trim();
                isImage = strValue.equalsIgnoreCase("IMAGE");
            } catch (Exception ex) {}
            finally
            {
                try { H5.H5Tclose(atid); } catch (HDF5Exception ex) {;}
                try { H5.H5Aclose(aid); } catch (HDF5Exception ex) {;}
            }
        }

        // check for the type of image and interlace mode
        // it is a true color image at one of three cases:
        // 1) IMAGE_SUBCLASS = IMAGE_TRUECOLOR,
        // 2) INTERLACE_MODE = INTERLACE_PIXEL,
        // 3) INTERLACE_MODE = INTERLACE_PLANE
        if (rank >=3 && isImage)
        {
            interlace = INTERLACE_PIXEL;
            try
            {
                // try to find out if the image type
                aid = H5.H5Aopen_name(did, "IMAGE_SUBCLASS");
                atid = H5.H5Aget_type(aid);
                int size = H5.H5Tget_size(atid);
                byte[] attrValue = new byte[size];
                H5.H5Aread(aid, atid, attrValue);
                String strValue = new String(attrValue).trim();
                if (strValue.equalsIgnoreCase("IMAGE_INDEXED"))
                    interlace = -1; // default interlace
            } catch (Exception ex) {}
            finally
            {
                try { H5.H5Tclose(atid); } catch (HDF5Exception ex) {;}
                try { H5.H5Aclose(aid); } catch (HDF5Exception ex) {;}
            }

            try
            {
                // try to find out interlace mode
                aid = H5.H5Aopen_name(did, "INTERLACE_MODE");
                atid = H5.H5Aget_type(aid);
                int size = H5.H5Tget_size(atid);
                byte[] attrValue = new byte[size];
                H5.H5Aread(aid, atid, attrValue);
                String strValue = new String(attrValue).trim();
                if (strValue.equalsIgnoreCase("INTERLACE_PLANE"))
                    interlace = INTERLACE_PLANE;
            } catch (Exception ex) {}
            finally
            {
                try { H5.H5Tclose(atid); } catch (HDF5Exception ex) {;}
                try { H5.H5Aclose(aid); } catch (HDF5Exception ex) {;}
            }

            if ((interlace == INTERLACE_PLANE && dims[0] <3) ||
                (interlace == INTERLACE_PIXEL && dims[2] <3))
            {
                // must have at least three color planes
                interlace = -1;
            }

            isTrueColor = (interlace == ScalarDS.INTERLACE_PIXEL ||
                interlace == ScalarDS.INTERLACE_PLANE);
        }


        startDims = new long[rank];
        selectedDims = new long[rank];
        for (int i=0; i<rank; i++) {
            startDims[i] = 0;
            selectedDims[i] = 1;
        }

        if (interlace == INTERLACE_PIXEL)
        {
            // 24-bit TRUE color image
            // [height][width][pixel components]
            selectedDims[2] = 3;
            selectedDims[0] = dims[0];
            selectedDims[1] = dims[1];
            selectedIndex[0] = 0; // index for height
            selectedIndex[1] = 1; // index for width
            selectedIndex[2] = 2; // index for depth
        }
        else if (interlace == INTERLACE_PLANE)
        {
            // 24-bit TRUE color image
            // [pixel components][height][width]
            selectedDims[0] = 3;
            selectedDims[1] = dims[1];
            selectedDims[2] = dims[2];
            selectedIndex[0] = 1; // index for height
            selectedIndex[1] = 2; // index for width
            selectedIndex[2] = 0; // index for depth
        }
        else if (rank == 1)
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
            // 3D dataset is arranged in the order of [frame][height][width] by default
            selectedIndex[1] = rank-1; // width, the fastest dimension
            selectedIndex[0] = rank-2; // height
            selectedIndex[2] = rank-3; // frames
            selectedDims[selectedIndex[0]] = dims[selectedIndex[0]];
            selectedDims[selectedIndex[1]] = dims[selectedIndex[1]];
            
            /*
            // In the case of images with only one component, the dataspace may
            // be either a two dimensional array, or a three dimensional array
            // with the third dimension of size 1.  For example, a 5 by 10 image
            // with 8 bit color indexes would be an HDF5 dataset with type
            // unsigned 8 bit integer.  The dataspace could be either a two
            // dimensional array, with dimensions [10][5], or three dimensions,
            // with dimensions either [10][5][1] ([height][width][1])
            // or [1][10][5] ([1][height][width]).
            if (dims[0] == 1)
            {
                // case [1][10][5]
                selectedIndex[0] = 1;
                selectedIndex[1] = 2;
                selectedIndex[2] = 0;
                selectedDims[1] = dims[1];
                selectedDims[2] = dims[2];
            }
            else
            {
                // case [10][5][1]
                selectedIndex[0] = 0;
                selectedIndex[1] = 1;
                selectedIndex[2] = 2;
                selectedDims[0] = dims[0];
                selectedDims[1] = dims[1];
            }
            */
        }

        if (rank > 1 && isText)
        {
            selectedIndex[0] = rank-1;
            selectedIndex[1] = 0;
            selectedDims[0] = 1;
            selectedDims[selectedIndex[0]] = dims[selectedIndex[0]];
        }
        
        close(did);
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
     * @see ncsa.hdf.object.Dataset#copy(ncsa.hdf.object.Group, java.lang.String, long[], java.lang.Object)
     */
    public Dataset copy(Group pgroup, String dstName, long[] dims, Object buff) throws Exception
    {
        if (pgroup == null)
            return null;
        
        Dataset dataset = null;
        int srcdid, dstdid, tid, sid, plist;
        String dname=null, path=null;

        if (pgroup.isRoot()) path = HObject.separator;
        else path = pgroup.getPath()+pgroup.getName()+HObject.separator;
        dname = path + dstName;

        srcdid = open();
        tid = H5.H5Dget_type(srcdid);
        sid = H5.H5Screate_simple(dims.length, dims, null);
        plist = H5.H5Dget_create_plist(srcdid);
        dstdid = H5.H5Dcreate(pgroup.getFID(), dname, tid, sid, plist);

        // write data values
        if (buff != null)
            H5.H5Dwrite(dstdid, tid, HDF5Constants.H5S_ALL, HDF5Constants.H5S_ALL, HDF5Constants.H5P_DEFAULT, buff);

        // copy attributes from one object to the new object
        ((H5File)getFileFormat()).copyAttributes(srcdid, dstdid);

        byte[] ref_buf = H5.H5Rcreate(
            pgroup.getFID(),
            dname,
            HDF5Constants.H5R_OBJECT,
            -1);
        long l = HDFNativeData.byteToLong(ref_buf, 0);
        long[] oid = {l};

        dataset = new H5ScalarDS(pgroup.getFileFormat(), dstName, path, oid);

        pgroup.addToMemberList(dataset);

        try { H5.H5Pclose(plist); } catch(Exception ex) {}
        try { H5.H5Sclose(sid); } catch(Exception ex) {}
        try { H5.H5Tclose(tid); } catch(Exception ex) {}
        try { H5.H5Dclose(srcdid); } catch(Exception ex) {}
        try { H5.H5Dclose(dstdid); } catch(Exception ex) {}

        return dataset;
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

    /*
     * (non-Javadoc)
     * @see ncsa.hdf.object.Dataset#read()
     */
    public Object read() throws HDF5Exception
    {
        Object theData = null;

        int fspace=-1, mspace=-1;

        if (rank <= 0) init();

        /* external files will need this */
        String pdir = this.getFileFormat().getAbsoluteFile().getParent();
        if (pdir == null) pdir = ".";
        H5.H5Dchdir_ext(pdir);
        
        int did = open();

        // check is storage space is allocated
        try {
            if (H5.H5Dget_storage_size(did) <=0)
            {
                throw new HDF5Exception("Storage space is not allocated.");
            }
        } catch (Exception ex) {}

        // check is fill value is defined
/*        
        try {
            int plist = H5.H5Dget_create_plist(did);
            int[] fillStatus = {0};
            H5.H5Pfill_value_defined(plist, fillStatus);
            try { H5.H5Pclose(plist); } catch (Exception ex2) {}
            if (fillStatus[0] == HDF5Constants.H5D_FILL_VALUE_UNDEFINED)
            {
                throw new HDF5Exception("Fill value is not defined.");
            }
        } catch (Exception ex) {}
*/
        try {
            long[] lsize = {1};
            for (int j=0; j<selectedDims.length; j++)
                lsize[0] *= selectedDims[j];

            if (lsize[0] == 0) {
                throw new HDF5Exception("No data to read.\nEither the dataset or the selected subset is empty.");
            }

            fspace = H5.H5Dget_space(did);
            mspace = H5.H5Screate_simple(1, lsize, null);

            // set the rectangle selection
            // HDF5 bug: for scalar dataset, H5Sselect_hyperslab gives core dump
            if (rank*dims[0] > 1) {
                H5.H5Sselect_hyperslab(
                    fspace,
                    HDF5Constants.H5S_SELECT_SET,
                    startDims,
                    selectedStride,
                    selectedDims,
                    null );   // set block to 1
            }

            /* do not support dataset region references */
            if (H5.H5Tequal(nativeDatatype, HDF5Constants.H5T_STD_REF_DSETREG)) {
                throw new HDF5Exception("Dataset region reference is not supported.");
             }
            
            boolean isREF = (H5.H5Tequal(nativeDatatype, HDF5Constants.H5T_STD_REF_OBJ));
            
            if ( originalBuf ==null || isText || isREF ||
                (originalBuf!=null && lsize[0] !=nPoints)) {
                theData = H5Datatype.allocateArray(nativeDatatype, (int)lsize[0]);
            } else
                 theData = originalBuf; // reuse the buffer if the size is the same
            
            if (theData != null) {
                if (isVLEN)
                {
                    H5.H5DreadVL(did, nativeDatatype, mspace, fspace, HDF5Constants.H5P_DEFAULT, (Object[])theData);
                }
                else
                {
                    H5.H5Dread( did, nativeDatatype, mspace, fspace, HDF5Constants.H5P_DEFAULT, theData);
                    if (isText && convertByteToString)
                        theData = byteToString((byte[])theData, H5.H5Tget_size(nativeDatatype));
                    else if (isREF)
                        theData = HDFNativeData.byteToLong((byte[])theData);
                }
            }
        } finally {
            if (fspace > 0) try { H5.H5Sclose(fspace); } catch (Exception ex2) {}
            if (mspace > 0) try { H5.H5Sclose(mspace); } catch (Exception ex2) {}
            close(did);
        }
        
        return theData;
    }

    /*
     * (non-Javadoc)
     * @see ncsa.hdf.object.Dataset#write(java.lang.Object)
     */
    public void write(Object buf) throws HDF5Exception
    {
        if (buf == null)
            return;

        if (isVLEN)
            throw(new HDF5Exception("Writing variable-length data is not supported"));

        int fspace=-1, mspace=-1, did=-1, tid=-1, status=-1;
        Object tmpData = null;

        long[] lsize = {1};
        boolean isAllSelected = true;
        for (int i=0; i<rank; i++)
        {
            lsize[0] *= selectedDims[i];
            if (selectedDims[i] < dims[i])
                isAllSelected = false;
        }

        try {
            did = open();

            if (isAllSelected)
            {
                mspace = HDF5Constants.H5S_ALL;
                fspace = HDF5Constants.H5S_ALL;
            }
            else
            {
                fspace = H5.H5Dget_space(did);
                mspace = H5.H5Screate_simple(1, lsize, null);
                H5.H5Sselect_hyperslab(
                    fspace,
                    HDF5Constants.H5S_SELECT_SET,
                    startDims,
                    selectedStride,
                    selectedDims,
                    null );
            }

            int org_tid = H5.H5Dget_type(did);
            tid = H5.H5Tget_native_type(org_tid);
            isText = (H5.H5Tget_class(tid)==HDF5Constants.H5T_STRING);
            try { H5.H5Tclose(org_tid); } catch (Exception ex) {}

            if ( isUnsigned && unsignedConverted)
            {
                tmpData = convertToUnsignedC(buf);
            }
            else if (isText && convertByteToString)
            {
                tmpData = stringToByte((String[])buf, H5.H5Tget_size(tid));
            }
            else
                tmpData = buf;

            H5.H5Dwrite(did, tid, mspace, fspace, HDF5Constants.H5P_DEFAULT, tmpData);
        } finally {
            tmpData = null;
            if (fspace > 0) try { H5.H5Sclose(fspace); } catch (Exception ex) {}
            if (mspace > 0) try { H5.H5Sclose(mspace); } catch (Exception ex) {}
            try { H5.H5Tclose(tid); } catch (Exception ex) {}
            close(did);
        }
    }

    /*
     * (non-Javadoc)
     * @see ncsa.hdf.object.DataFormat#getMetadata()
     */
    public List getMetadata() throws HDF5Exception
    {
        // load attributes first
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

            /* see if fill value is defined */
            int[] fillStatus = {0};
            if (H5.H5Pfill_value_defined(pid, fillStatus)>=0)
            {
                if (fillStatus[0] == HDF5Constants.H5D_FILL_VALUE_USER_DEFINED)
                {
                    fillValue = H5Datatype.allocateArray(nativeDatatype, 1);
                    try { H5.H5Pget_fill_value(pid, nativeDatatype, fillValue ); }
                    catch (Exception ex2) { fillValue = null; }
                }
            }

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

    /*
     * (non-Javadoc)
     * @see ncsa.hdf.object.DataFormat#writeMetadata(java.lang.Object)
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

    /*
     * (non-Javadoc)
     * @see ncsa.hdf.object.DataFormat#removeMetadata(java.lang.Object)
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

    /*
     * (non-Javadoc)
     * @see ncsa.hdf.object.HObject#open()
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

    /*
     * (non-Javadoc)
     * @see ncsa.hdf.object.HObject#close(int)
     */
    public void close(int did)
    {
        try { H5.H5Fflush(did, HDF5Constants.H5F_SCOPE_LOCAL); } catch (Exception ex) {}
        try { H5.H5Dclose(did); }
        catch (HDF5Exception ex) {;}
    }

    /*
     * (non-Javadoc)
     * @see ncsa.hdf.object.ScalarDS#getPalette()
     */
    public byte[][] getPalette()
    {
        if (palette == null)
            palette = readPalette(0);

        return palette;
    }

    /*
     * (non-Javadoc)
     * @see ncsa.hdf.object.ScalarDS#readPalette(int)
     */
    public byte[][] readPalette(int idx)
    {
        byte[][] thePalette = null;

        if (paletteRefs == null)
            return null;

        byte[] p = null;
        byte[] ref_buf = new byte[8];
        System.arraycopy(paletteRefs, idx*8, ref_buf, 0, 8);
        int did=-1, pal_id=-1, tid=-1;

        try {
            did = open();
            pal_id =  H5.H5Rdereference(getFID(), HDF5Constants.H5R_OBJECT, ref_buf);
            tid = H5.H5Dget_type(pal_id);

            // support only 3*256 byte palette data
            if (H5.H5Dget_storage_size(pal_id) <= 768) {
                p = new byte[3*256];
                H5.H5Dread( pal_id, tid, HDF5Constants.H5S_ALL, HDF5Constants.H5S_ALL, HDF5Constants.H5P_DEFAULT, p);
            }
        } catch (HDF5Exception ex)
        {
            p = null;
        } finally {
            try { H5.H5Tclose(tid); } catch (HDF5Exception ex2) {}
            close(pal_id);
            close(did);
        }

        if (p != null)
        {
            thePalette = new byte[3][256];
            for (int i=0; i<256; i++)
            {
                thePalette[0][i] = p[i*3];
                thePalette[1][i] = p[i*3+1];
                thePalette[2][i] = p[i*3+2];
            }
        }

        return thePalette;
    }

    /**
     * Creates a new dataset in file.
     * <p>
     * The following example shows how to create a string dataset using this function.
     * <pre>
        H5File file = new H5File("test.h5", H5File.CREATE);
        int max_str_len = 120;
        Datatype strType = new H5Datatype(Datatype.CLASS_STRING, max_str_len, -1, -1);
        int size = 10000;
        long dims[] = {size};
        long chunks[] = {1000};
        int gzip = 9;
        String strs[] = new String[size];
        
        for (int i=0; i<size; i++)
            strs[i] = String.valueOf(i);

        file.open();
        file.createScalarDS("/1D scalar strings", null, strType, dims, null, chunks, 
                gzip, strs);

        try { file.close(); } catch (Exception ex) {}
     * </pre>
     * 
     * @param name the name of the dataset to create.
     * @param pgroup the parent group of the new dataset.
     * @param type the datatype of the dataset.
     * @param dims the dimension size of the dataset. 
     * @param maxdims the max dimension size of the dataset. maxdims is set to dims if maxdims = null.
     * @param chunks the chunk size of the dataset. No chunking if chunk = null.
     * @param gzip the level of the gzip compression. No compression if gzip<=0.
     * @param data the array of data values.
     * @return the new dataset if successful. Otherwise returns null.
     */
    public static H5ScalarDS create(
        String name,
        Group pgroup,
        Datatype type,
        long[] dims,
        long[] maxdims,
        long[] chunks,
        int gzip,
        Object data) throws Exception
    {
        H5ScalarDS dataset = null;
        String fullPath = null;
        int did = -1;

        if (pgroup == null ||
            name == null ||
            dims == null ||
            (gzip>0 && chunks==null))
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

        // prepare the dataspace and datatype
        int rank = dims.length;
        int tid = type.toNative();
        int sid = H5.H5Screate_simple(rank, dims, maxdims);

        // figure out creation properties
        int plist = HDF5Constants.H5P_DEFAULT;

        if (chunks != null)
        {
            plist = H5.H5Pcreate (HDF5Constants.H5P_DATASET_CREATE);
            H5.H5Pset_layout(plist, HDF5Constants.H5D_CHUNKED);
            H5.H5Pset_chunk(plist, rank, chunks);
        }

        if (gzip > 0) {
            H5.H5Pset_deflate(plist, gzip);
        }
        int fid = file.open();
        did = H5.H5Dcreate(fid, fullPath, tid, sid, plist);

        byte[] ref_buf = H5.H5Rcreate( fid, fullPath, HDF5Constants.H5R_OBJECT, -1);
        long l = HDFNativeData.byteToLong(ref_buf, 0);
        long[] oid = {l};
        dataset = new H5ScalarDS(file, name, path, oid);

        try {H5.H5Pclose(plist);} catch (HDF5Exception ex) {};
        try {H5.H5Sclose(sid);} catch (HDF5Exception ex) {};
        try {H5.H5Dclose(did);} catch (HDF5Exception ex) {};

        if (dataset != null) {
            pgroup.addToMemberList(dataset);
            if (data != null)
                dataset.write(data);
        }

        return dataset;
    }

    /*
     * (non-Javadoc)
     * @see ncsa.hdf.object.ScalarDS#getPaletteRefs()
     */
    public byte[] getPaletteRefs()
    {
        return paletteRefs;
    }

    /** reads references of palettes into a byte array
     * Each reference requires  eight bytes storage. Therefore, the array length
     * is 8*numberOfPalettes.
    */
    private byte[] getPaletteRefs(int did)
    {
        int aid=-1, sid=-1, size=0, rank=0, atype=-1;
        byte[] ref_buf = null;

        try {
            aid = H5.H5Aopen_name(did, "PALETTE");
            sid = H5.H5Aget_space(aid);
            rank = H5.H5Sget_simple_extent_ndims(sid);
            size = 1;
            if (rank > 0)
            {
                long[] dims = new long[rank];
                H5.H5Sget_simple_extent_dims(sid, dims, null);
                for (int i=0; i<rank; i++)
                    size *= (int)dims[i];
            }

            ref_buf = new byte[size*8];
            atype = H5.H5Aget_type(aid);

            H5.H5Aread( aid, atype, ref_buf);
        } catch (HDF5Exception ex)
        {
            ref_buf = null;
        } finally {
            try { H5.H5Tclose(atype); } catch (HDF5Exception ex2) {}
            try { H5.H5Sclose(sid); } catch (HDF5Exception ex2) {}
            try { H5.H5Aclose(aid); } catch (HDF5Exception ex2) {}
        }

        return ref_buf;
    }

    /*
     * (non-Javadoc)
     * @see ncsa.hdf.object.Dataset#getDatatype()
     */
    public Datatype getDatatype()
    {
        if (datatype == null)
        {
            datatype = new H5Datatype(nativeDatatype);
        }

        return datatype;
    }

    /*
     * (non-Javadoc)
     * @see ncsa.hdf.object.HObject#setName(java.lang.String)
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

}
