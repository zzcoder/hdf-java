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
     * The data type of this data set.
     */
    private int datatype;

    /**
     * Creates a H4SDS object with specific name, path, and parent.
     * <p>
     * @param fid the file identifier.
     * @param filename the full path of the file that contains this data object.
     * @param name the name of this H4SDS.
     * @param path the full path of this H4SDS.
     * @param oid the unique identifier of this data object.
     */
    public H4SDS(
        int fid,
        String filename,
        String name,
        String path,
        long[] oid)
    {
        super (fid, filename, name, path, oid);
    }

    // ***** need to implement from DataFormat *****
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

        try {
            data = H4Accessory.allocateArray(datatype, datasize);

            if (data != null)
            {
                HDFLibrary.SDreaddata(id, start, null, select, data);
            }
        } finally
        {
            close(id);
        }

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
        String[] objName = {""};
        int[] sdInfo = {0, 0, 0};
        try {
            int[] tmpDim = new int[1];
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
                Attribute attr = new Attribute(attrName[0], attrInfo[0], attrDims);;
                attributeList.add(attr);

                Object buf = H4Accessory.allocateArray(attrInfo[0], attrInfo[1]);
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

    // ***** need to implement from DataFormat *****
    public boolean writeMetadata(Object info) throws HDFException
    {
        return false;
    }

    // ***** need to implement from DataFormat *****
    public boolean removeMetadata(Object info) throws HDFException
    {
        return false;
    }

    // ***** need to implement from HObejct *****
    public int open()
    {
        int id = -1;
        try {
            int index = HDFLibrary.SDreftoindex(sdid, (int)getOID()[1]);
            id = HDFLibrary.SDselect(sdid,index);
        } catch (HDFException ex)
        {
            id = -1;
        }

        return id;
    }

    // ***** need to implement from HObejct *****
    public static boolean close(int id)
    {
        boolean b = true;

        try {
            HDFLibrary.SDendaccess(id); }
        catch (HDFException ex) {
            b = false;
        }

        return b;
    }

    /**
     * Initializes the H4SDS such as dimension size of this dataset.
     */
    public void init()
    {
        int id = open();
        String[] objName = {""};
        int[] sdInfo = {0, 0, 0};
        int[] idims = new int[1];
        try {
            HDFLibrary.SDgetinfo(id, objName, idims, sdInfo);
            // mask off the litend bit
            sdInfo[1] = sdInfo[1] & (~HDFConstants.DFNT_LITEND);
            rank = sdInfo[0];
            if (rank <= 0) rank = 1;
            datatype = sdInfo[1];
            idims = new int[rank];
            HDFLibrary.SDgetinfo(id, objName, idims, sdInfo);
        } catch (HDFException ex) {}
        finally {
            close(id);
        }

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
        // need to more work to select any two dimensions
        if (rank == 1)
        {
            selectedDims[0] = dims[0];
        }
        else if (rank >= 2)
        {
            int colIdx = rank-1;
            int rowIdx = rank-2;
            selectedDims[colIdx] = dims[colIdx];
            selectedDims[rowIdx] = dims[rowIdx];
        }
    }

    // ***** need to implement from ScalarDS *****
    public byte[][] getPalette()
    {
        return null;
    }

    /**
     * Sets the SD interface identifiers.
     * <p>
     * The SD identifier is returned by SDstart(fname, flag), which initializes the
     * SD interface for the file specified by the parameter. SDstart(fname, flag)
     * is an expensive call. It should be only called once.
     * <p>

     * @param sdid the SDS interface identifier.
     */
    public void setAccess(int sdid)
    {

        this.sdid = sdid;
    }

}