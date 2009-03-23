package hdf.h5.enums.H5F;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

//The difference between a single file and a set of mounted files
public enum H5F_scope_t {
  H5F_SCOPE_LOCAL	(0),	//specified file handle only	
  H5F_SCOPE_GLOBAL(1),	//entire virtual file		
  H5F_SCOPE_DOWN  (2);	//for internal use only		
	private static final Map<Integer, H5F_scope_t> lookup = new HashMap<Integer, H5F_scope_t>();

	static {
		for (H5F_scope_t s : EnumSet.allOf(H5F_scope_t.class))
			lookup.put(s.getCode(), s);
	}

	private int code;

	H5F_scope_t(int scope_type) {
		this.code = scope_type;
	}

	public int getCode() {
		return this.code;
	}

	public static H5F_scope_t get(int code) {
		return lookup.get(code);
	}
}
