package test.object.misc;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.BitSet;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Vector;

import ncsa.hdf.hdf5lib.H5;
import ncsa.hdf.hdf5lib.HDF5Constants;
import ncsa.hdf.hdf5lib.HDFNativeData;
import ncsa.hdf.hdf5lib.exceptions.HDF5Exception;
import ncsa.hdf.object.Attribute;
import ncsa.hdf.object.CompoundDS;
import ncsa.hdf.object.Dataset;
import ncsa.hdf.object.Datatype;
import ncsa.hdf.object.FileFormat;
import ncsa.hdf.object.Group;
import ncsa.hdf.object.HObject;
import ncsa.hdf.object.h5.H5CompoundDS;
import ncsa.hdf.object.h5.H5Datatype;
import ncsa.hdf.object.h5.H5File;
import ncsa.hdf.object.h5.H5Group;
import ncsa.hdf.object.h5.H5ScalarDS;

public class DebugHDF {

    public static void main(final String[] args) {
        
        try { 
            int[] libversion = {0, 0, 0};
            H5.H5get_libversion(libversion); 
            System.out.println(libversion[0]+"."+libversion[1]+"."+libversion[2]);
        } catch (Exception ex) {ex.printStackTrace();}
        
//      try { create_debug_file();} catch(Exception ex) {}
//      try { createStrDataset( "G:\\temp\\H5DatasetCreate.h5"); } catch(Exception ex) {}
//      try { createDataset( "E:\\temp\\H5DatasetCreate.h5"); } catch(Exception ex) {ex.printStackTrace();}
//      try { testchunkchche(); } catch (Exception ex) {ex.printStackTrace();}
//      try { TestHDFcompound(); } catch (Exception ex) {ex.printStackTrace();}
//      try { TestHDFdelete( "E:\\temp\\H5DatasetDelete.h5"); } catch(Exception ex) {ex.printStackTrace();}/
//      try { TestHDFcomment( "E:\\temp\\H5DatasetComment.h5"); }   catch(Exception ex) {ex.printStackTrace();}
//      try { TestHDFgenotype( "E:\\temp\\genotypes_chr22_CEU.h5"); }   catch(Exception ex) {ex.printStackTrace();}
//      try { TestHDFvector( "E:\\temp\\TestVector.h5"); }   catch(Exception ex) {ex.printStackTrace();}
//      try { testSizeof(); }   catch(Exception ex) {ex.printStackTrace();}
//      try { testSDgetchunkinfo("E:\\temp\\MOD021KM.A2006016.0942.hdf"); }   catch(Exception ex) {ex.printStackTrace();}
//      try { testEnum("E:\\temp\\MOD021KM.A2006016.0942.hdf"); }   catch(Exception ex) {ex.printStackTrace();}
//      try { testHDF5OpenClose(); }   catch(Exception ex) {ex.printStackTrace();}
//      try { testHDF5Write("E:\\temp\\TestHDF5Write.h5"); }   catch(Exception ex) {ex.printStackTrace();}
//      try { TestHDF5Misc("E:\\hdf-files\\TestHDF5Misc.h5"); }   catch(Exception ex) {ex.printStackTrace();}
//      try { TestHDF5Get("E:\\hdf-files\\TestHDF5Get.h5"); }   catch(Exception ex) {ex.printStackTrace();}
//      try { TestHDF5Copy("d:\\hdf-files\\hdf5_test.h5", "/arrays/Vdata with mixed types"); }   catch(Exception ex) {ex.printStackTrace();}
//      try { TestHDF5Copy("d:\\hdf-files\\hdf5_test.h5", "/arrays"); }   catch(Exception ex) {ex.printStackTrace();}
//      try { TestHDF5Copy("d:\\hdf-files\\hdf5_test.h5", "/datatypes/H5T_NATIVE_INT"); }   catch(Exception ex) {ex.printStackTrace();}
//      try { testGetObjID(); }   catch(Exception ex) {ex.printStackTrace();}
//      try { testFillValue( "E:\\temp\\TestFillValue.h5"); } catch(Exception ex) {}
//      try { TestGetOneRow("E:\\hdf-files\\hdf5_test.h5", "/arrays/Vdata with mixed types", 0); }   catch(Exception ex) {ex.printStackTrace();}
//      try { TestGetOneRow("E:\\hdf-files\\HDF5FileDAOTest.h5", "/Group0/1/Table0"); }   catch(Exception ex) {ex.printStackTrace();}
//      try { TestH5OpenClose("TestH5Object.h5"); } catch(Exception ex) {ex.printStackTrace();}
//      try { checkMemory(); } catch(Exception ex) {ex.printStackTrace();}
//      try { testCompressedStrings("G:\\temp\\test.h5"); } catch(Exception ex) {ex.printStackTrace();}
//      try { testCreateLongPath("G:\\temp\\test_hdf5_5_group_levels.h5"); } catch(Exception ex) {ex.printStackTrace();}
//      try { TestH5Bug847("d:\\hdf-files\\h5bug847.h5"); } catch(Exception ex) {ex.printStackTrace();}
//      try { TestH5ReadChunk("d:\\hdf-files\\ExampleHDF5.hdf5"); } catch(Exception ex) {ex.printStackTrace();}
//      try { TestH5Bug863("d:\\hdf-files\\bug863.h5"); } catch(final Exception ex) {ex.printStackTrace();}
//      try { checkMemory(); } catch(Exception ex) {ex.printStackTrace();}
//      try { testConvertFromUnsignedC();} catch(Exception ex) {ex.printStackTrace();}
//      try { TestH5ReadPerf("d:\\hdf-files\\ushort_8kx8k_fast_order.h5"); } catch(Exception ex) {ex.printStackTrace();}
//      try { TestH5ReadPerf("d:\\hdf-files\\ushort_8kx8k_fast_order.h5"); } catch(Exception ex) {ex.printStackTrace();}
//      try { TestH5WriteFloats("g:\\temp\\t.h5"); } catch(Exception ex) {ex.printStackTrace();}
//      try { TestH5Vlen("g:\\temp\\t.h5"); } catch(Exception ex) {ex.printStackTrace();}
//      try { TestH5Array("g:\\temp\\t.h5"); } catch(Exception ex) {ex.printStackTrace();}
//      try { TestH5DreadNIO("d:\\hdf-files\\ushort_8kx8k_fast_order.h5"); } catch(Exception ex) {ex.printStackTrace();}
//      try { TestH5Compound2000Fields("g:\\temp\\h5comp2k.h5"); } catch(Exception ex) {ex.printStackTrace();}
//      try { TestMemoryLeakOpenClose("D:\\hdf-files\\SAFNWC_MSG2_TPW__200807281015_CoMd.h5"); } catch(Exception ex) {ex.printStackTrace();}
//      try { testTofwerkReaderBug1213("D:\\hdf-files\\bug1213_GCxGC_dummyData.h5"); } catch(Exception ex) {ex.printStackTrace();}
//      try { testTofwerkReaderBug1213("D:\\hdf-files\\bug1213_GCxGC_dummyData_chunk100x200.h5"); } catch(Exception ex) {ex.printStackTrace();}
//      try { testTofwerkReaderBug1213("G:\\Projects\\Java\\Release\\hdfview_release_test_files\\bug1213_GCxGC_dummyData_chunk10x20.h5"); } catch(Exception ex) {ex.printStackTrace();}
//      try { TestMemoryLeak("D:\\hdf-files\\debug_memory_leak.h5"); } catch(Exception ex) {ex.printStackTrace();}
//      try { TestBEAttr("G:\\temp\\TestBEAttr.h5"); } catch(Exception ex) {ex.printStackTrace();}
//      try { TestVlenRead("d:\\hdf-files\\test_vlen.h5"); } catch(Exception ex) {ex.printStackTrace();}
//        try { launchBrowser("http://www.armchairgeneral.com/tactics-101-021-intelligence-preparation-of-the-battlefield-in-urban-operations.htm"); } catch(Exception ex) {ex.printStackTrace();}
//        try { launchBrowser("G:\\Projects\\ERDC\\Data\\Hetereogenous_Objects\\Events_Intel-Report.pdf"); } catch(Exception ex) {ex.printStackTrace();}
//        try { launchBrowser("G:\\Projects\\ERDC\\Data\\Hetereogenous_Objects\\IPB_URL-Reference.txt"); } catch(Exception ex) {ex.printStackTrace();}
//        try { launchBrowser("G:\\Projects\\ERDC\\Data\\Hetereogenous_Objects\\Recon_Immersive-Video.avi"); } catch(Exception ex) {ex.printStackTrace();}
//        try { launchBrowser("G:\\Projects\\ERDC\\Data\\Hetereogenous_Objects\\Situation_Weather-XLS.xls"); } catch(Exception ex) {ex.printStackTrace();}
//        try { launchBrowser("G:\\Projects\\ERDC\\Data\\Hetereogenous_Objects\\Software_External-Link.txt"); } catch(Exception ex) {ex.printStackTrace();}       
//      try { TestVlen("d:\\hdf-files\\test_vlen.h5"); } catch(Exception ex) {ex.printStackTrace();}
//      try { TestPinning("G:\\Projects\\Rosetta\\debug\\test_pinning.h5"); } catch(Exception ex) {ex.printStackTrace();}
//      try { createDataset("g:\\temp\\testDataset.h5"); } catch(Exception ex) {ex.printStackTrace();}

//      try { TestVlen("d:\\hdf-files\\test_vlen.h5", FileFormat.WRITE); } catch(Exception ex) {ex.printStackTrace();}
//      try { TestVlen("d:\\hdf-files\\test_vlen_org.h5", FileFormat.READ); } catch(Exception ex) {ex.printStackTrace();}
//      try { TestVlen("g:\\temp\\test_vlen.h5", FileFormat.WRITE); } catch(Exception ex) {ex.printStackTrace();}
//        TestBit64();
//        TestBitmask();
//        TestBinaryWrite(1, 1);
//        TestBinaryWrite(9, 1);
//        TestBinaryWrite(15, 1);
//        TestBinaryWrite(127, 2);
//        TestBinaryWrite(2147483647, 2);
//        TestBinaryWrite(2147483647, 4);
//        TestBinaryWrite(9123456789123456789L, 8);
//    
//        try { TestBug1523("G:\\Projects\\HUGS\\data\\testfile02.h5.corrupt"); } catch(Exception ex) {ex.printStackTrace();}
//       try { TestBug1523("G:\\Projects\\HUGS\\data\\testfile02.h5"); } catch(Exception ex) {ex.printStackTrace();}
//        try { createINF("G:\\temp\\inf.h5"); } catch(Exception ex) {ex.printStackTrace();}
//        try { createNaN("G:\\temp\\nan_all.h5"); } catch(Exception ex) {ex.printStackTrace();}
//        try { testStrings("G:\\temp\\strs.h5"); } catch(Exception ex) {ex.printStackTrace();}
//        testVariableArity("null argument", null);
//        testVariableArity("no argument");
//        testVariableArity("1 argument", 1);
//        testVariableArity("2 argument", 1,"string");
//        testVariableArity("3 argument", 1,"string",2.59);
//       try { readDatatype(); } catch(Exception ex) {ex.printStackTrace();}
//       try { readTextFile("G:\\temp\\vlarsizes.txt"); } catch(Exception ex) {ex.printStackTrace();}
//       try {processa8apis(); } catch (Exception ex) {} 
//       try {convertByte2Long(); } catch (Exception ex) {ex.printStackTrace();} 
//       try {testH5IO("G:\\temp\\test.h5"); } catch (Exception ex) {ex.printStackTrace();} 
//        try {testH5Core("G:\\temp\\test.h5"); } catch (Exception ex) {ex.printStackTrace();} 
//        try {test1Dstrings("G:\\temp\\test.h5"); } catch (Exception ex) {ex.printStackTrace();} 
//        try {testUpdateAttr("G:\\temp\\test.h5"); } catch (Exception ex) {ex.printStackTrace();} 
        try {testCreateVlenStr("G:\\temp\\test.h5"); } catch (Exception ex) {ex.printStackTrace();} 
//        try {testH5TconvertStr(); } catch (Exception ex) {ex.printStackTrace();} 
//        try {testH5DeleteDS("g:\\temp\\strs.h5"); } catch (Exception ex) {ex.printStackTrace();} 
//        try {testExtendData("g:\\temp\\extended.h5", "dset", 1000, 1500); } catch (Exception ex) {ex.printStackTrace();} 
//        try {createNestedcompound("g:\\temp\\nested_cmp.h5", "dset"); } catch (Exception ex) {ex.printStackTrace();}
//        try {  testH5Vlen("G:\\temp\\str.h5") ; } catch (Exception ex) {ex.printStackTrace();}
//        try {  testH5VlenObj("G:\\temp\\str2.h5") ; } catch (Exception ex) {ex.printStackTrace();}
//        try {  testH5VlenAttr("G:\\temp\\vlen_str_attr.h5") ; } catch (Exception ex) {ex.printStackTrace();}
//        try {testRefData("g:\\temp\\refs.h5", "refs"); } catch (Exception ex) {ex.printStackTrace();}
//      try {testH5WriteDouble("g:\\temp\\double.h5"); } catch (Exception ex) {ex.printStackTrace();}
    }
    
    public static void testRefData(String fname, String dname)throws Exception
    {
    	int size = 10;
    	long dims[] = {size};
    	long[] maxdims = {HDF5Constants.H5S_UNLIMITED};
    	byte[][] ref_buf = new byte[2][8];
    	
    	float data[] = new float[size];
    	for (int i=0; i<size; i++)
    		data[i] = i;
    	
        H5File file = new H5File(fname, H5File.CREATE);
        file.open();
        
        // create a ref dataset
        Group grp = file.createGroup("grp", null);
        Dataset ds = file.createScalarDS("dset", grp, new H5Datatype(Datatype.CLASS_FLOAT, -1, -1, -1), dims, maxdims, null, 0, data);
        ref_buf[0] = H5.H5Rcreate(file.getFID(), grp.getFullName(), HDF5Constants.H5R_OBJECT, -1);
        ref_buf[1] = H5.H5Rcreate(file.getFID(), ds.getFullName(), HDF5Constants.H5R_OBJECT, -1);
        ds = file.createScalarDS(dname, null, new H5Datatype(Datatype.CLASS_REFERENCE, -1, -1, -1), new long[] {2}, null, null, 0, ref_buf);
        
        // create ref attributes
        Datatype attr_dtype = file.createDatatype( Datatype.CLASS_REFERENCE, Datatype.NATIVE, Datatype.NATIVE, Datatype.NATIVE);
        Attribute attr = new Attribute("ref", attr_dtype, new long[] {1});
        attr.setValue(ds.getFullName());
        file.writeAttribute(ds, attr, false);
        attr = new Attribute("refs", attr_dtype, new long[] {2});
        attr.setValue(ref_buf);
        ds.writeMetadata(attr);        
        
    	file.close();
    	
    	// open the file and the dataset with refs
    	file.open();
    	ds = (H5ScalarDS)file.get(dname);
     	long[] refs = (long[])ds.getData();
     	
     	// use low level API function, H5.H5Rget_name
       	String[] name = {""};;
     	for (int i=0; i<refs.length; i++) {
     		H5.H5Rget_name(file.getFID(), HDF5Constants.H5R_OBJECT, HDFNativeData.longToByte(refs[i]), name, 32);
    		System.out.println(name[0]);
    	}
    	
     	// if file.open() was called, search objects in memory by high level function, findObject() 
    	long[] oid = new long[1];
    	for (int i=0; i<refs.length; i++) {
    		oid[0] = refs[i];
     		HObject obj = FileFormat.findObject(file, oid);
    		System.out.println(obj.getFullName());
    	}

    	file.close();
	}
    
    private static void testH5VlenAttr( String fname) throws Exception
    {
        FileFormat fileFormat = FileFormat.getFileFormat(FileFormat.FILE_TYPE_HDF5);
        if (fileFormat == null)
        {
            System.err.println("Cannot find HDF5 FileFormat.");
            return;
        }

        H5File testFile = (H5File)fileFormat.create(fname);
        if (testFile == null)
        {
            System.err.println("Failed to create file:"+fname);
            return;
        }

        testFile.open();
        Group root = (Group)((javax.swing.tree.DefaultMutableTreeNode)testFile.getRootNode()).getUserObject();

        String[] data = {"abc de fghi jk lmn op qrst uvw xyz\n0 12 345 6 789"};
        long[] data_dims = {1};
        Datatype dtype = testFile.createDatatype(
            Datatype.CLASS_STRING, data[0].length(),
            Datatype.NATIVE, Datatype.NATIVE);
        Dataset dataset = testFile.createScalarDS
            ("text", root, dtype, data_dims, null, null, 0, data);

        long[] attr_dims = {3};
        String[] attr_value = {"a", "ab", "abc"};
        Datatype attr_dtype = testFile.createDatatype(
            Datatype.CLASS_STRING, 5,
            Datatype.NATIVE, Datatype.NATIVE);
        Attribute attr = new Attribute("foo", attr_dtype, attr_dims);
        
        //byte[] bvalue = Dataset.stringToByte(attr_value, 5);
        attr.setValue(attr_value);
        dataset.writeMetadata(attr);

        testFile.close();

        // read attributes back
        testFile = (H5File)fileFormat.open(fname, FileFormat.READ);
        if (testFile == null)
        {
            System.err.println("Failed to open file:"+fname);
            return;
        }
        testFile.open();
        root = (Group)((javax.swing.tree.DefaultMutableTreeNode)testFile.getRootNode()).getUserObject();
        dataset = (Dataset)root.getMemberList().get(0);
        List attrList = dataset.getMetadata();
        attr = (Attribute)attrList.get(0);
        String[] value = (String [])attr.getValue();
        testFile.close();
    }
    
