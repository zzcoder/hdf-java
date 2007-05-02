/**
 * 
 */
package test.unittests;

import java.util.Vector;
import ncsa.hdf.object.*;
import ncsa.hdf.object.h5.*;

/**
 * Creates an HDF5 file for unit tests.
 * 
 * @author xcao
 *
 */
public class H5TestFile {
    public final static String NAME_FILE_H5="TestHDF5.h5";
    public final static String NAME_GROUP = "/g0";
    public final static String NAME_GROUP_ATTR = "/g0_attr";
    public final static String NAME_GROUP_SUB = NAME_GROUP+"/g00";
    public final static String NAME_DATASET_INT = "/dataset_int";
    public final static String NAME_DATASET_FLOAT = "/dataset_float";
    public final static String NAME_DATASET_CHAR = "/dataset_byte";
    public final static String NAME_DATASET_STR = "/dataset_str";
    public final static String NAME_DATASET_ENUM = "/dataset_enum";    
    public final static String NAME_DATASET_IMAGE = "/dataset_image";
    public final static String NAME_DATASET_COMPOUND = "/dataset_comp";
    public final static String NAME_DATASET_SUB = NAME_GROUP + "/dataset_int";
    public final static String NAME_DATASET_SUB_SUB = NAME_GROUP_SUB+ "/dataset_float";
    public final static String NAME_DATASET_COMPOUND_SUB = NAME_GROUP + "/dataset_comp";
    public final static String NAME_DATATYPE_INT = NAME_GROUP + "/datatype_int";
    public final static String NAME_DATATYPE_FLOAT = NAME_GROUP + "/datatype_float";
    public final static String NAME_DATATYPE_STR = NAME_GROUP + "/datatype_str";

    // data space information
    public  final static int DATATYPE_SIZE = 4;
    public  final static int RANK = 2;
    public  final static long DIM1 = 50;
    public  final static long DIM2 = 10;
    public  static final long DIM3 = 20;
    public  final static long[] DIMs = {DIM1, DIM2};
    public  final static long[] CHUNKs = {DIM1/2, DIM2/2};
    public  final static int STR_LEN = 20;
    public  final static int DIM_SIZE = (int)(DIM1*DIM2);;
    
    /* testing data */
    public  final static int[] DATA_INT = new int[DIM_SIZE];
    public  final static float[] DATA_FLOAT = new float[DIM_SIZE];
    public  final static byte[] DATA_BYTE = new byte[DIM_SIZE];
    public  final static String[] DATA_STR = new String[DIM_SIZE];
    public  final static int[] DATA_ENUM = new int[DIM_SIZE];
    public  final static Vector DATA_COMP = new Vector(3);
    
    // compound names and datatypes
    public final static String[] COMPOUND_MEMBER_NAMES = {"int", "float", "string"};
    public final static H5Datatype[] COMPOUND_MEMBER_DATATYPES = {
        new H5Datatype(Datatype.CLASS_INTEGER, DATATYPE_SIZE, -1, -1), 
        new H5Datatype(Datatype.CLASS_FLOAT, DATATYPE_SIZE, -1, -1), 
        new H5Datatype(Datatype.CLASS_STRING, STR_LEN, -1, -1)};
    
    // attributes
    public final static Attribute ATTRIBUTE_STR = new Attribute(
            "attrName", 
            new H5Datatype(Datatype.CLASS_STRING, STR_LEN, -1, -1), 
            new long[] {1}, 
            new String[] {"attrValue"});
    public final static Attribute ATTRIBUTE_INT_ARRAY = new Attribute( 
            "arrayInt", 
            new H5Datatype(Datatype.CLASS_INTEGER, DATATYPE_SIZE, -1, -1), 
            new long[] {10}, 
            new int[] {1, 2, 3, 4, 5, 6, 7, 8, 9, 10});

    
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
    public static final void createTestFile()  throws Exception
    {
        H5File file=null;
        Group g0, g1, g00;
        Dataset[] dsets = new Dataset[10];
        
        H5Datatype typeInt = new H5Datatype(Datatype.CLASS_INTEGER, DATATYPE_SIZE, -1, -1);
        H5Datatype typeFloat = new H5Datatype(Datatype.CLASS_FLOAT, DATATYPE_SIZE, -1, -1);
        H5Datatype typeStr = new H5Datatype(Datatype.CLASS_STRING, STR_LEN, -1, -1);
        H5Datatype typeChar = new H5Datatype(Datatype.CLASS_CHAR, 1, -1, -1);
        H5Datatype typeEnum = new H5Datatype(Datatype.CLASS_ENUM, DATATYPE_SIZE, -1, -1);
       
        for (int i=0; i<DIM_SIZE; i++) {
            DATA_INT[i] = i;
            DATA_FLOAT[i] = i+i/100.0f;
            DATA_BYTE[i] = (byte)Math.IEEEremainder(i, 127);
            DATA_STR[i] = "str"+i;
            DATA_ENUM[i] = (int)Math.IEEEremainder(i, 2);
        }
        
        DATA_COMP.add(0, DATA_INT);
        DATA_COMP.add(1, DATA_FLOAT);
        DATA_COMP.add(2, DATA_STR);

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

        try { file.close(); } catch (Exception ex) {}
    }
}
