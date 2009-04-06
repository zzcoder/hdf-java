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

#include <stdlib.h>
#include <jni.h>
#include "hdf5.h"
#include "h5jni.h"
#include "H5F.h"

	/*
	 * Class:     hdf_h5_H5F
	 * Method:    H5Fis_hdf5
	 * Signature: (Ljava/lang/String;)Z
	 */
	JNIEXPORT jboolean JNICALL Java_hdf_h5_H5F_H5Fis_1hdf5
	  (JNIEnv *env, jclass cls, jstring filename)
	{
	    htri_t ret_val = 0;
	    char * file;
	    jboolean isCopy;

	    if (filename == NULL) {
	        h5nullArgument( env, "H5Fis_hdf5:  name is NULL");
	        return JNI_FALSE;
	    }

	    file = (char *)ENVPTR->GetStringUTFChars(ENVPAR filename,&isCopy);
	    if (file == NULL) {
	        h5JNIFatalError( env, "H5Fis_hdf5:  file name is not pinned");
	        return JNI_FALSE;
	    }

	    ret_val = H5Fis_hdf5(file);

	    ENVPTR->ReleaseStringUTFChars(ENVPAR filename,file);

	    if (ret_val < 0)
	    	h5libraryError(env);

	    if (ret_val > 0)
	        return JNI_TRUE;
	    else
	        return JNI_FALSE;
	}

	/*
	 * Class:     hdf_h5_H5F
	 * Method:    H5Fcreate
	 * Signature: (Ljava/lang/String;III)I
	 */
	JNIEXPORT jint JNICALL Java_hdf_h5_H5F_H5Fcreate
	  (JNIEnv *env, jclass cls, jstring filename, jint flags, jint create_id,
			  jint access_id)
	{
	    hid_t ret_val;
	    char* file;
	    jboolean isCopy;

	    if (filename == NULL) {
	        h5nullArgument( env, "H5Fcreate:  filename is NULL");
	        return -1;
	    }

	    file = (char *)ENVPTR->GetStringUTFChars(ENVPAR filename,&isCopy);
	    if (file == NULL) {
	        h5JNIFatalError( env, "H5Fcreate:  file filename is not pinned");
	        return -1;
	    }

	    ret_val = H5Fcreate(file, flags, create_id, access_id);

	    ENVPTR->ReleaseStringUTFChars(ENVPAR filename,file);
	    if (ret_val < 0) {
	        h5libraryError(env);
	    }

	    return (jint)ret_val;
	}

	/*
	 * Class:     hdf_h5_H5F
	 * Method:    H5Fclose
	 * Signature: (I)V
	 */
	JNIEXPORT void JNICALL Java_hdf_h5_H5F_H5Fclose
	  (JNIEnv *env, jclass cls, jint file_id)
	{
	    herr_t ret_val = 0;

	    if (file_id > 0)
	        ret_val = H5Fclose((hid_t) file_id );

	    if (ret_val < 0) {
	        h5libraryError(env);
	    }
	}

	/*
	 * Class:     hdf_h5_H5F
	 * Method:    H5Fopen
	 * Signature: (Ljava/lang/String;II)I
	 */
	JNIEXPORT jint JNICALL Java_hdf_h5_H5F_H5Fopen
	  (JNIEnv *env, jclass cls, jstring filename, jint flags, jint access_id)
	{
	    hid_t ret_val;
	    char* file;
	    jboolean isCopy;

	    if (filename == NULL) {
	        h5nullArgument( env, "H5Fopen: file name is NULL");
	        return -1;
	    }

	    file = (char *)ENVPTR->GetStringUTFChars(ENVPAR filename,&isCopy);
	    if (file == NULL) {
	        h5JNIFatalError( env, "H5Fopen: file name not pinned");
	        return -1;
	    }

	    ret_val = H5Fopen(file, (unsigned) flags, (hid_t) access_id );

	    ENVPTR->ReleaseStringUTFChars(ENVPAR filename,file);

	    if (ret_val < 0) {
	        h5libraryError(env);
	    }

	    return (jint)ret_val;
	}

	/*
	 * Class:     hdf_h5_H5F
	 * Method:    H5Freopen
	 * Signature: (I)I
	 */
	JNIEXPORT jint JNICALL Java_hdf_h5_H5F_H5Freopen
	  (JNIEnv *env, jclass cls, jint file_id)
	{
	    herr_t ret_val = -1;
	    ret_val =  H5Freopen((hid_t)file_id);
	    if (ret_val < 0) {
	        /* throw exception */
	        h5libraryError(env);
	    }
	    return (jint)ret_val;
	}

	/*
	 * Class:     hdf_h5_H5F
	 * Method:    H5Fflush
	 * Signature: (ILhdf/h5/enums/H5F_SCOPE;)V
	 */
	JNIEXPORT void JNICALL Java_hdf_h5_H5F_H5Fflush
	  (JNIEnv *env, jclass cls, jint object_id, jobject scope)
	{
	    herr_t ret_val = -1;
	    jint enum_val = H5F_SCOPE_LOCAL;

	    GET_ENUM_VALUE(scope, enum_val);

	    ret_val =  H5Fflush((hid_t) object_id, (H5F_scope_t) enum_val);

	    if (ret_val < 0) {
	        h5libraryError(env);
	    }
	}

	/*
	 * Class:     hdf_h5_H5F
	 * Method:    H5Fget_name
	 * Signature: (I)Ljava/lang/String;
	 */
	JNIEXPORT jstring JNICALL Java_hdf_h5_H5F_H5Fget_1name
	  (JNIEnv *env, jclass cls, jint file_id)
	{
	    char *namePtr;
	    jstring str;
	    ssize_t buf_size;

	    /* get the length of the name */
	    buf_size = H5Fget_name(file_id, NULL, 0);

	    if (buf_size <= 0) {
	        h5badArgument( env, "H5Fget_name:  buf_size <= 0");
	        return NULL;
	    }

	    buf_size++; /* add extra space for the null terminator */
	    namePtr = (char*)malloc(sizeof(char)*buf_size);
	    if (namePtr == NULL) {
	        h5outOfMemory( env, "H5Fget_name:  malloc failed");
	        return NULL;
	    }

	    buf_size = H5Fget_name ((hid_t) file_id, (char *)namePtr, (size_t)buf_size);

	    if (buf_size < 0) {
	        free(namePtr);
	        h5libraryError(env);
	        return NULL;
	    }

	    str = ENVPTR->NewStringUTF(ENVPAR namePtr);
	    free(namePtr);

	    return str;
	}


#ifdef __cplusplus
}
#endif
