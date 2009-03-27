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

// Floating-point normalization schemes
public enum H5T_NORM {
  ERROR       (-1),   //error
  IMPLIED     ( 0),   //msb of mantissa isn't stored, always 1
  MSBSET      ( 1),   //msb of mantissa is always 1
  NONE        ( 2);   //not normalized 
  //NONE must be last
	private static final Map<Integer, H5T_NORM> lookup = new HashMap<Integer, H5T_NORM>();

	static {
		for (H5T_NORM s : EnumSet.allOf(H5T_NORM.class))
			lookup.put(s.getCode(), s);
	}

	private int code;

	H5T_NORM(int norm_type) {
		this.code = norm_type;
	}

	public int getCode() {
		return this.code;
	}

	public static H5T_NORM get(int code) {
		return lookup.get(code);
	}
}
