package hdf.h5.enums.H5S;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

// DDifferent ways of combining selections
public enum H5S_seloper_t {
  H5S_SELECT_NOOP         (-1),  // error
  H5S_SELECT_SET          ( 0),  // Select "set" operation
  H5S_SELECT_OR           ( 1),  // Binary "or" operation for hyperslabs
                                 // (add new selection to existing selection)
                                 // Original region:  AAAAAAAAAA
                                 // New region:             BBBBBBBBBB
                                 // A or B:           CCCCCCCCCCCCCCCC
  H5S_SELECT_AND          ( 2),  // Binary "and" operation for hyperslabs
                                 // (only leave overlapped regions in selection)
                                 // Original region:  AAAAAAAAAA
                                 // New region:             BBBBBBBBBB
                                 // A and B:                CCCC
  H5S_SELECT_XOR          ( 3),  // Binary "xor" operation for hyperslabs
                                 // (only leave non-overlapped regions in selection)
                                 // Original region:  AAAAAAAAAA
                                 // New region:             BBBBBBBBBB
                                 // A xor B:          CCCCCC    CCCCCC
  H5S_SELECT_NOTB         ( 4),  // Binary "not" operation for hyperslabs
                                 // (only leave non-overlapped regions in original selection)
                                 // Original region:  AAAAAAAAAA
                                 // New region:             BBBBBBBBBB
                                 // A not B:          CCCCCC
  H5S_SELECT_NOTA         ( 5),  // Binary "not" operation for hyperslabs
                                 // (only leave non-overlapped regions in new selection)
                                 // Original region:  AAAAAAAAAA
                                 // New region:             BBBBBBBBBB
                                 // B not A:                    CCCCCC
  H5S_SELECT_APPEND       ( 6),  // Append elements to end of point selection
  H5S_SELECT_PREPEND      ( 7),  // Prepend elements to beginning of point selection
  H5S_SELECT_INVALID      ( 8);  // Invalid upper bound on selection operations
  private static final Map<Integer, H5S_seloper_t> lookup = new HashMap<Integer, H5S_seloper_t>();

  static {
    for (H5S_seloper_t s : EnumSet.allOf(H5S_seloper_t.class))
      lookup.put(s.getCode(), s);
  }

  private int code;

  H5S_seloper_t(int type) {
    this.code = type;
  }

  public int getCode() {
    return this.code;
  }

  public static H5S_seloper_t get(int code) {
    return lookup.get(code);
  }

}
