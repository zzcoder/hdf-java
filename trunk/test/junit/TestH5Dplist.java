package test.junit;

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

public class TestH5D {
    private static final String H5_FILE = "test.h5";
    private static final int DIM_X = 4;
    private static final int DIM_Y = 6;
    int H5fid = -1;
    int H5dsid = -1;
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
        assertTrue("TestH5D._createDataset: ",did > 0);

        return did;
    }

    private final int _openDataset(int fid, String name) {
        int did = -1;
        try {
            did = H5.H5Dopen(fid, name, HDF5Constants.H5P_DEFAULT);
        }
        catch (Throwable err) {
            did = -1;
            err.printStackTrace();
            fail("H5.H5Dopen: " + err);
        }
        assertTrue("TestH5D._openDataset: ",did > 0);

        return did;
    }

    @Before
    public void createH5file()
            throws NullPointerException, HDF5Exception {
        try {
            H5fid = H5.H5Fcreate(H5_FILE, HDF5Constants.H5F_ACC_TRUNC,
                    HDF5Constants.H5P_DEFAULT, HDF5Constants.H5P_DEFAULT);
            H5dsid = H5.H5Screate_simple(2, H5dims, null);
        }
        catch (Throwable err) {
            err.printStackTrace();
            fail("TestH5D.createH5file: " + err);
        }
        assertTrue("TestH5D.createH5file: H5.H5Fcreate: ",H5fid > 0);
        assertTrue("TestH5D.createH5file: H5.H5Screate_simple: ",H5dsid > 0);

        H5.H5Fflush(H5fid, HDF5Constants.H5F_SCOPE_LOCAL);
    }

    @After
    public void deleteH5file() throws HDF5LibraryException {
        if (H5dsid > 0) 
            H5.H5Sclose(H5dsid);
        if (H5fid > 0) 
            H5.H5Fclose(H5fid);

        _deleteFile(H5_FILE);
    }

    @Test
    public void testH5Dget_access_plist() throws Throwable, HDF5LibraryException {
        int dapl_id = -1;
        long nlinks = -1;
        int test_dapl_id = H5.H5Pcreate(HDF5Constants.H5P_DATASET_ACCESS);
        H5.H5Pset_nlinks(test_dapl_id, 134);
        nlinks = H5.H5Pget_nlinks(test_dapl_id);
        assertTrue("testH5Dget_access_plist: nlinks: ", nlinks == 134);
        
        int dataset_id = _createDataset(H5fid, H5dsid, "dset", test_dapl_id);
        
        try {
            dapl_id = H5.H5Dget_access_plist(dataset_id);
        }
        catch (Exception err) {
            err.printStackTrace();
            fail("testH5Dget_access_plist: H5.H5Dget_access_plist: " + err);
        }
        assertTrue("testH5Dget_access_plist: dapl_id: ", dapl_id > 0);
        assertTrue("testH5Dget_access_plist: ", H5.H5Pequal(dapl_id, test_dapl_id) > 0);

        // End access to the dataset and release resources used by it.
        try {
            if (dapl_id >= 0)
                H5.H5Pclose(dapl_id);
            if (dataset_id >= 0)
                H5.H5Dclose(dataset_id);
        }
        catch (Exception err) {
            err.printStackTrace();
        }
    }
    
//  @Test
//  public void testH5Dset_extent() throws Throwable, HDF5LibraryException {
//      int dapl_id = -1;
//      int test_dapl_id = H5.H5Pcreate(HDF5Constants.H5P_DATASET_ACCESS);
//      H5.H5Pset_nlinks(dapl1, 134);
//      int dataset_id = _createDataset(H5fid, H5dsid, "dset", test_dapl_id);
//      
//      try {
//          dapl_id = H5.H5Dset_extent(dataset_id);
//      }
//      catch (Exception err) {
//          err.printStackTrace();
//          fail("testH5Dset_extent: H5.H5Dget_access_plist: " + err);
//      }
//      assertTrue("testH5Dset_extent: dapl_id: ", dapl_id > 0);
////      assertTrue("testH5Dset_extent: ", H5.H5Pequal(dapl_id, HDF5Constants.H5P_DEFAULT) > 0);
//
//      // End access to the dataset and release resources used by it.
//      try {
//          if (dapl_id >= 0)
//              H5.H5Pclose(dapl_id);
//          if (dataset_id >= 0)
//              H5.H5Dclose(dataset_id);
//      }
//      catch (Exception err) {
//          err.printStackTrace();
//      }
//  }

}
