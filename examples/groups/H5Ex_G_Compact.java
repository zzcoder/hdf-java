package examples.groups;

import ncsa.hdf.hdf5lib.H5;
import ncsa.hdf.hdf5lib.HDF5Constants;
import ncsa.hdf.hdf5lib.structs.H5G_info_t;

public class H5Ex_G_Compact {

	private static final String FILE1 = "h5ex_g_compact1.h5";
	private static final String FILE2 = "h5ex_g_compact2.h5";
	private static final String GROUP = "G1";

	public static void CreateGroup() throws Exception
	{
	    int     	file_id = -1;
	    int 		group_id = -1;
	    int			fapl_id = -1; 
	    H5G_info_t  ginfo;
	    long		size;

	    //Create file 1.  This file will use original format groups.
	    file_id = H5.H5Fcreate (FILE1, HDF5Constants.H5F_ACC_TRUNC, HDF5Constants.H5P_DEFAULT, HDF5Constants.H5P_DEFAULT);
	    group_id = H5.H5Gcreate2(file_id, GROUP, HDF5Constants.H5P_DEFAULT, HDF5Constants.H5P_DEFAULT, HDF5Constants.H5P_DEFAULT);
	    
	    // Obtain the group info and print the group storage type.     
	    ginfo = H5.H5Gget_info(group_id);
	    System.out.println ("Group storage type for " + FILE1 + " is: ");
	    if (ginfo.storage_type == HDF5Constants.H5G_STORAGE_TYPE_COMPACT)
	    	System.out.println ("H5G_STORAGE_TYPE_COMPACT"); /* New compact format */
	    if (ginfo.storage_type == HDF5Constants.H5G_STORAGE_TYPE_DENSE)
	    	System.out.println ("H5G_STORAGE_TYPE_DENSE"); /* New dense (indexed) format */
	    if (ginfo.storage_type == HDF5Constants.H5G_STORAGE_TYPE_SYMBOL_TABLE)
	    	System.out.println ("H5G_STORAGE_TYPE_SYMBOL_TABLE");/* Original format */
	   
	    // Close file. 
	    if(group_id>=0)
	    	H5.H5Gclose (group_id);
	    if (file_id>=0)
	    	H5.H5Fclose (file_id);
	    
	    // Re-open file.  Need to get the correct file size.
	    file_id = H5.H5Fopen(FILE1, HDF5Constants.H5F_ACC_RDONLY, HDF5Constants.H5P_DEFAULT);

	    //Obtain and print the file size.
	    size = H5.H5Fget_filesize (file_id);
	    System.out.println ("File size for " + FILE1 + " is: "  +  size );

	    //Close FILE1.
	    H5.H5Fclose (file_id);

	    /*
	     * Set file access property list to allow the latest file format.
	     * This will allow the library to create new compact format groups.
	     */
	    fapl_id = H5.H5Pcreate (HDF5Constants.H5P_FILE_ACCESS);
	    H5.H5Pset_libver_bounds (fapl_id, HDF5Constants.H5F_LIBVER_LATEST, HDF5Constants.H5F_LIBVER_LATEST);

	    //Create file 2 using the new file access property list.
	    file_id = H5.H5Fcreate(FILE2, HDF5Constants.H5F_ACC_TRUNC, HDF5Constants.H5P_DEFAULT, fapl_id);
	    group_id = H5.H5Gcreate2(file_id, GROUP, HDF5Constants.H5P_DEFAULT, HDF5Constants.H5P_DEFAULT, HDF5Constants.H5P_DEFAULT);

	    //Obtain the group info and print the group storage type.
	    ginfo = H5.H5Gget_info(group_id);
	    System.out.println ("Group storage type for " + FILE2 + " is: ");
	    if (ginfo.storage_type == HDF5Constants.H5G_STORAGE_TYPE_COMPACT)
	    	System.out.println ("H5G_STORAGE_TYPE_COMPACT"); /* New compact format */
	    if (ginfo.storage_type == HDF5Constants.H5G_STORAGE_TYPE_DENSE)
	    	System.out.println ("H5G_STORAGE_TYPE_DENSE"); /* New dense (indexed) format */
	    if (ginfo.storage_type == HDF5Constants.H5G_STORAGE_TYPE_SYMBOL_TABLE)
	    	System.out.println ("H5G_STORAGE_TYPE_SYMBOL_TABLE");/* Original format */

	    //Close file. 
	    H5.H5Gclose (group_id);
	    H5.H5Fclose (file_id);
	    
	    //Re-open file.  Needed to get the correct file size.
	    file_id = H5.H5Fopen (FILE2, HDF5Constants.H5F_ACC_RDONLY, fapl_id);

	    //Obtain and print the file size.
	    size = H5.H5Fget_filesize (file_id);
	    System.out.println ("File size for " + FILE2 + " is: "  +  size );
	   
	    //Close FILE2 and release resources.
	    H5.H5Fclose (file_id);
	}

	public static void main(String[] args) {
		try { 
			H5Ex_G_Compact.CreateGroup();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
