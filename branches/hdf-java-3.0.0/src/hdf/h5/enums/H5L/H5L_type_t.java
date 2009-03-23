package hdf.h5.enums.H5L;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

// Link class types.
// Values less than 64 are reserved for the HDF5 library's internal use.
// Values 64 to 255 are for "user-defined" link class types; these types are
// defined by HDF5 but their behavior can be overridden by users.
// Users who want to create new classes of links should contact the HDF5
// development team at hdfhelp@ncsa.uiuc.edu .
// These values can never change because they appear in HDF5 files.
public enum H5L_type_t {
  H5L_TYPE_ERROR  (-1),      // Invalid link type id
  H5L_TYPE_HARD   (0),       // Hard link id 
  H5L_TYPE_SOFT   (1),       // Soft link id
  H5L_TYPE_EXTERNAL  (64),   // External link id 
  H5L_TYPE_MAX 		(255);	   // Maximum link type id 
  static final int H5L_TYPE_BUILTIN_MAX = H5L_TYPE_SOFT.getCode();      // Maximum value link value for "built-in" link types
  static final int H5L_TYPE_UD_MIN      = H5L_TYPE_EXTERNAL.getCode();  // Link ids at or above this value are "user-defined" link types. 
  
	private static final Map<Integer, H5L_type_t> lookup = new HashMap<Integer, H5L_type_t>();

	static {
		for (H5L_type_t s : EnumSet.allOf(H5L_type_t.class))
			lookup.put(s.getCode(), s);
	}

	private int code;

	H5L_type_t(int type) {
		this.code = type;
	}

	public int getCode() {
		return this.code;
	}

	public static H5L_type_t get(int code) {
		return lookup.get(code);
	}
}
