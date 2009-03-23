package hdf.h5.enums.H5D;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

// Values for time of writing fill value property
public enum H5D_fill_time_t {
  H5D_FILL_TIME_ERROR	(-1),
  H5D_FILL_TIME_ALLOC ( 0),
  H5D_FILL_TIME_NEVER	( 1),
  H5D_FILL_TIME_IFSET	( 2);
	private static final Map<Integer, H5D_fill_time_t> lookup = new HashMap<Integer, H5D_fill_time_t>();

	static {
		for (H5D_fill_time_t s : EnumSet.allOf(H5D_fill_time_t.class))
			lookup.put(s.getCode(), s);
	}

	private int code;

	H5D_fill_time_t(int fill_time_type) {
		this.code = fill_time_type;
	}

	public int getCode() {
		return this.code;
	}

	public static H5D_fill_time_t get(int code) {
		return lookup.get(code);
	}

}
