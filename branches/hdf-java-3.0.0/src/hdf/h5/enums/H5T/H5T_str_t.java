package hdf.h5.enums.H5T;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

// Type of padding to use in character strings.  Do not change these values
// since they appear in HDF5 files!

public enum H5T_str_t {
  H5T_STR_ERROR        (-1),   //error 
  H5T_STR_NULLTERM     ( 0),   //null terminate like in C
  H5T_STR_NULLPAD      ( 1),   //pad with nulls
  H5T_STR_SPACEPAD     ( 2),   //pad with spaces like in Fortran 
  H5T_STR_RESERVED_3   ( 3),   //reserved for later use
  H5T_STR_RESERVED_4   ( 4),   //reserved for later use
  H5T_STR_RESERVED_5   ( 5),   //reserved for later use
  H5T_STR_RESERVED_6   ( 6),   //reserved for later use
  H5T_STR_RESERVED_7   ( 7),   //reserved for later use
  H5T_STR_RESERVED_8   ( 8),   //reserved for later use
  H5T_STR_RESERVED_9   ( 9),   //reserved for later use
  H5T_STR_RESERVED_10  ( 10),  //reserved for later use
  H5T_STR_RESERVED_11  ( 11),  //reserved for later use	
  H5T_STR_RESERVED_12  ( 12),  //reserved for later use	
  H5T_STR_RESERVED_13  ( 13),  //reserved for later use
  H5T_STR_RESERVED_14  ( 14),  //reserved for later use
  H5T_STR_RESERVED_15  ( 15);  //reserved for later use
  public static final int H5T_NSTR = H5T_STR_RESERVED_3.getCode();		//num H5T_str_t types actually defined
	private static final Map<Integer, H5T_str_t> lookup = new HashMap<Integer, H5T_str_t>();

	static {
		for (H5T_str_t s : EnumSet.allOf(H5T_str_t.class))
			lookup.put(s.getCode(), s);
	}

	private int code;

	H5T_str_t(int str_type) {
		this.code = str_type;
	}

	public int getCode() {
		return this.code;
	}

	public static H5T_str_t get(int code) {
		return lookup.get(code);
	}
}
