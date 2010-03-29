/************************************************************
  This example shows how to set the conditions for
  conversion between compact and dense (indexed) groups.

  This file is intended for use with HDF5 Library version 1.8
 ************************************************************/
package examples.groups;

import ncsa.hdf.hdf5lib.H5;
import ncsa.hdf.hdf5lib.HDF5Constants;
import ncsa.hdf.hdf5lib.structs.H5G_info_t;


public class H5Ex_G_Phase {
	private static String FILE = "h5ex_g_phase.h5";
	private static int MAX_GROUPS = 7;
	private static int MAX_COMPACT = 5;
	private static int MIN_DENSE = 3;
	
	private static void CreateGroup() throws Exception {
		int			file_id = -1;
		int			group_id = -1;
		int			subgroup_id = -1;
		int			fapl_id = -1;
		int			gcpl_id = -1;
		H5G_info_t  ginfo;
		String 		name = "G0";    /* Name of subgroup_id */
		int    i;
 
		try{
			//Set file access property list to allow the latest file format.This will allow the library to create new format groups.
			fapl_id = H5.H5Pcreate(HDF5Constants.H5P_FILE_ACCESS);
			H5.H5Pset_libver_bounds (fapl_id, HDF5Constants.H5F_LIBVER_LATEST, HDF5Constants.H5F_LIBVER_LATEST);

			//Create group access property list and set the phase change conditions.  

			gcpl_id = H5.H5Pcreate(HDF5Constants.H5P_GROUP_CREATE);
			H5.H5Pset_link_phase_change (gcpl_id, MAX_COMPACT, MIN_DENSE);

			//Create a new file using the default properties.
			file_id = H5.H5Fcreate (FILE, HDF5Constants.H5F_ACC_TRUNC, HDF5Constants.H5P_DEFAULT, fapl_id);

			//Create primary group.
			group_id = H5.H5Gcreate2(file_id, name, HDF5Constants.H5P_DEFAULT, gcpl_id, HDF5Constants.H5P_DEFAULT);

			//Add subgroups to "group" one at a time, print the storage type for "group" after each subgroup is created.
			for (i=1; i<=MAX_GROUPS; i++) {
				//Define the subgroup name and create the subgroup.
				char append= (char) (((char)i )+ '0');
				name =name + append; /* G1, G2, G3 etc. */
				//System.out.println(name);
				subgroup_id = H5.H5Gcreate2(group_id, name, HDF5Constants.H5P_DEFAULT, HDF5Constants.H5P_DEFAULT,
						HDF5Constants.H5P_DEFAULT);
				H5.H5Gclose (subgroup_id);

				//Obtain the group info and print the group storage type
				ginfo = H5.H5Gget_info (group_id);
				System.out.print(ginfo.nlinks + " Group"+(ginfo.nlinks == 1 ? " " : "s") + ": Storage type is ");
				if(ginfo.storage_type == HDF5Constants.H5G_STORAGE_TYPE_COMPACT)
					System.out.println ("H5G_STORAGE_TYPE_COMPACT"); /* New compact format */
				if(ginfo.storage_type == HDF5Constants.H5G_STORAGE_TYPE_DENSE)
					System.out.println ("H5G_STORAGE_TYPE_DENSE"); /* New dense (indexed) format */
				if(ginfo.storage_type == HDF5Constants.H5G_STORAGE_TYPE_SYMBOL_TABLE)
					System.out.println ("H5G_STORAGE_TYPE_SYMBOL_TABLE"); /* Original format */
			}

			System.out.println("\n");

			//Delete subgroups one at a time, print the storage type for "group" after each subgroup is deleted.
			for (i=MAX_GROUPS; i>=1; i--) {
				//Define the subgroup name and delete the subgroup.
				H5.H5Ldelete(group_id, name, HDF5Constants.H5P_DEFAULT);
				name = name.substring(0, i+1);
				//Obtain the group info and print the group storage type
				ginfo = H5.H5Gget_info(group_id);
				System.out.print(ginfo.nlinks + " Group"+(ginfo.nlinks == 1 ? " " : "s") + ": Storage type is ");
				if(ginfo.storage_type == HDF5Constants.H5G_STORAGE_TYPE_COMPACT)
					System.out.println ("H5G_STORAGE_TYPE_COMPACT"); /* New compact format */
				if(ginfo.storage_type == HDF5Constants.H5G_STORAGE_TYPE_DENSE)
					System.out.println ("H5G_STORAGE_TYPE_DENSE"); /* New dense (indexed) format */
				if(ginfo.storage_type == HDF5Constants.H5G_STORAGE_TYPE_SYMBOL_TABLE)
					System.out.println ("H5G_STORAGE_TYPE_SYMBOL_TABLE"); /* Original format */
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		finally{
			//Close and release resources.
			if(fapl_id>=0)
				H5.H5Pclose (fapl_id);
			if(gcpl_id>=0)
				H5.H5Pclose (gcpl_id);
			if(group_id>=0)
				H5.H5Gclose (group_id);
			if(file_id>=0)
				H5.H5Fclose (file_id);
		}
}
		public static void main(String[] args) {
			try{
				H5Ex_G_Phase.CreateGroup();
			}catch(Exception ex) {
				ex.printStackTrace();
			}
		}
	
	
}
