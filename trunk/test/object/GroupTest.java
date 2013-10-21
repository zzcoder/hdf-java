/**
 * 
 */
package test.object;

import java.util.Iterator;
import java.util.List;

import junit.framework.TestCase;
import ncsa.hdf.hdf5lib.H5;
import ncsa.hdf.hdf5lib.HDF5Constants;
import ncsa.hdf.object.FileFormat;
import ncsa.hdf.object.Group;
import ncsa.hdf.object.HObject;
import ncsa.hdf.object.h5.H5File;
import ncsa.hdf.object.h5.H5Group;

/**
 * @author Rishi R Sinha
 * 
 */
public class GroupTest extends TestCase {
    private final static org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(GroupTest.class);

    private H5File testFile = null;
    private Group testGroup = null;

    /**
     * @param arg0
     */
    public GroupTest(String arg0) {
        super(arg0);
    }

    /*
     * (non-Javadoc)
     * 
     * @see junit.framework.TestCase#setUp()
     */
    @Override
	protected void setUp() throws Exception {
        super.setUp();
        H5File H5FILE = new H5File();
        testFile = (H5File) H5FILE.open(H5TestFile.NAME_FILE_H5,
                FileFormat.WRITE);
        assertNotNull(testFile);
        testGroup = (Group) testFile.get(H5TestFile.NAME_GROUP);
        assertNotNull(testGroup);
    }

    /*
     * (non-Javadoc)
     * 
     * @see junit.framework.TestCase#tearDown()
     */
    @Override
	protected void tearDown() throws Exception {
        super.tearDown();

        if (testFile != null) {
            try {
                testFile.close();
            }
            catch (final Exception ex) {
            }
            testFile = null;
        }
    }

    /**
     * Test method for {@link ncsa.hdf.object.Group#clear()}.
     * <p>
     * What to test:
     * <ul>
     * <li>For the root group clear the list.
     * </ul>
     * </ul>
     */
    public void testClear() {
    	log.debug("testClear");
        testGroup.clear();
        assertEquals(testGroup.getMemberList().size(), 0);
        int nObjs = 0;
        try {
            nObjs = H5.H5Fget_obj_count(testFile.getFID(),
                    HDF5Constants.H5F_OBJ_ALL);
        }
        catch (final Exception ex) {
            fail("H5.H5Fget_obj_count() failed. " + ex);
        }
        assertEquals(1, nObjs); // file id should be the only one left open
    }

    /**
     * Test method for
     * {@link ncsa.hdf.object.Group#addToMemberList(ncsa.hdf.object.HObject)}.
     * <p>
     * What to test:
     * <ul>
     * <li>Test for boundary conditions
     * <ul>
     * <li>Add null to the member list.
     * </ul>
     * <li>Test for failure
     * <ul>
     * <li>Add an already existing object to the list.
     * </ul>
     * <li>Test for general functionality
     * <ul>
     * <li>add a group to it.
     * </ul>
     * </ul>
     */
    public void testAddToMemberList() {
    	log.debug("testAddToMemberList");
        int previous_size = testGroup.getMemberList().size();
        testGroup.addToMemberList(null);
        assertEquals(testGroup.getMemberList().size(), previous_size);

        Group tmp = new H5Group(testFile, "tmp", "/grp0/", testGroup);
        testGroup.addToMemberList((HObject) testGroup.getMemberList().get(0));

        if (testGroup.getMemberList().size() != previous_size) {
            fail("addToMemberList adds an existing member to the member list.");
        }

        testGroup.addToMemberList(tmp);
        if (!testGroup.getMemberList().get(previous_size).equals(tmp)) {
            fail("Add to member list does not add to the end.");
        }
        if (testGroup.getMemberList().size() != previous_size + 1) {
            fail("Add to member list not working.");
        }
        int nObjs = 0;
        try {
            nObjs = H5.H5Fget_obj_count(testFile.getFID(),
                    HDF5Constants.H5F_OBJ_ALL);
        }
        catch (final Exception ex) {
            fail("H5.H5Fget_obj_count() failed. " + ex);
        }
        assertEquals(1, nObjs); // file id should be the only one left open
    }

