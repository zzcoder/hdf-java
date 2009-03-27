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

// How does file close behave?
// H5F_CLOSE_DEFAULT - Use the degree pre-defined by underlining VFL
// H5F_CLOSE_WEAK    - file closes only after all opened objects are closed
// H5F_CLOSE_SEMI    - if no opened objects, file is close; otherwise, file
//	       close fails
// H5F_CLOSE_STRONG  - if there are opened objects, close them first, then
//	       close file
//
public enum H5F_CLOSE_DEGREE {
  DEFAULT   (0),
  WEAK      (1),
  SEMI      (2),
  STRONG    (3);
	private static final Map<Integer, H5F_CLOSE_DEGREE> lookup = new HashMap<Integer, H5F_CLOSE_DEGREE>();

	static {
		for (H5F_CLOSE_DEGREE s : EnumSet.allOf(H5F_CLOSE_DEGREE.class))
			lookup.put(s.getCode(), s);
	}

	private int code;

	H5F_CLOSE_DEGREE(int close_degree_type) {
		this.code = close_degree_type;
	}

	public int getCode() {
		return this.code;
	}

	public static H5F_CLOSE_DEGREE get(int code) {
		return lookup.get(code);
	}
}
