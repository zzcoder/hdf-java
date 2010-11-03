package test.hdf5lib;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import ncsa.hdf.hdf5lib.H5;
import ncsa.hdf.hdf5lib.HDF5Constants;
import ncsa.hdf.hdf5lib.exceptions.HDF5LibraryException;

import org.junit.Before;
import org.junit.Test;

public class TestH5Tbasic {

    @Before
    public void checkOpenIDs() {
        assertTrue("H5 open ids is 0",H5.getOpenIDCount()==0);
    }
    
    @Test
    public void testH5Tcopy() throws Throwable, HDF5LibraryException {
        int H5strdid = H5.H5Tcopy(HDF5Constants.H5T_C_S1);
        assertTrue("H5.H5Tcopy",H5strdid > 0);
        if (H5strdid >= 0)
            H5.H5Tclose(H5strdid);
    }
    
    @Test
    public void testH5Tequal() throws Throwable, HDF5LibraryException {
        int H5strdid = H5.H5Tcopy(HDF5Constants.H5T_C_S1);
        assertTrue("H5.H5Tcopy",H5strdid > 0);
        boolean teq = H5.H5Tequal(HDF5Constants.H5T_C_S1, H5strdid);
        if (H5strdid >= 0)
            H5.H5Tclose(H5strdid);
        assertTrue("H5.H5Tequal",teq);
    }

    @Test
    public void testH5Tequal_not() throws Throwable, HDF5LibraryException {
        int H5strdid = H5.H5Tcopy(HDF5Constants.H5T_STD_U64LE);
        assertTrue("H5.H5Tcopy",H5strdid > 0);
        boolean teq = H5.H5Tequal(HDF5Constants.H5T_IEEE_F32BE, H5strdid);
        assertFalse("H5.H5Tequal",teq);
        if (H5strdid >= 0)
            H5.H5Tclose(H5strdid);
    }
    
    @Test
    public void testH5Tconvert() throws Throwable, HDF5LibraryException {
        String[] strs = {"a1234","b1234"};
        int srcLen=5, dstLen=10, srcId=-1, dstId=-1, dimSize=strs.length;
        byte[]   buf = new byte[dimSize*dstLen];
        
        for (int i=0; i<dimSize; i++)
        	System.arraycopy(strs[i].getBytes(), 0, buf, i*srcLen, 5);
   
        for (int i=0; i<dimSize; i++)
        	System.out.println(new String(buf, i*srcLen, srcLen));
        
        srcId = H5.H5Tcopy(HDF5Constants.H5T_C_S1);
        H5.H5Tset_size(srcId, srcLen);
 
        dstId = H5.H5Tcopy(HDF5Constants.H5T_C_S1);
        H5.H5Tset_size(dstId, dstLen);
 
        H5.H5Tconvert(srcId, dstId, dimSize, buf, null, HDF5Constants.H5P_DEFAULT);
        
        H5.H5Tclose(srcId);
        H5.H5Tclose(dstId);
        
        for (int i=0; i<strs.length; i++) {
        	assertTrue((new String(buf, i*dstLen, dstLen)).startsWith(strs[i]));
        }
    }
}
