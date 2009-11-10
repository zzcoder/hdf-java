package test.junit;

import java.io.File;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import ncsa.hdf.hdf5lib.H5;
import ncsa.hdf.hdf5lib.exceptions.HDF5LibraryException;
import ncsa.hdf.hdf5lib.structs.*;
import ncsa.hdf.hdf5lib.HDF5Constants;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestH5G {
    private static final boolean is16 = H5.isAPI16;
    private static final int DEFAULT = HDF5Constants.H5P_DEFAULT;
    private static final String H5_FILE = "test.h5";
    private static final String[] GROUPS = {"/G1", "/G1/G11", "/G1/G12", 
        "/G1/G11/G111", "/G1/G11/G112", "/G1/G11/G113", "/G1/G11/G114"};
    int H5fid = -1;
//
//    private final void _deleteFile(String filename)
//    {
//      File file = new File(filename);
//
//      if (file.exists()) {
//        try {
//          file.delete();
//        } 
//        catch (SecurityException e) {
//          ;//e.printStackTrace();
//        }
//      }
//    }
//
//    @Before
//    public void createH5file() throws HDF5LibraryException, NullPointerException {
//      H5fid = H5.H5Fcreate(H5_FILE, HDF5Constants.H5F_ACC_TRUNC, 
//          HDF5Constants.H5P_DEFAULT, HDF5Constants.H5P_DEFAULT);
//      
//      int gid = -1;
//      
//      for (int i=0; i<GROUPS.length; i++) {
//          gid = _createGroup(H5fid, GROUPS[i]);
//          assertTrue(gid > 0);
//          try { H5.H5Gclose(gid); } catch (Exception ex) {}
//      }
//
//      H5.H5Fflush(H5fid, HDF5Constants.H5F_SCOPE_LOCAL);
//    }
//
//    @After
//    public void deleteH5file() throws HDF5LibraryException {
//      if(H5fid > 0) {
//        H5.H5Fclose(H5fid);
//      }
//      _deleteFile(H5_FILE);
//    }
//    
//    private final int _createGroup(int fid, String name) {
//        int gid = -1;
//        try {
//            if (is16)
//                gid = H5.H5Gcreate(fid, name, 0);
//            else
//                gid = H5.H5Gcreate2(fid, name, DEFAULT, DEFAULT, DEFAULT);
//        }
//        catch (Throwable err) {
//            gid = -1;
//        }
//         
//        return gid;
//    }
//    
//    private final int _openGroup(int fid, String name) {
//        int gid = -1;
//        try {
//            if (is16)
//                gid = H5.H5Gopen(fid, name);
//            else
//                gid = H5.H5Gopen2(fid, name, DEFAULT);;
//        } catch (Throwable err) {
//            gid = -1;
//        }
//        
//        return gid;
//    }
//
//    @Test
//    public void testH5Gcreate() {
//      int fid = -1;
//
//      try { 
//        fid = H5.H5Fopen(H5_FILE, HDF5Constants.H5F_ACC_RDWR, HDF5Constants.H5P_DEFAULT); 
//      }
//      catch (Throwable err) {
//        fail("H5.H5Fopen: "+err);
//      }
//        assertTrue(fid > 0);
//        
//        int gid = _createGroup(fid, "/testH5Gcreate");
//        assertTrue(gid > 0);
//        
//        try { H5.H5Gclose(gid); } catch (Exception ex) {}
//     
//        // it should failed now because the group already exists in file
//        gid = _createGroup(fid, "/testH5Gcreate");
//        assertTrue(gid < 0);
//       
//        try { H5.H5Fclose(fid); } catch (Exception ex) {}
//    }
//
//    @Test
//    public void testH5Gcreate_anon() {
//        int fid = _openFile(H5_FILE, HDF5Constants.H5F_ACC_RDWR);
//        assertTrue(fid > 0);
//        
//        int gid = -1;
//        try {
//            gid = H5.H5Gcreate_anon(fid, DEFAULT, DEFAULT);
//        } catch (Throwable err) {
//            try { H5.H5Fclose(fid); } catch (Exception ex) {}
//            fail("H5.H5Gcreate_anon: "+err);
//        }
//        assertTrue(gid > 0);
//        
//        try { H5.H5Gclose(gid); } catch (Exception ex) {}
//        try { H5.H5Fclose(fid); } catch (Exception ex) {}
//    }
//
//    @Test
//    public void testH5Gopen() {
//        int fid = _openFile(H5_FILE, HDF5Constants.H5F_ACC_RDWR);
//
//        for (int i=0; i<GROUPS.length; i++) {
//            int gid = _openGroup(fid, GROUPS[i]);
//            assertTrue(gid>0);
//            try { H5.H5Gclose(gid); } catch (Exception ex) {}
//        }
//        
//        try { H5.H5Fclose(fid); } catch (Exception ex) {}
//    }
//
//    @Test
//    public void testH5Gget_create_plist() {
//        int gid=-1, pid=-1;
//        int fid = _openFile(H5_FILE, HDF5Constants.H5F_ACC_RDWR);
//
//        for (int i=0; i<GROUPS.length; i++) {
//            gid = _openGroup(fid, GROUPS[i]);
//            assertTrue(gid>0);
//            
//            try { pid = H5.H5Gget_create_plist(gid); }
//            catch (Throwable err) {
//                try { H5.H5Fclose(fid); } catch (Exception ex) {}
//                fail("H5.H5Gget_create_plist: "+err);
//            }
//            assertTrue(pid>0);
//            
//            //try { H5P.H5Pclose(pid); } catch (Exception ex) {}
//            try { H5.H5Gclose(gid); } catch (Exception ex) {}
//        }
//        
//        try { H5.H5Fclose(fid); } catch (Exception ex) {}
//    }
//
//    @Test
//    public void testH5Gget_info() {
//        H5G_info_t info = null;
//        
//        int fid = _openFile(H5_FILE, HDF5Constants.H5F_ACC_RDWR);
//        int gid = _openGroup(fid, GROUPS[0]);
//
//        try { info = H5.H5Gget_info(gid); } 
//        catch (Throwable err) {
//            try { H5.H5Fclose(fid); } catch (Exception ex) {}
//            fail("H5.H5Gget_info: "+err);
//        }
//        assertNotNull(info);
//        
//        //TODO add more cases after H5P is implemented.
//
//        try { H5.H5Gclose(gid); } catch (Exception ex) {}
//        try { H5.H5Fclose(fid); } catch (Exception ex) {}
//    }
//
//    @Test
//    public void testH5Gget_info_by_name() {
//        H5G_info_t info = null;
//        
//        int fid = _openFile(H5_FILE, HDF5Constants.H5F_ACC_RDWR);
//
//        try { info = H5.H5Gget_info_by_name(fid, GROUPS[0], DEFAULT); } 
//        catch (Throwable err) {
//            fail("H5.H5Gget_info_by_name: "+err);
//        }
//        assertNotNull(info);
//        
//        //TODO add more cases after H5P is implemented.
//
//        try { H5.H5Fclose(fid); } catch (Exception ex) {}
//    }
//
//    @Test
//    public void testH5Gget_info_by_idx() {
//        H5G_info_t info = null;
//        
//        int fid = _openFile(H5_FILE, HDF5Constants.H5F_ACC_RDWR);
//
//        try { info = H5.H5Gget_info_by_idx(fid, GROUPS[0], HDF5Constants.H5_INDEX_NAME, HDF5Constants.H5_ITER_INC, 1, DEFAULT); } 
//        catch (Throwable err) {
//            try { H5.H5Fclose(fid); } catch (Exception ex) {}
//            fail("H5.H5Gget_info_by_idx: "+err);
//        }
//        assertNotNull(info);
//        
//        //TODO add more cases after H5P is implemented.
//
//        try { H5.H5Fclose(fid); } catch (Exception ex) {}
//    }
//
//    @Test
//    public void testH5Gclose() {
//        int gid=-1;
//        int fid = _openFile(H5_FILE, HDF5Constants.H5F_ACC_RDWR);
//
//        for (int i=0; i<GROUPS.length; i++) {
//            gid = _openGroup(fid, GROUPS[i]);
//            assertTrue(gid>0);
//            
//            try { H5.H5Gclose(gid); }
//            catch (Throwable err) {
//                try { H5.H5Fclose(fid); } catch (Exception ex) {}
//                fail("H5Gclose: "+err);
//            }
//        }
//        
//        try { H5.H5Fclose(fid); } catch (Exception ex) {}
//    }
//
//    @Test
//    public void testH5Gget_obj_info_all() {
//        H5G_info_t info = null;
//        
//        int fid = _openFile(H5_FILE, HDF5Constants.H5F_ACC_RDWR);
//        int gid = _openGroup(fid, GROUPS[0]);
//
//        try { info = H5.H5Gget_info(gid); } 
//        catch (Throwable err) {
//            fail("H5.H5Gget_info: "+err);
//        }
//        try { H5.H5Gclose(gid); } catch (Exception ex) {}
//        assertNotNull(info);
//        assertTrue(info.nlinks>0);
//        String objNames[] = new String[(int)info.nlinks];
//        int objTypes[] = new int[(int)info.nlinks];
//        
//        try { H5.H5Gget_obj_info_all(fid, GROUPS[0], objNames, objTypes); } 
//        catch (Throwable err) {
//            fail("H5.H5Gget_obj_info_all: "+err);
//        }
//        
//        for (int i=0; i<objNames.length; i++) {
//            assertNotNull(objNames[i]);
//            assertTrue(objNames[i].length()>0);
//        }
//        
//        //TODO add more cases after H5P is implemented.
//
//        try { H5.H5Fclose(fid); } catch (Exception ex) {}
//    }

}
