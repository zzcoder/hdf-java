/**
 * 
 */
package test.unittests;

import ncsa.hdf.hdf5lib.H5;
import ncsa.hdf.hdf5lib.HDF5Constants;
import ncsa.hdf.object.h5.H5Datatype;
import ncsa.hdf.object.h5.H5Group;
import ncsa.hdf.object.Attribute;
import ncsa.hdf.object.Datatype;
import ncsa.hdf.object.FileFormat;
import ncsa.hdf.object.h5.H5File;
import junit.framework.TestCase;
import java.util.List;
/**
 * @author Rishi R. Sinha
 *
 */
public class AttributeTest extends TestCase {
	private static final H5File H5FILE = new H5File();
	    
    private H5File testFile = null;
   	private H5Group testGroup = null;
   	private Attribute strAttr = null;
   	private Attribute arrayIntAttr = null;

   	/**
	 * @param arg0
	 */
	public AttributeTest(String arg0) {
		super(arg0);
	}

	/* (non-Javadoc)
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
		
        testFile = (H5File)H5FILE.open(H5TestFile.NAME_FILE_H5, FileFormat.WRITE);
        assertNotNull(testFile);
        testGroup = (H5Group) testFile.get(H5TestFile.NAME_GROUP_ATTR);
        assertNotNull(testGroup);
		List testAttrs = testGroup.getMetadata();
		assertNotNull(testAttrs);
		strAttr = (Attribute) testAttrs.get(0);
		assertNotNull(strAttr);
		arrayIntAttr = (Attribute) testAttrs.get(1);
		assertNotNull(arrayIntAttr);
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

	/**
	 * Test method for {@link ncsa.hdf.object.Attribute#Attribute(java.lang.String, ncsa.hdf.object.Datatype, long[])}.
	 */
	public final void testAttributeStringDatatypeLongArray() {
		long[] attrDims = {1};
        String attrName = "CLASS";
        String[] classValue = {"IMAGE"};
        Datatype attrType = new H5Datatype(Datatype.CLASS_STRING, classValue[0].length()+1, -1, -1);
        Attribute attr = new Attribute(attrName, attrType, attrDims);
        attr.setValue(classValue);
        assertNotNull(attr);
	}

	/**
	 * Test method for {@link ncsa.hdf.object.Attribute#Attribute(java.lang.String, ncsa.hdf.object.Datatype, long[], java.lang.Object)}.
	 */
	public final void testAttributeStringDatatypeLongArrayObject() {
		long[] attrDims = {1};
        String attrName = "CLASS";
        String[] classValue = {"IMAGE"};
        Datatype attrType = new H5Datatype(Datatype.CLASS_STRING, classValue[0].length()+1, -1, -1);
        Attribute attr = new Attribute(attrName, attrType, attrDims, classValue);
        assertNotNull(attr);
	}

	/**
	 * Test method for {@link ncsa.hdf.object.Attribute#getValue()}.
	 */
	public final void testGetValue() {
		String[] value = (String[]) strAttr.getValue();
		if (!value[0].equals("String attribute."))
			fail("getValue() fails.");
		
		int[] intValue = (int[]) arrayIntAttr.getValue();
		long[] dims = arrayIntAttr.getDataDims();
		
		for (int i = 0; i < dims[0]; i++) {
			if (intValue[i] != i+1)
				fail("getValue() fails");
		}
	}

	/**
	 * Test method for {@link ncsa.hdf.object.Attribute#setValue(java.lang.Object)}.
	 */
	public final void testSetValue() {
		String[] tempValue = {"Temp String Value"};
		String[] prevValue = (String[]) strAttr.getValue();
		strAttr.setValue(tempValue);
		String[] value = (String[]) strAttr.getValue();
		if (!value[0].equals("Temp String Value"))
			fail("setValue() fails.");
		strAttr.setValue(prevValue);
		
		int[] tempIntArray = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
		int[] intPrevValue = (int[]) arrayIntAttr.getValue();
		arrayIntAttr.setValue(tempIntArray);
		
		int[] intValue = (int[]) arrayIntAttr.getValue();
		long[] dims = arrayIntAttr.getDataDims();
		
		for (int i = 0; i < dims[0]; i++) {
			if (intValue[i] != i)
				fail("getValue() fails");
		}
		arrayIntAttr.setValue(intPrevValue);
	}

	/**
	 * Test method for {@link ncsa.hdf.object.Attribute#getName()}.
	 */
	public final void testGetName() {
		if (!strAttr.getName().equals("strAttr"))
			fail("getName() fails.");
		if (!arrayIntAttr.getName().equals("arrayInt"))
			fail("getName() fails");
	}

	/**
	 * Test method for {@link ncsa.hdf.object.Attribute#getRank()}.
	 */
	public final void testGetRank() {
		if (strAttr.getRank() != 1)
			fail("getRank() fails.");
		if (arrayIntAttr.getRank() != 1)
			fail("getRank() fails");
	}

	/**
	 * Test method for {@link ncsa.hdf.object.Attribute#getDataDims()}.
	 */
	public final void testGetDataDims() {
		if ((strAttr.getDataDims())[0] != 1)
			fail("getDataDims() fails.");
		if (arrayIntAttr.getDataDims()[0] != 10)
			fail("getDataDims() fails");
	}

	/**
	 * Test method for {@link ncsa.hdf.object.Attribute#getType()}.
	 */
	public final void testGetType() {
		if (!strAttr.getType().getDatatypeDescription().equals("String, length = 20"))
			fail("getType() fails.");
		if (!arrayIntAttr.getType().getDatatypeDescription().equals("32-bit integer"))
			fail("getType() fails");
	}

	/**
	 * Test method for {@link ncsa.hdf.object.Attribute#isUnsigned()}.
	 */
	public final void testIsUnsigned() {
		assertFalse(strAttr.isUnsigned());
		assertFalse(arrayIntAttr.isUnsigned());
	}

	/**
	 * Test method for {@link ncsa.hdf.object.Attribute#toString(java.lang.String)}.
	 */
	public final void testToStringString() {
		if (!strAttr.toString(",").equals("String attribute."))
			fail("toString(string) fails for string.");
		if (!arrayIntAttr.toString(",").equals("1,2,3,4,5,6,7,8,9,10"))
			fail("toString(string) fails for integer array.");
	}

}
