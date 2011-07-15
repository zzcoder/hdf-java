/************************************************************
This example shows how to recursively traverse a file
using H5Ovisit and H5Lvisit.  The program prints all of
the objects in the file specified in FILE, then prints all
of the links in that file.  The default file used by this
example implements the structure described in the User's
Guide, chapter 4, figure 26.

This file is intended for use with HDF5 Library version 1.8
************************************************************/
package examples.groups;

import java.util.ArrayList;
import ncsa.hdf.hdf5lib.H5;
import ncsa.hdf.hdf5lib.HDF5Constants;
import ncsa.hdf.hdf5lib.callbacks.H5L_iterate_cb;
import ncsa.hdf.hdf5lib.callbacks.H5L_iterate_t;
import ncsa.hdf.hdf5lib.callbacks.H5O_iterate_cb;
import ncsa.hdf.hdf5lib.callbacks.H5O_iterate_t;
import ncsa.hdf.hdf5lib.structs.H5L_info_t;
import ncsa.hdf.hdf5lib.structs.H5O_info_t;


public class H5Ex_G_Visit {

	private static String FILE = "groups/h5ex_g_visit.h5";

		private static void VisitGroup() throws Exception {

		int file_id = -1;

		try{
			//Open file
			file_id = H5.H5Fopen(FILE, HDF5Constants.H5F_ACC_RDONLY, HDF5Constants.H5P_DEFAULT);

			//Begin iteration using H5Ovisit
			System.out.println("Objects in the file:");
			H5O_iterate_t iter_data = new H5O_iter_data();
			H5O_iterate_cb iter_cb = new H5O_iter_callback();
			H5.H5Ovisit(file_id, HDF5Constants.H5_INDEX_NAME, HDF5Constants.H5_ITER_NATIVE, iter_cb, iter_data);

			//Repeat the same process using H5Lvisit
			H5L_iterate_t iter_data2 = new H5L_iter_data();
			H5L_iterate_cb iter_cb2 = new H5L_iter_callback();
			System.out.println ("\nLinks in the file:");
			H5.H5Lvisit(file_id, HDF5Constants.H5_INDEX_NAME, HDF5Constants.H5_ITER_NATIVE, iter_cb2, iter_data2);
			
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally{
			//Close and release resources.
			if(file_id>=0) 
				H5.H5Fclose (file_id);
		}
	}
		public static void main(String[] args) {
			try{
				H5Ex_G_Visit.VisitGroup();
			}catch(Exception ex) {
				ex.printStackTrace();
			}
		}
}


/************************************************************
Operator function for H5Lvisit.  This function simply
retrieves the info for the object the current link points
to, and calls the operator function for H5Ovisit.
************************************************************/
class H5L_iter_data implements H5L_iterate_t {
    public ArrayList<idata> iterdata = new ArrayList<idata>();
}

class H5L_iter_callback implements H5L_iterate_cb {
    public int callback(int group, String name, H5L_info_t info, H5L_iterate_t op_data) {
    	
    	idata id = new idata(name, info.type);
        ((H5L_iter_data)op_data).iterdata.add(id);
    	
    	H5O_info_t infobuf;
    	int ret = 0;
    	try{
    	//Get type of the object and display its name and type. The name of the object is passed to this function by the Library.
    	infobuf = H5.H5Oget_info_by_name (group, name, HDF5Constants.H5P_DEFAULT);
    	H5O_iterate_cb iter_cbO = new H5O_iter_callback();
    	H5O_iterate_t iter_dataO = new H5O_iter_data();
    	ret=iter_cbO.callback(group, name, infobuf, iter_dataO);
    	}
    	catch (Exception e) {
			e.printStackTrace();
		}
    	
    	return ret;
    }
}

