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
 * The H4Accessory class provides common static methods to HDF4 objects.
 * <p>
 * @version 1.0 12/12/2001
 * @author Peter X. Cao, NCSA
 */
public final class H4Accessory
{
    /**
     *  Reads HDF file annontation (file labels and descriptions) into memory.
     *  The file annotation is stroed as attribute of the root group.
     *  <p>
     *  @param fid the file identifier.
     *  @param attrList the list of attributes.
     *  @return the updated attribute list.
     */
    public static final List getFileAnnotation(int fid, List attrList)
    throws HDFException
    {
        if (fid < 0 )
            return attrList;

        int anid = HDFConstants.FAIL;
        try
        {
            anid = HDFLibrary.ANstart(fid);
            // fileInfo[0] = n_file_label, fileInfo[1] = n_file_desc,
            // fileInfo[2] = n_data_label, fileInfo[3] = n_data_desc
            int[] fileInfo = new int[4];
            HDFLibrary.ANfileinfo(anid, fileInfo);

            if (fileInfo[0]+fileInfo[1] <= 0)
            {
                try { HDFLibrary.ANend(anid); } catch (HDFException ex) {}
                return attrList;
            }

            if (attrList == null)
                attrList = new Vector(fileInfo[0]+fileInfo[1], 5);

            // load file labels and descriptions
            int id = -1;
            int[] annTypes = {HDFConstants.AN_FILE_LABEL, HDFConstants.AN_FILE_DESC};
            for (int j=0; j<2; j++)
            {
                for (int i=0; i < fileInfo[j]; i++)
                {
                    try {
                        id = HDFLibrary.ANselect(anid, i, annTypes[j]);
                    } catch (HDFException ex)
                    {
                        id = HDFConstants.FAIL;
                    }

                    if (id == HDFConstants.FAIL)
                    {
                        try { HDFLibrary.ANendaccess(id); } catch (HDFException ex) {}
                        continue;
                    }

                    int length = 0;
                    try {
                        length = HDFLibrary.ANannlen(id)+1;
                    } catch (HDFException ex)
                    {
                        length = 0;
                    }

                    if (length > 0)
                    {
                        boolean b = false;
                        String str[] = {""};
                        try { b = HDFLibrary.ANreadann(id, str, length);
                        } catch ( HDFException ex) { b = false; }

                        if (b)
                        {
                            attrList.add(new Attribute("File label #"+i, str[0]));
                        }
                    }

                    try { HDFLibrary.ANendaccess(id); } catch (HDFException ex) {}
                } // for (int i=0; i < fileInfo[annTYpe]; i++)
            } // for (int annType=0; annType<2; annType++)
        } finally
        {
            try { HDFLibrary.ANend(anid); } catch (HDFException ex) {}
        }

        return attrList;
    }

    /**
     *  Reads GR globle attributes into memory.
     *  The attributes sre stroed as attributes of the root group.
     *  <p>
     *  @param grid the GR identifier.
     *  @param attrList the list of attributes.
     *  @return the updated attribute list.
     */
    public static final List getGRglobleAttribute(int grid, List attrList)
    throws HDFException
    {
        if (grid == HDFConstants.FAIL)
            return attrList;

        int[] attrInfo = {0, 0};
        HDFLibrary.GRfileinfo(grid, attrInfo);
        int numberOfAttributes = attrInfo[1];

        if (numberOfAttributes>0)
        {
            if (attrList == null)
                attrList = new Vector(numberOfAttributes, 5);

            String[] attrName = new String[1];
            for (int i=0; i<numberOfAttributes; i++)
            {
                attrName[0] = "";
                boolean b = false;
                try {
                    b =  HDFLibrary.GRattrinfo(grid, i, attrName, attrInfo);
                    // mask off the litend bit
                    attrInfo[0] = attrInfo[0] & (~HDFConstants.DFNT_LITEND);
                } catch (HDFException ex)
                {
                    b = false;
                }

                if (!b)
                    continue;

                long[] attrDims = {attrInfo[1]};
                Attribute attr = new Attribute(attrName[0], attrInfo[0], attrDims);;
                attrList.add(attr);

                Object buf = allocateArray(attrInfo[0], attrInfo[1]);
                try {
                    HDFLibrary.GRgetattr(grid, i, buf);
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

            } // for (int i=0; i<numberOfAttributes; i++)
        } // if (b && numberOfAttributes>0)

        return attrList;
    }

    /**
     *  Reads SDS globle attributes into memory.
     *  The attributes sre stroed as attributes of the root group.
     *  <p>
     *  @param sdid the SD identifier.
     *  @param attrList the list of attributes.
     *  @return the updated attribute list.
     */
    public static final List getSDSglobleAttribute(int sdid, List attrList)
    throws HDFException
    {
        if (sdid == HDFConstants.FAIL)
            return attrList;

        int[] attrInfo = {0, 0};
        HDFLibrary.SDfileinfo(sdid, attrInfo);

        int numberOfAttributes = attrInfo[1];
        if (numberOfAttributes>0)
        {
            if (attrList == null)
                attrList = new Vector(numberOfAttributes, 5);

            String[] attrName = new String[1];
            for (int i=0; i<numberOfAttributes; i++)
            {
                attrName[0] = "";
                boolean b = false;
                try {
                    b =  HDFLibrary.SDattrinfo(sdid, i, attrName, attrInfo);
                    // mask off the litend bit
                    attrInfo[0] = attrInfo[0] & (~HDFConstants.DFNT_LITEND);
                } catch (HDFException ex)
                {
                    b = false;
                }

                if (!b)
                    continue;

                long[] attrDims = {attrInfo[1]};
                Attribute attr = new Attribute(attrName[0], attrInfo[0], attrDims);;
                attrList.add(attr);

                Object buf = allocateArray(attrInfo[0], attrInfo[1]);
                try {
                    HDFLibrary.SDreadattr(sdid, i, buf);
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

            } // for (int i=0; i<numberOfAttributes; i++)
        } // if (b && numberOfAttributes>0)

        return attrList;
    }

    /**
     *  Allocate a 1D array large enough to hold a multidimensional
     *  array of 'datasize' elements of 'dataType' numbers.
     *
     *  @param dataType  the type of the iamge data
     *  @param datasize  the size of the data array
     *  @return an array of 'datasize' numbers of dataType.
     */
    public static final Object allocateArray(int dataType, int datasize)
    {
        if (datasize <= 0)
            return null;

        Object data = null;

        switch(dataType)
        {
            case HDFConstants.DFNT_CHAR:
            case HDFConstants.DFNT_UCHAR8:
            case HDFConstants.DFNT_UINT8:
            case HDFConstants.DFNT_INT8:
                data = new byte[datasize];
                break;
            case HDFConstants.DFNT_INT16:
            case HDFConstants.DFNT_UINT16:
                data = new short[datasize];
                break;
            case HDFConstants.DFNT_INT32:
            case HDFConstants.DFNT_UINT32:
                data = new int[datasize];
                break;
            case HDFConstants.DFNT_INT64:
            case HDFConstants.DFNT_UINT64:
                data = new long[datasize];
                break;
            case HDFConstants.DFNT_FLOAT32:
                data = new float[datasize];
                break;
            case HDFConstants.DFNT_FLOAT64:
                data = new double[datasize];
                break;
            default:
                data = null;
                break;
        }

        return data;
    }

}
