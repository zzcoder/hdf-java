package test.h5;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import hdf.h5.H5E;

import org.junit.Test;

public class TestH5E {
  static int hdf_java_classid = 0;

  @Test
  public void testH5Eregister_class() {
    int classid = 0;
    String cls_name = null;
    String lib_name = null;
    String version = null;

    // test cls_name is null
    try { 
      classid = H5E.H5Eregister_class(cls_name, lib_name, version); 
    }
    catch (NullPointerException err) {
      classid = -1;
    }
    catch (Throwable err) {
      fail("H5E.H5Eregister_class: "+err);
    }
    assertTrue("cls_name is null", classid < 0);
    cls_name = "HDF-Java-Error";
    classid = 0;

    // test lib_name is null
    try { 
      classid = H5E.H5Eregister_class(cls_name, lib_name, version); 
    }
    catch (NullPointerException err) {
      classid = -1;    
    }
    catch (Throwable err) {
      fail("H5E.H5Eregister_class: "+err);
    }
    assertTrue("lib_name is null", classid < 0);
    lib_name = "HDF-Java";
    classid = 0;

    // test version is null
    try { 
      classid = H5E.H5Eregister_class(cls_name, lib_name, version); 
    }
    catch (NullPointerException err) {
      classid = -1;    
    }
    catch (Throwable err) {
      fail("H5E.H5Eregister_class: "+err);
    }
    assertTrue("version is null", classid < 0);
    version = "3.0.0";
    classid = -1;
    try { 
      classid = H5E.H5Eregister_class(cls_name, lib_name, version); 
    }
    catch (Throwable err) {
      fail("H5E.H5Eregister_class: "+err);
    }
    assertFalse("registered error class", classid < 0);
    hdf_java_classid = classid;
//    System.out.println("hdf_java_classid"+hdf_java_classid);
  }

  @Test
  public void testH5Eunregister_class() {
    int classid = -1;
    // test classid is invalid
    try { 
      H5E.H5Eunregister_class(classid); }
    catch (IllegalArgumentException err) {
      classid = -1;    
    }
    catch (Throwable err) {
      classid = 0;    
    }
    assertTrue("classid is invalid", classid < 0);
    
    classid = hdf_java_classid;    
    try { H5E.H5Eunregister_class(classid); }
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
  public void testH5Eprint2() {
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
