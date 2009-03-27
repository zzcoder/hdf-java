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

public enum H5D_LAYOUT {
  ERROR	      (-1),
  COMPACT			( 0),	//raw data is very small
  CONTIGUOUS	( 1),	//the default
  CHUNKED			( 2),	//slow and fancy
  NLAYOUTS		( 3);	//this one must be last!
	private static final Map<Integer, H5D_LAYOUT> lookup = new HashMap<Integer, H5D_LAYOUT>();

	static {
		for (H5D_LAYOUT s : EnumSet.allOf(H5D_LAYOUT.class))
			lookup.put(s.getCode(), s);
	}

	private int code;

	H5D_LAYOUT(int layout_type) {
		this.code = layout_type;
	}

	public int getCode() {
		return this.code;
	}

	public static H5D_LAYOUT get(int code) {
		return lookup.get(code);
	}

}
