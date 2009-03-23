package hdf.h5.enums.H5D;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum H5D_chunk_index_t {
	H5D_CHUNK_BTREE			( 0);	// v1 B-tree index
	private static final Map<Integer, H5D_chunk_index_t> lookup = new HashMap<Integer, H5D_chunk_index_t>();

	static {
		for (H5D_chunk_index_t s : EnumSet.allOf(H5D_chunk_index_t.class))
			lookup.put(s.getCode(), s);
	}

	private int code;

	H5D_chunk_index_t(int chunk_index_type) {
		this.code = chunk_index_type;
	}

	public int getCode() {
		return this.code;
	}

	public static H5D_chunk_index_t get(int code) {
		return lookup.get(code);
	}

}
