/**
 * 
 */
package test.unittests;

import ncsa.hdf.hdf5lib.H5;
import ncsa.hdf.hdf5lib.HDF5Constants;
import ncsa.hdf.object.FileFormat;
import ncsa.hdf.object.Group;
import ncsa.hdf.object.h5.H5File;
import ncsa.hdf.object.h5.H5Group;
import ncsa.hdf.object.h5.H5ScalarDS;
import junit.framework.TestCase;
import ncsa.hdf.object.HObject;

/**
 * @author Rishi R. Sinha
 *
 */
public class HObjectTest extends TestCase {
	private static final H5File H5FILE = new H5File();
    private static final String GNAME = H5TestFile.NAME_GROUP;
    private static final String GNAME_SUB = H5TestFile.NAME_GROUP_SUB;
    
    private H5File testFile = null;
    private HObject testObj = null;
    private long testOID;
    
	/**
	 * @param arg0
	 */
	public HObjectTest(String arg0) {
		super(arg0);
	}

	/* (non-Javadoc)
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
		
        testFile = (H5File)H5FILE.open(H5TestFile.NAME_FILE_H5, FileFormat.WRITE);
        assertNotNull(testFile);
        testObj = (HObject) testFile.get(GNAME);
        assertNotNull(testObj);
        testOID = testObj.getOID()[0];
	}

	/* (non-Javadoc)
	 * @see junit.framework.TestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		super.tearDown();
		
        // make sure all objects are closed
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

	/**
	 * Test method for {@link ncsa.hdf.object.HObject#HObject()}.
	 */
	public final void testHObject() {
		//I cannot instantiate using this constructor so how can I test it.
	}

	/**
	 * Test method for {@link ncsa.hdf.object.HObject#HObject(ncsa.hdf.object.FileFormat, java.lang.String, java.lang.String)}.
	 */
	public final void testHObjectFileFormatStringString() {
		//I cannot instantiate using this constructor so how can I test it.
	}

	/**
	 * Test method for {@link ncsa.hdf.object.HObject#HObject(ncsa.hdf.object.FileFormat, java.lang.String, java.lang.String, long[])}.
	 */
	public final void testHObjectFileFormatStringStringLongArray() {
		//I cannot instantiate using this constructor so how can I test it.
	}

	/**
	 * Test method for {@link ncsa.hdf.object.HObject#getFile()}.
	 */
	public final void testGetFile() {
		if (!testObj.getFile().equals(H5TestFile.NAME_FILE_H5))
			fail("Wrong File");
	}

	/**
	 * Test method for {@link ncsa.hdf.object.HObject#getName()}.
	 */
	public final void testGetName() {
		if (!testObj.getName().equals(GNAME.substring(1)))
			fail("GetName returns wrong name");
	}

	/**
	 * Test method for {@link ncsa.hdf.object.HObject#getFullName()}.
	 */
	public final void testGetFullName() {
		if (!testObj.getFullName().equals(GNAME))
			fail("GetFullName returns wrong name");
	}

	/**
	 * Test method for {@link ncsa.hdf.object.HObject#getPath()}.
	 */
	public final void testGetPath() {
		if (!testObj.getPath().equals("/"))
			fail("GetPath returns wrong path");
	}

	/**
	 * Test method for {@link ncsa.hdf.object.HObject#setName(java.lang.String)}.
	 */
	public final void testSetName() {
        final String newName = "tmpName";

        // test set name to null
        try {
            testObj.setName(null);
        } catch (final Exception ex) { 
            ; // Expected - intentional
        }
       
        // set to an existing name
        try {
            testObj.setName(H5TestFile.NAME_DATASET_FLOAT);
        } catch (final Exception ex) { 
            ; // Expected - intentional
        }

        try { 
            testObj.setName(newName); 
        } catch (final Exception ex) { 
            fail("setName() failed. "+ ex);
        }
 
        // close the file and reopen it
        try {
            testFile.close();
            testFile.open();
            testObj = (HObject) testFile.get(newName);
        } catch (final Exception ex) { 
            fail("setName() failed. "+ ex);
        }
       
        HObject tmpObj;
        // test the old name
        try {
            tmpObj = (H5ScalarDS)testFile.get(GNAME);
         } catch (final Exception ex) { 
             tmpObj = null; // Expected - intentional
        }
        assertNull("The dataset should be null because it has been renamed", tmpObj);

        // set back the original name
        try { 
            testObj.setName(GNAME); 
        } catch (final Exception ex) { 
            fail("setName() failed. "+ ex);
        }
        
        // make sure the dataset is OK
        try {
            testObj = (HObject)testFile.get(GNAME);
        } catch (final Exception ex) { 
            fail("setName() failed. "+ ex);
        }
        assertNotNull(testObj);
	}

	/**
	 * Test method for {@link ncsa.hdf.object.HObject#setPath(java.lang.String)}.
	 */
	public final void testSetPath() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link ncsa.hdf.object.HObject#open()}.
	 */
	public final void testOpen() {
        int gid=-1;

        for (int loop=0; loop < 15; loop++) {
            gid=-1;
            try {
                gid = testObj.open();
            } catch (final Exception ex) { 
                fail("open() failed. "+ ex);
            }
            
            assertTrue(gid > 0);
            
            testObj.close(gid);
         }
	}

	/**
	 * Test method for {@link ncsa.hdf.object.HObject#close(int)}.
	 */
	public final void testClose() {
		testOpen();
	}

	/**
	 * Test method for {@link ncsa.hdf.object.HObject#getFID()}.
	 */
	public final void testGetFID() {
		if (testObj.getFID() != testFile.getFID())
			fail("getFID failed");
	}

	/**
	 * Test method for {@link ncsa.hdf.object.HObject#equalsOID(long[])}.
	 */
	public final void testEqualsOID() {
		long[] oid = new long[1];
		oid[0] = testOID;
		
		if (testObj.equalsOID(null) == true)
			fail("equalsOID() fails");
		if (testObj.equalsOID(oid) == false)
			fail("equalsOID() fails");
	}

	/**
	 * Test method for {@link ncsa.hdf.object.HObject#getFileFormat()}.
	 */
	public final void testGetFileFormat() {
		if (!testObj.getFileFormat().equals(testFile))
			fail("getFileFormat() failed.");
	}

	/**
	 * Test method for {@link ncsa.hdf.object.HObject#getOID()}.
	 */
	public final void testGetOID() {
		if (testObj.getOID()[0] != testOID)
		fail("getOID fails");
	}

	/**
	 * Test method for {@link ncsa.hdf.object.HObject#hasAttribute()}.
	 */
	public final void testHasAttribute() {
		if (testObj.hasAttribute() != false)
			fail("testAttribute() fails.");
	}

	/**
	 * Test method for {@link ncsa.hdf.object.HObject#toString()}.
	 */
	public final void testToString() {
		if (!testObj.toString().equals(GNAME.substring(1)))
			fail("toString() fails");
	}

}
