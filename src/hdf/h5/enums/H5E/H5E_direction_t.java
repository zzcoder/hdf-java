package hdf.h5.enums.H5E;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

// Error stack traversal direction
public enum H5E_direction_t {
  H5E_WALK_UPWARD		( 0),		/*begin deep, end at API function    */
  H5E_WALK_DOWNWARD	( 1);		/*begin at API function, end deep    */
	private static final Map<Integer, H5E_direction_t> lookup = new HashMap<Integer, H5E_direction_t>();

	static {
		for (H5E_direction_t s : EnumSet.allOf(H5E_direction_t.class))
			lookup.put(s.getCode(), s);
	}

	private int code;

	H5E_direction_t(int direction_type) {
		this.code = direction_type;
	}

	public int getCode() {
		return this.code;
	}

	public static H5E_direction_t get(int code) {
		return lookup.get(code);
	}

}
