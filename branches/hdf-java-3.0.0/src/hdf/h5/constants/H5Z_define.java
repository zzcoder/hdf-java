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

public class H5Z_define {
  public static final int H5Z_SO_INT_MINBITS_DEFAULT = 0;
  
  // Special parameters for szip compression
  public static final int H5_SZIP_ALLOW_K13_OPTION_MASK  = 1;
  public static final int H5_SZIP_CHIP_OPTION_MASK       = 2;
  public static final int H5_SZIP_EC_OPTION_MASK         = 4;
  public static final int H5_SZIP_NN_OPTION_MASK         = 32;
  public static final int H5_SZIP_MAX_PIXELS_PER_BLOCK   = 32;

  // for the shuffle filter
  public static final int H5Z_SHUFFLE_USER_NPARMS   = 0; // Number of parameters that users can set
  public static final int H5Z_SHUFFLE_TOTAL_NPARMS  = 1; // Total number of parameters for filter

  // for the szip filter
  public static final int H5Z_SZIP_USER_NPARMS   = 2;    // Number of parameters that users can set
  public static final int H5Z_SZIP_TOTAL_NPARMS  = 4;    // Total number of parameters for filter
  public static final int H5Z_SZIP_PARM_MASK     = 0;    // "User" parameter for option mask
  public static final int H5Z_SZIP_PARM_PPB      = 1;    // "User" parameter for pixels-per-block
  public static final int H5Z_SZIP_PARM_BPP      = 2;    // "Local" parameter for bits-per-pixel
  public static final int H5Z_SZIP_PARM_PPS      = 3;    // "Local" parameter for pixels-per-scanline

  // for the nbit filter
  public static final int H5Z_NBIT_USER_NPARMS   = 0;    // Number of parameters that users can set

  // for the scale offset filter
  public static final int H5Z_SCALEOFFSET_USER_NPARMS = 2;  // Number of parameters that users can set

  // General
  public static final int H5Z_FILTER_ALL   = 0;   // Symbol to remove all filters in H5Premove_filter
  public static final int H5Z_MAX_NFILTERS = 32;  // Maximum number of filters allowed in a pipeline
                                                  // (should probably be allowed to be an
                                                  // unlimited amount, but currently each
                                                  // filter uses a bit in a 32-bit field,
                                                  // so the format would have to be
                                                  // changed to accomodate that)

  // Flags for filter definition (stored)
  public static final int H5Z_FLAG_DEFMASK   = 0x00ff;  // definition flag mask
  public static final int H5Z_FLAG_MANDATORY = 0x0000;  // filter is mandatory
  public static final int H5Z_FLAG_OPTIONAL  = 0x0001;  // filter is optional

  // Additional flags for filter invocation (not stored)
  public static final int H5Z_FLAG_INVMASK  = 0xff00;  // invocation flag mask
  public static final int H5Z_FLAG_REVERSE  = 0x0100;  // reverse direction; read
  public static final int H5Z_FLAG_SKIP_EDC = 0x0200;  // skip EDC filters for read

  // Current version of the H5Z_class_t struct
  public static final int H5Z_CLASS_T_VERS = (1);

  // Bit flags for H5Zget_filter_info
  public static final int H5Z_FILTER_CONFIG_ENCODE_ENABLED = (0x0001);
  public static final int H5Z_FILTER_CONFIG_DECODE_ENABLED = (0x0002);

}
