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

// Conversion function persistence
public enum H5T_PERS {
  DONTCARE	(-1), //wild card
  HARD			( 0),	//hard conversion function
  SOFT			( 1);	//soft conversion function
	private static final Map<Integer, H5T_PERS> lookup = new HashMap<Integer, H5T_PERS>();

	static {
		for (H5T_PERS s : EnumSet.allOf(H5T_PERS.class))
			lookup.put(s.getCode(), s);
	}

	private int code;

	H5T_PERS(int pers_type) {
		this.code = pers_type;
	}

	public int getCode() {
		return this.code;
	}

	public static H5T_PERS get(int code) {
		return lookup.get(code);
	}
}
