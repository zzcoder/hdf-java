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
#include "H5Tconstant.h"
#include <jni.h>

/*
 * Class:     hdf_h5_constants_H5constant
 * Method:    J2C
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_hdf_h5_constants_H5Tconstant_J2C
  (JNIEnv *env, jclass cls, jint java_constant)
{
    switch (java_constant)
    {
    case hdf_h5_constants_H5Tconstant_JH5T_ALPHA_B16 : return H5T_ALPHA_B16 ;
    case hdf_h5_constants_H5Tconstant_JH5T_ALPHA_B32 : return H5T_ALPHA_B32 ;
    case hdf_h5_constants_H5Tconstant_JH5T_ALPHA_B64 : return H5T_ALPHA_B64 ;
    case hdf_h5_constants_H5Tconstant_JH5T_ALPHA_B8 : return H5T_ALPHA_B8 ;
    case hdf_h5_constants_H5Tconstant_JH5T_ALPHA_F32 : return H5T_ALPHA_F32 ;
    case hdf_h5_constants_H5Tconstant_JH5T_ALPHA_F64 : return H5T_ALPHA_F64 ;
    case hdf_h5_constants_H5Tconstant_JH5T_ALPHA_I16 : return H5T_ALPHA_I16 ;
    case hdf_h5_constants_H5Tconstant_JH5T_ALPHA_I32 : return H5T_ALPHA_I32 ;
    case hdf_h5_constants_H5Tconstant_JH5T_ALPHA_I64 : return H5T_ALPHA_I64 ;
    case hdf_h5_constants_H5Tconstant_JH5T_ALPHA_I8 : return H5T_ALPHA_I8 ;
    case hdf_h5_constants_H5Tconstant_JH5T_ALPHA_U16 : return H5T_ALPHA_U16 ;
    case hdf_h5_constants_H5Tconstant_JH5T_ALPHA_U32 : return H5T_ALPHA_U32 ;
    case hdf_h5_constants_H5Tconstant_JH5T_ALPHA_U64 : return H5T_ALPHA_U64 ;
    case hdf_h5_constants_H5Tconstant_JH5T_ALPHA_U8 : return H5T_ALPHA_U8 ;
    case hdf_h5_constants_H5Tconstant_JH5T_ARRAY : return H5T_ARRAY ;
    case hdf_h5_constants_H5Tconstant_JH5T_BITFIELD : return H5T_BITFIELD ;
    case hdf_h5_constants_H5Tconstant_JH5T_BKG_NO : return H5T_BKG_NO ;
    case hdf_h5_constants_H5Tconstant_JH5T_BKG_YES : return H5T_BKG_YES ;
    case hdf_h5_constants_H5Tconstant_JH5T_C_S1 : return H5T_C_S1 ;
    case hdf_h5_constants_H5Tconstant_JH5T_COMPOUND : return H5T_COMPOUND ;
    case hdf_h5_constants_H5Tconstant_JH5T_CONV_CONV : return H5T_CONV_CONV ;
    case hdf_h5_constants_H5Tconstant_JH5T_CONV_FREE : return H5T_CONV_FREE ;
    case hdf_h5_constants_H5Tconstant_JH5T_CONV_INIT : return H5T_CONV_INIT ;
    case hdf_h5_constants_H5Tconstant_JH5T_CSET_ASCII : return H5T_CSET_ASCII ;
    case hdf_h5_constants_H5Tconstant_JH5T_CSET_ERROR : return H5T_CSET_ERROR ;
    /*case hdf_h5_constants_H5Tconstant_JH5T_CSET_RESERVED_1 : return H5T_CSET_RESERVED_1 ;*/
    case hdf_h5_constants_H5Tconstant_JH5T_CSET_RESERVED_10 : return H5T_CSET_RESERVED_10 ;
    case hdf_h5_constants_H5Tconstant_JH5T_CSET_RESERVED_11 : return H5T_CSET_RESERVED_11 ;
    case hdf_h5_constants_H5Tconstant_JH5T_CSET_RESERVED_12 : return H5T_CSET_RESERVED_12 ;
    case hdf_h5_constants_H5Tconstant_JH5T_CSET_RESERVED_13 : return H5T_CSET_RESERVED_13 ;
    case hdf_h5_constants_H5Tconstant_JH5T_CSET_RESERVED_14 : return H5T_CSET_RESERVED_14 ;
    case hdf_h5_constants_H5Tconstant_JH5T_CSET_RESERVED_15 : return H5T_CSET_RESERVED_15 ;
    case hdf_h5_constants_H5Tconstant_JH5T_CSET_RESERVED_2 : return H5T_CSET_RESERVED_2 ;
    case hdf_h5_constants_H5Tconstant_JH5T_CSET_RESERVED_3 : return H5T_CSET_RESERVED_3 ;
    case hdf_h5_constants_H5Tconstant_JH5T_CSET_RESERVED_4 : return H5T_CSET_RESERVED_4 ;
    case hdf_h5_constants_H5Tconstant_JH5T_CSET_RESERVED_5 : return H5T_CSET_RESERVED_5 ;
    case hdf_h5_constants_H5Tconstant_JH5T_CSET_RESERVED_6 : return H5T_CSET_RESERVED_6 ;
    case hdf_h5_constants_H5Tconstant_JH5T_CSET_RESERVED_7 : return H5T_CSET_RESERVED_7 ;
    case hdf_h5_constants_H5Tconstant_JH5T_CSET_RESERVED_8 : return H5T_CSET_RESERVED_8 ;
    case hdf_h5_constants_H5Tconstant_JH5T_CSET_RESERVED_9 : return H5T_CSET_RESERVED_9 ;
    case hdf_h5_constants_H5Tconstant_JH5T_DIR_ASCEND : return H5T_DIR_ASCEND ;
    case hdf_h5_constants_H5Tconstant_JH5T_DIR_DEFAULT : return H5T_DIR_DEFAULT ;
    case hdf_h5_constants_H5Tconstant_JH5T_DIR_DESCEND : return H5T_DIR_DESCEND ;
    case hdf_h5_constants_H5Tconstant_JH5T_ENUM : return H5T_ENUM ;
    case hdf_h5_constants_H5Tconstant_JH5T_FLOAT : return H5T_FLOAT ;
    case hdf_h5_constants_H5Tconstant_JH5T_FORTRAN_S1 : return H5T_FORTRAN_S1 ;
    case hdf_h5_constants_H5Tconstant_JH5T_IEEE_F32BE : return H5T_IEEE_F32BE ;
    case hdf_h5_constants_H5Tconstant_JH5T_IEEE_F32LE : return H5T_IEEE_F32LE ;
    case hdf_h5_constants_H5Tconstant_JH5T_IEEE_F64BE : return H5T_IEEE_F64BE ;
    case hdf_h5_constants_H5Tconstant_JH5T_IEEE_F64LE : return H5T_IEEE_F64LE ;
    case hdf_h5_constants_H5Tconstant_JH5T_INTEGER : return H5T_INTEGER ;
    case hdf_h5_constants_H5Tconstant_JH5T_INTEL_B16 : return H5T_INTEL_B16 ;
    case hdf_h5_constants_H5Tconstant_JH5T_INTEL_B32 : return H5T_INTEL_B32 ;
    case hdf_h5_constants_H5Tconstant_JH5T_INTEL_B64 : return H5T_INTEL_B64 ;
    case hdf_h5_constants_H5Tconstant_JH5T_INTEL_B8 : return H5T_INTEL_B8 ;
    case hdf_h5_constants_H5Tconstant_JH5T_INTEL_F32 : return H5T_INTEL_F32 ;
    case hdf_h5_constants_H5Tconstant_JH5T_INTEL_F64 : return H5T_INTEL_F64 ;
    case hdf_h5_constants_H5Tconstant_JH5T_INTEL_I16 : return H5T_INTEL_I16 ;
    case hdf_h5_constants_H5Tconstant_JH5T_INTEL_I32 : return H5T_INTEL_I32 ;
    case hdf_h5_constants_H5Tconstant_JH5T_INTEL_I64 : return H5T_INTEL_I64 ;
    case hdf_h5_constants_H5Tconstant_JH5T_INTEL_I8 : return H5T_INTEL_I8 ;
    case hdf_h5_constants_H5Tconstant_JH5T_INTEL_U16 : return H5T_INTEL_U16 ;
    case hdf_h5_constants_H5Tconstant_JH5T_INTEL_U32 : return H5T_INTEL_U32 ;
    case hdf_h5_constants_H5Tconstant_JH5T_INTEL_U64 : return H5T_INTEL_U64 ;
    case hdf_h5_constants_H5Tconstant_JH5T_INTEL_U8 : return H5T_INTEL_U8 ;
    case hdf_h5_constants_H5Tconstant_JH5T_MIPS_B16 : return H5T_MIPS_B16 ;
    case hdf_h5_constants_H5Tconstant_JH5T_MIPS_B32 : return H5T_MIPS_B32 ;
    case hdf_h5_constants_H5Tconstant_JH5T_MIPS_B64 : return H5T_MIPS_B64 ;
    case hdf_h5_constants_H5Tconstant_JH5T_MIPS_B8 : return H5T_MIPS_B8 ;
    case hdf_h5_constants_H5Tconstant_JH5T_MIPS_F32 : return H5T_MIPS_F32 ;
    case hdf_h5_constants_H5Tconstant_JH5T_MIPS_F64 : return H5T_MIPS_F64 ;
    case hdf_h5_constants_H5Tconstant_JH5T_MIPS_I16 : return H5T_MIPS_I16 ;
    case hdf_h5_constants_H5Tconstant_JH5T_MIPS_I32 : return H5T_MIPS_I32 ;
    case hdf_h5_constants_H5Tconstant_JH5T_MIPS_I64 : return H5T_MIPS_I64 ;
    case hdf_h5_constants_H5Tconstant_JH5T_MIPS_I8 : return H5T_MIPS_I8 ;
    case hdf_h5_constants_H5Tconstant_JH5T_MIPS_U16 : return H5T_MIPS_U16 ;
    case hdf_h5_constants_H5Tconstant_JH5T_MIPS_U32 : return H5T_MIPS_U32 ;
    case hdf_h5_constants_H5Tconstant_JH5T_MIPS_U64 : return H5T_MIPS_U64 ;
    case hdf_h5_constants_H5Tconstant_JH5T_MIPS_U8 : return H5T_MIPS_U8 ;
    case hdf_h5_constants_H5Tconstant_JH5T_NATIVE_B16 : return H5T_NATIVE_B16 ;
    case hdf_h5_constants_H5Tconstant_JH5T_NATIVE_B32 : return H5T_NATIVE_B32 ;
    case hdf_h5_constants_H5Tconstant_JH5T_NATIVE_B64 : return H5T_NATIVE_B64 ;
    case hdf_h5_constants_H5Tconstant_JH5T_NATIVE_B8 : return H5T_NATIVE_B8 ;
    case hdf_h5_constants_H5Tconstant_JH5T_NATIVE_CHAR : return H5T_NATIVE_CHAR ;
    case hdf_h5_constants_H5Tconstant_JH5T_NATIVE_DOUBLE : return H5T_NATIVE_DOUBLE ;
    case hdf_h5_constants_H5Tconstant_JH5T_NATIVE_FLOAT : return H5T_NATIVE_FLOAT ;
    case hdf_h5_constants_H5Tconstant_JH5T_NATIVE_HADDR : return H5T_NATIVE_HADDR ;
    case hdf_h5_constants_H5Tconstant_JH5T_NATIVE_HBOOL : return H5T_NATIVE_HBOOL ;
    case hdf_h5_constants_H5Tconstant_JH5T_NATIVE_HERR : return H5T_NATIVE_HERR ;
    case hdf_h5_constants_H5Tconstant_JH5T_NATIVE_HSIZE : return H5T_NATIVE_HSIZE ;
    case hdf_h5_constants_H5Tconstant_JH5T_NATIVE_HSSIZE : return H5T_NATIVE_HSSIZE ;
    case hdf_h5_constants_H5Tconstant_JH5T_NATIVE_INT : return H5T_NATIVE_INT ;
    case hdf_h5_constants_H5Tconstant_JH5T_NATIVE_INT_FAST16 : return H5T_NATIVE_INT_FAST16 ;
    case hdf_h5_constants_H5Tconstant_JH5T_NATIVE_INT_FAST32 : return H5T_NATIVE_INT_FAST32 ;
    case hdf_h5_constants_H5Tconstant_JH5T_NATIVE_INT_FAST64 : return H5T_NATIVE_INT_FAST64 ;
    case hdf_h5_constants_H5Tconstant_JH5T_NATIVE_INT_FAST8 : return H5T_NATIVE_INT_FAST8 ;
    case hdf_h5_constants_H5Tconstant_JH5T_NATIVE_INT_LEAST16 : return H5T_NATIVE_INT_LEAST16 ;
    case hdf_h5_constants_H5Tconstant_JH5T_NATIVE_INT_LEAST32 : return H5T_NATIVE_INT_LEAST32 ;
    case hdf_h5_constants_H5Tconstant_JH5T_NATIVE_INT_LEAST64 : return H5T_NATIVE_INT_LEAST64 ;
    case hdf_h5_constants_H5Tconstant_JH5T_NATIVE_INT_LEAST8 : return H5T_NATIVE_INT_LEAST8 ;
    case hdf_h5_constants_H5Tconstant_JH5T_NATIVE_INT16 : return H5T_NATIVE_INT16 ;
    case hdf_h5_constants_H5Tconstant_JH5T_NATIVE_INT32 : return H5T_NATIVE_INT32 ;
    case hdf_h5_constants_H5Tconstant_JH5T_NATIVE_INT64 : return H5T_NATIVE_INT64 ;
    case hdf_h5_constants_H5Tconstant_JH5T_NATIVE_INT8 : return H5T_NATIVE_INT8 ;
    case hdf_h5_constants_H5Tconstant_JH5T_NATIVE_LDOUBLE : return H5T_NATIVE_LDOUBLE ;
    case hdf_h5_constants_H5Tconstant_JH5T_NATIVE_LLONG : return H5T_NATIVE_LLONG ;
    case hdf_h5_constants_H5Tconstant_JH5T_NATIVE_LONG : return H5T_NATIVE_LONG ;
    case hdf_h5_constants_H5Tconstant_JH5T_NATIVE_OPAQUE : return H5T_NATIVE_OPAQUE ;
    case hdf_h5_constants_H5Tconstant_JH5T_NATIVE_SCHAR : return H5T_NATIVE_SCHAR ;
    case hdf_h5_constants_H5Tconstant_JH5T_NATIVE_SHORT : return H5T_NATIVE_SHORT ;
    case hdf_h5_constants_H5Tconstant_JH5T_NATIVE_UCHAR : return H5T_NATIVE_UCHAR ;
    case hdf_h5_constants_H5Tconstant_JH5T_NATIVE_UINT : return H5T_NATIVE_UINT ;
    case hdf_h5_constants_H5Tconstant_JH5T_NATIVE_UINT_FAST16 : return H5T_NATIVE_UINT_FAST16 ;
    case hdf_h5_constants_H5Tconstant_JH5T_NATIVE_UINT_FAST32 : return H5T_NATIVE_UINT_FAST32 ;
    case hdf_h5_constants_H5Tconstant_JH5T_NATIVE_UINT_FAST64 : return H5T_NATIVE_UINT_FAST64 ;
    case hdf_h5_constants_H5Tconstant_JH5T_NATIVE_UINT_FAST8 : return H5T_NATIVE_UINT_FAST8 ;
    case hdf_h5_constants_H5Tconstant_JH5T_NATIVE_UINT_LEAST16 : return H5T_NATIVE_UINT_LEAST16 ;
    case hdf_h5_constants_H5Tconstant_JH5T_NATIVE_UINT_LEAST32 : return H5T_NATIVE_UINT_LEAST32 ;
    case hdf_h5_constants_H5Tconstant_JH5T_NATIVE_UINT_LEAST64 : return H5T_NATIVE_UINT_LEAST64 ;
    case hdf_h5_constants_H5Tconstant_JH5T_NATIVE_UINT_LEAST8 : return H5T_NATIVE_UINT_LEAST8 ;
    case hdf_h5_constants_H5Tconstant_JH5T_NATIVE_UINT16 : return H5T_NATIVE_UINT16 ;
    case hdf_h5_constants_H5Tconstant_JH5T_NATIVE_UINT32 : return H5T_NATIVE_UINT32 ;
    case hdf_h5_constants_H5Tconstant_JH5T_NATIVE_UINT64 : return H5T_NATIVE_UINT64 ;
    case hdf_h5_constants_H5Tconstant_JH5T_NATIVE_UINT8 : return H5T_NATIVE_UINT8 ;
    case hdf_h5_constants_H5Tconstant_JH5T_NATIVE_ULLONG : return H5T_NATIVE_ULLONG ;
    case hdf_h5_constants_H5Tconstant_JH5T_NATIVE_ULONG : return H5T_NATIVE_ULONG ;
    case hdf_h5_constants_H5Tconstant_JH5T_NATIVE_USHORT : return H5T_NATIVE_USHORT ;
    case hdf_h5_constants_H5Tconstant_JH5T_NCLASSES : return H5T_NCLASSES ;
    case hdf_h5_constants_H5Tconstant_JH5T_NO_CLASS : return H5T_NO_CLASS ;
    case hdf_h5_constants_H5Tconstant_JH5T_NORM_ERROR : return H5T_NORM_ERROR ;
    case hdf_h5_constants_H5Tconstant_JH5T_NORM_IMPLIED : return H5T_NORM_IMPLIED ;
    case hdf_h5_constants_H5Tconstant_JH5T_NORM_MSBSET : return H5T_NORM_MSBSET ;
    case hdf_h5_constants_H5Tconstant_JH5T_NORM_NONE : return H5T_NORM_NONE ;
    case hdf_h5_constants_H5Tconstant_JH5T_NPAD : return H5T_NPAD ;
    case hdf_h5_constants_H5Tconstant_JH5T_NSGN : return H5T_NSGN ;
    case hdf_h5_constants_H5Tconstant_JH5T_OPAQUE : return H5T_OPAQUE ;
    case hdf_h5_constants_H5Tconstant_JH5T_OPAQUE_TAG_MAX : return H5T_OPAQUE_TAG_MAX ;
    case hdf_h5_constants_H5Tconstant_JH5T_ORDER_BE : return H5T_ORDER_BE ;
    case hdf_h5_constants_H5Tconstant_JH5T_ORDER_ERROR : return H5T_ORDER_ERROR ;
    case hdf_h5_constants_H5Tconstant_JH5T_ORDER_LE : return H5T_ORDER_LE ;
    case hdf_h5_constants_H5Tconstant_JH5T_ORDER_NONE : return H5T_ORDER_NONE ;
    case hdf_h5_constants_H5Tconstant_JH5T_ORDER_VAX : return H5T_ORDER_VAX ;
    case hdf_h5_constants_H5Tconstant_JH5T_PAD_BACKGROUND : return H5T_PAD_BACKGROUND ;
    case hdf_h5_constants_H5Tconstant_JH5T_PAD_ERROR : return H5T_PAD_ERROR ;
    case hdf_h5_constants_H5Tconstant_JH5T_PAD_ONE : return H5T_PAD_ONE ;
    case hdf_h5_constants_H5Tconstant_JH5T_PAD_ZERO : return H5T_PAD_ZERO ;
    case hdf_h5_constants_H5Tconstant_JH5T_PERS_DONTCARE : return H5T_PERS_DONTCARE ;
    case hdf_h5_constants_H5Tconstant_JH5T_PERS_HARD : return H5T_PERS_HARD ;
    case hdf_h5_constants_H5Tconstant_JH5T_PERS_SOFT : return H5T_PERS_SOFT ;
    case hdf_h5_constants_H5Tconstant_JH5T_REFERENCE : return H5T_REFERENCE ;
    case hdf_h5_constants_H5Tconstant_JH5T_SGN_2 : return H5T_SGN_2 ;
    case hdf_h5_constants_H5Tconstant_JH5T_SGN_ERROR : return H5T_SGN_ERROR ;
    case hdf_h5_constants_H5Tconstant_JH5T_SGN_NONE : return H5T_SGN_NONE ;
    case hdf_h5_constants_H5Tconstant_JH5T_STD_B16BE : return H5T_STD_B16BE ;
    case hdf_h5_constants_H5Tconstant_JH5T_STD_B16LE : return H5T_STD_B16LE ;
    case hdf_h5_constants_H5Tconstant_JH5T_STD_B32BE : return H5T_STD_B32BE ;
    case hdf_h5_constants_H5Tconstant_JH5T_STD_B32LE : return H5T_STD_B32LE ;
    case hdf_h5_constants_H5Tconstant_JH5T_STD_B64BE : return H5T_STD_B64BE ;
    case hdf_h5_constants_H5Tconstant_JH5T_STD_B64LE : return H5T_STD_B64LE ;
    case hdf_h5_constants_H5Tconstant_JH5T_STD_B8BE : return H5T_STD_B8BE ;
    case hdf_h5_constants_H5Tconstant_JH5T_STD_B8LE : return H5T_STD_B8LE ;
    case hdf_h5_constants_H5Tconstant_JH5T_STD_I16BE : return H5T_STD_I16BE ;
    case hdf_h5_constants_H5Tconstant_JH5T_STD_I16LE : return H5T_STD_I16LE ;
    case hdf_h5_constants_H5Tconstant_JH5T_STD_I32BE : return H5T_STD_I32BE ;
    case hdf_h5_constants_H5Tconstant_JH5T_STD_I32LE : return H5T_STD_I32LE ;
    case hdf_h5_constants_H5Tconstant_JH5T_STD_I64BE : return H5T_STD_I64BE ;
    case hdf_h5_constants_H5Tconstant_JH5T_STD_I64LE : return H5T_STD_I64LE ;
    case hdf_h5_constants_H5Tconstant_JH5T_STD_I8BE : return H5T_STD_I8BE ;
    case hdf_h5_constants_H5Tconstant_JH5T_STD_I8LE : return H5T_STD_I8LE ;
    case hdf_h5_constants_H5Tconstant_JH5T_STD_REF_DSETREG : return H5T_STD_REF_DSETREG ;
    case hdf_h5_constants_H5Tconstant_JH5T_STD_REF_OBJ : return H5T_STD_REF_OBJ ;
    case hdf_h5_constants_H5Tconstant_JH5T_STD_U16BE : return H5T_STD_U16BE ;
    case hdf_h5_constants_H5Tconstant_JH5T_STD_U16LE : return H5T_STD_U16LE ;
    case hdf_h5_constants_H5Tconstant_JH5T_STD_U32BE : return H5T_STD_U32BE ;
    case hdf_h5_constants_H5Tconstant_JH5T_STD_U32LE : return H5T_STD_U32LE ;
    case hdf_h5_constants_H5Tconstant_JH5T_STD_U64BE : return H5T_STD_U64BE ;
    case hdf_h5_constants_H5Tconstant_JH5T_STD_U64LE : return H5T_STD_U64LE ;
    case hdf_h5_constants_H5Tconstant_JH5T_STD_U8BE : return H5T_STD_U8BE ;
    case hdf_h5_constants_H5Tconstant_JH5T_STD_U8LE : return H5T_STD_U8LE ;
    case hdf_h5_constants_H5Tconstant_JH5T_STR_ERROR : return H5T_STR_ERROR ;
    case hdf_h5_constants_H5Tconstant_JH5T_STR_NULLPAD : return H5T_STR_NULLPAD ;
    case hdf_h5_constants_H5Tconstant_JH5T_STR_NULLTERM : return H5T_STR_NULLTERM ;
    case hdf_h5_constants_H5Tconstant_JH5T_STR_RESERVED_10 : return H5T_STR_RESERVED_10 ;
    case hdf_h5_constants_H5Tconstant_JH5T_STR_RESERVED_11 : return H5T_STR_RESERVED_11 ;
    case hdf_h5_constants_H5Tconstant_JH5T_STR_RESERVED_12 : return H5T_STR_RESERVED_12 ;
    case hdf_h5_constants_H5Tconstant_JH5T_STR_RESERVED_13 : return H5T_STR_RESERVED_13 ;
    case hdf_h5_constants_H5Tconstant_JH5T_STR_RESERVED_14 : return H5T_STR_RESERVED_14 ;
    case hdf_h5_constants_H5Tconstant_JH5T_STR_RESERVED_15 : return H5T_STR_RESERVED_15 ;
    case hdf_h5_constants_H5Tconstant_JH5T_STR_RESERVED_3 : return H5T_STR_RESERVED_3 ;
    case hdf_h5_constants_H5Tconstant_JH5T_STR_RESERVED_4 : return H5T_STR_RESERVED_4 ;
    case hdf_h5_constants_H5Tconstant_JH5T_STR_RESERVED_5 : return H5T_STR_RESERVED_5 ;
    case hdf_h5_constants_H5Tconstant_JH5T_STR_RESERVED_6 : return H5T_STR_RESERVED_6 ;
    case hdf_h5_constants_H5Tconstant_JH5T_STR_RESERVED_7 : return H5T_STR_RESERVED_7 ;
    case hdf_h5_constants_H5Tconstant_JH5T_STR_RESERVED_8 : return H5T_STR_RESERVED_8 ;
    case hdf_h5_constants_H5Tconstant_JH5T_STR_RESERVED_9 : return H5T_STR_RESERVED_9 ;
    case hdf_h5_constants_H5Tconstant_JH5T_STR_SPACEPAD : return H5T_STR_SPACEPAD ;
    case hdf_h5_constants_H5Tconstant_JH5T_STRING : return H5T_STRING ;
    case hdf_h5_constants_H5Tconstant_JH5T_TIME : return H5T_TIME ;
    case hdf_h5_constants_H5Tconstant_JH5T_UNIX_D32BE : return H5T_UNIX_D32BE ;
    case hdf_h5_constants_H5Tconstant_JH5T_UNIX_D32LE : return H5T_UNIX_D32LE ;
    case hdf_h5_constants_H5Tconstant_JH5T_UNIX_D64BE : return H5T_UNIX_D64BE ;
    case hdf_h5_constants_H5Tconstant_JH5T_UNIX_D64LE : return H5T_UNIX_D64LE ;
    case hdf_h5_constants_H5Tconstant_JH5T_VARIABLE : return H5T_VARIABLE ;
    case hdf_h5_constants_H5Tconstant_JH5T_VLEN : return H5T_VLEN ;
    }

    return -1;
}

#ifdef __cplusplus
}
#endif
