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
#include "H5Fconstant.h"
#include <jni.h>

/*
 * Class:     hdf_h5_constants_H5constant
 * Method:    J2C
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_hdf_h5_constants_H5Fconstant_J2C
  (JNIEnv *env, jclass cls, jint java_constant)
{
    switch (java_constant)
    {
    case hdf_h5_constants_H5Fconstant_JH5F_ACC_CREAT : return H5F_ACC_CREAT;
    case hdf_h5_constants_H5Fconstant_JH5F_ACC_DEBUG : return H5F_ACC_DEBUG;
    case hdf_h5_constants_H5Fconstant_JH5F_ACC_EXCL : return H5F_ACC_EXCL;
    case hdf_h5_constants_H5Fconstant_JH5F_ACC_RDONLY : return H5F_ACC_RDONLY;
    case hdf_h5_constants_H5Fconstant_JH5F_ACC_RDWR : return H5F_ACC_RDWR;
    case hdf_h5_constants_H5Fconstant_JH5F_ACC_TRUNC : return H5F_ACC_TRUNC;
    /*case hdf_h5_constants_H5Fconstant_JH5F_ACC_DEFAULT : return H5F_ACC_DEFAULT;*/
    case hdf_h5_constants_H5Fconstant_JH5F_CLOSE_DEFAULT : return H5F_CLOSE_DEFAULT;
    case hdf_h5_constants_H5Fconstant_JH5F_CLOSE_SEMI : return H5F_CLOSE_SEMI;
    case hdf_h5_constants_H5Fconstant_JH5F_CLOSE_STRONG : return H5F_CLOSE_STRONG;
    case hdf_h5_constants_H5Fconstant_JH5F_CLOSE_WEAK : return H5F_CLOSE_WEAK;
    case hdf_h5_constants_H5Fconstant_JH5F_OBJ_ALL : return H5F_OBJ_ALL;
    case hdf_h5_constants_H5Fconstant_JH5F_OBJ_ATTR : return H5F_OBJ_ATTR;
    case hdf_h5_constants_H5Fconstant_JH5F_OBJ_DATASET : return H5F_OBJ_DATASET;
    case hdf_h5_constants_H5Fconstant_JH5F_OBJ_DATATYPE : return H5F_OBJ_DATATYPE;
    case hdf_h5_constants_H5Fconstant_JH5F_OBJ_FILE : return H5F_OBJ_FILE;
    case hdf_h5_constants_H5Fconstant_JH5F_OBJ_GROUP : return H5F_OBJ_GROUP;
    case hdf_h5_constants_H5Fconstant_JH5F_OBJ_LOCAL : return H5F_OBJ_LOCAL;
    case hdf_h5_constants_H5Fconstant_JH5F_SCOPE_DOWN : return H5F_SCOPE_DOWN;
    case hdf_h5_constants_H5Fconstant_JH5F_SCOPE_GLOBAL : return H5F_SCOPE_GLOBAL;
    case hdf_h5_constants_H5Fconstant_JH5F_SCOPE_LOCAL : return H5F_SCOPE_LOCAL;
    case hdf_h5_constants_H5Fconstant_JH5F_UNLIMITED : return H5F_UNLIMITED;
	}

    return -1;
}

#ifdef __cplusplus
}
#endif
