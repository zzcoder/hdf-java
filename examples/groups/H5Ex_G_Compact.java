package examples.groups;

import ncsa.hdf.hdf5lib.H5;
import ncsa.hdf.hdf5lib.HDF5Constants;
import ncsa.hdf.hdf5lib.structs.H5G_info_t;

public class H5Ex_G_Compact {
	/************************************************************

	  This example shows how to create "compact-or-indexed"
	  format groups, new to 1.8.  This example also illustrates
	  the space savings of compact groups by creating 2 files
	  which are identical except for the group format, and
	  displaying the file size of each.  Both files have one
	  empty group in the root group.

	  This file is intended for use with HDF5 Library version 1.8

	 ************************************************************/

	private static final String FILE1 = "h5ex_g_compact1.h5";
	private static final String  FILE2 = "h5ex_g_compact2.h5";
	private static final String  GROUP = "G1";

	public static void CreateGroup() throws Exception
	{
	    int       file, group, fapl;         /* Handles */
	    int      status;
	    H5G_info_t  ginfo;
	    long     size;

	    /*
	     * Create file 1.  This file will use original format groups.
	     */
	    file = H5.H5Fcreate (FILE1, HDF5Constants.H5F_ACC_TRUNC, HDF5Constants.H5P_DEFAULT, HDF5Constants.H5P_DEFAULT);
	    group = H5.H5Gcreate2 (file, GROUP, HDF5Constants.H5P_DEFAULT, HDF5Constants.H5P_DEFAULT, HDF5Constants.H5P_DEFAULT);

//	    /*
//	     * Obtain the group info and print the group storage type.
//	     */
//	    ginfo = H5.H5Gget_info (group);
//	    System.out.println ("Group storage type for %s is: ", FILE1);
//	    switch (ginfo.storage_type) {
//	        case H5G_STORAGE_TYPE_COMPACT:
//	            System.out.println ("H5G_STORAGE_TYPE_COMPACT\n");
//	                        /* New compact format */
//	            break;
//	        case H5G_STORAGE_TYPE_DENSE:
//	            System.out.println ("H5G_STORAGE_TYPE_DENSE\n");
//	                        /* New dense (indexed) format */
//	            break;
//	        case H5G_STORAGE_TYPE_SYMBOL_TABLE:
//	            System.out.println ("H5G_STORAGE_TYPE_SYMBOL_TABLE\n");
//	                        /* Original format */
//	    }
//
//	    /*
//	     * Close and re-open file.  Needed to get the correct file size.
//	     */
//	    status = H5Gclose (group);
//	    status = H5Fclose (file);
//	    file = H5Fopen (FILE1, H5F_ACC_RDONLY, HDF5Constants.H5P_DEFAULT);
//
//	    /*
//	     * Obtain and print the file size.
//	     */
//	    status = H5Fget_filesize (file, &size);
//	    System.out.println ("File size for %s is: %d bytes\n\n", FILE1, (int)size);
//
//	    /*
//	     * Close FILE1.
//	     */
//	    status = H5Fclose (file);
//
//	    /*
//	     * Set file access property list to allow the latest file format.
//	     * This will allow the library to create new compact format groups.
//	     */
//	    fapl = H5Pcreate (HDF5Constants.H5P_FILE_ACCESS);
//	    status = H5Pset_libver_bounds (fapl, HDF5Constants.H5F_LIBVER_LATEST, HDF5Constants.H5F_LIBVER_LATEST);
//
//	    /*
//	     * Create file 2 using the new file access property list.
//	     */
//	    file = H5Fcreate (FILE2, HDF5Constants.H5F_ACC_TRUNC, HDF5Constants.H5P_DEFAULT, fapl );
//	    group = H5Gcreate (file, GROUP, HDF5Constants.H5P_DEFAULT, HDF5Constants.H5P_DEFAULT, HDF5Constants.H5P_DEFAULT);
//
//	    /*
//	     * Obtain the group info and print the group storage type.
//	     */
//	    status = H5Gget_info (group, &ginfo);
//	    System.out.println ("Group storage type for %s is: ", FILE2);
//	    switch (ginfo.storage_type) {
//	        case H5G_STORAGE_TYPE_COMPACT:
//	            System.out.println ("H5G_STORAGE_TYPE_COMPACT\n");
//	                        /* New compact format */
//	            break;
//	        case H5G_STORAGE_TYPE_DENSE:
//	            System.out.println ("H5G_STORAGE_TYPE_DENSE\n");
//	                        /* New dense (indexed) format */
//	            break;
//	        case H5G_STORAGE_TYPE_SYMBOL_TABLE:
//	            System.out.println ("H5G_STORAGE_TYPE_SYMBOL_TABLE\n");
//	                        /* Original format */
//	    }
//
//	    /*
//	     * Close and re-open file.  Needed to get the correct file size.
//	     */
//	    status = H5Gclose (group);
//	    status = H5Fclose (file);
//	    file = H5Fopen (FILE2, H5F_ACC_RDONLY, fapl);
//
//	    /*
//	     * Obtain and print the file size.
//	     */
//	    status = H5Fget_filesize (file, &size);
//	    System.out.println ("File size for %s is: %d bytes\n", FILE2, (int)size);
//	    System.out.println ("\n");

	    /*
	     * Close and release resources.
	     */
	    status = H5.H5Gclose (group);
	    status = H5.H5Fclose (file);
	}

	public static void main(String[] args) {
		try { 
			H5Ex_G_Compact.CreateGroup();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
