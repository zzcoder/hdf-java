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
    public final static String NAME_GROUP_SUB = "/g0/g00";
    public final static String NAME_DATASET_INT = "/dataset_int";
    public final static String NAME_DATASET_FLOAT = "/dataset_float";
    public final static String NAME_DATASET_CHAR = "/dataset_byte";
    public final static String NAME_DATASET_STR = "/dataset_str";
    public final static String NAME_DATASET_ENUM = "/dataset_enum";    
    public final static String NAME_DATASET_ATTR = "/dataset_with_attr";
    public final static String NAME_DATASET_COMPOUND = "/dataset_comp";
    public final static String NAME_DATASET_SUB = "/g0/dataset_int";
    public final static String NAME_DATASET_SUB_SUB = "/g0/g00/dataset_float";
    
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
    
    static {
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
          /dataset_str             Dataset {50, 10}
          /dataset_with_attr       Dataset {50, 10}
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

        file = new H5File(NAME_FILE_H5, FileFormat.CREATE);
        file.open();
       
        g0 = file.createGroup(NAME_GROUP, null);
        g1 = file.createGroup(NAME_GROUP_ATTR, null);
        g00 = file.createGroup(NAME_GROUP_SUB, null);

        long[] attrDims = {1};
        String attrName = "Test attribute";
        String[] attrValue = {"Test for group attribute"};
        Datatype attrType = new H5Datatype(Datatype.CLASS_STRING, attrValue[0].length()+1, -1, -1);
        Attribute attr = new Attribute(attrName, attrType, attrDims);
        attr.setValue(attrValue);
        g1.writeMetadata(attr);

        file.createScalarDS(NAME_DATASET_INT, null, new H5Datatype(Datatype.CLASS_INTEGER, DATATYPE_SIZE, -1, -1), DIMs, null, CHUNKs, 9, DATA_INT);
        file.createScalarDS(NAME_DATASET_FLOAT, null, new H5Datatype(Datatype.CLASS_FLOAT, DATATYPE_SIZE, -1, -1), DIMs, null, CHUNKs, 9, DATA_FLOAT);
        file.createScalarDS(NAME_DATASET_CHAR, null, new H5Datatype(Datatype.CLASS_CHAR, 1, -1, -1), DIMs, null, CHUNKs, 9, DATA_BYTE);
        file.createScalarDS(NAME_DATASET_STR, null, new H5Datatype(Datatype.CLASS_STRING, STR_LEN, -1, -1), DIMs, null, CHUNKs, 9, DATA_STR);
        file.createScalarDS(NAME_DATASET_ENUM, null, new H5Datatype(Datatype.CLASS_ENUM, DATATYPE_SIZE, -1, -1), DIMs, null, CHUNKs, 9, DATA_ENUM);
        file.createScalarDS(NAME_DATASET_SUB, g0, new H5Datatype(Datatype.CLASS_INTEGER, DATATYPE_SIZE, -1, -1), DIMs, null, CHUNKs, 9, DATA_INT);
        file.createScalarDS(NAME_DATASET_SUB_SUB, g00, new H5Datatype(Datatype.CLASS_FLOAT, DATATYPE_SIZE, -1, -1), DIMs, null, CHUNKs, 9, DATA_FLOAT);
        file.createImage(NAME_DATASET_ATTR, null, new H5Datatype(Datatype.CLASS_INTEGER, DATATYPE_SIZE, -1, -1), DIMs, null, CHUNKs, 9, 1, -1, DATA_BYTE);
        Datatype[]  mdtypes = {new H5Datatype(Datatype.CLASS_INTEGER, DATATYPE_SIZE, -1, -1), new H5Datatype(Datatype.CLASS_FLOAT, DATATYPE_SIZE, -1, -1), new H5Datatype(Datatype.CLASS_STRING, STR_LEN, -1, -1)};
        String[] mnames = {"int", "float", "string"};
        file.createCompoundDS(NAME_DATASET_COMPOUND, null, DIMs, null, CHUNKs, 9, mnames, mdtypes, null, DATA_COMP);

        try { file.close(); } catch (Exception ex) {}
    }
}
