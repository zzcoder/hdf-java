package test.object;

import java.util.Vector;
import java.io.File;

import ncsa.hdf.hdf5lib.H5;
import ncsa.hdf.hdf5lib.HDF5Constants;
import ncsa.hdf.object.Attribute;
import ncsa.hdf.object.Dataset;
import ncsa.hdf.object.ScalarDS;
import ncsa.hdf.object.Datatype;
import ncsa.hdf.object.FileFormat;
import ncsa.hdf.object.Group;
import ncsa.hdf.object.h5.H5Datatype;
import ncsa.hdf.object.h5.H5File;

public class TestH5MemoryLeak 
{
    /** Name of test file */ 
    private final static String NAME_FILE_H5="TestH5MemoryLeak.h5";
    
    /** Name of test groups */
    private final static String NAME_GROUP = "/g0";
    private final static String NAME_GROUP_ATTR = "/g0_attr";
    private final static String NAME_GROUP_SUB = NAME_GROUP+"/g00";

    /** Name of test datasets */
    private final static String DNAMES[] = {
        "/dataset_byte", "/dataset_int",
        "/dataset_float", "/dataset_str",
        "/dataset_enum", "/dataset_image",
        "/dataset_comp", NAME_GROUP + "/dataset_int",
        NAME_GROUP_SUB+ "/dataset_float", NAME_GROUP + "/dataset_comp"};
    private final static String NAME_DATASET_CHAR           = DNAMES[0];
    private final static String NAME_DATASET_INT            = DNAMES[1];
    private final static String NAME_DATASET_FLOAT          = DNAMES[2];
    private final static String NAME_DATASET_STR            = DNAMES[3];
    private final static String NAME_DATASET_ENUM           = DNAMES[4];    
    private final static String NAME_DATASET_IMAGE          = DNAMES[5];
    private final static String NAME_DATASET_COMPOUND       = DNAMES[6];
    private final static String NAME_DATASET_SUB            = DNAMES[7];
    private final static String NAME_DATASET_SUB_SUB        = DNAMES[8];
    private final static String NAME_DATASET_COMPOUND_SUB   = DNAMES[9];
    
    /** Name of test dataype */
    private final static String NAME_DATATYPE_INT   = NAME_GROUP + "/datatype_int";
    private final static String NAME_DATATYPE_FLOAT = NAME_GROUP + "/datatype_float";
    private final static String NAME_DATATYPE_STR   = NAME_GROUP + "/datatype_str";

    // data space information
    private final static int DATATYPE_SIZE  = 4;
    private final static int RANK           = 2;
    private final static long DIM1          = 10;
    private final static long DIM2          = 5;
    private final static long[] DIMs        = {DIM1, DIM2};
    private final static long[] CHUNKs      = {DIM1/2, DIM2/2};
    private final static int STR_LEN        = 20;
    private final static int DIM_SIZE       = (int)(DIM1*DIM2);;
    
    /* testing data */
    private final static int[] DATA_INT     = new int[DIM_SIZE];
    private final static long[] DATA_LONG   = new long[DIM_SIZE];
    private final static float[] DATA_FLOAT = new float[DIM_SIZE];
    private final static byte[] DATA_BYTE   = new byte[DIM_SIZE];
    private final static String[] DATA_STR  = new String[DIM_SIZE];
    private final static int[] DATA_ENUM    = new int[DIM_SIZE];
    private final static Vector DATA_COMP   = new Vector(3);
    
    // compound names and datatypes
    private final static String[] COMPOUND_MEMBER_NAMES  = {"int32", "float32", "string", "uint32"};
    private final static H5Datatype[] COMPOUND_MEMBER_DATATYPES = {
        new H5Datatype(Datatype.CLASS_INTEGER, DATATYPE_SIZE, -1, -1), 
        new H5Datatype(Datatype.CLASS_FLOAT, DATATYPE_SIZE, -1, -1), 
        new H5Datatype(Datatype.CLASS_STRING, STR_LEN, -1, -1),
        new H5Datatype(Datatype.CLASS_INTEGER, DATATYPE_SIZE, -1, Datatype.SIGN_NONE)}; 
    
