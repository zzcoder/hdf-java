package test.hdf5lib;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;

import ncsa.hdf.hdf5lib.H5;
import ncsa.hdf.hdf5lib.HDF5Constants;
import ncsa.hdf.hdf5lib.exceptions.HDF5Exception;
import ncsa.hdf.hdf5lib.exceptions.HDF5LibraryException;
import ncsa.hdf.hdf5lib.exceptions.HDF5SymbolTableException;
import ncsa.hdf.hdf5lib.structs.H5L_info_t;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestH5Lcreate {
    private static final boolean is16 = H5.isAPI16;
    private static final String H5_EXTFILE = "test/hdf5lib/h5ex_g_iterate.h5";
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
            catch (Exception e) {
                e.printStackTrace();
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

    private final void _createSoftLink(int fid, String curname, int did, String dstname, int dcpl, int dapl) {
        boolean link_exists = false;
        try {
            H5.H5Lcreate_soft(curname, did, dstname, dcpl, dapl);
            H5.H5Fflush(fid, HDF5Constants.H5F_SCOPE_LOCAL);
            link_exists = H5.H5Lexists(did, dstname, dapl);
        }
        catch (Throwable err) {
            err.printStackTrace();
            fail("H5.H5Lcreate_soft: " + err);
        }
        assertTrue("TestH5L._createSoftLink ", link_exists);
    }

    private final void _createExternalLink(int fid, String ext_filename, String curname, int did, String dstname, int dcpl, int dapl) {
        boolean link_exists = false;
        try {
            H5.H5Lcreate_external(ext_filename, curname, did, dstname, dcpl, dapl);
            H5.H5Fflush(fid, HDF5Constants.H5F_SCOPE_LOCAL);
            link_exists = H5.H5Lexists(did, dstname, dapl);
        }
        catch (Throwable err) {
            err.printStackTrace();
            fail("H5.H5Lcreate_external: " + err);
        }
        assertTrue("TestH5L._createExternalLink ", link_exists);
    }

    @Before
    public void createH5file()
            throws NullPointerException, HDF5Exception {
        assertTrue("H5 open ids is 0",H5.getOpenIDCount()==0);
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
        if (H5did2 > 0) 
            H5.H5Dclose(H5did2);
        if (H5dsid > 0) 
            H5.H5Sclose(H5dsid);
        if (H5did1 > 0) 
            H5.H5Dclose(H5did1);
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

    @Test
    public void testH5Ldelete_hard_link() throws Throwable, HDF5LibraryException, NullPointerException {
        _createHardLink(H5fid, H5fid, "/G1/DS2", H5fid, "L1", HDF5Constants.H5P_DEFAULT, HDF5Constants.H5P_DEFAULT);
        H5.H5Ldelete(H5fid, "L1", HDF5Constants.H5P_DEFAULT);
        H5.H5Fflush(H5fid, HDF5Constants.H5F_SCOPE_LOCAL);
        boolean link_exists = H5.H5Lexists(H5fid, "L1", HDF5Constants.H5P_DEFAULT);
        assertFalse("testH5Lcreate_hard:H5Lexists ",link_exists);
    }

    @Test
    public void testH5Lcreate_soft() throws Throwable, HDF5LibraryException, NullPointerException {
        H5.H5Lcreate_soft("DS1", H5fid, "L1", HDF5Constants.H5P_DEFAULT, HDF5Constants.H5P_DEFAULT);
        H5.H5Fflush(H5fid, HDF5Constants.H5F_SCOPE_LOCAL);
        boolean link_exists = H5.H5Lexists(H5fid, "L1", HDF5Constants.H5P_DEFAULT);
        assertTrue("testH5Lcreate_soft:H5Lexists ",link_exists);
    }

    @Test(expected = HDF5SymbolTableException.class)
    public void testH5Lcreate_soft_dst_link_exists() throws Throwable, HDF5LibraryException, NullPointerException {
        _createSoftLink(H5fid, "/G1/DS2", H5fid, "L1", HDF5Constants.H5P_DEFAULT, HDF5Constants.H5P_DEFAULT);
        H5.H5Lcreate_soft("L1", H5fid, "/G1/DS2", HDF5Constants.H5P_DEFAULT, HDF5Constants.H5P_DEFAULT);
    }

    @Test
    public void testH5Ldelete_soft_link() throws Throwable, HDF5LibraryException, NullPointerException {
        _createSoftLink(H5fid, "/G1/DS2", H5fid, "L1", HDF5Constants.H5P_DEFAULT, HDF5Constants.H5P_DEFAULT);
        H5.H5Ldelete(H5fid, "L1", HDF5Constants.H5P_DEFAULT);
        H5.H5Fflush(H5fid, HDF5Constants.H5F_SCOPE_LOCAL);
        boolean link_exists = H5.H5Lexists(H5fid, "L1", HDF5Constants.H5P_DEFAULT);
        assertFalse("testH5Lcreate_soft:H5Lexists ",link_exists);
    }

    @Test
    public void testH5Lget_info_softlink() throws Throwable, HDF5LibraryException, NullPointerException {
        H5L_info_t link_info = null;
        _createSoftLink(H5fid, "/G1/DS2", H5fid, "L1", HDF5Constants.H5P_DEFAULT, HDF5Constants.H5P_DEFAULT);
        try {
            link_info = H5.H5Lget_info(H5fid, "L1", HDF5Constants.H5P_DEFAULT);
        }
        catch (Throwable err) {
            err.printStackTrace();
            fail("H5.H5Lget_info: " + err);
        }
        assertFalse("H5Lget_info ",link_info==null);
        assertTrue("H5Lget_info link type",link_info.type==HDF5Constants.H5L_TYPE_SOFT);
        assertTrue("Link Address ",link_info.address_val_size>0);
    }

    @Test
    public void testH5Lget_val_soft() throws Throwable, HDF5LibraryException {
        String link_value = null;
        _createSoftLink(H5fid, "/G1/DS2", H5fid, "L1", HDF5Constants.H5P_DEFAULT, HDF5Constants.H5P_DEFAULT);
        try {
            link_value = H5.H5Lget_val(H5fid, "L1", HDF5Constants.H5P_DEFAULT);
        }
        catch (Throwable err) {
            err.printStackTrace();
            fail("H5.H5Lget_val: " + err);
        }
        assertFalse("H5Lget_val ",link_value==null);
        assertTrue("Link Value ",link_value.compareTo("/G1/DS2")==0);
    }

    @Test
    public void testH5Lcreate_soft_dangle() throws Throwable, HDF5LibraryException, NullPointerException {
        H5.H5Lcreate_soft("DS3", H5fid, "L2", HDF5Constants.H5P_DEFAULT, HDF5Constants.H5P_DEFAULT);
        H5.H5Fflush(H5fid, HDF5Constants.H5F_SCOPE_LOCAL);
        boolean link_exists = H5.H5Lexists(H5fid, "L2", HDF5Constants.H5P_DEFAULT);
        assertTrue("testH5Lcreate_soft:H5Lexists ",link_exists);
    }

    @Test
    public void testH5Ldelete_soft_link_dangle() throws Throwable, HDF5LibraryException, NullPointerException {
        _createSoftLink(H5fid, "DS3", H5fid, "L2", HDF5Constants.H5P_DEFAULT, HDF5Constants.H5P_DEFAULT);
        H5.H5Ldelete(H5fid, "L2", HDF5Constants.H5P_DEFAULT);
        H5.H5Fflush(H5fid, HDF5Constants.H5F_SCOPE_LOCAL);
        boolean link_exists = H5.H5Lexists(H5fid, "L2", HDF5Constants.H5P_DEFAULT);
        assertFalse("testH5Lcreate_soft:H5Lexists ",link_exists);
    }

    @Test
    public void testH5Lget_info_softlink_dangle() throws Throwable, HDF5LibraryException, NullPointerException {
        H5L_info_t link_info = null;
        _createSoftLink(H5fid, "DS3", H5fid, "L2", HDF5Constants.H5P_DEFAULT, HDF5Constants.H5P_DEFAULT);
        try {
            link_info = H5.H5Lget_info(H5fid, "L2", HDF5Constants.H5P_DEFAULT);
        }
        catch (Throwable err) {
            err.printStackTrace();
            fail("H5.H5Lget_info: " + err);
        }
        assertFalse("H5Lget_info ",link_info==null);
        assertTrue("H5Lget_info link type",link_info.type==HDF5Constants.H5L_TYPE_SOFT);
        assertTrue("Link Address ",link_info.address_val_size>0);
    }

    @Test
    public void testH5Lget_val_dangle() throws Throwable, HDF5LibraryException {
        String link_value = null;
        _createSoftLink(H5fid, "DS3", H5fid, "L2", HDF5Constants.H5P_DEFAULT, HDF5Constants.H5P_DEFAULT);
        try {
            link_value = H5.H5Lget_val(H5fid, "L2", HDF5Constants.H5P_DEFAULT);
        }
        catch (Throwable err) {
            err.printStackTrace();
            fail("H5.H5Lget_val: " + err);
        }
        assertFalse("H5Lget_val ",link_value==null);
        assertTrue("Link Value ",link_value.compareTo("DS3")==0);
    }

    @Test
    public void testH5Lcreate_external() throws Throwable, HDF5LibraryException, NullPointerException {
        H5.H5Lcreate_external(H5_EXTFILE, "DT1", H5fid, "L1", HDF5Constants.H5P_DEFAULT, HDF5Constants.H5P_DEFAULT);
        H5.H5Fflush(H5fid, HDF5Constants.H5F_SCOPE_LOCAL);
        boolean link_exists = H5.H5Lexists(H5fid, "L1", HDF5Constants.H5P_DEFAULT);
        assertTrue("testH5Lcreate_external:H5Lexists ",link_exists);
    }

    @Test
    public void testH5Lget_info_externallink() throws Throwable, HDF5LibraryException, NullPointerException {
        H5L_info_t link_info = null;
        _createExternalLink(H5fid, H5_EXTFILE, "DT1", H5fid, "L1", HDF5Constants.H5P_DEFAULT, HDF5Constants.H5P_DEFAULT);
        try {
            link_info = H5.H5Lget_info(H5fid, "L1", HDF5Constants.H5P_DEFAULT);
        }
        catch (Throwable err) {
            err.printStackTrace();
            fail("H5.H5Lget_info: " + err);
        }
        assertFalse("H5Lget_info ",link_info==null);
        assertTrue("H5Lget_info link type",link_info.type==HDF5Constants.H5L_TYPE_EXTERNAL);
        assertTrue("Link Address ",link_info.address_val_size>0);
    }

    @Test
    public void testH5Lget_val_external() throws Throwable, HDF5LibraryException {
        String link_value = null;
        _createExternalLink(H5fid, H5_EXTFILE, "DT1", H5fid, "L1", HDF5Constants.H5P_DEFAULT, HDF5Constants.H5P_DEFAULT);
        try {
            link_value = H5.H5Lget_val(H5fid, "L1", HDF5Constants.H5P_DEFAULT);
        }
        catch (Throwable err) {
            err.printStackTrace();
            fail("H5.H5Lget_val: " + err);
        }
        assertFalse("H5Lget_val ",link_value==null);
        assertTrue("Link Value ",link_value.compareTo("DT1")==0);
    }

    @Test
    public void testH5Lget_val_external_filename() throws Throwable, HDF5LibraryException {
        String link_file = null;
        _createExternalLink(H5fid, H5_EXTFILE, "DT1", H5fid, "L1", HDF5Constants.H5P_DEFAULT, HDF5Constants.H5P_DEFAULT);
        try {
            link_file = H5.H5Lget_val_external(H5fid, "L1", HDF5Constants.H5P_DEFAULT);
        }
        catch (Throwable err) {
            err.printStackTrace();
            fail("H5.H5Lget_val_external: " + err);
        }
        assertFalse("H5Lget_val_external ",link_file==null);
        assertTrue("Link Filename ",link_file.compareTo(H5_EXTFILE)==0);
    }

}
