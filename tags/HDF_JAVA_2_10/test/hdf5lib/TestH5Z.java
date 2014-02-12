package test.hdf5lib;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import ncsa.hdf.hdf5lib.H5;
import ncsa.hdf.hdf5lib.HDF5Constants;
import ncsa.hdf.hdf5lib.exceptions.HDF5LibraryException;

import org.junit.Test;

public class TestH5Z {
    
    @Test
    public void testH5Zfilter_avail() {
        try {
            int filter_found = H5.H5Zfilter_avail(HDF5Constants.H5Z_FILTER_DEFLATE);
            assertTrue("H5.H5Zfilter_avail", filter_found > 0);
            filter_found = H5.H5Zfilter_avail(HDF5Constants.H5Z_FILTER_FLETCHER32);
            assertTrue("H5.H5Zfilter_avail", filter_found > 0);
            filter_found = H5.H5Zfilter_avail(HDF5Constants.H5Z_FILTER_NBIT);
            assertTrue("H5.H5Zfilter_avail", filter_found > 0);
            filter_found = H5.H5Zfilter_avail(HDF5Constants.H5Z_FILTER_SCALEOFFSET);
            assertTrue("H5.H5Zfilter_avail", filter_found > 0);
            filter_found = H5.H5Zfilter_avail(HDF5Constants.H5Z_FILTER_SHUFFLE);
            assertTrue("H5.H5Zfilter_avail", filter_found > 0);
            filter_found = H5.H5Zfilter_avail(HDF5Constants.H5Z_FILTER_SZIP);
            assertTrue("H5.H5Zfilter_avail", filter_found > 0);
        }
        catch (Throwable err) {
            err.printStackTrace();
            fail("H5.H5Zfilter_avail " + err);
        }
    }
    
    @Test
    public void testH5Zget_filter_info() {
        try {
            int filter_flag = H5.H5Zget_filter_info(HDF5Constants.H5Z_FILTER_DEFLATE);
            assertTrue("H5.H5Zget_filter_info", (filter_flag & HDF5Constants.H5Z_FILTER_CONFIG_DECODE_ENABLED) > 0);
            assertTrue("H5.H5Zget_filter_info", (filter_flag & HDF5Constants.H5Z_FILTER_CONFIG_ENCODE_ENABLED) > 0);
            filter_flag = H5.H5Zget_filter_info(HDF5Constants.H5Z_FILTER_FLETCHER32);
            assertTrue("H5.H5Zget_filter_info", (filter_flag & HDF5Constants.H5Z_FILTER_CONFIG_DECODE_ENABLED) > 0);
            assertTrue("H5.H5Zget_filter_info", (filter_flag & HDF5Constants.H5Z_FILTER_CONFIG_ENCODE_ENABLED) > 0);
            filter_flag = H5.H5Zget_filter_info(HDF5Constants.H5Z_FILTER_NBIT);
            assertTrue("H5.H5Zget_filter_info", (filter_flag & HDF5Constants.H5Z_FILTER_CONFIG_DECODE_ENABLED) > 0);
            assertTrue("H5.H5Zget_filter_info", (filter_flag & HDF5Constants.H5Z_FILTER_CONFIG_ENCODE_ENABLED) > 0);
            filter_flag = H5.H5Zget_filter_info(HDF5Constants.H5Z_FILTER_SCALEOFFSET);
            assertTrue("H5.H5Zget_filter_info", (filter_flag & HDF5Constants.H5Z_FILTER_CONFIG_DECODE_ENABLED) > 0);
            assertTrue("H5.H5Zget_filter_info", (filter_flag & HDF5Constants.H5Z_FILTER_CONFIG_ENCODE_ENABLED) > 0);
            filter_flag = H5.H5Zget_filter_info(HDF5Constants.H5Z_FILTER_SHUFFLE);
            assertTrue("H5.H5Zget_filter_info", (filter_flag & HDF5Constants.H5Z_FILTER_CONFIG_DECODE_ENABLED) > 0);
            assertTrue("H5.H5Zget_filter_info", (filter_flag & HDF5Constants.H5Z_FILTER_CONFIG_ENCODE_ENABLED) > 0);
            filter_flag = H5.H5Zget_filter_info(HDF5Constants.H5Z_FILTER_SZIP);
            assertTrue("H5.H5Zget_filter_info", (filter_flag & HDF5Constants.H5Z_FILTER_CONFIG_DECODE_ENABLED) > 0);
            assertTrue("H5.H5Zget_filter_info", (filter_flag & HDF5Constants.H5Z_FILTER_CONFIG_ENCODE_ENABLED) > 0);
        }
        catch (Throwable err) {
            err.printStackTrace();
            fail("H5.H5Zget_filter_info " + err);
        }
    }
    
    @Test(expected = HDF5LibraryException.class)
    public void testH5Zunregister_predefined() throws Throwable {
        int filter_found = H5.H5Zfilter_avail(HDF5Constants.H5Z_FILTER_SHUFFLE);
        assertTrue("H5.H5Zfilter_avail", filter_found > 0);
        
        H5.H5Zunregister(HDF5Constants.H5Z_FILTER_SHUFFLE);
    }
}
