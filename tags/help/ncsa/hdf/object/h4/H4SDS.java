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

package ncsa.hdf.object.h4;

import java.util.*;
import ncsa.hdf.hdflib.*;
import ncsa.hdf.object.*;

/**
 * H4SDS describes HDF4 Scientific Data Sets (SDS) and operations performed on
 * the SDS. A SDS, is a group of data structures used to store and describe
 * multidimensional arrays of scientific data.
 * <p>
 * The data contained in an SDS array has a data type associated with it. The
 * standard data types supported by the SD interface include 32- and 64-bit
 * floating-point numbers, 8-, 16- and 32-bit signed integers, 8-, 16- and
 * 32-bit unsigned integers, and 8-bit characters.
 * <p>
 * @version 1.0 12/12/2001
 * @author Peter X. Cao, NCSA
 */
public class H4SDS extends ScalarDS
{
    /**
     * The list of attributes of this data object. Members of the list are
     * instance of Attribute.
     */
    private List attributeList;

    /**
     * The SDS interface identifier obtained from SDstart(filename, access)
     */
    private int sdid;

    /**
     * Creates an H4SDS object with specific name and path.
     * <p>
     * @param fileFormat the HDF file.
     * @param name the name of this H4SDS.
     * @param path the full path of this H4SDS.
     * @param oid the unique identifier of this data object.
     */
    public H4SDS(
        FileFormat fileFormat,
        String name,
        String path,
        long[] oid)
    {
        super (fileFormat, name, path, oid);
        unsignedConverted = false;

        if (fileFormat instanceof H4File)
        {
            this.sdid = ((H4File)fileFormat).getSDAccessID();
        }
    }

    // ***** need to implement from ScalarDS *****
    public byte[][] readPalette(int idx) { return null;}

    // ***** need to implement from ScalarDS *****
    public byte[] getPaletteRefs() { return null;}

    // implementing Dataset
    public Datatype getDatatype()
    {
        if (datatype == null)
        {
            datatype = new H4Datatype(nativeDatatype);
        }

        return datatype;
    }

    // To do: Implementing Dataset
    public Dataset copy(Group pgroup, String dname, long[] dims, Object buff)
    throws Exception
    {
        Dataset dataset = null;
        int srcdid=-1, dstdid=-1, tid=-1, size=1, theRank=2;
        String path=null;
        int[] count=null, start=null;

        if (pgroup == null)
            return null;

        if (dname == null)
            dname = getName();

        if (pgroup.isRoot()) path = HObject.separator;
        else path = pgroup.getPath()+pgroup.getName()+HObject.separator;

        srcdid = open();
        if (srcdid < 0) return null;

        if (dims == null)
        {
            theRank = getRank();
            if (theRank <=0) init();
            theRank = getRank();

            dims = getDims();
        }
        else
        {
            theRank = dims.length;
        }

        start = new int[theRank];
        count = new int[theRank];
        for (int i=0; i<theRank; i++)
        {
            start[i] = 0;
            count[i] = (int)dims[i];
            size *= count[i];
        }

        // create the new dataset and attached it to the parent group
        tid = getNativeDataType();
        dstdid = HDFLibrary.SDcreate(
            ((H4File)pgroup.getFileFormat()).getSDAccessID(),
            dname, tid, theRank, count);
        if (dstdid < 0) return null;

        int ref = HDFLibrary.SDidtoref(dstdid);
        if (!pgroup.isRoot())
        {
            int vgid = pgroup.open();
            HDFLibrary.Vaddtagref(vgid, HDFConstants.DFTAG_NDG, ref);
            pgroup.close(vgid);
        }

        // copy attributes from one object to the new object
        copyAttribute(srcdid, dstdid);

        // read data from the source dataset
        if (buff == null)
        {
            buff = new byte[size * HDFLibrary.DFKNTsize(tid)];
            HDFLibrary.SDreaddata(srcdid, start, null, count, buff);
        }

        // write the data into the destination dataset
        HDFLibrary.SDwritedata(dstdid, start, null, count, buff);

        long[] oid = {HDFConstants.DFTAG_NDG, ref};
        dataset = new H4SDS(pgroup.getFileFormat(), dname, path, oid);

        pgroup.addToMemberList(dataset);

        close(srcdid);
        try { HDFLibrary.SDendaccess(dstdid); }
        catch (HDFException ex) { ; }

        return dataset;
    }

    // Implementing DataFormat
    public Object read() throws HDFException
    {
        Object theData = null;

        if (rank <=0 ) init();

        int id = open();
        if (id < 0) return null;

        int datasize = 1;
        int[] select = new int[rank];
        int[] start = new int[rank];
        for (int i=0; i<rank; i++)
        {
            datasize *= (int)selectedDims[i];
            select[i] = (int)selectedDims[i];
            start[i] = (int)startDims[i];
        }

        int[] stride = null;
        if (selectedStride != null)
        {
            stride = new int[rank];
            for (int i=0; i<rank; i++)
                stride[i] = (int)selectedStride[i];
        }

        try {
            theData = H4Datatype.allocateArray(nativeDatatype, datasize);

            if (theData != null)
            {
                HDFLibrary.SDreaddata(id, start, stride, select, theData);

                if (isText)
                    theData = byteToString((byte[])theData, select[0]);
            }
        } finally
        {
            close(id);
        }

        return theData;
    }

