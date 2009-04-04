/**
 * 
 */
package test.h5;

import static org.junit.Assert.*;
import hdf.h5.H5;
import hdf.h5.constants.H5Fconstant;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author xcao
 *
 */
public class TestH5 {

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
    }

    /**
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception {
    }

    /**
     * Test method for {@link hdf.h5.H5#J2C(int)}.
     */
    @Test
    public void testJ2C() {
        int H5F_ACC_RDONLY  = 0x0000;
        int H5F_ACC_RDWR    = 0x0001;
        int H5F_ACC_TRUNC   = 0x0002;
        int H5F_ACC_EXCL    = 0x0004;
        int H5F_ACC_DEBUG   = 0x0008;
        int H5F_ACC_CREAT   = 0x0010;
        int H5F_ACC_DEFAULT = 0xffff;
        int H5F_OBJ_FILE    = 0x0001;
        int H5F_OBJ_DATASET = 0x0002;
        int H5F_OBJ_GROUP   = 0x0004;
        int H5F_OBJ_DATATYPE = 0x0008;
        int H5F_OBJ_ATTR    = 0x0010;
        int H5F_OBJ_ALL     = H5F_OBJ_FILE|H5F_OBJ_DATASET|H5F_OBJ_GROUP|H5F_OBJ_DATATYPE|H5F_OBJ_ATTR;
        int H5F_OBJ_LOCAL   = 0x0020;
        
        int definedValues[] = {
                H5F_ACC_RDONLY,
                H5F_ACC_RDWR,
                H5F_ACC_TRUNC,
                H5F_ACC_EXCL,
                H5F_ACC_DEBUG,
                H5F_ACC_CREAT,
                H5F_OBJ_FILE,
                H5F_OBJ_DATASET,
                H5F_OBJ_GROUP,
                H5F_OBJ_DATATYPE,
                H5F_OBJ_ATTR,
                H5F_OBJ_ALL,
                H5F_OBJ_LOCAL
            };
        
        int j2cValues[] = {
                H5Fconstant.H5F_ACC_RDONLY,
                H5Fconstant.H5F_ACC_RDWR,
                H5Fconstant.H5F_ACC_TRUNC,
                H5Fconstant.H5F_ACC_EXCL,
                H5Fconstant.H5F_ACC_DEBUG,
                H5Fconstant.H5F_ACC_CREAT,
                H5Fconstant.H5F_OBJ_FILE,
                H5Fconstant.H5F_OBJ_DATASET,
                H5Fconstant.H5F_OBJ_GROUP,
                H5Fconstant.H5F_OBJ_DATATYPE,
                H5Fconstant.H5F_OBJ_ATTR,
                H5Fconstant.H5F_OBJ_ALL,
                H5Fconstant.H5F_OBJ_LOCAL
            };
        
        for (int i=0; i<definedValues.length; i++) {
            assertEquals(definedValues[i], j2cValues[i]);
        }
        
        assertFalse(H5F_ACC_RDONLY == H5Fconstant.H5F_ACC_RDWR);
        assertFalse(H5F_OBJ_FILE == H5Fconstant.H5F_OBJ_GROUP);

    }

    /**
     * Test method for {@link hdf.h5.H5#H5error_off()}.
     */
    @Test
    public void testH5error_off() {
        try { H5.H5error_off(); }
        catch (Throwable err) {
            fail("H5.H5error_off failed: "+err);
        }
    }

    /**
     * Test method for {@link hdf.h5.H5#H5open()}.
     */
    @Test
    public void testH5open() {
        try { H5.H5open(); }
        catch (Throwable err) {
            fail("H5.H5open failed: "+err);
        }
    }
    
    /**
     * Test method for {@link hdf.h5.H5#H5close()}.
     */
    @Test
    public void testH5close() {
        fail("Not yet implemented");
    }

    /**
     * Test method for {@link hdf.h5.H5#H5garbage_collect()}.
     */
    @Test
    public void testH5garbage_collect() {
        fail("Not yet implemented");
    }

    /**
     * Test method for {@link hdf.h5.H5#H5set_free_list_limits(int, int, int, int, int, int)}.
     */
    @Test
    public void testH5set_free_list_limits() {
        fail("Not yet implemented");
    }

    /**
     * Test method for {@link hdf.h5.H5#H5get_libversion(int[])}.
     */
    @Test
    public void testH5get_libversion() {
        fail("Not yet implemented");
    }

    /**
     * Test method for {@link hdf.h5.H5#H5check_version(int, int, int)}.
     */
    @Test
    public void testH5check_version() {
        int majnum=1, minnum=8, relnum=2;
        
        try { H5.H5check_version(majnum, minnum, relnum); }
        catch (Throwable err) {
            fail("H5.H5check_version failed: "+err);
        }
        
        try { H5.H5check_version(-1, 0, 0); }
        catch (Throwable err) {
            fail("H5.H5check_version failed: "+err);
        }
        
        
    }

    /**
     * Test method for {@link hdf.h5.H5#H5Eclear()}.
     */
    @Test
    public void testH5Eclear() {
        fail("Not yet implemented");
    }

}
