/**
 * 
 */
package test.view;

import ncsa.hdf.hdf5lib.H5;
import ncsa.hdf.hdf5lib.HDF5Constants;
import ncsa.hdf.object.FileFormat;
import ncsa.hdf.object.ScalarDS;
import ncsa.hdf.object.h5.H5File;
import ncsa.hdf.object.h5.H5ScalarDS;
import test.unittests.H5TestFile;
import ncsa.hdf.view.Tools;

import java.lang.reflect.*;

import junit.framework.TestCase;

/**
 * @author xcao
 *
 */
public class ToolsTest extends TestCase {
    private static final H5File H5FILE = new H5File();
    private static final double EPSILON = 0.0000001;

    private H5File testFile = null;
    private H5ScalarDS testDataset = null;

    /**
     * @param arg0
     */
    public ToolsTest(String arg0) {
        super(arg0);
    }

    /* (non-Javadoc)
     * @see junit.framework.TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();

        testFile = (H5File)H5FILE.open(H5TestFile.NAME_FILE_H5, FileFormat.WRITE);
        assertNotNull(testFile);
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
     * Test method for {@link ncsa.hdf.view.Tools#autoContrastCompute(java.lang.Object, double[], boolean)}.
     */
    public final void testAutoContrastCompute() {
        double[] params = {0, 0};
        ScalarDS dset = null;
        Object data = null;
        double min=0, max=1;
        double gain=0, bias=0;
        
        for (int i=0; i<TestFile.OBJ_NAMES.length; i++) {
            dset = null;
            data = null;
            
            min = TestFile.DATA_AUTO_MIN;
            max = TestFile.DATA_AUTO_MAX;

            try {
                dset = (ScalarDS)testFile.get(TestFile.OBJ_NAMES[i]);
                data = dset.getData();
                dset.convertFromUnsignedC();
                data = dset.getData();
            } catch (Exception ex) {
                fail("file.get() failed. "+ex);
            }
            assertNotNull(data);
            Tools.autoContrastCompute(data, params, TestFile.isUnsigned[i]);

            if (TestFile.DATA_NEW_MIN >= 0) {
                min = TestFile.DATA_NEW_MIN;
            }
            
            if (TestFile.DATA_NEW_MAX <= TestFile.DATA_MAX_VALUES[i]) {
                max = TestFile.DATA_NEW_MAX;
            }
            
            gain = TestFile.DATA_MAX_VALUES[i]/(max-min);
            bias = -min;

            assertEquals(gain, params[0], gain*EPSILON);
            assertEquals(bias, params[1], bias*EPSILON);
        }
    }

    /**
     * Test method for {@link ncsa.hdf.view.Tools#autoContrastApply(java.lang.Object, java.lang.Object, double[], boolean)}.
     */
    public final void testAutoContrastApply() {
        double[] params = {0, 0};
        ScalarDS dset = null;
        Object data = null;
        double min=0, max=1;
        double gain=0, bias=0, expected=0;
        Object data_out = null;
        
        for (int i=0; i<TestFile.OBJ_NAMES.length; i++) {
            dset = null;
            data = null;
            data_out = null;
            
            min = TestFile.DATA_AUTO_MIN;
            max = TestFile.DATA_AUTO_MAX;

            try {
                dset = (ScalarDS)testFile.get(TestFile.OBJ_NAMES[i]);
                data = dset.getData();
                dset.convertFromUnsignedC();
                data = dset.getData();
            } catch (Exception ex) {
                fail("file.get() failed. "+ex);
            }
            assertNotNull(data);
            
            Tools.autoContrastCompute(data, params, TestFile.isUnsigned[i]);
            data_out = Tools.autoContrastApply(data, data_out, params, TestFile.isUnsigned[i]);
            assertNotNull(data_out);

            if (TestFile.DATA_NEW_MIN >= 0) {
                min = TestFile.DATA_NEW_MIN;
            }
            
            if (TestFile.DATA_NEW_MAX <= TestFile.DATA_MAX_VALUES[i]) {
                max = TestFile.DATA_NEW_MAX;
            }
            
            gain = TestFile.DATA_MAX_VALUES[i]/(max-min);
            bias = -min;
            
            for (int j=0; j<TestFile.DATA_INT8.length; j++) {
                expected = (TestFile.DATA_INT8[j] + bias)*gain;
                if (expected < 0) {
                    expected = 0;
                } else if (expected>TestFile.DATA_MAX_VALUES[i]) {
                    expected = TestFile.DATA_MAX_VALUES[i];
                }
                assertEquals(expected, Array.getDouble(data_out, j), Math.max(1, expected*EPSILON));
            }
        }
    }
    