    // Implementing DataFormat
    public void write() throws HDFException
    {
        if (data == null)
            return;

        int id = open();
        if (id < 0) return;

        int[] select = new int[rank];
        int[] start = new int[rank];
        for (int i=0; i<rank; i++)
        {
            select[i] = (int)selectedDims[i];
            start[i] = (int)startDims[i];
        }

        int[] stride = null;
        if (selectedStride != null)
        {
            stride = new int[rank];
            for (int i=0; i<rank; i++)
                stride[i] = (int)selectedStride[i];
        }

        Object tmpData = null;
        try {
            if ( isUnsigned && unsignedConverted)
                tmpData = convertToUnsignedC(data);
            else
                tmpData = data;
            HDFLibrary.SDwritedata(id, start, stride, select, tmpData);
        } finally
        {
            tmpData = null;
            close(id);
        }
    }

    // Implementing DataFormat
    public List getMetadata() throws HDFException
    {
        if (attributeList != null)
            return attributeList;

        int id = open();
        String[] objName = {""};
        int[] sdInfo = {0, 0, 0};
        try {
            int[] tmpDim = new int[HDFConstants.MAX_VAR_DIMS];
            HDFLibrary.SDgetinfo(id, objName, tmpDim, sdInfo);
            int n = sdInfo[2];

            if (attributeList == null && n>0)
                attributeList = new Vector(n, 5);

            boolean b = false;
            String[] attrName = new String[1];
            int[] attrInfo = {0, 0};
            for (int i=0; i<n; i++)
            {
                attrName[0] = "";
                try {
                    b = HDFLibrary.SDattrinfo(id, i, attrName, attrInfo);
                    // mask off the litend bit
                    attrInfo[0] = attrInfo[0] & (~HDFConstants.DFNT_LITEND);
                } catch (HDFException ex)
                {
                    b = false;
                }

                if (!b) continue;

                long[] attrDims = {attrInfo[1]};
                Attribute attr = new Attribute(attrName[0], new H4Datatype(attrInfo[0]), attrDims);
                attributeList.add(attr);

                Object buf = H4Datatype.allocateArray(attrInfo[0], attrInfo[1]);
                try {
                    HDFLibrary.SDreadattr(id, i, buf);
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
        } finally {
            close(id);
        }

        return attributeList;
    }

   // To do: implementing DataFormat
    public void writeMetadata(Object info) throws Exception
    {
        // only attribute metadata is supported.
        if (!(info instanceof Attribute))
            return;

        getFileFormat().writeAttribute(this, (Attribute)info, true);

        if (attributeList == null)
            attributeList = new Vector();

        attributeList.add(info);
    }

   // To do: implementing DataFormat
    public void removeMetadata(Object info) throws HDFException {;}

    // Implementing HObject
    public int open()
    {
        int id = -1;
        try {
            int index = HDFLibrary.SDreftoindex(sdid, (int)oid[1]);
            id = HDFLibrary.SDselect(sdid,index);
        } catch (HDFException ex)
        {
            id = -1;
        }

        return id;
    }

    // Implementing HObject
    public void close(int id)
    {
        try { HDFLibrary.SDendaccess(id); }
        catch (HDFException ex) { ; }
    }

    /**
     * Initializes the H4SDS such as dimension size of this dataset.
     */
    public void init()
    {
        int id = open();
        String[] objName = {""};
        int[] sdInfo = {0, 0, 0};
        int[] idims = new int[HDFConstants.MAX_VAR_DIMS];
        try {
            HDFLibrary.SDgetinfo(id, objName, idims, sdInfo);
            // mask off the litend bit
            sdInfo[1] = sdInfo[1] & (~HDFConstants.DFNT_LITEND);
            rank = sdInfo[0];
            if (rank <= 0) rank = 1;
            nativeDatatype = sdInfo[1];
            isText = (nativeDatatype == HDFConstants.DFNT_CHAR ||
                nativeDatatype == HDFConstants.DFNT_UCHAR8);
            idims = new int[rank];
            HDFLibrary.SDgetinfo(id, objName, idims, sdInfo);

            /*
            if (idims[0] == 0)
            {
                // unlimited
                String[] dimname = {""};
                int[] diminfo = new int[3];
                int dimid = HDFLibrary.SDgetdimid(id, 0);
                HDFLibrary.SDdiminfo(dimid, dimname, diminfo);
            }
            */

        } catch (HDFException ex) {}
        finally {
            close(id);
        }

        isUnsigned = H4Datatype.isUnsigned(nativeDatatype);

        if (idims == null)
            return;

        dims = new long[rank];
        startDims = new long[rank];
        selectedDims = new long[rank];

        for (int i=0; i<rank; i++)
        {
            startDims[i] = 0;
            selectedDims[i] = 1;
            dims[i] = (long)idims[i];
        }

        // select only two dimension a time,
        // select only two dimension a time,
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
    }

    // Implementing ScalarDS
    public byte[][] getPalette()
    {
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
     * @param data the array of data values.
     * @return the new dataset if successful. Otherwise returns null.
     */
    public static H4SDS create(
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
        H4SDS dataset = null;
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

        // prepare the dataspace
        int tsize = 1;
        int rank = dims.length;
        int idims[] = new int[rank];
        int start[] = new int [rank];
        for (int i=0; i<rank; i++)
        {
            idims[i] = (int)dims[i];
            start[i] = 0;
            tsize *= idims[i];
        }

        // only the first element of the SDcreate parameter dim_sizes (i.e.,
        // the dimension of the lowest rank or the slowest-changing dimension)
        // can be assigned the value SD_UNLIMITED (or 0) to make the first
        // dimension unlimited.
        if (maxdims != null && maxdims[0]<0)
        {
            idims[0] = 0; // set to unlimited dimension.
        }

        int ichunks[] = null;
        if (chunks != null)
        {
            ichunks = new int[rank];
            for (int i=0; i<rank; i++)
                ichunks[i] = (int)chunks[i];
        }

        int sdid, sdsid, vgid;

        sdid = ((H4File)file).getSDAccessID();
        // datatype
        int tid = type.toNative();

        try {
            sdsid = HDFLibrary.SDcreate(sdid, name, tid, rank, idims);
            // set fill value to zero.
            int vsize = HDFLibrary.DFKNTsize(tid);
            byte[] fillValue = new byte[vsize];
            for (int i=0; i<vsize; i++) fillValue[i] = 0;
            HDFLibrary.SDsetfillvalue(sdsid, fillValue);

            // when we create a new dataset with unlimited dimension,
            // we have to write some data into the dataset or otherwise
            // the current dataset has zero dimensin size.
            if (idims[0] == 0 && data == null)
            {
                idims[0] = (int)dims[0];
                data = new byte[tsize*vsize];
            }
        } catch (Exception ex) { throw (ex); }

        if (sdsid < 0)
        {
            throw (new HDFException("Unable to create the new dataset."));
        }

        if (sdsid > 0 && data != null)
        {
            HDFLibrary.SDwritedata(sdsid, start, null, idims, data);
        }

        if (chunks != null)
        {
            // set chunk
            HDFOnlyChunkInfo chunkInfo = new HDFOnlyChunkInfo(ichunks);
            HDFLibrary.SDsetchunk (sdsid, chunkInfo, HDFConstants.HDF_CHUNK);
        }

        if (gzip > 0)
        {
            // set compression
            int compType = HDFConstants.COMP_CODE_DEFLATE;
            HDFDeflateCompInfo compInfo = new HDFDeflateCompInfo();
            compInfo.level = gzip;
            HDFLibrary.SDsetcompress(sdsid, compType, compInfo);
        }

        int ref = HDFLibrary.SDidtoref(sdsid);

        if (!pgroup.isRoot())
        {
            // add the dataset to the parent group
            vgid = pgroup.open();
            if (vgid < 0)
            {
                if (sdsid > 0) HDFLibrary.SDendaccess(sdsid);
                throw (new HDFException("Unable to open the parent group."));
            }

            HDFLibrary.Vaddtagref(vgid, HDFConstants.DFTAG_NDG, ref);

            pgroup.close(vgid);
        }

        try {
            if (sdsid > 0) HDFLibrary.SDendaccess(sdsid);
        } catch (Exception ex) {}

        long[] oid = {HDFConstants.DFTAG_NDG, ref};
        dataset = new H4SDS(file, name, path, oid);

        return dataset;
    }

    /**
     * copy attributes from one SDS to another SDS
     */
    private void copyAttribute(int srcdid, int dstdid)
    {
        try {
            String[] objName = {""};
            int[] sdInfo = {0, 0, 0};
            int[] tmpDim = new int[HDFConstants.MAX_VAR_DIMS];
            HDFLibrary.SDgetinfo(srcdid, objName, tmpDim, sdInfo);
            int numberOfAttributes = sdInfo[2];

            boolean b = false;
            String[] attrName = new String[1];
            int[] attrInfo = {0, 0};
            for (int i=0; i<numberOfAttributes; i++)
            {
                attrName[0] = "";
                try {
                    b = HDFLibrary.SDattrinfo(srcdid, i, attrName, attrInfo);
                } catch (HDFException ex) { b = false; }

                if (!b) continue;

                // read attribute data from source dataset
                byte[] attrBuff = new byte[attrInfo[1] * HDFLibrary.DFKNTsize(attrInfo[0])];
                try { HDFLibrary.SDreadattr(srcdid, i, attrBuff);
                } catch (HDFException ex) { attrBuff = null; }

                if (attrBuff == null)  continue;

                // attach attribute to the destination dataset
                HDFLibrary.SDsetattr(dstdid, attrName[0], attrInfo[0], attrInfo[1], attrBuff);
            } // for (int i=0; i<numberOfAttributes; i++)
        } catch (Exception ex) {}
    }

}
