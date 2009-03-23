package hdf.h5.enums.H5T;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

// Types of integer sign schemes
public enum H5T_sign_t {
  H5T_SGN_ERROR        (-1),   //error
  H5T_SGN_NONE         ( 0),   //this is an unsigned type
  H5T_SGN_2            ( 1),   //two's complement

  H5T_NSGN             ( 2);   //this must be last!
	private static final Map<Integer, H5T_sign_t> lookup = new HashMap<Integer, H5T_sign_t>();

	static {
		for (H5T_sign_t s : EnumSet.allOf(H5T_sign_t.class))
			lookup.put(s.getCode(), s);
	}

	private int code;

	H5T_sign_t(int sign_type) {
		this.code = sign_type;
	}

	public int getCode() {
		return this.code;
	}

	public static H5T_sign_t get(int code) {
		return lookup.get(code);
	}
}
