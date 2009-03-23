package hdf.h5.enums;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum H5_iter_order_t {
  H5_ITER_UNKNOWN (-1),       /* Unknown order */
  H5_ITER_INC (0),                /* Increasing order */
  H5_ITER_DEC (1),                /* Decreasing order */
  H5_ITER_NATIVE (2),             /* No particular order, whatever is fastest */
  H5_ITER_N	(3);	        /* Number of iteration orders */
	private static final Map<Integer, H5_iter_order_t> lookup = new HashMap<Integer, H5_iter_order_t>();

	static {
		for (H5_iter_order_t s : EnumSet.allOf(H5_iter_order_t.class))
			lookup.put(s.getCode(), s);
	}

	private int code;

	H5_iter_order_t(int iter_order_type) {
		this.code = iter_order_type;
	}

	public int getCode() {
		return this.code;
	}

	public static H5_iter_order_t get(int code) {
		return lookup.get(code);
	}

}
