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

public class H5Pconstant {
  final private static int JH5P_DATASET_CREATE = 2820;
  final private static int JH5P_DATASET_CREATE_DEFAULT = 2830;
  final private static int JH5P_DATASET_XFER = 2840;
  final private static int JH5P_DATASET_XFER_DEFAULT = 2850;
  final private static int JH5P_DEFAULT = 2860;
  final private static int JH5P_FILE_ACCESS = 2870;
  final private static int JH5P_FILE_ACCESS_DEFAULT = 2880;
  final private static int JH5P_FILE_CREATE = 2890;
  final private static int JH5P_FILE_CREATE_DEFAULT = 2900;
  final private static int JH5P_MOUNT = 2910;
  final private static int JH5P_MOUNT_DEFAULT = 2920;
  final private static int JH5P_NO_CLASS = 2930;
  final private static int JH5P_NO_CLASS_DEFAULT = 2940;

  ///////////////////////////////////////////////////////////////////////////
  //                Get the HDF5 constants from the library                //
  ///////////////////////////////////////////////////////////////////////////
  final public static int H5P_DATASET_CREATE = H5.J2C( JH5P_DATASET_CREATE );
  final public static int H5P_DATASET_CREATE_DEFAULT = H5.J2C( JH5P_DATASET_CREATE_DEFAULT );
  final public static int H5P_DATASET_XFER = H5.J2C( JH5P_DATASET_XFER );
  final public static int H5P_DATASET_XFER_DEFAULT = H5.J2C( JH5P_DATASET_XFER_DEFAULT );
  final public static int H5P_DEFAULT = H5.J2C( JH5P_DEFAULT );
  final public static int H5P_FILE_ACCESS = H5.J2C( JH5P_FILE_ACCESS );
  final public static int H5P_FILE_ACCESS_DEFAULT = H5.J2C( JH5P_FILE_ACCESS_DEFAULT );
  final public static int H5P_FILE_CREATE = H5.J2C( JH5P_FILE_CREATE );
  final public static int H5P_FILE_CREATE_DEFAULT = H5.J2C( JH5P_FILE_CREATE_DEFAULT );
  final public static int H5P_MOUNT = H5.J2C( JH5P_MOUNT );
  final public static int H5P_MOUNT_DEFAULT = H5.J2C( JH5P_MOUNT_DEFAULT );
  final public static int H5P_NO_CLASS = H5.J2C( JH5P_NO_CLASS );
  final public static int H5P_NO_CLASS_DEFAULT = H5.J2C( JH5P_NO_CLASS_DEFAULT );

}
