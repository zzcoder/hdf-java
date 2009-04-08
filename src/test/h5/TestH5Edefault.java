package test.h5;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;
import junit.framework.Assert;
import hdf.h5.H5E;
import hdf.h5.constants.H5Econstant;
import hdf.h5.enums.H5E_TYPE;
import hdf.h5.exceptions.HDF5LibraryException;

import org.junit.Ignore;
import org.junit.Test;

public class TestH5Edefault {

  @Test(expected=IllegalArgumentException.class)
  public void testH5Eprint2_invalid_classid() throws Throwable  {
    H5E.H5Eprint2(-1, null); 
  }

  @Test
  public void testH5Eprint2() {
    try { 
      H5E.H5Eclose_stack(0); 
    }
    catch (Throwable err) {
    }
    try { 
      H5E.H5Eprint2(H5Econstant.H5E_DEFAULT, null); 
    }
    catch (Throwable err) {
      err.printStackTrace();
      fail("H5E.H5Eprint2: "+err);
    }
  }

  @Test
  public void testH5Eget_current_stack() {
    try { 
      int stack_id = H5E.H5Eget_current_stack();
      assertFalse("H5E.H5get_current_stack: "+stack_id, stack_id<0);
    }
    catch (Throwable err) {
      err.printStackTrace();
      fail("H5E.H5get_current_stack: "+err);
    }
  }

  @Test(expected=IllegalArgumentException.class)
  public void testH5Eclose_stack_invalid_stackid() throws Throwable {
    H5E.H5Eclose_stack(-1); 
  }

  @Test(expected=IllegalArgumentException.class)
  public void testH5Eget_class_name_invalid_classid() throws Throwable {
    H5E.H5Eget_class_name(-1);
  }

  @Test(expected=IllegalArgumentException.class)
  public void testH5Eget_class_name_invalid_classname() throws Throwable {
    H5E.H5Eget_class_name(H5Econstant.H5E_DEFAULT);
  }

  @Test(expected=IllegalArgumentException.class)
  public void testH5Eclose_msg_invalid_errid() throws Throwable  {
    H5E.H5Eclose_msg(-1);
  }

  @Test(expected=IllegalArgumentException.class)
  public void testH5Ecreate_msg_invalid_errid() throws Throwable  {
    H5E.H5Ecreate_msg(-1, H5E_TYPE.MAJOR, "null");
  }

  @Test(expected=IllegalArgumentException.class)
  public void testH5Eget_msg_invalid_msgid() throws Throwable {
    H5E.H5Eget_msg(-1, null);
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