    // attributes
    private final static Attribute ATTRIBUTE_STR = new Attribute(
            "attrName", 
            new H5Datatype(Datatype.CLASS_STRING, STR_LEN, -1, -1), 
            new long[] {1}, 
            new String[] {"attrValue"});
    private final static Attribute ATTRIBUTE_INT_ARRAY = new Attribute( 
            "arrayInt", 
            new H5Datatype(Datatype.CLASS_INTEGER, DATATYPE_SIZE, -1, -1), 
            new long[] {10}, 
            new int[] {1, 2, 3, 4, 5, 6, 7, 8, 9, 10});
    
    /**
     * Test memory leak by create file, open dataset, read/write data in an infinite loop
     * <p>
     * @param args
     */
    public static void main(final String[] args) 
    {
        int nObjs = 0; // number of object left open
        Dataset dset =null;
        File tmpFile = null;
        
        for (int i=0; i<DIM_SIZE; i++) {
            DATA_INT[i] = i;
            DATA_LONG[i] = i;
            DATA_FLOAT[i] = i+i/100.0f;
            DATA_BYTE[i] = (byte)Math.IEEEremainder(i, 127);
            DATA_STR[i] = "str"+i;
            DATA_ENUM[i] = (int)Math.IEEEremainder(i, 2);
        }
        
        DATA_COMP.add(0, DATA_INT);
        DATA_COMP.add(1, DATA_FLOAT);
        DATA_COMP.add(2, DATA_STR);
        DATA_COMP.add(3, DATA_LONG);
 
        while(true)
        {
            tmpFile = null;
            try {
                try {
                    tmpFile = createTestFile();
                } catch (final Exception ex) {
                    System.err.print("createTestFile() failed.");
                    tmpFile = null;
                    break;
                }
                
                // test two open options: open full tree or open individual object only
                 for (int openOption=0; openOption<2; openOption++)
                {
                    nObjs = 0;
                    final H5File file = new H5File(NAME_FILE_H5, FileFormat.WRITE);
                    
                    if (openOption == 0) {
                        try { 
                            file.open(); // opent the full tree
                        } catch (final Exception ex) { 
                            System.err.println("file.open(). "+ ex);
                        }
                    }
                      
                    try {
                        final Group rootGrp = (Group) file.get("/");
                        
                        // datasets
                        for (int j=0; j<DNAMES.length; j++) {
                            dset = (Dataset)file.get(DNAMES[j]);
                            dset.init();
                            final Object data = dset.getData();
                            dset.write(data);
                            dset.getMetadata();
                            
                            // copy data into a new datast
                            if (dset instanceof ScalarDS) {
                                dset = dset.copy(rootGrp, DNAMES[j]+"_copy"+openOption, DIMs, data);
                            }
                        }
                        
                        // groups
                        file.get(NAME_GROUP);
                        file.get(NAME_GROUP_ATTR);
                        file.get(NAME_GROUP_SUB);

                        // datatypes
                        file.get(NAME_DATATYPE_INT);
                        file.get(NAME_DATATYPE_FLOAT);
                        file.get(NAME_DATATYPE_STR);
                    } catch (final Exception ex) { 
                         System.err.println("file.get(). "+ ex);
                    }
         
                    nObjs = 0;
                    try { nObjs = H5.H5Fget_obj_count(file.getFID(), HDF5Constants.H5F_OBJ_LOCAL); }
                    catch (final Exception ex) { ; }
                    if (nObjs > 1) {
                        System.err.println("Possible memory leak. Some objects are still open.");
                    }
                    
                    try {            
                        file.close();
                    } catch (final Exception ex) { 
                         System.err.println("file.close() failed. "+ ex);
                    }
                } // for (int openOption=0; openOption<2; openOption++)
            } finally {
                // delete the testing file
                if (tmpFile != null) {
                    tmpFile.delete();
                }
            }
        } // while (true)
    }
    
    /**
     * Calls garbage collector
     */
    private static void collectGarbage() {
        try {
            System.gc();
            Thread.sleep(100);
            System.runFinalization();
            Thread.sleep(100);
        }
        catch (final Exception ex){
            ex.printStackTrace();
        }
    }
    
