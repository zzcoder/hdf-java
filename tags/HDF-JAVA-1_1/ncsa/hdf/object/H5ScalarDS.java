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

     private boolean unsignedConverted;

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

    // Implementing DataFormat
    public Object read() throws HDF5Exception
    {
        if (data != null)
            return data; // data is loaded

        if (rank <= 0) init();

        int did = open();
        int fspace=-1, mspace=-1, tid=-1, nativeType=-1;

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
                    selectedStride,     // set selectedStride to 1
                    selectedDims,
                    null );   // set block to 1
            }

            tid = H5.H5Dget_type(did);
            nativeType = H5Datatype.toNativeType(tid);
            data = H5Datatype.allocateArray(nativeType, (int)lsize[0]);

            if (data != null)
            {
                H5.H5Dread(
                    did,
                    nativeType,
                    mspace,
                    fspace,
                    HDF5Constants.H5P_DEFAULT,
                    data);

                if (isText)
                    data = byteToString((byte[])data, H5.H5Tget_size(nativeType));
                else if (H5.H5Tget_class(nativeType) == HDF5Constants.H5T_REFERENCE)
                    data = HDFNativeData.byteToLong((byte[])data);
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

        return data;
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

        boolean attrExisted = false;
        Attribute attr = (Attribute)info;
        String name = attr.getName();

        if (attributeList == null)
            attributeList = new Vector();
        else
            attrExisted = attributeList.contains(attr);

        H5File.writeAttribute(this, attr, attrExisted);
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
            datatype = tid; // warning the datatype resource is not closed
            //try { H5.H5Tclose(tid); } catch (HDF5Exception ex2) {}
            try { H5.H5Sclose(sid); } catch (HDF5Exception ex2) {}
        }

        isUnsigned = H5Datatype.isUnsigned(datatype);

        // check for the type of image and interlace mode
        // it is a true color image at one of three cases:
        // 1) IMAGE_SUBCLASS = IMAGE_TRUECOLOR,
        // 2) INTERLACE_MODE = INTERLACE_PIXEL,
        // 3) INTERLACE_MODE = INTERLACE_PLANE
        if (rank >=3 && isImage)
        {
            int aid=-1, atid=-1;
            try
            {
                // try to find out if the dataset is an image
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
                // try to find out if the dataset is an image
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

            if (interlace == INTERLACE_PLANE && dims[0] <3)
            {
                // must have three color planes
                interlace = -1;
            }
            else if (interlace == INTERLACE_PIXEL && dims[2] <3)
            {
                // must have three color components
                interlace = -1;
            }
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
        if (palette != null)
            return palette;

        byte[] p = null;
        int did=-1, aid=-1, pal_id=-1, tid=-1;

        try {
            did = open();
            aid = H5.H5Aopen_name(did, "PALETTE");
            byte [] ref_buf = new byte[8];
            int atype = H5.H5Aget_type(aid);

            H5.H5Aread( aid, atype, ref_buf);
            H5.H5Tclose(atype);

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
            try { H5.H5Aclose(aid); } catch (HDF5Exception ex2) {}
            try { H5.H5Dclose(did); } catch (HDF5Exception ex2) {}
        }

        if (p != null)
        {
            palette = new byte[3][256];
            for (int i=0; i<256; i++)
            {
                palette[0][i] = p[i*3];
                palette[1][i] = p[i*3+1];
                palette[2][i] = p[i*3+2];
            }
        }

        return palette;
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
        int gzip) throws Exception
    {
        H5ScalarDS dataset = null;
        String fullPath = null;

        if (file == null ||
            name == null ||
            pgroup == null ||
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

        if (gzip > 0)
            H5.H5Pset_deflate(plist, gzip);
        int fid = file.open();
        int did = H5.H5Dcreate(fid, fullPath, tid, sid, plist);

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

}
