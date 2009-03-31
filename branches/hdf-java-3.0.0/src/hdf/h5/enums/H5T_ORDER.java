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

// Byte orders
public enum H5T_ORDER {
  ERROR      (-1),  //error 
  LE         ( 0),  //little endian
  BE         ( 1),  //bit endian   
  VAX        ( 2),  //VAX mixed endian
  NONE       ( 3);  //no particular order (strings, bits,..)
  //NONE must be last
	private static final Map<Integer, H5T_ORDER> lookup = new HashMap<Integer, H5T_ORDER>();

	static {
		for (H5T_ORDER s : EnumSet.allOf(H5T_ORDER.class))
			lookup.put(s.getCode(), s);
	}

	private int code;

	H5T_ORDER(int order_type) {
		this.code = order_type;
	}

	public int getCode() {
		return this.code;
	}

	public static H5T_ORDER get(int code) {
		return lookup.get(code);
	}
}
