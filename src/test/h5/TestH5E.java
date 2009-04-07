package test.h5;

import static org.junit.Assert.fail;
import hdf.h5.H5E;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestH5E {
  static int hdf_java_classid = 0;

  @Before
  public void H5Eregister_class() {
    hdf_java_classid = -1;
    try { 
      hdf_java_classid = H5E.H5Eregister_class("HDF-Java-Error", "HDF-Java", "3.0.0"); 
    }
    catch (Throwable err) {
      fail("H5E.H5Eregister_class: "+err);
    }
  }

  @After
  public void H5Eunregister_class() {
    try { 
      H5E.H5Eunregister_class(hdf_java_classid); 
    }
    catch (Throwable err) {
      fail("H5E.H5Eunregister_class: "+err);
    }
  }

  @Test
  public void testH5Eprint2() {
    try { 
      H5E.H5Eprint2(hdf_java_classid,null); 
    }
    catch (Throwable err) {
      fail("H5E.H5Eunregister_class: "+err);
    }
  }

  @Test
  public void testH5Eclose_msg() {
    fail("Not yet implemented");
  }

  @Test
  public void testH5Ecreate_msg() {
    fail("Not yet implemented");
  }

  @Test
  public void testH5Ecreate_stack() {
    fail("Not yet implemented");
  }

  @Test
  public void testH5Eget_current_stack() {
    fail("Not yet implemented");
  }

  @Test
  public void testH5Eclose_stack() {
    fail("Not yet implemented");
  }

  @Test
  public void testH5Eget_class_name() {
    fail("Not yet implemented");
  }

  @Test
  public void testH5Eset_current_stack() {
    fail("Not yet implemented");
  }

  @Test
  public void testH5Epop() {
    fail("Not yet implemented");
  }

  @Test
  public void testH5EprintIntFile() {
    fail("Not yet implemented");
  }

  @Test
  public void testH5EclearInt() {
    fail("Not yet implemented");
  }

  @Test
  public void testH5Eclear2() {
    fail("Not yet implemented");
  }

  @Test
  public void testH5Eauto_is_v2() {
    fail("Not yet implemented");
  }

  @Test
  public void testH5Eget_msg() {
    fail("Not yet implemented");
  }

  @Test
  public void testH5Eget_num() {
    fail("Not yet implemented");
  }

  @Test
  public void testH5EprintFile() {
    fail("Not yet implemented");
  }

  @Test
  public void testH5Eclear() {
    fail("Not yet implemented");
  }

}
