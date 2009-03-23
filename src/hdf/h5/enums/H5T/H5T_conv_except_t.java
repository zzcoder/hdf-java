package hdf.h5.enums.H5T;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

// The exception type passed into the conversion callback function
public enum H5T_conv_except_t {
  H5T_CONV_EXCEPT_RANGE_HI       (0),   //source value is greater than destination's range
  H5T_CONV_EXCEPT_RANGE_LOW      (1),   //source value is less than destination's range 
  H5T_CONV_EXCEPT_PRECISION      (2),   //source value loses precision in destination 
  H5T_CONV_EXCEPT_TRUNCATE       (3),   //source value is truncated in destination 
  H5T_CONV_EXCEPT_PINF           (4),   //source value is positive infinity(floating number)
  H5T_CONV_EXCEPT_NINF           (5),   //source value is negative infinity(floating number)
  H5T_CONV_EXCEPT_NAN            (6);   //source value is NaN(floating number) 
	private static final Map<Integer, H5T_conv_except_t> lookup = new HashMap<Integer, H5T_conv_except_t>();

	static {
		for (H5T_conv_except_t s : EnumSet.allOf(H5T_conv_except_t.class))
			lookup.put(s.getCode(), s);
	}

	private int code;

	H5T_conv_except_t(int conv_except_type) {
		this.code = conv_except_type;
	}

	public int getCode() {
		return this.code;
	}

	public static H5T_conv_except_t get(int code) {
		return lookup.get(code);
	}
}
