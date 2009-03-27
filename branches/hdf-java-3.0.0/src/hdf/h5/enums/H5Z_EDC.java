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

//Values to decide if EDC is enabled for reading data
public enum H5Z_EDC {
  ERROR_EDC       (-1),   // error value 
  DISABLE_EDC     ( 0),
  ENABLE_EDC      ( 1),
  NO_EDC          ( 2);   // must be the last 
	private static final Map<Integer, H5Z_EDC> lookup = new HashMap<Integer, H5Z_EDC>();

	static {
		for (H5Z_EDC s : EnumSet.allOf(H5Z_EDC.class))
			lookup.put(s.getCode(), s);
	}

	private int code;

	H5Z_EDC(int type) {
		this.code = type;
	}

	public int getCode() {
		return this.code;
	}

	public static H5Z_EDC get(int code) {
		return lookup.get(code);
	}
}
