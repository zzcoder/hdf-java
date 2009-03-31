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

//Special parameters for ScaleOffset filter
public enum H5Z_SO_SCALE {
  FLOAT_DSCALE (0),
  FLOAT_ESCALE (1),
  INT          (2);
  
  public static final int INT_MINBITS_DEFAULT = 0;

	private static final Map<Integer, H5Z_SO_SCALE> lookup = new HashMap<Integer, H5Z_SO_SCALE>();

	static {
		for (H5Z_SO_SCALE s : EnumSet.allOf(H5Z_SO_SCALE.class))
			lookup.put(s.getCode(), s);
	}

	private int code;

	H5Z_SO_SCALE(int type) {
		this.code = type;
	}

	public int getCode() {
		return this.code;
	}

	public static H5Z_SO_SCALE get(int code) {
		return lookup.get(code);
	}
}
