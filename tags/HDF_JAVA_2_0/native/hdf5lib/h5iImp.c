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
 *  Identifier API Functions of the HDF5 library. 
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

extern jboolean h5libraryError( JNIEnv *env );

/*
 * Class:     ncsa_hdf_hdf5lib_H5Header
 * Method:    H5Gget_linkval
 * Signature: (ILjava/lang/String;I[Ljava/lang/String;)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Iget_1type
  (JNIEnv *env, jclass clss, jint obj_id)
{
	H5I_type_t retVal = H5I_BADID;
	retVal =  H5Iget_type((hid_t)obj_id);
	if (retVal == H5I_BADID) {
		h5libraryError(env);
	}
	return (jint)retVal;
}


/***************************************************************
 *                   New APIs for HDF5.1.6                     *
 ***************************************************************/

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Iget_name(hid_t obj_id, char *name, size_t size ) 
 * Signature: (IJLjava/lang/String;)J
 */
JNIEXPORT jlong JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Iget_1name
  (JNIEnv *env, jclass clss, jint obj_id, jobjectArray name, jlong buf_size)
{
	char *aName;
	jstring str;
	hssize_t size;
	long bs;

	bs = (long)buf_size;
	if (bs <= 0) {
		h5badArgument( env, "H5Iget_name:  buf_size <= 0");
		return -1;
	}
	aName = (char*)malloc(sizeof(char)*bs);
	if (aName == NULL) {
		h5outOfMemory( env, "H5Iget_name:  malloc failed");
		return -1;
	}
	size = H5Iget_name((hid_t)obj_id, aName, (size_t)buf_size);
	if (size < 0) {
		free(aName);
		h5libraryError(env);
		/*  exception, returns immediately */
	}
	/* successful return -- save the string; */
#ifdef __cplusplus
	str = env->NewStringUTF(aName);
#else
	str = (*env)->NewStringUTF(env,aName);
#endif
	if (str == NULL) {
		free(aName);
		h5JNIFatalError( env,"H5Iget_name:  return string failed");
		return -1;
	}
	free(aName);
	/*  Note: throws ArrayIndexOutOfBoundsException, 
		ArrayStoreException */
#ifdef __cplusplus
	env->SetObjectArrayElement(name,0,str);
#else
	(*env)->SetObjectArrayElement(env,name,0,str);
#endif

	return (jlong)size;
}

#ifdef __cplusplus
}
#endif 
