package test.hdf5lib;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import ncsa.hdf.hdf5lib.H5;
import ncsa.hdf.hdf5lib.HDF5Constants;
import ncsa.hdf.hdf5lib.exceptions.HDF5LibraryException;

import org.junit.Test;

public class TestH5Z {
    
    @Test
    public void testH5Zfilter_avail() throws Throwable, HDF5LibraryException {
        int filter_found = H5.H5Zfilter_avail(HDF5Constants.H5Z_FILTER_DEFLATE);
        assertTrue("H5.H5Zfilter_avail",filter_found > 0);
        filter_found = H5.H5Zfilter_avail(HDF5Constants.H5Z_FILTER_FLETCHER32);
        assertTrue("H5.H5Zfilter_avail",filter_found > 0);
        filter_found = H5.H5Zfilter_avail(HDF5Constants.H5Z_FILTER_NBIT);
        assertTrue("H5.H5Zfilter_avail",filter_found > 0);
        filter_found = H5.H5Zfilter_avail(HDF5Constants.H5Z_FILTER_SCALEOFFSET);
        assertTrue("H5.H5Zfilter_avail",filter_found > 0);
        filter_found = H5.H5Zfilter_avail(HDF5Constants.H5Z_FILTER_SHUFFLE);
        assertTrue("H5.H5Zfilter_avail",filter_found > 0);
        filter_found = H5.H5Zfilter_avail(HDF5Constants.H5Z_FILTER_SZIP);
        assertTrue("H5.H5Zfilter_avail",filter_found > 0);
    }
    
    @Test
    public void testH5Zget_filter_info() throws Throwable, HDF5LibraryException {
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
    
    @Test(expected = HDF5LibraryException.class)
    public void testH5Zunregister_predefined() throws Throwable, HDF5LibraryException {
        int filter_found = H5.H5Zfilter_avail(HDF5Constants.H5Z_FILTER_SHUFFLE);
        assertTrue("H5.H5Zfilter_avail",filter_found > 0);
        
        H5.H5Zunregister(HDF5Constants.H5Z_FILTER_SHUFFLE);
    }
}
