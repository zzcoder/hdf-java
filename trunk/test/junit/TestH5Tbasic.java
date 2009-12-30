package test.junit;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import ncsa.hdf.hdf5lib.H5;
import ncsa.hdf.hdf5lib.HDF5Constants;
import ncsa.hdf.hdf5lib.exceptions.HDF5FunctionArgumentException;
import ncsa.hdf.hdf5lib.exceptions.HDF5LibraryException;

import org.junit.Test;

public class TestH5Tbasic {

    @Test(expected = HDF5LibraryException.class)
    public void testH5Tclose_invalid() throws Throwable, HDF5LibraryException {
        H5.H5Tclose(-1);
    }

    @Test(expected = HDF5LibraryException.class)
    public void testH5Tcreate_invalid()
            throws Throwable, HDF5LibraryException, NullPointerException {
        H5.H5Tcreate(-1, 0);
    }

}
