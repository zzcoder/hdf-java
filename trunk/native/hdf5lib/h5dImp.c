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
 *  Dataset Object API Functions of HDF5 library. 
 *
 *  Each routine wraps a single HDF entry point, generally with the
 *  analogous arguments and return codes.
 *
 *  For details of the HDF libraries, see the HDF Documentation at:
 *   http://hdf.ncsa.uiuc.edu/&isCopy/doc/
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
extern jboolean h5libraryError( JNIEnv *env );

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Dcreate
 * Signature: (ILjava/lang/String;III)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Dcreate
  (JNIEnv *env, jclass clss, jint loc_id, jstring name, jint type_id, 
  jint space_id, jint create_plist_id)
{
	herr_t status;
	char* file;
	jboolean isCopy;

	if (name == NULL) {
		h5nullArgument( env, "H5Dcreate:  name is NULL");
		return -1;
	}
#ifdef __cplusplus
	file = (char *)env->GetStringUTFChars(name,&isCopy);
#else
	file = (char *)(*env)->GetStringUTFChars(env,name,&isCopy);
#endif
	if (file == NULL) {
		h5JNIFatalError( env, "H5Dcreate:  file name not pinned");
		return -1;
	}

	status = H5Dcreate(loc_id, file, type_id, space_id, create_plist_id);

#ifdef __cplusplus
	env->ReleaseStringUTFChars(name,file);
#else
	(*env)->ReleaseStringUTFChars(env,name,file);
#endif
	if (status < 0) {
		h5libraryError(env);
	}
	return (jint)status;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Dopen
 * Signature: (ILjava/lang/String;)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Dopen
  (JNIEnv *env, jclass clss, jint loc_id, jstring name)
{
	hid_t status;
	char* file;
	jboolean isCopy;

	if (name == NULL) {
		h5nullArgument( env, "H5Dopen:  name is NULL");
		return -1;
	}
#ifdef __cplusplus
	file = (char *)env->GetStringUTFChars(name,&isCopy);
#else
	file = (char *)(*env)->GetStringUTFChars(env,name,&isCopy);
#endif
	if (file == NULL) {
		h5JNIFatalError( env, "H5Dopen:  file name not pinned");
		return -1;
	}
	status = H5Dopen((hid_t)loc_id, file ); 

#ifdef __cplusplus
	env->ReleaseStringUTFChars(name,file);
#else
	(*env)->ReleaseStringUTFChars(env,name,file);
#endif
	if (status < 0) {
		h5libraryError(env);
	}
	return (jint)status;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Dget_space
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Dget_1space
  (JNIEnv *env, jclass clss, jint dataset_id)
{
	hid_t retVal = -1;
	retVal =  H5Dget_space((hid_t)dataset_id ); 
	if (retVal < 0) {
		h5libraryError(env);
	}
	return (jint)retVal;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Dget_type
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Dget_1type
  (JNIEnv *env, jclass clss, jint dataset_id)
{
	hid_t retVal = -1;
	retVal = H5Dget_type((hid_t)dataset_id ); 
	if (retVal < 0) {
		h5libraryError(env);
	}
	return (jint)retVal;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Dget_create_plist
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Dget_1create_1plist
  (JNIEnv *env, jclass clss, jint dataset_id)
{
	hid_t retVal = -1;
	retVal =  H5Dget_create_plist((hid_t)dataset_id ); 
	if (retVal < 0) {
		h5libraryError(env);
	}
	return (jint)retVal;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Dread
 * Signature: (IIIII[B)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Dread
  (JNIEnv *env, jclass clss, jint dataset_id, jint mem_type_id, jint mem_space_id, 
  jint file_space_id, jint xfer_plist_id, jbyteArray buf)
{
	herr_t status;
	jbyte *byteP;
	jboolean isCopy;

	if ( buf == NULL ) {
		h5nullArgument( env, "H5Dread:  buf is NULL");
		return -1;
	}
#ifdef __cplusplus
	byteP = env->GetByteArrayElements(buf,&isCopy);
#else
	byteP = (*env)->GetByteArrayElements(env,buf,&isCopy);
#endif
	if (byteP == NULL) {
		h5JNIFatalError( env, "H5Dread:  buf not pinned");
		return -1;
	}

	status = H5Dread((hid_t)dataset_id, (hid_t)mem_type_id, (hid_t)mem_space_id,
		(hid_t)file_space_id, (hid_t)xfer_plist_id, byteP);
	
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
 * Method:    H5Dwrite
 * Signature: (IIIII[B)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Dwrite
  (JNIEnv *env, jclass clss, jint dataset_id, jint mem_type_id, jint mem_space_id, 
  jint file_space_id, jint xfer_plist_id, jbyteArray buf)
{
	herr_t status;
	jbyte *byteP;
	jboolean isCopy;

	if ( buf == NULL ) {
		h5nullArgument( env, "H5Dwrite:  buf is NULL");
		return -1;
	}
	 
#ifdef __cplusplus
	byteP = env->GetByteArrayElements(buf,&isCopy);
#else
	byteP = (*env)->GetByteArrayElements(env,buf,&isCopy);
#endif
	if (byteP == NULL) {
		h5JNIFatalError( env, "H5Dwrite:  buf not pinned");
		return -1;
	}

	status = H5Dwrite((hid_t)dataset_id, (hid_t)mem_type_id, (hid_t)mem_space_id, 
		(hid_t)file_space_id, (hid_t)xfer_plist_id, byteP);

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
 * Method:    H5Dextend
 * Signature: (IB)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Dextend
  (JNIEnv *env, jclass clss, jint dataset_id, jbyteArray size)
{
	herr_t status;
	jbyte *P;
	jboolean isCopy;
	hsize_t *sa;
	int i;
	int rank;
	hsize_t *lp;
	jlong *jlp;

	if ( size == NULL ) {
		h5nullArgument( env, "H5Dextend:  array of sizes is NULL");
		return -1;
	}
	/* 
	 *  Future:  check that the array has correct
	 *           rank (same as dataset dataset_id)
	 */
#ifdef __cplusplus
	P = env->GetByteArrayElements(size,&isCopy);
#else
	P = (*env)->GetByteArrayElements(env,size,&isCopy);
#endif
	if (P == NULL) {
		h5JNIFatalError( env, "H5Dextend:  array not pinned");
		return -1;
	}
#ifdef __cplusplus
	i = env->GetArrayLength(size);
#else
	i = (*env)->GetArrayLength(env,size);
#endif
	rank = i / sizeof(jlong);
	sa = lp = (hsize_t *)malloc(rank * sizeof(hsize_t));
	if (sa == NULL)  {
#ifdef __cplusplus
		env->ReleaseByteArrayElements(size,P,JNI_ABORT);
#else
		(*env)->ReleaseByteArrayElements(env,size,P,JNI_ABORT);
#endif
		h5JNIFatalError(env,  "H5Dextend:  size not converted to hsize_t");
		return -1;
	}
	jlp = (jlong *)P;
	for (i = 0; i < rank; i++) {
		*lp = (hsize_t)*jlp;
		lp++;
		jlp++;
	}

	status = H5Dextend((hid_t)dataset_id, (hsize_t *)sa);

#ifdef __cplusplus
	env->ReleaseByteArrayElements(size,P,JNI_ABORT);
#else
	(*env)->ReleaseByteArrayElements(env,size,P,JNI_ABORT);
#endif
	free(sa);

	if (status < 0) {
		h5libraryError(env);
	}
	return (jint)status;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Dclose
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Dclose
  (JNIEnv *env, jclass clss, jint dataset_id)
{
	hid_t retVal = -1;
	retVal =  H5Dclose((hid_t)dataset_id );
	if (retVal < 0) {
		h5libraryError(env);
	}
	return (jint)retVal;
}

/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Dget_storage_size
 * Signature: (I)J
 */
JNIEXPORT jlong JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Dget_1storage_1size
  (JNIEnv *env, jclass clss, jint dataset_id)
{
	hsize_t retVal = (hsize_t)-1;
	retVal =  H5Dget_storage_size((hid_t)dataset_id );
/* probably returns '0' if fails--don't do an exception
	if (retVal < 0) {
		h5libraryError(env);
	}
*/
	return (jlong)retVal;
}

/*
 * Copies the content of one dataset to another dataset
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Dcopy
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Dcopy
  (JNIEnv *env, jclass clss, jint src_id, jint dst_id)
{
	jbyte *buf;
	herr_t retVal = -1;
	hid_t src_did = (hid_t)src_id;
	hid_t dst_did = (hid_t)dst_id;
	hid_t tid=-1;
	hid_t sid=-1;
	hsize_t total_size = 0;


	sid = H5Dget_space(src_did);
	if (sid < 0) {
		h5libraryError(env);
		return -1;
	}

	tid = H5Dget_type(src_did);
	if (tid < 0) {
		H5Sclose(sid);
		h5libraryError(env);
		return -1;
	}

	total_size = H5Sget_simple_extent_npoints(sid) *
			H5Tget_size(tid);

	H5Sclose(sid);

	buf = (jbyte *)malloc( (int) (total_size * sizeof(jbyte)));
        if (buf == NULL) {
		H5Tclose(tid);
                h5outOfMemory( env, "H5Dcopy:  malloc failed");
                return -1;
        }

	retVal = H5Dread(src_did, tid, H5S_ALL, H5S_ALL, H5P_DEFAULT, buf);
	H5Tclose(tid);

	if (retVal < 0) {
		free(buf);
		h5libraryError(env);
		return (jint)retVal;
	}

	tid = H5Dget_type(dst_did);
	if (tid < 0) {
		free(buf);
		h5libraryError(env);
		return -1;
	}
	retVal = H5Dwrite(dst_did, tid, H5S_ALL, H5S_ALL, H5P_DEFAULT, buf);
	H5Tclose(tid);
	free(buf);

	if (retVal < 0) {
		h5libraryError(env);
	}

	return (jint)retVal;
}


/*
 * Copies the content of one dataset to another dataset
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Dvlen_get_buf_size
 * Signature: (III[I)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Dvlen_1get_1buf_1size
  (JNIEnv *env, jclass clss, 
jint dataset_id, jint type_id, jint space_id, jintArray size ) 
{
	herr_t status;
	jint *P;
	jboolean isCopy;
	hsize_t sz;

	if ( size == NULL ) {
		h5nullArgument( env, "H5Dvlen_get_buf_size:  size is NULL");
		return -1;
	}

#ifdef __cplusplus
	P = env->GetIntArrayElements(size,&isCopy);
#else
	P = (*env)->GetIntArrayElements(env,size,&isCopy);
#endif
	if (P == NULL) {
		h5JNIFatalError( env, "H5Dvlen_get_buf_size:  array not pinned");
		return -1;
	}

	status = (jint)H5Dvlen_get_buf_size((hid_t) dataset_id, 
		(hid_t) type_id, (hid_t) space_id, (hsize_t *)&sz);



	if (status < 0) {
#ifdef __cplusplus
		env->ReleaseIntArrayElements(size,P,JNI_ABORT);
#else
		(*env)->ReleaseIntArrayElements(env,size,P,JNI_ABORT);
#endif
		h5libraryError(env);
	} else {
		P[0] = (jint)sz;
#ifdef __cplusplus
		env->ReleaseIntArrayElements(size,P,0);
#else
		(*env)->ReleaseIntArrayElements(env,size,P,0);
#endif
	}
	return (jint)status;
}


/*
 * Class:     ncsa_hdf_hdf5lib_H5
 * Method:    H5Dvlen_reclaim
 * Signature: (IIIII[B)I
 */
JNIEXPORT jint JNICALL Java_ncsa_hdf_hdf5lib_H5_H5Dvlen_1reclaim
  (JNIEnv *env, jclass clss, jint type_id, jint space_id, 
   jint xfer_plist_id, jbyteArray buf)
{
	herr_t status;
	jbyte *byteP;
	jboolean isCopy;

	if ( buf == NULL ) {
		h5nullArgument( env, "H5Dwrite:  buf is NULL");
		return -1;
	}
#ifdef __cplusplus
	byteP = env->GetByteArrayElements(buf,&isCopy);
#else
	byteP = (*env)->GetByteArrayElements(env,buf,&isCopy);
#endif
	if (byteP == NULL) {
		h5JNIFatalError( env, "H5Dwrite:  buf not pinned");
		return -1;
	}

	status = H5Dvlen_reclaim((hid_t)type_id, 
		(hid_t)space_id, (hid_t)xfer_plist_id, byteP);

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
#ifdef __cplusplus
}
#endif 
