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

// Link class types.
// Values less than 64 are reserved for the HDF5 library's internal use.
// Values 64 to 255 are for "user-defined" link class types; these types are
// defined by HDF5 but their behavior can be overridden by users.
// Users who want to create new classes of links should contact the HDF5
// development team at hdfhelp@ncsa.uiuc.edu .
// These values can never change because they appear in HDF5 files.
public enum H5L_TYPE {
  ERROR     (-1),   // Invalid link type id
  HARD      ( 0),   // Hard link id 
  SOFT      ( 1),   // Soft link id
  EXTERNAL  (64),   // External link id 
  MAX 		 (255);	  // Maximum link type id 
  static final int BUILTIN_MAX = SOFT.getCode();      // Maximum value link value for "built-in" link types
  static final int UD_MIN      = EXTERNAL.getCode();  // Link ids at or above this value are "user-defined" link types. 
  
	private static final Map<Integer, H5L_TYPE> lookup = new HashMap<Integer, H5L_TYPE>();

	static {
		for (H5L_TYPE s : EnumSet.allOf(H5L_TYPE.class))
			lookup.put(s.getCode(), s);
	}

	private int code;

	H5L_TYPE(int type) {
		this.code = type;
	}

	public int getCode() {
		return this.code;
	}

	public static H5L_TYPE get(int code) {
		return lookup.get(code);
	}
}
