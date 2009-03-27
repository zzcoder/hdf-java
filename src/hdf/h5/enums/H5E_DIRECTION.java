/*****************************************************************************
 * Copyright by The HDF Group.                                               *
 * Copyright by the Board of Trustees of the University of Illinois.         *
 * All rights reserved.                                                      *
 *                                                                           *
 * This file is part of the HDF Java Products distribution.                  *
 * The full copyright notice, including terms governing use, modification,   *
 * and redistribution, is contained in the files COPYING and Copyright.html. *
 * COPYING can be found at the root of the source code distribution tree.    *
 * Or, see http://hdfgroup.org/products/hdf-java/doc/Copyright.html.         *
 * If you do not have access to either file, you may request a copy from     *
 * help@hdfgroup.org.                                                        *
 ****************************************************************************/

package hdf.h5.enums;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

// Error stack traversal direction
public enum H5E_DIRECTION {
  WALK_UPWARD		( 0),		//begin deep, end at API function
  WALK_DOWNWARD	( 1);		//begin at API function, end deep
	private static final Map<Integer, H5E_DIRECTION> lookup = new HashMap<Integer, H5E_DIRECTION>();

	static {
		for (H5E_DIRECTION s : EnumSet.allOf(H5E_DIRECTION.class))
			lookup.put(s.getCode(), s);
	}

	private int code;

	H5E_DIRECTION(int direction_type) {
		this.code = direction_type;
	}

	public int getCode() {
		return this.code;
	}

	public static H5E_DIRECTION get(int code) {
		return lookup.get(code);
	}

}
