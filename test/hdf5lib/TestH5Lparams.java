package test.hdf5lib;

import static org.junit.Assert.assertTrue;
import ncsa.hdf.hdf5lib.H5;
import ncsa.hdf.hdf5lib.exceptions.HDF5LibraryException;

import org.junit.Before;
import org.junit.Test;

public class TestH5Lparams {

    @Before
    public void checkOpenIDs() {
        assertTrue("H5 open ids is 0",H5.getOpenIDCount()==0);
    }

    @Test(expected = HDF5LibraryException.class)
    public void testH5Lget_val_invalid() throws Throwable {
        H5.H5Lget_val(-1, "Bogus", null, -1);
    }

    @Test(expected = NullPointerException.class)
    public void testH5Lget_val_null() throws Throwable {
        H5.H5Lget_val(-1, null, null, 0);
    }

    @Test(expected = HDF5LibraryException.class)
    public void testH5Lexists_invalid() throws Throwable {
        H5.H5Lexists(-1, "Bogus", -1);
    }

    @Test(expected = NullPointerException.class)
    public void testH5Lexists_null() throws Throwable {
        H5.H5Lexists(-1, null, 0);
    }

    @Test(expected = HDF5LibraryException.class)
    public void testH5Lget_info_invalid() throws Throwable {
        H5.H5Lget_info(-1, "Bogus", -1);
    }

    @Test(expected = NullPointerException.class)
    public void testH5Lget_info_null() throws Throwable {
        H5.H5Lget_info(-1, null, 0);
    }

    @Test(expected = HDF5LibraryException.class)
    public void testH5Lget_info_by_idx_invalid() throws Throwable {
        H5.H5Lget_info_by_idx(-1, "Bogus", -1, -1, -1L, -1);
    }

    @Test(expected = NullPointerException.class)
    public void testH5Lget_info_by_idx_null() throws Throwable {
        H5.H5Lget_info_by_idx(-1, null, 0, 0, 0L, 0);
    }

    @Test(expected = HDF5LibraryException.class)
    public void testH5Lget_name_by_idx_invalid() throws Throwable {
        H5.H5Lget_name_by_idx(-1, "Bogus", -1, -1, -1L, -1);
    }

    @Test(expected = NullPointerException.class)
    public void testH5Lget_name_by_idx_null() throws Throwable {
        H5.H5Lget_name_by_idx(-1, null, 0, 0, 0L, 0);
    }

    @Test(expected = HDF5LibraryException.class)
    public void testH5Lcreate_hard_invalid() throws Throwable {
        H5.H5Lcreate_hard(-1, "Bogus", -1, "Bogus", -1, -1);
    }

    @Test(expected = NullPointerException.class)
    public void testH5Lcreate_hard_null_current() throws Throwable {
        H5.H5Lcreate_hard(-1, null, 0, "Bogus", 0, 0);
    }

    @Test(expected = NullPointerException.class)
    public void testH5Lcreate_hard_null_dest() throws Throwable {
        H5.H5Lcreate_hard(-1, "Bogus", 0, null, 0, 0);
    }

    @Test(expected = HDF5LibraryException.class)
    public void testH5Ldelete_invalid() throws Throwable {
        H5.H5Ldelete(-1, "Bogus", -1);
    }

    @Test(expected = NullPointerException.class)
    public void testH5Ldelete_null() throws Throwable {
        H5.H5Ldelete(-1, null, 0);
    }

    @Test(expected = HDF5LibraryException.class)
    public void testH5Lcreate_soft_invalid() throws Throwable {
        H5.H5Lcreate_soft( "Bogus", -1, "Bogus", -1, -1);
    }

    @Test(expected = NullPointerException.class)
    public void testH5Lcreate_soft_null_current() throws Throwable {
        H5.H5Lcreate_soft(null, 0, "Bogus", 0, 0);
    }

    @Test(expected = NullPointerException.class)
    public void testH5Lcreate_soft_null_dest() throws Throwable {
        H5.H5Lcreate_soft("Bogus", 0, null, 0, 0);
    }

