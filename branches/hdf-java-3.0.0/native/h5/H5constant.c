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
#include "H5constant.h"
#include <jni.h>

/*
 * Class:     hdf_h5_constants_H5constant
 * Method:    J2C
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_hdf_h5_constants_H5constant_J2C
  (JNIEnv *env, jclass cls, jint java_constant)
{
    switch (java_constant)
    {
    case hdf_h5_constants_H5constant_JH5_SZIP_MAX_PIXELS_PER_BLOCK : return H5_SZIP_MAX_PIXELS_PER_BLOCK;
    case hdf_h5_constants_H5constant_JH5_SZIP_NN_OPTION_MASK : return H5_SZIP_NN_OPTION_MASK;
    case hdf_h5_constants_H5constant_JH5_SZIP_EC_OPTION_MASK : return H5_SZIP_EC_OPTION_MASK;
    case hdf_h5_constants_H5constant_JH5_SZIP_ALLOW_K13_OPTION_MASK : return H5_SZIP_ALLOW_K13_OPTION_MASK;
    case hdf_h5_constants_H5constant_JH5_SZIP_CHIP_OPTION_MASK : return H5_SZIP_CHIP_OPTION_MASK;
    case hdf_h5_constants_H5constant_JH5_ITER_ERROR : return H5_ITER_ERROR  ;
    case hdf_h5_constants_H5constant_JH5_ITER_CONT  : return H5_ITER_CONT   ;
    case hdf_h5_constants_H5constant_JH5_ITER_STOP  : return H5_ITER_STOP   ;
    }

    return -1;
}

#ifdef __cplusplus
}
#endif
