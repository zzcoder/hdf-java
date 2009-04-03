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
#include "H5Gconstant.h"
#include <jni.h>

/*
 * Class:     hdf_h5_constants_H5constant
 * Method:    J2C
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_hdf_h5_constants_H5Gconstant_J2C
  (JNIEnv *env, jclass cls, jint java_constant)
{
    switch (java_constant)
    {
    case hdf_h5_constants_H5Gconstant_JH5G_DATASET : return H5G_DATASET ;
    case hdf_h5_constants_H5Gconstant_JH5G_GROUP : return H5G_GROUP ;
    case hdf_h5_constants_H5Gconstant_JH5G_LINK : return H5G_LINK ;
    case hdf_h5_constants_H5Gconstant_JH5G_LINK_ERROR : return H5G_LINK_ERROR ;
    case hdf_h5_constants_H5Gconstant_JH5G_LINK_HARD : return H5G_LINK_HARD ;
    case hdf_h5_constants_H5Gconstant_JH5G_LINK_SOFT : return H5G_LINK_SOFT ;
    case hdf_h5_constants_H5Gconstant_JH5G_NLIBTYPES : return H5G_NLIBTYPES ;
    case hdf_h5_constants_H5Gconstant_JH5G_NTYPES : return H5G_NTYPES ;
    case hdf_h5_constants_H5Gconstant_JH5G_NUSERTYPES : return H5G_NUSERTYPES ;
    /*case hdf_h5_constants_H5Gconstant_JH5G_RESERVED_4 : return H5G_RESERVED_4 ;*/
    case hdf_h5_constants_H5Gconstant_JH5G_RESERVED_5 : return H5G_RESERVED_5 ;
    case hdf_h5_constants_H5Gconstant_JH5G_RESERVED_6 : return H5G_RESERVED_6 ;
    case hdf_h5_constants_H5Gconstant_JH5G_RESERVED_7 : return H5G_RESERVED_7 ;
    case hdf_h5_constants_H5Gconstant_JH5G_SAME_LOC : return H5G_SAME_LOC ;
    case hdf_h5_constants_H5Gconstant_JH5G_TYPE : return H5G_TYPE ;
    case hdf_h5_constants_H5Gconstant_JH5G_UNKNOWN : return H5G_UNKNOWN ;
    /*case hdf_h5_constants_H5Gconstant_JH5G_USERTYPE : return H5G_USERTYPE ;*/
    }

    return -1;
}

#ifdef __cplusplus
}
#endif
