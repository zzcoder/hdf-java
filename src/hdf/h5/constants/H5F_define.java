package hdf.h5.constants;


public class H5F_define {
//	public static final int H5_VERS_MAJOR	= 1;	// For major interface/format changes  
//	public static final int H5_VERS_MINOR	= 9;	// For minor interface/format changes
//	public static final int H5_VERS_RELEASE = 33;	// For tweaks, bug-fixes, or development
//	public static final String H5_VERS_SUBRELEASE = "";	// For pre-releases like snap0 

	/*
	 * These are the bits that can be passed to the `flags' argument of
	 * H5Fcreate() and H5Fopen(). Use the bit-wise OR operator (|) to combine
	 * them as needed.  As a side effect, they call H5check_version() to make sure
	 * that the application is compiled with a version of the hdf5 header files
	 * which are compatible with the library to which the application is linked.
	 * We're assuming that these constants are used rather early in the hdf5
	 * session.
	 *
	 */
	public static final int H5F_ACC_RDONLY	= 0x0000;	/*absence of rdwr => rd-only */
	public static final int H5F_ACC_RDWR	= 0x0001;	/*open for read and write    */
	public static final int H5F_ACC_TRUNC	= 0x0002;	/*overwrite existing files   */
	public static final int H5F_ACC_EXCL	= 0x0004;	/*fail if file already exists*/
	public static final int H5F_ACC_DEBUG	= 0x0008;	/*print debug info	     */
	public static final int H5F_ACC_CREAT	= 0x0010;	/*create non-existing files  */

	/* Value passed to H5Pset_elink_acc_flags to cause flags to be taken from the
	 * parent file. */
	public static final int H5F_ACC_DEFAULT = 0xffff;	/*ignore setting on lapl     */

	/* Flags for H5Fget_obj_count() & H5Fget_obj_ids() calls */
	public static final int H5F_OBJ_FILE	= 0x0001;       /* File objects */
	public static final int H5F_OBJ_DATASET	= 0x0002;       /* Dataset objects */
	public static final int H5F_OBJ_GROUP	= 0x0004;       /* Group objects */
	public static final int H5F_OBJ_DATATYPE = 0x0008;      /* Named datatype objects */
	public static final int H5F_OBJ_ATTR    = 0x0010;       /* Attribute objects */
	public static final int H5F_OBJ_ALL 	= H5F_OBJ_FILE|H5F_OBJ_DATASET|H5F_OBJ_GROUP|H5F_OBJ_DATATYPE|H5F_OBJ_ATTR;
	public static final int H5F_OBJ_LOCAL   = 0x0020;       /* Restrict search to objects opened through current file ID */
	                                        /* (as opposed to objects opened through any file ID accessing this file) */

	public static final long H5F_FAMILY_DEFAULT  = 0;

	/* Unlimited file size for H5Pset_external() */
	public static final long H5F_UNLIMITED	= -1L;
//
//	H5F_define(HDF5Library hdf5lib) {
//		hdf5lib.H5check_version(H5_VERS_MAJOR, H5_VERS_MINOR, H5_VERS_RELEASE);
//	}
}
