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

// The return value from conversion callback function H5T_conv_except_func_t
public enum H5T_CONV_RET {
  ABORT      (-1),   //abort conversion
  UNHANDLED  ( 0),   //callback function failed to handle the exception
  HANDLED    ( 1);   //callback function handled the exception successfully 
	private static final Map<Integer, H5T_CONV_RET> lookup = new HashMap<Integer, H5T_CONV_RET>();

	static {
		for (H5T_CONV_RET s : EnumSet.allOf(H5T_CONV_RET.class))
			lookup.put(s.getCode(), s);
	}

	private int code;

	H5T_CONV_RET(int conv_ret_type) {
		this.code = conv_ret_type;
	}

	public int getCode() {
		return this.code;
	}

	public static H5T_CONV_RET get(int code) {
		return lookup.get(code);
	}
}
