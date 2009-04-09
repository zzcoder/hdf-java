package test.h5;


import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Test suite for all unit tests of HDF5 library packages.
 *
 * @author xcao
 *
 */
public class TestAll {
    public static Test suite() {
        final TestSuite suite = new TestSuite("Test for test.unittests");

        suite.addTestSuite(TestH5.class);
        suite.addTestSuite(TestH5F.class);
        suite.addTestSuite(TestH5G.class);

        return suite;
    }

    public static void main(final String[] args) {

        junit.textui.TestRunner.run(suite());
    }
}