    /**
     * Test method for
     * {@link ncsa.hdf.object.Group#removeFromMemberList(ncsa.hdf.object.HObject)}
     * .
     * <p>
     * What to test:
     * <ul>
     * <li>Test for boundary conditions
     * <ul>
     * <li>Remove a null from the member list.
     * </ul>
     * <li>Test for failure
     * <ul>
     * <li>Remove a non existing object to the list.
     * </ul>
     * <li>Test for general functionality
     * <ul>
     * <li>Remove a group from it.
     * </ul>
     * </ul>
     */
    public void testRemoveFromMemberList() {
    	log.debug("testRemoveFromMemberList");
        int previous_size = testGroup.getMemberList().size();
        List memberList = testGroup.getMemberList();

        testGroup.removeFromMemberList(null);
        if (testGroup.getMemberList().size() != previous_size) {
            fail("removeFromMemberList removes a null from the member list.");
        }

        Group tmp = new H5Group(testFile, "tmp", "/grp0/", testGroup);
        testGroup.removeFromMemberList(tmp);
        if (testGroup.getMemberList().size() != previous_size) {
            fail("removeFromMemberList removes a non existing member from the member list.");
        }

        Iterator it = memberList.iterator();
        HObject obj = (HObject) it.next();
        testGroup.removeFromMemberList(obj);

        if (memberList.size() != previous_size - 1) {
            fail("The Number of members in list should be "
                    + (previous_size - 1));
        }
        int nObjs = 0;
        try {
            nObjs = H5.H5Fget_obj_count(testFile.getFID(),
                    HDF5Constants.H5F_OBJ_ALL);
        }
        catch (final Exception ex) {
            fail("H5.H5Fget_obj_count() failed. " + ex);
        }
        assertEquals(1, nObjs); // file id should be the only one left open
    }

    /**
     * Test method for {@link ncsa.hdf.object.Group#getMemberList()}.
     * <p>
     * <ul>
     * <li>testing the member list for the root group.
     * <ul>
     */
    public void testGetMemberList() {
    	log.debug("testGetMemberList");
        String objs[] = { "a_link_to_the_image", "dataset_comp", "dataset_int",
                "datatype_float", "datatype_int", "datatype_str", "datatype_uint", "g00" };
        List memberList = testGroup.getMemberList();
        Iterator it = memberList.iterator();
        int position = 0;
        while (it.hasNext()) {
            HObject obj = (HObject) it.next();
            assertEquals(objs[position++], obj.getName());
        }
        int nObjs = 0;
        try {
            nObjs = H5.H5Fget_obj_count(testFile.getFID(),
                    HDF5Constants.H5F_OBJ_ALL);
        }
        catch (final Exception ex) {
            fail("H5.H5Fget_obj_count() failed. " + ex);
        }
        assertEquals(1, nObjs); // file id should be the only one left open
    }

    /**
     * Test method for {@link ncsa.hdf.object.Group#getParent()}.
     * <p>
     * <ul>
     * <li>Test to get the parent of group g0.
     * </ul>
     */
    public void testGetParent() {
    	log.debug("testGetParent");
        assertEquals(testGroup.getParent().getName(), "/");
        int nObjs = 0;
        try {
            nObjs = H5.H5Fget_obj_count(testFile.getFID(),
                    HDF5Constants.H5F_OBJ_ALL);
        }
        catch (final Exception ex) {
            fail("H5.H5Fget_obj_count() failed. " + ex);
        }
        assertEquals(1, nObjs); // file id should be the only one left open
    }

    /**
     * Test method for {@link ncsa.hdf.object.Group#isRoot()}.
     * 
     * <ul>
     * <li>Test for not root.
     * </ul>
     */
    public void testIsRoot() {
    	log.debug("testIsRoot");
        assertFalse(testGroup.isRoot());
        int nObjs = 0;
        try {
            nObjs = H5.H5Fget_obj_count(testFile.getFID(),
                    HDF5Constants.H5F_OBJ_ALL);
        }
        catch (final Exception ex) {
            fail("H5.H5Fget_obj_count() failed. " + ex);
        }
        assertEquals(1, nObjs); // file id should be the only one left open
    }

    /**
     * Test method for {@link ncsa.hdf.object.Group#getNumberOfMembersInFile()}.
     * 
     * <ul>
     * <li>Test for the number of members in the file.
     * <ul>
     */
    public void testGetNumberOfMembersInFile() {
    	log.debug("testGetNumberOfMembersInFile");
        assertEquals(testGroup.getNumberOfMembersInFile(), 8);
        int nObjs = 0;
        try {
            nObjs = H5.H5Fget_obj_count(testFile.getFID(),
                    HDF5Constants.H5F_OBJ_ALL);
        }
        catch (final Exception ex) {
            fail("H5.H5Fget_obj_count() failed. " + ex);
        }
        assertEquals(1, nObjs); // file id should be the only one left open
    }

}
