package test.hdf5lib;

import static org.junit.Assert.assertTrue;
import ncsa.hdf.hdf5lib.H5;
import ncsa.hdf.hdf5lib.exceptions.HDF5LibraryException;

import org.junit.Before;
import org.junit.Test;

public class TestH5Dparams {

    @Before
    public void checkOpenIDs() {
        assertTrue("H5 open ids is 0",H5.getOpenIDCount()==0);
    }

    @Test//(expected = HDF5LibraryException.class)
    public void testH5Dclose_invalid() throws Throwable {
    	int did = H5.H5Dclose(-1);
        assertTrue(did == 0);
    }

    @Test(expected = NullPointerException.class)
    public void testH5Dcreate_null() throws Throwable {
        H5.H5Dcreate(-1, null, 0, 0, 0, 0, 0);
    }

    @Test(expected = HDF5LibraryException.class)
    public void testH5Dcreate_invalid() throws Throwable {
        H5.H5Dcreate(-1, "Bogus", -1, -1, -1, -1, -1);
    }

    @Test(expected = HDF5LibraryException.class)
    public void testH5Dcreate_anon_invalid() throws Throwable {
        H5.H5Dcreate_anon(-1, -1, -1, -1, -1);
    }

    @Test(expected = HDF5LibraryException.class)
    public void testH5Dget_access_plist_invalid() throws Throwable {
        H5.H5Dget_access_plist(-1);
    }

    @Test(expected = HDF5LibraryException.class)
    public void testH5Dget_create_plist_invalid() throws Throwable {
        H5.H5Dget_create_plist(-1);
    }

    @Test(expected = HDF5LibraryException.class)
    public void testH5Dget_offset_invalid() throws Throwable {
        H5.H5Dget_offset(-1);
    }

    @Test(expected = HDF5LibraryException.class)
    public void testH5Dget_space_invalid() throws Throwable {
        H5.H5Dget_space(-1);
    }

    @Test(expected = HDF5LibraryException.class)
    public void testH5Dget_type_invalid() throws Throwable {
        H5.H5Dget_type(-1);
    }

    @Test(expected = HDF5LibraryException.class)
    public void testH5Dget_space_status_invalid() throws Throwable {
        int[] status = new int[2];
        H5.H5Dget_space_status(-1, status);
    }

    @Test(expected = NullPointerException.class)
    public void testH5Dget_space_status_null() throws Throwable {
        H5.H5Dget_space_status(-1, null);
    }

    @Test(expected = HDF5LibraryException.class)
    public void testH5Dset_extent_status_invalid() throws Throwable {
        long[] size = new long[2];
        H5.H5Dset_extent(-1, size);
    }

    @Test(expected = NullPointerException.class)
    public void testH5Dset_extent_status_null() throws Throwable {
        H5.H5Dset_extent(-1, null);
    }

    @Test(expected = NullPointerException.class)
    public void testH5Dopen_null() throws Throwable {
        H5.H5Dopen(-1, null, 0);
    }

    @Test(expected = HDF5LibraryException.class)
    public void testH5Dopen_invalid() throws Throwable {
        H5.H5Dopen(-1, "Bogus", 0);
    }

    @Test(expected = HDF5LibraryException.class)
    public void testH5Dvlen_get_buf_size_invalid() throws Throwable {
        int[] size = new int[2];
        H5.H5Dvlen_get_buf_size(-1, -1, -1, size);
    }

    @Test(expected = NullPointerException.class)
    public void testH5Dvlen_get_buf_size_null() throws Throwable {
        H5.H5Dvlen_get_buf_size(-1, -1, -1, null);
    }

    @Test(expected = HDF5LibraryException.class)
    public void testH5Dvlen_reclaim_invalid() throws Throwable {
        byte[] buf = new byte[2];
        H5.H5Dvlen_reclaim(-1, -1, -1, buf);
    }

    @Test(expected = NullPointerException.class)
    public void testH5Dvlen_reclaim_null() throws Throwable {
        H5.H5Dvlen_reclaim(-1, -1, -1, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testH5Dget_storage_size_invalid() throws Throwable {
        H5.H5Dget_storage_size(-1);
    }

}
