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

// These are the various classes of datatypes 
// If this goes over 16 types (0-15), the file format will need to change)
public enum H5T_CLASS {
  NO_CLASS        (-1),   //error   
  INTEGER          (0),   //integer types   
  FLOAT            (1),   //floating-point types 
  TIME             (2),   //date and time types    
  STRING           (3),   //character string types 
  BITFIELD         (4),   //bit field types        
  OPAQUE           (5),   //opaque types           
  COMPOUND         (6),   //compound types         
  REFERENCE        (7),   //reference types        
  ENUM		         (8),		//enumeration types      
  VLEN		 				 (9),		//Variable-Length types  
  ARRAY	          (10),		//Array types            
  NCLASSES        (11);   //this must be last      
	private static final Map<Integer, H5T_CLASS> lookup = new HashMap<Integer, H5T_CLASS>();

	static {
		for (H5T_CLASS s : EnumSet.allOf(H5T_CLASS.class))
			lookup.put(s.getCode(), s);
	}

	private int code;

	H5T_CLASS(int class_type) {
		this.code = class_type;
	}

	public int getCode() {
		return this.code;
	}

	public static H5T_CLASS get(int code) {
		return lookup.get(code);
	}
}
