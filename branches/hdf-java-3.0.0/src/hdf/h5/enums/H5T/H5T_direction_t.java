package hdf.h5.enums.H5T;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

// The order to retrieve atomic native datatype
public enum H5T_direction_t {
  H5T_DIR_DEFAULT     (0),    //default direction is inscendent
  H5T_DIR_ASCEND      (1),    //in inscendent order
  H5T_DIR_DESCEND     (2);    //in descendent order
	private static final Map<Integer, H5T_direction_t> lookup = new HashMap<Integer, H5T_direction_t>();

	static {
		for (H5T_direction_t s : EnumSet.allOf(H5T_direction_t.class))
			lookup.put(s.getCode(), s);
	}

	private int code;

	H5T_direction_t(int direction_type) {
		this.code = direction_type;
	}

	public int getCode() {
		return this.code;
	}

	public static H5T_direction_t get(int code) {
		return lookup.get(code);
	}
}
