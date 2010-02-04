package test.hdf5lib;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import ncsa.hdf.hdf5lib.H5;
import ncsa.hdf.hdf5lib.HDF5Constants;
import ncsa.hdf.hdf5lib.exceptions.HDF5FunctionArgumentException;
import ncsa.hdf.hdf5lib.exceptions.HDF5LibraryException;

import org.junit.Test;

public class TestH5Lparams {

    @Test(expected = HDF5LibraryException.class)
    public void testH5Lget_val_invalid() throws Throwable, HDF5LibraryException {
        H5.H5Lget_val(-1, "Bogus", -1);
    }

    @Test(expected = NullPointerException.class)
    public void testH5Lget_val_null() throws Throwable, HDF5LibraryException, NullPointerException {
        H5.H5Lget_val(-1, null, 0);
    }

}
