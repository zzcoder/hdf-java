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

public class TestH5Ocopy {
    private static final String FILENAME = "testRefsattribute.h5";
    private static final int DIM_X = 4;
    private static final int DIM_Y = 6;
    int H5fid = -1;
    int H5dsid = -1;
    int H5did1 = -1;
    int H5did2 = -1;
    int H5gcpl = -1;
    int H5gid = -1;
    int H5dsid2 = -1;
    long[] dims = { 2 };

    private final void _deleteFile(String filename) {
        File file = new File(filename);

        if (file.exists()) {
            try {
                file.delete();
            }
            catch (Exception e) {
                e.printStackTrace();
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
        assertTrue("TestH5O._createDataset: ",did > 0);

        return did;
    }

    private final int _createGroup(int fid, String name) {
        int gid = -1;
        try {
            H5gcpl = HDF5Constants.H5P_DEFAULT;
            gid = H5.H5Gcreate(fid, name, HDF5Constants.H5P_DEFAULT,
                    H5gcpl, HDF5Constants.H5P_DEFAULT);
        }
        catch (Throwable err) {
            err.printStackTrace();
            fail("H5.H5Gcreate: " + err);
        }
        assertTrue("TestH5O._createGroup: ",gid > 0);

        return gid;
    }

    @Before
    public void createH5file()
            throws NullPointerException, HDF5Exception {
        assertTrue("H5 open ids is 0",H5.getOpenIDCount()==0);
        try {
            H5fid = H5.H5Fcreate(FILENAME, HDF5Constants.H5F_ACC_TRUNC,
                    HDF5Constants.H5P_DEFAULT, HDF5Constants.H5P_DEFAULT);
            H5dsid2 =  H5.H5Screate(HDF5Constants.H5S_SCALAR);
            H5did1 = _createDataset(H5fid, H5dsid2, "DS2", HDF5Constants.H5P_DEFAULT);
            H5dsid = H5.H5Screate_simple(1, dims, null);         
            H5gid = _createGroup(H5fid, "/G1");
            H5did2 = _createDataset(H5gid, H5dsid, "DS1", HDF5Constants.H5P_DEFAULT);
        }
        catch (Throwable err) {
            err.printStackTrace();
            fail("TestH5O.createH5file: " + err);
        }
        assertTrue("TestH5O.createH5file: H5.H5Fcreate: ",H5fid > 0);
        assertTrue("TestH5O.createH5file: H5.H5Screate_simple: ",H5dsid > 0);
        assertTrue("TestH5O.createH5file: H5.H5Gcreate: ",H5gid > 0);

        H5.H5Fflush(H5fid, HDF5Constants.H5F_SCOPE_LOCAL);
    }

    @After
    public void deleteH5file() throws HDF5LibraryException {
        if (H5gid > 0) 
            try {H5.H5Gclose(H5gid);} catch (Exception ex) {}
        if (H5did2 > 0) 
            try {H5.H5Dclose(H5did2);} catch (Exception ex) {}
        if (H5dsid > 0) 
            try {H5.H5Sclose(H5dsid);} catch (Exception ex) {}
        if (H5dsid2 > 0) 
            try {H5.H5Sclose(H5dsid2);} catch (Exception ex) {}
        if (H5did1 > 0) 
            try {H5.H5Dclose(H5did1);} catch (Exception ex) {}
        if (H5fid > 0) 
            try {H5.H5Fclose(H5fid);} catch (Exception ex) {}

        _deleteFile(FILENAME);
    }
    
    @Test
    public void testH5OcopyRefsAttr() {
        int ocp_plist_id = -1;
        byte rbuf0[]=null , rbuf1[] = null;
        byte[] dset_data = new byte[16];
        int attribute_id = -1;
        
        
        try {
            rbuf0 = H5.H5Rcreate(H5fid, "/G1", HDF5Constants.H5R_OBJECT, -1);
            rbuf1 = H5.H5Rcreate(H5fid, "DS2", HDF5Constants.H5R_OBJECT, -1);
            //System.arraycopy(rbuf0, 0, dset_data, 0, 8);
            System.arraycopy(rbuf1, 0, dset_data, 8, 8);

            attribute_id = H5.H5Acreate(H5did2, "A1", HDF5Constants.H5T_STD_REF_OBJ, H5dsid, HDF5Constants.H5P_DEFAULT, HDF5Constants.H5P_DEFAULT);
            H5.H5Awrite(attribute_id, HDF5Constants.H5T_STD_REF_OBJ, dset_data);
            
            H5.H5Aclose(attribute_id);
        }
        catch (Exception ex) {
            try {H5.H5Aclose(attribute_id);} catch (Exception exx) {}
            fail("testH5OcopyRefsAttr: H5Ocopy failed");
        }
        try {
            ocp_plist_id = H5.H5Pcreate(HDF5Constants.H5P_OBJECT_COPY);
            H5.H5Pset_copy_object(ocp_plist_id, HDF5Constants.H5O_COPY_EXPAND_REFERENCE_FLAG);
            H5.H5Ocopy(H5fid, ".", H5fid, "CPYREF", ocp_plist_id, HDF5Constants.H5P_DEFAULT);        
        }
        catch (Exception ex) {
            try {H5.H5Pclose(ocp_plist_id);} catch (Exception exx) {}
            fail("testH5OcopyRefsAttr: H5Ocopy failed");
        }
        try {H5.H5Pclose(ocp_plist_id);} catch (Exception ex) {}
    }
    
    @Test
    public void testH5OcopyRefsDatasettodiffFile() {
        byte rbuf1[] = null;
        byte[] dset_data = new byte[16];
        int ocp_plist_id = -1;
        int dataset_id = -1;        
        int H5fid2 = -1;        
        
        try{
            rbuf1 = H5.H5Rcreate(H5fid, "DS2", HDF5Constants.H5R_OBJECT, -1);
            System.arraycopy(rbuf1, 0, dset_data, 8, 8);
            
            dataset_id = H5.H5Dcreate(H5fid, "DSREF",
                    HDF5Constants.H5T_STD_REF_OBJ, H5dsid,
                    HDF5Constants.H5P_DEFAULT, HDF5Constants.H5P_DEFAULT, HDF5Constants.H5P_DEFAULT);
            H5.H5Dwrite(dataset_id, HDF5Constants.H5T_STD_REF_OBJ,
                    HDF5Constants.H5S_ALL, HDF5Constants.H5S_ALL,
                    HDF5Constants.H5P_DEFAULT, dset_data);
            H5.H5Dclose(dataset_id);
        }
        catch (Exception ex) {
            try {H5.H5Dclose(dataset_id);} catch (Exception exx) {}
            fail("testH5OcopyRefsDatasettodiffFile: H5Ocopy failed");
        }
        
        try {
            //create new file
            H5fid2 = H5.H5Fcreate("copy.h5", HDF5Constants.H5F_ACC_TRUNC,
                    HDF5Constants.H5P_DEFAULT, HDF5Constants.H5P_DEFAULT);
            H5.H5Fflush(H5fid2, HDF5Constants.H5F_SCOPE_LOCAL);
        }
        catch (Exception ex) {
            try {H5.H5Fclose(H5fid2);} catch (Exception exx) {}
            fail("testH5OcopyRefsDatasettodiffFile: H5Ocopy failed");
        }
        
        try {
            //create object copy property list id and set the flags.
            ocp_plist_id = H5.H5Pcreate(HDF5Constants.H5P_OBJECT_COPY);
            H5.H5Pset_copy_object(ocp_plist_id, HDF5Constants.H5O_COPY_EXPAND_REFERENCE_FLAG);
            
            //Perform copy function.
            H5.H5Ocopy(H5fid, ".", H5fid2, "CPYREFD", ocp_plist_id, HDF5Constants.H5P_DEFAULT);
        }
        catch (Exception ex){
            ex.printStackTrace();
            fail("testH5OcopyRefsDatasettodiffFile: H5Ocopy failed");
        }
            
            //Close file.
        try {H5.H5Fclose(H5fid2);} catch (Exception ex) {}
        try {H5.H5Pclose(ocp_plist_id);} catch (Exception ex) {}
    }
    
    @Test
    public void testH5OcopyRefsDatasettosameFile() {
        byte rbuf0[]=null , rbuf1[] = null;
        byte[] dset_data = new byte[16];
        int ocp_plist_id = -1;
        int dataset_id = -1;
        int did = -1;
        int obj_type = -1;
        int[] otype = { 1 };
        byte[] read_data = new byte[16];

        try {
            rbuf0 = H5.H5Rcreate(H5fid, "/G1", HDF5Constants.H5R_OBJECT, -1);
            rbuf1 = H5.H5Rcreate(H5fid, "DS2", HDF5Constants.H5R_OBJECT, -1);
            System.arraycopy(rbuf0, 0, dset_data, 0, 8);
            System.arraycopy(rbuf1, 0, dset_data, 8, 8);

            //Create a dataset and write object references to it.
            dataset_id = H5.H5Dcreate(H5fid, "DSREF",
                    HDF5Constants.H5T_STD_REF_OBJ, H5dsid,
                    HDF5Constants.H5P_DEFAULT, HDF5Constants.H5P_DEFAULT, HDF5Constants.H5P_DEFAULT);
            H5.H5Dwrite(dataset_id, HDF5Constants.H5T_STD_REF_OBJ,
                    HDF5Constants.H5S_ALL, HDF5Constants.H5S_ALL,
                    HDF5Constants.H5P_DEFAULT, dset_data);
            //Close the dataset.
            H5.H5Dclose(dataset_id);
        }
        catch (Exception ex) {
            try {H5.H5Dclose(dataset_id);} catch (Exception exx) {}
            fail("testH5OcopyRefsDatasettosameFile: H5Ocopy failed");
        }

        try {

            ocp_plist_id = H5.H5Pcreate(HDF5Constants.H5P_OBJECT_COPY);
            H5.H5Pset_copy_object(ocp_plist_id, HDF5Constants.H5O_COPY_EXPAND_REFERENCE_FLAG);
        }
        catch (Exception ex) {
            try {H5.H5Pclose(ocp_plist_id);} catch (Exception exx) {}
            fail("testH5OcopyRefsDatasettosameFile: H5Ocopy failed");
        }

        //Perform copy function.
        try {
            H5.H5Ocopy(H5fid, "DSREF", H5fid, "CPYREFD", ocp_plist_id, HDF5Constants.H5P_DEFAULT);
        }
        catch(Exception ex) {
            try {H5.H5Pclose(ocp_plist_id);} catch (Exception exx) {}
            fail("testH5OcopyRefsDatasettosameFile: H5Ocopy failed");
        }

        //Open the dataset that has been copied
        try {
            did = H5.H5Dopen(H5fid, "DSREF", HDF5Constants.H5P_DEFAULT);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        
        try {
            //Read the dataset object references in the read_data buffer.
            H5.H5Dread(did, HDF5Constants.H5T_STD_REF_OBJ, HDF5Constants.H5S_ALL,HDF5Constants.H5S_ALL, HDF5Constants.H5P_DEFAULT, read_data);
            System.arraycopy(read_data, 0, rbuf0, 0, 8);
            System.arraycopy(read_data, 8, rbuf1, 0, 8);    

            //Get the type of object the reference points to.
            obj_type = H5.H5Rget_obj_type(H5fid, HDF5Constants.H5R_OBJECT, rbuf1, otype);
            assertEquals(obj_type, HDF5Constants.H5O_TYPE_DATASET);

            obj_type = H5.H5Rget_obj_type(H5fid, HDF5Constants.H5R_OBJECT, rbuf0, otype);
            assertEquals(obj_type, HDF5Constants.H5O_TYPE_GROUP);

            //close the dataset
            H5.H5Dclose(did);
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        
        try {H5.H5Dclose(did);} catch (Exception ex) {}
        try {H5.H5Pclose(ocp_plist_id);} catch (Exception ex) {}
    }
    
    @Test
    public void testH5OcopyInvalidRef() { 
    	final int _pid_ = HDF5Constants.H5P_DEFAULT;
        int sid = -1;
        int did = -1;
        int aid = -1;

    	try {
    		sid = H5.H5Screate_simple(1, new long[] {1}, null);
    		did = H5.H5Dcreate(H5fid, "Dataset_with_invalid_Ref", HDF5Constants.H5T_NATIVE_INT, sid, _pid_, _pid_, _pid_);
    		aid = H5.H5Acreate(did, "Invalid_Ref", HDF5Constants.H5T_STD_REF_OBJ, sid, _pid_, _pid_);
    		H5.H5Awrite(aid, HDF5Constants.H5T_STD_REF_OBJ, new long[]{-1});
    		H5.H5Dclose(did);
    		H5.H5Aclose(aid);
    		H5.H5Sclose(sid);
    	} 
    	catch (Exception ex) {
            try {H5.H5Dclose(did);} catch (Exception exx) {}
            try {H5.H5Aclose(aid);} catch (Exception exx) {}
            try {H5.H5Sclose(sid);} catch (Exception exx) {}
    	}			

    	try {
    		int ocp_plist_id = H5.H5Pcreate(HDF5Constants.H5P_OBJECT_COPY);
    		H5.H5Pset_copy_object(ocp_plist_id, HDF5Constants.H5O_COPY_EXPAND_REFERENCE_FLAG);
    		try {
    			H5.H5Ocopy(H5fid, "/Dataset_with_invalid_Ref", H5fid, "/Dataset_with_invalid_Ref_cp", ocp_plist_id, _pid_);
    		} finally { H5.H5Pclose(ocp_plist_id);}

    	} catch (Exception ex) {}
    }        
 

}
