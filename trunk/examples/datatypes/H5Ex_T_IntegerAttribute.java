/************************************************************

  This example shows how to read and write integer datatypes
  to an attribute.  The program first writes integers to an
  attribute with a dataspace of DIM0xDIM1, then closes the
  file.  Next, it reopens the file, reads back the data, and
  outputs it to the screen.

  This file is intended for use with HDF5 Library verion 1.6

 ************************************************************/

package datatypes;

import java.text.DecimalFormat;

import ncsa.hdf.hdf5lib.H5;
import ncsa.hdf.hdf5lib.HDF5Constants;

public class H5Ex_T_IntegerAttribute {
	private static String FILENAME = "h5ex_t_intatt.h5";
	private static String DATASETNAME = "DS1";
	private static String ATTRIBUTENAME = "A1";
	private static final int DIM0 = 4;
	private static final int DIM1 = 7;
	private static final int RANK = 2;

	private static void CreateDataset() {
		int file_id = -1;
		int dataspace_id = -1;
		int dataset_id = -1;
		int attribute_id = -1;
		long[] dims = { DIM0, DIM1 };
		int[][] dset_data = new int[DIM0][DIM1];

		// Initialize data.
		for (int indx = 0; indx < DIM0; indx++)
			for (int jndx = 0; jndx < DIM1; jndx++) {
				dset_data[indx][jndx] = indx * jndx - jndx;
			}

		// Create a new file using default properties.
		try {
			file_id = H5.H5Fcreate(FILENAME, HDF5Constants.H5F_ACC_TRUNC,
					HDF5Constants.H5P_DEFAULT, HDF5Constants.H5P_DEFAULT);
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		// Create dataset with a scalar dataspace.
		try {
			dataspace_id = H5.H5Screate(HDF5Constants.H5S_SCALAR);
			if (dataspace_id >= 0) {
				dataset_id = H5.H5Dcreate(file_id, DATASETNAME,
						HDF5Constants.H5T_STD_I32LE, dataspace_id,
						HDF5Constants.H5P_DEFAULT, HDF5Constants.H5P_DEFAULT, HDF5Constants.H5P_DEFAULT);
				H5.H5Sclose(dataspace_id);
				dataspace_id = -1;
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		// Create dataspace. Setting maximum size to NULL sets the maximum
		// size to be the current size.
		try {
			dataspace_id = H5.H5Screate_simple(RANK, dims, null);
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		// Create the attribute and write the array data to it.
		try {
			if ((dataset_id >= 0) && (dataspace_id >= 0))
				attribute_id = H5.H5Acreate(dataset_id, ATTRIBUTENAME,
						HDF5Constants.H5T_STD_I64BE, dataspace_id,
						HDF5Constants.H5P_DEFAULT);
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		// Write the dataset.
		try {
			if (attribute_id >= 0)
				H5.H5Awrite(attribute_id, HDF5Constants.H5T_NATIVE_INT, dset_data);
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		// End access to the dataset and release resources used by it.
		try {
			if (attribute_id >= 0)
				H5.H5Aclose(attribute_id);
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

		// Terminate access to the data space.
		try {
			if (dataspace_id >= 0)
				H5.H5Sclose(dataspace_id);
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

	private static void ReadDataset() {
		int file_id = -1;
		int dataspace_id = -1;
		int dataset_id = -1;
		int attribute_id = -1;
		long[] dims = { DIM0, DIM1 };
		int[][] dset_data;

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

		try {
			if (dataset_id >= 0)
				attribute_id = H5.H5Aopen_name(dataset_id, ATTRIBUTENAME);
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		// Get dataspace and allocate memory for read buffer.
		try {
			if (attribute_id >= 0)
				dataspace_id = H5.H5Aget_space(attribute_id);
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		try {
			if (dataspace_id >= 0)
				H5.H5Sget_simple_extent_dims(dataspace_id, dims, null);
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		// Allocate array of pointers to two-dimensional arrays (the
		// elements of the dataset.
		dset_data = new int[(int) dims[0]][(int) (dims[1])];

		// Read data.
		try {
			if (attribute_id >= 0)
				H5.H5Aread(attribute_id, HDF5Constants.H5T_NATIVE_INT, dset_data);
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		// Output the data to the screen.
		DecimalFormat df = new DecimalFormat("#,##0");
		System.out.println(ATTRIBUTENAME + ":");
		for (int indx = 0; indx < dims[0]; indx++) {
			System.out.print(" [");
			for (int jndx = 0; jndx < dims[1]; jndx++) {
				System.out.print(" " + df.format(dset_data[indx][jndx]));
			}
			System.out.println("]");
		}
		System.out.println();

		// End access to the dataset and release resources used by it.
		try {
			if (attribute_id >= 0)
				H5.H5Aclose(attribute_id);
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

		// Terminate access to the data space.
		try {
			if (dataspace_id >= 0)
				H5.H5Sclose(dataspace_id);
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
		H5Ex_T_IntegerAttribute.CreateDataset();
		// Now we begin the read section of this example. Here we assume
		// the dataset and array have the same name and rank, but can have
		// any size. Therefore we must allocate a new array to read in
		// data using malloc().
		H5Ex_T_IntegerAttribute.ReadDataset();
	}

}
