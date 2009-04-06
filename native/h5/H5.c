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
	    herr_t ret_val = -1;
	    ret_val =  H5open();
	    if (ret_val < 0) {
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
	    int ret_val = H5dont_atexit();
	    if (ret_val < 0) {
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
	    int ret_val = H5check_version((unsigned)majnum, (unsigned)minnum, (unsigned)relnum);

	    if (ret_val < 0) {
	        h5libraryError(env);
	    }
	}

	/*
	 * Class:     hdf_h5_H5
	 * Method:    H5close
	 * Signature: ()V
	 */
	JNIEXPORT void JNICALL Java_hdf_h5_H5_H5close
	  (JNIEnv *env, jclass cls)
	{
	    herr_t ret_val = -1;
	    ret_val =  H5close();
	    if (ret_val < 0) {
	        h5libraryError(env);
	    }
	}

	/*
	 * Class:     hdf_h5_H5
	 * Method:    H5garbage_collect
	 * Signature: ()V
	 */
	JNIEXPORT void JNICALL Java_hdf_h5_H5_H5garbage_1collect
	  (JNIEnv *env, jclass cls)
	{
	    herr_t ret_val = -1;
	    ret_val =  H5garbage_collect();
	    if (ret_val < 0) {
	        h5libraryError(env);
	    }
	}

	/*
	 * Class:     hdf_h5_H5
	 * Method:    H5set_free_list_limits
	 * Signature: (IIIIII)V
	 */
	JNIEXPORT void JNICALL Java_hdf_h5_H5_H5set_1free_1list_1limits
	  (JNIEnv *env, jclass cls, jint reg_global_lim, jint reg_list_lim,
			  jint arr_global_lim, jint arr_list_lim, jint blk_global_lim,
			  jint blk_list_lim )
    {
	    int ret_val = H5set_free_list_limits((int)reg_global_lim, (int)reg_list_lim,
	        (int)arr_global_lim, (int)arr_list_lim, (int)blk_global_lim, (int)blk_list_lim);
	    if (ret_val < 0) {
	        h5libraryError(env);
	    }
	}

	/*
	 * Class:     hdf_h5_H5
	 * Method:    H5get_libversion
	 * Signature: ([I)V
	 */
	JNIEXPORT void JNICALL Java_hdf_h5_H5_H5get_1libversion
	  (JNIEnv *env, jclass cls, jintArray libversion)
	{
	    unsigned *theArray = NULL;
	    jboolean isCopy;
	    int ret_val;

	    if (libversion == NULL) {
	        h5nullArgument( env, "H5get_version:  libversion is NULL");
	    }
	    theArray = (unsigned *)ENVPTR->GetIntArrayElements(ENVPAR libversion,&isCopy);
	    if (theArray == NULL) {
	        h5JNIFatalError( env, "H5get_libversion:  input not pinned");
	    }

	    ret_val =  H5get_libversion(&(theArray[0]), &(theArray[1]), &(theArray[2]));

	    if (ret_val < 0) {
	        ENVPTR->ReleaseIntArrayElements(ENVPAR libversion,(jint *)theArray,JNI_ABORT);
	        h5libraryError(env);
	    } else {
	        ENVPTR->ReleaseIntArrayElements(ENVPAR libversion,(jint *)theArray,0);
	    }
	}


#ifdef __cplusplus
}
#endif