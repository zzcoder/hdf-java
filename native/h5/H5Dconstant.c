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
#include "H5Dconstant.h"
#include <jni.h>

/*
 * Class:     hdf_h5_constants_H5constant
 * Method:    J2C
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_hdf_h5_constants_H5Dconstant_J2C
  (JNIEnv *env, jclass cls, jint java_constant)
{
    switch (java_constant)
    {
    case hdf_h5_constants_H5Dconstant_JH5D_ALLOC_TIME_DEFAULT : return H5D_ALLOC_TIME_DEFAULT;
    case hdf_h5_constants_H5Dconstant_JH5D_ALLOC_TIME_EARLY : return H5D_ALLOC_TIME_EARLY;
    case hdf_h5_constants_H5Dconstant_JH5D_ALLOC_TIME_ERROR : return H5D_ALLOC_TIME_ERROR;
    case hdf_h5_constants_H5Dconstant_JH5D_ALLOC_TIME_INCR : return H5D_ALLOC_TIME_INCR;
    case hdf_h5_constants_H5Dconstant_JH5D_ALLOC_TIME_LATE : return H5D_ALLOC_TIME_LATE;
    case hdf_h5_constants_H5Dconstant_JH5D_CHUNKED : return H5D_CHUNKED;
    case hdf_h5_constants_H5Dconstant_JH5D_COMPACT : return H5D_COMPACT;
    case hdf_h5_constants_H5Dconstant_JH5D_CONTIGUOUS : return H5D_CONTIGUOUS;
    case hdf_h5_constants_H5Dconstant_JH5D_FILL_TIME_ALLOC : return H5D_FILL_TIME_ALLOC;
    case hdf_h5_constants_H5Dconstant_JH5D_FILL_TIME_ERROR : return H5D_FILL_TIME_ERROR;
    case hdf_h5_constants_H5Dconstant_JH5D_FILL_TIME_NEVER : return H5D_FILL_TIME_NEVER;
    case hdf_h5_constants_H5Dconstant_JH5D_FILL_VALUE_DEFAULT : return H5D_FILL_VALUE_DEFAULT;
    case hdf_h5_constants_H5Dconstant_JH5D_FILL_VALUE_ERROR : return H5D_FILL_VALUE_ERROR;
    case hdf_h5_constants_H5Dconstant_JH5D_FILL_VALUE_UNDEFINED : return H5D_FILL_VALUE_UNDEFINED;
    case hdf_h5_constants_H5Dconstant_JH5D_FILL_VALUE_USER_DEFINED : return H5D_FILL_VALUE_USER_DEFINED;
    case hdf_h5_constants_H5Dconstant_JH5D_LAYOUT_ERROR : return H5D_LAYOUT_ERROR;
    case hdf_h5_constants_H5Dconstant_JH5D_NLAYOUTS : return H5D_NLAYOUTS;
    case hdf_h5_constants_H5Dconstant_JH5D_SPACE_STATUS_ALLOCATED : return H5D_SPACE_STATUS_ALLOCATED;
    case hdf_h5_constants_H5Dconstant_JH5D_SPACE_STATUS_ERROR : return H5D_SPACE_STATUS_ERROR;
    case hdf_h5_constants_H5Dconstant_JH5D_SPACE_STATUS_NOT_ALLOCATED : return H5D_SPACE_STATUS_NOT_ALLOCATED;
    case hdf_h5_constants_H5Dconstant_JH5D_SPACE_STATUS_PART_ALLOCATED : return H5D_SPACE_STATUS_PART_ALLOCATED;    }

    return -1;
}

#ifdef __cplusplus
}
#endif
