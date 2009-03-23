package hdf.h5.enums.H5Z;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

//Values to decide if EDC is enabled for reading data
public enum H5Z_EDC_t {
  H5Z_ERROR_EDC       (-1),   /* error value */
  H5Z_DISABLE_EDC     ( 0),
  H5Z_ENABLE_EDC      ( 1),
  H5Z_NO_EDC          ( 2);     /* must be the last */
	private static final Map<Integer, H5Z_EDC_t> lookup = new HashMap<Integer, H5Z_EDC_t>();

	static {
		for (H5Z_EDC_t s : EnumSet.allOf(H5Z_EDC_t.class))
			lookup.put(s.getCode(), s);
	}

	private int code;

	H5Z_EDC_t(int type) {
		this.code = type;
	}

	public int getCode() {
		return this.code;
	}

	public static H5Z_EDC_t get(int code) {
		return lookup.get(code);
	}
}
