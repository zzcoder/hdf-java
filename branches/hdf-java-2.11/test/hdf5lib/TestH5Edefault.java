package test.hdf5lib;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import ncsa.hdf.hdf5lib.H5;
import ncsa.hdf.hdf5lib.HDF5Constants;
import ncsa.hdf.hdf5lib.exceptions.HDF5LibraryException;

import org.junit.Before;
import org.junit.Test;

public class TestH5Edefault {

    @Before
    public void H5Eset_default_stack() {
        assertTrue("H5 open ids is 0",H5.getOpenIDCount()==0);

        try {
            // Clear any active stack messages
            H5.H5Eclear2(HDF5Constants.H5E_DEFAULT);
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
            H5.H5Eprint2(HDF5Constants.H5E_DEFAULT, null);
        }
        catch (Throwable err) {
            err.printStackTrace();
            fail("H5.H5Eprint: " + err);
        }
    }

    @Test
    public void testH5Eget_current_stack() {
        long num_msg = -1;
        long num_msg_default = -1;
        int stack_id = -1;
        int stack_id_default = HDF5Constants.H5E_DEFAULT;
        try {
            H5.H5Fopen("test", 0, 1); 
        }
        catch (Throwable err) {
            //default stack id will be different after exception 
            stack_id_default = HDF5Constants.H5E_DEFAULT;
            //err.printStackTrace(); //This will clear the error stack
        }
        // Verify we have the correct number of messages
        try {
            num_msg_default = H5.H5Eget_num(stack_id_default);
        }
        catch (Throwable err) {
            err.printStackTrace();
            fail("H5.H5Eget_current_stack: " + err);
        }
        assertTrue("H5.H5Eget_current_stack: get_num #:" + num_msg_default,
                num_msg_default == 2);

        //Save a copy of the current stack and clears the current stack
        try {
            stack_id = H5.H5Eget_current_stack();
        }
        catch (Throwable err) {
            err.printStackTrace();
            fail("H5.H5Eget_current_stack: " + err);
        }
        assertFalse("H5.H5Eget_current_stack: get_current_stack - "
                + stack_id, stack_id < 0);
        assertFalse("H5.H5Eget_current_stack: get_current_stack - "
                + stack_id, stack_id == stack_id_default);

        // Verify we have the correct number of messages
        try {
            num_msg_default = H5.H5Eget_num(stack_id_default);
        }
        catch (Throwable err) {
            err.printStackTrace();
            fail("H5.H5Eget_current_stack: " + err);
        }
        assertTrue("H5.H5Eget_current_stack: get_num #:" + num_msg_default,
                num_msg_default == 0);

        //Verify the copy has the correct number of messages
        try {
            num_msg = H5.H5Eget_num(stack_id);
        }
        catch (Throwable err) {
            err.printStackTrace();
            fail("H5.H5Eget_current_stack: " + err);
        }
        assertTrue("H5.H5Eget_current_stack: get_num #:" + num_msg,
                num_msg == 2);
       
        try {
            H5.H5Eclose_stack(stack_id);
        }
        catch (Throwable err) {
            err.printStackTrace();
            fail("H5.H5Eget_current_stack: " + err);
        }
    }

