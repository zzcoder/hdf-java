package test.h5;

import hdf.h5.H5;
import hdf.h5.H5F;
import hdf.h5.H5G;
import hdf.h5.constants.H5Fconstant;
import hdf.h5.constants.H5Pconstant;
import hdf.h5.enums.H5F_SCOPE;

import java.io.File;

import junit.framework.TestCase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestH5F extends TestCase
{
    private static final boolean is16 = H5.isAPI16;
    private static final int DEFAULT = H5Pconstant.H5P_DEFAULT;
    private static final String H5_FILE = "test.h5";
    private static final String TXT_FILE = "test.txt";
    private static final int COUNT_OBJ_FILE = 1;
    private static final int COUNT_OBJ_DATASET = 0;
    private static final int COUNT_OBJ_GROUP = 0;
    private static final int COUNT_OBJ_DATATYPE = 0;
    private static final int COUNT_OBJ_ATTR = 0;
    private static final int COUNT_OBJ_ALL = (COUNT_OBJ_FILE+COUNT_OBJ_DATASET+
            COUNT_OBJ_GROUP+COUNT_OBJ_DATATYPE+COUNT_OBJ_ATTR);
    private static final int[] OBJ_COUNTS = {COUNT_OBJ_FILE, COUNT_OBJ_DATASET, 
        COUNT_OBJ_GROUP, COUNT_OBJ_DATATYPE, COUNT_OBJ_ATTR, COUNT_OBJ_ALL};
    private static final int[] OBJ_TYPES = {H5Fconstant.H5F_OBJ_FILE, 
        H5Fconstant.H5F_OBJ_DATASET, H5Fconstant.H5F_OBJ_GROUP, 
        H5Fconstant.H5F_OBJ_DATATYPE, H5Fconstant.H5F_OBJ_ATTR, 
        H5Fconstant.H5F_OBJ_ALL};

    private final void _deleteFile(String filename)
    {
        File file = new File(filename);
        
        if (file.exists()) {
            try {
                file.delete();
            } catch (SecurityException e) {
                ;//e.printStackTrace();
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
            ;//e.printStackTrace();
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
            fail("H5F.H5Fcreate failed: "+err);
        }
        
        assertTrue(fid > 0);
        
        if (fid > 0) {
            try { H5F.H5Fclose(fid); }
            catch (Exception e) {}
        }
    }
    
    private final int _createGroup(int fid, String name) {
        int gid = -1;
        try {
            if (is16)
                gid = H5G.H5Gcreate(fid, name, 0);
            else
                gid = H5G.H5Gcreate(fid, name, DEFAULT, DEFAULT, DEFAULT);
        }
        catch (Throwable err) {
            fail("H5G.H5Gcreate: "+err);
        }
        assertTrue(gid > 0);
        
        return gid;
    }
    
    private final int _openGroup(int fid, String name) {
        int gid = -1;
        try {
            if (is16)
                gid = H5G.H5Gopen(fid, name);
            else
                gid = H5G.H5Gopen(fid, name, DEFAULT);;
        } catch (Throwable err) {
            gid = -1;
        }
        
        return gid;

    }
    
    private final int _openFile(String filename, int flag) {
        int fid = -1;
        
        try { fid = H5F.H5Fopen(filename, flag, DEFAULT); }
        catch (Throwable err) {
            fail("H5F.H5Fopen: "+err);
        }
        assertTrue (fid > 0);
        return fid;
    }

    @Override
    @Before
    public void setUp() throws Exception {
        // create a test file
        _createH5File(H5_FILE);
        
        File txtFile = new File(TXT_FILE);
        
        if (!txtFile.exists())
            txtFile.createNewFile();
    }
    
    @Override
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
            fail("H5F.H5Fis_hdf5 failed: "+err);
        }
        assertTrue(isH5);
        
        try { isH5 = H5F.H5Fis_hdf5(TXT_FILE); }
        catch (Throwable err) {
            fail("H5F.H5Fis_hdf5 failed: "+err);
        }
        assertFalse(isH5);
    }

    @Test
    public void testH5Fcreate() {
        int fid;
        
        /* test null */
        try { 
            fid = H5F.H5Fcreate(null, H5Fconstant.H5F_ACC_TRUNC, DEFAULT, DEFAULT); 
            // shouldn't goto the line below
            fail("H5F.H5Fcreate(null, ...) should fail since the file name is null.");
        }
        catch (NullPointerException err) {}
        catch (Throwable err) {
            fail("H5F.H5Fopen: "+err);
        }
        
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
        
        /* test null */
        try { 
            fid = H5F.H5Fopen(null, H5Fconstant.H5F_ACC_RDWR, DEFAULT); 
            // shouldn't goto the line below
            fail("H5F.H5Fopen(null, ...) should fail since the file name is null.");
        }
        catch (NullPointerException err) {}
        catch (Throwable err) {
            fail("H5F.H5Fopen: "+err);
        }
        
        fid = _openFile(H5_FILE, H5Fconstant.H5F_ACC_RDWR);
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
        
        fid = _openFile(H5_FILE, H5Fconstant.H5F_ACC_RDWR);
        
        try { fid2 = H5F.H5Freopen(fid); }
        catch (Throwable err) {
            fail("H5F.H5Freopen: "+err);
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
        int fid = _openFile(H5_FILE, H5Fconstant.H5F_ACC_RDWR);
        
        try { H5F.H5Fflush(fid, H5F_SCOPE.GLOBAL); }
        catch (Throwable err) {
            fail("H5F.H5Fflush: "+err);
        }
       
        try { H5F.H5Fflush(fid, H5F_SCOPE.LOCAL); }
        catch (Throwable err) {
            fail("H5F.H5Fflush: "+err);
        }
        
        try { H5F.H5Fclose(fid); } catch (Exception ex) {}
    }

    @Test
    public void testH5Fclose() {
        int fid = _openFile(H5_FILE, H5Fconstant.H5F_ACC_RDWR);
        
        try { H5F.H5Fclose(fid); } 
        catch (Throwable err) {
            fail("H5F.H5Fclose: "+err);
        }
        
        // it should fail since the file was closed.
        try { 
            H5F.H5Fclose(fid); 
            
            // shouldn't goto the line below
            fail("H5F.H5Fclose should fail since the file was closed.");
        } catch (Throwable err) {}
        
        // cannot close a file with negative id.
        try { 
            H5F.H5Fclose(-1); 
            
            // shouldn't goto the line below
            fail("H5F.H5Fclose cannot close a file with negative id.");
        } catch (Throwable err) {}

    }

    @Test
    public void testH5Fget_create_plist() {
        int fid = _openFile(H5_FILE, H5Fconstant.H5F_ACC_RDWR);
        int plist = -1;
        
        try { plist = H5F.H5Fget_create_plist(fid); }
        catch (Throwable err) {
            fail("H5F.H5Fget_create_plist: "+err);
        }
        assertTrue(plist > 0);
       
        try { H5F.H5Fclose(fid); } catch (Exception ex) {}
        
        // it should fail because the file was closed.
        try { plist = H5F.H5Fget_create_plist(fid); }
        catch (Throwable err) {
            plist = -1;
        }
        assertTrue(plist < 0);
     }

    @Test
    public void testH5Fget_access_plist() {
        int fid = _openFile(H5_FILE, H5Fconstant.H5F_ACC_RDWR);
        int plist = -1;
        
        try { plist = H5F.H5Fget_access_plist(fid); }
        catch (Throwable err) {
            fail("H5F.H5Fget_access_plist: "+err);
        }
        assertTrue(plist > 0);
       
        try { H5F.H5Fclose(fid); } catch (Exception ex) {}
        
        // it should fail because the file was closed.
        try { plist = H5F.H5Fget_access_plist(fid); }
        catch (Throwable err) {
            plist = -1;
        }
        assertTrue(plist < 0);
    }

    @Test
    public void testH5Fget_intent() {
        int fid = -1, intent = 0;
        
        fid = _openFile(H5_FILE, H5Fconstant.H5F_ACC_RDWR);
        try { intent = H5F.H5Fget_intent(fid); }
        catch (Throwable err) {
            fail("H5F.H5Fget_intent: "+err);
        }
        assertEquals(intent, H5Fconstant.H5F_ACC_RDWR);
        try { H5F.H5Fclose(fid); } catch (Exception ex) {}
        
        fid = _openFile(H5_FILE, H5Fconstant.H5F_ACC_RDONLY);
        try { intent = H5F.H5Fget_intent(fid); }
        catch (Throwable err) {
            fail("H5F.H5Fget_intent: "+err);
        }
        assertEquals(intent, H5Fconstant.H5F_ACC_RDONLY);
        try { H5F.H5Fclose(fid); } catch (Exception ex) {}
   }

    @Test
    public void testH5Fget_obj_count() {
        int fid = -1;
        long count = -1;

        fid = _openFile(H5_FILE, H5Fconstant.H5F_ACC_RDWR);

        for (int i=0; i<OBJ_TYPES.length; i++) {
            try { count = H5F.H5Fget_obj_count(fid, OBJ_TYPES[i]); }
            catch (Throwable err) {
                fail("H5F.H5Fget_obj_count: "+err);
            }
            assertEquals(count, OBJ_COUNTS[i]);
        }
        
        try { H5F.H5Fclose(fid); } catch (Exception ex) {}
    }

    @Test
    /**
     * TODO: need to add objects to the file after H5G[D,T]create() functions 
     * are implemented.
     */
    public void testH5Fget_obj_ids() {
        long count = 0;
        int max_objs = 100;
        int[] obj_id_list = new int[max_objs];
        int[] open_obj_counts = new int[OBJ_TYPES.length];

        for (int i=0; i<OBJ_TYPES.length; i++)
            open_obj_counts[i] = 0;
        
        open_obj_counts[0] = 1;
        for (int i=0; i<OBJ_TYPES.length-1; i++)
            open_obj_counts[OBJ_TYPES.length-1] += open_obj_counts[i];
       
        int fid = _openFile(H5_FILE, H5Fconstant.H5F_ACC_RDWR);
        for (int i=0; i<OBJ_TYPES.length; i++) {
            try { count = H5F.H5Fget_obj_ids(fid, OBJ_TYPES[i], max_objs, obj_id_list); }
            catch (Throwable err) {
                fail("H5F.H5Fget_obj_ids: "+err);
            }
            assertEquals(count, open_obj_counts[i]);
        }

        try { H5F.H5Fclose(fid); } catch (Exception ex) {}
    }

    @Test
    public void testH5Fmount_unmount() {
        String file1 = "src.h5";
        String file2 = "dst.h5";
        String group1 = "/G";
        String group2 = "/MOUNTED";
        
        _createH5File(file1);
        _createH5File(file2);
        
        int fid1 = _openFile(file1, H5Fconstant.H5F_ACC_RDWR);
        assertTrue(fid1 > 0);
        
        int fid2 = _openFile(file2, H5Fconstant.H5F_ACC_RDWR);
        assertTrue(fid2 > 0);
        
        // create a group at file1
        int gid = _createGroup(fid1, group1);
        try { H5G.H5Gclose(gid); } catch (Exception ex) {}
        
        // create a group at file 2
        gid = _createGroup(fid2, group2);
        try { H5G.H5Gclose(gid); } catch (Exception ex) {}        

        // before mount, "/G/MOUNTED" does not exists in file1
        gid = _openGroup(fid1, group1+group2);
        assertTrue(gid < 0);
        
        // Mount file2 under G in the file1
        try { H5F.H5Fmount(fid1, group1, fid2, DEFAULT); }
        catch (Throwable err) {
            fail("H5F.H5Fmount: "+err);
        }
        
        // now file1 should have group "/G/MOUNTED"
        gid = _openGroup(fid1, group1+group2);
        assertTrue(gid > 0);
        try { H5G.H5Gclose(gid); } catch (Exception ex) {}  
        
        // unmount file2 from file1
        try { H5F.H5Funmount(fid1, group1); }
        catch (Throwable err) {
            fail("H5F.H5Funmount: "+err);
        }
       
        // file2 was unmounted from file1, "/G/MOUNTED" does not exists in file1
        gid = _openGroup(fid1, group1+group2);
        assertTrue(gid < 0);        

        try { H5F.H5Fclose(fid1); } catch (Exception ex) {}
        try { H5F.H5Fclose(fid2); } catch (Exception ex) {}
        
        _deleteFile(file1);
        _deleteFile(file2);        
    }

    @Test
    public void testH5Fget_freespace() {
        long freeSpace = 0;
        int fid = _openFile(H5_FILE, H5Fconstant.H5F_ACC_RDWR);

        try { freeSpace = H5F.H5Fget_freespace(fid); } 
        catch (Throwable err) {
            fail("H5F.H5Fget_freespace: "+err);
        }
        assertEquals(freeSpace, 0);
        
        //TODO add/and delete objects and test freespace

        try { H5F.H5Fclose(fid); } catch (Exception ex) {}
    }

    @Test
    public void testH5Fget_filesize() {
        long fileSize = 0;
        int fid = _openFile(H5_FILE, H5Fconstant.H5F_ACC_RDWR);

        try { fileSize = H5F.H5Fget_filesize(fid); } 
        catch (Throwable err) {
            fail("H5.H5Fget_freespace: "+err);
        }
        assertTrue(fileSize > 0);
        
        //TODO add/and delete objects and test freespace

        try { H5F.H5Fclose(fid); } catch (Exception ex) {}
    }

    @Test
    public void testH5Fget_mdc_hit_rate() {
        double rate;
        int fid = _openFile(H5_FILE, H5Fconstant.H5F_ACC_RDWR);
        
        try { rate = H5F.H5Fget_mdc_hit_rate(fid); } 
        catch (Throwable err) {
            fail("H5F.H5Fget_mdc_hit_rate: "+err);
        }

        try { H5F.H5Fclose(fid); } catch (Exception ex) {}
    }

    @Test
    public void testH5Fget_mdc_size() {
        int nentries = -1;
        long cache_sizes[] = new long[3];
        int fid = _openFile(H5_FILE, H5Fconstant.H5F_ACC_RDWR);
        
        try { nentries = H5F.H5Fget_mdc_size(fid, cache_sizes); } 
        catch (Throwable err) {
            fail("H5F.H5Fget_mdc_size: "+err);
        }
        assertTrue(nentries == 0);
        
        //TODO: test more cases of different cache sizes.
      
        try { H5F.H5Fclose(fid); } catch (Exception ex) {}
    }

    @Test
    public void testH5Freset_mdc_hit_rate_stats() {
        int fid = _openFile(H5_FILE, H5Fconstant.H5F_ACC_RDWR);

        try { H5F.H5Freset_mdc_hit_rate_stats(fid); } 
        catch (Throwable err) {
            fail("H5F.H5Freset_mdc_hit_rate_stats: "+err);
        }

        try { H5F.H5Fclose(fid); } catch (Exception ex) {}
    }

    @Test
    public void testH5Fget_name() {
        int fid = -1;
        String fname = null;
        
        try { fid = H5F.H5Fopen(H5_FILE, H5Fconstant.H5F_ACC_RDWR, DEFAULT); }
        catch (Throwable err) {
            fail("H5F.H5Fopen: "+err);
        }
        assertTrue (fid > 0);
        
        try { 
            fname = H5F.H5Fget_name(fid); 
        } catch (Throwable err) {
            fail("H5F.H5Fget_name: "+err);
        }
        assertNotNull(fname);
        assertEquals(fname, H5_FILE);

        try { H5F.H5Fclose(fid); } catch (Exception ex) {}
    }
}
