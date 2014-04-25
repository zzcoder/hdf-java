package test.hdf5lib;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;

import ncsa.hdf.hdf5lib.H5;
import ncsa.hdf.hdf5lib.HDF5Constants;

import org.junit.Before;
import org.junit.Test;

public class TestH5Fparams {

    @Before
    public void checkOpenIDs() {
        assertTrue("H5 open ids is 0",H5.getOpenIDCount()==0);
    }

    @Test(expected = NullPointerException.class)
    public void testH5Fcreate_null() throws Throwable {
        H5.H5Fcreate(null, HDF5Constants.H5F_ACC_TRUNC,
                HDF5Constants.H5P_DEFAULT, HDF5Constants.H5P_DEFAULT);
    }

    @Test(expected = NullPointerException.class)
    public void testH5Fopen_null() throws Throwable {
        H5.H5Fopen(null, HDF5Constants.H5F_ACC_RDWR, HDF5Constants.H5P_DEFAULT);
    }

    @Test(expected = NullPointerException.class)
    public void testH5Fis_hdf5_null() throws Throwable {
        H5.H5Fis_hdf5(null);
    }

    @Test(expected = NullPointerException.class)
    public void testH5Fmount_null() throws Throwable {
        H5.H5Fmount(-1, null, -1, HDF5Constants.H5P_DEFAULT);
    }

    @Test(expected = NullPointerException.class)
    public void testH5Funmount_null() throws Throwable {
        H5.H5Funmount(-1, null);
    }

    @Test
    public void testH5Fis_hdf5_text() {
        File txtFile = null;
        boolean isH5 = false;

        try {
            txtFile = new File("test.txt");
            if (!txtFile.exists())
                txtFile.createNewFile();
            isH5 = H5.H5Fis_hdf5("test.txt");
        }
        catch (Throwable err) {
            fail("H5.H5Fis_hdf5 failed on test.txt: " + err);
        }

        assertFalse(isH5);

        try {
            txtFile.delete();
        }
        catch (SecurityException e) {
            ;// e.printStackTrace();
        }
    }

    @Test//(expected = HDF5LibraryException.class)
    public void testH5Fclose_negative() throws Throwable {
        // cannot close a file with negative id.
        int fid = H5.H5Fclose(-1);
        assertTrue(fid == 0);
    }

    @Test
    public void testH5Fcreate() {
        int fid = -1;
        File file = null;

        try {
            fid = H5.H5Fcreate("test.h5", HDF5Constants.H5F_ACC_TRUNC,
                HDF5Constants.H5P_DEFAULT, HDF5Constants.H5P_DEFAULT);
            if (fid > 0) {
                H5.H5Fclose(fid);
            }
            file = new File("test.h5");
        }
        catch (Throwable err) {
            fail("H5.H5Fopen: " + err);
        }

        if (file.exists()) {
            try {
                file.delete();
            }
            catch (SecurityException e) {
                ;// e.printStackTrace();
            }
        }
    }

    @Test
    public void testH5Fflush_global() {
        int fid = -1;

        try {
            fid = H5.H5Fcreate("test.h5", HDF5Constants.H5F_ACC_TRUNC,
                    HDF5Constants.H5P_DEFAULT, HDF5Constants.H5P_DEFAULT);
        }
        catch (Throwable err) {
            fail("H5.H5Fopen: " + err);
        }

        try {
            H5.H5Fflush(fid, HDF5Constants.H5F_SCOPE_GLOBAL);
        }
        catch (Throwable err) {
            fail("H5.H5Fflush: " + err);
        }

        try {
            H5.H5Fclose(fid);
        }
        catch (Exception ex) {
        }
    }

    @Test
    public void testH5Fflush_local() {
        int fid = -1;

        try {
            fid = H5.H5Fcreate("test.h5", HDF5Constants.H5F_ACC_TRUNC,
                    HDF5Constants.H5P_DEFAULT, HDF5Constants.H5P_DEFAULT);
        }
        catch (Throwable err) {
            fail("H5.H5Fopen: " + err);
        }

        try {
            H5.H5Fflush(fid, HDF5Constants.H5F_SCOPE_LOCAL);
        }
        catch (Throwable err) {
            fail("H5.H5Fflush: " + err);
        }

        try {
            H5.H5Fclose(fid);
        }
        catch (Exception ex) {
        }
    }
}
