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

//Reference types allowed.
public enum H5R_TYPE {
  BADTYPE        (-1),   //invalid Reference Type
  OBJECT         ( 0),   //Object reference
  DATASET_REGION ( 1),   //Dataset Region Reference
  MAXTYPE        ( 2);   //highest type (Invalid as true type)
	private static final Map<Integer, H5R_TYPE> lookup = new HashMap<Integer, H5R_TYPE>();

	static {
		for (H5R_TYPE s : EnumSet.allOf(H5R_TYPE.class))
			lookup.put(s.getCode(), s);
	}

	private int code;

	H5R_TYPE(int type) {
		this.code = type;
	}

	public int getCode() {
		return this.code;
	}

	public static H5R_TYPE get(int code) {
		return lookup.get(code);
	}
}
