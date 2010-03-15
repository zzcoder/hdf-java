package test.hdf5lib;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.util.ArrayList;

import ncsa.hdf.hdf5lib.H5;
import ncsa.hdf.hdf5lib.HDF5Constants;
import ncsa.hdf.hdf5lib.callbacks.H5L_iterate_cb;
import ncsa.hdf.hdf5lib.callbacks.H5L_iterate_t;
import ncsa.hdf.hdf5lib.exceptions.HDF5Exception;
import ncsa.hdf.hdf5lib.exceptions.HDF5LibraryException;
import ncsa.hdf.hdf5lib.structs.H5L_info_t;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestH5Lcreate {
    private static final String H5_EXTFILE = "test/hdf5lib/h5ex_g_iterate.hdf";
    private static final String H5_FILE = "test.h5";
    private static final int DIM_X = 4;
    private static final int DIM_Y = 6;
    int H5fcpl = -1;
    int H5fid = -1;
    int H5dsid = -1;
    int H5did1 = -1;
    int H5did2 = -1;
    int H5gcpl = -1;
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
//            H5gcpl = H5.H5Pcreate(HDF5Constants.H5P_GROUP_CREATE);
//            H5.H5Pset_link_creation_order(H5gcpl, HDF5Constants.H5P_CRT_ORDER_TRACKED+HDF5Constants.H5P_CRT_ORDER_INDEXED);
            H5gcpl = HDF5Constants.H5P_DEFAULT;
            gid = H5.H5Gcreate2(fid, name, HDF5Constants.H5P_DEFAULT,
                    H5gcpl, HDF5Constants.H5P_DEFAULT);
        }
        catch (Throwable err) {
            err.printStackTrace();
            fail("H5.H5Gcreate: " + err);
        }
        assertTrue("TestH5L._createGroup: ",gid > 0);

        return gid;
    }

    private final void _createHardLink(int fid, int cid, String curname, int did, String dstname, int lcpl, int lapl) {
        boolean link_exists = false;
        try {
            H5.H5Lcreate_hard(cid, curname, did, dstname, lcpl, lapl);
            H5.H5Fflush(fid, HDF5Constants.H5F_SCOPE_LOCAL);
            link_exists = H5.H5Lexists(did, dstname, lapl);
        }
        catch (Throwable err) {
            err.printStackTrace();
            fail("H5.H5Lcreate_hard: " + err);
        }
        assertTrue("TestH5L._createHardLink ", link_exists);
    }

    private final void _createSoftLink(int fid, String curname, int did, String dstname, int lcpl, int lapl) {
        boolean link_exists = false;
        try {
            H5.H5Lcreate_soft(curname, did, dstname, lcpl, lapl);
            H5.H5Fflush(fid, HDF5Constants.H5F_SCOPE_LOCAL);
            link_exists = H5.H5Lexists(did, dstname, lapl);
        }
        catch (Throwable err) {
            err.printStackTrace();
            fail("H5.H5Lcreate_soft: " + err);
        }
        assertTrue("TestH5L._createSoftLink ", link_exists);
    }

    private final void _createExternalLink(int fid, String ext_filename, String curname, int did, String dstname, int lcpl, int lapl) {
        boolean link_exists = false;
        try {
            H5.H5Lcreate_external(ext_filename, curname, did, dstname, lcpl, lapl);
            H5.H5Fflush(fid, HDF5Constants.H5F_SCOPE_LOCAL);
            link_exists = H5.H5Lexists(did, dstname, lapl);
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
            H5fcpl = H5.H5Pcreate(HDF5Constants.H5P_FILE_CREATE);
            H5.H5Pset_link_creation_order(H5fcpl, HDF5Constants.H5P_CRT_ORDER_TRACKED+HDF5Constants.H5P_CRT_ORDER_INDEXED);
            H5fid = H5.H5Fcreate(H5_FILE, HDF5Constants.H5F_ACC_TRUNC,
                    H5fcpl, HDF5Constants.H5P_DEFAULT);
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
        if (H5gcpl > 0) 
            H5.H5Pclose(H5gcpl);
        if (H5did2 > 0) 
            H5.H5Dclose(H5did2);
        if (H5dsid > 0) 
            H5.H5Sclose(H5dsid);
        if (H5did1 > 0) 
            H5.H5Dclose(H5did1);
        if (H5fid > 0) 
            H5.H5Fclose(H5fid);
        if (H5fcpl > 0) 
            H5.H5Pclose(H5fcpl);

        _deleteFile(H5_FILE);
    }

    @Test
    public void testH5Lget_info_by_idx_n0_create() throws Throwable, HDF5LibraryException, NullPointerException {
        H5L_info_t link_info = null;
        int order = H5.H5Pget_link_creation_order(H5fcpl);
        assertTrue("creation order :"+order, order == HDF5Constants.H5P_CRT_ORDER_TRACKED+HDF5Constants.H5P_CRT_ORDER_INDEXED);
        try {
            link_info = H5.H5Lget_info_by_idx(H5fid, "/", HDF5Constants.H5_INDEX_CRT_ORDER, HDF5Constants.H5_ITER_INC, 0, HDF5Constants.H5P_DEFAULT);
        }
        catch (Throwable err) {
            err.printStackTrace();
            fail("H5.H5Lget_info_by_idx: " + err);
        }
        assertFalse("H5Lget_info_by_idx ",link_info==null);
        assertTrue("H5Lget_info_by_idx link type",link_info.type==HDF5Constants.H5L_TYPE_HARD);
    }

    @Test
    public void testH5Lget_info_by_idx_n1_create() throws Throwable, HDF5LibraryException, NullPointerException {
        H5L_info_t link_info = null;
        int order = H5.H5Pget_link_creation_order(H5fcpl);
        assertTrue("creation order :"+order, order == HDF5Constants.H5P_CRT_ORDER_TRACKED+HDF5Constants.H5P_CRT_ORDER_INDEXED);
        try {
            link_info = H5.H5Lget_info_by_idx(H5fid, "/", HDF5Constants.H5_INDEX_CRT_ORDER, HDF5Constants.H5_ITER_INC, 1, HDF5Constants.H5P_DEFAULT);
        }
        catch (Throwable err) {
            err.printStackTrace();
            fail("H5.H5Lget_info_by_idx: " + err);
        }
        assertFalse("H5Lget_info_by_idx ",link_info==null);
        assertTrue("H5Lget_info_by_idx link type",link_info.type==HDF5Constants.H5L_TYPE_HARD);
    }

    @Test(expected = HDF5LibraryException.class)
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

    @Test(expected = HDF5LibraryException.class)
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

    @Test(expected = HDF5LibraryException.class)
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

    @Test(expected = HDF5LibraryException.class)
    public void testH5Lcopy_cur_not_exists() throws Throwable, HDF5LibraryException, NullPointerException {
        H5.H5Lcopy(H5fid, "None", H5fid, "DS1", HDF5Constants.H5P_DEFAULT, HDF5Constants.H5P_DEFAULT);
    }

    @Test
    public void testH5Lcopy() throws Throwable, HDF5LibraryException, NullPointerException {
        H5.H5Lcopy(H5fid, "DS1", H5fid, "CPY1", HDF5Constants.H5P_DEFAULT, HDF5Constants.H5P_DEFAULT);
        H5.H5Fflush(H5fid, HDF5Constants.H5F_SCOPE_LOCAL);
        boolean link_exists = H5.H5Lexists(H5fid, "CPY1", HDF5Constants.H5P_DEFAULT);
        assertTrue("testH5Lcopy:H5Lexists ",link_exists);
    }

    @Test(expected = HDF5LibraryException.class)
    public void testH5Lcopy_dst_link_exists() throws Throwable, HDF5LibraryException, NullPointerException {
        _createHardLink(H5fid, H5fid, "/G1/DS2", H5fid, "CPY1", HDF5Constants.H5P_DEFAULT, HDF5Constants.H5P_DEFAULT);
        H5.H5Lcopy(H5fid, "CPY1", H5fid, "/G1/DS2", HDF5Constants.H5P_DEFAULT, HDF5Constants.H5P_DEFAULT);
    }

    @Test(expected = HDF5LibraryException.class)
    public void testH5Lmove_cur_not_exists() throws Throwable, HDF5LibraryException, NullPointerException {
        H5.H5Lmove(H5fid, "None", H5fid, "DS1", HDF5Constants.H5P_DEFAULT, HDF5Constants.H5P_DEFAULT);
    }

    @Test
    public void testH5Lmove() throws Throwable, HDF5LibraryException, NullPointerException {
        H5.H5Lmove(H5fid, "DS1", H5fid, "CPY1", HDF5Constants.H5P_DEFAULT, HDF5Constants.H5P_DEFAULT);
        H5.H5Fflush(H5fid, HDF5Constants.H5F_SCOPE_LOCAL);
        boolean link_exists = H5.H5Lexists(H5fid, "CPY1", HDF5Constants.H5P_DEFAULT);
        assertTrue("testH5Lcopy:H5Lexists ",link_exists);
        link_exists = H5.H5Lexists(H5fid, "DS1", HDF5Constants.H5P_DEFAULT);
        assertFalse("testH5Lcopy:H5Lexists ",link_exists);
    }

    @Test(expected = HDF5LibraryException.class)
    public void testH5Lmove_dst_link_exists() throws Throwable, HDF5LibraryException, NullPointerException {
        _createHardLink(H5fid, H5fid, "/G1/DS2", H5fid, "CPY1", HDF5Constants.H5P_DEFAULT, HDF5Constants.H5P_DEFAULT);
        H5.H5Lmove(H5fid, "CPY1", H5fid, "/G1/DS2", HDF5Constants.H5P_DEFAULT, HDF5Constants.H5P_DEFAULT);
    }

    @Test(expected = HDF5LibraryException.class)
    public void testH5Lget_val_by_idx_not_exist_name() throws Throwable, HDF5LibraryException, NullPointerException {
        H5.H5Lget_val_by_idx(H5fid, "None", HDF5Constants.H5_INDEX_CRT_ORDER, HDF5Constants.H5_ITER_INC, 0, HDF5Constants.H5P_DEFAULT);
    }

    @Test(expected = HDF5LibraryException.class)
    public void testH5Lget_val_by_idx_not_exist_create() throws Throwable, HDF5LibraryException, NullPointerException {
        H5.H5Lget_val_by_idx(H5fid, "None", HDF5Constants.H5_INDEX_NAME, HDF5Constants.H5_ITER_INC, 0, HDF5Constants.H5P_DEFAULT);
    }

    @Test
    public void testH5Lget_val_by_idx_n2_name() throws Throwable, HDF5LibraryException, NullPointerException {
        H5L_info_t link_info = null;
        String link_value = null;
        _createSoftLink(H5fid, "/G1/DS2", H5fid, "LS", HDF5Constants.H5P_DEFAULT, HDF5Constants.H5P_DEFAULT);
        try {
            link_info = H5.H5Lget_info_by_idx(H5fid, "/", HDF5Constants.H5_INDEX_NAME, HDF5Constants.H5_ITER_INC, 2, HDF5Constants.H5P_DEFAULT);
        }
        catch (Throwable err) {
            err.printStackTrace();
            fail("H5.H5Lget_info_by_idx: " + err);
        }
        assertFalse("testH5Lget_val_by_idx_n2 ",link_info==null);
        assertTrue("testH5Lget_val_by_idx_n2 link type",link_info.type==HDF5Constants.H5L_TYPE_SOFT);
        try {
            link_value = H5.H5Lget_val_by_idx(H5fid, "/", HDF5Constants.H5_INDEX_NAME, HDF5Constants.H5_ITER_INC, 2, HDF5Constants.H5P_DEFAULT);
        }
        catch (Throwable err) {
            err.printStackTrace();
            fail("H5.H5Lget_val_by_idx: " + err);
        }
        assertFalse("testH5Lget_val_by_idx_n2 ",link_value==null);
        assertTrue("testH5Lget_val_by_idx_n2 Link Value ", link_value.compareTo("/G1/DS2")==0);
    }

    @Test
    public void testH5Lget_val_by_idx_n2_create() throws Throwable, HDF5LibraryException, NullPointerException {
        H5L_info_t link_info = null;
        String link_value = null;
        int order = H5.H5Pget_link_creation_order(H5fcpl);
        assertTrue("creation order :"+order, order == HDF5Constants.H5P_CRT_ORDER_TRACKED+HDF5Constants.H5P_CRT_ORDER_INDEXED);
        _createSoftLink(H5fid, "/G1/DS2", H5fid, "LS", HDF5Constants.H5P_DEFAULT, HDF5Constants.H5P_DEFAULT);
        try {
            link_info = H5.H5Lget_info_by_idx(H5fid, "/", HDF5Constants.H5_INDEX_CRT_ORDER, HDF5Constants.H5_ITER_INC, 2, HDF5Constants.H5P_DEFAULT);
        }
        catch (Throwable err) {
            err.printStackTrace();
            fail("H5.H5Lget_info_by_idx: " + err);
        }
        assertFalse("testH5Lget_val_by_idx_n2 ",link_info==null);
        assertTrue("testH5Lget_val_by_idx_n2 link type",link_info.type==HDF5Constants.H5L_TYPE_SOFT);
        try {
            link_value = H5.H5Lget_val_by_idx(H5fid, "/", HDF5Constants.H5_INDEX_CRT_ORDER, HDF5Constants.H5_ITER_INC, 2, HDF5Constants.H5P_DEFAULT);
        }
        catch (Throwable err) {
            err.printStackTrace();
            fail("H5.H5Lget_val_by_idx: " + err);
        }
        assertFalse("testH5Lget_val_by_idx_n2 ",link_value==null);
        assertTrue("testH5Lget_val_by_idx_n2 Link Value ", link_value.compareTo("/G1/DS2")==0);
    }

    @Test
    public void testH5Lget_val_by_idx_external_name() throws Throwable, HDF5LibraryException, NullPointerException {
        H5L_info_t link_info = null;
        String link_value = null;
        _createExternalLink(H5fid, H5_EXTFILE, "DT1", H5fid, "LE", HDF5Constants.H5P_DEFAULT, HDF5Constants.H5P_DEFAULT);
        try {
            link_info = H5.H5Lget_info_by_idx(H5fid, "/", HDF5Constants.H5_INDEX_NAME, HDF5Constants.H5_ITER_INC, 2, HDF5Constants.H5P_DEFAULT);
        }
        catch (Throwable err) {
            err.printStackTrace();
            fail("H5.H5Lget_info_by_idx: " + err);
        }
        assertFalse("testH5Lget_val_by_idx_ext ",link_info==null);
        assertTrue("testH5Lget_val_by_idx_ext link type "+link_info.type,link_info.type==HDF5Constants.H5L_TYPE_EXTERNAL);
        try {
            link_value = H5.H5Lget_val_by_idx(H5fid, "/", HDF5Constants.H5_INDEX_NAME, HDF5Constants.H5_ITER_INC, 2, HDF5Constants.H5P_DEFAULT);
        }
        catch (Throwable err) {
            err.printStackTrace();
            fail("H5.H5Lget_val_by_idx: " + err);
        }
        assertFalse("testH5Lget_val_by_idx_ext ",link_value==null);
        assertTrue("testH5Lget_val_by_idx_ext Link Value ",link_value.compareTo("DT1")==0);
    }

    @Test
    public void testH5Lget_val_by_idx_external_create() throws Throwable, HDF5LibraryException, NullPointerException {
        H5L_info_t link_info = null;
        String link_value = null;
        _createExternalLink(H5fid, H5_EXTFILE, "DT1", H5fid, "LE", HDF5Constants.H5P_DEFAULT, HDF5Constants.H5P_DEFAULT);
        try {
            link_info = H5.H5Lget_info_by_idx(H5fid, "/", HDF5Constants.H5_INDEX_CRT_ORDER, HDF5Constants.H5_ITER_INC, 2, HDF5Constants.H5P_DEFAULT);
        }
        catch (Throwable err) {
            err.printStackTrace();
            fail("H5.H5Lget_info_by_idx: " + err);
        }
        assertFalse("testH5Lget_val_by_idx_ext ",link_info==null);
        assertTrue("testH5Lget_val_by_idx_ext link type "+link_info.type,link_info.type==HDF5Constants.H5L_TYPE_EXTERNAL);
        try {
            link_value = H5.H5Lget_val_by_idx(H5fid, "/", HDF5Constants.H5_INDEX_CRT_ORDER, HDF5Constants.H5_ITER_INC, 2, HDF5Constants.H5P_DEFAULT);
        }
        catch (Throwable err) {
            err.printStackTrace();
            fail("H5.H5Lget_val_by_idx: " + err);
        }
        assertFalse("testH5Lget_val_by_idx_ext ",link_value==null);
        assertTrue("testH5Lget_val_by_idx_ext Link Value ",link_value.compareTo("DT1")==0);
    }

    @Test(expected = HDF5LibraryException.class)
    public void testH5Ldelete_by_idx_not_exist_name() throws Throwable, HDF5LibraryException, NullPointerException {
        H5.H5Ldelete_by_idx(H5fid, "None", HDF5Constants.H5_INDEX_NAME, HDF5Constants.H5_ITER_INC, 0, HDF5Constants.H5P_DEFAULT);
    }

    @Test(expected = HDF5LibraryException.class)
    public void testH5Ldelete_by_idx_not_exist_create() throws Throwable, HDF5LibraryException, NullPointerException {
        H5.H5Ldelete_by_idx(H5fid, "None", HDF5Constants.H5_INDEX_CRT_ORDER, HDF5Constants.H5_ITER_INC, 0, HDF5Constants.H5P_DEFAULT);
    }

    @Test
    public void testH5Ldelete_by_idx_n2_name() throws Throwable, HDF5LibraryException, NullPointerException {
        H5L_info_t link_info = null;
        _createSoftLink(H5fid, "/G1/DS2", H5fid, "LS", HDF5Constants.H5P_DEFAULT, HDF5Constants.H5P_DEFAULT);
        try {
            link_info = H5.H5Lget_info_by_idx(H5fid, "/", HDF5Constants.H5_INDEX_NAME, HDF5Constants.H5_ITER_INC, 2, HDF5Constants.H5P_DEFAULT);
        }
        catch (Throwable err) {
            err.printStackTrace();
            fail("H5.H5Lget_info_by_idx: " + err);
        }
        assertFalse("testH5Ldelete_by_idx_n2 ",link_info==null);
        assertTrue("testH5Ldelete_by_idx_n2 link type",link_info.type==HDF5Constants.H5L_TYPE_SOFT);
        try {
            H5.H5Ldelete_by_idx(H5fid, "/", HDF5Constants.H5_INDEX_NAME, HDF5Constants.H5_ITER_INC, 2, HDF5Constants.H5P_DEFAULT);
        }
        catch (Throwable err) {
            err.printStackTrace();
            fail("H5.H5Ldelete_by_idx: " + err);
        }
        try {
            link_info = H5.H5Lget_info_by_idx(H5fid, "/", HDF5Constants.H5_INDEX_NAME, HDF5Constants.H5_ITER_INC, 2, HDF5Constants.H5P_DEFAULT);
        }
        catch (HDF5LibraryException err) {
            link_info = null;
        }
        catch (Throwable err) {
            err.printStackTrace();
            fail("H5.H5Ldelete_by_idx: " + err);
        }
        assertTrue("testH5Ldelete_by_idx_n2 ",link_info==null);
    }

    @Test
    public void testH5Ldelete_by_idx_n2_create() throws Throwable, HDF5LibraryException, NullPointerException {
        H5L_info_t link_info = null;
        _createSoftLink(H5fid, "/G1/DS2", H5fid, "LS", HDF5Constants.H5P_DEFAULT, HDF5Constants.H5P_DEFAULT);
        try {
            link_info = H5.H5Lget_info_by_idx(H5fid, "/", HDF5Constants.H5_INDEX_CRT_ORDER, HDF5Constants.H5_ITER_INC, 2, HDF5Constants.H5P_DEFAULT);
        }
        catch (Throwable err) {
            err.printStackTrace();
            fail("H5.H5Lget_info_by_idx: " + err);
        }
        assertFalse("testH5Ldelete_by_idx_n2 ",link_info==null);
        assertTrue("testH5Ldelete_by_idx_n2 link type",link_info.type==HDF5Constants.H5L_TYPE_SOFT);
        try {
            H5.H5Ldelete_by_idx(H5fid, "/", HDF5Constants.H5_INDEX_CRT_ORDER, HDF5Constants.H5_ITER_INC, 2, HDF5Constants.H5P_DEFAULT);
        }
        catch (Throwable err) {
            err.printStackTrace();
            fail("H5.H5Ldelete_by_idx: " + err);
        }
        try {
            link_info = H5.H5Lget_info_by_idx(H5fid, "/", HDF5Constants.H5_INDEX_CRT_ORDER, HDF5Constants.H5_ITER_INC, 2, HDF5Constants.H5P_DEFAULT);
        }
        catch (HDF5LibraryException err) {
            link_info = null;
        }
        catch (Throwable err) {
            err.printStackTrace();
            fail("H5.H5Ldelete_by_idx: " + err);
        }
        assertTrue("testH5Ldelete_by_idx_n2 ",link_info==null);
    }

    @Test
    public void testH5Lvist_create() throws Throwable, HDF5LibraryException, NullPointerException {
        int status = -1;
        
        int order = H5.H5Pget_link_creation_order(H5fcpl);
        assertTrue("creation order :"+order, order == HDF5Constants.H5P_CRT_ORDER_TRACKED+HDF5Constants.H5P_CRT_ORDER_INDEXED);
        
        _createHardLink(H5fid, H5fid, "/G1/DS2", H5fid, "CPY1", HDF5Constants.H5P_DEFAULT, HDF5Constants.H5P_DEFAULT);
        _createExternalLink(H5fid, H5_EXTFILE, "DT1", H5fid, "LE", HDF5Constants.H5P_DEFAULT, HDF5Constants.H5P_DEFAULT);
        _createSoftLink(H5fid, "/G1/DS2", H5fid, "LS", HDF5Constants.H5P_DEFAULT, HDF5Constants.H5P_DEFAULT);

        class idata {
            public String link_name = null;
            public int link_type = -1;
            idata(String name, int type) {
                this.link_name = name;
                this.link_type = type;
            }
        }
        class H5L_iter_data implements H5L_iterate_t {
            public ArrayList<idata> iterdata = new ArrayList<idata>();
        }
        H5L_iterate_t iter_data = new H5L_iter_data();
        class H5L_iter_callback implements H5L_iterate_cb {
            public int callback(int group, String name, H5L_info_t info, H5L_iterate_t op_data) {
                idata id = new idata(name, info.type);
                ((H5L_iter_data)op_data).iterdata.add(id);
                return 0;
            }
        }
        H5L_iterate_cb iter_cb = new H5L_iter_callback();
        try {
            status = H5.H5Lvisit(H5fid, HDF5Constants.H5_INDEX_CRT_ORDER, HDF5Constants.H5_ITER_INC, iter_cb, iter_data);
        }
        catch (Throwable err) {
            err.printStackTrace();
            fail("H5.H5Lvisit: " + err);
        }
        assertFalse("H5Lvisit ",((H5L_iter_data)iter_data).iterdata.isEmpty());
        assertTrue("H5Lvisit "+((H5L_iter_data)iter_data).iterdata.size(),((H5L_iter_data)iter_data).iterdata.size()==6);
        assertTrue("H5Lvisit "+((idata)((H5L_iter_data)iter_data).iterdata.get(0)).link_name,((idata)((H5L_iter_data)iter_data).iterdata.get(0)).link_name.compareToIgnoreCase("DS1")==0);
        assertTrue("H5Lvisit "+((idata)((H5L_iter_data)iter_data).iterdata.get(1)).link_name,((idata)((H5L_iter_data)iter_data).iterdata.get(1)).link_name.compareToIgnoreCase("G1")==0);
        assertTrue("H5Lvisit "+((idata)((H5L_iter_data)iter_data).iterdata.get(2)).link_name,((idata)((H5L_iter_data)iter_data).iterdata.get(2)).link_name.compareToIgnoreCase("G1/DS2")==0);
        assertTrue("H5Lvisit "+((idata)((H5L_iter_data)iter_data).iterdata.get(3)).link_name,((idata)((H5L_iter_data)iter_data).iterdata.get(3)).link_name.compareToIgnoreCase("CPY1")==0);
        assertTrue("H5Lvisit "+((idata)((H5L_iter_data)iter_data).iterdata.get(4)).link_name,((idata)((H5L_iter_data)iter_data).iterdata.get(4)).link_name.compareToIgnoreCase("LE")==0);
        assertTrue("H5Lvisit "+((idata)((H5L_iter_data)iter_data).iterdata.get(5)).link_name,((idata)((H5L_iter_data)iter_data).iterdata.get(5)).link_name.compareToIgnoreCase("LS")==0);
    }

}
