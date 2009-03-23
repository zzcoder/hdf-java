package hdf.h5.enums.H5T;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum H5T_cset_t {
  H5T_CSET_ERROR       (-1 ),  /*error                                      */
  H5T_CSET_ASCII       ( 0 ),   /*US ASCII                                   */
  H5T_CSET_UTF8        ( 1 ),   /*UTF-8 Unicode encoding		     */
  H5T_CSET_RESERVED_2  ( 2 ),   /*reserved for later use		     */
  H5T_CSET_RESERVED_3  ( 3 ),   /*reserved for later use		     */
  H5T_CSET_RESERVED_4  ( 4 ),   /*reserved for later use		     */
  H5T_CSET_RESERVED_5  ( 5 ),   /*reserved for later use		     */
  H5T_CSET_RESERVED_6  ( 6 ),   /*reserved for later use		     */
  H5T_CSET_RESERVED_7  ( 7 ),   /*reserved for later use		     */
  H5T_CSET_RESERVED_8  ( 8 ),   /*reserved for later use		     */
  H5T_CSET_RESERVED_9  ( 9 ),   /*reserved for later use		     */
  H5T_CSET_RESERVED_10 ( 10 ),  /*reserved for later use		     */
  H5T_CSET_RESERVED_11 ( 11 ),  /*reserved for later use		     */
  H5T_CSET_RESERVED_12 ( 12 ),  /*reserved for later use		     */
  H5T_CSET_RESERVED_13 ( 13 ),  /*reserved for later use		     */
  H5T_CSET_RESERVED_14 ( 14 ),  /*reserved for later use		     */
  H5T_CSET_RESERVED_15 ( 15 );   /*reserved for later use		     */
	private static final Map<Integer, H5T_cset_t> lookup = new HashMap<Integer, H5T_cset_t>();

	static {
		for (H5T_cset_t s : EnumSet.allOf(H5T_cset_t.class))
			lookup.put(s.getCode(), s);
	}

	private int code;

	H5T_cset_t(int cset_type) {
		this.code = cset_type;
	}

	public int getCode() {
		return this.code;
	}

	public static H5T_cset_t get(int code) {
		return lookup.get(code);
	}
}
