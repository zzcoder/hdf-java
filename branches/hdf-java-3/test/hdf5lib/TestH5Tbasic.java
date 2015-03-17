package test.hdf5lib;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import ncsa.hdf.hdf5lib.H5;
import ncsa.hdf.hdf5lib.HDF5Constants;

import org.junit.Before;
import org.junit.Test;

public class TestH5Tbasic {

    @Before
    public void checkOpenIDs() {
        assertTrue("H5 open ids is 0",H5.getOpenIDCount()==0);
    }
    
    @Test
    public void testH5Tcopy() {
        int H5strdid = -1;
        try {
            H5strdid = H5.H5Tcopy(HDF5Constants.H5T_C_S1);
            assertTrue("H5.H5Tcopy",H5strdid > 0);
        }
        catch (Throwable err) {
            err.printStackTrace();
            fail("testH5Tcopy: " + err);
        }
        finally {
            if (H5strdid >= 0)
                try {H5.H5Tclose(H5strdid);} catch (Exception ex) {}
        }
    }
    
    @Test
    public void testH5Tequal() {
        int H5strdid = -1;
        try {
            H5strdid = H5.H5Tcopy(HDF5Constants.H5T_C_S1);
            assertTrue("H5.H5Tcopy",H5strdid > 0);
            boolean teq = H5.H5Tequal(HDF5Constants.H5T_C_S1, H5strdid);
            assertTrue("H5.H5Tequal",teq);
        }
        catch (Throwable err) {
            err.printStackTrace();
            fail("testH5Tequal: " + err);
        }
        finally {
            if (H5strdid >= 0)
                try {H5.H5Tclose(H5strdid);} catch (Exception ex) {}
        }
    }

    @Test
    public void testH5Tequal_not() {
        int H5strdid = -1;
        try {
            H5strdid = H5.H5Tcopy(HDF5Constants.H5T_STD_U64LE);
            assertTrue("H5.H5Tcopy",H5strdid > 0);
            boolean teq = H5.H5Tequal(HDF5Constants.H5T_IEEE_F32BE, H5strdid);
            assertFalse("H5.H5Tequal",teq);
        }
        catch (Throwable err) {
            err.printStackTrace();
            fail("testH5Tequal_not: " + err);
        }
        finally {
            if (H5strdid >= 0)
                try {H5.H5Tclose(H5strdid);} catch (Exception ex) {}
        }
    }
    
    @Test
    public void testH5Tconvert() {
        String[] strs = {"a1234","b1234"};
        int srcLen = 5;
        int dstLen = 10;
        int srcId = -1;
        int dstId = -1;
        int dimSize = strs.length;
        byte[]   buf = new byte[dimSize*dstLen];
        
        for (int i=0; i<dimSize; i++)
            System.arraycopy(strs[i].getBytes(), 0, buf, i*srcLen, 5);
   
        try {
            srcId = H5.H5Tcopy(HDF5Constants.H5T_C_S1);
            H5.H5Tset_size(srcId, srcLen);
     
            dstId = H5.H5Tcopy(HDF5Constants.H5T_C_S1);
            H5.H5Tset_size(dstId, dstLen);
     
            H5.H5Tconvert(srcId, dstId, dimSize, buf, null, HDF5Constants.H5P_DEFAULT);
        }
        catch (Throwable err) {
            err.printStackTrace();
            fail("testH5Tconvert: " + err);
        }
        finally {
            try {H5.H5Tclose(srcId);} catch (Exception ex) {}
            try {H5.H5Tclose(dstId);} catch (Exception ex) {}
        }
        
        for (int i=0; i<strs.length; i++) {
            assertTrue((new String(buf, i*dstLen, dstLen)).startsWith(strs[i]));
        }
    }
    
    @Test
    public void testH5Torder_size() {
        int H5strdid = -1;
        try {
            // Fixed length string
            H5strdid = H5.H5Tcopy(HDF5Constants.H5T_C_S1);
            assertTrue("H5.H5Tcopy",H5strdid > 0);
            H5.H5Tset_size(H5strdid, (long)5);
            assertTrue(HDF5Constants.H5T_ORDER_NONE == H5.H5Tget_order(H5strdid));
            H5.H5Tset_order(H5strdid, HDF5Constants.H5T_ORDER_NONE);
            assertTrue(HDF5Constants.H5T_ORDER_NONE == H5.H5Tget_order(H5strdid));
            assertTrue(5 == H5.H5Tget_size(H5strdid));

            // Variable length string
            H5.H5Tset_size(H5strdid, HDF5Constants.H5T_VARIABLE);
            H5.H5Tset_order(H5strdid, HDF5Constants.H5T_ORDER_BE);
            assertTrue(HDF5Constants.H5T_ORDER_BE == H5.H5Tget_order(H5strdid));
        }
        catch (Throwable err) {
            err.printStackTrace();
            fail("testH5Torder: " + err);
        }
        finally {
            if (H5strdid >= 0)
                try {H5.H5Tclose(H5strdid);} catch (Exception ex) {}
        }
    }
}
