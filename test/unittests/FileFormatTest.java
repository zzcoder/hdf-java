/**
 * 
 */
package test.unittests;

import junit.framework.TestCase;
import ncsa.hdf.object.FileFormat;
import ncsa.hdf.object.h5.H5File;
import ncsa.hdf.object.Group;
import ncsa.hdf.object.Datatype;
import ncsa.hdf.object.ScalarDS;
import java.util.Enumeration;
import java.util.Iterator;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;

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

	/* (non-Javadoc)
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
		testFile = (FileFormat)H5FILE.open(H5TestFile.NAME_FILE_H5, FileFormat.WRITE);
        assertNotNull(testFile);
        testFile.open();
	}

	/* (non-Javadoc)
	 * @see junit.framework.TestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		super.tearDown();
	}

	/**
	 * Test method for {@link ncsa.hdf.object.FileFormat#FileFormat(java.lang.String)}.
	 */
	public final void testFileFormat() {
		FileFormat f = new H5File(H5TestFile.NAME_FILE_H5);
		assertNotNull(f);
	}

	/**
	 * Test method for {@link ncsa.hdf.object.FileFormat#open()}.
	 */
	public final void testOpen() {
		int fid = 0;
		try {
			fid = testFile.open();
		} catch (Exception ex) {
			fail("open() failed " + ex.getMessage());
		}
		if (fid < 0)
			fail("open() failed");
		try {
		testFile.close();
		} catch (Exception ex) {
			fail("close() failed" + ex.getMessage());
		}
		try {
			testFile.open();
		} catch (Exception ex) {
			fail("open() failed " + ex.getMessage());
		}
        assertNotNull(testFile);
	}

	/**
	 * Test method for {@link ncsa.hdf.object.FileFormat#open(java.lang.String, int)}.
	 */
	public final void testOpenStringInt() {
	    // Request the implementing class of FileFormat: H5File
	    FileFormat h5file = FileFormat.getFileFormat(FileFormat.FILE_TYPE_HDF5);
	         
	    try {
	     FileFormat test1 =  h5file.open(H5TestFile.NAME_FILE_H5, FileFormat.WRITE);
	     assertNotNull(test1);
	     test1.close();
	    } catch (Exception ex) {
	    	fail("open(String, Int) failed " + ex.getMessage());
	    }
	    
	}

	/**
	 * Test method for {@link ncsa.hdf.object.FileFormat#create(java.lang.String, int)}.
	 */
	public final void testCreateStringInt() {
		FileFormat f = FileFormat.getFileFormat(FileFormat.FILE_TYPE_HDF5);
		
		try {
			FileFormat test1 = f.create(H5TestFile.NAME_FILE_H5, FileFormat.FILE_CREATE_OPEN);
			Group g1 = (Group) test1.get(H5TestFile.NAME_GROUP);
			assertNotNull(g1);
		} catch (Exception ex) {
			fail("Create Failed " + ex.getMessage());
		}
		try {
			FileFormat test1 = f.create(H5TestFile.NAME_FILE_H5, FileFormat.FILE_CREATE_DELETE);
		} catch (Exception ex) {
			; //Expected to fail.
		}
	}

	/**
	 * Test method for {@link ncsa.hdf.object.FileFormat#getNumberOfMembers()}.
	 */
	public final void testGetNumberOfMembers() {
		int n = testFile.getNumberOfMembers();
		if (n != 21)
			fail("getNumberOfMembers() fails\n");
	}

	/**
	 * Test method for {@link ncsa.hdf.object.FileFormat#close()}.
	 */
	public final void testClose() {
		testOpen();
	}

	/**
	 * Test method for {@link ncsa.hdf.object.FileFormat#getRootNode()}.
	 */
	public final void testGetRootNode() {
		assertNotNull(testFile);
		try {
		testFile.open();
		}
		catch (Exception e) {
			;
		}
		DefaultMutableTreeNode theNode = (DefaultMutableTreeNode) testFile.getRootNode();
		assertNotNull(theNode);
		Group grp = (Group) theNode.getUserObject();
		if (!grp.getFullName().equals("/" + H5TestFile.NAME_FILE_H5))
			fail("getRootNode() fails");
	}

	/**
	 * Test method for {@link ncsa.hdf.object.FileFormat#getFilePath()}.
	 */
	public final void testGetFilePath() {
		if (!testFile.getFilePath().equals("TestHDF5.h5"))
			fail("getFilePath() fails");
	}

	/**
	 * Test method for {@link ncsa.hdf.object.FileFormat#isReadOnly()}.
	 */
	public final void testIsReadOnly() {
		if (testFile.isReadOnly())
			fail("isReady() fails."); // TODO
	}

	/**
	 * Test method for {@link ncsa.hdf.object.FileFormat#createDatatype(int, int, int, int)}.
	 */
	public final void testCreateDatatypeIntIntIntInt() {
		Datatype dt = null;
		try {
		dt = testFile.createDatatype(Datatype.CLASS_INTEGER, 32, Datatype.ORDER_NONE, Datatype.NSGN);
		} catch (Exception ex) {
			fail("createDatatype() failed" + ex.getMessage());
		}
		if (!dt.getDatatypeDescription().equals("32-bit integer"))
			fail("createDatatype(int, int, int, int) created wrong datatype"); // TODO
	}

	/**
	 * Test method for {@link ncsa.hdf.object.FileFormat#createDatatype(int, int, int, int, java.lang.String)}.
	 */
	public final void testCreateDatatypeIntIntIntIntString() {
		Datatype dt = null;
		try {
		dt = testFile.createDatatype(Datatype.CLASS_INTEGER, 32, Datatype.ORDER_NONE, Datatype.NSGN, "INTEGER");
		} catch (Exception ex) {
			fail("createDatatype() failed" + ex.getMessage());
		}
		System.out.println(dt.getDatatypeDescription());
		if (!dt.getDatatypeDescription().equals("32-bit integer"))
			fail("createDatatype(int, int, int, int, string) created wrong datatype");
	}

	/**
	 * Test method for {@link ncsa.hdf.object.FileFormat#addFileFormat(java.lang.String, ncsa.hdf.object.FileFormat)}.
	 *//*
	public final void testAddFileFormat() {
		fail("Not yet implemented"); // TODO
	}

	*//**
	 * Test method for {@link ncsa.hdf.object.FileFormat#removeFileFormat(java.lang.String)}.
	 *//*
	public final void testRemoveFileFormat() {
		fail("Not yet implemented"); // TODO
	}*/

	/**
	 * Test method for {@link ncsa.hdf.object.FileFormat#getFileFormat(java.lang.String)}.
	 */
	public final void testGetFileFormat() {
		FileFormat f = FileFormat.getFileFormat("HDF5");
		assertNotNull(f);
	}

	/**
	 * Test method for {@link ncsa.hdf.object.FileFormat#getFileFormatKeys()}.
	 */
	public final void testGetFileFormatKeys() {
		Enumeration e = FileFormat.getFileFormatKeys();
		String keys[] = {"HDF5", "HDF"};
		int pos = 0;
		while (e.hasMoreElements()) {
			if (!keys[pos++].equals((String)e.nextElement()))
				fail("getFileFormatKeys() failed.");
		}
	}

	/**
	 * Test method for {@link ncsa.hdf.object.FileFormat#getLibversion()}.
	 */
	public final void testGetLibversion() {
		if (!testFile.getLibversion().equals("NCSA HDF5 1.6.6"))
			fail("getLibVersion() fails."); // TODO
	}

	/**
	 * Test method for {@link ncsa.hdf.object.FileFormat#isThisType(java.lang.String)}.
	 */
	public final void testIsThisTypeString() {
		if (!testFile.isThisType(H5TestFile.NAME_FILE_H5))
			fail("isThisType(FileName) failed."); // TODO
	}

	/**
	 * Test method for {@link ncsa.hdf.object.FileFormat#isThisType(ncsa.hdf.object.FileFormat)}.
	 */
	public final void testIsThisTypeFileFormat() {
		if (!testFile.isThisType(testFile))
			fail("isThisType(FileFormat) failed."); // TODO
	}

	// CANNOT BE TESTED AS THE get methods here are protected.
