//
//   Creating and closing a dataset.

package examples.intro;

import ncsa.hdf.object.Datatype;
import ncsa.hdf.object.FileFormat;
import ncsa.hdf.object.h5.H5Datatype;
import ncsa.hdf.object.h5.H5File;
import ncsa.hdf.object.h5.H5Group;
import ncsa.hdf.object.h5.H5ScalarDS;


public class H5Object_CreateDataset {
	private static String FILENAME = "H5Object_CreateDataset.h5";
	private static String DATASETNAME = "dset";
	private static final int DIM_X = 4;
	private static final int DIM_Y = 6;
    private static final int DATATYPE_SIZE = 4;

	private static void CreateDataset() {
        H5File file = null;
        H5ScalarDS dset = null;
        int[][] dset_data = new int[DIM_X][DIM_Y];
		long[] dims = { DIM_X, DIM_Y };
        final H5Datatype typeInt = new H5Datatype(Datatype.CLASS_INTEGER,
                DATATYPE_SIZE, Datatype.ORDER_BE, -1);

		// Create a new file using default properties.
		try {
            file = new H5File(FILENAME, FileFormat.CREATE);
            file.open();
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		// Create the dataset.
		try {
            final H5Group rootGrp = (H5Group)file.get("/");
            dset = H5ScalarDS.create("/" + DATASETNAME, rootGrp, typeInt,
                    dims, null, null, 0,
                    dset_data);
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		// Close the file.
		try {
            file.close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static void main(String[] args) {
		H5Object_CreateDataset.CreateDataset();
	}

}
