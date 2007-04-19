/**
 * 
 */
package test.unittests;

import java.util.Vector;
import java.util.Enumeration;
import java.lang.reflect.Array;

import ncsa.hdf.hdf5lib.H5;
import ncsa.hdf.hdf5lib.HDF5Constants;
import ncsa.hdf.hdf5lib.HDFNativeData;
import ncsa.hdf.hdf5lib.exceptions.HDF5Exception;
import ncsa.hdf.object.Datatype;
import ncsa.hdf.object.FileFormat;
import ncsa.hdf.object.Attribute;
import ncsa.hdf.object.h5.*;
import junit.framework.TestCase;

/**
 * TestCase for H5CompoundDS.
 * <p>
 * This class tests all public methods in H5CompoundDS.
 * <p>
 * The compound dataset contains three fields: {int, float, string}.
 * 
 * @author xcao
 *
 */
public class H5CompoundDSTest extends TestCase {
    private static final H5File H5FILE = new H5File();
    private static final int NLOOPS = 5;
    private static final int TEST_VALUE_INT = Integer.MAX_VALUE;
    private static final float TEST_VALUE_FLOAT = Float.MAX_VALUE;
    private static final String TEST_VALUE_STR = "H5CompoundDSTest";
    
    private H5File testFile = null;
    private H5CompoundDS testDataset = null;
    
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

