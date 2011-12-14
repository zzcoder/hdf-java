package examples.datatypes;

import ncsa.hdf.hdf5lib.H5;
import ncsa.hdf.hdf5lib.HDF5Constants;

public class H5Ex_T_VLString 
{
    private static String FILENAME = "H5Ex_T_VLString.h5";
    private static String DATASETNAME = "DS1";

    private static void createDataset() {
        int fid=-1, tid=-1, sid =-1, did=-1;
        String[] str_data = { "Parting", "is such", "sweet", "sorrow." };
        int rank = 1;
        long[] dims = { str_data.length };

        // Create a new file using default properties.
        try {
            fid = H5.H5Fcreate(FILENAME, HDF5Constants.H5F_ACC_TRUNC,
                    HDF5Constants.H5P_DEFAULT, HDF5Constants.H5P_DEFAULT);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        try {
            tid = H5.H5Tcopy(HDF5Constants.H5T_C_S1);
               H5.H5Tset_size(tid, HDF5Constants.H5T_VARIABLE);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        
        // Create dataspace. Setting maximum size to NULL sets the maximum
        // size to be the current size.
        try {
            sid = H5.H5Screate_simple(rank, dims, null);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        // Create the dataset and write the string data to it.
        try {
            if ((fid >= 0) && (tid >= 0) && (sid >= 0)) {
                did = H5.H5Dcreate(fid, DATASETNAME, tid, sid, HDF5Constants.H5P_DEFAULT, HDF5Constants.H5P_DEFAULT, HDF5Constants.H5P_DEFAULT);
             }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        // Write the data to the dataset.
        try {
            if (did >=0)
                H5.H5DwriteString(did, tid, HDF5Constants.H5S_ALL, HDF5Constants.H5S_ALL, HDF5Constants.H5P_DEFAULT, str_data);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        try  {
            H5.H5Sclose(sid);
            H5.H5Tclose(tid);
            H5.H5Dclose(did);
            H5.H5Fclose(fid);         
        } catch (Exception e) {e.printStackTrace(); }
    }
    
    private static void readDataset() {
        int fid=-1, tid=-1, did=-1;
        String[] str_data = { "", "", "", "" };

         try {
            fid = H5.H5Fopen(FILENAME, HDF5Constants.H5F_ACC_RDONLY, HDF5Constants.H5P_DEFAULT);;
        } catch (Exception e) { e.printStackTrace(); }

        try {
             did = H5.H5Dopen(fid, DATASETNAME, HDF5Constants.H5P_DEFAULT);
             tid = H5.H5Dget_type(did);
             H5.H5DreadVL(did, tid, HDF5Constants.H5S_ALL, HDF5Constants.H5S_ALL, HDF5Constants.H5P_DEFAULT, str_data);
        } catch (Exception e) { e.printStackTrace(); }
        
        for (int i=0; i<str_data.length; i++)
            System.out.println(DATASETNAME + " [" + i + "]: " + str_data[i]);

        try  {
             H5.H5Tclose(tid);
            H5.H5Dclose(did);
            H5.H5Fclose(fid);         
        } catch (Exception e) {e.printStackTrace(); }
    }    

    public static void main(String[] args) {
        H5Ex_T_VLString.createDataset();
        H5Ex_T_VLString.readDataset();
    }

}
