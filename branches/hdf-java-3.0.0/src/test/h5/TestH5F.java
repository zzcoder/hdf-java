package test.h5;

import static org.junit.Assert.*;
import hdf.h5.H5F;
import hdf.h5.constants.H5Fconstant;
import hdf.h5.constants.H5Pconstant;
import hdf.h5.enums.H5F_SCOPE;

import java.io.File;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestH5F {
    private static final String H5_FILE = "test.h5";
    private static final String TXT_FILE = "test.txt";
    private static final int DEFAULT = H5Pconstant.H5P_DEFAULT;

    private final void _deleteFile(String filename)
    {
        File file = new File(filename);
        
        if (file.exists()) {
            try {
                file.delete();
            } catch (SecurityException e) {
                e.printStackTrace();
            }
        }
    }
    
    private final void _createH5File(String filename)
    {
        int fid = -1;
        
        try {
            fid = H5F.H5Fcreate(filename, H5Fconstant.H5F_ACC_TRUNC, 
                DEFAULT, DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        if (fid > 0) {
            try { H5F.H5Fclose(fid); }
            catch (Exception e) {}
        }
    }
    
    private final void _testH5Fcreate(String filename, int flag) {
        int fid = -1;
        
        // file already created at setUp()
        try {
            fid = H5F.H5Fcreate(filename, flag, 
                DEFAULT, DEFAULT);
        } catch (Throwable err) {
            fail("H5.H5Fcreate failed: "+err);
        }
        
        assertTrue(fid > 0);
        
        if (fid > 0) {
            try { H5F.H5Fclose(fid); }
            catch (Exception e) {}
        }
    }
    

    @Before
    public void setUp() throws Exception {
        // create a test file
        _createH5File(H5_FILE);
        
        File txtFile = new File(TXT_FILE);
        
        if (!txtFile.exists())
            txtFile.createNewFile();
    }
    
    @After
    public void tearDown() throws Exception {
        // delete the test files created at setUp()
        
        _deleteFile(H5_FILE);
        _deleteFile(TXT_FILE);
    }

    @Test
    public void testH5Fis_hdf5() {
        boolean isH5 = false;
        
        try { isH5 = H5F.H5Fis_hdf5(H5_FILE); }
        catch (Throwable err) {
            fail("H5.H5Fis_hdf5 failed: "+err);
        }
        assertTrue(isH5);
        
        try { isH5 = H5F.H5Fis_hdf5(TXT_FILE); }
        catch (Throwable err) {
            fail("H5.H5Fis_hdf5 failed: "+err);
        }
        assertFalse(isH5);
    }

    @Test
    public void testH5Fcreate() {
        int fid;
        _testH5Fcreate(H5_FILE, H5Fconstant.H5F_ACC_TRUNC);
        
        _deleteFile(H5_FILE);
        _testH5Fcreate(H5_FILE, H5Fconstant.H5F_ACC_EXCL);

        // expect to fail with H5F_ACC_EXCL since file exists.
        try {
            fid = H5F.H5Fcreate(H5_FILE, H5Fconstant.H5F_ACC_EXCL, 
                DEFAULT, DEFAULT);
        } catch (Throwable err) {
            fid = -1;
        }
        
        assertTrue(fid < 0);
       
        if (fid > 0) {
            try { H5F.H5Fclose(fid); }
            catch (Exception e) {}
        }
    }

    @Test
    public void testH5Fopen() {
        int fid = -1;
        
        try { fid = H5F.H5Fopen(H5_FILE, H5Fconstant.H5F_ACC_RDWR, DEFAULT); }
        catch (Throwable err) {
            fail("H5.H5Fopen: "+err);
        }
        
        assertTrue (fid > 0);
        try { H5F.H5Fclose(fid); } catch (Exception ex) {}
        
        // set the file to read-only
        File file = new File(H5_FILE);
        if (!file.setWritable(false)) {
            fail("File.setWritable(true) failed.");
            return;
        }
 
        // it should fail.
        try { fid = H5F.H5Fopen(H5_FILE, H5Fconstant.H5F_ACC_RDWR, DEFAULT); }
        catch (Throwable err) {
            fid = -1;
        }
        assertTrue(fid < 0);
        
        // it should fail.
        try { fid = H5F.H5Fopen(H5_FILE, H5Fconstant.H5F_ACC_RDONLY, DEFAULT); }
        catch (Throwable err) {
            fid = -1;
        }
        assertTrue(fid > 0);
        try { H5F.H5Fclose(fid); } catch (Exception ex) {}

        file.setWritable(true);    
    }

    @Test
    public void testH5Freopen() {
        int fid = -1, fid2=-1;
        
        try { fid = H5F.H5Fopen(H5_FILE, H5Fconstant.H5F_ACC_RDWR, DEFAULT); }
        catch (Throwable err) {
            fail("H5.H5Fopen: "+err);
        }
        assertTrue (fid > 0);
        
        try { fid2 = H5F.H5Freopen(fid); }
        catch (Throwable err) {
            fail("H5.H5Freopen: "+err);
        }
        assertTrue (fid2 > 0);
        try { H5F.H5Fclose(fid2); } catch (Exception ex) {}
        try { H5F.H5Fclose(fid); } catch (Exception ex) {}
        
        // should fail because the file was closed.
        try { fid2 = H5F.H5Freopen(fid); }
        catch (Throwable err) {
            fid2 = -1;
        }
        assertTrue (fid2 < 0);
    }

    @Test
    public void testH5Fflush() {
        int fid = -1;
        
        try { fid = H5F.H5Fopen(H5_FILE, H5Fconstant.H5F_ACC_RDWR, DEFAULT); }
        catch (Throwable err) {
            fail("H5.H5Fopen: "+err);
        }
        assertTrue (fid > 0);
        
        try { H5F.H5Fflush(fid, H5F_SCOPE.GLOBAL); }
        catch (Throwable err) {
            fail("H5.H5Fflush: "+err);
        }
       
        try { H5F.H5Fflush(fid, H5F_SCOPE.LOCAL); }
        catch (Throwable err) {
            fail("H5.H5Fflush: "+err);
        }
        
        try { H5F.H5Fclose(fid); } catch (Exception ex) {}
    }

    @Test
    public void testH5Fclose() {
        fail("Not yet implemented");
    }

    @Test
    public void testH5Fget_create_plist() {
        fail("Not yet implemented");
    }

    @Test
    public void testH5Fget_access_plist() {
        fail("Not yet implemented");
    }

    @Test
    public void testH5Fget_intent() {
        fail("Not yet implemented");
    }

    @Test
    public void testH5Fget_obj_count() {
        fail("Not yet implemented");
    }

    @Test
    public void testH5Fget_obj_ids() {
        fail("Not yet implemented");
    }

    @Test
    public void testH5Fmount() {
        fail("Not yet implemented");
    }

    @Test
    public void testH5Funmount() {
        fail("Not yet implemented");
    }

    @Test
    public void testH5Fget_freespace() {
        fail("Not yet implemented");
    }

    @Test
    public void testH5Fget_filesize() {
        fail("Not yet implemented");
    }

    @Test
    public void testH5Fget_mdc_hit_rate() {
        fail("Not yet implemented");
    }

    @Test
    public void testH5Fget_mdc_size() {
        fail("Not yet implemented");
    }

    @Test
    public void testH5Freset_mdc_hit_rate_stats() {
        fail("Not yet implemented");
    }

    @Test
    public void testH5Fget_name() {
        fail("Not yet implemented");
    }

}
