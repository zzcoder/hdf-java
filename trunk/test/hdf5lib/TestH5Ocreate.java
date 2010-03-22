package test.hdf5lib;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.util.ArrayList;

import ncsa.hdf.hdf5lib.H5;
import ncsa.hdf.hdf5lib.HDF5Constants;
import ncsa.hdf.hdf5lib.exceptions.HDF5Exception;
import ncsa.hdf.hdf5lib.exceptions.HDF5LibraryException;
import ncsa.hdf.hdf5lib.structs.H5O_info_t;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestH5Ocreate {
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
        assertTrue("TestH5O._createDataset: ",did > 0);

        return did;
    }

    private final int _createGroup(int fid, String name) {
        int gid = -1;
        try {
            H5gcpl = HDF5Constants.H5P_DEFAULT;
            gid = H5.H5Gcreate2(fid, name, HDF5Constants.H5P_DEFAULT,
                    H5gcpl, HDF5Constants.H5P_DEFAULT);
        }
        catch (Throwable err) {
            err.printStackTrace();
            fail("H5.H5Gcreate: " + err);
        }
        assertTrue("TestH5O._createGroup: ",gid > 0);

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
        assertTrue("TestH5O._createHardLink ", link_exists);
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
        assertTrue("TestH5O._createSoftLink ", link_exists);
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
        assertTrue("TestH5O._createExternalLink ", link_exists);
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
            fail("TestH5O.createH5file: " + err);
        }
        assertTrue("TestH5O.createH5file: H5.H5Fcreate: ",H5fid > 0);
        assertTrue("TestH5O.createH5file: H5.H5Screate_simple: ",H5dsid > 0);
        assertTrue("TestH5O.createH5file: H5.H5Gcreate: ",H5gid > 0);

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

    @Test(expected = HDF5LibraryException.class)
    public void testH5Ocopy_cur_not_exists() throws Throwable, HDF5LibraryException, NullPointerException {
        H5.H5Ocopy(H5fid, "None", H5fid, "DS1", HDF5Constants.H5P_DEFAULT, HDF5Constants.H5P_DEFAULT);
    }

    @Test
    public void testH5Ocopy() throws Throwable, HDF5LibraryException, NullPointerException {
        H5.H5Ocopy(H5fid, "DS1", H5fid, "CPY1", HDF5Constants.H5P_DEFAULT, HDF5Constants.H5P_DEFAULT);
        H5.H5Fflush(H5fid, HDF5Constants.H5F_SCOPE_LOCAL);
        boolean link_exists = H5.H5Lexists(H5fid, "CPY1", HDF5Constants.H5P_DEFAULT);
        assertTrue("testH5Ocopy:H5Lexists ",link_exists);
    }

    @Test(expected = HDF5LibraryException.class)
    public void testH5Ocopy_dst_link_exists() throws Throwable, HDF5LibraryException, NullPointerException {
        _createHardLink(H5fid, H5fid, "/G1/DS2", H5fid, "CPY1", HDF5Constants.H5P_DEFAULT, HDF5Constants.H5P_DEFAULT);
        H5.H5Ocopy(H5fid, "CPY1", H5fid, "/G1/DS2", HDF5Constants.H5P_DEFAULT, HDF5Constants.H5P_DEFAULT);
    }
