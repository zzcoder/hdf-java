/**
 * 
 */
package test.junit;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;

import ncsa.hdf.hdf5lib.H5;
import ncsa.hdf.hdf5lib.HDF5Constants;
import ncsa.hdf.hdf5lib.exceptions.HDF5LibraryException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestH5Sbasic {
    private static final String H5_FILE = "test.h5";
    int H5fid = -1;

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

    @Before
    public void createH5file()
            throws HDF5LibraryException, NullPointerException {
        H5fid = H5.H5Fcreate(H5_FILE, HDF5Constants.H5F_ACC_TRUNC,
                HDF5Constants.H5P_DEFAULT, HDF5Constants.H5P_DEFAULT);
        H5.H5Fflush(H5fid, HDF5Constants.H5F_SCOPE_LOCAL);
    }

    @After
    public void deleteH5file() throws HDF5LibraryException {
        if (H5fid > 0) {
            H5.H5Fclose(H5fid);
        }
        _deleteFile(H5_FILE);
    }

    @Test(expected = HDF5LibraryException.class)
    public void testH5Sclose_invalid() throws Throwable, HDF5LibraryException {
        H5.H5Sclose(-1);
    }

    @Test(expected = HDF5LibraryException.class)
    public void testH5Screate_invalid()
            throws Throwable, HDF5LibraryException, NullPointerException {
        H5.H5Screate(-1);
    }


//    /**
//     * Test method for {@link hdf.h5.H5S#H5Screate(hdf.h5.enums.H5S_CLASS)}.
//     */
//    @Ignore("Not yet implemented")
//    public final void testH5Screate() {
//        fail("Not yet implemented"); // TODO
//    }
//
//    /**
//     * Test method for {@link hdf.h5.H5S#H5Screate_simple(int, long[], long[])}.
//     */
//    @Ignore("Not yet implemented")
//    public final void testH5Screate_simple() {
//        fail("Not yet implemented"); // TODO
//    }
//
//    /**
//     * Test method for
//     * {@link hdf.h5.H5S#H5Sset_extent_simple(int, int, long[], long[])}.
//     */
//    @Ignore("Not yet implemented")
//    public final void testH5Sset_extent_simple() {
//        fail("Not yet implemented"); // TODO
//    }
//
//    /**
//     * Test method for {@link hdf.h5.H5S#H5Scopy(int)}.
//     */
//    @Ignore("Not yet implemented")
//    public final void testH5Scopy() {
//        fail("Not yet implemented"); // TODO
//    }
//
//    /**
//     * Test method for {@link hdf.h5.H5S#H5Sclose(int)}.
//     */
//    @Ignore("Not yet implemented")
//    public final void testH5Sclose() {
//        fail("Not yet implemented"); // TODO
//    }
//
//    /**
//     * Test method for {@link hdf.h5.H5S#H5Sencode(int, byte[], long[])}.
//     */
//    @Ignore("Not yet implemented")
//    public final void testH5SencodeIntByteArrayLongArray() {
//        fail("Not yet implemented"); // TODO
//    }
//
//    /**
//     * Test method for {@link hdf.h5.H5S#H5Sencode(int)}.
//     */
//    @Ignore("Not yet implemented")
//    public final void testH5SencodeInt() {
//        fail("Not yet implemented"); // TODO
//    }
//
//    /**
//     * Test method for {@link hdf.h5.H5S#H5Sdecode(byte[])}.
//     */
//    @Ignore("Not yet implemented")
//    public final void testH5Sdecode() {
//        fail("Not yet implemented"); // TODO
//    }
//
//    /**
//     * Test method for {@link hdf.h5.H5S#H5Sget_simple_extent_npoints(int)}.
//     */
//    @Ignore("Not yet implemented")
//    public final void testH5Sget_simple_extent_npoints() {
//        fail("Not yet implemented"); // TODO
//    }
//
//    /**
//     * Test method for {@link hdf.h5.H5S#H5Sget_simple_extent_ndims(int)}.
//     */
//    @Ignore("Not yet implemented")
//    public final void testH5Sget_simple_extent_ndims() {
//        fail("Not yet implemented"); // TODO
//    }
//
//    /**
//     * Test method for
//     * {@link hdf.h5.H5S#H5Sget_simple_extent_dims(int, long[], long[])}.
//     */
//    @Ignore("Not yet implemented")
//    public final void testH5Sget_simple_extent_dims() {
//        fail("Not yet implemented"); // TODO
//    }
//
//    /**
//     * Test method for {@link hdf.h5.H5S#H5Sis_simple(int)}.
//     */
//    @Ignore("Not yet implemented")
//    public final void testH5Sis_simple() {
//        fail("Not yet implemented"); // TODO
//    }
//
//    /**
//     * Test method for {@link hdf.h5.H5S#H5Sget_select_npoints(int)}.
//     */
//    @Ignore("Not yet implemented")
//    public final void testH5Sget_select_npoints() {
//        fail("Not yet implemented"); // TODO
//    }
//
//    /**
//     * Test method for
//     * {@link hdf.h5.H5S#H5Sselect_hyperslab(int, hdf.h5.enums.H5S_SELECT_OPER, long[], long[], long[], long[])}
//     * .
//     */
//    @Ignore("Not yet implemented")
//    public final void testH5Sselect_hyperslab() {
//        fail("Not yet implemented"); // TODO
//    }
//
//    /**
//     * Test method for
//     * {@link hdf.h5.H5S#H5Sselect_elements(int, hdf.h5.enums.H5S_SELECT_OPER, long, long[])}
//     * .
//     */
//    @Ignore("Not yet implemented")
//    public final void testH5Sselect_elements() {
//        fail("Not yet implemented"); // TODO
//    }
//
//    /**
//     * Test method for {@link hdf.h5.H5S#H5Sget_simple_extent_type(int)}.
//     */
//    @Ignore("Not yet implemented")
//    public final void testH5Sget_simple_extent_type() {
//        fail("Not yet implemented"); // TODO
//    }
//
//    /**
//     * Test method for {@link hdf.h5.H5S#H5Sset_extent_none(int)}.
//     */
//    @Ignore("Not yet implemented")
//    public final void testH5Sset_extent_none() {
//        fail("Not yet implemented"); // TODO
//    }
//
//    /**
//     * Test method for {@link hdf.h5.H5S#H5Sextent_copy(int, int)}.
//     */
//    @Ignore("Not yet implemented")
//    public final void testH5Sextent_copy() {
//        fail("Not yet implemented"); // TODO
//    }
//
//    /**
//     * Test method for {@link hdf.h5.H5S#H5Sextent_equal(int, int)}.
//     */
//    @Ignore("Not yet implemented")
//    public final void testH5Sextent_equal() {
//        fail("Not yet implemented"); // TODO
//    }
//
//    /**
//     * Test method for {@link hdf.h5.H5S#H5Sselect_all(int)}.
//     */
//    @Ignore("Not yet implemented")
//    public final void testH5Sselect_all() {
//        fail("Not yet implemented"); // TODO
//    }
//
//    /**
//     * Test method for {@link hdf.h5.H5S#H5Sselect_none(int)}.
//     */
//    @Ignore("Not yet implemented")
//    public final void testH5Sselect_none() {
//        fail("Not yet implemented"); // TODO
//    }
//
//    /**
//     * Test method for {@link hdf.h5.H5S#H5Soffset_simple(int, long[])}.
//     */
//    @Ignore("Not yet implemented")
//    public final void testH5Soffset_simple() {
//        fail("Not yet implemented"); // TODO
//    }
//
//    /**
//     * Test method for {@link hdf.h5.H5S#H5Sselect_valid(int)}.
//     */
//    @Ignore("Not yet implemented")
//    public final void testH5Sselect_valid() {
//        fail("Not yet implemented"); // TODO
//    }
//
//    /**
//     * Test method for {@link hdf.h5.H5S#H5Sget_select_hyper_nblocks(int)}.
//     */
//    @Ignore("Not yet implemented")
//    public final void testH5Sget_select_hyper_nblocks() {
//        fail("Not yet implemented"); // TODO
//    }
//
//    /**
//     * Test method for {@link hdf.h5.H5S#H5Sget_select_elem_npoints(int)}.
//     */
//    @Ignore("Not yet implemented")
//    public final void testH5Sget_select_elem_npoints() {
//        fail("Not yet implemented"); // TODO
//    }
//
//    /**
//     * Test method for
//     * {@link hdf.h5.H5S#H5Sget_select_hyper_blocklist(int, long, long)}.
//     */
//    @Ignore("Not yet implemented")
//    public final void testH5Sget_select_hyper_blocklist() {
//        fail("Not yet implemented"); // TODO
//    }
//
//    /**
//     * Test method for
//     * {@link hdf.h5.H5S#H5Sget_select_elem_pointlist(int, long, long)}.
//     */
//    @Ignore("Not yet implemented")
//    public final void testH5Sget_select_elem_pointlist() {
//        fail("Not yet implemented"); // TODO
//    }
//
//    /**
//     * Test method for
//     * {@link hdf.h5.H5S#H5Sget_select_bounds(int, long[], long[])}.
//     */
//    @Ignore("Not yet implemented")
//    public final void testH5Sget_select_bounds() {
//        fail("Not yet implemented"); // TODO
//    }
//
//    /**
//     * Test method for {@link hdf.h5.H5S#H5Sget_select_type(int)}.
//     */
//    @Ignore("Not yet implemented")
//    public final void testH5Sget_select_type() {
//        fail("Not yet implemented"); // TODO
//    }

}
