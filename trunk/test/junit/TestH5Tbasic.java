package test.junit;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import ncsa.hdf.hdf5lib.H5;
import ncsa.hdf.hdf5lib.HDF5Constants;
import ncsa.hdf.hdf5lib.exceptions.HDF5FunctionArgumentException;
import ncsa.hdf.hdf5lib.exceptions.HDF5LibraryException;

import org.junit.Test;

public class TestH5Tbasic {

    @Test(expected = HDF5LibraryException.class)
    public void testH5Tclose_invalid() throws Throwable, HDF5LibraryException {
        H5.H5Tclose(-1);
    }

    @Test(expected = HDF5LibraryException.class)
    public void testH5Tcreate_invalid()
            throws Throwable, HDF5LibraryException {
        H5.H5Tcreate(-1, 0);
    }

    @Test(expected = NullPointerException.class)
    public void testH5Topen_null() throws Throwable, HDF5LibraryException, NullPointerException {
        H5.H5Topen(-1, null, 0);
    }

    @Test(expected = HDF5LibraryException.class)
    public void testH5Topen_invalid() throws Throwable, HDF5LibraryException, NullPointerException {
        H5.H5Topen(-1, "Bogus", 0);
    }

    @Test(expected = NullPointerException.class)
    public void testH5Tcommit_null() throws Throwable, HDF5LibraryException, NullPointerException {
        H5.H5Tcommit(-1, null, 0, -1, -1, -1);
    }

    @Test(expected = HDF5LibraryException.class)
    public void testH5Tcommit_invalid() throws Throwable, HDF5LibraryException, NullPointerException {
        H5.H5Tcommit(-1, "Bogus", -1, -1, -1, -1);
    }

    @Test(expected = NullPointerException.class)
    public void testH5Tget_pad_null() throws Throwable, HDF5LibraryException, NullPointerException {
        H5.H5Tget_pad(-1, null);
    }

    @Test(expected = NullPointerException.class)
    public void testH5Tget_fields_null() throws Throwable, HDF5LibraryException, NullPointerException {
        H5.H5Tget_fields(-1, (long[])null);
    }

    @Test(expected = NullPointerException.class)
    public void testH5Tget_member_index_null() throws Throwable, HDF5LibraryException, NullPointerException {
        H5.H5Tget_member_index(-1, null);
    }

    @Test(expected = NullPointerException.class)
    public void testH5Tinsert_null() throws Throwable, HDF5LibraryException, NullPointerException {
        H5.H5Tinsert(-1, null, 0, 0);
    }

    @Test(expected = NullPointerException.class)
    public void testH5Tset_tag_null() throws Throwable, HDF5LibraryException, NullPointerException {
        H5.H5Tset_tag(-1, null);
    }

    @Test(expected = NullPointerException.class)
    public void testH5Tget_member_value_null() throws Throwable, HDF5LibraryException, NullPointerException {
        H5.H5Tget_member_value(-1, -1, (byte[])null);
    }

    @Test(expected = NullPointerException.class)
    public void testH5Tget_array_dims_null() throws Throwable, HDF5LibraryException, NullPointerException {
        H5.H5Tget_array_dims(-1, null);
    }

    @Test(expected = NullPointerException.class)
    public void testH5Tenum_insert_name_null() throws Throwable, HDF5LibraryException, NullPointerException {
        H5.H5Tenum_insert(-1, null, (byte[])null);
    }

    @Test(expected = NullPointerException.class)
    public void testH5Tenum_insert_null() throws Throwable, HDF5LibraryException, NullPointerException {
        H5.H5Tenum_insert(-1, "bogus", (byte[])null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testH5Tenum_nameof_invalid() throws Throwable, HDF5LibraryException, NullPointerException {
        H5.H5Tenum_nameof(-1, null, -1);
    }

    @Test(expected = NullPointerException.class)
    public void testH5Tenum_nameof_value_null() throws Throwable, HDF5LibraryException, NullPointerException {
        H5.H5Tenum_nameof(-1, null, 1);
    }

    @Test(expected = NullPointerException.class)
    public void testH5Tenum_valueof_name_null() throws Throwable, HDF5LibraryException, NullPointerException {
        H5.H5Tenum_valueof(-1, null, (byte[])null);
    }

    @Test(expected = NullPointerException.class)
    public void testH5Tenum_valueof_null() throws Throwable, HDF5LibraryException, NullPointerException {
        H5.H5Tenum_valueof(-1, "bogus", (byte[])null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testH5Tarray_create_invalid() throws Throwable, HDF5LibraryException, NullPointerException {
        H5.H5Tarray_create(-1, -1, null);
    }

    @Test(expected = NullPointerException.class)
    public void testH5Tarray_create_value_null() throws Throwable, HDF5LibraryException, NullPointerException {
        H5.H5Tarray_create(-1, 1, null);
    }

}
