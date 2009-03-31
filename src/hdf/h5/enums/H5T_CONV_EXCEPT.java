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

// The exception type passed into the conversion callback function
public enum H5T_CONV_EXCEPT {
  RANGE_HI       (0),   //source value is greater than destination's range
  RANGE_LOW      (1),   //source value is less than destination's range 
  PRECISION      (2),   //source value loses precision in destination 
  TRUNCATE       (3),   //source value is truncated in destination 
  PINF           (4),   //source value is positive infinity(floating number)
  NINF           (5),   //source value is negative infinity(floating number)
  NAN            (6);   //source value is NaN(floating number) 
	private static final Map<Integer, H5T_CONV_EXCEPT> lookup = new HashMap<Integer, H5T_CONV_EXCEPT>();

	static {
		for (H5T_CONV_EXCEPT s : EnumSet.allOf(H5T_CONV_EXCEPT.class))
			lookup.put(s.getCode(), s);
	}

	private int code;

	H5T_CONV_EXCEPT(int conv_except_type) {
		this.code = conv_except_type;
	}

	public int getCode() {
		return this.code;
	}

	public static H5T_CONV_EXCEPT get(int code) {
		return lookup.get(code);
	}
}
