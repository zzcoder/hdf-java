/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Copyright by The HDF Group.                                               *
 * Copyright by the Board of Trustees of the University of Illinois.         *
 * All rights reserved.                                                      *
 *                                                                           *
 * This file is part of HDF Java Products. The full HDF Java copyright       *
 * notice, including terms governing use, modification, and redistribution,  *
 * is contained in the file, COPYING.  COPYING can be found at the root of   *
 * the source code distribution tree. You can also access it online  at      *
 * http://www.hdfgroup.org/products/licenses.html.  If you do not have       *
 * access to the file, you may request a copy from help@hdfgroup.org.        *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

package hdf.h5.enums;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

// An object has a certain type. The first few numbers are reserved for use
// internally by HDF5. Users may add their own types with higher values.  The
// values are never stored in the file -- they only exist while an
// application is running.  An object may satisfy the `isa' function for more
// than one type.

public enum H5G_OBJECT_TYPE {
  UNKNOWN     (-1),   // Unknown object type
  GROUP       ( 0),   // Object is a group
  DATASET     ( 1),   // Object is a dataset 
  TYPE        ( 2),   // Object is a named data type
  LINK        ( 3),   // Object is a symbolic link
  UDLINK      ( 4),   // Object is a user-defined link
  RESERVED_5  ( 5),   // Reserved for future use
  RESERVED_6  ( 6),   // Reserved for future use 
  RESERVED_7  ( 7);   // Reserved for future use
	private static final Map<Integer, H5G_OBJECT_TYPE> lookup = new HashMap<Integer, H5G_OBJECT_TYPE>();

	static {
		for (H5G_OBJECT_TYPE s : EnumSet.allOf(H5G_OBJECT_TYPE.class))
			lookup.put(s.getCode(), s);
	}

	private int code;

	H5G_OBJECT_TYPE(int type) {
		this.code = type;
	}

	public int getCode() {
		return this.code;
	}

	public static H5G_OBJECT_TYPE get(int code) {
		return lookup.get(code);
	}
}
