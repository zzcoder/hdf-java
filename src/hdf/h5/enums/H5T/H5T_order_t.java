package hdf.h5.enums.H5T;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

// Byte orders
public enum H5T_order_t {
  H5T_ORDER_ERROR      (-1),  //error 
  H5T_ORDER_LE         ( 0),  //little endian
  H5T_ORDER_BE         ( 1),  //bit endian   
  H5T_ORDER_VAX        ( 2),  //VAX mixed endian
  H5T_ORDER_NONE       ( 3);  //no particular order (strings, bits,..)
  //H5T_ORDER_NONE must be last
	private static final Map<Integer, H5T_order_t> lookup = new HashMap<Integer, H5T_order_t>();

	static {
		for (H5T_order_t s : EnumSet.allOf(H5T_order_t.class))
			lookup.put(s.getCode(), s);
	}

	private int code;

	H5T_order_t(int order_type) {
		this.code = order_type;
	}

	public int getCode() {
		return this.code;
	}

	public static H5T_order_t get(int code) {
		return lookup.get(code);
	}
}