    @Test(expected = HDF5LibraryException.class)
    public void testH5Lcreate_external_invalid() throws Throwable {
        H5.H5Lcreate_external("PathToFile", "Bogus", -1, "Bogus", -1, -1);
    }

    @Test(expected = NullPointerException.class)
    public void testH5Lcreate_external_null_file() throws Throwable {
        H5.H5Lcreate_external(null, "Bogus", 0, "Bogus", 0, 0);
    }

    @Test(expected = NullPointerException.class)
    public void testH5Lcreate_external_null_current() throws Throwable {
        H5.H5Lcreate_external("PathToFile", null, 0, "Bogus", 0, 0);
    }

    @Test(expected = NullPointerException.class)
    public void testH5Lcreate_external_null_dest() throws Throwable {
        H5.H5Lcreate_external("PathToFile", "Bogus", 0, null, 0, 0);
    }

    @Test(expected = HDF5LibraryException.class)
    public void testH5Lcopy_invalid() throws Throwable {
        H5.H5Lcopy(-1, "Bogus", -1, "Bogus", -1, -1);
    }

    @Test(expected = NullPointerException.class)
    public void testH5Lcopy_null_current() throws Throwable {
        H5.H5Lcopy(-1, null, 0, "Bogus", 0, 0);
    }

    @Test(expected = NullPointerException.class)
    public void testH5Lcopy_null_dest() throws Throwable {
        H5.H5Lcopy(-1, "Bogus", 0, null, 0, 0);
    }

    @Test(expected = HDF5LibraryException.class)
    public void testH5Lmove_invalid() throws Throwable {
        H5.H5Lmove(-1, "Bogus", -1, "Bogus", -1, -1);
    }

    @Test(expected = NullPointerException.class)
    public void testH5Lmove_null_current() throws Throwable {
        H5.H5Lmove(-1, null, 0, "Bogus", 0, 0);
    }

    @Test(expected = NullPointerException.class)
    public void testH5Lmove_null_dest() throws Throwable {
        H5.H5Lmove(-1, "Bogus", 0, null, 0, 0);
    }

    @Test(expected = HDF5LibraryException.class)
    public void testH5Lget_val_by_idx_invalid() throws Throwable {
        H5.H5Lget_val_by_idx(-1, "Bogus", -1, -1, -1L, null, -1);
    }

    @Test(expected = NullPointerException.class)
    public void testH5Lget_val_by_idx_null() throws Throwable {
        H5.H5Lget_val_by_idx(-1, null, 0, 0, 0L, null, 0);
    }

    @Test(expected = HDF5LibraryException.class)
    public void testH5Ldelete_by_idx_invalid() throws Throwable {
        H5.H5Ldelete_by_idx(-1, "Bogus", -1, -1, -1L, -1);
    }

    @Test(expected = NullPointerException.class)
    public void testH5Ldelete_by_idx_null() throws Throwable {
        H5.H5Ldelete_by_idx(-1, null, 0, 0, 0L, 0);
    }

    @Test(expected = NullPointerException.class)
    public void testH5Lvisit_null() throws Throwable {
        H5.H5Lvisit(-1, -1, -1, null, null);
    }

    @Test(expected = NullPointerException.class)
    public void testH5Lvisit_by_name_nullname() throws Throwable {
        H5.H5Lvisit_by_name(-1, null, -1, -1, null, null, -1);
    }

    @Test(expected = NullPointerException.class)
    public void testH5Lvisit_by_name_null() throws Throwable {
        H5.H5Lvisit_by_name(-1, "Bogus", -1, -1, null, null, -1);
    }

    @Test(expected = NullPointerException.class)
    public void testH5Literate_null() throws Throwable {
        H5.H5Literate(-1, -1, -1, -1, null, null);
    }

    @Test(expected = NullPointerException.class)
    public void testH5Literate_by_name_nullname() throws Throwable {
        H5.H5Literate_by_name(-1, null, -1, -1, -1, null, null, -1);
    }

    @Test(expected = NullPointerException.class)
    public void testH5Literate_by_name_null() throws Throwable {
        H5.H5Literate_by_name(-1, "Bogus", -1, -1, -1, null, null, -1);
    }

}
