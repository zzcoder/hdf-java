package test.hdf5lib;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.io.File;

import ncsa.hdf.hdf5lib.H5;
import ncsa.hdf.hdf5lib.HDF5Constants;
import ncsa.hdf.hdf5lib.exceptions.HDF5Exception;
import ncsa.hdf.hdf5lib.exceptions.HDF5LibraryException;
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
	int type_id = -1, space_id = -1, lapl_id = -1;

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
			type_id = H5.H5Tcreate(HDF5Constants.H5T_ENUM, (long) 1);
			space_id = H5.H5Screate(HDF5Constants.H5S_NULL);
			lapl_id = H5.H5Pcreate(HDF5Constants.H5P_DATASET_ACCESS);
		} catch (Throwable err) {
			err.printStackTrace();
			fail("TestH5D.createH5file: " + err);
		}
		assertTrue("TestH5D.createH5file: H5.H5Fcreate: ", H5fid > 0);
		assertTrue("TestH5D.createH5file: H5.H5Screate_simple: ", H5dsid > 0);
		assertTrue("TestH5D.createH5file: _createDataset: ", H5did > 0);
		assertTrue(type_id > 0);
		assertTrue(space_id > 0);
		assertTrue(lapl_id > 0);

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

		if (type_id > 0)
			H5.H5Tclose(type_id);
		if (space_id > 0)
			H5.H5Sclose(space_id);
		if (lapl_id > 0)
			H5.H5Pclose(lapl_id);
		
	}
	
	@Test
	public void testH5Acreate2() throws Throwable, HDF5LibraryException {

		int atrr_id = -1;
		try {
			atrr_id = H5.H5Acreate(H5did, "dset", type_id, space_id, HDF5Constants.H5P_DEFAULT, HDF5Constants.H5P_DEFAULT);
			assertTrue("testH5Acreate2", atrr_id >= 0);
		} catch (Throwable err) {
			err.printStackTrace();
			fail("H5.H5Acreate2: " + err);
		} finally {
			if (atrr_id > 0)
				H5.H5Aclose(atrr_id);
		}
	}
	
	@Test(expected = HDF5LibraryException.class)
	public void testH5Acreate2_invalidobject() throws Throwable, HDF5LibraryException {
		H5.H5Acreate(H5dsid, "dset", type_id, space_id, HDF5Constants.H5P_DEFAULT, HDF5Constants.H5P_DEFAULT);
	}
	
	@Test(expected = NullPointerException.class)
	public void testH5Acreate2_nullname() throws Throwable, HDF5LibraryException {
		H5.H5Acreate(H5did, null, type_id, space_id, HDF5Constants.H5P_DEFAULT, HDF5Constants.H5P_DEFAULT);
	}

	@Test
	public void testH5Aopen() throws Throwable, HDF5LibraryException, NullPointerException {

		String attr_name = "dset";
		int attribute_id = -1, atrr_id = -1;

		try {
			atrr_id = H5.H5Acreate(H5did, attr_name, type_id, space_id,
					HDF5Constants.H5P_DEFAULT, HDF5Constants.H5P_DEFAULT);

			// Opening the existing attribute, attr_name(Created by H5ACreate2)
			// attached to an object identifier.
			attribute_id = H5.H5Aopen(H5did, attr_name,
					HDF5Constants.H5P_DEFAULT);
			assertTrue("testH5Aopen: H5Aopen", attribute_id >= 0);
		} catch (Throwable err) {
			err.printStackTrace();
			fail("H5.H5Aopen: " + err);
		} finally {
			if (atrr_id > 0)
				H5.H5Aclose(atrr_id);
			if (attribute_id > 0)
				H5.H5Aclose(attribute_id);
		}

	}
	
	@Test(expected = HDF5LibraryException.class)
	public void testH5Aopen_invalidname() throws Throwable, HDF5LibraryException {
		H5.H5Aopen(H5did, "attr_name", HDF5Constants.H5P_DEFAULT);
	}

	@Test
	public void testH5Aopen_by_idx() throws Throwable, HDF5LibraryException, NullPointerException{

		int loc_id = H5did;
		String obj_name = ".";
		int idx_type = HDF5Constants.H5_INDEX_CRT_ORDER;
		int order = HDF5Constants.H5_ITER_INC;
		long n = 0;
		int attr_id = -1, attribute_id = -1;
		int aapl_id = HDF5Constants.H5P_DEFAULT;

		try {
			attr_id = H5.H5Acreate(H5did, "file", type_id, space_id,
					HDF5Constants.H5P_DEFAULT, HDF5Constants.H5P_DEFAULT);

			// Opening the existing attribute, obj_name(Created by H5ACreate2)
			// by index, attached to an object identifier.
			attribute_id = H5.H5Aopen_by_idx(H5did, ".", HDF5Constants.H5_INDEX_CRT_ORDER, HDF5Constants.H5_ITER_INC,
					0, HDF5Constants.H5P_DEFAULT, lapl_id);

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
			if (attr_id > 0)
				H5.H5Aclose(attr_id);
			if (attribute_id > 0)
				H5.H5Aclose(attribute_id);
		}
	}
	
	@Test
	public void testH5Acreate_by_name() throws Throwable, HDF5LibraryException, NullPointerException {

		String obj_name = ".";
		String attr_name = "DATASET";
		int attribute_id = -1;
		boolean bool_val = false;

		try {
			attribute_id = H5.H5Acreate_by_name(H5fid, obj_name, attr_name,
					type_id, space_id, HDF5Constants.H5P_DEFAULT,
					HDF5Constants.H5P_DEFAULT, lapl_id);
			assertTrue("testH5Acreate_by_name: H5Acreate_by_name",
					attribute_id >= 0);

			// Check if the name of attribute attached to the object specified
			// by loc_id and obj_name exists.It should be true.
			bool_val = H5.H5Aexists_by_name(H5fid, obj_name, attr_name,
					lapl_id);
			assertTrue("testH5Acreate_by_name: H5Aexists_by_name",
					bool_val == true);

		} catch (Throwable err) {
			err.printStackTrace();
			fail("H5.H5Acreate_by_name " + err);
		} finally {
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
		int attr_id = -1, ret_val = -1;
		boolean bool_val = false;

		try {
			attr_id = H5.H5Acreate_by_name(loc_id, obj_name, old_attr_name,
					type_id, space_id, HDF5Constants.H5P_DEFAULT, HDF5Constants.H5P_DEFAULT, lapl_id);

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
		int attr1_id = -1, attr2_id = -1;

		try {
			attr1_id = H5.H5Acreate_by_name(loc_id, obj_name, attr_name,
					type_id, space_id, HDF5Constants.H5P_DEFAULT,
					HDF5Constants.H5P_DEFAULT, lapl_id);
			attr2_id = H5.H5Acreate_by_name(loc_id, obj_name, attr2_name,
					type_id, space_id, HDF5Constants.H5P_DEFAULT,
					HDF5Constants.H5P_DEFAULT, lapl_id);

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
			if (attr1_id > 0)
				H5.H5Aclose(attr1_id);
			if (attr2_id > 0)
				H5.H5Aclose(attr2_id);
		}

	}
	@Test
	public void testH5Aget_storage_size() throws Throwable{
		
		int attr_id = -1;
		long attr_size = -1;

		try {
			attr_id = H5.H5Acreate(H5did, "dset", type_id, space_id,
					HDF5Constants.H5P_DEFAULT, HDF5Constants.H5P_DEFAULT);

			attr_size = H5.H5Aget_storage_size(attr_id);
			assertTrue("The size of attribute is :", attr_size == 0);
		} catch (Throwable err) {
			err.printStackTrace();
			fail("H5.H5Aget_storage_size: " + err);
		} finally {
			if (attr_id > 0)
				H5.H5Aclose(attr_id);
		}
	}
	
	@Test
	public void testH5Aget_info() throws Throwable, HDF5LibraryException{
		
		H5A_info_t attr_info = null;
		int attribute_id = -1, atrr_id = -1;

		try {
			atrr_id = H5.H5Acreate(H5did, "dset", type_id, space_id,
					HDF5Constants.H5P_DEFAULT, HDF5Constants.H5P_DEFAULT);
			attribute_id = H5.H5Aopen(H5did, "dset", HDF5Constants.H5P_DEFAULT);
			// Calling H5Aget_info with attribute_id returned from H5Aopen.
			attr_info = H5.H5Aget_info(attribute_id);
			assertFalse("H5Aget_info ", attr_info == null);
			assertTrue("Corder_Valid should be false",
					attr_info.corder_valid == false);
			assertTrue("Character set used for attribute name",
					attr_info.cset == HDF5Constants.H5T_CSET_ASCII);
			assertTrue("Corder ", attr_info.corder == 0);
			assertEquals(attr_info.data_size, H5.H5Aget_storage_size(atrr_id));
		} catch (Throwable err) {
			err.printStackTrace();
			fail("H5.H5Aget_info: " + err);
		} finally {
			if (atrr_id > 0)
				H5.H5Aclose(atrr_id);
			if (attribute_id > 0)
				H5.H5Aclose(attribute_id);
		}
	}
	
	@Test
	public void testH5Aget_info1() throws Throwable, HDF5LibraryException{
		
		H5A_info_t attr_info = null;
		int attribute_id = -1, atrr_id = -1;
		int order = HDF5Constants.H5_ITER_INC;

		try {
			atrr_id = H5.H5Acreate(H5did, ".", type_id, space_id,
					HDF5Constants.H5P_DEFAULT, HDF5Constants.H5P_DEFAULT);
			attribute_id = H5.H5Aopen_by_idx(H5did, ".",
					HDF5Constants.H5_INDEX_CRT_ORDER, order, 0,
					HDF5Constants.H5P_DEFAULT, lapl_id);
			// Calling H5Aget_info with attribute_id returned from
			// H5Aopen_by_idx.
			attr_info = H5.H5Aget_info(attribute_id);

			assertFalse("H5Aget_info ", attr_info == null);
			assertTrue("Corder_Valid should be true",
					attr_info.corder_valid == true);
			assertTrue("Character set",
					attr_info.cset == HDF5Constants.H5T_CSET_ASCII);
			assertTrue("Corder ", attr_info.corder == 0);
			assertEquals(attr_info.data_size, H5.H5Aget_storage_size(atrr_id));
		} catch (Throwable err) {
			err.printStackTrace();
			fail("H5.H5Aget_info1: " + err);
		} finally {
			if (atrr_id > 0)
				H5.H5Aclose(atrr_id);
			if (attribute_id > 0)
				H5.H5Aclose(attribute_id);
		}

	}
	
	
	@Test
	public void testH5Aget_info_by_idx() throws Throwable, HDF5LibraryException, NullPointerException{

		int attr_id = -1, attr2_id = -1;;
		H5A_info_t attr_info = null;

		try {
			attr_id = H5.H5Acreate(H5did, "dset1", type_id, space_id,
					HDF5Constants.H5P_DEFAULT, HDF5Constants.H5P_DEFAULT);
			attr2_id = H5.H5Acreate(H5did, "dataset2", type_id, space_id,
					HDF5Constants.H5P_DEFAULT, HDF5Constants.H5P_DEFAULT);
			
			//Verify info for 1st attribute, in increasing creation order
			attr_info = H5.H5Aget_info_by_idx(H5did, ".", HDF5Constants.H5_INDEX_CRT_ORDER, HDF5Constants.H5_ITER_INC, 0, lapl_id);
			assertNotNull(attr_info);
			assertTrue("Corder ", attr_info.corder == 0);//should equal 0 as this is the order of 1st attribute created.
			assertEquals(attr_info.data_size, H5.H5Aget_storage_size(attr_id));

			//Verify info for 2nd attribute, in increasing creation order
			attr_info = H5.H5Aget_info_by_idx(H5did, ".", HDF5Constants.H5_INDEX_CRT_ORDER, HDF5Constants.H5_ITER_INC, 1, lapl_id);
			assertNotNull(attr_info);
			assertTrue("Corder", attr_info.corder == 1);
			assertEquals(attr_info.data_size, H5.H5Aget_storage_size(attr2_id));

			//verify info for 2nd attribute, in decreasing creation order
			attr_info = H5.H5Aget_info_by_idx(H5did, ".", HDF5Constants.H5_INDEX_CRT_ORDER, HDF5Constants.H5_ITER_DEC, 0, lapl_id);
			assertNotNull(attr_info);
			assertTrue("Corder", attr_info.corder == 1); //should equal 1 as this is the order of 2nd attribute created.

			//verify info for 1st attribute, in decreasing creation order
			attr_info = H5.H5Aget_info_by_idx(H5did, ".", HDF5Constants.H5_INDEX_CRT_ORDER, HDF5Constants.H5_ITER_DEC, 1, lapl_id);
			assertNotNull(attr_info);
			assertTrue("Corder", attr_info.corder == 0); //should equal 0 as this is the order of 1st attribute created.

			//verify info for 1st attribute, in increasing name order
			attr_info = H5.H5Aget_info_by_idx(H5did, ".", HDF5Constants.H5_INDEX_NAME, HDF5Constants.H5_ITER_INC, 1, lapl_id);
			assertNotNull(attr_info);
			assertTrue("Corder", attr_info.corder == 0); //should equal 0 as this is the order of 1st attribute created.

			//verify info for 2nd attribute, in decreasing name order
			attr_info = H5.H5Aget_info_by_idx(H5did, ".", HDF5Constants.H5_INDEX_NAME, HDF5Constants.H5_ITER_DEC, 1, lapl_id);
			assertNotNull(attr_info);
			assertTrue("Corder", attr_info.corder == 1); //should equal 1 as this is the order of 2nd attribute created.

		} catch (Throwable err) {
			err.printStackTrace();
			fail("H5.H5Aget_info_by_idx:" + err);
		} finally {
			if (attr_id > 0)
				H5.H5Aclose(attr_id);
			if (attr2_id > 0)
				H5.H5Aclose(attr2_id);
		}
	}

	@Test
	public void testH5Aget_info_by_name() throws Throwable, HDF5LibraryException, NullPointerException{

		int attr_id = -1;
		H5A_info_t attr_info = null;
		String obj_name = ".";
		String attr_name = "DATASET";

		try {
			attr_id = H5.H5Acreate_by_name(H5fid, obj_name, attr_name, type_id,
					space_id, HDF5Constants.H5P_DEFAULT,
					HDF5Constants.H5P_DEFAULT, lapl_id);
			attr_info = H5.H5Aget_info_by_name(H5fid, obj_name, attr_name,
					lapl_id);
			assertNotNull(attr_info);
		} catch (Throwable err) {
			err.printStackTrace();
			fail("H5.H5Aget_info_by_name:" + err);
		} finally {
			if (attr_id > 0)
				H5.H5Aclose(attr_id);
		}
	}
	
	@Test
	public void testH5Adelete_by_name() throws Throwable, HDF5LibraryException, NullPointerException{
	
		int attr_id = -1, ret_val = -1;
		boolean bool_val = false, exists = false;

		try {
			attr_id = H5.H5Acreate_by_name(H5fid, ".", "DATASET",
					type_id, space_id, HDF5Constants.H5P_DEFAULT,
					HDF5Constants.H5P_DEFAULT, lapl_id);
			ret_val = H5.H5Adelete_by_name(H5fid, ".", "DATASET", lapl_id);
			assertTrue("H5Adelete_by_name", ret_val >= 0);

			// Check if the Attribute still exists.
			bool_val = H5.H5Aexists_by_name(H5fid, ".", "DATASET",
					lapl_id);
			assertFalse("testH5Adelete_by_name: H5Aexists_by_name", bool_val);
			exists = H5.H5Aexists(H5fid, "DATASET");
			assertFalse("testH5Adelete_by_name: H5Aexists ",exists);

			// Negative test. Error thrown when we try to delete an attribute
			// that has already been deleted.
			try{
				ret_val = H5.H5Adelete_by_name(H5fid, ".", "DATASET", lapl_id);
				fail("Negative Test Failed: Error Not thrown.");
			} catch (AssertionError err) {
				fail("H5.H5Adelete_by_name: " + err);
			} catch (HDF5LibraryException err) {
			}
		} catch (Throwable err) {
			err.printStackTrace();
			fail("H5.H5Adelete_by_name " + err);
		} finally {
			if (attr_id > 0)
				H5.H5Aclose(attr_id);
		}
	}
	
	@Test
	public void testH5Aexists() throws Throwable, HDF5LibraryException, NullPointerException{
		
		boolean exists = false;
		int atrr_id = -1, attribute_id = -1;

		try {
			exists = H5.H5Aexists(H5fid, "None");
		} catch (Throwable err) {
			err.printStackTrace();
			fail("H5.H5Aexists: " + err);
		}
		assertFalse("H5Aexists ", exists);

		try {
			atrr_id = H5.H5Acreate(H5fid, "dset", type_id, space_id,
					HDF5Constants.H5P_DEFAULT, HDF5Constants.H5P_DEFAULT);
			exists = H5.H5Aexists(H5fid, "dset");
			assertTrue("H5Aexists ", exists);

			attribute_id = H5.H5Acreate_by_name(H5fid, ".", "attribute",
					type_id, space_id, HDF5Constants.H5P_DEFAULT,
					HDF5Constants.H5P_DEFAULT, lapl_id);
			exists = H5.H5Aexists(H5fid, "attribute");
			assertTrue("H5Aexists ", exists);

		} catch (Throwable err) {
			err.printStackTrace();
			fail("H5.H5Aexists: " + err);
		} finally {
			if (atrr_id > 0)
				H5.H5Aclose(atrr_id);
			if (attribute_id > 0)
				H5.H5Aclose(attribute_id);
		}
	}
	
	@Test
	public void testH5Adelete_by_idx_order() throws Throwable, HDF5LibraryException, NullPointerException{
		
		boolean exists = false;
		int attr1_id = -1, attr2_id = -1;
		int attr3_id = -1, attr4_id = -1;
		
		try {
			attr1_id = H5.H5Acreate_by_name(H5fid, ".", "attribute1",
					type_id, space_id, HDF5Constants.H5P_DEFAULT,
					HDF5Constants.H5P_DEFAULT, lapl_id);
			attr2_id = H5.H5Acreate_by_name(H5fid, ".", "attribute2",
					type_id, space_id, HDF5Constants.H5P_DEFAULT,
					HDF5Constants.H5P_DEFAULT, lapl_id);
			attr3_id = H5.H5Acreate_by_name(H5fid, ".", "attribute3",
					type_id, space_id, HDF5Constants.H5P_DEFAULT,
					HDF5Constants.H5P_DEFAULT, lapl_id);
			attr4_id = H5.H5Acreate_by_name(H5fid, ".", "attribute4",
					type_id, space_id, HDF5Constants.H5P_DEFAULT,
					HDF5Constants.H5P_DEFAULT, lapl_id);
					
			H5.H5Adelete_by_idx(H5fid, ".", HDF5Constants.H5_INDEX_CRT_ORDER, HDF5Constants.H5_ITER_INC, 3, lapl_id);
			exists = H5.H5Aexists(H5fid, "attribute4");
			assertFalse("H5Adelete_by_idx: H5Aexists", exists);

		} catch (Throwable err) {
			err.printStackTrace();
			fail("H5.H5Adelete_by_idx: " + err);
		} finally {
			if (attr1_id > 0)
				H5.H5Aclose(attr1_id);
			if (attr2_id > 0)
				H5.H5Aclose(attr2_id);
			if (attr3_id > 0)
				H5.H5Aclose(attr3_id);
			if (attr4_id > 0)
				H5.H5Aclose(attr4_id);
		}
	}
	
	@Test
	public void testH5Adelete_by_idx_name1() throws Throwable, HDF5LibraryException, NullPointerException{
		
		boolean exists = false;
		int attr1_id = -1, attr2_id = -1;
		int attr3_id = -1;
		
		try {
			attr1_id = H5.H5Acreate_by_name(H5fid, ".", "attribute1",
					type_id, space_id, HDF5Constants.H5P_DEFAULT,
					HDF5Constants.H5P_DEFAULT, lapl_id);
			attr2_id = H5.H5Acreate_by_name(H5fid, ".", "attribute2",
					type_id, space_id, HDF5Constants.H5P_DEFAULT,
					HDF5Constants.H5P_DEFAULT, lapl_id);
			attr3_id = H5.H5Acreate_by_name(H5fid, ".", "attribute3",
					type_id, space_id, HDF5Constants.H5P_DEFAULT,
					HDF5Constants.H5P_DEFAULT, lapl_id);		
			H5.H5Adelete_by_idx(H5fid, ".", HDF5Constants.H5_INDEX_NAME, HDF5Constants.H5_ITER_INC, 2, lapl_id);
			exists = H5.H5Aexists(H5fid, "attribute3");
			assertFalse("H5Adelete_by_idx: H5Aexists", exists);
		} catch (Throwable err) {
			err.printStackTrace();
			fail("H5.H5Adelete_by_idx: " + err);
		} finally {
			if (attr1_id > 0)
				H5.H5Aclose(attr1_id);
			if (attr2_id > 0)
				H5.H5Aclose(attr2_id);
			if (attr3_id > 0)
				H5.H5Aclose(attr3_id);
		}
	}
	
	@Test
	public void testH5Adelete_by_idx_name2() throws Throwable, HDF5LibraryException, NullPointerException{
		
		boolean exists = false;
		int attr1_id = -1, attr2_id = -1;
		int attr3_id = -1, attr4_id = -1;
		
		try {
			attr1_id = H5.H5Acreate_by_name(H5fid, ".", "attribute1",
					type_id, space_id, HDF5Constants.H5P_DEFAULT,
					HDF5Constants.H5P_DEFAULT, lapl_id);
			attr2_id = H5.H5Acreate_by_name(H5fid, ".", "attribute2",
					type_id, space_id, HDF5Constants.H5P_DEFAULT,
					HDF5Constants.H5P_DEFAULT, lapl_id);
			attr3_id = H5.H5Acreate_by_name(H5fid, ".", "attribute3",
					type_id, space_id, HDF5Constants.H5P_DEFAULT,
					HDF5Constants.H5P_DEFAULT, lapl_id);
			attr4_id = H5.H5Acreate_by_name(H5fid, ".", "attribute4",
					type_id, space_id, HDF5Constants.H5P_DEFAULT,
					HDF5Constants.H5P_DEFAULT, lapl_id);
					
			H5.H5Adelete_by_idx(H5fid, ".", HDF5Constants.H5_INDEX_NAME, HDF5Constants.H5_ITER_DEC, 3, lapl_id);
			exists = H5.H5Aexists(H5fid, "attribute1");
			assertFalse("H5Adelete_by_idx: H5Aexists", exists);
		} catch (Throwable err) {
			err.printStackTrace();
			fail("H5.H5Adelete_by_idx: " + err);
		} finally {
			if (attr1_id > 0)
				H5.H5Aclose(attr1_id);
			if (attr2_id > 0)
				H5.H5Aclose(attr2_id);
			if (attr3_id > 0)
				H5.H5Aclose(attr3_id);
			if (attr4_id > 0)
				H5.H5Aclose(attr4_id);
		}
	}
	
	@Test(expected = NullPointerException.class)
	public void testH5Adelete_by_idx_null() throws Throwable,
			HDF5LibraryException, NullPointerException {
		H5.H5Adelete_by_idx(H5fid, null, HDF5Constants.H5_INDEX_CRT_ORDER,
				HDF5Constants.H5_ITER_INC, 0, lapl_id);
	}

	@Test(expected = HDF5LibraryException.class)
	public void testH5Adelete_by_idx_invalidobject() throws Throwable,
			HDF5LibraryException, NullPointerException {
		H5.H5Adelete_by_idx(H5fid, "invalid", HDF5Constants.H5_INDEX_CRT_ORDER,
				HDF5Constants.H5_ITER_INC, 0, lapl_id);
	}
	
	@Test
	public void testH5Aopen_by_name() throws Throwable, HDF5LibraryException, NullPointerException {

		String obj_name = ".";
		String attr_name = "DATASET";
		int attribute_id = -1;
		int aid = -1;

		try {
			attribute_id = H5.H5Acreate_by_name(H5fid, obj_name, attr_name,
					type_id, space_id, HDF5Constants.H5P_DEFAULT,
					HDF5Constants.H5P_DEFAULT, lapl_id);

			//open Attribute by name
			if(attribute_id>=0){
				try{
					aid = H5.H5Aopen_by_name(H5fid, obj_name, attr_name, HDF5Constants.H5P_DEFAULT, lapl_id);
					assertTrue("testH5Aopen_by_name: ", aid>=0);
				}
				catch(Throwable err) {
					err.printStackTrace();
					fail("H5.H5Aopen_by_name " + err);
				}
			}
		} catch (Throwable err) {
			err.printStackTrace();
			fail("H5.H5Aopen_by_name " + err);
		} finally {
			if (aid > 0)
				H5.H5Aclose(aid);
			if (attribute_id > 0)
				H5.H5Aclose(attribute_id);
		}
	}

}



	