    /**
     * Creates an HDF5 test file. 
     * <p>
     * The test file contains the following objects:
     * <pre>
          /dataset_byte            Dataset {50, 10}
          /dataset_comp            Dataset {50, 10}
          /dataset_enum            Dataset {50, 10}
          /dataset_float           Dataset {50, 10}
          /dataset_int             Dataset {50, 10}
          /dataset_image           Dataset {50, 10}
          /dataset_str             Dataset {50, 10}
          /g0                      Group
          /g0/dataset_int          Dataset {50, 10}
          /g0/g00                  Group
          /g0/g00/dataset_float    Dataset {50, 10}
          /g0_attr                 Group
     * </pre> 
     * @throws Exception
     */
    private static final File createTestFile()  throws Exception
    {
        H5File file=null;
        Group g0, g1, g00;
        final Dataset[] dsets = new Dataset[10];
        
        final H5Datatype typeInt = new H5Datatype(Datatype.CLASS_INTEGER, DATATYPE_SIZE, -1, -1);
        final H5Datatype typeFloat = new H5Datatype(Datatype.CLASS_FLOAT, DATATYPE_SIZE, -1, -1);
        final H5Datatype typeStr = new H5Datatype(Datatype.CLASS_STRING, STR_LEN, -1, -1);
        final H5Datatype typeChar = new H5Datatype(Datatype.CLASS_CHAR, 1, -1, -1);
        final H5Datatype typeEnum = new H5Datatype(Datatype.CLASS_ENUM, DATATYPE_SIZE, -1, -1);
       

        file = new H5File(NAME_FILE_H5, FileFormat.CREATE);
        file.open();
       
        g0 = file.createGroup(NAME_GROUP, null);
        g1 = file.createGroup(NAME_GROUP_ATTR, null);
        g00 = file.createGroup(NAME_GROUP_SUB, null);

        g1.writeMetadata(ATTRIBUTE_STR);
        g1.writeMetadata(ATTRIBUTE_INT_ARRAY);

        dsets[0] = file.createScalarDS  (NAME_DATASET_INT, null, typeInt, DIMs, null, CHUNKs, 9, DATA_INT);
        dsets[1] = file.createScalarDS  (NAME_DATASET_FLOAT, null, typeFloat, DIMs, null, CHUNKs, 9, DATA_FLOAT);
        dsets[2] = file.createScalarDS  (NAME_DATASET_CHAR, null, typeChar, DIMs, null, CHUNKs, 9, DATA_BYTE);
        dsets[3] = file.createScalarDS  (NAME_DATASET_STR, null, typeStr, DIMs, null, CHUNKs, 9, DATA_STR);
        dsets[4] = file.createScalarDS  (NAME_DATASET_ENUM, null, typeEnum, DIMs, null, CHUNKs, 9, DATA_ENUM);
        dsets[5] = file.createScalarDS  (NAME_DATASET_SUB, g0, typeInt, DIMs, null, CHUNKs, 9, DATA_INT);
        dsets[6] = file.createScalarDS  (NAME_DATASET_SUB_SUB, g00, typeFloat, DIMs, null, CHUNKs, 9, DATA_FLOAT);
        dsets[7] = file.createImage     (NAME_DATASET_IMAGE, null, typeInt, DIMs, null, CHUNKs, 9, 1, -1, DATA_BYTE);
        dsets[8] = file.createCompoundDS(NAME_DATASET_COMPOUND, null, DIMs, null, CHUNKs, 9, COMPOUND_MEMBER_NAMES, COMPOUND_MEMBER_DATATYPES, null, DATA_COMP);
        dsets[9] = file.createCompoundDS(NAME_DATASET_COMPOUND_SUB, null, DIMs, null, CHUNKs, 9, COMPOUND_MEMBER_NAMES, COMPOUND_MEMBER_DATATYPES, null, DATA_COMP);
        
        for (int i=0; i<dsets.length; i++) {
            dsets[i].writeMetadata(ATTRIBUTE_STR);
            dsets[i].writeMetadata(ATTRIBUTE_INT_ARRAY);
        }
        
        file.createDatatype(Datatype.CLASS_INTEGER, DATATYPE_SIZE, -1, -1, NAME_DATATYPE_INT);
        file.createDatatype(Datatype.CLASS_FLOAT, DATATYPE_SIZE, -1, -1, NAME_DATATYPE_FLOAT);
        file.createDatatype(Datatype.CLASS_STRING, STR_LEN, -1, -1, NAME_DATATYPE_STR);

        try { file.close(); } catch (final Exception ex) {}
        
        return file;
    }
}
   