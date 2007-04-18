/**
 * 
 */
package test.unittests;

import ncsa.hdf.object.FileFormat;
import ncsa.hdf.object.h5.*;
import junit.framework.TestCase;

/**
 * @author xcao
 *
 */
public class H5CompoundDSTest extends TestCase {
    private static final H5File H5FILE = new H5File();
    private H5File testFile = null;
    private H5CompoundDS dset = null;
    
    /**
     * @param arg0
     */
    public H5CompoundDSTest(String arg0) {
        super(arg0);
    }

    /* (non-Javadoc)
     * @see junit.framework.TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
        
        try { H5TestFile.createTestFile(); } 
        catch (Exception ex) {
            fail("*** Unable to create test file. "+ex);
        }
        
        testFile = (H5File)H5FILE.open(H5TestFile.NAME_FILE_H5, FileFormat.WRITE);
        dset = (H5CompoundDS)testFile.get(H5TestFile.NAME_DATASET_COMPOUND);
    }

    /* (non-Javadoc)
     * @see junit.framework.TestCase#tearDown()
     */
    protected void tearDown() throws Exception {
        super.tearDown();
        
        if (testFile != null) {
            try { testFile.close(); } catch (Exception ex) {}
            testFile = null;
        }
    }

    /**
     * Test method for {@link ncsa.hdf.object.h5.H5CompoundDS#setName(java.lang.String)}.
     */
    public final void testSetName() {
        fail("Not yet implemented"); // TODO
    }

    /**
     * Test method for {@link ncsa.hdf.object.h5.H5CompoundDS#open()}.
     */
    public final void testOpen() {
        fail("Not yet implemented"); // TODO
    }

    /**
     * Test method for {@link ncsa.hdf.object.h5.H5CompoundDS#close(int)}.
     */
    public final void testClose() {
        fail("Not yet implemented"); // TODO
    }

    /**
     * Test method for {@link ncsa.hdf.object.h5.H5CompoundDS#clear()}.
     */
    public final void testClear() {
        fail("Not yet implemented"); // TODO
    }

    /**
     * Test method for {@link ncsa.hdf.object.h5.H5CompoundDS#init()}.
     */
    public final void testInit() {
        
        for (int testRound=0; testRound<2; testRound++) {
            dset.init();

            // test the rank
            int rank = dset.getRank();
            assertEquals(H5TestFile.RANK, rank);
            
            // test the dimesin sizes
            long[] dims = dset.getDims();
            assertNotNull(dims);
            for (int i=0; i<rank; i++) {
                assertEquals(H5TestFile.DIMs[i], dims[i]);
            }
            
            // start at 0
            long[] start = dset.getStartDims();
            assertNotNull(start);
            for (int i=0; i<rank; i++) {
                assertEquals(0, start[i]);
            }
           
            // test selection
            long[] selectedDims = dset.getSelectedDims();
            int[] selectedIndex = dset.getSelectedIndex();
            assertNotNull(selectedDims);
            assertNotNull(selectedIndex);
            if (rank == 1)
            {
                assertEquals(0, selectedIndex[0]);
                assertEquals(dims[0], selectedDims[0]);
            }
            else if (rank == 2)
            {
                assertEquals(0, selectedIndex[0]);
                assertEquals(1, selectedIndex[1]);
                assertEquals(dims[0], selectedDims[0]);
                assertEquals(dims[1], selectedDims[1]);
            }
            else if (rank > 2)
            {
                assertEquals(rank-2, selectedIndex[0]); // columns
                assertEquals(rank-1, selectedIndex[1]); // rows
                assertEquals(rank-3, selectedIndex[2]);
                assertEquals(dims[rank-1], selectedDims[rank-1]);
                assertEquals(dims[rank-2], selectedDims[rank-2]);
            }
            
            // by default, all members are selected
            int nmembers = dset.getSelectedMemberCount();
            assertTrue(nmembers > 0);
            for (int i=0; i<nmembers; i++) {
                assertTrue(dset.isMemberSelected(i));
            }
            
            // make some change and do another round of test
            // to make sure that the init() resets the default
            for (int i=0; i< rank; i++) {
                start[i] = 1;
                selectedDims[0] = 1;
            }
            for (int i=0; i<nmembers; i++) {
                dset.setMemberSelection(false);
            }
         } //for (int testRound=0; testRound<2; testRound++) {
    } //public final void testInit() {

