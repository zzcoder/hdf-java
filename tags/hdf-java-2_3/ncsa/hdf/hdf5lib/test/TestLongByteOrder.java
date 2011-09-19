/****************************************************************************
 * NCSA HDF                                                                 *
 * National Comptational Science Alliance                                   *
 * University of Illinois at Urbana-Champaign                               *
 * 605 E. Springfield, Champaign IL 61820                                   *
 *                                                                          *
 * For conditions of distribution and use, see the accompanying             *
 * java-hdf5/COPYING file.                                                  *
 *                                                                          *
 ****************************************************************************/

package ncsa.hdf.hdf5lib.test;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import ncsa.hdf.hdf5lib.*;
import ncsa.hdf.hdf5lib.exceptions.*;

public class TestLongByteOrder implements TestModule
{
	private String FILE_ROOT = null;


	String dbgInfo;
	boolean testResult = false;

	public TestLongByteOrder()
	{

	}

	public boolean setUp( String workingDir, boolean cleanFilesAtStart) {
		FILE_ROOT = workingDir;
		return true;
	}

	public boolean cleanUp( boolean saveFiles )
        {
                try {
                H5.H5close();
                } catch (HDF5Exception ex) {
                        dbgInfo += "\nH5close(): FAILED "+ex;
			return false;
                }
		return true;
        }


	public String getTestName() {
		String desc = "Test long write & read BE and LE";
		return desc;
	}

	public String getTestDescription() {
		String desc = "Test long, write and read byte orders.";
		return desc;
	}

	public void runTest() {
		testResult = false;
		boolean res;
		int ntests = 4;
		int passed = 0;
		dbgInfo = "\n\n========== Test Long: Write BE & read BE ==========";
		if(testLongBEBE()) {
			passed++;
		}
		dbgInfo += "\n\n========== Test Long: Write BE & read LE ==========";
		if(testLongBELE()) {
			passed++;
		}
		dbgInfo += "\n\n========== Test Long: Write LE & read LE ==========";
		if(testLongLELE()) {
			passed++;
		}
		dbgInfo += "\n\n========== Test Long: Write LE & read BE ==========";
		if(testLongLEBE()) {
			passed++;
		}
		dbgInfo += "\n\n========== Long/long/int/short/byte tests complete: "+passed+" of "+ntests+" passed  ==========";
		testResult = (passed == ntests);
	}

	public boolean testPassed()
	{
		return testResult;
	}
	public String getVerboseResult()
	{
		return dbgInfo;
	}


