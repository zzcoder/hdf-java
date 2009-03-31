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

//The difference between a single file and a set of mounted files
public enum H5F_SCOPE {
  LOCAL	(0),	//specified file handle only	
  GLOBAL(1),	//entire virtual file		
  DOWN  (2);	//for internal use only		
	private static final Map<Integer, H5F_SCOPE> lookup = new HashMap<Integer, H5F_SCOPE>();

	static {
		for (H5F_SCOPE s : EnumSet.allOf(H5F_SCOPE.class))
			lookup.put(s.getCode(), s);
	}

	private int code;

	H5F_SCOPE(int scope_type) {
		this.code = scope_type;
	}

	public int getCode() {
		return this.code;
	}

	public static H5F_SCOPE get(int code) {
		return lookup.get(code);
	}
}
