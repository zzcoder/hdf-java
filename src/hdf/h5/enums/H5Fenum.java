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

public enum H5Fenum {
// H5F_close_degree_t 
  H5F_CLOSE_DEFAULT,      // Use the degree pre-defined by underlining VFL
  H5F_CLOSE_WEAK,         // file closes only after all opened objects are closed
  H5F_CLOSE_SEMI,         // if no opened objects, file is close; otherwise, file close fails
  H5F_CLOSE_STRONG,       // if there are opened objects, close them first, then close file

//Library's file format versions
// H5F_libver_t 
  H5F_LIBVER_EARLIEST,    // Use the earliest possible format for storing objects
  H5F_LIBVER_LATEST,      // Use the latest possible format available for storing objects

//The difference between a single file and a set of mounted files
// H5F_scope_t {
  H5F_SCOPE_LOCAL,        //specified file handle only  
  H5F_SCOPE_GLOBAL,       //entire virtual file   
  H5F_SCOPE_DOWN;         //for internal use only   
}