	/**
	 * Test 
	 */
	private boolean testLongBEBE()
	{
		String fileName = FILE_ROOT+"LongBEBE.h5";
		String dataset1Name = "longBEArray";
		int nx = 100;
		int ny = 200;
		int rank = 2;
		int status;
		int i, j;
		int file, dataset1, dataset2;
		int tfile, tdataset1, tdataset2;
		int datatype2, dataspace2;   /* handles */
		int datatype1, dataspace1;   /* handles */
		long[] dimsf = {100, 200};	 /* dataset dimensions */

		long[] data = new long[nx*ny];	/* data to write */
		long[] outDataBEBE = new long[nx*ny];

		// Data  and output buffer initialization.
		for (j = 0; j < nx; j++) {
			for (i = 0; i < ny; i++) {
				data[j*ny+i] = (long)(j*nx+i);
			}
		}


		// Create a new file using H5F_ACC_TRUNC access,
		// default file creation properties, and default file
		// access properties.
		file = -1;
		try {
		file = H5.H5Fcreate(fileName,
			HDF5Constants.H5F_ACC_TRUNC,
			HDF5Constants.H5P_DEFAULT,
			HDF5Constants.H5P_DEFAULT);
		} catch (HDF5Exception ex) {
			dbgInfo += "\n\nH5create() Failed, exception: "+ex;
			return false;
		}

		// Describe the size of the array and create the data space
		// for fixed size dataset.
		dataspace1 = -1;
		try {
			dataspace1 = H5.H5Screate_simple(rank, dimsf, null);
		} catch (Exception ex) { 
			dbgInfo += "\nH5Screate_simple: dataspace1 failed "+ex; 
			return false;
		}

		// Define datatype for the data in the file.
		datatype1 = -1;
		try {
			datatype1 = H5.H5Tcopy(H5.J2C(HDF5CDataTypes.JH5T_STD_I64BE));
		} catch (Exception ex) { 
			dbgInfo += "\nH5Tcopy: datatype1 failed "+ex; 
			return false; 
		}

		// Create a new dataset within the file using defined dataspace and
		// datatype and default dataset creation properties.
		try {
			dataset1 = H5.H5Dcreate(file, dataset1Name, 
				datatype1, dataspace1,
				HDF5Constants.H5P_DEFAULT);
		} catch (HDF5Exception ex) {
			dbgInfo += "\n\nH5Dcreate() dataset1 Failed, exception: "+ex;
			return false;
		}

		// Write the data to the dataset using default transfer properties.
		try {
			status = H5.H5Dwrite(dataset1,
			H5.J2C(HDF5CDataTypes.JH5T_STD_I64BE),
			HDF5Constants.H5S_ALL,
			HDF5Constants.H5S_ALL,
			HDF5Constants.H5P_DEFAULT,
			data);
		} catch (Exception ex) {
			dbgInfo += "\nH5Dwrite(): failed dataset1 "+ex;
			return false;
		}

		try {
			H5.H5Sclose(dataspace1);
		} catch (Exception ex) {
			dbgInfo += "\nH5Sclose: dataspace 1 failed. "+ex;
			return false;
		}
		try {
			H5.H5Tclose(datatype1);
		} catch (Exception ex) {
			dbgInfo += "\nH5Tclose: datatype1 failed. "+ex;
			return false;
		}
		try {
			H5.H5Dclose(dataset1);
		} catch (Exception ex) {
			dbgInfo += "\nH5Dclose: dataset1 failed. "+ex;
			return false;
		 }

		try {
			H5.H5Fclose(file);
		} catch (HDF5Exception ex) {
			dbgInfo += "\nH5Fclose(): FAILED "+ex;
			return false;
		}

 
  		tfile = -1;
  		try {
 		tfile = H5.H5Fopen(fileName,
                                 HDF5Constants.H5F_ACC_RDWR,
                                 HDF5Constants.H5P_DEFAULT);
 		} catch (HDF5Exception ex) {
 			dbgInfo += "\n\nH5Fopen() second open Failed, exception: "+ex;
 			return false;
 		}
 
 		try {
 			tdataset1 = H5.H5Dopen(tfile, dataset1Name); 
 		} catch (HDF5Exception ex) {
 			dbgInfo += "\n\nH5Dopen() dataset1 Failed, exception: "+ex;
 			return false;
 		}
 
		dbgInfo += "\nRead long BE data as long BE";
 		try {
 			status = H5.H5Dread(tdataset1,
 				H5.J2C(HDF5CDataTypes.JH5T_STD_I64BE),
 				HDF5Constants.H5S_ALL,
 				HDF5Constants.H5S_ALL,
 				HDF5Constants.H5P_DEFAULT,
 				outDataBEBE);
 		} catch (Exception ex) {
			//ex.printStackTrace();
			dbgInfo += "\nH5Dread: failed. "+ex; 
			return false;
		}

		int errs = 0;
 		for (j = 0; (j < nx) && (errs < 10); j++)
 		{
 			for (i = 0; (i < ny) && (errs < 10); i++)  {
 				if (outDataBEBE[j*ny+i] != data[j*ny+i]) {
					String indat = java.lang.Long.toHexString(data[j*ny+i]);
					String outdat = java.lang.Long.toHexString(outDataBEBE[j*ny+i]);
 					dbgInfo += "\n* bad data at: ["+i+"]["+j+"]  original: "+data[j*ny+i]+" ( "+indat+" )  read back as "+outDataBEBE[j*ny+i]+" ( "+outdat+" )";
					errs++;
 				}
 			}
 		}
 
 		try {
 			H5.H5Dclose(tdataset1);
 		} catch (Exception ex) {
			dbgInfo += "\nH5Dclose: dataset1 after read 1 failed. "+ex;
			return false;
		}


 		try {
 			H5.H5Fclose(tfile);
 		} catch (HDF5Exception ex) {
 			dbgInfo += "\nH5Fclose(): FAILED "+ex;
 			return false;
 		}
		if (errs > 0) {
			dbgInfo += "\nLongBEBE() completed: "+errs+" errs.";
			return false;
		} else {
			dbgInfo += "\nLongBEBE(): OK ";
			return true;
		}
 	}

