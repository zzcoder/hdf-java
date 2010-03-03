package test.hdf5lib;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;

import ncsa.hdf.hdf5lib.H5;
import ncsa.hdf.hdf5lib.HDF5Constants;
import ncsa.hdf.hdf5lib.exceptions.HDF5Exception;
import ncsa.hdf.hdf5lib.exceptions.HDF5LibraryException;
import ncsa.hdf.hdf5lib.structs.H5L_info_t;
import ncsa.hdf.hdf5lib.structs.H5A_info_t;

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
	public void testH5Acreate2() throws Throwable, HDF5LibraryException, NullPointerException {

		int loc_id = H5did;
		String attr_name = "dset";
		int type_id = -1, space_id = -1, atrr_id = -1 ;
		int acpl_id = HDF5Constants.H5P_DEFAULT;
		int aapl_id = HDF5Constants.H5P_DEFAULT;

		try {
			type_id = H5.H5Tcreate(HDF5Constants.H5T_ENUM, (long) 1);
			space_id = H5.H5Screate(HDF5Constants.H5S_NULL);
			atrr_id = H5.H5Acreate2(loc_id, attr_name, type_id, space_id,
					acpl_id, aapl_id);

			// Check the value of attribute id returned from H5Acreate2.If it is
			// negative then test fails.
			assertTrue("testH5Acreate2: H5Acreate2", atrr_id >= 0);

			// Negative Test - Error should be thrown when H5Acreate2 is called
			// for
			// an invalid loc_id
			try {
				loc_id = H5dsid;
				H5.H5Acreate2(loc_id, attr_name, type_id, space_id, acpl_id,
						aapl_id);
				fail("Negative Test Failed:- Error not Thrown when location is not valid for an attribute.");
			} catch (AssertionError err) {
				fail("H5.H5Acreate2: " + err);
			} catch (HDF5LibraryException err) {
			}

		} catch (Throwable err) {
			err.printStackTrace();
			fail("H5.H5Acreate2: " + err);
		} finally {
			if (type_id > 0)
				H5.H5Tclose(type_id);
			if (space_id > 0)
				H5.H5Sclose(space_id);
			if (atrr_id > 0)
				H5.H5Aclose(atrr_id);
		}

		// Negative test- Error should be thrown when H5Acreate2 is called when
		// type_id and space_id are closed
		try {
			H5.H5Acreate2(loc_id, attr_name, type_id, space_id, acpl_id,
					aapl_id);
			fail("Negative Test Failed:- Error not Thrown when IDs are closed.");
		} catch (AssertionError err) {
			fail("H5.H5Acreate2: " + err);
		} catch (HDF5LibraryException err) {
		}

	}

	@Test
	public void testH5Aopen() throws Throwable, HDF5LibraryException, NullPointerException {

		int obj_id = H5did;
		String attr_name = "dset";
		int type_id = -1, space_id = -1;
		int attribute_id = -1, atrr_id = -1;
		int acpl_id = HDF5Constants.H5P_DEFAULT;
		int aapl_id = HDF5Constants.H5P_DEFAULT;

		try {
			type_id = H5.H5Tcreate(HDF5Constants.H5T_ENUM, (long) 1);
			space_id = H5.H5Screate(HDF5Constants.H5S_NULL);
			atrr_id = H5.H5Acreate2(obj_id, attr_name, type_id, space_id,
					acpl_id, aapl_id);

			// Opening the existing attribute, attr_name(Created by H5ACreate2)
			// attached to an object identifier.
			attribute_id = H5.H5Aopen(obj_id, attr_name, aapl_id);

			// Check the value of attribute id returned from H5Aopen.If it is
			// negative then test fails.
			assertTrue("testH5Aopen: H5Aopen", attribute_id >= 0);

			// Negative test- Error should be thrown when H5Aopen is called with
			// an
			// invalid attribute name(which hasn't been created).
			try {
				attr_name = "file";
				H5.H5Aopen(obj_id, attr_name, aapl_id);
				fail("Negative Test Failed:- Error not Thrown when attribute name is invalid.");
			} catch (AssertionError err) {
				fail("H5.H5Aopen: " + err);
			} catch (HDF5LibraryException err) {
			}

		} catch (Throwable err) {
			err.printStackTrace();
			fail("H5.H5Aopen: " + err);
		} finally {
			if (type_id > 0)
				H5.H5Tclose(type_id);
			if (space_id > 0)
				H5.H5Sclose(space_id);
			if (atrr_id > 0)
				H5.H5Aclose(atrr_id);
			if (attribute_id > 0)
				H5.H5Aclose(attribute_id);
		}

	}

	@Test
	public void testH5Aopen_by_idx() throws Throwable, HDF5LibraryException, NullPointerException{

		int loc_id = H5did;
		String obj_name = ".";
		int idx_type = HDF5Constants.H5_INDEX_CRT_ORDER;
		int order = HDF5Constants.H5_ITER_INC;
		long n = 0;
		int type_id = -1, space_id = -1, lapl_id = -1;
		int attr_id = -1, attribute_id = -1;
		int acpl_id = HDF5Constants.H5P_DEFAULT;
		int aapl_id = HDF5Constants.H5P_DEFAULT;
	
		try {
			type_id = H5.H5Tcreate(HDF5Constants.H5T_ENUM, (long) 1);
			space_id = H5.H5Screate(HDF5Constants.H5S_NULL);
			lapl_id = H5.H5Pcreate(HDF5Constants.H5P_DATASET_ACCESS);
			attr_id = H5.H5Acreate2(loc_id, obj_name, type_id, space_id,
					acpl_id, aapl_id);

			// Opening the existing attribute, obj_name(Created by H5ACreate2)
			// by index, attached to an object identifier.
			attribute_id = H5.H5Aopen_by_idx(loc_id, obj_name, idx_type, order,
					n, aapl_id, lapl_id);

			// Check the value of attribute id returned from H5Aopen_by_idx. If
			// it is negative then test fails.
			assertTrue("testH5Aopen_by_idx: H5Aopen_by_idx", attribute_id >= 0);

			// Negative test- Error should be thrown when H5Aopen_by_idx is
			// called
			// with n=5 and we do not have 5 attributes created.
			try {
				n = 5;
				H5.H5Aopen_by_idx(loc_id, obj_name, idx_type, order, n,
						aapl_id, lapl_id);
				fail("Negative Test Failed:- Error not Thrown when n is invalid.");
			} catch (AssertionError err) {
				fail("H5.H5Aopen_by_idx: " + err);
			} catch (HDF5LibraryException err) {
			}

			// Negative test- Error should be thrown when H5Aopen_by_idx is
			// called
			// with an invalid object name(which hasn't been created).
			try {
				n = 0;
				obj_name = "file";
				H5.H5Aopen_by_idx(loc_id, obj_name, idx_type, order, n,
						aapl_id, lapl_id);
				fail("Negative Test Failed:- Error not Thrown when attribute name is invalid.");
			} catch (AssertionError err) {
				fail("H5.H5Aopen_by_idx: " + err);
			} catch (HDF5LibraryException err) {
			}

		} catch (Throwable err) {
			err.printStackTrace();
			fail("H5.H5Aopen_by_idx: " + err);
		} finally {
			if (type_id > 0)
				H5.H5Tclose(type_id);
			if (space_id > 0)
				H5.H5Sclose(space_id);
			if (lapl_id > 0)
				H5.H5Pclose(lapl_id);
			if (attr_id > 0)
				H5.H5Aclose(attr_id);
			if (attribute_id > 0)
				H5.H5Aclose(attribute_id);
		}

		// Negative test- Error should be thrown when H5Aopen_by_idx is called
		// when IDs are closed
		try {
			H5.H5Aopen_by_idx(loc_id, obj_name, idx_type, order, n, aapl_id,
					lapl_id);
			fail("Negative Test Failed:- Error not Thrown when IDs are closed.");
		} catch (AssertionError err) {
			fail("H5.H5Aopen_by_idx: " + err);
		} catch (HDF5LibraryException err) {
		}

	}
	
	@Test
	public void testH5Acreate_by_name() throws Throwable, HDF5LibraryException, NullPointerException {

		int loc_id = H5fid;
		String obj_name = ".";
		String attr_name = "DATASET";
		int type_id = -1, space_id = -1, lapl_id = -1;
		int acpl_id = HDF5Constants.H5P_DEFAULT;
		int aapl_id = HDF5Constants.H5P_DEFAULT;
		int attribute_id = -1;
		boolean bool_val = false;

		try {
			type_id = H5.H5Tcreate(HDF5Constants.H5T_ENUM, (long) 1);
			space_id = H5.H5Screate(HDF5Constants.H5S_NULL);
			lapl_id = H5.H5Pcreate(HDF5Constants.H5P_DATASET_ACCESS);

			attribute_id = H5.H5Acreate_by_name(loc_id, obj_name, attr_name,
					type_id, space_id, acpl_id, aapl_id, lapl_id);

			// Check the value of attribute id returned from H5Acreate_by_name,
			// it should be non negative.
			assertTrue("testH5Acreate_by_name: H5Acreate_by_name",
					attribute_id >= 0);

			// Check if the name of attribute attached to the object specified
			// by loc_id and obj_name exists.It should be true.
			bool_val = H5.H5Aexists_by_name(loc_id, obj_name, attr_name,
					lapl_id);
			assertTrue("testH5Acreate_by_name: H5Aexists_by_name",
					bool_val == true);

		} catch (Throwable err) {
			err.printStackTrace();
			fail("H5.H5Acreate_by_name " + err);
		} finally {
			if (type_id > 0)
				H5.H5Tclose(type_id);
			if (space_id > 0)
				H5.H5Sclose(space_id);
			if (lapl_id > 0)
				H5.H5Pclose(lapl_id);
			if (attribute_id > 0)
				H5.H5Aclose(attribute_id);
		}

	}
	
	@Test
	public void testH5Arename_by_name() throws Throwable, HDF5LibraryException, NullPointerException {
		
		int loc_id = H5fid;
		String obj_name = ".";
		String old_attr_name = "old";
		String new_attr_name = "new";
		int type_id = -1, space_id = -1, lapl_id = -1;
		int attr_id = -1, ret_val = -1;
		int acpl_id = HDF5Constants.H5P_DEFAULT;
		int aapl_id = HDF5Constants.H5P_DEFAULT;
		boolean bool_val = false;

		try {
			type_id = H5.H5Tcreate(HDF5Constants.H5T_ENUM, (long) 1);
			space_id = H5.H5Screate(HDF5Constants.H5S_NULL);
			lapl_id = H5.H5Pcreate(HDF5Constants.H5P_DATASET_ACCESS);
			attr_id = H5.H5Acreate_by_name(loc_id, obj_name, old_attr_name,
					type_id, space_id, acpl_id, aapl_id, lapl_id);

			ret_val = H5.H5Arename_by_name(loc_id, obj_name, old_attr_name,
					new_attr_name, lapl_id);

			// Check the return value.It should be non negative.
			assertTrue("testH5Arename_by_name: H5Arename_by_name", ret_val >= 0);

			// Check if the new name of attribute attached to the object
			// specified by loc_id and obj_name exists.It should be true.
			bool_val = H5.H5Aexists_by_name(loc_id, obj_name, new_attr_name,
					lapl_id);
			assertTrue("testH5Arename_by_name: H5Aexists_by_name",
					bool_val == true);

			// Check if the old name of attribute attached to the object
			// specified by loc_id and obj_name exists. It should equal false.
			bool_val = H5.H5Aexists_by_name(loc_id, obj_name, old_attr_name,
					lapl_id);
			assertEquals(bool_val, false);

		} catch (Throwable err) {
			err.printStackTrace();
			fail("H5.H5Arename_by_name " + err);
		} finally {
			if (type_id > 0)
				H5.H5Tclose(type_id);
			if (space_id > 0)
				H5.H5Sclose(space_id);
			if (lapl_id > 0)
				H5.H5Pclose(lapl_id);
			if (attr_id > 0)
				H5.H5Aclose(attr_id);
		}

	}
	
	@Test
	public void testH5Aget_name_by_idx() throws Throwable, HDF5LibraryException, NullPointerException {
		
		int loc_id = H5fid;
		String obj_name = ".";
		String attr_name = "DATASET1", attr2_name = "DATASET2";
		String ret_name = null;
		int idx_type = HDF5Constants.H5_INDEX_NAME;
		int order = HDF5Constants.H5_ITER_INC;
		int n = 0;
		int type_id = -1, space_id = -1, lapl_id = -1;
		int acpl_id = HDF5Constants.H5P_DEFAULT;
		int aapl_id = HDF5Constants.H5P_DEFAULT;
		int attr1_id = -1, attr2_id = -1;

		try {
			type_id = H5.H5Tcreate(HDF5Constants.H5T_ENUM, (long) 1);
			space_id = H5.H5Screate(HDF5Constants.H5S_NULL);
			lapl_id = H5.H5Pcreate(HDF5Constants.H5P_DATASET_ACCESS);
			attr1_id = H5.H5Acreate_by_name(loc_id, obj_name, attr_name,
					type_id, space_id, acpl_id, aapl_id, lapl_id);
			attr2_id = H5.H5Acreate_by_name(loc_id, obj_name, attr2_name,
					type_id, space_id, acpl_id, aapl_id, lapl_id);

			// getting the 1st attribute name(n=0).
			ret_name = H5.H5Aget_name_by_idx(loc_id, obj_name, idx_type, order,
					n, lapl_id);
			assertFalse("H5Aget_name_by_idx ", ret_name == null);
			assertEquals(ret_name, attr_name);

			// getting the second attribute name(n=1)
			ret_name = H5.H5Aget_name_by_idx(loc_id, obj_name, idx_type, order,
					1, lapl_id);
			assertFalse("H5Aget_name_by_idx ", ret_name == null);
			assertEquals(ret_name, attr2_name);

		} catch (Throwable err) {
			err.printStackTrace();
			fail("H5.H5Aget_name_by_idx " + err);
		} finally {
			if (type_id > 0)
				H5.H5Tclose(type_id);
			if (space_id > 0)
				H5.H5Sclose(space_id);
			if (lapl_id > 0)
				H5.H5Pclose(lapl_id);
			if (attr1_id > 0)
				H5.H5Aclose(attr1_id);
			if (attr2_id > 0)
				H5.H5Aclose(attr2_id);
		}

	}
	@Test
	public void testH5Aget_storage_size() throws Throwable{
		
		int type_id = -1, space_id = -1, attr_id = -1;
		long attr_size = -1;

		try {
			type_id = H5.H5Tcreate(HDF5Constants.H5T_ENUM, (long) 1);
			space_id = H5.H5Screate(HDF5Constants.H5S_NULL);
			attr_id = H5.H5Acreate2(H5did, "dset", type_id, space_id,
					HDF5Constants.H5P_DEFAULT, HDF5Constants.H5P_DEFAULT);

			attr_size = H5.H5Aget_storage_size(attr_id);
			assertTrue("The size of attribute is :", attr_size == 0);
		} catch (Throwable err) {
			err.printStackTrace();
			fail("H5.H5Aget_storage_size: " + err);
		} finally {
			if (type_id > 0)
				H5.H5Tclose(type_id);
			if (space_id > 0)
				H5.H5Sclose(space_id);
			if (attr_id > 0)
				H5.H5Aclose(attr_id);
		}
	}
	
	@Test
	public void testH5Aget_info() throws Throwable, HDF5LibraryException{
		
		H5A_info_t attr_info = null;
		int type_id = -1, space_id = -1;
		int attribute_id = -1, atrr_id = -1;

		try {
			type_id = H5.H5Tcreate(HDF5Constants.H5T_ENUM, (long) 1);
			space_id = H5.H5Screate(HDF5Constants.H5S_NULL);
			atrr_id = H5.H5Acreate2(H5did, "dset", type_id, space_id,
					HDF5Constants.H5P_DEFAULT, HDF5Constants.H5P_DEFAULT);
			attribute_id = H5.H5Aopen(H5did, "dset", HDF5Constants.H5P_DEFAULT);
			// Calling H5Aget_info with attribute_id returned from H5Aopen.
			attr_info = H5.H5Aget_info(attribute_id);
			assertFalse("H5Aget_info ", attr_info == null);
			assertTrue("Corder_Valid should be false",
					attr_info.corder_valid == false);
			assertTrue("Character set used for attribute name",
					attr_info.cset == HDF5Constants.H5T_CSET_ASCII);
			assertTrue("Corder ", attr_info.corder >= 0);
			assertEquals(attr_info.data_size, H5.H5Aget_storage_size(atrr_id));
		} catch (Throwable err) {
			err.printStackTrace();
			fail("H5.H5Aget_info: " + err);
		} finally {
			if (type_id > 0)
				H5.H5Tclose(type_id);
			if (space_id > 0)
				H5.H5Sclose(space_id);
			if (atrr_id > 0)
				H5.H5Aclose(atrr_id);
			if (attribute_id > 0)
				H5.H5Aclose(attribute_id);
		}
	}
	
	@Test
	public void testH5Aget_info1() throws Throwable, HDF5LibraryException{
		
		H5A_info_t attr_info = null;
		int type_id = -1, space_id = -1, lapl_id = -1;
		int attribute_id = -1, atrr_id = -1;
		int order = HDF5Constants.H5_ITER_INC;

		try {
			type_id = H5.H5Tcreate(HDF5Constants.H5T_ENUM, (long) 1);
			space_id = H5.H5Screate(HDF5Constants.H5S_NULL);
			lapl_id = H5.H5Pcreate(HDF5Constants.H5P_DATASET_ACCESS);
			atrr_id = H5.H5Acreate2(H5did, ".", type_id, space_id,
					HDF5Constants.H5P_DEFAULT, HDF5Constants.H5P_DEFAULT);
			attribute_id = H5.H5Aopen_by_idx(H5did, ".", HDF5Constants.H5_INDEX_CRT_ORDER, order, 0,
					HDF5Constants.H5P_DEFAULT, lapl_id);
			//Calling H5Aget_info with attribute_id returned from H5Aopen_by_idx.
			attr_info = H5.H5Aget_info(attribute_id);

			assertFalse("H5Aget_info ", attr_info == null);
			assertTrue("Corder_Valid should be true",
					attr_info.corder_valid == true);
			assertTrue("Character set used for attribute name",
					attr_info.cset == HDF5Constants.H5T_CSET_ASCII);
			assertTrue("Corder ", attr_info.corder == order);
			assertEquals(attr_info.data_size, H5.H5Aget_storage_size(atrr_id));
		} catch (Throwable err) {
			err.printStackTrace();
			fail("H5.H5Aget_info1: " + err);
		} finally {
			if (type_id > 0)
				H5.H5Tclose(type_id);
			if (space_id > 0)
				H5.H5Sclose(space_id);
			if (lapl_id > 0)
				H5.H5Pclose(lapl_id);
			if (atrr_id > 0)
				H5.H5Aclose(atrr_id);
			if (attribute_id > 0)
				H5.H5Aclose(attribute_id);
		}

	}

}
