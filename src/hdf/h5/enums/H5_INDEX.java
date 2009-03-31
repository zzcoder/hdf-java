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

public enum H5_INDEX {
  UNKNOWN 	(-1),	// Unknown index type			
  NAME 		  ( 0),	// Index on names 	
  CRT_ORDER ( 1),	// Index on creation order 
  N 				( 2);	// Number of indices defined 
	private static final Map<Integer, H5_INDEX> lookup = new HashMap<Integer, H5_INDEX>();

	static {
		for (H5_INDEX s : EnumSet.allOf(H5_INDEX.class))
			lookup.put(s.getCode(), s);
	}

	private int code;

	H5_INDEX(int index_type) {
		this.code = index_type;
	}

	public int getCode() {
		return this.code;
	}

	public static H5_INDEX get(int code) {
		return lookup.get(code);
	}

}
