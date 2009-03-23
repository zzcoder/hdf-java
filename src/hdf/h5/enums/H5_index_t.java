package hdf.h5.enums;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum H5_index_t {
  H5_INDEX_UNKNOWN 	(-1),	// Unknown index type			
  H5_INDEX_NAME 		( 0),	// Index on names 	
  H5_INDEX_CRT_ORDER (1),	// Index on creation order 
  H5_INDEX_N 				( 2);	// Number of indices defined 
	private static final Map<Integer, H5_index_t> lookup = new HashMap<Integer, H5_index_t>();

	static {
		for (H5_index_t s : EnumSet.allOf(H5_index_t.class))
			lookup.put(s.getCode(), s);
	}

	private int code;

	H5_index_t(int index_type) {
		this.code = index_type;
	}

	public int getCode() {
		return this.code;
	}

	public static H5_index_t get(int code) {
		return lookup.get(code);
	}

}
