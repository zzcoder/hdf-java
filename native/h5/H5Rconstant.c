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
#include "H5Rconstant.h"
#include <jni.h>

/*
 * Class:     hdf_h5_constants_H5constant
 * Method:    J2C
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_hdf_h5_constants_H5Rconstant_J2C
  (JNIEnv *env, jclass cls, jint java_constant)
{
    switch (java_constant)
    {
    case hdf_h5_constants_H5Rconstant_JH5R_BADTYPE : return H5R_BADTYPE;
    case hdf_h5_constants_H5Rconstant_JH5R_DATASET_REGION : return H5R_DATASET_REGION;
    /*case hdf_h5_constants_H5Rconstant_JH5R_INTERNA : return H5R_INTERNA;*/
    case hdf_h5_constants_H5Rconstant_JH5R_MAXTYPE : return H5R_MAXTYPE;
    case hdf_h5_constants_H5Rconstant_JH5R_OBJ_REF_BUF_SIZE : return H5R_OBJ_REF_BUF_SIZE;
    case hdf_h5_constants_H5Rconstant_JH5R_OBJECT : return H5R_OBJECT;
    }

    return -1;
}

#ifdef __cplusplus
}
#endif
