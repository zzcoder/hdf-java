/**
 * 
 */
package test.hdf5lib;


import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import ncsa.hdf.hdf5lib.H5;
import ncsa.hdf.hdf5lib.HDF5Constants;
import ncsa.hdf.hdf5lib.exceptions.HDF5Exception;
import ncsa.hdf.hdf5lib.exceptions.HDF5LibraryException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestH5S {
    int H5sid = -1;
    int H5rank = 2;
    long H5dims[] = {5, 5};
    long H5maxdims[] = {10, 10};

    @Before
    public void createH5file()
            throws NullPointerException, HDF5Exception {
        assertTrue("H5 open ids is 0",H5.getOpenIDCount()==0);

        H5sid = H5.H5Screate_simple(H5rank, H5dims, H5maxdims);
        assertTrue("H5.H5Screate_simple_extent",H5sid > 0);
    }

    @After
    public void deleteH5file() throws HDF5LibraryException {
        if (H5sid > 0) {
            H5.H5Sclose(H5sid);
        }
    }

    @Test
    public void testH5Sget_simple_extent_ndims()
            throws Throwable, HDF5LibraryException, NullPointerException {
        int read_rank = -1;
        try {
            read_rank = H5.H5Sget_simple_extent_ndims(H5sid);
            assertTrue("H5.H5Screate_simple_extent_ndims",H5rank == read_rank);
        }
        catch (Throwable err) {
            err.printStackTrace();
            fail("H5.H5Sget_simple_extent_ndims: " + err);
        }
    }

    @Test
    public void testH5Sget_simple_extent_dims_null()
            throws Throwable, HDF5LibraryException, NullPointerException {
        int read_rank = -1;
        
        try {
            read_rank = H5.H5Sget_simple_extent_dims(H5sid, null, null);
            assertTrue("H5.H5Screate_simple_extent_dims",H5rank == read_rank);
        }
        catch (Throwable err) {
            err.printStackTrace();
            fail("H5.H5Sget_simple_extent_dims: " + err);
        }
    }

    @Test
    public void testH5Sget_simple_extent_dims()
            throws Throwable, HDF5LibraryException, NullPointerException {
        int read_rank = -1;
        long dims[] = {5, 5};
        long maxdims[] = {10, 10};
        
        try {
            read_rank = H5.H5Sget_simple_extent_dims(H5sid, dims, maxdims);
            assertTrue("H5.H5Screate_simple_extent_dims",H5rank == read_rank);
            assertTrue("H5.H5Screate_simple_extent_dims:dims",H5dims[0] == dims[0]);
            assertTrue("H5.H5Screate_simple_extent_dims:maxdims",H5maxdims[0] == maxdims[0]);
        }
        catch (Throwable err) {
            err.printStackTrace();
            fail("H5.H5Sget_simple_extent_dims: " + err);
        }
    }

    @Test
    public void testH5Sis_simple()
            throws Throwable, HDF5LibraryException, NullPointerException {
        boolean result = false;
        
        try {
            result = H5.H5Sis_simple(H5sid);
            assertTrue("H5.H5Sis_simple",result);
        }
        catch (Throwable err) {
            err.printStackTrace();
            fail("H5.H5Sis_simple: " + err);
        }
    }

    @Test
    public void testH5Scopy()
            throws Throwable, HDF5LibraryException, NullPointerException {
        int sid = -1;
        int read_rank = -1;

        try {
            sid = H5.H5Scopy(H5sid);
            assertTrue("H5.H5Sis_simple",sid > 0);
            read_rank = H5.H5Sget_simple_extent_ndims(sid);
            assertTrue("H5.H5Screate_simple_extent_ndims",H5rank == read_rank);
        }
        catch (Throwable err) {
            err.printStackTrace();
            fail("H5.H5Scopy: " + err);
        }

        try {H5.H5Sclose(sid);} catch (Exception ex) {}
    }

    @Test
    public void testH5Sextent_copy()
            throws Throwable, HDF5LibraryException, NullPointerException {
        int sid = -1;
        int class_type = -1;
        
        try {
            sid = H5.H5Screate(HDF5Constants.H5S_NULL);
            assertTrue("H5.H5Screate_null",sid > 0);
            H5.H5Sextent_copy(sid, H5sid);
        }
        catch (Throwable err) {
            err.printStackTrace();
            fail("H5.H5Sextent_copy: " + err);
        }
        class_type = H5.H5Sget_simple_extent_type(sid);
        assertTrue("H5.H5Screate_null: type",class_type == HDF5Constants.H5S_SIMPLE);

        try {H5.H5Sclose(sid);} catch (Exception ex) {}
    }

    @Test
    public void testH5Sextent_equal()
            throws Throwable, HDF5LibraryException, NullPointerException {
        int sid = -1;
        int class_type = -1;
        boolean result = false;
        
        try {
            sid = H5.H5Screate(HDF5Constants.H5S_NULL);
            assertTrue("H5.H5Screate_null",sid > 0);
        }
        catch (Throwable err) {
            err.printStackTrace();
            fail("H5.H5Screate: null " + err);
        }
        result = H5.H5Sextent_equal(sid, H5sid);
        assertFalse("H5.testH5Sextent_equal",result);
        
        try {
            H5.H5Sextent_copy(sid, H5sid);
        }
        catch (Throwable err) {
            err.printStackTrace();
            fail("H5.H5Sextent_copy " + err);
        }
        result = H5.H5Sextent_equal(sid, H5sid);
        assertTrue("H5.testH5Sextent_equal",result);

        try {H5.H5Sclose(sid);} catch (Exception ex) {}
    }

    @Test
    public void testH5Sencode_decode_null_dataspace()
            throws Throwable, HDF5LibraryException, NullPointerException {
        int sid = -1;
        int decoded_sid = -1;
        byte[] null_sbuf = null;
        boolean result = false;
        
        try {
            sid = H5.H5Screate(HDF5Constants.H5S_NULL);
            assertTrue("H5.H5Screate_null",sid > 0);
        }
        catch (Throwable err) {
            err.printStackTrace();
            fail("H5.H5Screate: null " + err);
        }
        
        try {
            null_sbuf = H5.H5Sencode(sid);
        }
        catch (Throwable err) {
            err.printStackTrace();
            fail("H5.H5Sencode " + err);
        }
        assertFalse("H5.testH5Sencode",null_sbuf==null);
        
        try {
            decoded_sid = H5.H5Sdecode(null_sbuf);
        }
        catch (Throwable err) {
            err.printStackTrace();
            fail("H5.H5Sdecode " + err);
        }
        assertTrue("H5.testH5Sdecode",decoded_sid>0);

        result = H5.H5Sextent_equal(sid, decoded_sid);
        assertTrue("H5.testH5Sextent_equal",result);

        try {H5.H5Sclose(decoded_sid);} catch (Exception ex) {}
        try {H5.H5Sclose(sid);} catch (Exception ex) {}
    }

    @Test
    public void testH5Sencode_decode_scalar_dataspace()
            throws Throwable, HDF5LibraryException, NullPointerException {
        int sid = -1;
        int decoded_sid = -1;
        byte[] scalar_sbuf = null;
        boolean result = false;
        int iresult = -1;
        long lresult = -1;
        
        try {
            sid = H5.H5Screate(HDF5Constants.H5S_SCALAR);
            assertTrue("H5.H5Screate_null",sid > 0);
        }
        catch (Throwable err) {
            err.printStackTrace();
            fail("H5.H5Screate: null " + err);
        }
        
        try {
            scalar_sbuf = H5.H5Sencode(sid);
        }
        catch (Throwable err) {
            err.printStackTrace();
            fail("H5.H5Sencode " + err);
        }
        assertFalse("H5.testH5Sencode",scalar_sbuf==null);
        
        try {
            decoded_sid = H5.H5Sdecode(scalar_sbuf);
        }
        catch (Throwable err) {
            err.printStackTrace();
            fail("H5.H5Sdecode " + err);
        }
        assertTrue("H5.testH5Sdecode",decoded_sid>0);

        result = H5.H5Sextent_equal(sid, decoded_sid);
        assertTrue("H5.testH5Sextent_equal",result);
        
        /* Verify decoded dataspace */
        lresult = H5.H5Sget_simple_extent_npoints(decoded_sid);
        assertTrue("H5.testH5Sget_simple_extent_npoints",lresult==1);

        iresult = H5.H5Sget_simple_extent_ndims(decoded_sid);
        assertTrue("H5.testH5Sget_simple_extent_ndims",iresult==0);

        try {H5.H5Sclose(decoded_sid);} catch (Exception ex) {}
        try {H5.H5Sclose(sid);} catch (Exception ex) {}
    }

}
