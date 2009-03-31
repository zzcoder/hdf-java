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

// Different types of dataspaces
public enum H5S_CLASS {
  NO_CLASS         (-1),  // error
  SCALAR           ( 0),  // scalar variable
  SIMPLE           ( 1),  // simple data space
  NULL             ( 2);  // null data space
  private static final Map<Integer, H5S_CLASS> lookup = new HashMap<Integer, H5S_CLASS>();

  static {
    for (H5S_CLASS s : EnumSet.allOf(H5S_CLASS.class))
      lookup.put(s.getCode(), s);
  }

  private int code;

  H5S_CLASS(int type) {
    this.code = type;
  }

  public int getCode() {
    return this.code;
  }

  public static H5S_CLASS get(int code) {
    return lookup.get(code);
  }

}
