package hdf.h5.enums.H5O;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

//Types of objects in file
public enum H5O_type_t {
  H5O_TYPE_UNKNOWN        (-1),   // Unknown object type
  H5O_TYPE_GROUP          ( 0),   // Object is a group
  H5O_TYPE_DATASET        ( 1),   // Object is a dataset
  H5O_TYPE_NAMED_DATATYPE ( 2),   // Object is a named data type 
  H5O_TYPE_NTYPES         ( 3);   // Number of different object types (must be last!)
	private static final Map<Integer, H5O_type_t> lookup = new HashMap<Integer, H5O_type_t>();

	static {
		for (H5O_type_t s : EnumSet.allOf(H5O_type_t.class))
			lookup.put(s.getCode(), s);
	}

	private int code;

	H5O_type_t(int type) {
		this.code = type;
	}

	public int getCode() {
		return this.code;
	}

	public static H5O_type_t get(int code) {
		return lookup.get(code);
	}
}
