package test.hdf5lib;

import static org.junit.Assert.assertFalse;
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
    private static final int RANK = 2;
    int H5fid = -1;
    int H5dsid = -1;
    int H5did = -1;
    int H5did0 = -1;
    int H5dcpl_id = -1;
    long[] H5dims = { DIM_X, DIM_Y };

    // Values for the status of space allocation
    enum H5D_space_status {
        H5D_SPACE_STATUS_ERROR(-1), H5D_SPACE_STATUS_NOT_ALLOCATED(0), H5D_SPACE_STATUS_PART_ALLOCATED(
                1), H5D_SPACE_STATUS_ALLOCATED(2);

        private int code;

        H5D_space_status(int space_status) {
            this.code = space_status;
        }

        public int getCode() {
            return this.code;
        }
    }

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

    private final void _createPDataset(int fid, int dsid, String name, int dcpl_val) {
        
        try {
            H5dcpl_id = H5.H5Pcreate(dcpl_val);
        }
        catch (Exception err) {
            err.printStackTrace();
            fail("H5.H5Pcreate: " + err);
        }
        assertTrue("testH5D._createPDataset: H5.H5Pcreate: ", H5dcpl_id > 0);

        // Set the allocation time to "early". This way we can be sure
        // that reading from the dataset immediately after creation will
        // return the fill value.
        try {
            H5.H5Pset_alloc_time(H5dcpl_id, HDF5Constants.H5D_ALLOC_TIME_EARLY);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        try {
            H5did0 = H5.H5Dcreate(fid, name,
                        HDF5Constants.H5T_STD_I32BE, dsid,
                        HDF5Constants.H5P_DEFAULT, H5dcpl_id, HDF5Constants.H5P_DEFAULT);
        }
        catch (Throwable err) {
            err.printStackTrace();
            fail("H5.H5Dcreate: " + err);
        }
        assertTrue("TestH5D._createPDataset: ", H5did0 > 0);
    }

    private final void _createDataset(int fid, int dsid, String name, int dapl) {
        try {
            H5did = H5.H5Dcreate(fid, name,
                        HDF5Constants.H5T_STD_I32BE, dsid,
                        HDF5Constants.H5P_DEFAULT, HDF5Constants.H5P_DEFAULT, dapl);
        }
        catch (Throwable err) {
            err.printStackTrace();
            fail("H5.H5Dcreate: " + err);
        }
        assertTrue("TestH5D._createPDataset: ", H5did > 0);
    }

//    private final int _openDataset(int fid, String name) {
//       int did = -1;
//       try {
//           did = H5.H5Dopen(fid, name, HDF5Constants.H5P_DEFAULT);
//      }
//       catch (Throwable err) {
//           did = -1;
//           err.printStackTrace();
//           fail("H5.H5Dopen: " + err);
//       }
//       assertTrue("TestH5D._openDataset: ",did > 0);
//       return did;
//    }

    @Before
    public void createH5file()
            throws NullPointerException, HDF5Exception {
       assertTrue("H5 open ids is 0",H5.getOpenIDCount()==0);

        try {
            H5fid = H5.H5Fcreate(H5_FILE, HDF5Constants.H5F_ACC_TRUNC,
                    HDF5Constants.H5P_DEFAULT, HDF5Constants.H5P_DEFAULT);
            H5dsid = H5.H5Screate_simple(RANK, H5dims, null);
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
        if (H5dcpl_id >= 0)
            H5.H5Pclose(H5dcpl_id);
        if (H5did0 >= 0)
            H5.H5Dclose(H5did0);
        if (H5did >= 0)
            H5.H5Dclose(H5did);
        if (H5dsid > 0) 
            H5.H5Sclose(H5dsid);
        if (H5fid > 0) 
            H5.H5Fclose(H5fid);

        _deleteFile(H5_FILE);
    }

    @Test
    public void testH5Dcreate() throws Throwable, HDF5LibraryException {
        int dataset_id = -1;
        try {
            dataset_id = H5.H5Dcreate(H5fid, "dset",
                HDF5Constants.H5T_STD_I32BE, H5dsid,
                HDF5Constants.H5P_DEFAULT, HDF5Constants.H5P_DEFAULT, HDF5Constants.H5P_DEFAULT);
        }
        catch (Exception err) {
            err.printStackTrace();
            fail("testH5Dcreate: " + err);
        }
        assertTrue(dataset_id > 0);

        // End access to the dataset and release resources used by it.
        try {
            if (dataset_id >= 0)
                H5.H5Dclose(dataset_id);
        }
        catch (Exception err) {
            err.printStackTrace();
        }
    }

    @Test
    public void testH5Dcreate_anon() throws Throwable, HDF5LibraryException {
        int dataset_id = -1;
        try {
            dataset_id = H5.H5Dcreate_anon(H5fid, HDF5Constants.H5T_STD_I32BE, 
                    H5dsid, HDF5Constants.H5P_DEFAULT, HDF5Constants.H5P_DEFAULT);
        }
        catch (Exception err) {
            err.printStackTrace();
            fail("testH5Dcreate_anon: " + err);
        }
        assertTrue(dataset_id > 0);

        // End access to the dataset and release resources used by it.
        try {
            if (dataset_id >= 0)
                H5.H5Dclose(dataset_id);
        }
        catch (Exception err) {
            err.printStackTrace();
        }
    }

    @Test
    public void testH5Dopen() throws Throwable, HDF5LibraryException {
        int dataset_id = -1;
        _createDataset(H5fid, H5dsid, "dset", HDF5Constants.H5P_DEFAULT);
        H5.H5Dclose(H5did);
        H5did = -1;
        
        try {
            dataset_id = H5.H5Dopen(H5fid, "dset", HDF5Constants.H5P_DEFAULT);
        }
        catch (Exception err) {
            err.printStackTrace();
            fail("testH5Dopen: " + err);
        }
        assertTrue("testH5Dopen: ", dataset_id > 0);

        // End access to the dataset and release resources used by it.
        try {
            if (dataset_id >= 0)
                H5.H5Dclose(dataset_id);
        }
        catch (Exception err) {
            err.printStackTrace();
        }
    }

    @Test
    public void testH5Dget_storage_size_empty() throws Throwable, HDF5LibraryException {
        long storage_size = 0;
        _createDataset(H5fid, H5dsid, "dset", HDF5Constants.H5P_DEFAULT);
        
        try {
            storage_size = H5.H5Dget_storage_size(H5did);
        }
        catch (Exception err) {
            err.printStackTrace();
            fail("testH5Dget_storage_size: " + err);
        }
        assertTrue("testH5Dget_storage_size: ", storage_size == 0);
    }

    @Test
    public void testH5Dget_storage_size() throws Throwable, HDF5LibraryException {
        long storage_size = 0;
        int[][] dset_data = new int[DIM_X][DIM_Y];
        int FILLVAL = 99;
        _createDataset(H5fid, H5dsid, "dset", HDF5Constants.H5P_DEFAULT);

        // Initialize the dataset.
        for (int indx = 0; indx < DIM_X; indx++)
            for (int jndx = 0; jndx < DIM_Y; jndx++)
                dset_data[indx][jndx] = FILLVAL;
 
        try {
            if (H5did >= 0)
                H5.H5Dwrite(H5did, HDF5Constants.H5T_NATIVE_INT,
                        HDF5Constants.H5S_ALL, HDF5Constants.H5S_ALL,
                        HDF5Constants.H5P_DEFAULT, dset_data[0]);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        
        try {
            storage_size = H5.H5Dget_storage_size(H5did);
        }
        catch (Exception err) {
            err.printStackTrace();
            fail("testH5Dget_storage_size: " + err);
        }
        assertTrue("testH5Dget_storage_size: "+storage_size, storage_size == DIM_X*DIM_Y*4);
    }

    @Test
    public void testH5Dget_access_plist() throws Throwable, HDF5LibraryException {
        int dapl_id = -1;
        int pequal = -1;
        int test_dapl_id = -1;
        
        try {
            test_dapl_id = H5.H5Pcreate(HDF5Constants.H5P_DATASET_ACCESS);
        }
        catch (Exception err) {
            err.printStackTrace();
            fail("testH5Dget_access_plist: H5.H5Pcreate: " + err);
        }
        assertTrue("testH5Dget_access_plist: test_dapl_id: ", test_dapl_id > 0);
       
        _createDataset(H5fid, H5dsid, "dset", test_dapl_id);
        
        try {
            dapl_id = H5.H5Dget_access_plist(H5did);
        }
        catch (Exception err) {
            err.printStackTrace();
            fail("testH5Dget_access_plist: H5.H5Dget_access_plist: " + err);
        }
        assertTrue("testH5Dget_access_plist: dapl_id: ", dapl_id > 0);
        pequal = H5.H5Pequal(dapl_id, test_dapl_id);

        // End access to the dataset and release resources used by it.
        try {
            if (dapl_id >= 0)
                H5.H5Pclose(dapl_id);
        }
        catch (Exception err) {
            err.printStackTrace();
        }
        try {
            if (test_dapl_id >= 0)
                H5.H5Pclose(test_dapl_id);
        }
        catch (Exception err) {
            err.printStackTrace();
        }
        assertTrue("testH5Dget_access_plist: ", pequal > 0);
    }
    
    @Test
    public void testH5Dget_space_status() throws Throwable, HDF5LibraryException {
        int[][] write_dset_data = new int[DIM_X][DIM_Y];
        int[] space_status = new int[1];
        int[] space_status0 = new int[1];

        // Initialize the dataset.
        for (int indx = 0; indx < DIM_X; indx++)
            for (int jndx = 0; jndx < DIM_Y; jndx++)
                write_dset_data[indx][jndx] = indx * jndx - jndx;

        _createPDataset(H5fid, H5dsid, "dset0", HDF5Constants.H5P_DATASET_CREATE);
        _createDataset(H5fid, H5dsid, "dset", HDF5Constants.H5P_DEFAULT);

        // Retrieve and print space status and storage size for dset0.
        try {
            H5.H5Dget_space_status(H5did0, space_status0);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        assertTrue("testH5Dget_space_status0 - H5.H5Dget_space_status: ", space_status0[0] == H5D_space_status.H5D_SPACE_STATUS_ALLOCATED.getCode());

        // Retrieve and print space status and storage size for dset.
        try {
            H5.H5Dget_space_status(H5did, space_status);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        assertFalse("testH5Dget_space_status - H5.H5Dget_space_status: ", space_status[0] == H5D_space_status.H5D_SPACE_STATUS_ALLOCATED.getCode());

        // Write the data to the dataset.
        try {
            H5.H5Dwrite(H5did, HDF5Constants.H5T_NATIVE_INT,
                        HDF5Constants.H5S_ALL, HDF5Constants.H5S_ALL,
                        HDF5Constants.H5P_DEFAULT, write_dset_data);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        // Retrieve and print space status and storage size for dset.
        try {
            H5.H5Dget_space_status(H5did, space_status);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        assertTrue("testH5Dget_space_status - H5.H5Dget_space_status: ", space_status[0] == H5D_space_status.H5D_SPACE_STATUS_ALLOCATED.getCode());
    }

    @Test(expected = HDF5LibraryException.class)
    public void testH5Dget_space_closed() throws Throwable, HDF5LibraryException {
        int dataset_id = -1;
        try {
            dataset_id = H5.H5Dcreate(H5fid, "dset",
                        HDF5Constants.H5T_STD_I32BE, H5dsid,
                        HDF5Constants.H5P_DEFAULT, HDF5Constants.H5P_DEFAULT, HDF5Constants.H5P_DEFAULT);
        }
        catch (Throwable err) {
            err.printStackTrace();
            fail("H5.H5Dcreate: " + err);
        }
        assertTrue("TestH5D.testH5Dget_space_closed: ", dataset_id > 0);
        H5.H5Dclose(dataset_id);
        
        H5.H5Dget_space(dataset_id);
    }

    @Test
    public void testH5Dget_space() throws Throwable, HDF5LibraryException {
        int dataspace_id = -1;
        _createDataset(H5fid, H5dsid, "dset", HDF5Constants.H5P_DEFAULT);
        
        try {
            dataspace_id = H5.H5Dget_space(H5did);
        }
        catch (Exception err) {
            err.printStackTrace();
            fail("H5.H5Dget_space: " + err);
        }
        assertTrue("TestH5D.testH5Dget_space: ", dataspace_id > 0);

        // End access to the dataspace and release resources used by it.
        try {
            if (dataspace_id >= 0)
                H5.H5Sclose(dataspace_id);
        }
        catch (Exception err) {
            err.printStackTrace();
        }
    }

    @Test(expected = HDF5LibraryException.class)
    public void testH5Dget_type_closed() throws Throwable, HDF5LibraryException {
        int dataset_id = -1;
        try {
            dataset_id = H5.H5Dcreate(H5fid, "dset",
                        HDF5Constants.H5T_STD_I32BE, H5dsid,
                        HDF5Constants.H5P_DEFAULT, HDF5Constants.H5P_DEFAULT, HDF5Constants.H5P_DEFAULT);
        }
        catch (Throwable err) {
            err.printStackTrace();
            fail("H5.H5Dcreate: " + err);
        }
        assertTrue("TestH5D.testH5Dget_type_closed: ", dataset_id > 0);
        H5.H5Dclose(dataset_id);
        
        H5.H5Dget_type(dataset_id);
    }

    @Test
    public void testH5Dget_type() throws Throwable, HDF5LibraryException {
        int datatype_id = -1;
        _createDataset(H5fid, H5dsid, "dset", HDF5Constants.H5P_DEFAULT);
        
        try {
            datatype_id = H5.H5Dget_type(H5did);
        }
        catch (Exception err) {
            err.printStackTrace();
            fail("H5.H5Dget_type: " + err);
        }
        assertTrue("TestH5D.testH5Dget_type: ", datatype_id > 0);

        // End access to the datatype and release resources used by it.
        try {
            if (datatype_id >= 0)
                H5.H5Tclose(datatype_id);
        }
        catch (Exception err) {
            err.printStackTrace();
        }
    }

}