	private boolean testLongBELE()
	{
		String fileName = FILE_ROOT+"LongBELE.h5";
		String dataset1Name = "longBEArray";
		int nx = 100;
		int ny = 200;
		int rank = 2;
		int status;
		int i, j;
		int file, dataset1, dataset2;
		int tfile, tdataset1, tdataset2;
		int datatype2, dataspace2;   /* handles */
		int datatype1, dataspace1;   /* handles */
		long[] dimsf = {100, 200};	 /* dataset dimensions */

		long[] data = new long[nx*ny];	/* data to write */
		long[] outDataBELE = new long[nx*ny];

		// Data  and output buffer initialization.
		for (j = 0; j < nx; j++) {
			for (i = 0; i < ny; i++) {
				data[j*ny+i] = (long)(j*nx+i);
			}
		}


		// Create a new file using H5F_ACC_TRUNC access,
		// default file creation properties, and default file
		// access properties.
		file = -1;
		try {
		file = H5.H5Fcreate(fileName,
			HDF5Constants.H5F_ACC_TRUNC,
			HDF5Constants.H5P_DEFAULT,
			HDF5Constants.H5P_DEFAULT);
		} catch (HDF5Exception ex) {
			dbgInfo += "\n\nH5create() Failed, exception: "+ex;
			return false;
		}

		// Describe the size of the array and create the data space
		// for fixed size dataset.
		dataspace1 = -1;
		try {
			dataspace1 = H5.H5Screate_simple(rank, dimsf, null);
		} catch (Exception ex) { 
			dbgInfo += "\nH5Screate_simple: dataspace1 failed "+ex; 
			return false;
		}

		// Define datatype for the data in the file.
		datatype1 = -1;
		try {
			datatype1 = H5.H5Tcopy(H5.J2C(HDF5CDataTypes.JH5T_STD_I64BE));
		} catch (Exception ex) { 
			dbgInfo += "\nH5Tcopy: datatype1 failed "+ex; 
			return false; 
		}

		// Create a new dataset within the file using defined dataspace and
		// datatype and default dataset creation properties.
		try {
			dataset1 = H5.H5Dcreate(file, dataset1Name, 
				datatype1, dataspace1,
				HDF5Constants.H5P_DEFAULT);
		} catch (HDF5Exception ex) {
			dbgInfo += "\n\nH5Dcreate() dataset1 Failed, exception: "+ex;
			return false;
		}

		// Write the data to the dataset using default transfer properties.
		try {
			status = H5.H5Dwrite(dataset1,
			H5.J2C(HDF5CDataTypes.JH5T_STD_I64BE),
			HDF5Constants.H5S_ALL,
			HDF5Constants.H5S_ALL,
			HDF5Constants.H5P_DEFAULT,
			data);
		} catch (Exception ex) {
			dbgInfo += "\nH5Dwrite(): failed dataset1 "+ex;
			return false;
		}

		try {
			H5.H5Sclose(dataspace1);
		} catch (Exception ex) {
			dbgInfo += "\nH5Sclose: dataspace 1 failed. "+ex;
			return false;
		}
		try {
			H5.H5Tclose(datatype1);
		} catch (Exception ex) {
			dbgInfo += "\nH5Tclose: datatype1 failed. "+ex;
			return false;
		}
		try {
			H5.H5Dclose(dataset1);
		} catch (Exception ex) {
			dbgInfo += "\nH5Dclose: dataset1 failed. "+ex;
			return false;
		 }

		try {
			H5.H5Fclose(file);
		} catch (HDF5Exception ex) {
			dbgInfo += "\nH5Fclose(): FAILED "+ex;
			return false;
		}

 
  		tfile = -1;
  		try {
 		tfile = H5.H5Fopen(fileName,
                                 HDF5Constants.H5F_ACC_RDWR,
                                 HDF5Constants.H5P_DEFAULT);
 		} catch (HDF5Exception ex) {
 			dbgInfo += "\n\nH5Fopen() second open Failed, exception: "+ex;
 			return false;
 		}
 
 		try {
 			tdataset1 = H5.H5Dopen(tfile, dataset1Name); 
 		} catch (HDF5Exception ex) {
 			dbgInfo += "\n\nH5Dopen() dataset1 Failed, exception: "+ex;
 			return false;
 		}
 
		dbgInfo += "\nRead long BE data as long LE";
 		try {
 			status = H5.H5Dread(tdataset1,
 				H5.J2C(HDF5CDataTypes.JH5T_STD_I64LE),
 				HDF5Constants.H5S_ALL,
 				HDF5Constants.H5S_ALL,
 				HDF5Constants.H5P_DEFAULT,
 				outDataBELE);
 		} catch (Exception ex) {
			//ex.printStackTrace();
			dbgInfo += "\nH5Dread: failed. "+ex; 
			return false;
		}

		int errs = 0;
 		for (j = 0; (j < nx) && (errs < 10); j++)
 		{
 			for (i = 0; (i < ny) && (errs < 10); i++)  {
 				if (outDataBELE[j*ny+i] != data[j*ny+i]) {
					String indat = java.lang.Long.toHexString(data[j*ny+i]);
					String outdat = java.lang.Long.toHexString(outDataBELE[j*ny+i]);
 					dbgInfo += "\n* bad data at: ["+i+"]["+j+"]  original: "+data[j*ny+i]+" ( "+indat+" )  read back as "+outDataBELE[j*ny+i]+" ( "+outdat+" )";
					errs++;
 				}
 			}
 		}
 
 		try {
 			H5.H5Dclose(tdataset1);
 		} catch (Exception ex) {
			dbgInfo += "\nH5Dclose: dataset1 after read 1 failed. "+ex;
			return false;
		}


 		try {
 			H5.H5Fclose(tfile);
 		} catch (HDF5Exception ex) {
 			dbgInfo += "\nH5Fclose(): FAILED "+ex;
 			return false;
 		}
		if (errs > 0) {
			dbgInfo += "\nLongBELE() completed: "+errs+" errs.";
			return false;
		} else {
			dbgInfo += "\nLongBELE(): OK ";
			return true;
		}
 	}

