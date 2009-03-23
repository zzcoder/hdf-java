package hdf.h5.enums.H5I;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

// Library type values.  Start with `1' instead of `0' because it makes the
// tracing output look better when hid_t values are large numbers.  Change the
// TYPE_BITS in H5I.c if the MAXID gets larger than 32 (an assertion will
// fail otherwise).

public enum H5I_type_t {
  H5I_UNINIT	(-2), /*uninitialized type			    */
  H5I_BADID		(-1),	//invalid Type
  H5I_FILE		(1),	//type ID for File objects	
  H5I_GROUP   (2),		        //type ID for Group objects
  H5I_DATATYPE  (3),	        //type ID for Datatype objects	
  H5I_DATASPACE (4),	        //type ID for Dataspace objects	
  H5I_DATASET   (5),	        //type ID for Dataset objects	
  H5I_ATTR      (6),		        //type ID for Attribute objects	
  H5I_REFERENCE (7),	        //type ID for Reference objects	
  H5I_VFL       (8),			//type ID for virtual file layer
  H5I_GENPROP_CLS (9),            //type ID for generic property list classes
  H5I_GENPROP_LST (10),            //type ID for generic property lists 
  H5I_ERROR_CLASS (11),            //type ID for error classes	
  H5I_ERROR_MSG   (12),              //type ID for error messages	
  H5I_ERROR_STACK (13),            //type ID for error stacks	
  H5I_NTYPES      (14);		        //number of library types, MUST BE LAST! 
	private static final Map<Integer, H5I_type_t> lookup = new HashMap<Integer, H5I_type_t>();

	static {
		for (H5I_type_t s : EnumSet.allOf(H5I_type_t.class))
			lookup.put(s.getCode(), s);
	}

	private int code;

	H5I_type_t(int type) {
		this.code = type;
	}

	public int getCode() {
		return this.code;
	}

	public static H5I_type_t get(int code) {
		return lookup.get(code);
	}
}
