/**
 * 
 */
package test.object;

import junit.framework.Test;
import junit.framework.TestResult;
import junit.framework.TestSuite;
import ncsa.hdf.hdf5lib.H5;

/**
 * Test suite for all unit tests of HDF5 objects.
 * 
 * @author xcao
 * 
 */
public class AllH5ObjectTests {
	public static Test suite() {
		try {
			H5TestFile.createTestFile(null);
		}
		catch (final Exception ex) {
			System.out.println("*** Unable to create HDF5 test file. " + ex);
			System.exit(-1);
		}
		
		final TestSuite suite = new TestSuite("Test for hdf-java objects");
		// $JUnit-BEGIN$

		// ncsa.hdf.object.h5 package
		suite.addTestSuite(H5CompoundDSTest.class);
		suite.addTestSuite(H5BugFixTest.class);
		suite.addTestSuite(H5ScalarDSTest.class);
		suite.addTestSuite(H5GroupTest.class);
		suite.addTestSuite(H5DatatypeTest.class);
		suite.addTestSuite(H5FileTest.class);

		// ncsa.hdf.object package
		suite.addTestSuite(CompoundDSTest.class);
		suite.addTestSuite(DatasetTest.class);
		suite.addTestSuite(ScalarDSTest.class);
		suite.addTestSuite(AttributeTest.class);
		suite.addTestSuite(DatatypeTest.class);
		suite.addTestSuite(FileFormatTest.class);
		suite.addTestSuite(GroupTest.class);
		suite.addTestSuite(HObjectTest.class);

		// $JUnit-END$
		return suite;
	}

	public static void main(final String[] args) {

		TestResult results = junit.textui.TestRunner.run(suite());
		if (!results.wasSuccessful())
			System.out.println("FAILED***:\tobject unit tests.\n");
		
		try {
			int openID = H5.getOpenIDCount();
			if(openID>0)
				System.out.println("Number of IDs still open: "+ openID);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
