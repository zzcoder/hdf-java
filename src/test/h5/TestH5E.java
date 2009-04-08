package test.h5;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import hdf.h5.H5E;
import hdf.h5.enums.H5E_TYPE;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class TestH5E {
  int hdf_java_classid = -1;
  int current_stackid = -1;

  @Before
  public void H5Eget_stack_class() {
    hdf_java_classid = -1;
    try { 
      hdf_java_classid = H5E.H5Eregister_class("HDF-Java-Error", "HDF-Java", "3.0.0"); 
      current_stackid = H5E.H5Eget_current_stack();
    }
    catch (Throwable err) {
      err.printStackTrace();
      fail("H5E.H5Eregister_class: "+err);
    }
  }

  @After
  public void H5Erestore_stack_class() {
    try { 
      H5E.H5Eunregister_class(hdf_java_classid); 
      hdf_java_classid = -1;
      H5E.H5Eclose_stack(current_stackid); 
      current_stackid = -1;
    }
    catch (Throwable err) {
      err.printStackTrace();
      fail("H5E.H5Eunregister_class: "+err);
    }
  }

  @Test
  public void testH5Eget_class_name() {
    try { 
      String class_name = H5E.H5Eget_class_name(hdf_java_classid); 
      assertNotNull("H5E.H5Eget_class_name: "+class_name,class_name);
    }
    catch (Throwable err) {
      err.printStackTrace();
      fail("H5E.H5Eget_class_name: "+err);
    }
  }

  @Test
  public void testH5Eprint2() {
    try { 
      assertFalse(current_stackid<0);
      H5E.H5Eprint2(current_stackid, null); 
    }
    catch (Throwable err) {
      err.printStackTrace();
      fail("H5E.H5Eprint2: "+err);
    }
  }

  @Ignore
  public void testH5Eclose_msg() {
    fail("Not yet implemented");
  }

  @Test(expected=NullPointerException.class)
  public void testH5Ecreate_msg_name_null() throws Throwable, NullPointerException {
    H5E.H5Ecreate_msg(hdf_java_classid, H5E_TYPE.MAJOR, null); 
  }

  @Test
  public void testH5Ecreate_msg_major() {
    try { 
      int err_id = H5E.H5Ecreate_msg(hdf_java_classid, H5E_TYPE.MAJOR, "Error in Test"); 
      assertFalse("H5E.H5Ecreate_msg: "+err_id, err_id<0);
      H5E.H5Eclose_msg(err_id);
    }
    catch (Throwable err) {
      err.printStackTrace();
      fail("H5E.H5Ecreate_msg: "+err);
    }
  }

  @Test
  public void testH5Ecreate_msg_minor() {
    try { 
      int err_id = H5E.H5Ecreate_msg(hdf_java_classid, H5E_TYPE.MINOR, "Error in Test Function"); 
      assertFalse("H5E.H5Ecreate_msg: "+err_id, err_id<0);
      H5E.H5Eclose_msg(err_id);
    }
    catch (Throwable err) {
      err.printStackTrace();
      fail("H5E.H5Ecreate_msg: "+err);
    }
  }

  @Test
  public void testH5Ecreate_stack() {
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
