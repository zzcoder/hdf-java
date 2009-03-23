package ncsa.hdf.hdf5lib.h5.enums.H5F;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

// How does file close behave?
// H5F_CLOSE_DEFAULT - Use the degree pre-defined by underlining VFL
// H5F_CLOSE_WEAK    - file closes only after all opened objects are closed
// H5F_CLOSE_SEMI    - if no opened objects, file is close; otherwise, file
//	       close fails
// H5F_CLOSE_STRONG  - if there are opened objects, close them first, then
//	       close file
//
public enum H5F_close_degree_t {
  H5F_CLOSE_DEFAULT   (0),
  H5F_CLOSE_WEAK      (1),
  H5F_CLOSE_SEMI      (2),
  H5F_CLOSE_STRONG    (3);
	private static final Map<Integer, H5F_close_degree_t> lookup = new HashMap<Integer, H5F_close_degree_t>();

	static {
		for (H5F_close_degree_t s : EnumSet.allOf(H5F_close_degree_t.class))
			lookup.put(s.getCode(), s);
	}

	private int code;

	H5F_close_degree_t(int close_degree_type) {
		this.code = close_degree_type;
	}

	public int getCode() {
		return this.code;
	}

	public static H5F_close_degree_t get(int code) {
		return lookup.get(code);
	}
}
