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
#include "H5Pconstant.h"
#include <jni.h>

/*
 * Class:     hdf_h5_constants_H5constant
 * Method:    J2C
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_hdf_h5_constants_H5Pconstant_J2C
  (JNIEnv *env, jclass cls, jint java_constant)
{
    switch (java_constant)
    {
    case hdf_h5_constants_H5Pconstant_JH5P_DATASET_CREATE : return H5P_DATASET_CREATE ;
    case hdf_h5_constants_H5Pconstant_JH5P_DATASET_CREATE_DEFAULT : return H5P_DATASET_CREATE_DEFAULT ;
    case hdf_h5_constants_H5Pconstant_JH5P_DATASET_XFER : return H5P_DATASET_XFER ;
    case hdf_h5_constants_H5Pconstant_JH5P_DATASET_XFER_DEFAULT : return H5P_DATASET_XFER_DEFAULT ;
    case hdf_h5_constants_H5Pconstant_JH5P_DEFAULT : return H5P_DEFAULT ;
    case hdf_h5_constants_H5Pconstant_JH5P_FILE_ACCESS : return H5P_FILE_ACCESS ;
    case hdf_h5_constants_H5Pconstant_JH5P_FILE_ACCESS_DEFAULT : return H5P_FILE_ACCESS_DEFAULT ;
    case hdf_h5_constants_H5Pconstant_JH5P_FILE_CREATE : return H5P_FILE_CREATE ;
    case hdf_h5_constants_H5Pconstant_JH5P_FILE_CREATE_DEFAULT : return H5P_FILE_CREATE_DEFAULT ;
    /*case hdf_h5_constants_H5Pconstant_JH5P_MOUNT : return H5P_MOUNT ;
    case hdf_h5_constants_H5Pconstant_JH5P_MOUNT_DEFAULT : return H5P_MOUNT_DEFAULT ;*/
    case hdf_h5_constants_H5Pconstant_JH5P_NO_CLASS : return H5P_NO_CLASS ;
    /*case hdf_h5_constants_H5Pconstant_JH5P_NO_CLASS_DEFAULT : return H5P_NO_CLASS_DEFAULT ;*/
    }

    return -1;
}

#ifdef __cplusplus
}
#endif
