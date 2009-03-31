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

// Enumerated type for the type of selection
public enum H5S_SEL {
  ERROR       (-1),  // Error
  NONE        ( 0),  // Nothing selected
  POINTS      ( 1),  // Sequence of points selected
  HYPERSLABS  ( 2),  // "New-style" hyperslab selection defined 
  ALL         ( 3),  // Entire extent selected
  N           ( 4);  // THIS MUST BE LAST
  private static final Map<Integer, H5S_SEL> lookup = new HashMap<Integer, H5S_SEL>();

  static {
    for (H5S_SEL s : EnumSet.allOf(H5S_SEL.class))
      lookup.put(s.getCode(), s);
  }

  private int code;

  H5S_SEL(int type) {
    this.code = type;
  }

  public int getCode() {
    return this.code;
  }

  public static H5S_SEL get(int code) {
    return lookup.get(code);
  }

}
