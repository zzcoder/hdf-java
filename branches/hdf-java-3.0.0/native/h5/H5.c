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

/*
 *  This code is the C-interface called by Java programs to access the
 *  general library functions of the HDF5 library.
 *
 *  Each routine wraps a single HDF entry point, generally with the
 *  analogous arguments and return codes.
 *
 *  For details of the HDF libraries, see the HDF Documentation at:
 *   http://www.hdfgroup.org/HDF5/doc/
 *
 */

#include <jni.h>
#include "hdf5.h"
#include "h5jni.h"
#include "H5.h"


	/*
	 * Class:     hdf_h5_H5
	 * Method:    H5open
	 * Signature: ()V
	 */
	JNIEXPORT void JNICALL Java_hdf_h5_H5_H5open
	  (JNIEnv *env, jclass cls)
	{
	    herr_t retVal = -1;
	    retVal =  H5open();
	    if (retVal < 0) {
	        h5libraryError(env);
	    }
	}

	/*
	 * Class:     hdf_h5_H5
	 * Method:    H5dont_atexit
	 * Signature: ()V
	 */
	JNIEXPORT void JNICALL Java_hdf_h5_H5_H5dont_1atexit
	  (JNIEnv *env, jclass cls)
	{
	    int retVal = H5dont_atexit();
	    if (retVal < 0) {
	        h5libraryError(env);
	    }
	}

	/*
	 * Class:     hdf_h5_H5
	 * Method:    H5check_version
	 * Signature: (III)V
	 */
	JNIEXPORT void JNICALL Java_hdf_h5_H5_H5check_1version
	  (JNIEnv *env, jclass cls, jint majnum, jint minnum, jint relnum)
	{
	    int retVal = H5check_version((unsigned)majnum, (unsigned)minnum, (unsigned)relnum);

	    if (retVal < 0) {
	        h5libraryError(env);
	    }
	}

#ifdef __cplusplus
}
#endif
