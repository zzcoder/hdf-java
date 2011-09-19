package test.hdf5lib;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;

import ncsa.hdf.hdf5lib.H5;
import ncsa.hdf.hdf5lib.HDF5Constants;
import ncsa.hdf.hdf5lib.exceptions.HDF5Exception;
import ncsa.hdf.hdf5lib.exceptions.HDF5LibraryException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestH5Pfapl {
    
    private static final String H5_FILE = "test.h5";
    private static final String H5_LOG_FILE = "test.log";
    private static final String H5_FAMILY_FILE = "test%05d";
    private static final int DIM_X = 4;
    private static final int DIM_Y = 6;
    int H5fid = -1;
    int H5dsid = -1;
    int H5did = -1;
    long[] H5dims = { DIM_X, DIM_Y };
    int fapl_id = -1;
    int plapl_id = -1;

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
    
    private final void _deleteLogFile() {
        File file = new File(H5_LOG_FILE);

        if (file.exists()) {
            try {
                file.delete();
            }
            catch (SecurityException e) {
                ;// e.printStackTrace();
            }
        }
    }

    private final void _deleteFamilyFile() {
        for(int indx = 0; ;indx++) {
            java.text.DecimalFormat myFormat = new java.text.DecimalFormat("00000");
            File file = new File("test"+myFormat.format(new Integer(indx))+".h5");

            if (file.exists()) {
                try {
                    file.delete();
                }
                catch (SecurityException e) {
                    ;// e.printStackTrace();
                }
            }
            else
                return;
        }
    }

    private final int _createDataset(int fid, int dsid, String name, int dapl) {
        int did = -1;
        try {
            did = H5.H5Dcreate(fid, name, HDF5Constants.H5T_STD_I32BE, dsid,
                    HDF5Constants.H5P_DEFAULT, HDF5Constants.H5P_DEFAULT, dapl);
        } catch (Throwable err) {
            err.printStackTrace();
            fail("H5.H5Dcreate: " + err);
        }
        assertTrue("TestH5D._createDataset: ", did > 0);

        return did;
    }

    private final void _createH5File(int fapl) {
        try {
            H5fid = H5.H5Fcreate(H5_FILE, HDF5Constants.H5F_ACC_TRUNC,
                    HDF5Constants.H5P_DEFAULT, fapl);
            H5dsid = H5.H5Screate_simple(2, H5dims, null);
            H5did = _createDataset(H5fid, H5dsid, "dset", HDF5Constants.H5P_DEFAULT);
        }
        catch (Throwable err) {
            err.printStackTrace();
            fail("TestH5D.createH5file: " + err);
        }
        assertTrue("TestH5D.createH5file: H5.H5Fcreate: ",H5fid > 0);
        assertTrue("TestH5D.createH5file: H5.H5Screate_simple: ",H5dsid > 0);
        assertTrue("TestH5D.createH5file: _createDataset: ",H5did > 0);

        try {
            H5.H5Fflush(H5fid, HDF5Constants.H5F_SCOPE_LOCAL);
        }
        catch (Throwable err) {
            err.printStackTrace();
        }
    }

    private final void _createH5familyFile(int fapl) {
        try {
            H5fid = H5.H5Fcreate(H5_FAMILY_FILE+".h5", HDF5Constants.H5F_ACC_TRUNC,
                    HDF5Constants.H5P_DEFAULT, fapl);
            H5dsid = H5.H5Screate_simple(2, H5dims, null);
            H5did = _createDataset(H5fid, H5dsid, "dset", HDF5Constants.H5P_DEFAULT);
        }
        catch (Throwable err) {
            err.printStackTrace();
            fail("TestH5D.createH5file: " + err);
        }
        assertTrue("TestH5D.createH5file: H5.H5Fcreate: ",H5fid > 0);
        assertTrue("TestH5D.createH5file: H5.H5Screate_simple: ",H5dsid > 0);
        assertTrue("TestH5D.createH5file: _createDataset: ",H5did > 0);

        try {
            H5.H5Fflush(H5fid, HDF5Constants.H5F_SCOPE_LOCAL);
        }
        catch (Throwable err) {
            err.printStackTrace();
        }
    }

    public void deleteH5file() throws HDF5LibraryException {
        if (H5dsid > 0) 
            H5.H5Sclose(H5dsid);
        if (H5did > 0) 
            H5.H5Dclose(H5did);         
        if (H5fid > 0) 
            H5.H5Fclose(H5fid);
 
        _deleteFile(H5_FILE);
    }

    public void deleteH5familyfile() throws HDF5LibraryException {
        if (H5dsid > 0) 
            H5.H5Sclose(H5dsid);
        if (H5did > 0) 
            H5.H5Dclose(H5did);         
        if (H5fid > 0) 
            H5.H5Fclose(H5fid);
 
        _deleteFamilyFile();
    }

    @Before
    public void createFileAccess()
            throws NullPointerException, HDF5Exception {
        assertTrue("H5 open ids is 0",H5.getOpenIDCount()==0);

        try {
            fapl_id = H5.H5Pcreate(HDF5Constants.H5P_FILE_ACCESS);
        }
        catch (Throwable err) {
            err.printStackTrace();
            fail("TestH5D.createFileAccess: " + err);
        }
        assertTrue(fapl_id > 0);
        try {
            plapl_id = H5.H5Pcreate(HDF5Constants.H5P_LINK_ACCESS);
        }
        catch (Throwable err) {
            err.printStackTrace();
            fail("H5Pset_elink_fapl: " + err);
        }
        assertTrue(plapl_id>0);
    }

    @After
    public void deleteFileAccess() throws HDF5LibraryException {
        if (fapl_id >0)
            H5.H5Pclose(fapl_id);
        if (plapl_id >0)
            H5.H5Pclose(plapl_id);
    }

    @Test(expected = HDF5LibraryException.class)
    public void testH5Pset_elink_fapl_NegativeID() throws Throwable, HDF5LibraryException {
        H5.H5Pset_elink_fapl(-1, fapl_id );
    }

    @Test
    public void testH5Pset_elink_fapl() throws Throwable, HDF5LibraryException {
        int ret_val = -1;
        try {
            ret_val = H5.H5Pset_elink_fapl(plapl_id, fapl_id );
        }
        catch (Throwable err) {
            err.printStackTrace();
            fail("H5Pset_elink_fapl: " + err);
        }
        assertTrue("H5Pset_elink_fapl", ret_val >= 0);
    }
    
    @Test
    public void testH5Pget_elink_fapl() throws Throwable, HDF5LibraryException {
        int ret_val_id = -1;
        try {
            ret_val_id = H5.H5Pget_elink_fapl(plapl_id);
            assertTrue("H5Pget_elink_fapl", ret_val_id >= 0);
            assertEquals(HDF5Constants.H5P_DEFAULT, ret_val_id );
        }
        catch (Throwable err) {
            err.printStackTrace();
            fail("H5Pget_elink_fapl: " + err);
        }
        finally {
            if (ret_val_id > 0)
                H5.H5Pclose(ret_val_id);    
        }
    }
    
    @Test
    public void testH5Pget_elink_fapl_SET() throws Throwable, HDF5LibraryException {
        int ret_val_id = -1;
        try {
            H5.H5Pset_elink_fapl(plapl_id, fapl_id );
            ret_val_id = H5.H5Pget_elink_fapl(plapl_id);
            assertTrue("H5Pget_elink_fapl_SET", ret_val_id >= 0);
        }
        catch (Throwable err) {
            err.printStackTrace();
            fail("H5Pget_elink_fapl_SET: " + err);
        }
        finally {
            if (ret_val_id > 0)
            H5.H5Pclose(ret_val_id);    
        }
    }
    
    @Test
    public void testH5P_fapl_core() throws Throwable, HDF5LibraryException {
        if (HDF5Constants.H5FD_CORE < 0)
            return;
        try {
            H5.H5Pset_fapl_core(fapl_id, 4096, false);
            int driver_type = H5.H5Pget_driver(fapl_id);
            assertTrue("H5Pget_driver: core = "+ driver_type, HDF5Constants.H5FD_CORE==driver_type);
        }
        catch (Throwable err) {
            err.printStackTrace();
            fail("H5Pset_fapl_core: " + err);
        }
        try {
            long[] increment = {-1};
            boolean[] backingstore = {true};
            H5.H5Pget_fapl_core(fapl_id, increment, backingstore);
            assertTrue("H5Pget_fapl_core: increment="+increment[0], increment[0]==4096);
            assertTrue("H5Pget_fapl_core: backingstore="+backingstore[0], !backingstore[0]);
        }
        catch (Throwable err) {
            err.printStackTrace();
            fail("H5Pget_fapl_core: " + err);
        }
    }
    
    @Test
    public void testH5P_fapl_family() throws Throwable, HDF5LibraryException {
        if (HDF5Constants.H5FD_FAMILY < 0)
            return;
        try {
            H5.H5Pset_fapl_family(fapl_id, 1024, HDF5Constants.H5P_DEFAULT);
            int driver_type = H5.H5Pget_driver(fapl_id);
            assertTrue("H5Pget_driver: family = "+ driver_type, HDF5Constants.H5FD_FAMILY==driver_type);
        }
        catch (Throwable err) {
            err.printStackTrace();
            fail("H5Pset_fapl_family: " + err);
        }
        try {
            long[] member_size = {0};
            int[] member_fapl = {-1};
            H5.H5Pget_fapl_family(fapl_id, member_size, member_fapl);
            assertTrue("H5Pget_fapl_family: member_size="+member_size[0], member_size[0]==1024);
            assertTrue("H5Pget_fapl_family: member_fapl ", H5.H5P_equal(member_fapl[0], HDF5Constants.H5P_FILE_ACCESS_DEFAULT));
        }
        catch (Throwable err) {
            err.printStackTrace();
            fail("H5Pget_fapl_family: " + err);
        }
        _createH5familyFile(fapl_id);
        deleteH5familyfile();
    }
    
    @Test
    public void testH5P_family_offset() throws Throwable, HDF5LibraryException {
        if (HDF5Constants.H5FD_FAMILY < 0)
            return;
        try {
            H5.H5Pset_fapl_family(fapl_id, 1024, HDF5Constants.H5P_DEFAULT);
            int driver_type = H5.H5Pget_driver(fapl_id);
            assertTrue("H5Pget_driver: family = "+ driver_type, HDF5Constants.H5FD_FAMILY==driver_type);
        }
        catch (Throwable err) {
            err.printStackTrace();
            fail("H5Pset_fapl_family: " + err);
        }
        _createH5familyFile(fapl_id);
        long family_offset = 512;
        try {
            H5.H5Pset_family_offset(fapl_id, family_offset);
        }
        catch (Throwable err) {
            err.printStackTrace();
            fail("H5Pget_fapl_family: " + err);
        }
        try {
            long offset = H5.H5Pget_family_offset(fapl_id);
            assertTrue("H5Pget_fapl_family: offset="+offset, offset==family_offset);
        }
        catch (Throwable err) {
            err.printStackTrace();
            fail("H5Pget_fapl_family: " + err);
        }
        deleteH5familyfile();
    }
    
    @Test
    public void testH5Pset_fapl_sec2() throws Throwable, HDF5LibraryException {
        if (HDF5Constants.H5FD_SEC2 < 0)
            return;
        try {
            H5.H5Pset_fapl_sec2(fapl_id);
            int driver_type = H5.H5Pget_driver(fapl_id);
            assertTrue("H5Pget_driver: sec2 = "+ driver_type, HDF5Constants.H5FD_SEC2==driver_type);
        }
        catch (Throwable err) {
            err.printStackTrace();
            fail("H5Pset_fapl_sec2: " + err);
        }
        _createH5File(fapl_id);
        deleteH5file();
    }
    
    @Test
    public void testH5Pset_fapl_stdio() throws Throwable, HDF5LibraryException {
        if (HDF5Constants.H5FD_STDIO < 0)
            return;
        try {
            H5.H5Pset_fapl_stdio(fapl_id);
            int driver_type = H5.H5Pget_driver(fapl_id);
            assertTrue("H5Pget_driver: stdio = "+ driver_type, HDF5Constants.H5FD_STDIO==driver_type);
        }
        catch (Throwable err) {
            err.printStackTrace();
            fail("H5Pset_fapl_stdio: " + err);
        }
        _createH5File(fapl_id);
        deleteH5file();
    }
    
    @Test
    public void testH5Pset_fapl_log() throws Throwable, HDF5LibraryException {
        if (HDF5Constants.H5FD_LOG < 0)
            return;
        try {
            long log_flags = HDF5Constants.H5FD_LOG_LOC_IO;
            H5.H5Pset_fapl_log(fapl_id, H5_LOG_FILE, log_flags, 1024);
            int driver_type = H5.H5Pget_driver(fapl_id);
            assertTrue("H5Pget_driver: log = "+ driver_type, HDF5Constants.H5FD_LOG==driver_type);
        }
        catch (Throwable err) {
            err.printStackTrace();
            fail("H5Pset_fapl_log: " + err);
        }
        _createH5File(fapl_id);
        deleteH5file();
        _deleteLogFile();
    }
    
    @Test
    public void testH5P_fapl_direct() throws Throwable, HDF5LibraryException {
        if (HDF5Constants.H5FD_DIRECT < 0)
            return;
        try {
            H5.H5Pset_fapl_direct(fapl_id, 1024, 4096, 8*4096);
            int driver_type = H5.H5Pget_driver(fapl_id);
            assertTrue("H5Pget_driver: direct = "+ driver_type, HDF5Constants.H5FD_DIRECT==driver_type);
        }
        catch (Throwable err) {
            err.printStackTrace();
            fail("H5Pset_fapl_direct: " + err);
        }
        try {
            long[] params = {-1, -1, -1};
            H5.H5Pget_fapl_direct(fapl_id, params);
            assertTrue("H5Pget_fapl_direct: alignment="+params[0], params[0]==1024);
            assertTrue("H5Pget_fapl_direct: block_size="+params[1], params[1]==4096);
            assertTrue("H5Pget_fapl_direct: cbuf_size="+params[2], params[2]==8*4096);
        }
        catch (Throwable err) {
            err.printStackTrace();
            fail("H5Pget_fapl_direct: " + err);
        }
        _createH5File(fapl_id);
        deleteH5file();
    }
    
    @Test
    public void testH5Pset_fapl_windows() throws Throwable, HDF5LibraryException {
        if (HDF5Constants.H5FD_WINDOWS < 0)
            return;
        try {
            H5.H5Pset_fapl_windows(fapl_id);
            int driver_type = H5.H5Pget_driver(fapl_id);
            assertTrue("H5Pget_driver: windows = "+ driver_type, HDF5Constants.H5FD_WINDOWS==driver_type);
        }
        catch (Throwable err) {
            err.printStackTrace();
            fail("H5Pset_fapl_windows: " + err);
        }
        _createH5File(fapl_id);
        deleteH5file();
    }
}