        testDataset = (H5CompoundDS)testFile.get(H5TestFile.NAME_DATASET_COMPOUND);
        assertNotNull(testDataset);
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
            testDataset.setName(newName); 
        } catch (Exception ex) { 
            fail("setName() failed. "+ ex);
        }
        
        // close the file and reopen it
        try {
            testFile.close();
            testFile = (H5File)H5FILE.open(H5TestFile.NAME_FILE_H5, FileFormat.WRITE);
            testDataset = (H5CompoundDS)testFile.get(newName);
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
            testDataset.setName(H5TestFile.NAME_DATASET_COMPOUND); 
        } catch (Exception ex) { 
            fail("setName() failed. "+ ex);
        }
        
        // make sure the dataset is OK
        try {
            testDataset = (H5CompoundDS)testFile.get(H5TestFile.NAME_DATASET_COMPOUND);
        } catch (Exception ex) { 
            fail("setName() failed. "+ ex);
        }
        assertNotNull(testDataset);
    }

    /**
     * Test method for {@link ncsa.hdf.object.h5.H5CompoundDS#open()}.
     * <p>
     * Cases tested:
     * <ul>
     *   <li> open a dataset identifier
     *   <li> get datatype and dataspace identifier for the dataset
     *   <li> Repeat all above
     * </ul>
     */
    public final void testOpen() {
        int did=-1, tid=-1, sid=-1;

         for (int loop=0; loop<NLOOPS; loop++) {
             did= tid= sid=-1;
             try {
                 did = testDataset.open();
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
     *   <li> Repeat all above
     * </ul>
     */
    public final void testClose() {
        int did=-1, tid=-1, sid=-1;

          for (int loop=0; loop<NLOOPS; loop++) {
              did= tid= sid=-1;
              try {
                  did = testDataset.open();
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
                  testDataset.close(did);
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
     *   <li> Repeat all above
     * </ul>
     */
    public final void testClear() {
        Vector data = null;
        
        try {
            data = (Vector)testDataset.getData();
        } catch (Exception ex) { 
            fail("getData() failed. "+ ex);
        }
        assertNotNull(data);
        assertTrue(data.size() > 0);

        Vector attrs = null;
        try { 
            attrs = (Vector)testDataset.getMetadata();
        } catch (Exception ex) { 
            fail("clear() failed. "+ ex);
        }

        // clear up the dataset
        testDataset.clear();
        assertFalse(data.size() > 0);
        
        // attribute is empty
        try { 
            attrs = (Vector)testDataset.getMetadata();
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
     *   <li> Repeat all above
     * </ul>
     */
    public final void testInit() {
        for (int loop=0; loop<NLOOPS; loop++) {
            testDataset.init();

            // test the rank
            int rank = testDataset.getRank();
            assertEquals(H5TestFile.RANK, rank);
            
            // test the dimesin sizes
            long[] dims = testDataset.getDims();
            assertNotNull(dims);
            for (int i=0; i<rank; i++) {
                assertEquals(H5TestFile.DIMs[i], dims[i]);
            }
            
            // start at 0
            long[] start = testDataset.getStartDims();
            assertNotNull(start);
            for (int i=0; i<rank; i++) {
                assertEquals(0, start[i]);
            }
           
            // test selection
            long[] selectedDims = testDataset.getSelectedDims();
            int[] selectedIndex = testDataset.getSelectedIndex();
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
            int nmembers = testDataset.getSelectedMemberCount();
            assertTrue(nmembers > 0);
            for (int i=0; i<nmembers; i++) {
                assertTrue(testDataset.isMemberSelected(i));
            }
            
            // make some change and do another round of test
            // to make sure that the init() resets the default
            for (int i=0; i< rank; i++) {
                start[i] = 1;
                selectedDims[0] = 1;
            }
            for (int i=0; i<nmembers; i++) {
                testDataset.setMemberSelection(false);
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
     *   <li> Repeat all above
     * </ul>
     */
    public final void testRead() {
        Vector data = null;

        for (int loop=0; loop<NLOOPS; loop++) {
            // read the whole dataset by default
            testDataset.init();

            try {
                data = (Vector)testDataset.getData();
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
            
            int rank = testDataset.getRank();
            long[] dims = testDataset.getDims();
            long[] start = testDataset.getStartDims();
            long[] count = testDataset.getSelectedDims();
            int[] selectedIndex = testDataset.getSelectedIndex();
            
            // read data row by row
            for (int i=0; i<rank; i++) {
                start[i] = 0;
                count[i] = 1;
            }
            int nrows = testDataset.getHeight();
            for (int i=0; i<nrows; i++) {
                testDataset.clearData();
                testDataset.init();
                
                // select one row only
                for (int j=0; j<rank; j++) {
                    count[j] = 1;
                }
                
                // select different rows
                start[0] = i;
                
                try {
                    data = (Vector)testDataset.getData();
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
            int nmembers = testDataset.getMemberCount();
            for (int i=0; i<nmembers; i++) {
                testDataset.clearData();
                testDataset.init();
                
                testDataset.setMemberSelection(false);
                testDataset.selectMember(i);
                
                try {
                    data = (Vector)testDataset.getData();
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
            data = (byte[])testDataset.readBytes();
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
     * <p>
     * Cases tested:
     * <ul>
     *   <li> Read/write the whole dataset
     *   <li> Read/write data row by row
     *   <li> Read/write data field bu field
     *   <li> Repeat all above
     *   <li> write the original data back to file
     * </ul>
     */
    public final void testWriteObject() {
        Vector data = null;

        for (int loop=0; loop<NLOOPS; loop++) {
            // read the whole dataset by default
            testDataset.init();

            try {
                data = (Vector)testDataset.getData();
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

            // change the data value
            for (int i=0; i<H5TestFile.DIM_SIZE; i++) {
                ints[i] = TEST_VALUE_INT;
                floats[i] = TEST_VALUE_FLOAT;
                strs[i] = TEST_VALUE_STR;
            }
            
            // write the data to file
            try { 
                testDataset.write(data);
            }  catch (Exception ex) { 
                fail("write() failed. "+ ex);
            }
            
            // close the file and reopen it
            try {
                testFile.close();
                testFile = (H5File)H5FILE.open(H5TestFile.NAME_FILE_H5, FileFormat.WRITE);
                testDataset = (H5CompoundDS)testFile.get(H5TestFile.NAME_DATASET_COMPOUND);
            } catch (Exception ex) { 
                fail("write() failed. "+ ex);
            }
            
            // read the data into memory to make sure the data is correct
            testDataset.init();
            testDataset.clearData();
            
            try {
                data = (Vector)testDataset.getData();
            } catch (Exception ex) { 
                fail("getData() failed. "+ ex);
            }
            assertNotNull(data);
            assertTrue(data.size() > 0);
            
            // check the data values
            ints = (int[])data.get(0);
            floats = (float[])data.get(1);
            strs = (String[])data.get(2);
            for (int i=0; i<H5TestFile.DIM_SIZE; i++) {
                assertEquals(TEST_VALUE_INT, ints[i]);
                assertEquals(TEST_VALUE_FLOAT, floats[i], Float.MIN_VALUE);
                assertTrue(TEST_VALUE_STR.equals(strs[i]));
            }
            
            // write the original data into file
            try { 
                testDataset.write(H5TestFile.DATA_COMP);
            }  catch (Exception ex) { 
                fail("write() failed. "+ ex);
            }
             
            int rank = testDataset.getRank();
            long[] dims = testDataset.getDims();
            long[] start = testDataset.getStartDims();
            long[] count = testDataset.getSelectedDims();
            int[] selectedIndex = testDataset.getSelectedIndex();
            
            // read data row by row
            for (int i=0; i<rank; i++) {
                start[i] = 0;
                count[i] = 1;
            }
            int nrows = testDataset.getHeight();
            for (int i=0; i<nrows; i++) {
                testDataset.clearData();
                testDataset.init();
                
                // select one row only
                for (int j=0; j<rank; j++) {
                    count[j] = 1;
                }
                
                // select different rows
                start[0] = i;
                
                try {
                    data = (Vector)testDataset.getData();
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
                
                // change the data value
                ints[0] = TEST_VALUE_INT;
                floats[0] = TEST_VALUE_FLOAT;
                strs[0] = TEST_VALUE_STR;
                
                // write data row by row
                try { 
                    testDataset.write(data);
                }  catch (Exception ex) { 
                    fail("write() failed. "+ ex);
                }
                
                // check if data is correct
                testDataset.clearData();
                try {
                    data = (Vector)testDataset.getData();
                } catch (Exception ex) { 
                    fail("getData() failed. "+ ex);
                }
                assertEquals(TEST_VALUE_INT, ints[0]);
                assertEquals(TEST_VALUE_FLOAT, floats[0], Float.MIN_VALUE);
                assertTrue(TEST_VALUE_STR.equals(strs[0]));
            } // for (int i=0; i<nrows; i++) {
            
            // write the original data into file
            testDataset.init();
            testDataset.clearData();
            try { 
                testDataset.write(H5TestFile.DATA_COMP);
            }  catch (Exception ex) { 
                fail("write() failed. "+ ex);
            }
            
            // read field by field
            int nmembers = testDataset.getMemberCount();
            for (int i=0; i<nmembers; i++) {
                testDataset.clearData();
                testDataset.init();
                
                testDataset.setMemberSelection(false);
                testDataset.selectMember(i);
                
                try {
                    data = (Vector)testDataset.getData();
                } catch (Exception ex) { 
                    fail("getData() failed. "+ ex);
                }
                assertNotNull(data);
                assertTrue(data.size()==1);
                
                // change the data value
                switch (i) {
                    case 0:
                        ints = (int[])data.get(0);
                        assertNotNull(ints);
                        for (int j=0; j<H5TestFile.DIM_SIZE; j++)
                            ints[j] = TEST_VALUE_INT;
                        break;
                    case 1:
                        floats = (float[])data.get(0);
                        assertNotNull(floats);
                        for (int j=0; j<H5TestFile.DIM_SIZE; j++)
                            floats[j] = TEST_VALUE_FLOAT;
                        break;
                    case 2:
                        strs = (String[])data.get(0);
                        assertNotNull(strs);
                        for (int j=0; j<H5TestFile.DIM_SIZE; j++)
                            strs[j] = TEST_VALUE_STR;
                        break;
                }
                
                // write data field y field
                try { 
                    testDataset.write(data);
                }  catch (Exception ex) { 
                    fail("write() failed. "+ ex);
                }
                
                // check if data is correct
                testDataset.clearData();
                try {
                    data = (Vector)testDataset.getData();
                } catch (Exception ex) { 
                    fail("getData() failed. "+ ex);
                }
                switch (i) {
                    case 0:
                        ints = (int[])data.get(0);
                        assertNotNull(ints);
                        for (int j=0; j<H5TestFile.DIM_SIZE; j++)
                            assertEquals(TEST_VALUE_INT, ints[j]);
                        break;
                    case 1:
                        floats = (float[])data.get(0);
                        assertNotNull(floats);
                        for (int j=0; j<H5TestFile.DIM_SIZE; j++)
                            assertEquals(TEST_VALUE_FLOAT, floats[j], Float.MIN_VALUE);
                        break;
                    case 2:
                        strs = (String[])data.get(0);
                        assertNotNull(strs);
                        for (int j=0; j<H5TestFile.DIM_SIZE; j++)
                            assertTrue(TEST_VALUE_STR.equals(strs[j]));
                        break;
                }
                
                // write the original data into file
                testDataset.init();
                testDataset.clearData();
                try { 
                    testDataset.write(H5TestFile.DATA_COMP);
                }  catch (Exception ex) { 
                    fail("write() failed. "+ ex);
                }
            } // for (int i=0; i<nmembers; i++) {
        } //for (int loop=0; loop<NLOOPS; loop++) {
        
        // write the original data into file
        testDataset.init();
        testDataset.clearData();
        try { 
            testDataset.write(H5TestFile.DATA_COMP);
        }  catch (Exception ex) { 
            fail("write() failed. "+ ex);
        }
        
    }

    /**
     * Test method for {@link ncsa.hdf.object.h5.H5CompoundDS#getDatatype()}.
     * <p>
     * Cases tested:
     * <ul>
     *   <li> Get the datatype object of the dataset
     *   <li> Make sure that the data types of compound members are correct
     *   <li> Make sure that the names of compound members are correct
     * </ul>
     */
    public final void testGetDatatype() {
        testDataset.init();
        
        H5Datatype dtype = (H5Datatype)testDataset.getDatatype();
        assertNotNull(dtype);
        assertEquals(H5Datatype.CLASS_COMPOUND, dtype.getDatatypeClass());
        
        int[] tids = testDataset.getMemberTypes();
        assertNotNull(tids);
        assertTrue(tids.length>0);
        
        int tid = -1;
        H5Datatype[] expectedTypes = H5TestFile.COMPOUND_MEMBER_DATATYPES;
        for (int i=0; i<expectedTypes.length; i++ ) {
            tid = -1;
             try {
                tid = expectedTypes[i].toNative();
                assertTrue(H5.H5Tequal(tid, tids[i]));
            }   catch (Exception ex) { 
                fail("dtypes[i].toNative() failed. "+ ex);
            }

            try { H5.H5Tclose(tid);} catch(Exception ex) {}
        }
        
        String[] names = testDataset.getMemberNames();
        assertNotNull(names);
        
        String[] expectedNames = H5TestFile.COMPOUND_MEMBER_NAMES;
        for (int i=0; i<expectedNames.length; i++ ) {
            assertTrue(expectedNames[i].equals(names[i]));
        }
     }

    /**
     * Test method for {@link ncsa.hdf.object.h5.H5CompoundDS#isString(int)}.
     * <p>
     * Cases tested:
     * <ul>
     *   <li> Test a string datatype with isString(int tid)
     *   <li> Test a non-string datatype with isString(int tid)
     * </ul>
     */
    public final void testIsString() {
        testDataset.init();
        
        int[] tids = testDataset.getMemberTypes();
        assertNotNull(tids);
        assertTrue(tids.length>0);
        
        assertFalse(testDataset.isString(tids[0]));
        assertFalse(testDataset.isString(tids[1]));
        assertTrue(testDataset.isString(tids[2]));
    }

    /**
     * Test method for {@link ncsa.hdf.object.h5.H5CompoundDS#getSize(int)}.
     * <p>
     * Cases tested:
     * <ul>
     *   <li> Test a sizes of member data types
     * </ul>
     */
    public final void testGetSize() {
        testDataset.init();
        
        int[] tids = testDataset.getMemberTypes();
        assertNotNull(tids);
        assertTrue(tids.length>0);
        
        assertEquals(H5TestFile.DATATYPE_SIZE, testDataset.getSize(tids[0]));
        assertEquals(H5TestFile.DATATYPE_SIZE, testDataset.getSize(tids[1]));
        assertEquals(H5TestFile.STR_LEN, testDataset.getSize(tids[2]));
    }

    /**
     * Test method for {@link ncsa.hdf.object.h5.H5CompoundDS#H5CompoundDS(ncsa.hdf.object.FileFormat, java.lang.String, java.lang.String)}.
     * <p>
     * Cases tested:
     * <ul>
     *   <li> Construct an H5CompoundDS object that exits in file
     *   <ul>
     *     <li> new H5CompoundDS (file, null, fullpath)
     *     <li> new H5CompoundDS (file, fullname, null)
     *     <li> new H5CompoundDS (file, name, path)
     *   </ul>
     *   <li> Construct an H5CompoundDS object that does not exist in file
     * </ul>
     */
    public final void testH5CompoundDSFileFormatStringString() {
        Vector data = null;
        String[] names = {null, H5TestFile.NAME_DATASET_COMPOUND_SUB, H5TestFile.NAME_DATASET_COMPOUND.substring(1)};
        String[] paths = {H5TestFile.NAME_DATASET_COMPOUND_SUB, null, H5TestFile.NAME_GROUP};

        H5File file = (H5File)testDataset.getFileFormat();
        assertNotNull(file);
        
        // test existing dataset in file
        for (int idx=0; idx<3; idx++) {
            H5CompoundDS dset = new H5CompoundDS(file, names[idx], paths[idx]);
            assertNotNull(dset);
            
            // make sure that the data content is correct
            try {
                data = (Vector)dset.getData();
            } catch (Exception ex) { 
                fail("getData() failed. "+ ex);
            }
            assertNotNull(data);
            assertTrue(data.size() > 0);
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

            // check the name and path
            assertTrue(H5TestFile.NAME_DATASET_COMPOUND_SUB.equals(dset.getFullName()));
            assertTrue(H5TestFile.NAME_DATASET_COMPOUND_SUB.equals(dset.getPath()+dset.getName()));
            
            dset.clear();
            dset = null;
        }
        
        // test a non-existing dataset
        H5CompoundDS dset = new H5CompoundDS(file, "NO_SUCH_DATASET", "NO_SUCH_PATH");
        dset.init();
        dset.clearData();
        data = null;
        try {
            data = (Vector)dset.getData();
        } catch (Exception ex) { 
            data = null; // Expected - intentional
        }
        assertNull(data);
    }

    /**
     * Test method for {@link ncsa.hdf.object.h5.H5CompoundDS#H5CompoundDS(ncsa.hdf.object.FileFormat, java.lang.String, java.lang.String, long[])}.
     * <p>
     * Cases tested:
     * <ul>
     *   <li> Construct an H5CompoundDS object that exits in file
     *   <ul>
     *     <li> new H5CompoundDS (file, null, fullpath, oid)
     *     <li> new H5CompoundDS (file, fullname, null, oid)
     *     <li> new H5CompoundDS (file, name, path, oid)
     *   </ul>
     *   <li> Construct an H5CompoundDS object that does not exist in file
     * </ul>
     */
    public final void testH5CompoundDSFileFormatStringStringLongArray() {
        Vector data = null;
        String[] names = {null, H5TestFile.NAME_DATASET_COMPOUND_SUB, H5TestFile.NAME_DATASET_COMPOUND.substring(1)};
        String[] paths = {H5TestFile.NAME_DATASET_COMPOUND_SUB, null, H5TestFile.NAME_GROUP};

        H5File file = (H5File)testDataset.getFileFormat();
        assertNotNull(file);
        
        // test existing dataset in file
        long[] oid = null;
        for (int idx=0; idx<3; idx++) {
            
            try
            {
                byte[] ref_buf = H5.H5Rcreate(file.getFID(), H5TestFile.NAME_DATASET_COMPOUND_SUB, HDF5Constants.H5R_OBJECT, -1);
                long l = HDFNativeData.byteToLong(ref_buf, 0);
                oid = new long[1];
                oid[0] = l; // save the object ID
            } catch (HDF5Exception ex) { 
                fail("H5.H5Rcreate() failed. "+ ex);
            }
            assertNotNull(oid);
            
            H5CompoundDS dset = new H5CompoundDS(file, names[idx], paths[idx], oid);
            assertNotNull(dset);
            
            // make sure that the data content is correct
            try {
                data = (Vector)dset.getData();
            } catch (Exception ex) { 
                fail("getData() failed. "+ ex);
            }
            assertNotNull(data);
            assertTrue(data.size() > 0);
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

            // check the name and path
            assertTrue(H5TestFile.NAME_DATASET_COMPOUND_SUB.equals(dset.getFullName()));
            assertTrue(H5TestFile.NAME_DATASET_COMPOUND_SUB.equals(dset.getPath()+dset.getName()));
            
            dset.clear();
            dset = null;
        }
        
        // test a non-existing dataset
        H5CompoundDS dset = new H5CompoundDS(file, "NO_SUCH_DATASET", "NO_SUCH_PATH", null);
        dset.init();
        dset.clearData();
        data = null;
        try {
            data = (Vector)dset.getData();
        } catch (Exception ex) { 
            data = null; // Expected - intentional
        }
        assertNull(data);
    }

    /**
     * Test method for {@link ncsa.hdf.object.h5.H5CompoundDS#getMetadata()}.
     * <p>
     * Cases tested:
     * <ul>
     *   <li> Get the attributes
     *   <li> Check the content of the attributes
     * </ul>
      */
    public final void testGetMetadata() {
        Vector attrs = null;
        
        try {
            attrs = (Vector) testDataset.getMetadata();
        } catch (Exception ex) { 
            fail("getMetadata() failed. "+ ex);
        }
        assertNotNull(attrs);
        assertTrue(attrs.size() > 0);
        
        int n = attrs.size();
        for (int i=0; i<n; i++) {
            Attribute attr = (Attribute) attrs.get(i);
            H5Datatype dtype = (H5Datatype)attr.getType();
            if (dtype.getDatatypeClass() == H5Datatype.CLASS_STRING) {
                assertTrue(H5TestFile.ATTRIBUTE_STR.getName().equals(attr.getName()));
                assertTrue(((String[]) H5TestFile.ATTRIBUTE_STR.getValue())[0].equals(((String[])attr.getValue())[0]));
            } else if (dtype.getDatatypeClass() == H5Datatype.CLASS_INTEGER) {
                assertTrue(H5TestFile.ATTRIBUTE_INT_ARRAY.getName().equals(attr.getName()));
                int[] expected = (int [])H5TestFile.ATTRIBUTE_INT_ARRAY.getValue();
                assertNotNull(expected);
                int[] ints = (int[]) attr.getValue();
                assertNotNull(ints);
                for (int j =0; j<expected.length; j++) 
                    assertEquals(expected[j], ints[j]);
            }
        } //for (int i=0; i<n; i++) {
    }

    /**
     * Test method for {@link ncsa.hdf.object.h5.H5CompoundDS#writeMetadata(java.lang.Object)}.
     * <p>
     * Cases tested:
     * <ul>
     *   <li> Update the value of an existing attribute
     *   <li> Attach a new attribute
     *   <li> Close and reopen file to check if the change is made in file
     *   <li> Restore to the orginal state
     * </ul>
     */
    public final void testWriteMetadata() {
        Vector attrs = null;
        Attribute attr = null;
        
        try {
            attrs = (Vector) testDataset.getMetadata();
        } catch (Exception ex) { 
            fail("getMetadata() failed. "+ ex);
        }
        assertNotNull(attrs);
        assertTrue(attrs.size() > 0);
        
        // update existing attribute
        int n = attrs.size();
        for (int i=0; i<n; i++) {
            attr = (Attribute) attrs.get(i);
            H5Datatype dtype = (H5Datatype)attr.getType();
            if (dtype.getDatatypeClass() == H5Datatype.CLASS_STRING) {
                String[] strs = (String[]) attr.getValue();
                strs[0] = TEST_VALUE_STR;
            } else if (dtype.getDatatypeClass() == H5Datatype.CLASS_INTEGER) {
                int[] ints = (int[]) attr.getValue();
                assertNotNull(ints);
                for (int j =0; j<ints.length; j++) 
                    ints[j] = TEST_VALUE_INT;
            }
            try  {
                testDataset.writeMetadata(attr);
            } catch (Exception ex) { 
                fail("writeMetadata() failed. "+ ex);
            }
        } //for (int i=0; i<n; i++) {
        
        // attache a new attribute
        attr = new Attribute(
                "float attribute", 
                new H5Datatype(Datatype.CLASS_FLOAT, 4, -1, -1), new long[] {1},
                new float[] {TEST_VALUE_FLOAT});
        try  {
            testDataset.writeMetadata(attr);
        } catch (Exception ex) { 
            fail("writeMetadata() failed. "+ ex);
        }

        // close the file and reopen it
        try {
            testDataset.clear();
            testFile.close();
            testFile = (H5File)H5FILE.open(H5TestFile.NAME_FILE_H5, FileFormat.WRITE);
            testDataset = (H5CompoundDS)testFile.get(H5TestFile.NAME_DATASET_COMPOUND);
        } catch (Exception ex) { 
            fail("write() failed. "+ ex);
        }
        
        // check the change in file
        try {
            attrs = (Vector) testDataset.getMetadata();
        } catch (Exception ex) { 
            fail("getMetadata() failed. "+ ex);
        }
        assertNotNull(attrs);
        assertTrue(attrs.size() > 0);
        
        n = attrs.size();
        Attribute newAttr = null;
        for (int i=0; i<n; i++) {
            attr = (Attribute) attrs.get(i);
            H5Datatype dtype = (H5Datatype)attr.getType();
            if (dtype.getDatatypeClass() == H5Datatype.CLASS_STRING) {
                assertTrue(H5TestFile.ATTRIBUTE_STR.getName().equals(attr.getName()));
                assertTrue(TEST_VALUE_STR.equals(((String[])attr.getValue())[0]));
            } else if (dtype.getDatatypeClass() == H5Datatype.CLASS_INTEGER) {
                assertTrue(H5TestFile.ATTRIBUTE_INT_ARRAY.getName().equals(attr.getName()));
                int[] ints = (int[]) attr.getValue();
                assertNotNull(ints);
                for (int j =0; j<ints.length; j++) 
                    assertEquals(TEST_VALUE_INT, ints[j]);
            } else if (dtype.getDatatypeClass() == H5Datatype.CLASS_FLOAT) {
                newAttr = attr;
                float[] floats = (float[]) attr.getValue();
                assertEquals(TEST_VALUE_FLOAT, floats[0], Float.MIN_VALUE);
            }
        } //for (int i=0; i<n; i++) {

        // remove the new attribute
        try {
            testDataset.removeMetadata(newAttr);
        } catch (Exception ex) { 
            fail("removeMetadata() failed. "+ ex);
        }
        
        // set the value to original
        n = attrs.size();
        for (int i=0; i<n; i++) {
            attr = (Attribute) attrs.get(i);
            H5Datatype dtype = (H5Datatype)attr.getType();
            if (dtype.getDatatypeClass() == H5Datatype.CLASS_STRING) {
                String[] strs = (String[]) attr.getValue();
                strs[0] = ((String[]) H5TestFile.ATTRIBUTE_STR.getValue())[0];
            } else if (dtype.getDatatypeClass() == H5Datatype.CLASS_INTEGER) {
                int[] ints = (int[]) attr.getValue();
                assertNotNull(ints);
                for (int j =0; j<ints.length; j++) {
                    int[] expected = (int [])H5TestFile.ATTRIBUTE_INT_ARRAY.getValue();
                    ints[j] = expected[j];
                }
            }
            try  {
                testDataset.writeMetadata(attr);
            } catch (Exception ex) { 
                fail("writeMetadata() failed. "+ ex);
            }
        } //for (int i=0; i<n; i++) {
    }

    /**
     * Test method for {@link ncsa.hdf.object.h5.H5CompoundDS#removeMetadata(java.lang.Object)}.
     * <p>
     * Cases tested:
     * <ul>
     *   <li> Remove all existing attributes
     *   <li> Close and reopen file to check if all attribute are removed from file
     *   <li> Restore to the orginal state
     * </ul>
     */
    public final void testRemoveMetadata() {
        Vector attrs = null;
        Attribute attr = null;
        
        try {
            attrs = (Vector) testDataset.getMetadata();
        } catch (Exception ex) { 
            fail("getMetadata() failed. "+ ex);
        }
        assertNotNull(attrs);
        assertTrue(attrs.size() > 0);
        
        // remove all attributes
        int n = attrs.size();
        Object[] arrayAttr = attrs.toArray();
        for (int i=0; i<n; i++) {
            try {
                testDataset.removeMetadata(arrayAttr[i]);
            } catch (Exception ex) { 
                fail("removeMetadata() failed. "+ ex);
            }
         }
        
        // close the file and reopen it
        try {
            testDataset.clear();
            testFile.close();
            testFile = (H5File)H5FILE.open(H5TestFile.NAME_FILE_H5, FileFormat.WRITE);
            testDataset = (H5CompoundDS)testFile.get(H5TestFile.NAME_DATASET_COMPOUND);
        } catch (Exception ex) { 
            fail("write() failed. "+ ex);
        }
        attrs = null;
        
        try {
            attrs = (Vector) testDataset.getMetadata();
        } catch (Exception ex) { 
            fail("getMetadata() failed. "+ ex);
        }
        assertNotNull(attrs);
        assertFalse(attrs.size() > 0);

        // restor to the original
        try  {
            testDataset.writeMetadata(H5TestFile.ATTRIBUTE_STR);
            testDataset.writeMetadata(H5TestFile.ATTRIBUTE_INT_ARRAY);
        } catch (Exception ex) { 
            fail("writeMetadata() failed. "+ ex);
        }
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
