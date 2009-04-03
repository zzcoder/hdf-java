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
#include "H5Sconstant.h"
#include <jni.h>

/*
 * Class:     hdf_h5_constants_H5constant
 * Method:    J2C
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_hdf_h5_constants_H5Sconstant_J2C
  (JNIEnv *env, jclass cls, jint java_constant)
{
    switch (java_constant)
    {
    case hdf_h5_constants_H5Sconstant_JH5S_ALL : return H5S_ALL ;
    /*case hdf_h5_constants_H5Sconstant_JH5S_COMPLEX : return H5S_COMPLEX ;*/
    case hdf_h5_constants_H5Sconstant_JH5S_MAX_RANK : return H5S_MAX_RANK ;
    case hdf_h5_constants_H5Sconstant_JH5S_NO_CLASS : return H5S_NO_CLASS ;
    case hdf_h5_constants_H5Sconstant_JH5S_SCALAR : return H5S_SCALAR ;
    case hdf_h5_constants_H5Sconstant_JH5S_SEL_ALL : return H5S_SEL_ALL ;
    case hdf_h5_constants_H5Sconstant_JH5S_SEL_ERROR : return H5S_SEL_ERROR ;
    case hdf_h5_constants_H5Sconstant_JH5S_SEL_HYPERSLABS : return H5S_SEL_HYPERSLABS ;
    case hdf_h5_constants_H5Sconstant_JH5S_SEL_N : return H5S_SEL_N ;
    case hdf_h5_constants_H5Sconstant_JH5S_SEL_NONE : return H5S_SEL_NONE ;
    case hdf_h5_constants_H5Sconstant_JH5S_SEL_POINTS : return H5S_SEL_POINTS ;
    case hdf_h5_constants_H5Sconstant_JH5S_SELECT_AND : return H5S_SELECT_AND ;
    case hdf_h5_constants_H5Sconstant_JH5S_SELECT_APPEND : return H5S_SELECT_APPEND ;
    case hdf_h5_constants_H5Sconstant_JH5S_SELECT_INVALID : return H5S_SELECT_INVALID ;
    case hdf_h5_constants_H5Sconstant_JH5S_SELECT_NOOP : return H5S_SELECT_NOOP ;
    case hdf_h5_constants_H5Sconstant_JH5S_SELECT_NOTA : return H5S_SELECT_NOTA ;
    case hdf_h5_constants_H5Sconstant_JH5S_SELECT_NOTB : return H5S_SELECT_NOTB ;
    case hdf_h5_constants_H5Sconstant_JH5S_SELECT_OR : return H5S_SELECT_OR ;
    case hdf_h5_constants_H5Sconstant_JH5S_SELECT_PREPEND : return H5S_SELECT_PREPEND ;
    case hdf_h5_constants_H5Sconstant_JH5S_SELECT_SET : return H5S_SELECT_SET ;
    case hdf_h5_constants_H5Sconstant_JH5S_SELECT_XOR : return H5S_SELECT_XOR ;
    case hdf_h5_constants_H5Sconstant_JH5S_SIMPLE : return H5S_SIMPLE ;
    case hdf_h5_constants_H5Sconstant_JH5S_UNLIMITED : return H5S_UNLIMITED ;
    }

    return -1;
}

#ifdef __cplusplus
}
#endif
