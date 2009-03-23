package hdf.h5.enums.H5T;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

// Commands sent to conversion functions
public enum H5T_cmd_t {
  H5T_CONV_INIT	(0),	//query and/or initialize private data
  H5T_CONV_CONV	(1), 	//convert data from source to dest datatype 
  H5T_CONV_FREE	(2);	//function is being removed from path	   
	private static final Map<Integer, H5T_cmd_t> lookup = new HashMap<Integer, H5T_cmd_t>();

	static {
		for (H5T_cmd_t s : EnumSet.allOf(H5T_cmd_t.class))
			lookup.put(s.getCode(), s);
	}

	private int code;

	H5T_cmd_t(int cmd_type) {
		this.code = cmd_type;
	}

	public int getCode() {
		return this.code;
	}

	public static H5T_cmd_t get(int code) {
		return lookup.get(code);
	}
}
