/************************************************************
 This example shows how to read and write data to a dataset
 using the N-Bit filter.  The program first checks if the
 N-Bit filter is available, then if it is it writes integers
 to a dataset using N-Bit, then closes the file. Next, it 
 reopens the file, reads back the data, and outputs the type
 of filter and the maximum value in the dataset to the screen.
************************************************************/

package examples.datasets;

import examples.datasets.H5Ex_D_Checksum.H5Z_filter;
import ncsa.hdf.hdf5lib.H5;
import ncsa.hdf.hdf5lib.HDF5Constants;

public class H5Ex_D_Nbit {

	private static String FILE = "h5ex_d_nbit.h5";
	private static String DATASET = "DS1";
	private static final int DIM_X = 32;
	private static final int DIM_Y = 64;
	private static final int CHUNK_X = 4;
	private static final int CHUNK_Y = 8;
	
	private static boolean checkNbitFilter() {
		try {
			//Check if N-Bit compression is available and can be used for both compression and decompression. 
			int available = H5.H5Zfilter_avail(HDF5Constants.H5Z_FILTER_NBIT);
			if (available ==0) {
				System.out.println("N-Bit filter not available.");
				return false;
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		try {
			int filter_info = H5.H5Zget_filter_info (HDF5Constants.H5Z_FILTER_NBIT);
			if (((filter_info & HDF5Constants.H5Z_FILTER_CONFIG_ENCODE_ENABLED) == 0)
					|| ((filter_info & HDF5Constants.H5Z_FILTER_CONFIG_DECODE_ENABLED) == 0)) {
				System.out.println("N-Bit filter not available for encoding and decoding.");
				return false;
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}
	
	private static void writeData() throws Exception {
		int file_id = -1;
		int space_id = -1;
		int dset_id = -1;
		int dtype_id = -1;
		int dcpl_id = -1;
		long[] dims = { DIM_X, DIM_Y };
		long[] chunks = {CHUNK_X, CHUNK_Y };
		int[][] dset_data = new int[DIM_X][DIM_Y]; 

		//Initialize data.
		for (int i=0; i<DIM_X; i++)
			for (int j=0; j<DIM_Y; j++)
				dset_data[i][j] = i * j - j;
		try{
			//Create a new file using the default properties.
			file_id = H5.H5Fcreate(FILE, HDF5Constants.H5F_ACC_TRUNC, HDF5Constants.H5P_DEFAULT, HDF5Constants.H5P_DEFAULT);

			//Create dataspace.  Setting maximum size to NULL sets the maximum
			// size to be the current size.
			space_id = H5.H5Screate_simple (2, dims, null);

			//Create the datatype to use with the N-Bit filter.  It has an uncompressed size of 32 bits,
			//but will have a size of 16 bits after being packed by the N-Bit filter.
			dtype_id = H5.H5Tcopy(HDF5Constants.H5T_STD_I32LE);
			H5.H5Tset_precision (dtype_id, 16);
			H5.H5Tset_offset (dtype_id, 5);

			//Create the dataset creation property list, add the N-Bit filter and set the chunk size.
			dcpl_id= H5.H5Pcreate (HDF5Constants.H5P_DATASET_CREATE);
			H5.H5Pset_nbit (dcpl_id);
			H5.H5Pset_chunk (dcpl_id, 2, chunks);

			//Create the dataset.
			dset_id = H5.H5Dcreate (file_id, DATASET, dtype_id, space_id, HDF5Constants.H5P_DEFAULT, dcpl_id,
					HDF5Constants.H5P_DEFAULT);

			//Write the data to the dataset.
			H5.H5Dwrite(dset_id, HDF5Constants.H5T_NATIVE_INT, HDF5Constants.H5S_ALL, HDF5Constants.H5S_ALL, HDF5Constants.H5P_DEFAULT,
					dset_data);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			//Close and release resources.
			if(dcpl_id>=0)
				H5.H5Pclose (dcpl_id);
			if(dtype_id>=0)
				H5.H5Tclose (dtype_id);
			if(dset_id>=0)
				H5.H5Dclose (dset_id);
			if(space_id>=0)
				H5.H5Sclose (space_id);
			if(file_id>=0)
				H5.H5Fclose (file_id);
		}
	}

	private static void readData() throws Exception {
		int file_id = -1;
		int dset_id = -1;
		int dcpl_id = -1;
		int[][] dset_data = new int[DIM_X][DIM_Y]; 

		// Open an existing file.
		try {
			file_id = H5.H5Fopen (FILE, HDF5Constants.H5F_ACC_RDONLY, HDF5Constants.H5P_DEFAULT);
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		// Open an existing dataset.
		try {
			if (file_id >= 0)
				dset_id = H5.H5Dopen (file_id, DATASET, HDF5Constants.H5P_DEFAULT);
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		// Retrieve the dataset creation property list.
		try {
			if (dset_id >= 0)
				dcpl_id = H5.H5Dget_create_plist (dset_id);
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		//Retrieve and print the filter type.  Here we only retrieve the
		//first filter because we know that we only added one filter.
		try{
			if(dcpl_id>=0){
				int[] nelmts = { 0 };
				int[] flags = { 0 };
				int[] cd_values = { 0 };
				String[] filter_name = { "filter" };
				int filter_type = -1;
				filter_type = H5.H5Pget_filter(dcpl_id, 0, flags, nelmts, cd_values, 6, filter_name);
				System.out.print("Filter type is: ");
				switch (H5Z_filter.get(filter_type)) {
				case H5Z_FILTER_DEFLATE:
					System.out.println("H5Z_FILTER_DEFLATE");
					break;
				case H5Z_FILTER_SHUFFLE:
					System.out.println("H5Z_FILTER_SHUFFLE");
					break;
				case H5Z_FILTER_FLETCHER32:
					System.out.println("H5Z_FILTER_FLETCHER32");
					break;
				case H5Z_FILTER_SZIP:
					System.out.println("H5Z_FILTER_SZIP");
					break;
				case H5Z_FILTER_NBIT:
					System.out.println("H5Z_FILTER_NBIT");
					break;
				case H5Z_FILTER_SCALEOFFSET:
					System.out.println("H5Z_FILTER_SCALEOFFSET");
					break;	
				default:
					System.out.println("H5Z_FILTER_ERROR");
				}
				System.out.println();
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		// Read the data using the default properties.
		try {
			if (dset_id >= 0) {
				int status = H5.H5Dread(dset_id, HDF5Constants.H5T_NATIVE_INT,
						HDF5Constants.H5S_ALL, HDF5Constants.H5S_ALL,
						HDF5Constants.H5P_DEFAULT, dset_data);
				// Check if the read was successful.
				if (status < 0) {
					System.out.print("Dataset read failed!");
					try {
						if (dcpl_id >= 0)
							H5.H5Pclose(dcpl_id);
						if (dset_id >= 0)
							H5.H5Dclose(dset_id);
						if (file_id >= 0)
							H5.H5Fclose(file_id);
					}
					catch (Exception e) {
						e.printStackTrace();
					}
					return;
				}
				//Find the maximum value in the dataset, to verify that it was read correctly.
				
				int max = dset_data[0][0];
				for (int i=0; i<DIM_X; i++)
					for (int j=0; j<DIM_Y; j++)
						if (max < dset_data[i][j])
							max = dset_data[i][j];

				//Print the maximum value.
				System.out.println("Maximum value in " + DATASET + " is: " + max);
				
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		// End access to the dataset and release resources used by it.
		try {
			if (dcpl_id >= 0)
				H5.H5Pclose(dcpl_id);
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		try {
			if (dset_id >= 0)
				H5.H5Dclose(dset_id);
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
	
	public static void main(String[] args) {
		/*
		 * Check if N-Bit compression is available and can be used for both
		 * compression and decompression.  Normally we do not perform error
		 * checking in these examples for the sake of clarity, but in this
		 * case we will make an exception because this filter is an
		 * optional part of the hdf5 library.
		 */
		try{
			if (H5Ex_D_Nbit.checkNbitFilter()) {
				H5Ex_D_Nbit.writeData();
				H5Ex_D_Nbit.readData();
			}
		}catch(Exception ex) {
			ex.printStackTrace();
		}
	}
}
