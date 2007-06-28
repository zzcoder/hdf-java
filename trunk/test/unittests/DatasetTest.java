/**
 * 
 */
package test.unittests;

import java.util.Arrays;

import ncsa.hdf.hdf5lib.H5;
import ncsa.hdf.hdf5lib.HDF5Constants;
import ncsa.hdf.object.FileFormat;
import ncsa.hdf.object.Dataset;
import ncsa.hdf.object.h5.H5File;
import junit.framework.TestCase;

/**
 * @author rsinha
 *
 */
public class DatasetTest extends TestCase {
	private static final H5File H5FILE = new H5File();

	private H5File testFile = null;
	String[] dsetNames = {H5TestFile.NAME_DATASET_INT, H5TestFile.NAME_DATASET_FLOAT, H5TestFile.NAME_DATASET_CHAR, 
			H5TestFile.NAME_DATASET_STR, H5TestFile.NAME_DATASET_ENUM, H5TestFile.NAME_DATASET_IMAGE, 
			H5TestFile.NAME_DATASET_COMPOUND};
	private Dataset[] dSets = new Dataset[dsetNames.length];
	/**
	 * @param arg0
	 */
	public DatasetTest(String arg0) {
		super(arg0);
	}

	/* (non-Javadoc)
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
		testFile = (H5File)H5FILE.open(H5TestFile.NAME_FILE_H5, FileFormat.READ);
		assertNotNull(testFile);
		testFile.open();
		for (int i = 0; i < dSets.length; i++) {
			dSets[i] = (Dataset) testFile.get(dsetNames[i]);
			dSets[i].init();
			assertNotNull(dSets[i]);
		}
	}

	/* (non-Javadoc)
	 * @see junit.framework.TestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		super.tearDown();
		final int fid = testFile.getFID();
		if (fid > 0) {
			int nObjs = 0;
			try { nObjs = H5.H5Fget_obj_count(fid, HDF5Constants.H5F_OBJ_LOCAL); }
			catch (final Exception ex) { fail("H5.H5Fget_obj_count() failed. "+ ex);   }
			assertEquals(1, nObjs); // file id should be the only one left open
		}

		if (testFile != null) {
			try { testFile.close(); } catch (final Exception ex) {}
			testFile = null;
		}

	}
	
	/*
	 * This method tests the setting and getting of basic metadata associated with a dataset like chunksize, datatype,
	 * compression, is it byte to be converted to string, dimension names, dimensions, rank, height, size, selectedDims
	 * selectedIndex, stride, width. In order to test all these methods I run them for each of the different 
	 * datasets that are found in the test file.
	 */
	public final void testMetadataAssociatedWithDataset() {
		for (int i =0; i < dsetNames.length; i++) {
			System.out.println(i);
			assertNull(dSets[i].getChunkSize());
			assertTrue(dSets[i].getCompression().equals("NONE"));
			//assertFalse(dSets[i].getConvertByteToString());
			assertNull(dSets[i].getDimNames());
			assertTrue(Arrays.equals(dSets[i].getDims(), H5TestFile.DIMs));
			if (H5TestFile.NAME_DATASET_STR.equals("/" + dSets[i].getName()))
				assertEquals(dSets[i].getHeight(), H5TestFile.DIM2);
			else
				assertEquals(dSets[i].getHeight(), H5TestFile.DIM1);
			assertEquals(dSets[i].getRank(), H5TestFile.RANK);
			long[] array = new long[2];
			if (H5TestFile.NAME_DATASET_STR.equals("/" + dSets[i].getName())) {
				array[0] = 1;
				array[1] = 10;
			}
			else {
				array[0] = 50;
				array[1] = 10;
			}
			assertTrue(Arrays.equals(dSets[i].getSelectedDims(), array));
			int[] arrayInt = new int[3];
			if (H5TestFile.NAME_DATASET_STR.equals("/" + dSets[i].getName())) {
				arrayInt[0] = 1; arrayInt[1] = 0; arrayInt[2] = 2;
			}
			else {
				arrayInt[0] = 0; arrayInt[1] = 1; arrayInt[2] = 2;
			}
			assertTrue(Arrays.equals(dSets[i].getSelectedIndex(), arrayInt));
			array[0] = 0; array[1] = 0;
			assertTrue(Arrays.equals(dSets[i].getStartDims(), array));
			array[0] = 1; array[1] = 1;
			assertTrue(Arrays.equals(dSets[i].getStride(), array));
			if (H5TestFile.NAME_DATASET_STR.equals("/" + dSets[i].getName()))
				assertEquals(dSets[i].getWidth(), 1);
			else
				assertEquals(dSets[i].getWidth(), H5TestFile.DIM2);
		}
	}
}
