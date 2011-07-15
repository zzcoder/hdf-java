package test.h5;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import hdf.h5.H5E;
import hdf.h5.H5F;
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
      fail("H5E.H5Eget_stack_class: "+err);
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
      fail("H5E.H5Erestore_stack_class: "+err);
    }
  }

  @Test
  public void testH5Eget_class_name() {
    try { 
      String class_name = H5E.H5Eget_class_name(hdf_java_classid); 
      assertNotNull("H5E.H5Eget_class_name: "+class_name,class_name);
      assertEquals("H5E.H5Eget_class_name: ","HDF-Java-Error",class_name);
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

  @Ignore("Tested with create_msg_major[minor]")
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
      assertFalse("H5E.H5Ecreate_msg_major: "+err_id, err_id<0);
      H5E.H5Eclose_msg(err_id);
    }
    catch (Throwable err) {
      err.printStackTrace();
      fail("H5E.H5Ecreate_msg_major: "+err);
    }
  }

  @Test
  public void testH5Ecreate_msg_minor() {
    try { 
      int err_id = H5E.H5Ecreate_msg(hdf_java_classid, H5E_TYPE.MINOR, "Error in Test Function"); 
      assertFalse("H5E.H5Ecreate_msg_minor: "+err_id, err_id<0);
      H5E.H5Eclose_msg(err_id);
    }
    catch (Throwable err) {
      err.printStackTrace();
      fail("H5E.H5Ecreate_msg_minor: "+err);
    }
  }

  @Test
  public void testH5Eget_msg() {
    try {
      H5E_TYPE[] error_msg_type = {H5E_TYPE.MINOR};
      int err_id = H5E.H5Ecreate_msg(hdf_java_classid, H5E_TYPE.MAJOR, "Error in Test"); 
      assertFalse("H5E.H5Eget_msg: H5Ecreate_msg - "+err_id, err_id<0);
      String msg = H5E.H5Eget_msg(err_id, error_msg_type); 
      assertNotNull("H5E.H5Eget_msg: "+msg,msg);
      assertEquals("H5E.H5Eget_msg: ","Error in Test",msg);
      assertEquals("H5E.H5Eget_msg: ", H5E_TYPE.MAJOR, error_msg_type[0]);
      H5E.H5Eclose_msg(err_id);
    }
    catch (Throwable err) {
      err.printStackTrace();
      fail("H5E.H5Eget_msg: "+err);
    }
  }

  @Test
  public void testH5Ecreate_stack() {
    try { 
      int stk_id = H5E.H5Ecreate_stack(); 
      assertFalse("H5E.H5Ecreate_stack: "+stk_id, stk_id<0);
      H5E.H5Eclose_stack(stk_id);
    }
    catch (Throwable err) {
      err.printStackTrace();
      fail("H5E.H5Ecreate_stack: "+err);
    }
  }

  @Test
  public void testH5Epop() {
    try { 
      H5E.H5Eset_current_stack(current_stackid); 
      try { 
        H5F.H5Fopen("test", 0, 1); 
      }
      catch (Throwable err) {
      }
      long num_msg = H5E.H5Eget_num(current_stackid);
      assertTrue("H5E.H5Epop", num_msg == 2);
      H5E.H5Epop(current_stackid, 1); 
      num_msg = H5E.H5Eget_num(current_stackid);
      assertTrue("H5E.H5Epop", num_msg == 1);
    }
    catch (Throwable err) {
      err.printStackTrace();
      fail("H5E.H5Epop: "+err);
    }
  }

  @Test
  public void testH5EprintInt() {
    try { 
      assertFalse(current_stackid<0);
      H5E.H5Eprint(current_stackid, null); 
    }
    catch (Throwable err) {
      err.printStackTrace();
      fail("H5E.H5EprintInt: "+err);
    }
  }

  @Test
  public void testH5EclearInt() {
    try {
      H5E.H5Eclear(current_stackid);
    }
    catch (Throwable err) {
      err.printStackTrace();
      fail("H5E.H5EclearInt: "+err);
    }
  }

  @Test
  public void testH5Eclear2() {
    try {
      H5E.H5Eclear2(current_stackid);
    }
    catch (Throwable err) {
      err.printStackTrace();
      fail("H5E.H5Eclear2: "+err);
    }
  }

  @Test
  public void testH5Eauto_is_v2() {
    try {
      boolean is_v2 = H5E.H5Eauto_is_v2(current_stackid);
      assertTrue("H5E.H5Eauto_is_v2: ", is_v2);
    }
    catch (Throwable err) {
      err.printStackTrace();
      fail("H5E.H5Eauto_is_v2: "+err);
    }
  }

  @Test
  public void testH5Eget_num() {
    try {
      long num_msg = H5E.H5Eget_num(current_stackid);
      assertTrue("H5E.H5Eget_num",num_msg == 0);
    }
    catch (Throwable err) {
      err.printStackTrace();
      fail("H5E.H5Eget_num: "+err);
    }
  }

  @Test
  public void testH5Eget_num_with_msg() {
    try {
      H5E.H5Eset_current_stack(current_stackid); 
      try { 
        H5F.H5Fopen("test", 0, 1); 
      }
      catch (Throwable err) {
      }
      long num_msg = H5E.H5Eget_num(current_stackid);
      assertTrue("H5E.H5Eget_num_with_msg", num_msg > 0);
    }
    catch (Throwable err) {
      err.printStackTrace();
      fail("H5E.H5Eget_num_with_msg: "+err);
    }
  }

  @Ignore("API1.6")
  public void testH5Eprint() {
    fail("Not yet implemented");
  }

  @Ignore("API1.6")
  public void testH5Eclear() {
    fail("Not yet implemented");
  }

}