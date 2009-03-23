package hdf.h5.enums.H5D;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum H5D_layout_t {
  H5D_LAYOUT_ERROR	(-1),
  H5D_COMPACT			( 0),	/*raw data is very small		     */
  H5D_CONTIGUOUS	( 1),	/*the default				     */
  H5D_CHUNKED			( 2),	/*slow and fancy			     */
  H5D_NLAYOUTS		( 3);	/*this one must be last!		     */
	private static final Map<Integer, H5D_layout_t> lookup = new HashMap<Integer, H5D_layout_t>();

	static {
		for (H5D_layout_t s : EnumSet.allOf(H5D_layout_t.class))
			lookup.put(s.getCode(), s);
	}

	private int code;

	H5D_layout_t(int layout_type) {
		this.code = layout_type;
	}

	public int getCode() {
		return this.code;
	}

	public static H5D_layout_t get(int code) {
		return lookup.get(code);
	}

}