	private boolean testLongLELE()
	{
		String fileName = FILE_ROOT+"LongLELE.h5";
		String dataset1Name = "longLEArray";
		int nx = 100;
		int ny = 200;
		int rank = 2;
		int status;
		int i, j;
		int file, dataset1, dataset2;
		int tfile, tdataset1, tdataset2;
		int datatype2, dataspace2;   /* handles */
		int datatype1, dataspace1;   /* handles */
		long[] dimsf = {100, 200};	 /* dataset dimensions */

		long[] data = new long[nx*ny];	/* data to write */
		long[] outDataLELE = new long[nx*ny];

		// Data  and output buffer initialization.
		for (j = 0; j < nx; j++) {
			for (i = 0; i < ny; i++) {
				data[j*ny+i] = (long)(j*nx+i);
			}
		}


		// Create a new file using H5F_ACC_TRUNC access,
		// default file creation properties, and default file
		// access properties.
		file = -1;
		try {
		file = H5.H5Fcreate(fileName,
			HDF5Constants.H5F_ACC_TRUNC,
			HDF5Constants.H5P_DEFAULT,
			HDF5Constants.H5P_DEFAULT);
		} catch (HDF5Exception ex) {
			dbgInfo += "\n\nH5create() Failed, exception: "+ex;
			return false;
		}

		// Describe the size of the array and create the data space
		// for fixed size dataset.
		dataspace1 = -1;
		try {
			dataspace1 = H5.H5Screate_simple(rank, dimsf, null);
		} catch (Exception ex) { 
			dbgInfo += "\nH5Screate_simple: dataspace1 failed "+ex; 
			return false;
		}

		// Define datatype for the data in the file.
		datatype1 = -1;
		try {
			datatype1 = H5.H5Tcopy(H5.J2C(HDF5CDataTypes.JH5T_STD_I64LE));
		} catch (Exception ex) { 
			dbgInfo += "\nH5Tcopy: datatype1 failed "+ex; 
			return false; 
		}

		// Create a new dataset within the file using defined dataspace and
		// datatype and default dataset creation properties.
		try {
			dataset1 = H5.H5Dcreate(file, dataset1Name, 
				datatype1, dataspace1,
				HDF5Constants.H5P_DEFAULT);
		} catch (HDF5Exception ex) {
			dbgInfo += "\n\nH5Dcreate() dataset1 Failed, exception: "+ex;
			return false;
		}

		// Write the data to the dataset using default transfer properties.
		try {
			status = H5.H5Dwrite(dataset1,
			H5.J2C(HDF5CDataTypes.JH5T_STD_I64LE),
			HDF5Constants.H5S_ALL,
			HDF5Constants.H5S_ALL,
			HDF5Constants.H5P_DEFAULT,
			data);
		} catch (Exception ex) {
			dbgInfo += "\nH5Dwrite(): failed dataset1 "+ex;
			return false;
		}

		try {
			H5.H5Sclose(dataspace1);
		} catch (Exception ex) {
			dbgInfo += "\nH5Sclose: dataspace 1 failed. "+ex;
			return false;
		}
		try {
			H5.H5Tclose(datatype1);
		} catch (Exception ex) {
			dbgInfo += "\nH5Tclose: datatype1 failed. "+ex;
			return false;
		}
		try {
			H5.H5Dclose(dataset1);
		} catch (Exception ex) {
			dbgInfo += "\nH5Dclose: dataset1 failed. "+ex;
			return false;
		 }

		try {
			H5.H5Fclose(file);
		} catch (HDF5Exception ex) {
			dbgInfo += "\nH5Fclose(): FAILED "+ex;
			return false;
		}

 
  		tfile = -1;
  		try {
 		tfile = H5.H5Fopen(fileName,
                                 HDF5Constants.H5F_ACC_RDWR,
                                 HDF5Constants.H5P_DEFAULT);
 		} catch (HDF5Exception ex) {
 			dbgInfo += "\n\nH5Fopen() second open Failed, exception: "+ex;
 			return false;
 		}
 
 		try {
 			tdataset1 = H5.H5Dopen(tfile, dataset1Name); 
 		} catch (HDF5Exception ex) {
 			dbgInfo += "\n\nH5Dopen() dataset1 Failed, exception: "+ex;
 			return false;
 		}
 
		dbgInfo += "\nRead long LE data as long LE";
 		try {
 			status = H5.H5Dread(tdataset1,
 				H5.J2C(HDF5CDataTypes.JH5T_STD_I64LE),
 				HDF5Constants.H5S_ALL,
 				HDF5Constants.H5S_ALL,
 				HDF5Constants.H5P_DEFAULT,
 				outDataLELE);
 		} catch (Exception ex) {
			//ex.printStackTrace();
			dbgInfo += "\nH5Dread: failed. "+ex; 
			return false;
		}

		int errs = 0;
 		for (j = 0; (j < nx) && (errs < 10); j++)
 		{
 			for (i = 0; (i < ny) && (errs < 10); i++)  {
 				if (outDataLELE[j*ny+i] != data[j*ny+i]) {
					String indat = java.lang.Long.toHexString(data[j*ny+i]);
					String outdat = java.lang.Long.toHexString(outDataLELE[j*ny+i]);
 					dbgInfo += "\n* bad data at: ["+i+"]["+j+"]  original: "+data[j*ny+i]+" ( "+indat+" )  read back as "+outDataLELE[j*ny+i]+" ( "+outdat+" )";
					errs++;
 				}
 			}
 		}
 
 		try {
 			H5.H5Dclose(tdataset1);
 		} catch (Exception ex) {
			dbgInfo += "\nH5Dclose: dataset1 after read 1 failed. "+ex;
			return false;
		}


 		try {
 			H5.H5Fclose(tfile);
 		} catch (HDF5Exception ex) {
 			dbgInfo += "\nH5Fclose(): FAILED "+ex;
 			return false;
 		}
		if (errs > 0) {
			dbgInfo += "\nLongLELE() completed: "+errs+" errs.";
			return false;
		} else {
			dbgInfo += "\nLongLELE(): OK ";
			return true;
		}
 	}

