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

#ifdef __cplusplus
extern "C" {
#endif

#include "hdf5.h"
#include "H5Iconstant.h"
#include <jni.h>

/*
 * Class:     hdf_h5_constants_H5constant
 * Method:    J2C
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_hdf_h5_constants_H5Iconstant_J2C
  (JNIEnv *env, jclass cls, jint java_constant)
{
    switch (java_constant)
    {
    case hdf_h5_constants_H5Iconstant_JH5I_ATTR : return H5I_ATTR ;
    case hdf_h5_constants_H5Iconstant_JH5I_BADID : return H5I_BADID ;
    case hdf_h5_constants_H5Iconstant_JH5I_DATASET : return H5I_DATASET ;
    case hdf_h5_constants_H5Iconstant_JH5I_DATASPACE : return H5I_DATASPACE ;
    case hdf_h5_constants_H5Iconstant_JH5I_DATATYPE : return H5I_DATATYPE ;
    case hdf_h5_constants_H5Iconstant_JH5I_FILE : return H5I_FILE ;
    /*case hdf_h5_constants_H5Iconstant_JH5I_FILE_CLOSING : return H5I_FILE_CLOSING ;*/
    case hdf_h5_constants_H5Iconstant_JH5I_GENPROP_CLS : return H5I_GENPROP_CLS ;
    case hdf_h5_constants_H5Iconstant_JH5I_GENPROP_LST : return H5I_GENPROP_LST ;
    case hdf_h5_constants_H5Iconstant_JH5I_GROUP : return H5I_GROUP ;
    case hdf_h5_constants_H5Iconstant_JH5I_INVALID_HID : return H5I_INVALID_HID ;
    /*case hdf_h5_constants_H5Iconstant_JH5I_NGROUPS : return H5I_NGROUPS ;*/
    case hdf_h5_constants_H5Iconstant_JH5I_REFERENCE : return H5I_REFERENCE ;
    /*case hdf_h5_constants_H5Iconstant_JH5I_TEMPBUF : return H5I_TEMPBUF ;*/
    case hdf_h5_constants_H5Iconstant_JH5I_VFL : return H5I_VFL ;
    }

    return -1;
}

#ifdef __cplusplus
}
#endif
