/**
 * 
 */
package test.object;

import java.util.Enumeration;

import junit.framework.TestCase;
import ncsa.hdf.hdf5lib.H5;
import ncsa.hdf.hdf5lib.HDF5Constants;
import ncsa.hdf.object.FileFormat;
import ncsa.hdf.object.h5.H5File;

/**
 * @author rsinha
 * 
 */
public class FileFormatTest extends TestCase {
    private static final H5File H5FILE = new H5File();

    private FileFormat testFile = null;

    /**
     * @param arg0
     */
    public FileFormatTest(String arg0) {
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
        testFile = H5FILE.open(H5TestFile.NAME_FILE_H5, FileFormat.WRITE);
        assertNotNull(testFile);
        testFile.open();
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
     * Test method for
     * {@link ncsa.hdf.object.FileFormat#create(java.lang.String, int)}.
     * <p>
     * What to test:
     * <ul>
     * <li>Create a file that is already created with option FILE_CREATE_OPEN.
     * <li>Create a file that is already created and opened with option
     * FILE_CREATE_DELETE.
     * <li>Create a file that is already created and not opened with
     * FILE_CREATE_DELETE.
     * <li>Create a file that is new with FILE_CREATE_DELETE.
     * <li>Create a file that is new with FILE_CREATE_OPEN.
     * </ul>
     * 
     */
    /*
     * RUTH - come back and update this with new method, createInstance public
     * final void testCreateStringInt() { FileFormat f =
     * FileFormat.getFileFormat(FileFormat.FILE_TYPE_HDF5);
     * 
     * try { f.create(H5TestFile.NAME_FILE_H5, FileFormat.FILE_CREATE_OPEN); }
     * catch (Exception ex) { fail("Create Failed " + ex.getMessage()); } try {
     * f.create(H5TestFile.NAME_FILE_H5, FileFormat.FILE_CREATE_DELETE); } catch
     * (Exception ex) { ; //Expected to fail. } try { f.create("simpleFile",
     * FileFormat.FILE_CREATE_DELETE); } catch (Exception ex) {
     * fail("Create failed " + ex.getMessage()); } try { f.create("testFile",
     * FileFormat.FILE_CREATE_DELETE); } catch (Exception ex) {
     * fail("Create failed " + ex.getMessage()); } try { f.create("testFile",
     * FileFormat.FILE_CREATE_OPEN); } catch (Exception ex) {
     * fail("Create failed " + ex.getMessage()); } }
     */
    /**
     * Test method for {@link ncsa.hdf.object.FileFormat#getNumberOfMembers()}.
     * <p>
     * <ul>
     * <li>Test the number of compements.
     * </ul>
     */
    public final void testGetNumberOfMembers() {
        assertEquals(testFile.getNumberOfMembers(), 21);
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
     * {@link ncsa.hdf.object.FileFormat#getFileFormat(java.lang.String)}.
     * <p>
     * <ul>
     * <li>Test for HDF5.
     * </ul>
     */
    public final void testGetFileFormat() {
        FileFormat f = FileFormat.getFileFormat("HDF5");
        assertNotNull(f);
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
     * Test method for {@link ncsa.hdf.object.FileFormat#getFileFormatKeys()}.
     * <p>
     * <ul>
     * <li>current file formats are HDF5, HDF.
     * </ul>
     */
    public final void testGetFileFormatKeys() {
     	
    	Enumeration<String> e = FileFormat.getFileFormatKeys();

        while (e.hasMoreElements())
        	assertNotNull(FileFormat.getFileFormat((String)e.nextElement()));
        
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
     * Test method for {@link ncsa.hdf.object.FileFormat#getFID()}.
     * <p>
     * <ul>
     * <li>Make sure the fid is not -1.
     * </ul>
     */
    public final void testGetFID() {
        assertTrue((testFile.getFID() != -1));
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
     * {@link ncsa.hdf.object.FileFormat#getInstance(java.lang.String)}.
     * <p>
     * <ul>
     * <li>Open an non existing file.
     * <li>Open an exisiting file.
     * </ul>
     */
    public final void testGetInstance() {
        H5File f = null;
        try {
            f = (H5File) FileFormat.getInstance("test_hdf5.h5");
        }
        catch (Exception ex) {
            ;
        }
        assertNull(f);

        try {
            f = (H5File) FileFormat.getInstance(H5TestFile.NAME_FILE_H5);
        }
        catch (Exception ex) {
            fail("getInstance() failed" + ex.getMessage());
        }
        assertNotNull(f);
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
     * Test method for {@link ncsa.hdf.object.FileFormat#getFileFormats()}.
     * <p>
     * <ul>
     * <li>Test that the FileFormat object is formed for HDF5.
     * </ul>
     */
    public final void testGetFileFormats() {
        FileFormat f = FileFormat.getFileFormat("HDF5");
        assertNotNull(f);
        FileFormat f1 = FileFormat.getFileFormat("ALL");
        assertNull(f1);
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
