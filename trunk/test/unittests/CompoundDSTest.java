/**
 * 
 */
package test.unittests;

import ncsa.hdf.hdf5lib.H5;
import ncsa.hdf.hdf5lib.HDF5Constants;
import ncsa.hdf.object.Datatype;
import ncsa.hdf.object.FileFormat;
import ncsa.hdf.object.h5.H5File;
import junit.framework.TestCase;
import ncsa.hdf.object.CompoundDS;

/**
 * @author rsinha
 *
 */
public class CompoundDSTest extends TestCase {
	private static final H5File H5FILE = new H5File();

	private H5File testFile = null;
	private CompoundDS testDS = null;
	/**
	 * @param arg0
	 */
	public CompoundDSTest(String arg0) {
		super(arg0);
	}

	/* (non-Javadoc)
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
		testFile = (H5File)H5FILE.open(H5TestFile.NAME_FILE_H5, FileFormat.WRITE);
		assertNotNull(testFile);
		testDS = (CompoundDS) testFile.get(H5TestFile.NAME_DATASET_COMPOUND);
		assertNotNull(testDS);
		testDS.init();
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
	 * This method uses the testDS generated in the setUp method tests to see if each of the fields have the correct
	 * type, name, order and dims or not.
	 */
	public final void testFieldsHaveCorrectNameTypeOrderAndDims() {
		int correctMemberCount = H5TestFile.COMPOUND_MEMBER_NAMES.length;
		if (testDS.getMemberCount() != correctMemberCount)
			fail("getMemberCount returns member count at " + testDS.getMemberCount() + 
					"while the correct answer is" + correctMemberCount);
		String[] names = testDS.getMemberNames();
		for (int i = 0; i < correctMemberCount; i++) {
			if (!names[i].equals(H5TestFile.COMPOUND_MEMBER_NAMES[i]))
				fail("Member Name at position " + i + "should be " + H5TestFile.COMPOUND_MEMBER_NAMES[i] + 
						", while getMemberNames returns " + names[i]);
		}
		Datatype[] types = testDS.getMemberTypes();
		for (int i = 0; i < correctMemberCount; i++) {
			if (!types[i].getDatatypeDescription().equals(H5TestFile.COMPOUND_MEMBER_DATATYPES[i].getDatatypeDescription()))
				fail("Member Type at position " + i + "should be " + 
						H5TestFile.COMPOUND_MEMBER_DATATYPES[i].getDatatypeDescription() + 
						", while getMemberTypes returns " + types[i].getDatatypeDescription());
		}
		int[] orders = testDS.getMemberOrders();
		for (int i = 0; i < correctMemberCount; i++) {
			if (orders[i] != 1)
				fail("Member Order at position " + i + "should be " + 1 + ", while getMemberOrders returns " + orders[i]);
		}
		for (int i = 0; i < correctMemberCount; i++) {
			if (testDS.getMemeberDims(i) == null)
				fail("getMemberDims returns a null.");
			if (testDS.getMemeberDims(i)[0] != 1)
				fail("Member Dims at position " + i + "should be {" + 1 + 
						"}, while getMemberOrders returns {" + testDS.getMemeberDims(i)[0]+ "}");
		}
	}
	
	/*
	 * This method tests the methods for selections for correctness and makes sure that selection, deselection and counts
	 * are working properly.
	 */
	public final void testSelectionDeselectionCountWorks() {
		if (testDS.getSelectedMemberCount() != H5TestFile.COMPOUND_MEMBER_NAMES.length)
			fail("Right after init getSelectedMemberCount returns" + testDS.getSelectedMemberCount() 
					+ ", when it should return " + H5TestFile.COMPOUND_MEMBER_NAMES.length);
		
		testDS.setMemberSelection(false);
		if (testDS.getSelectedMemberCount() != 0)
			fail("setMemberSelection was set false yet getSelectedMemberCount returned " + testDS.getSelectedMemberCount());
		testDS.setMemberSelection(true);
		if (testDS.getSelectedMemberCount() != H5TestFile.COMPOUND_MEMBER_NAMES.length)
			fail("setMemberSelection was set true yet getSelectedMemberCount returned " + testDS.getSelectedMemberCount());
		testDS.setMemberSelection(false);
		if (testDS.getSelectedMemberCount() != 0)
			fail("setMemberSelection was set false yet getSelectedMemberCount returned " + testDS.getSelectedMemberCount());
		for (int i = 0; i < testDS.getMemberCount(); i++) {
			testDS.selectMember(i);
			int[] orders = testDS.getSelectedMemberOrders();
			Datatype[] types = testDS.getMemberTypes();
			for (int j = 0; j <= i; j++) {
				if (!testDS.isMemberSelected(j))
					fail("Member " + j + "was selected while isMemberSelected says it wasnt.");
				if (orders[j] != 1)
					fail("Member Order at position " + j + "should be " + 1 + ", while getMemberOrders returns " + orders[j]);
				if (!types[j].getDatatypeDescription().equals(H5TestFile.COMPOUND_MEMBER_DATATYPES[j].getDatatypeDescription()))
					fail("Member Type at position " + i + "should be " + 
							H5TestFile.COMPOUND_MEMBER_DATATYPES[j].getDatatypeDescription() + 
							", while getMemberTypes returns " + types[j].getDatatypeDescription());
			}
		}
	}
}
