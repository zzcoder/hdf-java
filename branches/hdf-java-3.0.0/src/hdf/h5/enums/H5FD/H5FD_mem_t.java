package hdf.h5.enums.H5FD;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

// Types of allocation requests. The values larger than H5FD_MEM_DEFAULT
// should not change other than adding new types to the end. These numbers
// might appear in files.
public enum H5FD_mem_t {
	    H5FD_MEM_NOLIST		(-1),			//must be negative
	    H5FD_MEM_DEFAULT	( 0),			//must be zero
	    H5FD_MEM_SUPER    ( 1),
	    H5FD_MEM_BTREE    ( 2),
	    H5FD_MEM_DRAW     ( 3),
	    H5FD_MEM_GHEAP    ( 4),
	    H5FD_MEM_LHEAP    ( 5),
	    H5FD_MEM_OHDR     ( 6),

	    H5FD_MEM_NTYPES		(7);		//must be last
	  	private static final Map<Integer, H5FD_mem_t> lookup = new HashMap<Integer, H5FD_mem_t>();

	  	static {
	  		for (H5FD_mem_t s : EnumSet.allOf(H5FD_mem_t.class))
	  			lookup.put(s.getCode(), s);
	  	}

	  	private int code;

	  	H5FD_mem_t(int type) {
	  		this.code = type;
	  	}

	  	public int getCode() {
	  		return this.code;
	  	}

	  	public static H5FD_mem_t get(int code) {
	  		return lookup.get(code);
	  	}
}
