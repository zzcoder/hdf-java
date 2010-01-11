package test.junit;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;

import ncsa.hdf.hdf5lib.H5;
import ncsa.hdf.hdf5lib.HDF5Constants;
import ncsa.hdf.hdf5lib.exceptions.HDF5Exception;
import ncsa.hdf.hdf5lib.exceptions.HDF5LibraryException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestH5T {
    private static final String H5_FILE = "test.h5";
    int H5fid = -1;
    int H5strdid = -1;

    private final void _deleteFile(String filename) {
        File file = new File(filename);

        if (file.exists()) {
            try {
                file.delete();
            }
            catch (SecurityException e) {
                ;// e.printStackTrace();
            }
        }
    }

    @Before
    public void createH5file()
            throws NullPointerException, HDF5Exception {
        H5fid = H5.H5Fcreate(H5_FILE, HDF5Constants.H5F_ACC_TRUNC,
                HDF5Constants.H5P_DEFAULT, HDF5Constants.H5P_DEFAULT);
        assertTrue("H5.H5Fcreate",H5fid > 0);
        H5strdid = H5.H5Tcopy(HDF5Constants.H5T_C_S1);
        assertTrue("H5.H5Tcopy",H5strdid > 0);

        H5.H5Fflush(H5fid, HDF5Constants.H5F_SCOPE_LOCAL);
    }

    @After
    public void deleteH5file() throws HDF5LibraryException {
        if (H5strdid >= 0)
            H5.H5Tclose(H5strdid);
        if (H5fid > 0) 
            H5.H5Fclose(H5fid);

        _deleteFile(H5_FILE);
    }
    
    @Test
    public void testH5Tget_class() throws Throwable, HDF5LibraryException {
        int result = H5.H5Tget_class(H5strdid);
        assertTrue("H5.H5Tget_class",result > 0);
        String class_name = H5.H5Tget_class_name(result);
        assertTrue("H5.H5Tget_class",class_name.compareTo("H5T_STRING")==0);
    }
    
    @Test
    public void testH5Tget_size() throws Throwable, HDF5LibraryException {
        long dt_size = -1; 
        try {
            dt_size = H5.H5Tget_size(H5strdid);
        }
        catch (Throwable err) {
            err.printStackTrace();
            fail("testH5Tget_size:H5.H5Tget_size " + err);
        }
        assertTrue("testH5Tget_size",dt_size > 0);
    }
    
    @Test
    public void testH5Tset_size() throws Throwable, HDF5LibraryException {
        long dt_size = 5; 
        try {
            H5.H5Tset_size(H5strdid, dt_size);
        }
        catch (Throwable err) {
            err.printStackTrace();
            fail("testH5Tset_size:H5.H5Tset_size " + err);
        }
        try {
            dt_size = H5.H5Tget_size(H5strdid);
        }
        catch (Throwable err) {
            err.printStackTrace();
            fail("testH5Tget_size:H5.H5Tget_size " + err);
        }
        assertTrue("testH5Tget_size",dt_size == 5);
    }
    
    @Test
    public void testH5Tarray_create() throws Throwable, HDF5LibraryException {
       int filetype_id = -1;
       long[] adims = { 3, 5 };

       try {
           filetype_id = H5.H5Tarray_create(HDF5Constants.H5T_STD_I64LE, 2, adims);
        }
        catch (Throwable err) {
            err.printStackTrace();
            fail("testH5Tarray_create.H5Tarray_create " + err);
        }
        assertTrue("testH5Tarray_create",filetype_id > 0);
        if (filetype_id >= 0)
            H5.H5Tclose(filetype_id);
    }
    
    @Test
    public void testH5Tget_array_dims() throws Throwable, HDF5LibraryException {
       int filetype_id = -1;
       int ndims = 0;
       long[] adims = { 3, 5 };
       long[] rdims = new long[2];

       try {
           filetype_id = H5.H5Tarray_create(HDF5Constants.H5T_STD_I64LE, 2, adims);
        }
        catch (Throwable err) {
            err.printStackTrace();
            fail("testH5Tarray_create.H5Tarray_create " + err);
        }
        assertTrue("testH5Tget_array_dims:H5Tarray_create",filetype_id > 0);
       try {
           ndims = H5.H5Tget_array_dims(filetype_id, rdims);
        }
        catch (Throwable err) {
            err.printStackTrace();
            fail("testH5Tget_array_dims.H5Tget_array_dims " + err);
        }
        assertTrue("testH5Tget_array_dims",ndims == 2);
        assertTrue("testH5Tget_array_dims",adims[0] == rdims[0]);
        assertTrue("testH5Tget_array_dims",adims[1] == rdims[1]);
        
        if (filetype_id >= 0)
            H5.H5Tclose(filetype_id);
    }

}