    @Test
    public void testH5Eget_current_stack_pop() {
        long num_msg = -1;
        long num_msg_default = -1;
        int stack_id = -1;
        try {
            H5.H5Fopen("test", 0, 1);
        }
        catch (Throwable err) {
            //err.printStackTrace(); //This will clear the error stack
        }

        // Verify we have the correct number of messages
        try {
            num_msg_default = H5.H5Eget_num(HDF5Constants.H5E_DEFAULT);
        }
        catch (Throwable err) {
            err.printStackTrace();
            fail("H5.H5Eget_current_stack: " + err);
        }
        assertTrue("H5.H5Eget_current_stack: get_num #:" + num_msg_default,
                num_msg_default == 2);

        //Save a copy of the current stack and clears the current stack
        try {
            stack_id = H5.H5Eget_current_stack();
        }
        catch (Throwable err) {
            err.printStackTrace();
            fail("H5.H5Eget_current_stack: " + err);
        }
        assertFalse("H5.H5Eget_current_stack: get_current_stack - "
                + stack_id, stack_id < 0);
        assertFalse("H5.H5Eget_current_stack: get_current_stack - "
                + stack_id, stack_id == HDF5Constants.H5E_DEFAULT);

        // Verify we have the correct number of messages
        try {
            num_msg_default = H5.H5Eget_num(HDF5Constants.H5E_DEFAULT);
        }
        catch (Throwable err) {
            err.printStackTrace();
            fail("H5.H5Eget_current_stack: " + err);
        }
        assertTrue("H5.H5Eget_current_stack: get_num #:" + num_msg_default,
                num_msg_default == 0);

        //Verify the copy has the correct number of messages
        try {
            num_msg = H5.H5Eget_num(stack_id);
        }
        catch (Throwable err) {
            err.printStackTrace();
            fail("H5.H5Eget_current_stack: " + err);
        }
        assertTrue("H5.H5Eget_current_stack: get_num #:" + num_msg,
                num_msg == 2);

        //Generate errors on default stack
        try {
            H5.H5Fopen("test", 0, 1);
        }
        catch (Throwable err) {
            //err.printStackTrace(); //This will clear the error stack
        }

        // Verify we have the correct number of messages
        try {
            num_msg_default = H5.H5Eget_num(HDF5Constants.H5E_DEFAULT);
        }
        catch (Throwable err) {
            err.printStackTrace();
            fail("H5.H5Eget_current_stack: " + err);
        }
        assertTrue("H5.H5Eget_current_stack: get_num #:" + num_msg_default,
                num_msg_default == 2);

        //Remove one message from the current stack
        try {
            H5.H5Epop(HDF5Constants.H5E_DEFAULT, 1);
            num_msg_default = H5.H5Eget_num(HDF5Constants.H5E_DEFAULT);
        }
        catch (Throwable err) {
            err.printStackTrace();
            fail("H5.H5Eget_current_stack: " + err);
        }
        assertTrue("H5.H5Eget_current_stack: pop #:" + num_msg_default,
                num_msg_default == 1);

        //Verify the copy still has the correct number of messages
        try {
            num_msg = H5.H5Eget_num(stack_id);
        }
        catch (Throwable err) {
            err.printStackTrace();
            fail("H5.H5Eget_current_stack: " + err);
        }
        assertTrue("H5.H5Eget_current_stack: get_num #:" + num_msg,
                num_msg == 2);
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
        long num_msg = -1;
        int stack_id = -1;
        try {
            H5.H5Fopen("test", 0, 1);
        }
        catch (Throwable err) {
            //err.printStackTrace(); //This will clear the error stack
        }
        
        // Verify we have the correct number of messages
        try {
            num_msg = H5.H5Eget_num(HDF5Constants.H5E_DEFAULT);
        }
        catch (Throwable err) {
            err.printStackTrace();
            fail("H5.H5Eset_current_stack: " + err);
        }
        assertTrue("H5.H5Eset_current_stack: get_num #:" + num_msg,
                    num_msg == 2);
        
        //Save a copy of the current stack
        try {
            stack_id = H5.H5Eget_current_stack();
        }
        catch (Throwable err) {
            err.printStackTrace();
            fail("H5.H5Eset_current_stack: " + err);
        }
        assertFalse("H5.H5Eset_current_stack: get_current_stack - "
                    + stack_id, stack_id < 0);
        assertFalse("H5.H5Eset_current_stack: get_current_stack - "
                + stack_id, stack_id == HDF5Constants.H5E_DEFAULT);
        
        //Verify the copy has the correct number of messages
        try {
            num_msg = H5.H5Eget_num(stack_id);
        }
        catch (Throwable err) {
            err.printStackTrace();
            fail("H5.H5Eset_current_stack: " + err);
        }
        assertTrue("H5.H5Eset_current_stack: get_num #:" + num_msg,
                    num_msg == 2);

        //Generate errors on default stack
        try {
            H5.H5Fopen("test", 0, 1);
        }
        catch (Throwable err) {
            //err.printStackTrace(); //This will clear the error stack
        }

        // Verify we have the correct number of messages
        try {
            num_msg = H5.H5Eget_num(HDF5Constants.H5E_DEFAULT);
        }
        catch (Throwable err) {
            err.printStackTrace();
            fail("H5.H5Eget_current_stack: " + err);
        }
        assertTrue("H5.H5Eset_current_stack: get_num #:" + num_msg,
                num_msg == 2);
        
        //Remove one message from the current stack
        try {
            H5.H5Epop(HDF5Constants.H5E_DEFAULT, 1);
            num_msg = H5.H5Eget_num(HDF5Constants.H5E_DEFAULT);
        }
        catch (Throwable err) {
            err.printStackTrace();
            fail("H5.H5Eset_current_stack: " + err);
        }
        assertTrue("H5.H5Eset_current_stack: pop #:" + num_msg,
                    num_msg == 1);
        
        //Verify the copy still has the correct number of messages
        try {
            num_msg = H5.H5Eget_num(stack_id);
        }
        catch (Throwable err) {
            err.printStackTrace();
            fail("H5.H5Eset_current_stack: " + err);
        }
        assertTrue("H5.H5Eset_current_stack: get_num #:" + num_msg,
                    num_msg == 2);

        try {
            H5.H5Eset_current_stack(stack_id);
            num_msg = H5.H5Eget_num(HDF5Constants.H5E_DEFAULT);
        }
        catch (Throwable err) {
            err.printStackTrace();
            fail("H5.H5Eset_current_stack: " + err);
        }
        assertTrue("H5.H5Eset_current_stack: get_num - " + num_msg,
                    num_msg == 2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testH5Epop_invalid_stkid() throws Throwable {
        H5.H5Epop(-1, 0);
    }

    @Test
    public void testH5Epop() throws Throwable {
        long num_msg = -1;
        try {
            H5.H5Fopen("test", 0, 1);
        }
        catch (Throwable err) {
        }
        try {
            num_msg = H5.H5Eget_num(HDF5Constants.H5E_DEFAULT);
        }
        catch (Throwable err) {
            err.printStackTrace();
            fail("H5.H5Epop: " + err);
        }
        assertTrue("H5.H5Epop before #:" + num_msg, num_msg == 2);
        try {
            H5.H5Epop(HDF5Constants.H5E_DEFAULT, 1);
            num_msg = H5.H5Eget_num(HDF5Constants.H5E_DEFAULT);
        }
        catch (Throwable err) {
            err.printStackTrace();
            fail("H5.H5Epop: " + err);
        }
        assertTrue("H5.H5Epop after #:" + num_msg, num_msg == 1);
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
            H5.H5Eclear2(HDF5Constants.H5E_DEFAULT);
        }
        catch (Throwable err) {
            err.printStackTrace();
            fail("H5.H5Eclear2: " + err);
        }
    }

    @Test
    public void testH5Eclear2_with_msg() {
        long num_msg = -1;
        try {
            H5.H5Fopen("test", 0, 1);
        }
        catch (Throwable err) {
        }
        try {
            num_msg = H5.H5Eget_num(HDF5Constants.H5E_DEFAULT);
        }
        catch (Throwable err) {
            err.printStackTrace();
            fail("H5.H5Eclear2_with_msg: " + err);
        }
        assertTrue("H5.H5Eclear2_with_msg before #:" + num_msg,
                    num_msg == 2);
        try {
            H5.H5Eclear2(HDF5Constants.H5E_DEFAULT);
            num_msg = H5.H5Eget_num(HDF5Constants.H5E_DEFAULT);
        }
        catch (Throwable err) {
            err.printStackTrace();
            fail("H5.H5Eclear2_with_msg: " + err);
        }
            assertTrue("H5.H5Eclear2_with_msg after #:" + num_msg, num_msg == 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testH5Eauto_is_v2_invalid_stkid() throws Throwable {
        H5.H5Eauto_is_v2(-1);
    }

    @Test
    public void testH5Eauto_is_v2() {
        boolean is_v2 = false;
        try {
            is_v2 = H5.H5Eauto_is_v2(HDF5Constants.H5E_DEFAULT);
        }
        catch (Throwable err) {
            err.printStackTrace();
            fail("H5.H5Eauto_is_v2: " + err);
        }
        assertTrue("H5.H5Eauto_is_v2: ", is_v2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testH5Eget_num_invalid_stkid() throws Throwable {
        H5.H5Eget_num(-1);
    }

    @Test
    public void testH5Eget_num() {
        long num_msg = -1;
        try {
            num_msg = H5.H5Eget_num(HDF5Constants.H5E_DEFAULT);
        }
        catch (Throwable err) {
            err.printStackTrace();
            fail("H5.H5Eget_num: " + err);
        }
        assertTrue("H5.H5Eget_num #:" + num_msg, num_msg == 0);
    }

    @Test
    public void testH5Eget_num_with_msg() {
        long num_msg = -1;
        try {
            H5.H5Fopen("test", 0, 1);
        }
        catch (Throwable err) {
        }
        try {
            num_msg = H5.H5Eget_num(HDF5Constants.H5E_DEFAULT);
        }
        catch (Throwable err) {
            err.printStackTrace();
            fail("H5.H5Eget_num_with_msg: " + err);
        }
        assertTrue("H5.H5Eget_num_with_msg #:" + num_msg, num_msg > 0);
    }

}
