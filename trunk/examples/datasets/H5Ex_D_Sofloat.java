    /************************************************************
      This example shows how to read and write data to a dataset
      using the Scale-Offset filter.  The program first checks
      if the Scale-Offset filter is available, then if it is it
      writes floating point numbers to a dataset using
      Scale-Offset, then closes the file Next, it reopens the
      file, reads back the data, and outputs the type of filter
      and the maximum value in the dataset to the screen.

      This file is intended for use with HDF5 Library version 1.8
     ************************************************************/
package examples.datasets;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

import ncsa.hdf.hdf5lib.H5;
import ncsa.hdf.hdf5lib.HDF5Constants;

public class H5Ex_D_Sofloat {

    private static String FILE = "h5ex_d_sofloat.h5";
    private static String DATASET = "DS1";

    private static final int DIM_X = 32;
    private static final int DIM_Y = 64;
    private static final int CHUNK_X = 4;
    private static final int CHUNK_Y = 8;

    // Values for the status of space allocation
    enum H5Z_filter {
        H5Z_FILTER_ERROR(-1), H5Z_FILTER_NONE(0), H5Z_FILTER_DEFLATE(1), H5Z_FILTER_SHUFFLE(
                2), H5Z_FILTER_FLETCHER32(3), H5Z_FILTER_SZIP(4), H5Z_FILTER_NBIT(5), H5Z_FILTER_SCALEOFFSET(
                6), H5Z_FILTER_RESERVED(256), H5Z_FILTER_MAX(65535);
        private static final Map<Integer, H5Z_filter> lookup = new HashMap<Integer, H5Z_filter>();

        static {
            for (H5Z_filter s : EnumSet.allOf(H5Z_filter.class))
                lookup.put(s.getCode(), s);
        }

        private int code;

        H5Z_filter(int layout_type) {
            this.code = layout_type;
        }

        public int getCode() {
            return this.code;
        }

        public static H5Z_filter get(int code) {
            return lookup.get(code);
        }
    }

