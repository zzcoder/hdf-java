/****************************************************************************
 * NCSA HDF                                                                 *
 * National Comptational Science Alliance                                   *
 * University of Illinois at Urbana-Champaign                               *
 * 605 E. Springfield, Champaign IL 61820                                   *
 *                                                                          *
 * For conditions of distribution and use, see the accompanying             *
 * hdf-java/COPYING file.                                                   *
 *                                                                          *
 ****************************************************************************/
#ifdef __cplusplus
extern "C" {
#endif 

#include "hdf5.h"
#include "h5Constants.h"
#include <jni.h>

/*
 * Class:     ncsa_hdf_hdf5lib_H5Header converts Java constants defined
 *            at ncsa.hdf.hdf5lib.HDF5Constants.java to HDF5 runtime global variables.
 * Method:    J2c
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_J2C
  (JNIEnv *env, jclass clss, jint java_constant)
{
	switch (java_constant)
	{
		case JH5T_ALPHA_B16 : return H5T_ALPHA_B16;
		case JH5T_ALPHA_B32 : return H5T_ALPHA_B32;
		case JH5T_ALPHA_B64 : return H5T_ALPHA_B64;
		case JH5T_ALPHA_B8 : return H5T_ALPHA_B8;
		case JH5T_ALPHA_F32 : return H5T_ALPHA_F32;
		case JH5T_ALPHA_F64 : return H5T_ALPHA_F64;
		case JH5T_ALPHA_I16 : return H5T_ALPHA_I16;
		case JH5T_ALPHA_I32 : return H5T_ALPHA_I32;
		case JH5T_ALPHA_I64 : return H5T_ALPHA_I64;
		case JH5T_ALPHA_I8 : return H5T_ALPHA_I8;
		case JH5T_ALPHA_U16 : return H5T_ALPHA_U16;
		case JH5T_ALPHA_U32 : return H5T_ALPHA_U32;
		case JH5T_ALPHA_U64 : return H5T_ALPHA_U64;
		case JH5T_ALPHA_U8 : return H5T_ALPHA_U8;
		case JH5T_C_S1 : return H5T_C_S1;
		case JH5T_FORTRAN_S1 : return H5T_FORTRAN_S1;
		case JH5T_IEEE_F32BE : return H5T_IEEE_F32BE;
		case JH5T_IEEE_F32LE : return H5T_IEEE_F32LE;
		case JH5T_IEEE_F64BE : return H5T_IEEE_F64BE;
		case JH5T_IEEE_F64LE : return H5T_IEEE_F64LE;
		case JH5T_INTEL_B16 : return H5T_INTEL_B16;
		case JH5T_INTEL_B32 : return H5T_INTEL_B32;
		case JH5T_INTEL_B64 : return H5T_INTEL_B64;
		case JH5T_INTEL_B8 : return H5T_INTEL_B8;
		case JH5T_INTEL_F32 : return H5T_INTEL_F32;
		case JH5T_INTEL_F64 : return H5T_INTEL_F64;
		case JH5T_INTEL_I16 : return H5T_INTEL_I16;
		case JH5T_INTEL_I32 : return H5T_INTEL_I32;
		case JH5T_INTEL_I64 : return H5T_INTEL_I64;
		case JH5T_INTEL_I8 : return H5T_INTEL_I8;
		case JH5T_INTEL_U16 : return H5T_INTEL_U16;
		case JH5T_INTEL_U32 : return H5T_INTEL_U32;
		case JH5T_INTEL_U64 : return H5T_INTEL_U64;
		case JH5T_INTEL_U8 : return H5T_INTEL_U8;
		case JH5T_MIPS_B16 : return H5T_MIPS_B16;
		case JH5T_MIPS_B32 : return H5T_MIPS_B32;
		case JH5T_MIPS_B64 : return H5T_MIPS_B64;
		case JH5T_MIPS_B8 : return H5T_MIPS_B8;
		case JH5T_MIPS_F32 : return H5T_MIPS_F32;
		case JH5T_MIPS_F64 : return H5T_MIPS_F64;
		case JH5T_MIPS_I16 : return H5T_MIPS_I16;
		case JH5T_MIPS_I32 : return H5T_MIPS_I32;
		case JH5T_MIPS_I64 : return H5T_MIPS_I64;
		case JH5T_MIPS_I8 : return H5T_MIPS_I8;
		case JH5T_MIPS_U16 : return H5T_MIPS_U16;
		case JH5T_MIPS_U32 : return H5T_MIPS_U32;
		case JH5T_MIPS_U64 : return H5T_MIPS_U64;
		case JH5T_MIPS_U8 : return H5T_MIPS_U8;
		case JH5T_NATIVE_B16 : return H5T_NATIVE_B16;
		case JH5T_NATIVE_B32 : return H5T_NATIVE_B32;
		case JH5T_NATIVE_B64 : return H5T_NATIVE_B64;
		case JH5T_NATIVE_B8 : return H5T_NATIVE_B8;
		case JH5T_NATIVE_CHAR : return H5T_NATIVE_CHAR;
		case JH5T_NATIVE_DOUBLE : return H5T_NATIVE_DOUBLE;
		case JH5T_NATIVE_FLOAT : return H5T_NATIVE_FLOAT;
		case JH5T_NATIVE_HBOOL : return H5T_NATIVE_HBOOL;
		case JH5T_NATIVE_HERR : return H5T_NATIVE_HERR;
		case JH5T_NATIVE_HSIZE : return H5T_NATIVE_HSIZE;
		case JH5T_NATIVE_HSSIZE : return H5T_NATIVE_HSSIZE;
		case JH5T_NATIVE_INT : return H5T_NATIVE_INT;
		case JH5T_NATIVE_INT_FAST16 : return H5T_NATIVE_INT_FAST16;
		case JH5T_NATIVE_INT_FAST32 : return H5T_NATIVE_INT_FAST32;
		case JH5T_NATIVE_INT_FAST64 : return H5T_NATIVE_INT_FAST64;
		case JH5T_NATIVE_INT_FAST8 : return H5T_NATIVE_INT_FAST8;
		case JH5T_NATIVE_INT_LEAST16 : return H5T_NATIVE_INT_LEAST16;
		case JH5T_NATIVE_INT_LEAST32 : return H5T_NATIVE_INT_LEAST32;
		case JH5T_NATIVE_INT_LEAST64 : return H5T_NATIVE_INT_LEAST64;
		case JH5T_NATIVE_INT_LEAST8 : return H5T_NATIVE_INT_LEAST8;
		case JH5T_NATIVE_INT16 : return H5T_NATIVE_INT16;
		case JH5T_NATIVE_INT32 : return H5T_NATIVE_INT32;
		case JH5T_NATIVE_INT64 : return H5T_NATIVE_INT64;
		case JH5T_NATIVE_INT8 : return H5T_NATIVE_INT8;
		case JH5T_NATIVE_LDOUBLE : return H5T_NATIVE_LDOUBLE;
		case JH5T_NATIVE_LLONG : return H5T_NATIVE_LLONG;
		case JH5T_NATIVE_LONG : return H5T_NATIVE_LONG;
/*
		case JH5T_NATIVE_OPAQUE : return H5T_NATIVE_OPAQUE;
*/
		case JH5T_NATIVE_SCHAR : return H5T_NATIVE_SCHAR;
		case JH5T_NATIVE_SHORT : return H5T_NATIVE_SHORT;
		case JH5T_NATIVE_UCHAR : return H5T_NATIVE_UCHAR;
		case JH5T_NATIVE_UINT : return H5T_NATIVE_UINT;
		case JH5T_NATIVE_UINT_FAST16 : return H5T_NATIVE_UINT_FAST16;
		case JH5T_NATIVE_UINT_FAST32 : return H5T_NATIVE_UINT_FAST32;
		case JH5T_NATIVE_UINT_FAST64 : return H5T_NATIVE_UINT_FAST64;
		case JH5T_NATIVE_UINT_FAST8 : return H5T_NATIVE_UINT_FAST8;
		case JH5T_NATIVE_UINT_LEAST16 : return H5T_NATIVE_UINT_LEAST16;
		case JH5T_NATIVE_UINT_LEAST32 : return H5T_NATIVE_UINT_LEAST32;
		case JH5T_NATIVE_UINT_LEAST64 : return H5T_NATIVE_UINT_LEAST64;
		case JH5T_NATIVE_UINT_LEAST8 : return H5T_NATIVE_UINT_LEAST8;
		case JH5T_NATIVE_UINT16 : return H5T_NATIVE_UINT16;
		case JH5T_NATIVE_UINT32 : return H5T_NATIVE_UINT32;
		case JH5T_NATIVE_UINT64 : return H5T_NATIVE_UINT64;
		case JH5T_NATIVE_UINT8 : return H5T_NATIVE_UINT8;
		case JH5T_NATIVE_ULLONG : return H5T_NATIVE_ULLONG;
		case JH5T_NATIVE_ULONG : return H5T_NATIVE_ULONG;
		case JH5T_NATIVE_USHORT : return H5T_NATIVE_USHORT;
		case JH5T_NCSET : return H5T_NCSET;
		case JH5T_NSTR : return H5T_NSTR;
		case JH5T_STD_B16BE : return H5T_STD_B16BE;
		case JH5T_STD_B16LE : return H5T_STD_B16LE;
		case JH5T_STD_B32BE : return H5T_STD_B32BE;
		case JH5T_STD_B32LE : return H5T_STD_B32LE;
		case JH5T_STD_B64BE : return H5T_STD_B64BE;
		case JH5T_STD_B64LE : return H5T_STD_B64LE;
		case JH5T_STD_B8BE : return H5T_STD_B8BE;
		case JH5T_STD_B8LE : return H5T_STD_B8LE;
		case JH5T_STD_I16BE : return H5T_STD_I16BE;
		case JH5T_STD_I16LE : return H5T_STD_I16LE;
		case JH5T_STD_I32BE : return H5T_STD_I32BE;
		case JH5T_STD_I32LE : return H5T_STD_I32LE;
		case JH5T_STD_I64BE : return H5T_STD_I64BE;
		case JH5T_STD_I64LE : return H5T_STD_I64LE;
		case JH5T_STD_I8BE : return H5T_STD_I8BE;
		case JH5T_STD_I8LE : return H5T_STD_I8LE;
		case JH5T_STD_REF_DSETREG : return H5T_STD_REF_DSETREG;
		case JH5T_STD_REF_OBJ : return H5T_STD_REF_OBJ;
		case JH5T_STD_U16BE : return H5T_STD_U16BE;
		case JH5T_STD_U16LE : return H5T_STD_U16LE;
		case JH5T_STD_U32BE : return H5T_STD_U32BE;
		case JH5T_STD_U32LE : return H5T_STD_U32LE;
		case JH5T_STD_U64BE : return H5T_STD_U64BE;
		case JH5T_STD_U64LE : return H5T_STD_U64LE;
		case JH5T_STD_U8BE : return H5T_STD_U8BE;
		case JH5T_STD_U8LE : return H5T_STD_U8LE;
		case JH5T_UNIX_D32BE : return H5T_UNIX_D32BE;
		case JH5T_UNIX_D32LE : return H5T_UNIX_D32LE;
		case JH5T_UNIX_D64BE : return H5T_UNIX_D64BE;
		case JH5T_UNIX_D64LE : return H5T_UNIX_D64LE;
		default: return java_constant;
	}
}
#ifdef __cplusplus
}
#endif 
