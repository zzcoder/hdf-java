/**
 * 
 */
package test.unittests;

import ncsa.hdf.hdf5lib.H5;
import ncsa.hdf.hdf5lib.HDF5Constants;
import ncsa.hdf.object.FileFormat;
import ncsa.hdf.object.h5.H5File;
import ncsa.hdf.object.h5.H5Group;
import junit.framework.TestCase;
import ncsa.hdf.object.Group;
import ncsa.hdf.object.HObject;
import java.util.Iterator;
import java.util.List;

/**
 * @author Rishi R Sinha
 *
 */
public class GroupTest extends TestCase {
	
    private static final H5File H5FILE = new H5File();
    private static final String GNAME = H5TestFile.NAME_GROUP;
    private static final String GNAME_SUB = H5TestFile.NAME_GROUP_SUB;
    
    private H5File testFile = null;
    private Group testGroup = null;
    
	/**
	 * @param arg0
	 */
	public GroupTest(String arg0) {
		super(arg0);
	}

	/* (non-Javadoc)
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
		
        testFile = (H5File)H5FILE.open(H5TestFile.NAME_FILE_H5, FileFormat.WRITE);
        assertNotNull(testFile);
        testGroup = (Group) testFile.get(GNAME);
        assertNotNull(testGroup);
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
	 * Test method for {@link ncsa.hdf.object.Group#Group(ncsa.hdf.object.FileFormat, java.lang.String, java.lang.String, ncsa.hdf.object.Group)}.
	 */
	public void testGroupFileFormatStringStringGroup() {
        final String[] names = {null, GNAME_SUB, GNAME_SUB.substring(4)};
        final String[] paths = {GNAME_SUB, null, H5TestFile.NAME_GROUP};

        
        for (int idx=0; idx<names.length; idx++) {
            final Group grp = new H5Group(testFile, names[idx], paths[idx], testGroup);
            final int gid = grp.open();
            assertTrue(gid>0);
            grp.close(gid);
        }
        
        final H5Group grp = new H5Group(testFile, "NO_SUCH_DATASET", "NO_SUCH_PATH", testGroup);
        final int gid = grp.open();
        assertTrue(gid <=0);
	}

	
	/**
	 * Test method for {@link ncsa.hdf.object.Group#clear()}.
	 */
	public void testClear() {
		testGroup.clear();
		if (testGroup.getMemberList().size() != 0)
			fail("Clear Not Functional");
	}

	/**
	 * Test method for {@link ncsa.hdf.object.Group#addToMemberList(ncsa.hdf.object.HObject)}.
	 */
	public void testAddToMemberList() {
		
		
		int previous_size = testGroup.getMemberList().size();
		Group tmp = new H5Group(testFile, "tmp", "/grp0/", testGroup);
		testGroup.addToMemberList(tmp);
		int current_size = testGroup.getMemberList().size();
		
		if (current_size != previous_size + 1)
			fail("Add to member list not working");
	}

	/**
	 * Test method for {@link ncsa.hdf.object.Group#removeFromMemberList(ncsa.hdf.object.HObject)}.
	 */
	public void testRemoveFromMemberList() {
		List memberList = testGroup.getMemberList();
		Iterator it = memberList.iterator();
		
		HObject obj = (HObject) it.next();
		testGroup.removeFromMemberList(obj);
		
		if (memberList.size() != 3)
			fail("The Number of members in list should be 3");
	}

	/**
	 * Test method for {@link ncsa.hdf.object.Group#getMemberList()}.
	 */
	public void testGetMemberList() {
		String objs[] = {"a_link_to_the_image", "dataset_comp", "dataset_int", "g00"};
		List memberList = testGroup.getMemberList();
		Iterator it = memberList.iterator();
		int position = 0;
		while (it.hasNext()) {
			HObject obj = (HObject) it.next();
			if (!objs[position].equals(obj.getName())) {
				fail("Objects inside the Group Dont Match\n");
			}
			position++;
		}
	}

	/**
	 * Test method for {@link ncsa.hdf.object.Group#getParent()}.
	 */
	public void testGetParent() {
		HObject parent = testGroup.getParent();
	
		if (!parent.getName().equals("/"))
			fail("Incorrect Parent Name");
	}

	/**
	 * Test method for {@link ncsa.hdf.object.Group#isRoot()}.
	 */
	public void testIsRoot() {
		if (testGroup.isRoot())
			fail("This node is not root");
	}

	/**
	 * Test method for {@link ncsa.hdf.object.Group#getNumberOfMembersInFile()}.
	 */
	public void testGetNumberOfMembersInFile() {
		
		int nmf = testGroup.getNumberOfMembersInFile();
		
		if (nmf != 8)
			fail("Wrong number of members");
	}

}
