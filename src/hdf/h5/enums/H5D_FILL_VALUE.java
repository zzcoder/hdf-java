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

// Values for fill value status
public enum H5D_FILL_VALUE {
  ERROR        (-1),
  UNDEFINED    ( 0),
  DEFAULT      ( 1),
  USER_DEFINED ( 2);
	private static final Map<Integer, H5D_FILL_VALUE> lookup = new HashMap<Integer, H5D_FILL_VALUE>();

	static {
		for (H5D_FILL_VALUE s : EnumSet.allOf(H5D_FILL_VALUE.class))
			lookup.put(s.getCode(), s);
	}

	private int code;

	H5D_FILL_VALUE(int fill_value_type) {
		this.code = fill_value_type;
	}

	public int getCode() {
		return this.code;
	}

	public static H5D_FILL_VALUE get(int code) {
		return lookup.get(code);
	}

}
