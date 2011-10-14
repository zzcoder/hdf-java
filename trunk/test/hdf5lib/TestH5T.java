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

public class TestH5T {
    private static final String H5_FILE = "test.h5";
    int H5fid = -1;
    int H5strdid = -1;

    private final void _deleteFile(String filename) {
        File file = null;
        try {
            file = new File(filename);
        } 
        catch (Throwable err) {}

        if (file.exists()) {
            try {
                file.delete();
            }
            catch (SecurityException e) {
                ;// e.printStackTrace();
            }
        }
    }

    @Before
    public void createH5file() throws NullPointerException, HDF5Exception {
        assertTrue("H5 open ids is 0", H5.getOpenIDCount()==0);

        H5fid = H5.H5Fcreate(H5_FILE, HDF5Constants.H5F_ACC_TRUNC,
                HDF5Constants.H5P_DEFAULT, HDF5Constants.H5P_DEFAULT);
        assertTrue("H5.H5Fcreate", H5fid > 0);
        H5strdid = H5.H5Tcopy(HDF5Constants.H5T_C_S1);
        assertTrue("H5.H5Tcopy", H5strdid > 0);

        H5.H5Fflush(H5fid, HDF5Constants.H5F_SCOPE_LOCAL);
    }

    @After
    public void deleteH5file() throws HDF5LibraryException {
        if (H5strdid >= 0)
            H5.H5Tclose(H5strdid);
        if (H5fid > 0) 
            H5.H5Fclose(H5fid);

        _deleteFile(H5_FILE);
    }
    
    @Test(expected = HDF5LibraryException.class)
    public void testH5Tequal_type_error() throws Throwable {
        H5.H5Tequal(HDF5Constants.H5T_INTEGER, H5strdid);
    }
    
    @Test
    public void testH5Tget_class() {
        try {
            int result = H5.H5Tget_class(H5strdid);
            assertTrue("H5.H5Tget_class", result > 0);
            String class_name = H5.H5Tget_class_name(result);
            assertTrue("H5.H5Tget_class", class_name.compareTo("H5T_STRING")==0);
        }
        catch (Throwable err) {
            err.printStackTrace();
            fail("testH5Tget_class: " + err);
        }
    }
    
    @Test
    public void testH5Tget_size() {
        long dt_size = -1; 
        try {
            dt_size = H5.H5Tget_size(H5strdid);
        }
        catch (Throwable err) {
            err.printStackTrace();
            fail("testH5Tget_size:H5.H5Tget_size " + err);
        }
        assertTrue("testH5Tget_size", dt_size > 0);
    }
    
    @Test
    public void testH5Tset_size() {
        long dt_size = 5; 
        try {
            H5.H5Tset_size(H5strdid, dt_size);
        }
        catch (Throwable err) {
            err.printStackTrace();
            fail("testH5Tset_size:H5.H5Tset_size " + err);
        }
        try {
            dt_size = H5.H5Tget_size(H5strdid);
        }
        catch (Throwable err) {
            err.printStackTrace();
            fail("testH5Tget_size:H5.H5Tget_size " + err);
        }
        assertTrue("testH5Tget_size", dt_size == 5);
    }
    
    @Test
    public void testH5Tarray_create() {
       int filetype_id = -1;
       long[] adims = { 3, 5 };

       try {
           filetype_id = H5.H5Tarray_create(HDF5Constants.H5T_STD_I64LE, 2, adims);
           assertTrue("testH5Tarray_create", filetype_id > 0);
       }
       catch (Throwable err) {
           err.printStackTrace();
           fail("testH5Tarray_create.H5Tarray_create " + err);
       }
       finally {
           if (filetype_id >= 0)
               try {H5.H5Tclose(filetype_id);} catch (Exception ex) {}
       }
    }
    
    @Test
    public void testH5Tget_array_dims() {
       int filetype_id = -1;
       int ndims = 0;
       long[] adims = { 3, 5 };
       long[] rdims = new long[2];

       try {
           filetype_id = H5.H5Tarray_create(HDF5Constants.H5T_STD_I64LE, 2, adims);
       }
       catch (Throwable err) {
           err.printStackTrace();
           fail("testH5Tarray_create.H5Tarray_create " + err);
       }
       assertTrue("testH5Tget_array_dims:H5Tarray_create", filetype_id > 0);
       try {
           ndims = H5.H5Tget_array_dims(filetype_id, rdims);
           assertTrue("testH5Tget_array_dims", ndims == 2);
           assertTrue("testH5Tget_array_dims", adims[0] == rdims[0]);
           assertTrue("testH5Tget_array_dims", adims[1] == rdims[1]);
       }
       catch (Throwable err) {
           err.printStackTrace();
           fail("testH5Tget_array_dims.H5Tget_array_dims " + err);
       }
       finally {
           if (filetype_id >= 0)
               try {H5.H5Tclose(filetype_id);} catch (Exception ex) {}
       }
    }
    
