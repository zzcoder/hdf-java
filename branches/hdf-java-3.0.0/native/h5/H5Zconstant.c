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
#include "H5Zconstant.h"
#include <jni.h>

/*
 * Class:     hdf_h5_constants_H5constant
 * Method:    J2C
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_hdf_h5_constants_H5Zconstant_J2C
  (JNIEnv *env, jclass cls, jint java_constant)
{
    switch (java_constant)
    {
    case hdf_h5_constants_H5Zconstant_JH5Z_CB_CONT : return H5Z_CB_CONT ;
    case hdf_h5_constants_H5Zconstant_JH5Z_CB_ERROR : return H5Z_CB_ERROR ;
    case hdf_h5_constants_H5Zconstant_JH5Z_CB_FAIL : return H5Z_CB_FAIL ;
    case hdf_h5_constants_H5Zconstant_JH5Z_CB_NO : return H5Z_CB_NO ;
    case hdf_h5_constants_H5Zconstant_JH5Z_DISABLE_EDC : return H5Z_DISABLE_EDC ;
    case hdf_h5_constants_H5Zconstant_JH5Z_ENABLE_EDC : return H5Z_ENABLE_EDC ;
    case hdf_h5_constants_H5Zconstant_JH5Z_ERROR_EDC : return H5Z_ERROR_EDC ;
    case hdf_h5_constants_H5Zconstant_JH5Z_FILTER_DEFLATE : return H5Z_FILTER_DEFLATE ;
    case hdf_h5_constants_H5Zconstant_JH5Z_FILTER_ERROR : return H5Z_FILTER_ERROR ;
    case hdf_h5_constants_H5Zconstant_JH5Z_FILTER_FLETCHER32 : return H5Z_FILTER_FLETCHER32 ;
    case hdf_h5_constants_H5Zconstant_JH5Z_FILTER_MAX : return H5Z_FILTER_MAX ;
    case hdf_h5_constants_H5Zconstant_JH5Z_FILTER_NONE : return H5Z_FILTER_NONE ;
    case hdf_h5_constants_H5Zconstant_JH5Z_FILTER_RESERVED : return H5Z_FILTER_RESERVED ;
    case hdf_h5_constants_H5Zconstant_JH5Z_FILTER_SHUFFLE : return H5Z_FILTER_SHUFFLE ;
    case hdf_h5_constants_H5Zconstant_JH5Z_FILTER_SZIP : return H5Z_FILTER_SZIP ;
    case hdf_h5_constants_H5Zconstant_JH5Z_FLAG_DEFMASK : return H5Z_FLAG_DEFMASK ;
    case hdf_h5_constants_H5Zconstant_JH5Z_FLAG_INVMASK : return H5Z_FLAG_INVMASK ;
    case hdf_h5_constants_H5Zconstant_JH5Z_FLAG_MANDATORY : return H5Z_FLAG_MANDATORY ;
    case hdf_h5_constants_H5Zconstant_JH5Z_FLAG_OPTIONAL : return H5Z_FLAG_OPTIONAL ;
    case hdf_h5_constants_H5Zconstant_JH5Z_FLAG_REVERSE : return H5Z_FLAG_REVERSE ;
    case hdf_h5_constants_H5Zconstant_JH5Z_FLAG_SKIP_EDC : return H5Z_FLAG_SKIP_EDC ;
    case hdf_h5_constants_H5Zconstant_JH5Z_MAX_NFILTERS : return H5Z_MAX_NFILTERS ;
    case hdf_h5_constants_H5Zconstant_JH5Z_NO_EDC : return H5Z_NO_EDC ;
    case hdf_h5_constants_H5Zconstant_JH5Z_FILTER_CONFIG_ENCODE_ENABLED : return H5Z_FILTER_CONFIG_ENCODE_ENABLED ;
    case hdf_h5_constants_H5Zconstant_JH5Z_FILTER_CONFIG_DECODE_ENABLED : return H5Z_FILTER_CONFIG_DECODE_ENABLED ;
    case hdf_h5_constants_H5Zconstant_JH5Z_SO_INT_MINBITS_DEFAULT : return H5Z_SO_INT_MINBITS_DEFAULT ;
    case hdf_h5_constants_H5Zconstant_JH5_SZIP_ALLOW_K13_OPTION_MASK  : return H5_SZIP_ALLOW_K13_OPTION_MASK  ;
    case hdf_h5_constants_H5Zconstant_JH5_SZIP_CHIP_OPTION_MASK: return H5_SZIP_CHIP_OPTION_MASK       ;
    case hdf_h5_constants_H5Zconstant_JH5_SZIP_EC_OPTION_MASK: return H5_SZIP_EC_OPTION_MASK         ;
    case hdf_h5_constants_H5Zconstant_JH5_SZIP_NN_OPTION_MASK : return H5_SZIP_NN_OPTION_MASK         ;
    case hdf_h5_constants_H5Zconstant_JH5_SZIP_MAX_PIXELS_PER_BLOCK : return H5_SZIP_MAX_PIXELS_PER_BLOCK   ;
    case hdf_h5_constants_H5Zconstant_JH5Z_SHUFFLE_USER_NPARMS: return H5Z_SHUFFLE_USER_NPARMS   ;
    case hdf_h5_constants_H5Zconstant_JH5Z_SHUFFLE_TOTAL_NPARMS  : return H5Z_SHUFFLE_TOTAL_NPARMS  ;
    case hdf_h5_constants_H5Zconstant_JH5Z_SZIP_USER_NPARMS   : return H5Z_SZIP_USER_NPARMS   ;
    case hdf_h5_constants_H5Zconstant_JH5Z_SZIP_TOTAL_NPARMS  : return H5Z_SZIP_TOTAL_NPARMS  ;
    case hdf_h5_constants_H5Zconstant_JH5Z_SZIP_PARM_MASK     : return H5Z_SZIP_PARM_MASK     ;
    case hdf_h5_constants_H5Zconstant_JH5Z_SZIP_PARM_PPB      : return H5Z_SZIP_PARM_PPB      ;
    case hdf_h5_constants_H5Zconstant_JH5Z_SZIP_PARM_BPP      : return H5Z_SZIP_PARM_BPP      ;
    case hdf_h5_constants_H5Zconstant_JH5Z_SZIP_PARM_PPS      : return H5Z_SZIP_PARM_PPS      ;
    case hdf_h5_constants_H5Zconstant_JH5Z_NBIT_USER_NPARMS   : return H5Z_NBIT_USER_NPARMS   ;
    case hdf_h5_constants_H5Zconstant_JH5Z_SCALEOFFSET_USER_NPARMS : return H5Z_SCALEOFFSET_USER_NPARMS ;
    case hdf_h5_constants_H5Zconstant_JH5Z_FILTER_ALL   : return H5Z_FILTER_ALL   ;
    }

    return -1;
}

#ifdef __cplusplus
}
#endif
