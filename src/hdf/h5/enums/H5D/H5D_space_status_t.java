package hdf.h5.enums.H5D;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

// Values for the status of space allocation
public enum H5D_space_status_t {
  H5D_SPACE_STATUS_ERROR		(-1),
  H5D_SPACE_STATUS_NOT_ALLOCATED	( 0),
  H5D_SPACE_STATUS_PART_ALLOCATED	( 1),
  H5D_SPACE_STATUS_ALLOCATED		( 2);
	private static final Map<Integer, H5D_space_status_t> lookup = new HashMap<Integer, H5D_space_status_t>();

	static {
		for (H5D_space_status_t s : EnumSet.allOf(H5D_space_status_t.class))
			lookup.put(s.getCode(), s);
	}

	private int code;

	H5D_space_status_t(int space_status_type) {
		this.code = space_status_type;
	}

	public int getCode() {
		return this.code;
	}

	public static H5D_space_status_t get(int code) {
		return lookup.get(code);
	}

}
