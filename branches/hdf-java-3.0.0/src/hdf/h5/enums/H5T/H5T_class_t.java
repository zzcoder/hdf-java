package hdf.h5.enums.H5T;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

// These are the various classes of datatypes 
// If this goes over 16 types (0-15), the file format will need to change)
public enum H5T_class_t {
  H5T_NO_CLASS         (-1),  //error   
  H5T_INTEGER          (0),   //integer types   
  H5T_FLOAT            (1),   //floating-point types 
  H5T_TIME             (2),   //date and time types    
  H5T_STRING           (3),   //character string types 
  H5T_BITFIELD         (4),   //bit field types        
  H5T_OPAQUE           (5),   //opaque types           
  H5T_COMPOUND         (6),   //compound types         
  H5T_REFERENCE        (7),   //reference types        
  H5T_ENUM		         (8),		//enumeration types      
  H5T_VLEN		 				 (9),		//Variable-Length types  
  H5T_ARRAY	         (10),		//Array types            
  H5T_NCLASSES       (11);    //this must be last      
	private static final Map<Integer, H5T_class_t> lookup = new HashMap<Integer, H5T_class_t>();

	static {
		for (H5T_class_t s : EnumSet.allOf(H5T_class_t.class))
			lookup.put(s.getCode(), s);
	}

	private int code;

	H5T_class_t(int class_type) {
		this.code = class_type;
	}

	public int getCode() {
		return this.code;
	}

	public static H5T_class_t get(int code) {
		return lookup.get(code);
	}
}
