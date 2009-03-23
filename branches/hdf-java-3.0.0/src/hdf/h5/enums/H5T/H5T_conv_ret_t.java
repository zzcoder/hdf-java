package hdf.h5.enums.H5T;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

// The return value from conversion callback function H5T_conv_except_func_t
public enum H5T_conv_ret_t {
  H5T_CONV_ABORT      (-1),   //abort conversion
  H5T_CONV_UNHANDLED  ( 0),   //callback function failed to handle the exception
  H5T_CONV_HANDLED    ( 1);   //callback function handled the exception successfully 
	private static final Map<Integer, H5T_conv_ret_t> lookup = new HashMap<Integer, H5T_conv_ret_t>();

	static {
		for (H5T_conv_ret_t s : EnumSet.allOf(H5T_conv_ret_t.class))
			lookup.put(s.getCode(), s);
	}

	private int code;

	H5T_conv_ret_t(int conv_ret_type) {
		this.code = conv_ret_type;
	}

	public int getCode() {
		return this.code;
	}

	public static H5T_conv_ret_t get(int code) {
		return lookup.get(code);
	}
}
