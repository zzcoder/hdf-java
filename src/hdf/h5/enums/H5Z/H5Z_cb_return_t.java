package hdf.h5.enums.H5Z;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

//Return values for filter callback function
public enum H5Z_cb_return_t {
  H5Z_CB_ERROR  (-1),
  H5Z_CB_FAIL   ( 0),    // I/O should fail if filter fails.
  H5Z_CB_CONT   ( 1),    // I/O continues if filter fails.
  H5Z_CB_NO     ( 2);
	private static final Map<Integer, H5Z_cb_return_t> lookup = new HashMap<Integer, H5Z_cb_return_t>();

	static {
		for (H5Z_cb_return_t s : EnumSet.allOf(H5Z_cb_return_t.class))
			lookup.put(s.getCode(), s);
	}

	private int code;

	H5Z_cb_return_t(int type) {
		this.code = type;
	}

	public int getCode() {
		return this.code;
	}

	public static H5Z_cb_return_t get(int code) {
		return lookup.get(code);
	}
}
