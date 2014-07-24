/************************************************************

  This example shows how to read and write object references
  to an attribute.  The program first creates objects in the
  file and writes references to those objects to an
  attribute with a dataspace of DIM0, then closes the file.
  Next, it reopens the file, dereferences the references,
  and outputs the names of their targets to the screen.

  This file is intended for use with HDF5 Library verion 1.6

 ************************************************************/

package examples.datatypes;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

import ncsa.hdf.hdf5lib.H5;
import ncsa.hdf.hdf5lib.HDF5Constants;

public class H5Ex_T_ObjectReferenceAttribute {
	private static String FILENAME = "H5Ex_T_ObjectReferenceAttribute.h5";
	private static String DATASETNAME = "DS1";
	private static String ATTRIBUTENAME = "A1";
	private static String DATASETNAME2 = "DS2";
	private static String GROUPNAME = "G1";
	private static final int DIM0 = 2;
	private static final int RANK = 1;

	// Values for the status of space allocation
	enum H5G_obj {
		H5G_UNKNOWN(HDF5Constants.H5G_UNKNOWN), /* Unknown object type */
		H5G_GROUP(HDF5Constants.H5G_GROUP), /* Object is a group */
		H5G_DATASET(HDF5Constants.H5G_DATASET), /* Object is a dataset */
		H5G_TYPE(HDF5Constants.H5G_TYPE), /* Object is a named data type */
		H5G_LINK(HDF5Constants.H5G_LINK), /* Object is a symbolic link */
		H5G_UDLINK(HDF5Constants.H5G_UDLINK), /* Object is a user-defined link */
		H5G_RESERVED_5(HDF5Constants.H5G_RESERVED_5), /* Reserved for future use */
		H5G_RESERVED_6(HDF5Constants.H5G_RESERVED_6), /* Reserved for future use */
		H5G_RESERVED_7(HDF5Constants.H5G_RESERVED_7); /* Reserved for future use */
		private static final Map<Integer, H5G_obj> lookup = new HashMap<Integer, H5G_obj>();

		static {
			for (H5G_obj s : EnumSet.allOf(H5G_obj.class))
				lookup.put(s.getCode(), s);
		}

		private int code;

		H5G_obj(int layout_type) {
			this.code = layout_type;
		}

		public int getCode() {
			return this.code;
		}

		public static H5G_obj get(int code) {
			return lookup.get(code);
		}
	}

	private static void CreateDataset() {
		int file_id = -1;
		int dataspace_id = -1;
		int group_id = -1;
		int dataset_id = -1;
		int attribute_id = -1;
		long[] dims = { DIM0 };
		byte[][] dset_data = new byte[DIM0][8];

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
				dataset_id = H5.H5Dcreate(file_id, DATASETNAME2,
						HDF5Constants.H5T_STD_I32LE, dataspace_id,
						HDF5Constants.H5P_DEFAULT, HDF5Constants.H5P_DEFAULT, HDF5Constants.H5P_DEFAULT);
				if (dataset_id >= 0)
					H5.H5Dclose(dataset_id);
				dataset_id = -1;
				H5.H5Sclose(dataspace_id);
				dataspace_id = -1;
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		// Create a group in the file.
		try {
			if (file_id >= 0)
				group_id = H5.H5Gcreate(file_id, GROUPNAME, 
				        HDF5Constants.H5P_DEFAULT, HDF5Constants.H5P_DEFAULT, HDF5Constants.H5P_DEFAULT);
			if (group_id >= 0)
				H5.H5Gclose(group_id);
			group_id = -1;
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		// Create references to the previously created objects. Passing -1
		// as space_id causes this parameter to be ignored. Other values
		// besides valid dataspaces result in an error.
		try {
			if (file_id >= 0) {
				byte rbuf0[] = H5.H5Rcreate(file_id, GROUPNAME, HDF5Constants.H5R_OBJECT, -1);
				byte rbuf1[] = H5.H5Rcreate(file_id, DATASETNAME2, HDF5Constants.H5R_OBJECT, -1);
				for (int indx = 0; indx < 8; indx++) {
					dset_data[0][indx] = rbuf0[indx];
					dset_data[1][indx] = rbuf1[indx];
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		// Create dataset with a scalar dataspace to serve as the parent
		// for the attribute.
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
						HDF5Constants.H5T_STD_REF_OBJ, dataspace_id,
						HDF5Constants.H5P_DEFAULT, HDF5Constants.H5P_DEFAULT);
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		// Write the dataset.
		try {
			if (attribute_id >= 0)
				H5.H5Awrite(attribute_id, HDF5Constants.H5T_STD_REF_OBJ, dset_data);
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
		int object_type = -1;
		int object_id = -1;
		long[] dims = { DIM0 };
		byte[][] dset_data;

		// Open an existing file.
		try {
			file_id = H5.H5Fopen(FILENAME, HDF5Constants.H5F_ACC_RDONLY, HDF5Constants.H5P_DEFAULT);
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
				attribute_id = H5.H5Aopen_by_name(dataset_id, ".", ATTRIBUTENAME, 
				        HDF5Constants.H5P_DEFAULT, HDF5Constants.H5P_DEFAULT);
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
		dset_data = new byte[(int) dims[0]][8];

		// Read data.
		try {
			if (attribute_id >= 0)
				H5.H5Aread(attribute_id, HDF5Constants.H5T_STD_REF_OBJ, dset_data);
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		// Output the data to the screen.
		for (int indx = 0; indx < dims[0]; indx++) {
			System.out.println(ATTRIBUTENAME + "[" + indx + "]:");
			System.out.print("  ->");
			// Open the referenced object, get its name and type.
			try {
				if (dataset_id >= 0) {
			        int[] otype = { 1 };
					object_id = H5.H5Rdereference(dataset_id, HDF5Constants.H5P_DEFAULT, HDF5Constants.H5R_OBJECT, dset_data[indx]);
					object_type = H5.H5Rget_obj_type(dataset_id, HDF5Constants.H5R_OBJECT, dset_data[indx], otype);
				}
				String[] obj_name = new String[1];
				long name_size = 1;
				if (object_type >= 0) {
					// Get the length of the name and retrieve the name.
					name_size = 1 + H5.H5Iget_name(object_id, obj_name, name_size);
				}
				if ((object_id >= 0) && (object_type >= -1)) {
					switch (H5G_obj.get(object_type)) {
					case H5G_GROUP:
						System.out.print("H5G_GROUP");
						try {
							if (object_id >= 0)
								H5.H5Gclose(object_id);
						}
						catch (Exception e) {
							e.printStackTrace();
						}
						break;
					case H5G_DATASET:
						System.out.print("H5G_DATASET");
						try {
							if (object_id >= 0)
								H5.H5Dclose(object_id);
						}
						catch (Exception e) {
							e.printStackTrace();
						}
						break;
					case H5G_TYPE:
						System.out.print("H5G_TYPE");
						try {
							if (object_id >= 0)
								H5.H5Tclose(object_id);
						}
						catch (Exception e) {
							e.printStackTrace();
						}
						break;
					default:
						System.out.print("UNHANDLED");
					}
				}
				// Print the name.
				if (name_size > 1)
					System.out.println(": " + obj_name[0]);
			}
			catch (Exception e) {
				e.printStackTrace();
			}
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

	public static void main(String[] args) {
		H5Ex_T_ObjectReferenceAttribute.CreateDataset();
		// Now we begin the read section of this example. Here we assume
		// the dataset and array have the same name and rank, but can have
		// any size. Therefore we must allocate a new array to read in
		// data using malloc().
		H5Ex_T_ObjectReferenceAttribute.ReadDataset();
	}

}
