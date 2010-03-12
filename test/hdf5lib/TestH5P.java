package test.hdf5lib;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.junit.Assert.assertNotNull;

import java.io.File;

import ncsa.hdf.hdf5lib.H5;
import ncsa.hdf.hdf5lib.HDF5Constants;
import ncsa.hdf.hdf5lib.exceptions.HDF5Exception;
import ncsa.hdf.hdf5lib.exceptions.HDF5FunctionArgumentException;
import ncsa.hdf.hdf5lib.exceptions.HDF5LibraryException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestH5P {
	
    private static final String H5_FILE = "test.h5";
    private static final int DIM_X = 4;
    private static final int DIM_Y = 6;
    int H5fid = -1;
    int H5dsid = -1;
    int H5did = -1;
    long[] H5dims = { DIM_X, DIM_Y };
    int lapl_id = -1, fapl_id = -1;
    int gcpl_id = -1, ocpl_id = -1;
    int ocp_plist_id = -1, lcpl_id = -1;
    int plist_id = -1;
    int gapl_id = -1;

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
    public void createH5file()
            throws NullPointerException, HDF5Exception {
        assertTrue("H5 open ids is 0",H5.getOpenIDCount()==0);

        try {
            H5fid = H5.H5Fcreate(H5_FILE, HDF5Constants.H5F_ACC_TRUNC,
                    HDF5Constants.H5P_DEFAULT, HDF5Constants.H5P_DEFAULT);
            H5dsid = H5.H5Screate_simple(2, H5dims, null);
            H5did = _createDataset(H5fid, H5dsid, "dset", HDF5Constants.H5P_DEFAULT);
            lapl_id = H5.H5Pcreate(HDF5Constants.H5P_DATASET_ACCESS);
            fapl_id = H5.H5Pcreate(HDF5Constants.H5P_FILE_ACCESS);
            gcpl_id = H5.H5Pcreate(HDF5Constants.H5P_FILE_CREATE);
            ocpl_id = H5.H5Pcreate(HDF5Constants.H5P_DATASET_CREATE);
            ocp_plist_id = H5.H5Pcreate(HDF5Constants.H5P_OBJECT_COPY);
            lcpl_id = H5.H5Pcreate(HDF5Constants.H5P_LINK_CREATE);
            plist_id = H5.H5Pcreate(HDF5Constants.H5P_DATASET_XFER);
            gapl_id = H5.H5Pcreate(HDF5Constants.H5P_GROUP_ACCESS);
        }
        catch (Throwable err) {
            err.printStackTrace();
            fail("TestH5D.createH5file: " + err);
        }
        assertTrue("TestH5D.createH5file: H5.H5Fcreate: ",H5fid > 0);
        assertTrue("TestH5D.createH5file: H5.H5Screate_simple: ",H5dsid > 0);
        assertTrue("TestH5D.createH5file: _createDataset: ",H5did > 0);
        assertTrue(lapl_id > 0);
        assertTrue(fapl_id > 0);
        assertTrue(gcpl_id > 0);
        assertTrue(ocpl_id > 0);
        assertTrue(ocp_plist_id > 0);
        assertTrue(lcpl_id > 0);
        assertTrue(plist_id > 0);

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
        
        if (lapl_id >0)
        	H5.H5Pclose(lapl_id);
        if (fapl_id >0)
        	H5.H5Pclose(fapl_id);
        if (gcpl_id >0)
        	H5.H5Pclose(gcpl_id);
        if (ocpl_id >0)
        	H5.H5Pclose(ocpl_id);
        if (ocp_plist_id >0)
        	H5.H5Pclose(ocp_plist_id);
        if (lcpl_id >0)
        	H5.H5Pclose(lcpl_id);
        if (plist_id >0)
        	H5.H5Pclose(plist_id);
        if (gapl_id >0)
        	H5.H5Pclose(gapl_id);
    }

    @Test
    public void testH5Pget_nlinks() throws Throwable, HDF5LibraryException {
		long nlinks = -1;
		try {
			nlinks = (long) H5.H5Pget_nlinks(lapl_id);
		}
		catch (Throwable err) {
			err.printStackTrace();
			fail("H5.H5Pget_nlinks: " + err);
		}
		assertTrue("testH5Pget_nlinks", nlinks > 0);
		// Check the default value of nlinks.
		assertEquals(nlinks, 16L);
	}

    @Test
    public void testH5Pset_nlinks() throws Throwable, HDF5LibraryException {
    	long nlinks = 20;
    	int ret_val = -1;
    	try {
    		ret_val = H5.H5Pset_nlinks(lapl_id, nlinks);
    		nlinks = (long) H5.H5Pget_nlinks(lapl_id);
    	}
    	catch (Throwable err) {
    		err.printStackTrace();
    		fail("H5.H5Pset_nlinks: " + err);
    	}
    	assertTrue("testH5Pset_nlinks", ret_val >= 0);
    	// Check the value of nlinks retrieved from H5Pget_nlinks function.
    	assertEquals(nlinks, 20L); 
    }
    
    @Test
   
    public void testH5Pget_libver_bounds() throws Throwable, HDF5LibraryException {
    	int ret_val = -1;
    	int[] libver = new int[2];
    	
    	try {
    		ret_val = H5.H5Pget_libver_bounds(fapl_id, libver);
    	}
    	catch (Throwable err) {
    		err.printStackTrace();
    		fail("H5Pget_libver_bounds: " + err);
    	}
    	assertTrue("testH5Pget_libver_bounds", ret_val >= 0);
    	// Check the Earliest Version if the library
    	assertEquals(HDF5Constants.H5F_LIBVER_EARLIEST, libver[0]);
    	// Check the Latest Version if the library
    	assertEquals(HDF5Constants.H5F_LIBVER_LATEST, libver[1]);
    }
       
    @Test
    public void testH5Pset_libver_bounds() throws Throwable, HDF5LibraryException {
    	
    	int ret_val = -1;
    	int low = HDF5Constants.H5F_LIBVER_EARLIEST;
    	int high = HDF5Constants.H5F_LIBVER_LATEST;
    	int[] libver = new int[2];

    	try {
    		ret_val = H5.H5Pset_libver_bounds(fapl_id, low, high);
    		H5.H5Pget_libver_bounds(fapl_id, libver);
    	}
    	catch (Throwable err) {
    		err.printStackTrace();
    		fail("H5Pset_libver_bounds: " + err);
    	}
    	assertTrue("testH5Pset_libver_bounds", ret_val >= 0);
    	// Check the Earliest Version if the library
    	assertEquals(HDF5Constants.H5F_LIBVER_EARLIEST, libver[0]);
    	// Check the Latest Version if the library
    	assertEquals(HDF5Constants.H5F_LIBVER_LATEST, libver[1]);
    }
    
    @Test(expected = IllegalArgumentException.class)
	public void testH5Pset_libver_bounds_invalidlow() throws Throwable, HDF5LibraryException {
    	H5.H5Pset_libver_bounds(fapl_id, 5, HDF5Constants.H5F_LIBVER_LATEST);
	}
    
    @Test(expected = IllegalArgumentException.class)
	public void testH5Pset_libver_bounds_invalidhigh() throws Throwable, HDF5LibraryException {
    	H5.H5Pset_libver_bounds(fapl_id, HDF5Constants.H5F_LIBVER_LATEST, 5);
	}
    
    @Test
    public void testH5Pget_link_creation_order() throws Throwable, HDF5LibraryException {
		int crt_order_flags = 0;
		try {
			crt_order_flags = H5.H5Pget_link_creation_order(gcpl_id);
		}
		catch (Throwable err) {
			err.printStackTrace();
			fail("H5Pget_link_creation_order: " + err);
		}
		assertTrue("testH5Pget_link_creation_order", crt_order_flags >= 0);
	}

    @Test
    public void testH5Pset_link_creation_order_trackedPLUSindexed() throws Throwable, HDF5LibraryException {
    	int ret_val = -1;
    	int crt_order_flags = HDF5Constants.H5P_CRT_ORDER_TRACKED + HDF5Constants.H5P_CRT_ORDER_INDEXED;
    	int crtorderflags = 0;

    	try {
    		ret_val = H5.H5Pset_link_creation_order(gcpl_id, crt_order_flags);
    		crtorderflags = H5.H5Pget_link_creation_order(gcpl_id);
    	}
    	catch (Throwable err) {
    		err.printStackTrace();
    		fail("H5Pset_link_creation_order: " + err);
    	} 
    	assertTrue("testH5Pset_link_creation_order_trackedPLUSindexed",ret_val >= 0);
    	assertEquals(crt_order_flags, crtorderflags);
    }
    
    @Test
    public void testH5Pset_link_creation_order_tracked() throws Throwable, HDF5LibraryException {
    	int ret_val = -1;
    	int crtorderflags = 0;

    	try {
    		ret_val = H5.H5Pset_link_creation_order(gcpl_id, HDF5Constants.H5P_CRT_ORDER_TRACKED);
    		crtorderflags = H5.H5Pget_link_creation_order(gcpl_id);
    	}
    	catch (Throwable err) {
    		err.printStackTrace();
    		fail("H5Pset_link_creation_order: " + err);
    	} 
    	assertTrue("testH5Pset_link_creation_order_tracked",ret_val >= 0);
    	assertEquals(HDF5Constants.H5P_CRT_ORDER_TRACKED, crtorderflags);
    }
    
    @Test(expected = HDF5LibraryException.class)
	public void testH5Pset_link_creation_order_invalidvalue() throws Throwable, HDF5LibraryException {
    	H5.H5Pset_link_creation_order(gcpl_id, HDF5Constants.H5P_CRT_ORDER_INDEXED);
	}
    
    @Test
    public void testH5Pget_attr_creation_order() throws Throwable, HDF5LibraryException {
    	int crt_order_flags = 0;

    	try {
    		crt_order_flags = H5.H5Pget_attr_creation_order(ocpl_id);
    	}
    	catch (Throwable err) {
    		err.printStackTrace();
    		fail("H5Pget_attr_creation_order: " + err);
    	} 
    	assertTrue("testH5Pget_attr_creation_order", crt_order_flags >= 0);
    }
    
    @Test
    public void testH5Pset_attr_creation_order_trackedPLUSindexed() throws Throwable, HDF5LibraryException {
    	int ret_val = -1;
    	int crt_order_flags = HDF5Constants.H5P_CRT_ORDER_TRACKED + HDF5Constants.H5P_CRT_ORDER_INDEXED;
    	int crtorderflags = 0;

    	try {
    		ret_val = H5.H5Pset_attr_creation_order(ocpl_id, crt_order_flags);
    		crtorderflags = H5.H5Pget_attr_creation_order(ocpl_id);
    	}
    	catch (Throwable err) {
    		err.printStackTrace();
    		fail("H5Pset_attr_creation_order: " + err);
    	} 
    	assertTrue("testH5Pset_attr_creation_order_trackedPLUSindexed", ret_val >= 0);
    	assertEquals(crt_order_flags, crtorderflags);
    }
    
    @Test
    public void testH5Pset_attr_creation_order_tracked() throws Throwable, HDF5LibraryException {
    	int ret_val = -1;
    	int crtorderflags = 0;

    	try {
    		ret_val = H5.H5Pset_attr_creation_order(ocpl_id, HDF5Constants.H5P_CRT_ORDER_TRACKED);
    		crtorderflags = H5.H5Pget_attr_creation_order(ocpl_id);
    	} 
    	catch (Throwable err) {
    		err.printStackTrace();
    		fail("H5Pset_attr_creation_order: " + err);
    	} 
    	assertTrue("testH5Pset_attr_creation_order_tracked", ret_val >= 0);
    	assertEquals(HDF5Constants.H5P_CRT_ORDER_TRACKED, crtorderflags);
    }
    
    @Test(expected = HDF5LibraryException.class)
	public void testH5Pset_attr_creation_order_invalidvalue() throws Throwable, HDF5LibraryException {
    	H5.H5Pset_attr_creation_order(ocpl_id, HDF5Constants.H5P_CRT_ORDER_INDEXED);
	}
	
	@Test
	public void testH5Pset_copy_object() throws Throwable, HDF5LibraryException {
	
		int cpy_option = -1;

		try {
			H5.H5Pset_copy_object(ocp_plist_id, HDF5Constants.H5O_COPY_SHALLOW_HIERARCHY_FLAG);
			cpy_option = H5.H5Pget_copy_object(ocp_plist_id);	
		} 
		catch (Throwable err) {
			err.printStackTrace();
			fail("H5Pset_copy_object: " + err);
		} 
		assertEquals(HDF5Constants.H5O_COPY_SHALLOW_HIERARCHY_FLAG, cpy_option);
	}
	
	@Test(expected = HDF5LibraryException.class)
	public void testH5Pset_copy_object_invalidobject() throws Throwable, HDF5LibraryException {
		H5.H5Pset_copy_object(HDF5Constants.H5P_DEFAULT, HDF5Constants.H5O_COPY_SHALLOW_HIERARCHY_FLAG);
	}

	@Test
	public void testH5Pset_create_intermediate_group() throws Throwable, HDF5LibraryException {
	
		int ret_val = -1;
		try {
			ret_val = H5.H5Pset_create_intermediate_group(lcpl_id, true);
		}
		catch (Throwable err) {
			err.printStackTrace();
			fail("H5Pset_create_intermediate_group: " + err);
		} 
		assertTrue(ret_val>=0);
	}
	
	@Test(expected = HDF5LibraryException.class)
	public void testH5Pset_create_intermediate_group_invalidobject() throws Throwable, HDF5LibraryException {
		H5.H5Pset_create_intermediate_group(ocp_plist_id, true);
	}
	
	@Test
	public void testH5Pget_create_intermediate_group() throws Throwable, HDF5LibraryException {
		boolean flag = false;
		try {
			H5.H5Pset_create_intermediate_group(lcpl_id, true);
			flag = H5.H5Pget_create_intermediate_group(lcpl_id);	
		} 
		catch (Throwable err) {
			err.printStackTrace();
			fail("H5Pget_create_intermediate_group: " + err);
		}
		assertEquals(true, flag);
	}
	
	@Test
	public void testH5Pget_create_intermediate_group_notcreated() throws Throwable, HDF5LibraryException {
		boolean flag = true;
		try {
			flag = H5.H5Pget_create_intermediate_group(lcpl_id);	
		} 
		catch (Throwable err) {
			err.printStackTrace();
			fail("H5Pget_create_intermediate_group_notcreated: " + err);
		}
		assertEquals(false, flag);
	}
	
	@Test
	public void testH5Pset_data_transform() throws Throwable, HDF5LibraryException, NullPointerException {
		
		String expression = "(5/9.0)*(x-32)";
		int ret_val = -1;

		try {
			ret_val= H5.H5Pset_data_transform(plist_id, expression);	
		} 
		catch (Throwable err) {
			err.printStackTrace();
			fail("H5Pset_data_transform: " + err);
		}
		assertTrue(ret_val>=0);
		
	}
	
	@Test(expected = NullPointerException.class)
	public void testH5Pset_data_transform_NullExpression() throws Throwable, HDF5LibraryException, NullPointerException {
		H5.H5Pset_data_transform(plist_id, null);
	}
	
	@Test(expected = HDF5LibraryException.class)
	public void testH5Pset_data_transform_InvalidExpression1() throws Throwable, HDF5LibraryException, NullPointerException {
		H5.H5Pset_data_transform(plist_id, "");
	}
	
	@Test(expected = HDF5LibraryException.class)
	public void testH5Pset_data_transform_InvalidExpression2() throws Throwable, HDF5LibraryException, NullPointerException {
		H5.H5Pset_data_transform(plist_id, "hello");
	}
	
	@Test
	public void testH5Pget_data_transform() throws Throwable, HDF5LibraryException, IllegalArgumentException {
		
		String expression = "(5/9.0)*(x-32)";
		String [] express = {""};
		long express_size = 0;
		long size = 20;

		try {
			H5.H5Pset_data_transform(plist_id, expression);	
			express_size = H5.H5Pget_data_transform(plist_id, express, size);
		} 
		catch (Throwable err) {
			err.printStackTrace();
			fail("H5Pget_data_transform: " + err);
		}
		assertTrue(express_size>=0);
		assertTrue("The data transform expression: ", expression.equals(express[0]));
	}
	
	@Test(expected = HDF5LibraryException.class)
	public void testH5Pget_data_transform_ExpressionNotSet() throws Throwable, HDF5LibraryException, IllegalArgumentException {
		String [] express = {""};
		H5.H5Pget_data_transform(plist_id, express, 20);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testH5Pget_data_transform_IllegalSize() throws Throwable, HDF5LibraryException, IllegalArgumentException {
		String [] express = {""};
		H5.H5Pset_data_transform(plist_id, "(5/9.0)*(x-32)");
		H5.H5Pget_data_transform(plist_id, express, 0);
	}
	
	@Test
    public void testH5Pget_elink_acc_flags() throws Throwable, HDF5LibraryException {
		
		int get_flags = -1;
		try {
			get_flags = H5.H5Pget_elink_acc_flags(gapl_id);
		}
		catch (Throwable err) {
			err.printStackTrace();
			fail("H5Pget_elink_acc_flags: " + err);
		}
		assertTrue("H5Pget_elink_acc_flags", get_flags >= 0);
		assertEquals(HDF5Constants.H5F_ACC_DEFAULT, get_flags);
	}
	
	@Test
    public void testH5Pset_elink_acc_flags() throws Throwable, HDF5LibraryException, IllegalArgumentException {
		
		int get_flags = -1;
		int ret_val = -1;
		try {
			ret_val = H5.H5Pset_elink_acc_flags(lapl_id, HDF5Constants.H5F_ACC_RDWR);
			get_flags = H5.H5Pget_elink_acc_flags(lapl_id);
		}
		catch (Throwable err) {
			err.printStackTrace();
			fail("H5Pset_elink_acc_flags: " + err);
		}
		assertTrue("H5Pset_elink_acc_flags", ret_val >= 0);
		assertEquals(HDF5Constants.H5F_ACC_RDWR, get_flags);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testH5Pset_elink_acc_flags_InvalidFlag1() throws Throwable, HDF5LibraryException, IllegalArgumentException {
		H5.H5Pset_elink_acc_flags(lapl_id, HDF5Constants.H5F_ACC_TRUNC);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testH5Pset_elink_acc_flags_InvalidFlag2() throws Throwable, HDF5LibraryException, IllegalArgumentException {
		H5.H5Pset_elink_acc_flags(lapl_id, -1);
	}
	
}
