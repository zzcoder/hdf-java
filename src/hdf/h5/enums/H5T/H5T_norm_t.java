package hdf.h5.enums.H5T;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

// Floating-point normalization schemes
public enum H5T_norm_t {
  H5T_NORM_ERROR       (-1),   //error
  H5T_NORM_IMPLIED     ( 0),   //msb of mantissa isn't stored, always 1
  H5T_NORM_MSBSET      ( 1),   //msb of mantissa is always 1
  H5T_NORM_NONE        ( 2);   //not normalized 
  //H5T_NORM_NONE must be last
	private static final Map<Integer, H5T_norm_t> lookup = new HashMap<Integer, H5T_norm_t>();

	static {
		for (H5T_norm_t s : EnumSet.allOf(H5T_norm_t.class))
			lookup.put(s.getCode(), s);
	}

	private int code;

	H5T_norm_t(int norm_type) {
		this.code = norm_type;
	}

	public int getCode() {
		return this.code;
	}

	public static H5T_norm_t get(int code) {
		return lookup.get(code);
	}
}
