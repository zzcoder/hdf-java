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

// Types of integer sign schemes
public enum H5T_SGN {
  ERROR        (-1),   //error
  NONE         ( 0),   //this is an unsigned type
  COMP2        ( 1),   //two's complement
  NSGN         ( 2);   //this must be last!
	private static final Map<Integer, H5T_SGN> lookup = new HashMap<Integer, H5T_SGN>();

	static {
		for (H5T_SGN s : EnumSet.allOf(H5T_SGN.class))
			lookup.put(s.getCode(), s);
	}

	private int code;

	H5T_SGN(int sign_type) {
		this.code = sign_type;
	}

	public int getCode() {
		return this.code;
	}

	public static H5T_SGN get(int code) {
		return lookup.get(code);
	}
}
