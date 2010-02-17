/**
 * 
 */
package test.hdf5lib;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import ncsa.hdf.hdf5lib.H5;
import ncsa.hdf.hdf5lib.HDF5Constants;
import ncsa.hdf.hdf5lib.exceptions.HDF5FunctionArgumentException;
import ncsa.hdf.hdf5lib.exceptions.HDF5LibraryException;

import org.junit.Before;
import org.junit.Test;

public class TestH5Sbasic {

    @Before
    public void checkOpenIDs() {
        assertTrue("H5 open ids is 0",H5.getOpenIDCount()==0);
    }

    @Test
    public void testH5Sclose_invalid() throws Throwable, HDF5LibraryException {
        assertTrue(H5.H5Sclose(-1)<0);
    }

    @Test(expected = HDF5LibraryException.class)
    public void testH5Screate_invalid()
            throws Throwable, HDF5LibraryException, NullPointerException {
        H5.H5Screate(-1);
    }

    @Test(expected = HDF5LibraryException.class)
    public void testH5Sget_simple_extent_type_invalid()
            throws Throwable, HDF5LibraryException, NullPointerException {
        H5.H5Sget_simple_extent_type(-1);
    }

    @Test
    public void testH5Screate_scalar()
            throws Throwable, HDF5LibraryException, NullPointerException {
        int sid = -1;
        int class_type = -1;
        try {
            sid = H5.H5Screate(HDF5Constants.H5S_SCALAR);
        }
        catch (Throwable err) {
            err.printStackTrace();
            fail("H5.H5Screate: " + err);
        }
        assertTrue("H5.H5Screate_scalar",sid > 0);
        class_type = H5.H5Sget_simple_extent_type(sid);
        assertTrue("H5.H5Screate_scalar: type",class_type == HDF5Constants.H5S_SCALAR);

        try {H5.H5Sclose(sid);} catch (Exception ex) {ex.printStackTrace();}
    }

    @Test
    public void testH5Screate_null()
            throws Throwable, HDF5LibraryException, NullPointerException {
        int sid = -1;
        int class_type = -1;
        try {
            sid = H5.H5Screate(HDF5Constants.H5S_NULL);
        }
        catch (Throwable err) {
            err.printStackTrace();
            fail("H5.H5Screate: " + err);
        }
        assertTrue("H5.H5Screate_null",sid > 0);
        class_type = H5.H5Sget_simple_extent_type(sid);
        assertTrue("H5.H5Screate_null: type",class_type == HDF5Constants.H5S_NULL);

        try {H5.H5Sclose(sid);} catch (Exception ex) {}
    }

    @Test(expected = NullPointerException.class)
    public void testH5Screate_simple_dims_null()
            throws Throwable, HDF5LibraryException, NullPointerException {
        H5.H5Screate_simple(2, (long[])null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testH5Screate_simple_rank_invalid()
            throws Throwable, HDF5LibraryException, NullPointerException {
        long dims[] = {5, 5};
        H5.H5Screate_simple(-1, dims, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testH5Screate_simple_dims_invalid()
            throws Throwable, HDF5LibraryException, NullPointerException {
        long dims[] = {2, 2};
        H5.H5Screate_simple(5, dims, null);
    }

    @Test(expected = HDF5FunctionArgumentException.class)
    public void testH5Screate_simple_dims_exceed()
            throws Throwable, HDF5LibraryException, NullPointerException {
        long dims[] = {0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,
                21,22,23,24,25,26,27,28,29,30,31,32,33,34};
        H5.H5Screate_simple(35, dims, null);
    }

    @Test(expected = HDF5FunctionArgumentException.class)
    public void testH5Screate_simple_dims_zero()
            throws Throwable, HDF5LibraryException, NullPointerException {
        long dims[] = {0, 0};
        H5.H5Screate_simple(2, dims, null);
    }

    @Test
    public void testH5Screate_simple()
            throws Throwable, HDF5LibraryException, NullPointerException {
        int sid = -1;
        int class_type = -1;
        int rank = 2;
        long dims[] = {5, 5};
        long maxdims[] = {10, 10};
        
        try {
            sid = H5.H5Screate_simple(rank, dims, maxdims);
        }
        catch (Throwable err) {
            err.printStackTrace();
            fail("H5.H5Screate_simple: " + err);
        }
        assertTrue("H5.H5Screate_simple",sid > 0);
        class_type = H5.H5Sget_simple_extent_type(sid);
        assertTrue("H5.H5Screate_simple: type",class_type == HDF5Constants.H5S_SIMPLE);

        try {H5.H5Sclose(sid);} catch (Exception ex) {}
    }

    @Test
    public void testH5Screate_simple_max_default()
            throws Throwable, HDF5LibraryException, NullPointerException {
        int sid = -1;
        int rank = 2;
        long dims[] = {5, 5};
        
        try {
            sid = H5.H5Screate_simple(rank, dims, null);
        }
        catch (Throwable err) {
            err.printStackTrace();
            fail("H5.H5Screate_simple: " + err);
        }
        assertTrue("H5.H5Screate_simple_max_default",sid > 0);

        try {H5.H5Sclose(sid);} catch (Exception ex) {}
    }

    @Test
    public void testH5Screate_simple_extent()
            throws Throwable, HDF5LibraryException, NullPointerException {
        int sid = -1;
        int rank = 2;
        long dims[] = {5, 5};
        long maxdims[] = {10, 10};
        
        try {
            sid = H5.H5Screate(HDF5Constants.H5S_SIMPLE);
            assertTrue("H5.H5Screate_simple_extent",sid > 0);
            H5.H5Sset_extent_simple(sid, rank, dims, maxdims);
        }
        catch (Throwable err) {
            err.printStackTrace();
            fail("H5.H5Screate: " + err);
        }

        try {H5.H5Sclose(sid);} catch (Exception ex) {}
    }

    @Test(expected = IllegalArgumentException.class)
    public void testH5Sencode_invalid()
            throws Throwable, HDF5LibraryException, NullPointerException {
        H5.H5Sencode(-1);
    }

    @Test(expected = NullPointerException.class)
    public void testH5Sdecode_null()
            throws Throwable, HDF5LibraryException, NullPointerException {
        H5.H5Sdecode(null);
    }

}
