package test.hdf5lib;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import ncsa.hdf.hdf5lib.H5;
import ncsa.hdf.hdf5lib.HDF5Constants;
import ncsa.hdf.hdf5lib.exceptions.HDF5LibraryException;
import ncsa.hdf.hdf5lib.exceptions.HDF5SymbolTableException;
import ncsa.hdf.hdf5lib.structs.H5L_info_t;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestH5Lbasic {
    private static final boolean is16 = H5.isAPI16;
    private static final String H5_FILE = "test/hdf5lib/h5ex_g_iterate.h5";
    private static long H5la_ds1 = -1;
    private static long H5la_l1 = -1;
    int H5fid = -1;

    @Before
    public void openH5file()
            throws HDF5LibraryException, NullPointerException {
        assertTrue("H5 open ids is 0",H5.getOpenIDCount()==0);

        try {
            H5fid = H5.H5Fopen(H5_FILE, HDF5Constants.H5F_ACC_RDONLY,
                HDF5Constants.H5P_DEFAULT);
        }
        catch (Throwable err) {
            err.printStackTrace();
            fail("H5.H5Fopen: openH5file: " + err);
        }
    }

    @After
    public void closeH5file() throws HDF5LibraryException {
        if (H5fid > 0) {
            H5.H5Fclose(H5fid);
        }
    }

    @Test
    public void testH5Lexists() throws Throwable, HDF5LibraryException, NullPointerException {
        boolean link_exists = false;
        try {
            link_exists = H5.H5Lexists(H5fid, "None", HDF5Constants.H5P_DEFAULT);
        }
        catch (Throwable err) {
            err.printStackTrace();
            fail("H5.H5Lexists: " + err);
        }
        assertFalse("H5Lexists ",link_exists);
        try {
            link_exists = H5.H5Lexists(H5fid, "DS1", HDF5Constants.H5P_DEFAULT);
        }
        catch (Throwable err) {
            err.printStackTrace();
            fail("H5.H5Lexists: " + err);
        }
        assertTrue("H5Lexists ",link_exists);
        try {
            link_exists = H5.H5Lexists(H5fid, "G1/DS2", HDF5Constants.H5P_DEFAULT);
        }
        catch (Throwable err) {
            err.printStackTrace();
            fail("H5.H5Lexists: " + err);
        }
        assertTrue("H5Lexists ",link_exists);
    }

    @Test(expected = HDF5SymbolTableException.class)
    public void testH5Lget_info_not_exist() throws Throwable, HDF5LibraryException, NullPointerException {
        H5.H5Lget_info(H5fid, "None", HDF5Constants.H5P_DEFAULT);
    }

    @Test
    public void testH5Lget_info_dataset() throws Throwable, HDF5LibraryException, NullPointerException {
        H5L_info_t link_info = null;
        try {
            link_info = H5.H5Lget_info(H5fid, "DS1", HDF5Constants.H5P_DEFAULT);
        }
        catch (Throwable err) {
            err.printStackTrace();
            fail("H5.H5Lget_info: " + err);
        }
        assertFalse("H5Lget_info ",link_info==null);
        assertTrue("H5Lget_info link type",link_info.type==HDF5Constants.H5L_TYPE_HARD);
        H5la_ds1 = link_info.address_val_size;
    }

    @Test
    public void testH5Lget_info_hardlink() throws Throwable, HDF5LibraryException, NullPointerException {
        H5L_info_t link_info = null;
        try {
            link_info = H5.H5Lget_info(H5fid, "L1", HDF5Constants.H5P_DEFAULT);
        }
        catch (Throwable err) {
            err.printStackTrace();
            fail("H5.H5Lget_info: " + err);
        }
        assertFalse("H5Lget_info ",link_info==null);
        assertTrue("H5Lget_info link type",link_info.type==HDF5Constants.H5L_TYPE_HARD);
        assertTrue("Link Address ",link_info.address_val_size>0);
        H5la_l1 = link_info.address_val_size;
    }

    @Test(expected = HDF5SymbolTableException.class)
    public void testH5Lget_info_by_idx_not_exist() throws Throwable, HDF5LibraryException, NullPointerException {
        H5.H5Lget_info_by_idx(H5fid, "None", HDF5Constants.H5_INDEX_CRT_ORDER, HDF5Constants.H5_ITER_INC, 0, HDF5Constants.H5P_DEFAULT);
    }

    @Test
    public void testH5Lget_info_by_idx_n0() throws Throwable, HDF5LibraryException, NullPointerException {
        H5L_info_t link_info = null;
        try {
            link_info = H5.H5Lget_info_by_idx(H5fid, "/", HDF5Constants.H5_INDEX_NAME, HDF5Constants.H5_ITER_INC, 0, HDF5Constants.H5P_DEFAULT);
        }
        catch (Throwable err) {
            err.printStackTrace();
            fail("H5.H5Lget_info_by_idx: " + err);
        }
        assertFalse("H5Lget_info_by_idx ",link_info==null);
        assertTrue("H5Lget_info_by_idx link type",link_info.type==HDF5Constants.H5L_TYPE_HARD);
        assertTrue("Link Address ",link_info.address_val_size==H5la_ds1);
    }

    @Test
    public void testH5Lget_info_by_idx_n3() throws Throwable, HDF5LibraryException, NullPointerException {
        H5L_info_t link_info = null;
        try {
            link_info = H5.H5Lget_info_by_idx(H5fid, "/", HDF5Constants.H5_INDEX_NAME, HDF5Constants.H5_ITER_INC, 3, HDF5Constants.H5P_DEFAULT);
        }
        catch (Throwable err) {
            err.printStackTrace();
            fail("H5.H5Lget_info_by_idx: " + err);
        }
        assertFalse("H5Lget_info_by_idx ",link_info==null);
        assertTrue("H5Lget_info_by_idx link type",link_info.type==HDF5Constants.H5L_TYPE_HARD);
        assertTrue("Link Address ",link_info.address_val_size==H5la_l1);
    }

    @Test(expected = HDF5SymbolTableException.class)
    public void testH5Lget_name_by_idx_not_exist() throws Throwable, HDF5LibraryException, NullPointerException {
        H5.H5Lget_name_by_idx(H5fid, "None", HDF5Constants.H5_INDEX_CRT_ORDER, HDF5Constants.H5_ITER_INC, 0, HDF5Constants.H5P_DEFAULT);
    }

    @Test
    public void testH5Lget_name_by_idx_n0() throws Throwable, HDF5LibraryException, NullPointerException {
        String link_name = null;
        try {
            link_name = H5.H5Lget_name_by_idx(H5fid, "/", HDF5Constants.H5_INDEX_NAME, HDF5Constants.H5_ITER_INC, 0, HDF5Constants.H5P_DEFAULT);
        }
        catch (Throwable err) {
            err.printStackTrace();
            fail("H5.H5Lget_name_by_idx: " + err);
        }
        assertFalse("H5Lget_name_by_idx ",link_name==null);
        assertTrue("Link Name ",link_name.compareTo("DS1")==0);
    }

    @Test
    public void testH5Lget_name_by_idx_n3() throws Throwable, HDF5LibraryException, NullPointerException {
        String link_name = null;
        try {
            link_name = H5.H5Lget_name_by_idx(H5fid, "/", HDF5Constants.H5_INDEX_NAME, HDF5Constants.H5_ITER_INC, 3, HDF5Constants.H5P_DEFAULT);
        }
        catch (Throwable err) {
            err.printStackTrace();
            fail("H5.H5Lget_name_by_idx: " + err);
        }
        assertFalse("H5Lget_name_by_idx ",link_name==null);
        assertTrue("Link Name ",link_name.compareTo("L1")==0);
    }

}
