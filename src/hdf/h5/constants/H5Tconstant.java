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

package hdf.h5.constants;

import hdf.h5.H5;


/**
/**
 *  This class contains C constants and enumerated types of HDF5 library.
 *  The values of these constants are obtained from the library by calling
 *  J2C(int jconstant), where jconstant is any of the private constants which
 *  start their name with "JH5" need to be converted.
 *  <p>
 *  <b>Do not edit this file!</b>
 *  </p>
 */
///////////////////////////////////////////////////////////////////////////
//           This list must be identical to H5Constants.h                //
//                DO NOT EDIT THE LIST !!!                               //
///////////////////////////////////////////////////////////////////////////

public class H5Tconstant {
  final private static int JH5T_ALPHA_B16 = 3240;
  final private static int JH5T_ALPHA_B32 = 3250;
  final private static int JH5T_ALPHA_B64 = 3260;
  final private static int JH5T_ALPHA_B8 = 3270;
  final private static int JH5T_ALPHA_F32 = 3280;
  final private static int JH5T_ALPHA_F64 = 3290;
  final private static int JH5T_ALPHA_I16 = 3300;
  final private static int JH5T_ALPHA_I32 = 3310;
  final private static int JH5T_ALPHA_I64 = 3320;
  final private static int JH5T_ALPHA_I8 = 3330;
  final private static int JH5T_ALPHA_U16 = 3340;
  final private static int JH5T_ALPHA_U32 = 3350;
  final private static int JH5T_ALPHA_U64 = 3360;
  final private static int JH5T_ALPHA_U8 = 3370;
  final private static int JH5T_ARRAY = 3380;
  final private static int JH5T_BITFIELD = 3390;
  final private static int JH5T_BKG_NO = 3400;
  final private static int JH5T_BKG_YES = 3410;
  final private static int JH5T_C_S1 = 3420;
  final private static int JH5T_COMPOUND = 3430;
  final private static int JH5T_CONV_CONV = 3440;
  final private static int JH5T_CONV_FREE = 3450;
  final private static int JH5T_CONV_INIT = 3460;
  final private static int JH5T_CSET_ASCII = 3470;
  final private static int JH5T_CSET_ERROR = 3480;
  final private static int JH5T_CSET_RESERVED_1 = 3490;
  final private static int JH5T_CSET_RESERVED_10 = 3500;
  final private static int JH5T_CSET_RESERVED_11 = 3510;
  final private static int JH5T_CSET_RESERVED_12 = 3520;
  final private static int JH5T_CSET_RESERVED_13 = 3530;
  final private static int JH5T_CSET_RESERVED_14 = 3540;
  final private static int JH5T_CSET_RESERVED_15 = 3550;
  final private static int JH5T_CSET_RESERVED_2 = 3560;
  final private static int JH5T_CSET_RESERVED_3 = 3570;
  final private static int JH5T_CSET_RESERVED_4 = 3580;
  final private static int JH5T_CSET_RESERVED_5 = 3590;
  final private static int JH5T_CSET_RESERVED_6 = 3600;
  final private static int JH5T_CSET_RESERVED_7 = 3610;
  final private static int JH5T_CSET_RESERVED_8 = 3620;
  final private static int JH5T_CSET_RESERVED_9 = 3630;
  final private static int JH5T_DIR_ASCEND = 3640;
  final private static int JH5T_DIR_DEFAULT = 3650;
  final private static int JH5T_DIR_DESCEND = 3660;
  final private static int JH5T_ENUM = 3670;
  final private static int JH5T_FLOAT = 3680;
  final private static int JH5T_FORTRAN_S1 = 3690;
  final private static int JH5T_IEEE_F32BE = 3700;
  final private static int JH5T_IEEE_F32LE = 3710;
  final private static int JH5T_IEEE_F64BE = 3720;
  final private static int JH5T_IEEE_F64LE = 3730;
  final private static int JH5T_INTEGER = 3740;
  final private static int JH5T_INTEL_B16 = 3750;
  final private static int JH5T_INTEL_B32 = 3760;
  final private static int JH5T_INTEL_B64 = 3770;
  final private static int JH5T_INTEL_B8 = 3780;
  final private static int JH5T_INTEL_F32 = 3790;
  final private static int JH5T_INTEL_F64 = 3800;
  final private static int JH5T_INTEL_I16 = 3810;
  final private static int JH5T_INTEL_I32 = 3820;
  final private static int JH5T_INTEL_I64 = 3830;
  final private static int JH5T_INTEL_I8 = 3840;
  final private static int JH5T_INTEL_U16 = 3850;
  final private static int JH5T_INTEL_U32 = 3860;
  final private static int JH5T_INTEL_U64 = 3870;
  final private static int JH5T_INTEL_U8 = 3880;
  final private static int JH5T_MIPS_B16 = 3890;
  final private static int JH5T_MIPS_B32 = 3900;
  final private static int JH5T_MIPS_B64 = 3910;
  final private static int JH5T_MIPS_B8 = 3920;
  final private static int JH5T_MIPS_F32 = 3930;
  final private static int JH5T_MIPS_F64 = 3940;
  final private static int JH5T_MIPS_I16 = 3950;
  final private static int JH5T_MIPS_I32 = 3960;
  final private static int JH5T_MIPS_I64 = 3970;
  final private static int JH5T_MIPS_I8 = 3980;
  final private static int JH5T_MIPS_U16 = 3990;
  final private static int JH5T_MIPS_U32 = 4000;
  final private static int JH5T_MIPS_U64 = 4010;
  final private static int JH5T_MIPS_U8 = 4020;
  final private static int JH5T_NATIVE_B16 = 4030;
  final private static int JH5T_NATIVE_B32 = 4040;
  final private static int JH5T_NATIVE_B64 = 4050;
  final private static int JH5T_NATIVE_B8 = 4060;
  final private static int JH5T_NATIVE_CHAR = 4070;
  final private static int JH5T_NATIVE_DOUBLE = 4080;
  final private static int JH5T_NATIVE_FLOAT = 4090;
  final private static int JH5T_NATIVE_HADDR = 4100;
  final private static int JH5T_NATIVE_HBOOL = 4110;
  final private static int JH5T_NATIVE_HERR = 4120;
  final private static int JH5T_NATIVE_HSIZE = 4130;
  final private static int JH5T_NATIVE_HSSIZE = 4140;
  final private static int JH5T_NATIVE_INT = 4150;
  final private static int JH5T_NATIVE_INT_FAST16 = 4160;
  final private static int JH5T_NATIVE_INT_FAST32 = 4170;
  final private static int JH5T_NATIVE_INT_FAST64 = 4180;
  final private static int JH5T_NATIVE_INT_FAST8 = 4190;
  final private static int JH5T_NATIVE_INT_LEAST16 = 4200;
  final private static int JH5T_NATIVE_INT_LEAST32 = 4210;
  final private static int JH5T_NATIVE_INT_LEAST64 = 4220;
  final private static int JH5T_NATIVE_INT_LEAST8 = 4230;
  final private static int JH5T_NATIVE_INT16 = 4240;
  final private static int JH5T_NATIVE_INT32 = 4250;
  final private static int JH5T_NATIVE_INT64 = 4260;
  final private static int JH5T_NATIVE_INT8 = 4270;
  final private static int JH5T_NATIVE_LDOUBLE = 4280;
  final private static int JH5T_NATIVE_LLONG = 4290;
  final private static int JH5T_NATIVE_LONG = 4300;
  final private static int JH5T_NATIVE_OPAQUE = 4310;
  final private static int JH5T_NATIVE_SCHAR = 4320;
  final private static int JH5T_NATIVE_SHORT = 4330;
  final private static int JH5T_NATIVE_UCHAR = 4340;
  final private static int JH5T_NATIVE_UINT = 4350;
  final private static int JH5T_NATIVE_UINT_FAST16 = 4360;
  final private static int JH5T_NATIVE_UINT_FAST32 = 4370;
  final private static int JH5T_NATIVE_UINT_FAST64 = 4380;
  final private static int JH5T_NATIVE_UINT_FAST8 = 4390;
  final private static int JH5T_NATIVE_UINT_LEAST16 = 4400;
  final private static int JH5T_NATIVE_UINT_LEAST32 = 4410;
  final private static int JH5T_NATIVE_UINT_LEAST64 = 4420;
  final private static int JH5T_NATIVE_UINT_LEAST8 = 4430;
  final private static int JH5T_NATIVE_UINT16 = 4440;
  final private static int JH5T_NATIVE_UINT32 = 4450;
  final private static int JH5T_NATIVE_UINT64 = 4460;
  final private static int JH5T_NATIVE_UINT8 = 4470;
  final private static int JH5T_NATIVE_ULLONG = 4480;
  final private static int JH5T_NATIVE_ULONG = 4490;
  final private static int JH5T_NATIVE_USHORT = 4500;
  final private static int JH5T_NCLASSES = 4510;
  final private static int JH5T_NO_CLASS = 4520;
  final private static int JH5T_NORM_ERROR = 4530;
  final private static int JH5T_NORM_IMPLIED = 4540;
  final private static int JH5T_NORM_MSBSET = 4550;
  final private static int JH5T_NORM_NONE = 4560;
  final private static int JH5T_NPAD = 4570;
  final private static int JH5T_NSGN = 4580;
  final private static int JH5T_OPAQUE = 4590;
  final private static int JH5T_OPAQUE_TAG_MAX =4595; /* 1.6.5 */
  final private static int JH5T_ORDER_BE = 4600;
  final private static int JH5T_ORDER_ERROR = 4610;
  final private static int JH5T_ORDER_LE = 4620;
  final private static int JH5T_ORDER_NONE = 4630;
  final private static int JH5T_ORDER_VAX = 4640;
  final private static int JH5T_PAD_BACKGROUND = 4650;
  final private static int JH5T_PAD_ERROR = 4660;
  final private static int JH5T_PAD_ONE = 4670;
  final private static int JH5T_PAD_ZERO = 4680;
  final private static int JH5T_PERS_DONTCARE = 4690;
  final private static int JH5T_PERS_HARD = 4700;
  final private static int JH5T_PERS_SOFT = 4710;
  final private static int JH5T_REFERENCE = 4720;
  final private static int JH5T_SGN_2 = 4730;
  final private static int JH5T_SGN_ERROR = 4740;
  final private static int JH5T_SGN_NONE = 4750;
  final private static int JH5T_STD_B16BE = 4760;
  final private static int JH5T_STD_B16LE = 4770;
  final private static int JH5T_STD_B32BE = 4780;
  final private static int JH5T_STD_B32LE = 4790;
  final private static int JH5T_STD_B64BE = 4800;
  final private static int JH5T_STD_B64LE = 4810;
  final private static int JH5T_STD_B8BE = 4820;
  final private static int JH5T_STD_B8LE = 4830;
  final private static int JH5T_STD_I16BE = 4840;
  final private static int JH5T_STD_I16LE = 4850;
  final private static int JH5T_STD_I32BE = 4860;
  final private static int JH5T_STD_I32LE = 4870;
  final private static int JH5T_STD_I64BE = 4880;
  final private static int JH5T_STD_I64LE = 4890;
  final private static int JH5T_STD_I8BE = 4900;
  final private static int JH5T_STD_I8LE = 4910;
  final private static int JH5T_STD_REF_DSETREG = 4920;
  final private static int JH5T_STD_REF_OBJ = 4930;
  final private static int JH5T_STD_U16BE = 4940;
  final private static int JH5T_STD_U16LE = 4950;
  final private static int JH5T_STD_U32BE = 4960;
  final private static int JH5T_STD_U32LE = 4970;
  final private static int JH5T_STD_U64BE = 4980;
  final private static int JH5T_STD_U64LE = 4990;
  final private static int JH5T_STD_U8BE = 5000;
  final private static int JH5T_STD_U8LE = 5010;
  final private static int JH5T_STR_ERROR = 5020;
  final private static int JH5T_STR_NULLPAD = 5030;
  final private static int JH5T_STR_NULLTERM = 5040;
  final private static int JH5T_STR_RESERVED_10 = 5050;
  final private static int JH5T_STR_RESERVED_11 = 5060;
  final private static int JH5T_STR_RESERVED_12 = 5070;
  final private static int JH5T_STR_RESERVED_13 = 5080;
  final private static int JH5T_STR_RESERVED_14 = 5090;
  final private static int JH5T_STR_RESERVED_15 = 5100;
  final private static int JH5T_STR_RESERVED_3 = 5110;
  final private static int JH5T_STR_RESERVED_4 = 5120;
  final private static int JH5T_STR_RESERVED_5 = 5130;
  final private static int JH5T_STR_RESERVED_6 = 5140;
  final private static int JH5T_STR_RESERVED_7 = 5150;
  final private static int JH5T_STR_RESERVED_8 = 5160;
  final private static int JH5T_STR_RESERVED_9 = 5170;
  final private static int JH5T_STR_SPACEPAD = 5180;
  final private static int JH5T_STRING = 5190;
  final private static int JH5T_TIME = 5200;
  final private static int JH5T_UNIX_D32BE = 5210;
  final private static int JH5T_UNIX_D32LE = 5220;
  final private static int JH5T_UNIX_D64BE = 5230;
  final private static int JH5T_UNIX_D64LE = 5240;
  final private static int JH5T_VARIABLE = 5245;
  final private static int JH5T_VLEN = 5250;

