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
 *  file interface functions of the HDF5 library. 
 *
 *  Each routine wraps a single HDF entry point, generally with the
 *  analogous arguments and return codes.
 *
 *  For details of the HDF libraries, see the HDF Documentation at:
 *    http://hdf.ncsa.uiuc.edu/HDF5/doc/
 *
 */

#ifdef __cplusplus
extern "C" {
#endif 
#include "hdf5.h"

#include <stdio.h>
#include "jni.h"

extern jboolean h5JNIFatalError( JNIEnv *env, char *functName);
extern jboolean h5nullArgument( JNIEnv *env, char *functName);
extern jboolean h5libraryError( JNIEnv *env );

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Fopen
 * Signature: (Ljava/lang/String;II)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Fopen
  (JNIEnv *env, jclass clss, jstring name, jint flags, jint access_id)
{
	hid_t status;
	char* file;
	jboolean isCopy;

	if (name == NULL) {
		/* exception -- bad argument? */
		h5nullArgument( env, "H5Fopen:  name is NULL");
		return -1;
	} 

#ifdef __cplusplus
	file = (char *)env->GetStringUTFChars(name,&isCopy);
#else
	file = (char *)(*env)->GetStringUTFChars(env,name,&isCopy);
#endif
	if (file == NULL) {
		/* exception -- out of memory? */ 
		h5JNIFatalError( env, "H5Fopen:  file name not pinned");
		return -1; 
	}
	status = H5Fopen(file, (unsigned) flags, (hid_t) access_id );

#ifdef __cplusplus
	env->ReleaseStringUTFChars(name,file);
#else
	(*env)->ReleaseStringUTFChars(env,name,file);
#endif
	if (status < 0) {
		/* throw exception */
		h5libraryError(env);
	}
	return (jint)status;


}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Fcreate
 * Signature: (Ljava/lang/String;III)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Fcreate
  (JNIEnv *env, jclass clss, jstring name, jint flags, jint create_id, jint access_id)
{
	hid_t status;
	char* file;
	jboolean isCopy;

	if (name == NULL) {
		/* exception -- bad argument? */
		h5nullArgument( env, "H5Fcreate:  name is NULL");
		return -1;
	} 

#ifdef __cplusplus
	file = (char *)env->GetStringUTFChars(name,&isCopy);
#else
	file = (char *)(*env)->GetStringUTFChars(env,name,&isCopy);
#endif
	if (file == NULL) {
		/* exception -- out of memory? */ 
		h5JNIFatalError( env, "H5Fcreate:  file name is not pinned");
		return -1; 
	}

	status = H5Fcreate(file, flags, create_id, access_id); 

#ifdef __cplusplus
	env->ReleaseStringUTFChars(name,file);
#else
	(*env)->ReleaseStringUTFChars(env,name,file);
#endif
	if (status < 0) {
		/* throw exception */
		h5libraryError(env);
	}
	return (jint)status;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Fflush
 * Signature: (II)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Fflush
  (JNIEnv *env, jclass clss, jint object_id, jint scope)
{
	herr_t retVal = -1;
	retVal =  H5Fflush((hid_t) object_id, (H5F_scope_t) scope ); 
	if (retVal < 0) {
		/* throw exception */
		h5libraryError(env);
	}
	return (jint)retVal;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Fis_hdf5
 * Signature: (Ljava/lang/String;)B
 */
JNIEXPORT jboolean JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Fis_1hdf5
  (JNIEnv *env, jclass clss, jstring name)
{
	htri_t retVal = 0;
	char * file;
	jboolean isCopy;

	if (name == NULL) {
		/* exception -- bad argument? */
		h5nullArgument( env, "H5Fis_hdf5:  name is NULL");
		return JNI_FALSE;
	} 

#ifdef __cplusplus
	file = (char *)env->GetStringUTFChars(name,&isCopy);
#else
	file = (char *)(*env)->GetStringUTFChars(env,name,&isCopy);
#endif
	if (file == NULL) {
		/* exception -- out of memory? */ 
		h5JNIFatalError( env, "H5Fis_hdf5:  file name is not pinned");
		return JNI_FALSE;
	} 
		
	retVal = H5Fis_hdf5(file); 

#ifdef __cplusplus
	env->ReleaseStringUTFChars(name,file);
#else
	(*env)->ReleaseStringUTFChars(env,name,file);
#endif

	if (retVal > 0) {
		return JNI_TRUE;
	} else if (retVal == 0) {
		return JNI_FALSE;
	} else {
		/*  raise exception here -- return value is irrelevant */
		h5libraryError(env);
		return JNI_FALSE;
	}
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Fget_create_plist
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Fget_1create_1plist
  (JNIEnv *env, jclass clss, jint file_id)
{
	hid_t retVal = -1;
	retVal =  H5Fget_create_plist((hid_t) file_id );
	if (retVal < 0) {
		/* throw exception */
		h5libraryError(env);
	}
	return (jint)retVal;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Fget_access_plist
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Fget_1access_1plist
  (JNIEnv *env, jclass clss, jint file_id)
{
	hid_t retVal = -1;
	retVal =  H5Fget_access_plist((hid_t) file_id);
	if (retVal < 0) {
		/* throw exception */
		h5libraryError(env);
	}
	return (jint)retVal;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Fclose
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Fclose
  (JNIEnv *env, jclass clss, jint file_id)
{
	herr_t status = -1;
	status = H5Fclose((hid_t) file_id );
	if (status < 0) {
		h5libraryError(env);
	}
	return (jint)status;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Fmount
 * Signature: (ILjava/lang/String;II)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Fmount
  (JNIEnv *env, jclass clss, jint loc_id, jstring name, jint child_id, jint plist_id)
{
	herr_t status;
	char* file;
	jboolean isCopy;

	if (name == NULL) {
		/* exception -- bad argument? */
		h5nullArgument( env, "H5Fmount:  name is NULL");
		return -1;
	} 

#ifdef __cplusplus
	file = (char *)env->GetStringUTFChars(name,&isCopy);
#else
	file = (char *)(*env)->GetStringUTFChars(env,name,&isCopy);
#endif
	if (file == NULL) {
		/* exception -- out of memory? */ 
		h5JNIFatalError( env, "H5Fmount:  file name is not pinned");
		return -1; 
	}

	status = H5Fmount((hid_t) loc_id, file, (hid_t) child_id, (hid_t) plist_id );
#ifdef __cplusplus
	env->ReleaseStringUTFChars(name,file);
#else
	(*env)->ReleaseStringUTFChars(env,name,file);
#endif
	if (status < 0) {
		/* throw exception */
		h5libraryError(env);
	}
	return (jint)status;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Funmount
 * Signature: (ILjava/lang/String;)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Funmount
  (JNIEnv *env, jclass clss, jint loc_id, jstring name)
{
	herr_t status;
	char* file;
	jboolean isCopy;

	if (name == NULL) {
		/* exception -- bad argument? */
		h5nullArgument( env, "H5Funmount:  name is NULL");
		return -1;
	} 

#ifdef __cplusplus
	file = (char *)env->GetStringUTFChars(name,&isCopy);
#else
	file = (char *)(*env)->GetStringUTFChars(env,name,&isCopy);
#endif
	if (file == NULL) {
		h5JNIFatalError( env, "H5Funmount:  file name is not pinned");
		/* exception -- out of memory? */ 
		return -1; 
	}

	status = H5Funmount((hid_t) loc_id, file ); 

#ifdef __cplusplus
	env->ReleaseStringUTFChars(name,file);
#else
	(*env)->ReleaseStringUTFChars(env,name,file);
#endif
	if (status < 0) {
		/* throw exception */
		h5libraryError(env);
	}
	return (jint)status;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Freopen
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Freopen
  (JNIEnv *env, jclass clss, jint file_id)
{
	herr_t retVal = -1;
	retVal =  H5Freopen((hid_t)file_id);
	if (retVal < 0) {
		/* throw exception */
		h5libraryError(env);
	}
	return (jint)retVal;
}

#ifdef __cplusplus
}
#endif 
