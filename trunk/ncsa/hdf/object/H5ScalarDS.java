/****************************************************************************
 * NCSA HDF                                                                 *
 * National Comptational Science Alliance                                   *
 * University of Illinois at Urbana-Champaign                               *
 * 605 E. Springfield, Champaign IL 61820                                   *
 *                                                                          *
 * For conditions of distribution and use, see the accompanying             *
 * java-hdf COPYING file.                                                   *
 *                                                                          *
 ****************************************************************************/

package ncsa.hdf.object;

import java.util.*;
import ncsa.hdf.hdf5lib.*;
import ncsa.hdf.hdf5lib.exceptions.*;

/**
 * H5ScalarDS describes an multi-dimension array of HDF5 scalar or atomic data
 * types and operations performed on the scalar dataset.
 * <p>
 * The library predefines a modest number of datatypes having names like
 * H5T_arch_base where arch is an architecture name and base is a programming
 * type name.
 <pre>
    Architecture    Name Description
    IEEE            Standard floating point.
    STD             Semi-standard datatypes.
    UNIX            Unix operating systems.
    C/FORTRAN       C or Fortran programming languages.
    NATIVE          Machine dependent datatype.
    CRAY            Cray architectures.
    INTEL           All Intel and compatible CPU's.
    MIPS            SGI systems.
    ALPHA           DEC Alpha CPU's.

    Letter    Base name
    B         Bitfield
    D         Date and time
    F         Floating point
    I         Signed integer
    R         References
    S         Character string
    U         Unsigned integer

    Letters    Byte order
    BE         Big endian
    LE         Little endian
    VX         Vax order

    Example         Description
    H5T_IEEE_F32BE  Four-byte, big-endian, IEEE floating point
    H5T_STD_U16BE   Two-byte, big-endian, unsigned integer
    H5T_UNIX_D32LE  Four-byte, little-endian, time_t
    H5T_INTEL_B64   Eight-byte bit field on an Intel CPU
    H5T_CRAY_F64    Eight-byte Cray floating point
    H5T_STD_ROBJ    Reference to an entire object in a file
 </pre>
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

    /**
     * The indexed RGB color model with 256 colors.
     * The palette values are stored in a two-dimensional byte array and arrange
     * by color components of red, green and blue. palette[][] = byte[3][256],
     * where, palette[0][], palette[1][] and palette[2][] are the red, green and
     * blue components respectively.
     */
    private byte[][] palette;

    /**
     * Creates a H5ScalarDS object with specific name, path, and parent.
     * <p>
     * @param fid the file identifier.
     * @param filename the full path of the file that contains this data object.
     * @param name the name of this H5ScalarDS.
     * @param path the full path of this H5ScalarDS.
     * @param oid the unique identifier of this data object.
     */
    public H5ScalarDS(
        int fid,
        String filename,
        String name,
        String path,
        long[] oid)
    {
        super (fid, filename, name, path, oid);
    }

    /**
     * Reads data from file into memory if the data is loaded or returns the
     * data is the data is not loaded.
     */
    public Object read() throws HDF5Exception
    {
        if (data != null)
            return data; // data is loaded

        if (rank <= 0) init();

        int did = open();
        int fspace=-1, tid=-1, nativeType=-1;

        try
        {
            fspace = H5.H5Dget_space(did);

            // set the rectangle selection
            H5.H5Sselect_hyperslab(
                fspace,
                HDF5Constants.H5S_SELECT_SET,
                startDims,
                null,     // set stride to 1
                selectedDims,
                null );   // set block to 1

            tid = H5.H5Dget_type(did);
            nativeType = H5Accessory.toNativeType(tid);
            long lsize = 1;
            for (int j=0; j<selectedDims.length; j++)
                lsize *= selectedDims[j];

            data = H5Accessory.allocateArray(nativeType, (int)lsize);

            if (data != null)
            {
                H5.H5Dread(
                    did,
                    nativeType,
                    fspace,
                    fspace,
                    HDF5Constants.H5P_DEFAULT,
                    data);

                int typeClass = H5.H5Tget_class(nativeType);
                if (typeClass==HDF5Constants.H5T_STRING)
                    data = byteToString((byte[])data, H5.H5Tget_size(nativeType));
                else if (typeClass == HDF5Constants.H5T_REFERENCE)
                    data = HDFNativeData.byteToLong((byte[])data);
            }
        }
        finally
        {
            if (fspace > 0) try { H5.H5Sclose(fspace); } catch (Exception ex2) {}
            try { H5.H5Tclose(tid); } catch (HDF5Exception ex2) {}
            try { H5.H5Tclose(nativeType); } catch (HDF5Exception ex2) {}
            close(did);
        }

        return data;
    }

    // ***** need to implement from DataFormat *****
    public boolean write() throws HDF5Exception
    {
        return false;
    }

    // ***** implement from DataFormat *****
    public List getMetadata() throws HDF5Exception
    {
        // load attributes first
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
     * @return true if successful and false otherwise.
     */
    public boolean writeMetadata(Object info) throws HDF5Exception
    {
        Attribute attr = null;

        // only attribute metadata is supported.
        if (info instanceof Attribute)
            attr = (Attribute)info;
        else
            return false;

        boolean b = true;
        String name = attr.getName();
        List attrList = getMetadata();
        boolean attrExisted = attrList.contains(attr);

        int did = open();
        try {
            b = H5Accessory.writeAttribute(did, attr, attrExisted);
            // add the new attribute into attribute list
            if (!attrExisted) attrList.add(attr);
        } finally
        {
            close(did);
        }

        return b;
    }

    /**
     * Deletes an attribute from this dataset.
     * <p>
     * @param info the attribute to delete.
     */
    public boolean removeMetadata(Object info) throws HDF5Exception
    {
        Attribute attr = null;

        // only attribute metadata is supported.
        if (info instanceof Attribute)
            attr = (Attribute)info;
        else
            return false;

        boolean b = true;
        int did = open();
        try {
            H5.H5Adelete(did, attr.getName());
        } finally {
            close(did);
        }

        // delete attribute from the List in memory
        List attrList = null;
        try { attrList = getMetadata(); }
        catch (Exception ex) {}

        if (b && attrList != null)
        {
            attrList.remove(attr);
        }

        return b;
    }

    /**
     * Opens this dataset for access.
     * <p>
     * @return a dataset identifier if successful; otherwise returns a negative
     * value.
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
     * Ends access to a dataset specified by dataset_id and releases resources
     * used by it. Further use of the dataset identifier is illegal in calls to
     * the dataset API.
     */
    public static boolean close(int dataset_id)
    {
        boolean b = true;

        try
        {
            H5.H5Dclose(dataset_id);
        } catch (HDF5Exception ex)
        {
            b = false;
        }

        return b;
    }

    /**
     * Retrieve and initialize dimensions and member information.
     */
    public void init()
    {
        int did=-1, sid=-1, tid=-1;
        int fid = getFID();
        String fullName = getPath()+getName();

        try {
            did = H5.H5Dopen(fid, fullName);
            sid = H5.H5Dget_space(did);
            tid= H5.H5Dget_type(did);
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
        } catch (HDF5Exception ex) {}
        finally
        {
            try { H5.H5Tclose(tid); } catch (HDF5Exception ex2) {}
            try { H5.H5Sclose(sid); } catch (HDF5Exception ex2) {}
            try { H5.H5Dclose(did); } catch (HDF5Exception ex2) {}
        }

        startDims = new long[rank];
        selectedDims = new long[rank];
        for (int i=0; i<rank; i++)
        {
            startDims[i] = 0;
            selectedDims[i] = 1;
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

}