    /**
     * Test method for {@link ncsa.hdf.view.Tools#autoContrastConvertImageBuffer(java.lang.Object, byte[], boolean)}.
     * <p>
     * Test if the byte data is calculated based on the following
     * <pre>
         uint_8  x
         int_8   (x & 0x7F) << 1
         uint_16 (x >> 8) & 0xFF
         int_16  (x >> 7) & 0xFF
         uint_32 (x >> 24) & 0xFF
         int_32  (x >> 23) & 0xFF
         uint_64 (x >> 56) & 0xFF
         int_64  (x >> 55) & 0xFF
       </pre>
     */
    public final void testAutoContrastConvertImageBuffer() {
        double[] params = {0, 0};
        ScalarDS dset = null;
        Object data = null;
        byte expected=0;
        Object data_out = null;
        byte[] imgData = new byte[TestFile.DIM_SIZE];
        int retValue = 0;
        
        for (int i=0; i<TestFile.OBJ_NAMES.length; i++) {
            dset = null;
            data = null;
            data_out = null;
            
            try {
                dset = (ScalarDS)testFile.get(TestFile.OBJ_NAMES[i]);
                data = dset.getData();
                dset.convertFromUnsignedC();
                data = dset.getData();
            } catch (Exception ex) {
                fail("file.get() failed. "+ex);
            }
            assertNotNull(data);
            
            retValue =  Tools.autoContrastCompute(data, params, TestFile.isUnsigned[i]);
            assertTrue(retValue>=0);
           
            data_out = Tools.autoContrastApply(data, data_out, params, TestFile.isUnsigned[i]);
            assertNotNull(data_out);

            retValue = Tools.autoContrastConvertImageBuffer(data_out, imgData, TestFile.isUnsigned[i]);
            assertTrue(retValue>=0);

            for (int j=0; j<TestFile.DATA_INT8.length; j++) {
                switch (i) {
                    case 0:
                        expected = (byte) ((Array.getByte(data_out, j) & 0x7F) << 1);
                        break;
                    case 1:
                        expected = (byte) Array.getShort(data_out, j);
                        break;
                    case 2:
                        expected = (byte) ((Array.getShort(data_out, j) >> 7) & 0xFF);
                        break;
                    case 3:
                        expected = (byte) ((Array.getInt(data_out, j) >> 8) & 0xFF);
                        break;
                    case 4:
                        expected = (byte) ((Array.getInt(data_out, j) >> 23) & 0xFF);
                        break;
                    case 5:
                        expected = (byte) ((Array.getLong(data_out, j) >> 24) & 0xFF);
                        break;
                    case 6:
                        expected = (byte) ((Array.getLong(data_out, j) >> 55) & 0xFF);
                        break;
                }
                assertEquals(expected, imgData[j]);
            }
        }
     }

    /**
     * Test method for {@link ncsa.hdf.view.Tools#autoContrastComputeSliderRange(double[], double[], double[])}.
     public final void testAutoContrastComputeSliderRange() {
        fail("Not yet implemented"); // TODO
    }
    */

    /**
     * Test method for {@link ncsa.hdf.view.Tools#autoContrastComputeMinMax(java.lang.Object, double[])}.
     */
    public final void testAutoContrastComputeMinMax() {
        double[] minmax = {0, 0};
        ScalarDS dset = null;
        Object data = null;

        for (int i=0; i<TestFile.OBJ_NAMES.length; i++) {
            dset = null;
            data = null;
            
            try {
                dset = (ScalarDS)testFile.get(TestFile.OBJ_NAMES[i]);
                data = dset.getData();
                dset.convertFromUnsignedC();
                data = dset.getData();
            } catch (Exception ex) {
                fail("file.get() failed. "+ex);
            }
            assertNotNull(data);
            Tools.autoContrastComputeMinMax(data, minmax);

            assertEquals(TestFile.DATA_AUTO_MIN, minmax[0], EPSILON);
            assertEquals(TestFile.DATA_AUTO_MAX, minmax[1], EPSILON);
        }
    }
    
    /**
     * Test method for {@link ncsa.hdf.view.Tools#findMinMax(java.lang.Object, double[])}.
     */
    public final void testFindMinMax() {
        double[] minmax = {0, 0};
        ScalarDS dset = null;
        Object data = null;
        
        for (int i=0; i<TestFile.OBJ_NAMES.length; i++) {
            dset = null;
            data = null;
            
            try {
                dset = (ScalarDS)testFile.get(TestFile.OBJ_NAMES[i]);
                data = dset.getData();
                dset.convertFromUnsignedC();
                data = dset.getData();
            } catch (Exception ex) {
                fail("file.get() failed. "+ex);
            }
            assertNotNull(data);
            Tools.findMinMax(data, minmax);

            assertEquals(TestFile.DATA_MIN, minmax[0], EPSILON);
            assertEquals(TestFile.DATA_MAX, minmax[1], EPSILON);
        }
    }