    private static void createNestedcompound(String fname, String dname) throws Exception 
    {
        int DIM1 = 50;
        long[] dims = {DIM1};
        int cmpSize = 20;
        int fid=-1, did=-1, tid = -1, tid_nested=-1, sid=-1;
        int indexData[] = new int[DIM1];
        double lonData[] = new double[DIM1];
        double latData[] = new double[DIM1];
       
        
        for (int i=0; i<DIM1; i++) {
        	indexData[i] = i;
        	lonData[i] = 5200.1+i;
        	latData[i] = 10.2+i;
        }
        
        fid = H5.H5Fcreate(fname, HDF5Constants.H5F_ACC_TRUNC, HDF5Constants.H5P_DEFAULT, HDF5Constants.H5P_DEFAULT);
        sid = H5.H5Screate_simple(1, dims, null);
        
        tid_nested = H5.H5Tcreate(HDF5Constants.H5T_COMPOUND, 16);
        H5.H5Tinsert(tid_nested, "Lon", 0, HDF5Constants.H5T_NATIVE_DOUBLE);
        H5.H5Tinsert(tid_nested, "Lat", 8, HDF5Constants.H5T_NATIVE_DOUBLE);

        tid = H5.H5Tcreate(HDF5Constants.H5T_COMPOUND, cmpSize);
        H5.H5Tinsert(tid, "index", 0, HDF5Constants.H5T_NATIVE_INT32);
        H5.H5Tinsert(tid, "location", 4, tid_nested);
    
    	did = H5.H5Dcreate(fid, dname, tid, sid, HDF5Constants.H5P_DEFAULT, HDF5Constants.H5P_DEFAULT, HDF5Constants.H5P_DEFAULT);

    	// write the first field "index"
    	int tid_tmp = H5.H5Tcreate(HDF5Constants.H5T_COMPOUND, 4);
    	H5.H5Tinsert(tid_tmp, "index", 0, HDF5Constants.H5T_NATIVE_INT32);
        H5.H5Dwrite(did, tid_tmp, HDF5Constants.H5S_ALL, HDF5Constants.H5S_ALL, HDF5Constants.H5P_DEFAULT, indexData);
        H5.H5Tclose(tid_tmp);
 
        // write the first field of the nested compound, "location"->"Lon"
    	tid_tmp = H5.H5Tcreate(HDF5Constants.H5T_COMPOUND, 8);
    	H5.H5Tinsert(tid_tmp, "Lon", 0, HDF5Constants.H5T_NATIVE_DOUBLE);
    	int tid_tmp_nested = H5.H5Tcreate(HDF5Constants.H5T_COMPOUND, 8);
    	H5.H5Tinsert(tid_tmp_nested, "location", 0, tid_tmp);
        H5.H5Dwrite(did, tid_tmp_nested, HDF5Constants.H5S_ALL, HDF5Constants.H5S_ALL, HDF5Constants.H5P_DEFAULT, lonData);
        H5.H5Tclose(tid_tmp_nested);
        H5.H5Tclose(tid_tmp);

        // write the second field of the nested compound, "location"->"Lat"
    	tid_tmp = H5.H5Tcreate(HDF5Constants.H5T_COMPOUND, 8);
    	H5.H5Tinsert(tid_tmp, "Lat", 0, HDF5Constants.H5T_NATIVE_DOUBLE);
    	tid_tmp_nested = H5.H5Tcreate(HDF5Constants.H5T_COMPOUND, 8);
    	H5.H5Tinsert(tid_tmp_nested, "location", 0, tid_tmp);
        H5.H5Dwrite(did, tid_tmp_nested, HDF5Constants.H5S_ALL, HDF5Constants.H5S_ALL, HDF5Constants.H5P_DEFAULT, latData);
        H5.H5Tclose(tid_tmp_nested);
        H5.H5Tclose(tid_tmp);

        H5.H5Tclose(tid);
        H5.H5Tclose(tid_nested);
        H5.H5Sclose(sid);
        H5.H5Dclose(did);
        H5.H5Fclose(fid);
    }

    
    public static void testExtendData(String fname, String dname, int size, int newSize)throws Exception
    {
    	long dims[] = {size};
    	long[] maxdims = {HDF5Constants.H5S_UNLIMITED};
    	long newDims[] = {newSize};
    	int extended = newSize - size;
    	
    	if (extended<=0)
    		return; // nothing to extended
    	
    	float data[] = new float[size];
    	for (int i=0; i<size; i++)
    		data[i] = i;
    	
       	float extendedData[] = new float[extended];
    	for (int i=0; i<extended; i++)
    		extendedData[i] = size+i;
    	
        H5File file = new H5File(fname, H5File.CREATE);
        file.open();
        file.createScalarDS(dname, null, new H5Datatype(Datatype.CLASS_FLOAT, -1, -1, -1), dims, maxdims, null, 0, data);
    	file.close();
    	
    	// reopen the file
    	file.open();
    	H5ScalarDS ds = (H5ScalarDS)file.get(dname);
    	
    	ds.init();
    	ds.extend(newDims);
    	
        long [] start  = ds.getStartDims();
        long [] count  = ds.getSelectedDims();
        start[0] = size;
        count[0] = extended;
        ds.write(extendedData);
        
    	file.close();
	}

    private static final void testH5DeleteDS(String fname) throws Exception
    {
        String dname = "strs";
        int nloops=1000, rank=1, strLen=1, fid=-1, tid=-1, sid=-1, did=-1;
        String[] strData = { "1", "2", "3", "4" };
        long[] dims = { strData.length };
        byte[] byteData = new byte[strData.length*strLen];
        
        for (int i=0; i<strData.length; i++)
        	System.arraycopy(strData[i].getBytes(), 0, byteData, i*strLen, strLen);

        fid = H5.H5Fcreate(fname, HDF5Constants.H5F_ACC_TRUNC, HDF5Constants.H5P_DEFAULT, HDF5Constants.H5P_DEFAULT);
        tid = H5.H5Tcopy(HDF5Constants.H5T_C_S1);
        H5.H5Tset_size(tid, 1);
        sid = H5.H5Screate_simple(rank, dims, null);
        did = H5.H5Dcreate(fid, dname, tid, sid, HDF5Constants.H5P_DEFAULT);
        H5.H5Dwrite(did, tid, HDF5Constants.H5S_ALL, HDF5Constants.H5S_ALL, HDF5Constants.H5P_DEFAULT, byteData);

        H5.H5Dclose(did);
        H5.H5Fclose(fid);
         
        for (int loop=0; loop<nloops; loop++) {
    		fid = H5.H5Fopen(fname, HDF5Constants.H5F_ACC_RDWR, HDF5Constants.H5P_DEFAULT);
    		H5.H5Ldelete( fid, dname, HDF5Constants.H5P_DEFAULT);

    		strLen = loop+1;
            H5.H5Tset_size(tid, strLen);
            
            for (int i=0; i<strData.length; i++) {
            	strData[i] = "";
            	for (int j=0; j<=loop; j++) {
            		strData[i] += (i+1);
            	}
            }
            
            byteData = new byte[strData.length*strLen];
     
            for (int i=0; i<strData.length; i++) {
             	System.arraycopy(strData[i].getBytes(), 0, byteData, i*strLen, strLen);
            }

            did = H5.H5Dcreate(fid, dname, tid, sid, HDF5Constants.H5P_DEFAULT);
            H5.H5Dwrite(did, tid, HDF5Constants.H5S_ALL, HDF5Constants.H5S_ALL, HDF5Constants.H5P_DEFAULT, byteData);

            H5.H5Dclose(did);
            H5.H5Fclose(fid);
        }
        
        H5.H5Sclose(sid);
        H5.H5Tclose(tid);
    }    
    
    private static final void testH5TconvertStr() throws Exception
    {
        String[] strs = {"a1234","b1234"};
        int srcLen=5, dstLen=10, srcId=-1, dstId=-1, dimSize=strs.length;
        byte[]   buf = new byte[dimSize*dstLen];
        
        for (int i=0; i<dimSize; i++)
        	System.arraycopy(strs[i].getBytes(), 0, buf, i*srcLen, 5);
   
        for (int i=0; i<dimSize; i++)
        	System.out.println(new String(buf, i*srcLen, srcLen));
        
        srcId = H5.H5Tcopy(HDF5Constants.H5T_C_S1);
        H5.H5Tset_size(srcId, srcLen);
 
        dstId = H5.H5Tcopy(HDF5Constants.H5T_C_S1);
        H5.H5Tset_size(dstId, dstLen);

        H5.H5Tconvert(srcId, dstId, dimSize, buf, null, HDF5Constants.H5P_DEFAULT);
        
        H5.H5Tclose(srcId);
        H5.H5Tclose(dstId);
        
        for (int i=0; i<dimSize; i++) {
        	String str = new String(buf, i*dstLen, dstLen);
        	System.out.println(str);
        	System.out.println(str.startsWith(strs[i]));
        }
    }
    
    private static final void testCreateVlenStr(String fname) throws Exception
    {
    	String dname = "DS1";
    	int file_id=-1, type_id=-1, dataspace_id =-1, dataset_id = -1;
    	String[] strData = { "Parting", "is such", "sweet", "sorrow." };
    	int rank = 1;
    	long[] dims = { strData.length };

    	// Create a new file using default properties.
    	try {
    		file_id = H5.H5Fcreate(fname, HDF5Constants.H5F_ACC_TRUNC,
    				HDF5Constants.H5P_DEFAULT, HDF5Constants.H5P_DEFAULT);
    	}
    	catch (Exception e) {
    		e.printStackTrace();
    	}

    	try {
    		type_id = H5.H5Tcopy(HDF5Constants.H5T_C_S1);
    	   	H5.H5Tset_size(type_id, HDF5Constants.H5T_VARIABLE);
    	}
    	catch (Exception e) {
    		e.printStackTrace();
    	}
    	
    	// Create dataspace. Setting maximum size to NULL sets the maximum
    	// size to be the current size.
    	try {
    		dataspace_id = H5.H5Screate_simple(rank, dims, null);
    	}
    	catch (Exception e) {
    		e.printStackTrace();
    	}

    	// Create the dataset and write the string data to it.
    	try {
    		if ((file_id >= 0) && (type_id >= 0) && (dataspace_id >= 0))
    			dataset_id = H5.H5Dcreate(file_id, dname, type_id,
    					dataspace_id, HDF5Constants.H5P_DEFAULT);
    	}
    	catch (Exception e) {
    		e.printStackTrace();
    	}

    	// Write the data to the dataset.
    	try {
    		if ((dataset_id >= 0) && (type_id >= 0))
    			H5.H5DwriteString(dataset_id, type_id, HDF5Constants.H5S_ALL,
    					HDF5Constants.H5S_ALL, HDF5Constants.H5P_DEFAULT, strData);
    	}
    	catch (Exception e) {
    		e.printStackTrace();
    	}
    	
    	// Write the data to the dataset.
    	try {
    		if ((dataset_id >= 0) && (type_id >= 0)) {
    			String[] buf = new String[strData.length];
    			H5.H5Dread(dataset_id, H5.H5Dget_type(dataset_id), HDF5Constants.H5S_ALL,
    					HDF5Constants.H5S_ALL, HDF5Constants.H5P_DEFAULT, buf);
    			
     			for (int i =0; i<strData.length; i++)
    			System.out.println(buf[i]);
    		}
    	}
    	catch (Exception e) {
    		e.printStackTrace();
    	}


    	// End access to the dataset and release resources used by it.
    	try {
    		if (dataset_id >= 0)
    			H5.H5Dclose(dataset_id);
    	}
    	catch (Exception e) {
    		e.printStackTrace();
    	}

    	// Terminate access to the data space.
    	try {
    		if (dataspace_id >= 0)
    			H5.H5Sclose(dataspace_id);
    	}
    	catch (Exception e) {
    		e.printStackTrace();
    	}

    	// Terminate access to the file type.
    	try {
    		if (type_id >= 0)
    			H5.H5Tclose(type_id);
    	}
    	catch (Exception e) {
    		e.printStackTrace();
    	}

    	// Close the file.
    	try {
    		if (file_id >= 0)
    			H5.H5Fclose(file_id);
    	}
    	catch (Exception e) {
    		e.printStackTrace();
    	}
    }

    private static void testUpdateAttr(String fname) throws Exception
    {
    	int data[] = {1,2,3,4,5,6};
        long[] dims = {data.length};
        String dname = "/dset";

        // create a new file and a new dataset
        H5File file = new H5File(fname, FileFormat.CREATE);
        Datatype dtype = file.createDatatype(Datatype.CLASS_INTEGER, 4, Datatype.NATIVE, Datatype.NATIVE);
        Dataset dataset = file.createScalarDS (dname, null, dtype, dims, null, null, 0, data);
        
        // create and write an attribute to the dataset
        long[] attrDims = {2};
        int[] attrValue = {0, 10000};
        Attribute attr = new Attribute("range", dtype, attrDims);
        attr.setValue(attrValue); // set the attribute value
        dataset.writeMetadata(attr);

        // close the file
        file.close();

        // open the file with read and write access
        file = new H5File(fname, FileFormat.WRITE);

        // retrieve the dataset and attribute
        dataset = (Dataset)file.get(dname);
        attr = (Attribute)dataset.getMetadata().get(0);
        
        // change the attribute value
        if (attr!=null) {
            attrValue[0] = 100;
            attr.setValue(attrValue);

            dataset.writeMetadata(attr);        	
        }

        // close file resource
        file.close();
    }
    
	public static void test1Dstrings(String fname) throws Exception {
		//row count of my dataset
		int rowCount = 5;
		
		//max string length
		int maxStringLength = 5;
		
		//buffer of test data to write
		String[] data = {"12345","12345","12345","12345","12345"};
		byte[][] buffer = new byte[5][5];
		for(int i=0; i<data.length; i++){
			buffer[i] = data[i].getBytes();
		}
		
		//create my file
		File file = new File("fname");
		int fileId = H5.H5Fcreate(file.getAbsolutePath(),
				HDF5Constants.H5F_ACC_TRUNC, HDF5Constants.H5P_DEFAULT,
				HDF5Constants.H5P_DEFAULT);
		
		int RANK = 1;
		long[] dataset_dims = { rowCount };
		long[] max_dims = { HDF5Constants.H5S_UNLIMITED };
		long[] chunk_dims = { rowCount };
		
		int strtypeId = H5.H5Tcopy(HDF5Constants.H5T_C_S1);
		H5.H5Tset_size(strtypeId, maxStringLength);
		
		int memtypeId = H5.H5Tcopy(HDF5Constants.H5T_C_S1);
		H5.H5Tset_size(memtypeId, maxStringLength);
		
		int dataspaceId = H5.H5Screate_simple(RANK, dataset_dims, max_dims);
		int memspaceId = H5.H5Screate_simple(RANK, chunk_dims, chunk_dims);

		int dcplId = H5.H5Pcreate(HDF5Constants.H5P_DATASET_CREATE);
		H5.H5Pset_deflate(dcplId, 9);
		H5.H5Pset_chunk(dcplId, 1, chunk_dims);

		int datasetId = H5.H5Dcreate(fileId, "/ds0",
				strtypeId, dataspaceId, dcplId);
		
		long[] hyperslab_dims = { rowCount};
		memspaceId = H5.H5Screate_simple(1, hyperslab_dims, hyperslab_dims);
		
		H5.H5Sselect_hyperslab(dataspaceId, HDF5Constants.H5S_SELECT_SET,
				new long[] { 0 }, new long[] { 1 },
				new long[] { rowCount }, new long[] { 1});
		
		H5.H5Dwrite(datasetId, memtypeId, memspaceId,
				dataspaceId, HDF5Constants.H5P_DEFAULT, buffer);
		
		H5.H5Sclose(memspaceId);
		H5.H5Sclose(dataspaceId);
		H5.H5Tclose(strtypeId);
		H5.H5Tclose(memtypeId);
		H5.H5Dclose(datasetId);
		H5.H5Fclose(fileId);
	}



    private static void testH5Core(final String filename) throws Exception {
    	
    	int fapl_id = H5.H5Pcreate(HDF5Constants.H5P_FILE_ACCESS);
    	H5.H5Pset_fapl_core(fapl_id, 1024, true);
    	
    	
    }
    
    private static void testH5IO(final String filename) throws Exception {
    	int SIZE = 10*1024*1024;
    	int fid = -1, did = -1, sid = -1, rank = 1;
    	long[] dims = { SIZE };
    	float[] buf = new float[SIZE];

//    	for (int i = 0; i < buf.length; i++)
//    		buf[i] = i;
//
//    	try {
//    		fid = H5.H5Fcreate(filename, HDF5Constants.H5F_ACC_TRUNC,
//    				HDF5Constants.H5P_DEFAULT, HDF5Constants.H5P_DEFAULT);
//    		sid = H5.H5Screate_simple(rank, dims, null);
//    		did = H5.H5Dcreate(fid, "test", HDF5Constants.H5T_NATIVE_FLOAT,
//    				sid, HDF5Constants.H5P_DEFAULT);
//    	} finally {
//    		try {
//    			H5.H5Sclose(sid);
//    		} catch (HDF5Exception ex) {
//    		}
//    		try {
//    			H5.H5Dclose(did);
//    		} catch (HDF5Exception ex) {
//    		}
//
//    		try {
//    			H5.H5Fclose(fid);
//    		} catch (HDF5Exception ex) {
//    		}
//    	}
//
//    	try {
//
//    		fid = H5.H5Fopen(filename, HDF5Constants.H5F_ACC_RDWR,
//    				HDF5Constants.H5P_DEFAULT);
//    		did = H5.H5Dopen(fid, "test");
//
//    		long t0 = System.currentTimeMillis();
//    		H5.H5Dwrite_float(did, HDF5Constants.H5T_NATIVE_FLOAT,
//    				HDF5Constants.H5S_ALL, HDF5Constants.H5S_ALL,
//    				HDF5Constants.H5P_DEFAULT, buf);
//    		long t1 = System.currentTimeMillis();
//    		System.out.println("Time on writing (40MB): " + (t1 - t0));
//
//    	} finally {
//    		try {
//    			H5.H5Sclose(sid);
//    		} catch (HDF5Exception ex) {
//    		}
//    		try {
//    			H5.H5Dclose(did);
//    		} catch (HDF5Exception ex) {
//    		}
//
//    		try {
//    			H5.H5Fclose(fid);
//    		} catch (HDF5Exception ex) {
//    		}
//    	}

    	try {

    		fid = H5.H5Fopen(filename, HDF5Constants.H5F_ACC_RDWR,
    				HDF5Constants.H5P_DEFAULT);
    		did = H5.H5Dopen(fid, "test");

    		long t0 = System.currentTimeMillis();
    		H5.H5Dread_float(did, HDF5Constants.H5T_NATIVE_FLOAT,
    				HDF5Constants.H5S_ALL, HDF5Constants.H5S_ALL,
    				HDF5Constants.H5P_DEFAULT, buf);
    		System.out.println(buf[SIZE-1]);
    		long t1 = System.currentTimeMillis();
    		System.out.println("Time on reading (40MB): " + (t1 - t0));

    	} finally {
    		try {
    			H5.H5Sclose(sid);
    		} catch (HDF5Exception ex) {
    		}
    		try {
    			H5.H5Dclose(did);
    		} catch (HDF5Exception ex) {
    		}

    		try {
    			H5.H5Fclose(fid);
    		} catch (HDF5Exception ex) {
    		}
    	}

    }    

    
    
