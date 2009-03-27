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

public enum H5Zenum {
//Return values for filter callback function
// H5Z_cb_return_t 
  H5Z_CB_ERROR,
  H5Z_CB_FAIL,            // I/O should fail if filter fails.
  H5Z_CB_CONT,            // I/O continues if filter fails.
  H5Z_CB_NO,

//Values to decide if EDC is enabled for reading data
// H5Z_EDC_t
  H5Z_ERROR_EDC ,         // error value
  H5Z_DISABLE_EDC,
  H5Z_ENABLE_EDC,
  H5Z_NO_EDC,             // must be the last

//Filter IDs
// H5Z_filter_t 
  H5Z_FILTER_ERROR,       //no filter
  H5Z_FILTER_NONE,        //reserved indefinitely
  H5Z_FILTER_DEFLATE,     //deflation like gzip
  H5Z_FILTER_SHUFFLE,     //shuffle the data
  H5Z_FILTER_FLETCHER32,  //fletcher32 checksum of EDC
  H5Z_FILTER_SZIP,        //szip compression
  H5Z_FILTER_NBIT,        //nbit compression
  H5Z_FILTER_SCALEOFFSET, //scale+offset compression
  H5Z_FILTER_RESERVED,    //filter ids below this value are reserved for library use
  H5Z_FILTER_MAX,         //maximum filter id

//Special parameters for ScaleOffset filter
// H5Z_SO_scale_type_t 
  H5Z_SO_FLOAT_DSCALE,
  H5Z_SO_FLOAT_ESCALE,
  H5Z_SO_INT;
}
