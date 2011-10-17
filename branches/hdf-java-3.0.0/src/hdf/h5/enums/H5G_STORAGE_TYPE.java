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

// Types of link storage for groups.
public enum H5G_STORAGE_TYPE {
    UNKNOWN     (-1),	// Unknown link storage type
    SYMBOL_TABLE (0), // Links in group are stored with a "symbol table"
    // (this is sometimes called "old-style" groups
    COMPACT      (1),	// Links are stored in object header
    DENSE        (2); // Links are stored in fractal heap & indexed with v2 B-tree
    private static final Map<Integer, H5G_STORAGE_TYPE> lookup = new HashMap<Integer, H5G_STORAGE_TYPE>();

    static {
        for (H5G_STORAGE_TYPE s : EnumSet.allOf(H5G_STORAGE_TYPE.class))
            lookup.put(s.getCode(), s);
    }

    private int code;

    H5G_STORAGE_TYPE(int type) {
        this.code = type;
    }

    public int getCode() {
        return this.code;
    }

    public static H5G_STORAGE_TYPE get(int code) {
        return lookup.get(code);
    }
}