    public static void convertByte2Long() throws Exception {
        long[] la = {1000000000000000001L, 1000000000000000002L, 1000000000000000003L};
        byte[] ba = HDFNativeData.longToByte(0, la.length, la);
        if (ba.length != la.length*8) {
            System.out.println ("Failed to convert from byte[] to long[]");
            return ;
        }

//        // only need the two lines below to convert byte[] to long[]
//        ByteBuffer bb = ByteBuffer.wrap(ba);
//        long[] la2 = (bb.asLongBuffer()).array();
//        
//        if (la2.length != la.length) {
//            System.out.println ("Failed to convert from long[] to byte[]");
//            return;
//        }

        long[] la2 = HDFNativeData.byteToLong(ba);
        for (int i=0; i<la.length; i++) {
            if (la[i] != la2[i]) {
                System.out.println ("Failed to convert from long[] to byte[]");
                return;
            }
        }
        
        System.out.println ("OK: convert from byte[] to long[].");
    }
    
    public static void processa8apis() throws Exception {
        String[] newAPIs = new String[200];
        String[] allAPIs = new String[500];
        int nNew=0, nAll=0, idx=0, idx2=0;
        
        BufferedReader in_all18  = new BufferedReader(new FileReader("G:\\Projects\\Java\\1.8Support\\hdf5_1_8.txt"));
        BufferedReader in_new18  = new BufferedReader(new FileReader("G:\\Projects\\Java\\1.8Support\\hdf5_1_8_new.txt"));
        PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("G:\\Projects\\Java\\1.8Support\\hdf5_1_8_processed.txt")));
        
        String line = in_new18.readLine();
        while (line != null) {
            line = line.trim();
            if (line !=null && line.length()>0) {
                idx = line.indexOf(' ');
                idx2 = line.indexOf('(');
                newAPIs[nNew++] = line.substring(idx+1, idx2+1);
            }
            line = in_new18.readLine();
        }
        
        idx = 0;
        for (int i=0; i<nNew; i++) {
            line = newAPIs[i];
            if (line==null || line.length()<=0)
                continue;
            
            for (int j=i+1; j<nNew; j++) {
                if (line.equals(newAPIs[j])) {
                    idx++;
                    newAPIs[j] = null;
                }
            }
        }
        System.out.println("No. of new APIs = "+(nNew - idx));
        for (int i=0; i<nNew; i++)
            System.out.println(newAPIs[i]);
        
        line = in_all18.readLine();
        while (line != null) {
            line = line.trim();
            if (line !=null && line.length()>0)
                allAPIs[nAll++] = line;
            line = in_all18.readLine();
        }        
        
        // make sure no repeated elements
