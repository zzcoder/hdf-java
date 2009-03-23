package hdf.h5.enums.H5T;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

// Conversion function persistence
public enum H5T_pers_t {
  H5T_PERS_DONTCARE	(-1), //wild card
  H5T_PERS_HARD			(0),	//hard conversion function
  H5T_PERS_SOFT			(1); 	//soft conversion function
	private static final Map<Integer, H5T_pers_t> lookup = new HashMap<Integer, H5T_pers_t>();

	static {
		for (H5T_pers_t s : EnumSet.allOf(H5T_pers_t.class))
			lookup.put(s.getCode(), s);
	}

	private int code;

	H5T_pers_t(int pers_type) {
		this.code = pers_type;
	}

	public int getCode() {
		return this.code;
	}

	public static H5T_pers_t get(int code) {
		return lookup.get(code);
	}
}