    /**
     * Test method for {@link ncsa.hdf.view.Tools#computeStatistics(java.lang.Object, double[])}.
     */
    public final void testComputeStatistics() {
        double[] avgstd = {0, 0};
        ScalarDS dset = null;
        Object data = null;
        
        for (int i=0; i<TestFile.OBJ_NAMES.length; i++) {
            dset = null;
            data = null;
            
            try {
                dset = (ScalarDS)testFile.get(TestFile.OBJ_NAMES[i]);
                data = dset.getData();
                dset.convertFromUnsignedC();
                data = dset.getData();
            } catch (Exception ex) {
                fail("file.get() failed. "+ex);
            }
            assertNotNull(data);
            Tools.computeStatistics(data, avgstd);

            assertEquals(TestFile.DATA_AVG, avgstd[0], EPSILON);
            assertEquals(TestFile.DATA_STD, avgstd[1], EPSILON);
        }
    }

    /**
     * Test method for {@link ncsa.hdf.view.Tools#convertImageToHDF(java.lang.String, java.lang.String, java.lang.String, java.lang.String)}.
    public final void testConvertImageToHDF() {
        fail("Not yet implemented"); // TODO
    }
     */

    /**
     * Test method for {@link ncsa.hdf.view.Tools#saveImageAs(java.awt.image.BufferedImage, java.io.File, java.lang.String)}.
    public final void testSaveImageAs() {
        fail("Not yet implemented"); // TODO
    }
     */
    
    /**
     * Test method for {@link ncsa.hdf.view.Tools#createGrayPalette()}.
     public final void testCreateGrayPalette() {
        fail("Not yet implemented"); // TODO
    }
    */

    /**
     * Test method for {@link ncsa.hdf.view.Tools#createReverseGrayPalette()}.
    public final void testCreateReverseGrayPalette() {
        fail("Not yet implemented"); // TODO
    }
     */

    /**
     * Test method for {@link ncsa.hdf.view.Tools#createGrayWavePalette()}.
    public final void testCreateGrayWavePalette() {
        fail("Not yet implemented"); // TODO
    }
     */

    /**
     * Test method for {@link ncsa.hdf.view.Tools#createRainbowPalette()}.
     public final void testCreateRainbowPalette() {
        fail("Not yet implemented"); // TODO
    }
    */

    /**
     * Test method for {@link ncsa.hdf.view.Tools#createNaturePalette()}.
    public final void testCreateNaturePalette() {
        fail("Not yet implemented"); // TODO
    }
     */

    /**
     * Test method for {@link ncsa.hdf.view.Tools#createWavePalette()}.
    public final void testCreateWavePalette() {
        fail("Not yet implemented"); // TODO
    }
     */

    /**
     * Test method for {@link ncsa.hdf.view.Tools#hasAlpha(java.awt.Image)}.
    public final void testHasAlpha() {
        fail("Not yet implemented"); // TODO
    }
     */

    /**
     * Test method for {@link ncsa.hdf.view.Tools#createIndexedImage(byte[], byte[][], int, int)}.
    public final void testCreateIndexedImage() {
        fail("Not yet implemented"); // TODO
    }
     */

    /**
     * Test method for {@link ncsa.hdf.view.Tools#createTrueColorImage(byte[], boolean, int, int)}.
     public final void testCreateTrueColorImage() {
        fail("Not yet implemented"); // TODO
    }
    */

    /**
     * Test method for {@link ncsa.hdf.view.Tools#getBytes(java.lang.Object, double[], byte[])}.
    public final void testGetBytesObjectDoubleArrayByteArray() {
        fail("Not yet implemented"); // TODO
    }
     */

    /**
     * Test method for {@link ncsa.hdf.view.Tools#getBytes(java.lang.Object, double[], int, int, boolean, byte[])}.
    public final void testGetBytesObjectDoubleArrayIntIntBooleanByteArray() {
        fail("Not yet implemented"); // TODO
    }
     */

    /**
     * Test method for {@link ncsa.hdf.view.Tools#getBytes(java.lang.Object, double[], java.lang.Object, byte[])}.
    public final void testGetBytesObjectDoubleArrayObjectByteArray() {
        fail("Not yet implemented"); // TODO
    }
     */

    /**
     * Test method for {@link ncsa.hdf.view.Tools#getBytes(java.lang.Object, double[], int, int, boolean, java.lang.Object, byte[])}.
    public final void testGetBytesObjectDoubleArrayIntIntBooleanObjectByteArray() {
        fail("Not yet implemented"); // TODO
    }
     */

    /**
     * Test method for {@link ncsa.hdf.view.Tools#newInstance(java.lang.Class, java.lang.Object[])}.
    public final void testNewInstance() {
        fail("Not yet implemented"); // TODO
    }
     */

}
