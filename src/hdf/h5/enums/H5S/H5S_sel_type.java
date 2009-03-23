package hdf.h5.enums.H5S;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

// Enumerated type for the type of selection
public enum H5S_sel_type {
  H5S_SEL_ERROR       (-1),  // Error
  H5S_SEL_NONE        ( 0),  // Nothing selected
  H5S_SEL_POINTS      ( 1),  // Sequence of points selected
  H5S_SEL_HYPERSLABS  ( 2),  // "New-style" hyperslab selection defined 
  H5S_SEL_ALL         ( 3),  // Entire extent selected
  H5S_SEL_N           ( 4);  // THIS MUST BE LAST
  private static final Map<Integer, H5S_sel_type> lookup = new HashMap<Integer, H5S_sel_type>();

  static {
    for (H5S_sel_type s : EnumSet.allOf(H5S_sel_type.class))
      lookup.put(s.getCode(), s);
  }

  private int code;

  H5S_sel_type(int type) {
    this.code = type;
  }

  public int getCode() {
    return this.code;
  }

  public static H5S_sel_type get(int code) {
    return lookup.get(code);
  }

}
