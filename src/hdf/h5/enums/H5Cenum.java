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

public enum H5Cenum {
// H5C_cache_decr_mode 
  H5C_decr__off,
  H5C_decr__threshold,
  H5C_decr__age_out,
  H5C_decr__age_out_with_threshold,

// H5C_cache_flash_incr_mode 
  H5C_flash_incr__off,
  H5C_flash_incr__add_space,

// H5C_cache_incr_mode 
  H5C_incr__off,
  H5C_incr__threshold;
}
