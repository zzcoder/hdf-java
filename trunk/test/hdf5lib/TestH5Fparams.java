package test.hdf5lib;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;

import ncsa.hdf.hdf5lib.H5;
import ncsa.hdf.hdf5lib.HDF5Constants;
import ncsa.hdf.hdf5lib.exceptions.HDF5LibraryException;

import org.junit.Test;

public class TestH5Fparams {
    @Test(expected = NullPointerException.class)
    public void testH5Fcreate_null() throws Throwable, NullPointerException {
        H5.H5Fcreate(null, HDF5Constants.H5F_ACC_TRUNC,
                HDF5Constants.H5P_DEFAULT, HDF5Constants.H5P_DEFAULT);
    }

    @Test(expected = NullPointerException.class)
    public void testH5Fopen_null() throws Throwable, NullPointerException {
        H5.H5Fopen(null, HDF5Constants.H5F_ACC_RDWR, HDF5Constants.H5P_DEFAULT);
    }

    @Test(expected = NullPointerException.class)
    public void testH5Fis_hdf5_null()
            throws HDF5LibraryException, Throwable, NullPointerException {
        H5.H5Fis_hdf5(null);
    }

    @Test(expected = NullPointerException.class)
    public void testH5Fmount_null()
            throws HDF5LibraryException, Throwable, NullPointerException {
        H5.H5Fmount(-1, null, -1, HDF5Constants.H5P_DEFAULT);
    }

    @Test(expected = NullPointerException.class)
    public void testH5Funmount_null()
            throws HDF5LibraryException, Throwable, NullPointerException {
        H5.H5Funmount(-1, null);
    }

    @Test
    public void testH5Fis_hdf5_text() throws IOException, SecurityException {

        File txtFile = new File("test.txt");

        if (!txtFile.exists())
            txtFile.createNewFile();

        boolean isH5 = false;

        try {
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

    @Test(expected = HDF5LibraryException.class)
    public void testH5Fclose_negative() throws HDF5LibraryException, Throwable {
        // cannot close a file with negative id.
        H5.H5Fclose(-1);
    }

    @Test
    public void testH5Fcreate()
            throws HDF5LibraryException, NullPointerException {
        int fid = H5.H5Fcreate("test.h5", HDF5Constants.H5F_ACC_TRUNC,
                HDF5Constants.H5P_DEFAULT, HDF5Constants.H5P_DEFAULT);
        if (fid > 0) {
            H5.H5Fclose(fid);
        }
        File file = new File("test.h5");

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
