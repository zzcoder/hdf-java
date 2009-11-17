package test.junit;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import ncsa.hdf.hdf5lib.H5;
import ncsa.hdf.hdf5lib.HDF5Constants;
import ncsa.hdf.hdf5lib.exceptions.HDF5LibraryException;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class TestH5Edefault {

    @Before
    public void H5Eset_default_stack() {
        try {
            H5.H5Eset_current_stack(HDF5Constants.H5E_DEFAULT);
        }
        catch (HDF5LibraryException err) {
            err.printStackTrace();
            fail("H5Eset_default_stack: " + err);
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testH5Eprint2_invalid_classid() throws Throwable {
        H5.H5Eprint2(-1, null);
    }

    @Test
    public void testH5Eprint() {
        try {
            H5.H5Fopen("test", 0, 1);
        }
        catch (Throwable err) {
        }
        try {
            System.out.println("H5.isAPI16 is " + H5.isAPI16);
            if (H5.isAPI16) {
                H5.H5Eprint1(null);
                H5.H5Eclear();
            }
            else {
                H5.H5Eprint2(HDF5Constants.H5E_DEFAULT, null);
                H5.H5Eclear2(HDF5Constants.H5E_DEFAULT);
            }
        }
        catch (Throwable err) {
            err.printStackTrace();
            fail("H5.H5Eprint: " + err);
        }
    }

    @Test
    public void testH5Eget_current_stack() {
        try {
            int stack_id = H5.H5Eget_current_stack();
            assertFalse("H5.H5get_current_stack: " + stack_id, stack_id < 0);
        }
        catch (Throwable err) {
            err.printStackTrace();
            fail("H5.H5get_current_stack: " + err);
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testH5Eclose_stack_invalid_stackid() throws Throwable {
        H5.H5Eclose_stack(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testH5Eget_class_name_invalid_classid() throws Throwable {
        H5.H5Eget_class_name(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testH5Eget_class_name_invalid_classname() throws Throwable {
        H5.H5Eget_class_name(HDF5Constants.H5E_DEFAULT);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testH5Eclose_msg_invalid_errid() throws Throwable {
        H5.H5Eclose_msg(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testH5Ecreate_msg_invalid_errid() throws Throwable {
        H5.H5Ecreate_msg(-1, HDF5Constants.H5E_MAJOR, "null");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testH5Eget_msg_invalid_msgid() throws Throwable {
        H5.H5Eget_msg(-1, null);
    }

    @Test
    public void testH5Ecreate_stack() {
        try {
            int stack_id = H5.H5Ecreate_stack();
            assertTrue("H5.H5Ecreate_stack", stack_id > 0);
            H5.H5Eclose_stack(stack_id);
        }
        catch (Throwable err) {
            err.printStackTrace();
            fail("H5.H5Ecreate_stack: " + err);
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testH5Eset_current_stack_invalid_stkid() throws Throwable {
        H5.H5Eset_current_stack(-1);
    }

    @Test
    public void testH5Eset_current_stack() {
        try {
            H5.H5Fopen("test", 0, 1);
        }
        catch (Throwable err) {
            // err.printStackTrace();
        }
        try {
            long num_msg = H5.H5Eget_num(HDF5Constants.H5E_DEFAULT);
            assertTrue("H5.H5Eset_current_stack: get_num #:" + num_msg,
                    num_msg == 2);
            int stack_id = H5.H5Eget_current_stack();
            assertFalse("H5.H5Eset_current_stack: get_current_stack - "
                    + stack_id, stack_id < 0);
            H5.H5Epop(HDF5Constants.H5E_DEFAULT, 1);
            num_msg = H5.H5Eget_num(HDF5Constants.H5E_DEFAULT);
            assertTrue("H5.H5Eset_current_stack: pop #:" + num_msg,
                    num_msg == 0);
            H5.H5Eset_current_stack(stack_id);
            num_msg = H5.H5Eget_num(HDF5Constants.H5E_DEFAULT);
            assertTrue("H5.H5Eset_current_stack: get_num - " + num_msg,
                    num_msg == 2);
        }
        catch (Throwable err) {
            err.printStackTrace();
            fail("H5.H5Eset_current_stack: " + err);
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testH5Epop_invalid_stkid() throws Throwable {
        H5.H5Epop(-1, 0);
    }

    @Test
    public void testH5Epop() throws Throwable {
        try {
            H5.H5Fopen("test", 0, 1);
        }
        catch (Throwable err) {
        }
        try {
            long num_msg = H5.H5Eget_num(HDF5Constants.H5E_DEFAULT);
            assertTrue("H5.H5Epop before #:" + num_msg, num_msg == 2);
            H5.H5Epop(HDF5Constants.H5E_DEFAULT, 1);
            num_msg = H5.H5Eget_num(HDF5Constants.H5E_DEFAULT);
            assertTrue("H5.H5Epop after #:" + num_msg, num_msg == 1);
        }
        catch (Throwable err) {
            err.printStackTrace();
            fail("H5.H5Epop: " + err);
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testH5EprintInt_invalid_classid() throws Throwable {
        H5.H5Eprint2(-1, null);
    }

    @Test
    public void testH5EprintInt() {
        try {
            H5.H5Fopen("test", 0, 1);
        }
        catch (Throwable err) {
        }
        try {
            H5.H5Eprint2(HDF5Constants.H5E_DEFAULT, null);
        }
        catch (Throwable err) {
            err.printStackTrace();
            fail("H5.H5EprintInt: " + err);
        }
    }

    @Test
    public void testH5EclearInt() {
        try {
            H5.H5Eclear(HDF5Constants.H5E_DEFAULT);
        }
        catch (Throwable err) {
            err.printStackTrace();
            fail("H5.H5EclearInt: " + err);
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testH5Eclear2_invalid_stkid() throws Throwable {
        H5.H5Eclear2(-1);
    }

    @Test
    public void testH5Eclear() {
        try {
            if (H5.isAPI16) {
                H5.H5Eclear();
            }
            else {
                H5.H5Eclear2(HDF5Constants.H5E_DEFAULT);
            }
        }
        catch (Throwable err) {
            err.printStackTrace();
            fail("H5.H5Eclear2: " + err);
        }
    }

    @Test
    public void testH5Eclear2_with_msg() {
        try {
            H5.H5Fopen("test", 0, 1);
        }
        catch (Throwable err) {
        }
        try {
            long num_msg = H5.H5Eget_num(HDF5Constants.H5E_DEFAULT);
            assertTrue("H5.H5Eclear2_with_msg before #:" + num_msg,
                    num_msg == 2);
            H5.H5Eclear2(HDF5Constants.H5E_DEFAULT);
            num_msg = H5.H5Eget_num(HDF5Constants.H5E_DEFAULT);
            assertTrue("H5.H5Eclear2_with_msg after #:" + num_msg, num_msg == 0);
        }
        catch (Throwable err) {
            err.printStackTrace();
            fail("H5.H5Eclear2_with_msg: " + err);
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testH5Eauto_is_v2_invalid_stkid() throws Throwable {
        H5.H5Eauto_is_v2(-1);
    }

    @Test
    public void testH5Eauto_is_v2() {
        try {
            boolean is_v2 = H5.H5Eauto_is_v2(HDF5Constants.H5E_DEFAULT);
            if (H5.isAPI16)
                assertFalse("H5.H5Eauto_is_v2: ", is_v2);
            else
                assertTrue("H5.H5Eauto_is_v2: ", is_v2);
        }
        catch (Throwable err) {
            err.printStackTrace();
            fail("H5.H5Eauto_is_v2: " + err);
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testH5Eget_num_invalid_stkid() throws Throwable {
        H5.H5Eget_num(-1);
    }

    @Test
    public void testH5Eget_num() {
        try {
            long num_msg = H5.H5Eget_num(HDF5Constants.H5E_DEFAULT);
            assertTrue("H5.H5Eget_num #:" + num_msg, num_msg == 0);
        }
        catch (Throwable err) {
            err.printStackTrace();
            fail("H5.H5Eget_num: " + err);
        }
    }

    @Test
    public void testH5Eget_num_with_msg() {
        try {
            H5.H5Fopen("test", 0, 1);
        }
        catch (Throwable err) {
        }
        try {
            long num_msg = H5.H5Eget_num(HDF5Constants.H5E_DEFAULT);
            assertTrue("H5.H5Eget_num_with_msg #:" + num_msg, num_msg > 0);
        }
        catch (Throwable err) {
            err.printStackTrace();
            fail("H5.H5Eget_num_with_msg: " + err);
        }
    }

}
