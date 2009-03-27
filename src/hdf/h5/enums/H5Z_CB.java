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

//Return values for filter callback function
public enum H5Z_CB {
  ERROR  (-1),
  FAIL   ( 0),    // I/O should fail if filter fails.
  CONT   ( 1),    // I/O continues if filter fails.
  NO     ( 2);
	private static final Map<Integer, H5Z_CB> lookup = new HashMap<Integer, H5Z_CB>();

	static {
		for (H5Z_CB s : EnumSet.allOf(H5Z_CB.class))
			lookup.put(s.getCode(), s);
	}

	private int code;

	H5Z_CB(int type) {
		this.code = type;
	}

	public int getCode() {
		return this.code;
	}

	public static H5Z_CB get(int code) {
		return lookup.get(code);
	}
}
