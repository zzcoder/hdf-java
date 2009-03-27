/*****************************************************************************
 * Copyright by The HDF Group.                                               *
 * Copyright by the Board of Trustees of the University of Illinois.         *
 * All rights reserved.                                                      *
 *                                                                           *
 * This file is part of the HDF Java Products distribution.                  *
 * The full copyright notice, including terms governing use, modification,   *
 * and redistribution, is contained in the files COPYING and Copyright.html. *
 * COPYING can be found at the root of the source code distribution tree.    *
 * Or, see http://hdfgroup.org/products/hdf-java/doc/Copyright.html.         *
 * If you do not have access to either file, you may request a copy from     *
 * help@hdfgroup.org.                                                        *
 ****************************************************************************/

package hdf.h5.enums;

public enum H5Senum {
//Different types of dataspaces
// H5S_class_t 
  H5S_NO_CLASS,           // error
  H5S_SCALAR,             // scalar variable
  H5S_SIMPLE,             // simple data space
  H5S_NULL,               // null data space

// Enumerated type for the type of selection
// H5S_sel_type 
  H5S_SEL_ERROR,          // Error
  H5S_SEL_NONE,           // Nothing selected
  H5S_SEL_POINTS,         // Sequence of points selected
  H5S_SEL_HYPERSLABS,     // "New-style" hyperslab selection defined 
  H5S_SEL_ALL,            // Entire extent selected
  H5S_SEL_N,              // THIS MUST BE LAST

//Different ways of combining selections
// H5S_seloper_t 
  H5S_SELECT_NOOP,        // error
  H5S_SELECT_SET,         // Select "set" operation
  H5S_SELECT_OR,          // Binary "or" operation for hyperslabs
                          // (add new selection to existing selection)
                          // Original region:  AAAAAAAAAA
                          // New region:             BBBBBBBBBB
                          // A or B:           CCCCCCCCCCCCCCCC
  H5S_SELECT_AND,         // Binary "and" operation for hyperslabs
                          // (only leave overlapped regions in selection)
                          // Original region:  AAAAAAAAAA
                          // New region:             BBBBBBBBBB
                          // A and B:                CCCC
  H5S_SELECT_XOR,         // Binary "xor" operation for hyperslabs
                          // (only leave non-overlapped regions in selection)
                          // Original region:  AAAAAAAAAA
                          // New region:             BBBBBBBBBB
                          // A xor B:          CCCCCC    CCCCCC
  H5S_SELECT_NOTB,        // Binary "not" operation for hyperslabs
                          // (only leave non-overlapped regions in original selection)
                          // Original region:  AAAAAAAAAA
                          // New region:             BBBBBBBBBB
                          // A not B:          CCCCCC
  H5S_SELECT_NOTA,        // Binary "not" operation for hyperslabs
                          // (only leave non-overlapped regions in new selection)
                          // Original region:  AAAAAAAAAA
                          // New region:             BBBBBBBBBB
                          // B not A:                    CCCCCC
  H5S_SELECT_APPEND,      // Append elements to end of point selection
  H5S_SELECT_PREPEND,     // Prepend elements to beginning of point selection
  H5S_SELECT_INVALID;     // Invalid upper bound on selection operations
}
