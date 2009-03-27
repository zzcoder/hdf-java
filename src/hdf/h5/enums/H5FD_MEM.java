/*****************************************************************************
 * Copyright by The HDF Group.                                               *
 * Copyright by the Board of Trustees of the University of Illinois.         *
 * All rights reserved.                                                      *
 *                                                                           *
 * This file is part of the HDF Java Products distribution.                  *
 * The full copyright notice, including terms governing use, modification,   *
 * and redistribution, is contained in the files COPYING and Copyright.html. *
 * COPYING can be found at the root of the source code distribution tree.    *
 * Or, see http://hdfgroup.org/products/hdf-java/doc/Copyright.html.         *
 * If you do not have access to either file, you may request a copy from     *
 * help@hdfgroup.org.                                                        *
 ****************************************************************************/

package hdf.h5.enums;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

// Types of allocation requests. The values larger than H5FD_MEM_DEFAULT
// should not change other than adding new types to the end. These numbers
// might appear in files.
public enum H5FD_MEM {
	    NOLIST	 (-1),			//must be negative
	    DEFAULT	 ( 0),			//must be zero
	    SUPER    ( 1),
	    BTREE    ( 2),
	    DRAW     ( 3),
	    GHEAP    ( 4),
	    LHEAP    ( 5),
	    OHDR     ( 6),
	    NTYPES	 ( 7);		  //must be last
	  	private static final Map<Integer, H5FD_MEM> lookup = new HashMap<Integer, H5FD_MEM>();

	  	static {
	  		for (H5FD_MEM s : EnumSet.allOf(H5FD_MEM.class))
	  			lookup.put(s.getCode(), s);
	  	}

	  	private int code;

	  	H5FD_MEM(int type) {
	  		this.code = type;
	  	}

	  	public int getCode() {
	  		return this.code;
	  	}

	  	public static H5FD_MEM get(int code) {
	  		return lookup.get(code);
	  	}
}
