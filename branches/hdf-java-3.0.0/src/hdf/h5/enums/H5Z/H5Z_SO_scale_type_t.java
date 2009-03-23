package hdf.h5.enums.H5Z;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

//Special parameters for ScaleOffset filter
public enum H5Z_SO_scale_type_t {
  H5Z_SO_FLOAT_DSCALE (0),
  H5Z_SO_FLOAT_ESCALE (1),
  H5Z_SO_INT          (2);
  
  public static final int H5Z_SO_INT_MINBITS_DEFAULT = 0;

	private static final Map<Integer, H5Z_SO_scale_type_t> lookup = new HashMap<Integer, H5Z_SO_scale_type_t>();

	static {
		for (H5Z_SO_scale_type_t s : EnumSet.allOf(H5Z_SO_scale_type_t.class))
			lookup.put(s.getCode(), s);
	}

	private int code;

	H5Z_SO_scale_type_t(int type) {
		this.code = type;
	}

	public int getCode() {
		return this.code;
	}

	public static H5Z_SO_scale_type_t get(int code) {
		return lookup.get(code);
	}
}
