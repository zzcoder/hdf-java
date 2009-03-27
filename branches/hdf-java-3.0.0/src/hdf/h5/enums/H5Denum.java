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

public enum H5Denum {
// H5D_alloc_time_t
  H5D_ALLOC_TIME_ERROR,
  H5D_ALLOC_TIME_DEFAULT,
  H5D_ALLOC_TIME_EARLY,
  H5D_ALLOC_TIME_LATE,
  H5D_ALLOC_TIME_INCR,

// H5D_chunk_index_t 
  H5D_CHUNK_BTREE, // v1 B-tree index

//Values for time of writing fill value property
// H5D_fill_time_t 
  H5D_FILL_TIME_ERROR,
  H5D_FILL_TIME_ALLOC,
  H5D_FILL_TIME_NEVER,
  H5D_FILL_TIME_IFSET,

//Values for fill value status
// H5D_fill_value_t 
  H5D_FILL_VALUE_ERROR,
  H5D_FILL_VALUE_UNDEFINED,
  H5D_FILL_VALUE_DEFAULT,
  H5D_FILL_VALUE_USER_DEFINED,

// H5D_layout_t 
  H5D_LAYOUT_ERROR,
  H5D_COMPACT,            //raw data is very small
  H5D_CONTIGUOUS,         //the default 
  H5D_CHUNKED,            //slow and fancy
  H5D_NLAYOUTS,           //this one must be last LAYOUT!

//Values for the status of space allocation
// H5D_space_status_t 
  H5D_SPACE_STATUS_ERROR,
  H5D_SPACE_STATUS_NOT_ALLOCATED,
  H5D_SPACE_STATUS_PART_ALLOCATED,
  H5D_SPACE_STATUS_ALLOCATED;
}
