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

// Library type values.  Start with `1' instead of `0' because it makes the
// tracing output look better when hid_t values are large numbers.  Change the
// TYPE_BITS in H5I.c if the MAXID gets larger than 32 (an assertion will
// fail otherwise).

public enum H5I_TYPE {
  UNINIT	    (-2),    //uninitialized type
  BADID		    (-1),	   //invalid Type
  FILE		    ( 1),	   //type ID for File objects	
  GROUP       ( 2),		 //type ID for Group objects
  DATATYPE    ( 3),	   //type ID for Datatype objects	
  DATASPACE   ( 4),	   //type ID for Dataspace objects	
  DATASET     ( 5),	   //type ID for Dataset objects	
  ATTR        ( 6),		 //type ID for Attribute objects	
  REFERENCE   ( 7),	   //type ID for Reference objects	
  VFL         ( 8),		 //type ID for virtual file layer
  GENPROP_CLS ( 9),    //type ID for generic property list classes
  GENPROP_LST (10),    //type ID for generic property lists 
  ERROR_CLASS (11),    //type ID for error classes	
  ERROR_MSG   (12),    //type ID for error messages	
  ERROR_STACK (13),    //type ID for error stacks	
  NTYPES      (14);		 //number of library types, MUST BE LAST! 
	private static final Map<Integer, H5I_TYPE> lookup = new HashMap<Integer, H5I_TYPE>();

	static {
		for (H5I_TYPE s : EnumSet.allOf(H5I_TYPE.class))
			lookup.put(s.getCode(), s);
	}

	private int code;

	H5I_TYPE(int type) {
		this.code = type;
	}

	public int getCode() {
		return this.code;
	}

	public static H5I_TYPE get(int code) {
		return lookup.get(code);
	}
}
