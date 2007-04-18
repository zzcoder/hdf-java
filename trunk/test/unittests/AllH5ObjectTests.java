/**
 * 
 */
package test.unittests;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Test suite for all unit tests of HDF5 objects.
 * 
 * @author xcao
 *
 */
public class AllH5ObjectTests {
    public static Test suite() {
        TestSuite suite = new TestSuite("Test for test.unittests");
        //$JUnit-BEGIN$
        suite.addTestSuite(H5CompoundDSTest.class);
        //$JUnit-END$
        return suite;
    }

    public static void main(String[] args) {
        
        try { H5TestFile.createTestFile(); } 
        catch (Exception ex) {
            System.out.println("*** Unable to create HDF5 test file. "+ex);
            System.exit(-1);
        }
        
        junit.textui.TestRunner.run(suite());
    }

}
