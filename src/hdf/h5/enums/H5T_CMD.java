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

// Commands sent to conversion functions
public enum H5T_CMD {
  CONV_INIT	(0),	//query and/or initialize private data
  CONV_CONV	(1), 	//convert data from source to dest datatype 
  CONV_FREE	(2);	//function is being removed from path	   
	private static final Map<Integer, H5T_CMD> lookup = new HashMap<Integer, H5T_CMD>();

	static {
		for (H5T_CMD s : EnumSet.allOf(H5T_CMD.class))
			lookup.put(s.getCode(), s);
	}

	private int code;

	H5T_CMD(int cmd_type) {
		this.code = cmd_type;
	}

	public int getCode() {
		return this.code;
	}

	public static H5T_CMD get(int code) {
		return lookup.get(code);
	}
}
