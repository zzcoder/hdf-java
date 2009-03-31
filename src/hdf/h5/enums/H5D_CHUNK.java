/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Copyright by The HDF Group.                                               *
 * Copyright by the Board of Trustees of the University of Illinois.         *
 * All rights reserved.                                                      *
 *                                                                           *
 * This file is part of HDF Java Products. The full HDF Java copyright       *
 * notice, including terms governing use, modification, and redistribution,  *
 * is contained in the file, COPYING.  COPYING can be found at the root of   *
 * the source code distribution tree. You can also access it online  at      *
 * http://www.hdfgroup.org/products/licenses.html.  If you do not have       *
 * access to the file, you may request a copy from help@hdfgroup.org.        *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

package hdf.h5.enums;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum H5D_CHUNK {
	BTREE			( 0);	// v1 B-tree index
	private static final Map<Integer, H5D_CHUNK> lookup = new HashMap<Integer, H5D_CHUNK>();

	static {
		for (H5D_CHUNK s : EnumSet.allOf(H5D_CHUNK.class))
			lookup.put(s.getCode(), s);
	}

	private int code;

	H5D_CHUNK(int chunk_index_type) {
		this.code = chunk_index_type;
	}

	public int getCode() {
		return this.code;
	}

	public static H5D_CHUNK get(int code) {
		return lookup.get(code);
	}

}
