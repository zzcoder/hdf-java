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

/*
 *  This code is the C-interface called by Java programs to access the
 *  Reference API Functions of the HDF5 library. 
 *
 *  Each routine wraps a single HDF entry point, generally with the
 *  analogous arguments and return codes.
 *
 *  For details of the HDF libraries, see the HDF Documentation at:
 *   http://hdf.ncsa.uiuc.edu/HDF5/doc/
 *
 */
#ifdef __cplusplus
extern "C" {
#endif 

#include "hdf5.h"
#include <jni.h>
#include <stdlib.h>

extern jboolean h5JNIFatalError( JNIEnv *env, char *functName);
extern jboolean h5nullArgument( JNIEnv *env, char *functName);
extern jboolean h5badArgument( JNIEnv *env, char *functName);
extern jboolean h5libraryError( JNIEnv *env );

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Zunregister(H5Z_filter_t filter) 
 * Signature: ([BILjava/lang/String;II)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Zunregister
  (JNIEnv *env, jclass clss, 
  jbyteArray ref, jint loc_id, jstring name, jint filter)
{
	herr_t retValue;

	retValue = H5Zunregister((H5Z_filter_t)filter);

	if (retValue < 0) {
		h5libraryError(env);
	}

	return (jint)retValue;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Zfilter_avail(H5Z_filter_t filter) 
 * Signature: ([BILjava/lang/String;II)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Zfilter_1avail
  (JNIEnv *env, jclass clss, 
  jbyteArray ref, jint loc_id, jstring name, jint filter)
{
	herr_t retValue;

	retValue = H5Zfilter_avail((H5Z_filter_t)filter);

	if (retValue < 0) {
		h5libraryError(env);
	}

	return (jint)retValue;
}

#ifdef __cplusplus
}
#endif 
