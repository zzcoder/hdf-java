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

public class H5T_define {
	// Variable Length String information
	// Indicate that a string is variable length (null-terminated in C, instead of fixed length)
	public static final long H5T_VARIABLE = (-1);

	// Opaque information
	// Maximum length of an opaque tag
	public static final int H5T_OPAQUE_TAG_MAX  = 256;

//  public static final int H5T_NSTR = H5T_STR_RESERVED_3;   //num H5T_str_t types actually defined

}
