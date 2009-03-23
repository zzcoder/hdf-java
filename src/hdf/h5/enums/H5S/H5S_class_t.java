package hdf.h5.enums.H5S;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

// Different types of dataspaces
public enum H5S_class_t {
  H5S_NO_CLASS         (-1),  // error
  H5S_SCALAR           ( 0),  // scalar variable
  H5S_SIMPLE           ( 1),  // simple data space
  H5S_NULL             ( 2);  // null data space
  private static final Map<Integer, H5S_class_t> lookup = new HashMap<Integer, H5S_class_t>();

  static {
    for (H5S_class_t s : EnumSet.allOf(H5S_class_t.class))
      lookup.put(s.getCode(), s);
  }

  private int code;

  H5S_class_t(int type) {
    this.code = type;
  }

  public int getCode() {
    return this.code;
  }

  public static H5S_class_t get(int code) {
    return lookup.get(code);
  }

}
