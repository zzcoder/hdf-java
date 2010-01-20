/************************************************************

  This example shows how to create a chunked dataset.  The
  program first writes integers in a hyperslab selection to
  a chunked dataset with dataspace dimensions of DIM_XxDIM_Y
  and chunk size of CHUNK_XxCHUNK_Y, then closes the file.
  Next, it reopens the file, reads back the data, and
  outputs it to the screen.  Finally it reads the data again
  using a different hyperslab selection, and outputs
  the result to the screen.

  This file is intended for use with HDF5 Library verion 1.6

 ************************************************************/
package datasets;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

import ncsa.hdf.hdf5lib.H5;
import ncsa.hdf.hdf5lib.HDF5Constants;

public class H5Ex_D_Chunk {
	private static String FILENAME = "h5ex_d_chunk.h5";
	private static String DATASETNAME = "DS1";
	private static final int DIM_X = 6;
	private static final int DIM_Y = 8;
	private static final int CHUNK_X = 4;
	private static final int CHUNK_Y = 4;
	private static final int RANK = 2;
	private static final int NDIMS = 2;

	// Values for the status of space allocation
	enum H5D_layout {
		H5D_LAYOUT_ERROR(-1), H5D_COMPACT(0), H5D_CONTIGUOUS(1), H5D_CHUNKED(2), H5D_NLAYOUTS(
				3);
		private static final Map<Integer, H5D_layout> lookup = new HashMap<Integer, H5D_layout>();

		static {
			for (H5D_layout s : EnumSet.allOf(H5D_layout.class))
				lookup.put(s.getCode(), s);
		}

		private int code;

		H5D_layout(int layout_type) {
			this.code = layout_type;
		}

		public int getCode() {
			return this.code;
		}

		public static H5D_layout get(int code) {
			return lookup.get(code);
		}
	}

