package test.hdf5lib;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import ncsa.hdf.hdf5lib.H5;

import org.junit.Before;
import org.junit.Test;

public class TestH5Eregister {

    @Before
    public void checkOpenIDs() {
        assertTrue("H5 open ids is 0",H5.getOpenIDCount()==0);
    }

    @Test(expected = NullPointerException.class)
    public void testH5Eregister_class_cls_name_null() throws Throwable {
        H5.H5Eregister_class(null, "libname", "version");
    }

    @Test(expected = NullPointerException.class)
    public void testH5Eregister_class_lib_name_null() throws Throwable {
        H5.H5Eregister_class("clsname", null, "version");
    }

    @Test(expected = NullPointerException.class)
    public void testH5Eregister_class_version_null() throws Throwable {
        H5.H5Eregister_class("clsname", "libname", null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testH5Eunregister_class_invalid_classid() throws Throwable {
        H5.H5Eunregister_class(-1);
    }

    @Test
    public void testH5Eregister_class() {
        int hdf_java_classid = -1;
        try {
            hdf_java_classid = H5.H5Eregister_class("HDF-Java-Error",
                    "hdf-java", "2.5");
        }
        catch (Throwable err) {
            fail("H5.H5Eregister_class: " + err);
        }
        try {
            H5.H5Eunregister_class(hdf_java_classid);
        }
        catch (Throwable err) {
            fail("H5.H5Eunregister_class: " + err);
        }
    }
}
