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
import ncsa.hdf.object.ScalarDS;

/**
 * @author rsinha
 *
 */
public class ScalarDSTest extends TestCase {
	private static final H5File H5FILE = new H5File();

	private H5File testFile = null;
	private H5Group testGroup = null;
	private ScalarDS intDset = null;
	private ScalarDS floatDset = null;
	private ScalarDS charDset = null;
	private ScalarDS strDset = null;
	private ScalarDS enumDset = null;
	private ScalarDS imageDset = null;
	private ScalarDS imagePalete = null;
	private ScalarDS ORDset = null;
	/**
	 * @param arg0
	 */
	public ScalarDSTest(String arg0) {
		super(arg0);
	}

	/* (non-Javadoc)
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {		
		super.setUp();
		testFile = (H5File)H5FILE.open(H5TestFile.NAME_FILE_H5, FileFormat.READ);
		assertNotNull(testFile);
		testGroup = (H5Group) testFile.get(H5TestFile.NAME_GROUP_ATTR);
		assertNotNull(testGroup);

		intDset = (ScalarDS) testFile.get(H5TestFile.NAME_DATASET_INT);
		assertNotNull(intDset);
		intDset.init();
		floatDset = (ScalarDS) testFile.get(H5TestFile.NAME_DATASET_FLOAT);
		assertNotNull(floatDset);
		floatDset.init();
		charDset = (ScalarDS) testFile.get(H5TestFile.NAME_DATASET_CHAR);
		assertNotNull(charDset);
		charDset.init();
		strDset = (ScalarDS) testFile.get(H5TestFile.NAME_DATASET_STR);
		assertNotNull(strDset);
		strDset.init();
		enumDset = (ScalarDS) testFile.get(H5TestFile.NAME_DATASET_ENUM);
		assertNotNull(enumDset);
		enumDset.init();
		imageDset = (ScalarDS) testFile.get(H5TestFile.NAME_DATASET_IMAGE);
		assertNotNull(imageDset);
		imageDset.init();
		ORDset = (ScalarDS) testFile.get(H5TestFile.NAME_DATASET_OBJ_REF);
		assertNotNull(ORDset);
		ORDset.init();
		imagePalete = (ScalarDS) testFile.get(H5TestFile.NAME_DATASET_IMAGE_PALETTE);
		assertNotNull(imagePalete);
		imagePalete.init();
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
	 * This method tests whether the getInterlace, getImageDataRange, isImage, isImageDisplay, 
	 * setIsImageDisplay, getIsunsignedByteForImage, setIsUnsignedByteForImage
	 * I am testing the various functionalities specific to images on the image dataset.
	 * For non image specific functionalities like isImage I test them on all available datasets.
	 */
	public final void testImageFunctionality() {
		if (!imageDset.isImage())
			fail("Image Dataset is being returned as an non image dataset by isImage.");
		if (!imageDset.isImageDisplay())
			fail("Image Dataset is being returned as having non image display by isImageDisplay.");
		/*double[] min_max = imageDset.getImageDataRange();
		if (min_max == null)
			fail("getImageDataRange returns null for the Image.");
		if (min_max[0] != 0.0 || min_max[1] != 256.0)
			fail("Min is 0.0 and Max is 256.0 while getImageDataRange returns Min as " + min_max[0] + "and Max as " + min_max[1]);*/
		if (imageDset.getInterlace() != -1)
			fail("Interlace should be -1 while getInterlace reports it as " + imageDset.getInterlace());
		/*if (!imageDset.getIsUnsignedByteForImage())
			fail("isUnsignedByteForImage is returned as false for an Image.");*/
		imageDset.setIsUnsignedByteForImage(true);
		if (!imageDset.getIsUnsignedByteForImage())
			fail("isUnsignedByteForImage returns false after an explicity setIsUnsignedByteForImage(true).");
		/*imageDset.setIsUnsignedByteForImage(false);
		if (imageDset.getIsUnsignedByteForImage())
			fail("isUnsignedByteForImage returns true after an explicity setIsUnsignedByteForImage(false).");*/
		if (imageDset.isTrueColor())
			fail("isTrueColor returns true for Image Dataset.");
		if (intDset.isImage())
			fail("Integer Dataset is being returned as an image dataset by isImage.");
		if (floatDset.isImage())
			fail("Float Dataset is being returned as an image dataset by isImage.");
		if (charDset.isImage())
			fail("Character Dataset is being returned as an image dataset by isImage.");
		if (enumDset.isImage())
			fail("Enumeration Dataset is being returned as an image dataset by isImage.");
		if (imagePalete.isImage())
			fail("Image Palete is being returned as an image dataset by isImage.");
		if (ORDset.isImage())
			fail("Object Reference Dataset is being returned as an image dataset by isImage.");
		if (intDset.isImageDisplay())
			fail("Integer Dataset is being returned as an image display dataset by isImageDisplay.");
		if (floatDset.isImageDisplay())
			fail("Float Dataset is being returned as an image dispay dataset by isImageDisplay.");
		if (charDset.isImageDisplay())
			fail("Character Dataset is being returned as an image dispay dataset by isImageDisplay.");
		if (enumDset.isImageDisplay())
			fail("Enumeration Dataset is being returned as an image dispay dataset by isImageDisplay.");
		if (imagePalete.isImageDisplay())
			fail("Image Palete is being returned as an image dispay dataset by isImageDisplay.");
		if (ORDset.isImageDisplay())
			fail("Object Reference Dataset is being returned as an image dispay dataset by isImageDisplay.");
		intDset.setIsImageDisplay(true);
		if (!intDset.isImageDisplay())
			fail("isImageDisplay returns false after an explicity setIsImageDisplay(true).");
		intDset.setIsImageDisplay(false);
		if (intDset.isImageDisplay())
			fail("isImageDisplay returns true after an explicity setIsImageDisplay(false).");
	}
	
	/*
	 * Testing to make sure isText() method works. I test it against all available datasets. 
	 */
	public final void testIsText() {
		if (!strDset.isText())
			fail("isText returns false for the string dataset.");
		if (imageDset.isText())
			fail("isText returns true for Image Dataset.");
		if (intDset.isText())
			fail("isText returns true for an Integer Dataset.");
		if (floatDset.isText())
			fail("isText returns true for a Float Dataset.");
		if (charDset.isText())
			fail("isText returns true for a Character Dataset.");
		if (enumDset.isText())
			fail("isText returns true for an Enumeration Dataset.");
		if (imagePalete.isText())
			fail("isText returns true for an Image Palete Dataset.");
		if (ORDset.isText())
			fail("isText returns true for an Object Reference Dataset.");
	}
}
