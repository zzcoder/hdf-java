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

    @Before
    public void createH5file()
            throws NullPointerException, HDF5Exception {
        assertTrue("H5 open ids is 0",H5.getOpenIDCount()==0);

        try {
            H5fid = H5.H5Fcreate(H5_FILE, HDF5Constants.H5F_ACC_TRUNC,
                    HDF5Constants.H5P_DEFAULT, HDF5Constants.H5P_DEFAULT);
            H5dsid = H5.H5Screate_simple(2, H5dims, null);
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
 
        _deleteFile(H5_FILE);
    }

    @Test
    public void testH5Acreate2() throws Throwable, HDF5LibraryException {
    	        
        int atrr_id = -1;
        int loc_id = H5did;
        String attr_name= "dset";
        int type_id = -1;
        int space_id = -1;
        int acpl_id = HDF5Constants.H5P_DEFAULT;
        int aapl_id = HDF5Constants.H5P_DEFAULT;
    	
        try{
        	type_id = H5.H5Tcreate(HDF5Constants.H5T_ENUM, (long)1);
        	space_id = H5.H5Screate(HDF5Constants.H5S_NULL);
        	}
        catch(Throwable err){
        	err.printStackTrace();
            fail("H5.H5Tcreate: " + err);
            fail("H5.H5Screate: " + err);
        	}
        
        try {
        	atrr_id = H5.H5Acreate2(loc_id, attr_name, type_id, space_id, acpl_id, aapl_id );
        	} 
        catch (Throwable err) {
            err.printStackTrace();
            fail("H5.H5Acreate2: " + err);
        	}
        finally{
        	H5.H5Tclose(type_id);
        	H5.H5Sclose(space_id);
        	H5.H5Aclose(atrr_id);
        	}
        
        assertTrue("testH5Acreate2: H5Acreate2", atrr_id>=0);
        
     
    }
    
    @Test
    public void testH5Aopen() throws Throwable, HDF5LibraryException {
    	
    	 int atrr_id = -1;
    	 int attribute_id = -1;
         int obj_id=  H5did;
         String attr_name= "dset";
         int type_id = -1;
         int space_id = -1;
         int acpl_id = HDF5Constants.H5P_DEFAULT;
         int aapl_id = HDF5Constants.H5P_DEFAULT;
     	
         try{
         	type_id = H5.H5Tcreate(HDF5Constants.H5T_ENUM, (long)1);
         	space_id = H5.H5Screate(HDF5Constants.H5S_NULL);
         	}
         catch(Throwable err){
         	err.printStackTrace();
             fail("H5.H5Tcreate: " + err);
             fail("H5.H5Screate: " + err);
         	}
         
         try {
        	 //Creating an attribute, attr_name attached to an object identifier.
         	atrr_id = H5.H5Acreate2(obj_id, attr_name, type_id, space_id, acpl_id, aapl_id );
         	} 
         catch (Throwable err) {
             err.printStackTrace();
             fail("H5.H5Acreate2: " + err);
         	}
         try {
        	  //Opening the existing attribute, attr_name(Created by H5ACreate2) attached to an object identifier.
        	 attribute_id = H5.H5Aopen(obj_id, attr_name, aapl_id);
         	} 
         catch (Throwable err) {
             err.printStackTrace();
             fail("H5.H5Aopen: " + err);
         	}
         finally{
         	H5.H5Tclose(type_id);
         	H5.H5Sclose(space_id);
         	H5.H5Aclose(atrr_id);
         	H5.H5Aclose(attribute_id);
         	}
         
         assertTrue("testH5Aopen: H5Aopen", attribute_id>=0);
       
    }
    

 
}
