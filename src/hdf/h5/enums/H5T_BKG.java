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

// How is the `bkg' buffer used by the conversion function?
public enum H5T_BKG {
  NO		(0), 	//background buffer is not needed, send NULL
  TEMP	(1),	//bkg buffer used as temp storage only
  YES		(2);	//init bkg buf with data before conversion 
	private static final Map<Integer, H5T_BKG> lookup = new HashMap<Integer, H5T_BKG>();

	static {
		for (H5T_BKG s : EnumSet.allOf(H5T_BKG.class))
			lookup.put(s.getCode(), s);
	}

	private int code;

	H5T_BKG(int bkg_type) {
		this.code = bkg_type;
	}

	public int getCode() {
		return this.code;
	}

	public static H5T_BKG get(int code) {
		return lookup.get(code);
	}
}
