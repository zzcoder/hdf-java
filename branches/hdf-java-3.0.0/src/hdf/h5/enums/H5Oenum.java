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


public enum H5Oenum {
//Types of objects in file
// H5O_type_t
  H5O_TYPE_UNKNOWN,       // Unknown object type
  H5O_TYPE_GROUP,         // Object is a group
  H5O_TYPE_DATASET,       // Object is a dataset
  H5O_TYPE_NAMED_DATATYPE,// Object is a named data type 
  H5O_TYPE_NTYPES;        // Number of different object types (must be last!)
}
