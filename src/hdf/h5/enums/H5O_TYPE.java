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

//Types of objects in file
public enum H5O_TYPE {
  UNKNOWN        (-1),   // Unknown object type
  GROUP          ( 0),   // Object is a group
  DATASET        ( 1),   // Object is a dataset
  NAMED_DATATYPE ( 2),   // Object is a named data type 
  NTYPES         ( 3);   // Number of different object types (must be last!)
	private static final Map<Integer, H5O_TYPE> lookup = new HashMap<Integer, H5O_TYPE>();

	static {
		for (H5O_TYPE s : EnumSet.allOf(H5O_TYPE.class))
			lookup.put(s.getCode(), s);
	}

	private int code;

	H5O_TYPE(int type) {
		this.code = type;
	}

	public int getCode() {
		return this.code;
	}

	public static H5O_TYPE get(int code) {
		return lookup.get(code);
	}
}
