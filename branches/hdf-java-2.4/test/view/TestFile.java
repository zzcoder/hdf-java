package test.view;

import ncsa.hdf.object.*;
import ncsa.hdf.object.h5.*;

/**
 * Creates an HDF5 file for unit tests.
 * 
 * @author xcao
 *
 */
public class TestFile {
    
    
    public static final String NAME_FILE_H5="TestHDF5.h5";
    public static final String NAME_DATASET_INT8 = "/dataset_int8";
    public static final String NAME_DATASET_UINT8 = "/dataset_uint8";
    public static final String NAME_DATASET_INT16 = "/dataset_int16";
    public static final String NAME_DATASET_UINT16 = "/dataset_uint16";
    public static final String NAME_DATASET_INT32 = "/dataset_int32";
    public static final String NAME_DATASET_UINT32 = "/dataset_uint32";
    public static final String NAME_DATASET_INT64 = "/dataset_int64";
    
    public static final String[] OBJ_NAMES = {
        NAME_DATASET_INT8, NAME_DATASET_UINT8, NAME_DATASET_INT16,
        NAME_DATASET_UINT16, NAME_DATASET_INT32, NAME_DATASET_UINT32,
        NAME_DATASET_INT64};
    
    public static final long[] DATA_MAX_VALUES  = {127, 255, 32767, 65535, 2147483647, 4294967295L, 9223372036854775807L};
    public static final boolean[] isUnsigned = {false, true, false, true, false, true, false};
    
    // do not change the data, otherwise, the statistics will be wrong
    private static final byte[] _DATA = {
        1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 
        20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 
        37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53, 
        54, 55, 56, 57, 58, 59, 60, 61, 62, 63, 64, 65, 66, 67, 68, 69, 70, 
        71, 72, 73, 74, 75, 76, 77, 78, 79, 80};
    
    public static final double DATA_MIN = _DATA[0];
    public static final double DATA_MAX = _DATA[_DATA.length-1];
    public static final double DATA_AVG = 40.5;
    public static final double DATA_STD = 23.23790008;
    public static final double DATA_AUTO_MIN = DATA_AVG-3*DATA_STD;
    public static final double DATA_AUTO_MAX = DATA_AVG+3*DATA_STD;
    public static final double DATA_NEW_MIN = DATA_AUTO_MIN-(DATA_AUTO_MAX-DATA_AUTO_MIN)*0.1;
    public static final double DATA_NEW_MAX = DATA_AUTO_MAX+(DATA_AUTO_MAX-DATA_AUTO_MIN)*0.1;
   
    // data space information
    public static final int RANK = 2;
    public static final long DIM1 = _DATA.length;
    public static final long DIM2 = 1;
    public static final long[] DIMs = {DIM1, DIM2};
    public static final int DIM_SIZE = (int)(DIM1*DIM2);;
    
    /* testing data */
    public static final byte[] DATA_INT8 = new byte[DIM_SIZE];
    public static final short[] DATA_UINT8 = new short[DIM_SIZE];
    public static final short[] DATA_INT16 = new short[DIM_SIZE];
    public static final int[] DATA_UINT16 = new int[DIM_SIZE];
    public static final int[] DATA_INT32 = new int[DIM_SIZE];
    public static final long[] DATA_UINT32 = new long[DIM_SIZE];
    public static final long[] DATA_INT64 = new long[DIM_SIZE];
    
    /**
     * Creates an HDF5 test file. 
     * <p>
     * @throws Exception
     */
    public static final H5File createH5File(String fileName)  throws Exception
    {
        H5File file=null;
        
        if ((fileName == null) || (fileName.length()<1)) {
            fileName = NAME_FILE_H5;
        }
        
        final H5Datatype typeInt8 = new H5Datatype(Datatype.CLASS_INTEGER, 1, -1, -1);
        final H5Datatype typeUInt8 = new H5Datatype(Datatype.CLASS_INTEGER, 1, -1, Datatype.SIGN_NONE);
        final H5Datatype typeInt16 = new H5Datatype(Datatype.CLASS_INTEGER, 2, -1, -1);
        final H5Datatype typeUInt16 = new H5Datatype(Datatype.CLASS_INTEGER, 2, -1, Datatype.SIGN_NONE);
        final H5Datatype typeInt32 = new H5Datatype(Datatype.CLASS_INTEGER, 4, -1, -1);
        final H5Datatype typeUInt32 = new H5Datatype(Datatype.CLASS_INTEGER, 4, -1, Datatype.SIGN_NONE);
        final H5Datatype typeInt64 = new H5Datatype(Datatype.CLASS_INTEGER, 8, -1, -1);
       
        for (int i=0; i<DIM_SIZE; i++) {
            DATA_INT8[i] = _DATA[i];
            DATA_UINT8[i] = _DATA[i];
            DATA_INT16[i] = _DATA[i];
            DATA_UINT16[i] = _DATA[i];
            DATA_INT32[i] = _DATA[i];
            DATA_UINT32[i] = _DATA[i];
            DATA_INT64[i] = _DATA[i];
        }
        
        
        file = new H5File(fileName, FileFormat.CREATE);
        file.open();
       
        file.createScalarDS  (NAME_DATASET_INT8, null, typeInt8, DIMs, null, null, -1, DATA_INT8);
        file.createScalarDS  (NAME_DATASET_UINT8, null, typeUInt8, DIMs, null, null, -1, DATA_UINT8);
        file.createScalarDS  (NAME_DATASET_INT16, null, typeInt16, DIMs, null, null, -1, DATA_INT16);
        file.createScalarDS  (NAME_DATASET_UINT16, null, typeUInt16, DIMs, null, null, -1, DATA_UINT16);
        file.createScalarDS  (NAME_DATASET_INT32, null, typeInt32, DIMs, null, null, -1, DATA_INT32);
        file.createScalarDS  (NAME_DATASET_UINT32, null, typeUInt32, DIMs, null, null, -1, DATA_UINT32);
        file.createScalarDS  (NAME_DATASET_INT64, null, typeInt64, DIMs, null, null, -1, DATA_INT64);

        
        try { file.close(); } catch (final Exception ex) {}
        
        return file;
    }
    
}