  ///////////////////////////////////////////////////////////////////////////
  //                Get the HDF5 constants from the library                //
  ///////////////////////////////////////////////////////////////////////////
  final public static int H5T_ALPHA_B16 = H5.J2C( JH5T_ALPHA_B16 );
  final public static int H5T_ALPHA_B32 = H5.J2C( JH5T_ALPHA_B32 );
  final public static int H5T_ALPHA_B64 = H5.J2C( JH5T_ALPHA_B64 );
  final public static int H5T_ALPHA_B8 = H5.J2C( JH5T_ALPHA_B8 );
  final public static int H5T_ALPHA_F32 = H5.J2C( JH5T_ALPHA_F32 );
  final public static int H5T_ALPHA_F64 = H5.J2C( JH5T_ALPHA_F64 );
  final public static int H5T_ALPHA_I16 = H5.J2C( JH5T_ALPHA_I16 );
  final public static int H5T_ALPHA_I32 = H5.J2C( JH5T_ALPHA_I32 );
  final public static int H5T_ALPHA_I64 = H5.J2C( JH5T_ALPHA_I64 );
  final public static int H5T_ALPHA_I8 = H5.J2C( JH5T_ALPHA_I8 );
  final public static int H5T_ALPHA_U16 = H5.J2C( JH5T_ALPHA_U16 );
  final public static int H5T_ALPHA_U32 = H5.J2C( JH5T_ALPHA_U32 );
  final public static int H5T_ALPHA_U64 = H5.J2C( JH5T_ALPHA_U64 );
  final public static int H5T_ALPHA_U8 = H5.J2C( JH5T_ALPHA_U8 );
  final public static int H5T_ARRAY = H5.J2C( JH5T_ARRAY );
  final public static int H5T_BITFIELD = H5.J2C( JH5T_BITFIELD );
  final public static int H5T_BKG_NO = H5.J2C( JH5T_BKG_NO );
  final public static int H5T_BKG_YES = H5.J2C( JH5T_BKG_YES );
  final public static int H5T_C_S1 = H5.J2C( JH5T_C_S1 );
  final public static int H5T_COMPOUND = H5.J2C( JH5T_COMPOUND );
  final public static int H5T_CONV_CONV = H5.J2C( JH5T_CONV_CONV );
  final public static int H5T_CONV_FREE = H5.J2C( JH5T_CONV_FREE );
  final public static int H5T_CONV_INIT = H5.J2C( JH5T_CONV_INIT );
  final public static int H5T_CSET_ASCII = H5.J2C( JH5T_CSET_ASCII );
  final public static int H5T_CSET_ERROR = H5.J2C( JH5T_CSET_ERROR );
  final public static int H5T_CSET_RESERVED_1 = H5.J2C( JH5T_CSET_RESERVED_1 );
  final public static int H5T_CSET_RESERVED_10 = H5.J2C( JH5T_CSET_RESERVED_10 );
  final public static int H5T_CSET_RESERVED_11 = H5.J2C( JH5T_CSET_RESERVED_11 );
  final public static int H5T_CSET_RESERVED_12 = H5.J2C( JH5T_CSET_RESERVED_12 );
  final public static int H5T_CSET_RESERVED_13 = H5.J2C( JH5T_CSET_RESERVED_13 );
  final public static int H5T_CSET_RESERVED_14 = H5.J2C( JH5T_CSET_RESERVED_14 );
  final public static int H5T_CSET_RESERVED_15 = H5.J2C( JH5T_CSET_RESERVED_15 );
  final public static int H5T_CSET_RESERVED_2 = H5.J2C( JH5T_CSET_RESERVED_2 );
  final public static int H5T_CSET_RESERVED_3 = H5.J2C( JH5T_CSET_RESERVED_3 );
  final public static int H5T_CSET_RESERVED_4 = H5.J2C( JH5T_CSET_RESERVED_4 );
  final public static int H5T_CSET_RESERVED_5 = H5.J2C( JH5T_CSET_RESERVED_5 );
  final public static int H5T_CSET_RESERVED_6 = H5.J2C( JH5T_CSET_RESERVED_6 );
  final public static int H5T_CSET_RESERVED_7 = H5.J2C( JH5T_CSET_RESERVED_7 );
  final public static int H5T_CSET_RESERVED_8 = H5.J2C( JH5T_CSET_RESERVED_8 );
  final public static int H5T_CSET_RESERVED_9 = H5.J2C( JH5T_CSET_RESERVED_9 );
  final public static int H5T_DIR_ASCEND = H5.J2C( JH5T_DIR_ASCEND );
  final public static int H5T_DIR_DEFAULT = H5.J2C( JH5T_DIR_DEFAULT );
  final public static int H5T_DIR_DESCEND = H5.J2C( JH5T_DIR_DESCEND );
  final public static int H5T_ENUM = H5.J2C( JH5T_ENUM );
  final public static int H5T_FLOAT = H5.J2C( JH5T_FLOAT );
  final public static int H5T_FORTRAN_S1 = H5.J2C( JH5T_FORTRAN_S1 );
  final public static int H5T_IEEE_F32BE = H5.J2C( JH5T_IEEE_F32BE );
  final public static int H5T_IEEE_F32LE = H5.J2C( JH5T_IEEE_F32LE );
  final public static int H5T_IEEE_F64BE = H5.J2C( JH5T_IEEE_F64BE );
  final public static int H5T_IEEE_F64LE = H5.J2C( JH5T_IEEE_F64LE );
  final public static int H5T_INTEGER = H5.J2C( JH5T_INTEGER );
  final public static int H5T_INTEL_B16 = H5.J2C( JH5T_INTEL_B16 );
  final public static int H5T_INTEL_B32 = H5.J2C( JH5T_INTEL_B32 );
  final public static int H5T_INTEL_B64 = H5.J2C( JH5T_INTEL_B64 );
  final public static int H5T_INTEL_B8 = H5.J2C( JH5T_INTEL_B8 );
  final public static int H5T_INTEL_F32 = H5.J2C( JH5T_INTEL_F32 );
  final public static int H5T_INTEL_F64 = H5.J2C( JH5T_INTEL_F64 );
  final public static int H5T_INTEL_I16 = H5.J2C( JH5T_INTEL_I16 );
  final public static int H5T_INTEL_I32 = H5.J2C( JH5T_INTEL_I32 );
  final public static int H5T_INTEL_I64 = H5.J2C( JH5T_INTEL_I64 );
  final public static int H5T_INTEL_I8 = H5.J2C( JH5T_INTEL_I8 );
  final public static int H5T_INTEL_U16 = H5.J2C( JH5T_INTEL_U16 );
  final public static int H5T_INTEL_U32 = H5.J2C( JH5T_INTEL_U32 );
  final public static int H5T_INTEL_U64 = H5.J2C( JH5T_INTEL_U64 );
  final public static int H5T_INTEL_U8 = H5.J2C( JH5T_INTEL_U8 );
  final public static int H5T_MIPS_B16 = H5.J2C( JH5T_MIPS_B16 );
  final public static int H5T_MIPS_B32 = H5.J2C( JH5T_MIPS_B32 );
  final public static int H5T_MIPS_B64 = H5.J2C( JH5T_MIPS_B64 );
  final public static int H5T_MIPS_B8 = H5.J2C( JH5T_MIPS_B8 );
  final public static int H5T_MIPS_F32 = H5.J2C( JH5T_MIPS_F32 );
  final public static int H5T_MIPS_F64 = H5.J2C( JH5T_MIPS_F64 );
  final public static int H5T_MIPS_I16 = H5.J2C( JH5T_MIPS_I16 );
  final public static int H5T_MIPS_I32 = H5.J2C( JH5T_MIPS_I32 );
  final public static int H5T_MIPS_I64 = H5.J2C( JH5T_MIPS_I64 );
  final public static int H5T_MIPS_I8 = H5.J2C( JH5T_MIPS_I8 );
  final public static int H5T_MIPS_U16 = H5.J2C( JH5T_MIPS_U16 );
  final public static int H5T_MIPS_U32 = H5.J2C( JH5T_MIPS_U32 );
  final public static int H5T_MIPS_U64 = H5.J2C( JH5T_MIPS_U64 );
  final public static int H5T_MIPS_U8 = H5.J2C( JH5T_MIPS_U8 );
  final public static int H5T_NATIVE_B16 = H5.J2C( JH5T_NATIVE_B16 );
  final public static int H5T_NATIVE_B32 = H5.J2C( JH5T_NATIVE_B32 );
  final public static int H5T_NATIVE_B64 = H5.J2C( JH5T_NATIVE_B64 );
  final public static int H5T_NATIVE_B8 = H5.J2C( JH5T_NATIVE_B8 );
  final public static int H5T_NATIVE_CHAR = H5.J2C( JH5T_NATIVE_CHAR );
  final public static int H5T_NATIVE_DOUBLE = H5.J2C( JH5T_NATIVE_DOUBLE );
  final public static int H5T_NATIVE_FLOAT = H5.J2C( JH5T_NATIVE_FLOAT );
  final public static int H5T_NATIVE_HADDR = H5.J2C( JH5T_NATIVE_HADDR );
  final public static int H5T_NATIVE_HBOOL = H5.J2C( JH5T_NATIVE_HBOOL );
  final public static int H5T_NATIVE_HERR = H5.J2C( JH5T_NATIVE_HERR );
  final public static int H5T_NATIVE_HSIZE = H5.J2C( JH5T_NATIVE_HSIZE );
  final public static int H5T_NATIVE_HSSIZE = H5.J2C( JH5T_NATIVE_HSSIZE );
  final public static int H5T_NATIVE_INT = H5.J2C( JH5T_NATIVE_INT );
  final public static int H5T_NATIVE_INT_FAST16 = H5.J2C( JH5T_NATIVE_INT_FAST16 );
  final public static int H5T_NATIVE_INT_FAST32 = H5.J2C( JH5T_NATIVE_INT_FAST32 );
  final public static int H5T_NATIVE_INT_FAST64 = H5.J2C( JH5T_NATIVE_INT_FAST64 );
  final public static int H5T_NATIVE_INT_FAST8 = H5.J2C( JH5T_NATIVE_INT_FAST8 );
  final public static int H5T_NATIVE_INT_LEAST16 = H5.J2C( JH5T_NATIVE_INT_LEAST16 );
  final public static int H5T_NATIVE_INT_LEAST32 = H5.J2C( JH5T_NATIVE_INT_LEAST32 );
  final public static int H5T_NATIVE_INT_LEAST64 = H5.J2C( JH5T_NATIVE_INT_LEAST64 );
  final public static int H5T_NATIVE_INT_LEAST8 = H5.J2C( JH5T_NATIVE_INT_LEAST8 );
  final public static int H5T_NATIVE_INT16 = H5.J2C( JH5T_NATIVE_INT16 );
  final public static int H5T_NATIVE_INT32 = H5.J2C( JH5T_NATIVE_INT32 );
  final public static int H5T_NATIVE_INT64 = H5.J2C( JH5T_NATIVE_INT64 );
  final public static int H5T_NATIVE_INT8 = H5.J2C( JH5T_NATIVE_INT8 );
  final public static int H5T_NATIVE_LDOUBLE = H5.J2C( JH5T_NATIVE_LDOUBLE );
  final public static int H5T_NATIVE_LLONG = H5.J2C( JH5T_NATIVE_LLONG );
  final public static int H5T_NATIVE_LONG = H5.J2C( JH5T_NATIVE_LONG );
  final public static int H5T_NATIVE_OPAQUE = H5.J2C( JH5T_NATIVE_OPAQUE );
  final public static int H5T_NATIVE_SCHAR = H5.J2C( JH5T_NATIVE_SCHAR );
  final public static int H5T_NATIVE_SHORT = H5.J2C( JH5T_NATIVE_SHORT );
  final public static int H5T_NATIVE_UCHAR = H5.J2C( JH5T_NATIVE_UCHAR );
  final public static int H5T_NATIVE_UINT = H5.J2C( JH5T_NATIVE_UINT );
  final public static int H5T_NATIVE_UINT_FAST16 = H5.J2C( JH5T_NATIVE_UINT_FAST16 );
  final public static int H5T_NATIVE_UINT_FAST32 = H5.J2C( JH5T_NATIVE_UINT_FAST32 );
  final public static int H5T_NATIVE_UINT_FAST64 = H5.J2C( JH5T_NATIVE_UINT_FAST64 );
  final public static int H5T_NATIVE_UINT_FAST8 = H5.J2C( JH5T_NATIVE_UINT_FAST8 );
  final public static int H5T_NATIVE_UINT_LEAST16 = H5.J2C( JH5T_NATIVE_UINT_LEAST16 );
  final public static int H5T_NATIVE_UINT_LEAST32 = H5.J2C( JH5T_NATIVE_UINT_LEAST32 );
  final public static int H5T_NATIVE_UINT_LEAST64 = H5.J2C( JH5T_NATIVE_UINT_LEAST64 );
  final public static int H5T_NATIVE_UINT_LEAST8 = H5.J2C( JH5T_NATIVE_UINT_LEAST8 );
  final public static int H5T_NATIVE_UINT16 = H5.J2C( JH5T_NATIVE_UINT16 );
  final public static int H5T_NATIVE_UINT32 = H5.J2C( JH5T_NATIVE_UINT32 );
  final public static int H5T_NATIVE_UINT64 = H5.J2C( JH5T_NATIVE_UINT64 );
  final public static int H5T_NATIVE_UINT8 = H5.J2C( JH5T_NATIVE_UINT8 );
  final public static int H5T_NATIVE_ULLONG = H5.J2C( JH5T_NATIVE_ULLONG );
  final public static int H5T_NATIVE_ULONG = H5.J2C( JH5T_NATIVE_ULONG );
  final public static int H5T_NATIVE_USHORT = H5.J2C( JH5T_NATIVE_USHORT );
  final public static int H5T_NCLASSES = H5.J2C( JH5T_NCLASSES );
  final public static int H5T_NO_CLASS = H5.J2C( JH5T_NO_CLASS );
  final public static int H5T_NORM_ERROR = H5.J2C( JH5T_NORM_ERROR );
  final public static int H5T_NORM_IMPLIED = H5.J2C( JH5T_NORM_IMPLIED );
  final public static int H5T_NORM_MSBSET = H5.J2C( JH5T_NORM_MSBSET );
  final public static int H5T_NORM_NONE = H5.J2C( JH5T_NORM_NONE );
  final public static int H5T_NPAD = H5.J2C( JH5T_NPAD );
  final public static int H5T_NSGN = H5.J2C( JH5T_NSGN );
  final public static int H5T_OPAQUE = H5.J2C( JH5T_OPAQUE );
  final public static int H5T_OPAQUE_TAG_MAX  = H5.J2C( JH5T_OPAQUE_TAG_MAX  ); /* 1.6.5 */
  final public static int H5T_ORDER_BE = H5.J2C( JH5T_ORDER_BE );
  final public static int H5T_ORDER_ERROR = H5.J2C( JH5T_ORDER_ERROR );
  final public static int H5T_ORDER_LE = H5.J2C( JH5T_ORDER_LE );
  final public static int H5T_ORDER_NONE = H5.J2C( JH5T_ORDER_NONE );
  final public static int H5T_ORDER_VAX = H5.J2C( JH5T_ORDER_VAX );
  final public static int H5T_PAD_BACKGROUND = H5.J2C( JH5T_PAD_BACKGROUND );
  final public static int H5T_PAD_ERROR = H5.J2C( JH5T_PAD_ERROR );
  final public static int H5T_PAD_ONE = H5.J2C( JH5T_PAD_ONE );
  final public static int H5T_PAD_ZERO = H5.J2C( JH5T_PAD_ZERO );
  final public static int H5T_PERS_DONTCARE = H5.J2C( JH5T_PERS_DONTCARE );
  final public static int H5T_PERS_HARD = H5.J2C( JH5T_PERS_HARD );
  final public static int H5T_PERS_SOFT = H5.J2C( JH5T_PERS_SOFT );
  final public static int H5T_REFERENCE = H5.J2C( JH5T_REFERENCE );
  final public static int H5T_SGN_2 = H5.J2C( JH5T_SGN_2 );
  final public static int H5T_SGN_ERROR = H5.J2C( JH5T_SGN_ERROR );
  final public static int H5T_SGN_NONE = H5.J2C( JH5T_SGN_NONE );
  final public static int H5T_STD_B16BE = H5.J2C( JH5T_STD_B16BE );
  final public static int H5T_STD_B16LE = H5.J2C( JH5T_STD_B16LE );
  final public static int H5T_STD_B32BE = H5.J2C( JH5T_STD_B32BE );
  final public static int H5T_STD_B32LE = H5.J2C( JH5T_STD_B32LE );
  final public static int H5T_STD_B64BE = H5.J2C( JH5T_STD_B64BE );
  final public static int H5T_STD_B64LE = H5.J2C( JH5T_STD_B64LE );
  final public static int H5T_STD_B8BE = H5.J2C( JH5T_STD_B8BE );
  final public static int H5T_STD_B8LE = H5.J2C( JH5T_STD_B8LE );
  final public static int H5T_STD_I16BE = H5.J2C( JH5T_STD_I16BE );
  final public static int H5T_STD_I16LE = H5.J2C( JH5T_STD_I16LE );
  final public static int H5T_STD_I32BE = H5.J2C( JH5T_STD_I32BE );
  final public static int H5T_STD_I32LE = H5.J2C( JH5T_STD_I32LE );
  final public static int H5T_STD_I64BE = H5.J2C( JH5T_STD_I64BE );
  final public static int H5T_STD_I64LE = H5.J2C( JH5T_STD_I64LE );
  final public static int H5T_STD_I8BE = H5.J2C( JH5T_STD_I8BE );
  final public static int H5T_STD_I8LE = H5.J2C( JH5T_STD_I8LE );
  final public static int H5T_STD_REF_DSETREG = H5.J2C( JH5T_STD_REF_DSETREG );
  final public static int H5T_STD_REF_OBJ = H5.J2C( JH5T_STD_REF_OBJ );
  final public static int H5T_STD_U16BE = H5.J2C( JH5T_STD_U16BE );
  final public static int H5T_STD_U16LE = H5.J2C( JH5T_STD_U16LE );
  final public static int H5T_STD_U32BE = H5.J2C( JH5T_STD_U32BE );
  final public static int H5T_STD_U32LE = H5.J2C( JH5T_STD_U32LE );
  final public static int H5T_STD_U64BE = H5.J2C( JH5T_STD_U64BE );
  final public static int H5T_STD_U64LE = H5.J2C( JH5T_STD_U64LE );
  final public static int H5T_STD_U8BE = H5.J2C( JH5T_STD_U8BE );
  final public static int H5T_STD_U8LE = H5.J2C( JH5T_STD_U8LE );
  final public static int H5T_STR_ERROR = H5.J2C( JH5T_STR_ERROR );
  final public static int H5T_STR_NULLPAD = H5.J2C( JH5T_STR_NULLPAD );
  final public static int H5T_STR_NULLTERM = H5.J2C( JH5T_STR_NULLTERM );
  final public static int H5T_STR_RESERVED_10 = H5.J2C( JH5T_STR_RESERVED_10 );
  final public static int H5T_STR_RESERVED_11 = H5.J2C( JH5T_STR_RESERVED_11 );
  final public static int H5T_STR_RESERVED_12 = H5.J2C( JH5T_STR_RESERVED_12 );
  final public static int H5T_STR_RESERVED_13 = H5.J2C( JH5T_STR_RESERVED_13 );
  final public static int H5T_STR_RESERVED_14 = H5.J2C( JH5T_STR_RESERVED_14 );
  final public static int H5T_STR_RESERVED_15 = H5.J2C( JH5T_STR_RESERVED_15 );
  final public static int H5T_STR_RESERVED_3 = H5.J2C( JH5T_STR_RESERVED_3 );
  final public static int H5T_STR_RESERVED_4 = H5.J2C( JH5T_STR_RESERVED_4 );
  final public static int H5T_STR_RESERVED_5 = H5.J2C( JH5T_STR_RESERVED_5 );
  final public static int H5T_STR_RESERVED_6 = H5.J2C( JH5T_STR_RESERVED_6 );
  final public static int H5T_STR_RESERVED_7 = H5.J2C( JH5T_STR_RESERVED_7 );
  final public static int H5T_STR_RESERVED_8 = H5.J2C( JH5T_STR_RESERVED_8 );
  final public static int H5T_STR_RESERVED_9 = H5.J2C( JH5T_STR_RESERVED_9 );
  final public static int H5T_STR_SPACEPAD = H5.J2C( JH5T_STR_SPACEPAD );
  final public static int H5T_STRING = H5.J2C( JH5T_STRING );
  final public static int H5T_TIME = H5.J2C( JH5T_TIME );
  final public static int H5T_UNIX_D32BE = H5.J2C( JH5T_UNIX_D32BE );
  final public static int H5T_UNIX_D32LE = H5.J2C( JH5T_UNIX_D32LE );
  final public static int H5T_UNIX_D64BE = H5.J2C( JH5T_UNIX_D64BE );
  final public static int H5T_UNIX_D64LE = H5.J2C( JH5T_UNIX_D64LE );
  final public static int H5T_VARIABLE = H5.J2C( JH5T_VARIABLE );     // Rosetta Biosoftware 
  final public static int H5T_VLEN = H5.J2C( JH5T_VLEN );
  
  /**
   * Get the value of a constant variable from the library. Some of the
   * library constants are defined at run-time. Using get() will make sure
   * the constants are correct. 
   */
  private synchronized static native int get(String constantName);
  

}
