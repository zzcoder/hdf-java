package hdf.h5.enums.H5D;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

// Values for fill value status
public enum H5D_fill_value_t {
  H5D_FILL_VALUE_ERROR        (-1),
  H5D_FILL_VALUE_UNDEFINED    (0),
  H5D_FILL_VALUE_DEFAULT      (1),
  H5D_FILL_VALUE_USER_DEFINED (2);
	private static final Map<Integer, H5D_fill_value_t> lookup = new HashMap<Integer, H5D_fill_value_t>();

	static {
		for (H5D_fill_value_t s : EnumSet.allOf(H5D_fill_value_t.class))
			lookup.put(s.getCode(), s);
	}

	private int code;

	H5D_fill_value_t(int fill_value_type) {
		this.code = fill_value_type;
	}

	public int getCode() {
		return this.code;
	}

	public static H5D_fill_value_t get(int code) {
		return lookup.get(code);
	}

}