	private static void writeChunk() {
		int file_id = -1;
		int filespace_id = -1;
		int dataset_id = -1;
		int dcpl_id = -1;
		long[] dims = { DIM_X, DIM_Y };
		long[] chunk_dims = { CHUNK_X, CHUNK_Y };
		int[][] dset_data = new int[DIM_X][DIM_Y];

		// Initialize data to "1", to make it easier to see the selections.
		for (int indx = 0; indx < DIM_X; indx++)
			for (int jndx = 0; jndx < DIM_Y; jndx++)
				dset_data[indx][jndx] = 1;

		// Print the data to the screen.
		System.out.println("Original Data:");
		for (int indx = 0; indx < DIM_X; indx++) {
			System.out.print(" [ ");
			for (int jndx = 0; jndx < DIM_Y; jndx++)
				System.out.print(dset_data[indx][jndx] + " ");
			System.out.println("]");
		}
		System.out.println();

		// Create a new file using default properties.
		try {
			file_id = H5.H5Fcreate(FILENAME, HDF5Constants.H5F_ACC_TRUNC,
					HDF5Constants.H5P_DEFAULT, HDF5Constants.H5P_DEFAULT);
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		// Create dataspace. Setting maximum size to NULL sets the maximum
		// size to be the current size.
		try {
			filespace_id = H5.H5Screate_simple(RANK, dims, null);
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		// Create the dataset creation property list.
		try {
			dcpl_id = H5.H5Pcreate(HDF5Constants.H5P_DATASET_CREATE);
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		// Set the chunk size.
		try {
			if (dcpl_id >= 0)
				H5.H5Pset_chunk(dcpl_id, NDIMS, chunk_dims);
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		// Create the chunked dataset.
		try {
			if ((file_id >= 0) && (filespace_id >= 0) && (dcpl_id >= 0))
				dataset_id = H5.H5Dcreate(file_id, DATASETNAME,
						HDF5Constants.H5T_STD_I32LE, filespace_id, HDF5Constants.H5P_DEFAULT, dcpl_id, HDF5Constants.H5P_DEFAULT);
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		// Define and select the first part of the hyperslab selection.
		long[] start = { 0, 0 };
		long[] stride = { 3, 3 };
		long[] count = { 2, 3 };
		long[] block = { 2, 2 };
		try {
			if ((filespace_id >= 0))
				H5.H5Sselect_hyperslab(filespace_id, HDF5Constants.H5S_SELECT_SET,
						start, stride, count, block);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		// Define and select the second part of the hyperslab selection,
		// which is subtracted from the first selection by the use of
		// H5S_SELECT_NOTB
		block[0] = 1;
		block[1] = 1;
		try {
			if ((filespace_id >= 0)) {
				H5.H5Sselect_hyperslab(filespace_id, HDF5Constants.H5S_SELECT_NOTB,
						start, stride, count, block);

				// Write the data to the dataset.
				if (dataset_id >= 0)
					H5.H5Dwrite(dataset_id, HDF5Constants.H5T_NATIVE_INT,
							HDF5Constants.H5S_ALL, filespace_id, HDF5Constants.H5P_DEFAULT,
							dset_data);
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
			if (dataset_id >= 0)
				H5.H5Dclose(dataset_id);
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		try {
			if (filespace_id >= 0)
				H5.H5Sclose(filespace_id);
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

	private static void readChunk() {
		int file_id = -1;
		int filespace_id = -1;
		int dataset_id = -1;
		int dcpl_id = -1;
		int[][] dset_data = new int[DIM_X][DIM_Y];

		// Open an existing file.
		try {
			file_id = H5.H5Fopen(FILENAME, HDF5Constants.H5F_ACC_RDONLY,
					HDF5Constants.H5P_DEFAULT);
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		// Open an existing dataset.
		try {
			if (file_id >= 0)
				dataset_id = H5.H5Dopen(file_id, DATASETNAME, HDF5Constants.H5P_DEFAULT);
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		// Retrieve the dataset creation property list.
		try {
			if (dataset_id >= 0)
				dcpl_id = H5.H5Dget_create_plist(dataset_id);
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		// Print the storage layout.
		try {
			if (dcpl_id >= 0) {
				int layout_type = H5.H5Pget_layout(dcpl_id);
				System.out.print("Storage layout for " + DATASETNAME + " is: ");
				switch (H5D_layout.get(layout_type)) {
				case H5D_COMPACT:
					System.out.println("H5D_COMPACT");
					break;
				case H5D_CONTIGUOUS:
					System.out.println("H5D_CONTIGUOUS");
					break;
				case H5D_CHUNKED:
					System.out.println("H5D_CHUNKED");
					break;
				}
				System.out.println();
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		// Read the data using the default properties.
		try {
			if (dataset_id >= 0)
				H5.H5Dread(dataset_id, HDF5Constants.H5T_NATIVE_INT,
						HDF5Constants.H5S_ALL, HDF5Constants.H5S_ALL,
						HDF5Constants.H5P_DEFAULT, dset_data);
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		// Output the data to the screen.
		System.out.println("Data as written to disk by hyberslabs:");
		for (int indx = 0; indx < DIM_X; indx++) {
			System.out.print(" [ ");
			for (int jndx = 0; jndx < DIM_Y; jndx++)
				System.out.print(dset_data[indx][jndx] + " ");
			System.out.println("]");
		}
		System.out.println();

		// Initialize the read array.
		for (int indx = 0; indx < DIM_X; indx++)
			for (int jndx = 0; jndx < DIM_Y; jndx++)
				dset_data[indx][jndx] = 0;

		// Define and select the hyperslab to use for reading.
		try {
			if (dataset_id >= 0) {
				filespace_id = H5.H5Dget_space(dataset_id);

				long[] start = { 0, 1 };
				long[] stride = { 4, 4 };
				long[] count = { 2, 2 };
				long[] block = { 2, 3 };

				if (filespace_id >= 0) {
					H5.H5Sselect_hyperslab(filespace_id, HDF5Constants.H5S_SELECT_SET,
							start, stride, count, block);

					// Read the data using the previously defined hyperslab.
					if ((dataset_id >= 0) && (filespace_id >= 0))
						H5.H5Dread(dataset_id, HDF5Constants.H5T_NATIVE_INT,
								HDF5Constants.H5S_ALL, filespace_id, HDF5Constants.H5P_DEFAULT,
								dset_data);
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		// Output the data to the screen.
		System.out.println("Data as read from disk by hyberslab:");
		for (int indx = 0; indx < DIM_X; indx++) {
			System.out.print(" [ ");
			for (int jndx = 0; jndx < DIM_Y; jndx++)
				System.out.print(dset_data[indx][jndx] + " ");
			System.out.println("]");
		}
		System.out.println();

		// End access to the dataset and release resources used by it.
		try {
			if (dcpl_id >= 0)
				H5.H5Pclose(dcpl_id);
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		try {
			if (dataset_id >= 0)
				H5.H5Dclose(dataset_id);
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		try {
			if (filespace_id >= 0)
				H5.H5Sclose(filespace_id);
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
		H5Ex_D_Chunk.writeChunk();
		H5Ex_D_Chunk.readChunk();
	}

}
