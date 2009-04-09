package test.h5;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import hdf.h5.H5E;
import hdf.h5.H5F;
import hdf.h5.constants.H5Econstant;
import hdf.h5.enums.H5E_TYPE;

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
      H5F.H5Fopen("test", 0, 1); 
    }
    catch (Throwable err) {
    }
    try { 
      H5E.H5Eprint2(H5Econstant.H5E_DEFAULT, null); 
      H5E.H5Eclear2(H5Econstant.H5E_DEFAULT);
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
    try { 
      int stack_id = H5E.H5Ecreate_stack(); 
      assertTrue("H5E.H5Ecreate_stack", stack_id>0);
      H5E.H5Eclose_stack(stack_id); 
    }
    catch (Throwable err) {
      err.printStackTrace();
      fail("H5E.H5Ecreate_stack: "+err);
    }
  }

  @Test(expected=IllegalArgumentException.class)
  public void testH5Eset_current_stack_invalid_stkid() throws Throwable {
    H5E.H5Eset_current_stack(-1);
  }

  @Test
  public void testH5Eset_current_stack() {
    try { 
      H5F.H5Fopen("test", 0, 1); 
    }
    catch (Throwable err) {
    }
    try { 
      long num_msg = H5E.H5Eget_num(H5Econstant.H5E_DEFAULT);
      assertTrue("H5E.H5Eset_current_stack: get_num - ", num_msg == 2);
      int stack_id = H5E.H5Eget_current_stack();
      assertFalse("H5E.H5Eset_current_stack: get_current_stack - "+stack_id, stack_id<0);
      H5E.H5Epop(H5Econstant.H5E_DEFAULT, 1); 
      num_msg = H5E.H5Eget_num(H5Econstant.H5E_DEFAULT);
      assertTrue("H5E.H5Eset_current_stack: pop - ", num_msg == 0);
      H5E.H5Eset_current_stack(stack_id);
      num_msg = H5E.H5Eget_num(H5Econstant.H5E_DEFAULT);
      assertTrue("H5E.H5Eset_current_stack: get_num - ", num_msg == 2);
    }
    catch (Throwable err) {
      err.printStackTrace();
      fail("H5E.H5Eset_current_stack: "+err);
    }
  }

  @Test(expected=IllegalArgumentException.class)
  public void testH5Epop_invalid_stkid() throws Throwable {
    H5E.H5Epop(-1, 0); 
  }

  @Test
  public void testH5Epop() throws Throwable {
    try { 
      H5F.H5Fopen("test", 0, 1); 
    }
    catch (Throwable err) {
    }
    try {
      long num_msg = H5E.H5Eget_num(H5Econstant.H5E_DEFAULT);
      assertTrue("H5E.H5Epop", num_msg == 2);
      H5E.H5Epop(H5Econstant.H5E_DEFAULT, 1); 
      num_msg = H5E.H5Eget_num(H5Econstant.H5E_DEFAULT);
      assertTrue("H5E.H5Epop", num_msg == 1);
    }
    catch (Throwable err) {
      err.printStackTrace();
      fail("H5E.H5Epop: "+err);
    }
  }

  @Test(expected=IllegalArgumentException.class)
  public void testH5EprintInt_invalid_classid() throws Throwable  {
    H5E.H5Eprint(-1, null); 
  }

  @Test
  public void testH5EprintInt() {
    try { 
      H5F.H5Fopen("test", 0, 1); 
    }
    catch (Throwable err) {
    }
    try { 
      H5E.H5Eprint(H5Econstant.H5E_DEFAULT, null); 
    }
    catch (Throwable err) {
      err.printStackTrace();
      fail("H5E.H5EprintInt: "+err);
    }
  }

  @Test
  public void testH5EclearInt() {
    try {
      H5E.H5Eclear(H5Econstant.H5E_DEFAULT);
    }
    catch (Throwable err) {
      err.printStackTrace();
      fail("H5E.H5EclearInt: "+err);
    }
  }

  @Test(expected=IllegalArgumentException.class)
  public void testH5Eclear2_invalid_stkid() throws Throwable {
    H5E.H5Eclear2(-1);
  }

  @Test
  public void testH5Eclear2() {
    try {
      H5E.H5Eclear2(H5Econstant.H5E_DEFAULT);
    }
    catch (Throwable err) {
      err.printStackTrace();
      fail("H5E.H5Eclear2: "+err);
    }
  }

  @Test
  public void testH5Eclear2_with_msg() {
    try { 
      H5F.H5Fopen("test", 0, 1); 
    }
    catch (Throwable err) {
    }
    try {
      long num_msg = H5E.H5Eget_num(H5Econstant.H5E_DEFAULT);
      assertTrue("H5E.H5Eclear2_with_msg", num_msg == 2);
      H5E.H5Eclear2(H5Econstant.H5E_DEFAULT);
      num_msg = H5E.H5Eget_num(H5Econstant.H5E_DEFAULT);
      assertTrue("H5E.H5Eclear2_with_msg", num_msg == 0);
    }
    catch (Throwable err) {
      err.printStackTrace();
      fail("H5E.H5Eclear2_with_msg: "+err);
    }
  }

  @Test(expected=IllegalArgumentException.class)
  public void testH5Eauto_is_v2_invalid_stkid() throws Throwable {
    H5E.H5Eauto_is_v2(-1);
  }

  @Test
  public void testH5Eauto_is_v2() {
    try {
      boolean is_v2 = H5E.H5Eauto_is_v2(H5Econstant.H5E_DEFAULT);
      assertTrue("H5E.H5Eauto_is_v2: ", is_v2);
    }
    catch (Throwable err) {
      err.printStackTrace();
      fail("H5E.H5Eauto_is_v2: "+err);
    }
  }

  @Test(expected=IllegalArgumentException.class)
  public void testH5Eget_num_invalid_stkid() throws Throwable {
    H5E.H5Eget_num(-1);
  }

  @Test
  public void testH5Eget_num() {
    try {
      long num_msg = H5E.H5Eget_num(H5Econstant.H5E_DEFAULT);
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
      H5F.H5Fopen("test", 0, 1); 
    }
    catch (Throwable err) {
    }
    try {
      long num_msg = H5E.H5Eget_num(H5Econstant.H5E_DEFAULT);
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