	private boolean testLongLEBE()
	{
		String fileName = FILE_ROOT+"LongLEBE.h5";
		String dataset1Name = "longLEArray";
		int nx = 100;
		int ny = 200;
		int rank = 2;
		int status;
		int i, j;
		int file, dataset1, dataset2;
		int tfile, tdataset1, tdataset2;
		int datatype2, dataspace2;   /* handles */
		int datatype1, dataspace1;   /* handles */
		long[] dimsf = {100, 200};	 /* dataset dimensions */

		long[] data = new long[nx*ny];	/* data to write */
		long[] outDataLEBE = new long[nx*ny];

		// Data  and output buffer initialization.
		for (j = 0; j < nx; j++) {
			for (i = 0; i < ny; i++) {
				data[j*ny+i] = (long)(j*nx+i);
			}
		}


		// Create a new file using H5F_ACC_TRUNC access,
		// default file creation properties, and default file
		// access properties.
		file = -1;
		try {
		file = H5.H5Fcreate(fileName,
			HDF5Constants.H5F_ACC_TRUNC,
			HDF5Constants.H5P_DEFAULT,
			HDF5Constants.H5P_DEFAULT);
		} catch (HDF5Exception ex) {
			dbgInfo += "\n\nH5create() Failed, exception: "+ex;
			return false;
		}

		// Describe the size of the array and create the data space
		// for fixed size dataset.
		dataspace1 = -1;
		try {
			dataspace1 = H5.H5Screate_simple(rank, dimsf, null);
		} catch (Exception ex) { 
			dbgInfo += "\nH5Screate_simple: dataspace1 failed "+ex; 
			return false;
		}

		// Define datatype for the data in the file.
		datatype1 = -1;
		try {
			datatype1 = H5.H5Tcopy(H5.J2C(HDF5CDataTypes.JH5T_STD_I64LE));
		} catch (Exception ex) { 
			dbgInfo += "\nH5Tcopy: datatype1 failed "+ex; 
			return false; 
		}

		// Create a new dataset within the file using defined dataspace and
		// datatype and default dataset creation properties.
		try {
			dataset1 = H5.H5Dcreate(file, dataset1Name, 
				datatype1, dataspace1,
				HDF5Constants.H5P_DEFAULT);
		} catch (HDF5Exception ex) {
			dbgInfo += "\n\nH5Dcreate() dataset1 Failed, exception: "+ex;
			return false;
		}

		// Write the data to the dataset using default transfer properties.
		try {
			status = H5.H5Dwrite(dataset1,
			H5.J2C(HDF5CDataTypes.JH5T_STD_I64LE),
			HDF5Constants.H5S_ALL,
			HDF5Constants.H5S_ALL,
			HDF5Constants.H5P_DEFAULT,
			data);
		} catch (Exception ex) {
			dbgInfo += "\nH5Dwrite(): failed dataset1 "+ex;
			return false;
		}

		try {
			H5.H5Sclose(dataspace1);
		} catch (Exception ex) {
			dbgInfo += "\nH5Sclose: dataspace 1 failed. "+ex;
			return false;
		}
		try {
			H5.H5Tclose(datatype1);
		} catch (Exception ex) {
			dbgInfo += "\nH5Tclose: datatype1 failed. "+ex;
			return false;
		}
		try {
			H5.H5Dclose(dataset1);
		} catch (Exception ex) {
			dbgInfo += "\nH5Dclose: dataset1 failed. "+ex;
			return false;
		 }

		try {
			H5.H5Fclose(file);
		} catch (HDF5Exception ex) {
			dbgInfo += "\nH5Fclose(): FAILED "+ex;
			return false;
		}

 
  		tfile = -1;
  		try {
 		tfile = H5.H5Fopen(fileName,
                                 HDF5Constants.H5F_ACC_RDWR,
                                 HDF5Constants.H5P_DEFAULT);
 		} catch (HDF5Exception ex) {
 			dbgInfo += "\n\nH5Fopen() second open Failed, exception: "+ex;
 			return false;
 		}
 
 		try {
 			tdataset1 = H5.H5Dopen(tfile, dataset1Name); 
 		} catch (HDF5Exception ex) {
 			dbgInfo += "\n\nH5Dopen() dataset1 Failed, exception: "+ex;
 			return false;
 		}
 
		dbgInfo += "\nRead long LE data as long BE";
 		try {
 			status = H5.H5Dread(tdataset1,
 				H5.J2C(HDF5CDataTypes.JH5T_STD_I64BE),
 				HDF5Constants.H5S_ALL,
 				HDF5Constants.H5S_ALL,
 				HDF5Constants.H5P_DEFAULT,
 				outDataLEBE);
 		} catch (Exception ex) {
			//ex.printStackTrace();
			dbgInfo += "\nH5Dread: failed. "+ex; 
			return false;
		}

		int errs = 0;
 		for (j = 0; (j < nx) && (errs < 10); j++)
 		{
 			for (i = 0; (i < ny) && (errs < 10); i++)  {
 				if (outDataLEBE[j*ny+i] != data[j*ny+i]) {
					String indat = java.lang.Long.toHexString(data[j*ny+i]);
					String outdat = java.lang.Long.toHexString(outDataLEBE[j*ny+i]);
 					dbgInfo += "\n* bad data at: ["+i+"]["+j+"]  original: "+data[j*ny+i]+" ( "+indat+" )  read back as "+outDataLEBE[j*ny+i]+" ( "+outdat+" )";
					errs++;
 				}
 			}
 		}
 
 		try {
 			H5.H5Dclose(tdataset1);
 		} catch (Exception ex) {
			dbgInfo += "\nH5Dclose: dataset1 after read 1 failed. "+ex;
			return false;
		}


 		try {
 			H5.H5Fclose(tfile);
 		} catch (HDF5Exception ex) {
 			dbgInfo += "\nH5Fclose(): FAILED "+ex;
 			return false;
 		}
		if (errs > 0) {
			dbgInfo += "\nLongLEBE() completed: "+errs+" errs.";
			return false;
		} else {
			dbgInfo += "\nLongLEBE(): OK ";
			return true;
		}
 	}

}