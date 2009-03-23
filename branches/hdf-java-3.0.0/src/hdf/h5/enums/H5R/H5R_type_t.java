package hdf.h5.enums.H5R;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

//Reference types allowed.
public enum H5R_type_t {
  H5R_BADTYPE        (-1),   //invalid Reference Type
  H5R_OBJECT         ( 0),   //Object reference
  H5R_DATASET_REGION ( 1),   //Dataset Region Reference
  H5R_MAXTYPE        ( 2);   //highest type (Invalid as true type)
	private static final Map<Integer, H5R_type_t> lookup = new HashMap<Integer, H5R_type_t>();

	static {
		for (H5R_type_t s : EnumSet.allOf(H5R_type_t.class))
			lookup.put(s.getCode(), s);
	}

	private int code;

	H5R_type_t(int type) {
		this.code = type;
	}

	public int getCode() {
		return this.code;
	}

	public static H5R_type_t get(int code) {
		return lookup.get(code);
	}
}
