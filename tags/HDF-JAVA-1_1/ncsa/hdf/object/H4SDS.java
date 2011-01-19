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
import ncsa.hdf.hdflib.*;

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

    private boolean unsignedConverted;

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

    // Implementing DataFormat
    public Object read() throws HDFException
    {
        if (data != null)
            return data; // data is loaded

        if (rank <=0 ) init();

        int id = open();
        if (id < 0) return data;

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
            data = H4Datatype.allocateArray(datatype, datasize);

            if (data != null)
            {
                HDFLibrary.SDreaddata(id, start, stride, select, data);

                if (isText)
                    data = byteToString((byte[])data, select[0]);
            }
        } finally
        {
            close(id);
        }

        return data;
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
                Attribute attr = new Attribute(attrName[0], attrInfo[0], attrDims);
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
    public void writeMetadata(Object info) throws HDFException
    {
        // only attribute metadata is supported.
        if (!(info instanceof Attribute))
            return;

        H4File.writeAttribute(this, (Attribute)info);

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
            datatype = sdInfo[1];
            isText = (datatype == HDFConstants.DFNT_CHAR ||
                datatype == HDFConstants.DFNT_UCHAR8);
            idims = new int[rank];
            HDFLibrary.SDgetinfo(id, objName, idims, sdInfo);
        } catch (HDFException ex) {}
        finally {
            close(id);
        }

        isUnsigned = H4Datatype.isUnsigned(datatype);

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
        return null;
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
    public static H4SDS create(
        FileFormat file,
        String name,
        Group pgroup,
        Datatype type,
        long[] dims,
        long[] maxdims,
        long[] chunks,
        int gzip) throws Exception
    {
        H4SDS dataset = null;
        String fullPath = null;

        if (file == null ||
            name == null ||
            pgroup == null ||
            dims == null ||
            maxdims == null ||
            (gzip>0 && chunks==null))
            return null;

        String path = HObject.separator;
        if (!pgroup.isRoot())
            path = pgroup.getPath()+pgroup.getName()+HObject.separator;
        fullPath = path +  name;

        // datatype
        int tid = type.toNative();

        // prepare the dataspace
        int rank = dims.length;
        int idims[] = new int[rank];
        int imaxdims[] = new int[rank];
        for (int i=0; i<rank; i++)
        {
            idims[i] = (int)dims[i];
            imaxdims[i] = (int)imaxdims[i];
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

        try {
            sdsid = HDFLibrary.SDcreate(sdid, name, tid, rank, idims);
            // set fill value to zero.
            int vsize = HDFLibrary.DFKNTsize(tid);
            byte[] fillValue = new byte[vsize];
            for (int i=0; i<vsize; i++) fillValue[i] = 0;
            HDFLibrary.SDsetfillvalue(sdsid, fillValue);
        } catch (Exception ex)
        {
            throw (ex);
        }

        if (sdsid < 0)
        {
            throw (new HDFException("Unable to create the new dataset."));
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
}