    private static boolean checkScaleoffsetFilter() {
        try {
            int available = H5.H5Zfilter_avail(HDF5Constants.H5Z_FILTER_SCALEOFFSET);
            if (available == 0) {
                System.out.println("Scale-Offset filter not available.");
                return false;
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        try {
            int filter_info = H5.H5Zget_filter_info(HDF5Constants.H5Z_FILTER_SCALEOFFSET);
            if (((filter_info & HDF5Constants.H5Z_FILTER_CONFIG_ENCODE_ENABLED) == 0)
                    || ((filter_info & HDF5Constants.H5Z_FILTER_CONFIG_DECODE_ENABLED) == 0)) {
                System.out.println("Scale-Offset filter not available for encoding and decoding.");
                return false;
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    private static void writeData() {
        int file_id = -1;
        int space_id = -1;
        int dset_id = -1;
        int dcpl_id = -1;
        long[] dims = { DIM_X, DIM_Y };
        long[] chunk_dims = { CHUNK_X, CHUNK_Y };
        double[][] dset_data = new double[DIM_X][DIM_Y];
        double          max, min;
        int             i, j;
        double             x,y;

        //Initialize data.
        for (i=0; i<DIM_X; i++)
            for (j=0; j<DIM_Y; j++)
            {
                x = i;
                y = j;
                dset_data[i][j] = (x + 1) / (y + 0.3) + y;
            }

        //Find the maximum value in the dataset, to verify that it was read correctly.
        max = dset_data[0][0];
        min = dset_data[0][0];
        for (i=0; i<DIM_X; i++)
            for (j=0; j<DIM_Y; j++) {
                if (max < dset_data[i][j])
                    max = dset_data[i][j];
                if (min > dset_data[i][j])
                    min = dset_data[i][j];
            }

        //Print the maximum value.
        System.out.println("Maximum value in write buffer is: " + max);
        System.out.println("Minimum value in write buffer is: " + min);

        //Create a new file using the default properties.
        try{
            file_id = H5.H5Fcreate (FILE, HDF5Constants.H5F_ACC_TRUNC, HDF5Constants.H5P_DEFAULT, HDF5Constants.H5P_DEFAULT);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        //Create dataspace.  Setting maximum size to NULL sets the maximum  size to be the current size.
        try{
            space_id = H5.H5Screate_simple (2, dims, null);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        //Create the dataset creation property list, add the Scale-Offset
        // filter and set the chunk size.
        try{
            dcpl_id = H5.H5Pcreate (HDF5Constants.H5P_DATASET_CREATE);
            if(dcpl_id>=0){
                H5.H5Pset_scaleoffset(dcpl_id, HDF5Constants.H5Z_SO_FLOAT_DSCALE, 2);
                H5.H5Pset_chunk(dcpl_id, 2, chunk_dims);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        //Create the dataset.
        try{
            if ((file_id >= 0) && (space_id >= 0) && (dcpl_id >= 0))
                dset_id = H5.H5Dcreate(file_id, DATASET, HDF5Constants.H5T_IEEE_F64LE, space_id, HDF5Constants.H5P_DEFAULT, dcpl_id,
                        HDF5Constants.H5P_DEFAULT);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        //Write the data to the dataset.
        try{
            if(dset_id>=0)
                H5.H5Dwrite(dset_id, HDF5Constants.H5T_NATIVE_DOUBLE, HDF5Constants.H5S_ALL, HDF5Constants.H5S_ALL, HDF5Constants.H5P_DEFAULT,
                        dset_data);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        //Close and release resources.
        try{
            if(dcpl_id>=0)
                H5.H5Pclose (dcpl_id);
        }
        catch (Exception e) {
            e.printStackTrace();
        }    

        try{
            if(dset_id>=0)
                H5.H5Dclose (dset_id);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        try{
            if(space_id>=0)
                H5.H5Sclose (space_id);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        //Close file
        try{
            if(file_id>=0)
                H5.H5Fclose (file_id);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void readData() {
        int file_id = -1;
        int dset_id = -1;
        int dcpl_id = -1;
        double[][] dset_data = new double[DIM_X][DIM_Y];
        double          max, min;
        int             i, j;

        // Open file using the default properties.
        try{
            file_id = H5.H5Fopen (FILE, HDF5Constants.H5F_ACC_RDONLY, HDF5Constants.H5P_DEFAULT);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        // Open dataset using the default properties.
        try{
            if(file_id>=0)
                dset_id = H5.H5Dopen (file_id, DATASET, HDF5Constants.H5P_DEFAULT);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        //Retrieve dataset creation property list.
        try{
            if(dset_id>=0)
                dcpl_id = H5.H5Dget_create_plist(dset_id);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        //Retrieve and print the filter type.  Here we only retrieve the
        // first filter because we know that we only added one filter.
        try {
            if (dcpl_id >= 0) {
                // Java lib requires a valid filter_name object and cd_values
                int[] flags = { 0 };
                long[] cd_nelmts = { 1 };
                int[] cd_values = { 0 };
                String[] filter_name = { "" };
                int[] filter_config = { 0 };
                int filter_type = -1;

                filter_type = H5.H5Pget_filter(dcpl_id, 0, flags, cd_nelmts, cd_values,
                        10, filter_name, filter_config);
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

        //Read the data using the default properties.
        try{
            if(dset_id>=0)
                H5.H5Dread (dset_id, HDF5Constants.H5T_NATIVE_DOUBLE, HDF5Constants.H5S_ALL, HDF5Constants.H5S_ALL, HDF5Constants.H5P_DEFAULT,
                        dset_data);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        //Find the maximum value in the dataset, to verify that it was read correctly.
        max = dset_data[0][0];
        min = dset_data[0][0];
        for (i=0; i<DIM_X; i++)
            for (j=0; j<DIM_Y; j++) {
                if (max < dset_data[i][j])
                    max = dset_data[i][j];
                if (min > dset_data[i][j])
                    min = dset_data[i][j];
            }

        //Print the maximum value.
        System.out.println("Maximum value in "+ DATASET + " is: " + max);
        System.out.println("Minimum value in " + DATASET + " is: " + min);

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

        //Check if Scale-Offset compression is available and can be used
        //for both compression and decompression.  Normally we do not
        //perform error checking in these examples for the sake of
        //clarity, but in this case we will make an exception because this
        //filter is an optional part of the hdf5 library.
        if (H5Ex_D_Sofloat.checkScaleoffsetFilter()) {
            H5Ex_D_Sofloat.writeData();
            H5Ex_D_Sofloat.readData();
        }
    }
}
