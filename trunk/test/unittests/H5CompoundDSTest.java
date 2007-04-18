/**
 * 
 */
package test.unittests;

import java.util.Vector;
import java.lang.reflect.Array;

import ncsa.hdf.hdf5lib.H5;
import ncsa.hdf.object.FileFormat;
import ncsa.hdf.object.h5.*;
import junit.framework.TestCase;

/**
 * TestCase for H5CompoundDS.
 * <p>
 * This class tests all public methods in H5CompoundDS.
 * 
 * @author xcao
 *
 */
public class H5CompoundDSTest extends TestCase {
    private static final H5File H5FILE = new H5File();
    private static final int NLOOPS = 5;
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

        testFile = (H5File)H5FILE.open(H5TestFile.NAME_FILE_H5, FileFormat.WRITE);
        assertNotNull(testFile);

        dset = (H5CompoundDS)testFile.get(H5TestFile.NAME_DATASET_COMPOUND);
        assertNotNull(dset);
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
     * <p>
     * Cases tested:
     * <ul>
     *   <li> change the dataset name
     *   <li> close/re-open the file
     *   <li> get the dataset with the new name
     *   <li> failure test: get the dataset with the original name
     *   <li> set the name back to the original name
     * </ul>
     */
    public final void testSetName() {
         String newName = "tmpName";
        
        try { 
            dset.setName(newName); 
        } catch (Exception ex) { 
            fail("setName() failed. "+ ex);
        }
        
        // close the file and reopen it
        try {
            testFile.close();
            testFile = (H5File)H5FILE.open(H5TestFile.NAME_FILE_H5, FileFormat.WRITE);
            dset = (H5CompoundDS)testFile.get(newName);
        } catch (Exception ex) { 
            fail("setName() failed. "+ ex);
        }
        
        // test the old name
        H5CompoundDS tmpDset = null;
        try {
            tmpDset = (H5CompoundDS)testFile.get(H5TestFile.NAME_DATASET_COMPOUND);
         } catch (Exception ex) { 
             tmpDset = null; // Expected - intentional
        }
        assertNull("The dataset should be null because it has been renamed", tmpDset);

        // set back the original name
        try { 
            dset.setName(H5TestFile.NAME_DATASET_COMPOUND); 
        } catch (Exception ex) { 
            fail("setName() failed. "+ ex);
        }
        
        // make sure the dataset is OK
        try {
            dset = (H5CompoundDS)testFile.get(H5TestFile.NAME_DATASET_COMPOUND);
        } catch (Exception ex) { 
            fail("setName() failed. "+ ex);
        }
        assertNotNull(dset);
    }

    /**
     * Test method for {@link ncsa.hdf.object.h5.H5CompoundDS#open()}.
     * <p>
     * Cases tested:
     * <ul>
     *   <li> open a dataset identifier
     *   <li> get datatype and dataspace identifier for the dataset
     *   <li> repeat above
     * </ul>
     */
    public final void testOpen() {
        int did=-1, tid=-1, sid=-1;

         for (int loop=0; loop<NLOOPS; loop++) {
             did= tid= sid=-1;
             try {
                 did = dset.open();
                 tid = H5.H5Dget_type(did);
                 sid = H5.H5Dget_space(did);
             } catch (Exception ex) { 
                 fail("open() failed. "+ ex);
             }
             
             assertTrue(did > 0);
             assertTrue(tid > 0);
             assertTrue(sid > 0);
             
             try {
                 H5.H5Tclose(tid);
             } catch (Exception ex) {}
             try {
                 H5.H5Sclose(sid);
             } catch (Exception ex) {}
             try {
                 H5.H5Dclose(did);
             } catch (Exception ex) {}
          }
     }

    /**
     * Test method for {@link ncsa.hdf.object.h5.H5CompoundDS#close(int)}.
     * <p>
     * Cases tested:
     * <ul>
     *   <li> open a dataset identifier
     *   <li> get datatype and dataspace identifier for the dataset
     *   <li> close dataset
     *   <li> failure test for the closed did 
     *   <li> repeat above
     * </ul>
     */
    public final void testClose() {
        int did=-1, tid=-1, sid=-1;

          for (int loop=0; loop<NLOOPS; loop++) {
              did= tid= sid=-1;
              try {
                  did = dset.open();
                  tid = H5.H5Dget_type(did);
                  sid = H5.H5Dget_space(did);
              } catch (Exception ex) { 
                  fail("open() failed. "+ ex);
              }
              
              assertTrue(did > 0);
              assertTrue(tid > 0);
              assertTrue(sid > 0);
              
              try {
                  H5.H5Tclose(tid);
              } catch (Exception ex) {}
              try {
                  H5.H5Sclose(sid);
              } catch (Exception ex) {}
              
              try { 
                  dset.close(did);
              } catch (Exception ex) { 
                  fail("close() failed. "+ ex);
              }
              
              // dataset is closed, expect to fail
              try {
                  tid = H5.H5Dget_type(did);
              } catch (Exception ex) { 
                  tid = -1; // Expected - intentional
              }
              assertTrue(tid < 0);
              
              try {
                  sid = H5.H5Dget_space(did);
              } catch (Exception ex) { 
                  sid = -1; // Expected - intentional
              }
              assertTrue(sid < 0);
           }
    }

