package hdf.h5.enums.H5T;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

// Type of padding to use in other atomic types
public enum H5T_pad_t {
  H5T_PAD_ERROR        (-1),   //error 
  H5T_PAD_ZERO         ( 0),   //always set to zero 
  H5T_PAD_ONE          ( 1),   //always set to one 
  H5T_PAD_BACKGROUND   ( 2),   //set to background value  

  H5T_NPAD             ( 3);   //THIS MUST BE LAST 
	private static final Map<Integer, H5T_pad_t> lookup = new HashMap<Integer, H5T_pad_t>();

	static {
		for (H5T_pad_t s : EnumSet.allOf(H5T_pad_t.class))
			lookup.put(s.getCode(), s);
	}

	private int code;

	H5T_pad_t(int pad_type) {
		this.code = pad_type;
	}

	public int getCode() {
		return this.code;
	}

	public static H5T_pad_t get(int code) {
		return lookup.get(code);
	}
}
