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

package hdf.h5.structs;

import hdf.h5.enums.H5G_STORAGE_TYPE;

//Information struct for group (for H5Gget_info/H5Gget_info_by_name/H5Gget_info_by_idx)
public class H5G_info_t {
  public H5G_STORAGE_TYPE  storage_type; // Type of storage for links in group
  public long     nlinks;       // Number of links in group
  public long     max_corder;   // Current max. creation order value for group
  public int      mounted;      // Whether group has a file mounted on it
}