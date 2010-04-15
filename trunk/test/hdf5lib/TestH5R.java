package test.hdf5lib;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.junit.Assert.assertEquals;

import java.io.File;

import ncsa.hdf.hdf5lib.H5;
import ncsa.hdf.hdf5lib.HDF5Constants;
import ncsa.hdf.hdf5lib.exceptions.HDF5Exception;
import ncsa.hdf.hdf5lib.exceptions.HDF5LibraryException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestH5R {
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
    public void testH5Rget_name() throws Throwable, HDF5LibraryException {
        int loc_id=H5fid, ref_type=HDF5Constants.H5R_OBJECT;
        long ret_val=-1, size=-1;
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

 
}
