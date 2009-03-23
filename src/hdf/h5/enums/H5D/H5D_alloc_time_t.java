package hdf.h5.enums.H5D;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum H5D_alloc_time_t {
  H5D_ALLOC_TIME_ERROR	(-1),
  H5D_ALLOC_TIME_DEFAULT (0),
  H5D_ALLOC_TIME_EARLY	( 1),
  H5D_ALLOC_TIME_LATE		( 2),
  H5D_ALLOC_TIME_INCR		( 3);
	private static final Map<Integer, H5D_alloc_time_t> lookup = new HashMap<Integer, H5D_alloc_time_t>();

	static {
		for (H5D_alloc_time_t s : EnumSet.allOf(H5D_alloc_time_t.class))
			lookup.put(s.getCode(), s);
	}

	private int code;

	H5D_alloc_time_t(int alloc_time_type) {
		this.code = alloc_time_type;
	}

	public int getCode() {
		return this.code;
	}

	public static H5D_alloc_time_t get(int code) {
		return lookup.get(code);
	}

}
