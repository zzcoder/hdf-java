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
 * H5ScalarDS describes an multi-dimension array of HDF5 scalar or atomic data
 * types and operations performed on the scalar dataset, such as byte, int,
 * short, long, float, double and string.
 * <p>
 * The library predefines a modest number of datatypes. For details, read
 * <a href="http://hdf.ncsa.uiuc.edu/HDF5/doc/Datatypes.html">
 * The Datatype Interface (H5T)</a>
 * <p>
 * @version 1.0 12/12/2001
 * @author Peter X. Cao, NCSA
 */
public class H5ScalarDS extends ScalarDS
{
    /**
     * The list of attributes of this data object. Members of the list are
     * instance of Attribute.
     */
     private List attributeList;

     /** references of palettes */
     private byte[] paletteRefs;

    /**
     * Creates an H5ScalarDS object with specific name and path.
     * <p>
     * @param fileFormat the HDF file.
     * @param name the name of this H5ScalarDS.
     * @param path the full path of this H5ScalarDS.
     * @param oid the unique identifier of this data object.
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

        int did=-1, aid=-1, atid=-1;
        try
        {
            did = open();

            // try to find out if the dataset is an image
            aid = H5.H5Aopen_name(did, "CLASS");
            atid = H5.H5Aget_type(aid);
            int aclass = H5.H5Tget_class(atid);
            if (aclass == HDF5Constants.H5T_STRING)
            {
                byte[] attrValue = new byte[6];
                H5.H5Aread(aid, atid, attrValue);
                String strValue = new String(attrValue).trim();
                isImage = strValue.equalsIgnoreCase("IMAGE");
            }
        } catch (Exception ex) {}
        finally
        {
            try { H5.H5Tclose(atid); } catch (HDF5Exception ex) {;}
            try { H5.H5Aclose(aid); } catch (HDF5Exception ex) {;}
            try { H5.H5Dclose(did); } catch (HDF5Exception ex) {;}
        }
    }

    // To do: Implementing Dataset
    public Dataset copy(Group pgroup, String dstName, long[] dims, Object buff)
    throws Exception
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

    // Implementing DataFormat
    public Object read() throws HDF5Exception
    {
        Object theData = null;

        if (rank <= 0) init();

        int did = open();
        int fspace=-1, mspace=-1, tid=-1, nativeType=-1;

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

            tid = H5.H5Dget_type(did);
            nativeType = H5Datatype.toNativeType(tid);
            theData = H5Datatype.allocateArray(nativeType, (int)lsize[0]);

            if (theData != null)
            {
                H5.H5Dread(
                    did,
                    nativeType,
                    mspace,
                    fspace,
                    HDF5Constants.H5P_DEFAULT,
                    theData);

                if (isText)
                    theData = byteToString((byte[])theData, H5.H5Tget_size(nativeType));
                else if (H5.H5Tget_class(nativeType) == HDF5Constants.H5T_REFERENCE)
                    theData = HDFNativeData.byteToLong((byte[])theData);
            }
        }
        finally
        {
            if (fspace > 0)
                try { H5.H5Sclose(fspace); } catch (Exception ex2) {}
            if (mspace > 0)
                try { H5.H5Sclose(mspace); } catch (Exception ex2) {}
            try { H5.H5Tclose(tid); } catch (HDF5Exception ex2) {}
            try { H5.H5Tclose(nativeType); } catch (HDF5Exception ex2) {}
            close(did);
        }

        return theData;
    }

    //Implementing DataFormat
    public void write() throws HDF5Exception
    {
        if (data == null)
            return;

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

            tid = H5.H5Dget_type(did);
            if ( isUnsigned && unsignedConverted)
                tmpData = convertToUnsignedC(data);
            else if (isText)
            {
                tmpData = stringToByte((String[])data, H5.H5Tget_size(tid));
            }
            else
                tmpData = data;
            H5.H5Dwrite(did, tid, mspace, fspace, HDF5Constants.H5P_DEFAULT, tmpData);
        } finally {
            tmpData = null;
            if (fspace > 0) try { H5.H5Sclose(fspace); } catch (Exception ex) {}
            if (mspace > 0) try { H5.H5Sclose(mspace); } catch (Exception ex) {}
            try { H5.H5Tclose(tid); } catch (Exception ex) {}
            try { H5.H5Dclose(did); } catch (Exception ex) {}
        }
    }

    // Implementing DataFormat
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

            int[] flags = {0};
            int[] cd_nelmts = {1};
            int[] cd_values = {0};
            String[] cd_name ={""};
            int nfilt = H5.H5Pget_nfilters(pid);
            int filter = -1;
            for (int i=0; i<nfilt; i++)
            {
                filter = H5.H5Pget_filter(pid, i, flags, cd_nelmts, cd_values, 20, cd_name);
                if (filter == HDF5Constants.H5Z_FILTER_DEFLATE)
                {
                    compression = "DEFLATE";
                    break;
                }
            }

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
        catch (HDF5Exception ex) {;}
    }

    /**
     * Retrieve and initialize dimensions and member information.
     */
    public void init()
    {
        int did=-1, sid=-1, tid=-1;

        did = open();
        paletteRefs = getPaletteRefs(did);

        try {
            sid = H5.H5Dget_space(did);
            tid= H5.H5Dget_type(did);
            rank = H5.H5Sget_simple_extent_ndims(sid);
            isText = (H5.H5Tget_class(tid)==HDF5Constants.H5T_STRING);

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
        } catch (HDF5Exception ex) {}
        finally
        {
            nativeDatatype = tid; // warning the datatype resource is not closed
            //try { H5.H5Tclose(tid); } catch (HDF5Exception ex2) {}
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
                byte[] attrValue = new byte[6];
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
            try
            {
                // try to find out if the image type
                aid = H5.H5Aopen_name(did, "IMAGE_SUBCLASS");
                atid = H5.H5Aget_type(aid);
                byte[] attrValue = new byte[16];
                H5.H5Aread(aid, atid, attrValue);
                String strValue = new String(attrValue).trim();
                if (strValue.equalsIgnoreCase("IMAGE_TRUECOLOR"))
                    interlace = INTERLACE_PIXEL; // default interlace
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
                byte[] attrValue = new byte[16];
                H5.H5Aread(aid, atid, attrValue);
                String strValue = new String(attrValue).trim();
                if (strValue.equalsIgnoreCase("INTERLACE_PLANE"))
                    interlace = INTERLACE_PLANE;
                else
                    interlace = INTERLACE_PIXEL;
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
        close(did);

        startDims = new long[rank];
        selectedDims = new long[rank];
        for (int i=0; i<rank; i++)
        {
            startDims[i] = 0;
            selectedDims[i] = 1;
        }

        // select only two dimension a time,
        if (interlace == INTERLACE_PIXEL)
        {
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
            selectedIndex[0] = rank-2; // columns
            selectedIndex[1] = rank-1; // rows
            selectedIndex[2] = rank-3;
            selectedDims[rank-1] = dims[rank-1];
            selectedDims[rank-2] = dims[rank-2];
        }
    }

    // Implementing ScalarDS
    public byte[][] getPalette()
    {
        if (palette == null)
            palette = readPalette(0);

        return palette;
    }

    /** read specific image palette from file.
     *  @param idx the palette index to read.
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

            p = new byte[3*256];

            H5.H5Dread(
                pal_id,
                tid,
                HDF5Constants.H5S_ALL,
                HDF5Constants.H5S_ALL,
                HDF5Constants.H5P_DEFAULT,
                p);
        } catch (HDF5Exception ex)
        {
            p = null;
        } finally {
            try { H5.H5Tclose(tid); } catch (HDF5Exception ex2) {}
            try { H5.H5Dclose(pal_id); } catch (HDF5Exception ex2) {}
            try { H5.H5Dclose(did); } catch (HDF5Exception ex2) {}
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


    // Implementing ScalarDS
    public void convertFromUnsignedC()
    {
        if (data != null && isUnsigned && !unsignedConverted)
        {
            data = convertFromUnsignedC(data);
            unsignedConverted = true;
        }
    }

    // Implementing ScalarDS
    public void convertToUnsignedC()
    {
        if (data != null && isUnsigned)
        {
            data = convertToUnsignedC(data);
        }
    }

    /**
     * Creates a new dataset.
     * @param file the file which the dataset is added to.
     * @param name the name of the dataset to create.
     * @param pgroup the parent group of the new dataset.
     * @param type the datatype of the dataset.
     * @param dims the dimension size of the dataset.
     * @param maxdims the max dimension size of the dataset.
     * @param chunk the chunk size of the dataset.
     * @param gzip the level of the gzip compression.
     * @param data the array of data values.
     * @return the new dataset if successful. Otherwise returns null.
     */
    public static H5ScalarDS create(
        FileFormat file,
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

        if (file == null ||
            pgroup == null ||
            name == null ||
            dims == null ||
            (gzip>0 && chunks==null))
            return null;

        String path = HObject.separator;
        if (!pgroup.isRoot())
            path = pgroup.getPath()+pgroup.getName()+HObject.separator;
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

        if (gzip > 0) H5.H5Pset_deflate(plist, gzip);
        int fid = file.open();
        int did = H5.H5Dcreate(fid, fullPath, tid, sid, plist);
        if (did > 0 && data != null)
        {
            H5.H5Dwrite(
                did,
                tid,
                HDF5Constants.H5S_ALL,
                HDF5Constants.H5S_ALL,
                HDF5Constants.H5P_DEFAULT,
                data);
        }

        byte[] ref_buf = H5.H5Rcreate(
            fid,
            fullPath,
            HDF5Constants.H5R_OBJECT,
            -1);
        long l = HDFNativeData.byteToLong(ref_buf, 0);
        long[] oid = {l};

        try {H5.H5Sclose(sid);} catch (HDF5Exception ex) {};
        try {H5.H5Tclose(tid);} catch (HDF5Exception ex) {};
        try {H5.H5Dclose(did);} catch (HDF5Exception ex) {};
        try {H5.H5Pclose(plist);} catch (HDF5Exception ex) {};

        dataset = new H5ScalarDS(file, name, path, oid);

        return dataset;
    }

    /** returns the byte array of palette refs.
     *  returns null if there is no palette attribute attached to this dataset.
     */
    public byte[] getPaletteRefs()
    {
        return paletteRefs;
    }

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

    // implementing ScalarDS
    public Datatype getDatatype()
    {
        if (datatype == null)
        {
            datatype = new H5Datatype(nativeDatatype);
        }

        return datatype;
    }

}