//        for (int i=0; i<nNew; i++) {
//            line = newAPIs[i];
//            if (line==null || line.length()<=0)
//                continue;
//            
//            for (int j=i+1; j<nNew; j++) {
//                if (line.equals(newAPIs[j])) {
//                    System.out.println(i + " and "+ j + " REPEATED !!!!!");
//                }
//            }
//        }
//        
//        for (int i=0; i<nAll; i++) {
//            line = allAPIs[i];
//            for (int j=i+1; j<nAll; j++) {
//                if (line.endsWith(allAPIs[j])) {
//                    System.out.println(i + " and "+ j + " REPEATED !!!!!");
//                }
//            }
//        }
        
        int isNew, isFunc;
        String apiName, newName;
        for(int i=0; i<nAll; i++) {
            idx = allAPIs[i].indexOf(' ');
            apiName = allAPIs[i].substring(idx+1);
            isNew = isFunc = 0;
            for (int j=0; j<nNew; j++) {
                if (newAPIs[j] == null || newAPIs[j].length()<=0)
                    continue;
                
                if (apiName.startsWith(newAPIs[j]) || 
                    apiName.startsWith("*"+newAPIs[j])) {                    
                    isNew = 1;
                    break;
                }
            }
            
            if (allAPIs[i].indexOf("func")>0 || allAPIs[i].indexOf("*op_data")>0)
                isFunc = 1;
            
            line = isFunc+ "\t"+ isNew+ "\t"+ allAPIs[i].substring(0, idx) + "\t" +apiName;
            out.println(line);
        }
        
        in_new18.close();
        in_all18.close();
        out.close();
    }
    
    public static void readTextFile(String fname) throws Exception {
        BufferedReader in  = new BufferedReader(new FileReader(fname));
        String line = in.readLine();
        StringTokenizer st = new StringTokenizer(line, ",") ;
        int n = st.countTokens();
        
        PrintWriter out
        = new PrintWriter(new BufferedWriter(new FileWriter(fname+".out")));
        while(st.hasMoreTokens()) {
            out.print(st.nextToken());
            out.println();
        }
        
        in.close();
        out.close();
    }
    
    public static void writeDatatype() throws Exception{
        String fname = "g:\\temp\\t1.h5";
        long[] dims0D = {1};

        // retrieve an instance of H5File

        FileFormat fileFormat = FileFormat

                    .getFileFormat(FileFormat.FILE_TYPE_HDF5);



        if (fileFormat == null) {

              System.err.println("Cannot find HDF5 FileFormat.");

              return;

        }



        // create a new file with a given file name.

        H5File testFile = (H5File) fileFormat.create(fname);



        if (testFile == null) {

              System.err.println("Failed to create file:" + fname);

              return;

        }



        // open the file and retrieve the root group

        testFile.open();

        H5Group root = (H5Group)
((javax.swing.tree.DefaultMutableTreeNode) testFile

                    .getRootNode()).getUserObject();



        /** add an Attribute */

        Datatype attrType = testFile.createDatatype(

                    Datatype.CLASS_INTEGER, 4, Datatype.NATIVE,

                    Datatype.NATIVE);



  

        

        Attribute attr = new Attribute("attribut int", attrType,dims0D);

        int[] attrValue = {15}; // attribute value

        

        attr.setValue(attrValue); // set the attribute value

        root.writeMetadata(attr);



        

        // close file resource

        testFile.close();

  }

  

  private static void readDatatype() throws Exception {
      String fname = "g:\\temp\\t1.h5";

         // retrieve an instance of H5File

    FileFormat fileFormat =
FileFormat.getFileFormat(FileFormat.FILE_TYPE_HDF5);



    if (fileFormat == null)

    {

        System.err.println("Cannot find HDF5 FileFormat.");

        return;

    }



    // open the file with read and write access

    FileFormat testFile = fileFormat.open(fname, FileFormat.READ);



    if (testFile == null)

    {

        System.err.println("Failed to open file: "+fname);

        return;

    }



    // open the file and retrieve the file structure

    testFile.open();

    

    Group root =
(Group)((javax.swing.tree.DefaultMutableTreeNode)testFile.getRootNode()).getUserObject();



    /** read Attribute */

    Attribute attr = (Attribute)root.getMetadata().get(0);
    //System.out.println(attr);

        attr.getName(); // -> attribute int
        
        //System.out.println(attr.getName());

        attr.getValue(); // -> [15]
        //System.out.println(attr.getValue());

        attr.getType(); // -> null...

       // System.out.println(attr.getType());
  

  }
  

    private static final void testVariableArity(String desc, Object... args) {
        System.out.print(desc+":\t");
        
        if (args == null) {
            System.out.println("null args");
            return;
        }
        
        if (args.length == 0) {
            System.out.println("no argument.");
            return;
        }
        
        for (int i=0; i<args.length; i++)
            System.out.print("args["+i+"] = "+args[i]+",\t");
        System.out.println();
    }
    
    private static final void testStrings(String fname)  throws Exception 
    {
        final int strLen = 128;
        final long[] dims = {1000};
        final long[] chunks = {100};
        final String[] data = new String[(int)dims[0]];
        
        // retrieve an instance of H5File
        final FileFormat fileFormat = FileFormat.getFileFormat(FileFormat.FILE_TYPE_HDF5);

        if (fileFormat == null)
        {
            System.err.println("Cannot find HDF5 FileFormat.");
            return;
        }

        // create a new file with a given file name.
        final H5File testFile = (H5File)fileFormat.create(fname);

        if (testFile == null)
        {
            System.err.println("Failed to create file:"+fname);
            return;
        }
        
        for (int i=0; i<data.length; i++) {
            data[i] = "test string "+i;;
        }

        // open the file and retrieve the root group
        testFile.open();
        final Group root = (Group)((javax.swing.tree.DefaultMutableTreeNode)testFile.getRootNode()).getUserObject();

        Datatype dtype = testFile.createDatatype(
            Datatype.CLASS_STRING, strLen, Datatype.NATIVE, Datatype.NATIVE);
        Dataset dataset = testFile.createScalarDS
            ("dset", root, dtype, dims, null, chunks, 6, data);

        testFile.close();
    }

    private static final void createINF(String fname)  throws Exception {
        final long[] dims2D = {5, 2};
        final float[] data = new float[(int)dims2D[0]*(int)dims2D[1]];
        final double[] data2 = new double[(int)dims2D[0]*(int)dims2D[1]];
        
        System.out.println ("(POSITIVE_INFINITY == POSITIVE_INFINITY) \t= "+(Float.POSITIVE_INFINITY == Float.POSITIVE_INFINITY));
        System.out.println ("(POSITIVE_INFINITY != POSITIVE_INFINITY) \t= "+(Float.POSITIVE_INFINITY != Float.POSITIVE_INFINITY));
        System.out.println ("(NEGATIVE_INFINITY == NEGATIVE_INFINITY) \t= "+(Float.NEGATIVE_INFINITY == Float.NEGATIVE_INFINITY));
        System.out.println ("(NEGATIVE_INFINITY != NEGATIVE_INFINITY) \t= "+(Float.NEGATIVE_INFINITY != Float.NEGATIVE_INFINITY));
        System.out.println ("(POSITIVE_INFINITY == NEGATIVE_INFINITY) \t= "+(Float.POSITIVE_INFINITY == Float.NEGATIVE_INFINITY));
        System.out.println ("(POSITIVE_INFINITY == NaN)               \t= "+(Float.POSITIVE_INFINITY == Float.NaN));
        System.out.println ("(NaN == NaN)                             \t= "+(Float.NaN == Float.NaN));
        System.out.println ("(NaN != NaN)                             \t= "+(Float.NaN != Float.NaN));
                

        // retrieve an instance of H5File
        final FileFormat fileFormat = FileFormat.getFileFormat(FileFormat.FILE_TYPE_HDF5);

        if (fileFormat == null)
        {
            System.err.println("Cannot find HDF5 FileFormat.");
            return;
        }

        // create a new file with a given file name.
        final H5File testFile = (H5File)fileFormat.create(fname);

        if (testFile == null)
        {
            System.err.println("Failed to create file:"+fname);
            return;
        }
        
        for (int i=0; i<data.length/2; i++) {
            data[i] = Float.POSITIVE_INFINITY;
            data2[i] = Double.POSITIVE_INFINITY;
        }
        for (int i=data.length/2; i<data.length; i++) {
            data[i] = Float.NEGATIVE_INFINITY;
            data2[i] = Double.NEGATIVE_INFINITY;
        }

        // open the file and retrieve the root group
        testFile.open();
        final Group root = (Group)((javax.swing.tree.DefaultMutableTreeNode)testFile.getRootNode()).getUserObject();

        Datatype dtype = testFile.createDatatype(
            Datatype.CLASS_FLOAT, 4, Datatype.NATIVE, Datatype.NATIVE);
        Dataset dataset = testFile.createScalarDS
            ("f32", root, dtype, dims2D, null, null, 0, data);
        
        dtype = testFile.createDatatype(
                Datatype.CLASS_FLOAT, 8, Datatype.NATIVE, Datatype.NATIVE);
        dataset = testFile.createScalarDS
                ("f64", root, dtype, dims2D, null, null, 0, data2);

        testFile.close();

    }
    
    private static final void createNaN(String fname)  throws Exception {
        final long[] dims2D = {5000, 3000};
        final float[] data = new float[(int)dims2D[0]*(int)dims2D[1]];

        final long[] dims2D_small = {500, 300};
        final float[] data_small = new float[(int)dims2D_small[0]*(int)dims2D_small[1]];

        // retrieve an instance of H5File
        final FileFormat fileFormat = FileFormat.getFileFormat(FileFormat.FILE_TYPE_HDF5);

        if (fileFormat == null)
        {
            System.err.println("Cannot find HDF5 FileFormat.");
            return;
        }

        // create a new file with a given file name.
        final H5File testFile = (H5File)fileFormat.create(fname);

        if (testFile == null)
        {
            System.err.println("Failed to create file:"+fname);
            return;
        }
   
        for (int i=0; i<data.length; i+=2) {
            data[i] = Float.NaN;
            data[i+1] = Float.NaN;
        }
        
        for (int i=0; i<data_small.length; i+=2) {
            data_small[i] = Float.NaN;
            data_small[i+1] = Float.NaN;
        }
        
//        for (int i=0; i<data.length; i+=2) {
//            data[i] = (float)(Math.random()*1000.0);
//            data[i+1] = (float)(Math.random()*1000.0);
//        }
//        
//        for (int i=0; i<data_small.length; i+=2) {
//            data_small[i] = (float)(Math.random()*1000.0);
//            data_small[i+1] = (float)(Math.random()*1000.0);
//        }

        // open the file and retrieve the root group
        testFile.open();
        final Group root = (Group)((javax.swing.tree.DefaultMutableTreeNode)testFile.getRootNode()).getUserObject();

        Datatype dtype = testFile.createDatatype(
            Datatype.CLASS_FLOAT, 4, Datatype.NATIVE, Datatype.NATIVE);
        Dataset dataset = testFile.createScalarDS
            ("large", root, dtype, dims2D, null, null, 0, data);
        Dataset dataset_small = testFile.createScalarDS
        ("small", root, dtype, dims2D_small, null, null, 0, data_small);


        testFile.close();

    }
    
    private static final void TestBug1523(String fname) throws Exception {
        H5File file = new H5File(fname);
        file.open();
        file.close();
    }
    
    public static final String TestBinaryWrite(long v, int nbytes)
    {
        if (nbytes <=0)
            return null;
        
        int nhex = nbytes*2;
        short[] hex = new short[nhex];
        
        for (int i=0; i<nhex; i++)
            hex[i] = (short)(0x0f & (v << (i*16) ));
        
        StringBuffer sb = new StringBuffer();
        for (int i=nhex-1; i>=0; i--) {
            switch (hex[i]) {
            case 0:  sb.append("0000"); break;
            case 1:  sb.append("0001"); break;
            case 2:  sb.append("0010"); break;
            case 3:  sb.append("0011"); break;
            case 4:  sb.append("0100"); break;
            case 5:  sb.append("0101"); break;
            case 6:  sb.append("0110"); break;
            case 7:  sb.append("0111"); break;
            case 8:  sb.append("1000"); break;
            case 9:  sb.append("1001"); break;
            case 10: sb.append("1010"); break;
            case 11: sb.append("1011"); break;
            case 12: sb.append("1100"); break;
            case 13: sb.append("1101"); break;
            case 14: sb.append("1110"); break;
            case 15: sb.append("1111"); break;
            }
        }
        
        System.out.println(sb);
        
        return sb.toString();
        
    }


    private static final void TestBitmask()
    {
        int bmask=0, value = 0;
        BitSet theMask = new BitSet(8);
        
        theMask.set(0);
        System.out.println(theMask.cardinality());
        theMask.set(7);
        System.out.println(theMask.cardinality());
        
        for (int i=0; i<8; i++) {
            if (theMask.get(i))
                bmask += 1<<i;
        }
        
        System.out.println(bmask);
        
//        System.out.println("15 & 1 = "+ (15 & 1));
//        System.out.println("15 & 0x01 = "+ (15 & 0x01));
//        System.out.println("15 & 0x0f = "+ (15 & 0x0f));
//        System.out.println("15 & 2 = "+ (15 & 2));
//        System.out.println("15 & 4 = "+ (15 & 4));
//
//        System.out.println("6 & 1 = "+ (6 & 1));
//        System.out.println("6 & 0x01 = "+ (6 & 0x01));
//        System.out.println("6 & 0x0f = "+ (6 & 0x0f));
//        System.out.println("6 & 15 = "+ (6 & 15));
//        System.out.println("6 & 4 = "+ (6 & 4));
//        
//        System.out.println("7 & 1 = "+ (7 & 1));
//        System.out.println("7 & 0x01 = "+ (7 & 0x01));
//        System.out.println("7 & 0x0f = "+ (7 & 0x0f));
//        System.out.println("7 & 15 = "+ (7 & 15));
//        System.out.println("7 & 4 = "+ (7 & 4));
//        
//        System.out.println("8 & 1 = "+ (8 & 1));
//        System.out.println("8 & 0x01 = "+ (8 & 0x01));
//        System.out.println("8 & 0x0f = "+ (8 & 0x0f));
//        System.out.println("8 & 15 = "+ (8 & 15));
//        System.out.println("8 & 4 = "+ (8 & 4));        
    }
    
    
    private static final void TestBit64()
    {
        System.out.println("0xffffffffffffffff = "+ 0xffffffffffffffffL);
    }
    
    /**
     * Testing variable length strings. 
     * @param fname the file that contains a dataset of variable-lenght strings.
     * @throws Exception
     */
    private static final void TestVlen(String fname, int accessID) throws Exception
    {
        Object data = null;
        H5File h5file = new H5File(fname, accessID);;
        Dataset dataset = (Dataset)h5file.get("/Dataset1");
        
        if (dataset == null)
            return;
        else {
            dataset.init();
            data = dataset.read();
        }
        
        int loop = 0;
        while (loop<100) {
            long t0 = System.currentTimeMillis();
            dataset.write(data);
            System.out.println("loop #"+loop+++": " + 
                    (System.currentTimeMillis()-t0)+" (ms).");
            System.gc();
        }
        
        h5file.close();
    }


    /**
     * test performance and memory use for both cases of GetPrimitiveArrayCritical() 
     * and Get<type>ArrayElements().. 
     * @param fname the file that contains datasets of int8, int16, int32, int64, 
     * float, and double.
     * @throws Exception
     */
    private static final void TestPinning(String fname) throws Exception
    {
        Object data;
        String dnames[] = {"bytes", "shorts", "ints", "longs", "floats", "doubles"};
        Dataset dsets[] = new Dataset[dnames.length];
        
        H5File h5file = new H5File(fname); // test_pinning.h5
        try  { 
            h5file.open(); 
        }
        catch (Exception ex) { 
            ex.printStackTrace();
            return;
        }
        
        for (int i=0; i<dsets.length; i++) {
            dsets[i] = (Dataset)h5file.get(dnames[i]);
            if (dsets[i] != null)
                dsets[i].init();
        }
        
        int loop=0;
        while (true) {
            for (int i=0; i<dsets.length; i++) {
                data = dsets[i].read();    // test reading data from file
                dsets[i].write(data);      // test wring data to file
            }
            
            if ( (loop % 10) == 0) {
                System.out.println(loop);
            }
            loop++;
            System.gc();
        }
    }
    
    public static void launchBrowser(String url) throws Exception {
    	String os = System.getProperty("os.name");
    	Runtime runtime=Runtime.getRuntime();
    	try{
    		// Block for Windows Platform
    		if (os.startsWith("Windows")){
    			String cmd = "rundll32 url.dll,FileProtocolHandler "+ url;
    			Process p = runtime.exec(cmd);
    		}
    		//Block for Mac OS
    		else if(os.startsWith("Mac OS")){
    			Class fileMgr = Class.forName("com.apple.eio.FileManager");
    			Method openURL = fileMgr.getDeclaredMethod("openURL", new Class[] {String.class});
    			openURL.invoke(null, new Object[] {url});
    		}
    		//Block for UNIX Platform
    		else {
    			String[] browsers = {"firefox", "opera", "konqueror", "epiphany", "mozilla", "netscape" };
    			String browser = null;
    			for (int count = 0; count < browsers.length && browser == null; count++)
    				if (runtime.exec(new String[] {"which", browsers[count]}).waitFor() == 0)
    					browser = browsers[count];
    			if (browser == null)
    				throw new Exception("Could not find web browser");
    			else
    				runtime.exec(new String[] {browser, url});
    		}
    	}catch(Exception x){
    		System.err.println("Exception occurd while invoking Browser!");
    		x.printStackTrace();
    	}
    }    
    
	private static void testVlenRead(String fname) throws Exception {
		
		boolean  useBufferedReads = false;	// true = break up reads into smaller chunks, false = get data at once
		
		H5File h5File = new H5File(fname);
		
		Dataset dataset = (Dataset)h5File.get("/Dataset1");
		dataset.init();
		int dsId = dataset.open();
		
		long[] dims = dataset.getDims();

		Object data;
		
		if (useBufferedReads) {
			// uses own memory space
			int numRows = (int)dims[0];
			int numBufRows = 200000;
			for (int loop = 0; loop < 20; loop++) {
				System.out.println("loop = "+loop);
				for (int rowsRead = 0; rowsRead < numRows; ) {
					int rowsToRead = Math.min(numRows-rowsRead,numBufRows);
					data = (String[])getData(dataset,rowsRead,rowsToRead);
					rowsRead += rowsToRead;
				}
			}
		} else {
			// uses default "all" memory space
			for (int loop = 0; loop < 20; loop++) {
				System.out.println("loop = "+loop);
				data = dataset.read();
			}
		}



		System.out.println("Enter CR to exit: ");
		System.in.read();
		
		// close datasets
		dataset.close(dsId);
	}
	
    private static Object getData( Dataset ds, int startRow, int numRows ) throws IOException
    {
    	Object o = null;
    	if (ds != null) {
    		ds.init();
    		if( ds.getRank() != 1 )
    		{
    			throw new IllegalArgumentException("Dataset not 1D"); //$NON-NLS-1$
    		}
    		long[] dims = ds.getDims();
    		long[] selectionStart = ds.getStartDims();
    		long[] selectionStride = ds.getStride();
    		long[] selectionCount = ds.getSelectedDims();
    		selectionStart[0] = startRow;
    		if( selectionStart.length == 2 )
    		{
    			selectionStart[1] = 0;
    		}
    		selectionStride[0] = 1;
    		if( selectionStride.length == 2 )
    		{
    			selectionStride[1] = 0;
    		}
    		selectionCount[0] = Math.min(dims[0] - startRow, numRows);
    		if( selectionCount.length == 2 )
    		{
    			selectionCount[1] = dims[1];
    		}

    		try
    		{

    			//o = ds.read(); 
  			
  				// can also use H5.H5Dread() directly, bypassing the Java object layer and use own memory space			

    			int did = ds.open();
    			int tid = H5.H5Dget_type(did);

    			int nativeDatatype = H5Datatype.toNative(tid);

    			int msid = H5.H5Screate_simple(ds.getRank(),selectionCount, null);
    			int fsid = H5.H5Dget_space(did);
    			long[] lsize = {selectionCount[0]*(selectionCount.length > 1 ? selectionCount[1] : 1)};
    			Object theData = H5Datatype.allocateArray(nativeDatatype, (int)lsize[0]);
    			H5.H5Sselect_hyperslab(fsid,
    					HDF5Constants.H5S_SELECT_SET, selectionStart, selectionStride,
    					selectionCount, null );
    			H5.H5Dread( did, nativeDatatype, msid, fsid, HDF5Constants.H5P_DEFAULT, theData);
    			H5.H5Tclose(tid);
    			H5.H5Tclose(nativeDatatype);
    			H5.H5Sclose(msid);
    			H5.H5Sclose(fsid);
    			ds.close(did);
    			o =  theData;
  			
    		}
    		catch( Exception exc )
    		{
    			throw new IOException(exc.toString());
    		}
    	}
    	return o;
    }
    
    private static void testBEAttr(String fname) throws Exception
    {
        long[] dims2D = {20, 10};

        // retrieve an instance of H5File
        FileFormat fileFormat = FileFormat.getFileFormat(FileFormat.FILE_TYPE_HDF5);

        if (fileFormat == null)
        {
            System.err.println("Cannot find HDF5 FileFormat.");
            return;
        }

        // create a test file.
        H5File testFile = (H5File)fileFormat.create(fname);
        if (testFile == null)
        {
            System.err.println("Failed to create file:"+fname);
            return;
        }
        testFile.open();
        Group root = (Group)((javax.swing.tree.DefaultMutableTreeNode)testFile.getRootNode()).getUserObject();
        int[] dataIn = new int[20*10];
        for (int i=0; i<20; i++)
        {
            for (int j=0; j<10; j++)
            {
                dataIn[i*10+j] = i*100+j;
            }
        }
        Datatype dtype = testFile.createDatatype(
            Datatype.CLASS_INTEGER, 4, Datatype.ORDER_BE, Datatype.NATIVE);
        Dataset dataset = testFile.createScalarDS
            ("2D 32-bit integer 20x10", root, dtype, dims2D, null, null, 0, dataIn);
        testFile.close();

        // open the file with read and write access
        testFile = (H5File)fileFormat.open(fname, FileFormat.WRITE);
        if (testFile == null)
        {
            System.err.println("Failed to open file: "+fname);
            return;
        }

        // open the file and retrieve the file structure
        testFile.open();
        root = (H5Group)((javax.swing.tree.DefaultMutableTreeNode)testFile.getRootNode()).getUserObject();

        // retrieve athe dataset "2D 32-bit integer 20x10"
        dataset = (Dataset)root.getMemberList().get(0);

        // create 2D 32-bit (4 bytes) integer dataset of 20 by 10
        long[] attrDims = {2}; // 1D of size two
        int[] attrValue = {0, 10000}; // attribute value

        // create a attribute of 1D integer of size two
        Attribute attr = new Attribute("data range", dtype, attrDims);
        attr.setValue(attrValue); // set the attribute value

        // attach the attribute to the dataset
        dataset.writeMetadata(attr);

        // read the attribute into memory
        List attrList = dataset.getMetadata();
        attr = (Attribute)attrList.get(0);

        // print out attribute value
        System.out.println( attr.toString() );
        System.out.println( attr.toString("  ") );

        // close file resource
        testFile.close();
    }

    private static void testMemoryLeak(String fname) throws Exception
    {
    	/* a list of objects: char, compound, enum, float32, float64, image,
    	 * int16, int32, int64, int8, str, uchar, uint16, uint32, uint8
    	 */
        H5File testFile = null;
        Dataset dset = null;
        
        testFile = new H5File(fname, H5File.READ);
        
        dset = (Dataset)testFile.get("/char");
        System.out.println(dset);

        testFile.close();
        System.out.println("DONE!!!");

    }
    
    private static void testTofwerkReaderBug1213 (final String filename) throws IOException 
    {
        Object data = null;
        
        String gname = "/TimingData";
        String dname = "/TimingData/BufTimes";
    
        //String gname = "/PeakData";
        //String dname = "/PeakData/PeakData";
       
        // retrieve an instance of H5File
        FileFormat h5FileFormat = FileFormat.getFileFormat(FileFormat.FILE_TYPE_HDF5);
        H5File h5File = null;
        
        if (h5FileFormat == null)
        {
            throw new IOException("Cannot find HDF5 FileFormat.");
        }

        try
        {
            File file = new File(filename);
            System.out.println("Can Read?: " + file.canRead());

            // open the file with read and write access
            h5File = (H5File) h5FileFormat.createInstance(file.getPath(), FileFormat.READ);
            h5File.open();
            if (h5File == null)
            {
                throw new IOException("Failed to open file: "+file.getPath());
            }
           
            // open the file and retrieve the file structure
            Group root = (Group)((javax.swing.tree.DefaultMutableTreeNode)h5File.getRootNode()).getUserObject();
            java.util.List rootMembers = root.getMemberList();
            java.util.List rootAttributes =root.getMetadata();
            
           
            Group timingDataGroup = null;
            
            for(int i = 0; i < rootMembers.size(); i++)
            {
                Group curGrp = (Group)rootMembers.get(i);
                if (curGrp.getFullName().equals(gname))
                {
                    timingDataGroup = curGrp;
                    break;
                }
            }
            
            java.util.List timingMembers = timingDataGroup.getMemberList();
            Dataset dataset = null;
            
            for(int i = 0; i < timingMembers.size(); i++)
            {
                Dataset curSet = (Dataset)timingMembers.get(i);
                if (curSet.getFullName().equals(dname))
                {
                    dataset = curSet;
                    break;
                }
            }
            
            dataset.init();
            long[] start = dataset.getStartDims(); // the off set of the selection
            
            start[0] = 0;
            start[1] = 0;
           
            // crash here
            data = dataset.read();
            h5File.close();
        }
        catch (Exception e)
        {
            throw new IOException("Unhandled exception: " + e + ": " + e.getLocalizedMessage());
        }
        
        System.out.println("Exiting successfully.");
    }    

    private static void testMemoryLeakOpenClose(String fname) throws Exception
    {
        H5File testFile = null;
        
        while(true) {
            testFile = new H5File(fname, H5File.READ);
            testFile.open();
            testFile.getRootNode();
            try { Thread.sleep(100); } catch (Exception ex) {;}
            testFile.close();
        }
    }
    
    private static void testH5Compound2000Fields(final String filename) throws Exception 
    {
        int ncols = 12;
        FileFormat fileFormat = FileFormat.getFileFormat(FileFormat.FILE_TYPE_HDF5);
        H5File file = (H5File) fileFormat.create(filename);
        file.open();
        Group root = (Group) ((javax.swing.tree.DefaultMutableTreeNode) file.getRootNode()).getUserObject();
        
        int[] memRanks = new int[ncols];
        int[][] memDims = new int[ncols][1];

        
        String name = "comp2k";
        long[] dims = {32};
        String[] memberNames = new String[ncols];
        Datatype[] memberDatatypes = new Datatype[ncols];
        int[] memberRanks = new int[ncols];
        int[][] memberDims = new int[ncols][1];
        
        for (int i = 0; i < ncols; i++) {
            memberNames[i] = "m"+i;
            memberDatatypes[i] = new H5Datatype(Datatype.CLASS_INTEGER, 1, -1, -1);
            memberRanks[i] = 1;
            memDims[i][0] = 1;
        }
        
        H5CompoundDS dset = null;
        try {
            dset = (H5CompoundDS)H5CompoundDS.create(
                name, root, dims, memberNames, memberDatatypes, memberRanks, memberDims);
        } catch (final Exception ex) {
            ex.printStackTrace();
        }
        
        file.close();

    }
    
    private static void testH5DreadNIO(final String filename) throws Exception 
    {
        final String dname = "8kx8k";
        final int NLOOPS = 20;

        final H5File file = new H5File(filename, H5File.READ);
        file.open();
        
        H5ScalarDS dset = (H5ScalarDS)file.get(dname);
        dset.init();
        
        int rank = dset.getRank();
        long[] dims = dset.getDims();
        long[] start = dset.getStartDims();
        long[] count = dset.getSelectedDims();
        
        for (int i=0; i<rank; i++) {
            count[i] = dims[i];
            start[i] = 0;
        }
        count[0] = 1;
        
        long t0=0, t1=0, total_time=0;
        for (int i=0; i<dims[0]; i++) {
            start[0] = i;
            
            t0 = System.currentTimeMillis();
            dset.readBytes();
            t1 = System.currentTimeMillis();
            
            System.out.println("Time on reading (Java): "+(t1-t0));
            total_time += (t1-t0);
        }

        System.out.println("Total time on reading (Java): "+total_time);
        System.out.println("Average time on reading (Java): "+ (total_time/(dims[0])));
        
    }
    
    // see bug#1042
    private static void testH5Array(final String filename) throws Exception 
    {
        int array_dims[] = {20};
        int fid = H5.H5Fcreate(filename, HDF5Constants.H5F_ACC_TRUNC, HDF5Constants.H5P_DEFAULT, HDF5Constants.H5P_DEFAULT);
        int sid = H5.H5Screate_simple(2, new long[] {3,2}, null);

        int tid = H5.H5Tarray_create(HDF5Constants.H5T_NATIVE_UCHAR, 1, array_dims, null); 
        int did = H5.H5Dcreate(fid, "/ArrayOfChar", tid, sid, HDF5Constants.H5P_DEFAULT);
        byte buf[] = "this is a test. random charecters: jflda;jfkl;dsajfiewqptfidsjfvkcnvjkhgqjreojfdkla;jfsdatuieqkdkalfjdptueqfjdla;vndasjf".getBytes();
        H5.H5Dwrite(did, tid, HDF5Constants.H5S_ALL, HDF5Constants.H5S_ALL, HDF5Constants.H5P_DEFAULT, buf);
        H5.H5Tclose(tid);
        H5.H5Dclose(did);

        int array_dims2[] = {2, 5};
        tid = H5.H5Tarray_create(HDF5Constants.H5T_NATIVE_INT, 2, array_dims2, null); 
        did = H5.H5Dcreate(fid, "/ArrayOfInt", tid, sid, HDF5Constants.H5P_DEFAULT);
        int buf2[] = {0,1,2,3,4,5,6,7,8,9,10,1,2,3,4,5,6,7,8,9,20,1,2,3,4,5,6,7,8,9,30,1,2,3,4,5,6,7,8,9,40,1,2,3,4,5,6,7,8,9,50,1,2,3,4,5,6,7,8,9};
        H5.H5Dwrite(did, tid, HDF5Constants.H5S_ALL, HDF5Constants.H5S_ALL, HDF5Constants.H5P_DEFAULT, buf2);
        H5.H5Tclose(tid);
        H5.H5Dclose(did);
        
        array_dims[0] = 2;
        tid = H5.H5Tarray_create(HDF5Constants.H5T_C_S1, 1, array_dims, null); 
        H5.H5Tset_size(tid, 50);
        did = H5.H5Dcreate(fid, "/ArrayOfStr", tid, sid, HDF5Constants.H5P_DEFAULT);
        int totalSize = array_dims[0]*50*3*2;
        buf = new byte[totalSize];
        byte tmp[] = "HDF5 is a completely new Hierarchical Data Format product consisting of a data format specification and a supporting library implementation. HDF5 is designed to address some of the limitations of the older HDF product and to address current and anticipated requirements of modern systems and applications".getBytes();
        System.arraycopy(tmp, 0, buf, 0, tmp.length);
        H5.H5Dwrite(did, tid, HDF5Constants.H5S_ALL, HDF5Constants.H5S_ALL, HDF5Constants.H5P_DEFAULT, buf);
        H5.H5Tclose(tid);
        H5.H5Dclose(did);
        
        H5.H5Sclose(sid);
        H5.H5Fclose(fid);
   }
    
    private static void testH5Vlen(final String filename) throws Exception 
    {
        String buf[] = {"Parting", "is such", "sweet", "sorrow."};
        
        // Case 1, may run into infinite loop
        // int tid = H5.H5Tvlen_create(HDF5Constants.H5T_C_S1);
         
        // Case 2, differnt failure on differnt platforms
        int tid = H5.H5Tcopy(HDF5Constants.H5T_C_S1);
        H5.H5Tset_size(tid, HDF5Constants.H5T_VARIABLE);
        
        int fid = H5.H5Fcreate(filename, HDF5Constants.H5F_ACC_TRUNC, HDF5Constants.H5P_DEFAULT, HDF5Constants.H5P_DEFAULT);
        int sid = H5.H5Screate_simple(1, new long[] {4}, null);
        int did = H5.H5Dcreate(fid, "/str", tid, sid, HDF5Constants.H5P_DEFAULT);
        
        // write() fails on both case 1 and 2
        H5.H5Dwrite(did, tid, HDF5Constants.H5S_ALL, HDF5Constants.H5S_ALL, HDF5Constants.H5P_DEFAULT, buf);
        
        // clean up
        H5.H5Dclose(did);
        H5.H5Sclose(sid);
        H5.H5Tclose(tid);
        H5.H5Fclose(fid);
    }
    
    private static void testH5VlenObj(final String fname) throws Exception 
    {
        int strLen = -1;
        long[] dims = {4};
        String buf[] = {"Parting", "is such", "sweet", "sorrow."};
        
        // create a new file with a given file name.
        H5File testFile = new H5File(fname, H5File.CREATE);

        testFile.open();
        Group root = (Group)((javax.swing.tree.DefaultMutableTreeNode)testFile.getRootNode()).getUserObject();
        Datatype dtype = testFile.createDatatype(Datatype.CLASS_STRING, strLen, Datatype.NATIVE, Datatype.NATIVE);
        Dataset dataset = testFile.createScalarDS ("/str", root, dtype, dims, null, null, 0, buf);

        testFile.close();
     }    
    
    private static void testH5WriteFloats(final String filename) throws Exception 
    {
        FileFormat fileFormat = FileFormat.getFileFormat(FileFormat.FILE_TYPE_HDF5);
        H5File file = (H5File) fileFormat.create(filename);
        file.open();
        Group root = (Group) ((javax.swing.tree.DefaultMutableTreeNode) file.getRootNode()).getUserObject();
        Group group1 = file.createGroup("a", root);
        Group group2 = file.createGroup("b", group1);
        Datatype dtype = file.createDatatype(Datatype.CLASS_FLOAT, 4, Datatype.NATIVE, Datatype.NATIVE);

        // write a subset of the dataset
        long[] dims = {2, 3};
        Dataset dataset = file.createScalarDS("c", group2, dtype, dims, null, null, 0, null);
        
        dataset.init();
        long[] count = dataset.getSelectedDims();
        float[] data = new float[(int) (dims[0])];

        count[0] = dims[0];
        count[1] =1;
        
        for (long i = 0; i < dims[1]; i++) {
            for (int j = 0; j < data.length; j++) {
                data[j] = 1.0f + i * j + j;
            }
            dataset.write(data);
        }

        /* write the whole dataset
        long[] dims = {2, 3};
        float[] data = new float[(int) (dims[0]*dims[1])];
        Dataset dataset = file.createScalarDS("c", group2, dtype, dims, null, null, 0, null);

        for (int i = 0; i < data.length; i++) {
            data[i] = 1.0f + i * 10;
        }
        dataset.write(data);
        */
        
        file.close();
    }
    
    private static void testH5WriteDouble(final String filename) throws Exception 
    {
    	double[] data = new double[100];
        H5File file = new H5File(filename, H5File.CREATE);
        file.open();
        
        for (int i=0; i<data.length; i++)
        	data[i] = Math.random();
        
        long[] dims = {data.length};
        Datatype dtype = file.createDatatype(Datatype.CLASS_FLOAT, 8, Datatype.NATIVE, Datatype.NATIVE);

        Dataset dataset = file.createScalarDS("dset", null, dtype, dims, null, null, 0, null);
        dataset.init();
        dataset.write(data);

        file.close();
    }
    
    private static void testH5ReadPerf(final String filename) throws Exception 
    {
        final String dname = "8kx8k";
        final int NLOOPS = 20;

        final H5File file = new H5File(filename, H5File.READ);
        file.open();
        
        Dataset dset = (Dataset)file.get(dname);
        dset.init();
        
        int rank = dset.getRank();
        long[] dims = dset.getDims();
        long[] start = dset.getStartDims();
        long[] count = dset.getSelectedDims();
        
        for (int i=0; i<rank; i++) {
            count[i] = dims[i];
            start[i] = 0;
        }
        count[0] = 1;
        
        long t0=0, t1=0, total_time=0;
        
        for (int loop=0; loop<NLOOPS; loop++) {
            for (int i=0; i<dims[0]; i++) {
                start[0] = i;
                
                t0 = System.currentTimeMillis();
                dset.read();
                t1 = System.currentTimeMillis();
                
                System.out.println("Time on reading (Java): "+(t1-t0));
                total_time += (t1-t0);
            }
        }
        System.out.println("Total time on reading (Java): "+total_time);
        System.out.println("Average time on reading (Java): "+ (total_time/(NLOOPS*dims[0])));
        
    }

    /**
     *  Test converting the following unsigned values to signed values.
     *  <ul>
     *    <li> byte[] int8 = {-1, -128, 127, 0};
     *    <li> short[] int16 = {-1, -32768, 32767, 0};
     *    <li> int[] int32 = {-1, -2147483648, 2147483647, 0};
     * </ul>
     * Expected values
     *  <ul>
     *    <li> short[] uint8 = {255, 128, 127, 0};
     *    <li> int[] uint16 = {65535, 32768, 32767, 0};
     *    <li> long[] uint32 = {4294967295L, 2147483648L, 2147483647, 0};
     * </ul>
     */
    public final static void testConvertFromUnsignedC() {
        byte[] int8 = {-1, -128, 127, 0};
        short[] int16 = {-1, -32768, 32767, 0};
        int[] int32 = {-1, -2147483648, 2147483647, 0};
        
        short[] uint8 = {255, 128, 127, 0};
        int[] uint16 = {65535, 32768, 32767, 0};
        long[] uint32 = {4294967295L, 2147483648L, 2147483647, 0};

        short[] expected8 = (short[])Dataset.convertFromUnsignedC(int8, null);
        if (!(Arrays.equals(expected8, uint8))) {
            System.out.println("testConvertFromUnsignedC failed.");
        }

        int[] expected16 = (int[])Dataset.convertFromUnsignedC(int16, null);
        if (!(Arrays.equals(expected16, uint16))) {
            System.out.println("testConvertFromUnsignedC failed.");
        }

        long[] expected32 = (long[])Dataset.convertFromUnsignedC(int32, null);
            if (!(Arrays.equals(expected32, uint32))) {
                System.out.println("testConvertFromUnsignedC failed.");
            }
   }
    
    private static void testH5ReadChunk(final String filename) throws Exception 
    {
        final String dnames[] = { "chunk1000x1000", "chunk100x1000", "chunk1x1000", "chunk50x50", "nochunk"};

        final H5File file = new H5File(filename, H5File.READ);
        file.open();
        
        for (int i=0; i<dnames.length; i++) {
            final Dataset dset = (Dataset)file.get(dnames[i]);
            final long t0 = System.currentTimeMillis();
            final float[] fdata = (float[]) dset.getData();
            final long t1 = System.currentTimeMillis();
            dset.clear();
            System.out.println("Time on reading "+dnames[i]+" = "+ (t1-t0) +"ms");
        }
      }
    
    private static void testH5Bug863(final String filename) throws Exception 
    {
        //H5File file = new H5File(filename, H5File.READ);
        final int nloops = 1000000;
        
        while (true)
        {
            final H5File file = new H5File(filename, H5File.READ);
            //file.open();
            final Dataset dset = (Dataset)file.get("/Table0");
            dset.init();

            final int n = H5.H5Fget_obj_count(file.getFID(), HDF5Constants.H5F_OBJ_ALL);
            if (n>1) {
                System.out.println("*** Possible memory leak!!!");
            }

            file.close();
        }
    }
    
    private static void testH5Bug847(final String filename)  throws Exception 
    {
        List list=null;
        final int TEST_INT_VALUE = 999999999;
        long[] count, start, dims;
        int rank, nmembers, nrows=1;
        H5File file;
        CompoundDS dset;
        String NAME_DATASET_COMPOUND = "/comp_dataset";
        
         
        // create a test file
        create_test_file(filename);
        
        for (int rowIdx=0; rowIdx<nrows; rowIdx++) {
            // open the test file
            file = new H5File(filename, H5File.WRITE);
            file.open();

            // retrieve the compound dataset
            dset = (CompoundDS)file.get(NAME_DATASET_COMPOUND);
            dset.init();
            
            // get dataspace information
            rank = dset.getRank();
            count = dset.getSelectedDims();
            start = dset.getStartDims();
            dims = dset.getDims();
            nmembers = dset.getMemberCount();
            nrows = (int)dims[0];
            
            // select one row only
            for (int i=0; i<rank; i++) {
                count[i] = 1;
            }
            
            // select different rows
            start[0] = rowIdx;
            
            // 1)  read the table cell (using dataset selection to select only that row of the table)
            list = (List)dset.read();
           
            System.out.println(dset.getFullName() +",\tstart index = "+start[0]);
            for (int i=0; i<nmembers; i++) {
                System.out.print(Array.get(list.get(i), 0)+",\t");
            }
            System.out.println("\n");
            
            // 2)  re-initialize the Dataset
            dset.init();
            
            // 3)  call 'Dataset.clearData()'
            dset.clearData();
     
            // 4)  call 'Dataset.getData()'
            list = (List)dset.read();
            
            // 5)  change the correct column/row **, col0/row0
            final int[] read_row_data = (int []) list.get(0);
            // since only one row is selected, the data idex is always zero
            // it will fail if using read_row_data[rwoIdx] = TEST_INT_VALUE
            read_row_data[rowIdx] = TEST_INT_VALUE; 
             
            // 6)  call 'Dataset.write()'
            dset.write(list);
            
            // 7)  close the file
            file.close();
           
            // 8)  reopen the file and read the table cell as in step 1
            file.open();
            
            // 9)  assert that the value has been changed and is correct
            dset = (CompoundDS)file.get(NAME_DATASET_COMPOUND);
            dset.init();
            rank = dset.getRank();
            count = dset.getSelectedDims();
            start = dset.getStartDims();
            dims = dset.getDims();
            nmembers = dset.getMemberCount();
            for (int i=0; i<rank; i++) {
                start[i] = 0;
                count[i] = 1;
            }
            list = (List)dset.read();
            
             for (int i=0; i<nmembers; i++) {
                System.out.print(Array.get(list.get(i), 0)+",\t");
            }
            System.out.println("\n");
            
            System.out.println(dset.getFullName() +",\tstart index = "+start[0]);
            final int[] write_row_data = (int[]) list.get(0);
            if (write_row_data[0] == TEST_INT_VALUE) {
                System.out.println("data in file is correct");
            } else {
                System.out.println("data in file is incorrect");
            }

            file.close();
        }
    }

    private static void testCreateLongPath(final String fname) throws Exception {
        final int n = 5;
        H5File file=null;
        final Group g;
        Group parent;
        
        file = new H5File(fname, H5File.CREATE);
        file.open();
        parent = (Group)file.get("/");
        
        for (int i=0; i<n; i++) {
            parent = (Group)file.createGroup("group level "+i, parent);
        }
        
        // create 1D string compound dataset using hdf-java 2.4
        final String dset_name = "1D compound Strings looooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooong name";
        final int size = 100;
        final long dims[] = {size};
        final long chunks[] = {10};
        final int gzip = -1;
         final String strs[] = new String[size];
        for (int i=0; i<size; i++) {
            strs[i] = "";
        }
        final int max_str_len = 120;
        final Datatype strType = new H5Datatype(Datatype.CLASS_STRING, max_str_len, -1, -1);
        final Datatype[]  mdtypes = {strType};
        final String[] mnames = {"strings"};
        final Vector comp_data = new Vector();
        comp_data.add(strs);
        file.createCompoundDS(dset_name, null, dims, null, chunks, 
                gzip, mnames, mdtypes, null, comp_data);
        file.createCompoundDS(dset_name, parent, dims, null, chunks, 
                gzip, mnames, mdtypes, null, comp_data);
        H5.H5Glink(parent.open(), HDF5Constants.H5G_LINK_SOFT, dset_name, "/soft_link");

        try { file.close(); } catch (final Exception ex) {}
     }
    
    private static void testCompressedStrings(final String fname) throws Exception {
        H5File file=null;
        
        file = new H5File(fname, H5File.CREATE);
        file.open();
        
        final int max_str_len = 120;
        final Datatype strType = new H5Datatype(Datatype.CLASS_STRING, max_str_len, -1, -1);
        
        final int size = 10000;
        final long dims[] = {size};
        final long chunks[] = {1000};
        final int gzip = 9;
        
        final String strs[] = new String[size];
        for (int i=0; i<size; i++) {
            strs[i] = String.valueOf(i);
        }
        
        // set compound fields
        final Datatype[]  mdtypes = {strType};
        final String[] mnames = {"strings"};
        final Vector comp_data = new Vector();
        comp_data.add(strs);
        
        // create 1D string compound dataset using hdf-java 2.3
        final int mtid = strType.toNative();
        final int tsize = H5.H5Tget_size(mtid);
        final int tid = H5.H5Tcreate(HDF5Constants.H5T_COMPOUND, tsize);
        H5.H5Tinsert(tid, mnames[0], 0, mtid);
        final int sid = H5.H5Screate_simple(1, dims, null);
        int plist = HDF5Constants.H5P_DEFAULT;
        if (chunks != null)
        {
            plist = H5.H5Pcreate (HDF5Constants.H5P_DATASET_CREATE);
            H5.H5Pset_layout(plist, HDF5Constants.H5D_CHUNKED);
            H5.H5Pset_chunk(plist, 1, chunks);
            if (gzip > 0) {
                H5.H5Pset_deflate(plist, gzip);
            }
        }
        final int did = H5.H5Dcreate(file.getFID(), "/1D compound Strings", tid, sid, plist);
        try {H5.H5Pclose(plist);} catch (final Exception ex) {};
        try {H5.H5Tclose(tid);} catch (final Exception ex) {};
        try {H5.H5Sclose(sid);} catch (final Exception ex) {};
        try {H5.H5Dclose(did);} catch (final Exception ex) {};
        final H5CompoundDS dset = new H5CompoundDS(file, "1D compound Strings", "/", null);
        dset.init();
        final long selected[] = dset.getSelectedDims();
        selected[0] = dims[0];
        dset.write(comp_data);
        
        // create 1D string compound dataset using hdf-java 2.4
        file.createCompoundDS("/1D compound Strings2", null, dims, null, chunks, 
                gzip, mnames, mdtypes, null, comp_data);
        
        // create 1D string scalar dataset using using hdf-java 2.3 or 2.4
        file.createScalarDS("/1D scalar strings", null, strType, dims, null, chunks, 
                gzip, strs);

        try { file.close(); } catch (final Exception ex) {}
    }
    
    private static void collectGarbage() {
        try {
            System.gc();
            Thread.sleep(15);
            System.runFinalization();
            Thread.sleep(15);
        }
        catch (final Exception ex){
            ex.printStackTrace();
        }
    }
    
    public static void checkMemory() throws Exception {
       final int _SIZE = 5000000;
       
       final Object[] array = new Object[_SIZE];
       collectGarbage();
       long totalMem = Runtime.getRuntime().totalMemory();
       for (int i = 0; i < _SIZE; i++) {
           array[i] = new Object();
       }
       long freeMem = Runtime.getRuntime().freeMemory();
       long difference = ( totalMem - freeMem ) / _SIZE;
       System.out.println( difference + " \tbytes/object" );

       collectGarbage();
       totalMem = Runtime.getRuntime().totalMemory();
       for (int i = 0; i < _SIZE; i++) {
           array[i] = new String(String.valueOf(i));
       }
       freeMem = Runtime.getRuntime().freeMemory();
       difference = ( totalMem - freeMem ) / _SIZE;
       System.out.println( difference + " \tbytes/String" );
    }
    
    private static void testH5OpenClose(final String filename)  throws Exception 
    {
        int loop = 1000000;
        
        create_test_file(filename);
        
        final H5File file = new H5File(filename, H5File.READ);
        
        while (loop-- > 0) {
            file.open();
            file.close();
        }
    }
    
    private static void testGetOneRow (final String filename, final String objName) throws Exception
    {
        List data=null;
        
        // Get the source dataset
        final H5File file = new H5File(filename, H5File.READ);
        file.open();

        final CompoundDS dset = (CompoundDS)file.get(objName);
        
        int rank = dset.getRank();
        try { if (rank<=0) {
            dset.init();
        } } catch (final Exception ex) {}
        rank = dset.getRank(); 

        
        // 1)  I read a table from an H5 file; and use the 'select subset' code 
        //     to get only one row's worth of data before calling 'getData()
        final long[] count = dset.getSelectedDims();
        final long[] start = dset.getStartDims();
        final long[] dims = dset.getDims();
        for (int i=0; i<rank; i++) {
            start[i] = 0; // start the third data point
            count[i] = 1; // select only one row (the third row)
        }
        
        final int n = dset.getMemberCount();
        for (int s=0; s<dims[0]; s++) {
            start[0] = s;
            
            // 2)  I call 'Dataset.init()' to clear the selection
            dset.init();
            
            // 3)  I call 'Dataset.clearData()' to clear the file data from memory
            dset.clearData();
            
            // 4)  I call 'Dataset.getData()' to get the entire table's worth of data
            try { data = (List)dset.read(); }
            catch (final Exception ex) { ex.printStackTrace();}
            
            System.out.println(dset.getFullName() +",\tstart index = "+s);
            for (int i=0; i<n; i++) {
                System.out.print(Array.get(data.get(i), 0)+",\t");
            }
            System.out.println("\n");
        }
 
        file.close();
    }    

    private static void testFillValue(final String fname) throws Exception
    {
    	final int[] fill_int = {9999};
    	final float[] fill_float = {9999.99f};
    	final long[] dims = {20, 10};

    	int fid = H5.H5Fcreate(fname, HDF5Constants.H5F_ACC_TRUNC, HDF5Constants.H5P_DEFAULT, HDF5Constants.H5P_DEFAULT);
    	final int sid = H5.H5Screate_simple(2, dims, null);
    	
    	int plist = H5.H5Pcreate(HDF5Constants.H5P_DATASET_CREATE);
    	H5.H5Pset_fill_value(plist, HDF5Constants.H5T_NATIVE_INT, fill_int);
    	int did = H5.H5Dcreate(fid, "/int", HDF5Constants.H5T_NATIVE_INT, sid, plist );
    	H5.H5Dclose(did);
    	H5.H5Pclose(plist);

    	plist = H5.H5Pcreate(HDF5Constants.H5P_DATASET_CREATE);
    	H5.H5Pset_fill_value(plist, HDF5Constants.H5T_NATIVE_FLOAT, fill_float);
    	did = H5.H5Dcreate(fid, "/float", HDF5Constants.H5T_NATIVE_FLOAT, sid, plist );
    	H5.H5Dclose(did);
    	H5.H5Pclose(plist);
    	
    	H5.H5Fclose(fid);
    	
    	// reopen the file and check the fill value
    	final int[] fill_int_read = {0};
    	final float[] fill_float_read = {0f};
    	
    	fid = H5.H5Fopen(fname, HDF5Constants.H5F_ACC_RDONLY, HDF5Constants.H5P_DEFAULT);
    	did = H5.H5Dopen(fid,"/int");
    	plist = H5.H5Dget_create_plist(did);
    	H5.H5Pget_fill_value(plist, HDF5Constants.H5T_NATIVE_INT, fill_int_read);
    	if (fill_int_read[0] == fill_int[0]) {
            System.out.println("Correct fill value: "+fill_int_read[0]);
        } else {
            System.out.println("Incorrect fill value: "+fill_int_read[0]);
        }
    	H5.H5Dclose(did);
    	H5.H5Pclose(plist);
    	
    	did = H5.H5Dopen(fid,"/float");
    	plist = H5.H5Dget_create_plist(did);
    	H5.H5Pget_fill_value(plist, HDF5Constants.H5T_NATIVE_FLOAT, fill_float_read);
    	if (fill_float_read[0] == fill_float[0]) {
            System.out.println("Correct fill value: "+fill_float_read[0]);
        } else {
            System.out.println("Incorrect fill value: "+fill_float_read[0]);
        }
    	H5.H5Dclose(did);
    	H5.H5Pclose(plist);

    	H5.H5Fclose(fid);	
    }
    
    private static void testGetObjID() throws Exception
    {
        final HashMap typeMap = new HashMap();
        typeMap.put(new Integer(HDF5Constants.H5T_INTEGER), "integer");
        typeMap.put(new Integer(HDF5Constants.H5T_FLOAT), "float");
        typeMap.put(new Integer(HDF5Constants.H5T_TIME), "time");
        typeMap.put(new Integer(HDF5Constants.H5T_STRING), "string");
        typeMap.put(new Integer(HDF5Constants.H5T_BITFIELD), "bitfield");
        typeMap.put(new Integer(HDF5Constants.H5T_OPAQUE), "opaque");
        typeMap.put(new Integer(HDF5Constants.H5T_COMPOUND), "compound");
        typeMap.put(new Integer(HDF5Constants.H5T_REFERENCE), "reference");
        typeMap.put(new Integer(HDF5Constants.H5T_ENUM), "enum");
        typeMap.put(new Integer(HDF5Constants.H5T_VLEN), "vlen");
        typeMap.put(new Integer(HDF5Constants.H5T_ARRAY), "array");


        // get number of open objects
        final int numDatatypes = H5.H5Fget_obj_count(HDF5Constants.H5F_OBJ_ALL, HDF5Constants.H5F_OBJ_DATATYPE);
        System.out.println("The num of datatypes open: " + numDatatypes);

        // get open object handles
        final int[] objIds = new int[numDatatypes];
        H5.H5Fget_obj_ids(HDF5Constants.H5F_OBJ_ALL, HDF5Constants.H5F_OBJ_DATATYPE, numDatatypes, objIds);


        // for each open object, look up the type in the hash map and print out
        for (int i = 0; i < numDatatypes; i++) {
            final int typeClass = H5.H5Tget_class(objIds[i]);
            System.out.println(typeMap.get( new Integer(typeClass)));
            // above line prints integer, float, etc. in addition to compound,
        }
    }

    private static void testHDF5Copy (final String filename, final String objName) throws Exception
    {
        String newFilename = filename+"_copy.h5";

        // Get the source dataset
        H5File file = new H5File(filename, H5File.READ);
        HObject srcObj = file.get(objName);

        // Create a new file
        H5File newFile = (H5File) file.create(newFilename);
        Group rootGroup = (Group)newFile.get("/");
        newFile.createGroup("/grp", rootGroup);
        newFile.open();
        
        // copy to the root group, with the same name and different name
        Group group = (Group)newFile.get("/");
        newFile.copy(srcObj, group);
  
        
        // copy to a group
        //group = (Group)newFile.get("/grp"); // v2.3 bug at get() sub-groups
        group = (Group)group.getMemberList().get(0);
        newFile.copy(srcObj, group);
        
        file.close();
        newFile.close();
    }

    private static void testHDF5Get (final String filename) throws Exception
    {
        final H5File file = new H5File(filename, H5File.READ);
        final Group group = (Group)file.get("/Group0");
        System.out.println(group);
        file.close();
    }

    private static void testHDF5Misc (final String filename) throws Exception
    {
        final ncsa.hdf.object.h5.H5File file = new ncsa.hdf.object.h5.H5File(filename, ncsa.hdf.object.h5.H5File.READ);
        testGetMemberList(file);
        testGetPath(file);
        testIsRoot(file);
    }

    /**
     * This is a bug.    The objects that are returned by the
     * 'Group.getMemberList()' method don't appear to be initialized
     * properly.    The objects returned are typed correctly (the list
     * returned seems to contain 'Group', 'CompoundDS', or 'Dataset' objects),
     * but not all the methods available for these classes seem
     * to work propertly.
     */
    private static void testGetMemberList (final ncsa.hdf.object.h5.H5File file) throws Exception
    {
        final Group group = (Group)file.get("/");
        System.out.println("\nContents of file <doesn't work!>:");
        printHObject(group);
    }

    private static void printHObject (final HObject hObject) throws Exception
    {
        if (hObject instanceof Group) {
            printHObject((Group)hObject);
        }
        if (hObject instanceof CompoundDS) {
            printHObject((CompoundDS)hObject);
        }
        if (hObject instanceof Dataset) {
            printHObject((Dataset)hObject);
        }
    }

    private static void printHObject (final Group group) throws Exception
    {
        final List list = group.getMemberList();

        if (list == null) {
            return;
        }

        final int n = list.size();
        for (int i=0; i<n; i++)
        {
            final HObject hObject = (HObject) list.get(i);
            System.out.println(
                hObject.getPath() + hObject.getName() + " : " + hObject.getClass().getName());
            try
            {
                printHObject(hObject);
            }
            catch (final Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    private static void printHObject (final CompoundDS compoundDS) throws Exception
    {
        System.out.println(compoundDS.getData());
    }

    private static void printHObject (final Dataset dataset) throws Exception
    {
        System.out.println(dataset.getData());
    }

    /**
     * This is probably not a bug, but is inconvenient.    Normally, if you
     * have an HObject, you could use 'getPath() + getName()' to obtain
     * the full path, but the root HObject is a special case because 'getPath()'
     * returns a null object, rather than an empty string.    So, you have
     * to check if your HObject is the root.    See next method!
     */
    private static void testGetPath (final ncsa.hdf.object.h5.H5File file) throws Exception
    {
        final HObject hObject = file.get("/");
        System.out.println("Next line should print '':");
        System.out.println(hObject.getPath());
        System.out.println(hObject.getName());
    }

    /**
     * This is a bug.    The 'Group.isRoot()' operation operation seems
     * to return 'true' no matter what...
     */
    private static void testIsRoot (final ncsa.hdf.object.h5.H5File file) throws Exception
    {
        Group group = null;

        group = (Group)file.get("/");
        System.out.println("Next line should print 'true':");
        System.out.println(group.isRoot());

        group = (Group)file.get("/Group0");
        System.out.println("Next line should print 'false':");
        System.out.println(group.isRoot());

        group = (Group)file.get("/Group0/SubGroup0");
        System.out.println("Next line should print 'false':");
        System.out.println(group.isRoot());
    }

    public static void  testHDF5Write(final String filename)
    {
        try
        {
            final ncsa.hdf.object.h5.H5File file = new ncsa.hdf.object.h5.H5File(filename, ncsa.hdf.object.h5.H5File.WRITE);
            Dataset ds;


            // retrieve the dataset
            ds = (Dataset)file.get("/Dataset0");
            // init the dataset
            ds.init();
            String[] stringArray = (String[])ds.getData();
            System.out.println("Dataset0 == " + stringArray[0]);
            stringArray[0] += "!";
            // write the dataset to the file
            ds.write();
            // reopen the file
            file.close();
            file.open();
            // reread the dataset
            ds = (Dataset)file.get("/Dataset0");
            stringArray = (String[])ds.getData();
            System.out.println("Dataset0 == " + stringArray[0]);

            // retrieve the dataset
            ds = (Dataset)file.get("/Table0");
            // init the dataset
            ds.init();
            java.util.List list1 = (java.util.List)ds.getData();
            int[] intArray = (int[])list1.get(1);
            System.out.println("Member1 == " + intArray[0]);
            intArray[0]++;
            // write the dataset to the file
            ds.write();
            // reopen the file
            file.close();
            file.open();
            // reread the dataset
            ds = (ncsa.hdf.object.h5.H5CompoundDS)file.get("/Table0");
            list1 = (java.util.List)ds.getData();
            intArray = (int[])list1.get(1);
            System.out.println("Member1 == " + intArray[0]);
        }
        catch (final Exception e)
        {
            e.printStackTrace();
        }
    }

    public static void  testHDF5OpenClose () throws Exception
    {
        final H5File file = new H5File("E:\\hdf-files\\TestHDF5OpenClose.h5", H5File.READ);
        file.get("/Group0");
        file.close();
    }

    private static void testEnum(final String fileName) throws Exception
    {
        final int booleanEnum = H5.H5Tenum_create(HDF5Constants.H5T_STD_I8LE);
        int status = H5.H5Tenum_insert(booleanEnum, "true", new int[] {1});
        status = H5.H5Tenum_insert(booleanEnum, "false", new int[] {0});

        System.out.println(H5.H5Tget_member_name(booleanEnum, 0));
        System.out.println(H5.H5Tget_member_name(booleanEnum, 1));
    }

//    private static void testSDgetchunkinfo(final String fileName) throws Exception
//    {
//        final FileFormat fileFormat = FileFormat.getFileFormat(FileFormat.FILE_TYPE_HDF4);
//        if (fileFormat == null)
//        {
//             System.err.println("Cannot find HDF5 FileFormat.");
//             return;
//        }
//
//        // create a new file with a given file name.
//        final H4File testFile = (H4File)fileFormat.open(fileName, FileFormat.READ);
//        if (testFile == null)
//        {
//             System.err.println("Failed to open file: "+fileName);
//             return;
//        }
//
//        // retrieve the file structure
//        testFile.open();
//        final Group root = (Group)((javax.swing.tree.DefaultMutableTreeNode)testFile.getRootNode()).getUserObject();
//        final Dataset d = (Dataset)root.getMemberList().get(1);
//        if (d != null) {
//            System.out.println(d.getName());
//        } else
//        {
//             System.err.println("No such dataset in file: "+fileName);
//             testFile.close();
//             return;
//        }
//        final int did = d.open();
//        final HDFChunkInfo chunkInfo = new HDFChunkInfo();
//        final int[] cflag = {HDFConstants.HDF_NONE};
//        final boolean status = HDFLibrary.SDgetchunkinfo(did, chunkInfo, cflag);
//
//        if (status) {
//            System.out.println("Calling SDgetchunkinfo() is OK");
//        } else {
//            System.out.println("Calling SDgetchunkinfo() FAILED");
//        }
//
//        d.close(did);
//        testFile.close();
//    }

    private static void testSizeof () throws Exception
    {
        // Warm up all classes/methods we will use
        runGC ();
        usedMemory ();
        String bigstr = "";

        // Array to keep strong references to allocated objects
        final int count = 100000;
        final int strlen = 15;
        Object [] objects = new Object [count];
        final byte[] bytes= new byte[count*strlen];

        long heap1 = 0;
        int n = count*strlen;
        for (int i = 0; i < n; ++ i)
        {
            bytes[i] = (byte)(Math.random()*25.0 + 65);
        }

        bigstr = new String(bytes);

        // Allocate count+1 objects, discard the first one
        for (int i = -1; i < count; ++ i)
        {
            Object object = null;

            // Instantiate your data here and assign it to object
            //object = new Object ();
            //object = new Integer (i);
            //object = new Long (i);
            if (i < 0) {
                object = new String();
            } else
            {
                object = new String (bytes, i*strlen, strlen);
                n = i*strlen;

                // use less memory
                //object = bigstr.substring (n, n+strlen).trim();
            }

            if (i >= 0) {
                objects [i] = object;
            }
            else
            {
                object = null; // Discard the warm up object
                runGC ();
                heap1 = usedMemory (); // Take a before heap snapshot
            }
        }

        runGC ();
        final long heap2 = usedMemory (); // Take an after heap snapshot:

        final int size = Math.round (((float)(heap2 - heap1))/count);
        System.out.println ("'before' heap: " + heap1 +
                            ", 'after' heap: " + heap2);
        System.out.println ("heap delta: " + (heap2 - heap1) +
            ", {" + objects [0].getClass () + "} size = " + size + " bytes");

        for (int i = 0; i < count; ++ i) {
            objects [i] = null;
        }
        objects = null;
    }

    private static void runGC () throws Exception
    {
        // It helps to call Runtime.gc()
        // using several method calls:
        for (int r = 0; r < 4; ++ r) {
            _runGC ();
        }
    }

    private static void _runGC () throws Exception
    {
        long usedMem1 = usedMemory (), usedMem2 = Long.MAX_VALUE;
        final Runtime s_runtime = Runtime.getRuntime ();
        
        for (int i = 0; (usedMem1 < usedMem2) && (i < 500); ++ i)
        {
            s_runtime.runFinalization ();
            s_runtime.gc ();
            Thread.yield ();

            usedMem2 = usedMem1;
            usedMem1 = usedMemory ();
        }
    }

    private static long usedMemory ()
    {
        final Runtime s_runtime = Runtime.getRuntime ();
        return s_runtime.totalMemory () - s_runtime.freeMemory ();
    }

    private static void testHDFvector( final String fileName ) throws Exception
    {

        // retrieve an instance of H5File
        final FileFormat fileFormat = FileFormat.getFileFormat(FileFormat.FILE_TYPE_HDF5);
        if (fileFormat == null)
        {
             System.err.println("Cannot find HDF5 FileFormat.");
             return;
        }

        // create a new file with a given file name.
        final H5File tFile = (H5File)fileFormat.create(fileName);
        if (tFile == null)
        {
             System.err.println("Failed to create file:"+fileName);
             return;
        }

        final FileFormat testFile = fileFormat.open(fileName,FileFormat.WRITE);
        if(testFile == null){
          System.err.println("Failed to open file " + fileName);
          return;
     }
     testFile.open();
     final Group root = (Group)((javax.swing.tree.DefaultMutableTreeNode)testFile.getRootNode()).getUserObject();

    Vector v = new Vector();
    v.addElement(new Float(1.2));
    v.addElement(new Float(2.3));
    v.addElement(new Float(4.5));

    putData(testFile, root, "Vector Dataset",v,true, null);
    v = getData(root, "Vector Dataset");
    System.out.println("first time getData " + v);

    //adding five more elements tothe vector
    v.addElement(new Float(4));
    v.addElement(new Float(5));
    v.addElement(new Float(6));
    v.addElement(new Float(7));
    final long[] extended_dims = {7};
    putData(testFile, root, "Vector Dataset",v,false, extended_dims);
    System.out.println("After adding four more elements to the vector");

    v = getData(root, "Vector Dataset");
    System.out.println("Second time getData " + v);
    testFile.close();

    }

    public static void putData(final FileFormat testFile, final Group root, final String name,
        final Vector value, final boolean flag, final long[] extended_dims)throws Exception
    {
        final int size = value.size();
        final long[] dims = {size};
        final int dataLen = 0;
        // set the data values
        final float[] dataFl = new float[size];
        for (int i=0; i<size; i++){
            dataFl[i] =  new Float((value.elementAt(i)).toString()).floatValue();
        }

        if(flag == true ) {
            // create Vector dataset
            final Datatype dtype = testFile.createDatatype(
                        Datatype.CLASS_FLOAT,
                        Datatype.NATIVE,
                        Datatype.NATIVE,
                        Datatype.NATIVE);

           final long[] maxdims = {HDF5Constants.H5S_UNLIMITED};
           final Dataset dataset = testFile.createScalarDS(
                  name, root, dtype, dims, maxdims, null, 0, dataFl);

           dataset.write(dataFl);

       } else{
           final Dataset dset = (Dataset)root.getMemberList().get(0);
           final int did = dset.open();
           final int status = H5.H5Dextend(did, extended_dims);
           dset.close(did);
           dset.write(dataFl);
       }
    }

    public static Vector getData(final Group root, final String name )throws Exception
    {
        final Vector v = new Vector();
        // retrieve athe dataset " vector dataset "
        final Dataset dataset = (Dataset)root.getMemberList().get(0);
        dataset.init(); // reset the data selection
        final float[] dataRead = (float[])dataset.read();
        // print out the data values
        for(int i=0; i<dataRead.length; i++){
           v.add(new Float(dataRead[i]));
           System.out.println(v);
        }
        return v;
    }

    private static void testHDFgenotype(final String fileName) throws Exception {
        // retrieve an instance of H5File

        final FileFormat fileFormat = FileFormat.getFileFormat(FileFormat.FILE_TYPE_HDF5);

        if (fileFormat == null)
        {
            System.err.println("Cannot find HDF5 FileFormat.");
            return;
        }

        final FileFormat testFile = (H5File)fileFormat.create(fileName);

        if (testFile == null)
        {
            System.err.println("Failed to open file: "+fileName);
            return;
        }

        testFile.open();
        final Group root = (Group)((javax.swing.tree.DefaultMutableTreeNode)testFile.getRootNode()).getUserObject();

        final long[] dims = {52636};
        final String[] memberNames = {
                                "rs#",
                                "SNPalleles",
                                "chrom",
                                "pos",
                                "strand",
                                "genome_build",
                                "center",
                                "protLSID",
                                "assayLSID",
                                "panelLSID",
                                "QC_code",
                                "NA06985",
                                "NA06991",
                                "NA06993",
                                "NA06994",
                                "NA07000",
                                "NA07019",
                                "NA07022",
                                "NA07029",
                                "NA07034",
                                "NA07048",
                                "NA07055",
                                "NA07056",
                                "NA07345",
                                "NA07348",
                                "NA07357",
                                "NA10830",
                                "NA10831",
                                "NA10835",
                                "NA10838",
                                "NA10839",
                                "NA10846",
                                "NA10847",
                                "NA10851",
                                "NA10854",
                                "NA10855",
                                "NA10856",
                                "NA10857",
                                "NA10859",
                                "NA10860",
                                "NA10861",
                                "NA10863",
                                "NA11829",
                                "NA11830",
                                "NA11831",
                                "NA11832",
                                "NA11839",
                                "NA11840",
                                "NA11881",
                                "NA11882",
                                "NA11992",
                                "NA11993",
                                "NA11994",
                                "NA11995",
                                "NA12003",
                                "NA12004",
                                "NA12005",
                                "NA12006",
                                "NA12043",
                                "NA12044",
                                "NA12056",
                                "NA12057",
                                "NA12144",
                                "NA12145",
                                "NA12146",
                                "NA12154",
                                "NA12155",
                                "NA12156",
                                "NA12234",
                                "NA12236",
                                "NA12239",
                                "NA12248",
                                "NA12249",
                                "NA12264",
                                "NA12707",
                                "NA12716",
                                "NA12717",
                                "NA12740",
                                "NA12750",
                                "NA12751",
                                "NA12752",
                                "NA12753",
                                "NA12760",
                                "NA12761",
                                "NA12762",
                                "NA12763",
                                "NA12801",
                                "NA12802",
                                "NA12812",
                                "NA12813",
                                "NA12814",
                                "NA12815",
                                "NA12864",
                                "NA12865",
                                "NA12872",
                                "NA12873",
                                "NA12874",
                                "NA12875",
                                "NA12878",
                                "NA12891",
                                "NA12892"};
        final Datatype[] memberDatatypes = {
                       new H5Datatype(Datatype.CLASS_STRING, 12, -1, -1),
                       new H5Datatype(Datatype.CLASS_STRING, 4, -1, -1),
                       new H5Datatype(Datatype.CLASS_STRING, 6, -1, -1),
                       new H5Datatype(Datatype.CLASS_STRING, 10, -1, -1),
                       new H5Datatype(Datatype.CLASS_STRING, 2, -1, -1),
                       new H5Datatype(Datatype.CLASS_STRING, 10, -1, -1),
                       new H5Datatype(Datatype.CLASS_STRING, 10, -1, -1),
                       new H5Datatype(Datatype.CLASS_STRING, 60, -1, -1),
                       new H5Datatype(Datatype.CLASS_STRING, 60, -1, -1),
                       new H5Datatype(Datatype.CLASS_STRING, 60, -1, -1),
                       new H5Datatype(Datatype.CLASS_STRING, 4, -1, -1),
                       new H5Datatype(Datatype.CLASS_STRING, 3, -1, -1),
                       new H5Datatype(Datatype.CLASS_STRING, 3, -1, -1),
                       new H5Datatype(Datatype.CLASS_STRING, 3, -1, -1),
                       new H5Datatype(Datatype.CLASS_STRING, 3, -1, -1),
                       new H5Datatype(Datatype.CLASS_STRING, 3, -1, -1),
                       new H5Datatype(Datatype.CLASS_STRING, 3, -1, -1),
                       new H5Datatype(Datatype.CLASS_STRING, 3, -1, -1),
                       new H5Datatype(Datatype.CLASS_STRING, 3, -1, -1),
                       new H5Datatype(Datatype.CLASS_STRING, 3, -1, -1),
                       new H5Datatype(Datatype.CLASS_STRING, 3, -1, -1),
                       new H5Datatype(Datatype.CLASS_STRING, 3, -1, -1),
                       new H5Datatype(Datatype.CLASS_STRING, 3, -1, -1),
                       new H5Datatype(Datatype.CLASS_STRING, 3, -1, -1),
                       new H5Datatype(Datatype.CLASS_STRING, 3, -1, -1),
                       new H5Datatype(Datatype.CLASS_STRING, 3, -1, -1),
                       new H5Datatype(Datatype.CLASS_STRING, 3, -1, -1),
                       new H5Datatype(Datatype.CLASS_STRING, 3, -1, -1),
                       new H5Datatype(Datatype.CLASS_STRING, 3, -1, -1),
                       new H5Datatype(Datatype.CLASS_STRING, 3, -1, -1),
                       new H5Datatype(Datatype.CLASS_STRING, 3, -1, -1),
                       new H5Datatype(Datatype.CLASS_STRING, 3, -1, -1),
                       new H5Datatype(Datatype.CLASS_STRING, 3, -1, -1),
                       new H5Datatype(Datatype.CLASS_STRING, 3, -1, -1),
                       new H5Datatype(Datatype.CLASS_STRING, 3, -1, -1),
                       new H5Datatype(Datatype.CLASS_STRING, 3, -1, -1),
                       new H5Datatype(Datatype.CLASS_STRING, 3, -1, -1),
                       new H5Datatype(Datatype.CLASS_STRING, 3, -1, -1),
                       new H5Datatype(Datatype.CLASS_STRING, 3, -1, -1),
                       new H5Datatype(Datatype.CLASS_STRING, 3, -1, -1),
                       new H5Datatype(Datatype.CLASS_STRING, 3, -1, -1),
                       new H5Datatype(Datatype.CLASS_STRING, 3, -1, -1),
                       new H5Datatype(Datatype.CLASS_STRING, 3, -1, -1),
                       new H5Datatype(Datatype.CLASS_STRING, 3, -1, -1),
                       new H5Datatype(Datatype.CLASS_STRING, 3, -1, -1),
                       new H5Datatype(Datatype.CLASS_STRING, 3, -1, -1),
                       new H5Datatype(Datatype.CLASS_STRING, 3, -1, -1),
                       new H5Datatype(Datatype.CLASS_STRING, 3, -1, -1),
                       new H5Datatype(Datatype.CLASS_STRING, 3, -1, -1),
                       new H5Datatype(Datatype.CLASS_STRING, 3, -1, -1),
                       new H5Datatype(Datatype.CLASS_STRING, 3, -1, -1),
                       new H5Datatype(Datatype.CLASS_STRING, 3, -1, -1),
                       new H5Datatype(Datatype.CLASS_STRING, 3, -1, -1),
                       new H5Datatype(Datatype.CLASS_STRING, 3, -1, -1),
                       new H5Datatype(Datatype.CLASS_STRING, 3, -1, -1),
                       new H5Datatype(Datatype.CLASS_STRING, 3, -1, -1),
                       new H5Datatype(Datatype.CLASS_STRING, 3, -1, -1),
                       new H5Datatype(Datatype.CLASS_STRING, 3, -1, -1),
                       new H5Datatype(Datatype.CLASS_STRING, 3, -1, -1),
                       new H5Datatype(Datatype.CLASS_STRING, 3, -1, -1),
                       new H5Datatype(Datatype.CLASS_STRING, 3, -1, -1),
                       new H5Datatype(Datatype.CLASS_STRING, 3, -1, -1),
                       new H5Datatype(Datatype.CLASS_STRING, 3, -1, -1),
                       new H5Datatype(Datatype.CLASS_STRING, 3, -1, -1),
                       new H5Datatype(Datatype.CLASS_STRING, 3, -1, -1),
                       new H5Datatype(Datatype.CLASS_STRING, 3, -1, -1),
                       new H5Datatype(Datatype.CLASS_STRING, 3, -1, -1),
                       new H5Datatype(Datatype.CLASS_STRING, 3, -1, -1),
                       new H5Datatype(Datatype.CLASS_STRING, 3, -1, -1),
                       new H5Datatype(Datatype.CLASS_STRING, 3, -1, -1),
                       new H5Datatype(Datatype.CLASS_STRING, 3, -1, -1),
                       new H5Datatype(Datatype.CLASS_STRING, 3, -1, -1),
                       new H5Datatype(Datatype.CLASS_STRING, 3, -1, -1),
                       new H5Datatype(Datatype.CLASS_STRING, 3, -1, -1),
                       new H5Datatype(Datatype.CLASS_STRING, 3, -1, -1),
                       new H5Datatype(Datatype.CLASS_STRING, 3, -1, -1),
                       new H5Datatype(Datatype.CLASS_STRING, 3, -1, -1),
                       new H5Datatype(Datatype.CLASS_STRING, 3, -1, -1),
                       new H5Datatype(Datatype.CLASS_STRING, 3, -1, -1),
                       new H5Datatype(Datatype.CLASS_STRING, 3, -1, -1),
                       new H5Datatype(Datatype.CLASS_STRING, 3, -1, -1),
                       new H5Datatype(Datatype.CLASS_STRING, 3, -1, -1),
                       new H5Datatype(Datatype.CLASS_STRING, 3, -1, -1),
                       new H5Datatype(Datatype.CLASS_STRING, 3, -1, -1),
                       new H5Datatype(Datatype.CLASS_STRING, 3, -1, -1),
                       new H5Datatype(Datatype.CLASS_STRING, 3, -1, -1),
                       new H5Datatype(Datatype.CLASS_STRING, 3, -1, -1),
                       new H5Datatype(Datatype.CLASS_STRING, 3, -1, -1),
                       new H5Datatype(Datatype.CLASS_STRING, 3, -1, -1),
                       new H5Datatype(Datatype.CLASS_STRING, 3, -1, -1),
                       new H5Datatype(Datatype.CLASS_STRING, 3, -1, -1),
                       new H5Datatype(Datatype.CLASS_STRING, 3, -1, -1),
                       new H5Datatype(Datatype.CLASS_STRING, 3, -1, -1),
                       new H5Datatype(Datatype.CLASS_STRING, 3, -1, -1),
                       new H5Datatype(Datatype.CLASS_STRING, 3, -1, -1),
                       new H5Datatype(Datatype.CLASS_STRING, 3, -1, -1),
                       new H5Datatype(Datatype.CLASS_STRING, 3, -1, -1),
                       new H5Datatype(Datatype.CLASS_STRING, 3, -1, -1),
                       new H5Datatype(Datatype.CLASS_STRING, 3, -1, -1),
                       new H5Datatype(Datatype.CLASS_STRING, 3, -1, -1),
                       new H5Datatype(Datatype.CLASS_STRING, 3, -1, -1)};

        final int[] memberSizes = new int[101];
        for (int i=0; i<101; i++) {
            memberSizes[i] = 1;
        }

        /* compound data value must put into a vector field by field */
/*
        Vector dataIn = new Vector();
        int size = D1*D2*D3;
        int x[] = new int[size];
        for (int i=0; i<size; i++)  x[i] = i;
        dataIn.add(0, x);

        size = D1*D2*D3*10;
        float y[] = new float[size];
        for (int i=0; i<size; i++) y[i] = i+i/10;
        dataIn.add(1, y);
*/
        final Dataset d = testFile.createCompoundDS("chr22_CEU", root, dims, memberNames, memberDatatypes, memberSizes, null);
        testFile.close();

    }

    private static void testHDFcomment(final String fileName) throws Exception {
        // retrieve an instance of H5File

        final FileFormat fileFormat = FileFormat.getFileFormat(FileFormat.FILE_TYPE_HDF5);

        if (fileFormat == null)
        {
            System.err.println("Cannot find HDF5 FileFormat.");
            return;
        }

        FileFormat testFile = (H5File)fileFormat.create(fileName);

        if (testFile == null)
        {
            System.err.println("Failed to open file: "+fileName);
            return;
        }

        testFile.open();
        Group root = (Group)((javax.swing.tree.DefaultMutableTreeNode)testFile.getRootNode()).getUserObject();

        // create 2 dataset at the root
        final long[] dims2D = {20, 10};
        Datatype dtype = testFile.createDatatype( Datatype.CLASS_INTEGER, 4, Datatype.NATIVE, Datatype.NATIVE);
        Dataset dataset = testFile.createScalarDS ("i20x10", root, dtype, dims2D, null, null, 0, null);
        dtype = testFile.createDatatype( Datatype.CLASS_FLOAT, 8, Datatype.NATIVE, -1);
        dataset = testFile.createScalarDS ("d20x10", root, dtype, dims2D, null, null, 0, null);
        testFile.close();

        // open the file with read and write access
        testFile = fileFormat.open(fileName, FileFormat.WRITE);

        // open the file and retrieve the file structure
        testFile.open();
        root = (Group)((javax.swing.tree.DefaultMutableTreeNode)testFile.getRootNode()).getUserObject();

        dataset = (Dataset)root.getMemberList().get(1);

        testFile.delete(dataset);
    //    testFile.close();
        H5.H5Fflush(testFile.getFID(), HDF5Constants.H5F_SCOPE_GLOBAL);
    }

    private static void testHDFdelete(final String fileName) throws Exception {
        // retrieve an instance of H5File

        final FileFormat fileFormat = FileFormat.getFileFormat(FileFormat.FILE_TYPE_HDF5);

        if (fileFormat == null)
        {
            System.err.println("Cannot find HDF5 FileFormat.");
            return;
        }

        FileFormat testFile = (H5File)fileFormat.create(fileName);

        if (testFile == null)
        {
            System.err.println("Failed to open file: "+fileName);
            return;
        }

        testFile.open();
        Group root = (Group)((javax.swing.tree.DefaultMutableTreeNode)testFile.getRootNode()).getUserObject();

        // create 2 dataset at the root
        final long[] dims2D = {20, 10};
        Datatype dtype = testFile.createDatatype( Datatype.CLASS_INTEGER, 4, Datatype.NATIVE, Datatype.NATIVE);
        Dataset dataset = testFile.createScalarDS ("i20x10", root, dtype, dims2D, null, null, 0, null);
        dtype = testFile.createDatatype( Datatype.CLASS_FLOAT, 8, Datatype.NATIVE, -1);
        dataset = testFile.createScalarDS ("d20x10", root, dtype, dims2D, null, null, 0, null);
        testFile.close();

        // open the file with read and write access
        testFile = fileFormat.open(fileName, FileFormat.WRITE);

        // open the file and retrieve the file structure
        testFile.open();
        root = (Group)((javax.swing.tree.DefaultMutableTreeNode)testFile.getRootNode()).getUserObject();

        dataset = (Dataset)root.getMemberList().get(1);
        testFile.delete(dataset);
    //    testFile.close();
        H5.H5Fflush(testFile.getFID(), HDF5Constants.H5F_SCOPE_GLOBAL);
    }

    private static void testchunkchche() throws Exception {
        final int pid = H5.H5Pcreate(HDF5Constants.H5P_FILE_ACCESS);
        final int[] mdcNumElements = new int[1];
        final int[] rdccNumElements = new int[1];
        final int[] rdccNumBytes = new int[1];
        final double[] rdccW0 = new double[1];

        H5.H5Pget_cache(pid,
                      mdcNumElements,
                      rdccNumElements,
                      rdccNumBytes,
                      rdccW0);

        H5.H5Pset_cache(pid,
                      mdcNumElements[0],
                      rdccNumElements[0],
                      1024*1024*4,
                      rdccW0[0]);

        final int fid = H5.H5Fopen("H:\\java\\java8\\xcao\\test\\bigdata.h5",
            HDF5Constants.H5F_ACC_RDWR, pid);
        final int did = H5.H5Dopen(fid, "/PI");
        final int tid = H5.H5Dget_type(did);
        final int mtid = H5.H5Tget_native_type(tid);
        H5.H5Tclose(tid);

        final float[] allData = new float[125*130*39];

        final long dim1d[] = {125*130*39};
        final int msid = H5.H5Screate_simple(1, dim1d, null);
        if (H5.H5Dread_float(did, mtid, msid, HDF5Constants.H5S_ALL,
            HDF5Constants.H5P_DEFAULT, allData)<0) {
            System.err.println("##### Read data failed\n");
        }
        H5.H5Sclose(msid);

        for (int i=0; i<20; i++) {
            System.out.println(allData[i]);
        }
    }

    private static void testHDFcompound() {
        String FNAME = "H:\\java\\java8\\xcao\\test\\bigdata.h5";
        String DNAME = "PI";
    	
        try {
            create2Dcompound(FNAME, DNAME);
        } catch (final Exception ex) { ex.printStackTrace(); System.exit(1);}

        try {
            final H5CompoundDS d = (H5CompoundDS)FileFormat.getHObject(FNAME+"#//"+DNAME);
            if (d == null) {
                System.out.println("Failed to read dataset: "+DNAME);
                System.exit(1);
            }

            readHyperslab(d);

        } catch (final Exception ex) { ex.printStackTrace(); }
    }

    private static void readHyperslab(final H5CompoundDS d) throws Exception {
        int rank = d.getRank();
        if (rank <=0) {
            d.init();
            rank = d.getRank();
        }

        final long dims[] = d.getSelectedDims();
        final long selected[] = d.getSelectedDims();
        final long start[] = d.getStartDims();
        final long stride[] = d.getStride();
        final int selectedIdx[] = d.getSelectedIndex();
        final String mnames[] = d.getMemberNames();
        final int orders[] = d.getMemberOrders();

        selectedIdx[0]=1; selectedIdx[1]=2;selectedIdx[2]=0;
        selected[0] =1; selected[1] =5; selected[2] =10;
        stride[0] = stride[1] = stride[2] = 1;
        start[0] = 2; start[1] = 3; start[2] = 5;

        d.read();
        final Vector data = (Vector)d.getData();

        if (data != null) {
            final int n = java.lang.reflect.Array.getLength(data.get(0));
            final int m = d.getMemberCount();

            Object mdata = null;
            for (int i=0; i<m; i++) {
                System.out.println("\n");
                System.out.println(mnames[i]);
                mdata = data.get(i);
                for (int j=0; j<n; j++) {
                    for (int k=0; k<orders[i]; k++) {
                        System.out.print(java.lang.reflect.Array.get(mdata, j*orders[i]+k));
                        System.out.print("\t");
                    }
                    System.out.print("\n");
                }
            }
        } /* if (data != null) { */
    }

    private static void create2Dcompound(String FNAME, String DNAME) throws Exception 
    {
        final long DIM1 = 50;
        final long DIM2 = 10;
        final long DIM3 = 20;

        final FileFormat fileFormat = FileFormat.getFileFormat(FileFormat.FILE_TYPE_HDF5);
        final H5File h5File = (H5File) fileFormat.create(FNAME);
        h5File.open();
        final Group root = (Group) ((javax.swing.tree.DefaultMutableTreeNode) h5File.getRootNode()).getUserObject();
        final long[] dims = {DIM1, DIM2, DIM3};
        final String[] memberNames = {"x", "y"};
        final Datatype[] memberDatatypes = {
            new H5Datatype(Datatype.CLASS_INTEGER, -1, -1, -1),
            new H5Datatype(Datatype.CLASS_FLOAT, -1, -1, -1)
        };
        final int[] memberSizes = {1, 10};
        

        /* compound data value must put into a vector field by field */
        final Vector dataIn = new Vector();
        int size = (int)(DIM1*DIM2*DIM3);
        final int x[] = new int[size];
        for (int i=0; i<size; i++) {
            x[i] = i;
        }
        dataIn.add(0, x);

        size = (int)(DIM1*DIM2*DIM3)*10;
        final float y[] = new float[size];
        for (int i=0; i<size; i++) {
            y[i] = i+i/10;
        }
        dataIn.add(1, y);

        final Dataset d = h5File.createCompoundDS(DNAME, root, dims, memberNames, memberDatatypes, memberSizes, dataIn);
        h5File.close();
    }

    private static void createDataset( final String fname ) throws Exception
    {
        final long[] dims2D = {256, 200};
        final long[] dims3D = {20, 10, 5};
        final int[] dataInt = new int[(int)dims2D[0]*(int)dims2D[1]];
        final double[] dataDouble = new double[(int)dims2D[0]*(int)dims2D[1]];

        // retrieve an instance of H5File
        final FileFormat fileFormat = FileFormat.getFileFormat(FileFormat.FILE_TYPE_HDF5);

        if (fileFormat == null)
        {
            System.err.println("Cannot find HDF5 FileFormat.");
            return;
        }

        // create a new file with a given file name.
        final H5File testFile = (H5File)fileFormat.create(fname);

        if (testFile == null)
        {
            System.err.println("Failed to create file:"+fname);
            return;
        }
        
        for (int i=0; i<dataInt.length; i++) {
            dataDouble[i] = dataInt[i] = (i % (int)dims2D[1]);
        }

        // open the file and retrieve the root group
        testFile.open();
        final Group root = (Group)((javax.swing.tree.DefaultMutableTreeNode)testFile.getRootNode()).getUserObject();

        // create groups at the root
        final Group g1 = testFile.createGroup("integer arrays", root);
        final Group g2 = testFile.createGroup("float arrays", root);

        // create 2D 32-bit (4 bytes) integer dataset of 20 by 10
        Datatype dtype = testFile.createDatatype(
            Datatype.CLASS_INTEGER, 4, Datatype.NATIVE, Datatype.NATIVE);
        Dataset dataset = testFile.createScalarDS
            ("2D int", g1, dtype, dims2D, null, null, 0, dataInt);

        // create 2D 32-bit (4 bytes) integer dataset of 20 by 10
        dtype = testFile.createDatatype(
            Datatype.CLASS_INTEGER, 4, Datatype.NATIVE, Datatype.SIGN_NONE);
        dataset = testFile.createScalarDS
            ("2D uint", g1, dtype, dims2D, null, null, 0, dataInt);

        // create 3D 8-bit (1 byte) unsigned integer dataset of 20 by 10 by 5
        dtype = testFile.createDatatype(
            Datatype.CLASS_INTEGER, 1, Datatype.NATIVE, Datatype.SIGN_NONE);
        dataset = testFile.createScalarDS
            ("3D byte", g1, dtype, dims3D, null, null, 0, null);

        // create 2D 64-bit (8 bytes) double dataset of 20 by 10
        dtype = testFile.createDatatype(
            Datatype.CLASS_FLOAT, 8, Datatype.NATIVE, -1);
        dataset = testFile.createScalarDS
            ("2D double", g2, dtype, dims2D, null, null, 0, dataDouble);

        // create 3D 32-bit (4 bytes) float dataset of 20 by 10 by 5
        dtype = testFile.createDatatype(
            Datatype.CLASS_FLOAT, 4, Datatype.NATIVE, -1);
        dataset = testFile.createScalarDS
            ("3D float", g2, dtype, dims3D, null, null, 0, null);

        // close file resource
        testFile.close();
    }

    private static void createStrDataset( final String fname ) throws Exception
    {
        final long[] dims1D = {2};
        final long[] dims2D = {2,2};
        final long[] dims3D = {2,2,2};

        final String[] data3 = { "one", "two" };


        // Retrieve an instance of H5File
        final FileFormat fileFormat = FileFormat.getFileFormat(FileFormat.FILE_TYPE_HDF5);

        if (fileFormat == null)
        {
            System.err.println("Cannot find HDF5 FileFormat.");
            return;
        }

        // Create a new file with a given file name.
        final H5File testFile = (H5File)fileFormat.create(fname);

        if (testFile == null)
        {
            System.err.println("Failed to create file:"+fname);
            return;
        }

        // Open the file and retrieve the root group
        testFile.open();
        final Group root = (Group)((javax.swing.tree.DefaultMutableTreeNode)testFile.getRootNode()).getUserObject();

        // Create groups at the root
        final Group g1 = testFile.createGroup("integer arrays", root);
        final Group g2 = testFile.createGroup("float arrays", root);
        final Group g3 = testFile.createGroup("string arrays", root);
        System.out.println( "Just after create group calls" );

        // Create 2D 32-bit (4 bytes) integer dataset of 20 by 10
        Datatype dtype = testFile.createDatatype(
            Datatype.CLASS_INTEGER, 4, Datatype.NATIVE, Datatype.NATIVE);
        Dataset dataset = testFile.createScalarDS
            ("2D 32-bit integer 20x10", g1, dtype, dims2D, null, null, 0, null);

        // Create 3D 8-bit (1 byte) unsigned integer dataset of 20 by 10 by 5
        dtype = testFile.createDatatype(
            Datatype.CLASS_INTEGER, 1, Datatype.NATIVE, Datatype.SIGN_NONE);
        dataset = testFile.createScalarDS
            ("3D 8-bit unsigned integer 20x10x5", g1, dtype, dims3D, null, null, 0, null);

        // Create 2D 64-bit (8 bytes) double dataset of 20 by 10
        dtype = testFile.createDatatype(
            Datatype.CLASS_FLOAT, 8, Datatype.NATIVE, -1);
        dataset = testFile.createScalarDS
            ("2D 64-bit double 20x10", g2, dtype, dims2D, null, null, 0, null);

        // Create 3D 32-bit (4 bytes) float dataset of 20 by 10 by 5
        dtype = testFile.createDatatype(
            Datatype.CLASS_FLOAT, 4, Datatype.NATIVE, -1);
        dataset = testFile.createScalarDS
            ("3D 32-bit float  20x10x5", g2, dtype, dims3D, null, null, 0, null);

        // Create String dataset
        System.out.println( "Just before call for STRINGS" );
        try
        {
            final int strlen = 5;
            dtype = testFile.createDatatype(
                Datatype.CLASS_STRING, strlen, Datatype.NATIVE, -1);
            dataset = testFile.createScalarDS
                ("String 2", g3, dtype, dims1D, null, null, 0, data3);
        }
        catch (final Exception ex)
        {
            System.out.println( ex.getMessage() );
            System.exit( -1 );
        }


        // Close file resource
        testFile.close();

        System.out.println( "Normal EOJ" );
    }
    
    private static final boolean create_test_file(final String fname)  throws Exception
    {
        H5File file=null;
        Group g0, g1, g00;
        
        final String NAME_GROUP = "/g0";
        final String NAME_GROUP_ATTR = "/g0_attr";
        final String NAME_GROUP_SUB = "/g0/g00";
        final String NAME_DATASET_INT = "/dataset_int";
        final String NAME_DATASET_FLOAT = "/dataset_float";
        final String NAME_DATASET_CHAR = "/dataset_byte";
        final String NAME_DATASET_STR = "/dataset_str";
        final String NAME_DATASET_ENUM = "/dataset_enum";    
        final String NAME_DATASET_ATTR = "/dataset_with_attr";
        final String NAME_DATASET_COMPOUND = "/comp_dataset";
        final String NAME_DATASET_SUB = "/g0/dataset_int";
        final String NAME_DATASET_SUB_SUB = "/g0/g00/dataset_float";
        final long DIM1 = 50;
        final long DIM2 = 10;
        final long DIM3 = 20;
        final long[] DIMs = {DIM1, DIM2};
        final long[] CHUNKs = {DIM1/2, DIM2/2};
        final int RANK = 2;
        final int STR_LEN = 20;
        final int DIM_SIZE = (int)(DIM1*DIM2);;
        final String FNAME = "H:\\java\\java8\\xcao\\test\\bigdata.h5";
        final String DNAME = "PI";

        final int[] DATA_INT = new int[DIM_SIZE];
        final float[] DATA_FLOAT = new float[DIM_SIZE];
        final byte[] DATA_BYTE = new byte[DIM_SIZE];
        final String[] DATA_STR = new String[DIM_SIZE];
        final int[] DATA_ENUM = new int[DIM_SIZE];
        final Vector DATA_COMP = new Vector(3);

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
        
        
        file = new H5File(fname, H5File.CREATE);
        file.open();
        g0 = file.createGroup(NAME_GROUP, null);
        g1 = file.createGroup(NAME_GROUP_ATTR, null);
        g00 = file.createGroup(NAME_GROUP_SUB, null);

        final long[] attrDims = {1};
        final String attrName = "Test attribute";
        final String[] attrValue = {"Test for group attribute"};
        final Datatype attrType = new H5Datatype(Datatype.CLASS_STRING, attrValue[0].length()+1, -1, -1);
        final Attribute attr = new Attribute(attrName, attrType, attrDims);
        attr.setValue(attrValue);
        g1.writeMetadata(attr);

        file.createScalarDS(NAME_DATASET_INT, null, new H5Datatype(Datatype.CLASS_INTEGER, -1, -1, -1), DIMs, null, CHUNKs, 9, DATA_INT);
        file.createScalarDS(NAME_DATASET_FLOAT, null, new H5Datatype(Datatype.CLASS_FLOAT, -1, -1, -1), DIMs, null, CHUNKs, 9, DATA_FLOAT);
        file.createScalarDS(NAME_DATASET_CHAR, null, new H5Datatype(Datatype.CLASS_CHAR, -1, -1, -1), DIMs, null, CHUNKs, 9, DATA_BYTE);
        file.createScalarDS(NAME_DATASET_STR, null, new H5Datatype(Datatype.CLASS_STRING, STR_LEN, -1, -1), DIMs, null, CHUNKs, 9, DATA_STR);
        file.createScalarDS(NAME_DATASET_ENUM, null, new H5Datatype(Datatype.CLASS_ENUM, -1, -1, -1), DIMs, null, CHUNKs, 9, DATA_ENUM);
        file.createScalarDS(NAME_DATASET_SUB, g0, new H5Datatype(Datatype.CLASS_INTEGER, -1, -1, -1), DIMs, null, CHUNKs, 9, DATA_INT);
        file.createScalarDS(NAME_DATASET_SUB_SUB, g00, new H5Datatype(Datatype.CLASS_FLOAT, -1, -1, -1), DIMs, null, CHUNKs, 9, DATA_FLOAT);
        file.createImage(NAME_DATASET_ATTR, null, new H5Datatype(Datatype.CLASS_INTEGER, 1, -1, -1), DIMs, null, CHUNKs, 9, 1, -1, DATA_BYTE);
        final Datatype[]  mdtypes = {new H5Datatype(Datatype.CLASS_INTEGER, -1, -1, -1), new H5Datatype(Datatype.CLASS_FLOAT, -1, -1, -1), new H5Datatype(Datatype.CLASS_STRING, STR_LEN, -1, -1)};
        final String[] mnames = {"int", "float", "string"};
        file.createCompoundDS(NAME_DATASET_COMPOUND, null, DIMs, null, CHUNKs, 9, mnames, mdtypes, null, DATA_COMP);

        try { file.close(); } catch (final Exception ex) {}
        
        return true;
    }
    
    private static final boolean create_debug_file()  throws Exception
    {
        H5File file=null;
        Group g0, g1, g00;
        
        final String NAME_GROUP = "/g0";
        final String NAME_GROUP_ATTR = "/g0_attr";
        final String NAME_GROUP_SUB = "/g0/g00";
        final String NAME_DATASET_INT = "/dataset_int";
        final String NAME_DATASET_FLOAT = "/dataset_float";
        final String NAME_DATASET_CHAR = "/dataset_byte";
        final String NAME_DATASET_STR = "/dataset_str";
        final String NAME_DATASET_ENUM = "/dataset_enum";    
        final String NAME_DATASET_ATTR = "/dataset_with_attr";
        final String NAME_DATASET_COMPOUND = "/comp_dataset";
        final String NAME_DATASET_SUB = "/g0/dataset_int";
        final String NAME_DATASET_SUB_SUB = "/g0/g00/dataset_float";
        final long DIM1 = 50;
        final long DIM2 = 10;
        final long DIM3 = 20;
        final long[] DIMs = {DIM1, DIM2};
        final long[] CHUNKs = {DIM1/2, DIM2/2};
        final int RANK = 2;
        final int STR_LEN = 20;
        final int DIM_SIZE = (int)(DIM1*DIM2);;
        final String FNAME = "H:\\java\\java8\\xcao\\test\\bigdata.h5";
        final String DNAME = "PI";

        final int[] DATA_INT = new int[DIM_SIZE];
        final float[] DATA_FLOAT = new float[DIM_SIZE];
        final byte[] DATA_BYTE = new byte[DIM_SIZE];
        final String[] DATA_STR = new String[DIM_SIZE];
        final int[] DATA_ENUM = new int[DIM_SIZE];
        final Vector DATA_COMP = new Vector(3);

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
        
        file = new H5File("D:\\hdf-files\\debug_memory_leak.h5", H5File.CREATE);
        file.open();

        int tclass = Datatype.CLASS_INTEGER;
        int nosign = Datatype.SIGN_NONE;
        file.createScalarDS("int8", null, new H5Datatype(tclass, 1, -1, -1), DIMs, null, CHUNKs, 9, DATA_INT);
        file.createScalarDS("uint8", null, new H5Datatype(tclass, 1, -1, nosign), DIMs, null, CHUNKs, 9, DATA_INT);
        file.createScalarDS("int16", null, new H5Datatype(tclass, 2, -1, -1), DIMs, null, CHUNKs, 9, DATA_INT);
        file.createScalarDS("uint16", null, new H5Datatype(tclass, 2, -1, nosign), DIMs, null, CHUNKs, 9, DATA_INT);
        file.createScalarDS("int32", null, new H5Datatype(tclass, 4, -1, -1), DIMs, null, CHUNKs, 9, DATA_INT);
        file.createScalarDS("uint32", null, new H5Datatype(tclass, 4, -1, nosign), DIMs, null, CHUNKs, 9, DATA_INT);
        file.createScalarDS("int64", null, new H5Datatype(tclass, 8, -1, -1), DIMs, null, CHUNKs, 9, DATA_INT);
        
        tclass = Datatype.CLASS_FLOAT;
        file.createScalarDS("float32", null, new H5Datatype(tclass, 4, -1, -1), DIMs, null, CHUNKs, 9, DATA_FLOAT);
        file.createScalarDS("float64", null, new H5Datatype(tclass, 8, -1, -1), DIMs, null, CHUNKs, 9, DATA_FLOAT);

        tclass = Datatype.CLASS_CHAR;
        file.createScalarDS("char", null, new H5Datatype(tclass, -1, -1, -1), DIMs, null, CHUNKs, 9, DATA_BYTE);
        file.createScalarDS("uchar", null, new H5Datatype(tclass, -1, -1, nosign), DIMs, null, CHUNKs, 9, DATA_BYTE);
        
        file.createScalarDS("str", null, new H5Datatype(Datatype.CLASS_STRING, STR_LEN, -1, -1), DIMs, null, CHUNKs, 9, DATA_STR);
        file.createScalarDS("enum", null, new H5Datatype(Datatype.CLASS_ENUM, -1, -1, -1), DIMs, null, CHUNKs, 9, DATA_ENUM);
        file.createImage("image", null, new H5Datatype(Datatype.CLASS_INTEGER, 1, -1, -1), DIMs, null, CHUNKs, 9, 1, -1, DATA_BYTE);
        final Datatype[]  mdtypes = {new H5Datatype(Datatype.CLASS_INTEGER, -1, -1, -1), new H5Datatype(Datatype.CLASS_FLOAT, -1, -1, -1), new H5Datatype(Datatype.CLASS_STRING, STR_LEN, -1, -1)};
        final String[] mnames = {"int", "float", "string"};
        file.createCompoundDS("compound", null, DIMs, null, CHUNKs, 9, mnames, mdtypes, null, DATA_COMP);

        try { file.close(); } catch (final Exception ex) {}
        
        return true;
    }
    
}