    /**
     * Test method for {@link ncsa.hdf.object.h5.H5CompoundDS#clear()}.
     * <p>
     * Cases tested:
     * <ul>
     *   <li> Read data/attributes from file
     *   <li> clear the dataet
     *   <li> make sure that the data is empty
     *   <li> make sure that the attribute list is empty 
     *   <li> repeat above
     * </ul>
     */
    public final void testClear() {
        Vector data = null;
        
        try {
            data = (Vector)dset.getData();
        } catch (Exception ex) { 
            fail("getData() failed. "+ ex);
        }
        assertNotNull(data);
        assertTrue(data.size() > 0);

        Vector attrs = null;
        try { 
            attrs = (Vector)dset.getMetadata();
        } catch (Exception ex) { 
            fail("clear() failed. "+ ex);
        }

        // clear up the dataset
        dset.clear();
        assertFalse(data.size() > 0);
        
        // attribute is empty
        try { 
            attrs = (Vector)dset.getMetadata();
        } catch (Exception ex) { 
            fail("clear() failed. "+ ex);
        }
        assertTrue(attrs.size() == 0);
    }

    /**
     * Test method for {@link ncsa.hdf.object.h5.H5CompoundDS#init()}.
     * <p>
     * Cases tested:
     * <ul>
     *   <li> call init()
     *   <li> make that the dataspace is correct
     *   <li> make sure that member selection is correct
     *   <li> repeat above
     * </ul>
     */
    public final void testInit() {
        for (int loop=0; loop<NLOOPS; loop++) {
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
     * <p>
     * Cases tested:
     * <ul>
     *   <li> Read the whole dataset
     *   <li> Read data row by row
     *   <li> Read data field bu field
     *   <li> repeat above
     * </ul>
     */
    public final void testRead() {
        Vector data = null;

        for (int loop=0; loop<NLOOPS; loop++) {
            // read the whole dataset by default
            dset.init();

            try {
                data = (Vector)dset.getData();
            } catch (Exception ex) { 
                fail("getData() failed. "+ ex);
            }
            assertNotNull(data);
            assertTrue(data.size() > 0);
            
            // check the data values
            int[] ints = (int[])data.get(0);
            float[] floats = (float[])data.get(1);
            String[] strs = (String[])data.get(2);
            assertNotNull(ints);
            assertNotNull(floats);
            assertNotNull(strs);
            for (int i=0; i<H5TestFile.DIM_SIZE; i++) {
                assertEquals(H5TestFile.DATA_INT[i], ints[i]);
                assertEquals(H5TestFile.DATA_FLOAT[i], floats[i], Float.MIN_VALUE);
                assertTrue(H5TestFile.DATA_STR[i].equals(strs[i]));
            }
            
            int rank = dset.getRank();
            long[] dims = dset.getDims();
            long[] start = dset.getStartDims();
            long[] count = dset.getSelectedDims();
            int[] selectedIndex = dset.getSelectedIndex();
            
            // read data row by row
            for (int i=0; i<rank; i++) {
                start[i] = 0;
                count[i] = 1;
            }
            int nrows = dset.getHeight();
            for (int i=0; i<nrows; i++) {
                dset.clearData();
                dset.init();
                
                // select one row only
                for (int j=0; j<rank; j++) {
                    count[j] = 1;
                }
                
                // select different rows
                start[0] = i;
                
                try {
                    data = (Vector)dset.getData();
                } catch (Exception ex) { 
                    fail("getData() failed. "+ ex);
                }
                
                ints = (int[])data.get(0);
                floats = (float[])data.get(1);
                strs = (String[])data.get(2);
                assertNotNull(ints);
                assertNotNull(floats);
                assertNotNull(strs);
                assertEquals(H5TestFile.DATA_INT[i], ints[0]);
                assertEquals(H5TestFile.DATA_FLOAT[i], floats[0], Float.MIN_VALUE);
                assertTrue(H5TestFile.DATA_STR[i].equals(strs[0]));
            } // for (int i=0; i<nrows; i++) {
            
            // read field by field
            int nmembers = dset.getMemberCount();
            for (int i=0; i<nmembers; i++) {
                dset.clearData();
                dset.init();
                
                dset.setMemberSelection(false);
                dset.selectMember(i);
                
                try {
                    data = (Vector)dset.getData();
                } catch (Exception ex) { 
                    fail("getData() failed. "+ ex);
                }
                assertNotNull(data);
                assertTrue(data.size()==1);
                
                switch (i) {
                    case 0:
                        ints = (int[])data.get(0);
                        assertNotNull(ints);
                        for (int j=0; j<H5TestFile.DIM_SIZE; j++)
                            assertEquals(H5TestFile.DATA_INT[j], ints[j]);
                        break;
                    case 1:
                        floats = (float[])data.get(0);
                        assertNotNull(floats);
                        for (int j=0; j<H5TestFile.DIM_SIZE; j++)
                            assertEquals(H5TestFile.DATA_FLOAT[j], floats[j], Float.MIN_VALUE);
                        break;
                    case 2:
                        strs = (String[])data.get(0);
                        assertNotNull(strs);
                        for (int j=0; j<H5TestFile.DIM_SIZE; j++)
                            assertTrue(H5TestFile.DATA_STR[j].equals(strs[j]));
                        break;
                }
            } // for (int i=0; i<nmembers; i++) {
        } //for (int loop=0; loop<NLOOPS; loop++) {
    }

    /**
     * Test method for {@link ncsa.hdf.object.h5.H5CompoundDS#readBytes()}.
     * <p>
     * Cases tested:
     * <ul>
     *   <li> Read the whole dataset in a byte buffer
     *   <li> check the data size
     * </ul>
     */
    public final void testReadBytes() {
        byte[] data = null;
        
        try {
            data = (byte[])dset.readBytes();
        } catch (Exception ex) { 
            fail("readBytes() failed. "+ ex);
        }
        assertNotNull(data);
        
        int n = Array.getLength(data);
        int expected = H5TestFile.DIM_SIZE * (4+4+H5TestFile.STR_LEN);
        
        assertEquals(expected, n);
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
