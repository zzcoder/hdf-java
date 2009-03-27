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

public enum H5T_CSET {
  ERROR       (-1 ),   //error 
  ASCII       ( 0 ),   //US ASCII
  UTF8        ( 1 ),   //UTF-8 Unicode encoding
  RESERVED_2  ( 2 ),   //reserved for later use
  RESERVED_3  ( 3 ),   //reserved for later use
  RESERVED_4  ( 4 ),   //reserved for later use
  RESERVED_5  ( 5 ),   //reserved for later use
  RESERVED_6  ( 6 ),   //reserved for later use
  RESERVED_7  ( 7 ),   //reserved for later use
  RESERVED_8  ( 8 ),   //reserved for later use
  RESERVED_9  ( 9 ),   //reserved for later use
  RESERVED_10 ( 10 ),  //reserved for later use
  RESERVED_11 ( 11 ),  //reserved for later use
  RESERVED_12 ( 12 ),  //reserved for later use
  RESERVED_13 ( 13 ),  //reserved for later use
  RESERVED_14 ( 14 ),  //reserved for later use
  RESERVED_15 ( 15 );  //reserved for later use
	private static final Map<Integer, H5T_CSET> lookup = new HashMap<Integer, H5T_CSET>();

	static {
		for (H5T_CSET s : EnumSet.allOf(H5T_CSET.class))
			lookup.put(s.getCode(), s);
	}

	private int code;

	H5T_CSET(int cset_type) {
		this.code = cset_type;
	}

	public int getCode() {
		return this.code;
	}

	public static H5T_CSET get(int code) {
		return lookup.get(code);
	}
}
