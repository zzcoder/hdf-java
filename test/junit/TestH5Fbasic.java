package test.junit;

import java.io.File;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import ncsa.hdf.hdf5lib.H5;
import ncsa.hdf.hdf5lib.HDF5Constants;
import ncsa.hdf.hdf5lib.exceptions.HDF5LibraryException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestH5Fbasic {
    private static final String H5_FILE = "test.h5";
    private static final String TXT_FILE = "test.txt";
    int H5fid = -1;

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
            throws HDF5LibraryException, NullPointerException {
        H5fid = H5.H5Fcreate(H5_FILE, HDF5Constants.H5F_ACC_TRUNC,
                HDF5Constants.H5P_DEFAULT, HDF5Constants.H5P_DEFAULT);
        H5.H5Fflush(H5fid, HDF5Constants.H5F_SCOPE_LOCAL);
    }

    @After
    public void deleteH5file() throws HDF5LibraryException {
        if (H5fid > 0) {
            H5.H5Fclose(H5fid);
        }
        _deleteFile(H5_FILE);
    }

    @Test
    public void testH5Fcreate() {
        assertTrue(H5fid > 0);
    }

    @Test
    public void testH5Fis_hdf5() {
        boolean isH5 = false;

        try {
            isH5 = H5.H5Fis_hdf5(H5_FILE);
        }
        catch (Throwable err) {
            fail("H5.H5Fis_hdf5 failed on " + H5_FILE + ": " + err);
        }
        assertTrue(isH5 == true);
    }

    @Test(expected = HDF5LibraryException.class)
    public void testH5Fcreate_EXCL() throws HDF5LibraryException, Throwable {
        H5.H5Fcreate(H5_FILE, HDF5Constants.H5F_ACC_EXCL,
                HDF5Constants.H5P_DEFAULT, HDF5Constants.H5P_DEFAULT);
    }

    @Test(expected = HDF5LibraryException.class)
    public void testH5Fopen_read_only() throws HDF5LibraryException, Throwable {
        int fid = -1;

        try {
            fid = H5.H5Fopen(H5_FILE, HDF5Constants.H5F_ACC_RDWR,
                    HDF5Constants.H5P_DEFAULT);
        }
        catch (Throwable err) {
            fail("H5.H5Fopen: " + err);
        }
        try {
            H5.H5Fclose(fid);
        }
        catch (Exception ex) {
        }

        // set the file to read-only
        File file = new File(H5_FILE);
        if (file.setWritable(false)) {
            // this should fail.
            fid = H5.H5Fopen(H5_FILE, HDF5Constants.H5F_ACC_RDWR,
                    HDF5Constants.H5P_DEFAULT);

            try {
                H5.H5Fclose(fid);
            }
            catch (Exception ex) {
            }
        }
        else {
            fail("File.setWritable(true) failed.");
        }
    }

    @Test(expected = HDF5LibraryException.class)
    public void testH5Freopen_closed() throws HDF5LibraryException, Throwable {
        int fid = -1;
        int fid2 = -1;

        try {
            fid = H5.H5Fopen(H5_FILE, HDF5Constants.H5F_ACC_RDWR,
                    HDF5Constants.H5P_DEFAULT);
        }
        catch (Throwable err) {
            fail("H5.H5Fopen: " + err);
        }

        try {
            H5.H5Fclose(fid);
        }
        catch (Exception ex) {
        }

        // should fail because the file was closed.
        fid2 = H5.H5Freopen(fid);
    }

    @Test
    public void testH5Freopen() throws HDF5LibraryException, Throwable {
        int fid = -1;
        int fid2 = -1;

        try {
            fid = H5.H5Fopen(H5_FILE, HDF5Constants.H5F_ACC_RDWR,
                    HDF5Constants.H5P_DEFAULT);
        }
        catch (Throwable err) {
            fail("H5.H5Fopen: " + err);
        }

        try {
            fid2 = H5.H5Freopen(fid);
        }
        catch (Throwable err) {
            fail("H5.H5Freopen: " + err);
        }
        assertTrue(fid2 > 0);

        try {
            H5.H5Fclose(fid2);
        }
        catch (Exception ex) {
        }

        try {
            H5.H5Fclose(fid);
        }
        catch (Exception ex) {
        }
    }

    @Test
    public void testH5Fclose() {
        int fid = -1;

        try {
            fid = H5.H5Fopen(H5_FILE, HDF5Constants.H5F_ACC_RDWR,
                    HDF5Constants.H5P_DEFAULT);
        }
        catch (Throwable err) {
            fail("H5.H5Fopen: " + err);
        }

        try {
            H5.H5Fclose(fid);
        }
        catch (Throwable err) {
            fail("H5.H5Fclose: " + err);
        }
    }

    @Test(expected = HDF5LibraryException.class)
    public void testH5Fclose_twice() throws HDF5LibraryException, Throwable {
        int fid = -1;

        try {
            fid = H5.H5Fopen(H5_FILE, HDF5Constants.H5F_ACC_RDWR,
                    HDF5Constants.H5P_DEFAULT);
        }
        catch (Throwable err) {
            fail("H5.H5Fopen: " + err);
        }

        try {
            H5.H5Fclose(fid);
        }
        catch (Throwable err) {
            fail("H5.H5Fclose: " + err);
        }

        // it should fail since the file was closed.
        H5.H5Fclose(fid);
    }

    @Test
    public void testH5Fget_freespace() {
        long freeSpace = 0;

        try {
            freeSpace = H5.H5Fget_freespace(H5fid);
        }
        catch (Throwable err) {
            fail("H5.H5Fget_freespace: " + err);
        }
        assertEquals(freeSpace, 0);
    }

    // TODO add/and delete objects and test freespace

    @Test
    public void testH5Fget_filesize() {
        long fileSize = 0;

        try {
            fileSize = H5.H5Fget_filesize(H5fid);
        }
        catch (Throwable err) {
            fail("H5.H5Fget_freespace: " + err);
        }
        assertTrue(fileSize > 0);
    }

    // TODO add/and delete objects and test freespace

    @Test
    public void testH5Fget_mdc_hit_rate() {
        double rate;

        try {
            rate = H5.H5Fget_mdc_hit_rate(H5fid);
        }
        catch (Throwable err) {
            fail("H5.H5Fget_mdc_hit_rate: " + err);
        }
    }

    @Test
    public void testH5Fget_mdc_size() {
        int nentries = -1;
        long cache_sizes[] = new long[3];

        try {
            nentries = H5.H5Fget_mdc_size(H5fid, cache_sizes);
        }
        catch (Throwable err) {
            fail("H5.H5Fget_mdc_size: " + err);
        }
        assertTrue("H5.H5Fget_mdc_size #:" + nentries, nentries == 4);
    }

    // TODO: test more cases of different cache sizes.

    @Test
    public void testH5Freset_mdc_hit_rate_stats() {

        try {
            H5.H5Freset_mdc_hit_rate_stats(H5fid);
        }
        catch (Throwable err) {
            fail("H5.H5Freset_mdc_hit_rate_stats: " + err);
        }
    }

    @Test
    public void testH5Fget_name() {
        String fname = null;

        try {
            fname = H5.H5Fget_name(H5fid);
        }
        catch (Throwable err) {
            fail("H5.H5Fget_name: " + err);
        }
        assertNotNull(fname);
        assertEquals(fname, H5_FILE);
    }
}