/*	*//**
	 * Test method for {@link ncsa.hdf.object.FileFormat#setMaxMembers(int)}.
	 *//*
	public final void testSetMaxMembers() {
		testFile.setMaxMembers(1000);
		if ()
		fail("Not yet implemented"); // TODO
	}

	*//**
	 * Test method for {@link ncsa.hdf.object.FileFormat#setStartMembers(int)}.
	 *//*
	public final void testSetStartMembers() {
		fail("Not yet implemented"); // TODO
	}

	*//**
	 * Test method for {@link ncsa.hdf.object.FileFormat#getMaxMembers()}.
	 *//*
	public final void testGetMaxMembers() {
		fail("Not yet implemented"); // TODO
	}

	*//**
	 * Test method for {@link ncsa.hdf.object.FileFormat#getStartMembers()}.
	 *//*
	public final void testGetStartMembers() {
		fail("Not yet implemented"); // TODO
	}*/

	/**
	 * Test method for {@link ncsa.hdf.object.FileFormat#getFileExtensions()}.
	 */
	public final void testGetFileExtensions() {
		if (!FileFormat.getFileExtensions().equals("hdf, h4, hdf5, h5"))
			fail("getFileExtensions() fails");
	}

	/**
	 * Test method for {@link ncsa.hdf.object.FileFormat#getFID()}.
	 */
	public final void testGetFID() {
		int fid = testFile.getFID();
		if (fid == -1)
			fail("getFID() fails.");
	}

	/**
	 * Test method for {@link ncsa.hdf.object.FileFormat#addFileExtension(java.lang.String)}.
	 */
	public final void testAddFileExtension() {
		FileFormat.addFileExtension("he5");
		if (!FileFormat.getFileExtensions().equals("hdf, h4, hdf5, h5, he5"))
			fail("addFileExtension() fails");
	}

	/**
	 * Test method for {@link ncsa.hdf.object.FileFormat#getInstance(java.lang.String)}.
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
		catch (Exception ex){
			fail("getInstance() failed" + ex.getMessage());
		}
		assertNotNull(f);
	}

	/**
	 * Test method for {@link ncsa.hdf.object.FileFormat#get(java.lang.String)}.
	 */
	public final void testGet() {
		Group group = null;
		try {
			group = (Group) testFile.get(H5TestFile.NAME_GROUP);
			assertNotNull(group);
		} catch (Exception ex) {
			fail("get() failed " + ex.getMessage());
		}
		if (!group.getName().equals("g0"))
			fail("get() got the wrong group" + group.getName());
	}

	/**
	 * Test method for {@link ncsa.hdf.object.FileFormat#getFileFormats()}.
	 */
	public final void testGetFileFormats() {
		FileFormat f = FileFormat.getFileFormat("HDF5");
		assertNotNull(f);
	}

}
