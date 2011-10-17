package test.hdf5lib;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;

import ncsa.hdf.hdf5lib.H5;
import ncsa.hdf.hdf5lib.HDF5Constants;
import ncsa.hdf.hdf5lib.exceptions.HDF5Exception;
import ncsa.hdf.hdf5lib.exceptions.HDF5LibraryException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestH5R {
    private static final String H5_FILE = "testH5R.h5";
    private static final int DIM_X = 4;
    private static final int DIM_Y = 6;
    int H5fid = -1;
    int H5dsid = -1;
    int H5did = -1;
    int H5gid = -1;
    int H5did2 = -1;
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
        assertTrue("TestH5R._createDataset: ",did > 0);

        return did;
    }
    
    private final int _createGroup(int fid, String name) {
        int gid = -1;
        try {
            gid = H5.H5Gcreate(fid, name, HDF5Constants.H5P_DEFAULT,
            		HDF5Constants.H5P_DEFAULT, HDF5Constants.H5P_DEFAULT);
        }
        catch (Throwable err) {
            err.printStackTrace();
            fail("H5.H5Gcreate: " + err);
        }
        assertTrue("TestH5R._createGroup: ",gid > 0);

        return gid;
    }

    @Before
    public void createH5file()
            throws NullPointerException, HDF5Exception {
        assertTrue("H5 open ids is 0",H5.getOpenIDCount()==0);

        try {
            H5fid = H5.H5Fcreate(H5_FILE, HDF5Constants.H5F_ACC_TRUNC,
                    HDF5Constants.H5P_DEFAULT, HDF5Constants.H5P_DEFAULT);
            H5dsid = H5.H5Screate_simple(2, H5dims, null);
            H5gid = _createGroup(H5fid, "Group1");
            H5did2 = _createDataset(H5gid, H5dsid, "dset2", HDF5Constants.H5P_DEFAULT);
            H5did = _createDataset(H5fid, H5dsid, "dset", HDF5Constants.H5P_DEFAULT);
          
        }
        catch (Throwable err) {
            err.printStackTrace();
            fail("TestH5D.createH5file: " + err);
        }
        assertTrue("TestH5D.createH5file: H5.H5Fcreate: ",H5fid > 0);
        assertTrue("TestH5D.createH5file: H5.H5Screate_simple: ",H5dsid > 0);
        assertTrue("TestH5D.createH5file: _createDataset: ",H5did > 0);

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
        if (H5gid > 0) 
            H5.H5Gclose(H5gid);
        if (H5did2 > 0) 
            H5.H5Dclose(H5did2); 
 
        _deleteFile(H5_FILE);
    }

    @Test
    public void testH5Rget_name() throws Throwable, HDF5LibraryException {
        int loc_id=H5fid, ref_type=HDF5Constants.H5R_OBJECT;
        long ret_val=-1;
        byte[] ref=null;
        String[] name= {""};
        String objName = "/dset";
        
        ref = H5.H5Rcreate(H5fid, objName, ref_type, -1);
        
        try {
        	ret_val = H5.H5Rget_name(loc_id, ref_type, ref, name, 16);
        } catch (Throwable err) {
            err.printStackTrace();
            fail("H5.H5Rget_name: " + err);
        }
        
        assertTrue("testH5Rget_name: H5Rget_name", ret_val>0);
        
        assertTrue("The name of the object: ", objName.equals(name[0]));
        
    }
    
    @Test
    public void testH5Rget_obj_type2() throws Throwable, HDF5LibraryException {
    	int  ref_type=HDF5Constants.H5R_OBJECT;
    	byte[] ref=null;

    	String objName = "/dset";
    	int obj_type = -1;;
    	int[] otype = { 1 };

    	try{
    		ref = H5.H5Rcreate(H5fid, objName, ref_type, -1);
    	}
    	catch(Throwable err) {
    		err.printStackTrace();
    	}

    	try {
    		obj_type = H5.H5Rget_obj_type(H5fid, HDF5Constants.H5R_OBJECT, ref, otype);
    	} 
    	catch (Throwable err) {
    		err.printStackTrace();
    		fail("H5.H5Rget_obj_type2: " + err);
    	}
    	assertEquals(obj_type, HDF5Constants.H5O_TYPE_DATASET);        
    }

    @Test
    public void testH5Rcreate_refobj() throws Throwable, HDF5LibraryException {
    	byte[] ref = null;

    	try{
    		ref = H5.H5Rcreate(H5fid, "/dset", HDF5Constants.H5R_OBJECT, -1);
    	}
    	catch (Throwable err) {
    		err.printStackTrace();
    		fail("H5.H5Rcreate: " + err);
    	}
    	assertNotNull(ref);      
    }
    
    @Test
    public void testH5Rcreate_regionrefobj() throws Throwable, HDF5LibraryException {
    	byte[] ref = null;
    	try{
    		ref = H5.H5Rcreate(H5fid, "/dset", HDF5Constants.H5R_DATASET_REGION, H5dsid);
    	}
    	catch (Throwable err) {
    		err.printStackTrace();
    		fail("H5.H5Rcreate: " + err);
    	}
    	assertNotNull(ref);
    }
    
    @Test
    public void testH5Rdereference() throws Throwable, HDF5LibraryException {
    	byte[] ref1 = null;
    	byte[] ref2 = null;
    	int dataset_id = -1;
    	int group_id = -1;
    	try{
    		//Create reference on dataset 
    		ref1 = H5.H5Rcreate(H5fid, "/dset", HDF5Constants.H5R_DATASET_REGION, H5dsid);
    		dataset_id= H5.H5Rdereference(H5fid, HDF5Constants.H5R_DATASET_REGION, ref1);
    		
    		//Create reference on group
    		ref2 = H5.H5Rcreate(H5gid, "/Group1", HDF5Constants.H5R_OBJECT, -1);   		
    		group_id= H5.H5Rdereference(H5gid, HDF5Constants.H5R_OBJECT, ref2);
    	}
    	catch (Throwable err) {
    		err.printStackTrace();
    		fail("TestH5Rdereference " + err);
    	}
    	assertNotNull(ref1);
    	assertNotNull(ref2);
    	assertTrue(dataset_id>=0);
    	assertTrue(group_id>=0);

    	try{
    		H5.H5Dclose(dataset_id);
    	}
    	catch (Exception ex) {
    	}
    	try{
    		H5.H5Gclose(group_id);
    	}
    	catch (Exception ex) {
    	}
    }
    
    @Test
    public void testH5Rget_region() throws Throwable, HDF5LibraryException {
    	byte[] ref = null;
    	int dsid = -1;
    	try{
    		ref = H5.H5Rcreate(H5fid, "/dset", HDF5Constants.H5R_DATASET_REGION, H5dsid);
    		dsid = H5.H5Rget_region(H5fid, HDF5Constants.H5R_DATASET_REGION, ref);
    	}
    	catch (Throwable err) {
    		err.printStackTrace();
    		fail("TestH5Rget_region: " + err);
    	}
    	assertNotNull(ref);
    	assertTrue(dsid>=0);
    	
    	try{
    		H5.H5Sclose(dsid);
    	}
    	catch (Exception ex) {
    	}
    }
    
    @Test(expected = HDF5LibraryException.class)
    public void testH5Rget_name_Invalidreftype() throws Throwable, HDF5LibraryException, NullPointerException {
    	byte[] ref = null;
    	String[] name= {""};
    	ref = H5.H5Rcreate(H5fid, "/dset", HDF5Constants.H5R_OBJECT, -1);
    	H5.H5Rget_name(H5fid, HDF5Constants.H5R_DATASET_REGION, ref, name, 16);
    }
    
    @Test(expected = NullPointerException.class)
    public void testH5Rget_name_NULLreference() throws Throwable, HDF5LibraryException, NullPointerException {
    	byte[] ref = null;
    	String[] name= {""}; 	
    	H5.H5Rget_name(H5fid, HDF5Constants.H5R_OBJECT, ref, name, 16);
    }
    
    @Test(expected = HDF5LibraryException.class)
    public void testH5Rget_obj_type2_Invalidreftype() throws Throwable, HDF5LibraryException, NullPointerException {
    	byte[] ref = null;
    	int[] otype = { 1 };
    	ref = H5.H5Rcreate(H5fid, "/dset", HDF5Constants.H5R_OBJECT, -1);
    	H5.H5Rget_obj_type(H5fid, HDF5Constants.H5R_DATASET_REGION, ref, otype);
    }
    
    @Test(expected = HDF5LibraryException.class)
	public void testH5Rcreate_InvalidObjectName() throws Throwable, HDF5LibraryException, NullPointerException {
    	 H5.H5Rcreate(H5fid, "/GROUPS", HDF5Constants.H5R_OBJECT, -1);
	}
    
    @Test(expected = HDF5LibraryException.class)
	public void testH5Rcreate_Invalidspace_id() throws Throwable, HDF5LibraryException, NullPointerException {
    	 H5.H5Rcreate(H5fid, "/dset", HDF5Constants.H5R_DATASET_REGION, -1);
	}
    
    @Test(expected = IllegalArgumentException.class)
	public void testH5Rcreate_Invalidreftype() throws Throwable, HDF5LibraryException, NullPointerException {
    	H5.H5Rcreate(H5fid, "/dset", HDF5Constants.H5R_BADTYPE, -1);
	}
    
    @Test(expected = IllegalArgumentException.class)
    public void testH5Rgetregion_Invalidreftype() throws Throwable, HDF5LibraryException, NullPointerException {
    	byte[] ref = null;
    	ref = H5.H5Rcreate(H5fid, "/dset", HDF5Constants.H5R_OBJECT, H5dsid);
    	H5.H5Rget_region(H5fid, HDF5Constants.H5R_DATASET_REGION, ref);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testH5Rgetregion_Badreferencetype() throws Throwable, HDF5LibraryException, NullPointerException {
    	byte[] ref = null;
    	ref = H5.H5Rcreate(H5fid, "/dset", HDF5Constants.H5R_OBJECT, H5dsid);
    	H5.H5Rget_region(H5fid, HDF5Constants.H5R_OBJECT, ref);
    }
    
    @Test(expected = NullPointerException.class)
    public void testH5Rgetregion_Nullreference() throws Throwable, HDF5LibraryException, NullPointerException {
    	byte[] ref = null;
    	H5.H5Rget_region(H5fid, HDF5Constants.H5R_DATASET_REGION, ref);
    }
    
    @Test(expected = NullPointerException.class)
    public void testH5Rdereference_Nullreference() throws Throwable, HDF5LibraryException, NullPointerException {
    	byte[] ref = null;
    	H5.H5Rdereference(H5did2, HDF5Constants.H5R_OBJECT, ref);
    }
    
    @Test(expected = HDF5LibraryException.class)
    public void testH5Rdereference_Invalidreference() throws Throwable, HDF5LibraryException, NullPointerException {
    	byte[] ref1 = null;
    	byte[] ref2 = null;
    	ref1 = H5.H5Rcreate(H5fid, "/dset", HDF5Constants.H5R_DATASET_REGION, H5dsid);
    	ref2 = H5.H5Rcreate(H5gid, "/Group1", HDF5Constants.H5R_OBJECT, -1);
    	H5.H5Rdereference(H5gid, HDF5Constants.H5R_OBJECT, ref1);

    }
  
}