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

public enum H5enum {
// H5_index_t 
  H5_INDEX_UNKNOWN,     // Unknown index type     
  H5_INDEX_NAME,        // Index on names   
  H5_INDEX_CRT_ORDER,   // Index on creation order 
  H5_INDEX_N,           // Number of indices defined 

// H5_iter_order_t 
  H5_ITER_UNKNOWN,      // Unknown order
  H5_ITER_INC,          // Increasing order
  H5_ITER_DEC,          // Decreasing order
  H5_ITER_NATIVE,       // No particular order, whatever is fastest
  H5_ITER_N;            // Number of iteration orders
}
