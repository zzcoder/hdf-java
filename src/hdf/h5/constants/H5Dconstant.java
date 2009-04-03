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
public class H5Dconstant {
    ///////////////////////////////////////////////////////////////////////////
    //           The values are used in the C JNI header file.               //
    //      DO NOT CHANGE THE VALUES UNLESS YOU KNOW WHAT YOU DO !!!         //
    ///////////////////////////////////////////////////////////////////////////

    final private static int JH5D_ALLOC_TIME_DEFAULT = 10;
    final private static int JH5D_ALLOC_TIME_EARLY = 20;
    final private static int JH5D_ALLOC_TIME_ERROR = 30;
    final private static int JH5D_ALLOC_TIME_INCR = 40;
    final private static int JH5D_ALLOC_TIME_LATE = 50;
    final private static int JH5D_CHUNKED = 60;
    final private static int JH5D_COMPACT = 70;
    final private static int JH5D_CONTIGUOUS = 80;
    final private static int JH5D_FILL_TIME_ALLOC = 90;
    final private static int JH5D_FILL_TIME_ERROR = 100;
    final private static int JH5D_FILL_TIME_NEVER = 110;
    final private static int JH5D_FILL_VALUE_DEFAULT = 120;
    final private static int JH5D_FILL_VALUE_ERROR = 130;
    final private static int JH5D_FILL_VALUE_UNDEFINED = 140;
    final private static int JH5D_FILL_VALUE_USER_DEFINED = 150;
    final private static int JH5D_LAYOUT_ERROR = 160;
    final private static int JH5D_NLAYOUTS = 170;
    final private static int JH5D_SPACE_STATUS_ALLOCATED = 180;
    final private static int JH5D_SPACE_STATUS_ERROR = 190;
    final private static int JH5D_SPACE_STATUS_NOT_ALLOCATED = 200;
    final private static int JH5D_SPACE_STATUS_PART_ALLOCATED = 210;

    ///////////////////////////////////////////////////////////////////////////
    //                Get the HDF5 constants from the library                //
    ///////////////////////////////////////////////////////////////////////////
    final public static int H5D_ALLOC_TIME_DEFAULT = J2C( JH5D_ALLOC_TIME_DEFAULT );
    final public static int H5D_ALLOC_TIME_EARLY = J2C( JH5D_ALLOC_TIME_EARLY );
    final public static int H5D_ALLOC_TIME_ERROR = J2C( JH5D_ALLOC_TIME_ERROR );
    final public static int H5D_ALLOC_TIME_INCR = J2C( JH5D_ALLOC_TIME_INCR );
    final public static int H5D_ALLOC_TIME_LATE = J2C( JH5D_ALLOC_TIME_LATE );
    final public static int H5D_CHUNKED = J2C( JH5D_CHUNKED );
    final public static int H5D_COMPACT = J2C( JH5D_COMPACT );
    final public static int H5D_CONTIGUOUS = J2C( JH5D_CONTIGUOUS );
    final public static int H5D_FILL_TIME_ALLOC = J2C( JH5D_FILL_TIME_ALLOC );
    final public static int H5D_FILL_TIME_ERROR = J2C( JH5D_FILL_TIME_ERROR );
    final public static int H5D_FILL_TIME_NEVER = J2C( JH5D_FILL_TIME_NEVER );
    final public static int H5D_FILL_VALUE_DEFAULT = J2C( JH5D_FILL_VALUE_DEFAULT );
    final public static int H5D_FILL_VALUE_ERROR = J2C( JH5D_FILL_VALUE_ERROR );
    final public static int H5D_FILL_VALUE_UNDEFINED = J2C( JH5D_FILL_VALUE_UNDEFINED );
    final public static int H5D_FILL_VALUE_USER_DEFINED = J2C( JH5D_FILL_VALUE_USER_DEFINED );
    final public static int H5D_LAYOUT_ERROR = J2C( JH5D_LAYOUT_ERROR );
    final public static int H5D_NLAYOUTS = J2C( JH5D_NLAYOUTS );
    final public static int H5D_SPACE_STATUS_ALLOCATED = J2C( JH5D_SPACE_STATUS_ALLOCATED );
    final public static int H5D_SPACE_STATUS_ERROR = J2C( JH5D_SPACE_STATUS_ERROR );
    final public static int H5D_SPACE_STATUS_NOT_ALLOCATED = J2C( JH5D_SPACE_STATUS_NOT_ALLOCATED );
    final public static int H5D_SPACE_STATUS_PART_ALLOCATED = J2C( JH5D_SPACE_STATUS_PART_ALLOCATED );

    /**
     *  J2C converts a Java constant to an HDF5 constant determined at runtime
     *
     *  @param java_constant IN: The value of Java constant
     *  
     *  @return the value of an HDF5 constant determined at runtime
     **/
    private synchronized static native int J2C(int java_constant);

}
