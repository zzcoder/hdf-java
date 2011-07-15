/****************************************************************************
 * NCSA HDF                                                                 *
 * National Comptational Science Alliance                                   *
 * University of Illinois at Urbana-Champaign                               *
 * 605 E. Springfield, Champaign IL 61820                                   *
 *                                                                          *
 * For conditions of distribution and use, see the accompanying             *
 * hdf-java/COPYING file.                                                   *
 *                                                                          *
 ****************************************************************************/

package ncsa.hdf.hdf5lib;

/**
 *  Class HDF5Constants contains the C define constants and enumerated
 *  types of HDF5 library, e.g., 
 *  <pre>
 *  HDF5Library.H5Pcreate(HDF5Constants.H5P_FILE_ACCESS); 
 *  </pre>
 *
 *  <p>
 *  These values are generated automatically by /etc/Makefile
 *  <P>
 *  <B>Do not edit this file!</b>
 *
 *  <b>See also:</b> ncsa.hdf.hdf5lib.HDF5Library
 *  <br>
 *  <b>See also:</b> ncsa.hdf.hdf5lib.HDF5CDataTypes
 */
public class HDF5Constants
{
 
	/*AUTOMATICALLY GENERATED CONSTANTS*/ 
	final static public int H5D_CHUNKED = 2;
	final static public int H5D_COMPACT = 0;
	final static public int H5D_CONTIGUOUS = 1;
	final static public int H5D_LAYOUT_ERROR = -1;
	final static public int H5E_ALIGNMENT = 40;
	final static public int H5E_ALREADYINIT = 24;
	final static public int H5E_ARGS = 1;
	final static public int H5E_ATOM = 7;
	final static public int H5E_ATTR = 18;
	final static public int H5E_BADATOM = 25;
	final static public int H5E_BADFILE = 15;
	final static public int H5E_BADMESG = 41;
	final static public int H5E_BADRANGE = 4;
	final static public int H5E_BADTYPE = 3;
	final static public int H5E_BADVALUE = 5;
	final static public int H5E_BTREE = 9;
	final static public int H5E_CACHE = 8;
	final static public int H5E_CANTCOPY = 7;
	final static public int H5E_CANTCREATE = 11;
	final static public int H5E_CANTDECODE = 34;
	final static public int H5E_CANTDELETE = 42;
	final static public int H5E_CANTENCODE = 33;
	final static public int H5E_CANTFLUSH = 27;
	final static public int H5E_CANTFREE = 8;
	final static public int H5E_CANTINIT = 23;
	final static public int H5E_CANTINSERT = 36;
	final static public int H5E_CANTLIST = 37;
	final static public int H5E_CANTLOAD = 28;
	final static public int H5E_CANTOPENFILE = 12;
	final static public int H5E_CANTOPENOBJ = 43;
	final static public int H5E_CANTREGISTER = 26;
	final static public int H5E_CANTSPLIT = 35;
	final static public int H5E_CLOSEERROR = 21;
	final static public int H5E_COMPLEN = 44;
	final static public int H5E_CWG = 45;
	final static public int H5E_DATASET = 15;
	final static public int H5E_DATASPACE = 14;
	final static public int H5E_DATATYPE = 13;
	final static public int H5E_EFL = 20;
	final static public int H5E_EXISTS = 32;
	final static public int H5E_FILE = 4;
	final static public int H5E_FILEEXISTS = 9;
	final static public int H5E_FILEOPEN = 10;
	final static public int H5E_FUNC = 6;
	final static public int H5E_HEAP = 11;
	final static public int H5E_IO = 5;
	final static public int H5E_LINK = 46;
	final static public int H5E_LINKCOUNT = 38;
	final static public int H5E_MOUNT = 17;
	final static public int H5E_MPI = 49;
	final static public int H5E_NONE_MAJOR = 0;
	final static public int H5E_NONE_MINOR = 0;
	final static public int H5E_NOSPACE = 6;
	final static public int H5E_NOTCACHED = 30;
	final static public int H5E_NOTFOUND = 31;
	final static public int H5E_NOTHDF5 = 14;
	final static public int H5E_OHDR = 12;
	final static public int H5E_OVERFLOW = 22;
	final static public int H5E_PLINE = 19;
	final static public int H5E_PLIST = 17;
	final static public int H5E_PROTECT = 29;
	final static public int H5E_READERROR = 19;
	final static public int H5E_REFERENCE = 21;
	final static public int H5E_RESOURCE = 2;
	final static public int H5E_SEEKERROR = 18;
	final static public int H5E_SLINK = 47;
	final static public int H5E_STORAGE = 16;
	final static public int H5E_SYM = 10;
	final static public int H5E_TRUNCATED = 16;
	final static public int H5E_UNINITIALIZED = 1;
	final static public int H5E_UNSUPPORTED = 2;
	final static public int H5E_VERSION = 39;
	final static public int H5E_WALK_DOWNWARD = 1;
	final static public int H5E_WALK_UPWARD = 0;
	final static public int H5E_WRITEERROR = 20;
	final static public int H5F_ACC_CREAT = 16;
	final static public int H5F_ACC_DEBUG = 8;
	final static public int H5F_ACC_EXCL = 4;
	final static public int H5F_ACC_RDONLY = 0;
	final static public int H5F_ACC_RDWR = 1;
	final static public int H5F_ACC_TRUNC = 2;
	final static public int H5F_SCOPE_DOWN = 2;
	final static public int H5F_SCOPE_GLOBAL = 1;
	final static public int H5F_SCOPE_LOCAL = 0;
	final static public int H5F_UNLIMITED = -1;
	final static public int H5G_DATASET = 2;
	final static public int H5G_GROUP = 1;
	final static public int H5G_LINK = 0;
	final static public int H5G_LINK_ERROR = -1;
	final static public int H5G_LINK_HARD = 0;
	final static public int H5G_LINK_SOFT = 1;
	final static public int H5G_TYPE = 3;
	final static public int H5G_UNKNOWN = -1;
	final static public int H5I_ATTR = 16;
	final static public int H5I_BADID = -1;
	final static public int H5I_DATASET = 15;
	final static public int H5I_DATASPACE = 14;
	final static public int H5I_DATATYPE = 13;
	final static public int H5I_FILE = 1;
	final static public int H5I_GROUP = 12;
	final static public int H5I_REFERENCE = 18;
	final static public int H5I_VFL = 19;
	final static public int H5P_DATASET_CREATE = 2;
	final static public int H5P_DATASET_XFER = 3;
	final static public int H5P_DEFAULT = 0;
	final static public int H5P_FILE_ACCESS = 1;
	final static public int H5P_FILE_CREATE = 0;
	final static public int H5P_MOUNT = 4;
	final static public int H5P_NO_CLASS = -1;
	final static public int H5R_BADTYPE = -1;
	final static public int H5R_DATASET_REGION = 1;
	final static public int H5R_OBJECT = 0;
	final static public int H5S_ALL = 0;
	final static public int H5S_COMPLEX = 2;
	final static public int H5S_MAX_RANK = 32;
	final static public int H5S_NO_CLASS = -1;
	final static public int H5S_SCALAR = 0;
	final static public int H5S_SELECT_INVALID = 4;
	final static public int H5S_SELECT_NOOP = -1;
	final static public int H5S_SELECT_OR = 1;
	final static public int H5S_SELECT_SET = 0;
	final static public int H5S_SIMPLE = 1;
	final static public int H5S_UNLIMITED = -1;
	final static public int H5T_ARRAY = 10;
	final static public int H5T_BITFIELD = 4;
	final static public int H5T_COMPOUND = 6;
	final static public int H5T_CONV_CONV = 1;
	final static public int H5T_CONV_FREE = 2;
	final static public int H5T_CONV_INIT = 0;
	final static public int H5T_CSET_ASCII = 0;
	final static public int H5T_CSET_ERROR = -1;
	final static public int H5T_ENUM = 8;
	final static public int H5T_FLOAT = 1;
	final static public int H5T_INTEGER = 0;
	final static public int H5T_NORM_ERROR = -1;
	final static public int H5T_NORM_IMPLIED = 0;
	final static public int H5T_NORM_MSBSET = 1;
	final static public int H5T_NORM_NONE = 2;
	final static public int H5T_NO_CLASS = -1;
	final static public int H5T_OPAQUE = 5;
	final static public int H5T_ORDER_BE = 1;
	final static public int H5T_ORDER_ERROR = -1;
	final static public int H5T_ORDER_LE = 0;
	final static public int H5T_ORDER_NONE = 3;
	final static public int H5T_ORDER_VAX = 2;
	final static public int H5T_PAD_BACKGROUND = 2;
	final static public int H5T_PAD_ERROR = -1;
	final static public int H5T_PAD_ONE = 1;
	final static public int H5T_PAD_ZERO = 0;
	final static public int H5T_PERS_DONTCARE = -1;
	final static public int H5T_PERS_HARD = 0;
	final static public int H5T_PERS_SOFT = 1;
	final static public int H5T_REFERENCE = 7;
	final static public int H5T_SGN_2 = 1;
	final static public int H5T_SGN_ERROR = -1;
	final static public int H5T_SGN_NONE = 0;
	final static public int H5T_STRING = 3;
	final static public int H5T_STR_ERROR = -1;
	final static public int H5T_STR_NULLPAD = 1;
	final static public int H5T_STR_NULLTERM = 0;
	final static public int H5T_STR_SPACEPAD = 2;
	final static public int H5T_TIME = 2;
	final static public int H5T_VLEN = 9;
	final static public int H5Z_FILTER_DEFLATE = 1;
	final static public int H5Z_FILTER_ERROR = -1;
	final static public int H5Z_FILTER_NONE = 0;
	final static public int H5_VERS_MAJOR = 1;
	final static public int H5_VERS_MINOR = 4;
	final static public int H5_VERS_RELEASE = 3;
}