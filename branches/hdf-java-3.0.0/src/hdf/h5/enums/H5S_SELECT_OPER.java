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

// DDifferent ways of combining selections
public enum H5S_SELECT_OPER {
  NOOP         (-1),  // error
  SET          ( 0),  // Select "set" operation
  OR           ( 1),  // Binary "or" operation for hyperslabs
                      // (add new selection to existing selection)
                      // Original region:  AAAAAAAAAA
                      // New region:             BBBBBBBBBB
                      // A or B:           CCCCCCCCCCCCCCCC
  AND          ( 2),  // Binary "and" operation for hyperslabs
                      // (only leave overlapped regions in selection)
                      // Original region:  AAAAAAAAAA
                      // New region:             BBBBBBBBBB
                      // A and B:                CCCC
  XOR          ( 3),  // Binary "xor" operation for hyperslabs
                      // (only leave non-overlapped regions in selection)
                      // Original region:  AAAAAAAAAA
                      // New region:             BBBBBBBBBB
                      // A xor B:          CCCCCC    CCCCCC
  NOTB         ( 4),  // Binary "not" operation for hyperslabs
                      // (only leave non-overlapped regions in original selection)
                      // Original region:  AAAAAAAAAA
                      // New region:             BBBBBBBBBB
                      // A not B:          CCCCCC
  NOTA         ( 5),  // Binary "not" operation for hyperslabs
                      // (only leave non-overlapped regions in new selection)
                      // Original region:  AAAAAAAAAA
                      // New region:             BBBBBBBBBB
                      // B not A:                    CCCCCC
  APPEND       ( 6),  // Append elements to end of point selection
  PREPEND      ( 7),  // Prepend elements to beginning of point selection
  INVALID      ( 8);  // Invalid upper bound on selection operations
  private static final Map<Integer, H5S_SELECT_OPER> lookup = new HashMap<Integer, H5S_SELECT_OPER>();

  static {
    for (H5S_SELECT_OPER s : EnumSet.allOf(H5S_SELECT_OPER.class))
      lookup.put(s.getCode(), s);
  }

  private int code;

  H5S_SELECT_OPER(int type) {
    this.code = type;
  }

  public int getCode() {
    return this.code;
  }

  public static H5S_SELECT_OPER get(int code) {
    return lookup.get(code);
  }

}