    /**
     * Test method for {@link ncsa.hdf.object.h5.H5CompoundDS#read()}.
     */
    public final void testRead() {
        fail("Not yet implemented"); // TODO
    }

    /**
     * Test method for {@link ncsa.hdf.object.h5.H5CompoundDS#readBytes()}.
     */
    public final void testReadBytes() {
        fail("Not yet implemented"); // TODO
    }

    /**
     * Test method for {@link ncsa.hdf.object.h5.H5CompoundDS#write(java.lang.Object)}.
     */
    public final void testWriteObject() {
        fail("Not yet implemented"); // TODO
    }

    /**
     * Test method for {@link ncsa.hdf.object.h5.H5CompoundDS#getDatatype()}.
     */
    public final void testGetDatatype() {
        fail("Not yet implemented"); // TODO
    }

    /**
     * Test method for {@link ncsa.hdf.object.h5.H5CompoundDS#isString(int)}.
     */
    public final void testIsString() {
        fail("Not yet implemented"); // TODO
    }

    /**
     * Test method for {@link ncsa.hdf.object.h5.H5CompoundDS#getSize(int)}.
     */
    public final void testGetSize() {
        fail("Not yet implemented"); // TODO
    }

    /**
     * Test method for {@link ncsa.hdf.object.h5.H5CompoundDS#H5CompoundDS(ncsa.hdf.object.FileFormat, java.lang.String, java.lang.String)}.
     */
    public final void testH5CompoundDSFileFormatStringString() {
        fail("Not yet implemented"); // TODO
    }

    /**
     * Test method for {@link ncsa.hdf.object.h5.H5CompoundDS#H5CompoundDS(ncsa.hdf.object.FileFormat, java.lang.String, java.lang.String, long[])}.
     */
    public final void testH5CompoundDSFileFormatStringStringLongArray() {
        fail("Not yet implemented"); // TODO
    }

    /**
     * Test method for {@link ncsa.hdf.object.h5.H5CompoundDS#getMetadata()}.
     */
    public final void testGetMetadata() {
        fail("Not yet implemented"); // TODO
    }

    /**
     * Test method for {@link ncsa.hdf.object.h5.H5CompoundDS#writeMetadata(java.lang.Object)}.
     */
    public final void testWriteMetadata() {
        fail("Not yet implemented"); // TODO
    }

    /**
     * Test method for {@link ncsa.hdf.object.h5.H5CompoundDS#removeMetadata(java.lang.Object)}.
     */
    public final void testRemoveMetadata() {
        fail("Not yet implemented"); // TODO
    }

    /**
     * Test method for {@link ncsa.hdf.object.h5.H5CompoundDS#create(java.lang.String, ncsa.hdf.object.Group, long[], java.lang.String[], ncsa.hdf.object.Datatype[], int[], java.lang.Object)}.
     */
    public final void testCreateStringGroupLongArrayStringArrayDatatypeArrayIntArrayObject() {
        fail("Not yet implemented"); // TODO
    }

    /**
     * Test method for {@link ncsa.hdf.object.h5.H5CompoundDS#create(java.lang.String, ncsa.hdf.object.Group, long[], java.lang.String[], ncsa.hdf.object.Datatype[], int[], int[][], java.lang.Object)}.
     */
    public final void testCreateStringGroupLongArrayStringArrayDatatypeArrayIntArrayIntArrayArrayObject() {
        fail("Not yet implemented"); // TODO
    }

    /**
     * Test method for {@link ncsa.hdf.object.h5.H5CompoundDS#create(java.lang.String, ncsa.hdf.object.Group, long[], long[], long[], int, java.lang.String[], ncsa.hdf.object.Datatype[], int[], int[][], java.lang.Object)}.
     */
    public final void testCreateStringGroupLongArrayLongArrayLongArrayIntStringArrayDatatypeArrayIntArrayIntArrayArrayObject() {
        fail("Not yet implemented"); // TODO
    }
    
}
