package hdf.h5.enums.H5G;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

// Types of link storage for groups.

public enum H5G_storage_type_t {
  H5G_STORAGE_TYPE_UNKNOWN     (-1),	// Unknown link storage type
  H5G_STORAGE_TYPE_SYMBOL_TABLE (0),  // Links in group are stored with a "symbol table"
                                      // (this is sometimes called "old-style" groups
  H5G_STORAGE_TYPE_COMPACT      (1),	// Links are stored in object header
  H5G_STORAGE_TYPE_DENSE        (2); 	// Links are stored in fractal heap & indexed with v2 B-tree
	private static final Map<Integer, H5G_storage_type_t> lookup = new HashMap<Integer, H5G_storage_type_t>();

	static {
		for (H5G_storage_type_t s : EnumSet.allOf(H5G_storage_type_t.class))
			lookup.put(s.getCode(), s);
	}

	private int code;

	H5G_storage_type_t(int type) {
		this.code = type;
	}

	public int getCode() {
		return this.code;
	}

	public static H5G_storage_type_t get(int code) {
		return lookup.get(code);
	}
}