//
//    @Test
//    public void testH5Oget_info_by_idx_n0_create() throws Throwable, HDF5LibraryException, NullPointerException {
//        H5O_info_t link_info = null;
//        int order = H5.H5Pget_link_creation_order(H5fcpl);
//        assertTrue("creation order :"+order, order == HDF5Constants.H5P_CRT_ORDER_TRACKED+HDF5Constants.H5P_CRT_ORDER_INDEXED);
//        try {
//            link_info = H5.H5Oget_info_by_idx(H5fid, "/", HDF5Constants.H5_INDEX_CRT_ORDER, HDF5Constants.H5_ITER_INC, 0, HDF5Constants.H5P_DEFAULT);
//        }
//        catch (Throwable err) {
//            err.printStackTrace();
//            fail("H5.H5Oget_info_by_idx: " + err);
//        }
//        assertFalse("H5Oget_info_by_idx ",link_info==null);
//        assertTrue("H5Oget_info_by_idx link type",link_info.type==HDF5Constants.H5O_TYPE_HARD);
//    }
//
//    @Test
//    public void testH5Oget_info_by_idx_n1_create() throws Throwable, HDF5LibraryException, NullPointerException {
//        H5O_info_t link_info = null;
//        int order = H5.H5Pget_link_creation_order(H5fcpl);
//        assertTrue("creation order :"+order, order == HDF5Constants.H5P_CRT_ORDER_TRACKED+HDF5Constants.H5P_CRT_ORDER_INDEXED);
//        try {
//            link_info = H5.H5Oget_info_by_idx(H5fid, "/", HDF5Constants.H5_INDEX_CRT_ORDER, HDF5Constants.H5_ITER_INC, 1, HDF5Constants.H5P_DEFAULT);
//        }
//        catch (Throwable err) {
//            err.printStackTrace();
//            fail("H5.H5Oget_info_by_idx: " + err);
//        }
//        assertFalse("H5Oget_info_by_idx ",link_info==null);
//        assertTrue("H5Oget_info_by_idx link type",link_info.type==HDF5Constants.H5O_TYPE_HARD);
//    }
//
//    @Test
//    public void testH5Oget_info_softlink() throws Throwable, HDF5LibraryException, NullPointerException {
//        H5O_info_t link_info = null;
//        _createSoftLink(H5fid, "/G1/DS2", H5fid, "L1", HDF5Constants.H5P_DEFAULT, HDF5Constants.H5P_DEFAULT);
//        try {
//            link_info = H5.H5Oget_info(H5fid, "L1", HDF5Constants.H5P_DEFAULT);
//        }
//        catch (Throwable err) {
//            err.printStackTrace();
//            fail("H5.H5Oget_info: " + err);
//        }
//        assertFalse("H5Oget_info ",link_info==null);
//        assertTrue("H5Oget_info link type",link_info.type==HDF5Constants.H5O_TYPE_SOFT);
//        assertTrue("Link Address ",link_info.address_val_size>0);
//    }
//
//    @Test
//    public void testH5Oget_info_softlink_dangle() throws Throwable, HDF5LibraryException, NullPointerException {
//        H5O_info_t link_info = null;
//        _createSoftLink(H5fid, "DS3", H5fid, "L2", HDF5Constants.H5P_DEFAULT, HDF5Constants.H5P_DEFAULT);
//        try {
//            link_info = H5.H5Oget_info(H5fid, "L2", HDF5Constants.H5P_DEFAULT);
//        }
//        catch (Throwable err) {
//            err.printStackTrace();
//            fail("H5.H5Oget_info: " + err);
//        }
//        assertFalse("H5Oget_info ",link_info==null);
//        assertTrue("H5Oget_info link type",link_info.type==HDF5Constants.H5O_TYPE_SOFT);
//        assertTrue("Link Address ",link_info.address_val_size>0);
//    }
//
//    @Test
//    public void testH5Oget_info_externallink() throws Throwable, HDF5LibraryException, NullPointerException {
//        H5O_info_t link_info = null;
//        _createExternalLink(H5fid, H5_EXTFILE, "DT1", H5fid, "L1", HDF5Constants.H5P_DEFAULT, HDF5Constants.H5P_DEFAULT);
//        try {
//            link_info = H5.H5Oget_info(H5fid, "L1", HDF5Constants.H5P_DEFAULT);
//        }
//        catch (Throwable err) {
//            err.printStackTrace();
//            fail("H5.H5Oget_info: " + err);
//        }
//        assertFalse("H5Oget_info ",link_info==null);
//        assertTrue("H5Oget_info link type",link_info.type==HDF5Constants.H5O_TYPE_EXTERNAL);
//        assertTrue("Link Address ",link_info.address_val_size>0);
//    }
//
//    @Test(expected = HDF5LibraryException.class)
//    public void testH5Olink_cur_not_exists() throws Throwable, HDF5LibraryException, NullPointerException {
//        H5.H5Olink(H5fid, "None", H5fid, "DS1", HDF5Constants.H5P_DEFAULT, HDF5Constants.H5P_DEFAULT);
//    }
//
//    @Test
//    public void testH5Olink() throws Throwable, HDF5LibraryException, NullPointerException {
//        H5.H5Olink(H5fid, "DS1", H5fid, "CPY1", HDF5Constants.H5P_DEFAULT, HDF5Constants.H5P_DEFAULT);
//        H5.H5Fflush(H5fid, HDF5Constants.H5F_SCOPE_LOCAL);
//        boolean link_exists = H5.H5Oexists(H5fid, "CPY1", HDF5Constants.H5P_DEFAULT);
//        assertTrue("testH5Olink:H5Oexists ",link_exists);
//        link_exists = H5.H5Oexists(H5fid, "DS1", HDF5Constants.H5P_DEFAULT);
//        assertFalse("testH5Olink:H5Oexists ",link_exists);
//    }
//
//    @Test(expected = HDF5LibraryException.class)
//    public void testH5Olink_dst_link_exists() throws Throwable, HDF5LibraryException, NullPointerException {
//        _createHardLink(H5fid, H5fid, "/G1/DS2", H5fid, "CPY1", HDF5Constants.H5P_DEFAULT, HDF5Constants.H5P_DEFAULT);
//        H5.H5Olink(H5fid, "CPY1", H5fid, "/G1/DS2", HDF5Constants.H5P_DEFAULT, HDF5Constants.H5P_DEFAULT);
//    }
//
//    @Test
//    public void testH5Ovisit_create() throws Throwable, HDF5LibraryException, NullPointerException {
//        int order = H5.H5Pget_link_creation_order(H5fcpl);
//        assertTrue("creation order :"+order, order == HDF5Constants.H5P_CRT_ORDER_TRACKED+HDF5Constants.H5P_CRT_ORDER_INDEXED);
//        
//        _createHardLink(H5fid, H5fid, "/G1/DS2", H5fid, "CPY1", HDF5Constants.H5P_DEFAULT, HDF5Constants.H5P_DEFAULT);
//        _createExternalLink(H5fid, H5_EXTFILE, "DT1", H5fid, "LE", HDF5Constants.H5P_DEFAULT, HDF5Constants.H5P_DEFAULT);
//        _createSoftLink(H5fid, "/G1/DS2", H5fid, "LS", HDF5Constants.H5P_DEFAULT, HDF5Constants.H5P_DEFAULT);
//
//        class idata {
//            public String link_name = null;
//            public int link_type = -1;
//            idata(String name, int type) {
//                this.link_name = name;
//                this.link_type = type;
//            }
//        }
//        class H5O_iter_data implements H5O_iterate_t {
//            public ArrayList<idata> iterdata = new ArrayList<idata>();
//        }
//        H5O_iterate_t iter_data = new H5O_iter_data();
//        class H5O_iter_callback implements H5O_iterate_cb {
//            public int callback(int group, String name, H5O_info_t info, H5O_iterate_t op_data) {
//                idata id = new idata(name, info.type);
//                ((H5O_iter_data)op_data).iterdata.add(id);
//                return 0;
//            }
//        }
//        H5O_iterate_cb iter_cb = new H5O_iter_callback();
//        try {
//            H5.H5Ovisit(H5fid, HDF5Constants.H5_INDEX_CRT_ORDER, HDF5Constants.H5_ITER_INC, iter_cb, iter_data);
//        }
//        catch (Throwable err) {
//            err.printStackTrace();
//            fail("H5.H5Ovisit: " + err);
//        }
//        assertFalse("H5Ovisit ",((H5O_iter_data)iter_data).iterdata.isEmpty());
//        assertTrue("H5Ovisit "+((H5O_iter_data)iter_data).iterdata.size(),((H5O_iter_data)iter_data).iterdata.size()==6);
//        assertTrue("H5Ovisit "+((idata)((H5O_iter_data)iter_data).iterdata.get(0)).link_name,((idata)((H5O_iter_data)iter_data).iterdata.get(0)).link_name.compareToIgnoreCase("DS1")==0);
//        assertTrue("H5Ovisit "+((idata)((H5O_iter_data)iter_data).iterdata.get(1)).link_name,((idata)((H5O_iter_data)iter_data).iterdata.get(1)).link_name.compareToIgnoreCase("G1")==0);
//        assertTrue("H5Ovisit "+((idata)((H5O_iter_data)iter_data).iterdata.get(2)).link_name,((idata)((H5O_iter_data)iter_data).iterdata.get(2)).link_name.compareToIgnoreCase("G1/DS2")==0);
//        assertTrue("H5Ovisit "+((idata)((H5O_iter_data)iter_data).iterdata.get(3)).link_name,((idata)((H5O_iter_data)iter_data).iterdata.get(3)).link_name.compareToIgnoreCase("CPY1")==0);
//        assertTrue("H5Ovisit "+((idata)((H5O_iter_data)iter_data).iterdata.get(4)).link_name,((idata)((H5O_iter_data)iter_data).iterdata.get(4)).link_name.compareToIgnoreCase("LE")==0);
//        assertTrue("H5Ovisit "+((idata)((H5O_iter_data)iter_data).iterdata.get(5)).link_name,((idata)((H5O_iter_data)iter_data).iterdata.get(5)).link_name.compareToIgnoreCase("LS")==0);
//    }

}