    @Test
    public void testH5Tenum_functions() {
        int       tid=-1;
        String    enum_type="Enum_type";
        byte[]    enum_val = new byte[1];
        String    enum_name;

        /* Create a enumerate datatype */
        try {
            tid=H5.H5Tcreate(HDF5Constants.H5T_ENUM, (long)1);
        }
        catch (Throwable err) {
            err.printStackTrace();
            fail("testH5Tenum_functions:H5Tcreate " + err);
        }
        assertTrue("testH5Tenum_functions:H5Tcreate", tid > 0);
        try {
            enum_val[0]=10;
            H5.H5Tenum_insert(tid, "RED", enum_val);
            enum_val[0]=11;
            H5.H5Tenum_insert(tid, "GREEN", enum_val);
            enum_val[0]=12;
            H5.H5Tenum_insert(tid, "BLUE", enum_val);
            enum_val[0]=13;
            H5.H5Tenum_insert(tid, "ORANGE", enum_val);
            enum_val[0]=14;
            H5.H5Tenum_insert(tid, "YELLOW", enum_val);
        }
        catch (Throwable err) {
            err.printStackTrace();
            fail("testH5Tenum_functions:H5Tenum_insert " + err);
        }

        try {
            /* Query member number and member index by member name, for enumeration type. */
            assertTrue("Can't get member number", H5.H5Tget_nmembers(tid) == 5);
            assertTrue("Can't get correct index number", H5.H5Tget_member_index(tid, "ORANGE") == 3);
        }
        catch (Throwable err) {
            err.printStackTrace();
            fail("testH5Tenum_functions:H5Tget_nmembers " + err);
        }

        /* Commit enumeration datatype and close it */
        try {
            H5.H5Tcommit(H5fid, enum_type, tid, HDF5Constants.H5P_DEFAULT, HDF5Constants.H5P_DEFAULT, HDF5Constants.H5P_DEFAULT);
        }
        catch (Throwable err) {
            err.printStackTrace();
            fail("testH5Tenum_functions:H5Tcommit " + err);
        }
        try {
            if(tid > 0) 
                H5.H5Tclose(tid);
        }
        catch (Throwable err) {
            err.printStackTrace();
            fail("testH5Tenum_functions:H5Tclose " + err);
        }

        /* Open the dataytpe for query */
        try {
            tid = H5.H5Topen(H5fid, enum_type, HDF5Constants.H5P_DEFAULT);
        }
        catch (Throwable err) {
            err.printStackTrace();
            fail("testH5Tenum_functions:H5Topen2 " + err);
        }
        assertTrue("testH5Tenum_functions:H5Tcreate",tid > 0);

        try {
            /* Query member number and member index by member name, for enumeration type */
            assertTrue("Can't get member number", H5.H5Tget_nmembers(tid) == 5);
            assertTrue("Can't get correct index number", H5.H5Tget_member_index(tid, "ORANGE") == 3);
    
            /* Query member value by member name, for enumeration type */
            H5.H5Tenum_valueof (tid, "ORANGE", enum_val);
            assertTrue("Incorrect value for enum member", enum_val[0]==13);
    
            /* Query member value by member index, for enumeration type */
            H5.H5Tget_member_value (tid, 2, enum_val);
            assertTrue("Incorrect value for enum member", enum_val[0]==12);
    
            /* Query member name by member value, for enumeration type */
            enum_val[0] = 14;
            enum_name = H5.H5Tenum_nameof(tid, enum_val, 16);
            assertTrue("Incorrect name for enum member", enum_name.compareTo("YELLOW")==0);
        }
        catch (Throwable err) {
            err.printStackTrace();
            fail("testH5Tenum_functions:query " + err);
        }

        /* Close datatype and file */
        try {
            if(tid > 0) 
                H5.H5Tclose(tid);
        }
        catch (Throwable err) {
            err.printStackTrace();
            fail("testH5Tenum_functions:H5Tclose " + err);
        }
    }

}
