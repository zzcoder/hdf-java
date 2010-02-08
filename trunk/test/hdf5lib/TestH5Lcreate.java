package test.hdf5lib;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;

import ncsa.hdf.hdf5lib.H5;
import ncsa.hdf.hdf5lib.HDF5Constants;
import ncsa.hdf.hdf5lib.exceptions.HDF5Exception;
import ncsa.hdf.hdf5lib.exceptions.HDF5LibraryException;
import ncsa.hdf.hdf5lib.exceptions.HDF5MetaDataCacheException;
import ncsa.hdf.hdf5lib.exceptions.HDF5SymbolTableException;
import ncsa.hdf.hdf5lib.structs.H5L_info_t;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestH5Lcreate {
    private static final boolean is16 = H5.isAPI16;
    private static final String H5_FILE = "test.h5";
    private static final int DIM_X = 4;
    private static final int DIM_Y = 6;
    int H5fid = -1;
    int H5dsid = -1;
    int H5did1 = -1;
    int H5did2 = -1;
    int H5gid = -1;
    long[] H5dims = { DIM_X, DIM_Y };

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

    private final int _createDataset(int fid, int dsid, String name, int dapl) {
        int did = -1;
        try {
            did = H5.H5Dcreate(fid, name,
                        HDF5Constants.H5T_STD_I32BE, dsid,
                        HDF5Constants.H5P_DEFAULT, HDF5Constants.H5P_DEFAULT, dapl);
        }
        catch (Throwable err) {
            err.printStackTrace();
            fail("H5.H5Dcreate: " + err);
        }
        assertTrue("TestH5L._createDataset: ",did > 0);

        return did;
    }

    private final int _createGroup(int fid, String name) {
        int gid = -1;
        try {
            if (is16)
                gid = H5.H5Gcreate(fid, name, 0);
            else
                gid = H5.H5Gcreate2(fid, name, HDF5Constants.H5P_DEFAULT,
                        HDF5Constants.H5P_DEFAULT, HDF5Constants.H5P_DEFAULT);
        }
        catch (Throwable err) {
            err.printStackTrace();
            fail("H5.H5Gcreate: " + err);
        }
        assertTrue("TestH5L._createGroup: ",gid > 0);

        return gid;
    }

    private final void _createHardLink(int fid, int cid, String curname, int did, String dstname, int dcpl, int dapl) {
        boolean link_exists = false;
        try {
            H5.H5Lcreate_hard(cid, curname, did, dstname, dcpl, dapl);
            H5.H5Fflush(fid, HDF5Constants.H5F_SCOPE_LOCAL);
            link_exists = H5.H5Lexists(did, dstname, dapl);
        }
        catch (Throwable err) {
            err.printStackTrace();
            fail("H5.H5Lcreate_hard: " + err);
        }
        assertTrue("TestH5L._createHardLink ", link_exists);
    }

    @Before
    public void createH5file()
            throws NullPointerException, HDF5Exception {
        try {
            H5fid = H5.H5Fcreate(H5_FILE, HDF5Constants.H5F_ACC_TRUNC,
                    HDF5Constants.H5P_DEFAULT, HDF5Constants.H5P_DEFAULT);
            H5dsid = H5.H5Screate_simple(2, H5dims, null);
            H5did1 = _createDataset(H5fid, H5dsid, "DS1", HDF5Constants.H5P_DEFAULT);
            H5gid = _createGroup(H5fid, "/G1");
            H5did2 = _createDataset(H5gid, H5dsid, "DS2", HDF5Constants.H5P_DEFAULT);
        }
        catch (Throwable err) {
            err.printStackTrace();
            fail("TestH5L.createH5file: " + err);
        }
        assertTrue("TestH5L.createH5file: H5.H5Fcreate: ",H5fid > 0);
        assertTrue("TestH5L.createH5file: H5.H5Screate_simple: ",H5dsid > 0);
        assertTrue("TestH5L.createH5file: H5.H5Gcreate: ",H5gid > 0);

        H5.H5Fflush(H5fid, HDF5Constants.H5F_SCOPE_LOCAL);
    }

    @After
    public void deleteH5file() throws HDF5LibraryException {
        if (H5gid > 0) 
            H5.H5Gclose(H5gid);
        if (H5dsid > 0) 
            H5.H5Sclose(H5dsid);
        if (H5fid > 0) 
            H5.H5Fclose(H5fid);

        _deleteFile(H5_FILE);
    }

    @Test(expected = HDF5SymbolTableException.class)
    public void testH5Lcreate_hard_cur_not_exists() throws Throwable, HDF5LibraryException, NullPointerException {
        H5.H5Lcreate_hard(H5fid, "None", H5fid, "DS1", HDF5Constants.H5P_DEFAULT, HDF5Constants.H5P_DEFAULT);
    }

    @Test
    public void testH5Lcreate_hard() throws Throwable, HDF5LibraryException, NullPointerException {
        H5.H5Lcreate_hard(H5fid, "DS1", H5fid, "L1", HDF5Constants.H5P_DEFAULT, HDF5Constants.H5P_DEFAULT);
        H5.H5Fflush(H5fid, HDF5Constants.H5F_SCOPE_LOCAL);
        boolean link_exists = H5.H5Lexists(H5fid, "L1", HDF5Constants.H5P_DEFAULT);
        assertTrue("testH5Lcreate_hard:H5Lexists ",link_exists);
    }

    @Test(expected = HDF5SymbolTableException.class)
    public void testH5Lcreate_hard_dst_link_exists() throws Throwable, HDF5LibraryException, NullPointerException {
        _createHardLink(H5fid, H5fid, "/G1/DS2", H5fid, "L1", HDF5Constants.H5P_DEFAULT, HDF5Constants.H5P_DEFAULT);
        H5.H5Lcreate_hard(H5fid, "L1", H5fid, "/G1/DS2", HDF5Constants.H5P_DEFAULT, HDF5Constants.H5P_DEFAULT);
    }

}
