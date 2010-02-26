package test.hdf5lib;

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

public class TestH5A {
	private static final String H5_FILE = "test.h5";
	private static final int DIM_X = 4;
	private static final int DIM_Y = 6;
	int H5fid = -1;
	int H5dsid = -1;
	int H5did = -1;
	long[] H5dims = { DIM_X, DIM_Y };

	private final void _deleteFile(String filename) {
		File file = new File(filename);

		if (file.exists()) {
			try {
				file.delete();
			} catch (SecurityException e) {
				;// e.printStackTrace();
			}
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

	@Before
	public void createH5file() throws NullPointerException, HDF5Exception {
		assertTrue("H5 open ids is 0", H5.getOpenIDCount() == 0);

		try {
			H5fid = H5.H5Fcreate(H5_FILE, HDF5Constants.H5F_ACC_TRUNC,
					HDF5Constants.H5P_DEFAULT, HDF5Constants.H5P_DEFAULT);
			H5dsid = H5.H5Screate_simple(2, H5dims, null);
			H5did = _createDataset(H5fid, H5dsid, "dset",
					HDF5Constants.H5P_DEFAULT);
		} catch (Throwable err) {
			err.printStackTrace();
			fail("TestH5D.createH5file: " + err);
		}
		assertTrue("TestH5D.createH5file: H5.H5Fcreate: ", H5fid > 0);
		assertTrue("TestH5D.createH5file: H5.H5Screate_simple: ", H5dsid > 0);
		assertTrue("TestH5D.createH5file: _createDataset: ", H5did > 0);

		H5.H5Fflush(H5fid, HDF5Constants.H5F_SCOPE_LOCAL);
	}

	@After
	public void deleteH5file() throws HDF5LibraryException {
		if (H5dsid > 0)
			H5.H5Sclose(H5dsid);
		if (H5did > 0)
			H5.H5Dclose(H5did);
		if (H5fid > 0)
			H5.H5Fclose(H5fid);

		_deleteFile(H5_FILE);
	}

	@Test
	public void testH5Acreate2() throws Throwable, HDF5LibraryException {

		int atrr_id = -1;
		int loc_id = H5did;
		String attr_name = "dset";
		int type_id = -1;
		int space_id = -1;
		int acpl_id = HDF5Constants.H5P_DEFAULT;
		int aapl_id = HDF5Constants.H5P_DEFAULT;

		try {
			type_id = H5.H5Tcreate(HDF5Constants.H5T_ENUM, (long) 1);
			space_id = H5.H5Screate(HDF5Constants.H5S_NULL);
		} catch (Throwable err) {
			err.printStackTrace();
			fail("H5.H5Tcreate: " + err);
			fail("H5.H5Screate: " + err);
		}

		try {
			atrr_id = H5.H5Acreate2(loc_id, attr_name, type_id, space_id,
					acpl_id, aapl_id);

			// Check the value of attribute id returned from H5Acreate2.If it is negative then test fails.
			assertTrue("testH5Acreate2: H5Acreate2", atrr_id >= 0);
		} catch (Throwable err) {
			err.printStackTrace();
			fail("H5.H5Acreate2: " + err);
		}

		// Negative Test - Error should be thrown when H5Acreate2 is called for an invalid loc_id
		try {
			loc_id = H5dsid;
			H5.H5Acreate2(loc_id, attr_name, type_id, space_id, acpl_id, aapl_id);
			fail("Negative Test Failed:- Error not Thrown when location is not valid for an attribute.");
		} catch (AssertionError err) {
			fail("H5.H5Acreate2: " + err);
		} catch (HDF5LibraryException err) {
		}

		finally {
			H5.H5Tclose(type_id);
			H5.H5Sclose(space_id);
			H5.H5Aclose(atrr_id);
		}

		// Negative test- Error should be thrown when H5Acreate2 is called when type_id and space_id are closed
		try {
			H5.H5Acreate2(loc_id, attr_name, type_id, space_id, acpl_id, aapl_id);
			fail("Negative Test Failed:- Error not Thrown when IDs are closed.");
		} catch (AssertionError err) {
			fail("H5.H5Acreate2: " + err);
		} catch (HDF5LibraryException err) {
		}

	}

	@Test
	public void testH5Aopen() throws Throwable, HDF5LibraryException {

	    int attribute_id = -1;
		int obj_id = H5did;
		String attr_name = "dset";
		int type_id = -1;
		int space_id = -1;
		int atrr_id = -1;
		int acpl_id = HDF5Constants.H5P_DEFAULT;
		int aapl_id = HDF5Constants.H5P_DEFAULT;

		try {
			type_id  = H5.H5Tcreate(HDF5Constants.H5T_ENUM, (long) 1);
			space_id = H5.H5Screate(HDF5Constants.H5S_NULL);
			atrr_id  = H5.H5Acreate2(obj_id, attr_name, type_id, space_id, acpl_id, aapl_id);
		} catch (Throwable err) {
			err.printStackTrace();
			fail("H5.H5Tcreate: " + err);
			fail("H5.H5Screate: " + err);
			fail("H5.H5Acreate2: " + err);
		}

	    try {
			// Opening the existing attribute, attr_name(Created by H5ACreate2) attached to an object identifier.
			attribute_id = H5.H5Aopen(obj_id, attr_name, aapl_id);

			// Check the value of attribute id returned from H5Aopen.If it is negative then test fails.
			assertTrue("testH5Aopen: H5Aopen", attribute_id >= 0);

		} catch (Throwable err) {
			err.printStackTrace();
			fail("H5.H5Aopen: " + err);
		}

		// Negative test- Error should be thrown when H5Aopen is called with an invalid attribute name(which hasn't been created).
		try {
			attr_name = "file";
			H5.H5Aopen(obj_id, attr_name, aapl_id);
			fail("Negative Test Failed:- Error not Thrown when attribute name is invalid.");
		} catch (AssertionError err) {
			fail("H5.H5Aopen: " + err);
		} catch (HDF5LibraryException err) {
		}

		finally {
			H5.H5Tclose(type_id);
			H5.H5Sclose(space_id);
			H5.H5Aclose(atrr_id);
			H5.H5Aclose(attribute_id);
		}

	}
	
	@Test
	public void testH5Aopen_by_idx() throws Throwable, HDF5LibraryException {
		
		int loc_id = H5did;
		String obj_name = "."; 
		int idx_type = HDF5Constants.H5_INDEX_CRT_ORDER; 
		int order = HDF5Constants.H5_ITER_INC;
		int n = 0;
		int type_id = -1;
		int space_id = -1;
		int lapl_id = -1;
		int attr_id = -1;
		int acpl_id = HDF5Constants.H5P_DEFAULT;
		int aapl_id = HDF5Constants.H5P_DEFAULT;
		int attribute_id = -1;
	
		
		try {
			type_id  = H5.H5Tcreate(HDF5Constants.H5T_ENUM, (long) 1);
			space_id = H5.H5Screate(HDF5Constants.H5S_NULL);
			lapl_id  = H5.H5Pcreate(HDF5Constants.H5P_DATASET_ACCESS);
			attr_id  = H5.H5Acreate2(loc_id, obj_name, type_id, space_id, acpl_id, aapl_id);
						
		} catch (Throwable err) {
			err.printStackTrace();
			fail("H5.H5Tcreate: " + err);
			fail("H5.H5Screate: " + err);
			fail("H5.H5Pcreate: " + err);
			fail("H5.H5Acreate2: " + err);
		}
		   
		try {
			// Opening the existing attribute, obj_name(Created by H5ACreate2) by index, attached to an object identifier.
			attribute_id = H5.H5Aopen_by_idx(loc_id, obj_name, idx_type, order , n, aapl_id, lapl_id);

			// Check the value of attribute id returned from H5Aopen_by_idx. If it is negative then test fails.
			assertTrue("testH5Aopen_by_idx: H5Aopen_by_idx", attribute_id >= 0);

		} catch (Throwable err) {
			err.printStackTrace();
			fail("H5.H5Aopen_by_idx: " + err);
		}
		
		// Negative test- Error should be thrown when H5Aopen_by_idx is called with n=5 and we do not have 5 attributes created.
		try {
			n = 5;
			H5.H5Aopen_by_idx(loc_id, obj_name, idx_type, order , n, aapl_id, lapl_id);
			fail("Negative Test Failed:- Error not Thrown when n is invalid.");
		} catch (AssertionError err) {
			fail("H5.H5Aopen_by_idx: " + err);
		} catch (HDF5LibraryException err) {
		}
		
		
		// Negative test- Error should be thrown when H5Aopen_by_idx is called with an invalid attribute name(which hasn't been created).
		try {
			n = 0;
			obj_name = "file";
			H5.H5Aopen_by_idx(loc_id, obj_name, idx_type, order , n, aapl_id, lapl_id);
			fail("Negative Test Failed:- Error not Thrown when attribute name is invalid.");
		} catch (AssertionError err) {
			fail("H5.H5Aopen_by_idx: " + err);
		} catch (HDF5LibraryException err) {
		}
		
		
        finally
        {
           H5.H5Tclose(type_id);
		   H5.H5Sclose(space_id);
		   H5.H5Pclose(lapl_id);
		   H5.H5Aclose(attr_id);
		   H5.H5Aclose(attribute_id);
        }
        
     // Negative test- Error should be thrown when H5Aopen_by_idx is called when IDs are closed
		try {
			H5.H5Aopen_by_idx(loc_id, obj_name, idx_type, order , n, aapl_id, lapl_id);
			fail("Negative Test Failed:- Error not Thrown when IDs are closed.");
		} catch (AssertionError err) {
			fail("H5.H5Aopen_by_idx: " + err);
		} catch (HDF5LibraryException err) {
		}
	
	}

}
