/**
 * 
 */
package hdf.h5;

import static org.junit.Assert.*;

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
        fail("Not yet implemented");
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
