package test.junit;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;

import ncsa.hdf.hdf5lib.H5;
import ncsa.hdf.hdf5lib.HDF5Constants;
import ncsa.hdf.hdf5lib.exceptions.HDF5LibraryException;
import ncsa.hdf.hdf5lib.structs.H5G_info_t;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class TestH5G {
  private static final boolean is16 = H5.isAPI16;
  private static final String H5_FILE = "test.h5";
  private static final String[] GROUPS = {"/G1", "/G1/G11", "/G1/G12", 
    "/G1/G11/G111", "/G1/G11/G112", "/G1/G11/G113", "/G1/G11/G114"};
  int H5fid = -1;

  private final int _createGroup(int fid, String name) {
    int gid = -1;
    try {
      if (is16)
        gid = H5.H5Gcreate(fid, name, 0);
      else
        gid = H5.H5Gcreate2(fid, name, HDF5Constants.H5P_DEFAULT, HDF5Constants.H5P_DEFAULT, HDF5Constants.H5P_DEFAULT);
    }
    catch (Throwable err) {
      err.printStackTrace();
      fail("H5.H5Gcreate: "+err);
    }

    return gid;
  }

  private final int _openGroup(int fid, String name) {
    int gid = -1;
    try {
      if (is16)
        gid = H5.H5Gopen(fid, name);
      else
        gid = H5.H5Gopen2(fid, name, HDF5Constants.H5P_DEFAULT);;
    } 
    catch (Throwable err) {
      gid = -1;
      err.printStackTrace();
      fail("H5.H5Gcreate: "+err);
    }

    return gid;
  }

  private final void _deleteFile(String filename)
  {
    File file = new File(filename);

    if (file.exists()) {
      try {
        file.delete();
      } 
      catch (SecurityException e) {
        ;//e.printStackTrace();
      }
    }
  }

  @Before
  public void createH5file() throws HDF5LibraryException, NullPointerException {
    H5fid = H5.H5Fcreate(H5_FILE, HDF5Constants.H5F_ACC_TRUNC, 
        HDF5Constants.H5P_DEFAULT, HDF5Constants.H5P_DEFAULT);

    int gid = -1;

    for (int i=0; i<GROUPS.length; i++) {
      gid = _createGroup(H5fid, GROUPS[i]);
      assertTrue(gid > 0);
      try { H5.H5Gclose(gid); } catch (Exception ex) {}
    }

    H5.H5Fflush(H5fid, HDF5Constants.H5F_SCOPE_LOCAL);
  }

  @After
  public void deleteH5file() throws HDF5LibraryException {
    if(H5fid > 0) {
      H5.H5Fclose(H5fid);
    }
    _deleteFile(H5_FILE);
  }

  @Test
  public void testH5Gopen() {
    for (int i=0; i<GROUPS.length; i++) {
      int gid = _openGroup(H5fid, GROUPS[i]);
      assertTrue(gid>0);
      try { H5.H5Gclose(gid); } catch (Exception ex) {}
    }
  }

  @Test
  public void testH5Gget_create_plist() {
    int gid=-1;
    int pid=-1;

    for (int i=0; i<GROUPS.length; i++) {
      gid = _openGroup(H5fid, GROUPS[i]);
      assertTrue(gid>0);

      try { 
        pid = H5.H5Gget_create_plist(gid); 
      }
      catch (Throwable err) {
        err.printStackTrace();
        fail("H5.H5Gget_create_plist: "+err);
      }
      assertTrue(pid>0);

      try { H5.H5Gclose(gid); } catch (Exception ex) {}
    }
  }

  @Test
  public void testH5Gget_info() {
    H5G_info_t info = null;

    for (int i=0; i<GROUPS.length; i++) {

      try { 
        info = H5.H5Gget_info(H5fid); 
      } 
      catch (Throwable err) {
        err.printStackTrace();
        fail("H5.H5Gget_info: "+err);
      }
      assertNotNull(info);
    }
  }

  @Test
  public void testH5Gget_info_by_name() {
    H5G_info_t info = null;

    for (int i=0; i<GROUPS.length; i++) {
      try { 
        info = H5.H5Gget_info_by_name(H5fid, GROUPS[i], HDF5Constants.H5P_DEFAULT); 
      } 
      catch (Throwable err) {
        err.printStackTrace();
        fail("H5.H5Gget_info_by_name: "+err);
      }
      assertNotNull(info);
    }
  }

  @Test
  public void testH5Gget_info_by_idx() {
    H5G_info_t info = null;
    for (int i=0; i<2; i++) {
      try { 
        info = H5.H5Gget_info_by_idx(H5fid, "/G1", HDF5Constants.H5_INDEX_NAME, 
            HDF5Constants.H5_ITER_INC, i, HDF5Constants.H5P_DEFAULT); 
      } 
      catch (Throwable err) {
        err.printStackTrace();
        fail("H5.H5Gget_info_by_idx: "+err);
      }
      assertNotNull(info);
    }
  }

  @Ignore("Not yet Implemented")
  public void testH5Gget_obj_info_all() {
    H5G_info_t info = null;

    int gid = _openGroup(H5fid, GROUPS[0]);

    try { 
      info = H5.H5Gget_info(gid); 
    } 
    catch (Throwable err) {
      err.printStackTrace();
      fail("H5.H5Gget_info: "+err);
    }
    try { H5.H5Gclose(gid); } catch (Exception ex) {}
    assertNotNull(info);
    assertTrue(info.nlinks>0);
    String objNames[] = new String[(int)info.nlinks];
    int objTypes[] = new int[(int)info.nlinks];

//    try { 
//      H5.H5Gget_obj_info_all2(H5fid, GROUPS[0], objNames, objTypes);
//    } 
//    catch (Throwable err) {
//      err.printStackTrace();
//      fail("H5.H5Gget_obj_info_all: "+err);
//    }
//
//    for (int i=0; i<objNames.length; i++) {
//      assertNotNull(objNames[i]);
//      assertTrue(objNames[i].length()>0);
//    }
  }

}
