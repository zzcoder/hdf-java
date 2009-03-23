package hdf.h5.enums.H5T;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

// How is the `bkg' buffer used by the conversion function?
public enum H5T_bkg_t {
  H5T_BKG_NO		(0), 	//background buffer is not needed, send NULL
  H5T_BKG_TEMP	(1),	//bkg buffer used as temp storage only
  H5T_BKG_YES		(2);	//init bkg buf with data before conversion 
	private static final Map<Integer, H5T_bkg_t> lookup = new HashMap<Integer, H5T_bkg_t>();

	static {
		for (H5T_bkg_t s : EnumSet.allOf(H5T_bkg_t.class))
			lookup.put(s.getCode(), s);
	}

	private int code;

	H5T_bkg_t(int bkg_type) {
		this.code = bkg_type;
	}

	public int getCode() {
		return this.code;
	}

	public static H5T_bkg_t get(int code) {
		return lookup.get(code);
	}
}
