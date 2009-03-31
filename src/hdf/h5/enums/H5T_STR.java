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

// Type of padding to use in character strings.  Do not change these values
// since they appear in HDF5 files!

public enum H5T_STR {
  ERROR        (-1),   //error 
  NULLTERM     ( 0),   //null terminate like in C
  NULLPAD      ( 1),   //pad with nulls
  SPACEPAD     ( 2),   //pad with spaces like in Fortran 
  RESERVED_3   ( 3),   //reserved for later use
  RESERVED_4   ( 4),   //reserved for later use
  RESERVED_5   ( 5),   //reserved for later use
  RESERVED_6   ( 6),   //reserved for later use
  RESERVED_7   ( 7),   //reserved for later use
  RESERVED_8   ( 8),   //reserved for later use
  RESERVED_9   ( 9),   //reserved for later use
  RESERVED_10  ( 10),  //reserved for later use
  RESERVED_11  ( 11),  //reserved for later use	
  RESERVED_12  ( 12),  //reserved for later use	
  RESERVED_13  ( 13),  //reserved for later use
  RESERVED_14  ( 14),  //reserved for later use
  RESERVED_15  ( 15);  //reserved for later use
  public static final int H5T_NSTR = RESERVED_3.getCode();		//num H5T_str_t types actually defined
	private static final Map<Integer, H5T_STR> lookup = new HashMap<Integer, H5T_STR>();

	static {
		for (H5T_STR s : EnumSet.allOf(H5T_STR.class))
			lookup.put(s.getCode(), s);
	}

	private int code;

	H5T_STR(int str_type) {
		this.code = str_type;
	}

	public int getCode() {
		return this.code;
	}

	public static H5T_STR get(int code) {
		return lookup.get(code);
	}
}
