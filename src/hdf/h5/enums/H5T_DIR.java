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

// The order to retrieve atomic native datatype
public enum H5T_DIR {
  DEFAULT     (0),    //default direction is inscendent
  ASCEND      (1),    //in inscendent order
  DESCEND     (2);    //in descendent order
	private static final Map<Integer, H5T_DIR> lookup = new HashMap<Integer, H5T_DIR>();

	static {
		for (H5T_DIR s : EnumSet.allOf(H5T_DIR.class))
			lookup.put(s.getCode(), s);
	}

	private int code;

	H5T_DIR(int direction_type) {
		this.code = direction_type;
	}

	public int getCode() {
		return this.code;
	}

	public static H5T_DIR get(int code) {
		return lookup.get(code);
	}
}