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

public enum H5Ienum {
//Library type values.
// H5I_type_t
  H5I_UNINIT,             //uninitialized type/
  H5I_BADID,	            //invalid Type
  H5I_FILE,	              //type ID for File objects	
  H5I_GROUP,		          //type ID for Group objects
  H5I_DATATYPE,	          //type ID for Datatype objects	
  H5I_DATASPACE,	        //type ID for Dataspace objects	
  H5I_DATASET,	          //type ID for Dataset objects	
  H5I_ATTR,		            //type ID for Attribute objects	
  H5I_REFERENCE,	        //type ID for Reference objects	
  H5I_VFL,			          //type ID for virtual file layer
  H5I_GENPROP_CLS,        //type ID for generic property list classes
  H5I_GENPROP_LST,        //type ID for generic property lists 
  H5I_ERROR_CLASS,        //type ID for error classes	
  H5I_ERROR_MSG,          //type ID for error messages	
  H5I_ERROR_STACK,        //type ID for error stacks	
  H5I_NTYPES;		          //number of library types, MUST BE LAST! 
}
