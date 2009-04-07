package test.h5;

import static org.junit.Assert.fail;
import hdf.h5.H5E;

import org.junit.Test;

public class TestH5Eregister {

  @Test(expected=NullPointerException.class)
  public void testH5Eregister_class_cls_name_null() throws Throwable, NullPointerException {
    H5E.H5Eregister_class(null, "libname", "version"); 
    fail("H5E.H5Eregister_class: cls_name null");
  }

  @Test(expected=NullPointerException.class)
  public void testH5Eregister_class_lib_name_null() throws Throwable, NullPointerException {
    H5E.H5Eregister_class("clsname", null, "version"); 
    fail("H5E.H5Eregister_class: lib_name null");
  }

  @Test(expected=NullPointerException.class)
  public void testH5Eregister_class_version_null() throws Throwable, NullPointerException {
    H5E.H5Eregister_class("clsname", "libname", null); 
    fail("H5E.H5Eregister_class: version null");
  }

  @Test(expected=IllegalArgumentException.class)
  public void testH5Eunregister_class_invalid_classid() throws Throwable  {
      H5E.H5Eunregister_class(-1); 
      fail("H5E.H5Eunregister_class: invalid argument");
  }

  @Test
  public void testH5Eregister_class() {
    int hdf_java_classid = -1;
    try { 
      hdf_java_classid = H5E.H5Eregister_class("HDF-Java-Error", "HDF-Java", "3.0.0"); 
    }
    catch (Throwable err) {
      fail("H5E.H5Eregister_class: "+err);
    }
    try { 
      H5E.H5Eunregister_class(hdf_java_classid); 
    }
    catch (Throwable err) {
      fail("H5E.H5Eunregister_class: "+err);
    }
  }
}
