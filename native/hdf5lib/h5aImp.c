/****************************************************************************
 * NCSA HDF                                                                 *
 * National Comptational Science Alliance                                   *
 * University of Illinois at Urbana-Champaign                               *
 * 605 E. Springfield, Champaign IL 61820                                   *
 *                                                                          *
 * For conditions of distribution and use, see the accompanying             *
 * java-hdf5/COPYING file.                                                  *
 *                                                                          *
 ****************************************************************************/

/*
 *  This code is the C-interface called by Java programs to access the
 *  Attribute API Functions of the HDF5 library. 
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

extern jboolean h5outOfMemory( JNIEnv *env, char *functName);
extern jboolean h5JNIFatalError( JNIEnv *env, char *functName);
extern jboolean h5nullArgument( JNIEnv *env, char *functName);
extern jboolean h5badArgument( JNIEnv *env, char *functName);
extern jboolean h5libraryError( JNIEnv *env );

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Acreate
 * Signature: (ILjava/lang/String;III)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Acreate
  (JNIEnv *env, jclass clss, jint loc_id, jstring name, jint type_id, 
  jint space_id, jint create_plist)
{
	herr_t status;
	char* aName;
	jboolean isCopy;

	if (name == NULL) {
		h5nullArgument( env, "H5Acreate:  name is NULL");
		return -1;
	} 
#ifdef __cplusplus
	aName = (char *)env->GetStringUTFChars(name,&isCopy);
#else
	aName = (char *)(*env)->GetStringUTFChars(env,name,&isCopy);
#endif
	if (aName == NULL) {
		h5JNIFatalError( env, "H5Acreate: aName is not pinned");
		return -1;
	}

	status = H5Acreate((hid_t)loc_id, aName, (hid_t)type_id, 
		(hid_t)space_id, (hid_t)create_plist );

#ifdef __cplusplus
	env->ReleaseStringUTFChars(name,aName);
#else
	(*env)->ReleaseStringUTFChars(env,name,aName);
#endif

	if (status < 0) {
		h5libraryError(env);
	}
	return (jint)status;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Aopen_name
 * Signature: (ILjava/lang/String;)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Aopen_1name
  (JNIEnv *env, jclass clss, jint loc_id, jstring name)
{
	herr_t status;
	char* aName;
	jboolean isCopy;

	if (name == NULL) {
		h5nullArgument( env,"H5Aopen_name:  name is NULL");
		return -1;
	} 
#ifdef __cplusplus
	aName = (char *)env->GetStringUTFChars(name,&isCopy);
#else
	aName = (char *)(*env)->GetStringUTFChars(env,name,&isCopy);
#endif
	if (aName == NULL) {
		h5JNIFatalError( env,"H5Aopen_name: name is not pinned");
		return -1;
	}

	status = H5Aopen_name((hid_t)loc_id, aName); 

#ifdef __cplusplus
	env->ReleaseStringUTFChars(name,aName);
#else
	(*env)->ReleaseStringUTFChars(env,name,aName);
#endif

	if (status < 0) {
		h5libraryError(env);
	}
	return (jint)status;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Aopen_idx
 * Signature: (II)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Aopen_1idx
  (JNIEnv *env, jclass clss, jint loc_id, jint idx)
{
	herr_t retVal = -1;
	retVal =  H5Aopen_idx((hid_t)loc_id, (unsigned int) idx ); 
	if (retVal < 0) {
		h5libraryError(env);
	}
	return (jint)retVal;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Awrite
 * Signature: (II[B)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Awrite
  (JNIEnv *env, jclass clss, jint attr_id, jint mem_type_id, jbyteArray buf)
{
	herr_t status;
	jbyte *byteP;
	jboolean isCopy;

	if (buf == NULL) {
		h5nullArgument( env,"H5Awrite:  buf is NULL");
		return -1;
	} 
#ifdef __cplusplus
	byteP = env->GetByteArrayElements(buf,&isCopy);
#else
	byteP = (*env)->GetByteArrayElements(env,buf,&isCopy);
#endif
	if (byteP == NULL) {
		h5JNIFatalError( env,"H5Awrite: buf is not pinned");
		return -1;
	}
	status = H5Awrite((hid_t)attr_id, (hid_t)mem_type_id, byteP);
#ifdef __cplusplus
	env->ReleaseByteArrayElements(buf,byteP,JNI_ABORT);
#else
	(*env)->ReleaseByteArrayElements(env,buf,byteP,JNI_ABORT);
#endif

	if (status < 0) {
		h5libraryError(env);
	}
	return (jint)status;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Aread
 * Signature: (II[B)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Aread
  (JNIEnv *env, jclass clss, jint attr_id, jint mem_type_id, jbyteArray buf)
{
	herr_t status;
	jbyte *byteP;
	jboolean isCopy;

	if (buf == NULL) {
		h5nullArgument( env,"H5Aread:  buf is NULL");
		return -1;
	} 
#ifdef __cplusplus
	byteP = env->GetByteArrayElements(buf,&isCopy);
#else
	byteP = (*env)->GetByteArrayElements(env,buf,&isCopy);
#endif
	if (byteP == NULL) {
		h5JNIFatalError( env,"H5Aread: buf is not pinned");
		return -1;
	}

	status = H5Aread((hid_t)attr_id, (hid_t)mem_type_id, byteP);
 
	if (status < 0) {
#ifdef __cplusplus
		env->ReleaseByteArrayElements(buf,byteP,JNI_ABORT);
#else
		(*env)->ReleaseByteArrayElements(env,buf,byteP,JNI_ABORT);
#endif
		h5libraryError(env);
	} else  {
#ifdef __cplusplus
		env->ReleaseByteArrayElements(buf,byteP,0);
#else
		(*env)->ReleaseByteArrayElements(env,buf,byteP,0);
#endif
	}

	return (jint)status;

}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Aget_space
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Aget_1space
  (JNIEnv *env, jclass clss, jint attr_id)
{
	hid_t retVal = -1;
	retVal =  H5Aget_space((hid_t)attr_id); 
	if (retVal < 0) {
		/* throw exception */
		h5libraryError(env);
	}
	return (jint)retVal;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Aget_type
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Aget_1type
  (JNIEnv *env, jclass clss, jint attr_id)
{
	hid_t retVal = -1;
	retVal =  H5Aget_type((hid_t)attr_id);
	if (retVal < 0) {
		h5libraryError(env);
	}
	return (jint)retVal;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Aget_name
 * Signature: (IJLjava/lang/String;)J
 */
JNIEXPORT jlong JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Aget_1name
  (JNIEnv *env, jclass clss, jint attr_id, jlong buf_size, jobjectArray name)
{
	char *aName;
	jstring str;
	hssize_t size;
	long bs;

	bs = (long)buf_size;
	if (bs <= 0) {
		h5badArgument( env, "H5Aget_name:  buf_size <= 0");
		return -1;
	}
	aName = (char*)malloc(sizeof(char)*bs);
	if (aName == NULL) {
		h5outOfMemory( env, "H5Aget_name:  malloc failed");
		return -1;
	}
	size = H5Aget_name((hid_t)attr_id, (size_t)buf_size, aName);
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
		h5JNIFatalError( env,"H5Aget_name:  return string failed");
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

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Aget_num_attrs
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Aget_1num_1attrs
  (JNIEnv *env, jclass clss, jint loc_id)
{
	int retVal = -1;
	retVal =  H5Aget_num_attrs((hid_t)loc_id);
	if (retVal < 0) {
		h5libraryError(env);
	}
	return (jint)retVal;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Adelete
 * Signature: (ILjava/lang/String;)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Adelete
  (JNIEnv *env, jclass clss, jint loc_id, jstring name)
{
	herr_t status;
	char* aName;
	jboolean isCopy;

	if (name == NULL) {
		h5nullArgument( env,"H5Adelete:  name is NULL");
		return -1;
	} 
#ifdef __cplusplus
	aName = (char *)env->GetStringUTFChars(name,&isCopy);
#else
	aName = (char *)(*env)->GetStringUTFChars(env,name,&isCopy);
#endif
	if (aName == NULL) {
		h5JNIFatalError( env,"H5Adelete: name is not pinned");
		return -1;
	}

	status = H5Adelete((hid_t)loc_id, aName );

#ifdef __cplusplus
	env->ReleaseStringUTFChars(name,aName);
#else
	(*env)->ReleaseStringUTFChars(env,name,aName);
#endif

	if (status < 0) {
		h5libraryError(env);
	}
	return (jint)status;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Aclose
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Aclose
  (JNIEnv *env, jclass clss, jint attr_id)
{
	herr_t retVal = -1;
	retVal =  H5Aclose((hid_t)attr_id);
	if (retVal < 0) {
		/* throw exception */
		h5libraryError(env);
	}
	return (jint)retVal;
}
#ifdef __cplusplus
}
#endif
