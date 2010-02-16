package test.hdf5lib;

import static org.junit.Assert.assertEquals;
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

public class TestH5P {
	
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
    public void testH5Pget_nlinks() throws Throwable, HDF5LibraryException {
    	
    		  int lapl_id = -1;
    		  long nlinks = -1;
              
              try {
            	  lapl_id = H5.H5Pcreate(HDF5Constants.H5P_DATASET_ACCESS);
              } catch (Throwable err) 
              {
              }
              
              try {
               nlinks = (long)H5.H5Pget_nlinks(lapl_id);
            	   
              }catch (Throwable err) {
                   err.printStackTrace();
                   fail("H5.H5Pget_nlinks: " + err);
              } finally
              {
            	   H5.H5Pclose(lapl_id);
              }
               
              //Check if the value of nLinks is greater than zero.
              assertTrue("testH5Pget_nlinks: H5Pget_nlinks", nlinks>0);
               
              //Check the default value of nlink.
              assertEquals(nlinks, 16L);
               
              //Negative Test - Error should be thrown when H5Pget_nlinks is called for the file who access has been closed.
              try{
            	   H5.H5Pget_nlinks(lapl_id);
            	   fail("Negative Test Failed:- Error not Thrown when Access to File is Closed.");
              }
              catch(AssertionError err){
            	   fail("H5.H5Pget_nlinks: " + err);
              }catch(Throwable err){}
    }
    
    @Test
    public void testH5Pset_nlinks() throws Throwable, HDF5LibraryException {
    	
    	int lapl_id = -1;
		long nlinks = 20;
		int ret_val = -1;
       
        try {
     	  lapl_id = H5.H5Pcreate(HDF5Constants.H5P_DATASET_ACCESS);
        } catch (Throwable err) 
        {
    	   err.printStackTrace();
        }
       
        try {
        	ret_val = H5.H5Pset_nlinks(lapl_id, nlinks);
            nlinks = (long)H5.H5Pget_nlinks(lapl_id);
        }catch (Throwable err) {
            err.printStackTrace();
            fail("H5.H5Pset_nlinks: " + err);
        } finally
        {
     	   H5.H5Pclose(lapl_id);
        }
        //Check the ret_val value, if its is negative then test fails.
        assertTrue("testH5Pset_nlinks: H5Pset_nlinks", ret_val>=0);
        
        //Check the value of nlinks retrieved from H5Pget_nlinks function.
        assertEquals(nlinks, 20L);

        //Negative Test - Error should be thrown when H5Pset_nlinks is called for the file who access has been closed.
        try{
     	   H5.H5Pset_nlinks(lapl_id, nlinks);
     	   fail("Negative Test Failed:- Error not Thrown when Access to File is Closed.");
        }
        catch(AssertionError err){
     	   fail("H5.H5Pset_nlinks: " + err);
        }catch(Throwable err){}
        
        
      
    	
    }
    
    @Test
    public void testH5Pget_libver_bounds() throws Throwable, HDF5LibraryException {
    	
       	int fapl_id = -1;
		int ret_val = -1;
		
		long []libver = new long[2];
		
        try {
        	fapl_id = H5.H5Pcreate(HDF5Constants.H5P_FILE_ACCESS);
          } catch (Throwable err) 
          {
      	   err.printStackTrace();
          }
          
          try {
        	  ret_val = H5.H5Pget_libver_bounds(fapl_id, libver);
          }catch (Throwable err) {
              err.printStackTrace();
              fail("H5Pget_libver_bounds: " + err);
          } finally
          {
       	   H5.H5Pclose(fapl_id);
          }
		
          //Check the ret_val value, if its is negative then test fails.
          assertTrue("testH5Pget_libver_bounds: H5Pset_nlinks", ret_val>=0);
          
          //Check the Earliest Version if the library
          assertEquals(HDF5Constants.H5F_LIBVER_EARLIEST, libver[0] );
          
          //Check the Latest Version if the library
          assertEquals(HDF5Constants.H5F_LIBVER_LATEST, libver[1] );
          
          //Negative Test - Error should be thrown when H5Pget_libver_bounds is called for the file who access has been closed.
          try{
        	 H5.H5Pget_libver_bounds(fapl_id, libver);
       	   fail("Negative Test Failed:- Error not Thrown when Access to File is Closed.");
          }
          catch(AssertionError err){
       	   fail("H5.H5Pget_libver_bounds: " + err);
          }catch(Throwable err){}
		
    }
}
